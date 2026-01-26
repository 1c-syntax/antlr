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

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.LexerInterpreter;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenSource;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.UnbufferedTokenStream;
import org.antlr.v4.tool.LexerGrammar;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class UnbufferedTokenStreamTest extends AbstractBaseTest {
  @Test
  void testLookahead() throws Exception {
    LexerGrammar g = new LexerGrammar(
      """
        lexer grammar t;
        ID : 'a'..'z'+;
        INT : '0'..'9'+;
        SEMI : ';';
        ASSIGN : '=';
        PLUS : '+';
        MULT : '*';
        WS : ' '+;
        """);
    // Tokens: 012345678901234567
    // Input:  x = 302;
    CharStream input = CharStreams.fromString("x = 302;");
    LexerInterpreter lexEngine = g.createLexerInterpreter(input);
    TokenStream tokens = new UnbufferedTokenStream(lexEngine);

    assertThat(tokens.LT(1).getText()).isEqualTo("x");
    assertThat(tokens.LT(2).getText()).isEqualTo(" ");
    assertThat(tokens.LT(3).getText()).isEqualTo("=");
    assertThat(tokens.LT(4).getText()).isEqualTo(" ");
    assertThat(tokens.LT(5).getText()).isEqualTo("302");
    assertThat(tokens.LT(6).getText()).isEqualTo(";");
  }

  @Test
  void testNoBuffering() throws Exception {
    LexerGrammar g = new LexerGrammar(
      """
        lexer grammar t;
        ID : 'a'..'z'+;
        INT : '0'..'9'+;
        SEMI : ';';
        ASSIGN : '=';
        PLUS : '+';
        MULT : '*';
        WS : ' '+;
        """);
    // Tokens: 012345678901234567
    // Input:  x = 302;
    CharStream input = CharStreams.fromString("x = 302;");
    LexerInterpreter lexEngine = g.createLexerInterpreter(input);
    TestingUnbufferedTokenStream tokens = new TestingUnbufferedTokenStream(lexEngine);

    assertThat(tokens.getBuffer().toString()).isEqualTo("[[@0,0:0='x',<1>,1:0]]");
    assertThat(tokens.LT(1).getText()).isEqualTo("x");
    tokens.consume(); // move to WS
    assertThat(tokens.LT(1).getText()).isEqualTo(" ");
    assertThat(tokens.getRemainingBuffer().toString()).isEqualTo("[[@1,1:1=' ',<7>,1:1]]");
    tokens.consume();
    assertThat(tokens.LT(1).getText()).isEqualTo("=");
    assertThat(tokens.getRemainingBuffer().toString()).isEqualTo("[[@2,2:2='=',<4>,1:2]]");
    tokens.consume();
    assertThat(tokens.LT(1).getText()).isEqualTo(" ");
    assertThat(tokens.getRemainingBuffer().toString()).isEqualTo("[[@3,3:3=' ',<7>,1:3]]");
    tokens.consume();
    assertThat(tokens.LT(1).getText()).isEqualTo("302");
    assertThat(tokens.getRemainingBuffer().toString()).isEqualTo("[[@4,4:6='302',<2>,1:4]]");
    tokens.consume();
    assertThat(tokens.LT(1).getText()).isEqualTo(";");
    assertThat(tokens.getRemainingBuffer().toString()).isEqualTo("[[@5,7:7=';',<3>,1:7]]");
  }

  @Test
  void testMarkStart() throws Exception {
    LexerGrammar g = new LexerGrammar(
      """
        lexer grammar t;
        ID : 'a'..'z'+;
        INT : '0'..'9'+;
        SEMI : ';';
        ASSIGN : '=';
        PLUS : '+';
        MULT : '*';
        WS : ' '+;
        """);
    // Tokens: 012345678901234567
    // Input:  x = 302;
    CharStream input = CharStreams.fromString("x = 302;");
    LexerInterpreter lexEngine = g.createLexerInterpreter(input);
    TestingUnbufferedTokenStream tokens = new TestingUnbufferedTokenStream(lexEngine);

    int m = tokens.mark();
    assertThat(tokens.getBuffer().toString()).isEqualTo("[[@0,0:0='x',<1>,1:0]]");
    assertThat(tokens.LT(1).getText()).isEqualTo("x");
    tokens.consume(); // consume x
    assertThat(tokens.getBuffer().toString()).isEqualTo("[[@0,0:0='x',<1>,1:0], [@1,1:1=' ',<7>,1:1]]");
    tokens.consume(); // ' '
    tokens.consume(); // =
    tokens.consume(); // ' '
    tokens.consume(); // 302
    tokens.consume(); // ;
    assertThat(tokens.getBuffer().toString()).isEqualTo("""
        [[@0,0:0='x',<1>,1:0], [@1,1:1=' ',<7>,1:1],\
         [@2,2:2='=',<4>,1:2], [@3,3:3=' ',<7>,1:3],\
         [@4,4:6='302',<2>,1:4], [@5,7:7=';',<3>,1:7],\
         [@6,8:7='<EOF>',<-1>,1:8]]""");
  }

  @Test
  void testMarkThenRelease() throws Exception {
    LexerGrammar g = new LexerGrammar(
      """
        lexer grammar t;
        ID : 'a'..'z'+;
        INT : '0'..'9'+;
        SEMI : ';';
        ASSIGN : '=';
        PLUS : '+';
        MULT : '*';
        WS : ' '+;
        """);
    // Tokens: 012345678901234567
    // Input:  x = 302;
    CharStream input = CharStreams.fromString("x = 302 + 1;");
    LexerInterpreter lexEngine = g.createLexerInterpreter(input);
    TestingUnbufferedTokenStream tokens = new TestingUnbufferedTokenStream(lexEngine);

    int m = tokens.mark();
    assertThat(tokens.getBuffer().toString()).isEqualTo("[[@0,0:0='x',<1>,1:0]]");
    assertThat(tokens.LT(1).getText()).isEqualTo("x");
    tokens.consume(); // consume x
    assertThat(tokens.getBuffer().toString()).isEqualTo("[[@0,0:0='x',<1>,1:0], [@1,1:1=' ',<7>,1:1]]");
    tokens.consume(); // ' '
    tokens.consume(); // =
    tokens.consume(); // ' '
    assertThat(tokens.LT(1).getText()).isEqualTo("302");
    tokens.release(m); // "x = 302" is in buffer. will kill buffer
    tokens.consume(); // 302
    tokens.consume(); // ' '
    m = tokens.mark(); // mark at the +
    assertThat(tokens.LT(1).getText()).isEqualTo("+");
    tokens.consume(); // '+'
    tokens.consume(); // ' '
    tokens.consume(); // 1
    tokens.consume(); // ;
    assertThat(tokens.LT(1).getText()).isEqualTo("<EOF>");
    // we marked at the +, so that should be the start of the buffer
    assertThat(tokens.getBuffer().toString()).isEqualTo("""
        [[@6,8:8='+',<5>,1:8], [@7,9:9=' ',<7>,1:9],\
         [@8,10:10='1',<2>,1:10], [@9,11:11=';',<3>,1:11],\
         [@10,12:11='<EOF>',<-1>,1:12]]""");
    tokens.release(m);
  }

  protected static class TestingUnbufferedTokenStream extends UnbufferedTokenStream {

    public TestingUnbufferedTokenStream(TokenSource tokenSource) {
      super(tokenSource);
    }

    /**
     * For testing.  What's in moving window into token stream from
     * current index, LT(1) or tokens[p], to end of buffer?
     */
    protected List<? extends Token> getRemainingBuffer() {
      if (n == 0) {
        return Collections.emptyList();
      }

      return Arrays.asList(tokens).subList(p, n);
    }

    /**
     * For testing.  What's in moving window buffer into data stream.
     * From 0..p-1 have been consume.
     */
    protected List<? extends Token> getBuffer() {
      if (n == 0) {
        return Collections.emptyList();
      }

      return Arrays.asList(tokens).subList(0, n);
    }

  }
}
