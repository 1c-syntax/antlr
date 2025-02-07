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
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.tree.xpath.XPathLexer;
import org.junit.jupiter.api.Test;

import static org.antlr.v4.TestUtils.assertEquals;

/**
 * This class contains tests for specific API functionality in {@link TokenStream} and derived types.
 */
public class TokenStreamTest {

  /**
   * This is a targeted regression test for antlr/antlr4#1584 ({@link BufferedTokenStream} cannot be reused after EOF).
   */
  @Test
  public void testBufferedTokenStreamReuseAfterFill() {
    CharStream firstInput = CharStreams.fromString("A");
    BufferedTokenStream tokenStream = new BufferedTokenStream(new XPathLexer(firstInput));
    tokenStream.fill();
    assertEquals(2, tokenStream.size());
    assertEquals(XPathLexer.TOKEN_REF, tokenStream.get(0).getType());
    assertEquals(Token.EOF, tokenStream.get(1).getType());

    CharStream secondInput = CharStreams.fromString("A/");
    tokenStream.setTokenSource(new XPathLexer(secondInput));
    tokenStream.fill();
    assertEquals(3, tokenStream.size());
    assertEquals(XPathLexer.TOKEN_REF, tokenStream.get(0).getType());
    assertEquals(XPathLexer.ROOT, tokenStream.get(1).getType());
    assertEquals(Token.EOF, tokenStream.get(2).getType());
  }

}
