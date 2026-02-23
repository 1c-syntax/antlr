/*
 * This file is a part of ANTLR.
 *
 * Copyright (c) 2012-2025 The ANTLR Project. All rights reserved.
 * Copyright (c) 2025-2026 Valery Maximov <maximovvalery@gmail.com> and contributors
 *
 * Use of this file is governed by the BSD-3-Clause license that
 * can be found in the LICENSE.txt file in the project root.
 */
package org.antlr.v4.runtime.dfa;

import lombok.Getter;
import org.antlr.v4.runtime.atn.LexerActionExecutor;
import org.antlr.v4.runtime.atn.ParserATNSimulator;

/**
 * Stores information about a {@link DFAState} which is an accept state under some condition. Certain settings, such as
 * {@link ParserATNSimulator#getPredictionMode()}, may be used in addition to this information to determine whether or
 * not a particular state is an accept state.
 *
 * @author Sam Harwell
 */
@Getter
public class AcceptStateInfo {
  /**
   * If predicate evaluation is enabled, the final prediction of the accept state will be determined by the result of
   * predicate evaluation.
   */
  private final int prediction;

  private final LexerActionExecutor lexerActionExecutor;

  public AcceptStateInfo(int prediction) {
    this.prediction = prediction;
    this.lexerActionExecutor = null;
  }

  public AcceptStateInfo(int prediction, LexerActionExecutor lexerActionExecutor) {
    this.prediction = prediction;
    this.lexerActionExecutor = lexerActionExecutor;
  }

}
