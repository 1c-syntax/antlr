/*
 * This file is a part of ANTLR.
 *
 * Copyright (c) 2012-2025 The ANTLR Project. All rights reserved.
 * Copyright (c) 2025-2026 Valery Maximov <maximovvalery@gmail.com> and contributors
 *
 * Use of this file is governed by the BSD-3-Clause license that
 * can be found in the LICENSE.txt file in the project root.
 */
package org.antlr.v4.automata;

import org.antlr.v4.runtime.atn.ATN;
import org.antlr.v4.runtime.atn.ATNState;
import org.antlr.v4.tool.ast.ActionAST;
import org.antlr.v4.tool.ast.BlockAST;
import org.antlr.v4.tool.ast.GrammarAST;
import org.antlr.v4.tool.ast.PredAST;
import org.antlr.v4.tool.ast.TerminalAST;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.List;

@NullMarked
public interface ATNFactory {
  /**
   * A pair of states pointing to the left/right (start and end) states of a state submachine.  Used to build ATNs.
   */
  class Handle {
    public ATNState left;
    public ATNState right;

    public Handle(ATNState left, ATNState right) {
      this.left = left;
      this.right = right;
    }

    @Override
    public String toString() {
      return "(" + left + "," + right + ")";
    }
  }

  ATN createATN();

  void setCurrentRuleName(String name);

  void setCurrentOuterAlt(int alt);

  Handle rule(GrammarAST ruleAST, String name, Handle blk);

  ATNState newState();

  Handle label(Handle t);

  Handle listLabel(Handle t);

  Handle tokenRef(TerminalAST node);

  Handle set(GrammarAST associatedAST, List<GrammarAST> alts, boolean invert);

  Handle charSetLiteral(GrammarAST charSetAST);

  Handle range(GrammarAST a, GrammarAST b);

  /**
   * For a non-lexer, just build a simple token reference atom. For a lexer, a string is a sequence of char to match.
   * That is, "fog" is treated as 'f' 'o' 'g' not as a single transition in the DFA.  Machine==
   * o-'f'-&gt;o-'o'-&gt;o-'g'-&gt;o and has n+1 states for n characters.
   */
  Handle stringLiteral(TerminalAST stringLiteralAST);

  /**
   * For reference to rule r, build
   * <p>
   * o-e-&gt;(r)  o
   * <p>
   * where (r) is the start of rule r and the trailing o is not linked to from rule ref state directly (it's done thru
   * the transition(0) RuleClosureTransition.
   * <p>
   * If the rule r is just a list of tokens, it's block will be just a set on an edge
   * o-&gt;o-&gt;o-set-&gt;o-&gt;o-&gt;o, could inline it rather than doing the rule reference, but i'm not doing this
   * yet as I'm not sure it would help much in the ATN-&gt;DFA construction.
   * <p>
   *  TODO add to codegen: collapse alt blks that are sets into single matchSet
   */
  @Nullable Handle ruleRef(GrammarAST node);

  /**
   * From an empty alternative build Grip o-e-&gt;o
   */
  Handle epsilon(GrammarAST node);

  /**
   * Build what amounts to an epsilon transition with a semantic predicate action.  The pred is a pointer into the AST
   * of the SEMPRED token.
   */
  Handle sempred(PredAST pred);

  /**
   * Build what amounts to an epsilon transition with an action. The action goes into ATN though it is ignored during
   * analysis.
   */
  Handle action(ActionAST action);


  Handle action(String action);

  Handle alt(List<Handle> els);

  /**
   * From A|B|..|Z alternative block build
   * <p>
   * o-&gt;o-A-&gt;o-&gt;o (last ATNState is blockEndATNState pointed to by all alts) |          ^ o-&gt;o-B-&gt;o--| |
   * | ...        | |          | o-&gt;o-Z-&gt;o--|
   * <p>
   * So every alternative gets begin ATNState connected by epsilon and every alt right side points at a block end
   * ATNState.  There is a new ATNState in the ATNState in the Grip for each alt plus one for the end ATNState.
   * <p>
   * Special case: only one alternative: don't make a block with alt begin/end.
   * <p>
   * Special case: if just a list of tokens/chars/sets, then collapse to a single edge'd o-set-&gt;o graph.
   * <p>
   * Set alt number (1..n) in the left-Transition ATNState.
   */
  @Nullable Handle block(BlockAST blockAST, GrammarAST ebnfRoot, List<Handle> alternativeGrips);

  /**
   * From (A)? build either:
   * <p>
   * o--A-&gt;o |     ^ o----&gt;|
   * <p>
   * or, if A is a block, just add an empty alt to the end of the block
   */
  Handle optional(GrammarAST optAST, Handle blk);

  /**
   * From (A)+ build
   * <p>
   * |---|    (Transition 2 from A.right points at alt 1) v   |    (follow of loop is Transition 1) o-&gt;o-A-o-&gt;o
   * <p>
   * Meaning that the last ATNState in A points back to A's left Transition ATNState and we add a new begin/end
   * ATNState.  A can be single alternative or multiple.
   * <p>
   * During analysis we'll call the follow link (transition 1) alt n+1 for an n-alt A block.
   */
  Handle plus(GrammarAST plusAST, Handle blk);

  /**
   * From (A)* build
   * <p>
   * |---| v   | o-&gt;o-A-o--o (Transition 2 from block end points at alt 1; follow is Transition 1) |         ^
   * o---------| (optional branch is 2nd alt of optional block containing A+)
   * <p>
   * Meaning that the last (end) ATNState in A points back to A's left side ATNState and we add 3 new ATNStates (the
   * optional branch is built just like an optional subrule). See the Aplus() method for more on the loop back
   * Transition. The new node on right edge is set to RIGHT_EDGE_OF_CLOSURE so we can detect nested (A*)* loops and
   * insert an extra node.  Previously, two blocks shared same EOB node.
   * <p>
   * There are 2 or 3 decision points in a A*.  If A is not a block (i.e., it only has one alt), then there are two
   * decisions: the optional bypass and then loopback.  If A is a block of alts, then there are three decisions: bypass,
   * loopback, and A's decision point.
   * <p>
   * Note that the optional bypass must be outside the loop as (A|B)* is not the same thing as (A|B|)+.
   * <p>
   * This is an accurate ATN representation of the meaning of (A)*, but for generating code, I don't need a DFA for the
   * optional branch by virtue of how I generate code.  The exit-loopback-branch decision is sufficient to let me make
   * an appropriate enter, exit, loop determination.  See codegen.g
   */
  Handle star(GrammarAST starAST, Handle blk);

  /**
   * Build an atom with all possible values in its label
   */
  Handle wildcard(GrammarAST associatedAST);

  Handle lexerAltCommands(Handle alt, Handle cmds);

  Handle lexerCallCommand(GrammarAST ID, GrammarAST arg);

  Handle lexerCommand(GrammarAST ID);
}
