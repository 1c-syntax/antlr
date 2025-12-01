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

import org.antlr.v4.Tool;
import org.antlr.v4.automata.ATNPrinter;
import org.antlr.v4.automata.LexerATNFactory;
import org.antlr.v4.automata.ParserATNFactory;
import org.antlr.v4.parse.ANTLRParser;
import org.antlr.v4.runtime.atn.ATN;
import org.antlr.v4.runtime.atn.ATNState;
import org.antlr.v4.tool.ErrorType;
import org.antlr.v4.tool.Grammar;
import org.antlr.v4.tool.LexerGrammar;
import org.antlr.v4.tool.ast.GrammarAST;
import org.antlr.v4.tool.ast.GrammarRootAST;
import org.antlr.v4.tool.ast.RuleAST;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.antlr.v4.TestUtils.assertEquals;
import static org.assertj.core.api.Assertions.assertThat;

public class ATNConstructionTest extends AbstractBaseTest {

  @Test
  public void testA() throws Exception {
    Grammar g = new Grammar(
      "parser grammar P;\n" +
        "a : A;");
    String expecting =
      """
        RuleStart_a_0->s2
        s2-A->s3
        s3->RuleStop_a_1
        RuleStop_a_1-EOF->s4
        """;
    checkRuleATN(g, "a", expecting);
  }

  @Test
  public void testAB() throws Exception {
    Grammar g = new Grammar(
      "parser grammar P;\n" +
        "a : A B ;");
    String expecting =
      """
        RuleStart_a_0->s2
        s2-A->s3
        s3-B->s4
        s4->RuleStop_a_1
        RuleStop_a_1-EOF->s5
        """;
    checkRuleATN(g, "a", expecting);
  }

  @Test
  public void testAorB() throws Exception {
    Grammar g = new Grammar(
      "parser grammar P;\n" +
        "a : A | B {;} ;");
    String expecting =
      """
        RuleStart_a_0->BlockStart_5
        BlockStart_5->s2
        BlockStart_5->s3
        s2-A->BlockEnd_6
        s3-B->s4
        BlockEnd_6->RuleStop_a_1
        s4-action_0:-1->BlockEnd_6
        RuleStop_a_1-EOF->s7
        """;
    checkRuleATN(g, "a", expecting);
  }

  @Test
  public void testSetAorB() throws Exception {
    Grammar g = new Grammar(
      "parser grammar P;\n" +
        "a : A | B ;");
    String expecting =
      """
        RuleStart_a_0->s2
        s2-{A, B}->s3
        s3->RuleStop_a_1
        RuleStop_a_1-EOF->s4
        """;
    checkRuleATN(g, "a", expecting);
  }

  @Test
  public void testLexerIsntSetMultiCharString() throws Exception {
    LexerGrammar g = new LexerGrammar(
      "lexer grammar P;\n" +
        "A : ('0x' | '0X') ;");
    String expecting =
      """
        s0->RuleStart_A_1
        RuleStart_A_1->BlockStart_7
        BlockStart_7->s3
        BlockStart_7->s5
        s3-'0'->s4
        s5-'0'->s6
        s4-'x'->BlockEnd_8
        s6-'X'->BlockEnd_8
        BlockEnd_8->RuleStop_A_2
        """;
    checkTokensRule(g, null, expecting);
  }

  @Test
  public void testRange() throws Exception {
    LexerGrammar g = new LexerGrammar(
      "lexer grammar P;\n" +
        "A : 'a'..'c' ;"
    );
    String expecting =
      """
        s0->RuleStart_A_1
        RuleStart_A_1->s3
        s3-'a'..'c'->s4
        s4->RuleStop_A_2
        """;
    checkTokensRule(g, null, expecting);
  }

  @Test
  public void testCharSet() throws Exception {
    LexerGrammar g = new LexerGrammar(
      "lexer grammar P;\n" +
        "A : [abc] ;"
    );
    String expecting =
      """
        s0->RuleStart_A_1
        RuleStart_A_1->s3
        s3-{97..99}->s4
        s4->RuleStop_A_2
        """;
    checkTokensRule(g, null, expecting);
  }

  @Test
  public void testCharSetRange() throws Exception {
    LexerGrammar g = new LexerGrammar(
      "lexer grammar P;\n" +
        "A : [a-c] ;"
    );
    String expecting =
      """
        s0->RuleStart_A_1
        RuleStart_A_1->s3
        s3-{97..99}->s4
        s4->RuleStop_A_2
        """;
    checkTokensRule(g, null, expecting);
  }

