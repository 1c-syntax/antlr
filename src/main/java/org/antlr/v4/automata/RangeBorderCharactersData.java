/*
 * This file is a part of ANTLR.
 *
 * Copyright (c) 2012-2025 The ANTLR Project. All rights reserved.
 * Copyright (c) 2025 Valery Maximov <maximovvalery@gmail.com> and contributors
 *
 * Use of this file is governed by the BSD-3-Clause license that
 * can be found in the LICENSE.txt file in the project root.
 */
package org.antlr.v4.automata;

import org.antlr.runtime.tree.CommonTree;
import org.antlr.v4.tool.ErrorType;
import org.antlr.v4.tool.Grammar;

public class RangeBorderCharactersData {
  public int lowerFrom;
  public int upperFrom;
  public int lowerTo;
  public int upperTo;
  public boolean mixOfLowerAndUpperCharCase;

  public RangeBorderCharactersData(int lowerFrom, int upperFrom, int lowerTo, int upperTo, boolean mixOfLowerAndUpperCharCase) {
    this.lowerFrom = lowerFrom;
    this.upperFrom = upperFrom;
    this.lowerTo = lowerTo;
    this.upperTo = upperTo;
    this.mixOfLowerAndUpperCharCase = mixOfLowerAndUpperCharCase;
  }

  public static RangeBorderCharactersData getAndCheckCharactersData(int from, int to, Grammar grammar, CommonTree tree) {
    int lowerFrom = Character.toLowerCase(from);
    int upperFrom = Character.toUpperCase(from);
    int lowerTo = Character.toLowerCase(to);
    int upperTo = Character.toUpperCase(to);
    boolean isLowerFrom = lowerFrom == from;
    boolean isLowerTo = lowerTo == to;
    boolean mixOfLowerAndUpperCharCase = isLowerFrom && !isLowerTo || !isLowerFrom && isLowerTo;
    if (mixOfLowerAndUpperCharCase) {
      StringBuilder notImpliedCharacters = new StringBuilder();
      for (int i = from; i < to; i++) {
        if (Character.toLowerCase(i) == Character.toUpperCase(i)) {
          notImpliedCharacters.append((char) i);
        }
      }
      if (notImpliedCharacters.length() > 0) {
        grammar.tool.errMgr.grammarError(ErrorType.RANGE_PROBABLY_CONTAINS_NOT_IMPLIED_CHARACTERS, grammar.fileName, tree.getToken(),
          (char) from, (char) to, notImpliedCharacters.toString());
      }
    }
    return new RangeBorderCharactersData(lowerFrom, upperFrom, lowerTo, upperTo, mixOfLowerAndUpperCharCase);
  }
}
