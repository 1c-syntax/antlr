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
import org.antlr.v4.runtime.atn.ATNSerializer;
import org.antlr.v4.tool.DOTGenerator;
import org.antlr.v4.tool.Grammar;
import org.antlr.v4.tool.LexerGrammar;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.antlr.v4.TestUtils.assertEquals;


public class ATNSerializationTest extends AbstractBaseTest {
  @Test
  public void testSimpleNoBlock() throws Exception {
    Grammar g = new Grammar(
      "parser grammar T;\n" +
        "a : A B ;");
    String expecting =
      """
        max type 2
        0:RULE_START 0
        1:RULE_STOP 0
        2:BASIC 0
        3:BASIC 0
        4:BASIC 0
        5:BASIC 0
        rule 0:0
        0->2 EPSILON 0,0,0
        2->3 ATOM 1,0,0
        3->4 ATOM 2,0,0
        4->1 EPSILON 0,0,0
        """;
    ATN atn = createATN(g, true);
    String result = ATNSerializer.getDecoded(atn, Arrays.asList(g.getRuleNames()), Arrays.asList(g.getTokenNames()));
    assertEquals(expecting, result);
  }

  @Test
  public void testEOF() throws Exception {
    Grammar g = new Grammar(
      "parser grammar T;\n" +
        "a : A EOF ;");
    String expecting =
      """
        max type 1
        0:RULE_START 0
        1:RULE_STOP 0
        2:BASIC 0
        3:BASIC 0
        4:BASIC 0
        5:BASIC 0
        rule 0:0
        0->2 EPSILON 0,0,0
        2->3 ATOM 1,0,0
        3->4 ATOM 0,0,1
        4->1 EPSILON 0,0,0
        """;
    ATN atn = createATN(g, true);
    String result = ATNSerializer.getDecoded(atn, Arrays.asList(g.getRuleNames()), Arrays.asList(g.getTokenNames()));
    assertEquals(expecting, result);
  }

  @Test
  public void testEOFInSet() throws Exception {
    Grammar g = new Grammar(
      """
        parser grammar T;
        a : (A|EOF) ;""");
    String expecting =
      """
        max type 1
        0:RULE_START 0
        1:RULE_STOP 0
        2:BASIC 0
        3:BASIC 0
        4:BASIC 0
        rule 0:0
        0:EOF, A..A
        0->2 EPSILON 0,0,0
        2->3 SET 0,0,0
        3->1 EPSILON 0,0,0
        """;
    ATN atn = createATN(g, true);
    String result = ATNSerializer.getDecoded(atn, Arrays.asList(g.getRuleNames()), Arrays.asList(g.getTokenNames()));
    assertEquals(expecting, result);
  }

  @Test
  public void testNot() throws Exception {
    Grammar g = new Grammar(
      """
        parser grammar T;
        tokens {A, B, C}
        a : ~A ;""");
    String expecting =
      """
        max type 3
        0:RULE_START 0
        1:RULE_STOP 0
        2:BASIC 0
        3:BASIC 0
        4:BASIC 0
        rule 0:0
        0:A..A
        0->2 EPSILON 0,0,0
        2->3 NOT_SET 0,0,0
        3->1 EPSILON 0,0,0
        """;
    ATN atn = createATN(g, true);
    DOTGenerator gen = new DOTGenerator(g);
    System.out.println(gen.getDOT(atn.ruleToStartState[0]));
    String result = ATNSerializer.getDecoded(atn, Arrays.asList(g.getRuleNames()), Arrays.asList(g.getTokenNames()));
    assertEquals(expecting, result);
  }

  @Test
  public void testWildcard() throws Exception {
    Grammar g = new Grammar(
      """
        parser grammar T;
        tokens {A, B, C}
        a : . ;""");
    String expecting =
      """
        max type 3
        0:RULE_START 0
        1:RULE_STOP 0
        2:BASIC 0
        3:BASIC 0
        4:BASIC 0
        rule 0:0
        0->2 EPSILON 0,0,0
        2->3 WILDCARD 0,0,0
        3->1 EPSILON 0,0,0
        """;
    ATN atn = createATN(g, true);
    String result = ATNSerializer.getDecoded(atn, Arrays.asList(g.getRuleNames()), Arrays.asList(g.getTokenNames()));
    assertEquals(expecting, result);
  }

  @Test
  public void testPEGAchillesHeel() throws Exception {
    Grammar g = new Grammar(
      "parser grammar T;\n" +
        "a : A | A B ;");
    String expecting =
      """
        max type 2
        0:RULE_START 0
        1:RULE_STOP 0
        2:BASIC 0
        3:BASIC 0
        4:BASIC 0
        5:BLOCK_START 0 6
        6:BLOCK_END 0
        7:BASIC 0
        rule 0:0
        0->5 EPSILON 0,0,0
        2->6 ATOM 1,0,0
        3->4 ATOM 1,0,0
        4->6 ATOM 2,0,0
        5->2 EPSILON 0,0,0
        5->3 EPSILON 0,0,0
        6->1 EPSILON 0,0,0
        0:5
        """;
    ATN atn = createATN(g, true);
    String result = ATNSerializer.getDecoded(atn, Arrays.asList(g.getRuleNames()), Arrays.asList(g.getTokenNames()));
    assertEquals(expecting, result);
  }

