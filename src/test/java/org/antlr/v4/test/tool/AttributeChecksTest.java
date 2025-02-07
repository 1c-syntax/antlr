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

import static org.antlr.v4.tool.ErrorType.*;

public class AttributeChecksTest extends AbstractBaseTest {
  private static final String attributeTemplate =
    """
      parser grammar A;
      @members {<members>}
      tokens{ID}
      a[int x] returns [int y]
      @init {<init>}
          :   id=ID ids+=ID lab=b[34] labs+=b[34] {
      		 <inline>
      		 }
      		 c
          ;
          finally {<finally>}
      b[int d] returns [int e]
          :   {<inline2>}
          ;
      c   :   ;
      """;

  private static final String[] membersChecks = {
    "$a", "error(" + UNKNOWN_SIMPLE_ATTRIBUTE.code + "): A.g4:2:11: unknown attribute reference 'a' in '$a'\n",
    "$a.y", "error(" + UNKNOWN_SIMPLE_ATTRIBUTE.code + "): A.g4:2:11: unknown attribute reference 'a' in '$a.y'\n",
  };

  private static final String[] initChecks = {
    "$text", "",
    "$start", "",
    "$x = $y", "",
    "$y = $x", "",
    "$lab.e", "",
    "$ids", "",
    "$labs", "",

    "$c", "error(" + UNKNOWN_SIMPLE_ATTRIBUTE.code + "): A.g4:5:8: unknown attribute reference 'c' in '$c'\n",
    "$a.q", "error(" + UNKNOWN_RULE_ATTRIBUTE.code + "): A.g4:5:10: unknown attribute 'q' for rule 'a' in '$a.q'\n",
  };

  private static final String[] inlineChecks = {
    "$text", "",
    "$start", "",
    "$x = $y", "",
    "$y = $x", "",
    "$y.b = 3;", "",
    "$ctx.x = $ctx.y", "",
    "$lab.e", "",
    "$lab.text", "",
    "$b.e", "",
    "$c.text", "",
    "$ID", "",
    "$ID.text", "",
    "$id", "",
    "$id.text", "",
    "$ids", "",
    "$labs", "",
  };

  private static final String[] bad_inlineChecks = {
    "$lab", "error(" + ISOLATED_RULE_REF.code + "): A.g4:7:4: missing attribute access on rule reference 'lab' in '$lab'\n",
    "$q", "error(" + UNKNOWN_SIMPLE_ATTRIBUTE.code + "): A.g4:7:4: unknown attribute reference 'q' in '$q'\n",
    "$q.y", "error(" + UNKNOWN_SIMPLE_ATTRIBUTE.code + "): A.g4:7:4: unknown attribute reference 'q' in '$q.y'\n",
    "$q = 3", "error(" + UNKNOWN_SIMPLE_ATTRIBUTE.code + "): A.g4:7:4: unknown attribute reference 'q' in '$q'\n",
    "$q = 3;", "error(" + UNKNOWN_SIMPLE_ATTRIBUTE.code + "): A.g4:7:4: unknown attribute reference 'q' in '$q = 3;'\n",
    "$q.y = 3;", "error(" + UNKNOWN_SIMPLE_ATTRIBUTE.code + "): A.g4:7:4: unknown attribute reference 'q' in '$q.y'\n",
    "$q = $blort;", "error(" + UNKNOWN_SIMPLE_ATTRIBUTE.code + "): A.g4:7:4: unknown attribute reference 'q' in '$q = $blort;'\n" +
    "error(" + UNKNOWN_SIMPLE_ATTRIBUTE.code + "): A.g4:7:9: unknown attribute reference 'blort' in '$blort'\n",
    "$a.ick", "error(" + UNKNOWN_RULE_ATTRIBUTE.code + "): A.g4:7:6: unknown attribute 'ick' for rule 'a' in '$a.ick'\n",
    "$a.ick = 3;", "error(" + UNKNOWN_RULE_ATTRIBUTE.code + "): A.g4:7:6: unknown attribute 'ick' for rule 'a' in '$a.ick'\n",
    "$b.d", "error(" + INVALID_RULE_PARAMETER_REF.code + "): A.g4:7:6: parameter 'd' of rule 'b' is not accessible in this scope: $b.d\n",  // can't see rule ref's arg
    "$d.text", "error(" + UNKNOWN_SIMPLE_ATTRIBUTE.code + "): A.g4:7:4: unknown attribute reference 'd' in '$d.text'\n", // valid rule, but no ref
    "$lab.d", "error(" + INVALID_RULE_PARAMETER_REF.code + "): A.g4:7:8: parameter 'd' of rule 'b' is not accessible in this scope: $lab.d\n",
    "$ids = null;", "error(" + ASSIGNMENT_TO_LIST_LABEL.code + "): A.g4:7:4: cannot assign a value to list label 'ids'\n",
    "$labs = null;", "error(" + ASSIGNMENT_TO_LIST_LABEL.code + "): A.g4:7:4: cannot assign a value to list label 'labs'\n",
  };

