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

import org.antlr.v4.runtime.atn.ParserATNSimulator;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.antlr.v4.TestUtils.assertEquals;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test parser execution.
 * <p>
 * For the non-greedy stuff, the rule is that .* or any other non-greedy loop
 * (any + or * loop that has an alternative with '.' in it is automatically
 * non-greedy) never sees past the end of the rule containing that loop.
 * There is no automatic way to detect when the exit branch of a non-greedy
 * loop has seen enough input to determine how much the loop should consume
 * yet still allow matching the entire input. Of course, this is extremely
 * inefficient, particularly for things like
 * <p>
 * block : '{' (block|.)* '}' ;
 * <p>
 * that need only see one symbol to know when it hits a '}'. So, I
 * came up with a practical solution.  During prediction, the ATN
 * simulator never fall off the end of a rule to compute the global
 * FOLLOW. Instead, we terminate the loop, choosing the exit branch.
 * Otherwise, we predict to reenter the loop.  For example, input
 * "{ foo }" will allow the loop to match foo, but that's it. During
 * prediction, the ATN simulator will see that '}' reaches the end of a
 * rule that contains a non-greedy loop and stop prediction. It will choose
 * the exit branch of the inner loop. So, the way in which you construct
 * the rule containing a non-greedy loop dictates how far it will scan ahead.
 * Include everything after the non-greedy loop that you know it must scan
 * in order to properly make a prediction decision. these beasts are tricky,
 * so be careful. don't liberally sprinkle them around your code.
 * <p>
 * To simulate filter mode, use ( .* (pattern1|pattern2|...) )*
 * <p>
 * Nongreedy loops match as much input as possible while still allowing
 * the remaining input to match.
 */
public class ParserExecTest extends AbstractBaseTest {
  @Test
  @Disabled("Переделать на ANTLR runtime/Generator")
  public void testLabels() {
    String grammar =
      """
        grammar T;
        a : b1=b b2+=b* b3+=';' ;
        b : id=ID val+=INT*;
        ID : 'a'..'z'+ ;
        INT : '0'..'9'+;
        WS : (' '|'\\n') -> skip ;
        """;

    String found = execParser("T.g4", grammar, "TParser", "TLexer", "a",
      "abc 34;", false);
    assertEquals("", found);
    assertEquals(null, stderrDuringParse);
  }

  /**
   * This is a regression test for #270 "Fix operator += applied to a set of
   * tokens".
   * <a href="https://github.com/antlr/antlr4/issues/270">...</a>
   */
  @Test
  @Disabled("Переделать на ANTLR runtime/Generator")
  public void testListLabelOnSet() {
    String grammar =
      """
        grammar T;
        a : b b* ';' ;
        b : ID val+=(INT | FLOAT)*;
        ID : 'a'..'z'+ ;
        INT : '0'..'9'+;
        FLOAT : [0-9]+ '.' [0-9]+;
        WS : (' '|'\\n') -> skip ;
        """;

    String found = execParser("T.g4", grammar, "TParser", "TLexer", "a",
      "abc 34;", false);
    assertEquals("", found);
    assertEquals(null, stderrDuringParse);
  }

  @Test
  @Disabled("Переделать на ANTLR runtime/Generator")
  public void testBasic() {
    String grammar =
      """
        grammar T;
        a : ID INT {System.out.println($text);} ;
        ID : 'a'..'z'+ ;
        INT : '0'..'9'+;
        WS : (' '|'\\n') -> skip ;
        """;

    String found = execParser("T.g4", grammar, "TParser", "TLexer", "a",
      "abc 34", false);
    assertEquals("abc34\n", found);
  }

  @Test
  @Disabled("Переделать на ANTLR runtime/Generator")
  public void testAorB() {
    String grammar =
      """
        grammar T;
        a : ID {System.out.println(" alt 1");}\
          | INT {System.out.println("alt 2");}\
        ;
        ID : 'a'..'z'+ ;
        INT : '0'..'9'+;
        WS : (' '|'\\n') -> skip ;
        """;

    String found = execParser("T.g4", grammar, "TParser", "TLexer", "a",
      "34", false);
    assertEquals("alt 2\n", found);
  }

