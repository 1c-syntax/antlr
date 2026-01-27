/*
 * This file is a part of ANTLR.
 *
 * Copyright (c) 2012-2025 The ANTLR Project. All rights reserved.
 * Copyright (c) 2025-2026 Valery Maximov <maximovvalery@gmail.com> and contributors
 *
 * Use of this file is governed by the BSD-3-Clause license that
 * can be found in the LICENSE.txt file in the project root.
 */
package org.antlr.v4.test.tool;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.InputMismatchException;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.NoViableAltException;
import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.pattern.ParseTreeMatch;
import org.antlr.v4.runtime.tree.pattern.ParseTreePattern;
import org.antlr.v4.runtime.tree.pattern.ParseTreePatternMatcher;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class ParseTreeMatcherTest extends AbstractBaseTest {

  @Test
  void testChunking() {
    ParseTreePatternMatcher m = new ParseTreePatternMatcher(null, null);
    assertThat(m.split("<ID> = <expr> ;").toString()).isEqualTo("[ID, ' = ', expr, ' ;']");
    assertThat(m.split(" <ID> = <expr>").toString()).isEqualTo("[' ', ID, ' = ', expr]");
    assertThat(m.split("<ID> = <expr>").toString()).isEqualTo("[ID, ' = ', expr]");
    assertThat(m.split("<expr>").toString()).isEqualTo("[expr]");
    assertThat(m.split("\\<x\\> foo").toString()).isEqualTo("['<x> foo']");
    assertThat(m.split("foo \\<x\\> bar <tag>").toString()).isEqualTo("['foo <x> bar ', tag]");
  }

  @Test
  void testDelimiters() {
    ParseTreePatternMatcher m = new ParseTreePatternMatcher(null, null);
    m.setDelimiters("<<", ">>", "$");
    String result = m.split("<<ID>> = <<expr>> ;$<< ick $>>").toString();
    assertThat(result).isEqualTo("[ID, ' = ', expr, ' ;<< ick >>']");
  }

  @Test
  void testInvertedTags() {
    ParseTreePatternMatcher m = new ParseTreePatternMatcher(null, null);
    String result = null;
    try {
      m.split(">expr<");
    } catch (IllegalArgumentException iae) {
      result = iae.getMessage();
    }
    String expected = "tag delimiters out of order in pattern: >expr<";
    assertThat(result).isEqualTo(expected);
  }

  @Test
  void testUnclosedTag() {
    ParseTreePatternMatcher m = new ParseTreePatternMatcher(null, null);
    String result = null;
    try {
      m.split("<expr hi mom");
    } catch (IllegalArgumentException iae) {
      result = iae.getMessage();
    }
    String expected = "unterminated tag in pattern: <expr hi mom";
    assertThat(result).isEqualTo(expected);
  }

  @Test
  void testExtraClose() {
    ParseTreePatternMatcher m = new ParseTreePatternMatcher(null, null);
    String result = null;
    try {
      m.split("<expr> >");
    } catch (IllegalArgumentException iae) {
      result = iae.getMessage();
    }
    String expected = "missing start tag in pattern: <expr> >";
    assertThat(result).isEqualTo(expected);
  }

  @Test
  void testTokenizingPattern() throws Exception {
    String grammar =
      """
        grammar X1;
        s : ID '=' expr ';' ;
        expr : ID | INT ;
        ID : [a-z]+ ;
        INT : [0-9]+ ;
        WS : [ \\r\\n\\t]+ -> skip ;
        """;
    boolean ok =
      rawGenerateAndBuildRecognizer("X1.g4", grammar, "X1Parser", "X1Lexer", false);
    assertThat(ok).isTrue();

    ParseTreePatternMatcher m = getPatternMatcher("X1");

    List<? extends Token> tokens = m.tokenize("<ID> = <expr> ;");
    String results = tokens.toString();
    String expected = "[ID:3, [@-1,1:1='=',<1>,1:1], expr:7, [@-1,1:1=';',<2>,1:1]]";
    assertThat(results).isEqualTo(expected);
  }

  @Test
  void testCompilingPattern() throws Exception {
    String grammar =
      """
        grammar X2;
        s : ID '=' expr ';' ;
        expr : ID | INT ;
        ID : [a-z]+ ;
        INT : [0-9]+ ;
        WS : [ \\r\\n\\t]+ -> skip ;
        """;
    boolean ok =
      rawGenerateAndBuildRecognizer("X2.g4", grammar, "X2Parser", "X2Lexer", false);
    assertThat(ok).isTrue();

    ParseTreePatternMatcher m = getPatternMatcher("X2");

    ParseTreePattern t = m.compile("<ID> = <expr> ;", m.getParser().getRuleIndex("s"));
    String results = t.getPatternTree().toStringTree(m.getParser());
    String expected = "(s <ID> = (expr <expr>) ;)";
    assertThat(results).isEqualTo(expected);
  }

  @Test
  void testCompilingPatternConsumesAllTokens() throws Exception {
    String grammar =
      """
        grammar X2;
        s : ID '=' expr ';' ;
        expr : ID | INT ;
        ID : [a-z]+ ;
        INT : [0-9]+ ;
        WS : [ \\r\\n\\t]+ -> skip ;
        """;
    boolean ok =
      rawGenerateAndBuildRecognizer("X2.g4", grammar, "X2Parser", "X2Lexer", false);
    assertThat(ok).isTrue();

    ParseTreePatternMatcher m = getPatternMatcher("X2");

    boolean failed = false;
    try {
      m.compile("<ID> = <expr> ; extra", m.getParser().getRuleIndex("s"));
    } catch (ParseTreePatternMatcher.StartRuleDoesNotConsumeFullPattern e) {
      failed = true;
    }
    assertThat(failed).isTrue();
  }

  @Test
  void testPatternMatchesStartRule() throws Exception {
    String grammar =
      """
        grammar X2;
        s : ID '=' expr ';' ;
        expr : ID | INT ;
        ID : [a-z]+ ;
        INT : [0-9]+ ;
        WS : [ \\r\\n\\t]+ -> skip ;
        """;
    boolean ok =
      rawGenerateAndBuildRecognizer("X2.g4", grammar, "X2Parser", "X2Lexer", false);
    assertThat(ok).isTrue();

    ParseTreePatternMatcher m = getPatternMatcher("X2");

    boolean failed = false;
    try {
      m.compile("<ID> ;", m.getParser().getRuleIndex("s"));
    } catch (InputMismatchException e) {
      failed = true;
    }
    assertThat(failed).isTrue();
  }

  @Test
  void testPatternMatchesStartRule2() throws Exception {
    String grammar =
      """
        grammar X2;
        s : ID '=' expr ';' | expr ';' ;
        expr : ID | INT ;
        ID : [a-z]+ ;
        INT : [0-9]+ ;
        WS : [ \\r\\n\\t]+ -> skip ;
        """;
    boolean ok =
      rawGenerateAndBuildRecognizer("X2.g4", grammar, "X2Parser", "X2Lexer", false);
    assertThat(ok).isTrue();

    ParseTreePatternMatcher m = getPatternMatcher("X2");

    boolean failed = false;
    try {
      m.compile("<ID> <ID> ;", m.getParser().getRuleIndex("s"));
    } catch (NoViableAltException e) {
      failed = true;
    }
    assertThat(failed).isTrue();
  }

  @Test
  void testHiddenTokensNotSeenByTreePatternParser() throws Exception {
    String grammar =
      """
        grammar X2;
        s : ID '=' expr ';' ;
        expr : ID | INT ;
        ID : [a-z]+ ;
        INT : [0-9]+ ;
        WS : [ \\r\\n\\t]+ -> channel(HIDDEN) ;
        """;
    boolean ok =
      rawGenerateAndBuildRecognizer("X2.g4", grammar, "X2Parser", "X2Lexer", false);
    assertThat(ok).isTrue();

    ParseTreePatternMatcher m = getPatternMatcher("X2");

    ParseTreePattern t = m.compile("<ID> = <expr> ;", m.getParser().getRuleIndex("s"));
    String results = t.getPatternTree().toStringTree(m.getParser());
    String expected = "(s <ID> = (expr <expr>) ;)";
    assertThat(results).isEqualTo(expected);
  }

  @Test
  void testCompilingMultipleTokens() throws Exception {
    String grammar =
      """
        grammar X2;
        s : ID '=' ID ';' ;
        ID : [a-z]+ ;
        WS : [ \\r\\n\\t]+ -> skip ;
        """;
    boolean ok =
      rawGenerateAndBuildRecognizer("X2.g4", grammar, "X2Parser", "X2Lexer", false);
    assertThat(ok).isTrue();

    ParseTreePatternMatcher m = getPatternMatcher("X2");

    ParseTreePattern t = m.compile("<ID> = <ID> ;", m.getParser().getRuleIndex("s"));
    String results = t.getPatternTree().toStringTree(m.getParser());
    String expected = "(s <ID> = <ID> ;)";
    assertThat(results).isEqualTo(expected);
  }

  @Test
  void testIDNodeMatches() throws Exception {
    String grammar =
      """
        grammar X3;
        s : ID ';' ;
        ID : [a-z]+ ;
        WS : [ \\r\\n\\t]+ -> skip ;
        """;

    String input = "x ;";
    String pattern = "<ID>;";
    checkPatternMatch(grammar, "s", input, pattern, "X3");
  }

  @Test
  void testIDNodeWithLabelMatches() throws Exception {
    String grammar =
      """
        grammar X8;
        s : ID ';' ;
        ID : [a-z]+ ;
        WS : [ \\r\\n\\t]+ -> skip ;
        """;

    String input = "x ;";
    String pattern = "<id:ID>;";
    ParseTreeMatch m = checkPatternMatch(grammar, "s", input, pattern, "X8");
    assertThat(m.getLabels().toString()).isEqualTo("{ID=[x], id=[x]}");
    assertThat(m.get("id")).isNotNull();
    assertThat(m.get("ID")).isNotNull();
    assertThat(m.get("id").getText()).isEqualTo("x");
    assertThat(m.get("ID").getText()).isEqualTo("x");
    assertThat(m.getAll("id").toString()).isEqualTo("[x]");
    assertThat(m.getAll("ID").toString()).isEqualTo("[x]");

    assertThat(m.get("undefined")).isNull();
    assertThat(m.getAll("undefined").toString()).isEqualTo("[]");
  }

  @Test
  void testLabelGetsLastIDNode() throws Exception {
    String grammar =
      """
        grammar X9;
        s : ID ID ';' ;
        ID : [a-z]+ ;
        WS : [ \\r\\n\\t]+ -> skip ;
        """;

    String input = "x y;";
    String pattern = "<id:ID> <id:ID>;";
    ParseTreeMatch m = checkPatternMatch(grammar, "s", input, pattern, "X9");
    assertThat(m.getLabels().toString()).isEqualTo("{ID=[x, y], id=[x, y]}");
    assertThat(m.get("id")).isNotNull();
    assertThat(m.get("ID")).isNotNull();
    assertThat(m.get("id").getText()).isEqualTo("y");
    assertThat(m.get("ID").getText()).isEqualTo("y");
    assertThat(m.getAll("id").toString()).isEqualTo("[x, y]");
    assertThat(m.getAll("ID").toString()).isEqualTo("[x, y]");

    assertThat(m.get("undefined")).isNull();
    assertThat(m.getAll("undefined").toString()).isEqualTo("[]");
  }

  @Test
  void testIDNodeWithMultipleLabelMatches() throws Exception {
    String grammar =
      """
        grammar X7;
        s : ID ID ID ';' ;
        ID : [a-z]+ ;
        WS : [ \\r\\n\\t]+ -> skip ;
        """;

    String input = "x y z;";
    String pattern = "<a:ID> <b:ID> <a:ID>;";
    ParseTreeMatch m = checkPatternMatch(grammar, "s", input, pattern, "X7");
    assertThat(m.getLabels().toString()).isEqualTo("{ID=[x, y, z], a=[x, z], b=[y]}");
    assertThat(m.get("a")).isNotNull(); // get first
    assertThat(m.get("b")).isNotNull();
    assertThat(m.get("ID")).isNotNull();
    assertThat(m.get("a").getText()).isEqualTo("z");
    assertThat(m.get("b").getText()).isEqualTo("y");
    assertThat(m.get("ID").getText()).isEqualTo("z"); // get last
    assertThat(m.getAll("a").toString()).isEqualTo("[x, z]");
    assertThat(m.getAll("b").toString()).isEqualTo("[y]");
    assertThat(m.getAll("ID").toString()).isEqualTo("[x, y, z]"); // ordered

    assertThat(m.getTree().getText()).isEqualTo("xyz;"); // whitespace stripped by lexer

    assertThat(m.get("undefined")).isNull();
    assertThat(m.getAll("undefined").toString()).isEqualTo("[]");
  }

  @Test
  void testTokenAndRuleMatch() throws Exception {
    String grammar =
      """
        grammar X4;
        s : ID '=' expr ';' ;
        expr : ID | INT ;
        ID : [a-z]+ ;
        INT : [0-9]+ ;
        WS : [ \\r\\n\\t]+ -> skip ;
        """;

    String input = "x = 99;";
    String pattern = "<ID> = <expr> ;";
    checkPatternMatch(grammar, "s", input, pattern, "X4");
  }

  @Test
  void testTokenTextMatch() throws Exception {
    String grammar =
      """
        grammar X4;
        s : ID '=' expr ';' ;
        expr : ID | INT ;
        ID : [a-z]+ ;
        INT : [0-9]+ ;
        WS : [ \\r\\n\\t]+ -> skip ;
        """;

    String input = "x = 0;";
    String pattern = "<ID> = 1;";
    boolean invertMatch = true; // 0!=1
    checkPatternMatch(grammar, "s", input, pattern, "X4", invertMatch);

    input = "x = 0;";
    pattern = "<ID> = 0;";
    invertMatch = false;
    checkPatternMatch(grammar, "s", input, pattern, "X4", invertMatch);

    input = "x = 0;";
    pattern = "x = 0;";
    invertMatch = false;
    checkPatternMatch(grammar, "s", input, pattern, "X4", invertMatch);

    input = "x = 0;";
    pattern = "y = 0;";
    invertMatch = true;
    checkPatternMatch(grammar, "s", input, pattern, "X4", invertMatch);
  }

  @Test
  void testAssign() throws Exception {
    String grammar =
      "grammar X5;\n" +
        "s   : expr ';'\n" +
        //"    | 'return' expr ';'\n" +
        "    ;\n" +
        "expr: expr '.' ID\n" +
        "    | expr '*' expr\n" +
        "    | expr '=' expr\n" +
        "    | ID\n" +
        "    | INT\n" +
        "    ;\n" +
        "ID : [a-z]+ ;\n" +
        "INT : [0-9]+ ;\n" +
        "WS : [ \\r\\n\\t]+ -> skip ;\n";

    String input = "x = 99;";
    String pattern = "<ID> = <expr>;";
    checkPatternMatch(grammar, "s", input, pattern, "X5");
  }

  @Test
  void testLRecursiveExpr() throws Exception {
    String grammar =
      """
        grammar X6;
        s   : expr ';'
            ;
        expr: expr '.' ID
            | expr '*' expr
            | expr '=' expr
            | ID
            | INT
            ;
        ID : [a-z]+ ;
        INT : [0-9]+ ;
        WS : [ \\r\\n\\t]+ -> skip ;
        """;

    String input = "3*4*5";
    String pattern = "<expr> * <expr> * <expr>";
    checkPatternMatch(grammar, "expr", input, pattern, "X6");
  }

  public ParseTreeMatch checkPatternMatch(String grammar, String startRule,
                                          String input, String pattern,
                                          String grammarName) throws Exception {
    return checkPatternMatch(grammar, startRule, input, pattern, grammarName, false);
  }

  public ParseTreeMatch checkPatternMatch(String grammar, String startRule,
                                          String input, String pattern,
                                          String grammarName, boolean invertMatch) throws Exception {
    String grammarFileName = grammarName + ".g4";
    String parserName = grammarName + "Parser";
    String lexerName = grammarName + "Lexer";
    boolean ok =
      rawGenerateAndBuildRecognizer(grammarFileName, grammar, parserName, lexerName, false);
    assertThat(ok).isTrue();

    ParseTree result = execParser(startRule, input, parserName, lexerName);

    ParseTreePattern p = getPattern(grammarName, pattern, startRule);
    ParseTreeMatch match = p.match(result);
    boolean matched = match.succeeded();
    if (invertMatch) {
      assertThat(matched).isFalse();
    } else {
      assertThat(matched).isTrue();
    }
    return match;
  }

  public ParseTreePattern getPattern(String grammarName, String pattern, String ruleName) throws Exception {
    Class<? extends Lexer> lexerClass = loadLexerClassFromTempDir(grammarName + "Lexer");
    Constructor<? extends Lexer> ctor = lexerClass.getConstructor(CharStream.class);
    Lexer lexer = ctor.newInstance((CharStream) null);

    Class<? extends Parser> parserClass = loadParserClassFromTempDir(grammarName + "Parser");
    Constructor<? extends Parser> pctor = parserClass.getConstructor(TokenStream.class);
    Parser parser = pctor.newInstance(new CommonTokenStream(lexer));

    return parser.compileParseTreePattern(pattern, parser.getRuleIndex(ruleName));
  }

  public ParseTreePatternMatcher getPatternMatcher(String grammarName) throws Exception {
    Class<? extends Lexer> lexerClass = loadLexerClassFromTempDir(grammarName + "Lexer");
    Constructor<? extends Lexer> ctor = lexerClass.getConstructor(CharStream.class);
    Lexer lexer = ctor.newInstance((CharStream) null);

    Class<? extends Parser> parserClass = loadParserClassFromTempDir(grammarName + "Parser");
    Constructor<? extends Parser> pctor = parserClass.getConstructor(TokenStream.class);
    Parser parser = pctor.newInstance(new CommonTokenStream(lexer));

    return new ParseTreePatternMatcher(lexer, parser);
  }
}