  @Test
  public void testCharSetUnicodeBMPEscape() throws Exception {
    LexerGrammar g = new LexerGrammar(
      "lexer grammar P;\n" +
        "A : [\\uABCD] ;"
    );
    String expecting =
      """
        s0->RuleStart_A_1
        RuleStart_A_1->s3
        s3-43981->s4
        s4->RuleStop_A_2
        """;
    checkTokensRule(g, null, expecting);
  }

  @Test
  public void testCharSetUnicodeBMPEscapeRange() throws Exception {
    LexerGrammar g = new LexerGrammar(
      "lexer grammar P;\n" +
        "A : [a-c\\uABCD-\\uABFF] ;"
    );
    String expecting =
      """
        s0->RuleStart_A_1
        RuleStart_A_1->s3
        s3-{97..99, 43981..44031}->s4
        s4->RuleStop_A_2
        """;
    checkTokensRule(g, null, expecting);
  }

  @Test
  public void testCharSetUnicodeSMPEscape() throws Exception {
    LexerGrammar g = new LexerGrammar(
      "lexer grammar P;\n" +
        "A : [\\u{10ABCD}] ;"
    );
    String expecting =
      """
        s0->RuleStart_A_1
        RuleStart_A_1->s3
        s3-1092557->s4
        s4->RuleStop_A_2
        """;
    checkTokensRule(g, null, expecting);
  }

  @Test
  public void testCharSetUnicodeSMPEscapeRange() throws Exception {
    LexerGrammar g = new LexerGrammar(
      "lexer grammar P;\n" +
        "A : [a-c\\u{10ABCD}-\\u{10ABFF}] ;"
    );
    String expecting =
      """
        s0->RuleStart_A_1
        RuleStart_A_1->s3
        s3-{97..99, 1092557..1092607}->s4
        s4->RuleStop_A_2
        """;
    checkTokensRule(g, null, expecting);
  }

  @Test
  public void testCharSetUnicodePropertyEscape() throws Exception {
    // The Gothic script is long dead and unlikely to change (which would
    // cause this test to fail)
    LexerGrammar g = new LexerGrammar(
      "lexer grammar P;\n" +
        "A : [\\p{Gothic}] ;"
    );
    String expecting =
      """
        s0->RuleStart_A_1
        RuleStart_A_1->s3
        s3-{66352..66378}->s4
        s4->RuleStop_A_2
        """;
    checkTokensRule(g, null, expecting);
  }

  @Test
  public void testCharSetUnicodePropertyInvertEscape() throws Exception {
    LexerGrammar g = new LexerGrammar(
      "lexer grammar P;\n" +
        "A : [\\P{Gothic}] ;"
    );
    String expecting =
      """
        s0->RuleStart_A_1
        RuleStart_A_1->s3
        s3-{0..66351, 66379..1114111}->s4
        s4->RuleStop_A_2
        """;
    checkTokensRule(g, null, expecting);
  }

  @Test
  public void testCharSetUnicodeMultiplePropertyEscape() throws Exception {
    // Ditto the Mahajani script. Not going to change soon. I hope.
    LexerGrammar g = new LexerGrammar(
      "lexer grammar P;\n" +
        "A : [\\p{Gothic}\\p{Mahajani}] ;"
    );
    String expecting =
      """
        s0->RuleStart_A_1
        RuleStart_A_1->s3
        s3-{66352..66378, 69968..70006}->s4
        s4->RuleStop_A_2
        """;
    checkTokensRule(g, null, expecting);
  }

  @Test
  public void testCharSetUnicodePropertyOverlap() throws Exception {
    LexerGrammar g = new LexerGrammar(
      "lexer grammar P;\n" +
        "A : [\\p{ASCII_Hex_Digit}\\p{Hex_Digit}] ;"
    );
    String expecting =
      """
        s0->RuleStart_A_1
        RuleStart_A_1->s3
        s3-{48..57, 65..70, 97..102, 65296..65305, 65313..65318, 65345..65350}->s4
        s4->RuleStop_A_2
        """;
    checkTokensRule(g, null, expecting);
  }

