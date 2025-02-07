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

import org.antlr.v4.runtime.Token;
import org.antlr.v4.tool.Grammar;
import org.antlr.v4.tool.LexerGrammar;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.StringTokenizer;

import static org.antlr.v4.TestUtils.assertEquals;
import static org.assertj.core.api.Assertions.assertThat;

public class TokenTypeAssignmentTest extends AbstractBaseTest {

  @Test
  public void testParserSimpleTokens() throws Exception {
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
  public void testParserTokensSection() throws Exception {
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
  public void testLexerTokensSection() throws Exception {
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
  public void testCombinedGrammarLiterals() throws Exception {
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
  public void testLiteralInParserAndLexer() throws Exception {
    // 'x' is token and char in lexer rule
    Grammar g = new Grammar(
      """
        grammar t;
        a : 'x' E ;\s
        E: 'x' '0' ;
        """);

    String literals = "['x']";
    String foundLiterals = g.stringLiteralToTypeMap.keySet().toString();
    assertEquals(literals, foundLiterals);

    foundLiterals = g.implicitLexer.stringLiteralToTypeMap.keySet().toString();
    assertEquals("['x']", foundLiterals); // pushed in lexer from parser

    String[] typeToTokenName = g.getTokenDisplayNames();
    Set<String> tokens = new LinkedHashSet<>();
    for (String t : typeToTokenName) if (t != null) tokens.add(t);
    assertEquals("[<INVALID>, 'x', E]", tokens.toString());
  }

  @Test
  public void testPredDoesNotHideNameToLiteralMapInLexer() throws Exception {
    // 'x' is token and char in lexer rule
    Grammar g = new Grammar(
      """
        grammar t;
        a : 'x' X ;\s
        X: 'x' {true}?;
        """); // must match as alias even with pred

    assertEquals("{'x'=1}", g.stringLiteralToTypeMap.toString());
    assertEquals("{EOF=-1, X=1}", g.tokenNameToTypeMap.toString());

    // pushed in lexer from parser
    assertEquals("{'x'=1}", g.implicitLexer.stringLiteralToTypeMap.toString());
    assertEquals("{EOF=-1, X=1}", g.implicitLexer.tokenNameToTypeMap.toString());
  }

  @Test
  public void testCombinedGrammarWithRefToLiteralButNoTokenIDRef() throws Exception {
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
  public void testSetDoesNotMissTokenAliases() throws Exception {
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
  public void testParserCharLiteralWithEscape() throws Exception {
    Grammar g = new Grammar(
      """
        grammar t;
        a : '\\n';
        """);
    Set<?> literals = g.stringLiteralToTypeMap.keySet();
    // must store literals how they appear in the antlr grammar
    assertEquals("'\\n'", literals.toArray()[0]);
  }

  @Test
  public void testParserCharLiteralWithBasicUnicodeEscape() throws Exception {
    Grammar g = new Grammar(
      """
        grammar t;
        a : '\\uABCD';
        """);
    Set<?> literals = g.stringLiteralToTypeMap.keySet();
    // must store literals how they appear in the antlr grammar
    assertEquals("'\\uABCD'", literals.toArray()[0]);
  }

  @Test
  public void testParserCharLiteralWithExtendedUnicodeEscape() throws Exception {
    Grammar g = new Grammar(
      """
        grammar t;
        a : '\\u{1ABCD}';
        """);
    Set<?> literals = g.stringLiteralToTypeMap.keySet();
    // must store literals how they appear in the antlr grammar
    assertEquals("'\\u{1ABCD}'", literals.toArray()[0]);
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
      assertThat(g.getTokenType(tokenName) != Token.INVALID_TYPE)
        .as("token " + tokenName + " expected, but was undefined")
        .isTrue();
      tokens.remove(tokenName);
    }
    // make sure there are not any others (other than <EOF> etc...)
    for (String tokenName : tokens) {
      assertThat(g.getTokenType(tokenName) < Token.MIN_USER_TOKEN_TYPE)
        .as("unexpected token name " + tokenName)
        .isTrue();
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
    assertEquals("number of rules mismatch; expecting " + n + "; found " + g.rules.size(),
      n, g.rules.size());

  }

}
