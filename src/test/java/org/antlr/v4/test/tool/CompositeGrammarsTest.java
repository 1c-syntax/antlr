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

import org.antlr.v4.tool.ANTLRMessage;
import org.antlr.v4.tool.ErrorType;
import org.antlr.v4.tool.Grammar;
import org.antlr.v4.tool.GrammarSemanticsMessage;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.antlr.v4.TestUtils.assertEquals;
import static org.assertj.core.api.Assertions.assertThat;

public class CompositeGrammarsTest extends AbstractBaseTest {
  protected boolean debug = false;

  @Test
  public void testImportFileLocationInSubdir() {
    String slave =
      """
        parser grammar S;
        a : B {System.out.println("S.a");} ;
        """;
    mkdir(tmpdir);
    String subdir = tmpdir + "/sub";
    mkdir(subdir);
    writeFile(subdir, "S.g4", slave);
    // defines B from inherited token space
    String master =
      """
        grammar M;
        import S;
        s : a ;
        B : 'b' ;\
        WS : (' '|'\\n') -> skip ;
        """;
    writeFile(tmpdir, "M.g4", master);
    ErrorQueue equeue = antlr("M.g4", false, "-lib", subdir);
    assertEquals(0, equeue.size());
  }

  // Test for https://github.com/antlr/antlr4/issues/1317
  @Test
  public void testImportSelfLoop() {
    mkdir(tmpdir);
    String master =
      """
        grammar M;
        import M;
        s : 'a' ;
        """;
    writeFile(tmpdir, "M.g4", master);
    ErrorQueue equeue = antlr("M.g4", false, "-lib", tmpdir);
    assertEquals(0, equeue.size());
  }

  @Test
  public void testErrorInImportedGetsRightFilename() {
    String slave =
      """
        parser grammar S;
        a : 'a' | c;
        """;
    mkdir(tmpdir);
    writeFile(tmpdir, "S.g4", slave);
    String master =
      """
        grammar M;
        import S;
        """;
    writeFile(tmpdir, "M.g4", master);
    ErrorQueue equeue = antlr("M.g4", false, "-lib", tmpdir);
    ANTLRMessage msg = equeue.errors.get(0);
    assertEquals(ErrorType.UNDEFINED_RULE_REF, msg.getErrorType());
    assertEquals("c", msg.getArgs()[0]);
    assertEquals(2, msg.line);
    assertEquals(10, msg.charPosition);
    assertEquals("S.g4", new File(msg.fileName).getName());
  }

  @Test
  public void testImportFileNotSearchedForInOutputDir() {
    String slave =
      """
        parser grammar S;
        a : B {System.out.println("S.a");} ;
        """;
    mkdir(tmpdir);
    String outdir = tmpdir + "/out";
    mkdir(outdir);
    writeFile(outdir, "S.g4", slave);
    // defines B from inherited token space
    String master =
      """
        grammar M;
        import S;
        s : a ;
        B : 'b' ;\
        WS : (' '|'\\n') -> skip ;
        """;
    writeFile(tmpdir, "M.g4", master);
    ErrorQueue equeue = antlr("M.g4", false, "-o", outdir);
    assertEquals(ErrorType.CANNOT_FIND_IMPORTED_GRAMMAR, equeue.errors.get(0).getErrorType());
  }

  @Test
  public void testOutputDirShouldNotEffectImports() {
    String slave =
      """
        parser grammar S;
        a : B {System.out.println("S.a");} ;
        """;
    mkdir(tmpdir);
    String subdir = tmpdir + "/sub";
    mkdir(subdir);
    writeFile(subdir, "S.g4", slave);
    // defines B from inherited token space
    String master =
      """
        grammar M;
        import S;
        s : a ;
        B : 'b' ;\
        WS : (' '|'\\n') -> skip ;
        """;
    writeFile(tmpdir, "M.g4", master);
    String outdir = tmpdir + "/out";
    mkdir(outdir);
    ErrorQueue equeue = antlr("M.g4", false, "-o", outdir, "-lib", subdir);
    assertEquals(0, equeue.size());
  }

