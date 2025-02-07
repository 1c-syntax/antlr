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

import org.antlr.v4.tool.Grammar;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ActionTranslationTest extends AbstractBaseTest {
  String attributeTemplate =
    """
      attributeTemplate(members,init,inline,finally,inline2) ::= <<
      parser grammar A;
      @members {#members#<members>#end-members#}
      a[int x, int x1] returns [int y]
      @init {#init#<init>#end-init#}
          :   id=ID ids+=ID lab=b[34] c d {
      		 #inline#<inline>#end-inline#
      		 }
      		 c
          ;
          finally {#finally#<finally>#end-finally#}
      b[int d] returns [int e]
          :   {#inline2#<inline2>#end-inline2#}
          ;
      c returns [int x, int y] : ;
      d	 :   ;
      >>""";


  @Test
  public void testEscapedLessThanInAction() throws Exception {
    String action = "i<3; '<xmltag>'";
    String expected = "i<3; '<xmltag>'";
    testActions(attributeTemplate, "members", action, expected);
    testActions(attributeTemplate, "init", action, expected);
    testActions(attributeTemplate, "inline", action, expected);
    testActions(attributeTemplate, "finally", action, expected);
    testActions(attributeTemplate, "inline2", action, expected);
  }

  @Test
  public void testEscaped$InAction() throws Exception {
    String action = "int \\$n; \"\\$in string\\$\"";
    String expected = "int $n; \"$in string$\"";
    testActions(attributeTemplate, "members", action, expected);
    testActions(attributeTemplate, "init", action, expected);
    testActions(attributeTemplate, "inline", action, expected);
    testActions(attributeTemplate, "finally", action, expected);
    testActions(attributeTemplate, "inline2", action, expected);
  }

  /**
   * Regression test for "in antlr v4 lexer, $ translation issue in action".
   * <a href="https://github.com/antlr/antlr4/issues/176">...</a>
   */
  @Test
  public void testUnescaped$InAction() throws Exception {
    String action = "\\$string$";
    String expected = "$string$";
    testActions(attributeTemplate, "members", action, expected);
    testActions(attributeTemplate, "init", action, expected);
    testActions(attributeTemplate, "inline", action, expected);
    testActions(attributeTemplate, "finally", action, expected);
    testActions(attributeTemplate, "inline2", action, expected);
  }

  @Test
  public void testEscapedSlash() throws Exception {
    String action = "x = '\\n';";
    String expected = "x = '\\n';";
    testActions(attributeTemplate, "members", action, expected);
    testActions(attributeTemplate, "init", action, expected);
    testActions(attributeTemplate, "inline", action, expected);
    testActions(attributeTemplate, "finally", action, expected);
    testActions(attributeTemplate, "inline2", action, expected);
  }

  @Test
  public void testComplicatedArgParsing() throws Exception {
    String action = "x, (*a).foo(21,33), 3.2+1, '\\n', " +
      "\"a,oo\\nick\", {bl, \"fdkj\"eck}";
    String expected = "x, (*a).foo(21,33), 3.2+1, '\\n', " +
      "\"a,oo\\nick\", {bl, \"fdkj\"eck}";
    testActions(attributeTemplate, "members", action, expected);
    testActions(attributeTemplate, "init", action, expected);
    testActions(attributeTemplate, "inline", action, expected);
    testActions(attributeTemplate, "finally", action, expected);
    testActions(attributeTemplate, "inline2", action, expected);
  }

  @Test
  public void testComplicatedArgParsingWithTranslation() throws Exception {
    String action = "x, $ID.text+\"3242\", (*$ID).foo(21,33), 3.2+1, '\\n', " +
      "\"a,oo\\nick\", {bl, \"fdkj\"eck}";
    String expected =
      "x, (_localctx.ID!=null?_localctx.ID.getText():null)+\"3242\", " +
        "(*_localctx.ID).foo(21,33), 3.2+1, '\\n', \"a,oo\\nick\", {bl, \"fdkj\"eck}";
    testActions(attributeTemplate, "inline", action, expected);
  }

  @Test
  public void testArguments() throws Exception {
    String action = "$x; $ctx.x";
    String expected = "_localctx.x; _localctx.x";
    testActions(attributeTemplate, "inline", action, expected);
  }

  @Test
  public void testReturnValue() throws Exception {
    String action = "$y; $ctx.y";
    String expected = "_localctx.y; _localctx.y";
    testActions(attributeTemplate, "inline", action, expected);
  }

  @Test
  public void testReturnValueWithNumber() throws Exception {
    String action = "$ctx.x1";
    String expected = "_localctx.x1";
    testActions(attributeTemplate, "inline", action, expected);
  }

  @Test
  public void testReturnValuesCurrentRule() throws Exception {
    String action = "$y; $ctx.y;";
    String expected = "_localctx.y; _localctx.y;";
    testActions(attributeTemplate, "inline", action, expected);
  }

  @Test
  public void testReturnValues() throws Exception {
    String action = "$lab.e; $b.e; $y.e = \"\";";
    String expected = "_localctx.lab.e; _localctx.b.e; _localctx.y.e = \"\";";
    testActions(attributeTemplate, "inline", action, expected);
  }

  @Test
  public void testReturnWithMultipleRuleRefs() throws Exception {
    String action = "$c.x; $c.y;";
    String expected = "_localctx.c.x; _localctx.c.y;";
    testActions(attributeTemplate, "inline", action, expected);
  }

  @Test
  public void testTokenRefs() throws Exception {
    String action = "$id; $ID; $id.text; $id.getText(); $id.line;";
    String expected = "_localctx.id; _localctx.ID; (_localctx.id!=null?_localctx.id.getText():null);" +
      " _localctx.id.getText(); (_localctx.id!=null?_localctx.id.getLine():0);";
    testActions(attributeTemplate, "inline", action, expected);
  }

  @Test
  public void testRuleRefs() throws Exception {
    String action = "$lab.start; $c.text;";
    String expected = "(_localctx.lab!=null?(_localctx.lab.start):null); " +
      "(_localctx.c!=null?_input.getText(_localctx.c.start,_localctx.c.stop):null);";
    testActions(attributeTemplate, "inline", action, expected);
  }

  /**
   * Added in response to <a href="https://github.com/antlr/antlr4/issues/1211">...</a>
   */
  @Test
  public void testUnknownAttr() throws Exception {
    String action = "$qqq.text";
    String expected = ""; // was causing an exception
    testActions(attributeTemplate, "inline", action, expected);
  }

  /**
   * Regression test for issue #1295
   * $e.v yields incorrect value 0 in "e returns [int v] : '1' {$v = 1;} | '(' e ')' {$v = $e.v;} ;"
   * <a href="https://github.com/antlr/antlr4/issues/1295">...</a>
   */
  @Test
  public void testRuleRefsRecursive() throws Exception {
    String recursiveTemplate =
      """
        recursiveTemplate(inline) ::= <<
        parser grammar A;
        e returns [int v]
            :   INT {$v = $INT.int;}
            |   '(' e ')' {
        		 #inline#<inline>#end-inline#
        		 }
            ;
        >>""";
    String leftRecursiveTemplate =
      """
        recursiveTemplate(inline) ::= <<
        parser grammar A;
        e returns [int v]
            :   a=e op=('*'|'/') b=e  {$v = eval($a.v, $op.type, $b.v);}
            |   INT {$v = $INT.int;}
            |   '(' e ')' {
        		 #inline#<inline>#end-inline#
        		 }
            ;
        >>""";
    // ref to value returned from recursive call to rule
    String action = "$v = $e.v;";
    String expected = "_localctx.v =  _localctx.e.v;";
    testActions(recursiveTemplate, "inline", action, expected);
    testActions(leftRecursiveTemplate, "inline", action, expected);
    // ref to predefined attribute obtained from recursive call to rule
    action = "$v = $e.text.length();";
    expected = "_localctx.v =  (_localctx.e!=null?_input.getText(_localctx.e.start,_localctx.e.stop):null).length();";
    testActions(recursiveTemplate, "inline", action, expected);
    testActions(leftRecursiveTemplate, "inline", action, expected);
  }

  @Test
  public void testRefToTextAttributeForCurrentRule() throws Exception {
    String action = "$ctx.text; $text";

    // this is the expected translation for all cases
    String expected = "_localctx.text; _input.getText(_localctx.start, _input.LT(-1))";

    testActions(attributeTemplate, "init", action, expected);
    testActions(attributeTemplate, "inline", action, expected);
    testActions(attributeTemplate, "finally", action, expected);
  }

  @Test
  public void testEmptyActions() throws Exception {
    String gS =
      """
        grammar A;
        a[] : 'a' ;
        c : a[] c[] ;
        """;
    Grammar g = new Grammar(gS);
    assertThat(g).isNotNull();
  }
}
