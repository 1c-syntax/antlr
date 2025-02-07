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
import static org.assertj.core.api.Assertions.assertThat;

/**
 *
 */
public class LeftRecursionToolIssuesTest extends AbstractBaseTest {
  protected boolean debug = false;

  @Test
  @Disabled("Переделать на ANTLR runtime/Generator")
  public void testSimple() {
    String grammar =
      """
        grammar T;
        s @after {System.out.println($ctx.toStringTree(this));} : a ;
        a : a ID
          | ID\
          ;
        ID : 'a'..'z'+ ;
        WS : (' '|'\\n') -> skip ;
        """;
    String found = execParser("T.g4", grammar, "TParser", "TLexer",
      "s", "x", debug);
    String expecting = "(s (a x))\n";
    assertEquals(expecting, found);

    found = execParser("T.g4", grammar, "TParser", "TLexer",
      "s", "x y", debug);
    expecting = "(s (a (a x) y))\n";
    assertEquals(expecting, found);

    found = execParser("T.g4", grammar, "TParser", "TLexer",
      "s", "x y z", debug);
    expecting = "(s (a (a (a x) y) z))\n";
    assertEquals(expecting, found);
  }

  /**
   * This is a regression test for "Support direct calls to left-recursive
   * rules".
   * <a href="https://github.com/antlr/antlr4/issues/161">...</a>
   */
  @Test
  @Disabled("Переделать на ANTLR runtime/Generator")
  public void testDirectCallToLeftRecursiveRule() {
    String grammar =
      """
        grammar T;
        a @after {System.out.println($ctx.toStringTree(this));} : a ID
          | ID\
          ;
        ID : 'a'..'z'+ ;
        WS : (' '|'\\n') -> skip ;
        """;
    String found = execParser("T.g4", grammar, "TParser", "TLexer",
      "a", "x", debug);
    String expecting = "(a x)\n";
    assertEquals(expecting, found);

    found = execParser("T.g4", grammar, "TParser", "TLexer",
      "a", "x y", debug);
    expecting = "(a (a x) y)\n";
    assertEquals(expecting, found);

    found = execParser("T.g4", grammar, "TParser", "TLexer",
      "a", "x y z", debug);
    expecting = "(a (a (a x) y) z)\n";
    assertEquals(expecting, found);
  }

  @Test
  @Disabled("Переделать на ANTLR runtime/Generator")
  public void testSemPred() {
    String grammar =
      """
        grammar T;
        s @after {System.out.println($ctx.toStringTree(this));} : a ;
        a : a {true}? ID
          | ID\
          ;
        ID : 'a'..'z'+ ;
        WS : (' '|'\\n') -> skip ;
        """;
    String found = execParser("T.g4", grammar, "TParser", "TLexer",
      "s", "x y z", debug);
    String expecting = "(s (a (a (a x) y) z))\n";
    assertEquals(expecting, found);
  }

  @Test
  @Disabled("Переделать на ANTLR runtime/Generator")
  public void testSemPredFailOption() {
    String grammar =
      """
        grammar T;
        s @after {System.out.println($ctx.toStringTree(this));} : a ;
        a : a ID {false}?<fail='custom message'>
          | ID\
          ;
        ID : 'a'..'z'+ ;
        WS : (' '|'\\n') -> skip ;
        """;
    String found = execParser("T.g4", grammar, "TParser", "TLexer",
      "s", "x y z", debug);
    String expecting = "(s (a (a x) y z))\n";
    assertEquals(expecting, found);
    assertEquals("line 1:4 rule a custom message\n", stderrDuringParse);
  }

