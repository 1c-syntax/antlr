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

import org.antlr.runtime.RecognitionException;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CommonTokenFactory;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.IntStream;
import org.antlr.v4.runtime.LexerInterpreter;
import org.antlr.v4.runtime.UnbufferedCharStream;
import org.antlr.v4.runtime.misc.Interval;
import org.antlr.v4.tool.LexerGrammar;
import org.junit.jupiter.api.Test;

import java.io.Reader;
import java.io.StringReader;

import static org.assertj.core.api.Assertions.assertThat;

class UnbufferedCharStreamTest extends AbstractBaseTest {
  @Test
  void testNoChar() {
    CharStream input = createStream("");
    assertThat(input.LA(1)).isEqualTo(IntStream.EOF);
    assertThat(input.LA(2)).isEqualTo(IntStream.EOF);
  }

  @Test
  void testSeekPastEOF() {
    CharStream input = createStream("");
    assertThat(input.index()).isZero();
    input.seek(1);
    assertThat(input.index()).isZero();
  }

  @Test
  void testGetTextInMarkedRange() {
    CharStream input = createStream("xyz");
    input.consume();
    input.mark();
    assertThat(input.index()).isEqualTo(1);
    input.consume();
    input.consume();
    assertThat(input.getText(new Interval(1, 2))).isEqualTo("yz");
  }

  @Test
  void testLastChar() {
    CharStream input = createStream("abcdef");

    input.consume();
    assertThat(input.LA(-1)).isEqualTo('a');

    int m1 = input.mark();
    input.consume();
    input.consume();
    input.consume();
    assertThat(input.LA(-1)).isEqualTo('d');

    input.seek(2);
    assertThat(input.LA(-1)).isEqualTo('b');

    input.release(m1);
    input.seek(3);
    assertThat(input.LA(-1)).isEqualTo('c');
    // this special case is not required by the IntStream interface, but
    // UnbufferedCharStream allows it so we have to make sure the resulting
    // state is consistent
    input.seek(2);
    assertThat(input.LA(-1)).isEqualTo('b');
  }

  @Test
  void test1Char() {
    TestingUnbufferedCharStream input = createStream("x");
    assertThat(input.LA(1)).isEqualTo('x');  // Явно приводим 'x' к int
    input.consume();
    assertThat(input.LA(1)).isEqualTo(IntStream.EOF);
    String r = input.getRemainingBuffer();
    assertThat(r).isEqualTo("\uFFFF"); // shouldn't include x
    assertThat(input.getBuffer()).isEqualTo("\uFFFF"); // whole buffer
  }

  @Test
  void test2Char() {
    TestingUnbufferedCharStream input = createStream("xy");
    assertThat(input.LA(1)).isEqualTo('x');
    input.consume();
    assertThat(input.LA(1)).isEqualTo('y');
    assertThat(input.getRemainingBuffer()).isEqualTo("y"); // shouldn't include x
    assertThat(input.getBuffer()).isEqualTo("y");
    input.consume();
    assertThat(input.LA(1)).isEqualTo(IntStream.EOF);
    assertThat(input.getBuffer()).isEqualTo("\uFFFF");
  }

  @Test
  void test2CharAhead() {
    CharStream input = createStream("xy");
    assertThat(input.LA(1)).isEqualTo('x');
    assertThat(input.LA(2)).isEqualTo('y');
    assertThat(input.LA(3)).isEqualTo(IntStream.EOF);
  }

  @Test
  void testBufferExpand() {
    TestingUnbufferedCharStream input = createStream("01234", 2);
    assertThat(input.LA(1)).isEqualTo('0');
    assertThat(input.LA(2)).isEqualTo('1');
    assertThat(input.LA(3)).isEqualTo('2');
    assertThat(input.LA(4)).isEqualTo('3');
    assertThat(input.LA(5)).isEqualTo('4');
    assertThat(input.LA(6)).isEqualTo(IntStream.EOF); // вызываем LA(6), чтобы убедиться, что EOF прочитан
    assertThat(input.getBuffer()).isEqualTo("01234\uFFFF");
  }

  @Test
  void testBufferWrapSize1() {
    CharStream input = createStream("01234", 1);
    assertThat(input.LA(1)).isEqualTo('0');
    input.consume();
    assertThat(input.LA(1)).isEqualTo('1');
    input.consume();
    assertThat(input.LA(1)).isEqualTo('2');
    input.consume();
    assertThat(input.LA(1)).isEqualTo('3');
    input.consume();
    assertThat(input.LA(1)).isEqualTo('4');
    input.consume();
    assertThat(input.LA(1)).isEqualTo(IntStream.EOF);
  }

  @Test
  void testBufferWrapSize2() {
    CharStream input = createStream("01234", 2);
    assertThat(input.LA(1)).isEqualTo('0');
    input.consume();
    assertThat(input.LA(1)).isEqualTo('1');
    input.consume();
    assertThat(input.LA(1)).isEqualTo('2');
    input.consume();
    assertThat(input.LA(1)).isEqualTo('3');
    input.consume();
    assertThat(input.LA(1)).isEqualTo('4');
    input.consume();
    assertThat(input.LA(1)).isEqualTo(IntStream.EOF);
  }