  @Test
  public void test3Alts() throws Exception {
    Grammar g = new Grammar(
      "parser grammar T;\n" +
        "a : A | A B | A B C ;");
    String expecting =
      """
        max type 3
        0:RULE_START 0
        1:RULE_STOP 0
        2:BASIC 0
        3:BASIC 0
        4:BASIC 0
        5:BASIC 0
        6:BASIC 0
        7:BASIC 0
        8:BLOCK_START 0 9
        9:BLOCK_END 0
        10:BASIC 0
        rule 0:0
        0->8 EPSILON 0,0,0
        2->9 ATOM 1,0,0
        3->4 ATOM 1,0,0
        4->9 ATOM 2,0,0
        5->6 ATOM 1,0,0
        6->7 ATOM 2,0,0
        7->9 ATOM 3,0,0
        8->2 EPSILON 0,0,0
        8->3 EPSILON 0,0,0
        8->5 EPSILON 0,0,0
        9->1 EPSILON 0,0,0
        0:8
        """;
    ATN atn = createATN(g, true);
    String result = ATNSerializer.getDecoded(atn, Arrays.asList(g.getRuleNames()), Arrays.asList(g.getTokenNames()));
    assertEquals(expecting, result);
  }

  @Test
  public void testSimpleLoop() throws Exception {
    Grammar g = new Grammar(
      "parser grammar T;\n" +
        "a : A+ B ;");
    String expecting =
      """
        max type 2
        0:RULE_START 0
        1:RULE_STOP 0
        2:BASIC 0
        3:PLUS_BLOCK_START 0 4
        4:BLOCK_END 0
        5:PLUS_LOOP_BACK 0
        6:LOOP_END 0 5
        7:BASIC 0
        8:BASIC 0
        9:BASIC 0
        rule 0:0
        0->3 EPSILON 0,0,0
        2->4 ATOM 1,0,0
        3->2 EPSILON 0,0,0
        4->5 EPSILON 0,0,0
        5->3 EPSILON 0,0,0
        5->6 EPSILON 0,0,0
        6->7 EPSILON 0,0,0
        7->8 ATOM 2,0,0
        8->1 EPSILON 0,0,0
        0:5
        """;
    ATN atn = createATN(g, true);
    String result = ATNSerializer.getDecoded(atn, Arrays.asList(g.getRuleNames()), Arrays.asList(g.getTokenNames()));
    assertEquals(expecting, result);
  }

  @Test
  public void testRuleRef() throws Exception {
    Grammar g = new Grammar(
      """
        parser grammar T;
        a : e ;
        e : E ;
        """);
    String expecting =
      """
        max type 1
        0:RULE_START 0
        1:RULE_STOP 0
        2:RULE_START 1
        3:RULE_STOP 1
        4:BASIC 0
        5:BASIC 0
        6:BASIC 1
        7:BASIC 1
        8:BASIC 1
        rule 0:0
        rule 1:2
        0->4 EPSILON 0,0,0
        2->6 EPSILON 0,0,0
        4->5 RULE 2,1,0
        5->1 EPSILON 0,0,0
        6->7 ATOM 1,0,0
        7->3 EPSILON 0,0,0
        """;
    ATN atn = createATN(g, false);
    String result = ATNSerializer.getDecoded(atn, Arrays.asList(g.getRuleNames()), Arrays.asList(g.getTokenNames()));
    assertEquals(expecting, result);
  }

  @Test
  public void testLexerTwoRules() throws Exception {
    LexerGrammar lg = new LexerGrammar(
      """
        lexer grammar L;
        A : 'a' ;
        B : 'b' ;
        """);
    String expecting =
      """
        max type 2
        0:TOKEN_START -1
        1:RULE_START 0
        2:RULE_STOP 0
        3:RULE_START 1
        4:RULE_STOP 1
        5:BASIC 0
        6:BASIC 0
        7:BASIC 1
        8:BASIC 1
        rule 0:1 1
        rule 1:3 2
        mode 0:0
        0->1 EPSILON 0,0,0
        0->3 EPSILON 0,0,0
        1->5 EPSILON 0,0,0
        3->7 EPSILON 0,0,0
        5->6 ATOM 97,0,0
        6->2 EPSILON 0,0,0
        7->8 ATOM 98,0,0
        8->4 EPSILON 0,0,0
        0:0
        """;
    ATN atn = createATN(lg, true);
    String result = ATNSerializer.getDecoded(atn, Arrays.asList(lg.getRuleNames()), Arrays.asList(lg.getTokenNames()));
    assertEquals(expecting, result);
  }

