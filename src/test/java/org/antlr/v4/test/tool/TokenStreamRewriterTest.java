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
import org.antlr.v4.runtime.TokenStreamRewriter;
import org.antlr.v4.runtime.misc.Interval;
import org.antlr.v4.tool.LexerGrammar;
import org.junit.jupiter.api.Test;

import static org.antlr.v4.TestUtils.assertEquals;
import static org.assertj.core.api.Assertions.assertThat;

public class TokenStreamRewriterTest extends AbstractBaseTest {

  @Test
  public void testInsertBeforeIndex0() throws Exception {
    LexerGrammar g = new LexerGrammar(
      """
        lexer grammar T;
        A : 'a';
        B : 'b';
        C : 'c';
        """);
    LexerInterpreter lexEngine = g.createLexerInterpreter(CharStreams.fromString("abc"));
    CommonTokenStream stream = new CommonTokenStream(lexEngine);
    stream.fill();
    TokenStreamRewriter tokens = new TokenStreamRewriter(stream);
    tokens.insertBefore(0, "0");
    String result = tokens.getText();
    String expecting = "0abc";
    assertEquals(expecting, result);
  }

  @Test
  public void testInsertAfterLastIndex() throws Exception {
    LexerGrammar g = new LexerGrammar(
      """
        lexer grammar T;
        A : 'a';
        B : 'b';
        C : 'c';
        """);
    String input = "abc";
    LexerInterpreter lexEngine = g.createLexerInterpreter(CharStreams.fromString(input));
    CommonTokenStream stream = new CommonTokenStream(lexEngine);
    stream.fill();
    TokenStreamRewriter tokens = new TokenStreamRewriter(stream);
    tokens.insertAfter(2, "x");
    String result = tokens.getText();
    String expecting = "abcx";
    assertEquals(expecting, result);
  }

  @Test
  public void test2InsertBeforeAfterMiddleIndex() throws Exception {
    LexerGrammar g = new LexerGrammar(
      """
        lexer grammar T;
        A : 'a';
        B : 'b';
        C : 'c';
        """);
    String input = "abc";
    LexerInterpreter lexEngine = g.createLexerInterpreter(CharStreams.fromString(input));
    CommonTokenStream stream = new CommonTokenStream(lexEngine);
    stream.fill();
    TokenStreamRewriter tokens = new TokenStreamRewriter(stream);
    tokens.insertBefore(1, "x");
    tokens.insertAfter(1, "x");
    String result = tokens.getText();
    String expecting = "axbxc";
    assertEquals(expecting, result);
  }

  @Test
  public void testReplaceIndex0() throws Exception {
    LexerGrammar g = new LexerGrammar(
      """
        lexer grammar T;
        A : 'a';
        B : 'b';
        C : 'c';
        """);
    String input = "abc";
    LexerInterpreter lexEngine = g.createLexerInterpreter(CharStreams.fromString(input));
    CommonTokenStream stream = new CommonTokenStream(lexEngine);
    stream.fill();
    TokenStreamRewriter tokens = new TokenStreamRewriter(stream);
    tokens.replace(0, "x");
    String result = tokens.getText();
    String expecting = "xbc";
    assertEquals(expecting, result);
  }

  @Test
  public void testReplaceLastIndex() throws Exception {
    LexerGrammar g = new LexerGrammar(
      """
        lexer grammar T;
        A : 'a';
        B : 'b';
        C : 'c';
        """);
    String input = "abc";
    LexerInterpreter lexEngine = g.createLexerInterpreter(CharStreams.fromString(input));
    CommonTokenStream stream = new CommonTokenStream(lexEngine);
    stream.fill();
    TokenStreamRewriter tokens = new TokenStreamRewriter(stream);
    tokens.replace(2, "x");
    String result = tokens.getText();
    String expecting = "abx";
    assertEquals(expecting, result);
  }

  @Test
  public void testReplaceMiddleIndex() throws Exception {
    LexerGrammar g = new LexerGrammar(
      """
        lexer grammar T;
        A : 'a';
        B : 'b';
        C : 'c';
        """);
    String input = "abc";
    LexerInterpreter lexEngine = g.createLexerInterpreter(CharStreams.fromString(input));
    CommonTokenStream stream = new CommonTokenStream(lexEngine);
    stream.fill();
    TokenStreamRewriter tokens = new TokenStreamRewriter(stream);
    tokens.replace(1, "x");
    String result = tokens.getText();
    String expecting = "axc";
    assertEquals(expecting, result);
  }

