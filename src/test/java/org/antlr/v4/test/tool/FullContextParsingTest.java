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

/*
	 cover these cases:
	    dead end
	    single alt
	    single alt + preds
	    conflict
	    conflict + preds

 */
public class FullContextParsingTest extends AbstractBaseTest {
  @Test
  @Disabled("Переделать на ANTLR runtime/Generator")
  public void testAmbigYieldsCtxSensitiveDFA() {
    String grammar =
      """
        grammar T;
        s\
        @init {_interp.setPredictionMode(PredictionMode.LL_EXACT_AMBIG_DETECTION);}
        @after {dumpDFA();}
            : ID | ID {;} ;
        ID : 'a'..'z'+ ;
        WS : (' '|'\\t'|'\\n')+ -> skip ;
        """;
    String result = execParser("T.g4", grammar, "TParser", "TLexer", "s",
      "abc", true);
    String expecting =
      """
        Decision 0:
        s0-ID->:s1=>1
        """; // ctx sensitive
    assertEquals(expecting, result);
    assertEquals("line 1:0 reportAmbiguity d=0 (s): ambigAlts={1, 2}, input='abc'\n",
      this.stderrDuringParse);
  }

  public String testCtxSensitiveWithoutDFA(String input) {
    String grammar =
      """
        grammar T;
        s @after {dumpDFA();}
          : '$' a | '@' b ;
        a : e ID ;
        b : e INT ID ;
        e : INT | ;
        ID : 'a'..'z'+ ;
        INT : '0'..'9'+ ;
        WS : (' '|'\\t'|'\\n')+ -> skip ;
        """;
    return execParser("T.g4", grammar, "TParser", "TLexer", "s", input, true);
  }

  @Test
  @Disabled("Переделать на ANTLR runtime/Generator")
  public void testCtxSensitiveWithoutDFA1() {
    String result = testCtxSensitiveWithoutDFA("$ 34 abc");
    String expecting =
      """
        Decision 1:
        s0-INT->s1
        s1-ID->:s2=>1
        """;
    assertEquals(expecting, result);
    assertEquals("""
        line 1:5 reportAttemptingFullContext d=1 (e), input='34abc'
        line 1:2 reportContextSensitivity d=1 (e), input='34'
        """,
      this.stderrDuringParse);
  }

  @Test
  @Disabled("Переделать на ANTLR runtime/Generator")
  public void testCtxSensitiveWithoutDFA2() {
    String result = testCtxSensitiveWithoutDFA("@ 34 abc");
    String expecting =
      """
        Decision 1:
        s0-INT->s1
        s1-ID->:s2=>1
        """;
    assertEquals(expecting, result);
    assertEquals("""
        line 1:5 reportAttemptingFullContext d=1 (e), input='34abc'
        line 1:5 reportContextSensitivity d=1 (e), input='34abc'
        """,
      this.stderrDuringParse);
  }

  public String testCtxSensitiveWithDFA(String input) {
    String grammar =
      """
        grammar T;
        s @init{getInterpreter().enable_global_context_dfa = true;} @after {dumpDFA();}
          : '$' a | '@' b ;
        a : e ID ;
        b : e INT ID ;
        e : INT | ;
        ID : 'a'..'z'+ ;
        INT : '0'..'9'+ ;
        WS : (' '|'\\t'|'\\n')+ -> skip ;
        """;
    return execParser("T.g4", grammar, "TParser", "TLexer", "s",
      input, true);
  }

  @Test
  @Disabled("Переделать на ANTLR runtime/Generator")
  public void testCtxSensitiveWithDFA1() {
    String result = testCtxSensitiveWithDFA("$ 34 abc");
    String expecting =
      """
        Decision 1:
        s0-INT->s1
        s1-ID->:s2=>1
        s3**-ctx:15(a)->s4
        s4-INT->:s5=>1
        """;
    assertEquals(expecting, result);
    assertEquals("""
        line 1:5 reportAttemptingFullContext d=1 (e), input='34abc'
        line 1:2 reportContextSensitivity d=1 (e), input='34'
        """,
      this.stderrDuringParse);
  }

