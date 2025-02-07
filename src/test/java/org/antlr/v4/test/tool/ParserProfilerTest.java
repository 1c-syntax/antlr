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

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.LexerInterpreter;
import org.antlr.v4.runtime.ParserInterpreter;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.atn.DecisionInfo;
import org.antlr.v4.tool.Grammar;
import org.antlr.v4.tool.LexerGrammar;
import org.antlr.v4.tool.Rule;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.antlr.v4.TestUtils.assertEquals;

public class ParserProfilerTest extends AbstractBaseTest {
  LexerGrammar lg;

  @Override
  public void setUp() throws Exception {
    super.setUp();
    lg = new LexerGrammar(
      """
        lexer grammar L;
        WS : [ \\r\\t\\n]+ -> channel(HIDDEN) ;
        SEMI : ';' ;
        DOT : '.' ;
        ID : [a-zA-Z]+ ;
        INT : [0-9]+ ;
        PLUS : '+' ;
        MULT : '*' ;
        """);
  }

  @Test
  @Disabled
  public void testLL1() throws Exception {
    Grammar g = new Grammar(
      """
        parser grammar T;
        s : ';'{}
          | '.'
          ;
        """,
      lg);

    DecisionInfo[] info = interpAndGetDecisionInfo(lg, g, "s", ";");
    assertEquals(1, info.length);
    String expecting =
      "{decision=0, contextSensitivities=0, errors=0, ambiguities=0, SLL_lookahead=1, " +
        "SLL_ATNTransitions=1, SLL_DFATransitions=0, LL_Fallback=0, LL_lookahead=0, LL_ATNTransitions=0}";
    assertEquals(expecting, info[0].toString());
  }

  @Test
  @Disabled
  public void testLL2() throws Exception {
    Grammar g = new Grammar(
      """
        parser grammar T;
        s : ID ';'{}
          | ID '.'
          ;
        """,
      lg);

    DecisionInfo[] info = interpAndGetDecisionInfo(lg, g, "s", "xyz;");
    assertEquals(1, info.length);
    String expecting =
      "{decision=0, contextSensitivities=0, errors=0, ambiguities=0, SLL_lookahead=2, " +
        "SLL_ATNTransitions=2, SLL_DFATransitions=0, LL_Fallback=0, LL_lookahead=0, LL_ATNTransitions=0}";
    assertEquals(expecting, info[0].toString());
  }

  @Test
  @Disabled
  public void testRepeatedLL2() throws Exception {
    Grammar g = new Grammar(
      """
        parser grammar T;
        s : ID ';'{}
          | ID '.'
          ;
        """,
      lg);

    DecisionInfo[] info = interpAndGetDecisionInfo(lg, g, "s", "xyz;", "abc;");
    assertEquals(1, info.length);
    String expecting =
      "{decision=0, contextSensitivities=0, errors=0, ambiguities=0, SLL_lookahead=4, " +
        "SLL_ATNTransitions=2, SLL_DFATransitions=2, LL_Fallback=0, LL_lookahead=0, LL_ATNTransitions=0}";
    assertEquals(expecting, info[0].toString());
  }

  @Test
  @Disabled
  public void test3xLL2() throws Exception {
    Grammar g = new Grammar(
      """
        parser grammar T;
        s : ID ';'{}
          | ID '.'
          ;
        """,
      lg);

    // The '.' vs ';' causes another ATN transition
    DecisionInfo[] info = interpAndGetDecisionInfo(lg, g, "s", "xyz;", "abc;", "z.");
    assertEquals(1, info.length);
    String expecting =
      "{decision=0, contextSensitivities=0, errors=0, ambiguities=0, SLL_lookahead=6, " +
        "SLL_ATNTransitions=3, SLL_DFATransitions=3, LL_Fallback=0, LL_lookahead=0, LL_ATNTransitions=0}";
    assertEquals(expecting, info[0].toString());
  }

  @Test
  @Disabled
  public void testOptional() throws Exception {
    Grammar g = new Grammar(
      """
        parser grammar T;
        s : ID ('.' ID)? ';'
          | ID INT\s
          ;
        """,
      lg);

    DecisionInfo[] info = interpAndGetDecisionInfo(lg, g, "s", "a.b;");
    assertEquals(2, info.length);
    String expecting =
      "[{decision=0, contextSensitivities=0, errors=0, ambiguities=0, SLL_lookahead=1, " +
        "SLL_ATNTransitions=1, SLL_DFATransitions=0, LL_Fallback=0, LL_lookahead=0, LL_ATNTransitions=0}, " +
        "{decision=1, contextSensitivities=0, errors=0, ambiguities=0, SLL_lookahead=2, " +
        "SLL_ATNTransitions=2, SLL_DFATransitions=0, LL_Fallback=0, LL_lookahead=0, LL_ATNTransitions=0}]";
    assertEquals(expecting, Arrays.toString(info));
  }