  @Test
  public void testToStringStartStop() throws Exception {
    LexerGrammar g = new LexerGrammar(
      """
        lexer grammar T;
        ID : 'a'..'z'+;
        INT : '0'..'9'+;
        SEMI : ';';
        MUL : '*';
        ASSIGN : '=';
        WS : ' '+;
        """);
    // Tokens: 0123456789
    // Input:  x = 3 * 0;
    String input = "x = 3 * 0;";
    LexerInterpreter lexEngine = g.createLexerInterpreter(CharStreams.fromString(input));
    CommonTokenStream stream = new CommonTokenStream(lexEngine);
    stream.fill();
    TokenStreamRewriter tokens = new TokenStreamRewriter(stream);
    tokens.replace(4, 8, "0");
    stream.fill();
// replace 3 * 0 with 0

    String result = tokens.getTokenStream().getText();
    String expecting = "x = 3 * 0;";
    assertEquals(expecting, result);

    result = tokens.getText();
    expecting = "x = 0;";
    assertEquals(expecting, result);

    result = tokens.getText(Interval.of(0, 9));
    expecting = "x = 0;";
    assertEquals(expecting, result);

    result = tokens.getText(Interval.of(4, 8));
    expecting = "0";
    assertEquals(expecting, result);
  }

  @Test
  public void testToStringStartStop2() throws Exception {
    LexerGrammar g = new LexerGrammar(
      """
        lexer grammar T;
        ID : 'a'..'z'+;
        INT : '0'..'9'+;
        SEMI : ';';
        ASSIGN : '=';
        PLUS : '+';
        MULT : '*';
        WS : ' '+;
        """);
    // Tokens: 012345678901234567
    // Input:  x = 3 * 0 + 2 * 0;
    String input = "x = 3 * 0 + 2 * 0;";
    LexerInterpreter lexEngine = g.createLexerInterpreter(CharStreams.fromString(input));
    CommonTokenStream stream = new CommonTokenStream(lexEngine);
    stream.fill();
    TokenStreamRewriter tokens = new TokenStreamRewriter(stream);

    String result = tokens.getTokenStream().getText();
    String expecting = "x = 3 * 0 + 2 * 0;";
    assertEquals(expecting, result);

    tokens.replace(4, 8, "0");
    stream.fill();
// replace 3 * 0 with 0
    result = tokens.getText();
    expecting = "x = 0 + 2 * 0;";
    assertEquals(expecting, result);

    result = tokens.getText(Interval.of(0, 17));
    expecting = "x = 0 + 2 * 0;";
    assertEquals(expecting, result);

    result = tokens.getText(Interval.of(4, 8));
    expecting = "0";
    assertEquals(expecting, result);

    result = tokens.getText(Interval.of(0, 8));
    expecting = "x = 0";
    assertEquals(expecting, result);

    result = tokens.getText(Interval.of(12, 16));
    expecting = "2 * 0";
    assertEquals(expecting, result);

    tokens.insertAfter(17, "// comment");
    result = tokens.getText(Interval.of(12, 18));
    expecting = "2 * 0;// comment";
    assertEquals(expecting, result);

    result = tokens.getText(Interval.of(0, 8));
    stream.fill();
// try again after insert at end
    expecting = "x = 0";
    assertEquals(expecting, result);
  }


  @Test
  public void test2ReplaceMiddleIndex() throws Exception {
    LexerGrammar g = new LexerGrammar(
      """
        lexer grammar T;
        A : 'a';
        B : 'b';
        C : 'c';
        """);
    String input = "abc";
    LexerInterpreter lexEngine = g.createLexerInterpreter(CharStreams.fromString(input));
    CommonTokenStream stream = new CommonTokenStream(lexEngine);
    stream.fill();
    TokenStreamRewriter tokens = new TokenStreamRewriter(stream);
    tokens.replace(1, "x");
    tokens.replace(1, "y");
    String result = tokens.getText();
    String expecting = "ayc";
    assertEquals(expecting, result);
  }

  @Test
  public void test2ReplaceMiddleIndex1InsertBefore() throws Exception {
    LexerGrammar g = new LexerGrammar(
      """
        lexer grammar T;
        A : 'a';
        B : 'b';
        C : 'c';
        """);
    String input = "abc";
    LexerInterpreter lexEngine = g.createLexerInterpreter(CharStreams.fromString(input));
    CommonTokenStream stream = new CommonTokenStream(lexEngine);
    stream.fill();
    TokenStreamRewriter tokens = new TokenStreamRewriter(stream);
    tokens.insertBefore(0, "_");
    tokens.replace(1, "x");
    tokens.replace(1, "y");
    String result = tokens.getText();
    String expecting = "_ayc";
    assertEquals(expecting, result);
  }