  @Test
  public void testTokensFileInOutputDirAndImportFileInSubdir() {
    String slave =
      """
        parser grammar S;
        a : B {System.out.println("S.a");} ;
        """;
    mkdir(tmpdir);
    String subdir = tmpdir + "/sub";
    mkdir(subdir);
    writeFile(subdir, "S.g4", slave);
    String parser =
      """
        parser grammar MParser;
        import S;
        options {tokenVocab=MLexer;}
        s : a ;
        """;
    writeFile(tmpdir, "MParser.g4", parser);
    // defines B from inherited token space
    String lexer =
      """
        lexer grammar MLexer;
        B : 'b' ;\
        WS : (' '|'\\n') -> skip ;
        """;
    writeFile(tmpdir, "MLexer.g4", lexer);
    String outdir = tmpdir + "/out";
    mkdir(outdir);
    ErrorQueue equeue = antlr("MLexer.g4", false, "-o", outdir);
    assertEquals(0, equeue.size());
    equeue = antlr("MParser.g4", false, "-o", outdir, "-lib", subdir);
    assertEquals(0, equeue.size());
  }

  @Test
  @Disabled("Переделать на ANTLR runtime/Generator")
  public void testDelegatorInvokesDelegateRule() {
    String slave =
      """
        parser grammar S;
        a : B {System.out.println("S.a");} ;
        """;
    mkdir(tmpdir);
    writeFile(tmpdir, "S.g4", slave);
    // defines B from inherited token space
    String master =
      """
        grammar M;
        import S;
        s : a ;
        B : 'b' ;\
        WS : (' '|'\\n') -> skip ;
        """;
    String found = execParser("M.g4", master, "MParser", "MLexer",
      "s", "b", debug);
    assertEquals("S.a\n", found);
  }

  @Test
  @Disabled("Переделать на ANTLR runtime/Generator")
  public void testBringInLiteralsFromDelegate() {
    String slave =
      """
        parser grammar S;
        a : '=' 'a' {System.out.println("S.a");} ;
        """;
    mkdir(tmpdir);
    writeFile(tmpdir, "S.g4", slave);
    String master =
      """
        grammar M;
        import S;
        s : a ;
        WS : (' '|'\\n') -> skip ;
        """;
    String found = execParser("M.g4", master, "MParser", "MLexer",
      "s", "=a", debug);
    assertEquals("S.a\n", found);
  }

  @Test
  @Disabled("Переделать на ANTLR runtime/Generator")
  public void testDelegatorInvokesDelegateRuleWithArgs() {
    // must generate something like:
    // public int a(int x) throws RecognitionException { return gS.a(x); }
    // in M.
    String slave =
      """
        parser grammar S;
        a[int x] returns [int y] : B {System.out.print("S.a"); $y=1000;} ;
        """;
    mkdir(tmpdir);
    writeFile(tmpdir, "S.g4", slave);
    // defines B from inherited token space
    String master =
      """
        grammar M;
        import S;
        s : label=a[3] {System.out.println($label.y);} ;
        B : 'b' ;\
        WS : (' '|'\\n') -> skip ;
        """;
    String found = execParser("M.g4", master, "MParser", "MLexer",
      "s", "b", debug);
    assertEquals("S.a1000\n", found);
  }

  @Test
  @Disabled("Переделать на ANTLR runtime/Generator")
  public void testDelegatorInvokesDelegateRuleWithReturnStruct() {
    // must generate something like:
    // public int a(int x) throws RecognitionException { return gS.a(x); }
    // in M.
    String slave =
      """
        parser grammar S;
        a : B {System.out.print("S.a");} ;
        """;
    mkdir(tmpdir);
    writeFile(tmpdir, "S.g4", slave);
    // defines B from inherited token space
    String master =
      """
        grammar M;
        import S;
        s : a {System.out.println($a.text);} ;
        B : 'b' ;\
        WS : (' '|'\\n') -> skip ;
        """;
    String found = execParser("M.g4", master, "MParser", "MLexer",
      "s", "b", debug);
    assertEquals("S.ab\n", found);
  }

