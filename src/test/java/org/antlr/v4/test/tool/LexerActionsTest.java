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

public class LexerActionsTest extends AbstractBaseTest {
  // ----- ACTIONS --------------------------------------------------------

  @Test
  @Disabled("Переделать на ANTLR runtime/Generator")
  public void testActionExecutedInDFA() {
    String grammar =
      """
        lexer grammar L;
        I : '0'..'9'+ {System.out.println("I");} ;
        WS : (' '|'\\n') -> skip ;""";
    String found = execLexer("L.g4", grammar, "L", "34 34");
    String expecting =
      """
        I
        I
        [@0,0:1='34',<1>,1:0]
        [@1,3:4='34',<1>,1:3]
        [@2,5:4='<EOF>',<-1>,1:5]
        """;
    assertEquals(expecting, found);
  }

  @Test
  @Disabled("Переделать на ANTLR runtime/Generator")
  public void testActionEvalsAtCorrectIndex() {
    String grammar =
      """
        lexer grammar L;
        I : [0-9] {System.out.println("2nd char: "+(char)_input.LA(1));} [0-9]+ ;
        WS : (' '|'\\n') -> skip ;""";
    String found = execLexer("L.g4", grammar, "L", "123 45");
    String expecting =
      """
        2nd char: 2
        2nd char: 5
        [@0,0:2='123',<1>,1:0]
        [@1,4:5='45',<1>,1:4]
        [@2,6:5='<EOF>',<-1>,1:6]
        """;
    assertEquals(expecting, found);
  }

  /**
   * This is a regressing test for antlr/antlr4#469 "Not all internal lexer
   * rule actions are executed".
   * <a href="https://github.com/antlr/antlr4/issues/469">...</a>
   */
  @Test
  @Disabled("Переделать на ANTLR runtime/Generator")
  public void testEvalMultipleActions() {
    String grammar =
      """
        lexer grammar L;
        
        @lexer::members
        {
        class Marker
        {
           Marker (Lexer lexer) { this.lexer = lexer; }
        
           public String getText ()
           {
              return lexer._input.getText (new Interval (start_index, stop_index));
           }
        
           public void start ()  { start_index = lexer._input.index (); System.out.println ("Start:" + start_index);}
           public void stop () { stop_index = lexer._input.index (); System.out.println ("Stop:" + stop_index);}
        
           private int start_index = 0;
           private int stop_index = 0;
           private Lexer lexer;
        }
        
        Marker m_name = new Marker (this);
        }
        
        HELLO: 'hello' WS { m_name.start (); } NAME { m_name.stop (); } '\\n' { System.out.println ("Hello: " + m_name.getText ()); };
        NAME: ('a'..'z' | 'A'..'Z')+ ('\\n')?;
        
        fragment WS: [ \\r\\t\\n]+ ;
        """;
    String found = execLexer("L.g4", grammar, "L", "hello Steve\n");
    String expecting =
      """
        Start:6
        Stop:11
        Hello: Steve
        
        [@0,0:11='hello Steve\\n',<1>,1:0]
        [@1,12:11='<EOF>',<-1>,2:0]
        """;
    assertEquals(expecting, found);
  }

  @Test
  @Disabled("Переделать на ANTLR runtime/Generator")
  public void test2ActionsIn1Rule() {
    String grammar =
      """
        lexer grammar L;
        I : [0-9] {System.out.println("x");} [0-9]+ {System.out.println("y");} ;
        WS : (' '|'\\n') -> skip ;""";
    String found = execLexer("L.g4", grammar, "L", "123 45");
    String expecting =
      """
        x
        y
        x
        y
        [@0,0:2='123',<1>,1:0]
        [@1,4:5='45',<1>,1:4]
        [@2,6:5='<EOF>',<-1>,1:6]
        """;
    assertEquals(expecting, found);
  }

  @Test
  @Disabled("Переделать на ANTLR runtime/Generator")
  public void testAltActionsIn1Rule() {
    String grammar =
      """
        lexer grammar L;
        I : ( [0-9]+ {System.out.print("int");}
            | [a-z]+ {System.out.print("id");}
            )
            {System.out.println(" last");}
            ;
        WS : (' '|'\\n') -> skip ;""";
    String found = execLexer("L.g4", grammar, "L", "123 ab");
    String expecting =
      """
        int last
        id last
        [@0,0:2='123',<1>,1:0]
        [@1,4:5='ab',<1>,1:4]
        [@2,6:5='<EOF>',<-1>,1:6]
        """;
    assertEquals(expecting, found);
  }

  @Test
  @Disabled("Переделать на ANTLR runtime/Generator")
  public void testActionPlusCommand() {
    String grammar =
      """
        lexer grammar L;
        I : '0'..'9'+ {System.out.println("I");} -> skip ;
        WS : (' '|'\\n') -> skip ;""";
    String found = execLexer("L.g4", grammar, "L", "34 34");
    String expecting =
      """
        I
        I
        [@0,5:4='<EOF>',<-1>,1:5]
        """;
    assertEquals(expecting, found);
  }

  // ----- COMMANDS --------------------------------------------------------

  @Test
  @Disabled("Переделать на ANTLR runtime/Generator")
  public void testSkipCommand() {
    String grammar =
      """
        lexer grammar L;
        I : '0'..'9'+ {System.out.println("I");} ;
        WS : (' '|'\\n') -> skip ;""";
    String found = execLexer("L.g4", grammar, "L", "34 34");
    String expecting =
      """
        I
        I
        [@0,0:1='34',<1>,1:0]
        [@1,3:4='34',<1>,1:3]
        [@2,5:4='<EOF>',<-1>,1:5]
        """;
    assertEquals(expecting, found);
  }