  @Test
  public void testReplaceThenDeleteMiddleIndex() throws Exception {
    LexerGrammar g = new LexerGrammar(
      """
        lexer grammar T;
        A : 'a';
        B : 'b';
        C : 'c';
        """);
    String input = "abc";
    LexerInterpreter lexEngine = g.createLexerInterpreter(CharStreams.fromString(input));
    CommonTokenStream stream = new CommonTokenStream(lexEngine);
    stream.fill();
    TokenStreamRewriter tokens = new TokenStreamRewriter(stream);
    tokens.replace(1, "x");
    tokens.delete(1);
    String result = tokens.getText();
    String expecting = "ac";
    assertEquals(expecting, result);
  }

  @Test
  public void testInsertInPriorReplace() throws Exception {
    LexerGrammar g = new LexerGrammar(
      """
        lexer grammar T;
        A : 'a';
        B : 'b';
        C : 'c';
        """);
    String input = "abc";
    LexerInterpreter lexEngine = g.createLexerInterpreter(CharStreams.fromString(input));
    CommonTokenStream stream = new CommonTokenStream(lexEngine);
    stream.fill();
    TokenStreamRewriter tokens = new TokenStreamRewriter(stream);
    tokens.replace(0, 2, "x");
    tokens.insertBefore(1, "0");
    Exception exc = null;
    try {
      tokens.getText();
    } catch (IllegalArgumentException iae) {
      exc = iae;
    }
    String expecting = "insert op <InsertBeforeOp@[@1,1:1='b',<2>,1:1]:\"0\"> within boundaries of previous " +
      "<ReplaceOp@[@0,0:0='a',<1>,1:0]..[@2,2:2='c',<3>,1:2]:\"x\">";
    assertThat(exc).isNotNull();
    assertEquals(expecting, exc.getMessage());
  }

  @Test
  public void testInsertThenReplaceSameIndex() throws Exception {
    LexerGrammar g = new LexerGrammar(
      """
        lexer grammar T;
        A : 'a';
        B : 'b';
        C : 'c';
        """);
    String input = "abc";
    LexerInterpreter lexEngine = g.createLexerInterpreter(CharStreams.fromString(input));
    CommonTokenStream stream = new CommonTokenStream(lexEngine);
    stream.fill();
    TokenStreamRewriter tokens = new TokenStreamRewriter(stream);
    tokens.insertBefore(0, "0");
    tokens.replace(0, "x");
    stream.fill();
// supercedes insert at 0
    String result = tokens.getText();
    String expecting = "0xbc";
    assertEquals(expecting, result);
  }

  @Test
  public void test2InsertMiddleIndex() throws Exception {
    LexerGrammar g = new LexerGrammar(
      """
        lexer grammar T;
        A : 'a';
        B : 'b';
        C : 'c';
        """);
    String input = "abc";
    LexerInterpreter lexEngine = g.createLexerInterpreter(CharStreams.fromString(input));
    CommonTokenStream stream = new CommonTokenStream(lexEngine);
    stream.fill();
    TokenStreamRewriter tokens = new TokenStreamRewriter(stream);
    tokens.insertBefore(1, "x");
    tokens.insertBefore(1, "y");
    String result = tokens.getText();
    String expecting = "ayxbc";
    assertEquals(expecting, result);
  }

  @Test
  public void test2InsertThenReplaceIndex0() throws Exception {
    LexerGrammar g = new LexerGrammar(
      """
        lexer grammar T;
        A : 'a';
        B : 'b';
        C : 'c';
        """);
    String input = "abc";
    LexerInterpreter lexEngine = g.createLexerInterpreter(CharStreams.fromString(input));
    CommonTokenStream stream = new CommonTokenStream(lexEngine);
    stream.fill();
    TokenStreamRewriter tokens = new TokenStreamRewriter(stream);
    tokens.insertBefore(0, "x");
    tokens.insertBefore(0, "y");
    tokens.replace(0, "z");
    String result = tokens.getText();
    String expecting = "yxzbc";
    assertEquals(expecting, result);
  }

  @Test
  public void testReplaceThenInsertBeforeLastIndex() throws Exception {
    LexerGrammar g = new LexerGrammar(
      """
        lexer grammar T;
        A : 'a';
        B : 'b';
        C : 'c';
        """);
    String input = "abc";
    LexerInterpreter lexEngine = g.createLexerInterpreter(CharStreams.fromString(input));
    CommonTokenStream stream = new CommonTokenStream(lexEngine);
    stream.fill();
    TokenStreamRewriter tokens = new TokenStreamRewriter(stream);
    tokens.replace(2, "x");
    tokens.insertBefore(2, "y");
    String result = tokens.getText();
    String expecting = "abyx";
    assertEquals(expecting, result);
  }

