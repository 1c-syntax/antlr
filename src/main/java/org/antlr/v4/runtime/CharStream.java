/*
 * This file is a part of ANTLR.
 *
 * Copyright (c) 2012-2025 The ANTLR Project. All rights reserved.
 * Copyright (c) 2025 Valery Maximov <maximovvalery@gmail.com> and contributors
 *
 * Use of this file is governed by the BSD-3-Clause license that
 * can be found in the LICENSE.txt file in the project root.
 */
package org.antlr.v4.runtime;

import org.antlr.v4.runtime.misc.Interval;
import org.antlr.v4.runtime.misc.NotNull;

/**
 * A source of characters for an ANTLR lexer.
 */
public interface CharStream extends IntStream {
  /**
   * This method returns the text for a range of characters within this input
   * stream. This method is guaranteed to not throw an exception if the
   * specified {@code interval} lies entirely within a marked range. For more
   * information about marked ranges, see {@link IntStream#mark}.
   *
   * @param interval an interval within the stream
   * @return the text of the specified interval
   * @throws NullPointerException          if {@code interval} is {@code null}
   * @throws IllegalArgumentException      if {@code interval.a < 0}, or if
   *                                       {@code interval.b < interval.a - 1}, or if {@code interval.b} lies at or
   *                                       past the end of the stream
   * @throws UnsupportedOperationException if the stream does not support
   *                                       getting the text of the specified interval
   */
  @NotNull
  String getText(@NotNull Interval interval);
}
