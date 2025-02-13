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
import org.antlr.v4.codegen.model.DispatchMethod;
import org.antlr.v4.codegen.model.ListenerDispatchMethod;
import org.antlr.v4.codegen.model.VisitorDispatchMethod;
import org.antlr.v4.tool.Rule;

import java.util.ArrayList;

/**
 * A StructDecl to handle a '#' label on alt
 */
public class AltLabelStructDecl extends StructDecl {
  public String parentRule;

  public AltLabelStructDecl(OutputModelFactory factory, Rule r, String label) {
    super(factory, r);
    this.name = // override name set in super to the label ctx
      factory.getTarget().getAltLabelContextStructName(label);
    this.parentRule = r.name;
    derivedFromName = label;
  }

  @Override
  public void addDispatchMethods(Rule r) {
    dispatchMethods = new ArrayList<DispatchMethod>();
    if (factory.getGrammar().tool.gen_listener) {
      dispatchMethods.add(new ListenerDispatchMethod(factory, true));
      dispatchMethods.add(new ListenerDispatchMethod(factory, false));
    }
    if (factory.getGrammar().tool.gen_visitor) {
      dispatchMethods.add(new VisitorDispatchMethod(factory));
    }
  }

  @Override
  public int hashCode() {
    return name.hashCode();
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) return true;
    if (!(obj instanceof AltLabelStructDecl)) return false;

    return name.equals(((AltLabelStructDecl) obj).name);
  }
}
