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

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.antlr.v4.TestUtils.assertEquals;
import static org.assertj.core.api.Assertions.assertThat;

public class LexerExecTest extends AbstractBaseTest {
  @Test
  @Disabled("Переделать на ANTLR runtime/Generator")
  public void testQuoteTranslation() {
    String grammar =
      """
        lexer grammar L;
        QUOTE : '"' ;
        """; // make sure this compiles
    String found = execLexer("L.g4", grammar, "L", "\"");
    String expecting =
      """
        [@0,0:0='"',<1>,1:0]
        [@1,1:0='<EOF>',<-1>,1:1]
        """;
    assertEquals(expecting, found);
  }

  @Test
  @Disabled("Переделать на ANTLR runtime/Generator")
  public void testRefToRuleDoesNotSetTokenNorEmitAnother() {
    String grammar =
      """
        lexer grammar L;
        A : '-' I ;
        I : '0'..'9'+ ;
        WS : (' '|'\\n') -> skip ;""";
    String found = execLexer("L.g4", grammar, "L", "34 -21 3");
    String expecting =
      """
        [@0,0:1='34',<2>,1:0]
        [@1,3:5='-21',<1>,1:3]
        [@2,7:7='3',<2>,1:7]
        [@3,8:7='<EOF>',<-1>,1:8]
        """; // EOF has no length so range is 8:7 not 8:8
    assertEquals(expecting, found);
  }

  @Test
  @Disabled("Переделать на ANTLR runtime/Generator")
  public void testSlashes() {
    String grammar =
      """
        lexer grammar L;
        Backslash : '\\\\';
        Slash : '/';
        Vee : '\\\\/';
        Wedge : '/\\\\';
        WS : [ \\t] -> skip;""";
    String found = execLexer("L.g4", grammar, "L", "\\ / \\/ /\\");
    String expecting =
      """
        [@0,0:0='\\',<1>,1:0]
        [@1,2:2='/',<2>,1:2]
        [@2,4:5='\\/',<3>,1:4]
        [@3,7:8='/\\',<4>,1:7]
        [@4,9:8='<EOF>',<-1>,1:9]
        """;
    assertEquals(expecting, found);
  }

  /**
   * This is a regression test for antlr/antlr4#224: "Parentheses without
   * quantifier in lexer rules have unclear effect".
   * <a href="https://github.com/antlr/antlr4/issues/224">...</a>
   */
  @Test
  @Disabled("Переделать на ANTLR runtime/Generator")
  public void testParentheses() {
    String grammar =
      """
        lexer grammar Demo;
        
        START_BLOCK: '-.-.-';
        
        ID : (LETTER SEPARATOR) (LETTER SEPARATOR)+;
        fragment LETTER: L_A|L_K;
        fragment L_A: '.-';
        fragment L_K: '-.-';
        
        SEPARATOR: '!';
        """;
    String found = execLexer("Demo.g4", grammar, "Demo", "-.-.-!");
    String expecting =
      """
        [@0,0:4='-.-.-',<1>,1:0]
        [@1,5:5='!',<3>,1:5]
        [@2,6:5='<EOF>',<-1>,1:6]
        """;
    assertEquals(expecting, found);
  }

  @Test
  @Disabled("Переделать на ANTLR runtime/Generator")
  public void testNonGreedyTermination() {
    String grammar =
      """
        lexer grammar L;
        STRING : '"' ('""' | .)*? '"';""";

    String found = execLexer("L.g4", grammar, "L", "\"hi\"\"mom\"");
    assertEquals(
      """
        [@0,0:3='"hi"',<1>,1:0]
        [@1,4:8='"mom"',<1>,1:4]
        [@2,9:8='<EOF>',<-1>,1:9]
        """, found);
    assertThat(stderrDuringParse).isNull();
  }

