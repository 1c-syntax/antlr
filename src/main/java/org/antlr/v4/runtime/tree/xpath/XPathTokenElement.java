/*
 * This file is a part of ANTLR.
 *
 * Copyright (c) 2012-2025 The ANTLR Project. All rights reserved.
 * Copyright (c) 2025 Valery Maximov <maximovvalery@gmail.com> and contributors
 *
 * Use of this file is governed by the BSD-3-Clause license that
 * can be found in the LICENSE.txt file in the project root.
 */
package org.antlr.v4.runtime.tree.xpath;

import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.TerminalNode;
import org.antlr.v4.runtime.tree.Tree;
import org.antlr.v4.runtime.tree.Trees;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class XPathTokenElement extends XPathElement {
  protected int tokenType;

  public XPathTokenElement(String tokenName, int tokenType) {
    super(tokenName);
    this.tokenType = tokenType;
  }

  @Override
  public Collection<ParseTree> evaluate(ParseTree t) {
    // return all children of t that match nodeName
    List<ParseTree> nodes = new ArrayList<ParseTree>();
    for (Tree c : Trees.getChildren(t)) {
      if (c instanceof TerminalNode tnode) {
        if ((tnode.getSymbol().getType() == tokenType && !invert) ||
          (tnode.getSymbol().getType() != tokenType && invert)) {
          nodes.add(tnode);
        }
      }
    }
    return nodes;
  }
}
