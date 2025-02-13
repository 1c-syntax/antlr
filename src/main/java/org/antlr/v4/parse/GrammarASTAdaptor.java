/*
 * This file is a part of ANTLR.
 *
 * Copyright (c) 2012-2025 The ANTLR Project. All rights reserved.
 * Copyright (c) 2025 Valery Maximov <maximovvalery@gmail.com> and contributors
 *
 * Use of this file is governed by the BSD-3-Clause license that
 * can be found in the LICENSE.txt file in the project root.
 */
package org.antlr.v4.parse;

import org.antlr.runtime.CommonToken;
import org.antlr.runtime.Token;
import org.antlr.runtime.tree.CommonTreeAdaptor;
import org.antlr.v4.tool.ast.GrammarAST;
import org.antlr.v4.tool.ast.GrammarASTErrorNode;
import org.antlr.v4.tool.ast.RuleAST;
import org.antlr.v4.tool.ast.TerminalAST;


public class GrammarASTAdaptor extends CommonTreeAdaptor {
  org.antlr.runtime.CharStream input; // where we can find chars ref'd by tokens in tree

  public GrammarASTAdaptor() {
  }

  public GrammarASTAdaptor(org.antlr.runtime.CharStream input) {
    this.input = input;
  }

  @Override
  public GrammarAST nil() {
    return (GrammarAST) super.nil();
  }

  @Override
  public GrammarAST create(Token token) {
    return new GrammarAST(token);
  }

  @Override
  /** Make sure even imaginary nodes know the input stream */
  public GrammarAST create(int tokenType, String text) {
    GrammarAST t;
    if (tokenType == ANTLRParser.RULE) {
      // needed by TreeWizard to make RULE tree
      t = new RuleAST(new CommonToken(tokenType, text));
    } else if (tokenType == ANTLRParser.STRING_LITERAL) {
      // implicit lexer construction done with wizard; needs this node type
      // whereas grammar ANTLRParser.g can use token option to spec node type
      t = new TerminalAST(new CommonToken(tokenType, text));
    } else {
      t = (GrammarAST) super.create(tokenType, text);
    }
    t.token.setInputStream(input);
    return t;
  }

  @Override
  public GrammarAST create(int tokenType, Token fromToken, String text) {
    return (GrammarAST) super.create(tokenType, fromToken, text);
  }

  @Override
  public GrammarAST create(int tokenType, Token fromToken) {
    return (GrammarAST) super.create(tokenType, fromToken);
  }

  @Override
  public GrammarAST dupNode(Object t) {
    if (t == null) return null;
    return ((GrammarAST) t).dupNode(); //create(((GrammarAST)t).token);
  }

  @Override
  public GrammarASTErrorNode errorNode(org.antlr.runtime.TokenStream input, org.antlr.runtime.Token start, org.antlr.runtime.Token stop,
                                       org.antlr.runtime.RecognitionException e) {
    return new GrammarASTErrorNode(input, start, stop, e);
  }
}
