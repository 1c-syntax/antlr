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

import org.antlr.v4.runtime.atn.ATNSerializer;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.antlr.v4.TestUtils.assertEquals;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * test runtime parse errors
 */
@SuppressWarnings("unused")
public class ParseErrorsTest extends AbstractBaseTest {
  @Test
  @Disabled("Переделать на ANTLR runtime/Generator")
  public void testTokenMismatch() {
    String grammar =
      """
        grammar T;
        a : 'a' 'b' ;""";
    String found = execParser("T.g4", grammar, "TParser", "TLexer", "a", "aa", false);
    String expecting = "line 1:1 mismatched input 'a' expecting 'b'\n";
    String result = stderrDuringParse;
    assertEquals(expecting, result);
  }

  @Test
  @Disabled("Переделать на ANTLR runtime/Generator")
  public void testSingleTokenDeletion() {
    String grammar =
      """
        grammar T;
        a : 'a' 'b' ;""";
    String found = execParser("T.g4", grammar, "TParser", "TLexer", "a", "aab", false);
    String expecting = "line 1:1 extraneous input 'a' expecting 'b'\n";
    String result = stderrDuringParse;
    assertEquals(expecting, result);
  }

  @Test
  @Disabled("Переделать на ANTLR runtime/Generator")
  public void testSingleTokenDeletionExpectingSet() {
    String grammar =
      """
        grammar T;
        a : 'a' ('b'|'c') ;""";
    String found = execParser("T.g4", grammar, "TParser", "TLexer", "a", "aab", false);
    String expecting = "line 1:1 extraneous input 'a' expecting {'b', 'c'}\n";
    String result = stderrDuringParse;
    assertEquals(expecting, result);
  }

  @Test
  @Disabled("Переделать на ANTLR runtime/Generator")
  public void testSingleTokenInsertion() {
    String grammar =
      """
        grammar T;
        a : 'a' 'b' 'c' ;""";
    String found = execParser("T.g4", grammar, "TParser", "TLexer", "a", "ac", false);
    String expecting = "line 1:1 missing 'b' at 'c'\n";
    String result = stderrDuringParse;
    assertEquals(expecting, result);
  }

  @Test
  @Disabled("Переделать на ANTLR runtime/Generator")
  public void testConjuringUpToken() {
    String grammar =
      """
        grammar T;
        a : 'a' x='b' {System.out.println("conjured="+$x);} 'c' ;""";
    String result = execParser("T.g4", grammar, "TParser", "TLexer", "a", "ac", false);
    String expecting = "conjured=[@-1,-1:-1='<missing 'b'>',<2>,1:1]\n";
    assertEquals(expecting, result);
  }

  @Test
  @Disabled("Переделать на ANTLR runtime/Generator")
  public void testSingleSetInsertion() {
    String grammar =
      """
        grammar T;
        a : 'a' ('b'|'c') 'd' ;""";
    String found = execParser("T.g4", grammar, "TParser", "TLexer", "a", "ad", false);
    String expecting = "line 1:1 missing {'b', 'c'} at 'd'\n";
    String result = stderrDuringParse;
    assertEquals(expecting, result);
  }

  @Test
  @Disabled("Переделать на ANTLR runtime/Generator")
  public void testConjuringUpTokenFromSet() {
    String grammar =
      """
        grammar T;
        a : 'a' x=('b'|'c') {System.out.println("conjured="+$x);} 'd' ;""";
    String result = execParser("T.g4", grammar, "TParser", "TLexer", "a", "ad", false);
    String expecting = "conjured=[@-1,-1:-1='<missing 'b'>',<2>,1:1]\n";
    assertEquals(expecting, result);
  }

  @Test
  @Disabled("Переделать на ANTLR runtime/Generator")
  public void testLL2() {
    String grammar =
      """
        grammar T;
        a : 'a' 'b'\
          | 'a' 'c'\
        ;
        q : 'e' ;
        """;
    String found = execParser("T.g4", grammar, "TParser", "TLexer", "a", "ae", false);
    String expecting = "line 1:1 no viable alternative at input 'ae'\n";
    String result = stderrDuringParse;
    assertEquals(expecting, result);
  }

