/*
 * This file is a part of ANTLR.
 *
 * Copyright (c) 2012-2025 The ANTLR Project. All rights reserved.
 * Copyright (c) 2025 Valery Maximov <maximovvalery@gmail.com> and contributors
 *
 * Use of this file is governed by the BSD-3-Clause license that
 * can be found in the LICENSE.txt file in the project root.
 */
package org.antlr.v4.semantics;

import org.antlr.runtime.Token;
import org.antlr.v4.parse.ActionSplitterListener;

public class BlankActionSplitterListener implements ActionSplitterListener {
  @Override
  public void qualifiedAttr(String expr, Token x, Token y) {
  }

  @Override
  public void setAttr(String expr, Token x, Token rhs) {
  }

  @Override
  public void attr(String expr, Token x) {
  }

  public void templateInstance(String expr) {
  }

  @Override
  public void nonLocalAttr(String expr, Token x, Token y) {
  }

  @Override
  public void setNonLocalAttr(String expr, Token x, Token y, Token rhs) {
  }

  public void indirectTemplateInstance(String expr) {
  }

  public void setExprAttribute(String expr) {
  }

  public void setSTAttribute(String expr) {
  }

  public void templateExpr(String expr) {
  }

  @Override
  public void text(String text) {
  }
}
