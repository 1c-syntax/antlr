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

import static org.antlr.v4.TestUtils.assertEquals;

@Disabled("Переделать на ANTLR runtime/Generator")
public class SemPredEvalLexerTest extends AbstractBaseTest {

  @Test
  public void testDisableRule() {
    // winner not E1 or ID
    String grammar =
      """
        lexer grammar L;
        E1 : 'enum' {false}? ;
        E2 : 'enum' {true}? ;
        ID : 'a'..'z'+ ;
        WS : (' '|'\\n') -> skip ;""";
    String found = execLexer("L.g4", grammar, "L", "enum abc", true);
    String expecting =
      """
        [@0,0:3='enum',<2>,1:0]
        [@1,5:7='abc',<3>,1:5]
        [@2,8:7='<EOF>',<-1>,1:8]
        s0-' '->:s5=>4
        s0-'a'->:s6=>3
        s0-'e'->:s1=>3
        :s1=>3-'n'->:s2=>3
        :s2=>3-'u'->:s3=>3
        :s6=>3-'b'->:s6=>3
        :s6=>3-'c'->:s6=>3
        """;
    assertEquals(expecting, found);
  }

  @Test
  public void testIDvsEnum() {
    String grammar =
      """
        lexer grammar L;
        ENUM : 'enum' {false}? ;
        ID : 'a'..'z'+ ;
        WS : (' '|'\\n') -> skip ;""";
    String found = execLexer("L.g4", grammar, "L", "enum abc enum", true);
    String expecting =
      """
        [@0,0:3='enum',<2>,1:0]
        [@1,5:7='abc',<2>,1:5]
        [@2,9:12='enum',<2>,1:9]
        [@3,13:12='<EOF>',<-1>,1:13]
        s0-' '->:s5=>3
        s0-'a'->:s4=>2
        s0-'e'->:s1=>2
        :s1=>2-'n'->:s2=>2
        :s2=>2-'u'->:s3=>2
        :s4=>2-'b'->:s4=>2
        :s4=>2-'c'->:s4=>2
        """; // no 'm'-> transition...conflicts with pred
    assertEquals(expecting, found);
  }

  @Test
  public void testIDnotEnum() {
    String grammar =
      """
        lexer grammar L;
        ENUM : [a-z]+ {false}? ;
        ID   : [a-z]+ ;
        WS : (' '|'\\n') -> skip ;""";
    String found = execLexer("L.g4", grammar, "L", "enum abc enum", true);
    String expecting =
      """
        [@0,0:3='enum',<2>,1:0]
        [@1,5:7='abc',<2>,1:5]
        [@2,9:12='enum',<2>,1:9]
        [@3,13:12='<EOF>',<-1>,1:13]
        s0-' '->:s2=>3
        """; // no edges in DFA for enum/id. all paths lead to pred.
    assertEquals(expecting, found);
  }

  @Test
  public void testEnumNotID() {
    String grammar =
      """
        lexer grammar L;
        ENUM : [a-z]+ {getText().equals("enum")}? ;
        ID   : [a-z]+ ;
        WS : (' '|'\\n') -> skip ;""";
    String found = execLexer("L.g4", grammar, "L", "enum abc enum", true);
    String expecting =
      """
        [@0,0:3='enum',<1>,1:0]
        [@1,5:7='abc',<2>,1:5]
        [@2,9:12='enum',<1>,1:9]
        [@3,13:12='<EOF>',<-1>,1:13]
        s0-' '->:s3=>3
        """; // no edges in DFA for enum/id. all paths lead to pred.
    assertEquals(expecting, found);
  }

  @Test
  public void testIndent() {
    String grammar =
      """
        lexer grammar L;
        ID : [a-z]+ ;
        INDENT : [ \\t]+ {_tokenStartCharPositionInLine==0}?\s
                 {System.out.println("INDENT");}  ;\
        NL     : '\\n' ;\
        WS     : [ \\t]+ ;""";
    String found = execLexer("L.g4", grammar, "L", "abc\n  def  \n", true);
    // action output
    // ID
    // NL
    // INDENT
    // ID
    // WS
    String expecting =
      """
        INDENT
        [@0,0:2='abc',<1>,1:0]
        [@1,3:3='\\n',<3>,1:3]
        [@2,4:5='  ',<2>,2:0]
        [@3,6:8='def',<1>,2:2]
        [@4,9:10='  ',<4>,2:5]
        [@5,11:11='\\n',<3>,2:7]
        [@6,12:11='<EOF>',<-1>,3:0]
        s0-'
        '->:s2=>3
        s0-'a'->:s1=>1
        s0-'d'->:s1=>1
        :s1=>1-'b'->:s1=>1
        :s1=>1-'c'->:s1=>1
        :s1=>1-'e'->:s1=>1
        :s1=>1-'f'->:s1=>1
        """;
    assertEquals(expecting, found);
  }

  @Test
  public void testLexerInputPositionSensitivePredicates() {
    String grammar =
      """
        lexer grammar L;
        WORD1 : ID1+ {System.out.println(getText());} ;
        WORD2 : ID2+ {System.out.println(getText());} ;
        fragment ID1 : {getCharPositionInLine()<2}? [a-zA-Z];
        fragment ID2 : {getCharPositionInLine()>=2}? [a-zA-Z];
        WS : (' '|'\\n') -> skip;
        """;
    String found = execLexer("L.g4", grammar, "L", "a cde\nabcde\n");
    String expecting =
      """
        a
        cde
        ab
        cde
        [@0,0:0='a',<1>,1:0]
        [@1,2:4='cde',<2>,1:2]
        [@2,6:7='ab',<1>,2:0]
        [@3,8:10='cde',<2>,2:2]
        [@4,12:11='<EOF>',<-1>,3:0]
        """;
    assertEquals(expecting, found);
  }

  @Test
  public void testPredicatedKeywords() {
    String grammar =
      """
        lexer grammar A;
        ENUM : [a-z]+ {getText().equals("enum")}? {System.out.println("enum!");} ;
        ID   : [a-z]+ {System.out.println("ID "+getText());} ;
        WS   : [ \\n] -> skip ;""";
    String found = execLexer("A.g4", grammar, "A", "enum enu a");
    String expecting =
      """
        enum!
        ID enu
        ID a
        [@0,0:3='enum',<1>,1:0]
        [@1,5:7='enu',<2>,1:5]
        [@2,9:9='a',<2>,1:9]
        [@3,10:9='<EOF>',<-1>,1:10]
        """;
    assertEquals(expecting, found);
  }
}