  @Test
  public void testInsertThenReplaceLastIndex() throws Exception {
    LexerGrammar g = new LexerGrammar(
      """
        lexer grammar T;
        A : 'a';
        B : 'b';
        C : 'c';
        """);
    String input = "abc";
    LexerInterpreter lexEngine = g.createLexerInterpreter(CharStreams.fromString(input));
    CommonTokenStream stream = new CommonTokenStream(lexEngine);
    stream.fill();
    TokenStreamRewriter tokens = new TokenStreamRewriter(stream);
    tokens.insertBefore(2, "y");
    tokens.replace(2, "x");
    String result = tokens.getText();
    String expecting = "abyx";
    assertEquals(expecting, result);
  }

  @Test
  public void testReplaceThenInsertAfterLastIndex() throws Exception {
    LexerGrammar g = new LexerGrammar(
      """
        lexer grammar T;
        A : 'a';
        B : 'b';
        C : 'c';
        """);
    String input = "abc";
    LexerInterpreter lexEngine = g.createLexerInterpreter(CharStreams.fromString(input));
    CommonTokenStream stream = new CommonTokenStream(lexEngine);
    stream.fill();
    TokenStreamRewriter tokens = new TokenStreamRewriter(stream);
    tokens.replace(2, "x");
    tokens.insertAfter(2, "y");
    String result = tokens.getText();
    String expecting = "abxy";
    assertEquals(expecting, result);
  }

  @Test
  public void testReplaceRangeThenInsertAtLeftEdge() throws Exception {
    LexerGrammar g = new LexerGrammar(
      """
        lexer grammar T;
        A : 'a';
        B : 'b';
        C : 'c';
        """);
    String input = "abcccba";
    LexerInterpreter lexEngine = g.createLexerInterpreter(CharStreams.fromString(input));
    CommonTokenStream stream = new CommonTokenStream(lexEngine);
    stream.fill();
    TokenStreamRewriter tokens = new TokenStreamRewriter(stream);
    tokens.replace(2, 4, "x");
    tokens.insertBefore(2, "y");
    String result = tokens.getText();
    String expecting = "abyxba";
    assertEquals(expecting, result);
  }

  @Test
  public void testReplaceRangeThenInsertAtRightEdge() throws Exception {
    LexerGrammar g = new LexerGrammar(
      """
        lexer grammar T;
        A : 'a';
        B : 'b';
        C : 'c';
        """);
    String input = "abcccba";
    LexerInterpreter lexEngine = g.createLexerInterpreter(CharStreams.fromString(input));
    CommonTokenStream stream = new CommonTokenStream(lexEngine);
    stream.fill();
    TokenStreamRewriter tokens = new TokenStreamRewriter(stream);
    tokens.replace(2, 4, "x");
    tokens.insertBefore(4, "y");
    stream.fill(); // no effect; within range of a replace
    Exception exc = null;
    try {
      tokens.getText();
    } catch (IllegalArgumentException iae) {
      exc = iae;
    }
    String expecting = "insert op <InsertBeforeOp@[@4,4:4='c',<3>,1:4]:\"y\"> within boundaries of previous " +
      "<ReplaceOp@[@2,2:2='c',<3>,1:2]..[@4,4:4='c',<3>,1:4]:\"x\">";
    assertThat(exc).isNotNull();
    assertEquals(expecting, exc.getMessage());
  }

  @Test
  public void testReplaceRangeThenInsertAfterRightEdge() throws Exception {
    LexerGrammar g = new LexerGrammar(
      """
        lexer grammar T;
        A : 'a';
        B : 'b';
        C : 'c';
        """);
    String input = "abcccba";
    LexerInterpreter lexEngine = g.createLexerInterpreter(CharStreams.fromString(input));
    CommonTokenStream stream = new CommonTokenStream(lexEngine);
    stream.fill();
    TokenStreamRewriter tokens = new TokenStreamRewriter(stream);
    tokens.replace(2, 4, "x");
    tokens.insertAfter(4, "y");
    String result = tokens.getText();
    String expecting = "abxyba";
    assertEquals(expecting, result);
  }

