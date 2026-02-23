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

import org.antlr.v4.tool.ErrorType;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test the set stuff in lexer and parser
 */
class SetsTest extends AbstractBaseTest {
  protected boolean debug = false;

  @Test
  void testSeqDoesNotBecomeSet() {
    // this must return A not I to the parser; calling a nonfragment rule
    // from a nonfragment rule does not set the overall token.
    String grammar =
      """
        grammar P;
        a : C {System.out.println(_input.getText());} ;
        fragment A : '1' | '2';
        fragment B : '3' '4';
        C : A | B;
        """;
    String found = execParser("P.g4", grammar, "PParser", "PLexer",
      "a", "34", debug);
    assertThat(found).isEqualTo("34\n");
  }

  @Test
  void testParserSet() {
    String grammar =
      """
        grammar T;
        a : t=('x'|'y') {System.out.println($t.text);} ;
        """;
    String found = execParser("T.g4", grammar, "TParser", "TLexer",
      "a", "x", debug);
    assertThat(found).isEqualTo("x\n");
  }

  @Test
  void testParserNotSet() {
    String grammar =
      """
        grammar T;
        a : t=~('x'|'y') 'z' {System.out.println($t.text);} ;
        """;
    String found = execParser("T.g4", grammar, "TParser", "TLexer",
      "a", "zz", debug);
    assertThat(found).isEqualTo("z\n");
  }

  @Test
  void testParserNotToken() {
    String grammar =
      """
        grammar T;
        a : ~'x' 'z' {System.out.println(_input.getText());} ;
        """;
    String found = execParser("T.g4", grammar, "TParser", "TLexer",
      "a", "zz", debug);
    assertThat(found).isEqualTo("zz\n");
  }

  @Test
  void testParserNotTokenWithLabel() {
    String grammar =
      """
        grammar T;
        a : t=~'x' 'z' {System.out.println($t.text);} ;
        """;
    String found = execParser("T.g4", grammar, "TParser", "TLexer",
      "a", "zz", debug);
    assertThat(found).isEqualTo("z\n");
  }

  @Test
  void testRuleAsSet() {
    String grammar =
      """
        grammar T;
        a @after {System.out.println(_input.getText());} : 'a' | 'b' |'c' ;
        """;
    String found = execParser("T.g4", grammar, "TParser", "TLexer",
      "a", "b", debug);
    assertThat(found).isEqualTo("b\n");
  }

  @Test
  void testNotChar() {
    String grammar =
      """
        grammar T;
        a : A {System.out.println($A.text);} ;
        A : ~'b' ;
        """;
    String found = execParser("T.g4", grammar, "TParser", "TLexer",
      "a", "x", debug);
    assertThat(found).isEqualTo("x\n");
  }

  @Test
  void testOptionalSingleElement() {
    String grammar =
      """
        grammar T;
        a : A? 'c' {System.out.println(_input.getText());} ;
        A : 'b' ;
        """;
    String found = execParser("T.g4", grammar, "TParser", "TLexer",
      "a", "bc", debug);
    assertThat(found).isEqualTo("bc\n");
  }

  @Test
  void testOptionalLexerSingleElement() {
    String grammar =
      """
        grammar T;
        a : A {System.out.println(_input.getText());} ;
        A : 'b'? 'c' ;
        """;
    String found = execParser("T.g4", grammar, "TParser", "TLexer",
      "a", "bc", debug);
    assertThat(found).isEqualTo("bc\n");
  }

  @Test
  void testStarLexerSingleElement() {
    String grammar =
      """
        grammar T;
        a : A {System.out.println(_input.getText());} ;
        A : 'b'* 'c' ;
        """;
    String found = execParser("T.g4", grammar, "TParser", "TLexer",
      "a", "bbbbc", debug);
    assertThat(found).isEqualTo("bbbbc\n");
    found = execParser("T.g4", grammar, "TParser", "TLexer",
      "a", "c", debug);
    assertThat(found).isEqualTo("c\n");
  }

  @Test
  void testPlusLexerSingleElement() {
    String grammar =
      """
        grammar T;
        a : A {System.out.println(_input.getText());} ;
        A : 'b'+ 'c' ;
        """;
    String found = execParser("T.g4", grammar, "TParser", "TLexer",
      "a", "bbbbc", debug);
    assertThat(found).isEqualTo("bbbbc\n");
  }

  @Test
  void testOptionalSet() {
    String grammar =
      """
        grammar T;
        a : ('a'|'b')? 'c' {System.out.println(_input.getText());} ;
        """;
    String found = execParser("T.g4", grammar, "TParser", "TLexer",
      "a", "ac", debug);
    assertThat(found).isEqualTo("ac\n");
  }