  @Test
  @Disabled("Переделать на ANTLR runtime/Generator")
  public void testCtxSensitiveWithDFA2() {
    String result = testCtxSensitiveWithDFA("@ 34 abc");
    String expecting =
      """
        Decision 1:
        s0-INT->s1
        s1-ID->:s2=>1
        s3**-ctx:18(b)->s4
        s4-INT->s5
        s5-ID->:s6=>2
        """;
    assertEquals(expecting, result);
    assertEquals("""
        line 1:5 reportAttemptingFullContext d=1 (e), input='34abc'
        line 1:5 reportContextSensitivity d=1 (e), input='34abc'
        """,
      this.stderrDuringParse);
  }

  @Test
  @Disabled("Переделать на ANTLR runtime/Generator")
  public void testCtxSensitiveDFATwoDiffInputWithoutDFA() {
    String grammar =
      """
        grammar T;
        s @after {dumpDFA();}
          : ('$' a | '@' b)+ ;
        a : e ID ;
        b : e INT ID ;
        e : INT | ;
        ID : 'a'..'z'+ ;
        INT : '0'..'9'+ ;
        WS : (' '|'\\t'|'\\n')+ -> skip ;
        """;
    String result = execParser("T.g4", grammar, "TParser", "TLexer", "s",
      "$ 34 abc @ 34 abc", true);
    String expecting =
      """
        Decision 2:
        s0-INT->s1
        s1-ID->:s2=>1
        """;
    assertEquals(expecting, result);
    assertEquals("""
        line 1:5 reportAttemptingFullContext d=2 (e), input='34abc'
        line 1:2 reportContextSensitivity d=2 (e), input='34'
        line 1:14 reportAttemptingFullContext d=2 (e), input='34abc'
        line 1:14 reportContextSensitivity d=2 (e), input='34abc'
        """,
      this.stderrDuringParse);
  }

  @Test
  @Disabled("Переделать на ANTLR runtime/Generator")
  public void testCtxSensitiveDFATwoDiffInputWithDFA() {
    String grammar =
      """
        grammar T;
        s @init{getInterpreter().enable_global_context_dfa = true;} @after {dumpDFA();}
          : ('$' a | '@' b)+ ;
        a : e ID ;
        b : e INT ID ;
        e : INT | ;
        ID : 'a'..'z'+ ;
        INT : '0'..'9'+ ;
        WS : (' '|'\\t'|'\\n')+ -> skip ;
        """;
    String result = execParser("T.g4", grammar, "TParser", "TLexer", "s",
      "$ 34 abc @ 34 abc", true);
    String expecting =
      """
        Decision 2:
        s0-INT->s1
        s1-ID->:s2=>1
        s3**-ctx:17(a)->s4
        s3**-ctx:20(b)->s6
        s4-INT->:s5=>1
        s6-INT->s7
        s7-ID->:s8=>2
        """;
    assertEquals(expecting, result);
    assertEquals("""
        line 1:5 reportAttemptingFullContext d=2 (e), input='34abc'
        line 1:2 reportContextSensitivity d=2 (e), input='34'
        line 1:14 reportAttemptingFullContext d=2 (e), input='34abc'
        line 1:14 reportContextSensitivity d=2 (e), input='34abc'
        """,
      this.stderrDuringParse);
  }

  @Test
  @Disabled("Переделать на ANTLR runtime/Generator")
  public void testSLLSeesEOFInLLGrammarWithoutDFA() {
    String grammar =
      """
        grammar T;
        s @after {dumpDFA();}
          : a ;
        a : e ID ;
        b : e INT ID ;
        e : INT | ;
        ID : 'a'..'z'+ ;
        INT : '0'..'9'+ ;
        WS : (' '|'\\t'|'\\n')+ -> skip ;
        """;
    String result = execParser("T.g4", grammar, "TParser", "TLexer", "s",
      "34 abc", true);
    String expecting =
      """
        Decision 0:
        s0-INT->s1
        s1-ID->:s2=>1
        """; // Must point at accept state
    assertEquals(expecting, result);
    assertEquals("""
        line 1:3 reportAttemptingFullContext d=0 (e), input='34abc'
        line 1:0 reportContextSensitivity d=0 (e), input='34'
        """,
      this.stderrDuringParse);
  }

