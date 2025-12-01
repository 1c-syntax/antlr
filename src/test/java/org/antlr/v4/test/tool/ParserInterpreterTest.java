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
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.tool.Grammar;
import org.antlr.v4.tool.LexerGrammar;
import org.junit.jupiter.api.Test;

import static org.antlr.v4.TestUtils.assertEquals;

public class ParserInterpreterTest extends AbstractBaseTest {

  @Test
  public void testEmptyStartRule() throws Exception {
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
  public void testA() throws Exception {
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
    assertEquals("0..0", t.getSourceInterval().toString());
  }

  @Test
  public void testEOF() throws Exception {
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
    assertEquals("0..1", t.getSourceInterval().toString());
  }

  @Test
  public void testEOFInChild() throws Exception {
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
    assertEquals("0..1", t.getSourceInterval().toString());
    assertEquals("0..1", t.getChild(0).getSourceInterval().toString());
  }

  @Test
  public void testEmptyRuleAfterEOFInChild() throws Exception {
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
    assertEquals("0..1", t.getSourceInterval().toString()); // s
    assertEquals("0..1", t.getChild(0).getSourceInterval().toString()); // x
  }

  @Test
  public void testEmptyRuleAfterJustEOFInChild() throws Exception {
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
    assertEquals("0..0", t.getSourceInterval().toString()); // s
    assertEquals("0..0", t.getChild(0).getSourceInterval().toString()); // x
    // this next one is a weird special case where somebody tries to match beyond in the file
  }

  @Test
  public void testEmptyInput() throws Exception {
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
    assertEquals("0..0", t.getSourceInterval().toString()); // s
    assertEquals("0..-1", t.getChild(0).getSourceInterval().toString()); // x
  }

  @Test
  public void testEmptyInputWithCallsAfter() throws Exception {
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
    assertEquals("0..0", t.getSourceInterval().toString()); // s
    assertEquals("0..0", t.getChild(0).getSourceInterval().toString()); // x
  }

  @Test
  public void testEmptyFirstRule() throws Exception {
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
    assertEquals("0..0", t.getSourceInterval().toString()); // s
    // This gets an empty interval because the stop token is null for x
    assertEquals("0..-1", t.getChild(0).getSourceInterval().toString()); // x
  }

  @Test
  public void testAorB() throws Exception {
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
  public void testCall() throws Exception {
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
  public void testCall2() throws Exception {
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
  public void testOptionalA() throws Exception {
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
  public void testOptionalAorB() throws Exception {
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
  public void testStarA() throws Exception {
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
  public void testStarAorB() throws Exception {
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
  public void testLeftRecursion() throws Exception {
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
  public void testLeftRecursiveStartRule() throws Exception {
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
    assertEquals(expectedParseTree, t.toStringTree(parser));
    return t;
  }
}
