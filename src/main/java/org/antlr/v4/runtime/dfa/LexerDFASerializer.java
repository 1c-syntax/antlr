/*
 * This file is a part of ANTLR.
 *
 * Copyright (c) 2012-2025 The ANTLR Project. All rights reserved.
 * Copyright (c) 2025 Valery Maximov <maximovvalery@gmail.com> and contributors
 *
 * Use of this file is governed by the BSD-3-Clause license that
 * can be found in the LICENSE.txt file in the project root.
 */
package org.antlr.v4.runtime.dfa;

import org.antlr.v4.runtime.VocabularyImpl;
import org.antlr.v4.runtime.misc.NotNull;

public class LexerDFASerializer extends DFASerializer {
  public LexerDFASerializer(@NotNull DFA dfa) {
    super(dfa, VocabularyImpl.EMPTY_VOCABULARY);
  }

  @Override
  @NotNull
  protected String getEdgeLabel(int i) {
    return new StringBuilder("'")
      .appendCodePoint(i)
      .append("'")
      .toString();
  }
}