  @Test
  public void testReplaceAll() throws Exception {
    LexerGrammar g = new LexerGrammar(
      """
        lexer grammar T;
        A : 'a';
        B : 'b';
        C : 'c';
        """);
    String input = "abcccba";
    LexerInterpreter lexEngine = g.createLexerInterpreter(CharStreams.fromString(input));
    CommonTokenStream stream = new CommonTokenStream(lexEngine);
    stream.fill();
    TokenStreamRewriter tokens = new TokenStreamRewriter(stream);
    tokens.replace(0, 6, "x");
    String result = tokens.getText();
    String expecting = "x";
    assertEquals(expecting, result);
  }

  @Test
  public void testReplaceSubsetThenFetch() throws Exception {
    LexerGrammar g = new LexerGrammar(
      """
        lexer grammar T;
        A : 'a';
        B : 'b';
        C : 'c';
        """);
    String input = "abcccba";
    LexerInterpreter lexEngine = g.createLexerInterpreter(CharStreams.fromString(input));
    CommonTokenStream stream = new CommonTokenStream(lexEngine);
    stream.fill();
    TokenStreamRewriter tokens = new TokenStreamRewriter(stream);
    tokens.replace(2, 4, "xyz");
    String result = tokens.getText(Interval.of(0, 6));
    String expecting = "abxyzba";
    assertEquals(expecting, result);
  }

  @Test
  public void testReplaceThenReplaceSuperset() throws Exception {
    LexerGrammar g = new LexerGrammar(
      """
        lexer grammar T;
        A : 'a';
        B : 'b';
        C : 'c';
        """);
    String input = "abcccba";
    LexerInterpreter lexEngine = g.createLexerInterpreter(CharStreams.fromString(input));
    CommonTokenStream stream = new CommonTokenStream(lexEngine);
    stream.fill();
    TokenStreamRewriter tokens = new TokenStreamRewriter(stream);
    tokens.replace(2, 4, "xyz");
    tokens.replace(3, 5, "foo");
    stream.fill();
// overlaps, error
    Exception exc = null;
    try {
      tokens.getText();
    } catch (IllegalArgumentException iae) {
      exc = iae;
    }
    String expecting = "replace op boundaries of <ReplaceOp@[@3,3:3='c',<3>,1:3]..[@5,5:5='b',<2>,1:5]:\"foo\"> " +
      "overlap with previous <ReplaceOp@[@2,2:2='c',<3>,1:2]..[@4,4:4='c',<3>,1:4]:\"xyz\">";
    assertThat(exc).isNotNull();
    assertEquals(expecting, exc.getMessage());
  }

  @Test
  public void testReplaceThenReplaceLowerIndexedSuperset() throws Exception {
    LexerGrammar g = new LexerGrammar(
      """
        lexer grammar T;
        A : 'a';
        B : 'b';
        C : 'c';
        """);
    String input = "abcccba";
    LexerInterpreter lexEngine = g.createLexerInterpreter(CharStreams.fromString(input));
    CommonTokenStream stream = new CommonTokenStream(lexEngine);
    stream.fill();
    TokenStreamRewriter tokens = new TokenStreamRewriter(stream);
    tokens.replace(2, 4, "xyz");
    tokens.replace(1, 3, "foo");
    stream.fill();
// overlap, error
    Exception exc = null;
    try {
      tokens.getText();
    } catch (IllegalArgumentException iae) {
      exc = iae;
    }
    String expecting = "replace op boundaries of <ReplaceOp@[@1,1:1='b',<2>,1:1]..[@3,3:3='c',<3>,1:3]:\"foo\"> " +
      "overlap with previous <ReplaceOp@[@2,2:2='c',<3>,1:2]..[@4,4:4='c',<3>,1:4]:\"xyz\">";
    assertThat(exc).isNotNull();
    assertEquals(expecting, exc.getMessage());
  }

  @Test
  public void testReplaceSingleMiddleThenOverlappingSuperset() throws Exception {
    LexerGrammar g = new LexerGrammar(
      """
        lexer grammar T;
        A : 'a';
        B : 'b';
        C : 'c';
        """);
    String input = "abcba";
    LexerInterpreter lexEngine = g.createLexerInterpreter(CharStreams.fromString(input));
    CommonTokenStream stream = new CommonTokenStream(lexEngine);
    stream.fill();
    TokenStreamRewriter tokens = new TokenStreamRewriter(stream);
    tokens.replace(2, 2, "xyz");
    tokens.replace(0, 3, "foo");
    String result = tokens.getText();
    String expecting = "fooa";
    assertEquals(expecting, result);
  }