  @Test
  @Disabled("Переделать на ANTLR runtime/Generator")
  public void testTernaryExpr() {
    // must indicate EOF can follow or 'a<EOF>' won't match
    String grammar =
      """
        grammar T;
        s @after {System.out.println($ctx.toStringTree(this));} : e EOF ;
        e : e '*' e\
          | e '+' e\
          |<assoc=right> e '?' e ':' e\
          |<assoc=right> e '=' e\
          | ID\
          ;
        ID : 'a'..'z'+ ;
        WS : (' '|'\\n') -> skip ;
        """;
    String[] tests = {
      "a", "(s (e a) <EOF>)",
      "a+b", "(s (e (e a) + (e b)) <EOF>)",
      "a*b", "(s (e (e a) * (e b)) <EOF>)",
      "a?b:c", "(s (e (e a) ? (e b) : (e c)) <EOF>)",
      "a=b=c", "(s (e (e a) = (e (e b) = (e c))) <EOF>)",
      "a?b+c:d", "(s (e (e a) ? (e (e b) + (e c)) : (e d)) <EOF>)",
      "a?b=c:d", "(s (e (e a) ? (e (e b) = (e c)) : (e d)) <EOF>)",
      "a? b?c:d : e", "(s (e (e a) ? (e (e b) ? (e c) : (e d)) : (e e)) <EOF>)",
      "a?b: c?d:e", "(s (e (e a) ? (e b) : (e (e c) ? (e d) : (e e))) <EOF>)",
    };
    runTests(grammar, tests, "s");
  }

  /**
   * This is a regression test for antlr/antlr4#542 "First alternative cannot
   * be right-associative".
   * <a href="https://github.com/antlr/antlr4/issues/542">...</a>
   */
  @Test
  @Disabled("Переделать на ANTLR runtime/Generator")
  public void testTernaryExprExplicitAssociativity() {
    // must indicate EOF can follow or 'a<EOF>' won't match
    String grammar =
      """
        grammar T;
        s @after {System.out.println($ctx.toStringTree(this));} : e EOF ;
        e :<assoc=right> e '*' e\
          |<assoc=right> e '+' e\
          |<assoc=right> e '?' e ':' e\
          |<assoc=right> e '=' e\
          | ID\
          ;
        ID : 'a'..'z'+ ;
        WS : (' '|'\\n') -> skip ;
        """;
    String[] tests = {
      "a", "(s (e a) <EOF>)",
      "a+b", "(s (e (e a) + (e b)) <EOF>)",
      "a*b", "(s (e (e a) * (e b)) <EOF>)",
      "a?b:c", "(s (e (e a) ? (e b) : (e c)) <EOF>)",
      "a=b=c", "(s (e (e a) = (e (e b) = (e c))) <EOF>)",
      "a?b+c:d", "(s (e (e a) ? (e (e b) + (e c)) : (e d)) <EOF>)",
      "a?b=c:d", "(s (e (e a) ? (e (e b) = (e c)) : (e d)) <EOF>)",
      "a? b?c:d : e", "(s (e (e a) ? (e (e b) ? (e c) : (e d)) : (e e)) <EOF>)",
      "a?b: c?d:e", "(s (e (e a) ? (e b) : (e (e c) ? (e d) : (e e))) <EOF>)",
    };
    runTests(grammar, tests, "s");
  }

  @Test
  @Disabled("Переделать на ANTLR runtime/Generator")
  public void testExpressions() {
    // must indicate EOF can follow
    String grammar =
      """
        grammar T;
        s @after {System.out.println($ctx.toStringTree(this));} : e EOF ;
        e : e '.' ID
          | e '.' 'this'
          | '-' e
          | e '*' e
          | e ('+'|'-') e
          | INT
          | ID
          ;
        ID : 'a'..'z'+ ;
        INT : '0'..'9'+ ;
        WS : (' '|'\\n') -> skip ;
        """;
    String[] tests = {
      "a", "(s (e a) <EOF>)",
      "1", "(s (e 1) <EOF>)",
      "a-1", "(s (e (e a) - (e 1)) <EOF>)",
      "a.b", "(s (e (e a) . b) <EOF>)",
      "a.this", "(s (e (e a) . this) <EOF>)",
      "-a", "(s (e - (e a)) <EOF>)",
      "-a+b", "(s (e (e - (e a)) + (e b)) <EOF>)",
    };
    runTests(grammar, tests, "s");
  }

