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

/**
 * Terminal node of a simple {@code (a|b|c)} block.
 */
public final class BlockEndState extends ATNState {
  public BlockStartState startState;

  @Override
  public int getStateType() {
    return BLOCK_END;
  }
}
