/*
 * This file is a part of ANTLR.
 *
 * Copyright (c) 2012-2025 The ANTLR Project. All rights reserved.
 * Copyright (c) 2025-2026 Valery Maximov <maximovvalery@gmail.com> and contributors
 *
 * Use of this file is governed by the BSD-3-Clause license that
 * can be found in the LICENSE.txt file in the project root.
 */
package org.antlr.v4.runtime;

import org.antlr.v4.runtime.misc.Interval;
import org.jspecify.annotations.Nullable;

import java.util.Arrays;

public class IncrementalTokenStream extends CommonTokenStream {
  /**
   * ANTLR looks at the same tokens alot, and this avoids recalculating the
   * interval when the position and lookahead number doesn't move.
   */
  private int lastP = -1;
  private int lastK = -1;

  /**
   * This tracks the min/max token index looked at since the value was reset. This
   * is used to track how far ahead the grammar looked, since it may be outside
   * the rule context's start/stop tokens. We need to maintain a stack of such
   * indices.
   * <p>
   * Хранится двумя примитивными {@code int[]}-стеками (вместо {@code Stack<Interval>}):
   * убирает синхронизацию legacy-{@link java.util.Stack} и аллокацию {@link Interval}
   * на каждый {@link #LT} (горячий путь — миллионы вызовов на разбор крупного модуля).
   */
  private int[] minStack = new int[16];
  private int[] maxStack = new int[16];
  private int sp;

  /**
   * Constructs a new {@link IncrementalTokenStream} using the specified token
   * source and the default token channel ({@link Token#DEFAULT_CHANNEL}).
   *
   * @param tokenSource The token source.
   */
  public IncrementalTokenStream(TokenSource tokenSource) {
    super(tokenSource);
  }

  /**
   * Constructs a new {@link IncrementalTokenStream} using the specified token
   * source and filtering tokens to the specified channel. Only tokens whose
   * {@link Token#getChannel} matches {@code channel} or have the
   * {@link Token#getType} equal to {@link Token#EOF} will be returned by the
   * token stream lookahead methods.
   *
   * @param tokenSource The token source.
   * @param channel     The channel to use for filtering tokens.
   */
  public IncrementalTokenStream(TokenSource tokenSource, int channel) {
    this(tokenSource);
    this.channel = channel;
  }

  /**
   * Push a new minimum/maximum token state.
   *
   * @param min Minimum token index
   * @param max Maximum token index
   */
  public void pushMinMax(int min, int max) {
    if (sp == minStack.length) {
      minStack = Arrays.copyOf(minStack, sp << 1);
      maxStack = Arrays.copyOf(maxStack, sp << 1);
    }
    minStack[sp] = min;
    maxStack[sp] = max;
    sp++;
  }

  /**
   * Pop the current minimum/maximum token state and return it.
   */
  public Interval popMinMax() {
    if (sp == 0) {
      throw new IndexOutOfBoundsException("Can't pop the min max state when there are 0 states");
    }
    sp--;
    return Interval.of(minStack[sp], maxStack[sp]);
  }

  /** Признак пустого стека min/max (без аллокаций). */
  public boolean isMinMaxEmpty() {
    return sp == 0;
  }

  /** Минимальный тронутый индекс на вершине стека (горячий путь, без аллокаций). */
  public int peekMinTokenIndex() {
    return minStack[sp - 1];
  }

  /** Максимальный тронутый индекс на вершине стека (горячий путь, без аллокаций). */
  public int peekMaxTokenIndex() {
    return maxStack[sp - 1];
  }

  /** Снять вершину стека min/max без создания {@link Interval}. */
  public void popMinMaxDiscard() {
    if (sp == 0) {
      throw new IndexOutOfBoundsException("Can't pop the min max state when there are 0 states");
    }
    sp--;
  }

  /**
   * This is an override of the base LT function that tracks the minimum/maximum
   * token index looked at.
   */
  @Override
  @Nullable
  public Token LT(int k) {
    Token result = super.LT(k);
    // Adjust the top of the minimum maximum stack if the position/lookahead amount
    // changed.
    if (sp != 0 && (lastP != p || lastK != k) && result != null) {
      int idx = result.getTokenIndex();
      int top = sp - 1;
      if (idx < minStack[top]) {
        minStack[top] = idx;
      }
      if (idx > maxStack[top]) {
        maxStack[top] = idx;
      }
      lastP = p;
      lastK = k;
    }
    return result;
  }
}