  @Test
  public void testLexerUnicodeSMPLiteralSerializedToSet() throws Exception {
    LexerGrammar lg = new LexerGrammar(
      "lexer grammar L;\n" +
        "INT : '\\u{1F4A9}' ;");
    String expecting =
      """
        max type 1
        0:TOKEN_START -1
        1:RULE_START 0
        2:RULE_STOP 0
        3:BASIC 0
        4:BASIC 0
        rule 0:1 1
        mode 0:0
        0:128169..128169
        0->1 EPSILON 0,0,0
        1->3 EPSILON 0,0,0
        3->4 SET 0,0,0
        4->2 EPSILON 0,0,0
        0:0
        """;
    ATN atn = createATN(lg, true);
    String result = ATNSerializer.getDecoded(atn, Arrays.asList(lg.getRuleNames()), Arrays.asList(lg.getTokenNames()));
    assertEquals(expecting, result);
  }

  @Test
  public void testLexerUnicodeSMPRangeSerializedToSet() throws Exception {
    LexerGrammar lg = new LexerGrammar(
      "lexer grammar L;\n" +
        "INT : ('a'..'\\u{1F4A9}') ;");
    String expecting =
      """
        max type 1
        0:TOKEN_START -1
        1:RULE_START 0
        2:RULE_STOP 0
        3:BASIC 0
        4:BASIC 0
        rule 0:1 1
        mode 0:0
        0:'a'..128169
        0->1 EPSILON 0,0,0
        1->3 EPSILON 0,0,0
        3->4 SET 0,0,0
        4->2 EPSILON 0,0,0
        0:0
        """;
    ATN atn = createATN(lg, true);
    String result = ATNSerializer.getDecoded(atn, Arrays.asList(lg.getRuleNames()), Arrays.asList(lg.getTokenNames()));
    assertEquals(expecting, result);
  }

  @Test
  public void testLexerUnicodeSMPSetSerializedAfterBMPSet() throws Exception {
    LexerGrammar lg = new LexerGrammar(
      """
        lexer grammar L;
        SMP : ('\\u{1F4A9}' | '\\u{1F4AF}') ;
        BMP : ('a' | 'x') ;""");
    String expecting =
      """
        max type 2
        0:TOKEN_START -1
        1:RULE_START 0
        2:RULE_STOP 0
        3:RULE_START 1
        4:RULE_STOP 1
        5:BASIC 0
        6:BASIC 0
        7:BASIC 1
        8:BASIC 1
        rule 0:1 1
        rule 1:3 2
        mode 0:0
        0:'a'..'a', 'x'..'x'
        1:128169..128169, 128175..128175
        0->1 EPSILON 0,0,0
        0->3 EPSILON 0,0,0
        1->5 EPSILON 0,0,0
        3->7 EPSILON 0,0,0
        5->6 SET 1,0,0
        6->2 EPSILON 0,0,0
        7->8 SET 0,0,0
        8->4 EPSILON 0,0,0
        0:0
        """;
    ATN atn = createATN(lg, true);
    String result = ATNSerializer.getDecoded(atn, Arrays.asList(lg.getRuleNames()), Arrays.asList(lg.getTokenNames()));
    assertEquals(expecting, result);
  }

  @Test
  public void testLexerNotLiteral() throws Exception {
    LexerGrammar lg = new LexerGrammar(
      "lexer grammar L;\n" +
        "INT : ~'a' ;");
    String expecting =
      """
        max type 1
        0:TOKEN_START -1
        1:RULE_START 0
        2:RULE_STOP 0
        3:BASIC 0
        4:BASIC 0
        rule 0:1 1
        mode 0:0
        0:'a'..'a'
        0->1 EPSILON 0,0,0
        1->3 EPSILON 0,0,0
        3->4 NOT_SET 0,0,0
        4->2 EPSILON 0,0,0
        0:0
        """;
    ATN atn = createATN(lg, true);
    String result = ATNSerializer.getDecoded(atn, Arrays.asList(lg.getRuleNames()), Arrays.asList(lg.getTokenNames()));
    assertEquals(expecting, result);
  }

  @Test
  public void testLexerRange() throws Exception {
    LexerGrammar lg = new LexerGrammar(
      """
        lexer grammar L;
        INT : '0'..'9' ;
        """);
    String expecting =
      """
        max type 1
        0:TOKEN_START -1
        1:RULE_START 0
        2:RULE_STOP 0
        3:BASIC 0
        4:BASIC 0
        rule 0:1 1
        mode 0:0
        0->1 EPSILON 0,0,0
        1->3 EPSILON 0,0,0
        3->4 RANGE 48,57,0
        4->2 EPSILON 0,0,0
        0:0
        """;
    ATN atn = createATN(lg, true);
    String result = ATNSerializer.getDecoded(atn, Arrays.asList(lg.getRuleNames()), Arrays.asList(lg.getTokenNames()));
    assertEquals(expecting, result);
  }