  @Test
  @Disabled("Переделать на ANTLR runtime/Generator")
  public void testJavaExpressions() {
    // Generates about 7k in bytecodes for generated e_ rule;
    // Well within the 64k method limit. e_primary compiles
    // to about 2k in bytecodes.
    // this is simplified from real java
    // must indicate EOF can follow
    String grammar =
      """
        grammar T;
        s @after {System.out.println($ctx.toStringTree(this));} : e EOF ;
        expressionList
            :   e (',' e)*
            ;
        e   :   '(' e ')'
            |   'this'\s
            |   'super'
            |   INT
            |   ID
            |   type '.' 'class'
            |   e '.' ID
            |   e '.' 'this'
            |   e '.' 'super' '(' expressionList? ')'
            |   e '.' 'new' ID '(' expressionList? ')'
        	 |	 'new' type ( '(' expressionList? ')' | ('[' e ']')+)
            |   e '[' e ']'
            |   '(' type ')' e
            |   e ('++' | '--')
            |   e '(' expressionList? ')'
            |   ('+'|'-'|'++'|'--') e
            |   ('~'|'!') e
            |   e ('*'|'/'|'%') e
            |   e ('+'|'-') e
            |   e ('<<' | '>>>' | '>>') e
            |   e ('<=' | '>=' | '>' | '<') e
            |   e 'instanceof' e
            |   e ('==' | '!=') e
            |   e '&' e
            |<assoc=right> e '^' e
            |   e '|' e
            |   e '&&' e
            |   e '||' e
            |   e '?' e ':' e
            |<assoc=right>\
                e ('='
                  |'+='
                  |'-='
                  |'*='
                  |'/='
                  |'&='
                  |'|='
                  |'^='
                  |'>>='
                  |'>>>='
                  |'<<='
                  |'%=') e
            ;
        type: ID\s
            | ID '[' ']'
            | 'int'
        	 | 'int' '[' ']'\s
            ;
        ID : ('a'..'z'|'A'..'Z'|'_'|'$')+;
        INT : '0'..'9'+ ;
        WS : (' '|'\\n') -> skip ;
        """;
    String[] tests = {
      "a|b&c", "(s (e (e a) | (e (e b) & (e c))) <EOF>)",
      "(a|b)&c", "(s (e (e ( (e (e a) | (e b)) )) & (e c)) <EOF>)",
      "a > b", "(s (e (e a) > (e b)) <EOF>)",
      "a >> b", "(s (e (e a) >> (e b)) <EOF>)",
      "a=b=c", "(s (e (e a) = (e (e b) = (e c))) <EOF>)",
      "a^b^c", "(s (e (e a) ^ (e (e b) ^ (e c))) <EOF>)",
      "(T)x", "(s (e ( (type T) ) (e x)) <EOF>)",
      "new A().b", "(s (e (e new (type A) ( )) . b) <EOF>)",
      "(T)t.f()", "(s (e (e ( (type T) ) (e (e t) . f)) ( )) <EOF>)",
      "a.f(x)==T.c", "(s (e (e (e (e a) . f) ( (expressionList (e x)) )) == (e (e T) . c)) <EOF>)",
      "a.f().g(x,1)", "(s (e (e (e (e (e a) . f) ( )) . g) ( (expressionList (e x) , (e 1)) )) <EOF>)",
      "new T[((n-1) * x) + 1]", "(s (e new (type T) [ (e (e ( (e (e ( (e (e n) - (e 1)) )) * (e x)) )) + (e 1)) ]) <EOF>)",
    };
    runTests(grammar, tests, "s");
  }