  @Test
  @Disabled("Переделать на ANTLR runtime/Generator")
  public void testDelegatorAccessesDelegateMembers() {
    String slave =
      """
        parser grammar S;
        @parser::members {
          public void foo() {System.out.println("foo");}
        }
        a : B ;
        """;
    mkdir(tmpdir);
    writeFile(tmpdir, "S.g4", slave);
    // uses no rules from the import
    // gS is import pointer
    String master =
      """
        grammar M;
        import S;
        s : 'b' {foo();} ;
        WS : (' '|'\\n') -> skip ;
        """;
    String found = execParser("M.g4", master, "MParser", "MLexer",
      "s", "b", debug);
    assertEquals("foo\n", found);
  }

  @Test
  @Disabled("Переделать на ANTLR runtime/Generator")
  public void testDelegatorInvokesFirstVersionOfDelegateRule() {
    String slave =
      """
        parser grammar S;
        a : b {System.out.println("S.a");} ;
        b : B ;
        """;
    mkdir(tmpdir);
    writeFile(tmpdir, "S.g4", slave);
    String slave2 =
      """
        parser grammar T;
        a : B {System.out.println("T.a");} ;
        """; // hidden by S.a
    writeFile(tmpdir, "T.g4", slave2);
    String master =
      """
        grammar M;
        import S,T;
        s : a ;
        B : 'b' ;
        WS : (' '|'\\n') -> skip ;
        """;
    String found = execParser("M.g4", master, "MParser", "MLexer",
      "s", "b", debug);
    assertEquals("S.a\n", found);
  }

  @Test
  @Disabled("Переделать на ANTLR runtime/Generator")
  public void testDelegatesSeeSameTokenType() {
    // A, B, C token type order
    String slave =
      """
        parser grammar S;
        tokens { A, B, C }
        x : A {System.out.println("S.x");} ;
        """;
    mkdir(tmpdir);
    writeFile(tmpdir, "S.g4", slave);
    // reverse order
    String slave2 =
      """
        parser grammar T;
        tokens { C, B, A }
        y : A {System.out.println("T.y");} ;
        """;
    mkdir(tmpdir);
    writeFile(tmpdir, "T.g4", slave2);
    // The lexer will create rules to match letters a, b, c.
    // The associated token types A, B, C must have the same value
    // and all import'd parsers.  Since ANTLR regenerates all imports
    // for use with the delegator M, it can generate the same token type
    // mapping in each parser:
    // public static final int C=6;
    // public static final int EOF=-1;
    // public static final int B=5;
    // public static final int WS=7;
    // public static final int A=4;

    // matches AA, which should be "aa"
    // another order: B, A, C
    String master =
      """
        grammar M;
        import S,T;
        s : x y ;
        B : 'b' ;
        A : 'a' ;
        C : 'c' ;
        WS : (' '|'\\n') -> skip ;
        """;
    String found = execParser("M.g4", master, "MParser", "MLexer",
      "s", "aa", debug);
    assertEquals("""
      S.x
      T.y
      """, found);
  }

  @Test
  @Disabled("Переделать на ANTLR runtime/Generator")
  public void testDelegatesSeeSameTokenType2() throws Exception {
    ErrorQueue equeue = new ErrorQueue();
    // A, B, C token type order
    String slave =
      """
        parser grammar S;
        tokens { A, B, C }
        x : A {System.out.println("S.x");} ;
        """;
    mkdir(tmpdir);
    writeFile(tmpdir, "S.g4", slave);
    // reverse order
    String slave2 =
      """
        parser grammar T;
        tokens { C, B, A }
        y : A {System.out.println("T.y");} ;
        """;
    mkdir(tmpdir);
    writeFile(tmpdir, "T.g4", slave2);

    // matches AA, which should be "aa"
    // another order: B, A, C
    String master =
      """
        grammar M;
        import S,T;
        s : x y ;
        B : 'b' ;
        A : 'a' ;
        C : 'c' ;
        WS : (' '|'\\n') -> skip ;
        """;
    writeFile(tmpdir, "M.g4", master);
    Grammar g = new Grammar(tmpdir + "/M.g4", master, equeue);

    String expectedTokenIDToTypeMap = "{EOF=-1, B=1, A=2, C=3, WS=4}";
    String expectedStringLiteralToTypeMap = "{'a'=2, 'b'=1, 'c'=3}";
    String expectedTypeToTokenList = "[B, A, C, WS]";

    assertEquals(expectedTokenIDToTypeMap, g.tokenNameToTypeMap.toString());
    assertEquals(expectedStringLiteralToTypeMap, sort(g.stringLiteralToTypeMap).toString());
    assertEquals(expectedTypeToTokenList, realElements(g.typeToTokenList).toString());

    assertEquals("unexpected errors: " + equeue, 0, equeue.errors.size());

    String found = execParser("M.g4", master, "MParser", "MLexer",
      "s", "aa", debug);
    assertEquals("""
      S.x
      T.y
      """, found);
  }

