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

import org.antlr.v4.runtime.BufferedTokenStream;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.tree.xpath.XPathLexer;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * This class contains tests for specific API functionality in {@link TokenStream} and derived types.
 */
class TokenStreamTest {

  /**
   * This is a targeted regression test for antlr/antlr4#1584 ({@link BufferedTokenStream} cannot be reused after EOF).
   */
  @Test
  void testBufferedTokenStreamReuseAfterFill() {
    CharStream firstInput = CharStreams.fromString("A");
    BufferedTokenStream tokenStream = new BufferedTokenStream(new XPathLexer(firstInput));
    tokenStream.fill();
    assertThat(tokenStream.size()).isEqualTo(2);
    assertThat(tokenStream.get(0).getType()).isEqualTo(XPathLexer.TOKEN_REF);
    assertThat(tokenStream.get(1).getType()).isEqualTo(Token.EOF);

    CharStream secondInput = CharStreams.fromString("A/");
    tokenStream.setTokenSource(new XPathLexer(secondInput));
    tokenStream.fill();
    assertThat(tokenStream.size()).isEqualTo(3);
    assertThat(tokenStream.get(0).getType()).isEqualTo(XPathLexer.TOKEN_REF);
    assertThat(tokenStream.get(1).getType()).isEqualTo(XPathLexer.ROOT);
    assertThat(tokenStream.get(2).getType()).isEqualTo(Token.EOF);
  }

}
