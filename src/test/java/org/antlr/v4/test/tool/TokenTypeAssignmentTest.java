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

import org.antlr.v4.runtime.Token;
import org.antlr.v4.tool.Grammar;
import org.antlr.v4.tool.LexerGrammar;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.StringTokenizer;

import static org.assertj.core.api.Assertions.assertThat;

class TokenTypeAssignmentTest extends AbstractBaseTest {

  @Test
  void testParserSimpleTokens() throws Exception {
    Grammar g = new Grammar(
      """
        parser grammar t;
        a : A | B;
        b : C ;""");
    String rules = "a, b";
    String tokenNames = "A, B, C";
    checkSymbols(g, rules, tokenNames);
  }

  @Test
  void testParserTokensSection() throws Exception {
    Grammar g = new Grammar(
      """
        parser grammar t;
        tokens {
          C,
          D\
        }
        a : A | B;
        b : C ;""");
    String rules = "a, b";
    String tokenNames = "A, B, C, D";
    checkSymbols(g, rules, tokenNames);
  }

  @Test
  void testLexerTokensSection() throws Exception {
    LexerGrammar g = new LexerGrammar(
      """
        lexer grammar t;
        tokens {
          C,
          D\
        }
        A : 'a';
        C : 'c' ;""");
    String rules = "A, C";
    String tokenNames = "A, C, D";
    checkSymbols(g, rules, tokenNames);
  }

  @Test
  void testCombinedGrammarLiterals() throws Exception {
    // "foo" is not a token name
    Grammar g = new Grammar(
      """
        grammar t;
        a : 'begin' b 'end';
        b : C ';' ;
        ID : 'a' ;
        FOO : 'foo' ;
        C : 'c' ;
        """);        // nor is 'c'
    String rules = "a, b";
    String tokenNames = "C, FOO, ID, 'begin', 'end', ';'";
    checkSymbols(g, rules, tokenNames);
  }

  @Test
  void testLiteralInParserAndLexer() throws Exception {
    // 'x' is token and char in lexer rule
    Grammar g = new Grammar(
      """
        grammar t;
        a : 'x' E ;\s
        E: 'x' '0' ;
        """);

    String literals = "['x']";
    assertThat(g.stringLiteralToTypeMap.keySet()).hasToString(literals);

    assertThat(g.implicitLexer.stringLiteralToTypeMap.keySet()).hasToString("['x']"); // pushed in lexer from parser

    String[] typeToTokenName = g.getTokenDisplayNames();
    Set<String> tokens = new LinkedHashSet<>();
    for (String t : typeToTokenName) if (t != null) tokens.add(t);
    assertThat(tokens).hasToString("[<INVALID>, 'x', E]");
  }

  @Test
  void testPredDoesNotHideNameToLiteralMapInLexer() throws Exception {
    // 'x' is token and char in lexer rule
    Grammar g = new Grammar(
      """
        grammar t;
        a : 'x' X ;\s
        X: 'x' {true}?;
        """); // must match as alias even with pred

    assertThat(g.stringLiteralToTypeMap).hasToString("{'x'=1}");
    assertThat(g.tokenNameToTypeMap).hasToString("{EOF=-1, X=1}");

    // pushed in lexer from parser
    assertThat(g.implicitLexer.stringLiteralToTypeMap).hasToString("{'x'=1}");
    assertThat(g.implicitLexer.tokenNameToTypeMap).hasToString("{EOF=-1, X=1}");
  }

  @Test
  void testCombinedGrammarWithRefToLiteralButNoTokenIDRef() throws Exception {
    Grammar g = new Grammar(
      """
        grammar t;
        a : 'a' ;
        A : 'a' ;
        """);
    String rules = "a";
    String tokenNames = "A, 'a'";
    checkSymbols(g, rules, tokenNames);
  }

  @Test
  void testSetDoesNotMissTokenAliases() throws Exception {
    Grammar g = new Grammar(
      """
        grammar t;
        a : 'a'|'b' ;
        A : 'a' ;
        B : 'b' ;
        """);
    String rules = "a";
    String tokenNames = "A, 'a', B, 'b'";
    checkSymbols(g, rules, tokenNames);
  }

  // T E S T  L I T E R A L  E S C A P E S

  @Test
  void testParserCharLiteralWithEscape() throws Exception {
    Grammar g = new Grammar(
      """
        grammar t;
        a : '\\n';
        """);
    Set<?> literals = g.stringLiteralToTypeMap.keySet();
    // must store literals how they appear in the antlr grammar
    assertThat(literals.toArray()[0]).isEqualTo("'\\n'");
  }

  @Test
  void testParserCharLiteralWithBasicUnicodeEscape() throws Exception {
    Grammar g = new Grammar(
      """
        grammar t;
        a : '\\uABCD';
        """);
    Set<?> literals = g.stringLiteralToTypeMap.keySet();
    // must store literals how they appear in the antlr grammar
    assertThat(literals.toArray()[0]).isEqualTo("'\\uABCD'");
  }

  @Test
  void testParserCharLiteralWithExtendedUnicodeEscape() throws Exception {
    Grammar g = new Grammar(
      """
        grammar t;
        a : '\\u{1ABCD}';
        """);
    Set<?> literals = g.stringLiteralToTypeMap.keySet();
    // must store literals how they appear in the antlr grammar
    assertThat(literals.toArray()[0]).isEqualTo("'\\u{1ABCD}'");
  }

  protected void checkSymbols(Grammar g, String rulesStr, String allValidTokensStr) {
    String[] typeToTokenName = g.getTokenNames();
    Set<String> tokens = new HashSet<>();
    for (int i = 0; i < typeToTokenName.length; i++) {
      String t = typeToTokenName[i];
      if (t != null) {
        if (t.startsWith(Grammar.AUTO_GENERATED_TOKEN_NAME_PREFIX)) {
          tokens.add(g.getTokenDisplayName(i));
        } else {
          tokens.add(t);
        }
      }
    }

    // make sure expected tokens are there
    StringTokenizer st = new StringTokenizer(allValidTokensStr, ", ");
    while (st.hasMoreTokens()) {
      String tokenName = st.nextToken();
      assertThat(g.getTokenType(tokenName))
        .as("token " + tokenName + " expected, but was undefined")
        .isNotEqualTo(Token.INVALID_TYPE);
      tokens.remove(tokenName);
    }
    // make sure there are not any others (other than <EOF> etc...)
    for (String tokenName : tokens) {
      assertThat(g.getTokenType(tokenName))
        .as("unexpected token name " + tokenName)
        .isLessThan(Token.MIN_USER_TOKEN_TYPE);
    }

    // make sure all expected rules are there
    st = new StringTokenizer(rulesStr, ", ");
    int n = 0;
    while (st.hasMoreTokens()) {
      String ruleName = st.nextToken();
      assertThat(g.getRule(ruleName))
        .as("rule " + ruleName + " expected")
        .isNotNull();
      n++;
    }
    // make sure there are no extra rules
    assertThat(g.rules)
      .as("number of rules mismatch; expecting " + n + "; found " + g.rules.size())
      .hasSize(n);

  }

}