  private static final String[] finallyChecks = {
    "$text", "",
    "$start", "",
    "$x = $y", "",
    "$y = $x", "",
    "$lab.e", "",
    "$lab.text", "",
    "$id", "",
    "$id.text", "",
    "$ids", "",
    "$labs", "",

    "$lab", "error(" + ISOLATED_RULE_REF.code + "): A.g4:10:14: missing attribute access on rule reference 'lab' in '$lab'\n",
    "$q", "error(" + UNKNOWN_SIMPLE_ATTRIBUTE.code + "): A.g4:10:14: unknown attribute reference 'q' in '$q'\n",
    "$q.y", "error(" + UNKNOWN_SIMPLE_ATTRIBUTE.code + "): A.g4:10:14: unknown attribute reference 'q' in '$q.y'\n",
    "$q = 3", "error(" + UNKNOWN_SIMPLE_ATTRIBUTE.code + "): A.g4:10:14: unknown attribute reference 'q' in '$q'\n",
    "$q = 3;", "error(" + UNKNOWN_SIMPLE_ATTRIBUTE.code + "): A.g4:10:14: unknown attribute reference 'q' in '$q = 3;'\n",
    "$q.y = 3;", "error(" + UNKNOWN_SIMPLE_ATTRIBUTE.code + "): A.g4:10:14: unknown attribute reference 'q' in '$q.y'\n",
    "$q = $blort;", "error(" + UNKNOWN_SIMPLE_ATTRIBUTE.code + "): A.g4:10:14: unknown attribute reference 'q' in '$q = $blort;'\n" +
    "error(" + UNKNOWN_SIMPLE_ATTRIBUTE.code + "): A.g4:10:19: unknown attribute reference 'blort' in '$blort'\n",
    "$a.ick", "error(" + UNKNOWN_RULE_ATTRIBUTE.code + "): A.g4:10:16: unknown attribute 'ick' for rule 'a' in '$a.ick'\n",
    "$a.ick = 3;", "error(" + UNKNOWN_RULE_ATTRIBUTE.code + "): A.g4:10:16: unknown attribute 'ick' for rule 'a' in '$a.ick'\n",
    "$b.e", "error(" + UNKNOWN_SIMPLE_ATTRIBUTE.code + "): A.g4:10:14: unknown attribute reference 'b' in '$b.e'\n", // can't see rule refs outside alts
    "$b.d", "error(" + UNKNOWN_SIMPLE_ATTRIBUTE.code + "): A.g4:10:14: unknown attribute reference 'b' in '$b.d'\n",
    "$c.text", "error(" + UNKNOWN_SIMPLE_ATTRIBUTE.code + "): A.g4:10:14: unknown attribute reference 'c' in '$c.text'\n",
    "$lab.d", "error(" + INVALID_RULE_PARAMETER_REF.code + "): A.g4:10:18: parameter 'd' of rule 'b' is not accessible in this scope: $lab.d\n",
  };

