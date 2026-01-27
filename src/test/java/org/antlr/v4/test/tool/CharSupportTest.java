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

import org.antlr.v4.misc.CharSupport;
import org.antlr.v4.runtime.misc.IntervalSet;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class CharSupportTest {

  @Test
  void testGetANTLRCharLiteralForChar() {
    assertThat(CharSupport.getANTLRCharLiteralForChar(-1)).isEqualTo("'<INVALID>'");
    assertThat(CharSupport.getANTLRCharLiteralForChar('\n')).isEqualTo("'\\n'");
    assertThat(CharSupport.getANTLRCharLiteralForChar('\\')).isEqualTo("'\\\\'");
    assertThat(CharSupport.getANTLRCharLiteralForChar('\'')).isEqualTo("'\\''");
    assertThat(CharSupport.getANTLRCharLiteralForChar('b')).isEqualTo("'b'");
    assertThat(CharSupport.getANTLRCharLiteralForChar(0xFFFF)).isEqualTo("'\\uFFFF'");
    assertThat(CharSupport.getANTLRCharLiteralForChar(0x10FFFF)).isEqualTo("'\\u{10FFFF}'");
  }

  @Test
  void testGetCharValueFromGrammarCharLiteral() {
    assertThat(CharSupport.getCharValueFromGrammarCharLiteral(null)).isEqualTo(-1);
    assertThat(CharSupport.getCharValueFromGrammarCharLiteral("")).isEqualTo(-1);
    assertThat(CharSupport.getCharValueFromGrammarCharLiteral("b")).isEqualTo(-1);
    assertThat(CharSupport.getCharValueFromGrammarCharLiteral("foo")).isEqualTo(111);
  }

  @Test
  void testGetStringFromGrammarStringLiteral() {
    assertThat(CharSupport.getStringFromGrammarStringLiteral("foo\\u{bbb")).isNull();
    assertThat(CharSupport.getStringFromGrammarStringLiteral("foo\\u{[]bb")).isNull();
    assertThat(CharSupport.getStringFromGrammarStringLiteral("foo\\u[]bb")).isNull();
    assertThat(CharSupport.getStringFromGrammarStringLiteral("foo\\ubb")).isNull();

    assertThat(CharSupport.getStringFromGrammarStringLiteral("foo\\u{bb}bb")).isEqualTo("oo»b");
  }

  @Test
  void testGetCharValueFromCharInGrammarLiteral() {
    assertThat(CharSupport.getCharValueFromCharInGrammarLiteral("f")).isEqualTo(102);

    assertThat(CharSupport.getCharValueFromCharInGrammarLiteral("' ")).isEqualTo(-1);
    assertThat(CharSupport.getCharValueFromCharInGrammarLiteral("\\ ")).isEqualTo(-1);
    assertThat(CharSupport.getCharValueFromCharInGrammarLiteral("\\'")).isEqualTo(39);
    assertThat(CharSupport.getCharValueFromCharInGrammarLiteral("\\n")).isEqualTo(10);

    assertThat(CharSupport.getCharValueFromCharInGrammarLiteral("foobar")).isEqualTo(-1);
    assertThat(CharSupport.getCharValueFromCharInGrammarLiteral("\\u1234")).isEqualTo(4660);
    assertThat(CharSupport.getCharValueFromCharInGrammarLiteral("\\u{12}")).isEqualTo(18);

    assertThat(CharSupport.getCharValueFromCharInGrammarLiteral("\\u{")).isEqualTo(-1);
    assertThat(CharSupport.getCharValueFromCharInGrammarLiteral("foo")).isEqualTo(-1);
  }

  @Test
  void testParseHexValue() {
    assertThat(CharSupport.parseHexValue("foobar", -1, 3)).isEqualTo(-1);
    assertThat(CharSupport.parseHexValue("foobar", 1, -1)).isEqualTo(-1);
    assertThat(CharSupport.parseHexValue("foobar", 1, 3)).isEqualTo(-1);
    assertThat(CharSupport.parseHexValue("123456", 1, 3)).isEqualTo(35);
  }

  @Test
  void testCapitalize() {
    assertThat(CharSupport.capitalize("foo")).isEqualTo("Foo");
  }

  @Test
  void testGetIntervalSetEscapedString() {
    assertThat(CharSupport.getIntervalSetEscapedString(new IntervalSet())).isEmpty();
    assertThat(CharSupport.getIntervalSetEscapedString(new IntervalSet(0))).isEqualTo("'\\u0000'");
    assertThat(CharSupport.getIntervalSetEscapedString(new IntervalSet(3, 1, 2))).isEqualTo("'\\u0001'..'\\u0003'");
  }

  @Test
  void testGetRangeEscapedString() {
    assertThat(CharSupport.getRangeEscapedString(2, 4)).isEqualTo("'\\u0002'..'\\u0004'");
    assertThat(CharSupport.getRangeEscapedString(2, 2)).isEqualTo("'\\u0002'");
  }
}