  @Test
  void test1Mark() {
    TestingUnbufferedCharStream input = createStream("xyz");
    int m = input.mark();
    assertThat(input.LA(1)).isEqualTo('x');
    assertThat(input.LA(2)).isEqualTo('y');
    assertThat(input.LA(3)).isEqualTo('z');
    input.release(m);
    assertThat(input.LA(4)).isEqualTo(IntStream.EOF);
    assertThat(input.getBuffer()).isEqualTo("xyz\uFFFF");
  }

  @Test
  void test1MarkWithConsumesInSequence() {
    TestingUnbufferedCharStream input = createStream("xyz");
    int m = input.mark();
    input.consume(); // x, moves to y
    input.consume(); // y
    input.consume(); // z, moves to EOF
    assertThat(input.LA(1)).isEqualTo(IntStream.EOF);
    assertThat(input.getBuffer()).isEqualTo("xyz\uFFFF");
    input.release(m); // wipes buffer
    assertThat(input.getBuffer()).isEqualTo("\uFFFF");
  }

  @Test
  void test2Mark() {
    TestingUnbufferedCharStream input = createStream("xyz", 100);
    assertThat(input.LA(1)).isEqualTo('x');
    input.consume(); // reset buffer index (p) to 0
    int m1 = input.mark();
    assertThat(input.LA(1)).isEqualTo('y');
    input.consume();
    int m2 = input.mark();
    assertThat(input.getBuffer()).isEqualTo("yz");
    input.release(m2); // drop to 1 marker
    input.consume();
    input.release(m1); // shifts remaining char to beginning
    assertThat(input.LA(1)).isEqualTo(IntStream.EOF);
    assertThat(input.getBuffer()).isEqualTo("\uFFFF");
  }

  @Test
  void testAFewTokens() throws RecognitionException {
    LexerGrammar g = new LexerGrammar(
      "lexer grammar t;\n" +
        "ID : 'a'..'z'+;\n" +
        "INT : '0'..'9'+;\n" +
        "SEMI : ';';\n" +
        "ASSIGN : '=';\n" +
        "PLUS : '+';\n" +
        "MULT : '*';\n" +
        "WS : ' '+;\n");
    // Tokens: 012345678901234567
    // Input:  x = 3 * 0 + 2 * 0;
    TestingUnbufferedCharStream input = createStream("x = 302 * 91 + 20234234 * 0;");
    LexerInterpreter lexEngine = g.createLexerInterpreter(input);
    // copy text into tokens from char stream
    lexEngine.setTokenFactory(new CommonTokenFactory(true));
    CommonTokenStream tokens = new CommonTokenStream(lexEngine);
    String result = tokens.LT(1).getText();
    String expecting = "x";
    assertThat(result).isEqualTo(expecting);
    tokens.fill();
    expecting =
      "[[@0,0:0='x',<1>,1:0], [@1,1:1=' ',<7>,1:1], [@2,2:2='=',<4>,1:2]," +
        " [@3,3:3=' ',<7>,1:3], [@4,4:6='302',<2>,1:4], [@5,7:7=' ',<7>,1:7]," +
        " [@6,8:8='*',<6>,1:8], [@7,9:9=' ',<7>,1:9], [@8,10:11='91',<2>,1:10]," +
        " [@9,12:12=' ',<7>,1:12], [@10,13:13='+',<5>,1:13], [@11,14:14=' ',<7>,1:14]," +
        " [@12,15:22='20234234',<2>,1:15], [@13,23:23=' ',<7>,1:23]," +
        " [@14,24:24='*',<6>,1:24], [@15,25:25=' ',<7>,1:25], [@16,26:26='0',<2>,1:26]," +
        " [@17,27:27=';',<3>,1:27], [@18,28:27='',<-1>,1:28]]";
    assertThat(tokens.getTokens()).hasToString(expecting);
  }


  protected static TestingUnbufferedCharStream createStream(String text) {
    return new TestingUnbufferedCharStream(new StringReader(text));
  }

  protected static TestingUnbufferedCharStream createStream(String text, int bufferSize) {
    return new TestingUnbufferedCharStream(new StringReader(text), bufferSize);
  }

  protected static class TestingUnbufferedCharStream extends UnbufferedCharStream {

    public TestingUnbufferedCharStream(Reader input) {
      super(input);
    }

    public TestingUnbufferedCharStream(Reader input, int bufferSize) {
      super(input, bufferSize);
    }

    /**
     * For testing.  What's in moving window into data stream from
     * current index, LA(1) or data[p], to end of buffer?
     */
    public String getRemainingBuffer() {
      if (n == 0) return "";
      int len = n;
      if (data[len - 1] == IntStream.EOF) {
        // Don't pass -1 to new String().
        return new String(data, p, len - p - 1) + "\uFFFF";
      } else {
        return new String(data, p, len - p);
      }
    }

    /**
     * For testing.  What's in moving window buffer into data stream.
     * From 0..p-1 have been consume.
     */
    public String getBuffer() {
      if (n == 0) return "";
      int len = n;
      // Don't pass -1 to new String().
      if (data[len - 1] == IntStream.EOF) {
        // Don't pass -1 to new String().
        return new String(data, 0, len - 1) + "\uFFFF";
      } else {
        return new String(data, 0, len);
      }
    }

  }
}