  @Test
  @Disabled("Переделать на ANTLR runtime/Generator")
  public void testNonGreedyTermination2() {
    String grammar =
      """
        lexer grammar L;
        STRING : '"' ('""' | .)+? '"';""";

    String found = execLexer("L.g4", grammar, "L", "\"\"\"mom\"");
    assertEquals(
      """
        [@0,0:6='""\"mom"',<1>,1:0]
        [@1,7:6='<EOF>',<-1>,1:7]
        """, found);
    assertThat(stderrDuringParse).isNull();
  }

  @Test
  @Disabled("Переделать на ANTLR runtime/Generator")
  public void testGreedyOptional() {
    String grammar =
      """
        lexer grammar L;
        CMT : '//' .*? '\\n' CMT?;
        WS : (' '|'\\t')+;""";

    String found = execLexer("L.g4", grammar, "L", "//blah\n//blah\n");
    assertEquals(
      """
        [@0,0:13='//blah\\n//blah\\n',<1>,1:0]
        [@1,14:13='<EOF>',<-1>,3:0]
        """, found);
    assertThat(stderrDuringParse).isNull();
  }

  @Test
  @Disabled("Переделать на ANTLR runtime/Generator")
  public void testNonGreedyOptional() {
    String grammar =
      """
        lexer grammar L;
        CMT : '//' .*? '\\n' CMT??;
        WS : (' '|'\\t')+;""";

    String found = execLexer("L.g4", grammar, "L", "//blah\n//blah\n");
    assertEquals(
      """
        [@0,0:6='//blah\\n',<1>,1:0]
        [@1,7:13='//blah\\n',<1>,2:0]
        [@2,14:13='<EOF>',<-1>,3:0]
        """, found);
    assertThat(stderrDuringParse).isNull();
  }

  @Test
  @Disabled("Переделать на ANTLR runtime/Generator")
  public void testGreedyClosure() {
    String grammar =
      "lexer grammar L;\n"
        + "CMT : '//' .*? '\\n' CMT*;\n"
        + "WS : (' '|'\\t')+;";

    String found = execLexer("L.g4", grammar, "L", "//blah\n//blah\n");
    assertEquals(
      "[@0,0:13='//blah\\n//blah\\n',<1>,1:0]\n" +
        "[@1,14:13='<EOF>',<-1>,3:0]\n", found);
    assertThat(stderrDuringParse).isNull();
  }

  @Test
  @Disabled("Переделать на ANTLR runtime/Generator")
  public void testNonGreedyClosure() {
    String grammar =
      "lexer grammar L;\n"
        + "CMT : '//' .*? '\\n' CMT*?;\n"
        + "WS : (' '|'\\t')+;";

    String found = execLexer("L.g4", grammar, "L", "//blah\n//blah\n");
    assertEquals(
      "[@0,0:6='//blah\\n',<1>,1:0]\n" +
        "[@1,7:13='//blah\\n',<1>,2:0]\n" +
        "[@2,14:13='<EOF>',<-1>,3:0]\n", found);
    assertThat(stderrDuringParse).isNull();
  }

  @Test
  @Disabled("Переделать на ANTLR runtime/Generator")
  public void testGreedyPositiveClosure() {
    String grammar =
      "lexer grammar L;\n"
        + "CMT : ('//' .*? '\\n')+;\n"
        + "WS : (' '|'\\t')+;";

    String found = execLexer("L.g4", grammar, "L", "//blah\n//blah\n");
    assertEquals(
      "[@0,0:13='//blah\\n//blah\\n',<1>,1:0]\n" +
        "[@1,14:13='<EOF>',<-1>,3:0]\n", found);
    assertThat(stderrDuringParse).isNull();
  }

  @Test
  @Disabled("Переделать на ANTLR runtime/Generator")
  public void testNonGreedyPositiveClosure() {
    String grammar =
      """
        lexer grammar L;
        CMT : ('//' .*? '\\n')+?;
        WS : (' '|'\\t')+;""";

    String found = execLexer("L.g4", grammar, "L", "//blah\n//blah\n");
    assertEquals(
      """
        [@0,0:6='//blah\\n',<1>,1:0]
        [@1,7:13='//blah\\n',<1>,2:0]
        [@2,14:13='<EOF>',<-1>,3:0]
        """, found);
    assertThat(stderrDuringParse).isNull();
  }

