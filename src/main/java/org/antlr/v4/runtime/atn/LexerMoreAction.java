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

import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.misc.MurmurHash;
import org.antlr.v4.runtime.misc.NotNull;

/**
 * Implements the {@code more} lexer action by calling {@link Lexer#more}.
 *
 * <p>The {@code more} command does not have any parameters, so this action is
 * implemented as a singleton instance exposed by {@link #INSTANCE}.</p>
 *
 * @author Sam Harwell
 * @since 4.2
 */
public final class LexerMoreAction implements LexerAction {
  /**
   * Provides a singleton instance of this parameterless lexer action.
   */
  public static final LexerMoreAction INSTANCE = new LexerMoreAction();

  /**
   * Constructs the singleton instance of the lexer {@code more} command.
   */
  private LexerMoreAction() {
  }

  /**
   * {@inheritDoc}
   *
   * @return This method returns {@link LexerActionType#MORE}.
   */
  @Override
  public LexerActionType getActionType() {
    return LexerActionType.MORE;
  }

  /**
   * {@inheritDoc}
   *
   * @return This method returns {@code false}.
   */
  @Override
  public boolean isPositionDependent() {
    return false;
  }

  /**
   * {@inheritDoc}
   *
   * <p>This action is implemented by calling {@link Lexer#more}.</p>
   */
  @Override
  public void execute(@NotNull Lexer lexer) {
    lexer.more();
  }

  @Override
  public int hashCode() {
    int hash = MurmurHash.initialize();
    hash = MurmurHash.update(hash, getActionType().ordinal());
    return MurmurHash.finish(hash, 1);
  }

  @Override
  @SuppressWarnings("EqualsWhichDoesntCheckParameterClass")
  public boolean equals(Object obj) {
    return obj == this;
  }

  @Override
  public String toString() {
    return "more";
  }
}
