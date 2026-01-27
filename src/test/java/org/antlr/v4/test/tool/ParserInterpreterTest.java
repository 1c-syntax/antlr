/*
 * This file is a part of ANTLR.
 *
 * Copyright (c) 2012-2025 The ANTLR Project. All rights reserved.
 * Copyright (c) 2025-2026 Valery Maximov <maximovvalery@gmail.com> and contributors
 *
 * Use of this file is governed by the BSD-3-Clause license that
 * can be found in the LICENSE.txt file in the project root.
 */
package org.antlr.v4.test.tool;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.LexerInterpreter;
import org.antlr.v4.runtime.ParserInterpreter;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.tool.Grammar;
import org.antlr.v4.tool.LexerGrammar;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ParserInterpreterTest extends AbstractBaseTest {

  @Test
  void testEmptyStartRule() throws Exception {
    LexerGrammar lg = new LexerGrammar(
      """
        lexer grammar L;
        A : 'a' ;
        """);
    Grammar g = new Grammar(
      "parser grammar T;\ns :  ;",
      lg);

    testInterp(lg, g, "s", "", "s");
    testInterp(lg, g, "s", "a", "s");
  }

  @Test
  void testA() throws Exception {
    LexerGrammar lg = new LexerGrammar(
      """
        lexer grammar L;
        A : 'a' ;
        """);
    Grammar g = new Grammar(
      """
        parser grammar T;
        s : A ;""",
      lg);

    ParseTree t = testInterp(lg, g, "s", "a", "(s a)");
    assertThat(t.getSourceInterval()).hasToString("0..0");
  }

  @Test
  void testEOF() throws Exception {
    LexerGrammar lg = new LexerGrammar(
      """
        lexer grammar L;
        A : 'a' ;
        """);
    Grammar g = new Grammar(
      """
        parser grammar T;
        s : A EOF ;""",
      lg);

    ParseTree t = testInterp(lg, g, "s", "a", "(s a <EOF>)");
    assertThat(t.getSourceInterval()).hasToString("0..1");
  }

  @Test
  void testEOFInChild() throws Exception {
    LexerGrammar lg = new LexerGrammar(
      """
        lexer grammar L;
        A : 'a' ;
        """);
    Grammar g = new Grammar(
      """
        parser grammar T;
        s : x ;
        x : A EOF ;""",
      lg);

    ParseTree t = testInterp(lg, g, "s", "a", "(s (x a <EOF>))");
    assertThat(t.getSourceInterval()).hasToString("0..1");
    assertThat(t.getChild(0).getSourceInterval()).hasToString("0..1");
  }

  @Test
  void testEmptyRuleAfterEOFInChild() throws Exception {
    LexerGrammar lg = new LexerGrammar(
      """
        lexer grammar L;
        A : 'a' ;
        """);
    Grammar g = new Grammar(
      """
        parser grammar T;
        s : x y;
        x : A EOF ;
        y : ;""",
      lg);

    ParseTree t = testInterp(lg, g, "s", "a", "(s (x a <EOF>) y)");
    assertThat(t.getSourceInterval()).hasToString("0..1"); // s
    assertThat(t.getChild(0).getSourceInterval()).hasToString("0..1"); // x
  }

  @Test
  void testEmptyRuleAfterJustEOFInChild() throws Exception {
    LexerGrammar lg = new LexerGrammar(
      """
        lexer grammar L;
        A : 'a' ;
        """);
    Grammar g = new Grammar(
      """
        parser grammar T;
        s : x y;
        x : EOF ;
        y : ;""",
      lg);

    ParseTree t = testInterp(lg, g, "s", "", "(s (x <EOF>) y)");
    assertThat(t.getSourceInterval()).hasToString("0..0"); // s
    assertThat(t.getChild(0).getSourceInterval()).hasToString("0..0"); // x
    // this next one is a weird special case where somebody tries to match beyond in the file
  }

  @Test
  void testEmptyInput() throws Exception {
    LexerGrammar lg = new LexerGrammar(
      """
        lexer grammar L;
        A : 'a' ;
        """);
    Grammar g = new Grammar(
      """
        parser grammar T;
        s : x EOF ;
        x : ;
        """,
      lg);

    ParseTree t = testInterp(lg, g, "s", "", "(s x <EOF>)");
    assertThat(t.getSourceInterval()).hasToString("0..0"); // s
    assertThat(t.getChild(0).getSourceInterval()).hasToString("0..-1"); // x
  }

  @Test
  void testEmptyInputWithCallsAfter() throws Exception {
    LexerGrammar lg = new LexerGrammar(
      """
        lexer grammar L;
        A : 'a' ;
        """);
    Grammar g = new Grammar(
      """
        parser grammar T;
        s : x y ;
        x : EOF ;
        y : z ;
        z : ;""",
      lg);

    ParseTree t = testInterp(lg, g, "s", "", "(s (x <EOF>) (y z))");
    assertThat(t.getSourceInterval()).hasToString("0..0"); // s
    assertThat(t.getChild(0).getSourceInterval()).hasToString("0..0"); // x
  }

  @Test
  void testEmptyFirstRule() throws Exception {
    LexerGrammar lg = new LexerGrammar(
      """
        lexer grammar L;
        A : 'a' ;
        """);
    Grammar g = new Grammar(
      """
        parser grammar T;
        s : x A ;
        x : ;
        """,
      lg);

    ParseTree t = testInterp(lg, g, "s", "a", "(s x a)");
    assertThat(t.getSourceInterval()).hasToString("0..0"); // s
    // This gets an empty interval because the stop token is null for x
    assertThat(t.getChild(0).getSourceInterval()).hasToString("0..-1"); // x
  }

  @Test
  void testAorB() throws Exception {
    LexerGrammar lg = new LexerGrammar(
      """
        lexer grammar L;
        A : 'a' ;
        B : 'b' ;
        C : 'c' ;
        """);
    Grammar g = new Grammar(
      """
        parser grammar T;
        s : A{;} | B ;""",
      lg);
    testInterp(lg, g, "s", "a", "(s a)");
    testInterp(lg, g, "s", "b", "(s b)");
  }

  @Test
  void testCall() throws Exception {
    LexerGrammar lg = new LexerGrammar(
      """
        lexer grammar L;
        A : 'a' ;
        B : 'b' ;
        C : 'c' ;
        """);
    Grammar g = new Grammar(
      """
        parser grammar T;
        s : t C ;
        t : A{;} | B ;
        """,
      lg);

    testInterp(lg, g, "s", "ac", "(s (t a) c)");
    testInterp(lg, g, "s", "bc", "(s (t b) c)");
  }

  @Test
  void testCall2() throws Exception {
    LexerGrammar lg = new LexerGrammar(
      """
        lexer grammar L;
        A : 'a' ;
        B : 'b' ;
        C : 'c' ;
        """);
    Grammar g = new Grammar(
      """
        parser grammar T;
        s : t C ;
        t : u ;
        u : A{;} | B ;
        """,
      lg);

    testInterp(lg, g, "s", "ac", "(s (t (u a)) c)");
    testInterp(lg, g, "s", "bc", "(s (t (u b)) c)");
  }

  @Test
  void testOptionalA() throws Exception {
    LexerGrammar lg = new LexerGrammar(
      """
        lexer grammar L;
        A : 'a' ;
        B : 'b' ;
        C : 'c' ;
        """);
    Grammar g = new Grammar(
      """
        parser grammar T;
        s : A? B ;
        """,
      lg);

    testInterp(lg, g, "s", "b", "(s b)");
    testInterp(lg, g, "s", "ab", "(s a b)");
  }

  @Test
  void testOptionalAorB() throws Exception {
    LexerGrammar lg = new LexerGrammar(
      """
        lexer grammar L;
        A : 'a' ;
        B : 'b' ;
        C : 'c' ;
        """);
    Grammar g = new Grammar(
      """
        parser grammar T;
        s : (A{;}|B)? C ;
        """,
      lg);

    testInterp(lg, g, "s", "c", "(s c)");
    testInterp(lg, g, "s", "ac", "(s a c)");
    testInterp(lg, g, "s", "bc", "(s b c)");
  }

  @Test
  void testStarA() throws Exception {
    LexerGrammar lg = new LexerGrammar(
      """
        lexer grammar L;
        A : 'a' ;
        B : 'b' ;
        C : 'c' ;
        """);
    Grammar g = new Grammar(
      """
        parser grammar T;
        s : A* B ;
        """,
      lg);

    testInterp(lg, g, "s", "b", "(s b)");
    testInterp(lg, g, "s", "ab", "(s a b)");
    testInterp(lg, g, "s", "aaaaaab", "(s a a a a a a b)");
  }

  @Test
  void testStarAorB() throws Exception {
    LexerGrammar lg = new LexerGrammar(
      """
        lexer grammar L;
        A : 'a' ;
        B : 'b' ;
        C : 'c' ;
        """);
    Grammar g = new Grammar(
      """
        parser grammar T;
        s : (A{;}|B)* C ;
        """,
      lg);

    testInterp(lg, g, "s", "c", "(s c)");
    testInterp(lg, g, "s", "ac", "(s a c)");
    testInterp(lg, g, "s", "bc", "(s b c)");
    testInterp(lg, g, "s", "abaaabc", "(s a b a a a b c)");
    testInterp(lg, g, "s", "babac", "(s b a b a c)");
  }

  @Test
  void testLeftRecursion() throws Exception {
    LexerGrammar lg = new LexerGrammar(
      """
        lexer grammar L;
        A : 'a' ;
        B : 'b' ;
        C : 'c' ;
        PLUS : '+' ;
        MULT : '*' ;
        """);
    Grammar g = new Grammar(
      """
        parser grammar T;
        s : e ;
        e : e MULT e
          | e PLUS e
          | A
          ;
        """,
      lg);

    testInterp(lg, g, "s", "a", "(s (e a))");
    testInterp(lg, g, "s", "a+a", "(s (e (e a) + (e a)))");
    testInterp(lg, g, "s", "a*a", "(s (e (e a) * (e a)))");
    testInterp(lg, g, "s", "a+a+a", "(s (e (e (e a) + (e a)) + (e a)))");
    testInterp(lg, g, "s", "a*a+a", "(s (e (e (e a) * (e a)) + (e a)))");
    testInterp(lg, g, "s", "a+a*a", "(s (e (e a) + (e (e a) * (e a))))");
  }

  /**
   * This is a regression test for antlr/antlr4#461.
   * <a href="https://github.com/antlr/antlr4/issues/461">...</a>
   */
  @Test
  void testLeftRecursiveStartRule() throws Exception {
    LexerGrammar lg = new LexerGrammar(
      """
        lexer grammar L;
        A : 'a' ;
        B : 'b' ;
        C : 'c' ;
        PLUS : '+' ;
        MULT : '*' ;
        """);
    Grammar g = new Grammar(
      """
        parser grammar T;
        s : e ;
        e : e MULT e
          | e PLUS e
          | A
          ;
        """,
      lg);

    testInterp(lg, g, "e", "a", "(e a)");
    testInterp(lg, g, "e", "a+a", "(e (e a) + (e a))");
    testInterp(lg, g, "e", "a*a", "(e (e a) * (e a))");
    testInterp(lg, g, "e", "a+a+a", "(e (e (e a) + (e a)) + (e a))");
    testInterp(lg, g, "e", "a*a+a", "(e (e (e a) * (e a)) + (e a))");
    testInterp(lg, g, "e", "a+a*a", "(e (e a) + (e (e a) * (e a)))");
  }

  ParseTree testInterp(LexerGrammar lg, Grammar g,
                       String startRule, String input,
                       String expectedParseTree) {
    LexerInterpreter lexEngine = lg.createLexerInterpreter(CharStreams.fromString(input));
    CommonTokenStream tokens = new CommonTokenStream(lexEngine);
    ParserInterpreter parser = g.createParserInterpreter(tokens);
    ParseTree t = parser.parse(g.rules.get(startRule).index);
    System.out.println("parse tree: " + t.toStringTree(parser));
    assertThat(t.toStringTree(parser)).isEqualTo(expectedParseTree);
    return t;
  }
}