  @Test
  @Disabled("Переделать на ANTLR runtime/Generator")
  public void testRecursiveLexerRuleRefWithWildcardStar1() {
    String grammar =
      """
        lexer grammar L;
        CMT : '/*' (CMT | .)*? '*/' ;
        WS : (' '|'\\n')+ ;
        """
      /*+ "ANY : .;"*/;

    String expecting =
      """
        [@0,0:8='/* ick */',<1>,1:0]
        [@1,9:9='\\n',<2>,1:9]
        [@2,10:34='/* /* */\\n/* /*nested*/ */',<1>,2:0]
        [@3,35:35='\\n',<2>,3:16]
        [@4,36:35='<EOF>',<-1>,4:0]
        """;

    // stuff on end of comment matches another rule
    String found = execLexer("L.g4", grammar, "L",
      """
        /* ick */
        /* /* */
        /* /*nested*/ */
        """);
    assertEquals(expecting, found);
    assertThat(stderrDuringParse).isNull();
  }

  @Test
  @Disabled("Переделать на ANTLR runtime/Generator")
  public void testRecursiveLexerRuleRefWithWildcardStar2() {
    String grammar =
      """
        lexer grammar L;
        CMT : '/*' (CMT | .)*? '*/' ;
        WS : (' '|'\\n')+ ;
        """
      /*+ "ANY : .;"*/;

    // stuff on end of comment doesn't match another rule
    String expecting =
      """
        [@0,0:8='/* ick */',<1>,1:0]
        [@1,10:10='\\n',<2>,1:10]
        [@2,11:36='/* /* */x\\n/* /*nested*/ */',<1>,2:0]
        [@3,38:38='\\n',<2>,3:17]
        [@4,39:38='<EOF>',<-1>,4:0]
        """;
    String found = execLexer("L.g4", grammar, "L",
      """
        /* ick */x
        /* /* */x
        /* /*nested*/ */x
        """);
    assertEquals(expecting, found);
    assertEquals(
      """
        line 1:9 token recognition error at: 'x'
        line 3:16 token recognition error at: 'x'
        """, stderrDuringParse);
  }

  @Test
  @Disabled("Переделать на ANTLR runtime/Generator")
  public void testRecursiveLexerRuleRefWithWildcardPlus1() {
    String grammar =
      """
        lexer grammar L;
        CMT : '/*' (CMT | .)+? '*/' ;
        WS : (' '|'\\n')+ ;
        """
      /*+ "ANY : .;"*/;

    String expecting =
      """
        [@0,0:8='/* ick */',<1>,1:0]
        [@1,9:9='\\n',<2>,1:9]
        [@2,10:34='/* /* */\\n/* /*nested*/ */',<1>,2:0]
        [@3,35:35='\\n',<2>,3:16]
        [@4,36:35='<EOF>',<-1>,4:0]
        """;

    // stuff on end of comment matches another rule
    String found = execLexer("L.g4", grammar, "L",
      """
        /* ick */
        /* /* */
        /* /*nested*/ */
        """);
    assertEquals(expecting, found);
    assertThat(stderrDuringParse).isNull();
  }

  @Test
  @Disabled("Переделать на ANTLR runtime/Generator")
  public void testRecursiveLexerRuleRefWithWildcardPlus2() {
    String grammar =
      """
        lexer grammar L;
        CMT : '/*' (CMT | .)+? '*/' ;
        WS : (' '|'\\n')+ ;
        """
      /*+ "ANY : .;"*/;

    // stuff on end of comment doesn't match another rule
    String expecting =
      """
        [@0,0:8='/* ick */',<1>,1:0]
        [@1,10:10='\\n',<2>,1:10]
        [@2,11:36='/* /* */x\\n/* /*nested*/ */',<1>,2:0]
        [@3,38:38='\\n',<2>,3:17]
        [@4,39:38='<EOF>',<-1>,4:0]
        """;
    String found = execLexer("L.g4", grammar, "L",
      """
        /* ick */x
        /* /* */x
        /* /*nested*/ */x
        """);
    assertEquals(expecting, found);
    assertEquals(
      """
        line 1:9 token recognition error at: 'x'
        line 3:16 token recognition error at: 'x'
        """, stderrDuringParse);
  }

