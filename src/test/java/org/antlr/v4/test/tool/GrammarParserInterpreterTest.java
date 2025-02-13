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
import org.antlr.v4.runtime.InterpreterRuleContext;
import org.antlr.v4.runtime.LexerInterpreter;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.tool.Grammar;
import org.antlr.v4.tool.GrammarParserInterpreter;
import org.antlr.v4.tool.LexerGrammar;
import org.junit.jupiter.api.Test;

import static org.antlr.v4.TestUtils.assertEquals;

/**
 * Tests to ensure GrammarParserInterpreter subclass of ParserInterpreter
 * hasn't messed anything up.
 */
public class GrammarParserInterpreterTest {
  public static final String lexerText = """
    lexer grammar L;
    PLUS : '+' ;
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
        s : ID
          | INT{;}
          ;
        """,
      lg);
    testInterp(lg, g, "s", "a", "(s:1 a)");
    testInterp(lg, g, "s", "3", "(s:2 3)");
  }

  @Test
  public void testAltsAsSet() throws Exception {
    LexerGrammar lg = new LexerGrammar(lexerText);
    Grammar g = new Grammar(
      """
        parser grammar T;
        s : ID
          | INT
          ;
        """,
      lg);
    testInterp(lg, g, "s", "a", "(s:1 a)");
    testInterp(lg, g, "s", "3", "(s:1 3)");
  }

  @Test
  public void testAltsWithLabels() throws Exception {
    LexerGrammar lg = new LexerGrammar(lexerText);
    Grammar g = new Grammar(
      """
        parser grammar T;
        s : ID  # foo
          | INT # bar
          ;
        """,
      lg);
    // it won't show the labels here because my simple node text provider above just shows the alternative
    testInterp(lg, g, "s", "a", "(s:1 a)");
    testInterp(lg, g, "s", "3", "(s:2 3)");
  }

  @Test
  public void testOneAlt() throws Exception {
    LexerGrammar lg = new LexerGrammar(lexerText);
    Grammar g = new Grammar(
      """
        parser grammar T;
        s : ID
          ;
        """,
      lg);
    testInterp(lg, g, "s", "a", "(s:1 a)");
  }


  @Test
  public void testLeftRecursionWithMultiplePrimaryAndRecursiveOps() throws Exception {
    LexerGrammar lg = new LexerGrammar(lexerText);
    Grammar g = new Grammar(
      """
        parser grammar T;
        s : e EOF ;
        e : e MULT e
          | e PLUS e
          | INT
          | ID
          ;
        """,
      lg);

    testInterp(lg, g, "s", "a", "(s:1 (e:4 a) <EOF>)");
    testInterp(lg, g, "e", "a", "(e:4 a)");
    testInterp(lg, g, "e", "34", "(e:3 34)");
    testInterp(lg, g, "e", "a+1", "(e:2 (e:4 a) + (e:3 1))");
    testInterp(lg, g, "e", "1+2*a", "(e:2 (e:3 1) + (e:1 (e:3 2) * (e:4 a)))");
  }

  InterpreterRuleContext testInterp(LexerGrammar lg, Grammar g,
                                    String startRule, String input,
                                    String expectedParseTree) {
    LexerInterpreter lexEngine = lg.createLexerInterpreter(CharStreams.fromString(input));
    CommonTokenStream tokens = new CommonTokenStream(lexEngine);
    GrammarParserInterpreter parser = g.createGrammarParserInterpreter(tokens);
    ParseTree t = parser.parse(g.rules.get(startRule).index);
    InterpreterTreeTextProvider nodeTextProvider = new InterpreterTreeTextProvider(g.getRuleNames());
    String treeStr = Trees.toStringTree(t, nodeTextProvider);
    System.out.println("parse tree: " + treeStr);
    assertEquals(expectedParseTree, treeStr);
    return (InterpreterRuleContext) t;
  }
}
