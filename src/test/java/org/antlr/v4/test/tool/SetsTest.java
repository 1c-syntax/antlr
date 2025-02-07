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

import org.antlr.v4.tool.ErrorType;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.antlr.v4.TestUtils.assertEquals;

/**
 * Test the set stuff in lexer and parser
 */
@Disabled("Переделать на ANTLR runtime/Generator")
public class SetsTest extends AbstractBaseTest {
  protected boolean debug = false;

  @Test
  public void testSeqDoesNotBecomeSet() {
    // this must return A not I to the parser; calling a nonfragment rule
    // from a nonfragment rule does not set the overall token.
    String grammar =
      "grammar P;\n" +
        "a : C {System.out.println(_input.getText());} ;\n" +
        "fragment A : '1' | '2';\n" +
        "fragment B : '3' '4';\n" +
        "C : A | B;\n";
    String found = execParser("P.g4", grammar, "PParser", "PLexer",
      "a", "34", debug);
    assertEquals("34\n", found);
  }

  @Test
  public void testParserSet() {
    String grammar =
      "grammar T;\n" +
        "a : t=('x'|'y') {System.out.println($t.text);} ;\n";
    String found = execParser("T.g4", grammar, "TParser", "TLexer",
      "a", "x", debug);
    assertEquals("x\n", found);
  }

  @Test
  public void testParserNotSet() {
    String grammar =
      "grammar T;\n" +
        "a : t=~('x'|'y') 'z' {System.out.println($t.text);} ;\n";
    String found = execParser("T.g4", grammar, "TParser", "TLexer",
      "a", "zz", debug);
    assertEquals("z\n", found);
  }

  @Test
  public void testParserNotToken() {
    String grammar =
      "grammar T;\n" +
        "a : ~'x' 'z' {System.out.println(_input.getText());} ;\n";
    String found = execParser("T.g4", grammar, "TParser", "TLexer",
      "a", "zz", debug);
    assertEquals("zz\n", found);
  }

  @Test
  public void testParserNotTokenWithLabel() {
    String grammar =
      "grammar T;\n" +
        "a : t=~'x' 'z' {System.out.println($t.text);} ;\n";
    String found = execParser("T.g4", grammar, "TParser", "TLexer",
      "a", "zz", debug);
    assertEquals("z\n", found);
  }

  @Test
  public void testRuleAsSet() {
    String grammar =
      "grammar T;\n" +
        "a @after {System.out.println(_input.getText());} : 'a' | 'b' |'c' ;\n";
    String found = execParser("T.g4", grammar, "TParser", "TLexer",
      "a", "b", debug);
    assertEquals("b\n", found);
  }

  @Test
  public void testNotChar() {
    String grammar =
      "grammar T;\n" +
        "a : A {System.out.println($A.text);} ;\n" +
        "A : ~'b' ;\n";
    String found = execParser("T.g4", grammar, "TParser", "TLexer",
      "a", "x", debug);
    assertEquals("x\n", found);
  }

  @Test
  public void testOptionalSingleElement() {
    String grammar =
      "grammar T;\n" +
        "a : A? 'c' {System.out.println(_input.getText());} ;\n" +
        "A : 'b' ;\n";
    String found = execParser("T.g4", grammar, "TParser", "TLexer",
      "a", "bc", debug);
    assertEquals("bc\n", found);
  }

  @Test
  public void testOptionalLexerSingleElement() {
    String grammar =
      "grammar T;\n" +
        "a : A {System.out.println(_input.getText());} ;\n" +
        "A : 'b'? 'c' ;\n";
    String found = execParser("T.g4", grammar, "TParser", "TLexer",
      "a", "bc", debug);
    assertEquals("bc\n", found);
  }

  @Test
  public void testStarLexerSingleElement() {
    String grammar =
      "grammar T;\n" +
        "a : A {System.out.println(_input.getText());} ;\n" +
        "A : 'b'* 'c' ;\n";
    String found = execParser("T.g4", grammar, "TParser", "TLexer",
      "a", "bbbbc", debug);
    assertEquals("bbbbc\n", found);
    found = execParser("T.g4", grammar, "TParser", "TLexer",
      "a", "c", debug);
    assertEquals("c\n", found);
  }

  @Test
  public void testPlusLexerSingleElement() {
    String grammar =
      "grammar T;\n" +
        "a : A {System.out.println(_input.getText());} ;\n" +
        "A : 'b'+ 'c' ;\n";
    String found = execParser("T.g4", grammar, "TParser", "TLexer",
      "a", "bbbbc", debug);
    assertEquals("bbbbc\n", found);
  }

  @Test
  public void testOptionalSet() {
    String grammar =
      "grammar T;\n" +
        "a : ('a'|'b')? 'c' {System.out.println(_input.getText());} ;\n";
    String found = execParser("T.g4", grammar, "TParser", "TLexer",
      "a", "ac", debug);
    assertEquals("ac\n", found);
  }