  @Test
  public void testLexerEOF() throws Exception {
    LexerGrammar lg = new LexerGrammar(
      """
        lexer grammar L;
        INT : 'a' EOF ;
        """);
    String expecting =
      """
        max type 1
        0:TOKEN_START -1
        1:RULE_START 0
        2:RULE_STOP 0
        3:BASIC 0
        4:BASIC 0
        5:BASIC 0
        rule 0:1 1
        mode 0:0
        0->1 EPSILON 0,0,0
        1->3 EPSILON 0,0,0
        3->4 ATOM 97,0,0
        4->5 ATOM 0,0,1
        5->2 EPSILON 0,0,0
        0:0
        """;
    ATN atn = createATN(lg, true);
    String result = ATNSerializer.getDecoded(atn, Arrays.asList(lg.getRuleNames()), Arrays.asList(lg.getTokenNames()));
    assertEquals(expecting, result);
  }

  @Test
  public void testLexerEOFInSet() throws Exception {
    LexerGrammar lg = new LexerGrammar(
      """
        lexer grammar L;
        INT : 'a' (EOF|'\\n') ;
        """);
    String expecting =
      """
        max type 1
        0:TOKEN_START -1
        1:RULE_START 0
        2:RULE_STOP 0
        3:BASIC 0
        4:BASIC 0
        5:BLOCK_START 0 6
        6:BLOCK_END 0
        rule 0:1 1
        mode 0:0
        0:EOF, '\\n'..'\\n'
        0->1 EPSILON 0,0,0
        1->3 EPSILON 0,0,0
        3->5 ATOM 97,0,0
        4->6 SET 0,0,0
        5->4 EPSILON 0,0,0
        6->2 EPSILON 0,0,0
        0:0
        1:5
        """;
    ATN atn = createATN(lg, true);
    String result = ATNSerializer.getDecoded(atn, Arrays.asList(lg.getRuleNames()), Arrays.asList(lg.getTokenNames()));
    assertEquals(expecting, result);
  }

  @Test
  public void testLexerLoops() throws Exception {
    LexerGrammar lg = new LexerGrammar(
      """
        lexer grammar L;
        INT : '0'..'9'+ ;
        """);
    String expecting =
      """
        max type 1
        0:TOKEN_START -1
        1:RULE_START 0
        2:RULE_STOP 0
        3:BASIC 0
        4:PLUS_BLOCK_START 0 5
        5:BLOCK_END 0
        6:PLUS_LOOP_BACK 0
        7:LOOP_END 0 6
        rule 0:1 1
        mode 0:0
        0->1 EPSILON 0,0,0
        1->4 EPSILON 0,0,0
        3->5 RANGE 48,57,0
        4->3 EPSILON 0,0,0
        5->6 EPSILON 0,0,0
        6->4 EPSILON 0,0,0
        6->7 EPSILON 0,0,0
        7->2 EPSILON 0,0,0
        0:0
        1:6
        """;
    ATN atn = createATN(lg, true);
    String result = ATNSerializer.getDecoded(atn, Arrays.asList(lg.getRuleNames()), Arrays.asList(lg.getTokenNames()));
    assertEquals(expecting, result);
  }

  @Test
  public void testLexerAction() throws Exception {
    LexerGrammar lg = new LexerGrammar(
      """
        lexer grammar L;
        A : 'a' {a} ;
        B : 'b' ;
        C : 'c' {c} ;
        """);
    String expecting =
      """
        max type 3
        0:TOKEN_START -1
        1:RULE_START 0
        2:RULE_STOP 0
        3:RULE_START 1
        4:RULE_STOP 1
        5:RULE_START 2
        6:RULE_STOP 2
        7:BASIC 0
        8:BASIC 0
        9:BASIC 0
        10:BASIC 1
        11:BASIC 1
        12:BASIC 2
        13:BASIC 2
        14:BASIC 2
        rule 0:1 1
        rule 1:3 2
        rule 2:5 3
        mode 0:0
        0->1 EPSILON 0,0,0
        0->3 EPSILON 0,0,0
        0->5 EPSILON 0,0,0
        1->7 EPSILON 0,0,0
        3->10 EPSILON 0,0,0
        5->12 EPSILON 0,0,0
        7->8 ATOM 97,0,0
        8->9 ACTION 0,0,0
        9->2 EPSILON 0,0,0
        10->11 ATOM 98,0,0
        11->4 EPSILON 0,0,0
        12->13 ATOM 99,0,0
        13->14 ACTION 2,1,0
        14->6 EPSILON 0,0,0
        0:0
        """;
    ATN atn = createATN(lg, true);
    String result = ATNSerializer.getDecoded(atn, Arrays.asList(lg.getRuleNames()), Arrays.asList(lg.getTokenNames()));
    assertEquals(expecting, result);
  }

