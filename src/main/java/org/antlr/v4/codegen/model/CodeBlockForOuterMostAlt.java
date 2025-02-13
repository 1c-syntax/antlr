/*
 * This file is a part of ANTLR.
 *
 * Copyright (c) 2012-2025 The ANTLR Project. All rights reserved.
 * Copyright (c) 2025 Valery Maximov <maximovvalery@gmail.com> and contributors
 *
 * Use of this file is governed by the BSD-3-Clause license that
 * can be found in the LICENSE.txt file in the project root.
 */
package org.antlr.v4.codegen.model;

import org.antlr.v4.codegen.OutputModelFactory;
import org.antlr.v4.tool.Alternative;

/**
 * The code associated with the outermost alternative of a rule.
 * Sometimes we might want to treat them differently in the
 * code generation.
 */
public class CodeBlockForOuterMostAlt extends CodeBlockForAlt {
  /**
   * The label for the alternative; or null if the alternative is not labeled.
   */
  public String altLabel;
  /**
   * The alternative.
   */
  public Alternative alt;

  public CodeBlockForOuterMostAlt(OutputModelFactory factory, Alternative alt) {
    super(factory);
    this.alt = alt;
    altLabel = alt.ast.altLabel != null ? alt.ast.altLabel.getText() : null;
  }
}
