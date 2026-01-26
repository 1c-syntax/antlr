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

import org.antlr.v4.misc.EscapeSequenceParsing;
import org.antlr.v4.misc.EscapeSequenceParsing.Result;
import org.antlr.v4.runtime.misc.IntervalSet;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class EscapeSequenceParsingTest {
  @Test
  void testParseEmpty() {
    assertThat(EscapeSequenceParsing.parseEscape("", 0).type).isEqualTo(
      EscapeSequenceParsing.Result.Type.INVALID);
  }

  @Test
  void testParseJustBackslash() {
    assertThat(EscapeSequenceParsing.parseEscape("\\", 0).type).isEqualTo(
      EscapeSequenceParsing.Result.Type.INVALID);
  }

  @Test
  void testParseInvalidEscape() {
    assertThat(EscapeSequenceParsing.parseEscape("\\z", 0).type).isEqualTo(
      EscapeSequenceParsing.Result.Type.INVALID);
  }

  @Test
  void testParseNewline() {
    assertThat(EscapeSequenceParsing.parseEscape("\\n", 0)).isEqualTo(
      new Result(Result.Type.CODE_POINT, '\n', IntervalSet.EMPTY_SET, 0, 2));
  }

  @Test
  void testParseTab() {
    assertThat(EscapeSequenceParsing.parseEscape("\\t", 0)).isEqualTo(
      new Result(Result.Type.CODE_POINT, '\t', IntervalSet.EMPTY_SET, 0, 2));
  }

  @Test
  void testParseUnicodeTooShort() {
    assertThat(EscapeSequenceParsing.parseEscape("\\uABC", 0).type).isEqualTo(
      EscapeSequenceParsing.Result.Type.INVALID);
  }

  @Test
  void testParseUnicodeBMP() {
    assertThat(EscapeSequenceParsing.parseEscape("\\uABCD", 0)).isEqualTo(
      new Result(Result.Type.CODE_POINT, 0xABCD, IntervalSet.EMPTY_SET, 0, 6));
  }

  @Test
  void testParseUnicodeSMPTooShort() {
    assertThat(EscapeSequenceParsing.parseEscape("\\u{}", 0).type).isEqualTo(
      EscapeSequenceParsing.Result.Type.INVALID);
  }

  @Test
  void testParseUnicodeSMPMissingCloseBrace() {
    assertThat(EscapeSequenceParsing.parseEscape("\\u{12345", 0).type).isEqualTo(
      EscapeSequenceParsing.Result.Type.INVALID);
  }

  @Test
  void testParseUnicodeTooBig() {
    assertThat(EscapeSequenceParsing.parseEscape("\\u{110000}", 0).type).isEqualTo(
      EscapeSequenceParsing.Result.Type.INVALID);
  }

  @Test
  void testParseUnicodeSMP() {
    assertThat(EscapeSequenceParsing.parseEscape("\\u{10ABCD}", 0)).isEqualTo(
      new Result(Result.Type.CODE_POINT, 0x10ABCD, IntervalSet.EMPTY_SET, 0, 10));
  }

  @Test
  void testParseUnicodePropertyTooShort() {
    assertThat(EscapeSequenceParsing.parseEscape("\\p{}", 0).type).isEqualTo(
      EscapeSequenceParsing.Result.Type.INVALID);
  }

  @Test
  void testParseUnicodePropertyMissingCloseBrace() {
    assertThat(EscapeSequenceParsing.parseEscape("\\p{1234", 0).type).isEqualTo(
      EscapeSequenceParsing.Result.Type.INVALID);
  }

  @Test
  void testParseUnicodeProperty() {
    assertThat(EscapeSequenceParsing.parseEscape("\\p{Deseret}", 0)).isEqualTo(
      new Result(Result.Type.PROPERTY, -1, IntervalSet.of(66560, 66639), 0, 11));
  }

  @Test
  void testParseUnicodePropertyInvertedTooShort() {
    assertThat(EscapeSequenceParsing.parseEscape("\\P{}", 0).type).isEqualTo(
      EscapeSequenceParsing.Result.Type.INVALID);
  }

  @Test
  void testParseUnicodePropertyInvertedMissingCloseBrace() {
    assertThat(EscapeSequenceParsing.parseEscape("\\P{Deseret", 0).type).isEqualTo(
      EscapeSequenceParsing.Result.Type.INVALID);
  }

  @Test
  void testParseUnicodePropertyInverted() {
    IntervalSet expected = IntervalSet.of(0, 66559);
    expected.add(66640, Character.MAX_CODE_POINT);
    assertThat(EscapeSequenceParsing.parseEscape("\\P{Deseret}", 0)).isEqualTo(
      new Result(Result.Type.PROPERTY, -1, expected, 0, 11));
  }
}