  @Test
  public void testRangeOrRange() throws Exception {
    LexerGrammar g = new LexerGrammar(
      "lexer grammar P;\n" +
        "A : ('a'..'c' 'h' | 'q' 'j'..'l') ;"
    );
    String expecting =
      """
        s0->RuleStart_A_1
        RuleStart_A_1->BlockStart_7
        BlockStart_7->s3
        BlockStart_7->s5
        s3-'a'..'c'->s4
        s5-'q'->s6
        s4-'h'->BlockEnd_8
        s6-'j'..'l'->BlockEnd_8
        BlockEnd_8->RuleStop_A_2
        """;
    checkTokensRule(g, null, expecting);
  }

  @Test
  public void testStringLiteralInParser() throws Exception {
    Grammar g = new Grammar(
      "grammar P;\n" +
        "a : A|'b' ;"
    );
    String expecting =
      """
        RuleStart_a_0->s2
        s2-{'b', A}->s3
        s3->RuleStop_a_1
        RuleStop_a_1-EOF->s4
        """;
    checkRuleATN(g, "a", expecting);
  }

  @Test
  public void testABorCD() throws Exception {
    Grammar g = new Grammar(
      "parser grammar P;\n" +
        "a : A B | C D;");
    String expecting =
      """
        RuleStart_a_0->BlockStart_6
        BlockStart_6->s2
        BlockStart_6->s4
        s2-A->s3
        s4-C->s5
        s3-B->BlockEnd_7
        s5-D->BlockEnd_7
        BlockEnd_7->RuleStop_a_1
        RuleStop_a_1-EOF->s8
        """;
    checkRuleATN(g, "a", expecting);
  }

  @Test
  public void testbA() throws Exception {
    Grammar g = new Grammar(
      """
        parser grammar P;
        a : b A ;
        b : B ;""");
    String expecting =
      """
        RuleStart_a_0->s4
        s4-b->RuleStart_b_2
        s5-A->s6
        s6->RuleStop_a_1
        RuleStop_a_1-EOF->s9
        """;
    checkRuleATN(g, "a", expecting);
    expecting =
      """
        RuleStart_b_2->s7
        s7-B->s8
        s8->RuleStop_b_3
        RuleStop_b_3->s5
        """;
    checkRuleATN(g, "b", expecting);
  }

  @Test
  public void testFollow() throws Exception {
    Grammar g = new Grammar(
      """
        parser grammar P;
        a : b A ;
        b : B ;
        c : b C;""");
    String expecting =
      """
        RuleStart_b_2->s9
        s9-B->s10
        s10->RuleStop_b_3
        RuleStop_b_3->s7
        RuleStop_b_3->s12
        """;
    checkRuleATN(g, "b", expecting);
  }

  @Test
  public void testAorEpsilon() throws Exception {
    Grammar g = new Grammar(
      "parser grammar P;\n" +
        "a : A | ;");
    String expecting =
      """
        RuleStart_a_0->BlockStart_4
        BlockStart_4->s2
        BlockStart_4->s3
        s2-A->BlockEnd_5
        s3->BlockEnd_5
        BlockEnd_5->RuleStop_a_1
        RuleStop_a_1-EOF->s6
        """;
    checkRuleATN(g, "a", expecting);
  }

  @Test
  public void testAOptional() throws Exception {
    Grammar g = new Grammar(
      "parser grammar P;\n" +
        "a : A?;");
    String expecting =
      """
        RuleStart_a_0->BlockStart_3
        BlockStart_3->s2
        BlockStart_3->BlockEnd_4
        s2-A->BlockEnd_4
        BlockEnd_4->RuleStop_a_1
        RuleStop_a_1-EOF->s5
        """;
    checkRuleATN(g, "a", expecting);
  }

  @Test
  public void testAorBoptional() throws Exception {
    Grammar g = new Grammar(
      "parser grammar P;\n" +
        "a : (A{;}|B)?;");
    String expecting =
      """
        RuleStart_a_0->BlockStart_5
        BlockStart_5->s2
        BlockStart_5->s4
        BlockStart_5->BlockEnd_6
        s2-A->s3
        s4-B->BlockEnd_6
        BlockEnd_6->RuleStop_a_1
        s3-action_0:-1->BlockEnd_6
        RuleStop_a_1-EOF->s7
        """;
    checkRuleATN(g, "a", expecting);
  }