  @Test
  @Disabled("Переделать на ANTLR runtime/Generator")
  public void testSLLSeesEOFInLLGrammarWithDFA() {
    String grammar =
      """
        grammar T;
        s @init{getInterpreter().enable_global_context_dfa = true;} @after {dumpDFA();}
          : a ;
        a : e ID ;
        b : e INT ID ;
        e : INT | ;
        ID : 'a'..'z'+ ;
        INT : '0'..'9'+ ;
        WS : (' '|'\\t'|'\\n')+ -> skip ;
        """;
    String result = execParser("T.g4", grammar, "TParser", "TLexer", "s",
      "34 abc", true);
    String expecting =
      """
        Decision 0:
        s0-INT->s1
        s1-ID->:s2=>1
        s3**-ctx:11(a)->s4
        s4-INT->:s5=>1
        """; // Must point at accept state
    assertEquals(expecting, result);
    assertEquals("""
        line 1:3 reportAttemptingFullContext d=0 (e), input='34abc'
        line 1:0 reportContextSensitivity d=0 (e), input='34'
        """,
      this.stderrDuringParse);
  }

  @Test
  @Disabled("Переделать на ANTLR runtime/Generator")
  public void testFullContextIF_THEN_ELSEParseWithoutDFA() {
    String grammar =
      """
        grammar T;
        s\
        @init {_interp.setPredictionMode(PredictionMode.LL_EXACT_AMBIG_DETECTION);}
        @after {dumpDFA();}
            : '{' stat* '}'\
            ;
        stat: 'if' ID 'then' stat ('else' ID)?
            | 'return'
            ;\
        ID : 'a'..'z'+ ;
        WS : (' '|'\\t'|'\\n')+ -> skip ;
        """;
    String input = "{ if x then return }";
    String result = execParser("T.g4", grammar, "TParser", "TLexer", "s",
      input, true);
    String expecting =
      """
        Decision 1:
        s0-'}'->:s1=>2
        """;
    assertEquals(expecting, result);
    assertEquals(null, this.stderrDuringParse);

    input = "{ if x then return else foo }";
    result = execParser("T.g4", grammar, "TParser", "TLexer", "s",
      input, true);
    expecting =
      """
        Decision 1:
        s0-'else'->:s1=>1
        """;
    assertEquals(expecting, result);
    // Technically, this input sequence is not ambiguous because else
    // uniquely predicts going into the optional subrule. else cannot
    // be matched by exiting stat since that would only match '}' or
    // the start of a stat. But, we are using the theory that
    // SLL(1)=LL(1) and so we are avoiding full context parsing
    // by declaring all else clause parsing to be ambiguous.
    assertEquals("""
        line 1:19 reportAttemptingFullContext d=1 (stat), input='else'
        line 1:19 reportContextSensitivity d=1 (stat), input='else'
        """,
      this.stderrDuringParse);

    input =
      "{ if x then if y then return else foo }";
    result = execParser("T.g4", grammar, "TParser", "TLexer", "s",
      input, true);
    expecting =
      """
        Decision 1:
        s0-'}'->:s2=>2
        s0-'else'->:s1=>1
        """;
    assertEquals(expecting, result);
    assertEquals("""
        line 1:29 reportAttemptingFullContext d=1 (stat), input='else'
        line 1:38 reportAmbiguity d=1 (stat): ambigAlts={1, 2}, input='elsefoo}'
        """,
      this.stderrDuringParse);

    // should not be ambiguous because the second 'else bar' clearly
    // indicates that the first else should match to the innermost if.
    // LL_EXACT_AMBIG_DETECTION makes us keep going to resolve

    input =
      "{ if x then if y then return else foo else bar }";
    result = execParser("T.g4", grammar, "TParser", "TLexer", "s",
      input, true);
    expecting =
      """
        Decision 1:
        s0-'else'->:s1=>1
        """;
    assertEquals(expecting, result);
    assertEquals("""
        line 1:29 reportAttemptingFullContext d=1 (stat), input='else'
        line 1:38 reportContextSensitivity d=1 (stat), input='elsefooelse'
        line 1:38 reportAttemptingFullContext d=1 (stat), input='else'
        line 1:38 reportContextSensitivity d=1 (stat), input='else'
        """,
      this.stderrDuringParse);

    input =
      "{ if x then return else foo\n" +
        "if x then if y then return else foo }";
    result = execParser("T.g4", grammar, "TParser", "TLexer", "s",
      input, true);
    expecting =
      """
        Decision 1:
        s0-'}'->:s2=>2
        s0-'else'->:s1=>1
        """;
    assertEquals(expecting, result);
    assertEquals("""
        line 1:19 reportAttemptingFullContext d=1 (stat), input='else'
        line 1:19 reportContextSensitivity d=1 (stat), input='else'
        line 2:27 reportAttemptingFullContext d=1 (stat), input='else'
        line 2:36 reportAmbiguity d=1 (stat): ambigAlts={1, 2}, input='elsefoo}'
        """,
      this.stderrDuringParse);

    input =
      "{ if x then return else foo\n" +
        "if x then if y then return else foo }";
    result = execParser("T.g4", grammar, "TParser", "TLexer", "s",
      input, true);
    expecting =
      """
        Decision 1:
        s0-'}'->:s2=>2
        s0-'else'->:s1=>1
        """;
    assertEquals(expecting, result);
    assertEquals("""
        line 1:19 reportAttemptingFullContext d=1 (stat), input='else'
        line 1:19 reportContextSensitivity d=1 (stat), input='else'
        line 2:27 reportAttemptingFullContext d=1 (stat), input='else'
        line 2:36 reportAmbiguity d=1 (stat): ambigAlts={1, 2}, input='elsefoo}'
        """,
      this.stderrDuringParse);
  }

