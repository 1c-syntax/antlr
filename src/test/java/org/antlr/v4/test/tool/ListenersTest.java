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

public class ListenersTest extends AbstractBaseTest {
  @Test
  @Disabled("Переделать на ANTLR runtime/Generator")
  public void testBasic() {
    String grammar =
      """
        grammar T;
        @header {import org.antlr.v4.runtime.tree.TerminalNode;}
        @parser::members {
        public static class LeafListener extends TBaseListener {
            public void visitTerminal(TerminalNode node) {
              System.out.println(node.getSymbol().getText());
            }
          }}
        s
        @after {\
          System.out.println($r.ctx.toStringTree(this));\
          ParseTreeWalker walker = new ParseTreeWalker();
          walker.walk(new LeafListener(), $r.ctx);\
        }
          : r=a ;
        a : INT INT\
          | ID\
          ;
        MULT: '*' ;
        ADD : '+' ;
        INT : [0-9]+ ;
        ID  : [a-z]+ ;
        WS : [ \\t\\n]+ -> skip ;
        """;
    String result = execParser("T.g4", grammar, "TParser", "TLexer", "s", "1 2", false);
    String expecting = """
      (a 1 2)
      1
      2
      """;
    assertEquals(expecting, result);
  }

  @Test
  @Disabled("Переделать на ANTLR runtime/Generator")
  public void testTokenGetters() {
    String grammar =
      """
        grammar T;
        @parser::members {
        public static class LeafListener extends TBaseListener {
            public void exitA(TParser.AContext ctx) {
              if (ctx.getChildCount()==2) System.out.printf("%s %s %s",ctx.INT(0).getSymbol().getText(),ctx.INT(1).getSymbol().getText(),ctx.INT());
              else System.out.println(ctx.ID().getSymbol());
            }
          }}
        s
        @after {\
          System.out.println($r.ctx.toStringTree(this));\
          ParseTreeWalker walker = new ParseTreeWalker();
          walker.walk(new LeafListener(), $r.ctx);\
        }
          : r=a ;
        a : INT INT\
          | ID\
          ;
        MULT: '*' ;
        ADD : '+' ;
        INT : [0-9]+ ;
        ID  : [a-z]+ ;
        WS : [ \\t\\n]+ -> skip ;
        """;
    String result = execParser("T.g4", grammar, "TParser", "TLexer", "s", "1 2", false);
    String expecting =
      """
        (a 1 2)
        1 2 [1, 2]
        """;
    assertEquals(expecting, result);

    result = execParser("T.g4", grammar, "TParser", "TLexer", "s", "abc", false);
    expecting = """
      (a abc)
      [@0,0:2='abc',<4>,1:0]
      """;
    assertEquals(expecting, result);
  }

  @Test
  @Disabled("Переделать на ANTLR runtime/Generator")
  public void testRuleGetters() {
    // forces list
    // a list still
    String grammar =
      """
        grammar T;
        @parser::members {
        public static class LeafListener extends TBaseListener {
            public void exitA(TParser.AContext ctx) {
              if (ctx.getChildCount()==2) {
                System.out.printf("%s %s %s",ctx.b(0).start.getText(),
                                  ctx.b(1).start.getText(),ctx.b().get(0).start.getText());
              }
              else System.out.println(ctx.b(0).start.getText());
            }
          }}
        s
        @after {\
          System.out.println($r.ctx.toStringTree(this));\
          ParseTreeWalker walker = new ParseTreeWalker();
          walker.walk(new LeafListener(), $r.ctx);\
        }
          : r=a ;
        a : b b\
          | b\
          ;
        b : ID | INT ;
        MULT: '*' ;
        ADD : '+' ;
        INT : [0-9]+ ;
        ID  : [a-z]+ ;
        WS : [ \\t\\n]+ -> skip ;
        """;
    String result = execParser("T.g4", grammar, "TParser", "TLexer", "s", "1 2", false);
    String expecting = """
      (a (b 1) (b 2))
      1 2 1
      """;
    assertEquals(expecting, result);

    result = execParser("T.g4", grammar, "TParser", "TLexer", "s", "abc", false);
    expecting = """
      (a (b abc))
      abc
      """;
    assertEquals(expecting, result);
  }

  @Test
  @Disabled("Переделать на ANTLR runtime/Generator")
  public void testLR() {
    String grammar =
      """
        grammar T;
        @parser::members {
        public static class LeafListener extends TBaseListener {
            public void exitE(TParser.EContext ctx) {
              if (ctx.getChildCount()==3) {
                System.out.printf("%s %s %s\\n",ctx.e(0).start.getText(),
                                  ctx.e(1).start.getText(),\
                                  ctx.e().get(0).start.getText());
              }
              else System.out.println(ctx.INT().getSymbol().getText());
            }
          }\
        }
        s
        @after {\
          System.out.println($r.ctx.toStringTree(this));\
          ParseTreeWalker walker = new ParseTreeWalker();
          walker.walk(new LeafListener(), $r.ctx);\
        }
          : r=e ;
        e : e op='*' e
          | e op='+' e
          | INT
          ;
        MULT: '*' ;
        ADD : '+' ;
        INT : [0-9]+ ;
        WS : [ \\t\\n]+ -> skip ;
        """;
    String result = execParser("T.g4", grammar, "TParser", "TLexer", "s", "1+2*3", false);
    String expecting =
      """
        (e (e 1) + (e (e 2) * (e 3)))
        1
        2
        3
        2 3 2
        1 2 1
        """;
    assertEquals(expecting, result);
  }

  @Test
  @Disabled("Переделать на ANTLR runtime/Generator")
  public void testLRWithLabels() {
    String grammar =
      """
        grammar T;
        @parser::members {
          public static class LeafListener extends TBaseListener {
            public void exitCall(TParser.CallContext ctx) {
              System.out.printf("%s %s",ctx.e().start.getText(),
                        ctx.eList());
            }
            public void exitInt(TParser.IntContext ctx) {
              System.out.println(ctx.INT().getSymbol().getText());
            }
          }
        }
        s
        @after {\
          System.out.println($r.ctx.toStringTree(this));\
          ParseTreeWalker walker = new ParseTreeWalker();
          walker.walk(new LeafListener(), $r.ctx);\
        }
          : r=e ;
        e : e '(' eList ')' # Call
          | INT             # Int
          ;    \s
        eList : e (',' e)* ;
        MULT: '*' ;
        ADD : '+' ;
        INT : [0-9]+ ;
        WS : [ \\t\\n]+ -> skip ;
        """;
    String result = execParser("T.g4", grammar, "TParser", "TLexer", "s", "1(2,3)", false);
    String expecting =
      """
        (e (e 1) ( (eList (e 2) , (e 3)) ))
        1
        2
        3
        1 [13 6]
        """;
    assertEquals(expecting, result);
  }
}
