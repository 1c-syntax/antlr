/*
 * This file is a part of ANTLR.
 *
 * Copyright (c) 2012-2025 The ANTLR Project. All rights reserved.
 * Copyright (c) 2025-2026 Valery Maximov <maximovvalery@gmail.com> and contributors
 *
 * Use of this file is governed by the BSD-3-Clause license that
 * can be found in the LICENSE.txt file in the project root.
 */
package org.antlr.v4.runtime.misc;

import lombok.Value;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

/**
 * @author Sam Harwell
 */
@NullMarked
@Value
public class Tuple3<T1 extends @Nullable Object, T2 extends @Nullable Object, T3 extends @Nullable Object> {
  @Nullable T1 item1;
  @Nullable T2 item2;
  @Nullable T3 item3;

  public Tuple3(@Nullable T1 item1, @Nullable T2 item2, @Nullable T3 item3) {
    this.item1 = item1;
    this.item2 = item2;
    this.item3 = item3;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    } else if (!(obj instanceof Tuple3<?, ?, ?>)) {
      return false;
    }

    Tuple3<?, ?, ?> other = (Tuple3<?, ?, ?>) obj;
    return Tuple.equals(this.item1, other.item1)
      && Tuple.equals(this.item2, other.item2)
      && Tuple.equals(this.item3, other.item3);
  }

  @Override
  public int hashCode() {
    int hash = 5;
    hash = 79 * hash + (this.item1 != null ? this.item1.hashCode() : 0);
    hash = 79 * hash + (this.item2 != null ? this.item2.hashCode() : 0);
    hash = 79 * hash + (this.item3 != null ? this.item3.hashCode() : 0);
    return hash;
  }

  @Override
  public String toString() {
    return String.format("(%s, %s, %s)", item1, item2, item3);
  }

}
