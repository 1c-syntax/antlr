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

import lombok.experimental.UtilityClass;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.Objects;

/**
 * @author Sam Harwell
 */
@NullMarked
@UtilityClass
public final class Tuple {

  public <T1, T2> Pair<@Nullable T1, @Nullable T2> create(@Nullable T1 item1, @Nullable T2 item2) {
    return new Pair<>(item1, item2);
  }

  public <T1, T2, T3> Tuple3<T1, T2, T3> create(T1 item1, T2 item2, T3 item3) {
    return new Tuple3<>(item1, item2, item3);
  }

  /*package*/
  boolean equals(@Nullable Object x, @Nullable Object y) {
    return Objects.equals(x, y);
  }
}
