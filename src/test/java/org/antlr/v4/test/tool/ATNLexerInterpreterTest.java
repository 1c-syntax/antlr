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

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.atn.ATN;
import org.antlr.v4.runtime.atn.ATNState;
import org.antlr.v4.runtime.misc.Utils;
import org.antlr.v4.tool.DOTGenerator;
import org.antlr.v4.tool.LexerGrammar;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.antlr.v4.TestUtils.assertEquals;

/**
 * Lexer rules are little quirky when it comes to wildcards. Problem
 * stems from the fact that we want the longest match to win among
 * several rules and even within a rule. However, that conflicts
 * with the notion of non-greedy, which by definition tries to match
 * the fewest possible. During ATN construction, non-greedy loops
 * have their entry and exit branches reversed so that the ATN
 * simulator will see the exit branch 1st, giving it a priority. The
 * 1st path to the stop state kills any other paths for that rule
 * that begin with the wildcard. In general, this does everything we
 * want, but occasionally there are some quirks as you'll see from
 * the tests below.
 */
public class ATNLexerInterpreterTest extends AbstractBaseTest {

  @Test
  public void testLexerTwoRules() throws Exception {
    LexerGrammar lg = new LexerGrammar(
      """
        lexer grammar L;
        A : 'a' ;
        B : 'b' ;
        """);
    String expecting = "A, B, A, B, EOF";
    checkLexerMatches(lg, "abab", expecting);
  }

  @Test
  public void testShortLongRule() throws Exception {
    // this alt is preferred since there are no non-greedy configs
    LexerGrammar lg = new LexerGrammar(
      """
        lexer grammar L;
        A : 'xy'
          | 'xyz'
          ;
        Z : 'z'
          ;
        """);
    checkLexerMatches(lg, "xy", "A, EOF");
    checkLexerMatches(lg, "xyz", "A, EOF");
  }

  @Test
  public void testShortLongRule2() throws Exception {
    // make sure nongreedy mech cut off doesn't kill this alt
    LexerGrammar lg = new LexerGrammar(
      """
        lexer grammar L;
        A : 'xyz'
          | 'xy'
          ;
        """);
    checkLexerMatches(lg, "xy", "A, EOF");
    checkLexerMatches(lg, "xyz", "A, EOF");
  }

  @Test
  public void testWildOnEndFirstAlt() throws Exception {
    // should pursue '.' since xyz hits stop first, before 2nd alt
    LexerGrammar lg = new LexerGrammar(
      """
        lexer grammar L;
        A : 'xy' .
          | 'xy'
          ;
        Z : 'z'
          ;
        """);
    checkLexerMatches(lg, "xy", "A, EOF");
    checkLexerMatches(lg, "xyz", "A, EOF");
  }

  @Test
  public void testWildOnEndLastAlt() throws Exception {
    // this alt is preferred since there are no non-greedy configs
    LexerGrammar lg = new LexerGrammar(
      """
        lexer grammar L;
        A : 'xy'
          | 'xy' .
          ;
        Z : 'z'
          ;
        """);
    checkLexerMatches(lg, "xy", "A, EOF");
    checkLexerMatches(lg, "xyz", "A, EOF");
  }

  @Test
  public void testWildcardNonQuirkWhenSplitBetweenTwoRules() throws Exception {
    LexerGrammar lg = new LexerGrammar(
      """
        lexer grammar L;
        A : 'xy' ;
        B : 'xy' . 'z' ;
        """);
    checkLexerMatches(lg, "xy", "A, EOF");
    checkLexerMatches(lg, "xyqz", "B, EOF");
  }

  @Test
  public void testLexerLoops() throws Exception {
    LexerGrammar lg = new LexerGrammar(
      """
        lexer grammar L;
        INT : '0'..'9'+ ;
        ID : 'a'..'z'+ ;
        """);
    String expecting = "ID, INT, ID, INT, EOF";
    checkLexerMatches(lg, "a34bde3", expecting);
  }