  @Test
  @Disabled("Переделать на ANTLR runtime/Generator")
  public void testAPlus() {
    String grammar =
      """
        grammar T;
        a : ID+ {System.out.println($text);} ;
        ID : 'a'..'z'+ ;
        WS : (' '|'\\n') -> skip ;
        """;

    String found = execParser("T.g4", grammar, "TParser", "TLexer", "a",
      "a b c", false);
    assertEquals("abc\n", found);
  }

  // force complex decision
  @Test
  @Disabled("Переделать на ANTLR runtime/Generator")
  public void testAorAPlus() {
    String grammar =
      """
        grammar T;
        a : (ID|ID)+ {System.out.println($text);} ;
        ID : 'a'..'z'+ ;
        WS : (' '|'\\n') -> skip ;
        """;

    String found = execParser("T.g4", grammar, "TParser", "TLexer", "a",
      "a b c", false);
    assertEquals("abc\n", found);
  }

  private static final String ifIfElseGrammarFormat =
    """
      grammar T;
      start : statement+ ;
      statement : 'x' | ifStatement;
      ifStatement : 'if' 'y' statement %s {System.out.println($text);};
      ID : 'a'..'z'+ ;
      WS : (' '|'\\n') -> channel(HIDDEN);
      """;

  @Test
  @Disabled("Переделать на ANTLR runtime/Generator")
  public void testIfIfElseGreedyBinding1() {
    final String input = "if y if y x else x";
    final String expectedInnerBound = "if y x else x\nif y if y x else x\n";

    String grammar = String.format(ifIfElseGrammarFormat, "('else' statement)?");
    String found = execParser("T.g4", grammar, "TParser", "TLexer", "start", input, false);
    assertEquals(expectedInnerBound, found);

  }

  @Test
  @Disabled("Переделать на ANTLR runtime/Generator")
  public void testIfIfElseGreedyBinding2() {
    final String input = "if y if y x else x";
    final String expectedInnerBound = "if y x else x\nif y if y x else x\n";

    String grammar = String.format(ifIfElseGrammarFormat, "('else' statement|)");
    String found = execParser("T.g4", grammar, "TParser", "TLexer", "start", input, false);
    assertEquals(expectedInnerBound, found);
  }

  @Test
  @Disabled("Переделать на ANTLR runtime/Generator")
  public void testIfIfElseNonGreedyBinding() {
    final String input = "if y if y x else x";
    final String expectedOuterBound = "if y x\nif y if y x else x\n";

    String grammar = String.format(ifIfElseGrammarFormat, "('else' statement)??");
    String found = execParser("T.g4", grammar, "TParser", "TLexer", "start", input, false);
    assertEquals(expectedOuterBound, found);

    grammar = String.format(ifIfElseGrammarFormat, "(|'else' statement)");
    found = execParser("T.g4", grammar, "TParser", "TLexer", "start", input, false);
    assertEquals(expectedOuterBound, found);
  }

  @Test
  @Disabled("Переделать на ANTLR runtime/Generator")
  public void testAStar() {
    String grammar =
      """
        grammar T;
        a : ID* {System.out.println($text);} ;
        ID : 'a'..'z'+ ;
        WS : (' '|'\\n') -> skip ;
        """;

    String found = execParser("T.g4", grammar, "TParser", "TLexer", "a",
      "", false);
    assertEquals("\n", found);
    found = execParser("T.g4", grammar, "TParser", "TLexer", "a",
      "a b c", false);
    assertEquals("abc\n", found);
  }

