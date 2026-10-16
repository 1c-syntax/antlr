/*
 * This file is a part of ANTLR.
 *
 * Copyright (c) 2012-2025 The ANTLR Project. All rights reserved.
 * Copyright (c) 2025-2026 Valery Maximov <maximovvalery@gmail.com> and contributors
 *
 * Use of this file is governed by the BSD-3-Clause license that
 * can be found in the LICENSE.txt file in the project root.
 */
package org.antlr.v4.runtime;

import org.antlr.v4.test.runtime.java.api.TestIncrementalBasicLexer;
import org.antlr.v4.test.runtime.java.api.TestIncrementalBasicParser;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Тесты для Tokenizer
 */
class TokenizerTest {

  @ParameterizedTest
  @ValueSource(strings = {
    "",
    "\uFEFF",
    "foo 5555 bar",
    "\uFEFFfoo 5555 bar",
    "\uFEFF\uFEFFfoo",
    "foo Привет 5555\r\nbar",
    "foo \uD83D\uDE00 bar\n12"
  })
  void tokensFromStringMatchTokensFromBytes(String text) {
    var fromString = new BasicTokenizer(text);
    var fromBytes = new BasicTokenizer(utf8(text));

    assertThat(describe(fromString.getTokens())).isEqualTo(describe(fromBytes.getTokens()));
  }

  @Test
  void byteOrderMarkIsNotPartOfText() {
    var tokenizer = new BasicTokenizer("\uFEFFfoo 5555");

    var first = tokenizer.getTokens().get(0);
    assertThat(first.getText()).isEqualTo("foo");
    assertThat(first.getStartIndex()).isZero();
    assertThat(first.getCharPositionInLine()).isZero();
  }

  @Test
  void rebuildFromStringMatchesRebuildFromBytes() {
    var fromString = new BasicTokenizer("foo 5555");
    var fromBytes = new BasicTokenizer("foo 5555");
    var text = "\uFEFFfoo 5555 Привет bar";

    fromString.rebuild(text);
    fromBytes.rebuild(utf8(text));

    assertThat(describe(fromString.getTokens())).isEqualTo(describe(fromBytes.getTokens()));
    assertThat(fromString.getAst().toStringTree()).isEqualTo(fromBytes.getAst().toStringTree());
  }

  private static InputStream utf8(String text) {
    return new ByteArrayInputStream(text.getBytes(StandardCharsets.UTF_8));
  }

  private static List<String> describe(List<Token> tokens) {
    return tokens.stream()
      .map(token -> token.getType() + ":" + token.getChannel()
        + ":" + token.getStartIndex() + ":" + token.getStopIndex()
        + ":" + token.getLine() + ":" + token.getCharPositionInLine()
        + ":" + token.getText())
      .toList();
  }

  private static class BasicTokenizer
    extends Tokenizer<TestIncrementalBasicParser.ProgramContext, TestIncrementalBasicParser> {

    BasicTokenizer(String content) {
      super(content, new TestIncrementalBasicLexer(CharStreams.fromString("")), TestIncrementalBasicParser.class);
    }

    BasicTokenizer(InputStream content) {
      super(content, new TestIncrementalBasicLexer(CharStreams.fromString("")), TestIncrementalBasicParser.class);
    }

    @Override
    protected TestIncrementalBasicParser.ProgramContext rootAST() {
      return parser.program();
    }
  }
}