  @Test
  @Disabled("Переделать на ANTLR runtime/Generator")
  public void testLL3() {
    String grammar =
      """
        grammar T;
        a : 'a' 'b'* 'c'\
          | 'a' 'b' 'd'\
          ;
        q : 'e' ;
        """;
    System.out.println(grammar);
    String found = execParser("T.g4", grammar, "TParser", "TLexer", "a", "abe", false);
    String expecting = "line 1:2 no viable alternative at input 'abe'\n";
    String result = stderrDuringParse;
    assertEquals(expecting, result);
  }

  @Test
  @Disabled("Переделать на ANTLR runtime/Generator")
  public void testLLStar() {
    String grammar =
      """
        grammar T;
        a : 'a'+ 'b'\
          | 'a'+ 'c'\
        ;
        q : 'e' ;
        """;
    String found = execParser("T.g4", grammar, "TParser", "TLexer", "a", "aaae", false);
    String expecting = "line 1:3 no viable alternative at input 'aaae'\n";
    String result = stderrDuringParse;
    assertEquals(expecting, result);
  }

  @Test
  @Disabled("Переделать на ANTLR runtime/Generator")
  public void testSingleTokenDeletionBeforeLoop() {
    String grammar =
      """
        grammar T;
        a : 'a' 'b'* EOF ;""";
    String found = execParser("T.g4", grammar, "TParser", "TLexer", "a", "aabc", false);
    String expecting = """
      line 1:1 extraneous input 'a' expecting {<EOF>, 'b'}
      line 1:3 token recognition error at: 'c'
      """;
    String result = stderrDuringParse;
    assertEquals(expecting, result);
  }

  @Test
  @Disabled("Переделать на ANTLR runtime/Generator")
  public void testMultiTokenDeletionBeforeLoop() {
    // can only delete 1 before loop
    String grammar =
      """
        grammar T;
        a : 'a' 'b'* 'c';""";
    String found = execParser("T.g4", grammar, "TParser", "TLexer", "a", "aacabc", false);
    String expecting =
      "line 1:1 extraneous input 'a' expecting {'b', 'c'}\n";
    String result = stderrDuringParse;
    assertEquals(expecting, result);
  }

  @Test
  @Disabled("Переделать на ANTLR runtime/Generator")
  public void testSingleTokenDeletionDuringLoop() {
    String grammar =
      """
        grammar T;
        a : 'a' 'b'* 'c' ;""";
    String found = execParser("T.g4", grammar, "TParser", "TLexer", "a", "ababbc", false);
    String expecting = "line 1:2 extraneous input 'a' expecting {'b', 'c'}\n";
    String result = stderrDuringParse;
    assertEquals(expecting, result);
  }

  @Test
  @Disabled("Переделать на ANTLR runtime/Generator")
  public void testMultiTokenDeletionDuringLoop() {
    String grammar =
      """
        grammar T;
        a : 'a' 'b'* 'c' ;""";
    String found = execParser("T.g4", grammar, "TParser", "TLexer", "a", "abaaababc", false);
    String expecting =
      """
        line 1:2 extraneous input 'a' expecting {'b', 'c'}
        line 1:6 extraneous input 'a' expecting {'b', 'c'}
        """;
    String result = stderrDuringParse;
    assertEquals(expecting, result);
  }

  // ------

  @Test
  @Disabled("Переделать на ANTLR runtime/Generator")
  public void testSingleTokenDeletionBeforeLoop2() {
    String grammar =
      """
        grammar T;
        a : 'a' ('b'|'z'{;})* EOF ;""";
    String found = execParser("T.g4", grammar, "TParser", "TLexer", "a", "aabc", false);
    String expecting = """
      line 1:1 extraneous input 'a' expecting {<EOF>, 'b', 'z'}
      line 1:3 token recognition error at: 'c'
      """;
    String result = stderrDuringParse;
    assertEquals(expecting, result);
  }

  @Test
  @Disabled("Переделать на ANTLR runtime/Generator")
  public void testMultiTokenDeletionBeforeLoop2() {
    // can only delete 1 before loop
    String grammar =
      """
        grammar T;
        a : 'a' ('b'|'z'{;})* 'c';""";
    String found = execParser("T.g4", grammar, "TParser", "TLexer", "a", "aacabc", false);
    String expecting =
      "line 1:1 extraneous input 'a' expecting {'b', 'z', 'c'}\n";
    String result = stderrDuringParse;
    assertEquals(expecting, result);
  }

