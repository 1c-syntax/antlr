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

import org.junit.jupiter.api.Test;
import org.stringtemplate.v4.ST;

import static org.antlr.v4.tool.ErrorType.ARG_CONFLICTS_WITH_RULE;
import static org.antlr.v4.tool.ErrorType.ILLEGAL_OPTION;
import static org.antlr.v4.tool.ErrorType.LABEL_BLOCK_NOT_A_SET;
import static org.antlr.v4.tool.ErrorType.LABEL_CONFLICTS_WITH_ARG;
import static org.antlr.v4.tool.ErrorType.LABEL_CONFLICTS_WITH_LOCAL;
import static org.antlr.v4.tool.ErrorType.LABEL_CONFLICTS_WITH_RETVAL;
import static org.antlr.v4.tool.ErrorType.LABEL_CONFLICTS_WITH_RULE;
import static org.antlr.v4.tool.ErrorType.LOCAL_CONFLICTS_WITH_ARG;
import static org.antlr.v4.tool.ErrorType.LOCAL_CONFLICTS_WITH_RETVAL;
import static org.antlr.v4.tool.ErrorType.LOCAL_CONFLICTS_WITH_RULE;
import static org.antlr.v4.tool.ErrorType.REPEATED_PREQUEL;
import static org.antlr.v4.tool.ErrorType.RETVAL_CONFLICTS_WITH_ARG;
import static org.antlr.v4.tool.ErrorType.RETVAL_CONFLICTS_WITH_RULE;
import static org.antlr.v4.tool.ErrorType.TOKEN_NAMES_MUST_START_UPPER;

public class BasicSemanticErrorsTest extends AbstractBaseTest {
  static String[] U = {
    // INPUT
    """
parser grammar U;
options { foo=bar; k=3;}
tokens {
        ID,
        f,
        S
}
tokens { A }
options { x=y; }

a
options { blech=bar; greedy=true; }
        :       ID
        ;
b : ( options { ick=bar; greedy=true; } : ID )+ ;
c : ID<blue> ID<x=y> ;""",
    // YIELDS
    "warning(" + ILLEGAL_OPTION.code + "): U.g4:2:10: unsupported option 'foo'\n" +
      "warning(" + ILLEGAL_OPTION.code + "): U.g4:2:19: unsupported option 'k'\n" +
      "error(" + TOKEN_NAMES_MUST_START_UPPER.code + "): U.g4:5:8: token names must start with an uppercase letter: f\n" +
      "warning(" + ILLEGAL_OPTION.code + "): U.g4:9:10: unsupported option 'x'\n" +
      "error(" + REPEATED_PREQUEL.code + "): U.g4:9:0: repeated grammar prequel spec (options, tokens, or import); please merge\n" +
      "error(" + REPEATED_PREQUEL.code + "): U.g4:8:0: repeated grammar prequel spec (options, tokens, or import); please merge\n" +
      "warning(" + ILLEGAL_OPTION.code + "): U.g4:12:10: unsupported option 'blech'\n" +
      "warning(" + ILLEGAL_OPTION.code + "): U.g4:12:21: unsupported option 'greedy'\n" +
      "warning(" + ILLEGAL_OPTION.code + "): U.g4:15:16: unsupported option 'ick'\n" +
      "warning(" + ILLEGAL_OPTION.code + "): U.g4:15:25: unsupported option 'greedy'\n" +
      "warning(" + ILLEGAL_OPTION.code + "): U.g4:16:16: unsupported option 'x'\n",
  };


  @Test
  public void testU() {
    super.testErrors(U, false);
  }

  /**
   * Regression test for #25 "Don't allow labels on not token set subrules".
   * <a href="https://github.com/antlr/antlr4/issues/25">...</a>
   */
  @Test
  public void testIllegalNonSetLabel() {
    String grammar =
      """
        grammar T;
        ss : op=('=' | '+=' | expr) EOF;
        expr : '=' '=';
        """;

    String expected =
      "error(" + LABEL_BLOCK_NOT_A_SET.code + "): T.g4:2:5: label 'op' assigned to a block which is not a set\n";

    testErrors(new String[]{grammar, expected}, false);
  }

  @Test
  public void testArgumentRetvalLocalConflicts() {
    String grammarTemplate =
      """
        grammar T;
        ss<if(args)>[<args>]<endif> <if(retvals)>returns [<retvals>]<endif>
        <if(locals)>locals [<locals>]<endif>
          : <body> EOF;
        expr : '=';
        """;

    String expected =
      "error(" + ARG_CONFLICTS_WITH_RULE.code + "): T.g4:2:7: parameter 'expr' conflicts with rule with same name\n" +
        "error(" + RETVAL_CONFLICTS_WITH_RULE.code + "): T.g4:2:26: return value 'expr' conflicts with rule with same name\n" +
        "error(" + LOCAL_CONFLICTS_WITH_RULE.code + "): T.g4:3:12: local 'expr' conflicts with rule with same name\n" +
        "error(" + RETVAL_CONFLICTS_WITH_ARG.code + "): T.g4:2:26: return value 'expr' conflicts with parameter with same name\n" +
        "error(" + LOCAL_CONFLICTS_WITH_ARG.code + "): T.g4:3:12: local 'expr' conflicts with parameter with same name\n" +
        "error(" + LOCAL_CONFLICTS_WITH_RETVAL.code + "): T.g4:3:12: local 'expr' conflicts with return value with same name\n" +
        "error(" + LABEL_CONFLICTS_WITH_RULE.code + "): T.g4:4:4: label 'expr' conflicts with rule with same name\n" +
        "error(" + LABEL_CONFLICTS_WITH_ARG.code + "): T.g4:4:4: label 'expr' conflicts with parameter with same name\n" +
        "error(" + LABEL_CONFLICTS_WITH_RETVAL.code + "): T.g4:4:4: label 'expr' conflicts with return value with same name\n" +
        "error(" + LABEL_CONFLICTS_WITH_LOCAL.code + "): T.g4:4:4: label 'expr' conflicts with local with same name\n";
    ST grammarST = new ST(grammarTemplate);
    grammarST.add("args", "int expr");
    grammarST.add("retvals", "int expr");
    grammarST.add("locals", "int expr");
    grammarST.add("body", "expr=expr");
    testErrors(new String[]{grammarST.render(), expected}, false);
  }
}