  @Test
  @Disabled("Переделать на ANTLR runtime/Generator")
  public void testDeclarations() {
    // must indicate EOF can follow
    // binds less tight than suffixes
    String grammar =
      """
        grammar T;
        s @after {System.out.println($ctx.toStringTree(this));} : declarator EOF ;
        declarator
                : declarator '[' e ']'
                | declarator '[' ']'
                | declarator '(' ')'
                | '*' declarator
                | '(' declarator ')'
                | ID
                ;
        e : INT ;
        ID : 'a'..'z'+ ;
        INT : '0'..'9'+ ;
        WS : (' '|'\\n') -> skip ;
        """;
    String[] tests = {
      "a", "(s (declarator a) <EOF>)",
      "*a", "(s (declarator * (declarator a)) <EOF>)",
      "**a", "(s (declarator * (declarator * (declarator a))) <EOF>)",
      "a[3]", "(s (declarator (declarator a) [ (e 3) ]) <EOF>)",
      "b[]", "(s (declarator (declarator b) [ ]) <EOF>)",
      "(a)", "(s (declarator ( (declarator a) )) <EOF>)",
      "a[]()", "(s (declarator (declarator (declarator a) [ ]) ( )) <EOF>)",
      "a[][]", "(s (declarator (declarator (declarator a) [ ]) [ ]) <EOF>)",
      "*a[]", "(s (declarator * (declarator (declarator a) [ ])) <EOF>)",
      "(*a)[]", "(s (declarator (declarator ( (declarator * (declarator a)) )) [ ]) <EOF>)",
    };
    runTests(grammar, tests, "s");
  }

  @Test
  @Disabled("Переделать на ANTLR runtime/Generator")
  public void testReturnValueAndActions() {
    String grammar =
      """
        grammar T;
        s : e {System.out.println($e.v);} ;
        e returns [int v, List<String> ignored]
          : a=e '*' b=e {$v = $a.v * $b.v;}
          | a=e '+' b=e {$v = $a.v + $b.v;}
          | INT {$v = $INT.int;}
          | '(' x=e ')' {$v = $x.v;}
          ;
        INT : '0'..'9'+ ;
        WS : (' '|'\\n') -> skip ;
        """;
    String[] tests = {
      "4", "4",
      "1+2", "3",
      "1+2*3", "7",
      "(1+2)*3", "9",
    };
    runTests(grammar, tests, "s");
  }

  /**
   * This is a regression test for antlr/antlr4#677 "labels not working in
   * grammar file".
   * <a href="https://github.com/antlr/antlr4/issues/677">...</a>
   *
   * <p>This test treats {@code ,} and {@code >>} as part of a single compound
   * operator (similar to a ternary operator).</p>
   */
  @Test
  @Disabled("Переделать на ANTLR runtime/Generator")
  public void testReturnValueAndActionsList1() {
    String grammar =
      """
        grammar T;
        s @after {System.out.println($ctx.toStringTree(this));} : expr EOF;
        expr:
            a=expr '*' a=expr #Factor
            | b+=expr (',' b+=expr)* '>>' c=expr #Send
            | ID #JustId //semantic check on modifiers
        ;
        
        ID  : ('a'..'z'|'A'..'Z'|'_')
              ('a'..'z'|'A'..'Z'|'0'..'9'|'_')*
        ;
        
        WS : [ \\t\\n]+ -> skip ;
        """;
    String[] tests = {
      "a*b", "(s (expr (expr a) * (expr b)) <EOF>)",
      "a,c>>x", "(s (expr (expr a) , (expr c) >> (expr x)) <EOF>)",
      "x", "(s (expr x) <EOF>)",
      "a*b,c,x*y>>r", "(s (expr (expr (expr a) * (expr b)) , (expr c) , (expr (expr x) * (expr y)) >> (expr r)) <EOF>)",
    };
    runTests(grammar, tests, "s");
  }

