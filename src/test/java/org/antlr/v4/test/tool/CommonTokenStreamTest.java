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
import org.antlr.v4.runtime.CommonToken;
import org.antlr.v4.runtime.CommonTokenFactory;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.IntStream;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenFactory;
import org.antlr.v4.runtime.TokenSource;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.WritableToken;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class CommonTokenStreamTest extends BufferedTokenStreamTest {

  @Override
  protected TokenStream createTokenStream(TokenSource src) {
    return new CommonTokenStream(src);
  }

  @Test
  void testOffChannel() {
    TokenSource lexer = // simulate input " x =34  ;\n"
      new TokenSource() {
        int i = 0;

        final WritableToken[] tokens = {
          new CommonToken(1, " ") {{
            channel = Lexer.HIDDEN;
          }},
          new CommonToken(1, "x"),
          new CommonToken(1, " ") {{
            channel = Lexer.HIDDEN;
          }},
          new CommonToken(1, "="),
          new CommonToken(1, "34"),
          new CommonToken(1, " ") {{
            channel = Lexer.HIDDEN;
          }},
          new CommonToken(1, " ") {{
            channel = Lexer.HIDDEN;
          }},
          new CommonToken(1, ";"),
          new CommonToken(1, "\n") {{
            channel = Lexer.HIDDEN;
          }},
          new CommonToken(Token.EOF, "")
        };

        @Override
        public Token nextToken() {
          return tokens[i++];
        }

        @Override
        public String getSourceName() {
          return "test";
        }

        @Override
        public int getCharPositionInLine() {
          return 0;
        }

        @Override
        public int getLine() {
          return 0;
        }

        @Override
        public CharStream getInputStream() {
          return null;
        }

        @Override
        public TokenFactory getTokenFactory() {
          return CommonTokenFactory.DEFAULT;
        }

        @Override
        public void setTokenFactory(TokenFactory factory) {
        }
      };

    CommonTokenStream tokens = new CommonTokenStream(lexer);

    assertThat(tokens.LT(1).getText()).isEqualTo("x"); // must skip first off channel token
    tokens.consume();
    assertThat(tokens.LT(1).getText()).isEqualTo("=");
    assertThat(tokens.LT(-1).getText()).isEqualTo("x");

    tokens.consume();
    assertThat(tokens.LT(1).getText()).isEqualTo("34");
    assertThat(tokens.LT(-1).getText()).isEqualTo("=");

    tokens.consume();
    assertThat(tokens.LT(1).getText()).isEqualTo(";");
    assertThat(tokens.LT(-1).getText()).isEqualTo("34");

    tokens.consume();
    assertThat(tokens.LA(1)).isEqualTo(Token.EOF);
    assertThat(tokens.LT(-1).getText()).isEqualTo(";");

    assertThat(tokens.LT(-2).getText()).isEqualTo("34");
    assertThat(tokens.LT(-3).getText()).isEqualTo("=");
    assertThat(tokens.LT(-4).getText()).isEqualTo("x");
  }

  @Test
  void testFetchOffChannel() {
    TokenSource lexer = // simulate input " x =34  ; \n"
      // token indexes   01234 56789
      new TokenSource() {
        int i = 0;
        final WritableToken[] tokens = {
          new CommonToken(1, " ") {{
            channel = Lexer.HIDDEN;
          }}, // 0
          new CommonToken(1, "x"),                // 1
          new CommonToken(1, " ") {{
            channel = Lexer.HIDDEN;
          }},  // 2
          new CommonToken(1, "="),                // 3
          new CommonToken(1, "34"),              // 4
          new CommonToken(1, " ") {{
            channel = Lexer.HIDDEN;
          }},  // 5
          new CommonToken(1, " ") {{
            channel = Lexer.HIDDEN;
          }}, // 6
          new CommonToken(1, ";"),                // 7
          new CommonToken(1, " ") {{
            channel = Lexer.HIDDEN;
          }},// 8
          new CommonToken(1, "\n") {{
            channel = Lexer.HIDDEN;
          }},// 9
          new CommonToken(Token.EOF, "")            // 10
        };

        @Override
        public Token nextToken() {
          return tokens[i++];
        }

        @Override
        public String getSourceName() {
          return "test";
        }

        @Override
        public int getCharPositionInLine() {
          return 0;
        }

        @Override
        public int getLine() {
          return 0;
        }

        @Override
        public CharStream getInputStream() {
          return null;
        }

        @Override
        public TokenFactory getTokenFactory() {
          return CommonTokenFactory.DEFAULT;
        }

        @Override
        public void setTokenFactory(TokenFactory factory) {
        }
      };

    CommonTokenStream tokens = new CommonTokenStream(lexer);
    tokens.fill();
    assertThat(tokens.getHiddenTokensToLeft(0)).isNull();
    assertThat(tokens.getHiddenTokensToRight(0)).isNull();

    assertThat(tokens.getHiddenTokensToLeft(1)).hasToString("[[@0,0:0=' ',<1>,channel=1,0:-1]]");
    assertThat(tokens.getHiddenTokensToRight(1)).hasToString("[[@2,0:0=' ',<1>,channel=1,0:-1]]");

    assertThat(tokens.getHiddenTokensToLeft(2)).isNull();
    assertThat(tokens.getHiddenTokensToRight(2)).isNull();

    assertThat(tokens.getHiddenTokensToLeft(3)).hasToString("[[@2,0:0=' ',<1>,channel=1,0:-1]]");
    assertThat(tokens.getHiddenTokensToRight(3)).isNull();

    assertThat(tokens.getHiddenTokensToLeft(4)).isNull();
    assertThat(tokens.getHiddenTokensToRight(4))
      .hasToString("[[@5,0:0=' ',<1>,channel=1,0:-1], [@6,0:0=' ',<1>,channel=1,0:-1]]");

    assertThat(tokens.getHiddenTokensToLeft(5)).isNull();
    assertThat(tokens.getHiddenTokensToRight(5)).hasToString("[[@6,0:0=' ',<1>,channel=1,0:-1]]");

    assertThat(tokens.getHiddenTokensToLeft(6)).hasToString("[[@5,0:0=' ',<1>,channel=1,0:-1]]");
    assertThat(tokens.getHiddenTokensToRight(6)).isNull();

    assertThat(tokens.getHiddenTokensToLeft(7))
      .hasToString("[[@5,0:0=' ',<1>,channel=1,0:-1], [@6,0:0=' ',<1>,channel=1,0:-1]]");
    assertThat(tokens.getHiddenTokensToRight(7))
      .hasToString("[[@8,0:0=' ',<1>,channel=1,0:-1], [@9,0:0='\\n',<1>,channel=1,0:-1]]");

    assertThat(tokens.getHiddenTokensToLeft(8)).isNull();
    assertThat(tokens.getHiddenTokensToRight(8)).hasToString("[[@9,0:0='\\n',<1>,channel=1,0:-1]]");

    assertThat(tokens.getHiddenTokensToLeft(9)).hasToString("[[@8,0:0=' ',<1>,channel=1,0:-1]]");
    assertThat(tokens.getHiddenTokensToRight(9)).isNull();
  }

  @Test
  void testSingleEOF() {
    TokenSource lexer = new TokenSource() {

      @Override
      public Token nextToken() {
        return new CommonToken(Token.EOF);
      }

      @Override
      public int getLine() {
        return 0;
      }

      @Override
      public int getCharPositionInLine() {
        return 0;
      }

      @Override
      public CharStream getInputStream() {
        return null;
      }

      @Override
      public String getSourceName() {
        return IntStream.UNKNOWN_SOURCE_NAME;
      }

      @Override
      public TokenFactory getTokenFactory() {
        throw new UnsupportedOperationException("Not supported yet.");
      }

      @Override
      public void setTokenFactory(TokenFactory factory) {
        throw new UnsupportedOperationException("Not supported yet.");
      }
    };

    CommonTokenStream tokens = new CommonTokenStream(lexer);
    tokens.fill();

    assertThat(tokens.LA(1)).isEqualTo(Token.EOF);
    assertThat(tokens.index()).isZero();
    assertThat(tokens.size()).isEqualTo(1);
  }
}