  private static final String[] dynMembersChecks = {
    "$S", "error(" + UNKNOWN_SIMPLE_ATTRIBUTE.code + "): A.g4:2:11: unknown attribute reference 'S' in '$S'\n",
    "$S::i", "error(" + UNDEFINED_RULE_IN_NONLOCAL_REF.code + "): A.g4:2:11: reference to undefined rule 'S' in non-local ref '$S::i'\n",
    "$S::i=$S::i", "error(" + UNDEFINED_RULE_IN_NONLOCAL_REF.code + "): A.g4:2:11: reference to undefined rule 'S' in non-local ref '$S::i'\n" +
    "error(" + UNDEFINED_RULE_IN_NONLOCAL_REF.code + "): A.g4:2:17: reference to undefined rule 'S' in non-local ref '$S::i'\n",

    "$b::f", "error(" + UNKNOWN_RULE_ATTRIBUTE.code + "): A.g4:2:14: unknown attribute 'f' for rule 'b' in '$b::f'\n",
    "$S::j", "error(" + UNDEFINED_RULE_IN_NONLOCAL_REF.code + "): A.g4:2:11: reference to undefined rule 'S' in non-local ref '$S::j'\n",
    "$S::j = 3;", "error(" + UNDEFINED_RULE_IN_NONLOCAL_REF.code + "): A.g4:2:11: reference to undefined rule 'S' in non-local ref '$S::j = 3;'\n",
    "$S::j = $S::k;", "error(" + UNDEFINED_RULE_IN_NONLOCAL_REF.code + "): A.g4:2:11: reference to undefined rule 'S' in non-local ref '$S::j = $S::k;'\n",
  };

  private static final String[] dynInitChecks = {
    "$a", "error(" + ISOLATED_RULE_REF.code + "): A.g4:5:8: missing attribute access on rule reference 'a' in '$a'\n",
    "$b", "error(" + UNKNOWN_SIMPLE_ATTRIBUTE.code + "): A.g4:5:8: unknown attribute reference 'b' in '$b'\n",
    "$lab", "error(" + ISOLATED_RULE_REF.code + "): A.g4:5:8: missing attribute access on rule reference 'lab' in '$lab'\n",
    "$b::f", "error(" + UNKNOWN_RULE_ATTRIBUTE.code + "): A.g4:5:11: unknown attribute 'f' for rule 'b' in '$b::f'\n",
    "$S::i", "error(" + UNDEFINED_RULE_IN_NONLOCAL_REF.code + "): A.g4:5:8: reference to undefined rule 'S' in non-local ref '$S::i'\n",
    "$S::i=$S::i", "error(" + UNDEFINED_RULE_IN_NONLOCAL_REF.code + "): A.g4:5:8: reference to undefined rule 'S' in non-local ref '$S::i'\n" +
    "error(" + UNDEFINED_RULE_IN_NONLOCAL_REF.code + "): A.g4:5:14: reference to undefined rule 'S' in non-local ref '$S::i'\n",
    "$a::z", "error(" + UNKNOWN_RULE_ATTRIBUTE.code + "): A.g4:5:11: unknown attribute 'z' for rule 'a' in '$a::z'\n",
    "$S", "error(" + UNKNOWN_SIMPLE_ATTRIBUTE.code + "): A.g4:5:8: unknown attribute reference 'S' in '$S'\n",

    "$S::j", "error(" + UNDEFINED_RULE_IN_NONLOCAL_REF.code + "): A.g4:5:8: reference to undefined rule 'S' in non-local ref '$S::j'\n",
    "$S::j = 3;", "error(" + UNDEFINED_RULE_IN_NONLOCAL_REF.code + "): A.g4:5:8: reference to undefined rule 'S' in non-local ref '$S::j = 3;'\n",
    "$S::j = $S::k;", "error(" + UNDEFINED_RULE_IN_NONLOCAL_REF.code + "): A.g4:5:8: reference to undefined rule 'S' in non-local ref '$S::j = $S::k;'\n",
  };

