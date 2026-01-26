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

import static org.antlr.v4.TestUtils.assertEquals;

public class EscapeSequenceParsingTest {
  @Test
  void testParseEmpty() {
    assertEquals(
      EscapeSequenceParsing.Result.Type.INVALID,
      EscapeSequenceParsing.parseEscape("", 0).type);
  }

  @Test
  void testParseJustBackslash() {
    assertEquals(
      EscapeSequenceParsing.Result.Type.INVALID,
      EscapeSequenceParsing.parseEscape("\\", 0).type);
  }

  @Test
  void testParseInvalidEscape() {
    assertEquals(
      EscapeSequenceParsing.Result.Type.INVALID,
      EscapeSequenceParsing.parseEscape("\\z", 0).type);
  }

  @Test
  void testParseNewline() {
    assertEquals(
      new Result(Result.Type.CODE_POINT, '\n', IntervalSet.EMPTY_SET, 0, 2),
      EscapeSequenceParsing.parseEscape("\\n", 0));
  }

  @Test
  void testParseTab() {
    assertEquals(
      new Result(Result.Type.CODE_POINT, '\t', IntervalSet.EMPTY_SET, 0, 2),
      EscapeSequenceParsing.parseEscape("\\t", 0));
  }

  @Test
  void testParseUnicodeTooShort() {
    assertEquals(
      EscapeSequenceParsing.Result.Type.INVALID,
      EscapeSequenceParsing.parseEscape("\\uABC", 0).type);
  }

  @Test
  void testParseUnicodeBMP() {
    assertEquals(
      new Result(Result.Type.CODE_POINT, 0xABCD, IntervalSet.EMPTY_SET, 0, 6),
      EscapeSequenceParsing.parseEscape("\\uABCD", 0));
  }

  @Test
  void testParseUnicodeSMPTooShort() {
    assertEquals(
      EscapeSequenceParsing.Result.Type.INVALID,
      EscapeSequenceParsing.parseEscape("\\u{}", 0).type);
  }

  @Test
  void testParseUnicodeSMPMissingCloseBrace() {
    assertEquals(
      EscapeSequenceParsing.Result.Type.INVALID,
      EscapeSequenceParsing.parseEscape("\\u{12345", 0).type);
  }

  @Test
  void testParseUnicodeTooBig() {
    assertEquals(
      EscapeSequenceParsing.Result.Type.INVALID,
      EscapeSequenceParsing.parseEscape("\\u{110000}", 0).type);
  }

  @Test
  void testParseUnicodeSMP() {
    assertEquals(
      new Result(Result.Type.CODE_POINT, 0x10ABCD, IntervalSet.EMPTY_SET, 0, 10),
      EscapeSequenceParsing.parseEscape("\\u{10ABCD}", 0));
  }

  @Test
  void testParseUnicodePropertyTooShort() {
    assertEquals(
      EscapeSequenceParsing.Result.Type.INVALID,
      EscapeSequenceParsing.parseEscape("\\p{}", 0).type);
  }

  @Test
  void testParseUnicodePropertyMissingCloseBrace() {
    assertEquals(
      EscapeSequenceParsing.Result.Type.INVALID,
      EscapeSequenceParsing.parseEscape("\\p{1234", 0).type);
  }

  @Test
  void testParseUnicodeProperty() {
    assertEquals(
      new Result(Result.Type.PROPERTY, -1, IntervalSet.of(66560, 66639), 0, 11),
      EscapeSequenceParsing.parseEscape("\\p{Deseret}", 0));
  }

  @Test
  void testParseUnicodePropertyInvertedTooShort() {
    assertEquals(
      EscapeSequenceParsing.Result.Type.INVALID,
      EscapeSequenceParsing.parseEscape("\\P{}", 0).type);
  }

  @Test
  void testParseUnicodePropertyInvertedMissingCloseBrace() {
    assertEquals(
      EscapeSequenceParsing.Result.Type.INVALID,
      EscapeSequenceParsing.parseEscape("\\P{Deseret", 0).type);
  }

  @Test
  void testParseUnicodePropertyInverted() {
    IntervalSet expected = IntervalSet.of(0, 66559);
    expected.add(66640, Character.MAX_CODE_POINT);
    assertEquals(
      new Result(Result.Type.PROPERTY, -1, expected, 0, 11),
      EscapeSequenceParsing.parseEscape("\\P{Deseret}", 0));
  }
}