  @Test
  @Disabled("Переделать на ANTLR runtime/Generator")
  public void testLL1OptionalBlock() {
    String grammar =
      """
        grammar T;
        a : (ID|{}INT)? {System.out.println($text);} ;
        ID : 'a'..'z'+ ;
        INT : '0'..'9'+ ;
        WS : (' '|'\\n') -> skip ;
        """;

    String found = execParser("T.g4", grammar, "TParser", "TLexer", "a",
      "", false);
    assertEquals("\n", found);
    found = execParser("T.g4", grammar, "TParser", "TLexer", "a",
      "a", false);
    assertEquals("a\n", found);
  }

  // force complex decision
  @Test
  @Disabled("Переделать на ANTLR runtime/Generator")
  public void testAorAStar() {
    String grammar =
      """
        grammar T;
        a : (ID|ID)* {System.out.println($text);} ;
        ID : 'a'..'z'+ ;
        WS : (' '|'\\n') -> skip ;
        """;

    String found = execParser("T.g4", grammar, "TParser", "TLexer", "a",
      "", false);
    assertEquals("\n", found);
    found = execParser("T.g4", grammar, "TParser", "TLexer", "a",
      "a b c", false);
    assertEquals("abc\n", found);
  }

  @Test
  @Disabled("Переделать на ANTLR runtime/Generator")
  public void testAorBPlus() {
    String grammar =
      """
        grammar T;
        a : (ID|INT{;})+ {System.out.println($text);} ;
        ID : 'a'..'z'+ ;
        INT : '0'..'9'+;
        WS : (' '|'\\n') -> skip ;
        """;

    String found = execParser("T.g4", grammar, "TParser", "TLexer", "a",
      "a 34 c", false);
    assertEquals("a34c\n", found);
  }

  @Test
  @Disabled("Переделать на ANTLR runtime/Generator")
  public void testAorBStar() {
    String grammar =
      """
        grammar T;
        a : (ID|INT{;})* {System.out.println($text);} ;
        ID : 'a'..'z'+ ;
        INT : '0'..'9'+;
        WS : (' '|'\\n') -> skip ;
        """;

    String found = execParser("T.g4", grammar, "TParser", "TLexer", "a",
      "", false);
    assertEquals("\n", found);
    found = execParser("T.g4", grammar, "TParser", "TLexer", "a",
      "a 34 c", false);
    assertEquals("a34c\n", found);
  }


  /**
   * This test is meant to detect regressions of bug antlr/antlr4#41.
   * <a href="https://github.com/antlr/antlr4/issues/41">...</a>
   */
  @Test
  @Disabled("Переделать на ANTLR runtime/Generator")
  public void testOptional1() {
    String grammar =
      """
        grammar T;
        stat : ifstat | 'x';
        ifstat : 'if' stat ('else' stat)?;
        WS : [ \\n\\t]+ -> skip ;""";

    String found = execParser("T.g4", grammar, "TParser", "TLexer", "stat", "x", false);
    assertEquals("", found);
    assertThat(this.stderrDuringParse).isNull();
  }

  @Test
  @Disabled("Переделать на ANTLR runtime/Generator")
  public void testOptional2() {
    String grammar =
      """
        grammar T;
        stat : ifstat | 'x';
        ifstat : 'if' stat ('else' stat)?;
        WS : [ \\n\\t]+ -> skip ;""";

    String found = execParser("T.g4", grammar, "TParser", "TLexer", "stat", "if x else x", false);
    assertEquals("", found);
    assertThat(this.stderrDuringParse).isNull();
  }

  @Test
  @Disabled("Переделать на ANTLR runtime/Generator")
  public void testOptional3() {
    String grammar =
      """
        grammar T;
        stat : ifstat | 'x';
        ifstat : 'if' stat ('else' stat)?;
        WS : [ \\n\\t]+ -> skip ;""";

    String found = execParser("T.g4", grammar, "TParser", "TLexer", "stat", "if x", false);
    assertEquals("", found);
    assertThat(this.stderrDuringParse).isNull();
  }

