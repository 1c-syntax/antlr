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

import org.antlr.v4.tool.ANTLRMessage;
import org.antlr.v4.tool.ErrorType;
import org.antlr.v4.tool.Grammar;
import org.antlr.v4.tool.GrammarSemanticsMessage;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;

class CompositeGrammarsTest extends AbstractBaseTest {
  protected boolean debug = false;

  @Test
  void testImportFileLocationInSubdir() {
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
    assertThat(equeue.size()).isZero();
  }

  // Test for https://github.com/antlr/antlr4/issues/1317
  @Test
  void testImportSelfLoop() {
    mkdir(tmpdir);
    String master =
      """
        grammar M;
        import M;
        s : 'a' ;
        """;
    writeFile(tmpdir, "M.g4", master);
    ErrorQueue equeue = antlr("M.g4", false, "-lib", tmpdir);
    assertThat(equeue.size()).isZero();
  }

  @Test
  void testErrorInImportedGetsRightFilename() {
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
    assertThat(msg.getErrorType()).isEqualTo(ErrorType.UNDEFINED_RULE_REF);
    assertThat(msg.getArgs()[0]).isEqualTo("c");
    assertThat(msg.line).isEqualTo(2);
    assertThat(msg.charPosition).isEqualTo(10);
    assertThat(new File(msg.fileName)).hasName("S.g4");
  }

  @Test
  void testImportFileNotSearchedForInOutputDir() {
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
    assertThat(equeue.errors.get(0).getErrorType()).isEqualTo(ErrorType.CANNOT_FIND_IMPORTED_GRAMMAR);
  }

  @Test
  void testOutputDirShouldNotEffectImports() {
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
    assertThat(equeue.size()).isZero();
  }

  @Test
  void testTokensFileInOutputDirAndImportFileInSubdir() {
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
    assertThat(equeue.size()).isZero();
    equeue = antlr("MParser.g4", false, "-o", outdir, "-lib", subdir);
    assertThat(equeue.size()).isZero();
  }

  @Test
  void testDelegatorInvokesDelegateRule() {
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
    assertThat(found).isEqualTo("S.a\n");
  }

  @Test
  void testBringInLiteralsFromDelegate() {
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
    assertThat(found).isEqualTo("S.a\n");
  }

  @Test
  void testDelegatorInvokesDelegateRuleWithArgs() {
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
    assertThat(found).isEqualTo("S.a1000\n");
  }

  @Test
  void testDelegatorInvokesDelegateRuleWithReturnStruct() {
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
    assertThat(found).isEqualTo("S.ab\n");
  }

  @Test
  void testDelegatorAccessesDelegateMembers() {
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
    assertThat(found).isEqualTo("foo\n");
  }

  @Test
  void testDelegatorInvokesFirstVersionOfDelegateRule() {
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
    assertThat(found).isEqualTo("S.a\n");
  }

  @Test
  void testDelegatesSeeSameTokenType() {
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
    assertThat(found).isEqualTo("""
      S.x
      T.y
      """);
  }

  @Test
  void testDelegatesSeeSameTokenType2() throws Exception {
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

    assertThat(g.tokenNameToTypeMap).hasToString(expectedTokenIDToTypeMap);
    assertThat(sort(g.stringLiteralToTypeMap)).hasToString(expectedStringLiteralToTypeMap);
    assertThat(realElements(g.typeToTokenList)).hasToString(expectedTypeToTokenList);

    assertThat(equeue.errors).isEmpty();

    String found = execParser("M.g4", master, "MParser", "MLexer",
      "s", "aa", debug);
    assertThat(found).isEqualTo("""
      S.x
      T.y
      """);
  }

  @Test
  void testCombinedImportsCombined() throws Exception {
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

    assertThat(equeue.errors).isEmpty();

    String found = execParser("M.g4", master, "MParser", "MLexer",
      "s", "x 34 9", debug);
    assertThat(found).isEqualTo("S.x\n");
  }

  @Test
  void testImportedTokenVocabIgnoredWithWarning() throws Exception {
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

    assertThat(equeue.errors).isEmpty();
    assertThat(equeue.warnings).hasSize(1);
  }

  @Test
  void testSyntaxErrorsInImportsNotThrownOut() throws Exception {
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

    assertThat(equeue.errors.get(0).getErrorType()).isEqualTo(ErrorType.SYNTAX_ERROR);
  }

  @Test
  void testDelegatorRuleOverridesDelegate() {
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
    assertThat(found).isEqualTo("S.a\n");
  }

  @Test
  void testDelegatorRuleOverridesLookaheadInDelegate() {
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
    assertThat(found).isEqualTo("JavaDecl: floatx=3;\n");
  }

  @Test
  void testDelegatorRuleOverridesDelegates() {
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
    assertThat(found).isEqualTo("""
      M.b
      S.a
      """);
  }
  // LEXER INHERITANCE

  @Test
  void testLexerDelegatorInvokesDelegateRule() {
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
    assertThat(found).isEqualTo(expecting);
  }

  @Test
  void testLexerDelegatorRuleOverridesDelegate() {
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
    assertThat(found).isEqualTo("""
      M.A
      [@0,0:1='ab',<1>,1:0]
      [@1,2:1='<EOF>',<-1>,1:2]
      """);
  }

