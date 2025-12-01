/*
 * This file is a part of ANTLR.
 *
 * Copyright (c) 2012-2025 The ANTLR Project. All rights reserved.
 * Copyright (c) 2025 Valery Maximov <maximovvalery@gmail.com> and contributors
 *
 * Use of this file is governed by the BSD-3-Clause license that
 * can be found in the LICENSE.txt file in the project root.
 */
package org.antlr.v4.test.tool;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.atn.ATN;
import org.antlr.v4.runtime.misc.IntervalSet;
import org.antlr.v4.tool.Grammar;
import org.junit.jupiter.api.Test;

import static org.antlr.v4.TestUtils.assertEquals;

public class ExpectedTokensTest extends AbstractBaseTest {
  @Test
  public void testEpsilonAltSubrule() throws Exception {
    String gtext =
      """
        parser grammar T;
        a : A (B | ) C ;
        """;
    Grammar g = new Grammar(gtext);
    String atnText =
      """
        RuleStart_a_0->s2
        s2-A->BlockStart_5
        BlockStart_5->s3
        BlockStart_5->s4
        s3-B->BlockEnd_6
        s4->BlockEnd_6
        BlockEnd_6->s7
        s7-C->s8
        s8->RuleStop_a_1
        RuleStop_a_1-EOF->s9
        """;
    checkRuleATN(g, "a", atnText);

    ATN atn = g.getATN();
    int blkStartStateNumber = 5;
    IntervalSet tokens = atn.getExpectedTokens(blkStartStateNumber, null);
    assertEquals("{B, C}", tokens.toString(g.getVocabulary()));
  }

  @Test
  public void testOptionalSubrule() throws Exception {
    String gtext =
      """
        parser grammar T;
        a : A B? C ;
        """;
    Grammar g = new Grammar(gtext);
    String atnText =
      """
        RuleStart_a_0->s2
        s2-A->BlockStart_4
        BlockStart_4->s3
        BlockStart_4->BlockEnd_5
        s3-B->BlockEnd_5
        BlockEnd_5->s6
        s6-C->s7
        s7->RuleStop_a_1
        RuleStop_a_1-EOF->s8
        """;
    checkRuleATN(g, "a", atnText);

    ATN atn = g.getATN();
    int blkStartStateNumber = 4;
    IntervalSet tokens = atn.getExpectedTokens(blkStartStateNumber, null);
    assertEquals("{B, C}", tokens.toString(g.getVocabulary()));
  }

  @Test
  public void testFollowIncluded() throws Exception {
    String gtext =
      """
        parser grammar T;
        a : b A ;
        b : B | ;""";
    Grammar g = new Grammar(gtext);
    String atnText =
      """
        RuleStart_a_0->s4
        s4-b->RuleStart_b_2
        s5-A->s6
        s6->RuleStop_a_1
        RuleStop_a_1-EOF->s11
        """;
    checkRuleATN(g, "a", atnText);
    atnText =
      """
        RuleStart_b_2->BlockStart_9
        BlockStart_9->s7
        BlockStart_9->s8
        s7-B->BlockEnd_10
        s8->BlockEnd_10
        BlockEnd_10->RuleStop_b_3
        RuleStop_b_3->s5
        """;
    checkRuleATN(g, "b", atnText);

    ATN atn = g.getATN();

    // From the start of 'b' with empty stack, can only see B and EOF
    int blkStartStateNumber = 9;
    IntervalSet tokens = atn.getExpectedTokens(blkStartStateNumber, ParserRuleContext.emptyContext());
    assertEquals("{<EOF>, B}", tokens.toString(g.getVocabulary()));

    // Now call from 'a'
    tokens = atn.getExpectedTokens(blkStartStateNumber, new ParserRuleContext(ParserRuleContext.emptyContext(), 4));
    assertEquals("{A, B}", tokens.toString(g.getVocabulary()));
  }

  // Test for https://github.com/antlr/antlr4/issues/1480
  // can't reproduce
  @Test
  public void testFollowIncludedInLeftRecursiveRule() throws Exception {
    String gtext =
      """
        grammar T;
        s : expr EOF ;
        expr : L expr R
             | expr PLUS expr
             | ID
             ;
        """;
    Grammar g = new Grammar(gtext);
    String atnText =
      """
        RuleStart_expr_2->BlockStart_13
        BlockStart_13->s7
        BlockStart_13->s12
        s7-action_1:-1->s8
        s12-ID->BlockEnd_14
        s8-L->s9
        BlockEnd_14->StarLoopEntry_20
        s9-expr->RuleStart_expr_2
        StarLoopEntry_20->StarBlockStart_18
        StarLoopEntry_20->s21
        s10-R->s11
        StarBlockStart_18->s15
        s21->RuleStop_expr_3
        s11->BlockEnd_14
        s15-2 >= _p->s16
        RuleStop_expr_3->s5
        RuleStop_expr_3->s10
        RuleStop_expr_3->BlockEnd_19
        s16-PLUS->s17
        s17-expr->RuleStart_expr_2
        BlockEnd_19->StarLoopBack_22
        StarLoopBack_22->StarLoopEntry_20
        """;
    checkRuleATN(g, "expr", atnText);

    ATN atn = g.getATN();

    // Simulate call stack after input '(x' from rule s
    ParserRuleContext callStackFrom_s = new ParserRuleContext(null, 4);
    ParserRuleContext callStackFrom_expr = new ParserRuleContext(callStackFrom_s, 9);
    int afterID = 14;
    IntervalSet tokens = atn.getExpectedTokens(afterID, callStackFrom_expr);
    assertEquals("{R, PLUS}", tokens.toString(g.getVocabulary()));

    // Simulate call stack after input '(x' from within rule expr
    callStackFrom_expr = new ParserRuleContext(null, 9);
    tokens = atn.getExpectedTokens(afterID, callStackFrom_expr);
    assertEquals("{R, PLUS}", tokens.toString(g.getVocabulary()));
  }
}