  @Test
  @Disabled("Переделать на ANTLR runtime/Generator")
  public void testOptional4() {
    String grammar =
      """
        grammar T;
        stat : ifstat | 'x';
        ifstat : 'if' stat ('else' stat)?;
        WS : [ \\n\\t]+ -> skip ;""";

    String found = execParser("T.g4", grammar, "TParser", "TLexer", "stat", "if if x else x", false);
    assertEquals("", found);
    assertThat(this.stderrDuringParse).isNull();
  }

  /**
   * This test is meant to test the expected solution to antlr/antlr4#42.
   * <a href="https://github.com/antlr/antlr4/issues/42">...</a>
   */
  @Test
  @Disabled("Переделать на ANTLR runtime/Generator")
  public void testPredicatedIfIfElse() {
    String grammar =
      """
        grammar T;
        stmt : ifStmt | ID;
        ifStmt : 'if' ID stmt (options{sll=true;} : 'else' stmt | );
        ELSE : 'else';
        ID : [a-zA-Z]+;
        WS : [ \\n\\t]+ -> skip;
        """;

    String found = execParser("T.g4", grammar, "TParser", "TLexer", "stmt",
      "if x if x a else b", true);
    String expecting = "";
    assertEquals(expecting, found);
    assertThat(this.stderrDuringParse).isNull();
  }

  /**
   * This is a regression test for antlr/antlr4#195 "label 'label' type
   * mismatch with previous definition: TOKEN_LABEL!=RULE_LABEL"
   * <a href="https://github.com/antlr/antlr4/issues/195">...</a>
   */
  @Test
  @Disabled("Переделать на ANTLR runtime/Generator")
  public void testLabelAliasingAcrossLabeledAlternatives() {
    String grammar =
      """
        grammar T;
        start : a* EOF;
        a
          : label=subrule {System.out.println($label.text);} #One
          | label='y' {System.out.println($label.text);} #Two
          ;
        subrule : 'x';
        WS : (' '|'\\n') -> skip ;
        """;

    String found = execParser("T.g4", grammar, "TParser", "TLexer", "start",
      "xy", false);
    assertEquals("x\ny\n", found);
  }

  /**
   * This is a regression test for antlr/antlr4#334 "BailErrorStrategy: bails
   * out on proper input".
   * <a href="https://github.com/antlr/antlr4/issues/334">...</a>
   */
  @Test
  @Disabled("Переделать на ANTLR runtime/Generator")
  public void testPredictionIssue334() {
    String grammar =
      """
        grammar T;
        
        file @init{setErrorHandler(new BailErrorStrategy());}\s
        @after {System.out.println($ctx.toStringTree(this));}
          :   item (SEMICOLON item)* SEMICOLON? EOF ;
        item : A B?;
        
        
        
        SEMICOLON: ';';
        
        A : 'a'|'A';
        B : 'b'|'B';
        
        WS      : [ \\r\\t\\n]+ -> skip;
        """;

    String input = "a";
    String found = execParser("T.g4", grammar, "TParser", "TLexer", "file", input, false);
    assertEquals("(file (item a) <EOF>)\n", found);
    assertThat(stderrDuringParse).isNull();
  }

  /**
   * This is a regressino test for antlr/antlr4#299 "Repeating subtree not
   * accessible in visitor".
   * <a href="https://github.com/antlr/antlr4/issues/299">...</a>
   */
  @Test
  @Disabled("Переделать на ANTLR runtime/Generator")
  public void testListLabelForClosureContext() {
    String grammar =
      """
        grammar T;
        ifStatement
        @after { List<? extends ElseIfStatementContext> items = $ctx.elseIfStatement(); }
            : 'if' expression
              ( ( 'then'
                  executableStatement*
                  elseIfStatement*  // <--- problem is here
                  elseStatement?
                  'end' 'if'
                ) | executableStatement )
            ;
        
        elseIfStatement
            : 'else' 'if' expression 'then' executableStatement*
            ;
        expression : 'a' ;
        executableStatement : 'a' ;
        elseStatement : 'a' ;
        """;
    String input = "a";
    String found = execParser("T.g4", grammar, "TParser", "TLexer", "expression", input, false);
    assertEquals("", found);
    assertThat(stderrDuringParse).isNull();
  }

