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

public class PredAST extends ActionAST {
  public PredAST(PredAST node) {
    super(node);
  }

  public PredAST(Token t) {
    super(t);
  }

  public PredAST(int type) {
    super(type);
  }

  public PredAST(int type, Token t) {
    super(type, t);
  }

  @Override
  public PredAST dupNode() {
    return new PredAST(this);
  }

  @Override
  public Object visit(GrammarASTVisitor v) {
    return v.visit(this);
  }
}
