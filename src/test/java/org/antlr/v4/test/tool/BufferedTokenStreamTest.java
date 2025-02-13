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

import org.antlr.v4.runtime.BufferedTokenStream;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.LexerInterpreter;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenSource;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.tool.LexerGrammar;
import org.junit.jupiter.api.Test;

import static org.antlr.v4.TestUtils.assertEquals;


public class BufferedTokenStreamTest extends AbstractBaseTest {

  protected TokenStream createTokenStream(TokenSource src) {
    return new BufferedTokenStream(src);
  }


  @Test
  public void testFirstToken() throws Exception {
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
    // Input:  x = 3 * 0 + 2 * 0;
    CharStream input = CharStreams.fromString("x = 3 * 0 + 2 * 0;");
    LexerInterpreter lexEngine = g.createLexerInterpreter(input);
    TokenStream tokens = createTokenStream(lexEngine);

    String result = tokens.LT(1).getText();
    String expecting = "x";
    assertEquals(expecting, result);
  }

  @Test
  public void test2ndToken() throws Exception {
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
    // Input:  x = 3 * 0 + 2 * 0;
    CharStream input = CharStreams.fromString("x = 3 * 0 + 2 * 0;");
    LexerInterpreter lexEngine = g.createLexerInterpreter(input);
    TokenStream tokens = createTokenStream(lexEngine);

    String result = tokens.LT(2).getText();
    String expecting = " ";
    assertEquals(expecting, result);
  }

  @Test
  public void testCompleteBuffer() throws Exception {
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
    // Input:  x = 3 * 0 + 2 * 0;
    CharStream input = CharStreams.fromString("x = 3 * 0 + 2 * 0;");
    LexerInterpreter lexEngine = g.createLexerInterpreter(input);
    TokenStream tokens = createTokenStream(lexEngine);

    int i = 1;
    Token t = tokens.LT(i);
    while (t.getType() != Token.EOF) {
      i++;
      t = tokens.LT(i);
    }
    tokens.LT(i++); // push it past end
    tokens.LT(i);

    String result = tokens.getText();
    String expecting = "x = 3 * 0 + 2 * 0;";
    assertEquals(expecting, result);
  }

  @Test
  public void testCompleteBufferAfterConsuming() throws Exception {
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
    // Input:  x = 3 * 0 + 2 * 0;
    CharStream input = CharStreams.fromString("x = 3 * 0 + 2 * 0;");
    LexerInterpreter lexEngine = g.createLexerInterpreter(input);
    TokenStream tokens = createTokenStream(lexEngine);

    Token t = tokens.LT(1);
    while (t.getType() != Token.EOF) {
      tokens.consume();
      t = tokens.LT(1);
    }

    String result = tokens.getText();
    String expecting = "x = 3 * 0 + 2 * 0;";
    assertEquals(expecting, result);
  }

  @Test
  public void testLookback() throws Exception {
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
    // Input:  x = 3 * 0 + 2 * 0;
    CharStream input = CharStreams.fromString("x = 3 * 0 + 2 * 0;");
    LexerInterpreter lexEngine = g.createLexerInterpreter(input);
    TokenStream tokens = createTokenStream(lexEngine);

    tokens.consume(); // get x into buffer
    Token t = tokens.LT(-1);
    assertEquals("x", t.getText());

    tokens.consume();
    tokens.consume(); // consume '='
    t = tokens.LT(-3);
    assertEquals("x", t.getText());
    t = tokens.LT(-2);
    assertEquals(" ", t.getText());
    t = tokens.LT(-1);
    assertEquals("=", t.getText());
  }

}