  /**
   * This test ensures that {@link ParserATNSimulator} produces a correct
   * result when the grammar contains multiple explicit references to
   * {@code EOF} inside of parser rules.
   */
  @Test
  @Disabled("Переделать на ANTLR runtime/Generator")
  public void testMultipleEOFHandling() {
    String grammar =
      """
        grammar T;
        prog : ('x' | 'x' 'y') EOF EOF;
        """;
    String input = "x";
    String found = execParser("T.g4", grammar, "TParser", "TLexer", "prog", input, false);
    assertEquals("", found);
    assertThat(stderrDuringParse).isNull();
  }

  /**
   * This test ensures that {@link ParserATNSimulator} does not produce a
   * {@link StackOverflowError} when it encounters an {@code EOF} transition
   * inside a closure.
   */
  @Test
  @Disabled("Переделать на ANTLR runtime/Generator")
  public void testEOFInClosure() {
    String grammar =
      """
        grammar T;
        prog : stat EOF;
        stat : 'x' ('y' | EOF)*?;
        """;
    String input = "x";
    String found = execParser("T.g4", grammar, "TParser", "TLexer", "prog", input, false);
    assertEquals("", found);
    assertThat(stderrDuringParse).isNull();
  }

  /**
   * This is a regression test for antlr/antlr4#561 "Issue with parser
   * generation in 4.2.2"
   * <a href="https://github.com/antlr/antlr4/issues/561">...</a>
   */
  @Test
  @Disabled("Переделать на ANTLR runtime/Generator")
  public void testReferenceToATN() {
    String grammar =
      """
        grammar T;
        a : (ID|ATN)* ATN? {System.out.println($text);} ;
        ID : 'a'..'z'+ ;
        ATN : '0'..'9'+;
        WS : (' '|'\\n') -> skip ;
        """;

    String found = execParser("T.g4", grammar, "TParser", "TLexer", "a",
      "", false);
    assertEquals("\n", found);
    found = execParser("T.g4", grammar, "TParser", "TLexer", "a",
      "a 34 c", false);
    assertEquals("a34c\n", found);
  }

  /**
   * This is a regression test for antlr/antlr4#588 "ClassCastException during
   * semantic predicate handling".
   * <a href="https://github.com/antlr/antlr4/issues/588">...</a>
   */
  @Test
  @Disabled("Переделать на ANTLR runtime/Generator")
  public void testFailedPredicateExceptionState() throws IOException {
    String grammar = load("Psl.g4", "UTF-8");
    String found = execParser("Psl.g4", grammar, "PslParser", "PslLexer", "floating_constant", " . 234", false);
    assertEquals("", found);
    assertEquals("line 1:6 rule floating_constant DEC:A floating-point constant cannot have internal white space\n", stderrDuringParse);
  }

  /**
   * This is a regression test for antlr/antlr4#563 "Inconsistent token
   * handling in ANTLR4".
   * <a href="https://github.com/antlr/antlr4/issues/563">...</a>
   */
  @Test
  @Disabled("Переделать на ANTLR runtime/Generator")
  public void testAlternateQuotes() {
    String lexerGrammar =
      """
        lexer grammar ModeTagsLexer;
        
        // Default mode rules (the SEA)
        OPEN  : '«'     -> mode(ISLAND) ;       // switch to ISLAND mode
        TEXT  : ~'«'+ ;                         // clump all text together
        
        mode ISLAND;
        CLOSE : '»'     -> mode(DEFAULT_MODE) ; // back to SEA mode\s
        SLASH : '/' ;
        ID    : [a-zA-Z]+ ;                     // match/send ID in tag to parser
        """;
    String parserGrammar =
      """
        parser grammar ModeTagsParser;
        
        options { tokenVocab=ModeTagsLexer; } // use tokens from ModeTagsLexer.g4
        
        file: (tag | TEXT)* ;
        
        tag : '«' ID '»'
            | '«' '/' ID '»'
            ;""";

    boolean success = rawGenerateAndBuildRecognizer("ModeTagsLexer.g4",
      lexerGrammar,
      null,
      "ModeTagsLexer");
    assertThat(success).isTrue();

    String found = execParser("ModeTagsParser.g4", parserGrammar, "ModeTagsParser", "ModeTagsLexer", "file", "", false);
    assertEquals("", found);
    assertThat(stderrDuringParse).isNull();
  }

