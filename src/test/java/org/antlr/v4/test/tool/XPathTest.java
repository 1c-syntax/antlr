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

import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.RuleContext;
import org.antlr.v4.runtime.misc.Tuple2;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.TerminalNode;
import org.antlr.v4.runtime.tree.xpath.XPath;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.antlr.v4.TestUtils.assertEquals;
import static org.assertj.core.api.Assertions.assertThat;

@Disabled("Переделать на ANTLR runtime/Generator")
public class XPathTest extends AbstractBaseTest {
  public static final String grammar = """
    grammar Expr;
    prog:   func+ ;
    func:  'def' ID '(' arg (',' arg)* ')' body ;
    body:  '{' stat+ '}' ;
    arg :  ID ;
    stat:   expr ';'                 # printExpr
        |   ID '=' expr ';'          # assign
        |   'return' expr ';'        # ret
        |   ';'                      # blank
        ;
    expr:   expr ('*'|'/') expr      # MulDiv
        |   expr ('+'|'-') expr      # AddSub
        |   primary                  # prim
        ;
    primary\
        :   INT                      # int
        |   ID                       # id
        |   '(' expr ')'             # parens
    	 ;\
    
    MUL :   '*' ; // assigns token name to '*' used above in grammar
    DIV :   '/' ;
    ADD :   '+' ;
    SUB :   '-' ;
    RETURN : 'return' ;
    ID  :   [a-zA-Z]+ ;      // match identifiers
    INT :   [0-9]+ ;         // match integers
    NEWLINE:'\\r'? '\\n' -> skip;     // return newlines to parser (is end-statement signal)
    WS  :   [ \\t]+ -> skip ; // toss out whitespace
    """;
  public static final String SAMPLE_PROGRAM = """
    def f(x,y) { x = 3+4; y; ; }
    def g(x) { return 1+2*x; }
    """;

  @Test
  public void testValidPaths() throws Exception {
    boolean ok = rawGenerateAndBuildRecognizer("Expr.g4", grammar, "ExprParser", "ExprLexer", false);
    assertThat(ok).isTrue();

    String[] xpath = {"/prog/func",    // all funcs under prog at root
      "/prog/*",      // all children of prog at root
      "/*/func",      // all func kids of any root node
      "prog",        // prog must be root node
      "/prog",      // prog must be root node
      "/*",        // any root
      "*",        // any root
      "//ID",        // any ID in tree
      "//expr/primary/ID",// any ID child of a primary under any expr
      "//body//ID",    // any ID under a body
      "//'return'",    // any 'return' literal in tree, matched by literal name
      "//RETURN",      // any 'return' literal in tree, matched by symbolic name
      "//primary/*",    // all kids of any primary
      "//func/*/stat",  // all stat nodes grandkids of any func node
      "/prog/func/'def'",  // all def literal kids of func kid of prog
      "//stat/';'",    // all ';' under any stat node
      "//expr/primary/!ID",  // anything but ID under primary under any expr node
      "//expr/!primary",  // anything but primary under any expr node
      "//!*",        // nothing anywhere
      "/!*",        // nothing at root
      "//expr//ID",    // any ID under any expression (tests antlr/antlr4#370)
    };
    String[] expected = {"[func, func]", "[func, func]", "[func, func]", "[prog]", "[prog]", "[prog]", "[prog]", "[f, x, y, x, y, g, x, x]", "[y, x]", "[x, y, x]", "[return]", "[return]", "[3, 4, y, 1, 2, x]", "[stat, stat, stat, stat]", "[def, def]", "[;, ;, ;, ;]", "[3, 4, 1, 2]", "[expr, expr, expr, expr, expr, expr]", "[]", "[]", "[y, x]",};

    for (int i = 0; i < xpath.length; i++) {
      List<String> nodes = getNodeStrings(SAMPLE_PROGRAM, xpath[i], "prog", "ExprParser", "ExprLexer");
      String result = nodes.toString();
      assertEquals("path " + xpath[i] + " failed", expected[i], result);
    }
  }

