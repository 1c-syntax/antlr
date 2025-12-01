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

import org.antlr.v4.automata.ParserATNFactory;
import org.antlr.v4.runtime.atn.ATN;
import org.antlr.v4.runtime.atn.ATNState;
import org.antlr.v4.runtime.atn.BlockStartState;
import org.antlr.v4.runtime.atn.LexerATNSimulator;
import org.antlr.v4.runtime.misc.IntegerList;
import org.antlr.v4.tool.DOTGenerator;
import org.antlr.v4.tool.Grammar;
import org.antlr.v4.tool.LexerGrammar;
import org.antlr.v4.tool.Rule;
import org.junit.jupiter.api.Test;

import static org.antlr.v4.TestUtils.assertEquals;

// NOTICE: TOKENS IN LEXER, PARSER MUST BE SAME OR TOKEN TYPE MISMATCH

public class ATNInterpreterTest extends AbstractBaseTest {
  @Test
  public void testSimpleNoBlock() throws Exception {
    LexerGrammar lg = new LexerGrammar(
      """
        lexer grammar L;
        A : 'a' ;
        B : 'b' ;
        C : 'c' ;
        """);
    Grammar g = new Grammar(
      "parser grammar T;\n" +
        "a : A B ;");
    checkMatchedAlt(lg, g, "ab", 1);
  }