  @Test
  public void testSetAorBoptional() throws Exception {
    Grammar g = new Grammar(
      "parser grammar P;\n" +
        "a : (A|B)?;");
    String expecting =
      """
        RuleStart_a_0->BlockStart_3
        BlockStart_3->s2
        BlockStart_3->BlockEnd_4
        s2-{A, B}->BlockEnd_4
        BlockEnd_4->RuleStop_a_1
        RuleStop_a_1-EOF->s5
        """;
    checkRuleATN(g, "a", expecting);
  }

  @Test
  public void testAorBthenC() throws Exception {
    Grammar g = new Grammar(
      "parser grammar P;\n" +
        "a : (A | B) C;");
    String expecting =
      """
        RuleStart_a_0->s2
        s2-{A, B}->s3
        s3-C->s4
        s4->RuleStop_a_1
        RuleStop_a_1-EOF->s5
        """;
    checkRuleATN(g, "a", expecting);
  }

  @Test
  public void testAplus() throws Exception {
    Grammar g = new Grammar(
      "parser grammar P;\n" +
        "a : A+;");
    String expecting =
      """
        RuleStart_a_0->PlusBlockStart_3
        PlusBlockStart_3->s2
        s2-A->BlockEnd_4
        BlockEnd_4->PlusLoopBack_5
        PlusLoopBack_5->PlusBlockStart_3
        PlusLoopBack_5->s6
        s6->RuleStop_a_1
        RuleStop_a_1-EOF->s7
        """;
    checkRuleATN(g, "a", expecting);
  }

  @Test
  public void testAplusSingleAltHasPlusASTPointingAtLoopBackState() throws Exception {
    Grammar g = new Grammar(
      "parser grammar P;\n" +
        "s : a B ;\n" +      // (RULE a (BLOCK (ALT (+ (BLOCK (ALT A))))))
        "a : A+;");
    String expecting =
      """
        RuleStart_a_2->PlusBlockStart_8
        PlusBlockStart_8->s7
        s7-A->BlockEnd_9
        BlockEnd_9->PlusLoopBack_10
        PlusLoopBack_10->PlusBlockStart_8
        PlusLoopBack_10->s11
        s11->RuleStop_a_3
        RuleStop_a_3->s5
        """;
    checkRuleATN(g, "a", expecting);
    // Get all AST -> ATNState relationships. Make sure loopback is covered when no loop entry decision
    List<GrammarAST> ruleNodes = g.ast.getNodesWithType(ANTLRParser.RULE);
    RuleAST a = (RuleAST) ruleNodes.get(1);
    List<GrammarAST> nodesInRule = a.getNodesWithType(null);
    Map<GrammarAST, ATNState> covered = new LinkedHashMap<>();
    for (GrammarAST node : nodesInRule) {
      if (node.atnState != null) {
        covered.put(node, node.atnState);
      }
    }
    assertEquals("{RULE=2, BLOCK=8, +=10, BLOCK=8, A=7}", covered.toString());
  }

  @Test
  public void testAorBplus() throws Exception {
    Grammar g = new Grammar(
      "parser grammar P;\n" +
        "a : (A|B{;})+;");
    String expecting =
      """
        RuleStart_a_0->PlusBlockStart_5
        PlusBlockStart_5->s2
        PlusBlockStart_5->s3
        s2-A->BlockEnd_6
        s3-B->s4
        BlockEnd_6->PlusLoopBack_7
        s4-action_0:-1->BlockEnd_6
        PlusLoopBack_7->PlusBlockStart_5
        PlusLoopBack_7->s8
        s8->RuleStop_a_1
        RuleStop_a_1-EOF->s9
        """;
    checkRuleATN(g, "a", expecting);
  }

  @Test
  public void testAorBorEmptyPlus() throws Exception {
    Grammar g = new Grammar(
      "parser grammar P;\n" +
        "a : (A | B | )+ ;");
    String expecting =
      """
        RuleStart_a_0->PlusBlockStart_5
        PlusBlockStart_5->s2
        PlusBlockStart_5->s3
        PlusBlockStart_5->s4
        s2-A->BlockEnd_6
        s3-B->BlockEnd_6
        s4->BlockEnd_6
        BlockEnd_6->PlusLoopBack_7
        PlusLoopBack_7->PlusBlockStart_5
        PlusLoopBack_7->s8
        s8->RuleStop_a_1
        RuleStop_a_1-EOF->s9
        """;
    checkRuleATN(g, "a", expecting);
  }