  @Test
  public void testLexerNotSet() throws Exception {
    LexerGrammar lg = new LexerGrammar(
      """
        lexer grammar L;
        ID : ~('a'|'b')
         ;""");
    String expecting = "ID, EOF";
    checkLexerMatches(lg, "c", expecting);
  }

  @Test
  public void testLexerSetUnicodeBMP() throws Exception {
    LexerGrammar lg = new LexerGrammar(
      """
        lexer grammar L;
        ID : ('\u611B'|'\u611C')
         ;""");
    String expecting = "ID, EOF";
    checkLexerMatches(lg, "\u611B", expecting);
  }

  @Test
  public void testLexerNotSetUnicodeBMP() throws Exception {
    LexerGrammar lg = new LexerGrammar(
      """
        lexer grammar L;
        ID : ~('\u611B'|'\u611C')
         ;""");
    String expecting = "ID, EOF";
    checkLexerMatches(lg, "\u611D", expecting);
  }

  @Test
  public void testLexerNotSetUnicodeBMPMatchesSMP() throws Exception {
    LexerGrammar lg = new LexerGrammar(
      """
        lexer grammar L;
        ID : ~('\u611B'|'\u611C')
         ;""");
    String expecting = "ID, EOF";
    checkLexerMatches(lg, new StringBuilder().appendCodePoint(0x1F4A9).toString(), expecting);
  }

  @Test
  public void testLexerSetUnicodeSMP() throws Exception {
    LexerGrammar lg = new LexerGrammar(
      """
        lexer grammar L;
        ID : ('\\u{1F4A9}'|'\\u{1F4AA}')
         ;""");
    String expecting = "ID, EOF";
    checkLexerMatches(lg, new StringBuilder().appendCodePoint(0x1F4A9).toString(), expecting);
  }

  @Test
  public void testLexerNotBMPSetMatchesUnicodeSMP() throws Exception {
    LexerGrammar lg = new LexerGrammar(
      """
        lexer grammar L;
        ID : ~('a'|'b')
         ;""");
    String expecting = "ID, EOF";
    checkLexerMatches(lg, new StringBuilder().appendCodePoint(0x1F4A9).toString(), expecting);
  }

  @Test
  public void testLexerNotBMPSetMatchesBMP() throws Exception {
    LexerGrammar lg = new LexerGrammar(
      """
        lexer grammar L;
        ID : ~('a'|'b')
         ;""");
    String expecting = "ID, EOF";
    checkLexerMatches(lg, "\u611B", expecting);
  }

  @Test
  public void testLexerNotBMPSetMatchesSMP() throws Exception {
    LexerGrammar lg = new LexerGrammar(
      """
        lexer grammar L;
        ID : ~('a'|'b')
         ;""");
    String expecting = "ID, EOF";
    checkLexerMatches(lg, new StringBuilder().appendCodePoint(0x1F4A9).toString(), expecting);
  }

  @Test
  public void testLexerNotSMPSetMatchesBMP() throws Exception {
    LexerGrammar lg = new LexerGrammar(
      """
        lexer grammar L;
        ID : ~('\\u{1F4A9}'|'\\u{1F4AA}')
         ;""");
    String expecting = "ID, EOF";
    checkLexerMatches(lg, "\u611B", expecting);
  }

  @Test
  public void testLexerNotSMPSetMatchesSMP() throws Exception {
    LexerGrammar lg = new LexerGrammar(
      """
        lexer grammar L;
        ID : ~('\\u{1F4A9}'|'\\u{1F4AA}')
         ;""");
    String expecting = "ID, EOF";
    checkLexerMatches(lg, new StringBuilder().appendCodePoint(0x1D7C0).toString(), expecting);
  }

  @Test
  public void testLexerRangeUnicodeSMP() throws Exception {
    LexerGrammar lg = new LexerGrammar(
      """
        lexer grammar L;
        ID : ('\\u{1F4A9}'..'\\u{1F4B0}')
         ;""");
    String expecting = "ID, EOF";
    checkLexerMatches(lg, new StringBuilder().appendCodePoint(0x1F4AF).toString(), expecting);
  }

