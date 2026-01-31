/*
 * This file is a part of ANTLR.
 *
 * Copyright (c) 2012-2025 The ANTLR Project. All rights reserved.
 * Copyright (c) 2025-2026 Valery Maximov <maximovvalery@gmail.com> and contributors
 *
 * Use of this file is governed by the BSD-3-Clause license that
 * can be found in the LICENSE.txt file in the project root.
 */
package org.antlr.v4.runtime.tree.pattern;

import lombok.Getter;
import org.antlr.v4.runtime.CommonToken;
import org.antlr.v4.runtime.Token;
import org.jspecify.annotations.Nullable;

/**
 * A {@link Token} object representing a token of a particular type; e.g., {@code <ID>}. These tokens are created for
 * {@link TagChunk} chunks where the tag corresponds to a lexer rule or token type.
 */
@Getter
public class TokenTagToken extends CommonToken {
  /**
   * This is the backing field for {@link #getTokenName}.
   */
  private final String tokenName;

  /**
   * This is the backing field for {@link #getLabel}.
   */
  private final @Nullable String label;

  /**
   * Constructs a new instance of {@link TokenTagToken} for an unlabeled tag with the specified token name and type.
   *
   * @param tokenName The token name.
   * @param type      The token type.
   */
  public TokenTagToken(String tokenName, int type) {
    this(tokenName, type, null);
  }

  /**
   * Constructs a new instance of {@link TokenTagToken} with the specified token name, type, and label.
   *
   * @param tokenName The token name.
   * @param type      The token type.
   * @param label     The label associated with the token tag, or {@code null} if the token tag is unlabeled.
   */
  public TokenTagToken(String tokenName, int type, @Nullable String label) {
    super(type);
    this.tokenName = tokenName;
    this.label = label;
  }

  /**
   * {@inheritDoc}
   *
   * <p>The implementation for {@link TokenTagToken} returns the token tag
   * formatted with {@code <} and {@code >} delimiters.</p>
   */
  @Override
  public String getText() {
    if (label != null) {
      return "<" + label + ":" + tokenName + ">";
    }

    return "<" + tokenName + ">";
  }

  /**
   * {@inheritDoc}
   *
   * <p>The implementation for {@link TokenTagToken} returns a string of the form
   * {@code tokenName:type}.</p>
   */
  @Override
  public String toString() {
    return tokenName + ":" + type;
  }
}