  @Test
  @Disabled("Переделать на ANTLR runtime/Generator")
  public void testCombinedImportsCombined() throws Exception {
    ErrorQueue equeue = new ErrorQueue();
    // A, B, C token type order
    String slave =
      """
        grammar S;
        tokens { A, B, C }
        x : 'x' INT {System.out.println("S.x");} ;
        INT : '0'..'9'+ ;
        WS : (' '|'\\n') -> skip ;
        """;
    mkdir(tmpdir);
    writeFile(tmpdir, "S.g4", slave);

    String master =
      """
        grammar M;
        import S;
        s : x INT ;
        """;
    writeFile(tmpdir, "M.g4", master);
    @SuppressWarnings("unused")
    Grammar g = new Grammar(tmpdir + "/M.g4", master, equeue);

    assertEquals("unexpected errors: " + equeue, 0, equeue.errors.size());

    String found = execParser("M.g4", master, "MParser", "MLexer",
      "s", "x 34 9", debug);
    assertEquals("S.x\n", found);
  }

  @Test
  public void testImportedTokenVocabIgnoredWithWarning() throws Exception {
    ErrorQueue equeue = new ErrorQueue();
    String slave =
      """
        parser grammar S;
        options {tokenVocab=whatever;}
        tokens { A }
        x : A {System.out.println("S.x");} ;
        """;
    mkdir(tmpdir);
    writeFile(tmpdir, "S.g4", slave);

    String master =
      """
        grammar M;
        import S;
        s : x ;
        WS : (' '|'\\n') -> skip ;
        """;
    writeFile(tmpdir, "M.g4", master);
    Grammar g = new Grammar(tmpdir + "/M.g4", master, equeue);

    Object expectedArg = "S";
    ErrorType expectedMsgID = ErrorType.OPTIONS_IN_DELEGATE;
    GrammarSemanticsMessage expectedMessage =
      new GrammarSemanticsMessage(expectedMsgID, g.fileName, null, expectedArg);
    checkGrammarSemanticsWarning(equeue, expectedMessage);

    assertEquals("unexpected errors: " + equeue, 0, equeue.errors.size());
    assertEquals("unexpected warnings: " + equeue, 1, equeue.warnings.size());
  }

  @Test
  public void testSyntaxErrorsInImportsNotThrownOut() throws Exception {
    ErrorQueue equeue = new ErrorQueue();
    String slave =
      """
        parser grammar S;
        options {toke
        """;
    mkdir(tmpdir);
    writeFile(tmpdir, "S.g4", slave);

    String master =
      """
        grammar M;
        import S;
        s : x ;
        WS : (' '|'\\n') -> skip ;
        """;
    writeFile(tmpdir, "M.g4", master);
    @SuppressWarnings("unused")
    Grammar g = new Grammar(tmpdir + "/M.g4", master, equeue);

    assertEquals(ErrorType.SYNTAX_ERROR, equeue.errors.get(0).getErrorType());
  }

  @Test
  @Disabled("Переделать на ANTLR runtime/Generator")
  public void testDelegatorRuleOverridesDelegate() {
    String slave =
      """
        parser grammar S;
        a : b {System.out.println("S.a");} ;
        b : B ;
        """;
    mkdir(tmpdir);
    writeFile(tmpdir, "S.g4", slave);
    String master =
      """
        grammar M;
        import S;
        b : 'b'|'c' ;
        WS : (' '|'\\n') -> skip ;
        """;
    String found = execParser("M.g4", master, "MParser", "MLexer",
      "a", "c", debug);
    assertEquals("S.a\n", found);
  }

