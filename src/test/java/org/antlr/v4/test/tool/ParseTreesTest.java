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

public class ParseTreesTest extends AbstractBaseTest {
  @Test
  @Disabled("Переделать на ANTLR runtime/Generator")
  public void testTokenAndRuleContextString() {
    String grammar =
      """
        grammar T;
        s
        @init {setBuildParseTree(true);}
        @after {System.out.println($r.ctx.toStringTree(this));}
          :r=a ;
        a : 'x' {System.out.println(getRuleInvocationStack());} ;
        """;
    String result = execParser("T.g4", grammar, "TParser", "TLexer", "s", "x", false);
    String expecting = "[a, s]\n(a x)\n";
    assertEquals(expecting, result);
  }

  @Test
  @Disabled("Переделать на ANTLR runtime/Generator")
  public void testToken2() {
    String grammar =
      """
        grammar T;
        s
        @init {setBuildParseTree(true);}
        @after {System.out.println($r.ctx.toStringTree(this));}
          :r=a ;
        a : 'x' 'y'
          ;
        """;
    String result = execParser("T.g4", grammar, "TParser", "TLexer", "s", "xy", false);
    String expecting = "(a x y)\n";
    assertEquals(expecting, result);
  }

  @Test
  @Disabled("Переделать на ANTLR runtime/Generator")
  public void test2Alts() {
    String grammar =
      """
        grammar T;
        s
        @init {setBuildParseTree(true);}
        @after {System.out.println($r.ctx.toStringTree(this));}
          :r=a ;
        a : 'x' | 'y'
          ;
        """;
    String result = execParser("T.g4", grammar, "TParser", "TLexer", "s", "y", false);
    String expecting = "(a y)\n";
    assertEquals(expecting, result);
  }

  @Test
  @Disabled("Переделать на ANTLR runtime/Generator")
  public void test2AltLoop() {
    String grammar =
      """
        grammar T;
        s
        @init {setBuildParseTree(true);}
        @after {System.out.println($r.ctx.toStringTree(this));}
          :r=a ;
        a : ('x' | 'y')* 'z'
          ;
        """;
    String result = execParser("T.g4", grammar, "TParser", "TLexer", "s", "xyyxyxz", false);
    String expecting = "(a x y y x y x z)\n";
    assertEquals(expecting, result);
  }

  @Test
  @Disabled("Переделать на ANTLR runtime/Generator")
  public void testRuleRef() {
    String grammar =
      """
        grammar T;
        s
        @init {setBuildParseTree(true);}
        @after {System.out.println($r.ctx.toStringTree(this));}
          : r=a ;
        a : b 'x'
          ;
        b : 'y' ;
        """;
    String result = execParser("T.g4", grammar, "TParser", "TLexer", "s", "yx", false);
    String expecting = "(a (b y) x)\n";
    assertEquals(expecting, result);
  }

  // ERRORS

  @Test
  @Disabled("Переделать на ANTLR runtime/Generator")
  public void testExtraToken() {
    String grammar =
      """
        grammar T;
        s
        @init {setBuildParseTree(true);}
        @after {System.out.println($r.ctx.toStringTree(this));}
          : r=a ;
        a : 'x' 'y'
          ;
        Z : 'z';\s
        """;
    String result = execParser("T.g4", grammar, "TParser", "TLexer", "s", "xzy", false);
    String expecting = "(a x z y)\n"; // ERRORs not shown. z is colored red in tree view
    assertEquals(expecting, result);
  }

  @Test
  @Disabled("Переделать на ANTLR runtime/Generator")
  public void testNoViableAlt() {
    String grammar =
      """
        grammar T;
        s
        @init {setBuildParseTree(true);}
        @after {System.out.println($r.ctx.toStringTree(this));}
          : r=a ;
        a : 'x' | 'y'
          ;
        Z : 'z';\s
        """;
    String result = execParser("T.g4", grammar, "TParser", "TLexer", "s", "z", false);
    String expecting = "(a z)\n";
    assertEquals(expecting, result);
  }

  @Test
  @Disabled("Переделать на ANTLR runtime/Generator")
  public void testSync() {
    String grammar =
      """
        grammar T;
        s
        @init {setBuildParseTree(true);}
        @after {System.out.println($r.ctx.toStringTree(this));}
          : r=a ;
        a : 'x' 'y'* '!'
          ;
        Z : 'z';\s
        """;
    String result = execParser("T.g4", grammar, "TParser", "TLexer", "s", "xzyy!", false);
    String expecting = "(a x z y y !)\n";
    assertEquals(expecting, result);
  }
}
