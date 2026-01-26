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

import org.antlr.v4.runtime.misc.Interval;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class CodePointCharStreamTest {
  @Test
  void emptyBytesHasSize0() {
    CodePointCharStream s = CharStreams.fromString("");
    assertThat(s.size()).isEqualTo(0);
    assertThat(s.index()).isEqualTo(0);
    assertThat(s.toString()).isEqualTo("");
  }

  @Test
  void emptyBytesLookAheadReturnsEOF() {
    CodePointCharStream s = CharStreams.fromString("");
    assertThat(s.LA(1)).isEqualTo(IntStream.EOF);
    assertThat(s.index()).isEqualTo(0);
  }

  @Test
  void singleLatinCodePointHasSize1() {
    CodePointCharStream s = CharStreams.fromString("X");
    assertThat(s.size()).isEqualTo(1);
  }

  @Test
  void consumingSingleLatinCodePointShouldMoveIndex() {
    CodePointCharStream s = CharStreams.fromString("X");
    assertThat(s.index()).isEqualTo(0);
    s.consume();
    assertThat(s.index()).isEqualTo(1);
  }

  @Test
  void singleLatinCodePointLookAheadShouldReturnCodePoint() {
    CodePointCharStream s = CharStreams.fromString("X");
    assertThat(s.LA(1)).isEqualTo('X');
    assertThat(s.index()).isEqualTo(0);
  }

  @Test
  void multipleLatinCodePointsLookAheadShouldReturnCodePoints() {
    CodePointCharStream s = CharStreams.fromString("XYZ");
    assertThat(s.LA(1)).isEqualTo('X');
    assertThat(s.index()).isEqualTo(0);
    assertThat(s.LA(2)).isEqualTo('Y');
    assertThat(s.index()).isEqualTo(0);
    assertThat(s.LA(3)).isEqualTo('Z');
    assertThat(s.index()).isEqualTo(0);
  }

  @Test
  void singleLatinCodePointLookAheadPastEndShouldReturnEOF() {
    CodePointCharStream s = CharStreams.fromString("X");
    assertThat(s.LA(2)).isEqualTo(IntStream.EOF);
  }

  @Test
  void singleCJKCodePointHasSize1() {
    CodePointCharStream s = CharStreams.fromString("\u611B");
    assertThat(s.size()).isEqualTo(1);
    assertThat(s.index()).isEqualTo(0);
  }

  @Test
  void consumingSingleCJKCodePointShouldMoveIndex() {
    CodePointCharStream s = CharStreams.fromString("\u611B");
    assertThat(s.index()).isEqualTo(0);
    s.consume();
    assertThat(s.index()).isEqualTo(1);
  }

  @Test
  void singleCJKCodePointLookAheadShouldReturnCodePoint() {
    CodePointCharStream s = CharStreams.fromString("\u611B");
    assertThat(s.LA(1)).isEqualTo(0x611B);
    assertThat(s.index()).isEqualTo(0);
  }

  @Test
  void singleCJKCodePointLookAheadPastEndShouldReturnEOF() {
    CodePointCharStream s = CharStreams.fromString("\u611B");
    assertThat(s.LA(2)).isEqualTo(IntStream.EOF);
    assertThat(s.index()).isEqualTo(0);
  }

  @Test
  void singleEmojiCodePointHasSize1() {
    CodePointCharStream s = CharStreams.fromString(
      new StringBuilder().appendCodePoint(0x1F4A9).toString());
    assertThat(s.size()).isEqualTo(1);
    assertThat(s.index()).isEqualTo(0);
  }

  @Test
  void consumingSingleEmojiCodePointShouldMoveIndex() {
    CodePointCharStream s = CharStreams.fromString(
      new StringBuilder().appendCodePoint(0x1F4A9).toString());
    assertThat(s.index()).isEqualTo(0);
    s.consume();
    assertThat(s.index()).isEqualTo(1);
  }

  @Test
  void singleEmojiCodePointLookAheadShouldReturnCodePoint() {
    CodePointCharStream s = CharStreams.fromString(
      new StringBuilder().appendCodePoint(0x1F4A9).toString());
    assertThat(s.LA(1)).isEqualTo(0x1F4A9);
    assertThat(s.index()).isEqualTo(0);
  }

  @Test
  void singleEmojiCodePointLookAheadPastEndShouldReturnEOF() {
    CodePointCharStream s = CharStreams.fromString(
      new StringBuilder().appendCodePoint(0x1F4A9).toString());
    assertThat(s.LA(2)).isEqualTo(IntStream.EOF);
    assertThat(s.index()).isEqualTo(0);
  }

  @Test
  void getTextWithLatin() {
    CodePointCharStream s = CharStreams.fromString("0123456789");
    assertThat(s.getText(Interval.of(3, 7))).isEqualTo("34567");
  }

  @Test
  void getTextWithCJK() {
    CodePointCharStream s = CharStreams.fromString("01234\u40946789");
    assertThat(s.getText(Interval.of(3, 7))).isEqualTo("34\u409467");
  }

  @Test
  void getTextWithEmoji() {
    CodePointCharStream s = CharStreams.fromString(
      new StringBuilder("01234")
        .appendCodePoint(0x1F522)
        .append("6789")
        .toString());
    assertThat(s.getText(Interval.of(3, 7))).isEqualTo("34\uD83D\uDD2267");
  }

  @Test
  void toStringWithLatin() {
    CodePointCharStream s = CharStreams.fromString("0123456789");
    assertThat(s.toString()).isEqualTo("0123456789");
  }

  @Test
  void toStringWithCJK() {
    CodePointCharStream s = CharStreams.fromString("01234\u40946789");
    assertThat(s.toString()).isEqualTo("01234\u40946789");
  }

  @Test
  void toStringWithEmoji() {
    CodePointCharStream s = CharStreams.fromString(
      new StringBuilder("01234")
        .appendCodePoint(0x1F522)
        .append("6789")
        .toString());
    assertThat(s.toString()).isEqualTo("01234\uD83D\uDD226789");
  }

  @Test
  void lookAheadWithLatin() {
    CodePointCharStream s = CharStreams.fromString("0123456789");
    assertThat(s.LA(6)).isEqualTo('5');
  }

  @Test
  void lookAheadWithCJK() {
    CodePointCharStream s = CharStreams.fromString("01234\u40946789");
    assertThat(s.LA(6)).isEqualTo(0x4094);
  }

  @Test
  void lookAheadWithEmoji() {
    CodePointCharStream s = CharStreams.fromString(
      new StringBuilder("01234")
        .appendCodePoint(0x1F522)
        .append("6789")
        .toString());
    assertThat(s.LA(6)).isEqualTo(0x1F522);
  }

  @Test
  void seekWithLatin() {
    CodePointCharStream s = CharStreams.fromString("0123456789");
    s.seek(5);
    assertThat(s.LA(1)).isEqualTo(53);
  }

  @Test
  void seekWithCJK() {
    CodePointCharStream s = CharStreams.fromString("01234\u40946789");
    s.seek(5);
    assertThat(s.LA(1)).isEqualTo(0x4094);
  }

  @Test
  void seekWithEmoji() {
    CodePointCharStream s = CharStreams.fromString(
      new StringBuilder("01234")
        .appendCodePoint(0x1F522)
        .append("6789")
        .toString());
    s.seek(5);
    assertThat(s.LA(1)).isEqualTo(0x1F522);
  }

  @Test
  void lookBehindWithLatin() {
    CodePointCharStream s = CharStreams.fromString("0123456789");
    s.seek(6);
    assertThat(s.LA(-1)).isEqualTo('5');
  }

  @Test
  void lookBehindWithCJK() {
    CodePointCharStream s = CharStreams.fromString("01234\u40946789");
    s.seek(6);
    assertThat(s.LA(-1)).isEqualTo(0x4094);
  }

  @Test
  void lookBehindWithEmoji() {
    CodePointCharStream s = CharStreams.fromString(
      new StringBuilder("01234")
        .appendCodePoint(0x1F522)
        .append("6789")
        .toString());
    s.seek(6);
    assertThat(s.LA(-1)).isEqualTo(0x1F522);
  }

  @Test
  void asciiContentsShouldUse8BitBuffer() {
    CodePointCharStream s = CharStreams.fromString("hello");
    assertThat(s.getInternalStorage() instanceof byte[]).isTrue();
    assertThat(s.size()).isEqualTo(5);
  }

  @Test
  void bmpContentsShouldUse16BitBuffer() {
    CodePointCharStream s = CharStreams.fromString("hello \u4E16\u754C");
    assertThat(s.getInternalStorage() instanceof char[]).isTrue();
    assertThat(s.size()).isEqualTo(8);
  }

  @Test
  void smpContentsShouldUse32BitBuffer() {
    CodePointCharStream s = CharStreams.fromString("hello \uD83C\uDF0D");
    assertThat(s.getInternalStorage() instanceof int[]).isTrue();
    assertThat(s.size()).isEqualTo(7);
  }
}
