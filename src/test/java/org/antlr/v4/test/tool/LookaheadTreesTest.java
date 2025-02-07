/*
 * This file is a part of ANTLR.
 *
 * Copyright (c) 2012-2025 The ANTLR Project. All rights reserved.
 * Copyright (c) 2025 Valery Maximov <maximovvalery@gmail.com> and contributors
 *
 * Use of this file is governed by the BSD-3-Clause license that
 * can be found in the LICENSE.txt file in the project root.
 */
package org.antlr.v4.test.tool;

import org.antlr.v4.gui.Trees;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.LexerInterpreter;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.atn.DecisionInfo;
import org.antlr.v4.runtime.atn.LookaheadEventInfo;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.tool.Grammar;
import org.antlr.v4.tool.GrammarParserInterpreter;
import org.antlr.v4.tool.LexerGrammar;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.antlr.v4.TestUtils.assertEquals;

public class LookaheadTreesTest {
  public static final String lexerText =
    """
      lexer grammar L;
      DOT  : '.' ;
      SEMI : ';' ;
      BANG : '!' ;
      PLUS : '+' ;
      LPAREN : '(' ;
      RPAREN : ')' ;
      MULT : '*' ;
      ID : [a-z]+ ;
      INT : [0-9]+ ;
      WS : [ \\r\\t\\n]+ ;
      """;

  @Test
  public void testAlts() throws Exception {
    LexerGrammar lg = new LexerGrammar(lexerText);
    Grammar g = new Grammar(
      """
        parser grammar T;
        s : e SEMI EOF ;
        e : ID DOT ID
          | ID LPAREN RPAREN
          ;
        """,
      lg);

    String startRuleName = "s";
    int decision = 0;

    testLookaheadTrees(lg, g, "a.b;", startRuleName, decision,
      new String[]{"(e:1 a . b)", "(e:2 a <error .>)"});
  }

  @Test
  public void testIncludeEOF() throws Exception {
    LexerGrammar lg = new LexerGrammar(lexerText);
    Grammar g = new Grammar(
      """
        parser grammar T;
        s : e ;
        e : ID DOT ID EOF
          | ID DOT ID EOF
          ;
        """,
      lg);

    int decision = 0;
    testLookaheadTrees(lg, g, "a.b", "s", decision,
      new String[]{"(e:1 a . b <EOF>)", "(e:2 a . b <EOF>)"});
  }

  @Test
  public void testCallLeftRecursiveRule() throws Exception {
    LexerGrammar lg = new LexerGrammar(lexerText);
    Grammar g = new Grammar(
      """
        parser grammar T;
        s : a BANG EOF;
        a : e SEMI\s
          | ID SEMI\s
          ;\
        e : e MULT e
          | e PLUS e
          | e DOT e
          | ID
          | INT
          ;
        """,
      lg);

    int decision = 0;
    testLookaheadTrees(lg, g, "x;!", "s", decision,
      new String[]{"(a:1 (e:4 x) ;)",
        "(a:2 x ;)"}); // shouldn't include BANG, EOF
    decision = 2; // (...)* in e
    testLookaheadTrees(lg, g, "x+1;!", "s", decision,
      new String[]{"(e:1 (e:4 x) <error +>)",
        "(e:2 (e:4 x) + (e:5 1))",
        "(e:3 (e:4 x) <error +>)"});
  }

  public void testLookaheadTrees(LexerGrammar lg, Grammar g,
                                 String input,
                                 String startRuleName,
                                 int decision,
                                 String[] expectedTrees) {
    int startRuleIndex = g.getRule(startRuleName).index;
    InterpreterTreeTextProvider nodeTextProvider =
      new InterpreterTreeTextProvider(g.getRuleNames());

    LexerInterpreter lexEngine = lg.createLexerInterpreter(CharStreams.fromString(input));
    CommonTokenStream tokens = new CommonTokenStream(lexEngine);
    GrammarParserInterpreter parser = g.createGrammarParserInterpreter(tokens);
    parser.setProfile(true);
    ParseTree t = parser.parse(startRuleIndex);

    DecisionInfo decisionInfo = parser.getParseInfo().getDecisionInfo()[decision];
    LookaheadEventInfo lookaheadEventInfo = decisionInfo.SLL_MaxLookEvent;

    List<ParserRuleContext> lookaheadParseTrees =
      GrammarParserInterpreter.getLookaheadParseTrees(g, parser, tokens, startRuleIndex, lookaheadEventInfo.decision,
        lookaheadEventInfo.startIndex, lookaheadEventInfo.stopIndex);

    assertEquals(expectedTrees.length, lookaheadParseTrees.size());
    for (int i = 0; i < lookaheadParseTrees.size(); i++) {
      ParserRuleContext lt = lookaheadParseTrees.get(i);
      assertEquals(expectedTrees[i], Trees.toStringTree(lt, nodeTextProvider));
    }
  }
}