  @Test
  @Disabled("Переделать на ANTLR runtime/Generator")
  public void testFullContextIF_THEN_ELSEParseWithDFA() {
    String grammar =
      """
        grammar T;
        s\
        @init {getInterpreter().enable_global_context_dfa = true; _interp.setPredictionMode(PredictionMode.LL_EXACT_AMBIG_DETECTION);}
        @after {dumpDFA();}
            : '{' stat* '}'\
            ;
        stat: 'if' ID 'then' stat ('else' ID)?
            | 'return'
            ;\
        ID : 'a'..'z'+ ;
        WS : (' '|'\\t'|'\\n')+ -> skip ;
        """;
    String input = "{ if x then return }";
    String result = execParser("T.g4", grammar, "TParser", "TLexer", "s",
      input, true);
    String expecting =
      """
        Decision 1:
        s0-'}'->:s1=>2
        """;
    assertEquals(expecting, result);
    assertEquals(null, this.stderrDuringParse);

    input = "{ if x then return else foo }";
    result = execParser("T.g4", grammar, "TParser", "TLexer", "s",
      input, true);
    expecting =
      """
        Decision 1:
        s0-'else'->:s1=>1
        s2**-ctx:7(s)->s3
        s3-'else'->:s4=>1
        """;
    assertEquals(expecting, result);
    // Technically, this input sequence is not ambiguous because else
    // uniquely predicts going into the optional subrule. else cannot
    // be matched by exiting stat since that would only match '}' or
    // the start of a stat. But, we are using the theory that
    // SLL(1)=LL(1) and so we are avoiding full context parsing
    // by declaring all else clause parsing to be ambiguous.
    assertEquals("""
        line 1:19 reportAttemptingFullContext d=1 (stat), input='else'
        line 1:19 reportContextSensitivity d=1 (stat), input='else'
        """,
      this.stderrDuringParse);

    input =
      "{ if x then if y then return else foo }";
    result = execParser("T.g4", grammar, "TParser", "TLexer", "s",
      input, true);
    expecting =
      """
        Decision 1:
        s0-'}'->:s8=>2
        s0-'else'->:s1=>1
        s2**-ctx:19(stat)->s3**
        s3**-ctx:7(s)->s4
        s4-'else'->s5
        s5-ID->:s6=>1
        :s6=>1-'}'->:s7=>1
        """;
    assertEquals(expecting, result);
    assertEquals("""
        line 1:29 reportAttemptingFullContext d=1 (stat), input='else'
        line 1:38 reportAmbiguity d=1 (stat): ambigAlts={1, 2}, input='elsefoo}'
        """,
      this.stderrDuringParse);

    // should not be ambiguous because the second 'else bar' clearly
    // indicates that the first else should match to the innermost if.
    // LL_EXACT_AMBIG_DETECTION makes us keep going to resolve

    input =
      "{ if x then if y then return else foo else bar }";
    result = execParser("T.g4", grammar, "TParser", "TLexer", "s",
      input, true);
    expecting =
      """
        Decision 1:
        s0-'else'->:s1=>1
        s2**-ctx:7(s)->s8
        s2**-ctx:19(stat)->s3**
        s3**-ctx:7(s)->s4
        s4-'else'->s5
        s5-ID->:s6=>1
        :s6=>1-'else'->:s7=>1
        s8-'else'->:s7=>1
        """;
    assertEquals(expecting, result);
    assertEquals("""
        line 1:29 reportAttemptingFullContext d=1 (stat), input='else'
        line 1:38 reportContextSensitivity d=1 (stat), input='elsefooelse'
        line 1:38 reportAttemptingFullContext d=1 (stat), input='else'
        line 1:38 reportContextSensitivity d=1 (stat), input='else'
        """,
      this.stderrDuringParse);

    input =
      "{ if x then return else foo\n" +
        "if x then if y then return else foo }";
    result = execParser("T.g4", grammar, "TParser", "TLexer", "s",
      input, true);
    expecting =
      """
        Decision 1:
        s0-'}'->:s10=>2
        s0-'else'->:s1=>1
        s2**-ctx:7(s)->s3
        s2**-ctx:19(stat)->s5**
        s3-'else'->:s4=>1
        s5**-ctx:7(s)->s6
        s6-'else'->s7
        s7-ID->:s8=>1
        :s8=>1-'}'->:s9=>1
        """;
    assertEquals(expecting, result);
    assertEquals("""
        line 1:19 reportAttemptingFullContext d=1 (stat), input='else'
        line 1:19 reportContextSensitivity d=1 (stat), input='else'
        line 2:27 reportAttemptingFullContext d=1 (stat), input='else'
        line 2:36 reportAmbiguity d=1 (stat): ambigAlts={1, 2}, input='elsefoo}'
        """,
      this.stderrDuringParse);

    input =
      "{ if x then return else foo\n" +
        "if x then if y then return else foo }";
    result = execParser("T.g4", grammar, "TParser", "TLexer", "s",
      input, true);
    expecting =
      """
        Decision 1:
        s0-'}'->:s10=>2
        s0-'else'->:s1=>1
        s2**-ctx:7(s)->s3
        s2**-ctx:19(stat)->s5**
        s3-'else'->:s4=>1
        s5**-ctx:7(s)->s6
        s6-'else'->s7
        s7-ID->:s8=>1
        :s8=>1-'}'->:s9=>1
        """;
    assertEquals(expecting, result);
    assertEquals("""
        line 1:19 reportAttemptingFullContext d=1 (stat), input='else'
        line 1:19 reportContextSensitivity d=1 (stat), input='else'
        line 2:27 reportAttemptingFullContext d=1 (stat), input='else'
        line 2:36 reportAmbiguity d=1 (stat): ambigAlts={1, 2}, input='elsefoo}'
        """,
      this.stderrDuringParse);
  }

