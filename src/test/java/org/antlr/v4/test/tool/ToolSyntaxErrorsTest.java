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
import org.antlr.v4.tool.ErrorType;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ToolSyntaxErrorsTest extends AbstractBaseTest {
  static String[] A = {
    // INPUT
    "grammar A;\n",
    // YIELDS
    "error(" + ErrorType.NO_RULES.code + "): A.g4::: grammar 'A' has no rules\n",

    "lexer grammar A;\n", "error(" + ErrorType.NO_RULES.code + "): A.g4::: grammar 'A' has no rules\n",

    "A;", "error(" + ErrorType.SYNTAX_ERROR.code + "): A.g4:1:0: syntax error: 'A' came as a complete surprise to me\n",

    "grammar ;", "error(" + ErrorType.SYNTAX_ERROR.code + "): A.g4:1:8: syntax error: ';' came as a complete surprise to me while looking for an identifier\n",

    """
grammar A
a : ID ;
""", "error(" + ErrorType.SYNTAX_ERROR.code + "): A.g4:2:0: syntax error: missing SEMI at 'a'\n",

    """
grammar A;
a : ID ;;
b : B ;""", "error(" + ErrorType.SYNTAX_ERROR.code + "): A.g4:2:8: syntax error: ';' came as a complete surprise to me\n",

    """
grammar A;;
a : ID ;
""", "error(" + ErrorType.SYNTAX_ERROR.code + "): A;.g4:1:10: syntax error: ';' came as a complete surprise to me\n",

    """
grammar A;
a @init : ID ;
""", "error(" + ErrorType.SYNTAX_ERROR.code + "): A.g4:2:8: syntax error: mismatched input ':' expecting ACTION while matching rule preamble\n",

    """
grammar A;
a  ( A | B ) D ;
b : B ;""", "error(" + ErrorType.SYNTAX_ERROR.code + "): A.g4:2:3: syntax error: '(' came as a complete surprise to me while matching rule preamble\n" + "error(" + ErrorType.SYNTAX_ERROR.code + "): A.g4:2:11: syntax error: mismatched input ')' expecting SEMI while matching a rule\n" + "error(" + ErrorType.SYNTAX_ERROR.code + "): A.g4:2:15: syntax error: mismatched input ';' expecting COLON while matching a lexer rule\n",};

  @Test
  public void AllErrorCodesDistinct() {
    ErrorType[] errorTypes = ErrorType.class.getEnumConstants();
    for (int i = 0; i < errorTypes.length; i++) {
      for (int j = i + 1; j < errorTypes.length; j++) {
        assertThat(errorTypes[j].code).isNotEqualTo(errorTypes[i].code);
      }
    }
  }


  @Test
  public void testA() {
    super.testErrors(A, true);
  }


  @Test
  public void testExtraColon() {
    String[] pair = new String[]{"""
grammar A;
a : : A ;
b : B ;""", "error(" + ErrorType.SYNTAX_ERROR.code + "): A.g4:2:4: syntax error: ':' came as a complete surprise to me while matching alternative\n",};
    super.testErrors(pair, true);
  }


  @Test
  public void testMissingRuleSemi() {
    String[] pair = new String[]{"""
grammar A;
a : A\s
b : B ;""", "error(" + ErrorType.SYNTAX_ERROR.code + "): A.g4:3:0: syntax error: unterminated rule (missing ';') detected at 'b :' while looking for rule element\n",};
    super.testErrors(pair, true);
  }


  @Test
  public void testMissingRuleSemi2() {
    String[] pair = new String[]{"""
lexer grammar A;
A : 'a'\s
B : 'b' ;""", "error(" + ErrorType.SYNTAX_ERROR.code + "): A.g4:3:0: syntax error: unterminated rule (missing ';') detected at 'B :' while looking for lexer rule element\n",};
    super.testErrors(pair, true);
  }


  @Test
  public void testMissingRuleSemi3() {
    String[] pair = new String[]{"""
grammar A;
a : A\s
b[int i] returns [int y] : B ;""", "error(" + ErrorType.SYNTAX_ERROR.code + "): A.g4:3:9: syntax error: unterminated rule (missing ';') detected at 'returns int y' while looking for rule element\n"};
    super.testErrors(pair, true);
  }


  @Test
  public void testMissingRuleSemi4() {
    String[] pair = new String[]{"""
grammar A;
a : b\s
  catch [Exception e] {...}
b : B ;
""",

      "error(" + ErrorType.SYNTAX_ERROR.code + "): A.g4:2:4: syntax error: unterminated rule (missing ';') detected at 'b catch' while looking for rule element\n"};
    super.testErrors(pair, true);
  }


  @Test
  public void testMissingRuleSemi5() {
    String[] pair = new String[]{"""
grammar A;
a : A\s
  catch [Exception e] {...}
""",

      "error(" + ErrorType.SYNTAX_ERROR.code + "): A.g4:2:4: syntax error: unterminated rule (missing ';') detected at 'A catch' while looking for rule element\n"};
    super.testErrors(pair, true);
  }


  @Test
  public void testBadRulePrequelStart() {
    String[] pair = new String[]{"""
grammar A;
a @ options {k=1;} : A ;
b : B ;""",

      "error(" + ErrorType.SYNTAX_ERROR.code + "): A.g4:2:4: syntax error: 'options {' came as a complete surprise to me while looking for an identifier\n"};
    super.testErrors(pair, true);
  }


  @Test
  public void testBadRulePrequelStart2() {
    String[] pair = new String[]{"""
grammar A;
a } : A ;
b : B ;""",

      "error(" + ErrorType.SYNTAX_ERROR.code + "): A.g4:2:2: syntax error: '}' came as a complete surprise to me while matching rule preamble\n"};
    super.testErrors(pair, true);
  }


  @Test
  public void testModeInParser() {
    String[] pair = new String[]{"""
grammar A;
a : A ;
mode foo;
b : B ;""",

      "error(" + ErrorType.SYNTAX_ERROR.code + "): A.g4:4:0: syntax error: 'b' came as a complete surprise to me\n" + "error(" + ErrorType.SYNTAX_ERROR.code + "): A.g4:4:6: syntax error: mismatched input ';' expecting COLON while matching a lexer rule\n"};
    super.testErrors(pair, true);
  }

  /**
   * This is a regression test for antlr/antlr4#243
   * "Generate a good message for unterminated strings"
   * <a href="https://github.com/antlr/antlr4/issues/243">...</a>
   */

  @Test
  public void testUnterminatedStringLiteral() {
    String[] pair = new String[]{"""
grammar A;
a : 'x
  ;
""",

      "error(" + ErrorType.UNTERMINATED_STRING_LITERAL.code + "): A.g4:2:4: unterminated string literal\n"};
    super.testErrors(pair, true);
  }

  /**
   * This is a regression test for antlr/antlr4#262
   * "Parser Rule Name Starting With an Underscore"
   * <a href="https://github.com/antlr/antlr4/issues/262">...</a>
   */

  @Test
  public void testParserRuleNameStartingWithUnderscore() {
    String[] pair = new String[]{"""
grammar A;
_a : 'x' ;
""",

      "error(" + ErrorType.SYNTAX_ERROR.code + "): A.g4:2:0: syntax error: '_' came as a complete surprise to me\n"};
    super.testErrors(pair, true);
  }

  /**
   * This is a regression test for antlr/antlr4#194
   * "NullPointerException on 'options{}' in grammar file"
   * <a href="https://github.com/antlr/antlr4/issues/194">...</a>
   */

  @Test
  public void testEmptyGrammarOptions() {
    String[] pair = new String[]{"""
grammar A;
options {}
a : 'x' ;
""",

      ""};
    super.testErrors(pair, true);
  }

  /**
   * This is a "related" regression test for antlr/antlr4#194
   * "NullPointerException on 'options{}' in grammar file"
   * <a href="https://github.com/antlr/antlr4/issues/194">...</a>
   */

  @Test
  public void testEmptyRuleOptions() {
    String[] pair = new String[]{"""
grammar A;
a options{} : 'x' ;
""",

      ""};
    super.testErrors(pair, true);
  }

  /**
   * This is a "related" regression test for antlr/antlr4#194
   * "NullPointerException on 'options{}' in grammar file"
   * <a href="https://github.com/antlr/antlr4/issues/194">...</a>
   */

  @Test
  public void testEmptyBlockOptions() {
    String[] pair = new String[]{"""
grammar A;
a : (options{} : 'x') ;
""",

      ""};
    super.testErrors(pair, true);
  }


  @Test
  public void testEmptyTokensBlock() {
    String[] pair = new String[]{"""
grammar A;
tokens {}
a : 'x' ;
""",

      ""};
    super.testErrors(pair, true);
  }

  /**
   * This is a regression test for antlr/antlr4#190
   * "NullPointerException building lexer grammar using bogus 'token' action"
   * <a href="https://github.com/antlr/antlr4/issues/190">...</a>
   */

  @Test
  public void testInvalidLexerCommand() {
    // "meant" to use -> popMode
    String[] pair = new String[]{"""
grammar A;
tokens{Foo}
b : Foo ;
X : 'foo1' -> popmode;
Y : 'foo2' -> token(Foo);""", // "meant" to use -> type(Foo)

      "error(" + ErrorType.INVALID_LEXER_COMMAND.code + "): A.g4:4:14: lexer command 'popmode' does not exist or is not supported by the current target\n" + "error(" + ErrorType.INVALID_LEXER_COMMAND.code + "): A.g4:5:14: lexer command 'token' does not exist or is not supported by the current target\n"};
    super.testErrors(pair, true);
  }


  @Test
  public void testLexerCommandArgumentValidation() {
    // "meant" to use -> popMode
    String[] pair = new String[]{"""
grammar A;
tokens{Foo}
b : Foo ;
X : 'foo1' -> popMode(Foo);
Y : 'foo2' -> type;""", // "meant" to use -> type(Foo)

      "error(" + ErrorType.UNWANTED_LEXER_COMMAND_ARGUMENT.code + "): A.g4:4:14: lexer command 'popMode' does not take any arguments\n" + "error(" + ErrorType.MISSING_LEXER_COMMAND_ARGUMENT.code + "): A.g4:5:14: missing argument for lexer command 'type'\n"};
    super.testErrors(pair, true);
  }


  @Test
  public void testRuleRedefinition() {
    String[] pair = new String[]{"""
grammar Oops;

ret_ty : A ;
ret_ty : B ;

A : 'a' ;
B : 'b' ;
""",

      "error(" + ErrorType.RULE_REDEFINITION.code + "): Oops.g4:4:0: rule 'ret_ty' redefinition; previous at line 3\n"};
    super.testErrors(pair, true);
  }


  @Test
  public void testEpsilonClosureAnalysis() {
    String grammar = """
      grammar A;
      x : ;
      y1 : x+;
      y2 : x*;
      z1 : ('foo' | 'bar'? 'bar2'?)*;
      z2 : ('foo' | 'bar' 'bar2'? | 'bar2')*;
      """;
    String expected = "error(" + ErrorType.EPSILON_CLOSURE.code + "): A.g4:3:0: rule 'y1' contains a closure with at least one alternative that can match an empty string\n" + "error(" + ErrorType.EPSILON_CLOSURE.code + "): A.g4:4:0: rule 'y2' contains a closure with at least one alternative that can match an empty string\n" + "error(" + ErrorType.EPSILON_CLOSURE.code + "): A.g4:5:0: rule 'z1' contains a closure with at least one alternative that can match an empty string\n";

    String[] pair = new String[]{grammar, expected};

    super.testErrors(pair, true);
  }

  // Test for https://github.com/antlr/antlr4/issues/1203

  @Test
  public void testEpsilonNestedClosureAnalysis() {
    String grammar = """
      grammar T;
      s : (a a)* ;
      a : 'foo'* ;
      """;
    String expected = "error(" + ErrorType.EPSILON_CLOSURE.code + "): T.g4:2:0: rule 's' contains a closure with at least one alternative that can match an empty string\n";

    String[] pair = new String[]{grammar, expected};

    super.testErrors(pair, true);
  }

  // Test for https://github.com/antlr/antlr4/issues/1203

  @Test
  public void testEpsilonOptionalAndClosureAnalysis() {
    String grammar = """
      grammar T;
      s : (a a)? ;
      a : 'foo'* ;
      """;
    String expected = "warning(" + ErrorType.EPSILON_OPTIONAL.code + "): T.g4:2:0: rule 's' contains an optional block with at least one alternative that can match an empty string\n";

    String[] pair = new String[]{grammar, expected};

    super.testErrors(pair, true);
  }


  @Test
  public void testEpsilonOptionalAnalysis() {
    String grammar = """
      grammar A;
      x : ;
      y  : x?;
      z1 : ('foo' | 'bar'? 'bar2'?)?;
      z2 : ('foo' | 'bar' 'bar2'? | 'bar2')?;
      """;
    String expected = "warning(" + ErrorType.EPSILON_OPTIONAL.code + "): A.g4:3:0: rule 'y' contains an optional block with at least one alternative that can match an empty string\n" + "warning(" + ErrorType.EPSILON_OPTIONAL.code + "): A.g4:4:0: rule 'z1' contains an optional block with at least one alternative that can match an empty string\n";

    String[] pair = new String[]{grammar, expected};

    super.testErrors(pair, true);
  }

  /**
   * This is a regression test for antlr/antlr4#315
   * "Inconsistent lexer error msg for actions"
   * <a href="https://github.com/antlr/antlr4/issues/315">...</a>
   */

  @Test
  public void testActionAtEndOfOneLexerAlternative() {
    String grammar = """
      grammar A;
      stat : 'start' CharacterLiteral 'end' EOF;
      
      // Lexer
      
      CharacterLiteral
          :   '\\'' SingleCharacter '\\''
          |   '\\'' ~[\\r\\n] {notifyErrorListeners("unclosed character literal");}
          ;
      
      fragment
      SingleCharacter
          :   ~['\\\\\\r\\n]
          ;
      
      WS   : [ \\r\\t\\n]+ -> skip ;
      """;
    String expected = "";

    String[] pair = new String[]{grammar, expected};
    super.testErrors(pair, true);
  }

  /**
   * This is a regression test for antlr/antlr4#308 "NullPointer exception"
   * <a href="https://github.com/antlr/antlr4/issues/308">...</a>
   */

  @Test
  public void testDoubleQuotedStringLiteral() {
    String grammar = """
      lexer grammar A;
      WHITESPACE : (" " | "\\t" | "\\n" | "\\r" | "\\f");
      """;
    String expected = "error(" + ErrorType.SYNTAX_ERROR.code + "): A.g4:2:14: syntax error: '\"' came as a complete surprise to me\n" + "error(" + ErrorType.SYNTAX_ERROR.code + "): A.g4:2:16: syntax error: '\"' came as a complete surprise to me\n" + "error(" + ErrorType.SYNTAX_ERROR.code + "): A.g4:2:20: syntax error: '\"' came as a complete surprise to me\n" + "error(" + ErrorType.SYNTAX_ERROR.code + "): A.g4:2:21: syntax error: '\\' came as a complete surprise to me\n" + "error(" + ErrorType.SYNTAX_ERROR.code + "): A.g4:2:23: syntax error: '\"' came as a complete surprise to me\n" + "error(" + ErrorType.SYNTAX_ERROR.code + "): A.g4:2:27: syntax error: '\"' came as a complete surprise to me\n" + "error(" + ErrorType.SYNTAX_ERROR.code + "): A.g4:2:28: syntax error: '\\' came as a complete surprise to me\n" + "error(" + ErrorType.SYNTAX_ERROR.code + "): A.g4:2:30: syntax error: '\"' came as a complete surprise to me\n" + "error(" + ErrorType.SYNTAX_ERROR.code + "): A.g4:2:34: syntax error: '\"' came as a complete surprise to me\n" + "error(" + ErrorType.SYNTAX_ERROR.code + "): A.g4:2:35: syntax error: '\\' came as a complete surprise to me\n" + "error(" + ErrorType.SYNTAX_ERROR.code + "): A.g4:2:37: syntax error: '\"' came as a complete surprise to me\n" + "error(" + ErrorType.SYNTAX_ERROR.code + "): A.g4:2:41: syntax error: '\"' came as a complete surprise to me\n" + "error(" + ErrorType.SYNTAX_ERROR.code + "): A.g4:2:42: syntax error: '\\' came as a complete surprise to me\n" + "error(" + ErrorType.SYNTAX_ERROR.code + "): A.g4:2:44: syntax error: '\"' came as a complete surprise to me\n";

    String[] pair = new String[]{grammar, expected};

    super.testErrors(pair, true);
  }

  /**
   * This is a regression test for <a href="https://github.com/antlr/antlr4/issues/1815">...</a>
   * "Null ptr exception in SqlBase.g4"
   */

  @Test
  public void testDoubleQuoteInTwoStringLiterals() {
    String grammar = """
      lexer grammar A;
      STRING : '\\"' '\\"' 'x' ;""";
    String expected = "warning(" + ErrorType.INVALID_ESCAPE_SEQUENCE.code + "): A.g4:2:10: invalid escape sequence '\\\"'\n" + "warning(" + ErrorType.INVALID_ESCAPE_SEQUENCE.code + "): A.g4:2:15: invalid escape sequence '\\\"'\n";

    String[] pair = new String[]{grammar, expected};

    super.testErrors(pair, true);
  }

  /**
   * This test ensures that the {@link ErrorType#INVALID_ESCAPE_SEQUENCE}
   * error is not reported for escape sequences that are known to be valid.
   */

  @Test
  public void testValidEscapeSequences() {
    String grammar = """
      lexer grammar A;
      NORMAL_ESCAPE : '\\b \\t \\n \\f \\r \\' \\\\';
      UNICODE_ESCAPE : '\\u0001 \\u00A1 \\u00a1 \\uaaaa \\uAAAA';
      """;
    String expected = "";

    String[] pair = new String[]{grammar, expected};

    super.testErrors(pair, true);
  }

  /**
   * This is a regression test for antlr/antlr4#507 "NullPointerException When
   * Generating Code from Grammar".
   * <a href="https://github.com/antlr/antlr4/issues/507">...</a>
   */

  @Test
  public void testInvalidEscapeSequences() {
    String grammar = """
      lexer grammar A;
      RULE : 'Foo \\uAABG \\x \\u';
      """;
    String expected = "warning(" + ErrorType.INVALID_ESCAPE_SEQUENCE.code + "): A.g4:2:12: invalid escape sequence '\\uAABG'\n" + "warning(" + ErrorType.INVALID_ESCAPE_SEQUENCE.code + "): A.g4:2:19: invalid escape sequence '\\x'\n" + "warning(" + ErrorType.INVALID_ESCAPE_SEQUENCE.code + "): A.g4:2:22: invalid escape sequence '\\u'\n" + "warning(" + ErrorType.EPSILON_TOKEN.code + "): A.g4:2:0: non-fragment lexer rule 'RULE' can match the empty string\n";

    String[] pair = new String[]{grammar, expected};

    super.testErrors(pair, true);
  }

  /**
   * This is a regression test for antlr/antlr4#959 "NullPointerException".
   * <a href="https://github.com/antlr/antlr4/issues/959">...</a>
   */

  @Test
  public void testNotAllowedEmptyStrings() {
    String grammar = """
      lexer grammar T;
      Error0: '''test''';
      Error1: '' 'test';
      Error2: 'test' '';
      Error3: '';
      NotError: ' ';""";
    String expected = "error(" + ErrorType.EMPTY_STRINGS_AND_SETS_NOT_ALLOWED.code + "): T.g4:2:8: string literals and sets cannot be empty: ''\n" + "error(" + ErrorType.EMPTY_STRINGS_AND_SETS_NOT_ALLOWED.code + "): T.g4:2:16: string literals and sets cannot be empty: ''\n" + "error(" + ErrorType.EMPTY_STRINGS_AND_SETS_NOT_ALLOWED.code + "): T.g4:3:8: string literals and sets cannot be empty: ''\n" + "error(" + ErrorType.EMPTY_STRINGS_AND_SETS_NOT_ALLOWED.code + "): T.g4:4:15: string literals and sets cannot be empty: ''\n" + "error(" + ErrorType.EMPTY_STRINGS_AND_SETS_NOT_ALLOWED.code + "): T.g4:5:8: string literals and sets cannot be empty: ''\n";

    String[] pair = new String[]{grammar, expected};

    super.testErrors(pair, true);
  }


  @Test
  public void testInvalidCharSetsAndStringLiterals() {
    //https://github.com/antlr/antlr4/issues/1077
    String grammar = """
      lexer grammar Test;
      INVALID_STRING_LITERAL:       '\\"' | '\\]' | '\\u24';
      INVALID_STRING_LITERAL_RANGE: 'GH'..'LM';
      INVALID_CHAR_SET:             [\\u24\\uA2][\\{];
      EMPTY_STRING_LITERAL_RANGE:   'F'..'A' | 'Z';
      EMPTY_CHAR_SET:               [f-az][];
      START_HYPHEN_IN_CHAR_SET:     [-z];
      END_HYPHEN_IN_CHAR_SET:       [a-];
      SINGLE_HYPHEN_IN_CHAR_SET:    [-];
      VALID_STRING_LITERALS:        '\\u1234' | '\\t' | '\\'';
      VALID_CHAR_SET:               [`\\-=\\]];""";

    String expected = "warning(" + ErrorType.INVALID_ESCAPE_SEQUENCE.code + "): Test.g4:2:31: invalid escape sequence '\\\"'\n" + "warning(" + ErrorType.INVALID_ESCAPE_SEQUENCE.code + "): Test.g4:2:38: invalid escape sequence '\\]'\n" + "warning(" + ErrorType.INVALID_ESCAPE_SEQUENCE.code + "): Test.g4:2:45: invalid escape sequence '\\u24'\n" + "error(" + ErrorType.INVALID_LITERAL_IN_LEXER_SET.code + "): Test.g4:3:30: multi-character literals are not allowed in lexer sets: 'GH'\n" + "error(" + ErrorType.INVALID_LITERAL_IN_LEXER_SET.code + "): Test.g4:3:36: multi-character literals are not allowed in lexer sets: 'LM'\n" + "warning(" + ErrorType.INVALID_ESCAPE_SEQUENCE.code + "): Test.g4:4:30: invalid escape sequence '\\u24\\u'\n" + "warning(" + ErrorType.INVALID_ESCAPE_SEQUENCE.code + "): Test.g4:4:40: invalid escape sequence '\\{'\n" + "error(" + ErrorType.EMPTY_STRINGS_AND_SETS_NOT_ALLOWED.code + "): Test.g4:5:33: string literals and sets cannot be empty: 'F'..'A'\n" + "error(" + ErrorType.EMPTY_STRINGS_AND_SETS_NOT_ALLOWED.code + "): Test.g4:6:30: string literals and sets cannot be empty: 'f'..'a'\n" + "error(" + ErrorType.EMPTY_STRINGS_AND_SETS_NOT_ALLOWED.code + "): Test.g4:6:36: string literals and sets cannot be empty: []\n" + "warning(" + ErrorType.EPSILON_TOKEN.code + "): Test.g4:2:0: non-fragment lexer rule 'INVALID_STRING_LITERAL' can match the empty string\n";

    String[] pair = new String[]{grammar, expected};

    super.testErrors(pair, true);
  }


  @Test
  public void testInvalidUnicodeEscapesInCharSet() {
    String grammar = """
      lexer grammar Test;
      INVALID_EXTENDED_UNICODE_EMPTY: [\\u{}];
      INVALID_EXTENDED_UNICODE_NOT_TERMINATED: [\\u{];
      INVALID_EXTENDED_UNICODE_TOO_LONG: [\\u{110000}];
      INVALID_UNICODE_PROPERTY_EMPTY: [\\p{}];
      INVALID_UNICODE_PROPERTY_NOT_TERMINATED: [\\p{];
      INVALID_INVERTED_UNICODE_PROPERTY_EMPTY: [\\P{}];
      INVALID_UNICODE_PROPERTY_UNKNOWN: [\\p{NotAProperty}];
      INVALID_INVERTED_UNICODE_PROPERTY_UNKNOWN: [\\P{NotAProperty}];
      UNICODE_PROPERTY_NOT_ALLOWED_IN_RANGE: [\\p{Uppercase_Letter}-\\p{Lowercase_Letter}];
      UNICODE_PROPERTY_NOT_ALLOWED_IN_RANGE_2: [\\p{Letter}-Z];
      UNICODE_PROPERTY_NOT_ALLOWED_IN_RANGE_3: [A-\\p{Number}];
      INVERTED_UNICODE_PROPERTY_NOT_ALLOWED_IN_RANGE: [\\P{Uppercase_Letter}-\\P{Number}];
      """;

    String expected = "warning(" + ErrorType.INVALID_ESCAPE_SEQUENCE.code + "): Test.g4:2:32: invalid escape sequence '\\u{}'\n" + "warning(" + ErrorType.INVALID_ESCAPE_SEQUENCE.code + "): Test.g4:3:41: invalid escape sequence '\\u{'\n" + "warning(" + ErrorType.INVALID_ESCAPE_SEQUENCE.code + "): Test.g4:4:35: invalid escape sequence '\\u{110'\n" + "warning(" + ErrorType.INVALID_ESCAPE_SEQUENCE.code + "): Test.g4:5:32: invalid escape sequence '\\p{}'\n" + "warning(" + ErrorType.INVALID_ESCAPE_SEQUENCE.code + "): Test.g4:6:41: invalid escape sequence '\\p{'\n" + "warning(" + ErrorType.INVALID_ESCAPE_SEQUENCE.code + "): Test.g4:7:41: invalid escape sequence '\\P{}'\n" + "warning(" + ErrorType.INVALID_ESCAPE_SEQUENCE.code + "): Test.g4:8:34: invalid escape sequence '\\p{NotAProperty}'\n" + "warning(" + ErrorType.INVALID_ESCAPE_SEQUENCE.code + "): Test.g4:9:43: invalid escape sequence '\\P{NotAProperty}'\n" + "error(" + ErrorType.UNICODE_PROPERTY_NOT_ALLOWED_IN_RANGE.code + "): Test.g4:10:39: unicode property escapes not allowed in lexer charset range: [\\p{Uppercase_Letter}-\\p{Lowercase_Letter}]\n" + "error(" + ErrorType.UNICODE_PROPERTY_NOT_ALLOWED_IN_RANGE.code + "): Test.g4:11:41: unicode property escapes not allowed in lexer charset range: [\\p{Letter}-Z]\n" + "error(" + ErrorType.UNICODE_PROPERTY_NOT_ALLOWED_IN_RANGE.code + "): Test.g4:12:41: unicode property escapes not allowed in lexer charset range: [A-\\p{Number}]\n" + "error(" + ErrorType.UNICODE_PROPERTY_NOT_ALLOWED_IN_RANGE.code + "): Test.g4:13:48: unicode property escapes not allowed in lexer charset range: [\\P{Uppercase_Letter}-\\P{Number}]\n";

    String[] pair = new String[]{grammar, expected};

    super.testErrors(pair, true);
  }

  /**
   * This test ensures the {@link ErrorType#UNRECOGNIZED_ASSOC_OPTION} warning
   * is produced as described in the documentation.
   */

  @Test
  public void testUnrecognizedAssocOption() {
    String grammar = """
      grammar A;
      x : 'x'
        | x '+'<assoc=right> x   // warning 157
        |<assoc=right> x '*' x   // ok
        ;
      """;
    String expected = "warning(" + ErrorType.UNRECOGNIZED_ASSOC_OPTION.code + "): A.g4:3:10: rule 'x' contains an 'assoc' terminal option in an unrecognized location\n";

    String[] pair = new String[]{grammar, expected};

    super.testErrors(pair, true);
  }

  /**
   * This test ensures the {@link ErrorType#FRAGMENT_ACTION_IGNORED} warning
   * is produced as described in the documentation.
   */

  @Test
  public void testFragmentActionIgnored() {
    String grammar = """
      lexer grammar A;
      X1 : 'x' -> more    // ok
         ;
      Y1 : 'x' {more();}  // ok
         ;
      fragment
      X2 : 'x' -> more    // warning 158
         ;
      fragment
      Y2 : 'x' {more();}  // warning 158
         ;
      """;
    String expected = "warning(" + ErrorType.FRAGMENT_ACTION_IGNORED.code + "): A.g4:7:12: fragment rule 'X2' contains an action or command which can never be executed\n" + "warning(" + ErrorType.FRAGMENT_ACTION_IGNORED.code + "): A.g4:10:9: fragment rule 'Y2' contains an action or command which can never be executed\n";

    String[] pair = new String[]{grammar, expected};

    super.testErrors(pair, true);
  }

  /**
   * This is a regression test for antlr/antlr4#500 "Array Index Out Of
   * Bounds".
   * <a href="https://github.com/antlr/antlr4/issues/500">...</a>
   */

  @Test
  public void testTokenNamedEOF() {
    String grammar = """
      lexer grammar A;
      WS : ' ';
       EOF : 'a';
      """;
    String expected = "error(" + ErrorType.RESERVED_RULE_NAME.code + "): A.g4:3:1: cannot declare a rule with reserved name 'EOF'\n";

    String[] pair = new String[]{grammar, expected};

    super.testErrors(pair, true);
  }

  /**
   * This is a regression test for antlr/antlr4#649 "unknown target causes
   * null ptr exception.".
   * <a href="https://github.com/antlr/antlr4/issues/649">...</a>
   */

  @Test
  public void testInvalidLanguageInGrammar() {
    String grammar = """
      grammar T;
      options { language=Foo; }
      start : 'T' EOF;
      """;
    String expected = "error(" + ErrorType.CANNOT_CREATE_TARGET_GENERATOR.code + "):  ANTLR cannot generate 'org.antlr.v4.codegen.target.FooTarget' code as of version " + Tool.VERSION + "\n";

    String[] pair = new String[]{grammar, expected};

    super.testErrors(pair, true);
  }

  /**
   * This is a regression test for antlr/antlr4#649 "unknown target causes
   * null ptr exception.".
   * <a href="https://github.com/antlr/antlr4/issues/649">...</a>
   */

  @Test
  public void testInvalidLanguageInGrammarWithLexerCommand() {
    String grammar = """
      grammar T;
      options { language=Foo; }
      start : 'T' EOF;
      Something : 'something' -> channel(CUSTOM);""";
    String expected = "error(" + ErrorType.CANNOT_CREATE_TARGET_GENERATOR.code + "):  ANTLR cannot generate 'org.antlr.v4.codegen.target.FooTarget' code as of version " + Tool.VERSION + "\n" + "warning(" + ErrorType.CONSTANT_VALUE_IS_NOT_A_RECOGNIZED_CHANNEL_NAME.code + "): T.g4:4:35: 'CUSTOM' is not a recognized channel name\n";

    String[] pair = new String[]{grammar, expected};

    super.testErrors(pair, true);
  }


  @Test
  public void testChannelDefinitionInLexer() {
    String grammar = """
      lexer grammar T;
      
      channels {
      	WHITESPACE_CHANNEL,
      	COMMENT_CHANNEL
      }
      
      COMMENT:    '//' ~[\\n]+ -> channel(COMMENT_CHANNEL);
      WHITESPACE: [ \\t]+      -> channel(WHITESPACE_CHANNEL);
      """;

    String expected = "";

    String[] pair = {grammar, expected};
    super.testErrors(pair, true);
  }


  @Test
  public void testChannelDefinitionInParser() {
    String grammar = """
      parser grammar T;
      
      channels {
      	WHITESPACE_CHANNEL,
      	COMMENT_CHANNEL
      }
      
      start : EOF;
      """;

    String expected = "error(" + ErrorType.CHANNELS_BLOCK_IN_PARSER_GRAMMAR.code + "): T.g4:3:0: custom channels are not supported in parser grammars\n";

    String[] pair = {grammar, expected};
    super.testErrors(pair, true);
  }


  @Test
  public void testChannelDefinitionInCombined() {
    String grammar = """
      grammar T;
      
      channels {
      	WHITESPACE_CHANNEL,
      	COMMENT_CHANNEL
      }
      
      start : EOF;
      
      COMMENT:    '//' ~[\\n]+ -> channel(COMMENT_CHANNEL);
      WHITESPACE: [ \\t]+      -> channel(WHITESPACE_CHANNEL);
      """;

    String expected = "warning(" + ErrorType.CONSTANT_VALUE_IS_NOT_A_RECOGNIZED_CHANNEL_NAME.code + "): T.g4:10:35: 'COMMENT_CHANNEL' is not a recognized channel name\n" + "warning(" + ErrorType.CONSTANT_VALUE_IS_NOT_A_RECOGNIZED_CHANNEL_NAME.code + "): T.g4:11:35: 'WHITESPACE_CHANNEL' is not a recognized channel name\n" + "error(" + ErrorType.CHANNELS_BLOCK_IN_COMBINED_GRAMMAR.code + "): T.g4:3:0: custom channels are not supported in combined grammars\n";

    String[] pair = {grammar, expected};
    super.testErrors(pair, true);
  }

  /**
   * This is a regression test for antlr/antlr4#497 now that antlr/antlr4#309
   * is resolved.
   * <a href="https://github.com/antlr/antlr4/issues/497">...</a>
   * <a href="https://github.com/antlr/antlr4/issues/309">...</a>
   */

  @Test
  public void testChannelDefinitions() {
    String grammar = """
      lexer grammar T;
      
      channels {
      	WHITESPACE_CHANNEL,
      	COMMENT_CHANNEL
      }
      
      COMMENT:    '//' ~[\\n]+ -> channel(COMMENT_CHANNEL);
      WHITESPACE: [ \\t]+      -> channel(WHITESPACE_CHANNEL);
      NEWLINE:    '\\r'? '\\n' -> channel(NEWLINE_CHANNEL);""";

    // WHITESPACE_CHANNEL and COMMENT_CHANNEL are defined, but NEWLINE_CHANNEL is not
    String expected = "warning(" + ErrorType.CONSTANT_VALUE_IS_NOT_A_RECOGNIZED_CHANNEL_NAME.code + "): T.g4:10:34: 'NEWLINE_CHANNEL' is not a recognized channel name\n";

    String[] pair = {grammar, expected};
    super.testErrors(pair, true);
  }

  /**
   * This is a test for {@link ErrorType#RULE_WITH_TOO_FEW_ALT_LABELS_GROUP}.
   *
   * <p>
   * This test verifies that
   * {@link ErrorType#RULE_WITH_TOO_FEW_ALT_LABELS_GROUP} is reported if one
   * of the rules in a base context group uses alt labels, and another does
   * not.</p>
   */

  @Test
  public void testRuleWithTooFewAltLabelsGroup() {
    String grammar = """
      grammar T;
      tokens { Foo1, Foo2 }
      start options { baseContext = start2; }
        : Foo1 EOF
        ;
      start2
        : Foo1 EOF # labeled
        | Foo2 EOF # labeled
        ;
      """;

    String expected = "error(" + ErrorType.RULE_WITH_TOO_FEW_ALT_LABELS_GROUP.code + "): T.g4:3:0: rule 'start': must label all alternatives in rules with the same base context, or none\n";

    String[] pair = {grammar, expected};
    super.testErrors(pair, true);
  }

  /**
   * This is a test for {@link ErrorType#RULE_WITH_TOO_FEW_ALT_LABELS_GROUP}.
   *
   * <p>
   * This test verifies that
   * {@link ErrorType#RULE_WITH_TOO_FEW_ALT_LABELS_GROUP} is disabled if
   * {@link ErrorType#RULE_WITH_TOO_FEW_ALT_LABELS} is reported for one of the
   * rules with the base context of the group.</p>
   */

  @Test
  public void testRuleWithTooFewAltLabelsGroupSuppressed() {
    String grammar = """
      grammar T;
      tokens { Foo1, Foo2 }
      start options { baseContext = start2; }
        : Foo1 EOF
        ;
      start2
        : Foo1 EOF
        | Foo2 EOF # labeled
        ;
      """;

    String expected = "error(" + ErrorType.RULE_WITH_TOO_FEW_ALT_LABELS.code + "): T.g4:6:0: rule 'start2': must label all alternatives or none\n";

    String[] pair = {grammar, expected};
    super.testErrors(pair, true);
  }

  /**
   * This is a test for {@link ErrorType#BASE_CONTEXT_MUST_BE_RULE_NAME}.
   */

  @Test
  public void testBaseContextMustBeRuleName() {
    String grammar = """
      grammar T;
      tokens { Foo1, Foo2 }
      start options { baseContext = start2; }
        : Foo1 EOF
        ;
      """;

    String expected = "error(" + ErrorType.BASE_CONTEXT_MUST_BE_RULE_NAME.code + "): T.g4:3:30: rule 'start': baseContext option value must reference a rule\n";

    String[] pair = {grammar, expected};
    super.testErrors(pair, true);
  }

  /**
   * This is a test for {@link ErrorType#BASE_CONTEXT_CANNOT_BE_TRANSITIVE}.
   */

  @Test
  public void testBaseContextCannotBeTransitive() {
    String grammar = """
      grammar T;
      tokens { Foo1, Foo2 }
      start options { baseContext = start2; }
        : Foo1 EOF
        ;
      start2 options { baseContext = start3; }
        : Foo1 EOF
        ;
      start3
        : Foo1 EOF
        ;
      """;

    String expected = "error(" + ErrorType.BASE_CONTEXT_CANNOT_BE_TRANSITIVE.code + "): T.g4:3:30: rule 'start': base context must reference a rule that does not specify a base context\n";

    String[] pair = {grammar, expected};
    super.testErrors(pair, true);
  }

  /**
   * This is a test for {@link ErrorType#BASE_CONTEXT_CANNOT_BE_TRANSITIVE}.
   *
   * <p>
   * This test verifies that
   * {@link ErrorType#BASE_CONTEXT_CANNOT_BE_TRANSITIVE} applies when the base
   * context option is explicitly set to the enclosing rule.</p>
   */

  @Test
  public void testBaseContextCannotBeTransitive_Self() {
    String grammar = """
      grammar T;
      tokens { Foo1 }
      start options { baseContext = start; }
        : Foo1 EOF
        ;
      """;

    String expected = "error(" + ErrorType.BASE_CONTEXT_CANNOT_BE_TRANSITIVE.code + "): T.g4:3:30: rule 'start': base context must reference a rule that does not specify a base context\n";

    String[] pair = {grammar, expected};
    super.testErrors(pair, true);
  }

  // Test for https://github.com/antlr/antlr4/issues/1556

  @Test
  public void testRangeInParserGrammar() {
    String grammar = """
      grammar T;
      a:  'A'..'Z' ;
      """;
    String expected = "error(" + ErrorType.TOKEN_RANGE_IN_PARSER.code + "): T.g4:2:4: token ranges not allowed in parser: 'A'..'Z'\n";

    String[] pair = new String[]{grammar, expected};

    super.testErrors(pair, true);
  }
}