  /**
   * This is a regression test for antlr/antlr4#677 "labels not working in
   * grammar file".
   * <a href="https://github.com/antlr/antlr4/issues/677">...</a>
   *
   * <p>This test treats the {@code ,} and {@code >>} operators separately.</p>
   */
  @Test
  @Disabled("Переделать на ANTLR runtime/Generator")
  public void testReturnValueAndActionsList2() {
    String grammar =
      """
        grammar T;
        s @after {System.out.println($ctx.toStringTree(this));} : expr EOF;
        expr:
            a=expr '*' a=expr #Factor
            | b+=expr ',' b+=expr #Comma
            | b+=expr '>>' c=expr #Send
            | ID #JustId //semantic check on modifiers
        ;
        
        ID  : ('a'..'z'|'A'..'Z'|'_')
              ('a'..'z'|'A'..'Z'|'0'..'9'|'_')*
        ;
        
        WS : [ \\t\\n]+ -> skip ;
        """;
    String[] tests = {
      "a*b", "(s (expr (expr a) * (expr b)) <EOF>)",
      "a,c>>x", "(s (expr (expr (expr a) , (expr c)) >> (expr x)) <EOF>)",
      "x", "(s (expr x) <EOF>)",
      "a*b,c,x*y>>r", "(s (expr (expr (expr (expr (expr a) * (expr b)) , (expr c)) , (expr (expr x) * (expr y))) >> (expr r)) <EOF>)",
    };
    runTests(grammar, tests, "s");
  }

  @Test
  @Disabled("Переделать на ANTLR runtime/Generator")
  public void testLabelsOnOpSubrule() {
    String grammar =
      """
        grammar T;
        s @after {System.out.println($ctx.toStringTree(this));} : e ;
        e : a=e op=('*'|'/') b=e  {}
          | INT {}
          | '(' x=e ')' {}
          ;
        INT : '0'..'9'+ ;
        WS : (' '|'\\n') -> skip ;
        """;
    String[] tests = {
      "4", "(s (e 4))",
      "1*2/3", "(s (e (e (e 1) * (e 2)) / (e 3)))",
      "(1/2)*3", "(s (e (e ( (e (e 1) / (e 2)) )) * (e 3)))",
    };
    runTests(grammar, tests, "s");
  }

  @Test
  @Disabled("Переделать на ANTLR runtime/Generator")
  public void testReturnValueAndActionsAndLabels() {
    String grammar =
      """
        grammar T;
        s : q=e {System.out.println($e.v);} ;
        
        e returns [int v]
          : a=e op='*' b=e {$v = $a.v * $b.v;}  # mult
          | a=e '+' b=e {$v = $a.v + $b.v;}     # add
          | INT         {$v = $INT.int;}        # anInt
          | '(' x=e ')' {$v = $x.v;}            # parens
          | x=e '++'    {$v = $x.v+1;}          # inc
          | e '--'                              # dec
          | ID          {$v = 3;}               # anID
          ;\s
        
        ID : 'a'..'z'+ ;
        INT : '0'..'9'+ ;
        WS : (' '|'\\n') -> skip ;
        """;
    String[] tests = {
      "4", "4",
      "1+2", "3",
      "1+2*3", "7",
      "i++*3", "12",
    };
    runTests(grammar, tests, "s");
  }

