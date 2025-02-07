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


import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.antlr.v4.TestUtils.assertEquals;
import static org.assertj.core.api.Assertions.assertThat;

public class LexerErrorsTest extends AbstractBaseTest {
  // TEST DETECTION
  @Test
  @Disabled("Переделать на ANTLR runtime/Generator")
  public void testInvalidCharAtStart() {
    String grammar =
      """
        lexer grammar L;
        A : 'a' 'b' ;
        """;
    String tokens = execLexer("L.g4", grammar, "L", "x");
    String expectingTokens =
      "[@0,1:0='<EOF>',<-1>,1:1]\n";
    assertEquals(expectingTokens, tokens);
    String expectingError = "line 1:0 token recognition error at: 'x'\n";
    String error = stderrDuringParse;
    assertEquals(expectingError, error);
  }

  @Test
  @Disabled("Переделать на ANTLR runtime/Generator")
  public void testStringsEmbeddedInActions() {
    String grammar =
      """
        lexer grammar Actions;
        ACTION2 : '[' (STRING | ~'"')*? ']';
        STRING : '"' ('\\\\"' | .)*? '"';
        WS : [ \\t\\r\\n]+ -> skip;
        """;
    String tokens = execLexer("Actions.g4", grammar, "Actions", "[\"foo\"]");
    String expectingTokens =
      """
        [@0,0:6='["foo"]',<1>,1:0]
        [@1,7:6='<EOF>',<-1>,1:7]
        """;
    assertEquals(expectingTokens, tokens);
    assertThat(stderrDuringParse).isNull();

    tokens = execLexer("Actions.g4", grammar, "Actions", "[\"foo]");
    expectingTokens =
      "[@0,6:5='<EOF>',<-1>,1:6]\n";
    assertEquals(expectingTokens, tokens);
    assertEquals("line 1:0 token recognition error at: '[\"foo]'\n", stderrDuringParse);
  }

  @Test
  @Disabled("Переделать на ANTLR runtime/Generator")
  public void testEnforcedGreedyNestedBrances() {
    String grammar =
      """
        lexer grammar R;
        ACTION : '{' (ACTION | ~[{}])* '}';
        WS : [ \\r\\n\\t]+ -> skip;
        """;
    String tokens = execLexer("R.g4", grammar, "R", "{ { } }");
    String expectingTokens =
      """
        [@0,0:6='{ { } }',<1>,1:0]
        [@1,7:6='<EOF>',<-1>,1:7]
        """;
    assertEquals(expectingTokens, tokens);
    assertEquals(null, stderrDuringParse);

    tokens = execLexer("R.g4", grammar, "R", "{ { }");
    expectingTokens =
      "[@0,5:4='<EOF>',<-1>,1:5]\n";
    assertEquals(expectingTokens, tokens);
    assertEquals("line 1:0 token recognition error at: '{ { }'\n", stderrDuringParse);
  }

  @Test
  @Disabled("Переделать на ANTLR runtime/Generator")
  public void testInvalidCharAtStartAfterDFACache() {
    String grammar =
      """
        lexer grammar L;
        A : 'a' 'b' ;
        """;
    String tokens = execLexer("L.g4", grammar, "L", "abx");
    String expectingTokens =
      """
        [@0,0:1='ab',<1>,1:0]
        [@1,3:2='<EOF>',<-1>,1:3]
        """;
    assertEquals(expectingTokens, tokens);
    String expectingError = "line 1:2 token recognition error at: 'x'\n";
    String error = stderrDuringParse;
    assertEquals(expectingError, error);
  }

  @Test
  @Disabled("Переделать на ANTLR runtime/Generator")
  public void testInvalidCharInToken() {
    String grammar =
      """
        lexer grammar L;
        A : 'a' 'b' ;
        """;
    String tokens = execLexer("L.g4", grammar, "L", "ax");
    String expectingTokens =
      "[@0,2:1='<EOF>',<-1>,1:2]\n";
    assertEquals(expectingTokens, tokens);
    String expectingError = "line 1:0 token recognition error at: 'ax'\n";
    String error = stderrDuringParse;
    assertEquals(expectingError, error);
  }