  @Test
  public void testCombineInserts() throws Exception {
    LexerGrammar g = new LexerGrammar(
      """
        lexer grammar T;
        A : 'a';
        B : 'b';
        C : 'c';
        """);
    String input = "abc";
    LexerInterpreter lexEngine = g.createLexerInterpreter(CharStreams.fromString(input));
    CommonTokenStream stream = new CommonTokenStream(lexEngine);
    stream.fill();
    TokenStreamRewriter tokens = new TokenStreamRewriter(stream);
    tokens.insertBefore(0, "x");
    tokens.insertBefore(0, "y");
    String result = tokens.getText();
    String expecting = "yxabc";
    assertEquals(expecting, result);
  }

  @Test
  public void testCombine3Inserts() throws Exception {
    LexerGrammar g = new LexerGrammar(
      """
        lexer grammar T;
        A : 'a';
        B : 'b';
        C : 'c';
        """);
    String input = "abc";
    LexerInterpreter lexEngine = g.createLexerInterpreter(CharStreams.fromString(input));
    CommonTokenStream stream = new CommonTokenStream(lexEngine);
    stream.fill();
    TokenStreamRewriter tokens = new TokenStreamRewriter(stream);
    tokens.insertBefore(1, "x");
    tokens.insertBefore(0, "y");
    tokens.insertBefore(1, "z");
    String result = tokens.getText();
    String expecting = "yazxbc";
    assertEquals(expecting, result);
  }

  @Test
  public void testCombineInsertOnLeftWithReplace() throws Exception {
    LexerGrammar g = new LexerGrammar(
      """
        lexer grammar T;
        A : 'a';
        B : 'b';
        C : 'c';
        """);
    String input = "abc";
    LexerInterpreter lexEngine = g.createLexerInterpreter(CharStreams.fromString(input));
    CommonTokenStream stream = new CommonTokenStream(lexEngine);
    stream.fill();
    TokenStreamRewriter tokens = new TokenStreamRewriter(stream);
    tokens.replace(0, 2, "foo");
    tokens.insertBefore(0, "z");
    stream.fill();
// combine with left edge of rewrite
    String result = tokens.getText();
    String expecting = "zfoo";
    assertEquals(expecting, result);
  }

  @Test
  public void testCombineInsertOnLeftWithDelete() throws Exception {
    LexerGrammar g = new LexerGrammar(
      """
        lexer grammar T;
        A : 'a';
        B : 'b';
        C : 'c';
        """);
    String input = "abc";
    LexerInterpreter lexEngine = g.createLexerInterpreter(CharStreams.fromString(input));
    CommonTokenStream stream = new CommonTokenStream(lexEngine);
    stream.fill();
    TokenStreamRewriter tokens = new TokenStreamRewriter(stream);
    tokens.delete(0, 2);
    tokens.insertBefore(0, "z");
    stream.fill();
// combine with left edge of rewrite
    String result = tokens.getText();
    String expecting = "z";
    stream.fill();
// make sure combo is not znull
    assertEquals(expecting, result);
  }

  @Test
  public void testDisjointInserts() throws Exception {
    LexerGrammar g = new LexerGrammar(
      """
        lexer grammar T;
        A : 'a';
        B : 'b';
        C : 'c';
        """);
    String input = "abc";
    LexerInterpreter lexEngine = g.createLexerInterpreter(CharStreams.fromString(input));
    CommonTokenStream stream = new CommonTokenStream(lexEngine);
    stream.fill();
    TokenStreamRewriter tokens = new TokenStreamRewriter(stream);
    tokens.insertBefore(1, "x");
    tokens.insertBefore(2, "y");
    tokens.insertBefore(0, "z");
    String result = tokens.getText();
    String expecting = "zaxbyc";
    assertEquals(expecting, result);
  }

  @Test
  public void testOverlappingReplace() throws Exception {
    LexerGrammar g = new LexerGrammar(
      """
        lexer grammar T;
        A : 'a';
        B : 'b';
        C : 'c';
        """);
    String input = "abcc";
    LexerInterpreter lexEngine = g.createLexerInterpreter(CharStreams.fromString(input));
    CommonTokenStream stream = new CommonTokenStream(lexEngine);
    stream.fill();
    TokenStreamRewriter tokens = new TokenStreamRewriter(stream);
    tokens.replace(1, 2, "foo");
    tokens.replace(0, 3, "bar");
    stream.fill();
// wipes prior nested replace
    String result = tokens.getText();
    String expecting = "bar";
    assertEquals(expecting, result);
  }