  /**
   * This is a regression test for antlr/antlr4#433 "Not all context accessor
   * methods are generated when an alternative rule label is used for multiple
   * alternatives".
   * <a href="https://github.com/antlr/antlr4/issues/433">...</a>
   */
  @Test
  @Disabled("Переделать на ANTLR runtime/Generator")
  public void testMultipleAlternativesWithCommonLabel() {
    String grammar =
      """
        grammar T;
        s : e {System.out.println($e.v);} ;
        
        e returns [int v]
          : e '*' e     {$v = ((BinaryContext)$ctx).e(0).v * ((BinaryContext)$ctx).e(1).v;}  # binary
          | e '+' e     {$v = ((BinaryContext)$ctx).e(0).v + ((BinaryContext)$ctx).e(1).v;}  # binary
          | INT         {$v = $INT.int;}                                                     # anInt
          | '(' e ')'   {$v = $e.v;}                                                         # parens
          | left=e INC  {assert(((UnaryContext)$ctx).INC() != null); $v = $left.v + 1;}      # unary
          | left=e DEC  {assert(((UnaryContext)$ctx).DEC() != null); $v = $left.v - 1;}      # unary
          | ID          {$v = 3;}                                                            # anID
          ;\s
        
        ID : 'a'..'z'+ ;
        INT : '0'..'9'+ ;
        INC : '++' ;
        DEC : '--' ;
        WS : (' '|'\\n') -> skip ;
        """;
    String[] tests = {
      "4", "4",
      "1+2", "3",
      "1+2*3", "7",
      "i++*3", "12",
    };
    runTests(grammar, tests, "s");
  }

  @Test
  @Disabled("Переделать на ANTLR runtime/Generator")
  public void testPrefixOpWithActionAndLabel() {
    String grammar =
      """
        grammar T;
        s : e {System.out.println($e.result);} ;
        
        e returns [String result]
            :   ID '=' e1=e    { $result = "(" + $ID.getText() + "=" + $e1.result + ")"; }
            |   ID             { $result = $ID.getText(); }
            |   e1=e '+' e2=e  { $result = "(" + $e1.result + "+" + $e2.result + ")"; }
            ;
        ID : 'a'..'z'+ ;
        INT : '0'..'9'+ ;
        WS : (' '|'\\n') -> skip ;
        """;
    String[] tests = {
      "a", "a",
      "a+b", "(a+b)",
      "a=b+c", "((a=b)+c)",
    };
    runTests(grammar, tests, "s");
  }

  @Test
  @Disabled("Переделать на ANTLR runtime/Generator")
  public void testAmbigLR() {
    String grammar =
      """
        grammar Expr;
        prog:   stat ;
        stat:   expr NEWLINE                # printExpr
            |   ID '=' expr NEWLINE         # assign
            |   NEWLINE                     # blank
            ;
        expr:   expr ('*'|'/') expr      # MulDiv
            |   expr ('+'|'-') expr      # AddSub
            |   INT                      # int
            |   ID                       # id
            |   '(' expr ')'             # parens
            ;
        
        MUL :   '*' ; // assigns token name to '*' used above in grammar
        DIV :   '/' ;
        ADD :   '+' ;
        SUB :   '-' ;
        ID  :   [a-zA-Z]+ ;      // match identifiers
        INT :   [0-9]+ ;         // match integers
        NEWLINE:'\\r'? '\\n' ;     // return newlines to parser (is end-statement signal)
        WS  :   [ \\t]+ -> skip ; // toss out whitespace
        """;
    String result = execParser("Expr.g4", grammar, "ExprParser", "ExprLexer", "prog", "1\n", true);
    assertThat(stderrDuringParse).isNull();

    result = execParser("Expr.g4", grammar, "ExprParser", "ExprLexer", "prog", "a = 5\n", true);
    assertThat(stderrDuringParse).isNull();

    result = execParser("Expr.g4", grammar, "ExprParser", "ExprLexer", "prog", "b = 6\n", true);
    assertThat(stderrDuringParse).isNull();

    result = execParser("Expr.g4", grammar, "ExprParser", "ExprLexer", "prog", "a+b*2\n", true);
    assertThat(stderrDuringParse).isNull();

    result = execParser("Expr.g4", grammar, "ExprParser", "ExprLexer", "prog", "(1+2)*3\n", true);
    assertThat(stderrDuringParse).isNull();
  }

  @Test
  public void testCheckForNonLeftRecursiveRule() {
    String grammar =
      """
        grammar T;
        s @after {System.out.println($ctx.toStringTree(this));} : a ;
        a : a ID
          ;
        ID : 'a'..'z'+ ;
        WS : (' '|'\\n') -> skip ;
        """;
    String expected =
      "error(" + ErrorType.NO_NON_LR_ALTS.code + "): T.g4:3:0: left recursive rule 'a' must contain an alternative which is not left recursive\n";
    testErrors(new String[]{grammar, expected}, false);
  }

