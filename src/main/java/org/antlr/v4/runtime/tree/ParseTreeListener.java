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

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.misc.NotNull;

/**
 * This interface describes the minimal core of methods triggered
 * by {@link ParseTreeWalker}. E.g.,
 * <p>
 * ParseTreeWalker walker = new ParseTreeWalker();
 * walker.walk(myParseTreeListener, myParseTree); <-- triggers events in your listener
 * <p>
 * If you want to trigger events in multiple listeners during a single
 * tree walk, you can use the ParseTreeDispatcher object available at
 * <p>
 * https://github.com/antlr/antlr4/issues/841
 */
public interface ParseTreeListener {
  void visitTerminal(@NotNull TerminalNode node);

  void visitErrorNode(@NotNull ErrorNode node);

  void enterEveryRule(@NotNull ParserRuleContext ctx);

  void exitEveryRule(@NotNull ParserRuleContext ctx);
}
