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

import org.antlr.v4.runtime.atn.ATNConfigSet;
import org.antlr.v4.runtime.atn.SimulatorState;
import org.antlr.v4.runtime.dfa.DFA;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.BitSet;

/**
 * Provides an empty default implementation of {@link ANTLRErrorListener}. The default implementation of each method
 * does nothing, but can be overridden as necessary.
 *
 * @author Sam Harwell
 */
@NullMarked
public class BaseErrorListener implements ParserErrorListener {
  @Override
  public <T extends Token> void syntaxError(Recognizer<T, ?> recognizer,
                                            @Nullable T offendingSymbol,
                                            int line,
                                            int charPositionInLine,
                                            String msg,
                                            @Nullable RecognitionException e) {
  }

  @Override
  public void reportAmbiguity(Parser recognizer,
                              DFA dfa,
                              int startIndex,
                              int stopIndex,
                              boolean exact,
                              @Nullable BitSet ambigAlts,
                              ATNConfigSet configs) {
  }

  @Override
  public void reportAttemptingFullContext(Parser recognizer,
                                          DFA dfa,
                                          int startIndex,
                                          int stopIndex,
                                          @Nullable BitSet conflictingAlts,
                                          SimulatorState conflictState) {
  }

  @Override
  public void reportContextSensitivity(Parser recognizer,
                                       DFA dfa,
                                       int startIndex,
                                       int stopIndex,
                                       int prediction,
                                       SimulatorState acceptState) {
  }
}
