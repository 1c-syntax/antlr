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

import org.antlr.v4.codegen.ActionTranslator;
import org.antlr.v4.codegen.OutputModelFactory;
import org.antlr.v4.codegen.model.chunk.ActionChunk;
import org.antlr.v4.runtime.atn.AbstractPredicateTransition;
import org.antlr.v4.runtime.misc.NotNull;
import org.antlr.v4.tool.ast.ActionAST;
import org.antlr.v4.tool.ast.GrammarAST;

import java.util.List;

/**
 *
 */
public class SemPred extends Action {
  /**
   * The user-specified terminal option {@code fail}, if it was used and the
   * value is a string literal. For example:
   *
   * <p>
   * {@code {pred}?<fail='message'>}</p>
   */
  public String msg;
  /**
   * The predicate string with <code>{</code> and <code>}?</code> stripped from the ends.
   */
  public String predicate;

  /**
   * The translated chunks of the user-specified terminal option {@code fail},
   * if it was used and the value is an action. For example:
   *
   * <p>
   * {@code {pred}?<fail={"Java literal"}>}</p>
   */
  @ModelElement
  public List<ActionChunk> failChunks;

  public SemPred(OutputModelFactory factory, @NotNull ActionAST ast) {
    super(factory, ast);

    assert ast.atnState != null
      && ast.atnState.getNumberOfTransitions() == 1
      && ast.atnState.transition(0) instanceof AbstractPredicateTransition;

    GrammarAST failNode = ast.getOptionAST("fail");
    predicate = ast.getText();
    if (predicate.startsWith("{") && predicate.endsWith("}?")) {
      predicate = predicate.substring(1, predicate.length() - 2);
    }
    predicate = factory.getTarget().getTargetStringLiteralFromString(predicate);

    if (failNode == null) return;

    if (failNode instanceof ActionAST failActionNode) {
      RuleFunction rf = factory.getCurrentRuleFunction();
      failChunks = ActionTranslator.translateAction(factory, rf,
        failActionNode.token,
        failActionNode);
    } else {
      msg = factory.getTarget().getTargetStringLiteralFromANTLRStringLiteral(factory.getGenerator(),
        failNode.getText(),
        true);
    }
  }
}
