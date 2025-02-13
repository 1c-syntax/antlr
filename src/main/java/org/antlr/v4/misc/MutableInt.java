/*
 * This file is a part of ANTLR.
 *
 * Copyright (c) 2012-2025 The ANTLR Project. All rights reserved.
 * Copyright (c) 2025 Valery Maximov <maximovvalery@gmail.com> and contributors
 *
 * Use of this file is governed by the BSD-3-Clause license that
 * can be found in the LICENSE.txt file in the project root.
 */
package org.antlr.v4.misc;

@SuppressWarnings("serial")
public class MutableInt extends Number implements Comparable<Number> {
  public int v;

  public MutableInt(int v) {
    this.v = v;
  }

  @Override
  public boolean equals(Object o) {
    if (o instanceof Number) return v == ((Number) o).intValue();
    return false;
  }

  @Override
  public int hashCode() {
    return v;
  }

  @Override
  public int compareTo(Number o) {
    return v - o.intValue();
  }

  @Override
  public int intValue() {
    return v;
  }

  @Override
  public long longValue() {
    return v;
  }

  @Override
  public float floatValue() {
    return v;
  }

  @Override
  public double doubleValue() {
    return v;
  }

  @Override
  public String toString() {
    return String.valueOf(v);
  }
}