  @Test
  @Disabled("Переделать на ANTLR runtime/Generator")
  public void testActionPlacement() {
    String grammar =
      """
        lexer grammar L;
        I : ({System.out.println("stuff fail: " + getText());} 'a' | {System.out.println("stuff0: " + getText());} 'a' {System.out.println("stuff1: " + getText());} 'b' {System.out.println("stuff2: " + getText());}) {System.out.println(getText());} ;
        WS : (' '|'\\n') -> skip ;
        J : .;
        """;
    String found = execLexer("L.g4", grammar, "L", "ab");
    String expecting =
      """
        stuff0:\s
        stuff1: a
        stuff2: ab
        ab
        [@0,0:1='ab',<1>,1:0]
        [@1,2:1='<EOF>',<-1>,1:2]
        """;
    assertEquals(expecting, found);
  }

  @Test
  @Disabled("Переделать на ANTLR runtime/Generator")
  public void testGreedyConfigs() {
    String grammar =
      """
        lexer grammar L;
        I : ('a' | 'ab') {System.out.println(getText());} ;
        WS : (' '|'\\n') -> skip ;
        J : .;
        """;
    String found = execLexer("L.g4", grammar, "L", "ab");
    String expecting =
      """
        ab
        [@0,0:1='ab',<1>,1:0]
        [@1,2:1='<EOF>',<-1>,1:2]
        """;
    assertEquals(expecting, found);
  }

  @Test
  @Disabled("Переделать на ANTLR runtime/Generator")
  public void testNonGreedyConfigs() {
    String grammar =
      """
        lexer grammar L;
        I : .*? ('a' | 'ab') {System.out.println(getText());} ;
        WS : (' '|'\\n') -> skip ;
        J : . {System.out.println(getText());};
        """;
    String found = execLexer("L.g4", grammar, "L", "ab");
    String expecting =
      """
        a
        b
        [@0,0:0='a',<1>,1:0]
        [@1,1:1='b',<3>,1:1]
        [@2,2:1='<EOF>',<-1>,1:2]
        """;
    assertEquals(expecting, found);
  }

  @Test
  @Disabled("Переделать на ANTLR runtime/Generator")
  public void testKeywordID() {
    // has priority
    String grammar =
      """
        lexer grammar L;
        KEND : 'end' ;
        ID : 'a'..'z'+ ;
        WS : (' '|'\\n')+ ;""";
    String found = execLexer("L.g4", grammar, "L", "end eend ending a");
    String expecting =
      """
        [@0,0:2='end',<1>,1:0]
        [@1,3:3=' ',<3>,1:3]
        [@2,4:7='eend',<2>,1:4]
        [@3,8:8=' ',<3>,1:8]
        [@4,9:14='ending',<2>,1:9]
        [@5,15:15=' ',<3>,1:15]
        [@6,16:16='a',<2>,1:16]
        [@7,17:16='<EOF>',<-1>,1:17]
        """;
    assertEquals(expecting, found);
  }

