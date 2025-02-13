/*
 * This file is a part of ANTLR.
 *
 * Copyright (c) 2012-2025 The ANTLR Project. All rights reserved.
 * Copyright (c) 2025 Valery Maximov <maximovvalery@gmail.com> and contributors
 *
 * Use of this file is governed by the BSD-3-Clause license that
 * can be found in the LICENSE.txt file in the project root.
 */
package org.antlr.v4.codegen.model.decl;

import org.antlr.v4.codegen.OutputModelFactory;

/**
 * {@code public XContext X() { }}
 */
public class ContextRuleGetterDecl extends ContextGetterDecl {
  public String ctxName;
  public boolean optional;

  public ContextRuleGetterDecl(OutputModelFactory factory, String name, String ctxName, boolean optional) {
    super(factory, name);
    this.ctxName = ctxName;
    this.optional = optional;
  }
}