  @Test
  void testStarSet() {
    String grammar =
      """
        grammar T;
        a : ('a'|'b')* 'c' {System.out.println(_input.getText());} ;
        """;
    String found = execParser("T.g4", grammar, "TParser", "TLexer",
      "a", "abaac", debug);
    assertThat(found).isEqualTo("abaac\n");
  }

  @Test
  void testPlusSet() {
    String grammar =
      """
        grammar T;
        a : ('a'|'b')+ 'c' {System.out.println(_input.getText());} ;
        """;
    String found = execParser("T.g4", grammar, "TParser", "TLexer",
      "a", "abaac", debug);
    assertThat(found).isEqualTo("abaac\n");
  }

  @Test
  void testLexerOptionalSet() {
    String grammar =
      """
        grammar T;
        a : A {System.out.println(_input.getText());} ;
        A : ('a'|'b')? 'c' ;
        """;
    String found = execParser("T.g4", grammar, "TParser", "TLexer",
      "a", "ac", debug);
    assertThat(found).isEqualTo("ac\n");
  }

  @Test
  void testLexerStarSet() {
    String grammar =
      """
        grammar T;
        a : A {System.out.println(_input.getText());} ;
        A : ('a'|'b')* 'c' ;
        """;
    String found = execParser("T.g4", grammar, "TParser", "TLexer",
      "a", "abaac", debug);
    assertThat(found).isEqualTo("abaac\n");
  }

  @Test
  void testLexerPlusSet() {
    String grammar =
      """
        grammar T;
        a : A {System.out.println(_input.getText());} ;
        A : ('a'|'b')+ 'c' ;
        """;
    String found = execParser("T.g4", grammar, "TParser", "TLexer",
      "a", "abaac", debug);
    assertThat(found).isEqualTo("abaac\n");
  }

  @Test
  void testNotCharSet() {
    String grammar =
      """
        grammar T;
        a : A {System.out.println($A.text);} ;
        A : ~('b'|'c') ;
        """;
    String found = execParser("T.g4", grammar, "TParser", "TLexer",
      "a", "x", debug);
    assertThat(found).isEqualTo("x\n");
  }

  @Test
  void testNotCharSetWithLabel() {
    String grammar =
      """
        grammar T;
        a : A {System.out.println($A.text);} ;
        A : h=~('b'|'c') ;
        """;
    String found = execParser("T.g4", grammar, "TParser", "TLexer",
      "a", "x", debug);
    assertThat(found).isEqualTo("x\n");
  }

  @Test
  void testNotCharSetWithRuleRef() {
    // might be a useful feature to add someday
    String[] pair = new String[]{
      """
grammar T;
a : A {System.out.println($A.text);} ;
A : ~('a'|B) ;
B : 'b' ;
""",
      "error(" + ErrorType.UNSUPPORTED_REFERENCE_IN_LEXER_SET.code
      + "): T.g4:3:10: rule reference 'B' is not currently supported in a set\n"
    };
    super.testErrors(pair, true);
  }

  @Test
  void testNotCharSetWithString() {
    // might be a useful feature to add someday
    String[] pair = new String[]{
      """
grammar T;
a : A {System.out.println($A.text);} ;
A : ~('a'|'aa') ;
B : 'b' ;
""",
      "error(" + ErrorType.INVALID_LITERAL_IN_LEXER_SET.code
      + "): T.g4:3:10: multi-character literals are not allowed in lexer sets: 'aa'\n"
    };
    super.testErrors(pair, true);
  }

  @Test
  void testNotCharSetWithRuleRef3() {
    String grammar =
      "grammar T;\n" +
        "a : A {System.out.println($A.text);} ;\n" +
        "A : ('a'|B) ;\n" +  // this doesn't collapse to set but works
        "fragment\n" +
        "B : ~('a'|'c') ;\n";
    String found = execParser("T.g4", grammar, "TParser", "TLexer",
      "a", "x", debug);
    assertThat(found).isEqualTo("x\n");
  }

  @Test
  void testCharSetLiteral() {
    String grammar =
      """
        grammar T;
        a : (A {System.out.println($A.text);})+ ;
        A : [AaBb] ;
        WS : (' '|'\\n')+ -> skip ;
        """;
    String found = execParser("T.g4", grammar, "TParser", "TLexer",
      "a", "A a B b", debug);
    assertThat(found).isEqualTo("""
      A
      a
      B
      b
      """);
  }

  @Test
  void testComplementSet() {
    String grammar =
      """
        grammar T;
        parse : ~NEW_LINE;
        NEW_LINE: '\\r'? '\\n';""";
    String found = execParser("T.g4", grammar, "TParser", "TLexer", "parse", "a", false);
    assertThat(found).isEqualTo("");
    assertThat(this.stderrDuringParse).isEqualTo(
      "line 1:0 token recognition error at: 'a'\nline 1:1 missing {} at '<EOF>'\n");
  }
}