  @Test
  public void testLexerRangeUnicodeBMPToSMP() throws Exception {
    LexerGrammar lg = new LexerGrammar(
      """
        lexer grammar L;
        ID : ('\\u611B'..'\\u{1F4B0}')
         ;""");
    String expecting = "ID, EOF";
    checkLexerMatches(lg, new StringBuilder().appendCodePoint(0x12001).toString(), expecting);
  }

  @Test
  public void testLexerKeywordIDAmbiguity() throws Exception {
    LexerGrammar lg = new LexerGrammar(
      """
        lexer grammar L;
        KEND : 'end' ;
        ID : 'a'..'z'+ ;
        WS : (' '|'\\n')+ ;""");
    var expecting = "KEND, EOF";
    checkLexerMatches(lg, "end", expecting);
    expecting = "ID, EOF";
    checkLexerMatches(lg, "ending", expecting);
    expecting = "ID, WS, KEND, WS, ID, EOF";
    checkLexerMatches(lg, "a end bcd", expecting);
  }

  @Test
  public void testLexerRuleRef() throws Exception {
    LexerGrammar lg = new LexerGrammar(
      """
        lexer grammar L;
        INT : DIGIT+ ;
        fragment DIGIT : '0'..'9' ;
        WS : (' '|'\\n')+ ;""");
    String expecting = "INT, WS, INT, EOF";
    checkLexerMatches(lg, "32 99", expecting);
  }

  @Test
  public void testRecursiveLexerRuleRef() throws Exception {
    LexerGrammar lg = new LexerGrammar(
      """
        lexer grammar L;
        CMT : '/*' (CMT | ~'*')+ '*/' ;
        WS : (' '|'\\n')+ ;""");
    String expecting = "CMT, WS, CMT, EOF";
    checkLexerMatches(lg, "/* ick */\n/* /*nested*/ */", expecting);
  }

  @Test
  public void testRecursiveLexerRuleRefWithWildcard() throws Exception {
    LexerGrammar lg = new LexerGrammar(
      """
        lexer grammar L;
        CMT : '/*' (CMT | .)*? '*/' ;
        WS : (' '|'\\n')+ ;""");

    String expecting = "CMT, WS, CMT, WS, EOF";
    checkLexerMatches(lg,
      """
        /* ick */
        /* /* */
        /* /*nested*/ */
        """,
      expecting);
  }

  @Test
  public void testLexerWildcardGreedyLoopByDefault() throws Exception {
    LexerGrammar lg = new LexerGrammar(
      """
        lexer grammar L;
        CMT : '//' .* '\\n' ;
        """);
    String expecting = "CMT, EOF";
    checkLexerMatches(lg, "//x\n//y\n", expecting);
  }

  @Test
  public void testLexerWildcardLoopExplicitNonGreedy() throws Exception {
    LexerGrammar lg = new LexerGrammar(
      """
        lexer grammar L;
        CMT : '//' .*? '\\n' ;
        """);
    String expecting = "CMT, CMT, EOF";
    checkLexerMatches(lg, "//x\n//y\n", expecting);
  }

  @Test
  public void testLexerEscapeInString() throws Exception {
    LexerGrammar lg = new LexerGrammar(
      """
        lexer grammar L;
        STR : '[' ('~' ']' | .)* ']' ;
        """);
    checkLexerMatches(lg, "[a~]b]", "STR, EOF");
    checkLexerMatches(lg, "[a]", "STR, EOF");
  }

  @Test
  public void testLexerWildcardGreedyPlusLoopByDefault() throws Exception {
    LexerGrammar lg = new LexerGrammar(
      """
        lexer grammar L;
        CMT : '//' .+ '\\n' ;
        """);
    String expecting = "CMT, EOF";
    checkLexerMatches(lg, "//x\n//y\n", expecting);
  }

  @Test
  public void testLexerWildcardExplicitNonGreedyPlusLoop() throws Exception {
    LexerGrammar lg = new LexerGrammar(
      """
        lexer grammar L;
        CMT : '//' .+? '\\n' ;
        """);
    String expecting = "CMT, CMT, EOF";
    checkLexerMatches(lg, "//x\n//y\n", expecting);
  }