  /**
   * Tests predictions for the following case involving closures.
   * <a href="http://www.antlr.org/wiki/display/~admin/2011/12/29/Flaw+in+ANTLR+v3+LL">...</a>(*)+analysis+algorithm
   */
  @Test
  @Disabled("Переделать на ANTLR runtime/Generator")
  public void testLoopsSimulateTailRecursion() {
    String grammar =
      """
        grammar T;
        prog
        @init {_interp.setPredictionMode(PredictionMode.LL_EXACT_AMBIG_DETECTION);}
            : expr_or_assign*;
        expr_or_assign
            :   expr '++' {System.out.println("fail.");}
            |   expr {System.out.println("pass: "+$expr.text);}
            ;
        expr: expr_primary ('<-' ID)? ;
        expr_primary
            : '(' ID ')'
            | ID '(' ID ')'
            | ID
            ;
        ID  : [a-z]+ ;
        """;

    String found = execParser("T.g4", grammar, "TParser", "TLexer", "prog", "a(i)<-x", true);
    assertEquals("pass: a(i)<-x\n", found);

    String expecting =
      """
        line 1:3 reportAttemptingFullContext d=3 (expr_primary), input='a(i)'
        line 1:7 reportAmbiguity d=3 (expr_primary): ambigAlts={2, 3}, input='a(i)<-x'
        """;
    assertEquals(expecting, this.stderrDuringParse);
  }