  @Test
  public void testLexerNotSet() throws Exception {
    LexerGrammar lg = new LexerGrammar(
      """
        lexer grammar L;
        ID : ~('a'|'b')
         ;""");
    String expecting =
      """
        max type 1
        0:TOKEN_START -1
        1:RULE_START 0
        2:RULE_STOP 0
        3:BASIC 0
        4:BASIC 0
        rule 0:1 1
        mode 0:0
        0:'a'..'b'
        0->1 EPSILON 0,0,0
        1->3 EPSILON 0,0,0
        3->4 NOT_SET 0,0,0
        4->2 EPSILON 0,0,0
        0:0
        """;
    ATN atn = createATN(lg, true);
    String result = ATNSerializer.getDecoded(atn, Arrays.asList(lg.getRuleNames()), Arrays.asList(lg.getTokenNames()));
    assertEquals(expecting, result);
  }

  @Test
  public void testLexerSetWithRange() throws Exception {
    LexerGrammar lg = new LexerGrammar(
      """
        lexer grammar L;
        ID : ('a'|'b'|'e'|'p'..'t')
         ;""");
    String expecting =
      """
        max type 1
        0:TOKEN_START -1
        1:RULE_START 0
        2:RULE_STOP 0
        3:BASIC 0
        4:BASIC 0
        rule 0:1 1
        mode 0:0
        0:'a'..'b', 'e'..'e', 'p'..'t'
        0->1 EPSILON 0,0,0
        1->3 EPSILON 0,0,0
        3->4 SET 0,0,0
        4->2 EPSILON 0,0,0
        0:0
        """;
    ATN atn = createATN(lg, true);
    String result = ATNSerializer.getDecoded(atn, Arrays.asList(lg.getRuleNames()), Arrays.asList(lg.getTokenNames()));
    assertEquals(expecting, result);
  }

  @Test
  public void testLexerNotSetWithRange() throws Exception {
    LexerGrammar lg = new LexerGrammar(
      """
        lexer grammar L;
        ID : ~('a'|'b'|'e'|'p'..'t')
         ;""");
    String expecting =
      """
        max type 1
        0:TOKEN_START -1
        1:RULE_START 0
        2:RULE_STOP 0
        3:BASIC 0
        4:BASIC 0
        rule 0:1 1
        mode 0:0
        0:'a'..'b', 'e'..'e', 'p'..'t'
        0->1 EPSILON 0,0,0
        1->3 EPSILON 0,0,0
        3->4 NOT_SET 0,0,0
        4->2 EPSILON 0,0,0
        0:0
        """;
    ATN atn = createATN(lg, true);
    String result = ATNSerializer.getDecoded(atn, Arrays.asList(lg.getRuleNames()), Arrays.asList(lg.getTokenNames()));
    assertEquals(expecting, result);
  }

  @Test
  public void testLexerUnicodeUnescapedBMPNotSet() throws Exception {
    LexerGrammar lg = new LexerGrammar(
      """
        lexer grammar L;
        ID : ~('\u4E9C'|'\u4E9D')
         ;""");
    String expecting =
      """
        max type 1
        0:TOKEN_START -1
        1:RULE_START 0
        2:RULE_STOP 0
        3:BASIC 0
        4:BASIC 0
        rule 0:1 1
        mode 0:0
        0:'\\u4E9C'..'\\u4E9D'
        0->1 EPSILON 0,0,0
        1->3 EPSILON 0,0,0
        3->4 NOT_SET 0,0,0
        4->2 EPSILON 0,0,0
        0:0
        """;
    ATN atn = createATN(lg, true);
    String result = ATNSerializer.getDecoded(atn, Arrays.asList(lg.getRuleNames()), Arrays.asList(lg.getTokenNames()));
    assertEquals(expecting, result);
  }

  @Test
  public void testLexerUnicodeUnescapedBMPSetWithRange() throws Exception {
    LexerGrammar lg = new LexerGrammar(
      """
        lexer grammar L;
        ID : ('\u4E9C'|'\u4E9D'|'\u6C5F'|'\u305F'..'\u307B')
         ;""");
    String expecting =
      """
        max type 1
        0:TOKEN_START -1
        1:RULE_START 0
        2:RULE_STOP 0
        3:BASIC 0
        4:BASIC 0
        rule 0:1 1
        mode 0:0
        0:'\\u305F'..'\\u307B', '\\u4E9C'..'\\u4E9D', '\\u6C5F'..'\\u6C5F'
        0->1 EPSILON 0,0,0
        1->3 EPSILON 0,0,0
        3->4 SET 0,0,0
        4->2 EPSILON 0,0,0
        0:0
        """;
    ATN atn = createATN(lg, true);
    String result = ATNSerializer.getDecoded(atn, Arrays.asList(lg.getRuleNames()), Arrays.asList(lg.getTokenNames()));
    assertEquals(expecting, result);
  }

