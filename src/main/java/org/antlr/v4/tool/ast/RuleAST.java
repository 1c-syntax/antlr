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
import org.antlr.runtime.tree.Tree;
import org.antlr.v4.parse.ANTLRParser;
import org.antlr.v4.tool.Grammar;

public class RuleAST extends GrammarASTWithOptions {
  public RuleAST(RuleAST node) {
    super(node);
  }

  public RuleAST(Token t) {
    super(t);
  }

  public RuleAST(int type) {
    super(type);
  }

  public boolean isLexerRule() {
    String name = getRuleName();
    return name != null && Grammar.isTokenName(name);
  }

  public String getRuleName() {
    GrammarAST nameNode = (GrammarAST) getChild(0);
    if (nameNode != null) return nameNode.getText();
    return null;
  }

  @Override
  public RuleAST dupNode() {
    return new RuleAST(this);
  }

  public ActionAST getLexerAction() {
    Tree blk = getFirstChildWithType(ANTLRParser.BLOCK);
    if (blk.getChildCount() == 1) {
      Tree onlyAlt = blk.getChild(0);
      Tree lastChild = onlyAlt.getChild(onlyAlt.getChildCount() - 1);
      if (lastChild.getType() == ANTLRParser.ACTION) {
        return (ActionAST) lastChild;
      }
    }
    return null;
  }

  @Override
  public Object visit(GrammarASTVisitor v) {
    return v.visit(this);
  }
}