  @Test
  public void testSet() throws Exception {
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
        tokens {A,B,C}
        a : ~A ;""");
    checkMatchedAlt(lg, g, "b", 1);
  }

  @Test
  public void testPEGAchillesHeel() throws Exception {
    LexerGrammar lg = new LexerGrammar(
      """
        lexer grammar L;
        A : 'a' ;
        B : 'b' ;
        C : 'c' ;
        """);
    Grammar g = new Grammar(
      "parser grammar T;\n" +
        "a : A | A B ;");
    checkMatchedAlt(lg, g, "a", 1);
    checkMatchedAlt(lg, g, "ab", 2);
    checkMatchedAlt(lg, g, "abc", 2);
  }

  @Test
  public void testMustTrackPreviousGoodAlt() throws Exception {
    LexerGrammar lg = new LexerGrammar(
      """
        lexer grammar L;
        A : 'a' ;
        B : 'b' ;
        C : 'c' ;
        """);
    Grammar g = new Grammar(
      "parser grammar T;\n" +
        "a : A | A B ;");

    checkMatchedAlt(lg, g, "a", 1);
    checkMatchedAlt(lg, g, "ab", 2);

    checkMatchedAlt(lg, g, "ac", 1);
    checkMatchedAlt(lg, g, "abc", 2);
  }

  @Test
  public void testMustTrackPreviousGoodAlt2() throws Exception {
    LexerGrammar lg = new LexerGrammar(
      """
        lexer grammar L;
        A : 'a' ;
        B : 'b' ;
        C : 'c' ;
        D : 'd' ;
        """);
    Grammar g = new Grammar(
      "parser grammar T;\n" +
        "a : A | A B | A B C ;");

    checkMatchedAlt(lg, g, "a", 1);
    checkMatchedAlt(lg, g, "ab", 2);
    checkMatchedAlt(lg, g, "abc", 3);

    checkMatchedAlt(lg, g, "ad", 1);
    checkMatchedAlt(lg, g, "abd", 2);
    checkMatchedAlt(lg, g, "abcd", 3);
  }

  @Test
  public void testMustTrackPreviousGoodAlt3() throws Exception {
    LexerGrammar lg = new LexerGrammar(
      """
        lexer grammar L;
        A : 'a' ;
        B : 'b' ;
        C : 'c' ;
        D : 'd' ;
        """);
    Grammar g = new Grammar(
      "parser grammar T;\n" +
        "a : A B | A | A B C ;");

    checkMatchedAlt(lg, g, "a", 2);
    checkMatchedAlt(lg, g, "ab", 1);
    checkMatchedAlt(lg, g, "abc", 3);

    checkMatchedAlt(lg, g, "ad", 2);
    checkMatchedAlt(lg, g, "abd", 1);
    checkMatchedAlt(lg, g, "abcd", 3);
  }

  @Test
  public void testAmbigAltChooseFirst() throws Exception {
    LexerGrammar lg = new LexerGrammar(
      """
        lexer grammar L;
        A : 'a' ;
        B : 'b' ;
        C : 'c' ;
        D : 'd' ;
        """);
    Grammar g = new Grammar(
      "parser grammar T;\n" +
        "a : A B | A B ;"); // first alt
    checkMatchedAlt(lg, g, "ab", 1);
    checkMatchedAlt(lg, g, "abc", 1);
  }

  @Test
  public void testAmbigAltChooseFirstWithFollowingToken() throws Exception {
    LexerGrammar lg = new LexerGrammar(
      """
        lexer grammar L;
        A : 'a' ;
        B : 'b' ;
        C : 'c' ;
        D : 'd' ;
        """);
    Grammar g = new Grammar(
      "parser grammar T;\n" +
        "a : (A B | A B) C ;"); // first alt
    checkMatchedAlt(lg, g, "abc", 1);
    checkMatchedAlt(lg, g, "abcd", 1);
  }

  @Test
  public void testAmbigAltChooseFirstWithFollowingToken2() throws Exception {
    LexerGrammar lg = new LexerGrammar(
      """
        lexer grammar L;
        A : 'a' ;
        B : 'b' ;
        C : 'c' ;
        D : 'd' ;
        """);
    Grammar g = new Grammar(
      "parser grammar T;\n" +
        "a : (A B | A B | C) D ;");
    checkMatchedAlt(lg, g, "abd", 1);
    checkMatchedAlt(lg, g, "abdc", 1);
    checkMatchedAlt(lg, g, "cd", 3);
  }

  @Test
  public void testAmbigAltChooseFirst2() throws Exception {
    LexerGrammar lg = new LexerGrammar(
      """
        lexer grammar L;
        A : 'a' ;
        B : 'b' ;
        C : 'c' ;
        D : 'd' ;
        """);
    Grammar g = new Grammar(
      "parser grammar T;\n" +
        "a : A B | A B | A B C ;");

    checkMatchedAlt(lg, g, "ab", 1);
    checkMatchedAlt(lg, g, "abc", 3);

    checkMatchedAlt(lg, g, "abd", 1);
    checkMatchedAlt(lg, g, "abcd", 3);
  }

  @Test
  public void testSimpleLoop() throws Exception {
    LexerGrammar lg = new LexerGrammar(
      """
        lexer grammar L;
        A : 'a' ;
        B : 'b' ;
        C : 'c' ;
        D : 'd' ;
        """);
    Grammar g = new Grammar(
      "parser grammar T;\n" +
        "a : A+ B ;");
    checkMatchedAlt(lg, g, "ab", 1);
    checkMatchedAlt(lg, g, "aab", 1);
    checkMatchedAlt(lg, g, "aaaaaab", 1);
    checkMatchedAlt(lg, g, "aabd", 1);
  }

  @Test
  public void testCommonLeftPrefix() throws Exception {
    LexerGrammar lg = new LexerGrammar(
      """
        lexer grammar L;
        A : 'a' ;
        B : 'b' ;
        C : 'c' ;
        """);
    Grammar g = new Grammar(
      "parser grammar T;\n" +
        "a : A B | A C ;");
    checkMatchedAlt(lg, g, "ab", 1);
    checkMatchedAlt(lg, g, "ac", 2);
  }

  @Test
  public void testArbitraryLeftPrefix() throws Exception {
    LexerGrammar lg = new LexerGrammar(
      """
        lexer grammar L;
        A : 'a' ;
        B : 'b' ;
        C : 'c' ;
        """);
    Grammar g = new Grammar(
      "parser grammar T;\n" +
        "a : A+ B | A+ C ;");
    checkMatchedAlt(lg, g, "aac", 2);
  }

  @Test
  public void testRecursiveLeftPrefix() throws Exception {
    LexerGrammar lg = new LexerGrammar(
      """
        lexer grammar L;
        A : 'a' ;
        B : 'b' ;
        C : 'c' ;
        LP : '(' ;
        RP : ')' ;
        INT : '0'..'9'+ ;
        """
    );
    Grammar g = new Grammar(
      """
        parser grammar T;
        tokens {A,B,C,LP,RP,INT}
        a : e B | e C ;
        e : LP e RP
          | INT
          ;""");
    checkMatchedAlt(lg, g, "34b", 1);
    checkMatchedAlt(lg, g, "34c", 2);
    checkMatchedAlt(lg, g, "(34)b", 1);
    checkMatchedAlt(lg, g, "(34)c", 2);
    checkMatchedAlt(lg, g, "((34))b", 1);
    checkMatchedAlt(lg, g, "((34))c", 2);
  }

  public void checkMatchedAlt(LexerGrammar lg, final Grammar g,
                              String inputString,
                              int expected) {
    ATN lexatn = createATN(lg, true);
    LexerATNSimulator lexInterp = new LexerATNSimulator(lexatn);
    IntegerList types = getTokenTypesViaATN(inputString, lexInterp);
    System.out.println(types);

    g.importVocab(lg);

    ParserATNFactory f = new ParserATNFactory(g);
    ATN atn = f.createATN();

    IntTokenStream input = new IntTokenStream(types);
    System.out.println("input=" + input.types);
    ParserInterpreterForTesting interp = new ParserInterpreterForTesting(g, input);
    ATNState startState = atn.ruleToStartState[g.getRule("a").index];
    if (startState.transition(0).target instanceof BlockStartState) {
      startState = startState.transition(0).target;
    }

    DOTGenerator dot = new DOTGenerator(g);
    System.out.println(dot.getDOT(atn.ruleToStartState[g.getRule("a").index]));
    Rule r = g.getRule("e");
    if (r != null) System.out.println(dot.getDOT(atn.ruleToStartState[r.index]));

    int result = interp.matchATN(input, startState);
    assertEquals(expected, result);
  }
}