  @Test
  public void testStarSet() {
    String grammar =
      "grammar T;\n" +
        "a : ('a'|'b')* 'c' {System.out.println(_input.getText());} ;\n";
    String found = execParser("T.g4", grammar, "TParser", "TLexer",
      "a", "abaac", debug);
    assertEquals("abaac\n", found);
  }

  @Test
  public void testPlusSet() {
    String grammar =
      "grammar T;\n" +
        "a : ('a'|'b')+ 'c' {System.out.println(_input.getText());} ;\n";
    String found = execParser("T.g4", grammar, "TParser", "TLexer",
      "a", "abaac", debug);
    assertEquals("abaac\n", found);
  }

  @Test
  public void testLexerOptionalSet() {
    String grammar =
      "grammar T;\n" +
        "a : A {System.out.println(_input.getText());} ;\n" +
        "A : ('a'|'b')? 'c' ;\n";
    String found = execParser("T.g4", grammar, "TParser", "TLexer",
      "a", "ac", debug);
    assertEquals("ac\n", found);
  }

  @Test
  public void testLexerStarSet() {
    String grammar =
      "grammar T;\n" +
        "a : A {System.out.println(_input.getText());} ;\n" +
        "A : ('a'|'b')* 'c' ;\n";
    String found = execParser("T.g4", grammar, "TParser", "TLexer",
      "a", "abaac", debug);
    assertEquals("abaac\n", found);
  }

  @Test
  public void testLexerPlusSet() {
    String grammar =
      "grammar T;\n" +
        "a : A {System.out.println(_input.getText());} ;\n" +
        "A : ('a'|'b')+ 'c' ;\n";
    String found = execParser("T.g4", grammar, "TParser", "TLexer",
      "a", "abaac", debug);
    assertEquals("abaac\n", found);
  }

  @Test
  public void testNotCharSet() {
    String grammar =
      "grammar T;\n" +
        "a : A {System.out.println($A.text);} ;\n" +
        "A : ~('b'|'c') ;\n";
    String found = execParser("T.g4", grammar, "TParser", "TLexer",
      "a", "x", debug);
    assertEquals("x\n", found);
  }

  @Test
  public void testNotCharSetWithLabel() {
    String grammar =
      "grammar T;\n" +
        "a : A {System.out.println($A.text);} ;\n" +
        "A : h=~('b'|'c') ;\n";
    String found = execParser("T.g4", grammar, "TParser", "TLexer",
      "a", "x", debug);
    assertEquals("x\n", found);
  }

  @Test
  public void testNotCharSetWithRuleRef() {
    // might be a useful feature to add someday
    String[] pair = new String[]{
      "grammar T;\n" +
        "a : A {System.out.println($A.text);} ;\n" +
        "A : ~('a'|B) ;\n" +
        "B : 'b' ;\n",
      "error(" + ErrorType.UNSUPPORTED_REFERENCE_IN_LEXER_SET.code + "): T.g4:3:10: rule reference 'B' is not currently supported in a set\n"
    };
    super.testErrors(pair, true);
  }

  @Test
  public void testNotCharSetWithString() {
    // might be a useful feature to add someday
    String[] pair = new String[]{
      "grammar T;\n" +
        "a : A {System.out.println($A.text);} ;\n" +
        "A : ~('a'|'aa') ;\n" +
        "B : 'b' ;\n",
      "error(" + ErrorType.INVALID_LITERAL_IN_LEXER_SET.code + "): T.g4:3:10: multi-character literals are not allowed in lexer sets: 'aa'\n"
    };
    super.testErrors(pair, true);
  }

  @Test
  public void testNotCharSetWithRuleRef3() {
    String grammar =
      "grammar T;\n" +
        "a : A {System.out.println($A.text);} ;\n" +
        "A : ('a'|B) ;\n" +  // this doesn't collapse to set but works
        "fragment\n" +
        "B : ~('a'|'c') ;\n";
    String found = execParser("T.g4", grammar, "TParser", "TLexer",
      "a", "x", debug);
    assertEquals("x\n", found);
  }

  @Test
  public void testCharSetLiteral() {
    String grammar =
      "grammar T;\n" +
        "a : (A {System.out.println($A.text);})+ ;\n" +
        "A : [AaBb] ;\n" +
        "WS : (' '|'\\n')+ -> skip ;\n";
    String found = execParser("T.g4", grammar, "TParser", "TLexer",
      "a", "A a B b", debug);
    assertEquals("A\n" +
      "a\n" +
      "B\n" +
      "b\n", found);
  }

  @Test
  public void testComplementSet() {
    String grammar =
      "grammar T;\n" +
        "parse : ~NEW_LINE;\n" +
        "NEW_LINE: '\\r'? '\\n';";
    String found = execParser("T.g4", grammar, "TParser", "TLexer", "parse", "a", false);
    assertEquals("", found);
    assertEquals("line 1:0 token recognition error at: 'a'\nline 1:1 missing {} at '<EOF>'\n", this.stderrDuringParse);
  }
}
