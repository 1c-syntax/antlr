/*
 * This file is a part of ANTLR.
 *
 * Copyright (c) 2012-2025 The ANTLR Project. All rights reserved.
 * Copyright (c) 2025 Valery Maximov <maximovvalery@gmail.com> and contributors
 *
 * Use of this file is governed by the BSD-3-Clause license that
 * can be found in the LICENSE.txt file in the project root.
 */
package org.antlr.v4.runtime.tree;

import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.RuleContext;
import org.antlr.v4.runtime.Token;

/**
 * An interface to access the tree of {@link RuleContext} objects created
 * during a parse that makes the data structure look like a simple parse tree.
 * This node represents both internal nodes, rule invocations,
 * and leaf nodes, token matches.
 *
 * <p>The payload is either a {@link Token} or a {@link RuleContext} object.</p>
 */
public interface ParseTree extends SyntaxTree {
  // the following methods narrow the return type; they are not additional methods
  @Override
  ParseTree getParent();

  @Override
  ParseTree getChild(int i);

  /**
   * The {@link ParseTreeVisitor} needs a double dispatch method.
   */
  <T> T accept(ParseTreeVisitor<? extends T> visitor);

  /**
   * Return the combined text of all leaf nodes. Does not get any
   * off-channel tokens (if any) so won't return whitespace and
   * comments if they are sent to parser on hidden channel.
   */
  String getText();

  /**
   * Specialize toStringTree so that it can print out more information
   * based upon the parser.
   */
  String toStringTree(Parser parser);
}