  private static final String[] dynInlineChecks = {
    "$a", "error(" + ISOLATED_RULE_REF.code + "): A.g4:7:4: missing attribute access on rule reference 'a' in '$a'\n",
    "$b", "error(" + ISOLATED_RULE_REF.code + "): A.g4:7:4: missing attribute access on rule reference 'b' in '$b'\n",
    "$lab", "error(" + ISOLATED_RULE_REF.code + "): A.g4:7:4: missing attribute access on rule reference 'lab' in '$lab'\n",
    "$b::f", "error(" + UNKNOWN_RULE_ATTRIBUTE.code + "): A.g4:7:7: unknown attribute 'f' for rule 'b' in '$b::f'\n",
    "$S::i", "error(" + UNDEFINED_RULE_IN_NONLOCAL_REF.code + "): A.g4:7:4: reference to undefined rule 'S' in non-local ref '$S::i'\n",
    "$S::i=$S::i", "error(" + UNDEFINED_RULE_IN_NONLOCAL_REF.code + "): A.g4:7:4: reference to undefined rule 'S' in non-local ref '$S::i'\n" +
    "error(" + UNDEFINED_RULE_IN_NONLOCAL_REF.code + "): A.g4:7:10: reference to undefined rule 'S' in non-local ref '$S::i'\n",
    "$a::z", "error(" + UNKNOWN_RULE_ATTRIBUTE.code + "): A.g4:7:7: unknown attribute 'z' for rule 'a' in '$a::z'\n",

    "$S::j", "error(" + UNDEFINED_RULE_IN_NONLOCAL_REF.code + "): A.g4:7:4: reference to undefined rule 'S' in non-local ref '$S::j'\n",
    "$S::j = 3;", "error(" + UNDEFINED_RULE_IN_NONLOCAL_REF.code + "): A.g4:7:4: reference to undefined rule 'S' in non-local ref '$S::j = 3;'\n",
    "$S::j = $S::k;", "error(" + UNDEFINED_RULE_IN_NONLOCAL_REF.code + "): A.g4:7:4: reference to undefined rule 'S' in non-local ref '$S::j = $S::k;'\n",
    "$Q[-1]::y", "error(" + UNKNOWN_SIMPLE_ATTRIBUTE.code + "): A.g4:7:4: unknown attribute reference 'Q' in '$Q'\n",
    "$Q[-i]::y", "error(" + UNKNOWN_SIMPLE_ATTRIBUTE.code + "): A.g4:7:4: unknown attribute reference 'Q' in '$Q'\n",
    "$Q[i]::y", "error(" + UNKNOWN_SIMPLE_ATTRIBUTE.code + "): A.g4:7:4: unknown attribute reference 'Q' in '$Q'\n",
    "$Q[0]::y", "error(" + UNKNOWN_SIMPLE_ATTRIBUTE.code + "): A.g4:7:4: unknown attribute reference 'Q' in '$Q'\n",
    "$Q[-1]::y = 23;", "error(" + UNKNOWN_SIMPLE_ATTRIBUTE.code + "): A.g4:7:4: unknown attribute reference 'Q' in '$Q'\n",
    "$Q[-i]::y = 23;", "error(" + UNKNOWN_SIMPLE_ATTRIBUTE.code + "): A.g4:7:4: unknown attribute reference 'Q' in '$Q'\n",
    "$Q[i]::y = 23;", "error(" + UNKNOWN_SIMPLE_ATTRIBUTE.code + "): A.g4:7:4: unknown attribute reference 'Q' in '$Q'\n",
    "$Q[0]::y = 23;", "error(" + UNKNOWN_SIMPLE_ATTRIBUTE.code + "): A.g4:7:4: unknown attribute reference 'Q' in '$Q'\n",
    "$S[-1]::y", "error(" + UNKNOWN_SIMPLE_ATTRIBUTE.code + "): A.g4:7:4: unknown attribute reference 'S' in '$S'\n",
    "$S[-i]::y", "error(" + UNKNOWN_SIMPLE_ATTRIBUTE.code + "): A.g4:7:4: unknown attribute reference 'S' in '$S'\n",
    "$S[i]::y", "error(" + UNKNOWN_SIMPLE_ATTRIBUTE.code + "): A.g4:7:4: unknown attribute reference 'S' in '$S'\n",
    "$S[0]::y", "error(" + UNKNOWN_SIMPLE_ATTRIBUTE.code + "): A.g4:7:4: unknown attribute reference 'S' in '$S'\n",
    "$S[-1]::y = 23;", "error(" + UNKNOWN_SIMPLE_ATTRIBUTE.code + "): A.g4:7:4: unknown attribute reference 'S' in '$S'\n",
    "$S[-i]::y = 23;", "error(" + UNKNOWN_SIMPLE_ATTRIBUTE.code + "): A.g4:7:4: unknown attribute reference 'S' in '$S'\n",
    "$S[i]::y = 23;", "error(" + UNKNOWN_SIMPLE_ATTRIBUTE.code + "): A.g4:7:4: unknown attribute reference 'S' in '$S'\n",
    "$S[0]::y = 23;", "error(" + UNKNOWN_SIMPLE_ATTRIBUTE.code + "): A.g4:7:4: unknown attribute reference 'S' in '$S'\n",
    "$S[$S::y]::i", "error(" + UNKNOWN_SIMPLE_ATTRIBUTE.code + "): A.g4:7:4: unknown attribute reference 'S' in '$S'\n" +
    "error(" + UNDEFINED_RULE_IN_NONLOCAL_REF.code + "): A.g4:7:7: reference to undefined rule 'S' in non-local ref '$S::y'\n"
  };

