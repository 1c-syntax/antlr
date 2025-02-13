/*
 * This file is a part of ANTLR.
 *
 * Copyright (c) 2012-2025 The ANTLR Project. All rights reserved.
 * Copyright (c) 2025 Valery Maximov <maximovvalery@gmail.com> and contributors
 *
 * Use of this file is governed by the BSD-3-Clause license that
 * can be found in the LICENSE.txt file in the project root.
 */
package org.antlr.v4.tool.ast;

import org.antlr.runtime.Token;

public class StarBlockAST extends GrammarAST implements RuleElementAST, QuantifierAST {
  private final boolean _greedy;

  public StarBlockAST(StarBlockAST node) {
    super(node);
    _greedy = node._greedy;
  }

  public StarBlockAST(int type, Token t, Token nongreedy) {
    super(type, t);
    _greedy = nongreedy == null;
  }

  @Override
  public boolean isGreedy() {
    return _greedy;
  }

  @Override
  public StarBlockAST dupNode() {
    return new StarBlockAST(this);
  }

  @Override
  public Object visit(GrammarASTVisitor v) {
    return v.visit(this);
  }
}