  @Test
  public void testOverlappingReplace2() throws Exception {
    LexerGrammar g = new LexerGrammar(
      """
        lexer grammar T;
        A : 'a';
        B : 'b';
        C : 'c';
        """);
    String input = "abcc";
    LexerInterpreter lexEngine = g.createLexerInterpreter(CharStreams.fromString(input));
    CommonTokenStream stream = new CommonTokenStream(lexEngine);
    stream.fill();
    TokenStreamRewriter tokens = new TokenStreamRewriter(stream);
    tokens.replace(0, 3, "bar");
    tokens.replace(1, 2, "foo");
    stream.fill();
// cannot split earlier replace
    Exception exc = null;
    try {
      tokens.getText();
    } catch (IllegalArgumentException iae) {
      exc = iae;
    }
    String expecting = "replace op boundaries of <ReplaceOp@[@1,1:1='b',<2>,1:1]..[@2,2:2='c',<3>,1:2]:\"foo\"> " +
      "overlap with previous <ReplaceOp@[@0,0:0='a',<1>,1:0]..[@3,3:3='c',<3>,1:3]:\"bar\">";
    assertThat(exc).isNotNull();
    assertEquals(expecting, exc.getMessage());
  }

  @Test
  public void testOverlappingReplace3() throws Exception {
    LexerGrammar g = new LexerGrammar(
      """
        lexer grammar T;
        A : 'a';
        B : 'b';
        C : 'c';
        """);
    String input = "abcc";
    LexerInterpreter lexEngine = g.createLexerInterpreter(CharStreams.fromString(input));
    CommonTokenStream stream = new CommonTokenStream(lexEngine);
    stream.fill();
    TokenStreamRewriter tokens = new TokenStreamRewriter(stream);
    tokens.replace(1, 2, "foo");
    tokens.replace(0, 2, "bar");
    stream.fill();
// wipes prior nested replace
    String result = tokens.getText();
    String expecting = "barc";
    assertEquals(expecting, result);
  }

  @Test
  public void testOverlappingReplace4() throws Exception {
    LexerGrammar g = new LexerGrammar(
      """
        lexer grammar T;
        A : 'a';
        B : 'b';
        C : 'c';
        """);
    String input = "abcc";
    LexerInterpreter lexEngine = g.createLexerInterpreter(CharStreams.fromString(input));
    CommonTokenStream stream = new CommonTokenStream(lexEngine);
    stream.fill();
    TokenStreamRewriter tokens = new TokenStreamRewriter(stream);
    tokens.replace(1, 2, "foo");
    tokens.replace(1, 3, "bar");
    stream.fill();
// wipes prior nested replace
    String result = tokens.getText();
    String expecting = "abar";
    assertEquals(expecting, result);
  }

  @Test
  public void testDropIdenticalReplace() throws Exception {
    LexerGrammar g = new LexerGrammar(
      """
        lexer grammar T;
        A : 'a';
        B : 'b';
        C : 'c';
        """);
    String input = "abcc";
    LexerInterpreter lexEngine = g.createLexerInterpreter(CharStreams.fromString(input));
    CommonTokenStream stream = new CommonTokenStream(lexEngine);
    stream.fill();
    TokenStreamRewriter tokens = new TokenStreamRewriter(stream);
    tokens.replace(1, 2, "foo");
    tokens.replace(1, 2, "foo");
    stream.fill();
// drop previous, identical
    String result = tokens.getText();
    String expecting = "afooc";
    assertEquals(expecting, result);
  }

  @Test
  public void testDropPrevCoveredInsert() throws Exception {
    LexerGrammar g = new LexerGrammar(
      """
        lexer grammar T;
        A : 'a';
        B : 'b';
        C : 'c';
        """);
    String input = "abc";
    LexerInterpreter lexEngine = g.createLexerInterpreter(CharStreams.fromString(input));
    CommonTokenStream stream = new CommonTokenStream(lexEngine);
    stream.fill();
    TokenStreamRewriter tokens = new TokenStreamRewriter(stream);
    tokens.insertBefore(1, "foo");
    tokens.replace(1, 2, "foo");
    stream.fill();
// kill prev insert
    String result = tokens.getText();
    String expecting = "afoofoo";
    assertEquals(expecting, result);
  }

  @Test
  public void testLeaveAloneDisjointInsert() throws Exception {
    LexerGrammar g = new LexerGrammar(
      """
        lexer grammar T;
        A : 'a';
        B : 'b';
        C : 'c';
        """);
    String input = "abcc";
    LexerInterpreter lexEngine = g.createLexerInterpreter(CharStreams.fromString(input));
    CommonTokenStream stream = new CommonTokenStream(lexEngine);
    stream.fill();
    TokenStreamRewriter tokens = new TokenStreamRewriter(stream);
    tokens.insertBefore(1, "x");
    tokens.replace(2, 3, "foo");
    String result = tokens.getText();
    String expecting = "axbfoo";
    assertEquals(expecting, result);
  }