  @Test
  public void testLexerUnicodeUnescapedBMPNotSetWithRange() throws Exception {
    LexerGrammar lg = new LexerGrammar(
      """
        lexer grammar L;
        ID : ~('\u4E9C'|'\u4E9D'|'\u6C5F'|'\u305F'..'\u307B')
         ;""");
    String expecting =
      """
        max type 1
        0:TOKEN_START -1
        1:RULE_START 0
        2:RULE_STOP 0
        3:BASIC 0
        4:BASIC 0
        rule 0:1 1
        mode 0:0
        0:'\\u305F'..'\\u307B', '\\u4E9C'..'\\u4E9D', '\\u6C5F'..'\\u6C5F'
        0->1 EPSILON 0,0,0
        1->3 EPSILON 0,0,0
        3->4 NOT_SET 0,0,0
        4->2 EPSILON 0,0,0
        0:0
        """;
    ATN atn = createATN(lg, true);
    String result = ATNSerializer.getDecoded(atn, Arrays.asList(lg.getRuleNames()), Arrays.asList(lg.getTokenNames()));
    assertEquals(expecting, result);
  }

  @Test
  public void testLexerUnicodeEscapedBMPNotSet() throws Exception {
    LexerGrammar lg = new LexerGrammar(
      """
        lexer grammar L;
        ID : ~('\\u4E9C'|'\\u4E9D')
         ;""");
    String expecting =
      """
        max type 1
        0:TOKEN_START -1
        1:RULE_START 0
        2:RULE_STOP 0
        3:BASIC 0
        4:BASIC 0
        rule 0:1 1
        mode 0:0
        0:'\\u4E9C'..'\\u4E9D'
        0->1 EPSILON 0,0,0
        1->3 EPSILON 0,0,0
        3->4 NOT_SET 0,0,0
        4->2 EPSILON 0,0,0
        0:0
        """;
    ATN atn = createATN(lg, true);
    String result = ATNSerializer.getDecoded(atn, Arrays.asList(lg.getRuleNames()), Arrays.asList(lg.getTokenNames()));
    assertEquals(expecting, result);
  }

  @Test
  public void testLexerUnicodeEscapedBMPSetWithRange() throws Exception {
    LexerGrammar lg = new LexerGrammar(
      """
        lexer grammar L;
        ID : ('\\u4E9C'|'\\u4E9D'|'\\u6C5F'|'\\u305F'..'\\u307B')
         ;""");
    String expecting =
      """
        max type 1
        0:TOKEN_START -1
        1:RULE_START 0
        2:RULE_STOP 0
        3:BASIC 0
        4:BASIC 0
        rule 0:1 1
        mode 0:0
        0:'\\u305F'..'\\u307B', '\\u4E9C'..'\\u4E9D', '\\u6C5F'..'\\u6C5F'
        0->1 EPSILON 0,0,0
        1->3 EPSILON 0,0,0
        3->4 SET 0,0,0
        4->2 EPSILON 0,0,0
        0:0
        """;
    ATN atn = createATN(lg, true);
    String result = ATNSerializer.getDecoded(atn, Arrays.asList(lg.getRuleNames()), Arrays.asList(lg.getTokenNames()));
    assertEquals(expecting, result);
  }

  @Test
  public void testLexerUnicodeEscapedBMPNotSetWithRange() throws Exception {
    LexerGrammar lg = new LexerGrammar(
      """
        lexer grammar L;
        ID : ~('\\u4E9C'|'\\u4E9D'|'\\u6C5F'|'\\u305F'..'\\u307B')
         ;""");
    String expecting =
      """
        max type 1
        0:TOKEN_START -1
        1:RULE_START 0
        2:RULE_STOP 0
        3:BASIC 0
        4:BASIC 0
        rule 0:1 1
        mode 0:0
        0:'\\u305F'..'\\u307B', '\\u4E9C'..'\\u4E9D', '\\u6C5F'..'\\u6C5F'
        0->1 EPSILON 0,0,0
        1->3 EPSILON 0,0,0
        3->4 NOT_SET 0,0,0
        4->2 EPSILON 0,0,0
        0:0
        """;
    ATN atn = createATN(lg, true);
    String result = ATNSerializer.getDecoded(atn, Arrays.asList(lg.getRuleNames()), Arrays.asList(lg.getTokenNames()));
    assertEquals(expecting, result);
  }

