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

public class IncrementalParserRuleContext extends ParserRuleContext {
  /* Avoid having to recompute depth on every single depth call */
  private int cachedDepth;
  private RuleContext cachedParent;

  // This is an epoch number that can be used to tell which pieces were
  // modified during a given incremental parse. The incremental parser
  // adds the current epoch number to all rule contexts it creates.
  // The epoch number is incremented every time a new parser instance is created.
  public int epoch = -1;

  // Min/max индексы токенов, тронутых при lookahead/lookbehind.
  // Хранятся двумя примитивами, чтобы не аллоцировать Interval на каждый узел правила
  // и на каждое объединение (горячий путь инкрементального парсинга).
  private int minTokenIndex = Integer.MAX_VALUE;
  private int maxTokenIndex = Integer.MIN_VALUE;

  /**
   * Get the minimum token index this rule touched.
   */
  public int getMinTokenIndex() {
    return minTokenIndex;
  }

  /**
   * Get the maximum token index this rule touched.
   */
  public int getMaxTokenIndex() {
    return maxTokenIndex;
  }

  /**
   * Get the interval this rule touched.
   */
  public Interval getMinMaxTokenIndex() {
    return Interval.of(minTokenIndex, maxTokenIndex);
  }

  public void setMinMaxTokenIndex(Interval index) {
    minTokenIndex = index.a;
    maxTokenIndex = index.b;
  }

  /**
   * Расширить интервал тронутых токенов без аллокации {@link Interval}.
   *
   * @param min минимальный индекс токена
   * @param max максимальный индекс токена
   */
  public void unionMinMax(int min, int max) {
    if (min < minTokenIndex) {
      minTokenIndex = min;
    }
    if (max > maxTokenIndex) {
      maxTokenIndex = max;
    }
  }

  /**
   * Compute the depth of this context in the parse tree.
   *
   * @note The incremental parser uses a caching implementation.
   *
   */
  @Override
  public int depth() {
    if (cachedParent != null && cachedParent == this.parent) {
      return cachedDepth;
    }
    int n;
    if (this.parent != null) {
      int parentDepth = this.parent.depth();
      this.cachedParent = this.parent;
      this.cachedDepth = n = parentDepth + 1;
    } else {
      this.cachedDepth = n = 1;
    }
    return n;
  }

  public IncrementalParserRuleContext(IncrementalParserRuleContext parent, int invokingStateNumber) {
    super(parent, invokingStateNumber);
  }
}