  @Test
  @Disabled("Переделать на ANTLR runtime/Generator")
  public void testHexVsID() {
    String grammar =
      """
        lexer grammar L;
        HexLiteral : '0' ('x'|'X') HexDigit+ ;
        DecimalLiteral : ('0' | '1'..'9' '0'..'9'*) ;
        FloatingPointLiteral : ('0x' | '0X') HexDigit* ('.' HexDigit*)? ;
        DOT : '.' ;
        ID : 'a'..'z'+ ;
        fragment HexDigit : ('0'..'9'|'a'..'f'|'A'..'F') ;
        WS : (' '|'\\n')+ ;""";
    String found = execLexer("L.g4", grammar, "L", "x 0 1 a.b a.l");
    String expecting =
      """
        [@0,0:0='x',<5>,1:0]
        [@1,1:1=' ',<6>,1:1]
        [@2,2:2='0',<2>,1:2]
        [@3,3:3=' ',<6>,1:3]
        [@4,4:4='1',<2>,1:4]
        [@5,5:5=' ',<6>,1:5]
        [@6,6:6='a',<5>,1:6]
        [@7,7:7='.',<4>,1:7]
        [@8,8:8='b',<5>,1:8]
        [@9,9:9=' ',<6>,1:9]
        [@10,10:10='a',<5>,1:10]
        [@11,11:11='.',<4>,1:11]
        [@12,12:12='l',<5>,1:12]
        [@13,13:12='<EOF>',<-1>,1:13]
        """;
    assertEquals(expecting, found);
  }

  // must get DONE EOF
  @Test
  @Disabled("Переделать на ANTLR runtime/Generator")
  public void testEOFByItself() {
    String grammar =
      """
        lexer grammar L;
        DONE : EOF ;
        A : 'a';
        """;
    String found = execLexer("L.g4", grammar, "L", "");
    String expecting =
      """
        [@0,0:-1='<EOF>',<1>,1:0]
        [@1,0:-1='<EOF>',<-1>,1:0]
        """;
    assertEquals(expecting, found);
  }

  @Test
  @Disabled("Переделать на ANTLR runtime/Generator")
  public void testEOFSuffixInFirstRule() {
    String grammar =
      """
        lexer grammar L;
        A : 'a' EOF ;
        B : 'a';
        C : 'c';
        """;
    String found = execLexer("L.g4", grammar, "L", "");
    String expecting =
      "[@0,0:-1='<EOF>',<-1>,1:0]\n";
    assertEquals(expecting, found);

    found = execLexer("L.g4", grammar, "L", "a");
    expecting =
      """
        [@0,0:0='a',<1>,1:0]
        [@1,1:0='<EOF>',<-1>,1:1]
        """;
    assertEquals(expecting, found);
  }

  @Test
  @Disabled("Переделать на ANTLR runtime/Generator")
  public void testCharSet() {
    String grammar =
      """
        lexer grammar L;
        I : '0'..'9'+ {System.out.println("I");} ;
        WS : [ \\n\\u000D] -> skip ;""";
    String found = execLexer("L.g4", grammar, "L", "34\r\n 34");
    String expecting =
      """
        I
        I
        [@0,0:1='34',<1>,1:0]
        [@1,5:6='34',<1>,2:1]
        [@2,7:6='<EOF>',<-1>,2:3]
        """;
    assertEquals(expecting, found);
  }

  @Test
  @Disabled("Переделать на ANTLR runtime/Generator")
  public void testCharSetPlus() {
    String grammar =
      """
        lexer grammar L;
        I : '0'..'9'+ {System.out.println("I");} ;
        WS : [ \\n\\u000D]+ -> skip ;""";
    String found = execLexer("L.g4", grammar, "L", "34\r\n 34");
    String expecting =
      """
        I
        I
        [@0,0:1='34',<1>,1:0]
        [@1,5:6='34',<1>,2:1]
        [@2,7:6='<EOF>',<-1>,2:3]
        """;
    assertEquals(expecting, found);
  }

  @Test
  @Disabled("Переделать на ANTLR runtime/Generator")
  public void testCharSetNot() {
    String grammar =
      """
        lexer grammar L;
        I : ~[ab \\n] ~[ \\ncd]* {System.out.println("I");} ;
        WS : [ \\n\\u000D]+ -> skip ;""";
    String found = execLexer("L.g4", grammar, "L", "xaf");
    String expecting =
      """
        I
        [@0,0:2='xaf',<1>,1:0]
        [@1,3:2='<EOF>',<-1>,1:3]
        """;
    assertEquals(expecting, found);
  }

