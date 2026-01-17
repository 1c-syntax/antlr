/*
 * This file is a part of ANTLR.
 *
 * Copyright (c) 2012-2025 The ANTLR Project. All rights reserved.
 * Copyright (c) 2025-2026 Valery Maximov <maximovvalery@gmail.com> and contributors
 *
 * Use of this file is governed by the BSD-3-Clause license that
 * can be found in the LICENSE.txt file in the project root.
 */
package org.antlr.v4.runtime.atn;

import org.antlr.v4.runtime.dfa.DFAState;
import org.antlr.v4.runtime.dfa.EmptyEdgeMap;
import org.antlr.v4.runtime.misc.NotNull;

public abstract class ATNSimulator {

  public static final char RULE_VARIANT_DELIMITER = '$';
  public static final String RULE_LF_VARIANT_MARKER = "$lf$";
  public static final String RULE_NOLF_VARIANT_MARKER = "$nolf$";

  /**
   * Must distinguish between missing edge and edge we know leads nowhere
   */
  @NotNull
  public static final DFAState ERROR;
  @NotNull
  public final ATN atn;

  static {
    ERROR = new DFAState(new EmptyEdgeMap<>(0, -1), new EmptyEdgeMap<>(0, -1), new ATNConfigSet());
    ERROR.stateNumber = Integer.MAX_VALUE;
  }

  public ATNSimulator(@NotNull ATN atn) {
    this.atn = atn;
  }

  public abstract void reset();

  /**
   * Clear the DFA cache used by the current instance. Since the DFA cache may
   * be shared by multiple ATN simulators, this method may affect the
   * performance (but not accuracy) of other parsers which are being used
   * concurrently.
   *
   * @throws UnsupportedOperationException if the current instance does not
   *                                       support clearing the DFA.
   * @since 4.3
   */
  public void clearDFA() {
    atn.clearDFA();
  }

}
