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
import org.antlr.v4.runtime.atn.PlusBlockStartState;
import org.antlr.v4.runtime.atn.PlusLoopbackState;
import org.antlr.v4.tool.ast.BlockAST;
import org.antlr.v4.tool.ast.GrammarAST;

import java.util.List;

public class PlusBlock extends Loop {
  @ModelElement
  public ThrowNoViableAlt error;

  public PlusBlock(OutputModelFactory factory,
                   GrammarAST plusRoot,
                   List<CodeBlockForAlt> alts) {
    super(factory, plusRoot, alts);
    BlockAST blkAST = (BlockAST) plusRoot.getChild(0);
    PlusBlockStartState blkStart = (PlusBlockStartState) blkAST.atnState;
    PlusLoopbackState loop = blkStart.loopBackState;
    stateNumber = blkStart.loopBackState.stateNumber;
    blockStartStateNumber = blkStart.stateNumber;
    loopBackStateNumber = loop.stateNumber;
    this.error = getThrowNoViableAlt(factory, plusRoot, null);
    decision = loop.decision;
  }
}