  @Test
  @Disabled("Переделать на ANTLR runtime/Generator")
  public void testCharSetInSet() {
    String grammar =
      """
        lexer grammar L;
        I : (~[ab \\n]|'a') {System.out.println("I");} ;
        WS : [ \\n\\u000D]+ -> skip ;""";
    String found = execLexer("L.g4", grammar, "L", "a x");
    String expecting =
      """
        I
        I
        [@0,0:0='a',<1>,1:0]
        [@1,2:2='x',<1>,1:2]
        [@2,3:2='<EOF>',<-1>,1:3]
        """;
    assertEquals(expecting, found);
  }

  @Test
  @Disabled("Переделать на ANTLR runtime/Generator")
  public void testCharSetRange() {
    String grammar =
      """
        lexer grammar L;
        I : [0-9]+ {System.out.println("I");} ;
        ID : [a-zA-Z] [a-zA-Z0-9]* {System.out.println("ID");} ;
        WS : [ \\n\\u0009\\r]+ -> skip ;""";
    String found = execLexer("L.g4", grammar, "L", "34\r 34 a2 abc \n   ");
    String expecting =
      """
        I
        I
        ID
        ID
        [@0,0:1='34',<1>,1:0]
        [@1,4:5='34',<1>,1:4]
        [@2,7:8='a2',<2>,1:7]
        [@3,10:12='abc',<2>,1:10]
        [@4,18:17='<EOF>',<-1>,2:3]
        """;
    assertEquals(expecting, found);
  }

  @Test
  @Disabled("Переделать на ANTLR runtime/Generator")
  public void testCharSetWithMissingEndRange() {
    String grammar =
      """
        lexer grammar L;
        I : [0-]+ {System.out.println("I");} ;
        WS : [ \\n\\u000D]+ -> skip ;""";
    String found = execLexer("L.g4", grammar, "L", "00\r\n");
    String expecting =
      """
        I
        [@0,0:1='00',<1>,1:0]
        [@1,4:3='<EOF>',<-1>,2:0]
        """;
    assertEquals(expecting, found);
  }

  @Test
  @Disabled("Переделать на ANTLR runtime/Generator")
  public void testCharSetWithMissingEscapeChar() {
    String grammar =
      """
        lexer grammar L;
        I : [0-9]+ {System.out.println("I");} ;
        WS : [ \\n]+ -> skip ;""";
    String found = execLexer("L.g4", grammar, "L", "34 ");
    String expecting =
      """
        I
        [@0,0:1='34',<1>,1:0]
        [@1,3:2='<EOF>',<-1>,1:3]
        """;
    assertEquals(expecting, found);
  }

  @Test
  @Disabled("Переделать на ANTLR runtime/Generator")
  public void testCharSetWithEscapedChar() {
    String grammar =
      """
        lexer grammar L;
        DASHBRACK : [\\-\\]]+ {System.out.println("DASHBRACK");} ;
        WS : [ \\n]+ -> skip ;""";
    String found = execLexer("L.g4", grammar, "L", "- ] ");
    String expecting =
      """
        DASHBRACK
        DASHBRACK
        [@0,0:0='-',<1>,1:0]
        [@1,2:2=']',<1>,1:2]
        [@2,4:3='<EOF>',<-1>,1:4]
        """;
    assertEquals(expecting, found);
  }

  @Test
  @Disabled("Переделать на ANTLR runtime/Generator")
  public void testCharSetWithQuote() {
    String grammar =
      """
        lexer grammar L;
        A : ["a-z]+ {System.out.println("A");} ;
        WS : [ \\n\\t]+ -> skip ;""";
    String found = execLexer("L.g4", grammar, "L", "b\"a");
    String expecting =
      """
        A
        [@0,0:2='b"a',<1>,1:0]
        [@1,3:2='<EOF>',<-1>,1:3]
        """;
    assertEquals(expecting, found);
  }