  @Test
  public void testLexerUnicodeEscapedSMPNotSet() throws Exception {
    LexerGrammar lg = new LexerGrammar(
      """
        lexer grammar L;
        ID : ~('\\u{1F4A9}'|'\\u{1F4AA}')
         ;""");
    String expecting =
      """
        max type 1
        0:TOKEN_START -1
        1:RULE_START 0
        2:RULE_STOP 0
        3:BASIC 0
        4:BASIC 0
        rule 0:1 1
        mode 0:0
        0:128169..128170
        0->1 EPSILON 0,0,0
        1->3 EPSILON 0,0,0
        3->4 NOT_SET 0,0,0
        4->2 EPSILON 0,0,0
        0:0
        """;
    ATN atn = createATN(lg, true);
    String result = ATNSerializer.getDecoded(atn, Arrays.asList(lg.getRuleNames()), Arrays.asList(lg.getTokenNames()));
    assertEquals(expecting, result);
  }

  @Test
  public void testLexerUnicodeEscapedSMPSetWithRange() throws Exception {
    LexerGrammar lg = new LexerGrammar(
      """
        lexer grammar L;
        ID : ('\\u{1F4A9}'|'\\u{1F4AA}'|'\\u{1F441}'|'\\u{1D40F}'..'\\u{1D413}')
         ;""");
    String expecting =
      """
        max type 1
        0:TOKEN_START -1
        1:RULE_START 0
        2:RULE_STOP 0
        3:BASIC 0
        4:BASIC 0
        rule 0:1 1
        mode 0:0
        0:119823..119827, 128065..128065, 128169..128170
        0->1 EPSILON 0,0,0
        1->3 EPSILON 0,0,0
        3->4 SET 0,0,0
        4->2 EPSILON 0,0,0
        0:0
        """;
    ATN atn = createATN(lg, true);
    String result = ATNSerializer.getDecoded(atn, Arrays.asList(lg.getRuleNames()), Arrays.asList(lg.getTokenNames()));
    assertEquals(expecting, result);
  }

  @Test
  public void testLexerUnicodeEscapedSMPNotSetWithRange() throws Exception {
    LexerGrammar lg = new LexerGrammar(
      """
        lexer grammar L;
        ID : ~('\\u{1F4A9}'|'\\u{1F4AA}'|'\\u{1F441}'|'\\u{1D40F}'..'\\u{1D413}')
         ;""");
    String expecting =
      """
        max type 1
        0:TOKEN_START -1
        1:RULE_START 0
        2:RULE_STOP 0
        3:BASIC 0
        4:BASIC 0
        rule 0:1 1
        mode 0:0
        0:119823..119827, 128065..128065, 128169..128170
        0->1 EPSILON 0,0,0
        1->3 EPSILON 0,0,0
        3->4 NOT_SET 0,0,0
        4->2 EPSILON 0,0,0
        0:0
        """;
    ATN atn = createATN(lg, true);
    String result = ATNSerializer.getDecoded(atn, Arrays.asList(lg.getRuleNames()), Arrays.asList(lg.getTokenNames()));
    assertEquals(expecting, result);
  }

  @Test
  public void testLexerWildcardWithMode() throws Exception {
    LexerGrammar lg = new LexerGrammar(
      """
        lexer grammar L;
        ID : 'a'..'z'+ ;
        mode CMT;\
        COMMENT : '*/' {skip(); popMode();} ;
        JUNK : . {more();} ;
        """);
    String expecting =
      """
        max type 3
        0:TOKEN_START -1
        1:TOKEN_START -1
        2:RULE_START 0
        3:RULE_STOP 0
        4:RULE_START 1
        5:RULE_STOP 1
        6:RULE_START 2
        7:RULE_STOP 2
        8:BASIC 0
        9:PLUS_BLOCK_START 0 10
        10:BLOCK_END 0
        11:PLUS_LOOP_BACK 0
        12:LOOP_END 0 11
        13:BASIC 1
        14:BASIC 1
        15:BASIC 1
        16:BASIC 1
        17:BASIC 1
        18:BASIC 2
        19:BASIC 2
        20:BASIC 2
        rule 0:2 1
        rule 1:4 2
        rule 2:6 3
        mode 0:0
        mode 1:1
        0->2 EPSILON 0,0,0
        1->4 EPSILON 0,0,0
        1->6 EPSILON 0,0,0
        2->9 EPSILON 0,0,0
        4->13 EPSILON 0,0,0
        6->18 EPSILON 0,0,0
        8->10 RANGE 97,122,0
        9->8 EPSILON 0,0,0
        10->11 EPSILON 0,0,0
        11->9 EPSILON 0,0,0
        11->12 EPSILON 0,0,0
        12->3 EPSILON 0,0,0
        13->14 ATOM 42,0,0
        14->15 ATOM 47,0,0
        15->16 EPSILON 0,0,0
        16->17 ACTION 1,0,0
        17->5 EPSILON 0,0,0
        18->19 WILDCARD 0,0,0
        19->20 ACTION 2,1,0
        20->7 EPSILON 0,0,0
        0:0
        1:1
        2:11
        """;
    ATN atn = createATN(lg, true);
    String result = ATNSerializer.getDecoded(atn, Arrays.asList(lg.getRuleNames()), Arrays.asList(lg.getTokenNames()));
    assertEquals(expecting, result);
  }