  @Test
  public void testEmptyOrEmpty() throws Exception {
    Grammar g = new Grammar(
      "parser grammar P;\n" +
        "a : | ;");
    String expecting =
      """
        RuleStart_a_0->BlockStart_4
        BlockStart_4->s2
        BlockStart_4->s3
        s2->BlockEnd_5
        s3->BlockEnd_5
        BlockEnd_5->RuleStop_a_1
        RuleStop_a_1-EOF->s6
        """;
    checkRuleATN(g, "a", expecting);
  }

  @Test
  public void testAStar() throws Exception {
    Grammar g = new Grammar(
      "parser grammar P;\n" +
        "a : A*;");
    String expecting =
      """
        RuleStart_a_0->StarLoopEntry_5
        StarLoopEntry_5->StarBlockStart_3
        StarLoopEntry_5->s6
        StarBlockStart_3->s2
        s6->RuleStop_a_1
        s2-A->BlockEnd_4
        RuleStop_a_1-EOF->s8
        BlockEnd_4->StarLoopBack_7
        StarLoopBack_7->StarLoopEntry_5
        """;
    checkRuleATN(g, "a", expecting);
  }

  @Test
  public void testNestedAstar() throws Exception {
    Grammar g = new Grammar(
      "parser grammar P;\n" +
        "a : (COMMA ID*)*;");
    String expecting =
      """
        RuleStart_a_0->StarLoopEntry_11
        StarLoopEntry_11->StarBlockStart_9
        StarLoopEntry_11->s12
        StarBlockStart_9->s2
        s12->RuleStop_a_1
        s2-COMMA->StarLoopEntry_6
        RuleStop_a_1-EOF->s14
        StarLoopEntry_6->StarBlockStart_4
        StarLoopEntry_6->s7
        StarBlockStart_4->s3
        s7->BlockEnd_10
        s3-ID->BlockEnd_5
        BlockEnd_10->StarLoopBack_13
        BlockEnd_5->StarLoopBack_8
        StarLoopBack_13->StarLoopEntry_11
        StarLoopBack_8->StarLoopEntry_6
        """;
    checkRuleATN(g, "a", expecting);
  }

  @Test
  public void testAorBstar() throws Exception {
    Grammar g = new Grammar(
      "parser grammar P;\n" +
        "a : (A | B{;})* ;");
    String expecting =
      """
        RuleStart_a_0->StarLoopEntry_7
        StarLoopEntry_7->StarBlockStart_5
        StarLoopEntry_7->s8
        StarBlockStart_5->s2
        StarBlockStart_5->s3
        s8->RuleStop_a_1
        s2-A->BlockEnd_6
        s3-B->s4
        RuleStop_a_1-EOF->s10
        BlockEnd_6->StarLoopBack_9
        s4-action_0:-1->BlockEnd_6
        StarLoopBack_9->StarLoopEntry_7
        """;
    checkRuleATN(g, "a", expecting);
  }

  @Test
  public void testPredicatedAorB() throws Exception {
    Grammar g = new Grammar(
      "parser grammar P;\n" +
        "a : {p1}? A | {p2}? B ;");
    String expecting =
      """
        RuleStart_a_0->BlockStart_6
        BlockStart_6->s2
        BlockStart_6->s4
        s2-pred_0:0->s3
        s4-pred_0:1->s5
        s3-A->BlockEnd_7
        s5-B->BlockEnd_7
        BlockEnd_7->RuleStop_a_1
        RuleStop_a_1-EOF->s8
        """;
    checkRuleATN(g, "a", expecting);
  }

  @Test
  public void testParserRuleRefInLexerRule() {
    boolean threwException = false;
    ErrorQueue errorQueue = new ErrorQueue();
    try {
      String gstr =
        """
          grammar U;
          a : A;
          A : a;
          """;

      Tool tool = new Tool();
      tool.removeListeners();
      tool.addListener(errorQueue);
      assertEquals(0, errorQueue.size());
      GrammarRootAST grammarRootAST = tool.parseGrammarFromString(gstr);
      assertEquals(0, errorQueue.size());
      Grammar g = tool.createGrammar(grammarRootAST);
      assertEquals(0, errorQueue.size());
      g.fileName = "<string>";
      tool.process(g, false);
    } catch (Exception e) {
      threwException = true;
      e.printStackTrace();
    }
    System.out.println(errorQueue);
    assertEquals(1, errorQueue.errors.size());
    assertEquals(ErrorType.PARSER_RULE_REF_IN_LEXER_RULE, errorQueue.errors.get(0).getErrorType());
    assertEquals("[a, A]", Arrays.toString(errorQueue.errors.get(0).getArgs()));
    assertThat(!threwException).isTrue();
  }