  @Test
  @Disabled("Переделать на ANTLR runtime/Generator")
  public void testInvalidCharInTokenAfterDFACache() {
    String grammar =
      """
        lexer grammar L;
        A : 'a' 'b' ;
        """;
    String tokens = execLexer("L.g4", grammar, "L", "abax");
    String expectingTokens =
      """
        [@0,0:1='ab',<1>,1:0]
        [@1,4:3='<EOF>',<-1>,1:4]
        """;
    assertEquals(expectingTokens, tokens);
    String expectingError = "line 1:2 token recognition error at: 'ax'\n";
    String error = stderrDuringParse;
    assertEquals(expectingError, error);
  }

  @Test
  @Disabled("Переделать на ANTLR runtime/Generator")
  public void testDFAToATNThatFailsBackToDFA() {
    String grammar =
      """
        lexer grammar L;
        A : 'ab' ;
        B : 'abc' ;
        """;
    // The first ab caches the DFA then abx goes through the DFA but
    // into the ATN for the x, which fails. Must go back into DFA
    // and return to previous dfa accept state
    String tokens = execLexer("L.g4", grammar, "L", "ababx");
    String expectingTokens =
      """
        [@0,0:1='ab',<1>,1:0]
        [@1,2:3='ab',<1>,1:2]
        [@2,5:4='<EOF>',<-1>,1:5]
        """;
    assertEquals(expectingTokens, tokens);
    String expectingError = "line 1:4 token recognition error at: 'x'\n";
    String error = stderrDuringParse;
    assertEquals(expectingError, error);
  }

  @Test
  @Disabled("Переделать на ANTLR runtime/Generator")
  public void testDFAToATNThatMatchesThenFailsInATN() {
    String grammar =
      """
        lexer grammar L;
        A : 'ab' ;
        B : 'abc' ;
        C : 'abcd' ;
        """;
    // The first ab caches the DFA then abx goes through the DFA but
    // into the ATN for the c.  It marks that hasn't except state
    // and then keeps going in the ATN. It fails on the x, but
    // uses the previous accepted in the ATN not DFA
    String tokens = execLexer("L.g4", grammar, "L", "ababcx");
    String expectingTokens =
      """
        [@0,0:1='ab',<1>,1:0]
        [@1,2:4='abc',<2>,1:2]
        [@2,6:5='<EOF>',<-1>,1:6]
        """;
    assertEquals(expectingTokens, tokens);
    String expectingError = "line 1:5 token recognition error at: 'x'\n";
    String error = stderrDuringParse;
    assertEquals(expectingError, error);
  }

  @Test
  @Disabled("Переделать на ANTLR runtime/Generator")
  public void testErrorInMiddle() {
    String grammar =
      """
        lexer grammar L;
        A : 'abc' ;
        """;
    String tokens = execLexer("L.g4", grammar, "L", "abx");
    String expectingTokens =
      "[@0,3:2='<EOF>',<-1>,1:3]\n";
    assertEquals(expectingTokens, tokens);
    String expectingError = "line 1:0 token recognition error at: 'abx'\n";
    String error = stderrDuringParse;
    assertEquals(expectingError, error);
  }

  // TEST RECOVERY

  /**
   * This is a regression test for #45 "NullPointerException in LexerATNSimulator.execDFA".
   * <a href="https://github.com/antlr/antlr4/issues/46">...</a>
   */
  @Test
  @Disabled("Переделать на ANTLR runtime/Generator")
  public void testLexerExecDFA() {
    String grammar =
      """
        grammar T;
        start : ID ':' expr;
        expr : primary expr? {} | expr '->' ID;
        primary : ID;
        ID : [a-z]+;
        
        """;
    String result = execLexer("T.g4", grammar, "TLexer", "x : x", false);
    String expecting =
      """
        [@0,0:0='x',<3>,1:0]
        [@1,2:2=':',<1>,1:2]
        [@2,4:4='x',<3>,1:4]
        [@3,5:4='<EOF>',<-1>,1:5]
        """;
    assertEquals(expecting, result);
    assertEquals("""
        line 1:1 token recognition error at: ' '
        line 1:3 token recognition error at: ' '
        """,
      this.stderrDuringParse);
  }

}