  @Test
  public void testCheckForLeftRecursiveEmptyFollow() {
    String grammar =
      """
        grammar T;
        s @after {System.out.println($ctx.toStringTree(this));} : a ;
        a : a ID?
          | ID
          ;
        ID : 'a'..'z'+ ;
        WS : (' '|'\\n') -> skip ;
        """;
    String expected =
      "error(" + ErrorType.EPSILON_LR_FOLLOW.code + "): T.g4:3:0: left recursive rule 'a' contains a left recursive alternative which can be followed by the empty string\n";
    testErrors(new String[]{grammar, expected}, false);
  }

  /**
   * This is a regression test for #239 "recoursive parser using implicit
   * tokens ignore white space lexer rule".
   * <a href="https://github.com/antlr/antlr4/issues/239">...</a>
   */
  @Test
  @Disabled("Переделать на ANTLR runtime/Generator")
  public void testWhitespaceInfluence() {
    String grammar =
      """
        grammar Expr;
        prog : expression EOF;
        expression
            : ID '(' expression (',' expression)* ')'               # doFunction
            | '(' expression ')'                                    # doParenthesis
            | '!' expression                                        # doNot
            | '-' expression                                        # doNegate
            | '+' expression                                        # doPositiv
            | expression '^' expression                             # doPower
            | expression '*' expression                             # doMultipy
            | expression '/' expression                             # doDivide
            | expression '%' expression                             # doModulo
            | expression '-' expression                             # doMinus
            | expression '+' expression                             # doPlus
            | expression '=' expression                             # doEqual
            | expression '!=' expression                            # doNotEqual
            | expression '>' expression                             # doGreather
            | expression '>=' expression                            # doGreatherEqual
            | expression '<' expression                             # doLesser
            | expression '<=' expression                            # doLesserEqual
            | expression K_IN '(' expression (',' expression)* ')'  # doIn
            | expression ( '&' | K_AND) expression                  # doAnd
            | expression ( '|' | K_OR) expression                   # doOr
            | '[' expression (',' expression)* ']'                  # newArray
            | K_TRUE                                                # newTrueBoolean
            | K_FALSE                                               # newFalseBoolean
            | NUMBER                                                # newNumber
            | DATE                                                  # newDateTime
            | ID                                                    # newIdentifier
            | SQ_STRING                                             # newString
            | K_NULL                                                # newNull
            ;
        
        // Fragments
        fragment DIGIT    : '0' .. '9'; \s
        fragment UPPER    : 'A' .. 'Z';
        fragment LOWER    : 'a' .. 'z';
        fragment LETTER   : LOWER | UPPER;
        fragment WORD     : LETTER | '_' | '$' | '#' | '.';
        fragment ALPHANUM : WORD | DIGIT; \s
        
        // Tokens
        ID              : LETTER ALPHANUM*;
        NUMBER          : DIGIT+ ('.' DIGIT+)? (('e'|'E')('+'|'-')? DIGIT+)?;
        DATE            : '\\'' DIGIT DIGIT DIGIT DIGIT '-' DIGIT DIGIT '-' DIGIT DIGIT (' ' DIGIT DIGIT ':' DIGIT DIGIT ':' DIGIT DIGIT ('.' DIGIT+)?)? '\\'';
        SQ_STRING       : '\\'' ('\\'\\'' | ~'\\'')* '\\'';
        DQ_STRING       : '"' ('\\\\"' | ~'"')* '"';
        WS              : [ \\t\\n\\r]+ -> skip ;
        COMMENTS        : ('/*' .*? '*/' | '//' ~'\\n'* '\\n' ) -> skip;
        """;

    String expected =
      "";
    String result = execParser("Expr.g4", grammar, "ExprParser", "ExprLexer", "prog", "Test(1,3)", false);
    assertEquals(expected, result);
    assertThat(stderrDuringParse).isNull();

    expected =
      "";
    result = execParser("Expr.g4", grammar, "ExprParser", "ExprLexer", "prog", "Test(1, 3)", false);
    assertEquals(expected, result);
    assertThat(stderrDuringParse).isNull();
  }