  @Test
  @Disabled("Переделать на ANTLR runtime/Generator")
  public void testMoreCommand() {
    String grammar =
      """
        lexer grammar L;
        I : '0'..'9'+ {System.out.println("I");} ;
        WS : '#' -> more ;""";
    String found = execLexer("L.g4", grammar, "L", "34#10");
    String expecting =
      """
        I
        I
        [@0,0:1='34',<1>,1:0]
        [@1,2:4='#10',<1>,1:2]
        [@2,5:4='<EOF>',<-1>,1:5]
        """;
    assertEquals(expecting, found);
  }

  @Test
  @Disabled("Переделать на ANTLR runtime/Generator")
  public void testTypeCommand() {
    String grammar =
      """
        lexer grammar L;
        I : '0'..'9'+ {System.out.println("I");} ;
        HASH : '#' -> type(HASH) ;""";
    String found = execLexer("L.g4", grammar, "L", "34#");
    String expecting =
      """
        I
        [@0,0:1='34',<1>,1:0]
        [@1,2:2='#',<2>,1:2]
        [@2,3:2='<EOF>',<-1>,1:3]
        """;
    assertEquals(expecting, found);
  }

  @Test
  @Disabled("Переделать на ANTLR runtime/Generator")
  public void testCombinedCommand() {
    String grammar =
      """
        lexer grammar L;
        I : '0'..'9'+ {System.out.println("I");} ;
        HASH : '#' -> type(100), skip, more  ;""";
    String found = execLexer("L.g4", grammar, "L", "34#11");
    String expecting =
      """
        I
        I
        [@0,0:1='34',<1>,1:0]
        [@1,2:4='#11',<1>,1:2]
        [@2,5:4='<EOF>',<-1>,1:5]
        """;
    assertEquals(expecting, found);
  }

  @Test
  @Disabled("Переделать на ANTLR runtime/Generator")
  public void testLexerMode() {
    String grammar =
      """
        lexer grammar L;
        STRING_START : '"' -> pushMode(STRING_MODE), more;
        WS : (' '|'\\n') -> skip ;
        mode STRING_MODE;
        STRING : '"' -> popMode;
        ANY : . -> more;
        """;
    String found = execLexer("L.g4", grammar, "L", "\"abc\" \"ab\"");
    String expecting =
      """
        [@0,0:4='"abc"',<2>,1:0]
        [@1,6:9='"ab"',<2>,1:6]
        [@2,10:9='<EOF>',<-1>,1:10]
        """;
    assertEquals(expecting, found);
  }

  @Test
  @Disabled("Переделать на ANTLR runtime/Generator")
  public void testLexerPushPopModeAction() {
    // token type 2
    String grammar =
      """
        lexer grammar L;
        STRING_START : '"' -> pushMode(STRING_MODE), more ;
        WS : (' '|'\\n') -> skip ;
        mode STRING_MODE;
        STRING : '"' -> popMode ;
        ANY : . -> more ;
        """;
    String found = execLexer("L.g4", grammar, "L", "\"abc\" \"ab\"");
    String expecting =
      """
        [@0,0:4='"abc"',<2>,1:0]
        [@1,6:9='"ab"',<2>,1:6]
        [@2,10:9='<EOF>',<-1>,1:10]
        """;
    assertEquals(expecting, found);
  }

  @Test
  @Disabled("Переделать на ANTLR runtime/Generator")
  public void testLexerModeAction() {
    // ttype 2 since '"' ambiguity
    String grammar =
      """
        lexer grammar L;
        STRING_START : '"' -> mode(STRING_MODE), more ;
        WS : (' '|'\\n') -> skip ;
        mode STRING_MODE;
        STRING : '"' -> mode(DEFAULT_MODE) ;
        ANY : . -> more ;
        """;
    String found = execLexer("L.g4", grammar, "L", "\"abc\" \"ab\"");
    String expecting =
      """
        [@0,0:4='"abc"',<2>,1:0]
        [@1,6:9='"ab"',<2>,1:6]
        [@2,10:9='<EOF>',<-1>,1:10]
        """;
    assertEquals(expecting, found);
  }

  // ----- PREDICATES --------------------------------------------------------

  /**
   * This is a regression test for antlr/antlr4#398 "Lexer: literal matches
   * while negated char set fail to match"
   * <a href="https://github.com/antlr/antlr4/issues/398">...</a>
   */
  @Test
  @Disabled("Переделать на ANTLR runtime/Generator")
  public void testFailingPredicateEvalIsNotCached() {
    String grammar =
      """
        lexer grammar TestLexer;
        
        fragment WS: [ \\t]+;
        fragment EOL: '\\r'? '\\n';
        
        LINE: WS? ~[\\r\\n]* EOL { !getText().trim().startsWith("Item:") }?;
        ITEM: WS? 'Item:' -> pushMode(ITEM_HEADING_MODE);
        
        mode ITEM_HEADING_MODE;
        
        NAME: ~[\\r\\n]+;
        SECTION_HEADING_END: EOL -> popMode;
        """;
    String input =
      """
        A line here.
        Item: name of item
        Another line.
        More line.
        """;
    String found = execLexer("TestLexer.g4", grammar, "TestLexer", input);
    String expecting =
      """
        [@0,0:12='A line here.\\n',<1>,1:0]
        [@1,13:17='Item:',<2>,2:0]
        [@2,18:30=' name of item',<3>,2:5]
        [@3,31:31='\\n',<4>,2:18]
        [@4,32:45='Another line.\\n',<1>,3:0]
        [@5,46:56='More line.\\n',<1>,4:0]
        [@6,57:56='<EOF>',<-1>,5:0]
        """;
    assertEquals(expecting, found);
  }

}