  @Test
  @Disabled
  public void test2xOptional() throws Exception {
    Grammar g = new Grammar(
      """
        parser grammar T;
        s : ID ('.' ID)? ';'
          | ID INT\s
          ;
        """,
      lg);

    DecisionInfo[] info = interpAndGetDecisionInfo(lg, g, "s", "a.b;", "a.b;");
    assertEquals(2, info.length);
    String expecting =
      "[{decision=0, contextSensitivities=0, errors=0, ambiguities=0, SLL_lookahead=2, " +
        "SLL_ATNTransitions=1, SLL_DFATransitions=1, LL_Fallback=0, LL_lookahead=0, LL_ATNTransitions=0}, " +
        "{decision=1, contextSensitivities=0, errors=0, ambiguities=0, SLL_lookahead=4, " +
        "SLL_ATNTransitions=2, SLL_DFATransitions=2, LL_Fallback=0, LL_lookahead=0, LL_ATNTransitions=0}]";
    assertEquals(expecting, Arrays.toString(info));
  }

  @Test
  @Disabled
  public void testContextSensitivity() throws Exception {
    Grammar g = new Grammar(
      """
        parser grammar T;
        a : '.' e ID\s
          | ';' e INT ID ;
        e : INT | ;
        """,
      lg);
    DecisionInfo[] info = interpAndGetDecisionInfo(lg, g, "a", "; 1 x");
    assertEquals(2, info.length);
    String expecting =
      "{decision=1, contextSensitivities=1, errors=0, ambiguities=0, SLL_lookahead=3, SLL_ATNTransitions=2, SLL_DFATransitions=0, LL_Fallback=1, LL_lookahead=3, LL_ATNTransitions=2}";
    assertEquals(expecting, info[1].toString());
  }

  @Test
  @Disabled
  public void testDeepLookahead() throws Exception {
    // d=1 entry, d=2 bypass
    Grammar g = new Grammar(
      """
        parser grammar T;
        s : e ';'
          | e '.'\s
          ;
        e : (ID|INT) ({true}? '+' e)*
          ;
        """,
      lg);

    // pred forces to
    // ambig and ('+' e)* tail recursion forces lookahead to fall out of e
    // any non-precedence predicates are always evaluated as true by the interpreter
    DecisionInfo[] info = interpAndGetDecisionInfo(lg, g, "s", "a+b+c;");
    // at "+b" it uses k=1 and enters loop then calls e for b...
    // e matches and d=2 uses "+c;" for k=3
    assertEquals(2, info.length);
    String expecting =
      "[{decision=0, contextSensitivities=0, errors=0, ambiguities=0, SLL_lookahead=6, " +
        "SLL_ATNTransitions=6, SLL_DFATransitions=0, LL_Fallback=0, LL_lookahead=0, LL_ATNTransitions=0}, " +
        "{decision=1, contextSensitivities=0, errors=0, ambiguities=1, SLL_lookahead=5, " +
        "SLL_ATNTransitions=2, SLL_DFATransitions=3, LL_Fallback=2, LL_lookahead=3, LL_ATNTransitions=3}]";
    assertEquals(expecting, Arrays.toString(info));
  }

  @Test
  @Disabled
  public void testProfilerGeneratedCode() {
    String grammar =
      """
        grammar T;
        s : a+ ID EOF ;
        a : ID ';'{}
          | ID '.'
          ;
        WS : [ \\r\\t\\n]+ -> channel(HIDDEN) ;
        SEMI : ';' ;
        DOT : '.' ;
        ID : [a-zA-Z]+ ;
        INT : [0-9]+ ;
        PLUS : '+' ;
        MULT : '*' ;
        """;

    String found = execParser("T.g4", grammar, "TParser", "TLexer", "s",
      "xyz;abc;z.q", false, true);
    String expecting =
      "[{decision=0, contextSensitivities=0, errors=0, ambiguities=0, SLL_lookahead=6, SLL_ATNTransitions=4, " +
        "SLL_DFATransitions=2, LL_Fallback=0, LL_lookahead=0, LL_ATNTransitions=0}," +
        " {decision=1, contextSensitivities=0, errors=0, ambiguities=0, SLL_lookahead=6, " +
        "SLL_ATNTransitions=3, SLL_DFATransitions=3, LL_Fallback=0, LL_lookahead=0, LL_ATNTransitions=0}]\n";
    assertEquals(expecting, found);
    assertEquals(null, stderrDuringParse);
  }

  public DecisionInfo[] interpAndGetDecisionInfo(
    LexerGrammar lg, Grammar g,
    String startRule, String... input) {

    LexerInterpreter lexEngine = lg.createLexerInterpreter(null);
    ParserInterpreter parser = g.createParserInterpreter(null);
    parser.setProfile(true);
    for (String s : input) {
      lexEngine.reset();
      parser.reset();
      lexEngine.setInputStream(CharStreams.fromString(s));
      CommonTokenStream tokens = new CommonTokenStream(lexEngine);
      parser.setInputStream(tokens);
      Rule r = g.rules.get(startRule);
      if (r == null) {
        return parser.getParseInfo().getDecisionInfo();
      }
      ParserRuleContext t = parser.parse(r.index);
    }
    return parser.getParseInfo().getDecisionInfo();
  }
}
