/*
 * This file is a part of ANTLR.
 *
 * Copyright (c) 2012-2025 The ANTLR Project. All rights reserved.
 * Copyright (c) 2025 Valery Maximov <maximovvalery@gmail.com> and contributors
 *
 * Use of this file is governed by the BSD-3-Clause license that
 * can be found in the LICENSE.txt file in the project root.
 */
package org.antlr.v4.runtime.atn;

import org.antlr.v4.runtime.misc.IntervalSet;
import org.antlr.v4.runtime.misc.NotNull;

/**
 * TODO: make all transitions sets? no, should remove set edges
 */
public final class AtomTransition extends Transition {
  /**
   * The token type or character value; or, signifies special label.
   */
  public final int label;

  public AtomTransition(@NotNull ATNState target, int label) {
    super(target);
    this.label = label;
  }

  @Override
  public int getSerializationType() {
    return ATOM;
  }

  @Override
  @NotNull
  public IntervalSet label() {
    return IntervalSet.of(label);
  }

  @Override
  public boolean matches(int symbol, int minVocabSymbol, int maxVocabSymbol) {
    return label == symbol;
  }

  @Override
  @NotNull
  public String toString() {
    return String.valueOf(label);
  }
}