  @Test
  @Disabled("Переделать на ANTLR runtime/Generator")
  public void testDelegatorRuleOverridesLookaheadInDelegate() {
    String slave =
      """
        parser grammar JavaDecl;
        type : 'int' ;
        decl : type ID ';'
             | type ID init ';' {System.out.println("JavaDecl: "+$text);}
             ;
        init : '=' INT ;
        """;
    mkdir(tmpdir);
    writeFile(tmpdir, "JavaDecl.g4", slave);
    String master =
      """
        grammar Java;
        import JavaDecl;
        prog : decl ;
        type : 'int' | 'float' ;
        
        ID  : 'a'..'z'+ ;
        INT : '0'..'9'+ ;
        WS : (' '|'\\n') -> skip ;
        """;
    // for float to work in decl, type must be overridden
    String found = execParser("Java.g4", master, "JavaParser", "JavaLexer",
      "prog", "float x = 3;", debug);
    assertEquals("JavaDecl: floatx=3;\n", found);
  }

  @Test
  @Disabled("Переделать на ANTLR runtime/Generator")
  public void testDelegatorRuleOverridesDelegates() {
    String slave =
      """
        parser grammar S;
        a : b {System.out.println("S.a");} ;
        b : 'b' ;
        """;
    mkdir(tmpdir);
    writeFile(tmpdir, "S.g4", slave);

    String slave2 =
      """
        parser grammar T;
        tokens { A }
        b : 'b' {System.out.println("T.b");} ;
        """;
    writeFile(tmpdir, "T.g4", slave2);

    String master =
      """
        grammar M;
        import S, T;
        b : 'b'|'c' {System.out.println("M.b");}|B|A ;
        WS : (' '|'\\n') -> skip ;
        """;
    String found = execParser("M.g4", master, "MParser", "MLexer",
      "a", "c", debug);
    assertEquals("""
      M.b
      S.a
      """, found);
  }
  // LEXER INHERITANCE

  @Test
  @Disabled("Переделать на ANTLR runtime/Generator")
  public void testLexerDelegatorInvokesDelegateRule() {
    String slave =
      """
        lexer grammar S;
        A : 'a' {System.out.println("S.A");} ;
        C : 'c' ;
        """;
    mkdir(tmpdir);
    writeFile(tmpdir, "S.g4", slave);
    String master =
      """
        lexer grammar M;
        import S;
        B : 'b' ;
        WS : (' '|'\\n') -> skip ;
        """;
    String expecting =
      """
        S.A
        [@0,0:0='a',<3>,1:0]
        [@1,1:1='b',<1>,1:1]
        [@2,2:2='c',<4>,1:2]
        [@3,3:2='<EOF>',<-1>,1:3]
        """;
    String found = execLexer("M.g4", master, "M", "abc", debug);
    assertEquals(expecting, found);
  }

  @Test
  @Disabled("Переделать на ANTLR runtime/Generator")
  public void testLexerDelegatorRuleOverridesDelegate() {
    String slave =
      """
        lexer grammar S;
        A : 'a' {System.out.println("S.A");} ;
        B : 'b' {System.out.println("S.B");} ;
        """;
    mkdir(tmpdir);
    writeFile(tmpdir, "S.g4", slave);
    String master =
      """
        lexer grammar M;
        import S;
        A : 'a' B {System.out.println("M.A");} ;
        WS : (' '|'\\n') -> skip ;
        """;
    String found = execLexer("M.g4", master, "M", "ab", debug);
    assertEquals("""
      M.A
      [@0,0:1='ab',<1>,1:0]
      [@1,2:1='<EOF>',<-1>,1:2]
      """, found);
  }