  @Test
  public void testLeaveAloneDisjointInsert2() throws Exception {
    LexerGrammar g = new LexerGrammar(
      """
        lexer grammar T;
        A : 'a';
        B : 'b';
        C : 'c';
        """);
    String input = "abcc";
    LexerInterpreter lexEngine = g.createLexerInterpreter(CharStreams.fromString(input));
    CommonTokenStream stream = new CommonTokenStream(lexEngine);
    stream.fill();
    TokenStreamRewriter tokens = new TokenStreamRewriter(stream);
    tokens.replace(2, 3, "foo");
    tokens.insertBefore(1, "x");
    String result = tokens.getText();
    String expecting = "axbfoo";
    assertEquals(expecting, result);
  }

  @Test
  public void testInsertBeforeTokenThenDeleteThatToken() throws Exception {
    LexerGrammar g = new LexerGrammar(
      """
        lexer grammar T;
        A : 'a';
        B : 'b';
        C : 'c';
        """);
    String input = "abc";
    LexerInterpreter lexEngine = g.createLexerInterpreter(CharStreams.fromString(input));
    CommonTokenStream stream = new CommonTokenStream(lexEngine);
    stream.fill();
    TokenStreamRewriter tokens = new TokenStreamRewriter(stream);
    tokens.insertBefore(2, "y");
    tokens.delete(2);
    String result = tokens.getText();
    String expecting = "aby";
    assertEquals(expecting, result);
  }

  // Test for https://github.com/antlr/antlr4/issues/550
  @Test
  public void testDistinguishBetweenInsertAfterAndInsertBeforeToPreserverOrder() throws Exception {
    LexerGrammar g = new LexerGrammar(
      """
        lexer grammar T;
        A : 'a';
        B : 'b';
        C : 'c';
        """);
    String input = "aa";
    LexerInterpreter lexEngine = g.createLexerInterpreter(CharStreams.fromString(input));
    CommonTokenStream stream = new CommonTokenStream(lexEngine);
    stream.fill();
    TokenStreamRewriter tokens = new TokenStreamRewriter(stream);
    tokens.insertBefore(0, "<b>");
    tokens.insertAfter(0, "</b>");
    tokens.insertBefore(1, "<b>");
    tokens.insertAfter(1, "</b>");
    String result = tokens.getText();
    String expecting = "<b>a</b><b>a</b>"; // fails with <b>a<b></b>a</b>"
    assertEquals(expecting, result);
  }

  @Test
  public void testDistinguishBetweenInsertAfterAndInsertBeforeToPreserverOrder2() throws Exception {
    LexerGrammar g = new LexerGrammar(
      """
        lexer grammar T;
        A : 'a';
        B : 'b';
        C : 'c';
        """);
    String input = "aa";
    LexerInterpreter lexEngine = g.createLexerInterpreter(CharStreams.fromString(input));
    CommonTokenStream stream = new CommonTokenStream(lexEngine);
    stream.fill();
    TokenStreamRewriter tokens = new TokenStreamRewriter(stream);
    tokens.insertBefore(0, "<p>");
    tokens.insertBefore(0, "<b>");
    tokens.insertAfter(0, "</p>");
    tokens.insertAfter(0, "</b>");
    tokens.insertBefore(1, "<b>");
    tokens.insertAfter(1, "</b>");
    String result = tokens.getText();
    String expecting = "<b><p>a</p></b><b>a</b>";
    assertEquals(expecting, result);
  }

  // Test for https://github.com/antlr/antlr4/issues/550
  @Test
  public void testPreservesOrderOfContiguousInserts() throws Exception {
    LexerGrammar g = new LexerGrammar(
      """
        lexer grammar T;
        A : 'a';
        B : 'b';
        C : 'c';
        """);
    String input = "ab";
    LexerInterpreter lexEngine = g.createLexerInterpreter(CharStreams.fromString(input));
    CommonTokenStream stream = new CommonTokenStream(lexEngine);
    stream.fill();
    TokenStreamRewriter tokens = new TokenStreamRewriter(stream);
    tokens.insertBefore(0, "<p>");
    tokens.insertBefore(0, "<b>");
    tokens.insertBefore(0, "<div>");
    tokens.insertAfter(0, "</p>");
    tokens.insertAfter(0, "</b>");
    tokens.insertAfter(0, "</div>");
    tokens.insertBefore(1, "!");
    String result = tokens.getText();
    String expecting = "<div><b><p>a</p></b></div>!b";
    assertEquals(expecting, result);
  }

}
