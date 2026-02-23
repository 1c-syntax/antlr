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

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ParseTreesTest extends AbstractBaseTest {
  @Test
  void testTokenAndRuleContextString() {
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
    assertThat(result).isEqualTo(expecting);
  }

  @Test
  void testToken2() {
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
    assertThat(result).isEqualTo(expecting);
  }

  @Test
  void test2Alts() {
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
    assertThat(result).isEqualTo(expecting);
  }

  @Test
  void test2AltLoop() {
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
    assertThat(result).isEqualTo(expecting);
  }

  @Test
  void testRuleRef() {
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
    assertThat(result).isEqualTo(expecting);
  }

  // ERRORS

  @Test
  void testExtraToken() {
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
    assertThat(result).isEqualTo(expecting);
  }

  @Test
  void testNoViableAlt() {
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
    assertThat(result).isEqualTo(expecting);
  }

  @Test
  void testSync() {
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
    assertThat(result).isEqualTo(expecting);
  }
}