  @Test
  @Disabled("Переделать на ANTLR runtime/Generator")
  public void testKeywordVSIDOrder() {
    // rules in lexer are imported at END so rules in master override
    // *and* get priority over imported rules. So importing ID doesn't
    // mess up keywords in master grammar
    ErrorQueue equeue = new ErrorQueue();
    String slave =
      """
        lexer grammar S;
        ID : 'a'..'z'+ ;
        """;
    mkdir(tmpdir);
    writeFile(tmpdir, "S.g4", slave);
    String master =
      """
        grammar M;
        import S;
        a : A {System.out.println("M.a: "+$A);} ;
        A : 'abc' {System.out.println("M.A");} ;
        WS : (' '|'\\n') -> skip ;
        """;
    String found = execParser("M.g4", master, "MParser", "MLexer",
      "a", "abc", debug);

    assertEquals("unexpected errors: " + equeue, 0, equeue.errors.size());
    assertEquals("unexpected warnings: " + equeue, 0, equeue.warnings.size());

    assertEquals("""
      M.A
      M.a: [@0,0:2='abc',<1>,1:0]
      """, found);
  }

  // Make sure that M can import S that imports T.
  @Test
  @Disabled("Переделать на ANTLR runtime/Generator")
  public void test3LevelImport() throws Exception {
    ErrorQueue equeue = new ErrorQueue();
    String slave =
      """
        parser grammar T;
        a : T ;
        """;
    mkdir(tmpdir);
    writeFile(tmpdir, "T.g4", slave);
    String slave2 =
      """
        parser grammar S;
        import T;
        a : S ;
        """;
    mkdir(tmpdir);
    writeFile(tmpdir, "S.g4", slave2);

    String master =
      """
        grammar M;
        import S;
        a : M ;
        """;
    writeFile(tmpdir, "M.g4", master);
    Grammar g = new Grammar(tmpdir + "/M.g4", master, equeue);

    String expectedTokenIDToTypeMap = "{EOF=-1, M=1}"; // S and T aren't imported; overridden
    String expectedStringLiteralToTypeMap = "{}";
    String expectedTypeToTokenList = "[M]";

    assertEquals(expectedTokenIDToTypeMap,
      g.tokenNameToTypeMap.toString());
    assertEquals(expectedStringLiteralToTypeMap, g.stringLiteralToTypeMap.toString());
    assertEquals(expectedTypeToTokenList,
      realElements(g.typeToTokenList).toString());

    assertEquals("unexpected errors: " + equeue, 0, equeue.errors.size());

    boolean ok =
      rawGenerateAndBuildRecognizer("M.g4", master, "MParser", null);
    boolean expecting = true; // should be ok
    assertEquals(expecting, ok);
  }

  @Test
  @Disabled("Переделать на ANTLR runtime/Generator")
  public void testBigTreeOfImports() throws Exception {
    ErrorQueue equeue = new ErrorQueue();
    String slave =
      """
        parser grammar T;
        tokens{T}
        x : T ;
        """;
    mkdir(tmpdir);
    writeFile(tmpdir, "T.g4", slave);
    slave =
      """
        parser grammar S;
        import T;
        tokens{S}
        y : S ;
        """;
    mkdir(tmpdir);
    writeFile(tmpdir, "S.g4", slave);

    slave =
      """
        parser grammar C;
        tokens{C}
        i : C ;
        """;
    mkdir(tmpdir);
    writeFile(tmpdir, "C.g4", slave);
    slave =
      """
        parser grammar B;
        tokens{B}
        j : B ;
        """;
    mkdir(tmpdir);
    writeFile(tmpdir, "B.g4", slave);
    slave =
      """
        parser grammar A;
        import B,C;
        tokens{A}
        k : A ;
        """;
    mkdir(tmpdir);
    writeFile(tmpdir, "A.g4", slave);

    String master =
      """
        grammar M;
        import S,A;
        tokens{M}
        a : M ;
        """;
    writeFile(tmpdir, "M.g4", master);
    Grammar g = new Grammar(tmpdir + "/M.g4", master, equeue);

    assertEquals("[]", equeue.errors.toString());
    assertEquals("[]", equeue.warnings.toString());
    String expectedTokenIDToTypeMap = "{EOF=-1, M=1, S=2, T=3, A=4, B=5, C=6}";
    String expectedStringLiteralToTypeMap = "{}";
    String expectedTypeToTokenList = "[M, S, T, A, B, C]";

    assertEquals(expectedTokenIDToTypeMap,
      g.tokenNameToTypeMap.toString());
    assertEquals(expectedStringLiteralToTypeMap, g.stringLiteralToTypeMap.toString());
    assertEquals(expectedTypeToTokenList,
      realElements(g.typeToTokenList).toString());

    boolean ok =
      rawGenerateAndBuildRecognizer("M.g4", master, "MParser", null);
    boolean expecting = true; // should be ok
    assertEquals(expecting, ok);
  }