  @Test
  @Disabled("Переделать на ANTLR runtime/Generator")
  public void testCharSetWithQuote2() {
    String grammar =
      """
        lexer grammar L;
        A : ["\\\\ab]+ {System.out.println("A");} ;
        WS : [ \\n\\t]+ -> skip ;""";
    String found = execLexer("L.g4", grammar, "L", "b\"\\a");
    String expecting =
      """
        A
        [@0,0:3='b"\\a',<1>,1:0]
        [@1,4:3='<EOF>',<-1>,1:4]
        """;
    assertEquals(expecting, found);
  }

  @Test
  @Disabled("Переделать на ANTLR runtime/Generator")
  public void testPositionAdjustingLexer() throws IOException {
    String grammar = load("PositionAdjustingLexer.g4", null);
    String input =
      """
        tokens
        tokens {
        notLabel
        label1 =
        label2 +=
        notLabel
        """;
    String found = execLexer("PositionAdjustingLexer.g4", grammar, "PositionAdjustingLexer", input);

    final int TOKENS = 4;
    final int LABEL = 5;
    final int IDENTIFIER = 6;
    String expecting =
      "[@0,0:5='tokens',<" + IDENTIFIER + ">,1:0]\n" +
        "[@1,7:12='tokens',<" + TOKENS + ">,2:0]\n" +
        "[@2,14:14='{',<3>,2:7]\n" +
        "[@3,16:23='notLabel',<" + IDENTIFIER + ">,3:0]\n" +
        "[@4,25:30='label1',<" + LABEL + ">,4:0]\n" +
        "[@5,32:32='=',<1>,4:7]\n" +
        "[@6,34:39='label2',<" + LABEL + ">,5:0]\n" +
        "[@7,41:42='+=',<2>,5:7]\n" +
        "[@8,44:51='notLabel',<" + IDENTIFIER + ">,6:0]\n" +
        "[@9,53:52='<EOF>',<-1>,7:0]\n";

    assertEquals(expecting, found);
  }

  /**
   * This is a regression test for antlr/antlr4#76 "Serialized ATN strings
   * should be split when longer than 2^16 bytes (class file limitation)"
   * <a href="https://github.com/antlr/antlr4/issues/76">...</a>
   */
  @Test
  @Disabled("Переделать на ANTLR runtime/Generator")
  public void testLargeLexer() {
    StringBuilder grammar = new StringBuilder();
    grammar.append("lexer grammar L;\n");
    grammar.append("WS : [ \\t\\r\\n]+ -> skip;\n");
    for (int i = 0; i < 4000; i++) {
      grammar.append("KW").append(i).append(" : 'KW' '").append(i).append("';\n");
    }

    String input = "KW400";
    String found = execLexer("L.g4", grammar.toString(), "L", input);
    String expecting =
      """
        [@0,0:4='KW400',<402>,1:0]
        [@1,5:4='<EOF>',<-1>,1:5]
        """;
    assertEquals(expecting, found);
  }

  /**
   * This is a regression test for antlr/antlr4#687 "Empty zero-length tokens
   * cannot have lexer commands" and antlr/antlr4#688 "Lexer cannot match
   * zero-length tokens"
   * <a href="https://github.com/antlr/antlr4/issues/687">...</a>
   * <a href="https://github.com/antlr/antlr4/issues/688">...</a>
   */
  @Test
  @Disabled("Переделать на ANTLR runtime/Generator")
  public void testZeroLengthToken() {
    String grammar =
      """
        lexer grammar L;
        
        BeginString
        	:	'\\'' -> more, pushMode(StringMode)
        	;
        
        mode StringMode;
        
        	StringMode_X : 'x' -> more;
        	StringMode_Done : -> more, mode(EndStringMode);
        
        mode EndStringMode;	
        
        	EndString : '\\'' -> popMode;
        """;
    String found = execLexer("L.g4", grammar, "L", "'xxx'");
    String expecting =
      """
        [@0,0:4=''xxx'',<1>,1:0]
        [@1,5:4='<EOF>',<-1>,1:5]
        """;
    assertEquals(expecting, found);
  }
}
