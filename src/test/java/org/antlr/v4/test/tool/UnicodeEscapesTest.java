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

import org.antlr.v4.codegen.UnicodeEscapes;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class UnicodeEscapesTest {
  @Test
  void latinJavaEscape() {
    StringBuilder sb = new StringBuilder();
    UnicodeEscapes.appendJavaStyleEscapedCodePoint(0x0061, sb);
    assertThat(sb).hasToString("\\u0061");
  }

  @Test
  void bmpJavaEscape() {
    StringBuilder sb = new StringBuilder();
    UnicodeEscapes.appendJavaStyleEscapedCodePoint(0xABCD, sb);
    assertThat(sb).hasToString("\\uABCD");
  }

  @Test
  void smpJavaEscape() {
    StringBuilder sb = new StringBuilder();
    UnicodeEscapes.appendJavaStyleEscapedCodePoint(0x1F4A9, sb);
    assertThat(sb).hasToString("\\uD83D\\uDCA9");
  }
}