  @Test
  public void testRulesVisibleThroughMultilevelImport() throws Exception {
    ErrorQueue equeue = new ErrorQueue();
    String slave =
      """
        parser grammar T;
        x : T ;
        """;
    mkdir(tmpdir);
    writeFile(tmpdir, "T.g4", slave);
    // A, B, C token type order
    String slave2 =
      """
        parser grammar S;
        import T;
        a : S ;
        """;
    mkdir(tmpdir);
    writeFile(tmpdir, "S.g4", slave2);

    String master =
      """
        grammar M;
        import S;
        a : M x ;
        """; // x MUST BE VISIBLE TO M
    writeFile(tmpdir, "M.g4", master);
    Grammar g = new Grammar(tmpdir + "/M.g4", master, equeue);

    String expectedTokenIDToTypeMap = "{EOF=-1, M=1, T=2}";
    String expectedStringLiteralToTypeMap = "{}";
    String expectedTypeToTokenList = "[M, T]";

    assertEquals(expectedTokenIDToTypeMap,
      g.tokenNameToTypeMap.toString());
    assertEquals(expectedStringLiteralToTypeMap, g.stringLiteralToTypeMap.toString());
    assertEquals(expectedTypeToTokenList,
      realElements(g.typeToTokenList).toString());

    assertEquals("unexpected errors: " + equeue, 0, equeue.errors.size());
  }

  @Test
  @Disabled("Переделать на ANTLR runtime/Generator")
  public void testNestedComposite() throws Exception {
    // Wasn't compiling. http://www.antlr.org/jira/browse/ANTLR-438
    ErrorQueue equeue = new ErrorQueue();
    String gstr =
      """
        lexer grammar L;
        T1: '1';
        T2: '2';
        T3: '3';
        T4: '4';
        """;
    mkdir(tmpdir);
    writeFile(tmpdir, "L.g4", gstr);
    gstr =
      """
        parser grammar G1;
        s: a | b;
        a: T1;
        b: T2;
        """;
    mkdir(tmpdir);
    writeFile(tmpdir, "G1.g4", gstr);

    gstr =
      """
        parser grammar G2;
        import G1;
        a: T3;
        """;
    mkdir(tmpdir);
    writeFile(tmpdir, "G2.g4", gstr);
    String G3str =
      """
        grammar G3;
        import G2;
        b: T4;
        """;
    mkdir(tmpdir);
    writeFile(tmpdir, "G3.g4", G3str);

    Grammar g = new Grammar(tmpdir + "/G3.g4", G3str, equeue);

    String expectedTokenIDToTypeMap = "{EOF=-1, T4=1, T3=2}";
    String expectedStringLiteralToTypeMap = "{}";
    String expectedTypeToTokenList = "[T4, T3]";

    assertEquals(expectedTokenIDToTypeMap,
      g.tokenNameToTypeMap.toString());
    assertEquals(expectedStringLiteralToTypeMap, g.stringLiteralToTypeMap.toString());
    assertEquals(expectedTypeToTokenList,
      realElements(g.typeToTokenList).toString());

    assertEquals("unexpected errors: " + equeue, 0, equeue.errors.size());

    boolean ok =
      rawGenerateAndBuildRecognizer("G3.g4", G3str, "G3Parser", null);
    boolean expecting = true; // should be ok
    assertEquals(expecting, ok);
  }

  @Test
  public void testHeadersPropogatedCorrectlyToImportedGrammars() {
    String slave =
      """
        parser grammar S;
        a : B {System.out.print("S.a");} ;
        """;
    mkdir(tmpdir);
    writeFile(tmpdir, "S.g4", slave);
    // defines B from inherited token space
    String master =
      """
        grammar M;
        import S;
        @header{package mypackage;}
        s : a ;
        B : 'b' ;\
        WS : (' '|'\\n') -> skip ;
        """;
    ErrorQueue equeue = antlr("M.g4", master, false);
    int expecting = 0; // should be ok
    assertEquals(expecting, equeue.errors.size());
  }