  @Test
  @Disabled("Переделать на ANTLR runtime/Generator")
  public void testSingleTokenDeletionDuringLoop2() {
    String grammar =
      """
        grammar T;
        a : 'a' ('b'|'z'{;})* 'c' ;""";
    String found = execParser("T.g4", grammar, "TParser", "TLexer", "a", "ababbc", false);
    String expecting = "line 1:2 extraneous input 'a' expecting {'b', 'z', 'c'}\n";
    String result = stderrDuringParse;
    assertEquals(expecting, result);
  }

  @Test
  @Disabled("Переделать на ANTLR runtime/Generator")
  public void testMultiTokenDeletionDuringLoop2() {
    String grammar =
      """
        grammar T;
        a : 'a' ('b'|'z'{;})* 'c' ;""";
    String found = execParser("T.g4", grammar, "TParser", "TLexer", "a", "abaaababc", false);
    String expecting =
      """
        line 1:2 extraneous input 'a' expecting {'b', 'z', 'c'}
        line 1:6 extraneous input 'a' expecting {'b', 'z', 'c'}
        """;
    String result = stderrDuringParse;
    assertEquals(expecting, result);
  }

  @Test
  @Disabled("Переделать на ANTLR runtime/Generator")
  public void testLL1ErrorInfo() {
    String grammar =
      """
        grammar T;
        start : animal (AND acClass)? service EOF;
        animal : (DOG | CAT );
        service : (HARDWARE | SOFTWARE) ;
        AND : 'and';
        DOG : 'dog';
        CAT : 'cat';
        HARDWARE: 'hardware';
        SOFTWARE: 'software';
        WS : ' ' -> skip ;\
        acClass
        @init
        { System.out.println(getExpectedTokens().toString(tokenNames)); }
          : ;
        """;
    String result = execParser("T.g4", grammar, "TParser", "TLexer", "start", "dog and software", false);
    String expecting = "{'hardware', 'software'}\n";
    assertEquals(expecting, result);
  }

  /**
   * This is a regression test for #6 "NullPointerException in getMissingSymbol".
   * <a href="https://github.com/antlr/antlr4/issues/6">...</a>
   */
  @Test
  @Disabled("Переделать на ANTLR runtime/Generator")
  public void testInvalidEmptyInput() {
    String grammar =
      """
        grammar T;
        start : ID+;
        ID : [a-z]+;
        
        """;
    String result = execParser("T.g4", grammar, "TParser", "TLexer", "start", "", true);
    String expecting = "";
    assertEquals(expecting, result);
    assertEquals("line 1:0 mismatched input '<EOF>' expecting ID\n", this.stderrDuringParse);
  }

  /**
   * Regression test for "Getter for context is not a list when it should be".
   * <a href="https://github.com/antlr/antlr4/issues/19">...</a>
   */
  @Test
  @Disabled("Переделать на ANTLR runtime/Generator")
  public void testContextListGetters() {
    String grammar =
      """
        grammar T;
        @parser::members{
          void foo() {
            SContext s = null;
            List<? extends AContext> a = s.a();
            List<? extends BContext> b = s.b();
          }
        }
        s : (a | b)+;
        a : 'a' {System.out.print('a');};
        b : 'b' {System.out.print('b');};
        """;
    String result = execParser("T.g", grammar, "TParser", "TLexer", "s", "abab", true);
    String expecting = "abab\n";
    assertEquals(expecting, result);
    assertThat(this.stderrDuringParse).isNull();
  }

  /**
   * This is a regression test for #26 "an exception upon simple rule with double recursion in an alternative".
   * <a href="https://github.com/antlr/antlr4/issues/26">...</a>
   */
  void testDuplicatedLeftRecursiveCall(String input) {
    String grammar =
      """
        grammar T;
        start : expr EOF;
        expr : 'x'
             | expr expr
             ;
        
        """;
    String result = execParser("T.g4", grammar, "TParser", "TLexer", "start", input, true);
    assertEquals("", result);
    assertThat(this.stderrDuringParse).isNull();
  }


  @Test
  @Disabled("Переделать на ANTLR runtime/Generator")
  public void testDuplicatedLeftRecursiveCall1() {
    testDuplicatedLeftRecursiveCall("x");
  }