  @Test
  public void testWeirdChar() throws Exception {
    boolean ok = rawGenerateAndBuildRecognizer("Expr.g4", grammar, "ExprParser", "ExprLexer", false);
    assertThat(ok).isTrue();

    String path = "&";
    String expected = "Invalid tokens or characters at index 0 in path '&'";

    testError(SAMPLE_PROGRAM, path, expected, "prog", "ExprParser", "ExprLexer");
  }

  @Test
  public void testWeirdChar2() throws Exception {
    boolean ok = rawGenerateAndBuildRecognizer("Expr.g4", grammar, "ExprParser", "ExprLexer", false);
    assertThat(ok).isTrue();

    String path = "//w&e/";
    String expected = "Invalid tokens or characters at index 3 in path '//w&e/'";

    testError(SAMPLE_PROGRAM, path, expected, "prog", "ExprParser", "ExprLexer");
  }

  @Test
  public void testBadSyntax() throws Exception {
    boolean ok = rawGenerateAndBuildRecognizer("Expr.g4", grammar, "ExprParser", "ExprLexer", false);
    assertThat(ok).isTrue();

    String path = "///";
    String expected = "/ at index 2 isn't a valid rule name";

    testError(SAMPLE_PROGRAM, path, expected, "prog", "ExprParser", "ExprLexer");
  }

  @Test
  public void testMissingWordAtEnd() throws Exception {
    boolean ok = rawGenerateAndBuildRecognizer("Expr.g4", grammar, "ExprParser", "ExprLexer", false);
    assertThat(ok).isTrue();

    String path = "//";
    String expected = "Missing path element at end of path";

    testError(SAMPLE_PROGRAM, path, expected, "prog", "ExprParser", "ExprLexer");
  }

  @Test
  public void testBadTokenName() throws Exception {
    boolean ok = rawGenerateAndBuildRecognizer("Expr.g4", grammar, "ExprParser", "ExprLexer", false);
    assertThat(ok).isTrue();

    String path = "//Ick";
    String expected = "Ick at index 2 isn't a valid token name";

    testError(SAMPLE_PROGRAM, path, expected, "prog", "ExprParser", "ExprLexer");
  }

  @Test
  public void testBadRuleName() throws Exception {
    boolean ok = rawGenerateAndBuildRecognizer("Expr.g4", grammar, "ExprParser", "ExprLexer", false);
    assertThat(ok).isTrue();

    String path = "/prog/ick";
    String expected = "ick at index 6 isn't a valid rule name";

    testError(SAMPLE_PROGRAM, path, expected, "prog", "ExprParser", "ExprLexer");
  }

  protected void testError(String input, String path, String expected, String startRuleName, String parserName, String lexerName) throws Exception {
    Tuple2<Parser, Lexer> pl = getParserAndLexer(input, parserName, lexerName);
    Parser parser = pl.getItem1();
    ParseTree tree = execStartRule(startRuleName, parser);

    IllegalArgumentException e = null;
    try {
      XPath.findAll(tree, path, parser);
    } catch (IllegalArgumentException iae) {
      e = iae;
    }
    assertThat(e).isNotNull();
    assertEquals(expected, e.getMessage());
  }

  public List<String> getNodeStrings(String input, String xpath, String startRuleName, String parserName, String lexerName) throws Exception {
    Tuple2<Parser, Lexer> pl = getParserAndLexer(input, parserName, lexerName);
    Parser parser = pl.getItem1();
    ParseTree tree = execStartRule(startRuleName, parser);

    List<String> nodes = new ArrayList<>();
    for (ParseTree t : XPath.findAll(tree, xpath, parser)) {
      if (t instanceof RuleContext r) {
        nodes.add(parser.getRuleNames()[r.getRuleIndex()]);
      } else {
        TerminalNode token = (TerminalNode) t;
        nodes.add(token.getText());
      }
    }
    return nodes;
  }
}
