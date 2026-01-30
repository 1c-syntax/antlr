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

/**
 * Represents a span of raw text (concrete syntax) between tags in a tree pattern string.
 */
@Getter
class TextChunk extends Chunk {
  /**
   * This is the backing field for {@link #getText}.
   */
  private final String text;

  /**
   * Constructs a new instance of {@link TextChunk} with the specified text.
   *
   * @param text The text of this chunk.
   *
   * @throws IllegalArgumentException if {@code text} is {@code null}.
   */
  public TextChunk(String text) {
    this.text = text;
  }

  /**
   * {@inheritDoc}
   *
   * <p>The implementation for {@link TextChunk} returns the result of
   * {@link #getText()} in single quotes.</p>
   */
  @Override
  public String toString() {
    return "'" + text + "'";
  }
}
