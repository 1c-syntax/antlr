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

import org.antlr.v4.runtime.misc.IntegerList;
import org.antlr.v4.runtime.misc.Utils;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class IntegerListTest {
  @Test
  public void emptyListToEmptyCharArray() {
    IntegerList l = new IntegerList();
    assertThat(Utils.toCharArray(l)).isEqualTo(new char[0]);
  }

  @Test
  public void surrogateRangeIntegerToCharArray() {
    IntegerList l = new IntegerList();
    // Java allows dangling surrogates, so (currently) we do
    // as well. We could change this if desired.
    l.add(0xDC00);
    char[] expected = new char[]{0xDC00};
    assertThat(Utils.toCharArray(l)).isEqualTo(expected);
  }

  @Test
  public void unicodeBMPIntegerListToCharArray() {
    IntegerList l = new IntegerList();
    l.add(0x35);
    l.add(0x4E94);
    l.add(0xFF15);
    char[] expected = new char[]{0x35, 0x4E94, 0xFF15};
    assertThat(Utils.toCharArray(l)).isEqualTo(expected);
  }

  @Test
  public void unicodeSMPIntegerListToCharArray() {
    IntegerList l = new IntegerList();
    l.add(0x104A5);
    l.add(0x116C5);
    l.add(0x1D7FB);
    char[] expected = new char[]{0xD801, 0xDCA5, 0xD805, 0xDEC5, 0xD835, 0xDFFB};
    assertThat(Utils.toCharArray(l)).isEqualTo(expected);
  }
}