  @Test
  @Disabled("Переделать на ANTLR runtime/Generator")
  public void testImportedRuleWithAction() {
    // wasn't terminating. @after was injected into M as if it were @members
    String slave =
      """
        parser grammar S;
        a @after {int x;} : B ;
        """;
    mkdir(tmpdir);
    writeFile(tmpdir, "S.g4", slave);
    String master =
      """
        grammar M;
        import S;
        s : a ;
        B : 'b' ;\
        WS : (' '|'\\n') -> skip ;
        """;
    String found = execParser("M.g4", master, "MParser", "MLexer",
      "s", "b", debug);
    assertEquals("", found);
  }

  @Test
  @Disabled("Переделать на ANTLR runtime/Generator")
  public void testImportedGrammarWithEmptyOptions() {
    String slave =
      """
        parser grammar S;
        options {}
        a : B ;
        """;
    mkdir(tmpdir);
    writeFile(tmpdir, "S.g4", slave);
    String master =
      """
        grammar M;
        import S;
        s : a ;
        B : 'b' ;\
        WS : (' '|'\\n') -> skip ;
        """;
    String found = execParser("M.g4", master, "MParser", "MLexer",
      "s", "b", debug);
    assertEquals("", found);
  }

  /**
   * This is a regression test for antlr/antlr4#248 "Including grammar with
   * only fragments breaks generated lexer".
   * <a href="https://github.com/antlr/antlr4/issues/248">...</a>
   */
  @Test
  @Disabled("Переделать на ANTLR runtime/Generator")
  public void testImportLexerWithOnlyFragmentRules() {
    String slave =
      """
        lexer grammar Unicode;
        
        fragment
        UNICODE_CLASS_Zs    : '\\u0020' | '\\u00A0' | '\\u1680' | '\\u180E'
                            | '\\u2000'..'\\u200A'
                            | '\\u202F' | '\\u205F' | '\\u3000'
                            ;
        """;
    String master =
      """
        grammar Test;
        import Unicode;
        
        program : 'test' 'test' ;
        
        WS : (UNICODE_CLASS_Zs)+ -> skip;
        """;

    mkdir(tmpdir);
    writeFile(tmpdir, "Unicode.g4", slave);
    String found = execParser("Test.g4", master, "TestParser", "TestLexer", "program", "test test", debug);
    assertEquals("", found);
    assertThat(stderrDuringParse).isNull();
  }

  /**
   * This is a regression test for antlr/antlr4#670 "exception when importing
   * grammar".
   * <a href="https://github.com/antlr/antlr4/issues/670">...</a>
   */
  @Test
  @Disabled("Переделать на ANTLR runtime/Generator")
  public void testImportLargeGrammar() throws Exception {
    String slave = load("Java.g4", "UTF-8");
    String master =
      """
        grammar NewJava;
        import Java;
        """;

    System.out.println("dir " + tmpdir);
    mkdir(tmpdir);
    writeFile(tmpdir, "Java.g4", slave);
    String found = execParser("NewJava.g4", master, "NewJavaParser", "NewJavaLexer", "compilationUnit", "package Foo;", debug);
    assertEquals("", found);
    assertThat(stderrDuringParse).isNull();
  }

  /**
   * This is a regression test for antlr/antlr4#670 "exception when importing
   * grammar".
   * <a href="https://github.com/antlr/antlr4/issues/670">...</a>
   */
  @Test
  @Disabled("Переделать на ANTLR runtime/Generator")
  public void testImportLeftRecursiveGrammar() {
    String slave =
      """
        grammar Java;
        e : '(' e ')'
          | e '=' e
          | ID
          ;
        ID : [a-z]+ ;
        """;
    String master =
      """
        grammar T;
        import Java;
        s : e ;
        """;

    System.out.println("dir " + tmpdir);
    mkdir(tmpdir);
    writeFile(tmpdir, "Java.g4", slave);
    String found = execParser("T.g4", master, "TParser", "TLexer", "s", "a=b", debug);
    assertEquals("", found);
    assertThat(stderrDuringParse).isNull();
  }
}