  private static final String[] dynFinallyChecks = {
    "$a", "error(" + ISOLATED_RULE_REF.code + "): A.g4:10:14: missing attribute access on rule reference 'a' in '$a'\n",
    "$b", "error(" + UNKNOWN_SIMPLE_ATTRIBUTE.code + "): A.g4:10:14: unknown attribute reference 'b' in '$b'\n",
    "$lab", "error(" + ISOLATED_RULE_REF.code + "): A.g4:10:14: missing attribute access on rule reference 'lab' in '$lab'\n",
    "$b::f", "error(" + UNKNOWN_RULE_ATTRIBUTE.code + "): A.g4:10:17: unknown attribute 'f' for rule 'b' in '$b::f'\n",
    "$S", "error(" + UNKNOWN_SIMPLE_ATTRIBUTE.code + "): A.g4:10:14: unknown attribute reference 'S' in '$S'\n",
    "$S::i", "error(" + UNDEFINED_RULE_IN_NONLOCAL_REF.code + "): A.g4:10:14: reference to undefined rule 'S' in non-local ref '$S::i'\n",
    "$S::i=$S::i", "error(" + UNDEFINED_RULE_IN_NONLOCAL_REF.code + "): A.g4:10:14: reference to undefined rule 'S' in non-local ref '$S::i'\n" +
    "error(" + UNDEFINED_RULE_IN_NONLOCAL_REF.code + "): A.g4:10:20: reference to undefined rule 'S' in non-local ref '$S::i'\n",
    "$a::z", "error(" + UNKNOWN_RULE_ATTRIBUTE.code + "): A.g4:10:17: unknown attribute 'z' for rule 'a' in '$a::z'\n",

    "$S::j", "error(" + UNDEFINED_RULE_IN_NONLOCAL_REF.code + "): A.g4:10:14: reference to undefined rule 'S' in non-local ref '$S::j'\n",
    "$S::j = 3;", "error(" + UNDEFINED_RULE_IN_NONLOCAL_REF.code + "): A.g4:10:14: reference to undefined rule 'S' in non-local ref '$S::j = 3;'\n",
    "$S::j = $S::k;", "error(" + UNDEFINED_RULE_IN_NONLOCAL_REF.code + "): A.g4:10:14: reference to undefined rule 'S' in non-local ref '$S::j = $S::k;'\n",
  };


  @Test
  public void testMembersActions() {
    testActions("members", membersChecks, attributeTemplate);
  }

  @Test
  public void testDynamicMembersActions() {
    testActions("members", dynMembersChecks, attributeTemplate);
  }

  @Test
  public void testInitActions() {
    testActions("init", initChecks, attributeTemplate);
  }

  @Test
  public void testDynamicInitActions() {
    testActions("init", dynInitChecks, attributeTemplate);
  }

  @Test
  public void testInlineActions() {
    testActions("inline", inlineChecks, attributeTemplate);
  }

  @Test
  public void testDynamicInlineActions() {
    testActions("inline", dynInlineChecks, attributeTemplate);
  }

  @Test
  public void testBadInlineActions() {
    testActions("inline", bad_inlineChecks, attributeTemplate);
  }

  @Test
  public void testFinallyActions() {
    testActions("finally", finallyChecks, attributeTemplate);
  }

  @Test
  public void testDynamicFinallyActions() {
    testActions("finally", dynFinallyChecks, attributeTemplate);
  }

  @Test
  public void testTokenRef() {
    String grammar =
      """
        parser grammar S;
        tokens{ID}
        a : x=ID {Token t = $x; t = $ID;} ;
        """;
    String expected =
      "";
    testErrors(new String[]{grammar, expected}, false);
  }

  public void testActions(String location, String[] pairs, String template) {
    for (int i = 0; i < pairs.length; i += 2) {
      String action = pairs[i];
      String expected = pairs[i + 1];
      ST st = new ST(template);
      st.add(location, action);
      String grammar = st.render();
      testErrors(new String[]{grammar, expected}, false);
    }
  }
}