  // does not fail since ('*/')? can't match and have rule succeed
  @Test
  public void testLexerGreedyOptionalShouldWorkAsWeExpect() throws Exception {
    LexerGrammar lg = new LexerGrammar(
      """
        lexer grammar L;
        CMT : '/*' ('*/')? '*/' ;
        """);
    String expecting = "CMT, EOF";
    checkLexerMatches(lg, "/**/", expecting);
  }

  @Test
  public void testGreedyBetweenRules() throws Exception {
    LexerGrammar lg = new LexerGrammar(
      """
        lexer grammar L;
        A : '<a>' ;
        B : '<' .+ '>' ;
        """);
    String expecting = "B, EOF";
    checkLexerMatches(lg, "<a><x>", expecting);
  }

  @Test
  public void testNonGreedyBetweenRules() throws Exception {
    LexerGrammar lg = new LexerGrammar(
      """
        lexer grammar L;
        A : '<a>' ;
        B : '<' .+? '>' ;
        """);
    String expecting = "A, B, EOF";
    checkLexerMatches(lg, "<a><x>", expecting);
  }

  @Test
  public void testEOFAtEndOfLineComment() throws Exception {
    LexerGrammar lg = new LexerGrammar(
      """
        lexer grammar L;
        CMT : '//' ~('\\n')* ;
        """);
    String expecting = "CMT, EOF";
    checkLexerMatches(lg, "//x", expecting);
  }

  @Test
  public void testEOFAtEndOfLineComment2() throws Exception {
    LexerGrammar lg = new LexerGrammar(
      """
        lexer grammar L;
        CMT : '//' ~('\\n'|'\\r')* ;
        """);
    String expecting = "CMT, EOF";
    checkLexerMatches(lg, "//x", expecting);
  }

  /**
   * only positive sets like (EOF|'\n') can match EOF and not in wildcard or ~foo sets
   * EOF matches but does not advance cursor.
   */
  @Test
  public void testEOFInSetAtEndOfLineComment() throws Exception {
    LexerGrammar lg = new LexerGrammar(
      """
        lexer grammar L;
        CMT : '//' .* (EOF|'\\n') ;
        """);
    String expecting = "CMT, EOF";
    checkLexerMatches(lg, "//", expecting);
  }

  @Test
  public void testEOFSuffixInSecondRule() throws Exception {
    // shorter than 'a' EOF, despite EOF being 0 width
    LexerGrammar lg = new LexerGrammar(
      """
        lexer grammar L;
        A : 'a' ;
        B : 'a' EOF ;
        """);
    String expecting = "B, EOF";
    checkLexerMatches(lg, "a", expecting);
  }

  @Test
  public void testEOFSuffixInFirstRule() throws Exception {
    LexerGrammar lg = new LexerGrammar(
      """
        lexer grammar L;
        A : 'a' EOF ;
        B : 'a';
        """);
    String expecting = "A, EOF";
    checkLexerMatches(lg, "a", expecting);
  }

  @Test
  public void testEOFByItself() throws Exception {
    LexerGrammar lg = new LexerGrammar(
      """
        lexer grammar L;
        DONE : EOF ;
        A : 'a';
        """);
    String expecting = "A, DONE, EOF";
    checkLexerMatches(lg, "a", expecting);
  }

  protected void checkLexerMatches(LexerGrammar lg, String inputString, String expecting) {
    ATN atn = createATN(lg, true);
    CharStream input = CharStreams.fromString(inputString);
    ATNState startState = atn.modeNameToStartState.get("DEFAULT_MODE");
    DOTGenerator dot = new DOTGenerator(lg);
    System.out.println(dot.getDOT(startState, true));

    List<String> tokenTypes = getTokenTypes(lg, atn, input);

    String result = Utils.join(tokenTypes.iterator(), ", ");
    System.out.println(tokenTypes);
    assertEquals(expecting, result);
  }

}