  @Test
  @Disabled("Переделать на ANTLR runtime/Generator")
  public void testAmbiguityNoLoop() {
    // simpler version of testLoopsSimulateTailRecursion, no loops
    String grammar =
      """
        grammar T;
        prog
        @init {_interp.setPredictionMode(PredictionMode.LL_EXACT_AMBIG_DETECTION);}
            : expr expr {System.out.println("alt 1");}
            | expr
            ;
        expr: '@'
            | ID '@'
            | ID
            ;
        ID  : [a-z]+ ;
        WS  : [ \\r\\n\\t]+ -> skip ;
        """;

    String found = execParser("T.g4", grammar, "TParser", "TLexer", "prog", "a@", true);
    assertEquals("alt 1\n", found);

    String expecting =
      """
        line 1:1 reportAttemptingFullContext d=0 (prog), input='a@'
        line 1:2 reportAmbiguity d=0 (prog): ambigAlts={1, 2}, input='a@'
        line 1:1 reportAttemptingFullContext d=1 (expr), input='a@'
        line 1:2 reportContextSensitivity d=1 (expr), input='a@'
        """;
    assertEquals(expecting, this.stderrDuringParse);
  }

  @Test
  @Disabled("Переделать на ANTLR runtime/Generator")
  public void testExprAmbiguity() {
    // translated left-recursive expr rule to test ambig detection
    String grammar =
      """
        grammar T;
        s
        @init {_interp.setPredictionMode(PredictionMode.LL_EXACT_AMBIG_DETECTION);}
            :   expr[0] {System.out.println($expr.ctx.toStringTree(this));} ;
        
        expr[int _p]
            :   ID
                ( {5 >= $_p}? '*' expr[6]
                | {4 >= $_p}? '+' expr[5]
                )*
            ;
        
        ID  :   [a-zA-Z]+ ;      // match identifiers
        WS  :   [ \\t\\r\\n]+ -> skip ; // toss out whitespace
        """;

    String found = execParser("T.g4", grammar, "TParser", "TLexer", "s", "a+b", true);
    assertEquals("(expr a + (expr b))\n", found);

    String expecting =
      """
        line 1:1 reportAttemptingFullContext d=1 (expr), input='+'
        line 1:2 reportContextSensitivity d=1 (expr), input='+b'
        """;
    assertEquals(expecting, this.stderrDuringParse);

    found = execParser("T.g4", grammar, "TParser", "TLexer", "s", "a+b*c", true);
    assertEquals("(expr a + (expr b * (expr c)))\n", found);

    expecting =
      """
        line 1:1 reportAttemptingFullContext d=1 (expr), input='+'
        line 1:2 reportContextSensitivity d=1 (expr), input='+b'
        line 1:3 reportAttemptingFullContext d=1 (expr), input='*'
        line 1:5 reportAmbiguity d=1 (expr): ambigAlts={1, 2}, input='*c'
        """;
    assertEquals(expecting, this.stderrDuringParse);
  }

}