  /**
   * This is a regression test for antlr/antlr4#509 "Incorrect rule chosen in
   * unambiguous grammar".
   * <a href="https://github.com/antlr/antlr4/issues/509">...</a>
   */
  @Test
  @Disabled("Переделать на ANTLR runtime/Generator")
  public void testPrecedenceFilterConsidersContext() {
    String grammar =
      """
        grammar T;
        prog
        @after {System.out.println($ctx.toStringTree(this));}
        : statement* EOF {};
        statement: letterA | statement letterA 'b' ;
        letterA: 'a';
        """;

    String found = execParser("T.g4", grammar, "TParser", "TLexer", "prog",
      "aa", false);
    assertEquals("(prog (statement (letterA a)) (statement (letterA a)) <EOF>)\n", found);
  }

  /**
   * This is a regression test for antlr/antlr4#625 "Duplicate action breaks
   * operator precedence"
   * <a href="https://github.com/antlr/antlr4/issues/625">...</a>
   */
  @Test
  @Disabled("Переделать на ANTLR runtime/Generator")
  public void testMultipleActions() {
    String grammar =
      """
        grammar T;
        s @after {System.out.println($ctx.toStringTree(this));} : e ;
        e : a=e op=('*'|'/') b=e  {}{}
          | INT {}{}
          | '(' x=e ')' {}{}
          ;
        INT : '0'..'9'+ ;
        WS : (' '|'\\n') -> skip ;
        """;
    String[] tests = {
      "4", "(s (e 4))",
      "1*2/3", "(s (e (e (e 1) * (e 2)) / (e 3)))",
      "(1/2)*3", "(s (e (e ( (e (e 1) / (e 2)) )) * (e 3)))",
    };
    runTests(grammar, tests, "s");
  }

  /**
   * This is a regression test for antlr/antlr4#625 "Duplicate action breaks
   * operator precedence"
   * <a href="https://github.com/antlr/antlr4/issues/625">...</a>
   */
  @Test
  @Disabled("Переделать на ANTLR runtime/Generator")
  public void testMultipleActionsPredicatesOptions() {
    String grammar =
      """
        grammar T;
        s @after {System.out.println($ctx.toStringTree(this));} : e ;
        e : a=e op=('*'|'/') b=e  {}{true}?
          | a=e op=('+'|'-') b=e  {}<p=3>{true}?<fail='Message'>
          | INT {}{}
          | '(' x=e ')' {}{}
          ;
        INT : '0'..'9'+ ;
        WS : (' '|'\\n') -> skip ;
        """;
    String[] tests = {
      "4", "(s (e 4))",
      "1*2/3", "(s (e (e (e 1) * (e 2)) / (e 3)))",
      "(1/2)*3", "(s (e (e ( (e (e 1) / (e 2)) )) * (e 3)))",
    };
    runTests(grammar, tests, "s");
  }

  public void runTests(String grammar, String[] tests, String startRule) {
    boolean success = rawGenerateAndBuildRecognizer("T.g4", grammar, "TParser", "TLexer");
    assertThat(success).isTrue();
    writeRecognizerAndCompile("TParser",
      "TLexer",
      startRule,
      debug,
      false);

    for (int i = 0; i < tests.length; i += 2) {
      String test = tests[i];
      String expecting = tests[i + 1] + "\n";
      writeFile(tmpdir, "input", test);
      String found = execRecognizer();
      System.out.print(test + " -> " + found);
      assertEquals(expecting, found);
    }
  }
}