  @Test
  void testKeywordVSIDOrder() {
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

    assertThat(equeue.errors).isEmpty();
    assertThat(equeue.warnings).isEmpty();

    assertThat(found).isEqualTo("""
      M.A
      M.a: [@0,0:2='abc',<1>,1:0]
      """);
  }

  // Make sure that M can import S that imports T.
  @Test
  void test3LevelImport() throws Exception {
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

    assertThat(g.tokenNameToTypeMap).hasToString(expectedTokenIDToTypeMap);
    assertThat(g.stringLiteralToTypeMap).hasToString(expectedStringLiteralToTypeMap);
    assertThat(realElements(g.typeToTokenList)).hasToString(expectedTypeToTokenList);

    assertThat(equeue.errors).isEmpty();

    boolean ok =
      rawGenerateAndBuildRecognizer("M.g4", master, "MParser", null);
    boolean expecting = true; // should be ok
    assertThat(ok).isEqualTo(expecting);
  }

  @Test
  void testBigTreeOfImports() throws Exception {
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

    assertThat(equeue.errors).hasToString("[]");
    assertThat(equeue.warnings).hasToString("[]");
    String expectedTokenIDToTypeMap = "{EOF=-1, M=1, S=2, T=3, A=4, B=5, C=6}";
    String expectedStringLiteralToTypeMap = "{}";
    String expectedTypeToTokenList = "[M, S, T, A, B, C]";

    assertThat(g.tokenNameToTypeMap).hasToString(expectedTokenIDToTypeMap);
    assertThat(g.stringLiteralToTypeMap).hasToString(expectedStringLiteralToTypeMap);
    assertThat(realElements(g.typeToTokenList)).hasToString(expectedTypeToTokenList);

    boolean ok =
      rawGenerateAndBuildRecognizer("M.g4", master, "MParser", null);
    boolean expecting = true; // should be ok
    assertThat(ok).isEqualTo(expecting);
  }

  @Test
  void testRulesVisibleThroughMultilevelImport() throws Exception {
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

    assertThat(g.tokenNameToTypeMap).hasToString(expectedTokenIDToTypeMap);
    assertThat(g.stringLiteralToTypeMap).hasToString(expectedStringLiteralToTypeMap);
    assertThat(realElements(g.typeToTokenList)).hasToString(expectedTypeToTokenList);

    assertThat(equeue.errors).isEmpty();
  }

  @Test
  void testNestedComposite() throws Exception {
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

    assertThat(g.tokenNameToTypeMap).hasToString(expectedTokenIDToTypeMap);
    assertThat(g.stringLiteralToTypeMap).hasToString(expectedStringLiteralToTypeMap);
    assertThat(realElements(g.typeToTokenList)).hasToString(expectedTypeToTokenList);

    assertThat(equeue.errors).isEmpty();

    boolean ok =
      rawGenerateAndBuildRecognizer("G3.g4", G3str, "G3Parser", null);
    boolean expecting = true; // should be ok
    assertThat(ok).isEqualTo(expecting);
  }

  @Test
  void testHeadersPropogatedCorrectlyToImportedGrammars() {
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
    assertThat(equeue.errors).hasSize(expecting);
  }

  @Test
  void testImportedRuleWithAction() {
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
    assertThat(found).isEmpty();
  }

  @Test
  void testImportedGrammarWithEmptyOptions() {
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
    assertThat(found).isEmpty();
  }

  /**
   * This is a regression test for antlr/antlr4#248 "Including grammar with
   * only fragments breaks generated lexer".
   * <a href="https://github.com/antlr/antlr4/issues/248">...</a>
   */
  @Test
  void testImportLexerWithOnlyFragmentRules() {
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
    assertThat(found).isEmpty();
    assertThat(stderrDuringParse).isNull();
  }

  /**
   * This is a regression test for antlr/antlr4#670 "exception when importing
   * grammar".
   * <a href="https://github.com/antlr/antlr4/issues/670">...</a>
   */
  @Test
  void testImportLargeGrammar() throws Exception {
    String fullFileName = "Java.g4";
    int size = 65000;
    String slave;
    try (InputStream fis = Thread.currentThread().getContextClassLoader().getResourceAsStream(fullFileName);
         InputStreamReader isr = fis != null ? new InputStreamReader(fis, StandardCharsets.UTF_8) : null) {
      if (fis == null) {
        throw new IOException("Could not find resource: " + fullFileName);
      }
      char[] data = new char[size];
      int n = isr.read(data);
      slave = new String(data, 0, n);
    }

    String master =
      """
        grammar NewJava;
        import Java;
        """;

    System.out.println("dir " + tmpdir);
    mkdir(tmpdir);
    writeFile(tmpdir, "Java.g4", slave);
    String found = execParser("NewJava.g4", master, "NewJavaParser", "NewJavaLexer", "compilationUnit", "package Foo;", debug);
    assertThat(found).isEmpty();
    assertThat(stderrDuringParse).isNull();
  }

  /**
   * This is a regression test for antlr/antlr4#670 "exception when importing
   * grammar".
   * <a href="https://github.com/antlr/antlr4/issues/670">...</a>
   */
  @Test
  void testImportLeftRecursiveGrammar() {
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
    assertThat(found).isEmpty();
    assertThat(stderrDuringParse).isNull();
  }
}