  /**
   * This is a regression test for tunnelvisionlabs/antlr4cs#71 "Erroneous
   * extraneous input detected in C# (but not in Java)".
   * <a href="https://github.com/tunnelvisionlabs/antlr4cs/issues/71">...</a>
   */
  @Test
  @Disabled("Переделать на ANTLR runtime/Generator")
  public void testCSharpIssue71() {
    String grammar =
      """
        grammar Expr;
        
        root
        	:	assignment EOF
        	;
        
        assignment
        	:	LOCAL_VARIABLE '=' expression
        	;
        
        expression
        	:	logical_and_expression
        	;
        
        logical_and_expression
        	:	relational_expression ('AND' relational_expression)*
        	;
        
        relational_expression
        	:	primary_expression (('<'|'>') primary_expression)*
        	;
        
        primary_expression
        	:	'(' + expression + ')'
        	|	UNSIGNED_INT
        	|	LOCAL_VARIABLE
        	;
        
        LOCAL_VARIABLE
        	:	[_a-z][_a-zA-Z0-9]*
        	;
        
        UNSIGNED_INT
        	:	('0'|'1'..'9''0'..'9'*)
        	;
        
        WS
        	:	[ \\t\\r\\n]+ -> channel(HIDDEN)
        	;
        """;

    String input = "b = (((a > 10)) AND ((a < 15)))";
    String found = execParser("Expr.g4", grammar, "ExprParser", "ExprLexer", "root",
      input, false);
    assertEquals("", found);
    assertThat(stderrDuringParse).isNull();
  }

  /**
   * This is a regression test for antlr/antlr4#672 "Initialization failed in
   * locals".
   * <a href="https://github.com/antlr/antlr4/issues/672">...</a>
   */
  @Test
  @Disabled("Переделать на ANTLR runtime/Generator")
  public void testAttributeValueInitialization() {
    String grammar =
      """
        grammar Data;\s
        
        file : group+ EOF;\s
        
        group: INT sequence {System.out.println($sequence.values.size());} ;\s
        
        sequence returns [List<Integer> values = new ArrayList<Integer>()]\s
          locals[List<Integer> localValues = new ArrayList<Integer>()]
                 : (INT {$localValues.add($INT.int);})* {$values.addAll($localValues);}
        ;\s
        
        INT : [0-9]+ ; // match integers\s
        WS : [ \\t\\n\\r]+ -> skip ; // toss out all whitespace
        """;

    String input = "2 9 10 3 1 2 3";
    String found = execParser("Data.g4", grammar, "DataParser", "DataLexer", "file", input, false);
    assertEquals("6\n", found);
    assertThat(stderrDuringParse).isNull();
  }

  @Test
  @Disabled("Переделать на ANTLR runtime/Generator")
  public void testParserProperty() {
    String grammar =
      """
        grammar T;
        @members {
        boolean Property() {
        	return true;
        }
        }
        a : {$parser.Property()}? ID {System.out.println("valid");}
          ;
        ID : 'a'..'z'+ ;
        WS : (' '|'\\n') -> skip ;""";
    String found = execParser("T.g4", grammar, "TParser", "TLexer", "a", "abc", false);
    assertEquals("valid\n", found);
    assertThat(this.stderrDuringParse).isNull();
  }
}