  @Test
  public void testLexerNotSetWithRange2() throws Exception {
    LexerGrammar lg = new LexerGrammar(
      """
        lexer grammar L;
        ID : ~('a'|'b') ~('e'|'p'..'t')
         ;""");
    String expecting =
      """
        max type 1
        0:TOKEN_START -1
        1:RULE_START 0
        2:RULE_STOP 0
        3:BASIC 0
        4:BASIC 0
        5:BASIC 0
        rule 0:1 1
        mode 0:0
        0:'a'..'b'
        1:'e'..'e', 'p'..'t'
        0->1 EPSILON 0,0,0
        1->3 EPSILON 0,0,0
        3->4 NOT_SET 0,0,0
        4->5 NOT_SET 1,0,0
        5->2 EPSILON 0,0,0
        0:0
        """;
    ATN atn = createATN(lg, true);
    String result = ATNSerializer.getDecoded(atn, Arrays.asList(lg.getRuleNames()), Arrays.asList(lg.getTokenNames()));
    assertEquals(expecting, result);
  }

  @Test
  public void testModeInLexer() throws Exception {
    LexerGrammar lg = new LexerGrammar(
      """
        lexer grammar L;
        A : 'a'
         ;
        B : 'b';
        mode M;
        C : 'c';
        D : 'd';
        """);
    String expecting =
      """
        max type 4
        0:TOKEN_START -1
        1:TOKEN_START -1
        2:RULE_START 0
        3:RULE_STOP 0
        4:RULE_START 1
        5:RULE_STOP 1
        6:RULE_START 2
        7:RULE_STOP 2
        8:RULE_START 3
        9:RULE_STOP 3
        10:BASIC 0
        11:BASIC 0
        12:BASIC 1
        13:BASIC 1
        14:BASIC 2
        15:BASIC 2
        16:BASIC 3
        17:BASIC 3
        rule 0:2 1
        rule 1:4 2
        rule 2:6 3
        rule 3:8 4
        mode 0:0
        mode 1:1
        0->2 EPSILON 0,0,0
        0->4 EPSILON 0,0,0
        1->6 EPSILON 0,0,0
        1->8 EPSILON 0,0,0
        2->10 EPSILON 0,0,0
        4->12 EPSILON 0,0,0
        6->14 EPSILON 0,0,0
        8->16 EPSILON 0,0,0
        10->11 ATOM 97,0,0
        11->3 EPSILON 0,0,0
        12->13 ATOM 98,0,0
        13->5 EPSILON 0,0,0
        14->15 ATOM 99,0,0
        15->7 EPSILON 0,0,0
        16->17 ATOM 100,0,0
        17->9 EPSILON 0,0,0
        0:0
        1:1
        """;
    ATN atn = createATN(lg, true);
    String result = ATNSerializer.getDecoded(atn, Arrays.asList(lg.getRuleNames()), Arrays.asList(lg.getTokenNames()));
    assertEquals(expecting, result);
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
    String expecting =
      """
        max type 3
        0:TOKEN_START -1
        1:TOKEN_START -1
        2:TOKEN_START -1
        3:RULE_START 0
        4:RULE_STOP 0
        5:RULE_START 1
        6:RULE_STOP 1
        7:RULE_START 2
        8:RULE_STOP 2
        9:BASIC 0
        10:BASIC 0
        11:BASIC 1
        12:BASIC 1
        13:BASIC 2
        14:BASIC 2
        rule 0:3 1
        rule 1:5 2
        rule 2:7 3
        mode 0:0
        mode 1:1
        mode 2:2
        0->3 EPSILON 0,0,0
        1->5 EPSILON 0,0,0
        2->7 EPSILON 0,0,0
        3->9 EPSILON 0,0,0
        5->11 EPSILON 0,0,0
        7->13 EPSILON 0,0,0
        9->10 ATOM 97,0,0
        10->4 EPSILON 0,0,0
        11->12 ATOM 98,0,0
        12->6 EPSILON 0,0,0
        13->14 ATOM 99,0,0
        14->8 EPSILON 0,0,0
        0:0
        1:1
        2:2
        """;
    ATN atn = createATN(lg, true);
    String result = ATNSerializer.getDecoded(atn, Arrays.asList(lg.getRuleNames()), Arrays.asList(lg.getTokenNames()));
    assertEquals(expecting, result);
  }

}