  @Test
  @Disabled("Переделать на ANTLR runtime/Generator")
  public void testDuplicatedLeftRecursiveCall2() {
    testDuplicatedLeftRecursiveCall("xx");
  }

  @Test
  @Disabled("Переделать на ANTLR runtime/Generator")
  public void testDuplicatedLeftRecursiveCall3() {
    testDuplicatedLeftRecursiveCall("xxx");
  }

  @Test
  @Disabled("Переделать на ANTLR runtime/Generator")
  public void testDuplicatedLeftRecursiveCall4() {
    testDuplicatedLeftRecursiveCall("xxxx");
  }

  /**
   * Regression test for "Ambiguity at k=1 prevents full context parsing".
   * <a href="https://github.com/antlr/antlr4/issues/44">...</a>
   */
  @Test
  @Disabled("Переделать на ANTLR runtime/Generator")
  public void testConflictingAltAnalysis() {
    String grammar =
      """
        grammar T;
        ss : s s EOF;
        s : | x;
        x : 'a' 'b';
        """;
    String result = execParser("T.g", grammar, "TParser", "TLexer", "ss", "abab", true);
    String expecting = "";
    assertEquals(expecting, result);
    assertEquals(
      """
        line 1:4 reportAttemptingFullContext d=0 (s), input='ab'
        line 1:2 reportContextSensitivity d=0 (s), input='a'
        """,
      this.stderrDuringParse);
  }

  /**
   * This is a regression test for #45 "NullPointerException in ATNConfig.hashCode".
   * <a href="https://github.com/antlr/antlr4/issues/45">...</a>
   * <p/>
   * The original cause of this issue was an error in the tool's ATN state optimization,
   * which is now detected early in {@link ATNSerializer} by ensuring that all
   * serialized transitions point to states which were not removed.
   */
  @Test
  @Disabled("Переделать на ANTLR runtime/Generator")
  public void testInvalidATNStateRemoval() {
    String grammar =
      """
        grammar T;
        start : ID ':' expr;
        expr : primary expr? {} | expr '->' ID;
        primary : ID;
        ID : [a-z]+;
        
        """;
    String result = execParser("T.g4", grammar, "TParser", "TLexer", "start", "x:x", true);
    String expecting = "";
    assertEquals(expecting, result);
    assertThat(this.stderrDuringParse).isNull();
  }

  @Test
  @Disabled("Переделать на ANTLR runtime/Generator")
  public void testNoViableAltAvoidance() {
    // "a." matches 'a' to rule e but then realizes '.' won't match.
    // previously would cause noviablealt. now prediction pretends to
    // have "a' predict 2nd alt of e. Will get syntax error later so
    // let it get farther.
    String grammar =
      """
        grammar T;
        s : e '!' ;
        e : 'a' 'b'
          | 'a'
          ;
        DOT : '.' ;
        WS : [ \\t\\r\\n]+ -> skip;
        """;
    String found = execParser("T.g4", grammar, "TParser", "TLexer", "s", "a.", false);
    String expecting =
      "line 1:1 mismatched input '.' expecting '!'\n";
    String result = stderrDuringParse;
    assertEquals(expecting, result);
  }

  @Test
  @Disabled("Переделать на ANTLR runtime/Generator")
  public void testSingleTokenDeletionConsumption() {
    String grammar =
      """
        grammar T;
        set: ('b'|'c') ;
        a: 'a' set 'd' {System.out.println($set.stop);} ;""";
    String found = execParser("T.g4", grammar, "TParser", "TLexer", "a", "aabd", false);
    assertEquals("[@2,2:2='b',<1>,1:2]\n", found);
    assertEquals("line 1:1 extraneous input 'a' expecting {'b', 'c'}\n", this.stderrDuringParse);
  }

  @Test
  @Disabled("Переделать на ANTLR runtime/Generator")
  public void testSingleSetInsertionConsumption() {
    String grammar =
      """
        grammar T;
        set: ('b'|'c') ;
        a: 'a' set 'd' {System.out.println($set.stop);} ;""";
    String found = execParser("T.g4", grammar, "TParser", "TLexer", "a", "ad", false);
    assertEquals("[@0,0:0='a',<3>,1:0]\n", found);
    assertEquals("line 1:1 missing {'b', 'c'} at 'd'\n", this.stderrDuringParse);
  }
}
