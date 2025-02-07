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

import org.antlr.v4.runtime.atn.ATN;
import org.antlr.v4.runtime.atn.ATNDeserializationOptions;
import org.antlr.v4.runtime.atn.ATNDeserializer;
import org.antlr.v4.runtime.atn.ATNSerializer;
import org.antlr.v4.runtime.misc.Utils;
import org.antlr.v4.tool.Grammar;
import org.antlr.v4.tool.LexerGrammar;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.antlr.v4.TestUtils.assertEquals;

public class ATNDeserializationTest extends AbstractBaseTest {
  @Test
  public void testSimpleNoBlock() throws Exception {
    Grammar g = new Grammar(
      "parser grammar T;\n" +
        "a : A B ;");
    checkDeserializationIsStable(g);
  }

  @Test
  public void testEOF() throws Exception {
    Grammar g = new Grammar(
      "parser grammar T;\n" +
        "a : EOF ;");
    checkDeserializationIsStable(g);
  }

  @Test
  public void testEOFInSet() throws Exception {
    Grammar g = new Grammar(
      "parser grammar T;\n" +
        "a : (EOF|A) ;");
    checkDeserializationIsStable(g);
  }

  @Test
  public void testNot() throws Exception {
    Grammar g = new Grammar(
      """
        parser grammar T;
        tokens {A, B, C}
        a : ~A ;""");
    checkDeserializationIsStable(g);
  }

  @Test
  public void testWildcard() throws Exception {
    Grammar g = new Grammar(
      """
        parser grammar T;
        tokens {A, B, C}
        a : . ;""");
    checkDeserializationIsStable(g);
  }

  @Test
  public void testPEGAchillesHeel() throws Exception {
    Grammar g = new Grammar(
      "parser grammar T;\n" +
        "a : A | A B ;");
    checkDeserializationIsStable(g);
  }

  @Test
  public void test3Alts() throws Exception {
    Grammar g = new Grammar(
      "parser grammar T;\n" +
        "a : A | A B | A B C ;");
    checkDeserializationIsStable(g);
  }

  @Test
  public void testSimpleLoop() throws Exception {
    Grammar g = new Grammar(
      "parser grammar T;\n" +
        "a : A+ B ;");
    checkDeserializationIsStable(g);
  }

  @Test
  public void testRuleRef() throws Exception {
    Grammar g = new Grammar(
      """
        parser grammar T;
        a : e ;
        e : E ;
        """);
    checkDeserializationIsStable(g);
  }

  @Test
  public void testLexerTwoRules() throws Exception {
    LexerGrammar lg = new LexerGrammar(
      """
        lexer grammar L;
        A : 'a' ;
        B : 'b' ;
        """);
    checkDeserializationIsStable(lg);
  }

  @Test
  public void testLexerEOF() throws Exception {
    LexerGrammar lg = new LexerGrammar(
      """
        lexer grammar L;
        A : 'a' EOF ;
        """);
    checkDeserializationIsStable(lg);
  }

  @Test
  public void testLexerEOFInSet() throws Exception {
    LexerGrammar lg = new LexerGrammar(
      """
        lexer grammar L;
        A : 'a' (EOF|'\\n') ;
        """);
    checkDeserializationIsStable(lg);
  }

  @Test
  public void testLexerRange() throws Exception {
    LexerGrammar lg = new LexerGrammar(
      """
        lexer grammar L;
        INT : '0'..'9' ;
        """);
    checkDeserializationIsStable(lg);
  }

  @Test
  public void testLexerLoops() throws Exception {
    LexerGrammar lg = new LexerGrammar(
      """
        lexer grammar L;
        INT : '0'..'9'+ ;
        """);
    checkDeserializationIsStable(lg);
  }

  @Test
  public void testLexerNotSet() throws Exception {
    LexerGrammar lg = new LexerGrammar(
      """
        lexer grammar L;
        ID : ~('a'|'b')
         ;""");
    checkDeserializationIsStable(lg);
  }

  @Test
  public void testLexerNotSetWithRange() throws Exception {
    LexerGrammar lg = new LexerGrammar(
      """
        lexer grammar L;
        ID : ~('a'|'b'|'e'|'p'..'t')
         ;""");
    checkDeserializationIsStable(lg);
  }

  @Test
  public void testLexerNotSetWithRange2() throws Exception {
    LexerGrammar lg = new LexerGrammar(
      """
        lexer grammar L;
        ID : ~('a'|'b') ~('e'|'p'..'t')
         ;""");
    checkDeserializationIsStable(lg);
  }

  @Test
  public void test2ModesInLexer() throws Exception {
    LexerGrammar lg = new LexerGrammar(
      """
        lexer grammar L;
        A : 'a'
         ;
        mode M;
        B : 'b';
        mode M2;
        C : 'c';
        """);
    checkDeserializationIsStable(lg);
  }

  protected void checkDeserializationIsStable(Grammar g) {
    ATN atn = createATN(g, false);
    char[] data = Utils.toCharArray(ATNSerializer.getSerialized(atn, Arrays.asList(g.getRuleNames())));
    String atnData = ATNSerializer.getDecoded(atn, Arrays.asList(g.getRuleNames()), Arrays.asList(g.getTokenNames()));
    ATNDeserializationOptions options = new ATNDeserializationOptions();
    options.setOptimize(false);
    ATN atn2 = new ATNDeserializer(options).deserialize(data);
    String atn2Data = ATNSerializer.getDecoded(atn2, Arrays.asList(g.getRuleNames()), Arrays.asList(g.getTokenNames()));

    assertEquals(atnData, atn2Data);
  }
}