  /**
   * Test for <a href="https://github.com/antlr/antlr4/issues/1369">...</a>
   * Repeated edges:
   * <p>
   * RuleStop_e_3->BlockEnd_26
   * RuleStop_e_3->BlockEnd_26
   * RuleStop_e_3->BlockEnd_26
   */
  @Test
  public void testForRepeatedTransitionsToStopState() throws Exception {
    String gstr =
      """
        grammar T;
        \t s : e EOF;
        \t e :<assoc=right> e '*' e
        \t   |<assoc=right> e '+' e
        \t   |<assoc=right> e '?' e ':' e
        \t   |<assoc=right> e '=' e
        \t   | ID
        \t   ;
        \t ID : 'a'..'z'+ ;
        \t WS : (' '|'\\n') -> skip ;""";
    Grammar g = new Grammar(gstr);
    String expecting =
      """
        RuleStart_e_2->s7
        s7-action_1:-1->s8
        s8-ID->s9
        s9->StarLoopEntry_27
        StarLoopEntry_27->StarBlockStart_25
        StarLoopEntry_27->s28
        StarBlockStart_25->s10
        StarBlockStart_25->s13
        StarBlockStart_25->s16
        StarBlockStart_25->s22
        s28->RuleStop_e_3
        s10-5 >= _p->s11
        s13-4 >= _p->s14
        s16-3 >= _p->s17
        s22-2 >= _p->s23
        RuleStop_e_3->s5
        RuleStop_e_3->BlockEnd_26
        RuleStop_e_3->s19
        RuleStop_e_3->s21
        s11-'*'->s12
        s14-'+'->s15
        s17-'?'->s18
        s23-'='->s24
        s12-e->RuleStart_e_2
        s15-e->RuleStart_e_2
        s18-e->RuleStart_e_2
        s24-e->RuleStart_e_2
        BlockEnd_26->StarLoopBack_29
        s19-':'->s20
        StarLoopBack_29->StarLoopEntry_27
        s20-e->RuleStart_e_2
        s21->BlockEnd_26
        """;
    checkRuleATN(g, "e", expecting);
  }

  @Test
  public void testDefaultMode() throws Exception {
    LexerGrammar g = new LexerGrammar(
      """
        lexer grammar L;
        A : 'a' ;
        X : 'x' ;
        mode FOO;
        B : 'b' ;
        C : 'c' ;
        """);
    String expecting =
      """
        s0->RuleStart_A_2
        s0->RuleStart_X_4
        RuleStart_A_2->s10
        RuleStart_X_4->s12
        s10-'a'->s11
        s12-'x'->s13
        s11->RuleStop_A_3
        s13->RuleStop_X_5
        """;
    checkTokensRule(g, "DEFAULT_MODE", expecting);
  }

  @Test
  public void testMode() throws Exception {
    LexerGrammar g = new LexerGrammar(
      """
        lexer grammar L;
        A : 'a' ;
        X : 'x' ;
        mode FOO;
        B : 'b' ;
        C : 'c' ;
        """);
    String expecting =
      """
        s1->RuleStart_B_6
        s1->RuleStart_C_8
        RuleStart_B_6->s14
        RuleStart_C_8->s16
        s14-'b'->s15
        s16-'c'->s17
        s15->RuleStop_B_7
        s17->RuleStop_C_9
        """;
    checkTokensRule(g, "FOO", expecting);
  }

  void checkTokensRule(LexerGrammar g, String modeName, String expecting) {
    if (modeName == null) modeName = "DEFAULT_MODE";
    if (g.modes.get(modeName) == null) {
      System.err.println("no such mode " + modeName);
      return;
    }
    ParserATNFactory f = new LexerATNFactory(g);
    ATN nfa = f.createATN();
    ATNState startState = nfa.modeNameToStartState.get(modeName);
    ATNPrinter serializer = new ATNPrinter(g, startState);
    String result = serializer.asString();
    assertEquals(expecting, result);
  }
}
