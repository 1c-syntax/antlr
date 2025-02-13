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

import org.antlr.v4.codegen.UnicodeEscapes;
import org.junit.jupiter.api.Test;

import static org.antlr.v4.TestUtils.assertEquals;

public class UnicodeEscapesTest {
  @Test
  public void latinJavaEscape() {
    StringBuilder sb = new StringBuilder();
    UnicodeEscapes.appendJavaStyleEscapedCodePoint(0x0061, sb);
    assertEquals("\\u0061", sb.toString());
  }

  @Test
  public void bmpJavaEscape() {
    StringBuilder sb = new StringBuilder();
    UnicodeEscapes.appendJavaStyleEscapedCodePoint(0xABCD, sb);
    assertEquals("\\uABCD", sb.toString());
  }

  @Test
  public void smpJavaEscape() {
    StringBuilder sb = new StringBuilder();
    UnicodeEscapes.appendJavaStyleEscapedCodePoint(0x1F4A9, sb);
    assertEquals("\\uD83D\\uDCA9", sb.toString());
  }
}
