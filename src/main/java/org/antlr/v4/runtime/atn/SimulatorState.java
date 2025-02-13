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

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.dfa.DFAState;
import org.antlr.v4.runtime.misc.NotNull;

/**
 * @author Sam Harwell
 */
public class SimulatorState {
  public final ParserRuleContext outerContext;

  public final DFAState s0;

  public final boolean useContext;
  public final ParserRuleContext remainingOuterContext;

  public SimulatorState(ParserRuleContext outerContext, @NotNull DFAState s0, boolean useContext, ParserRuleContext remainingOuterContext) {
    this.outerContext = outerContext != null ? outerContext : ParserRuleContext.emptyContext();
    this.s0 = s0;
    this.useContext = useContext;
    this.remainingOuterContext = remainingOuterContext;
  }
}
