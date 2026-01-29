/*
 * This file is a part of ANTLR.
 *
 * Copyright (c) 2012-2025 The ANTLR Project. All rights reserved.
 * Copyright (c) 2025-2026 Valery Maximov <maximovvalery@gmail.com> and contributors
 *
 * Use of this file is governed by the BSD-3-Clause license that
 * can be found in the LICENSE.txt file in the project root.
 */
// Generated from TestIncrementalJava.g4 by ANTLR 4.x
package org.antlr.v4.test.runtime.java.api;

import org.antlr.v4.runtime.FailedPredicateException;
import org.antlr.v4.runtime.IncrementalParser;
import org.antlr.v4.runtime.IncrementalParserData;
import org.antlr.v4.runtime.IncrementalParserRuleContext;
import org.antlr.v4.runtime.IncrementalTokenStream;
import org.antlr.v4.runtime.NoViableAltException;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.RuleContext;
import org.antlr.v4.runtime.RuleVersion;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.Vocabulary;
import org.antlr.v4.runtime.VocabularyImpl;
import org.antlr.v4.runtime.atn.ATN;
import org.antlr.v4.runtime.atn.ATNDeserializer;
import org.antlr.v4.runtime.atn.ParserATNSimulator;
import org.antlr.v4.runtime.misc.NotNull;
import org.antlr.v4.runtime.misc.Nullable;
import org.antlr.v4.runtime.tree.TerminalNode;

import java.util.List;

public class TestIncrementalJavaParser extends IncrementalParser {
  public static final int
    ABSTRACT = 1, ASSERT = 2, BOOLEAN = 3, BREAK = 4, BYTE = 5, CASE = 6, CATCH = 7, CHAR = 8,
    CLASS = 9, CONST = 10, CONTINUE = 11, DEFAULT = 12, DO = 13, DOUBLE = 14, ELSE = 15,
    ENUM = 16, EXTENDS = 17, FINAL = 18, FINALLY = 19, FLOAT = 20, FOR = 21, IF = 22, GOTO = 23,
    IMPLEMENTS = 24, IMPORT = 25, INSTANCEOF = 26, INT = 27, INTERFACE = 28, LONG = 29,
    NATIVE = 30, NEW = 31, PACKAGE = 32, PRIVATE = 33, PROTECTED = 34, PUBLIC = 35, RETURN = 36,
    SHORT = 37, STATIC = 38, STRICTFP = 39, SUPER = 40, SWITCH = 41, SYNCHRONIZED = 42,
    THIS = 43, THROW = 44, THROWS = 45, TRANSIENT = 46, TRY = 47, VOID = 48, VOLATILE = 49,
    WHILE = 50, IntegerLiteral = 51, FloatingPointLiteral = 52, BooleanLiteral = 53,
    CharacterLiteral = 54, StringLiteral = 55, NullLiteral = 56, LPAREN = 57, RPAREN = 58,
    LBRACE = 59, RBRACE = 60, LBRACK = 61, RBRACK = 62, SEMI = 63, COMMA = 64, DOT = 65,
    ASSIGN = 66, GT = 67, LT = 68, BANG = 69, TILDE = 70, QUESTION = 71, COLON = 72, EQUAL = 73,
    LE = 74, GE = 75, NOTEQUAL = 76, AND = 77, OR = 78, INC = 79, DEC = 80, ADD = 81, SUB = 82,
    MUL = 83, DIV = 84, BITAND = 85, BITOR = 86, CARET = 87, MOD = 88, ADD_ASSIGN = 89,
    SUB_ASSIGN = 90, MUL_ASSIGN = 91, DIV_ASSIGN = 92, AND_ASSIGN = 93, OR_ASSIGN = 94,
    XOR_ASSIGN = 95, MOD_ASSIGN = 96, LSHIFT_ASSIGN = 97, RSHIFT_ASSIGN = 98, URSHIFT_ASSIGN = 99,
    Identifier = 100, AT = 101, ELLIPSIS = 102, WS = 103, COMMENT = 104, LINE_COMMENT = 105;
  public static final int
    RULE_compilationUnit = 0, RULE_packageDeclaration = 1, RULE_importDeclaration = 2,
    RULE_typeDeclaration = 3, RULE_classOrInterfaceDeclaration = 4, RULE_classOrInterfaceModifiers = 5,
    RULE_classOrInterfaceModifier = 6, RULE_modifiers = 7, RULE_classDeclaration = 8,
    RULE_normalClassDeclaration = 9, RULE_typeParameters = 10, RULE_typeParameter = 11,
    RULE_typeBound = 12, RULE_enumDeclaration = 13, RULE_enumBody = 14, RULE_enumConstants = 15,
    RULE_enumConstant = 16, RULE_enumBodyDeclarations = 17, RULE_interfaceDeclaration = 18,
    RULE_normalInterfaceDeclaration = 19, RULE_typeList = 20, RULE_classBody = 21,
    RULE_interfaceBody = 22, RULE_classBodyDeclaration = 23, RULE_memberDecl = 24,
    RULE_memberDeclaration = 25, RULE_genericMethodOrConstructorDecl = 26,
    RULE_genericMethodOrConstructorRest = 27, RULE_methodDeclaration = 28,
    RULE_fieldDeclaration = 29, RULE_interfaceBodyDeclaration = 30, RULE_interfaceMemberDecl = 31,
    RULE_interfaceMethodOrFieldDecl = 32, RULE_interfaceMethodOrFieldRest = 33,
    RULE_methodDeclaratorRest = 34, RULE_voidMethodDeclaratorRest = 35, RULE_interfaceMethodDeclaratorRest = 36,
    RULE_interfaceGenericMethodDecl = 37, RULE_voidInterfaceMethodDeclaratorRest = 38,
    RULE_constructorDeclaratorRest = 39, RULE_constantDeclarator = 40, RULE_variableDeclarators = 41,
    RULE_variableDeclarator = 42, RULE_constantDeclaratorsRest = 43, RULE_constantDeclaratorRest = 44,
    RULE_variableDeclaratorId = 45, RULE_variableInitializer = 46, RULE_arrayInitializer = 47,
    RULE_modifier = 48, RULE_packageOrTypeName = 49, RULE_enumConstantName = 50,
    RULE_typeName = 51, RULE_type = 52, RULE_classOrInterfaceType = 53, RULE_primitiveType = 54,
    RULE_variableModifier = 55, RULE_typeArguments = 56, RULE_typeArgument = 57,
    RULE_qualifiedNameList = 58, RULE_formalParameters = 59, RULE_formalParameterDecls = 60,
    RULE_formalParameterDeclsRest = 61, RULE_methodBody = 62, RULE_constructorBody = 63,
    RULE_qualifiedName = 64, RULE_literal = 65, RULE_annotations = 66, RULE_annotation = 67,
    RULE_annotationName = 68, RULE_elementValuePairs = 69, RULE_elementValuePair = 70,
    RULE_elementValue = 71, RULE_elementValueArrayInitializer = 72, RULE_annotationTypeDeclaration = 73,
    RULE_annotationTypeBody = 74, RULE_annotationTypeElementDeclaration = 75,
    RULE_annotationTypeElementRest = 76, RULE_annotationMethodOrConstantRest = 77,
    RULE_annotationMethodRest = 78, RULE_annotationConstantRest = 79, RULE_defaultValue = 80,
    RULE_block = 81, RULE_blockStatement = 82, RULE_localVariableDeclarationStatement = 83,
    RULE_localVariableDeclaration = 84, RULE_variableModifiers = 85, RULE_statement = 86,
    RULE_catches = 87, RULE_catchClause = 88, RULE_catchType = 89, RULE_finallyBlock = 90,
    RULE_resourceSpecification = 91, RULE_resources = 92, RULE_resource = 93,
    RULE_formalParameter = 94, RULE_switchBlockStatementGroups = 95, RULE_switchBlockStatementGroup = 96,
    RULE_switchLabel = 97, RULE_forControl = 98, RULE_forInit = 99, RULE_enhancedForControl = 100,
    RULE_forUpdate = 101, RULE_parExpression = 102, RULE_expressionList = 103,
    RULE_statementExpression = 104, RULE_constantExpression = 105, RULE_expression = 106,
    RULE_primary = 107, RULE_creator = 108, RULE_createdName = 109, RULE_innerCreator = 110,
    RULE_arrayCreatorRest = 111, RULE_classCreatorRest = 112, RULE_explicitGenericInvocation = 113,
    RULE_nonWildcardTypeArguments = 114, RULE_typeArgumentsOrDiamond = 115,
    RULE_nonWildcardTypeArgumentsOrDiamond = 116, RULE_superSuffix = 117,
    RULE_explicitGenericInvocationSuffix = 118, RULE_arguments = 119;

  private static String[] makeRuleNames() {
    return new String[]{
      "compilationUnit", "packageDeclaration", "importDeclaration", "typeDeclaration",
      "classOrInterfaceDeclaration", "classOrInterfaceModifiers", "classOrInterfaceModifier",
      "modifiers", "classDeclaration", "normalClassDeclaration", "typeParameters",
      "typeParameter", "typeBound", "enumDeclaration", "enumBody", "enumConstants",
      "enumConstant", "enumBodyDeclarations", "interfaceDeclaration", "normalInterfaceDeclaration",
      "typeList", "classBody", "interfaceBody", "classBodyDeclaration", "memberDecl",
      "memberDeclaration", "genericMethodOrConstructorDecl", "genericMethodOrConstructorRest",
      "methodDeclaration", "fieldDeclaration", "interfaceBodyDeclaration",
      "interfaceMemberDecl", "interfaceMethodOrFieldDecl", "interfaceMethodOrFieldRest",
      "methodDeclaratorRest", "voidMethodDeclaratorRest", "interfaceMethodDeclaratorRest",
      "interfaceGenericMethodDecl", "voidInterfaceMethodDeclaratorRest", "constructorDeclaratorRest",
      "constantDeclarator", "variableDeclarators", "variableDeclarator", "constantDeclaratorsRest",
      "constantDeclaratorRest", "variableDeclaratorId", "variableInitializer",
      "arrayInitializer", "modifier", "packageOrTypeName", "enumConstantName",
      "typeName", "type", "classOrInterfaceType", "primitiveType", "variableModifier",
      "typeArguments", "typeArgument", "qualifiedNameList", "formalParameters",
      "formalParameterDecls", "formalParameterDeclsRest", "methodBody", "constructorBody",
      "qualifiedName", "literal", "annotations", "annotation", "annotationName",
      "elementValuePairs", "elementValuePair", "elementValue", "elementValueArrayInitializer",
      "annotationTypeDeclaration", "annotationTypeBody", "annotationTypeElementDeclaration",
      "annotationTypeElementRest", "annotationMethodOrConstantRest", "annotationMethodRest",
      "annotationConstantRest", "defaultValue", "block", "blockStatement",
      "localVariableDeclarationStatement", "localVariableDeclaration", "variableModifiers",
      "statement", "catches", "catchClause", "catchType", "finallyBlock", "resourceSpecification",
      "resources", "resource", "formalParameter", "switchBlockStatementGroups",
      "switchBlockStatementGroup", "switchLabel", "forControl", "forInit",
      "enhancedForControl", "forUpdate", "parExpression", "expressionList",
      "statementExpression", "constantExpression", "expression", "primary",
      "creator", "createdName", "innerCreator", "arrayCreatorRest", "classCreatorRest",
      "explicitGenericInvocation", "nonWildcardTypeArguments", "typeArgumentsOrDiamond",
      "nonWildcardTypeArgumentsOrDiamond", "superSuffix", "explicitGenericInvocationSuffix",
      "arguments"
    };
  }

  public static final String[] ruleNames = makeRuleNames();

  private static String[] makeLiteralNames() {
    return new String[]{
      null, "'abstract'", "'assert'", "'boolean'", "'break'", "'byte'", "'case'",
      "'catch'", "'char'", "'class'", "'const'", "'continue'", "'default'",
      "'do'", "'double'", "'else'", "'enum'", "'extends'", "'final'", "'finally'",
      "'float'", "'for'", "'if'", "'goto'", "'implements'", "'import'", "'instanceof'",
      "'int'", "'interface'", "'long'", "'native'", "'new'", "'package'", "'private'",
      "'protected'", "'public'", "'return'", "'short'", "'static'", "'strictfp'",
      "'super'", "'switch'", "'synchronized'", "'this'", "'throw'", "'throws'",
      "'transient'", "'try'", "'void'", "'volatile'", "'while'", null, null,
      null, null, null, "'null'", "'('", "')'", "'{'", "'}'", "'['", "']'",
      "';'", "','", "'.'", "'='", "'>'", "'<'", "'!'", "'~'", "'?'", "':'",
      "'=='", "'<='", "'>='", "'!='", "'&&'", "'||'", "'++'", "'--'", "'+'",
      "'-'", "'*'", "'/'", "'&'", "'|'", "'^'", "'%'", "'+='", "'-='", "'*='",
      "'/='", "'&='", "'|='", "'^='", "'%='", "'<<='", "'>>='", "'>>>='", null,
      "'@'", "'...'"
    };
  }

  private static final String[] _LITERAL_NAMES = makeLiteralNames();

  private static String[] makeSymbolicNames() {
    return new String[]{
      null, "ABSTRACT", "ASSERT", "BOOLEAN", "BREAK", "BYTE", "CASE", "CATCH",
      "CHAR", "CLASS", "CONST", "CONTINUE", "DEFAULT", "DO", "DOUBLE", "ELSE",
      "ENUM", "EXTENDS", "FINAL", "FINALLY", "FLOAT", "FOR", "IF", "GOTO",
      "IMPLEMENTS", "IMPORT", "INSTANCEOF", "INT", "INTERFACE", "LONG", "NATIVE",
      "NEW", "PACKAGE", "PRIVATE", "PROTECTED", "PUBLIC", "RETURN", "SHORT",
      "STATIC", "STRICTFP", "SUPER", "SWITCH", "SYNCHRONIZED", "THIS", "THROW",
      "THROWS", "TRANSIENT", "TRY", "VOID", "VOLATILE", "WHILE", "IntegerLiteral",
      "FloatingPointLiteral", "BooleanLiteral", "CharacterLiteral", "StringLiteral",
      "NullLiteral", "LPAREN", "RPAREN", "LBRACE", "RBRACE", "LBRACK", "RBRACK",
      "SEMI", "COMMA", "DOT", "ASSIGN", "GT", "LT", "BANG", "TILDE", "QUESTION",
      "COLON", "EQUAL", "LE", "GE", "NOTEQUAL", "AND", "OR", "INC", "DEC",
      "ADD", "SUB", "MUL", "DIV", "BITAND", "BITOR", "CARET", "MOD", "ADD_ASSIGN",
      "SUB_ASSIGN", "MUL_ASSIGN", "DIV_ASSIGN", "AND_ASSIGN", "OR_ASSIGN",
      "XOR_ASSIGN", "MOD_ASSIGN", "LSHIFT_ASSIGN", "RSHIFT_ASSIGN", "URSHIFT_ASSIGN",
      "Identifier", "AT", "ELLIPSIS", "WS", "COMMENT", "LINE_COMMENT"
    };
  }

  private static final String[] _SYMBOLIC_NAMES = makeSymbolicNames();
  public static final Vocabulary VOCABULARY = new VocabularyImpl(_LITERAL_NAMES, _SYMBOLIC_NAMES);

  @Override
  @NotNull
  public Vocabulary getVocabulary() {
    return VOCABULARY;
  }

  @Override
  public String getGrammarFileName() {
    return "TestIncrementalJava.g4";
  }

  @Override
  public String[] getRuleNames() {
    return ruleNames;
  }

  @Override
  public String getSerializedATN() {
    return _serializedATN;
  }

  @NotNull
  private FailedPredicateException createFailedPredicateException() {
    return createFailedPredicateException(null);
  }

  @NotNull
  private FailedPredicateException createFailedPredicateException(@Nullable String predicate) {
    return createFailedPredicateException(predicate, null);
  }

  @NotNull
  protected FailedPredicateException createFailedPredicateException(@Nullable String predicate, @Nullable String message) {
    return new FailedPredicateException(this, predicate, message);
  }

  public TestIncrementalJavaParser(IncrementalTokenStream input) {
    this(input, null);
  }

  public TestIncrementalJavaParser(IncrementalTokenStream input, IncrementalParserData data) {
    super(input, data);
    setInterpreter(new ParserATNSimulator(this, _ATN));
  }


  public static class CompilationUnitContext extends IncrementalParserRuleContext {
    public AnnotationsContext annotations() {
      return getRuleContext(AnnotationsContext.class, 0);
    }

    public TerminalNode EOF() {
      return getToken(TestIncrementalJavaParser.EOF, 0);
    }

    public PackageDeclarationContext packageDeclaration() {
      return getRuleContext(PackageDeclarationContext.class, 0);
    }

    public ClassOrInterfaceDeclarationContext classOrInterfaceDeclaration() {
      return getRuleContext(ClassOrInterfaceDeclarationContext.class, 0);
    }

    public List<? extends ImportDeclarationContext> importDeclaration() {
      return getRuleContexts(ImportDeclarationContext.class);
    }

    public ImportDeclarationContext importDeclaration(int i) {
      return getRuleContext(ImportDeclarationContext.class, i);
    }

    public List<? extends TypeDeclarationContext> typeDeclaration() {
      return getRuleContexts(TypeDeclarationContext.class);
    }

    public TypeDeclarationContext typeDeclaration(int i) {
      return getRuleContext(TypeDeclarationContext.class, i);
    }

    public CompilationUnitContext(ParserRuleContext parent, int invokingState) {
      super((IncrementalParserRuleContext) parent, invokingState);
    }

    @Override
    public int getRuleIndex() {
      return RULE_compilationUnit;
    }
  }

  @RuleVersion(0)
  public final CompilationUnitContext compilationUnit() throws RecognitionException {
    // Check whether we need to execute this rule.
    CompilationUnitContext guardResult = (CompilationUnitContext) guardRule((IncrementalParserRuleContext) _ctx, getState(), RULE_compilationUnit);
    // If we found an existing context that is valid, return it.
    if (guardResult != null) {
      this._input.seek(guardResult.stop.getTokenIndex() + 1);
      return guardResult;
    }
    CompilationUnitContext _localctx = new CompilationUnitContext(_ctx, getState());
    enterRule(_localctx, 0, RULE_compilationUnit);
    int _la;
    try {
      setState(281);
      _errHandler.sync(this);
      switch (getInterpreter().adaptivePredict(_input, 7, _ctx)) {
        case 1:
          enterOuterAlt(_localctx, 1);
        {
          setState(240);
          annotations();
          setState(261);
          _errHandler.sync(this);
          switch (_input.LA(1)) {
            case PACKAGE: {
              setState(241);
              packageDeclaration();
              setState(245);
              _errHandler.sync(this);
              _la = _input.LA(1);
              while (_la == IMPORT) {
                {
                  {
                    setState(242);
                    importDeclaration();
                  }
                }
                setState(247);
                _errHandler.sync(this);
                _la = _input.LA(1);
              }
              setState(251);
              _errHandler.sync(this);
              _la = _input.LA(1);
              while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << ABSTRACT) | (1L << CLASS) | (1L << ENUM) | (1L << FINAL) | (1L << INTERFACE) | (1L << PRIVATE) | (1L << PROTECTED) | (1L << PUBLIC) | (1L << STATIC) | (1L << STRICTFP) | (1L << SEMI))) != 0) || _la == AT) {
                {
                  {
                    setState(248);
                    typeDeclaration();
                  }
                }
                setState(253);
                _errHandler.sync(this);
                _la = _input.LA(1);
              }
            }
            break;
            case ABSTRACT:
            case CLASS:
            case ENUM:
            case FINAL:
            case INTERFACE:
            case PRIVATE:
            case PROTECTED:
            case PUBLIC:
            case STATIC:
            case STRICTFP:
            case AT: {
              setState(254);
              classOrInterfaceDeclaration();
              setState(258);
              _errHandler.sync(this);
              _la = _input.LA(1);
              while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << ABSTRACT) | (1L << CLASS) | (1L << ENUM) | (1L << FINAL) | (1L << INTERFACE) | (1L << PRIVATE) | (1L << PROTECTED) | (1L << PUBLIC) | (1L << STATIC) | (1L << STRICTFP) | (1L << SEMI))) != 0) || _la == AT) {
                {
                  {
                    setState(255);
                    typeDeclaration();
                  }
                }
                setState(260);
                _errHandler.sync(this);
                _la = _input.LA(1);
              }
            }
            break;
            default:
              throw new NoViableAltException(this);
          }
          setState(263);
          match(EOF);
        }
        break;
        case 2:
          enterOuterAlt(_localctx, 2);
        {
          setState(266);
          _errHandler.sync(this);
          _la = _input.LA(1);
          if (_la == PACKAGE) {
            {
              setState(265);
              packageDeclaration();
            }
          }

          setState(271);
          _errHandler.sync(this);
          _la = _input.LA(1);
          while (_la == IMPORT) {
            {
              {
                setState(268);
                importDeclaration();
              }
            }
            setState(273);
            _errHandler.sync(this);
            _la = _input.LA(1);
          }
          setState(277);
          _errHandler.sync(this);
          _la = _input.LA(1);
          while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << ABSTRACT) | (1L << CLASS) | (1L << ENUM) | (1L << FINAL) | (1L << INTERFACE) | (1L << PRIVATE) | (1L << PROTECTED) | (1L << PUBLIC) | (1L << STATIC) | (1L << STRICTFP) | (1L << SEMI))) != 0) || _la == AT) {
            {
              {
                setState(274);
                typeDeclaration();
              }
            }
            setState(279);
            _errHandler.sync(this);
            _la = _input.LA(1);
          }
          setState(280);
          match(EOF);
        }
        break;
      }
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
  }

  public static class PackageDeclarationContext extends IncrementalParserRuleContext {
    public TerminalNode PACKAGE() {
      return getToken(TestIncrementalJavaParser.PACKAGE, 0);
    }

    public QualifiedNameContext qualifiedName() {
      return getRuleContext(QualifiedNameContext.class, 0);
    }

    public TerminalNode SEMI() {
      return getToken(TestIncrementalJavaParser.SEMI, 0);
    }

    public PackageDeclarationContext(ParserRuleContext parent, int invokingState) {
      super((IncrementalParserRuleContext) parent, invokingState);
    }

    @Override
    public int getRuleIndex() {
      return RULE_packageDeclaration;
    }
  }

  @RuleVersion(0)
  public final PackageDeclarationContext packageDeclaration() throws RecognitionException {
    // Check whether we need to execute this rule.
    PackageDeclarationContext guardResult = (PackageDeclarationContext) guardRule((IncrementalParserRuleContext) _ctx, getState(), RULE_packageDeclaration);
    // If we found an existing context that is valid, return it.
    if (guardResult != null) {
      this._input.seek(guardResult.stop.getTokenIndex() + 1);
      return guardResult;
    }
    PackageDeclarationContext _localctx = new PackageDeclarationContext(_ctx, getState());
    enterRule(_localctx, 2, RULE_packageDeclaration);
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(283);
        match(PACKAGE);
        setState(284);
        qualifiedName();
        setState(285);
        match(SEMI);
      }
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
  }

  public static class ImportDeclarationContext extends IncrementalParserRuleContext {
    public TerminalNode IMPORT() {
      return getToken(TestIncrementalJavaParser.IMPORT, 0);
    }

    public QualifiedNameContext qualifiedName() {
      return getRuleContext(QualifiedNameContext.class, 0);
    }

    public TerminalNode SEMI() {
      return getToken(TestIncrementalJavaParser.SEMI, 0);
    }

    public TerminalNode STATIC() {
      return getToken(TestIncrementalJavaParser.STATIC, 0);
    }

    public TerminalNode DOT() {
      return getToken(TestIncrementalJavaParser.DOT, 0);
    }

    public TerminalNode MUL() {
      return getToken(TestIncrementalJavaParser.MUL, 0);
    }

    public ImportDeclarationContext(ParserRuleContext parent, int invokingState) {
      super((IncrementalParserRuleContext) parent, invokingState);
    }

    @Override
    public int getRuleIndex() {
      return RULE_importDeclaration;
    }
  }

  @RuleVersion(0)
  public final ImportDeclarationContext importDeclaration() throws RecognitionException {
    // Check whether we need to execute this rule.
    ImportDeclarationContext guardResult = (ImportDeclarationContext) guardRule((IncrementalParserRuleContext) _ctx, getState(), RULE_importDeclaration);
    // If we found an existing context that is valid, return it.
    if (guardResult != null) {
      this._input.seek(guardResult.stop.getTokenIndex() + 1);
      return guardResult;
    }
    ImportDeclarationContext _localctx = new ImportDeclarationContext(_ctx, getState());
    enterRule(_localctx, 4, RULE_importDeclaration);
    int _la;
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(287);
        match(IMPORT);
        setState(289);
        _errHandler.sync(this);
        _la = _input.LA(1);
        if (_la == STATIC) {
          {
            setState(288);
            match(STATIC);
          }
        }

        setState(291);
        qualifiedName();
        setState(294);
        _errHandler.sync(this);
        _la = _input.LA(1);
        if (_la == DOT) {
          {
            setState(292);
            match(DOT);
            setState(293);
            match(MUL);
          }
        }

        setState(296);
        match(SEMI);
      }
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
  }

  public static class TypeDeclarationContext extends IncrementalParserRuleContext {
    public ClassOrInterfaceDeclarationContext classOrInterfaceDeclaration() {
      return getRuleContext(ClassOrInterfaceDeclarationContext.class, 0);
    }

    public TerminalNode SEMI() {
      return getToken(TestIncrementalJavaParser.SEMI, 0);
    }

    public TypeDeclarationContext(ParserRuleContext parent, int invokingState) {
      super((IncrementalParserRuleContext) parent, invokingState);
    }

    @Override
    public int getRuleIndex() {
      return RULE_typeDeclaration;
    }
  }

  @RuleVersion(0)
  public final TypeDeclarationContext typeDeclaration() throws RecognitionException {
    // Check whether we need to execute this rule.
    TypeDeclarationContext guardResult = (TypeDeclarationContext) guardRule((IncrementalParserRuleContext) _ctx, getState(), RULE_typeDeclaration);
    // If we found an existing context that is valid, return it.
    if (guardResult != null) {
      this._input.seek(guardResult.stop.getTokenIndex() + 1);
      return guardResult;
    }
    TypeDeclarationContext _localctx = new TypeDeclarationContext(_ctx, getState());
    enterRule(_localctx, 6, RULE_typeDeclaration);
    try {
      setState(300);
      _errHandler.sync(this);
      switch (_input.LA(1)) {
        case ABSTRACT:
        case CLASS:
        case ENUM:
        case FINAL:
        case INTERFACE:
        case PRIVATE:
        case PROTECTED:
        case PUBLIC:
        case STATIC:
        case STRICTFP:
        case AT:
          enterOuterAlt(_localctx, 1);
        {
          setState(298);
          classOrInterfaceDeclaration();
        }
        break;
        case SEMI:
          enterOuterAlt(_localctx, 2);
        {
          setState(299);
          match(SEMI);
        }
        break;
        default:
          throw new NoViableAltException(this);
      }
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
  }

  public static class ClassOrInterfaceDeclarationContext extends IncrementalParserRuleContext {
    public ClassOrInterfaceModifiersContext classOrInterfaceModifiers() {
      return getRuleContext(ClassOrInterfaceModifiersContext.class, 0);
    }

    public ClassDeclarationContext classDeclaration() {
      return getRuleContext(ClassDeclarationContext.class, 0);
    }

    public InterfaceDeclarationContext interfaceDeclaration() {
      return getRuleContext(InterfaceDeclarationContext.class, 0);
    }

    public ClassOrInterfaceDeclarationContext(ParserRuleContext parent, int invokingState) {
      super((IncrementalParserRuleContext) parent, invokingState);
    }

    @Override
    public int getRuleIndex() {
      return RULE_classOrInterfaceDeclaration;
    }
  }

  @RuleVersion(0)
  public final ClassOrInterfaceDeclarationContext classOrInterfaceDeclaration() throws RecognitionException {
    // Check whether we need to execute this rule.
    ClassOrInterfaceDeclarationContext guardResult = (ClassOrInterfaceDeclarationContext) guardRule((IncrementalParserRuleContext) _ctx, getState(), RULE_classOrInterfaceDeclaration);
    // If we found an existing context that is valid, return it.
    if (guardResult != null) {
      this._input.seek(guardResult.stop.getTokenIndex() + 1);
      return guardResult;
    }
    ClassOrInterfaceDeclarationContext _localctx = new ClassOrInterfaceDeclarationContext(_ctx, getState());
    enterRule(_localctx, 8, RULE_classOrInterfaceDeclaration);
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(302);
        classOrInterfaceModifiers();
        setState(305);
        _errHandler.sync(this);
        switch (_input.LA(1)) {
          case CLASS:
          case ENUM: {
            setState(303);
            classDeclaration();
          }
          break;
          case INTERFACE:
          case AT: {
            setState(304);
            interfaceDeclaration();
          }
          break;
          default:
            throw new NoViableAltException(this);
        }
      }
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
  }

  public static class ClassOrInterfaceModifiersContext extends IncrementalParserRuleContext {
    public List<? extends ClassOrInterfaceModifierContext> classOrInterfaceModifier() {
      return getRuleContexts(ClassOrInterfaceModifierContext.class);
    }

    public ClassOrInterfaceModifierContext classOrInterfaceModifier(int i) {
      return getRuleContext(ClassOrInterfaceModifierContext.class, i);
    }

    public ClassOrInterfaceModifiersContext(ParserRuleContext parent, int invokingState) {
      super((IncrementalParserRuleContext) parent, invokingState);
    }

    @Override
    public int getRuleIndex() {
      return RULE_classOrInterfaceModifiers;
    }
  }

  @RuleVersion(0)
  public final ClassOrInterfaceModifiersContext classOrInterfaceModifiers() throws RecognitionException {
    // Check whether we need to execute this rule.
    ClassOrInterfaceModifiersContext guardResult = (ClassOrInterfaceModifiersContext) guardRule((IncrementalParserRuleContext) _ctx, getState(), RULE_classOrInterfaceModifiers);
    // If we found an existing context that is valid, return it.
    if (guardResult != null) {
      this._input.seek(guardResult.stop.getTokenIndex() + 1);
      return guardResult;
    }
    ClassOrInterfaceModifiersContext _localctx = new ClassOrInterfaceModifiersContext(_ctx, getState());
    enterRule(_localctx, 10, RULE_classOrInterfaceModifiers);
    try {
      int _alt;
      enterOuterAlt(_localctx, 1);
      {
        setState(310);
        _errHandler.sync(this);
        _alt = getInterpreter().adaptivePredict(_input, 12, _ctx);
        while (_alt != 2 && _alt != ATN.INVALID_ALT_NUMBER) {
          if (_alt == 1) {
            {
              {
                setState(307);
                classOrInterfaceModifier();
              }
            }
          }
          setState(312);
          _errHandler.sync(this);
          _alt = getInterpreter().adaptivePredict(_input, 12, _ctx);
        }
      }
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
  }

  public static class ClassOrInterfaceModifierContext extends IncrementalParserRuleContext {
    public AnnotationContext annotation() {
      return getRuleContext(AnnotationContext.class, 0);
    }

    public TerminalNode PUBLIC() {
      return getToken(TestIncrementalJavaParser.PUBLIC, 0);
    }

    public TerminalNode PROTECTED() {
      return getToken(TestIncrementalJavaParser.PROTECTED, 0);
    }

    public TerminalNode PRIVATE() {
      return getToken(TestIncrementalJavaParser.PRIVATE, 0);
    }

    public TerminalNode ABSTRACT() {
      return getToken(TestIncrementalJavaParser.ABSTRACT, 0);
    }

    public TerminalNode STATIC() {
      return getToken(TestIncrementalJavaParser.STATIC, 0);
    }

    public TerminalNode FINAL() {
      return getToken(TestIncrementalJavaParser.FINAL, 0);
    }

    public TerminalNode STRICTFP() {
      return getToken(TestIncrementalJavaParser.STRICTFP, 0);
    }

    public ClassOrInterfaceModifierContext(ParserRuleContext parent, int invokingState) {
      super((IncrementalParserRuleContext) parent, invokingState);
    }

    @Override
    public int getRuleIndex() {
      return RULE_classOrInterfaceModifier;
    }
  }

  @RuleVersion(0)
  public final ClassOrInterfaceModifierContext classOrInterfaceModifier() throws RecognitionException {
    // Check whether we need to execute this rule.
    ClassOrInterfaceModifierContext guardResult = (ClassOrInterfaceModifierContext) guardRule((IncrementalParserRuleContext) _ctx, getState(), RULE_classOrInterfaceModifier);
    // If we found an existing context that is valid, return it.
    if (guardResult != null) {
      this._input.seek(guardResult.stop.getTokenIndex() + 1);
      return guardResult;
    }
    ClassOrInterfaceModifierContext _localctx = new ClassOrInterfaceModifierContext(_ctx, getState());
    enterRule(_localctx, 12, RULE_classOrInterfaceModifier);
    int _la;
    try {
      setState(315);
      _errHandler.sync(this);
      switch (_input.LA(1)) {
        case AT:
          enterOuterAlt(_localctx, 1);
        {
          setState(313);
          annotation();
        }
        break;
        case ABSTRACT:
        case FINAL:
        case PRIVATE:
        case PROTECTED:
        case PUBLIC:
        case STATIC:
        case STRICTFP:
          enterOuterAlt(_localctx, 2);
        {
          setState(314);
          _la = _input.LA(1);
          if (!((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << ABSTRACT) | (1L << FINAL) | (1L << PRIVATE) | (1L << PROTECTED) | (1L << PUBLIC) | (1L << STATIC) | (1L << STRICTFP))) != 0))) {
            _errHandler.recoverInline(this);
          } else {
            if (_input.LA(1) == Token.EOF) {
              matchedEOF = true;
            }

            _errHandler.reportMatch(this);
            consume();
          }
        }
        break;
        default:
          throw new NoViableAltException(this);
      }
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
  }

  public static class ModifiersContext extends IncrementalParserRuleContext {
    public List<? extends ModifierContext> modifier() {
      return getRuleContexts(ModifierContext.class);
    }

    public ModifierContext modifier(int i) {
      return getRuleContext(ModifierContext.class, i);
    }

    public ModifiersContext(ParserRuleContext parent, int invokingState) {
      super((IncrementalParserRuleContext) parent, invokingState);
    }

    @Override
    public int getRuleIndex() {
      return RULE_modifiers;
    }
  }

  @RuleVersion(0)
  public final ModifiersContext modifiers() throws RecognitionException {
    // Check whether we need to execute this rule.
    ModifiersContext guardResult = (ModifiersContext) guardRule((IncrementalParserRuleContext) _ctx, getState(), RULE_modifiers);
    // If we found an existing context that is valid, return it.
    if (guardResult != null) {
      this._input.seek(guardResult.stop.getTokenIndex() + 1);
      return guardResult;
    }
    ModifiersContext _localctx = new ModifiersContext(_ctx, getState());
    enterRule(_localctx, 14, RULE_modifiers);
    try {
      int _alt;
      enterOuterAlt(_localctx, 1);
      {
        setState(320);
        _errHandler.sync(this);
        _alt = getInterpreter().adaptivePredict(_input, 14, _ctx);
        while (_alt != 2 && _alt != ATN.INVALID_ALT_NUMBER) {
          if (_alt == 1) {
            {
              {
                setState(317);
                modifier();
              }
            }
          }
          setState(322);
          _errHandler.sync(this);
          _alt = getInterpreter().adaptivePredict(_input, 14, _ctx);
        }
      }
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
  }

  public static class ClassDeclarationContext extends IncrementalParserRuleContext {
    public NormalClassDeclarationContext normalClassDeclaration() {
      return getRuleContext(NormalClassDeclarationContext.class, 0);
    }

    public EnumDeclarationContext enumDeclaration() {
      return getRuleContext(EnumDeclarationContext.class, 0);
    }

    public ClassDeclarationContext(ParserRuleContext parent, int invokingState) {
      super((IncrementalParserRuleContext) parent, invokingState);
    }

    @Override
    public int getRuleIndex() {
      return RULE_classDeclaration;
    }
  }

  @RuleVersion(0)
  public final ClassDeclarationContext classDeclaration() throws RecognitionException {
    // Check whether we need to execute this rule.
    ClassDeclarationContext guardResult = (ClassDeclarationContext) guardRule((IncrementalParserRuleContext) _ctx, getState(), RULE_classDeclaration);
    // If we found an existing context that is valid, return it.
    if (guardResult != null) {
      this._input.seek(guardResult.stop.getTokenIndex() + 1);
      return guardResult;
    }
    ClassDeclarationContext _localctx = new ClassDeclarationContext(_ctx, getState());
    enterRule(_localctx, 16, RULE_classDeclaration);
    try {
      setState(325);
      _errHandler.sync(this);
      switch (_input.LA(1)) {
        case CLASS:
          enterOuterAlt(_localctx, 1);
        {
          setState(323);
          normalClassDeclaration();
        }
        break;
        case ENUM:
          enterOuterAlt(_localctx, 2);
        {
          setState(324);
          enumDeclaration();
        }
        break;
        default:
          throw new NoViableAltException(this);
      }
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
  }

  public static class NormalClassDeclarationContext extends IncrementalParserRuleContext {
    public TerminalNode CLASS() {
      return getToken(TestIncrementalJavaParser.CLASS, 0);
    }

    public TerminalNode Identifier() {
      return getToken(TestIncrementalJavaParser.Identifier, 0);
    }

    public ClassBodyContext classBody() {
      return getRuleContext(ClassBodyContext.class, 0);
    }

    public TypeParametersContext typeParameters() {
      return getRuleContext(TypeParametersContext.class, 0);
    }

    public TerminalNode EXTENDS() {
      return getToken(TestIncrementalJavaParser.EXTENDS, 0);
    }

    public TypeContext type() {
      return getRuleContext(TypeContext.class, 0);
    }

    public TerminalNode IMPLEMENTS() {
      return getToken(TestIncrementalJavaParser.IMPLEMENTS, 0);
    }

    public TypeListContext typeList() {
      return getRuleContext(TypeListContext.class, 0);
    }

    public NormalClassDeclarationContext(ParserRuleContext parent, int invokingState) {
      super((IncrementalParserRuleContext) parent, invokingState);
    }

    @Override
    public int getRuleIndex() {
      return RULE_normalClassDeclaration;
    }
  }

  @RuleVersion(0)
  public final NormalClassDeclarationContext normalClassDeclaration() throws RecognitionException {
    // Check whether we need to execute this rule.
    NormalClassDeclarationContext guardResult = (NormalClassDeclarationContext) guardRule((IncrementalParserRuleContext) _ctx, getState(), RULE_normalClassDeclaration);
    // If we found an existing context that is valid, return it.
    if (guardResult != null) {
      this._input.seek(guardResult.stop.getTokenIndex() + 1);
      return guardResult;
    }
    NormalClassDeclarationContext _localctx = new NormalClassDeclarationContext(_ctx, getState());
    enterRule(_localctx, 18, RULE_normalClassDeclaration);
    int _la;
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(327);
        match(CLASS);
        setState(328);
        match(Identifier);
        setState(330);
        _errHandler.sync(this);
        _la = _input.LA(1);
        if (_la == LT) {
          {
            setState(329);
            typeParameters();
          }
        }

        setState(334);
        _errHandler.sync(this);
        _la = _input.LA(1);
        if (_la == EXTENDS) {
          {
            setState(332);
            match(EXTENDS);
            setState(333);
            type();
          }
        }

        setState(338);
        _errHandler.sync(this);
        _la = _input.LA(1);
        if (_la == IMPLEMENTS) {
          {
            setState(336);
            match(IMPLEMENTS);
            setState(337);
            typeList();
          }
        }

        setState(340);
        classBody();
      }
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
  }

  public static class TypeParametersContext extends IncrementalParserRuleContext {
    public TerminalNode LT() {
      return getToken(TestIncrementalJavaParser.LT, 0);
    }

    public List<? extends TypeParameterContext> typeParameter() {
      return getRuleContexts(TypeParameterContext.class);
    }

    public TypeParameterContext typeParameter(int i) {
      return getRuleContext(TypeParameterContext.class, i);
    }

    public TerminalNode GT() {
      return getToken(TestIncrementalJavaParser.GT, 0);
    }

    public List<? extends TerminalNode> COMMA() {
      return getTokens(TestIncrementalJavaParser.COMMA);
    }

    public TerminalNode COMMA(int i) {
      return getToken(TestIncrementalJavaParser.COMMA, i);
    }

    public TypeParametersContext(ParserRuleContext parent, int invokingState) {
      super((IncrementalParserRuleContext) parent, invokingState);
    }

    @Override
    public int getRuleIndex() {
      return RULE_typeParameters;
    }
  }

  @RuleVersion(0)
  public final TypeParametersContext typeParameters() throws RecognitionException {
    // Check whether we need to execute this rule.
    TypeParametersContext guardResult = (TypeParametersContext) guardRule((IncrementalParserRuleContext) _ctx, getState(), RULE_typeParameters);
    // If we found an existing context that is valid, return it.
    if (guardResult != null) {
      this._input.seek(guardResult.stop.getTokenIndex() + 1);
      return guardResult;
    }
    TypeParametersContext _localctx = new TypeParametersContext(_ctx, getState());
    enterRule(_localctx, 20, RULE_typeParameters);
    int _la;
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(342);
        match(LT);
        setState(343);
        typeParameter();
        setState(348);
        _errHandler.sync(this);
        _la = _input.LA(1);
        while (_la == COMMA) {
          {
            {
              setState(344);
              match(COMMA);
              setState(345);
              typeParameter();
            }
          }
          setState(350);
          _errHandler.sync(this);
          _la = _input.LA(1);
        }
        setState(351);
        match(GT);
      }
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
  }

  public static class TypeParameterContext extends IncrementalParserRuleContext {
    public TerminalNode Identifier() {
      return getToken(TestIncrementalJavaParser.Identifier, 0);
    }

    public TerminalNode EXTENDS() {
      return getToken(TestIncrementalJavaParser.EXTENDS, 0);
    }

    public TypeBoundContext typeBound() {
      return getRuleContext(TypeBoundContext.class, 0);
    }

    public TypeParameterContext(ParserRuleContext parent, int invokingState) {
      super((IncrementalParserRuleContext) parent, invokingState);
    }

    @Override
    public int getRuleIndex() {
      return RULE_typeParameter;
    }
  }

  @RuleVersion(0)
  public final TypeParameterContext typeParameter() throws RecognitionException {
    // Check whether we need to execute this rule.
    TypeParameterContext guardResult = (TypeParameterContext) guardRule((IncrementalParserRuleContext) _ctx, getState(), RULE_typeParameter);
    // If we found an existing context that is valid, return it.
    if (guardResult != null) {
      this._input.seek(guardResult.stop.getTokenIndex() + 1);
      return guardResult;
    }
    TypeParameterContext _localctx = new TypeParameterContext(_ctx, getState());
    enterRule(_localctx, 22, RULE_typeParameter);
    int _la;
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(353);
        match(Identifier);
        setState(356);
        _errHandler.sync(this);
        _la = _input.LA(1);
        if (_la == EXTENDS) {
          {
            setState(354);
            match(EXTENDS);
            setState(355);
            typeBound();
          }
        }

      }
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
  }

  public static class TypeBoundContext extends IncrementalParserRuleContext {
    public List<? extends TypeContext> type() {
      return getRuleContexts(TypeContext.class);
    }

    public TypeContext type(int i) {
      return getRuleContext(TypeContext.class, i);
    }

    public List<? extends TerminalNode> BITAND() {
      return getTokens(TestIncrementalJavaParser.BITAND);
    }

    public TerminalNode BITAND(int i) {
      return getToken(TestIncrementalJavaParser.BITAND, i);
    }

    public TypeBoundContext(ParserRuleContext parent, int invokingState) {
      super((IncrementalParserRuleContext) parent, invokingState);
    }

    @Override
    public int getRuleIndex() {
      return RULE_typeBound;
    }
  }

  @RuleVersion(0)
  public final TypeBoundContext typeBound() throws RecognitionException {
    // Check whether we need to execute this rule.
    TypeBoundContext guardResult = (TypeBoundContext) guardRule((IncrementalParserRuleContext) _ctx, getState(), RULE_typeBound);
    // If we found an existing context that is valid, return it.
    if (guardResult != null) {
      this._input.seek(guardResult.stop.getTokenIndex() + 1);
      return guardResult;
    }
    TypeBoundContext _localctx = new TypeBoundContext(_ctx, getState());
    enterRule(_localctx, 24, RULE_typeBound);
    int _la;
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(358);
        type();
        setState(363);
        _errHandler.sync(this);
        _la = _input.LA(1);
        while (_la == BITAND) {
          {
            {
              setState(359);
              match(BITAND);
              setState(360);
              type();
            }
          }
          setState(365);
          _errHandler.sync(this);
          _la = _input.LA(1);
        }
      }
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
  }

  public static class EnumDeclarationContext extends IncrementalParserRuleContext {
    public TerminalNode ENUM() {
      return getToken(TestIncrementalJavaParser.ENUM, 0);
    }

    public TerminalNode Identifier() {
      return getToken(TestIncrementalJavaParser.Identifier, 0);
    }

    public EnumBodyContext enumBody() {
      return getRuleContext(EnumBodyContext.class, 0);
    }

    public TerminalNode IMPLEMENTS() {
      return getToken(TestIncrementalJavaParser.IMPLEMENTS, 0);
    }

    public TypeListContext typeList() {
      return getRuleContext(TypeListContext.class, 0);
    }

    public EnumDeclarationContext(ParserRuleContext parent, int invokingState) {
      super((IncrementalParserRuleContext) parent, invokingState);
    }

    @Override
    public int getRuleIndex() {
      return RULE_enumDeclaration;
    }

  }

  @RuleVersion(0)
  public final EnumDeclarationContext enumDeclaration() throws RecognitionException {
    // Check whether we need to execute this rule.
    EnumDeclarationContext guardResult = (EnumDeclarationContext) guardRule((IncrementalParserRuleContext) _ctx, getState(), RULE_enumDeclaration);
    // If we found an existing context that is valid, return it.
    if (guardResult != null) {
      this._input.seek(guardResult.stop.getTokenIndex() + 1);
      return guardResult;
    }
    EnumDeclarationContext _localctx = new EnumDeclarationContext(_ctx, getState());
    enterRule(_localctx, 26, RULE_enumDeclaration);
    int _la;
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(366);
        match(ENUM);
        setState(367);
        match(Identifier);
        setState(370);
        _errHandler.sync(this);
        _la = _input.LA(1);
        if (_la == IMPLEMENTS) {
          {
            setState(368);
            match(IMPLEMENTS);
            setState(369);
            typeList();
          }
        }

        setState(372);
        enumBody();
      }
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
  }

  public static class EnumBodyContext extends IncrementalParserRuleContext {
    public TerminalNode LBRACE() {
      return getToken(TestIncrementalJavaParser.LBRACE, 0);
    }

    public TerminalNode RBRACE() {
      return getToken(TestIncrementalJavaParser.RBRACE, 0);
    }

    public EnumConstantsContext enumConstants() {
      return getRuleContext(EnumConstantsContext.class, 0);
    }

    public TerminalNode COMMA() {
      return getToken(TestIncrementalJavaParser.COMMA, 0);
    }

    public EnumBodyDeclarationsContext enumBodyDeclarations() {
      return getRuleContext(EnumBodyDeclarationsContext.class, 0);
    }

    public EnumBodyContext(ParserRuleContext parent, int invokingState) {
      super((IncrementalParserRuleContext) parent, invokingState);
    }

    @Override
    public int getRuleIndex() {
      return RULE_enumBody;
    }
  }

  @RuleVersion(0)
  public final EnumBodyContext enumBody() throws RecognitionException {
    // Check whether we need to execute this rule.
    EnumBodyContext guardResult = (EnumBodyContext) guardRule((IncrementalParserRuleContext) _ctx, getState(), RULE_enumBody);
    // If we found an existing context that is valid, return it.
    if (guardResult != null) {
      this._input.seek(guardResult.stop.getTokenIndex() + 1);
      return guardResult;
    }
    EnumBodyContext _localctx = new EnumBodyContext(_ctx, getState());
    enterRule(_localctx, 28, RULE_enumBody);
    int _la;
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(374);
        match(LBRACE);
        setState(376);
        _errHandler.sync(this);
        _la = _input.LA(1);
        if (_la == Identifier || _la == AT) {
          {
            setState(375);
            enumConstants();
          }
        }

        setState(379);
        _errHandler.sync(this);
        _la = _input.LA(1);
        if (_la == COMMA) {
          {
            setState(378);
            match(COMMA);
          }
        }

        setState(382);
        _errHandler.sync(this);
        _la = _input.LA(1);
        if (_la == SEMI) {
          {
            setState(381);
            enumBodyDeclarations();
          }
        }

        setState(384);
        match(RBRACE);
      }
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
  }

  public static class EnumConstantsContext extends IncrementalParserRuleContext {
    public List<? extends EnumConstantContext> enumConstant() {
      return getRuleContexts(EnumConstantContext.class);
    }

    public EnumConstantContext enumConstant(int i) {
      return getRuleContext(EnumConstantContext.class, i);
    }

    public List<? extends TerminalNode> COMMA() {
      return getTokens(TestIncrementalJavaParser.COMMA);
    }

    public TerminalNode COMMA(int i) {
      return getToken(TestIncrementalJavaParser.COMMA, i);
    }

    public EnumConstantsContext(ParserRuleContext parent, int invokingState) {
      super((IncrementalParserRuleContext) parent, invokingState);
    }

    @Override
    public int getRuleIndex() {
      return RULE_enumConstants;
    }
  }

  @RuleVersion(0)
  public final EnumConstantsContext enumConstants() throws RecognitionException {
    // Check whether we need to execute this rule.
    EnumConstantsContext guardResult = (EnumConstantsContext) guardRule((IncrementalParserRuleContext) _ctx, getState(), RULE_enumConstants);
    // If we found an existing context that is valid, return it.
    if (guardResult != null) {
      this._input.seek(guardResult.stop.getTokenIndex() + 1);
      return guardResult;
    }
    EnumConstantsContext _localctx = new EnumConstantsContext(_ctx, getState());
    enterRule(_localctx, 30, RULE_enumConstants);
    try {
      int _alt;
      enterOuterAlt(_localctx, 1);
      {
        setState(386);
        enumConstant();
        setState(391);
        _errHandler.sync(this);
        _alt = getInterpreter().adaptivePredict(_input, 26, _ctx);
        while (_alt != 2 && _alt != ATN.INVALID_ALT_NUMBER) {
          if (_alt == 1) {
            {
              {
                setState(387);
                match(COMMA);
                setState(388);
                enumConstant();
              }
            }
          }
          setState(393);
          _errHandler.sync(this);
          _alt = getInterpreter().adaptivePredict(_input, 26, _ctx);
        }
      }
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
  }

  public static class EnumConstantContext extends IncrementalParserRuleContext {
    public TerminalNode Identifier() {
      return getToken(TestIncrementalJavaParser.Identifier, 0);
    }

    public AnnotationsContext annotations() {
      return getRuleContext(AnnotationsContext.class, 0);
    }

    public ArgumentsContext arguments() {
      return getRuleContext(ArgumentsContext.class, 0);
    }

    public ClassBodyContext classBody() {
      return getRuleContext(ClassBodyContext.class, 0);
    }

    public EnumConstantContext(ParserRuleContext parent, int invokingState) {
      super((IncrementalParserRuleContext) parent, invokingState);
    }

    @Override
    public int getRuleIndex() {
      return RULE_enumConstant;
    }
  }

  @RuleVersion(0)
  public final EnumConstantContext enumConstant() throws RecognitionException {
    // Check whether we need to execute this rule.
    EnumConstantContext guardResult = (EnumConstantContext) guardRule((IncrementalParserRuleContext) _ctx, getState(), RULE_enumConstant);
    // If we found an existing context that is valid, return it.
    if (guardResult != null) {
      this._input.seek(guardResult.stop.getTokenIndex() + 1);
      return guardResult;
    }
    EnumConstantContext _localctx = new EnumConstantContext(_ctx, getState());
    enterRule(_localctx, 32, RULE_enumConstant);
    int _la;
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(395);
        _errHandler.sync(this);
        _la = _input.LA(1);
        if (_la == AT) {
          {
            setState(394);
            annotations();
          }
        }

        setState(397);
        match(Identifier);
        setState(399);
        _errHandler.sync(this);
        _la = _input.LA(1);
        if (_la == LPAREN) {
          {
            setState(398);
            arguments();
          }
        }

        setState(402);
        _errHandler.sync(this);
        _la = _input.LA(1);
        if (_la == LBRACE) {
          {
            setState(401);
            classBody();
          }
        }

      }
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
  }

  public static class EnumBodyDeclarationsContext extends IncrementalParserRuleContext {
    public TerminalNode SEMI() {
      return getToken(TestIncrementalJavaParser.SEMI, 0);
    }

    public List<? extends ClassBodyDeclarationContext> classBodyDeclaration() {
      return getRuleContexts(ClassBodyDeclarationContext.class);
    }

    public ClassBodyDeclarationContext classBodyDeclaration(int i) {
      return getRuleContext(ClassBodyDeclarationContext.class, i);
    }

    public EnumBodyDeclarationsContext(ParserRuleContext parent, int invokingState) {
      super((IncrementalParserRuleContext) parent, invokingState);
    }

    @Override
    public int getRuleIndex() {
      return RULE_enumBodyDeclarations;
    }
  }

  @RuleVersion(0)
  public final EnumBodyDeclarationsContext enumBodyDeclarations() throws RecognitionException {
    // Check whether we need to execute this rule.
    EnumBodyDeclarationsContext guardResult = (EnumBodyDeclarationsContext) guardRule((IncrementalParserRuleContext) _ctx, getState(), RULE_enumBodyDeclarations);
    // If we found an existing context that is valid, return it.
    if (guardResult != null) {
      this._input.seek(guardResult.stop.getTokenIndex() + 1);
      return guardResult;
    }
    EnumBodyDeclarationsContext _localctx = new EnumBodyDeclarationsContext(_ctx, getState());
    enterRule(_localctx, 34, RULE_enumBodyDeclarations);
    int _la;
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(404);
        match(SEMI);
        setState(408);
        _errHandler.sync(this);
        _la = _input.LA(1);
        while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << ABSTRACT) | (1L << BOOLEAN) | (1L << BYTE) | (1L << CHAR) | (1L << CLASS) | (1L << DOUBLE) | (1L << ENUM) | (1L << FINAL) | (1L << FLOAT) | (1L << INT) | (1L << INTERFACE) | (1L << LONG) | (1L << NATIVE) | (1L << PRIVATE) | (1L << PROTECTED) | (1L << PUBLIC) | (1L << SHORT) | (1L << STATIC) | (1L << STRICTFP) | (1L << SYNCHRONIZED) | (1L << TRANSIENT) | (1L << VOID) | (1L << VOLATILE) | (1L << LBRACE) | (1L << SEMI))) != 0) || ((((_la - 68)) & ~0x3f) == 0 && ((1L << (_la - 68)) & ((1L << (LT - 68)) | (1L << (Identifier - 68)) | (1L << (AT - 68)))) != 0)) {
          {
            {
              setState(405);
              classBodyDeclaration();
            }
          }
          setState(410);
          _errHandler.sync(this);
          _la = _input.LA(1);
        }
      }
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
  }

  public static class InterfaceDeclarationContext extends IncrementalParserRuleContext {
    public NormalInterfaceDeclarationContext normalInterfaceDeclaration() {
      return getRuleContext(NormalInterfaceDeclarationContext.class, 0);
    }

    public AnnotationTypeDeclarationContext annotationTypeDeclaration() {
      return getRuleContext(AnnotationTypeDeclarationContext.class, 0);
    }

    public InterfaceDeclarationContext(ParserRuleContext parent, int invokingState) {
      super((IncrementalParserRuleContext) parent, invokingState);
    }

    @Override
    public int getRuleIndex() {
      return RULE_interfaceDeclaration;
    }
  }

  @RuleVersion(0)
  public final InterfaceDeclarationContext interfaceDeclaration() throws RecognitionException {
    // Check whether we need to execute this rule.
    InterfaceDeclarationContext guardResult = (InterfaceDeclarationContext) guardRule((IncrementalParserRuleContext) _ctx, getState(), RULE_interfaceDeclaration);
    // If we found an existing context that is valid, return it.
    if (guardResult != null) {
      this._input.seek(guardResult.stop.getTokenIndex() + 1);
      return guardResult;
    }
    InterfaceDeclarationContext _localctx = new InterfaceDeclarationContext(_ctx, getState());
    enterRule(_localctx, 36, RULE_interfaceDeclaration);
    try {
      setState(413);
      _errHandler.sync(this);
      switch (_input.LA(1)) {
        case INTERFACE:
          enterOuterAlt(_localctx, 1);
        {
          setState(411);
          normalInterfaceDeclaration();
        }
        break;
        case AT:
          enterOuterAlt(_localctx, 2);
        {
          setState(412);
          annotationTypeDeclaration();
        }
        break;
        default:
          throw new NoViableAltException(this);
      }
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
  }

  public static class NormalInterfaceDeclarationContext extends IncrementalParserRuleContext {
    public TerminalNode INTERFACE() {
      return getToken(TestIncrementalJavaParser.INTERFACE, 0);
    }

    public TerminalNode Identifier() {
      return getToken(TestIncrementalJavaParser.Identifier, 0);
    }

    public InterfaceBodyContext interfaceBody() {
      return getRuleContext(InterfaceBodyContext.class, 0);
    }

    public TypeParametersContext typeParameters() {
      return getRuleContext(TypeParametersContext.class, 0);
    }

    public TerminalNode EXTENDS() {
      return getToken(TestIncrementalJavaParser.EXTENDS, 0);
    }

    public TypeListContext typeList() {
      return getRuleContext(TypeListContext.class, 0);
    }

    public NormalInterfaceDeclarationContext(ParserRuleContext parent, int invokingState) {
      super((IncrementalParserRuleContext) parent, invokingState);
    }

    @Override
    public int getRuleIndex() {
      return RULE_normalInterfaceDeclaration;
    }
  }

  @RuleVersion(0)
  public final NormalInterfaceDeclarationContext normalInterfaceDeclaration() throws RecognitionException {
    // Check whether we need to execute this rule.
    NormalInterfaceDeclarationContext guardResult = (NormalInterfaceDeclarationContext) guardRule((IncrementalParserRuleContext) _ctx, getState(), RULE_normalInterfaceDeclaration);
    // If we found an existing context that is valid, return it.
    if (guardResult != null) {
      this._input.seek(guardResult.stop.getTokenIndex() + 1);
      return guardResult;
    }
    NormalInterfaceDeclarationContext _localctx = new NormalInterfaceDeclarationContext(_ctx, getState());
    enterRule(_localctx, 38, RULE_normalInterfaceDeclaration);
    int _la;
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(415);
        match(INTERFACE);
        setState(416);
        match(Identifier);
        setState(418);
        _errHandler.sync(this);
        _la = _input.LA(1);
        if (_la == LT) {
          {
            setState(417);
            typeParameters();
          }
        }

        setState(422);
        _errHandler.sync(this);
        _la = _input.LA(1);
        if (_la == EXTENDS) {
          {
            setState(420);
            match(EXTENDS);
            setState(421);
            typeList();
          }
        }

        setState(424);
        interfaceBody();
      }
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
  }

  public static class TypeListContext extends IncrementalParserRuleContext {
    public List<? extends TypeContext> type() {
      return getRuleContexts(TypeContext.class);
    }

    public TypeContext type(int i) {
      return getRuleContext(TypeContext.class, i);
    }

    public List<? extends TerminalNode> COMMA() {
      return getTokens(TestIncrementalJavaParser.COMMA);
    }

    public TerminalNode COMMA(int i) {
      return getToken(TestIncrementalJavaParser.COMMA, i);
    }

    public TypeListContext(ParserRuleContext parent, int invokingState) {
      super((IncrementalParserRuleContext) parent, invokingState);
    }

    @Override
    public int getRuleIndex() {
      return RULE_typeList;
    }
  }

  @RuleVersion(0)
  public final TypeListContext typeList() throws RecognitionException {
    // Check whether we need to execute this rule.
    TypeListContext guardResult = (TypeListContext) guardRule((IncrementalParserRuleContext) _ctx, getState(), RULE_typeList);
    // If we found an existing context that is valid, return it.
    if (guardResult != null) {
      this._input.seek(guardResult.stop.getTokenIndex() + 1);
      return guardResult;
    }
    TypeListContext _localctx = new TypeListContext(_ctx, getState());
    enterRule(_localctx, 40, RULE_typeList);
    int _la;
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(426);
        type();
        setState(431);
        _errHandler.sync(this);
        _la = _input.LA(1);
        while (_la == COMMA) {
          {
            {
              setState(427);
              match(COMMA);
              setState(428);
              type();
            }
          }
          setState(433);
          _errHandler.sync(this);
          _la = _input.LA(1);
        }
      }
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
  }

  public static class ClassBodyContext extends IncrementalParserRuleContext {
    public TerminalNode LBRACE() {
      return getToken(TestIncrementalJavaParser.LBRACE, 0);
    }

    public TerminalNode RBRACE() {
      return getToken(TestIncrementalJavaParser.RBRACE, 0);
    }

    public List<? extends ClassBodyDeclarationContext> classBodyDeclaration() {
      return getRuleContexts(ClassBodyDeclarationContext.class);
    }

    public ClassBodyDeclarationContext classBodyDeclaration(int i) {
      return getRuleContext(ClassBodyDeclarationContext.class, i);
    }

    public ClassBodyContext(ParserRuleContext parent, int invokingState) {
      super((IncrementalParserRuleContext) parent, invokingState);
    }

    @Override
    public int getRuleIndex() {
      return RULE_classBody;
    }
  }

  @RuleVersion(0)
  public final ClassBodyContext classBody() throws RecognitionException {
    // Check whether we need to execute this rule.
    ClassBodyContext guardResult = (ClassBodyContext) guardRule((IncrementalParserRuleContext) _ctx, getState(), RULE_classBody);
    // If we found an existing context that is valid, return it.
    if (guardResult != null) {
      this._input.seek(guardResult.stop.getTokenIndex() + 1);
      return guardResult;
    }
    ClassBodyContext _localctx = new ClassBodyContext(_ctx, getState());
    enterRule(_localctx, 42, RULE_classBody);
    int _la;
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(434);
        match(LBRACE);
        setState(438);
        _errHandler.sync(this);
        _la = _input.LA(1);
        while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << ABSTRACT) | (1L << BOOLEAN) | (1L << BYTE) | (1L << CHAR) | (1L << CLASS) | (1L << DOUBLE) | (1L << ENUM) | (1L << FINAL) | (1L << FLOAT) | (1L << INT) | (1L << INTERFACE) | (1L << LONG) | (1L << NATIVE) | (1L << PRIVATE) | (1L << PROTECTED) | (1L << PUBLIC) | (1L << SHORT) | (1L << STATIC) | (1L << STRICTFP) | (1L << SYNCHRONIZED) | (1L << TRANSIENT) | (1L << VOID) | (1L << VOLATILE) | (1L << LBRACE) | (1L << SEMI))) != 0) || ((((_la - 68)) & ~0x3f) == 0 && ((1L << (_la - 68)) & ((1L << (LT - 68)) | (1L << (Identifier - 68)) | (1L << (AT - 68)))) != 0)) {
          {
            {
              setState(435);
              classBodyDeclaration();
            }
          }
          setState(440);
          _errHandler.sync(this);
          _la = _input.LA(1);
        }
        setState(441);
        match(RBRACE);
      }
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
  }

  public static class InterfaceBodyContext extends IncrementalParserRuleContext {
    public TerminalNode LBRACE() {
      return getToken(TestIncrementalJavaParser.LBRACE, 0);
    }

    public TerminalNode RBRACE() {
      return getToken(TestIncrementalJavaParser.RBRACE, 0);
    }

    public List<? extends InterfaceBodyDeclarationContext> interfaceBodyDeclaration() {
      return getRuleContexts(InterfaceBodyDeclarationContext.class);
    }

    public InterfaceBodyDeclarationContext interfaceBodyDeclaration(int i) {
      return getRuleContext(InterfaceBodyDeclarationContext.class, i);
    }

    public InterfaceBodyContext(ParserRuleContext parent, int invokingState) {
      super((IncrementalParserRuleContext) parent, invokingState);
    }

    @Override
    public int getRuleIndex() {
      return RULE_interfaceBody;
    }
  }

  @RuleVersion(0)
  public final InterfaceBodyContext interfaceBody() throws RecognitionException {
    // Check whether we need to execute this rule.
    InterfaceBodyContext guardResult = (InterfaceBodyContext) guardRule((IncrementalParserRuleContext) _ctx, getState(), RULE_interfaceBody);
    // If we found an existing context that is valid, return it.
    if (guardResult != null) {
      this._input.seek(guardResult.stop.getTokenIndex() + 1);
      return guardResult;
    }
    InterfaceBodyContext _localctx = new InterfaceBodyContext(_ctx, getState());
    enterRule(_localctx, 44, RULE_interfaceBody);
    int _la;
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(443);
        match(LBRACE);
        setState(447);
        _errHandler.sync(this);
        _la = _input.LA(1);
        while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << ABSTRACT) | (1L << BOOLEAN) | (1L << BYTE) | (1L << CHAR) | (1L << CLASS) | (1L << DOUBLE) | (1L << ENUM) | (1L << FINAL) | (1L << FLOAT) | (1L << INT) | (1L << INTERFACE) | (1L << LONG) | (1L << NATIVE) | (1L << PRIVATE) | (1L << PROTECTED) | (1L << PUBLIC) | (1L << SHORT) | (1L << STATIC) | (1L << STRICTFP) | (1L << SYNCHRONIZED) | (1L << TRANSIENT) | (1L << VOID) | (1L << VOLATILE) | (1L << SEMI))) != 0) || ((((_la - 68)) & ~0x3f) == 0 && ((1L << (_la - 68)) & ((1L << (LT - 68)) | (1L << (Identifier - 68)) | (1L << (AT - 68)))) != 0)) {
          {
            {
              setState(444);
              interfaceBodyDeclaration();
            }
          }
          setState(449);
          _errHandler.sync(this);
          _la = _input.LA(1);
        }
        setState(450);
        match(RBRACE);
      }
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
  }

  public static class ClassBodyDeclarationContext extends IncrementalParserRuleContext {
    public TerminalNode SEMI() {
      return getToken(TestIncrementalJavaParser.SEMI, 0);
    }

    public BlockContext block() {
      return getRuleContext(BlockContext.class, 0);
    }

    public TerminalNode STATIC() {
      return getToken(TestIncrementalJavaParser.STATIC, 0);
    }

    public ModifiersContext modifiers() {
      return getRuleContext(ModifiersContext.class, 0);
    }

    public MemberDeclContext memberDecl() {
      return getRuleContext(MemberDeclContext.class, 0);
    }

    public ClassBodyDeclarationContext(ParserRuleContext parent, int invokingState) {
      super((IncrementalParserRuleContext) parent, invokingState);
    }

    @Override
    public int getRuleIndex() {
      return RULE_classBodyDeclaration;
    }
  }

  @RuleVersion(0)
  public final ClassBodyDeclarationContext classBodyDeclaration() throws RecognitionException {
    // Check whether we need to execute this rule.
    ClassBodyDeclarationContext guardResult = (ClassBodyDeclarationContext) guardRule((IncrementalParserRuleContext) _ctx, getState(), RULE_classBodyDeclaration);
    // If we found an existing context that is valid, return it.
    if (guardResult != null) {
      this._input.seek(guardResult.stop.getTokenIndex() + 1);
      return guardResult;
    }
    ClassBodyDeclarationContext _localctx = new ClassBodyDeclarationContext(_ctx, getState());
    enterRule(_localctx, 46, RULE_classBodyDeclaration);
    int _la;
    try {
      setState(460);
      _errHandler.sync(this);
      switch (getInterpreter().adaptivePredict(_input, 38, _ctx)) {
        case 1:
          enterOuterAlt(_localctx, 1);
        {
          setState(452);
          match(SEMI);
        }
        break;
        case 2:
          enterOuterAlt(_localctx, 2);
        {
          setState(454);
          _errHandler.sync(this);
          _la = _input.LA(1);
          if (_la == STATIC) {
            {
              setState(453);
              match(STATIC);
            }
          }

          setState(456);
          block();
        }
        break;
        case 3:
          enterOuterAlt(_localctx, 3);
        {
          setState(457);
          modifiers();
          setState(458);
          memberDecl();
        }
        break;
      }
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
  }

  public static class MemberDeclContext extends IncrementalParserRuleContext {
    public GenericMethodOrConstructorDeclContext genericMethodOrConstructorDecl() {
      return getRuleContext(GenericMethodOrConstructorDeclContext.class, 0);
    }

    public MemberDeclarationContext memberDeclaration() {
      return getRuleContext(MemberDeclarationContext.class, 0);
    }

    public TerminalNode VOID() {
      return getToken(TestIncrementalJavaParser.VOID, 0);
    }

    public TerminalNode Identifier() {
      return getToken(TestIncrementalJavaParser.Identifier, 0);
    }

    public VoidMethodDeclaratorRestContext voidMethodDeclaratorRest() {
      return getRuleContext(VoidMethodDeclaratorRestContext.class, 0);
    }

    public ConstructorDeclaratorRestContext constructorDeclaratorRest() {
      return getRuleContext(ConstructorDeclaratorRestContext.class, 0);
    }

    public InterfaceDeclarationContext interfaceDeclaration() {
      return getRuleContext(InterfaceDeclarationContext.class, 0);
    }

    public ClassDeclarationContext classDeclaration() {
      return getRuleContext(ClassDeclarationContext.class, 0);
    }

    public MemberDeclContext(ParserRuleContext parent, int invokingState) {
      super((IncrementalParserRuleContext) parent, invokingState);
    }

    @Override
    public int getRuleIndex() {
      return RULE_memberDecl;
    }

  }

  @RuleVersion(0)
  public final MemberDeclContext memberDecl() throws RecognitionException {
    // Check whether we need to execute this rule.
    MemberDeclContext guardResult = (MemberDeclContext) guardRule((IncrementalParserRuleContext) _ctx, getState(), RULE_memberDecl);
    // If we found an existing context that is valid, return it.
    if (guardResult != null) {
      this._input.seek(guardResult.stop.getTokenIndex() + 1);
      return guardResult;
    }
    MemberDeclContext _localctx = new MemberDeclContext(_ctx, getState());
    enterRule(_localctx, 48, RULE_memberDecl);
    try {
      setState(471);
      _errHandler.sync(this);
      switch (getInterpreter().adaptivePredict(_input, 39, _ctx)) {
        case 1:
          enterOuterAlt(_localctx, 1);
        {
          setState(462);
          genericMethodOrConstructorDecl();
        }
        break;
        case 2:
          enterOuterAlt(_localctx, 2);
        {
          setState(463);
          memberDeclaration();
        }
        break;
        case 3:
          enterOuterAlt(_localctx, 3);
        {
          setState(464);
          match(VOID);
          setState(465);
          match(Identifier);
          setState(466);
          voidMethodDeclaratorRest();
        }
        break;
        case 4:
          enterOuterAlt(_localctx, 4);
        {
          setState(467);
          match(Identifier);
          setState(468);
          constructorDeclaratorRest();
        }
        break;
        case 5:
          enterOuterAlt(_localctx, 5);
        {
          setState(469);
          interfaceDeclaration();
        }
        break;
        case 6:
          enterOuterAlt(_localctx, 6);
        {
          setState(470);
          classDeclaration();
        }
        break;
      }
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
  }

  public static class MemberDeclarationContext extends IncrementalParserRuleContext {
    public TypeContext type() {
      return getRuleContext(TypeContext.class, 0);
    }

    public MethodDeclarationContext methodDeclaration() {
      return getRuleContext(MethodDeclarationContext.class, 0);
    }

    public FieldDeclarationContext fieldDeclaration() {
      return getRuleContext(FieldDeclarationContext.class, 0);
    }

    public MemberDeclarationContext(ParserRuleContext parent, int invokingState) {
      super((IncrementalParserRuleContext) parent, invokingState);
    }

    @Override
    public int getRuleIndex() {
      return RULE_memberDeclaration;
    }
  }

  @RuleVersion(0)
  public final MemberDeclarationContext memberDeclaration() throws RecognitionException {
    // Check whether we need to execute this rule.
    MemberDeclarationContext guardResult = (MemberDeclarationContext) guardRule((IncrementalParserRuleContext) _ctx, getState(), RULE_memberDeclaration);
    // If we found an existing context that is valid, return it.
    if (guardResult != null) {
      this._input.seek(guardResult.stop.getTokenIndex() + 1);
      return guardResult;
    }
    MemberDeclarationContext _localctx = new MemberDeclarationContext(_ctx, getState());
    enterRule(_localctx, 50, RULE_memberDeclaration);
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(473);
        type();
        setState(476);
        _errHandler.sync(this);
        switch (getInterpreter().adaptivePredict(_input, 40, _ctx)) {
          case 1: {
            setState(474);
            methodDeclaration();
          }
          break;
          case 2: {
            setState(475);
            fieldDeclaration();
          }
          break;
        }
      }
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
  }

  public static class GenericMethodOrConstructorDeclContext extends IncrementalParserRuleContext {
    public TypeParametersContext typeParameters() {
      return getRuleContext(TypeParametersContext.class, 0);
    }

    public GenericMethodOrConstructorRestContext genericMethodOrConstructorRest() {
      return getRuleContext(GenericMethodOrConstructorRestContext.class, 0);
    }

    public GenericMethodOrConstructorDeclContext(ParserRuleContext parent, int invokingState) {
      super((IncrementalParserRuleContext) parent, invokingState);
    }

    @Override
    public int getRuleIndex() {
      return RULE_genericMethodOrConstructorDecl;
    }
  }

  @RuleVersion(0)
  public final GenericMethodOrConstructorDeclContext genericMethodOrConstructorDecl() throws RecognitionException {
    // Check whether we need to execute this rule.
    GenericMethodOrConstructorDeclContext guardResult = (GenericMethodOrConstructorDeclContext) guardRule((IncrementalParserRuleContext) _ctx, getState(), RULE_genericMethodOrConstructorDecl);
    // If we found an existing context that is valid, return it.
    if (guardResult != null) {
      this._input.seek(guardResult.stop.getTokenIndex() + 1);
      return guardResult;
    }
    GenericMethodOrConstructorDeclContext _localctx = new GenericMethodOrConstructorDeclContext(_ctx, getState());
    enterRule(_localctx, 52, RULE_genericMethodOrConstructorDecl);
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(478);
        typeParameters();
        setState(479);
        genericMethodOrConstructorRest();
      }
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
  }

  public static class GenericMethodOrConstructorRestContext extends IncrementalParserRuleContext {
    public TerminalNode Identifier() {
      return getToken(TestIncrementalJavaParser.Identifier, 0);
    }

    public MethodDeclaratorRestContext methodDeclaratorRest() {
      return getRuleContext(MethodDeclaratorRestContext.class, 0);
    }

    public TypeContext type() {
      return getRuleContext(TypeContext.class, 0);
    }

    public TerminalNode VOID() {
      return getToken(TestIncrementalJavaParser.VOID, 0);
    }

    public ConstructorDeclaratorRestContext constructorDeclaratorRest() {
      return getRuleContext(ConstructorDeclaratorRestContext.class, 0);
    }

    public GenericMethodOrConstructorRestContext(ParserRuleContext parent, int invokingState) {
      super((IncrementalParserRuleContext) parent, invokingState);
    }

    @Override
    public int getRuleIndex() {
      return RULE_genericMethodOrConstructorRest;
    }
  }

  @RuleVersion(0)
  public final GenericMethodOrConstructorRestContext genericMethodOrConstructorRest() throws RecognitionException {
    // Check whether we need to execute this rule.
    GenericMethodOrConstructorRestContext guardResult = (GenericMethodOrConstructorRestContext) guardRule((IncrementalParserRuleContext) _ctx, getState(), RULE_genericMethodOrConstructorRest);
    // If we found an existing context that is valid, return it.
    if (guardResult != null) {
      this._input.seek(guardResult.stop.getTokenIndex() + 1);
      return guardResult;
    }
    GenericMethodOrConstructorRestContext _localctx = new GenericMethodOrConstructorRestContext(_ctx, getState());
    enterRule(_localctx, 54, RULE_genericMethodOrConstructorRest);
    try {
      setState(489);
      _errHandler.sync(this);
      switch (getInterpreter().adaptivePredict(_input, 42, _ctx)) {
        case 1:
          enterOuterAlt(_localctx, 1);
        {
          setState(483);
          _errHandler.sync(this);
          switch (_input.LA(1)) {
            case BOOLEAN:
            case BYTE:
            case CHAR:
            case DOUBLE:
            case FLOAT:
            case INT:
            case LONG:
            case SHORT:
            case Identifier: {
              setState(481);
              type();
            }
            break;
            case VOID: {
              setState(482);
              match(VOID);
            }
            break;
            default:
              throw new NoViableAltException(this);
          }
          setState(485);
          match(Identifier);
          setState(486);
          methodDeclaratorRest();
        }
        break;
        case 2:
          enterOuterAlt(_localctx, 2);
        {
          setState(487);
          match(Identifier);
          setState(488);
          constructorDeclaratorRest();
        }
        break;
      }
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
  }

  public static class MethodDeclarationContext extends IncrementalParserRuleContext {
    public TerminalNode Identifier() {
      return getToken(TestIncrementalJavaParser.Identifier, 0);
    }

    public MethodDeclaratorRestContext methodDeclaratorRest() {
      return getRuleContext(MethodDeclaratorRestContext.class, 0);
    }

    public MethodDeclarationContext(ParserRuleContext parent, int invokingState) {
      super((IncrementalParserRuleContext) parent, invokingState);
    }

    @Override
    public int getRuleIndex() {
      return RULE_methodDeclaration;
    }
  }

  @RuleVersion(0)
  public final MethodDeclarationContext methodDeclaration() throws RecognitionException {
    // Check whether we need to execute this rule.
    MethodDeclarationContext guardResult = (MethodDeclarationContext) guardRule((IncrementalParserRuleContext) _ctx, getState(), RULE_methodDeclaration);
    // If we found an existing context that is valid, return it.
    if (guardResult != null) {
      this._input.seek(guardResult.stop.getTokenIndex() + 1);
      return guardResult;
    }
    MethodDeclarationContext _localctx = new MethodDeclarationContext(_ctx, getState());
    enterRule(_localctx, 56, RULE_methodDeclaration);
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(491);
        match(Identifier);
        setState(492);
        methodDeclaratorRest();
      }
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
  }

  public static class FieldDeclarationContext extends IncrementalParserRuleContext {
    public VariableDeclaratorsContext variableDeclarators() {
      return getRuleContext(VariableDeclaratorsContext.class, 0);
    }

    public TerminalNode SEMI() {
      return getToken(TestIncrementalJavaParser.SEMI, 0);
    }

    public FieldDeclarationContext(ParserRuleContext parent, int invokingState) {
      super((IncrementalParserRuleContext) parent, invokingState);
    }

    @Override
    public int getRuleIndex() {
      return RULE_fieldDeclaration;
    }
  }

  @RuleVersion(0)
  public final FieldDeclarationContext fieldDeclaration() throws RecognitionException {
    // Check whether we need to execute this rule.
    FieldDeclarationContext guardResult = (FieldDeclarationContext) guardRule((IncrementalParserRuleContext) _ctx, getState(), RULE_fieldDeclaration);
    // If we found an existing context that is valid, return it.
    if (guardResult != null) {
      this._input.seek(guardResult.stop.getTokenIndex() + 1);
      return guardResult;
    }
    FieldDeclarationContext _localctx = new FieldDeclarationContext(_ctx, getState());
    enterRule(_localctx, 58, RULE_fieldDeclaration);
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(494);
        variableDeclarators();
        setState(495);
        match(SEMI);
      }
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
  }

  public static class InterfaceBodyDeclarationContext extends IncrementalParserRuleContext {
    public ModifiersContext modifiers() {
      return getRuleContext(ModifiersContext.class, 0);
    }

    public InterfaceMemberDeclContext interfaceMemberDecl() {
      return getRuleContext(InterfaceMemberDeclContext.class, 0);
    }

    public TerminalNode SEMI() {
      return getToken(TestIncrementalJavaParser.SEMI, 0);
    }

    public InterfaceBodyDeclarationContext(ParserRuleContext parent, int invokingState) {
      super((IncrementalParserRuleContext) parent, invokingState);
    }

    @Override
    public int getRuleIndex() {
      return RULE_interfaceBodyDeclaration;
    }
  }

  @RuleVersion(0)
  public final InterfaceBodyDeclarationContext interfaceBodyDeclaration() throws RecognitionException {
    // Check whether we need to execute this rule.
    InterfaceBodyDeclarationContext guardResult = (InterfaceBodyDeclarationContext) guardRule((IncrementalParserRuleContext) _ctx, getState(), RULE_interfaceBodyDeclaration);
    // If we found an existing context that is valid, return it.
    if (guardResult != null) {
      this._input.seek(guardResult.stop.getTokenIndex() + 1);
      return guardResult;
    }
    InterfaceBodyDeclarationContext _localctx = new InterfaceBodyDeclarationContext(_ctx, getState());
    enterRule(_localctx, 60, RULE_interfaceBodyDeclaration);
    try {
      setState(501);
      _errHandler.sync(this);
      switch (_input.LA(1)) {
        case ABSTRACT:
        case BOOLEAN:
        case BYTE:
        case CHAR:
        case CLASS:
        case DOUBLE:
        case ENUM:
        case FINAL:
        case FLOAT:
        case INT:
        case INTERFACE:
        case LONG:
        case NATIVE:
        case PRIVATE:
        case PROTECTED:
        case PUBLIC:
        case SHORT:
        case STATIC:
        case STRICTFP:
        case SYNCHRONIZED:
        case TRANSIENT:
        case VOID:
        case VOLATILE:
        case LT:
        case Identifier:
        case AT:
          enterOuterAlt(_localctx, 1);
        {
          setState(497);
          modifiers();
          setState(498);
          interfaceMemberDecl();
        }
        break;
        case SEMI:
          enterOuterAlt(_localctx, 2);
        {
          setState(500);
          match(SEMI);
        }
        break;
        default:
          throw new NoViableAltException(this);
      }
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
  }

  public static class InterfaceMemberDeclContext extends IncrementalParserRuleContext {
    public InterfaceMethodOrFieldDeclContext interfaceMethodOrFieldDecl() {
      return getRuleContext(InterfaceMethodOrFieldDeclContext.class, 0);
    }

    public InterfaceGenericMethodDeclContext interfaceGenericMethodDecl() {
      return getRuleContext(InterfaceGenericMethodDeclContext.class, 0);
    }

    public TerminalNode VOID() {
      return getToken(TestIncrementalJavaParser.VOID, 0);
    }

    public TerminalNode Identifier() {
      return getToken(TestIncrementalJavaParser.Identifier, 0);
    }

    public VoidInterfaceMethodDeclaratorRestContext voidInterfaceMethodDeclaratorRest() {
      return getRuleContext(VoidInterfaceMethodDeclaratorRestContext.class, 0);
    }

    public InterfaceDeclarationContext interfaceDeclaration() {
      return getRuleContext(InterfaceDeclarationContext.class, 0);
    }

    public ClassDeclarationContext classDeclaration() {
      return getRuleContext(ClassDeclarationContext.class, 0);
    }

    public InterfaceMemberDeclContext(ParserRuleContext parent, int invokingState) {
      super((IncrementalParserRuleContext) parent, invokingState);
    }

    @Override
    public int getRuleIndex() {
      return RULE_interfaceMemberDecl;
    }
  }

  @RuleVersion(0)
  public final InterfaceMemberDeclContext interfaceMemberDecl() throws RecognitionException {
    // Check whether we need to execute this rule.
    InterfaceMemberDeclContext guardResult = (InterfaceMemberDeclContext) guardRule((IncrementalParserRuleContext) _ctx, getState(), RULE_interfaceMemberDecl);
    // If we found an existing context that is valid, return it.
    if (guardResult != null) {
      this._input.seek(guardResult.stop.getTokenIndex() + 1);
      return guardResult;
    }
    InterfaceMemberDeclContext _localctx = new InterfaceMemberDeclContext(_ctx, getState());
    enterRule(_localctx, 62, RULE_interfaceMemberDecl);
    try {
      setState(510);
      _errHandler.sync(this);
      switch (_input.LA(1)) {
        case BOOLEAN:
        case BYTE:
        case CHAR:
        case DOUBLE:
        case FLOAT:
        case INT:
        case LONG:
        case SHORT:
        case Identifier:
          enterOuterAlt(_localctx, 1);
        {
          setState(503);
          interfaceMethodOrFieldDecl();
        }
        break;
        case LT:
          enterOuterAlt(_localctx, 2);
        {
          setState(504);
          interfaceGenericMethodDecl();
        }
        break;
        case VOID:
          enterOuterAlt(_localctx, 3);
        {
          setState(505);
          match(VOID);
          setState(506);
          match(Identifier);
          setState(507);
          voidInterfaceMethodDeclaratorRest();
        }
        break;
        case INTERFACE:
        case AT:
          enterOuterAlt(_localctx, 4);
        {
          setState(508);
          interfaceDeclaration();
        }
        break;
        case CLASS:
        case ENUM:
          enterOuterAlt(_localctx, 5);
        {
          setState(509);
          classDeclaration();
        }
        break;
        default:
          throw new NoViableAltException(this);
      }
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
  }

  public static class InterfaceMethodOrFieldDeclContext extends IncrementalParserRuleContext {
    public TypeContext type() {
      return getRuleContext(TypeContext.class, 0);
    }

    public TerminalNode Identifier() {
      return getToken(TestIncrementalJavaParser.Identifier, 0);
    }

    public InterfaceMethodOrFieldRestContext interfaceMethodOrFieldRest() {
      return getRuleContext(InterfaceMethodOrFieldRestContext.class, 0);
    }

    public InterfaceMethodOrFieldDeclContext(ParserRuleContext parent, int invokingState) {
      super((IncrementalParserRuleContext) parent, invokingState);
    }

    @Override
    public int getRuleIndex() {
      return RULE_interfaceMethodOrFieldDecl;
    }
  }

  @RuleVersion(0)
  public final InterfaceMethodOrFieldDeclContext interfaceMethodOrFieldDecl() throws RecognitionException {
    // Check whether we need to execute this rule.
    InterfaceMethodOrFieldDeclContext guardResult = (InterfaceMethodOrFieldDeclContext) guardRule((IncrementalParserRuleContext) _ctx, getState(), RULE_interfaceMethodOrFieldDecl);
    // If we found an existing context that is valid, return it.
    if (guardResult != null) {
      this._input.seek(guardResult.stop.getTokenIndex() + 1);
      return guardResult;
    }
    InterfaceMethodOrFieldDeclContext _localctx = new InterfaceMethodOrFieldDeclContext(_ctx, getState());
    enterRule(_localctx, 64, RULE_interfaceMethodOrFieldDecl);
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(512);
        type();
        setState(513);
        match(Identifier);
        setState(514);
        interfaceMethodOrFieldRest();
      }
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
  }

  public static class InterfaceMethodOrFieldRestContext extends IncrementalParserRuleContext {
    public ConstantDeclaratorsRestContext constantDeclaratorsRest() {
      return getRuleContext(ConstantDeclaratorsRestContext.class, 0);
    }

    public TerminalNode SEMI() {
      return getToken(TestIncrementalJavaParser.SEMI, 0);
    }

    public InterfaceMethodDeclaratorRestContext interfaceMethodDeclaratorRest() {
      return getRuleContext(InterfaceMethodDeclaratorRestContext.class, 0);
    }

    public InterfaceMethodOrFieldRestContext(ParserRuleContext parent, int invokingState) {
      super((IncrementalParserRuleContext) parent, invokingState);
    }

    @Override
    public int getRuleIndex() {
      return RULE_interfaceMethodOrFieldRest;
    }
  }

  @RuleVersion(0)
  public final InterfaceMethodOrFieldRestContext interfaceMethodOrFieldRest() throws RecognitionException {
    // Check whether we need to execute this rule.
    InterfaceMethodOrFieldRestContext guardResult = (InterfaceMethodOrFieldRestContext) guardRule((IncrementalParserRuleContext) _ctx, getState(), RULE_interfaceMethodOrFieldRest);
    // If we found an existing context that is valid, return it.
    if (guardResult != null) {
      this._input.seek(guardResult.stop.getTokenIndex() + 1);
      return guardResult;
    }
    InterfaceMethodOrFieldRestContext _localctx = new InterfaceMethodOrFieldRestContext(_ctx, getState());
    enterRule(_localctx, 66, RULE_interfaceMethodOrFieldRest);
    try {
      setState(520);
      _errHandler.sync(this);
      switch (_input.LA(1)) {
        case LBRACK:
        case ASSIGN:
          enterOuterAlt(_localctx, 1);
        {
          setState(516);
          constantDeclaratorsRest();
          setState(517);
          match(SEMI);
        }
        break;
        case LPAREN:
          enterOuterAlt(_localctx, 2);
        {
          setState(519);
          interfaceMethodDeclaratorRest();
        }
        break;
        default:
          throw new NoViableAltException(this);
      }
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
  }

  public static class MethodDeclaratorRestContext extends IncrementalParserRuleContext {
    public FormalParametersContext formalParameters() {
      return getRuleContext(FormalParametersContext.class, 0);
    }

    public MethodBodyContext methodBody() {
      return getRuleContext(MethodBodyContext.class, 0);
    }

    public TerminalNode SEMI() {
      return getToken(TestIncrementalJavaParser.SEMI, 0);
    }

    public List<? extends TerminalNode> LBRACK() {
      return getTokens(TestIncrementalJavaParser.LBRACK);
    }

    public TerminalNode LBRACK(int i) {
      return getToken(TestIncrementalJavaParser.LBRACK, i);
    }

    public List<? extends TerminalNode> RBRACK() {
      return getTokens(TestIncrementalJavaParser.RBRACK);
    }

    public TerminalNode RBRACK(int i) {
      return getToken(TestIncrementalJavaParser.RBRACK, i);
    }

    public TerminalNode THROWS() {
      return getToken(TestIncrementalJavaParser.THROWS, 0);
    }

    public QualifiedNameListContext qualifiedNameList() {
      return getRuleContext(QualifiedNameListContext.class, 0);
    }

    public MethodDeclaratorRestContext(ParserRuleContext parent, int invokingState) {
      super((IncrementalParserRuleContext) parent, invokingState);
    }

    @Override
    public int getRuleIndex() {
      return RULE_methodDeclaratorRest;
    }
  }

  @RuleVersion(0)
  public final MethodDeclaratorRestContext methodDeclaratorRest() throws RecognitionException {
    // Check whether we need to execute this rule.
    MethodDeclaratorRestContext guardResult = (MethodDeclaratorRestContext) guardRule((IncrementalParserRuleContext) _ctx, getState(), RULE_methodDeclaratorRest);
    // If we found an existing context that is valid, return it.
    if (guardResult != null) {
      this._input.seek(guardResult.stop.getTokenIndex() + 1);
      return guardResult;
    }
    MethodDeclaratorRestContext _localctx = new MethodDeclaratorRestContext(_ctx, getState());
    enterRule(_localctx, 68, RULE_methodDeclaratorRest);
    int _la;
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(522);
        formalParameters();
        setState(527);
        _errHandler.sync(this);
        _la = _input.LA(1);
        while (_la == LBRACK) {
          {
            {
              setState(523);
              match(LBRACK);
              setState(524);
              match(RBRACK);
            }
          }
          setState(529);
          _errHandler.sync(this);
          _la = _input.LA(1);
        }
        setState(532);
        _errHandler.sync(this);
        _la = _input.LA(1);
        if (_la == THROWS) {
          {
            setState(530);
            match(THROWS);
            setState(531);
            qualifiedNameList();
          }
        }

        setState(536);
        _errHandler.sync(this);
        switch (_input.LA(1)) {
          case LBRACE: {
            setState(534);
            methodBody();
          }
          break;
          case SEMI: {
            setState(535);
            match(SEMI);
          }
          break;
          default:
            throw new NoViableAltException(this);
        }
      }
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
  }

  public static class VoidMethodDeclaratorRestContext extends IncrementalParserRuleContext {
    public FormalParametersContext formalParameters() {
      return getRuleContext(FormalParametersContext.class, 0);
    }

    public MethodBodyContext methodBody() {
      return getRuleContext(MethodBodyContext.class, 0);
    }

    public TerminalNode SEMI() {
      return getToken(TestIncrementalJavaParser.SEMI, 0);
    }

    public TerminalNode THROWS() {
      return getToken(TestIncrementalJavaParser.THROWS, 0);
    }

    public QualifiedNameListContext qualifiedNameList() {
      return getRuleContext(QualifiedNameListContext.class, 0);
    }

    public VoidMethodDeclaratorRestContext(ParserRuleContext parent, int invokingState) {
      super((IncrementalParserRuleContext) parent, invokingState);
    }

    @Override
    public int getRuleIndex() {
      return RULE_voidMethodDeclaratorRest;
    }
  }

  @RuleVersion(0)
  public final VoidMethodDeclaratorRestContext voidMethodDeclaratorRest() throws RecognitionException {
    // Check whether we need to execute this rule.
    VoidMethodDeclaratorRestContext guardResult = (VoidMethodDeclaratorRestContext) guardRule((IncrementalParserRuleContext) _ctx, getState(), RULE_voidMethodDeclaratorRest);
    // If we found an existing context that is valid, return it.
    if (guardResult != null) {
      this._input.seek(guardResult.stop.getTokenIndex() + 1);
      return guardResult;
    }
    VoidMethodDeclaratorRestContext _localctx = new VoidMethodDeclaratorRestContext(_ctx, getState());
    enterRule(_localctx, 70, RULE_voidMethodDeclaratorRest);
    int _la;
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(538);
        formalParameters();
        setState(541);
        _errHandler.sync(this);
        _la = _input.LA(1);
        if (_la == THROWS) {
          {
            setState(539);
            match(THROWS);
            setState(540);
            qualifiedNameList();
          }
        }

        setState(545);
        _errHandler.sync(this);
        switch (_input.LA(1)) {
          case LBRACE: {
            setState(543);
            methodBody();
          }
          break;
          case SEMI: {
            setState(544);
            match(SEMI);
          }
          break;
          default:
            throw new NoViableAltException(this);
        }
      }
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
  }

  public static class InterfaceMethodDeclaratorRestContext extends IncrementalParserRuleContext {
    public FormalParametersContext formalParameters() {
      return getRuleContext(FormalParametersContext.class, 0);
    }

    public TerminalNode SEMI() {
      return getToken(TestIncrementalJavaParser.SEMI, 0);
    }

    public List<? extends TerminalNode> LBRACK() {
      return getTokens(TestIncrementalJavaParser.LBRACK);
    }

    public TerminalNode LBRACK(int i) {
      return getToken(TestIncrementalJavaParser.LBRACK, i);
    }

    public List<? extends TerminalNode> RBRACK() {
      return getTokens(TestIncrementalJavaParser.RBRACK);
    }

    public TerminalNode RBRACK(int i) {
      return getToken(TestIncrementalJavaParser.RBRACK, i);
    }

    public TerminalNode THROWS() {
      return getToken(TestIncrementalJavaParser.THROWS, 0);
    }

    public QualifiedNameListContext qualifiedNameList() {
      return getRuleContext(QualifiedNameListContext.class, 0);
    }

    public InterfaceMethodDeclaratorRestContext(ParserRuleContext parent, int invokingState) {
      super((IncrementalParserRuleContext) parent, invokingState);
    }

    @Override
    public int getRuleIndex() {
      return RULE_interfaceMethodDeclaratorRest;
    }
  }

  @RuleVersion(0)
  public final InterfaceMethodDeclaratorRestContext interfaceMethodDeclaratorRest() throws RecognitionException {
    // Check whether we need to execute this rule.
    InterfaceMethodDeclaratorRestContext guardResult = (InterfaceMethodDeclaratorRestContext) guardRule((IncrementalParserRuleContext) _ctx, getState(), RULE_interfaceMethodDeclaratorRest);
    // If we found an existing context that is valid, return it.
    if (guardResult != null) {
      this._input.seek(guardResult.stop.getTokenIndex() + 1);
      return guardResult;
    }
    InterfaceMethodDeclaratorRestContext _localctx = new InterfaceMethodDeclaratorRestContext(_ctx, getState());
    enterRule(_localctx, 72, RULE_interfaceMethodDeclaratorRest);
    int _la;
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(547);
        formalParameters();
        setState(552);
        _errHandler.sync(this);
        _la = _input.LA(1);
        while (_la == LBRACK) {
          {
            {
              setState(548);
              match(LBRACK);
              setState(549);
              match(RBRACK);
            }
          }
          setState(554);
          _errHandler.sync(this);
          _la = _input.LA(1);
        }
        setState(557);
        _errHandler.sync(this);
        _la = _input.LA(1);
        if (_la == THROWS) {
          {
            setState(555);
            match(THROWS);
            setState(556);
            qualifiedNameList();
          }
        }

        setState(559);
        match(SEMI);
      }
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
  }

  public static class InterfaceGenericMethodDeclContext extends IncrementalParserRuleContext {
    public TypeParametersContext typeParameters() {
      return getRuleContext(TypeParametersContext.class, 0);
    }

    public TerminalNode Identifier() {
      return getToken(TestIncrementalJavaParser.Identifier, 0);
    }

    public InterfaceMethodDeclaratorRestContext interfaceMethodDeclaratorRest() {
      return getRuleContext(InterfaceMethodDeclaratorRestContext.class, 0);
    }

    public TypeContext type() {
      return getRuleContext(TypeContext.class, 0);
    }

    public TerminalNode VOID() {
      return getToken(TestIncrementalJavaParser.VOID, 0);
    }

    public InterfaceGenericMethodDeclContext(ParserRuleContext parent, int invokingState) {
      super((IncrementalParserRuleContext) parent, invokingState);
    }

    @Override
    public int getRuleIndex() {
      return RULE_interfaceGenericMethodDecl;
    }
  }

  @RuleVersion(0)
  public final InterfaceGenericMethodDeclContext interfaceGenericMethodDecl() throws RecognitionException {
    // Check whether we need to execute this rule.
    InterfaceGenericMethodDeclContext guardResult = (InterfaceGenericMethodDeclContext) guardRule((IncrementalParserRuleContext) _ctx, getState(), RULE_interfaceGenericMethodDecl);
    // If we found an existing context that is valid, return it.
    if (guardResult != null) {
      this._input.seek(guardResult.stop.getTokenIndex() + 1);
      return guardResult;
    }
    InterfaceGenericMethodDeclContext _localctx = new InterfaceGenericMethodDeclContext(_ctx, getState());
    enterRule(_localctx, 74, RULE_interfaceGenericMethodDecl);
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(561);
        typeParameters();
        setState(564);
        _errHandler.sync(this);
        switch (_input.LA(1)) {
          case BOOLEAN:
          case BYTE:
          case CHAR:
          case DOUBLE:
          case FLOAT:
          case INT:
          case LONG:
          case SHORT:
          case Identifier: {
            setState(562);
            type();
          }
          break;
          case VOID: {
            setState(563);
            match(VOID);
          }
          break;
          default:
            throw new NoViableAltException(this);
        }
        setState(566);
        match(Identifier);
        setState(567);
        interfaceMethodDeclaratorRest();
      }
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
  }

  public static class VoidInterfaceMethodDeclaratorRestContext extends IncrementalParserRuleContext {
    public FormalParametersContext formalParameters() {
      return getRuleContext(FormalParametersContext.class, 0);
    }

    public TerminalNode SEMI() {
      return getToken(TestIncrementalJavaParser.SEMI, 0);
    }

    public TerminalNode THROWS() {
      return getToken(TestIncrementalJavaParser.THROWS, 0);
    }

    public QualifiedNameListContext qualifiedNameList() {
      return getRuleContext(QualifiedNameListContext.class, 0);
    }

    public VoidInterfaceMethodDeclaratorRestContext(ParserRuleContext parent, int invokingState) {
      super((IncrementalParserRuleContext) parent, invokingState);
    }

    @Override
    public int getRuleIndex() {
      return RULE_voidInterfaceMethodDeclaratorRest;
    }
  }

  @RuleVersion(0)
  public final VoidInterfaceMethodDeclaratorRestContext voidInterfaceMethodDeclaratorRest() throws RecognitionException {
    // Check whether we need to execute this rule.
    VoidInterfaceMethodDeclaratorRestContext guardResult = (VoidInterfaceMethodDeclaratorRestContext) guardRule((IncrementalParserRuleContext) _ctx, getState(), RULE_voidInterfaceMethodDeclaratorRest);
    // If we found an existing context that is valid, return it.
    if (guardResult != null) {
      this._input.seek(guardResult.stop.getTokenIndex() + 1);
      return guardResult;
    }
    VoidInterfaceMethodDeclaratorRestContext _localctx = new VoidInterfaceMethodDeclaratorRestContext(_ctx, getState());
    enterRule(_localctx, 76, RULE_voidInterfaceMethodDeclaratorRest);
    int _la;
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(569);
        formalParameters();
        setState(572);
        _errHandler.sync(this);
        _la = _input.LA(1);
        if (_la == THROWS) {
          {
            setState(570);
            match(THROWS);
            setState(571);
            qualifiedNameList();
          }
        }

        setState(574);
        match(SEMI);
      }
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
  }

  public static class ConstructorDeclaratorRestContext extends IncrementalParserRuleContext {
    public FormalParametersContext formalParameters() {
      return getRuleContext(FormalParametersContext.class, 0);
    }

    public ConstructorBodyContext constructorBody() {
      return getRuleContext(ConstructorBodyContext.class, 0);
    }

    public TerminalNode THROWS() {
      return getToken(TestIncrementalJavaParser.THROWS, 0);
    }

    public QualifiedNameListContext qualifiedNameList() {
      return getRuleContext(QualifiedNameListContext.class, 0);
    }

    public ConstructorDeclaratorRestContext(ParserRuleContext parent, int invokingState) {
      super((IncrementalParserRuleContext) parent, invokingState);
    }

    @Override
    public int getRuleIndex() {
      return RULE_constructorDeclaratorRest;
    }
  }

  @RuleVersion(0)
  public final ConstructorDeclaratorRestContext constructorDeclaratorRest() throws RecognitionException {
    // Check whether we need to execute this rule.
    ConstructorDeclaratorRestContext guardResult = (ConstructorDeclaratorRestContext) guardRule((IncrementalParserRuleContext) _ctx, getState(), RULE_constructorDeclaratorRest);
    // If we found an existing context that is valid, return it.
    if (guardResult != null) {
      this._input.seek(guardResult.stop.getTokenIndex() + 1);
      return guardResult;
    }
    ConstructorDeclaratorRestContext _localctx = new ConstructorDeclaratorRestContext(_ctx, getState());
    enterRule(_localctx, 78, RULE_constructorDeclaratorRest);
    int _la;
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(576);
        formalParameters();
        setState(579);
        _errHandler.sync(this);
        _la = _input.LA(1);
        if (_la == THROWS) {
          {
            setState(577);
            match(THROWS);
            setState(578);
            qualifiedNameList();
          }
        }

        setState(581);
        constructorBody();
      }
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
  }

  public static class ConstantDeclaratorContext extends IncrementalParserRuleContext {
    public TerminalNode Identifier() {
      return getToken(TestIncrementalJavaParser.Identifier, 0);
    }

    public ConstantDeclaratorRestContext constantDeclaratorRest() {
      return getRuleContext(ConstantDeclaratorRestContext.class, 0);
    }

    public ConstantDeclaratorContext(ParserRuleContext parent, int invokingState) {
      super((IncrementalParserRuleContext) parent, invokingState);
    }

    @Override
    public int getRuleIndex() {
      return RULE_constantDeclarator;
    }
  }

  @RuleVersion(0)
  public final ConstantDeclaratorContext constantDeclarator() throws RecognitionException {
    // Check whether we need to execute this rule.
    ConstantDeclaratorContext guardResult = (ConstantDeclaratorContext) guardRule((IncrementalParserRuleContext) _ctx, getState(), RULE_constantDeclarator);
    // If we found an existing context that is valid, return it.
    if (guardResult != null) {
      this._input.seek(guardResult.stop.getTokenIndex() + 1);
      return guardResult;
    }
    ConstantDeclaratorContext _localctx = new ConstantDeclaratorContext(_ctx, getState());
    enterRule(_localctx, 80, RULE_constantDeclarator);
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(583);
        match(Identifier);
        setState(584);
        constantDeclaratorRest();
      }
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
  }

  public static class VariableDeclaratorsContext extends IncrementalParserRuleContext {
    public List<? extends VariableDeclaratorContext> variableDeclarator() {
      return getRuleContexts(VariableDeclaratorContext.class);
    }

    public VariableDeclaratorContext variableDeclarator(int i) {
      return getRuleContext(VariableDeclaratorContext.class, i);
    }

    public List<? extends TerminalNode> COMMA() {
      return getTokens(TestIncrementalJavaParser.COMMA);
    }

    public TerminalNode COMMA(int i) {
      return getToken(TestIncrementalJavaParser.COMMA, i);
    }

    public VariableDeclaratorsContext(ParserRuleContext parent, int invokingState) {
      super((IncrementalParserRuleContext) parent, invokingState);
    }

    @Override
    public int getRuleIndex() {
      return RULE_variableDeclarators;
    }
  }

  @RuleVersion(0)
  public final VariableDeclaratorsContext variableDeclarators() throws RecognitionException {
    // Check whether we need to execute this rule.
    VariableDeclaratorsContext guardResult = (VariableDeclaratorsContext) guardRule((IncrementalParserRuleContext) _ctx, getState(), RULE_variableDeclarators);
    // If we found an existing context that is valid, return it.
    if (guardResult != null) {
      this._input.seek(guardResult.stop.getTokenIndex() + 1);
      return guardResult;
    }
    VariableDeclaratorsContext _localctx = new VariableDeclaratorsContext(_ctx, getState());
    enterRule(_localctx, 82, RULE_variableDeclarators);
    int _la;
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(586);
        variableDeclarator();
        setState(591);
        _errHandler.sync(this);
        _la = _input.LA(1);
        while (_la == COMMA) {
          {
            {
              setState(587);
              match(COMMA);
              setState(588);
              variableDeclarator();
            }
          }
          setState(593);
          _errHandler.sync(this);
          _la = _input.LA(1);
        }
      }
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
  }

  public static class VariableDeclaratorContext extends IncrementalParserRuleContext {
    public VariableDeclaratorIdContext variableDeclaratorId() {
      return getRuleContext(VariableDeclaratorIdContext.class, 0);
    }

    public TerminalNode ASSIGN() {
      return getToken(TestIncrementalJavaParser.ASSIGN, 0);
    }

    public VariableInitializerContext variableInitializer() {
      return getRuleContext(VariableInitializerContext.class, 0);
    }

    public VariableDeclaratorContext(ParserRuleContext parent, int invokingState) {
      super((IncrementalParserRuleContext) parent, invokingState);
    }

    @Override
    public int getRuleIndex() {
      return RULE_variableDeclarator;
    }
  }

  @RuleVersion(0)
  public final VariableDeclaratorContext variableDeclarator() throws RecognitionException {
    // Check whether we need to execute this rule.
    VariableDeclaratorContext guardResult = (VariableDeclaratorContext) guardRule((IncrementalParserRuleContext) _ctx, getState(), RULE_variableDeclarator);
    // If we found an existing context that is valid, return it.
    if (guardResult != null) {
      this._input.seek(guardResult.stop.getTokenIndex() + 1);
      return guardResult;
    }
    VariableDeclaratorContext _localctx = new VariableDeclaratorContext(_ctx, getState());
    enterRule(_localctx, 84, RULE_variableDeclarator);
    int _la;
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(594);
        variableDeclaratorId();
        setState(597);
        _errHandler.sync(this);
        _la = _input.LA(1);
        if (_la == ASSIGN) {
          {
            setState(595);
            match(ASSIGN);
            setState(596);
            variableInitializer();
          }
        }

      }
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
  }

  public static class ConstantDeclaratorsRestContext extends IncrementalParserRuleContext {
    public ConstantDeclaratorRestContext constantDeclaratorRest() {
      return getRuleContext(ConstantDeclaratorRestContext.class, 0);
    }

    public List<? extends TerminalNode> COMMA() {
      return getTokens(TestIncrementalJavaParser.COMMA);
    }

    public TerminalNode COMMA(int i) {
      return getToken(TestIncrementalJavaParser.COMMA, i);
    }

    public List<? extends ConstantDeclaratorContext> constantDeclarator() {
      return getRuleContexts(ConstantDeclaratorContext.class);
    }

    public ConstantDeclaratorContext constantDeclarator(int i) {
      return getRuleContext(ConstantDeclaratorContext.class, i);
    }

    public ConstantDeclaratorsRestContext(ParserRuleContext parent, int invokingState) {
      super((IncrementalParserRuleContext) parent, invokingState);
    }

    @Override
    public int getRuleIndex() {
      return RULE_constantDeclaratorsRest;
    }
  }

  @RuleVersion(0)
  public final ConstantDeclaratorsRestContext constantDeclaratorsRest() throws RecognitionException {
    // Check whether we need to execute this rule.
    ConstantDeclaratorsRestContext guardResult = (ConstantDeclaratorsRestContext) guardRule((IncrementalParserRuleContext) _ctx, getState(), RULE_constantDeclaratorsRest);
    // If we found an existing context that is valid, return it.
    if (guardResult != null) {
      this._input.seek(guardResult.stop.getTokenIndex() + 1);
      return guardResult;
    }
    ConstantDeclaratorsRestContext _localctx = new ConstantDeclaratorsRestContext(_ctx, getState());
    enterRule(_localctx, 86, RULE_constantDeclaratorsRest);
    int _la;
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(599);
        constantDeclaratorRest();
        setState(604);
        _errHandler.sync(this);
        _la = _input.LA(1);
        while (_la == COMMA) {
          {
            {
              setState(600);
              match(COMMA);
              setState(601);
              constantDeclarator();
            }
          }
          setState(606);
          _errHandler.sync(this);
          _la = _input.LA(1);
        }
      }
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
  }

  public static class ConstantDeclaratorRestContext extends IncrementalParserRuleContext {
    public TerminalNode ASSIGN() {
      return getToken(TestIncrementalJavaParser.ASSIGN, 0);
    }

    public VariableInitializerContext variableInitializer() {
      return getRuleContext(VariableInitializerContext.class, 0);
    }

    public List<? extends TerminalNode> LBRACK() {
      return getTokens(TestIncrementalJavaParser.LBRACK);
    }

    public TerminalNode LBRACK(int i) {
      return getToken(TestIncrementalJavaParser.LBRACK, i);
    }

    public List<? extends TerminalNode> RBRACK() {
      return getTokens(TestIncrementalJavaParser.RBRACK);
    }

    public TerminalNode RBRACK(int i) {
      return getToken(TestIncrementalJavaParser.RBRACK, i);
    }

    public ConstantDeclaratorRestContext(ParserRuleContext parent, int invokingState) {
      super((IncrementalParserRuleContext) parent, invokingState);
    }

    @Override
    public int getRuleIndex() {
      return RULE_constantDeclaratorRest;
    }
  }

  @RuleVersion(0)
  public final ConstantDeclaratorRestContext constantDeclaratorRest() throws RecognitionException {
    // Check whether we need to execute this rule.
    ConstantDeclaratorRestContext guardResult = (ConstantDeclaratorRestContext) guardRule((IncrementalParserRuleContext) _ctx, getState(), RULE_constantDeclaratorRest);
    // If we found an existing context that is valid, return it.
    if (guardResult != null) {
      this._input.seek(guardResult.stop.getTokenIndex() + 1);
      return guardResult;
    }
    ConstantDeclaratorRestContext _localctx = new ConstantDeclaratorRestContext(_ctx, getState());
    enterRule(_localctx, 88, RULE_constantDeclaratorRest);
    int _la;
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(611);
        _errHandler.sync(this);
        _la = _input.LA(1);
        while (_la == LBRACK) {
          {
            {
              setState(607);
              match(LBRACK);
              setState(608);
              match(RBRACK);
            }
          }
          setState(613);
          _errHandler.sync(this);
          _la = _input.LA(1);
        }
        setState(614);
        match(ASSIGN);
        setState(615);
        variableInitializer();
      }
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
  }

  public static class VariableDeclaratorIdContext extends IncrementalParserRuleContext {
    public TerminalNode Identifier() {
      return getToken(TestIncrementalJavaParser.Identifier, 0);
    }

    public List<? extends TerminalNode> LBRACK() {
      return getTokens(TestIncrementalJavaParser.LBRACK);
    }

    public TerminalNode LBRACK(int i) {
      return getToken(TestIncrementalJavaParser.LBRACK, i);
    }

    public List<? extends TerminalNode> RBRACK() {
      return getTokens(TestIncrementalJavaParser.RBRACK);
    }

    public TerminalNode RBRACK(int i) {
      return getToken(TestIncrementalJavaParser.RBRACK, i);
    }

    public VariableDeclaratorIdContext(ParserRuleContext parent, int invokingState) {
      super((IncrementalParserRuleContext) parent, invokingState);
    }

    @Override
    public int getRuleIndex() {
      return RULE_variableDeclaratorId;
    }
  }

  @RuleVersion(0)
  public final VariableDeclaratorIdContext variableDeclaratorId() throws RecognitionException {
    // Check whether we need to execute this rule.
    VariableDeclaratorIdContext guardResult = (VariableDeclaratorIdContext) guardRule((IncrementalParserRuleContext) _ctx, getState(), RULE_variableDeclaratorId);
    // If we found an existing context that is valid, return it.
    if (guardResult != null) {
      this._input.seek(guardResult.stop.getTokenIndex() + 1);
      return guardResult;
    }
    VariableDeclaratorIdContext _localctx = new VariableDeclaratorIdContext(_ctx, getState());
    enterRule(_localctx, 90, RULE_variableDeclaratorId);
    int _la;
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(617);
        match(Identifier);
        setState(622);
        _errHandler.sync(this);
        _la = _input.LA(1);
        while (_la == LBRACK) {
          {
            {
              setState(618);
              match(LBRACK);
              setState(619);
              match(RBRACK);
            }
          }
          setState(624);
          _errHandler.sync(this);
          _la = _input.LA(1);
        }
      }
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
  }

  public static class VariableInitializerContext extends IncrementalParserRuleContext {
    public ArrayInitializerContext arrayInitializer() {
      return getRuleContext(ArrayInitializerContext.class, 0);
    }

    public ExpressionContext expression() {
      return getRuleContext(ExpressionContext.class, 0);
    }

    public VariableInitializerContext(ParserRuleContext parent, int invokingState) {
      super((IncrementalParserRuleContext) parent, invokingState);
    }

    @Override
    public int getRuleIndex() {
      return RULE_variableInitializer;
    }
  }

  @RuleVersion(0)
  public final VariableInitializerContext variableInitializer() throws RecognitionException {
    // Check whether we need to execute this rule.
    VariableInitializerContext guardResult = (VariableInitializerContext) guardRule((IncrementalParserRuleContext) _ctx, getState(), RULE_variableInitializer);
    // If we found an existing context that is valid, return it.
    if (guardResult != null) {
      this._input.seek(guardResult.stop.getTokenIndex() + 1);
      return guardResult;
    }
    VariableInitializerContext _localctx = new VariableInitializerContext(_ctx, getState());
    enterRule(_localctx, 92, RULE_variableInitializer);
    try {
      setState(627);
      _errHandler.sync(this);
      switch (_input.LA(1)) {
        case LBRACE:
          enterOuterAlt(_localctx, 1);
        {
          setState(625);
          arrayInitializer();
        }
        break;
        case BOOLEAN:
        case BYTE:
        case CHAR:
        case DOUBLE:
        case FLOAT:
        case INT:
        case LONG:
        case NEW:
        case SHORT:
        case SUPER:
        case THIS:
        case VOID:
        case IntegerLiteral:
        case FloatingPointLiteral:
        case BooleanLiteral:
        case CharacterLiteral:
        case StringLiteral:
        case NullLiteral:
        case LPAREN:
        case LT:
        case BANG:
        case TILDE:
        case INC:
        case DEC:
        case ADD:
        case SUB:
        case Identifier:
          enterOuterAlt(_localctx, 2);
        {
          setState(626);
          expression(0);
        }
        break;
        default:
          throw new NoViableAltException(this);
      }
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
  }

  public static class ArrayInitializerContext extends IncrementalParserRuleContext {
    public TerminalNode LBRACE() {
      return getToken(TestIncrementalJavaParser.LBRACE, 0);
    }

    public TerminalNode RBRACE() {
      return getToken(TestIncrementalJavaParser.RBRACE, 0);
    }

    public List<? extends VariableInitializerContext> variableInitializer() {
      return getRuleContexts(VariableInitializerContext.class);
    }

    public VariableInitializerContext variableInitializer(int i) {
      return getRuleContext(VariableInitializerContext.class, i);
    }

    public List<? extends TerminalNode> COMMA() {
      return getTokens(TestIncrementalJavaParser.COMMA);
    }

    public TerminalNode COMMA(int i) {
      return getToken(TestIncrementalJavaParser.COMMA, i);
    }

    public ArrayInitializerContext(ParserRuleContext parent, int invokingState) {
      super((IncrementalParserRuleContext) parent, invokingState);
    }

    @Override
    public int getRuleIndex() {
      return RULE_arrayInitializer;
    }
  }

  @RuleVersion(0)
  public final ArrayInitializerContext arrayInitializer() throws RecognitionException {
    // Check whether we need to execute this rule.
    ArrayInitializerContext guardResult = (ArrayInitializerContext) guardRule((IncrementalParserRuleContext) _ctx, getState(), RULE_arrayInitializer);
    // If we found an existing context that is valid, return it.
    if (guardResult != null) {
      this._input.seek(guardResult.stop.getTokenIndex() + 1);
      return guardResult;
    }
    ArrayInitializerContext _localctx = new ArrayInitializerContext(_ctx, getState());
    enterRule(_localctx, 94, RULE_arrayInitializer);
    int _la;
    try {
      int _alt;
      enterOuterAlt(_localctx, 1);
      {
        setState(629);
        match(LBRACE);
        setState(641);
        _errHandler.sync(this);
        _la = _input.LA(1);
        if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << BOOLEAN) | (1L << BYTE) | (1L << CHAR) | (1L << DOUBLE) | (1L << FLOAT) | (1L << INT) | (1L << LONG) | (1L << NEW) | (1L << SHORT) | (1L << SUPER) | (1L << THIS) | (1L << VOID) | (1L << IntegerLiteral) | (1L << FloatingPointLiteral) | (1L << BooleanLiteral) | (1L << CharacterLiteral) | (1L << StringLiteral) | (1L << NullLiteral) | (1L << LPAREN) | (1L << LBRACE))) != 0) || ((((_la - 68)) & ~0x3f) == 0 && ((1L << (_la - 68)) & ((1L << (LT - 68)) | (1L << (BANG - 68)) | (1L << (TILDE - 68)) | (1L << (INC - 68)) | (1L << (DEC - 68)) | (1L << (ADD - 68)) | (1L << (SUB - 68)) | (1L << (Identifier - 68)))) != 0)) {
          {
            setState(630);
            variableInitializer();
            setState(635);
            _errHandler.sync(this);
            _alt = getInterpreter().adaptivePredict(_input, 62, _ctx);
            while (_alt != 2 && _alt != ATN.INVALID_ALT_NUMBER) {
              if (_alt == 1) {
                {
                  {
                    setState(631);
                    match(COMMA);
                    setState(632);
                    variableInitializer();
                  }
                }
              }
              setState(637);
              _errHandler.sync(this);
              _alt = getInterpreter().adaptivePredict(_input, 62, _ctx);
            }
            setState(639);
            _errHandler.sync(this);
            _la = _input.LA(1);
            if (_la == COMMA) {
              {
                setState(638);
                match(COMMA);
              }
            }

          }
        }

        setState(643);
        match(RBRACE);
      }
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
  }

  public static class ModifierContext extends IncrementalParserRuleContext {
    public AnnotationContext annotation() {
      return getRuleContext(AnnotationContext.class, 0);
    }

    public TerminalNode PUBLIC() {
      return getToken(TestIncrementalJavaParser.PUBLIC, 0);
    }

    public TerminalNode PROTECTED() {
      return getToken(TestIncrementalJavaParser.PROTECTED, 0);
    }

    public TerminalNode PRIVATE() {
      return getToken(TestIncrementalJavaParser.PRIVATE, 0);
    }

    public TerminalNode STATIC() {
      return getToken(TestIncrementalJavaParser.STATIC, 0);
    }

    public TerminalNode ABSTRACT() {
      return getToken(TestIncrementalJavaParser.ABSTRACT, 0);
    }

    public TerminalNode FINAL() {
      return getToken(TestIncrementalJavaParser.FINAL, 0);
    }

    public TerminalNode NATIVE() {
      return getToken(TestIncrementalJavaParser.NATIVE, 0);
    }

    public TerminalNode SYNCHRONIZED() {
      return getToken(TestIncrementalJavaParser.SYNCHRONIZED, 0);
    }

    public TerminalNode TRANSIENT() {
      return getToken(TestIncrementalJavaParser.TRANSIENT, 0);
    }

    public TerminalNode VOLATILE() {
      return getToken(TestIncrementalJavaParser.VOLATILE, 0);
    }

    public TerminalNode STRICTFP() {
      return getToken(TestIncrementalJavaParser.STRICTFP, 0);
    }

    public ModifierContext(ParserRuleContext parent, int invokingState) {
      super((IncrementalParserRuleContext) parent, invokingState);
    }

    @Override
    public int getRuleIndex() {
      return RULE_modifier;
    }

  }

  @RuleVersion(0)
  public final ModifierContext modifier() throws RecognitionException {
    // Check whether we need to execute this rule.
    ModifierContext guardResult = (ModifierContext) guardRule((IncrementalParserRuleContext) _ctx, getState(), RULE_modifier);
    // If we found an existing context that is valid, return it.
    if (guardResult != null) {
      this._input.seek(guardResult.stop.getTokenIndex() + 1);
      return guardResult;
    }
    ModifierContext _localctx = new ModifierContext(_ctx, getState());
    enterRule(_localctx, 96, RULE_modifier);
    int _la;
    try {
      setState(647);
      _errHandler.sync(this);
      switch (_input.LA(1)) {
        case AT:
          enterOuterAlt(_localctx, 1);
        {
          setState(645);
          annotation();
        }
        break;
        case ABSTRACT:
        case FINAL:
        case NATIVE:
        case PRIVATE:
        case PROTECTED:
        case PUBLIC:
        case STATIC:
        case STRICTFP:
        case SYNCHRONIZED:
        case TRANSIENT:
        case VOLATILE:
          enterOuterAlt(_localctx, 2);
        {
          setState(646);
          _la = _input.LA(1);
          if (!((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << ABSTRACT) | (1L << FINAL) | (1L << NATIVE) | (1L << PRIVATE) | (1L << PROTECTED) | (1L << PUBLIC) | (1L << STATIC) | (1L << STRICTFP) | (1L << SYNCHRONIZED) | (1L << TRANSIENT) | (1L << VOLATILE))) != 0))) {
            _errHandler.recoverInline(this);
          } else {
            if (_input.LA(1) == Token.EOF) {
              matchedEOF = true;
            }

            _errHandler.reportMatch(this);
            consume();
          }
        }
        break;
        default:
          throw new NoViableAltException(this);
      }
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
  }

  public static class PackageOrTypeNameContext extends IncrementalParserRuleContext {
    public QualifiedNameContext qualifiedName() {
      return getRuleContext(QualifiedNameContext.class, 0);
    }

    public PackageOrTypeNameContext(ParserRuleContext parent, int invokingState) {
      super((IncrementalParserRuleContext) parent, invokingState);
    }

    @Override
    public int getRuleIndex() {
      return RULE_packageOrTypeName;
    }

  }

  @RuleVersion(0)
  public final PackageOrTypeNameContext packageOrTypeName() throws RecognitionException {
    // Check whether we need to execute this rule.
    PackageOrTypeNameContext guardResult = (PackageOrTypeNameContext) guardRule((IncrementalParserRuleContext) _ctx, getState(), RULE_packageOrTypeName);
    // If we found an existing context that is valid, return it.
    if (guardResult != null) {
      this._input.seek(guardResult.stop.getTokenIndex() + 1);
      return guardResult;
    }
    PackageOrTypeNameContext _localctx = new PackageOrTypeNameContext(_ctx, getState());
    enterRule(_localctx, 98, RULE_packageOrTypeName);
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(649);
        qualifiedName();
      }
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
  }

  public static class EnumConstantNameContext extends IncrementalParserRuleContext {
    public TerminalNode Identifier() {
      return getToken(TestIncrementalJavaParser.Identifier, 0);
    }

    public EnumConstantNameContext(ParserRuleContext parent, int invokingState) {
      super((IncrementalParserRuleContext) parent, invokingState);
    }

    @Override
    public int getRuleIndex() {
      return RULE_enumConstantName;
    }
  }

  @RuleVersion(0)
  public final EnumConstantNameContext enumConstantName() throws RecognitionException {
    // Check whether we need to execute this rule.
    EnumConstantNameContext guardResult = (EnumConstantNameContext) guardRule((IncrementalParserRuleContext) _ctx, getState(), RULE_enumConstantName);
    // If we found an existing context that is valid, return it.
    if (guardResult != null) {
      this._input.seek(guardResult.stop.getTokenIndex() + 1);
      return guardResult;
    }
    EnumConstantNameContext _localctx = new EnumConstantNameContext(_ctx, getState());
    enterRule(_localctx, 100, RULE_enumConstantName);
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(651);
        match(Identifier);
      }
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
  }

  public static class TypeNameContext extends IncrementalParserRuleContext {
    public QualifiedNameContext qualifiedName() {
      return getRuleContext(QualifiedNameContext.class, 0);
    }

    public TypeNameContext(ParserRuleContext parent, int invokingState) {
      super((IncrementalParserRuleContext) parent, invokingState);
    }

    @Override
    public int getRuleIndex() {
      return RULE_typeName;
    }
  }

  @RuleVersion(0)
  public final TypeNameContext typeName() throws RecognitionException {
    // Check whether we need to execute this rule.
    TypeNameContext guardResult = (TypeNameContext) guardRule((IncrementalParserRuleContext) _ctx, getState(), RULE_typeName);
    // If we found an existing context that is valid, return it.
    if (guardResult != null) {
      this._input.seek(guardResult.stop.getTokenIndex() + 1);
      return guardResult;
    }
    TypeNameContext _localctx = new TypeNameContext(_ctx, getState());
    enterRule(_localctx, 102, RULE_typeName);
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(653);
        qualifiedName();
      }
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
  }

  public static class TypeContext extends IncrementalParserRuleContext {
    public ClassOrInterfaceTypeContext classOrInterfaceType() {
      return getRuleContext(ClassOrInterfaceTypeContext.class, 0);
    }

    public List<? extends TerminalNode> LBRACK() {
      return getTokens(TestIncrementalJavaParser.LBRACK);
    }

    public TerminalNode LBRACK(int i) {
      return getToken(TestIncrementalJavaParser.LBRACK, i);
    }

    public List<? extends TerminalNode> RBRACK() {
      return getTokens(TestIncrementalJavaParser.RBRACK);
    }

    public TerminalNode RBRACK(int i) {
      return getToken(TestIncrementalJavaParser.RBRACK, i);
    }

    public PrimitiveTypeContext primitiveType() {
      return getRuleContext(PrimitiveTypeContext.class, 0);
    }

    public TypeContext(ParserRuleContext parent, int invokingState) {
      super((IncrementalParserRuleContext) parent, invokingState);
    }

    @Override
    public int getRuleIndex() {
      return RULE_type;
    }
  }

  @RuleVersion(0)
  public final TypeContext type() throws RecognitionException {
    // Check whether we need to execute this rule.
    TypeContext guardResult = (TypeContext) guardRule((IncrementalParserRuleContext) _ctx, getState(), RULE_type);
    // If we found an existing context that is valid, return it.
    if (guardResult != null) {
      this._input.seek(guardResult.stop.getTokenIndex() + 1);
      return guardResult;
    }
    TypeContext _localctx = new TypeContext(_ctx, getState());
    enterRule(_localctx, 104, RULE_type);
    try {
      int _alt;
      setState(671);
      _errHandler.sync(this);
      switch (_input.LA(1)) {
        case Identifier:
          enterOuterAlt(_localctx, 1);
        {
          setState(655);
          classOrInterfaceType();
          setState(660);
          _errHandler.sync(this);
          _alt = getInterpreter().adaptivePredict(_input, 66, _ctx);
          while (_alt != 2 && _alt != ATN.INVALID_ALT_NUMBER) {
            if (_alt == 1) {
              {
                {
                  setState(656);
                  match(LBRACK);
                  setState(657);
                  match(RBRACK);
                }
              }
            }
            setState(662);
            _errHandler.sync(this);
            _alt = getInterpreter().adaptivePredict(_input, 66, _ctx);
          }
        }
        break;
        case BOOLEAN:
        case BYTE:
        case CHAR:
        case DOUBLE:
        case FLOAT:
        case INT:
        case LONG:
        case SHORT:
          enterOuterAlt(_localctx, 2);
        {
          setState(663);
          primitiveType();
          setState(668);
          _errHandler.sync(this);
          _alt = getInterpreter().adaptivePredict(_input, 67, _ctx);
          while (_alt != 2 && _alt != ATN.INVALID_ALT_NUMBER) {
            if (_alt == 1) {
              {
                {
                  setState(664);
                  match(LBRACK);
                  setState(665);
                  match(RBRACK);
                }
              }
            }
            setState(670);
            _errHandler.sync(this);
            _alt = getInterpreter().adaptivePredict(_input, 67, _ctx);
          }
        }
        break;
        default:
          throw new NoViableAltException(this);
      }
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
  }

  public static class ClassOrInterfaceTypeContext extends IncrementalParserRuleContext {
    public List<? extends TerminalNode> Identifier() {
      return getTokens(TestIncrementalJavaParser.Identifier);
    }

    public TerminalNode Identifier(int i) {
      return getToken(TestIncrementalJavaParser.Identifier, i);
    }

    public List<? extends TypeArgumentsContext> typeArguments() {
      return getRuleContexts(TypeArgumentsContext.class);
    }

    public TypeArgumentsContext typeArguments(int i) {
      return getRuleContext(TypeArgumentsContext.class, i);
    }

    public List<? extends TerminalNode> DOT() {
      return getTokens(TestIncrementalJavaParser.DOT);
    }

    public TerminalNode DOT(int i) {
      return getToken(TestIncrementalJavaParser.DOT, i);
    }

    public ClassOrInterfaceTypeContext(ParserRuleContext parent, int invokingState) {
      super((IncrementalParserRuleContext) parent, invokingState);
    }

    @Override
    public int getRuleIndex() {
      return RULE_classOrInterfaceType;
    }
  }

  @RuleVersion(0)
  public final ClassOrInterfaceTypeContext classOrInterfaceType() throws RecognitionException {
    // Check whether we need to execute this rule.
    ClassOrInterfaceTypeContext guardResult = (ClassOrInterfaceTypeContext) guardRule((IncrementalParserRuleContext) _ctx, getState(), RULE_classOrInterfaceType);
    // If we found an existing context that is valid, return it.
    if (guardResult != null) {
      this._input.seek(guardResult.stop.getTokenIndex() + 1);
      return guardResult;
    }
    ClassOrInterfaceTypeContext _localctx = new ClassOrInterfaceTypeContext(_ctx, getState());
    enterRule(_localctx, 106, RULE_classOrInterfaceType);
    try {
      int _alt;
      enterOuterAlt(_localctx, 1);
      {
        setState(673);
        match(Identifier);
        setState(675);
        _errHandler.sync(this);
        switch (getInterpreter().adaptivePredict(_input, 69, _ctx)) {
          case 1: {
            setState(674);
            typeArguments();
          }
          break;
        }
        setState(684);
        _errHandler.sync(this);
        _alt = getInterpreter().adaptivePredict(_input, 71, _ctx);
        while (_alt != 2 && _alt != ATN.INVALID_ALT_NUMBER) {
          if (_alt == 1) {
            {
              {
                setState(677);
                match(DOT);
                setState(678);
                match(Identifier);
                setState(680);
                _errHandler.sync(this);
                switch (getInterpreter().adaptivePredict(_input, 70, _ctx)) {
                  case 1: {
                    setState(679);
                    typeArguments();
                  }
                  break;
                }
              }
            }
          }
          setState(686);
          _errHandler.sync(this);
          _alt = getInterpreter().adaptivePredict(_input, 71, _ctx);
        }
      }
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
  }

  public static class PrimitiveTypeContext extends IncrementalParserRuleContext {
    public TerminalNode BOOLEAN() {
      return getToken(TestIncrementalJavaParser.BOOLEAN, 0);
    }

    public TerminalNode CHAR() {
      return getToken(TestIncrementalJavaParser.CHAR, 0);
    }

    public TerminalNode BYTE() {
      return getToken(TestIncrementalJavaParser.BYTE, 0);
    }

    public TerminalNode SHORT() {
      return getToken(TestIncrementalJavaParser.SHORT, 0);
    }

    public TerminalNode INT() {
      return getToken(TestIncrementalJavaParser.INT, 0);
    }

    public TerminalNode LONG() {
      return getToken(TestIncrementalJavaParser.LONG, 0);
    }

    public TerminalNode FLOAT() {
      return getToken(TestIncrementalJavaParser.FLOAT, 0);
    }

    public TerminalNode DOUBLE() {
      return getToken(TestIncrementalJavaParser.DOUBLE, 0);
    }

    public PrimitiveTypeContext(ParserRuleContext parent, int invokingState) {
      super((IncrementalParserRuleContext) parent, invokingState);
    }

    @Override
    public int getRuleIndex() {
      return RULE_primitiveType;
    }
  }

  @RuleVersion(0)
  public final PrimitiveTypeContext primitiveType() throws RecognitionException {
    // Check whether we need to execute this rule.
    PrimitiveTypeContext guardResult = (PrimitiveTypeContext) guardRule((IncrementalParserRuleContext) _ctx, getState(), RULE_primitiveType);
    // If we found an existing context that is valid, return it.
    if (guardResult != null) {
      this._input.seek(guardResult.stop.getTokenIndex() + 1);
      return guardResult;
    }
    PrimitiveTypeContext _localctx = new PrimitiveTypeContext(_ctx, getState());
    enterRule(_localctx, 108, RULE_primitiveType);
    int _la;
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(687);
        _la = _input.LA(1);
        if (!((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << BOOLEAN) | (1L << BYTE) | (1L << CHAR) | (1L << DOUBLE) | (1L << FLOAT) | (1L << INT) | (1L << LONG) | (1L << SHORT))) != 0))) {
          _errHandler.recoverInline(this);
        } else {
          if (_input.LA(1) == Token.EOF) {
            matchedEOF = true;
          }

          _errHandler.reportMatch(this);
          consume();
        }
      }
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
  }

  public static class VariableModifierContext extends IncrementalParserRuleContext {
    public TerminalNode FINAL() {
      return getToken(TestIncrementalJavaParser.FINAL, 0);
    }

    public AnnotationContext annotation() {
      return getRuleContext(AnnotationContext.class, 0);
    }

    public VariableModifierContext(ParserRuleContext parent, int invokingState) {
      super((IncrementalParserRuleContext) parent, invokingState);
    }

    @Override
    public int getRuleIndex() {
      return RULE_variableModifier;
    }

  }

  @RuleVersion(0)
  public final VariableModifierContext variableModifier() throws RecognitionException {
    // Check whether we need to execute this rule.
    VariableModifierContext guardResult = (VariableModifierContext) guardRule((IncrementalParserRuleContext) _ctx, getState(), RULE_variableModifier);
    // If we found an existing context that is valid, return it.
    if (guardResult != null) {
      this._input.seek(guardResult.stop.getTokenIndex() + 1);
      return guardResult;
    }
    VariableModifierContext _localctx = new VariableModifierContext(_ctx, getState());
    enterRule(_localctx, 110, RULE_variableModifier);
    try {
      setState(691);
      _errHandler.sync(this);
      switch (_input.LA(1)) {
        case FINAL:
          enterOuterAlt(_localctx, 1);
        {
          setState(689);
          match(FINAL);
        }
        break;
        case AT:
          enterOuterAlt(_localctx, 2);
        {
          setState(690);
          annotation();
        }
        break;
        default:
          throw new NoViableAltException(this);
      }
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
  }

  public static class TypeArgumentsContext extends IncrementalParserRuleContext {
    public TerminalNode LT() {
      return getToken(TestIncrementalJavaParser.LT, 0);
    }

    public List<? extends TypeArgumentContext> typeArgument() {
      return getRuleContexts(TypeArgumentContext.class);
    }

    public TypeArgumentContext typeArgument(int i) {
      return getRuleContext(TypeArgumentContext.class, i);
    }

    public TerminalNode GT() {
      return getToken(TestIncrementalJavaParser.GT, 0);
    }

    public List<? extends TerminalNode> COMMA() {
      return getTokens(TestIncrementalJavaParser.COMMA);
    }

    public TerminalNode COMMA(int i) {
      return getToken(TestIncrementalJavaParser.COMMA, i);
    }

    public TypeArgumentsContext(ParserRuleContext parent, int invokingState) {
      super((IncrementalParserRuleContext) parent, invokingState);
    }

    @Override
    public int getRuleIndex() {
      return RULE_typeArguments;
    }
  }

  @RuleVersion(0)
  public final TypeArgumentsContext typeArguments() throws RecognitionException {
    // Check whether we need to execute this rule.
    TypeArgumentsContext guardResult = (TypeArgumentsContext) guardRule((IncrementalParserRuleContext) _ctx, getState(), RULE_typeArguments);
    // If we found an existing context that is valid, return it.
    if (guardResult != null) {
      this._input.seek(guardResult.stop.getTokenIndex() + 1);
      return guardResult;
    }
    TypeArgumentsContext _localctx = new TypeArgumentsContext(_ctx, getState());
    enterRule(_localctx, 112, RULE_typeArguments);
    int _la;
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(693);
        match(LT);
        setState(694);
        typeArgument();
        setState(699);
        _errHandler.sync(this);
        _la = _input.LA(1);
        while (_la == COMMA) {
          {
            {
              setState(695);
              match(COMMA);
              setState(696);
              typeArgument();
            }
          }
          setState(701);
          _errHandler.sync(this);
          _la = _input.LA(1);
        }
        setState(702);
        match(GT);
      }
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
  }

  public static class TypeArgumentContext extends IncrementalParserRuleContext {
    public TypeContext type() {
      return getRuleContext(TypeContext.class, 0);
    }

    public TerminalNode QUESTION() {
      return getToken(TestIncrementalJavaParser.QUESTION, 0);
    }

    public TerminalNode EXTENDS() {
      return getToken(TestIncrementalJavaParser.EXTENDS, 0);
    }

    public TerminalNode SUPER() {
      return getToken(TestIncrementalJavaParser.SUPER, 0);
    }

    public TypeArgumentContext(ParserRuleContext parent, int invokingState) {
      super((IncrementalParserRuleContext) parent, invokingState);
    }

    @Override
    public int getRuleIndex() {
      return RULE_typeArgument;
    }

  }

  @RuleVersion(0)
  public final TypeArgumentContext typeArgument() throws RecognitionException {
    // Check whether we need to execute this rule.
    TypeArgumentContext guardResult = (TypeArgumentContext) guardRule((IncrementalParserRuleContext) _ctx, getState(), RULE_typeArgument);
    // If we found an existing context that is valid, return it.
    if (guardResult != null) {
      this._input.seek(guardResult.stop.getTokenIndex() + 1);
      return guardResult;
    }
    TypeArgumentContext _localctx = new TypeArgumentContext(_ctx, getState());
    enterRule(_localctx, 114, RULE_typeArgument);
    int _la;
    try {
      setState(710);
      _errHandler.sync(this);
      switch (_input.LA(1)) {
        case BOOLEAN:
        case BYTE:
        case CHAR:
        case DOUBLE:
        case FLOAT:
        case INT:
        case LONG:
        case SHORT:
        case Identifier:
          enterOuterAlt(_localctx, 1);
        {
          setState(704);
          type();
        }
        break;
        case QUESTION:
          enterOuterAlt(_localctx, 2);
        {
          setState(705);
          match(QUESTION);
          setState(708);
          _errHandler.sync(this);
          _la = _input.LA(1);
          if (_la == EXTENDS || _la == SUPER) {
            {
              setState(706);
              _la = _input.LA(1);
              if (!(_la == EXTENDS || _la == SUPER)) {
                _errHandler.recoverInline(this);
              } else {
                if (_input.LA(1) == Token.EOF) {
                  matchedEOF = true;
                }

                _errHandler.reportMatch(this);
                consume();
              }
              setState(707);
              type();
            }
          }

        }
        break;
        default:
          throw new NoViableAltException(this);
      }
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
  }

  public static class QualifiedNameListContext extends IncrementalParserRuleContext {
    public List<? extends QualifiedNameContext> qualifiedName() {
      return getRuleContexts(QualifiedNameContext.class);
    }

    public QualifiedNameContext qualifiedName(int i) {
      return getRuleContext(QualifiedNameContext.class, i);
    }

    public List<? extends TerminalNode> COMMA() {
      return getTokens(TestIncrementalJavaParser.COMMA);
    }

    public TerminalNode COMMA(int i) {
      return getToken(TestIncrementalJavaParser.COMMA, i);
    }

    public QualifiedNameListContext(ParserRuleContext parent, int invokingState) {
      super((IncrementalParserRuleContext) parent, invokingState);
    }

    @Override
    public int getRuleIndex() {
      return RULE_qualifiedNameList;
    }
  }

  @RuleVersion(0)
  public final QualifiedNameListContext qualifiedNameList() throws RecognitionException {
    // Check whether we need to execute this rule.
    QualifiedNameListContext guardResult = (QualifiedNameListContext) guardRule((IncrementalParserRuleContext) _ctx, getState(), RULE_qualifiedNameList);
    // If we found an existing context that is valid, return it.
    if (guardResult != null) {
      this._input.seek(guardResult.stop.getTokenIndex() + 1);
      return guardResult;
    }
    QualifiedNameListContext _localctx = new QualifiedNameListContext(_ctx, getState());
    enterRule(_localctx, 116, RULE_qualifiedNameList);
    int _la;
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(712);
        qualifiedName();
        setState(717);
        _errHandler.sync(this);
        _la = _input.LA(1);
        while (_la == COMMA) {
          {
            {
              setState(713);
              match(COMMA);
              setState(714);
              qualifiedName();
            }
          }
          setState(719);
          _errHandler.sync(this);
          _la = _input.LA(1);
        }
      }
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
  }

  public static class FormalParametersContext extends IncrementalParserRuleContext {
    public TerminalNode LPAREN() {
      return getToken(TestIncrementalJavaParser.LPAREN, 0);
    }

    public TerminalNode RPAREN() {
      return getToken(TestIncrementalJavaParser.RPAREN, 0);
    }

    public FormalParameterDeclsContext formalParameterDecls() {
      return getRuleContext(FormalParameterDeclsContext.class, 0);
    }

    public FormalParametersContext(ParserRuleContext parent, int invokingState) {
      super((IncrementalParserRuleContext) parent, invokingState);
    }

    @Override
    public int getRuleIndex() {
      return RULE_formalParameters;
    }
  }

  @RuleVersion(0)
  public final FormalParametersContext formalParameters() throws RecognitionException {
    // Check whether we need to execute this rule.
    FormalParametersContext guardResult = (FormalParametersContext) guardRule((IncrementalParserRuleContext) _ctx, getState(), RULE_formalParameters);
    // If we found an existing context that is valid, return it.
    if (guardResult != null) {
      this._input.seek(guardResult.stop.getTokenIndex() + 1);
      return guardResult;
    }
    FormalParametersContext _localctx = new FormalParametersContext(_ctx, getState());
    enterRule(_localctx, 118, RULE_formalParameters);
    int _la;
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(720);
        match(LPAREN);
        setState(722);
        _errHandler.sync(this);
        _la = _input.LA(1);
        if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << BOOLEAN) | (1L << BYTE) | (1L << CHAR) | (1L << DOUBLE) | (1L << FINAL) | (1L << FLOAT) | (1L << INT) | (1L << LONG) | (1L << SHORT))) != 0) || _la == Identifier || _la == AT) {
          {
            setState(721);
            formalParameterDecls();
          }
        }

        setState(724);
        match(RPAREN);
      }
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
  }

  public static class FormalParameterDeclsContext extends IncrementalParserRuleContext {
    public VariableModifiersContext variableModifiers() {
      return getRuleContext(VariableModifiersContext.class, 0);
    }

    public TypeContext type() {
      return getRuleContext(TypeContext.class, 0);
    }

    public FormalParameterDeclsRestContext formalParameterDeclsRest() {
      return getRuleContext(FormalParameterDeclsRestContext.class, 0);
    }

    public FormalParameterDeclsContext(ParserRuleContext parent, int invokingState) {
      super((IncrementalParserRuleContext) parent, invokingState);
    }

    @Override
    public int getRuleIndex() {
      return RULE_formalParameterDecls;
    }
  }

  @RuleVersion(0)
  public final FormalParameterDeclsContext formalParameterDecls() throws RecognitionException {
    // Check whether we need to execute this rule.
    FormalParameterDeclsContext guardResult = (FormalParameterDeclsContext) guardRule((IncrementalParserRuleContext) _ctx, getState(), RULE_formalParameterDecls);
    // If we found an existing context that is valid, return it.
    if (guardResult != null) {
      this._input.seek(guardResult.stop.getTokenIndex() + 1);
      return guardResult;
    }
    FormalParameterDeclsContext _localctx = new FormalParameterDeclsContext(_ctx, getState());
    enterRule(_localctx, 120, RULE_formalParameterDecls);
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(726);
        variableModifiers();
        setState(727);
        type();
        setState(728);
        formalParameterDeclsRest();
      }
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
  }

  public static class FormalParameterDeclsRestContext extends IncrementalParserRuleContext {
    public VariableDeclaratorIdContext variableDeclaratorId() {
      return getRuleContext(VariableDeclaratorIdContext.class, 0);
    }

    public TerminalNode COMMA() {
      return getToken(TestIncrementalJavaParser.COMMA, 0);
    }

    public FormalParameterDeclsContext formalParameterDecls() {
      return getRuleContext(FormalParameterDeclsContext.class, 0);
    }

    public TerminalNode ELLIPSIS() {
      return getToken(TestIncrementalJavaParser.ELLIPSIS, 0);
    }

    public FormalParameterDeclsRestContext(ParserRuleContext parent, int invokingState) {
      super((IncrementalParserRuleContext) parent, invokingState);
    }

    @Override
    public int getRuleIndex() {
      return RULE_formalParameterDeclsRest;
    }
  }

  @RuleVersion(0)
  public final FormalParameterDeclsRestContext formalParameterDeclsRest() throws RecognitionException {
    // Check whether we need to execute this rule.
    FormalParameterDeclsRestContext guardResult = (FormalParameterDeclsRestContext) guardRule((IncrementalParserRuleContext) _ctx, getState(), RULE_formalParameterDeclsRest);
    // If we found an existing context that is valid, return it.
    if (guardResult != null) {
      this._input.seek(guardResult.stop.getTokenIndex() + 1);
      return guardResult;
    }
    FormalParameterDeclsRestContext _localctx = new FormalParameterDeclsRestContext(_ctx, getState());
    enterRule(_localctx, 122, RULE_formalParameterDeclsRest);
    int _la;
    try {
      setState(737);
      _errHandler.sync(this);
      switch (_input.LA(1)) {
        case Identifier:
          enterOuterAlt(_localctx, 1);
        {
          setState(730);
          variableDeclaratorId();
          setState(733);
          _errHandler.sync(this);
          _la = _input.LA(1);
          if (_la == COMMA) {
            {
              setState(731);
              match(COMMA);
              setState(732);
              formalParameterDecls();
            }
          }

        }
        break;
        case ELLIPSIS:
          enterOuterAlt(_localctx, 2);
        {
          setState(735);
          match(ELLIPSIS);
          setState(736);
          variableDeclaratorId();
        }
        break;
        default:
          throw new NoViableAltException(this);
      }
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
  }

  public static class MethodBodyContext extends IncrementalParserRuleContext {
    public BlockContext block() {
      return getRuleContext(BlockContext.class, 0);
    }

    public MethodBodyContext(ParserRuleContext parent, int invokingState) {
      super((IncrementalParserRuleContext) parent, invokingState);
    }

    @Override
    public int getRuleIndex() {
      return RULE_methodBody;
    }
  }

  @RuleVersion(0)
  public final MethodBodyContext methodBody() throws RecognitionException {
    // Check whether we need to execute this rule.
    MethodBodyContext guardResult = (MethodBodyContext) guardRule((IncrementalParserRuleContext) _ctx, getState(), RULE_methodBody);
    // If we found an existing context that is valid, return it.
    if (guardResult != null) {
      this._input.seek(guardResult.stop.getTokenIndex() + 1);
      return guardResult;
    }
    MethodBodyContext _localctx = new MethodBodyContext(_ctx, getState());
    enterRule(_localctx, 124, RULE_methodBody);
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(739);
        block();
      }
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
  }

  public static class ConstructorBodyContext extends IncrementalParserRuleContext {
    public BlockContext block() {
      return getRuleContext(BlockContext.class, 0);
    }

    public ConstructorBodyContext(ParserRuleContext parent, int invokingState) {
      super((IncrementalParserRuleContext) parent, invokingState);
    }

    @Override
    public int getRuleIndex() {
      return RULE_constructorBody;
    }
  }

  @RuleVersion(0)
  public final ConstructorBodyContext constructorBody() throws RecognitionException {
    // Check whether we need to execute this rule.
    ConstructorBodyContext guardResult = (ConstructorBodyContext) guardRule((IncrementalParserRuleContext) _ctx, getState(), RULE_constructorBody);
    // If we found an existing context that is valid, return it.
    if (guardResult != null) {
      this._input.seek(guardResult.stop.getTokenIndex() + 1);
      return guardResult;
    }
    ConstructorBodyContext _localctx = new ConstructorBodyContext(_ctx, getState());
    enterRule(_localctx, 126, RULE_constructorBody);
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(741);
        block();
      }
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
  }

  public static class QualifiedNameContext extends IncrementalParserRuleContext {
    public List<? extends TerminalNode> Identifier() {
      return getTokens(TestIncrementalJavaParser.Identifier);
    }

    public TerminalNode Identifier(int i) {
      return getToken(TestIncrementalJavaParser.Identifier, i);
    }

    public List<? extends TerminalNode> DOT() {
      return getTokens(TestIncrementalJavaParser.DOT);
    }

    public TerminalNode DOT(int i) {
      return getToken(TestIncrementalJavaParser.DOT, i);
    }

    public QualifiedNameContext(ParserRuleContext parent, int invokingState) {
      super((IncrementalParserRuleContext) parent, invokingState);
    }

    @Override
    public int getRuleIndex() {
      return RULE_qualifiedName;
    }
  }

  @RuleVersion(0)
  public final QualifiedNameContext qualifiedName() throws RecognitionException {
    // Check whether we need to execute this rule.
    QualifiedNameContext guardResult = (QualifiedNameContext) guardRule((IncrementalParserRuleContext) _ctx, getState(), RULE_qualifiedName);
    // If we found an existing context that is valid, return it.
    if (guardResult != null) {
      this._input.seek(guardResult.stop.getTokenIndex() + 1);
      return guardResult;
    }
    QualifiedNameContext _localctx = new QualifiedNameContext(_ctx, getState());
    enterRule(_localctx, 128, RULE_qualifiedName);
    try {
      int _alt;
      enterOuterAlt(_localctx, 1);
      {
        setState(743);
        match(Identifier);
        setState(748);
        _errHandler.sync(this);
        _alt = getInterpreter().adaptivePredict(_input, 80, _ctx);
        while (_alt != 2 && _alt != ATN.INVALID_ALT_NUMBER) {
          if (_alt == 1) {
            {
              {
                setState(744);
                match(DOT);
                setState(745);
                match(Identifier);
              }
            }
          }
          setState(750);
          _errHandler.sync(this);
          _alt = getInterpreter().adaptivePredict(_input, 80, _ctx);
        }
      }
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
  }

  public static class LiteralContext extends IncrementalParserRuleContext {
    public TerminalNode IntegerLiteral() {
      return getToken(TestIncrementalJavaParser.IntegerLiteral, 0);
    }

    public TerminalNode FloatingPointLiteral() {
      return getToken(TestIncrementalJavaParser.FloatingPointLiteral, 0);
    }

    public TerminalNode CharacterLiteral() {
      return getToken(TestIncrementalJavaParser.CharacterLiteral, 0);
    }

    public TerminalNode StringLiteral() {
      return getToken(TestIncrementalJavaParser.StringLiteral, 0);
    }

    public TerminalNode BooleanLiteral() {
      return getToken(TestIncrementalJavaParser.BooleanLiteral, 0);
    }

    public TerminalNode NullLiteral() {
      return getToken(TestIncrementalJavaParser.NullLiteral, 0);
    }

    public LiteralContext(ParserRuleContext parent, int invokingState) {
      super((IncrementalParserRuleContext) parent, invokingState);
    }

    @Override
    public int getRuleIndex() {
      return RULE_literal;
    }
  }

  @RuleVersion(0)
  public final LiteralContext literal() throws RecognitionException {
    // Check whether we need to execute this rule.
    LiteralContext guardResult = (LiteralContext) guardRule((IncrementalParserRuleContext) _ctx, getState(), RULE_literal);
    // If we found an existing context that is valid, return it.
    if (guardResult != null) {
      this._input.seek(guardResult.stop.getTokenIndex() + 1);
      return guardResult;
    }
    LiteralContext _localctx = new LiteralContext(_ctx, getState());
    enterRule(_localctx, 130, RULE_literal);
    int _la;
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(751);
        _la = _input.LA(1);
        if (!((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << IntegerLiteral) | (1L << FloatingPointLiteral) | (1L << BooleanLiteral) | (1L << CharacterLiteral) | (1L << StringLiteral) | (1L << NullLiteral))) != 0))) {
          _errHandler.recoverInline(this);
        } else {
          if (_input.LA(1) == Token.EOF) {
            matchedEOF = true;
          }

          _errHandler.reportMatch(this);
          consume();
        }
      }
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
  }

  public static class AnnotationsContext extends IncrementalParserRuleContext {
    public List<? extends AnnotationContext> annotation() {
      return getRuleContexts(AnnotationContext.class);
    }

    public AnnotationContext annotation(int i) {
      return getRuleContext(AnnotationContext.class, i);
    }

    public AnnotationsContext(ParserRuleContext parent, int invokingState) {
      super((IncrementalParserRuleContext) parent, invokingState);
    }

    @Override
    public int getRuleIndex() {
      return RULE_annotations;
    }
  }

  @RuleVersion(0)
  public final AnnotationsContext annotations() throws RecognitionException {
    // Check whether we need to execute this rule.
    AnnotationsContext guardResult = (AnnotationsContext) guardRule((IncrementalParserRuleContext) _ctx, getState(), RULE_annotations);
    // If we found an existing context that is valid, return it.
    if (guardResult != null) {
      this._input.seek(guardResult.stop.getTokenIndex() + 1);
      return guardResult;
    }
    AnnotationsContext _localctx = new AnnotationsContext(_ctx, getState());
    enterRule(_localctx, 132, RULE_annotations);
    try {
      int _alt;
      enterOuterAlt(_localctx, 1);
      {
        setState(754);
        _errHandler.sync(this);
        _alt = 1;
        do {
          switch (_alt) {
            case 1: {
              {
                setState(753);
                annotation();
              }
            }
            break;
            default:
              throw new NoViableAltException(this);
          }
          setState(756);
          _errHandler.sync(this);
          _alt = getInterpreter().adaptivePredict(_input, 81, _ctx);
        } while (_alt != 2 && _alt != ATN.INVALID_ALT_NUMBER);
      }
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
  }

  public static class AnnotationContext extends IncrementalParserRuleContext {
    public TerminalNode AT() {
      return getToken(TestIncrementalJavaParser.AT, 0);
    }

    public AnnotationNameContext annotationName() {
      return getRuleContext(AnnotationNameContext.class, 0);
    }

    public TerminalNode LPAREN() {
      return getToken(TestIncrementalJavaParser.LPAREN, 0);
    }

    public TerminalNode RPAREN() {
      return getToken(TestIncrementalJavaParser.RPAREN, 0);
    }

    public ElementValuePairsContext elementValuePairs() {
      return getRuleContext(ElementValuePairsContext.class, 0);
    }

    public ElementValueContext elementValue() {
      return getRuleContext(ElementValueContext.class, 0);
    }

    public AnnotationContext(ParserRuleContext parent, int invokingState) {
      super((IncrementalParserRuleContext) parent, invokingState);
    }

    @Override
    public int getRuleIndex() {
      return RULE_annotation;
    }
  }

  @RuleVersion(0)
  public final AnnotationContext annotation() throws RecognitionException {
    // Check whether we need to execute this rule.
    AnnotationContext guardResult = (AnnotationContext) guardRule((IncrementalParserRuleContext) _ctx, getState(), RULE_annotation);
    // If we found an existing context that is valid, return it.
    if (guardResult != null) {
      this._input.seek(guardResult.stop.getTokenIndex() + 1);
      return guardResult;
    }
    AnnotationContext _localctx = new AnnotationContext(_ctx, getState());
    enterRule(_localctx, 134, RULE_annotation);
    int _la;
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(758);
        match(AT);
        setState(759);
        annotationName();
        setState(766);
        _errHandler.sync(this);
        _la = _input.LA(1);
        if (_la == LPAREN) {
          {
            setState(760);
            match(LPAREN);
            setState(763);
            _errHandler.sync(this);
            switch (getInterpreter().adaptivePredict(_input, 82, _ctx)) {
              case 1: {
                setState(761);
                elementValuePairs();
              }
              break;
              case 2: {
                setState(762);
                elementValue();
              }
              break;
            }
            setState(765);
            match(RPAREN);
          }
        }

      }
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
  }

  public static class AnnotationNameContext extends IncrementalParserRuleContext {
    public List<? extends TerminalNode> Identifier() {
      return getTokens(TestIncrementalJavaParser.Identifier);
    }

    public TerminalNode Identifier(int i) {
      return getToken(TestIncrementalJavaParser.Identifier, i);
    }

    public List<? extends TerminalNode> DOT() {
      return getTokens(TestIncrementalJavaParser.DOT);
    }

    public TerminalNode DOT(int i) {
      return getToken(TestIncrementalJavaParser.DOT, i);
    }

    public AnnotationNameContext(ParserRuleContext parent, int invokingState) {
      super((IncrementalParserRuleContext) parent, invokingState);
    }

    @Override
    public int getRuleIndex() {
      return RULE_annotationName;
    }
  }

  @RuleVersion(0)
  public final AnnotationNameContext annotationName() throws RecognitionException {
    // Check whether we need to execute this rule.
    AnnotationNameContext guardResult = (AnnotationNameContext) guardRule((IncrementalParserRuleContext) _ctx, getState(), RULE_annotationName);
    // If we found an existing context that is valid, return it.
    if (guardResult != null) {
      this._input.seek(guardResult.stop.getTokenIndex() + 1);
      return guardResult;
    }
    AnnotationNameContext _localctx = new AnnotationNameContext(_ctx, getState());
    enterRule(_localctx, 136, RULE_annotationName);
    int _la;
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(768);
        match(Identifier);
        setState(773);
        _errHandler.sync(this);
        _la = _input.LA(1);
        while (_la == DOT) {
          {
            {
              setState(769);
              match(DOT);
              setState(770);
              match(Identifier);
            }
          }
          setState(775);
          _errHandler.sync(this);
          _la = _input.LA(1);
        }
      }
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
  }

  public static class ElementValuePairsContext extends IncrementalParserRuleContext {
    public List<? extends ElementValuePairContext> elementValuePair() {
      return getRuleContexts(ElementValuePairContext.class);
    }

    public ElementValuePairContext elementValuePair(int i) {
      return getRuleContext(ElementValuePairContext.class, i);
    }

    public List<? extends TerminalNode> COMMA() {
      return getTokens(TestIncrementalJavaParser.COMMA);
    }

    public TerminalNode COMMA(int i) {
      return getToken(TestIncrementalJavaParser.COMMA, i);
    }

    public ElementValuePairsContext(ParserRuleContext parent, int invokingState) {
      super((IncrementalParserRuleContext) parent, invokingState);
    }

    @Override
    public int getRuleIndex() {
      return RULE_elementValuePairs;
    }
  }

  @RuleVersion(0)
  public final ElementValuePairsContext elementValuePairs() throws RecognitionException {
    // Check whether we need to execute this rule.
    ElementValuePairsContext guardResult = (ElementValuePairsContext) guardRule((IncrementalParserRuleContext) _ctx, getState(), RULE_elementValuePairs);
    // If we found an existing context that is valid, return it.
    if (guardResult != null) {
      this._input.seek(guardResult.stop.getTokenIndex() + 1);
      return guardResult;
    }
    ElementValuePairsContext _localctx = new ElementValuePairsContext(_ctx, getState());
    enterRule(_localctx, 138, RULE_elementValuePairs);
    int _la;
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(776);
        elementValuePair();
        setState(781);
        _errHandler.sync(this);
        _la = _input.LA(1);
        while (_la == COMMA) {
          {
            {
              setState(777);
              match(COMMA);
              setState(778);
              elementValuePair();
            }
          }
          setState(783);
          _errHandler.sync(this);
          _la = _input.LA(1);
        }
      }
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
  }

  public static class ElementValuePairContext extends IncrementalParserRuleContext {
    public TerminalNode Identifier() {
      return getToken(TestIncrementalJavaParser.Identifier, 0);
    }

    public TerminalNode ASSIGN() {
      return getToken(TestIncrementalJavaParser.ASSIGN, 0);
    }

    public ElementValueContext elementValue() {
      return getRuleContext(ElementValueContext.class, 0);
    }

    public ElementValuePairContext(ParserRuleContext parent, int invokingState) {
      super((IncrementalParserRuleContext) parent, invokingState);
    }

    @Override
    public int getRuleIndex() {
      return RULE_elementValuePair;
    }

  }

  @RuleVersion(0)
  public final ElementValuePairContext elementValuePair() throws RecognitionException {
    // Check whether we need to execute this rule.
    ElementValuePairContext guardResult = (ElementValuePairContext) guardRule((IncrementalParserRuleContext) _ctx, getState(), RULE_elementValuePair);
    // If we found an existing context that is valid, return it.
    if (guardResult != null) {
      this._input.seek(guardResult.stop.getTokenIndex() + 1);
      return guardResult;
    }
    ElementValuePairContext _localctx = new ElementValuePairContext(_ctx, getState());
    enterRule(_localctx, 140, RULE_elementValuePair);
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(784);
        match(Identifier);
        setState(785);
        match(ASSIGN);
        setState(786);
        elementValue();
      }
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
  }

  public static class ElementValueContext extends IncrementalParserRuleContext {
    public ExpressionContext expression() {
      return getRuleContext(ExpressionContext.class, 0);
    }

    public AnnotationContext annotation() {
      return getRuleContext(AnnotationContext.class, 0);
    }

    public ElementValueArrayInitializerContext elementValueArrayInitializer() {
      return getRuleContext(ElementValueArrayInitializerContext.class, 0);
    }

    public ElementValueContext(ParserRuleContext parent, int invokingState) {
      super((IncrementalParserRuleContext) parent, invokingState);
    }

    @Override
    public int getRuleIndex() {
      return RULE_elementValue;
    }
  }

  @RuleVersion(0)
  public final ElementValueContext elementValue() throws RecognitionException {
    // Check whether we need to execute this rule.
    ElementValueContext guardResult = (ElementValueContext) guardRule((IncrementalParserRuleContext) _ctx, getState(), RULE_elementValue);
    // If we found an existing context that is valid, return it.
    if (guardResult != null) {
      this._input.seek(guardResult.stop.getTokenIndex() + 1);
      return guardResult;
    }
    ElementValueContext _localctx = new ElementValueContext(_ctx, getState());
    enterRule(_localctx, 142, RULE_elementValue);
    try {
      setState(791);
      _errHandler.sync(this);
      switch (_input.LA(1)) {
        case BOOLEAN:
        case BYTE:
        case CHAR:
        case DOUBLE:
        case FLOAT:
        case INT:
        case LONG:
        case NEW:
        case SHORT:
        case SUPER:
        case THIS:
        case VOID:
        case IntegerLiteral:
        case FloatingPointLiteral:
        case BooleanLiteral:
        case CharacterLiteral:
        case StringLiteral:
        case NullLiteral:
        case LPAREN:
        case LT:
        case BANG:
        case TILDE:
        case INC:
        case DEC:
        case ADD:
        case SUB:
        case Identifier:
          enterOuterAlt(_localctx, 1);
        {
          setState(788);
          expression(0);
        }
        break;
        case AT:
          enterOuterAlt(_localctx, 2);
        {
          setState(789);
          annotation();
        }
        break;
        case LBRACE:
          enterOuterAlt(_localctx, 3);
        {
          setState(790);
          elementValueArrayInitializer();
        }
        break;
        default:
          throw new NoViableAltException(this);
      }
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
  }

  public static class ElementValueArrayInitializerContext extends IncrementalParserRuleContext {
    public TerminalNode LBRACE() {
      return getToken(TestIncrementalJavaParser.LBRACE, 0);
    }

    public TerminalNode RBRACE() {
      return getToken(TestIncrementalJavaParser.RBRACE, 0);
    }

    public List<? extends ElementValueContext> elementValue() {
      return getRuleContexts(ElementValueContext.class);
    }

    public ElementValueContext elementValue(int i) {
      return getRuleContext(ElementValueContext.class, i);
    }

    public List<? extends TerminalNode> COMMA() {
      return getTokens(TestIncrementalJavaParser.COMMA);
    }

    public TerminalNode COMMA(int i) {
      return getToken(TestIncrementalJavaParser.COMMA, i);
    }

    public ElementValueArrayInitializerContext(ParserRuleContext parent, int invokingState) {
      super((IncrementalParserRuleContext) parent, invokingState);
    }

    @Override
    public int getRuleIndex() {
      return RULE_elementValueArrayInitializer;
    }
  }

  @RuleVersion(0)
  public final ElementValueArrayInitializerContext elementValueArrayInitializer() throws RecognitionException {
    // Check whether we need to execute this rule.
    ElementValueArrayInitializerContext guardResult = (ElementValueArrayInitializerContext) guardRule((IncrementalParserRuleContext) _ctx, getState(), RULE_elementValueArrayInitializer);
    // If we found an existing context that is valid, return it.
    if (guardResult != null) {
      this._input.seek(guardResult.stop.getTokenIndex() + 1);
      return guardResult;
    }
    ElementValueArrayInitializerContext _localctx = new ElementValueArrayInitializerContext(_ctx, getState());
    enterRule(_localctx, 144, RULE_elementValueArrayInitializer);
    int _la;
    try {
      int _alt;
      enterOuterAlt(_localctx, 1);
      {
        setState(793);
        match(LBRACE);
        setState(802);
        _errHandler.sync(this);
        _la = _input.LA(1);
        if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << BOOLEAN) | (1L << BYTE) | (1L << CHAR) | (1L << DOUBLE) | (1L << FLOAT) | (1L << INT) | (1L << LONG) | (1L << NEW) | (1L << SHORT) | (1L << SUPER) | (1L << THIS) | (1L << VOID) | (1L << IntegerLiteral) | (1L << FloatingPointLiteral) | (1L << BooleanLiteral) | (1L << CharacterLiteral) | (1L << StringLiteral) | (1L << NullLiteral) | (1L << LPAREN) | (1L << LBRACE))) != 0) || ((((_la - 68)) & ~0x3f) == 0 && ((1L << (_la - 68)) & ((1L << (LT - 68)) | (1L << (BANG - 68)) | (1L << (TILDE - 68)) | (1L << (INC - 68)) | (1L << (DEC - 68)) | (1L << (ADD - 68)) | (1L << (SUB - 68)) | (1L << (Identifier - 68)) | (1L << (AT - 68)))) != 0)) {
          {
            setState(794);
            elementValue();
            setState(799);
            _errHandler.sync(this);
            _alt = getInterpreter().adaptivePredict(_input, 87, _ctx);
            while (_alt != 2 && _alt != ATN.INVALID_ALT_NUMBER) {
              if (_alt == 1) {
                {
                  {
                    setState(795);
                    match(COMMA);
                    setState(796);
                    elementValue();
                  }
                }
              }
              setState(801);
              _errHandler.sync(this);
              _alt = getInterpreter().adaptivePredict(_input, 87, _ctx);
            }
          }
        }

        setState(805);
        _errHandler.sync(this);
        _la = _input.LA(1);
        if (_la == COMMA) {
          {
            setState(804);
            match(COMMA);
          }
        }

        setState(807);
        match(RBRACE);
      }
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
  }

  public static class AnnotationTypeDeclarationContext extends IncrementalParserRuleContext {
    public TerminalNode AT() {
      return getToken(TestIncrementalJavaParser.AT, 0);
    }

    public TerminalNode INTERFACE() {
      return getToken(TestIncrementalJavaParser.INTERFACE, 0);
    }

    public TerminalNode Identifier() {
      return getToken(TestIncrementalJavaParser.Identifier, 0);
    }

    public AnnotationTypeBodyContext annotationTypeBody() {
      return getRuleContext(AnnotationTypeBodyContext.class, 0);
    }

    public AnnotationTypeDeclarationContext(ParserRuleContext parent, int invokingState) {
      super((IncrementalParserRuleContext) parent, invokingState);
    }

    @Override
    public int getRuleIndex() {
      return RULE_annotationTypeDeclaration;
    }
  }

  @RuleVersion(0)
  public final AnnotationTypeDeclarationContext annotationTypeDeclaration() throws RecognitionException {
    // Check whether we need to execute this rule.
    AnnotationTypeDeclarationContext guardResult = (AnnotationTypeDeclarationContext) guardRule((IncrementalParserRuleContext) _ctx, getState(), RULE_annotationTypeDeclaration);
    // If we found an existing context that is valid, return it.
    if (guardResult != null) {
      this._input.seek(guardResult.stop.getTokenIndex() + 1);
      return guardResult;
    }
    AnnotationTypeDeclarationContext _localctx = new AnnotationTypeDeclarationContext(_ctx, getState());
    enterRule(_localctx, 146, RULE_annotationTypeDeclaration);
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(809);
        match(AT);
        setState(810);
        match(INTERFACE);
        setState(811);
        match(Identifier);
        setState(812);
        annotationTypeBody();
      }
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
  }

  public static class AnnotationTypeBodyContext extends IncrementalParserRuleContext {
    public TerminalNode LBRACE() {
      return getToken(TestIncrementalJavaParser.LBRACE, 0);
    }

    public TerminalNode RBRACE() {
      return getToken(TestIncrementalJavaParser.RBRACE, 0);
    }

    public List<? extends AnnotationTypeElementDeclarationContext> annotationTypeElementDeclaration() {
      return getRuleContexts(AnnotationTypeElementDeclarationContext.class);
    }

    public AnnotationTypeElementDeclarationContext annotationTypeElementDeclaration(int i) {
      return getRuleContext(AnnotationTypeElementDeclarationContext.class, i);
    }

    public AnnotationTypeBodyContext(ParserRuleContext parent, int invokingState) {
      super((IncrementalParserRuleContext) parent, invokingState);
    }

    @Override
    public int getRuleIndex() {
      return RULE_annotationTypeBody;
    }
  }

  @RuleVersion(0)
  public final AnnotationTypeBodyContext annotationTypeBody() throws RecognitionException {
    // Check whether we need to execute this rule.
    AnnotationTypeBodyContext guardResult = (AnnotationTypeBodyContext) guardRule((IncrementalParserRuleContext) _ctx, getState(), RULE_annotationTypeBody);
    // If we found an existing context that is valid, return it.
    if (guardResult != null) {
      this._input.seek(guardResult.stop.getTokenIndex() + 1);
      return guardResult;
    }
    AnnotationTypeBodyContext _localctx = new AnnotationTypeBodyContext(_ctx, getState());
    enterRule(_localctx, 148, RULE_annotationTypeBody);
    int _la;
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(814);
        match(LBRACE);
        setState(818);
        _errHandler.sync(this);
        _la = _input.LA(1);
        while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << ABSTRACT) | (1L << BOOLEAN) | (1L << BYTE) | (1L << CHAR) | (1L << CLASS) | (1L << DOUBLE) | (1L << ENUM) | (1L << FINAL) | (1L << FLOAT) | (1L << INT) | (1L << INTERFACE) | (1L << LONG) | (1L << NATIVE) | (1L << PRIVATE) | (1L << PROTECTED) | (1L << PUBLIC) | (1L << SHORT) | (1L << STATIC) | (1L << STRICTFP) | (1L << SYNCHRONIZED) | (1L << TRANSIENT) | (1L << VOLATILE) | (1L << SEMI))) != 0) || _la == Identifier || _la == AT) {
          {
            {
              setState(815);
              annotationTypeElementDeclaration();
            }
          }
          setState(820);
          _errHandler.sync(this);
          _la = _input.LA(1);
        }
        setState(821);
        match(RBRACE);
      }
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
  }

  public static class AnnotationTypeElementDeclarationContext extends IncrementalParserRuleContext {
    public ModifiersContext modifiers() {
      return getRuleContext(ModifiersContext.class, 0);
    }

    public AnnotationTypeElementRestContext annotationTypeElementRest() {
      return getRuleContext(AnnotationTypeElementRestContext.class, 0);
    }

    public TerminalNode SEMI() {
      return getToken(TestIncrementalJavaParser.SEMI, 0);
    }

    public AnnotationTypeElementDeclarationContext(ParserRuleContext parent, int invokingState) {
      super((IncrementalParserRuleContext) parent, invokingState);
    }

    @Override
    public int getRuleIndex() {
      return RULE_annotationTypeElementDeclaration;
    }
  }

  @RuleVersion(0)
  public final AnnotationTypeElementDeclarationContext annotationTypeElementDeclaration() throws RecognitionException {
    // Check whether we need to execute this rule.
    AnnotationTypeElementDeclarationContext guardResult = (AnnotationTypeElementDeclarationContext) guardRule((IncrementalParserRuleContext) _ctx, getState(), RULE_annotationTypeElementDeclaration);
    // If we found an existing context that is valid, return it.
    if (guardResult != null) {
      this._input.seek(guardResult.stop.getTokenIndex() + 1);
      return guardResult;
    }
    AnnotationTypeElementDeclarationContext _localctx = new AnnotationTypeElementDeclarationContext(_ctx, getState());
    enterRule(_localctx, 150, RULE_annotationTypeElementDeclaration);
    try {
      setState(827);
      _errHandler.sync(this);
      switch (_input.LA(1)) {
        case ABSTRACT:
        case BOOLEAN:
        case BYTE:
        case CHAR:
        case CLASS:
        case DOUBLE:
        case ENUM:
        case FINAL:
        case FLOAT:
        case INT:
        case INTERFACE:
        case LONG:
        case NATIVE:
        case PRIVATE:
        case PROTECTED:
        case PUBLIC:
        case SHORT:
        case STATIC:
        case STRICTFP:
        case SYNCHRONIZED:
        case TRANSIENT:
        case VOLATILE:
        case Identifier:
        case AT:
          enterOuterAlt(_localctx, 1);
        {
          setState(823);
          modifiers();
          setState(824);
          annotationTypeElementRest();
        }
        break;
        case SEMI:
          enterOuterAlt(_localctx, 2);
        {
          setState(826);
          match(SEMI);
        }
        break;
        default:
          throw new NoViableAltException(this);
      }
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
  }

  public static class AnnotationTypeElementRestContext extends IncrementalParserRuleContext {
    public TypeContext type() {
      return getRuleContext(TypeContext.class, 0);
    }

    public AnnotationMethodOrConstantRestContext annotationMethodOrConstantRest() {
      return getRuleContext(AnnotationMethodOrConstantRestContext.class, 0);
    }

    public TerminalNode SEMI() {
      return getToken(TestIncrementalJavaParser.SEMI, 0);
    }

    public NormalClassDeclarationContext normalClassDeclaration() {
      return getRuleContext(NormalClassDeclarationContext.class, 0);
    }

    public NormalInterfaceDeclarationContext normalInterfaceDeclaration() {
      return getRuleContext(NormalInterfaceDeclarationContext.class, 0);
    }

    public EnumDeclarationContext enumDeclaration() {
      return getRuleContext(EnumDeclarationContext.class, 0);
    }

    public AnnotationTypeDeclarationContext annotationTypeDeclaration() {
      return getRuleContext(AnnotationTypeDeclarationContext.class, 0);
    }

    public AnnotationTypeElementRestContext(ParserRuleContext parent, int invokingState) {
      super((IncrementalParserRuleContext) parent, invokingState);
    }

    @Override
    public int getRuleIndex() {
      return RULE_annotationTypeElementRest;
    }
  }

  @RuleVersion(0)
  public final AnnotationTypeElementRestContext annotationTypeElementRest() throws RecognitionException {
    // Check whether we need to execute this rule.
    AnnotationTypeElementRestContext guardResult = (AnnotationTypeElementRestContext) guardRule((IncrementalParserRuleContext) _ctx, getState(), RULE_annotationTypeElementRest);
    // If we found an existing context that is valid, return it.
    if (guardResult != null) {
      this._input.seek(guardResult.stop.getTokenIndex() + 1);
      return guardResult;
    }
    AnnotationTypeElementRestContext _localctx = new AnnotationTypeElementRestContext(_ctx, getState());
    enterRule(_localctx, 152, RULE_annotationTypeElementRest);
    try {
      setState(849);
      _errHandler.sync(this);
      switch (_input.LA(1)) {
        case BOOLEAN:
        case BYTE:
        case CHAR:
        case DOUBLE:
        case FLOAT:
        case INT:
        case LONG:
        case SHORT:
        case Identifier:
          enterOuterAlt(_localctx, 1);
        {
          setState(829);
          type();
          setState(830);
          annotationMethodOrConstantRest();
          setState(831);
          match(SEMI);
        }
        break;
        case CLASS:
          enterOuterAlt(_localctx, 2);
        {
          setState(833);
          normalClassDeclaration();
          setState(835);
          _errHandler.sync(this);
          switch (getInterpreter().adaptivePredict(_input, 92, _ctx)) {
            case 1: {
              setState(834);
              match(SEMI);
            }
            break;
          }
        }
        break;
        case INTERFACE:
          enterOuterAlt(_localctx, 3);
        {
          setState(837);
          normalInterfaceDeclaration();
          setState(839);
          _errHandler.sync(this);
          switch (getInterpreter().adaptivePredict(_input, 93, _ctx)) {
            case 1: {
              setState(838);
              match(SEMI);
            }
            break;
          }
        }
        break;
        case ENUM:
          enterOuterAlt(_localctx, 4);
        {
          setState(841);
          enumDeclaration();
          setState(843);
          _errHandler.sync(this);
          switch (getInterpreter().adaptivePredict(_input, 94, _ctx)) {
            case 1: {
              setState(842);
              match(SEMI);
            }
            break;
          }
        }
        break;
        case AT:
          enterOuterAlt(_localctx, 5);
        {
          setState(845);
          annotationTypeDeclaration();
          setState(847);
          _errHandler.sync(this);
          switch (getInterpreter().adaptivePredict(_input, 95, _ctx)) {
            case 1: {
              setState(846);
              match(SEMI);
            }
            break;
          }
        }
        break;
        default:
          throw new NoViableAltException(this);
      }
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
  }

  public static class AnnotationMethodOrConstantRestContext extends IncrementalParserRuleContext {
    public AnnotationMethodRestContext annotationMethodRest() {
      return getRuleContext(AnnotationMethodRestContext.class, 0);
    }

    public AnnotationConstantRestContext annotationConstantRest() {
      return getRuleContext(AnnotationConstantRestContext.class, 0);
    }

    public AnnotationMethodOrConstantRestContext(ParserRuleContext parent, int invokingState) {
      super((IncrementalParserRuleContext) parent, invokingState);
    }

    @Override
    public int getRuleIndex() {
      return RULE_annotationMethodOrConstantRest;
    }
  }

  @RuleVersion(0)
  public final AnnotationMethodOrConstantRestContext annotationMethodOrConstantRest() throws RecognitionException {
    // Check whether we need to execute this rule.
    AnnotationMethodOrConstantRestContext guardResult = (AnnotationMethodOrConstantRestContext) guardRule((IncrementalParserRuleContext) _ctx, getState(), RULE_annotationMethodOrConstantRest);
    // If we found an existing context that is valid, return it.
    if (guardResult != null) {
      this._input.seek(guardResult.stop.getTokenIndex() + 1);
      return guardResult;
    }
    AnnotationMethodOrConstantRestContext _localctx = new AnnotationMethodOrConstantRestContext(_ctx, getState());
    enterRule(_localctx, 154, RULE_annotationMethodOrConstantRest);
    try {
      setState(853);
      _errHandler.sync(this);
      switch (getInterpreter().adaptivePredict(_input, 97, _ctx)) {
        case 1:
          enterOuterAlt(_localctx, 1);
        {
          setState(851);
          annotationMethodRest();
        }
        break;
        case 2:
          enterOuterAlt(_localctx, 2);
        {
          setState(852);
          annotationConstantRest();
        }
        break;
      }
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
  }

  public static class AnnotationMethodRestContext extends IncrementalParserRuleContext {
    public TerminalNode Identifier() {
      return getToken(TestIncrementalJavaParser.Identifier, 0);
    }

    public TerminalNode LPAREN() {
      return getToken(TestIncrementalJavaParser.LPAREN, 0);
    }

    public TerminalNode RPAREN() {
      return getToken(TestIncrementalJavaParser.RPAREN, 0);
    }

    public DefaultValueContext defaultValue() {
      return getRuleContext(DefaultValueContext.class, 0);
    }

    public AnnotationMethodRestContext(ParserRuleContext parent, int invokingState) {
      super((IncrementalParserRuleContext) parent, invokingState);
    }

    @Override
    public int getRuleIndex() {
      return RULE_annotationMethodRest;
    }
  }

  @RuleVersion(0)
  public final AnnotationMethodRestContext annotationMethodRest() throws RecognitionException {
    // Check whether we need to execute this rule.
    AnnotationMethodRestContext guardResult = (AnnotationMethodRestContext) guardRule((IncrementalParserRuleContext) _ctx, getState(), RULE_annotationMethodRest);
    // If we found an existing context that is valid, return it.
    if (guardResult != null) {
      this._input.seek(guardResult.stop.getTokenIndex() + 1);
      return guardResult;
    }
    AnnotationMethodRestContext _localctx = new AnnotationMethodRestContext(_ctx, getState());
    enterRule(_localctx, 156, RULE_annotationMethodRest);
    int _la;
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(855);
        match(Identifier);
        setState(856);
        match(LPAREN);
        setState(857);
        match(RPAREN);
        setState(859);
        _errHandler.sync(this);
        _la = _input.LA(1);
        if (_la == DEFAULT) {
          {
            setState(858);
            defaultValue();
          }
        }

      }
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
  }

  public static class AnnotationConstantRestContext extends IncrementalParserRuleContext {
    public VariableDeclaratorsContext variableDeclarators() {
      return getRuleContext(VariableDeclaratorsContext.class, 0);
    }

    public AnnotationConstantRestContext(ParserRuleContext parent, int invokingState) {
      super((IncrementalParserRuleContext) parent, invokingState);
    }

    @Override
    public int getRuleIndex() {
      return RULE_annotationConstantRest;
    }
  }

  @RuleVersion(0)
  public final AnnotationConstantRestContext annotationConstantRest() throws RecognitionException {
    // Check whether we need to execute this rule.
    AnnotationConstantRestContext guardResult = (AnnotationConstantRestContext) guardRule((IncrementalParserRuleContext) _ctx, getState(), RULE_annotationConstantRest);
    // If we found an existing context that is valid, return it.
    if (guardResult != null) {
      this._input.seek(guardResult.stop.getTokenIndex() + 1);
      return guardResult;
    }
    AnnotationConstantRestContext _localctx = new AnnotationConstantRestContext(_ctx, getState());
    enterRule(_localctx, 158, RULE_annotationConstantRest);
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(861);
        variableDeclarators();
      }
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
  }

  public static class DefaultValueContext extends IncrementalParserRuleContext {
    public TerminalNode DEFAULT() {
      return getToken(TestIncrementalJavaParser.DEFAULT, 0);
    }

    public ElementValueContext elementValue() {
      return getRuleContext(ElementValueContext.class, 0);
    }

    public DefaultValueContext(ParserRuleContext parent, int invokingState) {
      super((IncrementalParserRuleContext) parent, invokingState);
    }

    @Override
    public int getRuleIndex() {
      return RULE_defaultValue;
    }
  }

  @RuleVersion(0)
  public final DefaultValueContext defaultValue() throws RecognitionException {
    // Check whether we need to execute this rule.
    DefaultValueContext guardResult = (DefaultValueContext) guardRule((IncrementalParserRuleContext) _ctx, getState(), RULE_defaultValue);
    // If we found an existing context that is valid, return it.
    if (guardResult != null) {
      this._input.seek(guardResult.stop.getTokenIndex() + 1);
      return guardResult;
    }
    DefaultValueContext _localctx = new DefaultValueContext(_ctx, getState());
    enterRule(_localctx, 160, RULE_defaultValue);
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(863);
        match(DEFAULT);
        setState(864);
        elementValue();
      }
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
  }

  public static class BlockContext extends IncrementalParserRuleContext {
    public TerminalNode LBRACE() {
      return getToken(TestIncrementalJavaParser.LBRACE, 0);
    }

    public TerminalNode RBRACE() {
      return getToken(TestIncrementalJavaParser.RBRACE, 0);
    }

    public List<? extends BlockStatementContext> blockStatement() {
      return getRuleContexts(BlockStatementContext.class);
    }

    public BlockStatementContext blockStatement(int i) {
      return getRuleContext(BlockStatementContext.class, i);
    }

    public BlockContext(ParserRuleContext parent, int invokingState) {
      super((IncrementalParserRuleContext) parent, invokingState);
    }

    @Override
    public int getRuleIndex() {
      return RULE_block;
    }
  }

  @RuleVersion(0)
  public final BlockContext block() throws RecognitionException {
    // Check whether we need to execute this rule.
    BlockContext guardResult = (BlockContext) guardRule((IncrementalParserRuleContext) _ctx, getState(), RULE_block);
    // If we found an existing context that is valid, return it.
    if (guardResult != null) {
      this._input.seek(guardResult.stop.getTokenIndex() + 1);
      return guardResult;
    }
    BlockContext _localctx = new BlockContext(_ctx, getState());
    enterRule(_localctx, 162, RULE_block);
    int _la;
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(866);
        match(LBRACE);
        setState(870);
        _errHandler.sync(this);
        _la = _input.LA(1);
        while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << ABSTRACT) | (1L << ASSERT) | (1L << BOOLEAN) | (1L << BREAK) | (1L << BYTE) | (1L << CHAR) | (1L << CLASS) | (1L << CONTINUE) | (1L << DO) | (1L << DOUBLE) | (1L << ENUM) | (1L << FINAL) | (1L << FLOAT) | (1L << FOR) | (1L << IF) | (1L << INT) | (1L << INTERFACE) | (1L << LONG) | (1L << NEW) | (1L << PRIVATE) | (1L << PROTECTED) | (1L << PUBLIC) | (1L << RETURN) | (1L << SHORT) | (1L << STATIC) | (1L << STRICTFP) | (1L << SUPER) | (1L << SWITCH) | (1L << SYNCHRONIZED) | (1L << THIS) | (1L << THROW) | (1L << TRY) | (1L << VOID) | (1L << WHILE) | (1L << IntegerLiteral) | (1L << FloatingPointLiteral) | (1L << BooleanLiteral) | (1L << CharacterLiteral) | (1L << StringLiteral) | (1L << NullLiteral) | (1L << LPAREN) | (1L << LBRACE) | (1L << SEMI))) != 0) || ((((_la - 68)) & ~0x3f) == 0 && ((1L << (_la - 68)) & ((1L << (LT - 68)) | (1L << (BANG - 68)) | (1L << (TILDE - 68)) | (1L << (INC - 68)) | (1L << (DEC - 68)) | (1L << (ADD - 68)) | (1L << (SUB - 68)) | (1L << (Identifier - 68)) | (1L << (AT - 68)))) != 0)) {
          {
            {
              setState(867);
              blockStatement();
            }
          }
          setState(872);
          _errHandler.sync(this);
          _la = _input.LA(1);
        }
        setState(873);
        match(RBRACE);
      }
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
  }

  public static class BlockStatementContext extends IncrementalParserRuleContext {
    public LocalVariableDeclarationStatementContext localVariableDeclarationStatement() {
      return getRuleContext(LocalVariableDeclarationStatementContext.class, 0);
    }

    public ClassOrInterfaceDeclarationContext classOrInterfaceDeclaration() {
      return getRuleContext(ClassOrInterfaceDeclarationContext.class, 0);
    }

    public StatementContext statement() {
      return getRuleContext(StatementContext.class, 0);
    }

    public BlockStatementContext(ParserRuleContext parent, int invokingState) {
      super((IncrementalParserRuleContext) parent, invokingState);
    }

    @Override
    public int getRuleIndex() {
      return RULE_blockStatement;
    }
  }

  @RuleVersion(0)
  public final BlockStatementContext blockStatement() throws RecognitionException {
    // Check whether we need to execute this rule.
    BlockStatementContext guardResult = (BlockStatementContext) guardRule((IncrementalParserRuleContext) _ctx, getState(), RULE_blockStatement);
    // If we found an existing context that is valid, return it.
    if (guardResult != null) {
      this._input.seek(guardResult.stop.getTokenIndex() + 1);
      return guardResult;
    }
    BlockStatementContext _localctx = new BlockStatementContext(_ctx, getState());
    enterRule(_localctx, 164, RULE_blockStatement);
    try {
      setState(878);
      _errHandler.sync(this);
      switch (getInterpreter().adaptivePredict(_input, 100, _ctx)) {
        case 1:
          enterOuterAlt(_localctx, 1);
        {
          setState(875);
          localVariableDeclarationStatement();
        }
        break;
        case 2:
          enterOuterAlt(_localctx, 2);
        {
          setState(876);
          classOrInterfaceDeclaration();
        }
        break;
        case 3:
          enterOuterAlt(_localctx, 3);
        {
          setState(877);
          statement();
        }
        break;
      }
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
  }

  public static class LocalVariableDeclarationStatementContext extends IncrementalParserRuleContext {
    public LocalVariableDeclarationContext localVariableDeclaration() {
      return getRuleContext(LocalVariableDeclarationContext.class, 0);
    }

    public TerminalNode SEMI() {
      return getToken(TestIncrementalJavaParser.SEMI, 0);
    }

    public LocalVariableDeclarationStatementContext(ParserRuleContext parent, int invokingState) {
      super((IncrementalParserRuleContext) parent, invokingState);
    }

    @Override
    public int getRuleIndex() {
      return RULE_localVariableDeclarationStatement;
    }
  }

  @RuleVersion(0)
  public final LocalVariableDeclarationStatementContext localVariableDeclarationStatement() throws RecognitionException {
    // Check whether we need to execute this rule.
    LocalVariableDeclarationStatementContext guardResult = (LocalVariableDeclarationStatementContext) guardRule((IncrementalParserRuleContext) _ctx, getState(), RULE_localVariableDeclarationStatement);
    // If we found an existing context that is valid, return it.
    if (guardResult != null) {
      this._input.seek(guardResult.stop.getTokenIndex() + 1);
      return guardResult;
    }
    LocalVariableDeclarationStatementContext _localctx = new LocalVariableDeclarationStatementContext(_ctx, getState());
    enterRule(_localctx, 166, RULE_localVariableDeclarationStatement);
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(880);
        localVariableDeclaration();
        setState(881);
        match(SEMI);
      }
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
  }

  public static class LocalVariableDeclarationContext extends IncrementalParserRuleContext {
    public VariableModifiersContext variableModifiers() {
      return getRuleContext(VariableModifiersContext.class, 0);
    }

    public TypeContext type() {
      return getRuleContext(TypeContext.class, 0);
    }

    public VariableDeclaratorsContext variableDeclarators() {
      return getRuleContext(VariableDeclaratorsContext.class, 0);
    }

    public LocalVariableDeclarationContext(ParserRuleContext parent, int invokingState) {
      super((IncrementalParserRuleContext) parent, invokingState);
    }

    @Override
    public int getRuleIndex() {
      return RULE_localVariableDeclaration;
    }
  }

  @RuleVersion(0)
  public final LocalVariableDeclarationContext localVariableDeclaration() throws RecognitionException {
    // Check whether we need to execute this rule.
    LocalVariableDeclarationContext guardResult = (LocalVariableDeclarationContext) guardRule((IncrementalParserRuleContext) _ctx, getState(), RULE_localVariableDeclaration);
    // If we found an existing context that is valid, return it.
    if (guardResult != null) {
      this._input.seek(guardResult.stop.getTokenIndex() + 1);
      return guardResult;
    }
    LocalVariableDeclarationContext _localctx = new LocalVariableDeclarationContext(_ctx, getState());
    enterRule(_localctx, 168, RULE_localVariableDeclaration);
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(883);
        variableModifiers();
        setState(884);
        type();
        setState(885);
        variableDeclarators();
      }
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
  }

  public static class VariableModifiersContext extends IncrementalParserRuleContext {
    public List<? extends VariableModifierContext> variableModifier() {
      return getRuleContexts(VariableModifierContext.class);
    }

    public VariableModifierContext variableModifier(int i) {
      return getRuleContext(VariableModifierContext.class, i);
    }

    public VariableModifiersContext(ParserRuleContext parent, int invokingState) {
      super((IncrementalParserRuleContext) parent, invokingState);
    }

    @Override
    public int getRuleIndex() {
      return RULE_variableModifiers;
    }
  }

  @RuleVersion(0)
  public final VariableModifiersContext variableModifiers() throws RecognitionException {
    // Check whether we need to execute this rule.
    VariableModifiersContext guardResult = (VariableModifiersContext) guardRule((IncrementalParserRuleContext) _ctx, getState(), RULE_variableModifiers);
    // If we found an existing context that is valid, return it.
    if (guardResult != null) {
      this._input.seek(guardResult.stop.getTokenIndex() + 1);
      return guardResult;
    }
    VariableModifiersContext _localctx = new VariableModifiersContext(_ctx, getState());
    enterRule(_localctx, 170, RULE_variableModifiers);
    int _la;
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(890);
        _errHandler.sync(this);
        _la = _input.LA(1);
        while (_la == FINAL || _la == AT) {
          {
            {
              setState(887);
              variableModifier();
            }
          }
          setState(892);
          _errHandler.sync(this);
          _la = _input.LA(1);
        }
      }
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
  }

  public static class StatementContext extends IncrementalParserRuleContext {
    public BlockContext block() {
      return getRuleContext(BlockContext.class, 0);
    }

    public TerminalNode ASSERT() {
      return getToken(TestIncrementalJavaParser.ASSERT, 0);
    }

    public List<? extends ExpressionContext> expression() {
      return getRuleContexts(ExpressionContext.class);
    }

    public ExpressionContext expression(int i) {
      return getRuleContext(ExpressionContext.class, i);
    }

    public TerminalNode SEMI() {
      return getToken(TestIncrementalJavaParser.SEMI, 0);
    }

    public TerminalNode COLON() {
      return getToken(TestIncrementalJavaParser.COLON, 0);
    }

    public TerminalNode IF() {
      return getToken(TestIncrementalJavaParser.IF, 0);
    }

    public ParExpressionContext parExpression() {
      return getRuleContext(ParExpressionContext.class, 0);
    }

    public List<? extends StatementContext> statement() {
      return getRuleContexts(StatementContext.class);
    }

    public StatementContext statement(int i) {
      return getRuleContext(StatementContext.class, i);
    }

    public TerminalNode ELSE() {
      return getToken(TestIncrementalJavaParser.ELSE, 0);
    }

    public TerminalNode FOR() {
      return getToken(TestIncrementalJavaParser.FOR, 0);
    }

    public TerminalNode LPAREN() {
      return getToken(TestIncrementalJavaParser.LPAREN, 0);
    }

    public ForControlContext forControl() {
      return getRuleContext(ForControlContext.class, 0);
    }

    public TerminalNode RPAREN() {
      return getToken(TestIncrementalJavaParser.RPAREN, 0);
    }

    public TerminalNode WHILE() {
      return getToken(TestIncrementalJavaParser.WHILE, 0);
    }

    public TerminalNode DO() {
      return getToken(TestIncrementalJavaParser.DO, 0);
    }

    public TerminalNode TRY() {
      return getToken(TestIncrementalJavaParser.TRY, 0);
    }

    public CatchesContext catches() {
      return getRuleContext(CatchesContext.class, 0);
    }

    public FinallyBlockContext finallyBlock() {
      return getRuleContext(FinallyBlockContext.class, 0);
    }

    public ResourceSpecificationContext resourceSpecification() {
      return getRuleContext(ResourceSpecificationContext.class, 0);
    }

    public TerminalNode SWITCH() {
      return getToken(TestIncrementalJavaParser.SWITCH, 0);
    }

    public TerminalNode LBRACE() {
      return getToken(TestIncrementalJavaParser.LBRACE, 0);
    }

    public SwitchBlockStatementGroupsContext switchBlockStatementGroups() {
      return getRuleContext(SwitchBlockStatementGroupsContext.class, 0);
    }

    public TerminalNode RBRACE() {
      return getToken(TestIncrementalJavaParser.RBRACE, 0);
    }

    public TerminalNode SYNCHRONIZED() {
      return getToken(TestIncrementalJavaParser.SYNCHRONIZED, 0);
    }

    public TerminalNode RETURN() {
      return getToken(TestIncrementalJavaParser.RETURN, 0);
    }

    public TerminalNode THROW() {
      return getToken(TestIncrementalJavaParser.THROW, 0);
    }

    public TerminalNode BREAK() {
      return getToken(TestIncrementalJavaParser.BREAK, 0);
    }

    public TerminalNode Identifier() {
      return getToken(TestIncrementalJavaParser.Identifier, 0);
    }

    public TerminalNode CONTINUE() {
      return getToken(TestIncrementalJavaParser.CONTINUE, 0);
    }

    public StatementExpressionContext statementExpression() {
      return getRuleContext(StatementExpressionContext.class, 0);
    }

    public StatementContext(ParserRuleContext parent, int invokingState) {
      super((IncrementalParserRuleContext) parent, invokingState);
    }

    @Override
    public int getRuleIndex() {
      return RULE_statement;
    }
  }

  @RuleVersion(0)
  public final StatementContext statement() throws RecognitionException {
    // Check whether we need to execute this rule.
    StatementContext guardResult = (StatementContext) guardRule((IncrementalParserRuleContext) _ctx, getState(), RULE_statement);
    // If we found an existing context that is valid, return it.
    if (guardResult != null) {
      this._input.seek(guardResult.stop.getTokenIndex() + 1);
      return guardResult;
    }
    StatementContext _localctx = new StatementContext(_ctx, getState());
    enterRule(_localctx, 172, RULE_statement);
    int _la;
    try {
      setState(979);
      _errHandler.sync(this);
      switch (getInterpreter().adaptivePredict(_input, 111, _ctx)) {
        case 1:
          enterOuterAlt(_localctx, 1);
        {
          setState(893);
          block();
        }
        break;
        case 2:
          enterOuterAlt(_localctx, 2);
        {
          setState(894);
          match(ASSERT);
          setState(895);
          expression(0);
          setState(898);
          _errHandler.sync(this);
          _la = _input.LA(1);
          if (_la == COLON) {
            {
              setState(896);
              match(COLON);
              setState(897);
              expression(0);
            }
          }

          setState(900);
          match(SEMI);
        }
        break;
        case 3:
          enterOuterAlt(_localctx, 3);
        {
          setState(902);
          match(IF);
          setState(903);
          parExpression();
          setState(904);
          statement();
          setState(907);
          _errHandler.sync(this);
          switch (getInterpreter().adaptivePredict(_input, 103, _ctx)) {
            case 1: {
              setState(905);
              match(ELSE);
              setState(906);
              statement();
            }
            break;
          }
        }
        break;
        case 4:
          enterOuterAlt(_localctx, 4);
        {
          setState(909);
          match(FOR);
          setState(910);
          match(LPAREN);
          setState(911);
          forControl();
          setState(912);
          match(RPAREN);
          setState(913);
          statement();
        }
        break;
        case 5:
          enterOuterAlt(_localctx, 5);
        {
          setState(915);
          match(WHILE);
          setState(916);
          parExpression();
          setState(917);
          statement();
        }
        break;
        case 6:
          enterOuterAlt(_localctx, 6);
        {
          setState(919);
          match(DO);
          setState(920);
          statement();
          setState(921);
          match(WHILE);
          setState(922);
          parExpression();
          setState(923);
          match(SEMI);
        }
        break;
        case 7:
          enterOuterAlt(_localctx, 7);
        {
          setState(925);
          match(TRY);
          setState(926);
          block();
          setState(932);
          _errHandler.sync(this);
          switch (_input.LA(1)) {
            case CATCH: {
              setState(927);
              catches();
              setState(929);
              _errHandler.sync(this);
              _la = _input.LA(1);
              if (_la == FINALLY) {
                {
                  setState(928);
                  finallyBlock();
                }
              }

            }
            break;
            case FINALLY: {
              setState(931);
              finallyBlock();
            }
            break;
            default:
              throw new NoViableAltException(this);
          }
        }
        break;
        case 8:
          enterOuterAlt(_localctx, 8);
        {
          setState(934);
          match(TRY);
          setState(935);
          resourceSpecification();
          setState(936);
          block();
          setState(938);
          _errHandler.sync(this);
          _la = _input.LA(1);
          if (_la == CATCH) {
            {
              setState(937);
              catches();
            }
          }

          setState(941);
          _errHandler.sync(this);
          _la = _input.LA(1);
          if (_la == FINALLY) {
            {
              setState(940);
              finallyBlock();
            }
          }

        }
        break;
        case 9:
          enterOuterAlt(_localctx, 9);
        {
          setState(943);
          match(SWITCH);
          setState(944);
          parExpression();
          setState(945);
          match(LBRACE);
          setState(946);
          switchBlockStatementGroups();
          setState(947);
          match(RBRACE);
        }
        break;
        case 10:
          enterOuterAlt(_localctx, 10);
        {
          setState(949);
          match(SYNCHRONIZED);
          setState(950);
          parExpression();
          setState(951);
          block();
        }
        break;
        case 11:
          enterOuterAlt(_localctx, 11);
        {
          setState(953);
          match(RETURN);
          setState(955);
          _errHandler.sync(this);
          _la = _input.LA(1);
          if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << BOOLEAN) | (1L << BYTE) | (1L << CHAR) | (1L << DOUBLE) | (1L << FLOAT) | (1L << INT) | (1L << LONG) | (1L << NEW) | (1L << SHORT) | (1L << SUPER) | (1L << THIS) | (1L << VOID) | (1L << IntegerLiteral) | (1L << FloatingPointLiteral) | (1L << BooleanLiteral) | (1L << CharacterLiteral) | (1L << StringLiteral) | (1L << NullLiteral) | (1L << LPAREN))) != 0) || ((((_la - 68)) & ~0x3f) == 0 && ((1L << (_la - 68)) & ((1L << (LT - 68)) | (1L << (BANG - 68)) | (1L << (TILDE - 68)) | (1L << (INC - 68)) | (1L << (DEC - 68)) | (1L << (ADD - 68)) | (1L << (SUB - 68)) | (1L << (Identifier - 68)))) != 0)) {
            {
              setState(954);
              expression(0);
            }
          }

          setState(957);
          match(SEMI);
        }
        break;
        case 12:
          enterOuterAlt(_localctx, 12);
        {
          setState(958);
          match(THROW);
          setState(959);
          expression(0);
          setState(960);
          match(SEMI);
        }
        break;
        case 13:
          enterOuterAlt(_localctx, 13);
        {
          setState(962);
          match(BREAK);
          setState(964);
          _errHandler.sync(this);
          _la = _input.LA(1);
          if (_la == Identifier) {
            {
              setState(963);
              match(Identifier);
            }
          }

          setState(966);
          match(SEMI);
        }
        break;
        case 14:
          enterOuterAlt(_localctx, 14);
        {
          setState(967);
          match(CONTINUE);
          setState(969);
          _errHandler.sync(this);
          _la = _input.LA(1);
          if (_la == Identifier) {
            {
              setState(968);
              match(Identifier);
            }
          }

          setState(971);
          match(SEMI);
        }
        break;
        case 15:
          enterOuterAlt(_localctx, 15);
        {
          setState(972);
          match(SEMI);
        }
        break;
        case 16:
          enterOuterAlt(_localctx, 16);
        {
          setState(973);
          statementExpression();
          setState(974);
          match(SEMI);
        }
        break;
        case 17:
          enterOuterAlt(_localctx, 17);
        {
          setState(976);
          match(Identifier);
          setState(977);
          match(COLON);
          setState(978);
          statement();
        }
        break;
      }
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
  }

  public static class CatchesContext extends IncrementalParserRuleContext {
    public List<? extends CatchClauseContext> catchClause() {
      return getRuleContexts(CatchClauseContext.class);
    }

    public CatchClauseContext catchClause(int i) {
      return getRuleContext(CatchClauseContext.class, i);
    }

    public CatchesContext(ParserRuleContext parent, int invokingState) {
      super((IncrementalParserRuleContext) parent, invokingState);
    }

    @Override
    public int getRuleIndex() {
      return RULE_catches;
    }
  }

  @RuleVersion(0)
  public final CatchesContext catches() throws RecognitionException {
    // Check whether we need to execute this rule.
    CatchesContext guardResult = (CatchesContext) guardRule((IncrementalParserRuleContext) _ctx, getState(), RULE_catches);
    // If we found an existing context that is valid, return it.
    if (guardResult != null) {
      this._input.seek(guardResult.stop.getTokenIndex() + 1);
      return guardResult;
    }
    CatchesContext _localctx = new CatchesContext(_ctx, getState());
    enterRule(_localctx, 174, RULE_catches);
    int _la;
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(982);
        _errHandler.sync(this);
        do {
          {
            {
              setState(981);
              catchClause();
            }
          }
          setState(984);
          _errHandler.sync(this);
          _la = _input.LA(1);
        } while (_la == CATCH);
      }
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
  }

  public static class CatchClauseContext extends IncrementalParserRuleContext {
    public TerminalNode CATCH() {
      return getToken(TestIncrementalJavaParser.CATCH, 0);
    }

    public TerminalNode LPAREN() {
      return getToken(TestIncrementalJavaParser.LPAREN, 0);
    }

    public VariableModifiersContext variableModifiers() {
      return getRuleContext(VariableModifiersContext.class, 0);
    }

    public CatchTypeContext catchType() {
      return getRuleContext(CatchTypeContext.class, 0);
    }

    public TerminalNode Identifier() {
      return getToken(TestIncrementalJavaParser.Identifier, 0);
    }

    public TerminalNode RPAREN() {
      return getToken(TestIncrementalJavaParser.RPAREN, 0);
    }

    public BlockContext block() {
      return getRuleContext(BlockContext.class, 0);
    }

    public CatchClauseContext(ParserRuleContext parent, int invokingState) {
      super((IncrementalParserRuleContext) parent, invokingState);
    }

    @Override
    public int getRuleIndex() {
      return RULE_catchClause;
    }
  }

  @RuleVersion(0)
  public final CatchClauseContext catchClause() throws RecognitionException {
    // Check whether we need to execute this rule.
    CatchClauseContext guardResult = (CatchClauseContext) guardRule((IncrementalParserRuleContext) _ctx, getState(), RULE_catchClause);
    // If we found an existing context that is valid, return it.
    if (guardResult != null) {
      this._input.seek(guardResult.stop.getTokenIndex() + 1);
      return guardResult;
    }
    CatchClauseContext _localctx = new CatchClauseContext(_ctx, getState());
    enterRule(_localctx, 176, RULE_catchClause);
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(986);
        match(CATCH);
        setState(987);
        match(LPAREN);
        setState(988);
        variableModifiers();
        setState(989);
        catchType();
        setState(990);
        match(Identifier);
        setState(991);
        match(RPAREN);
        setState(992);
        block();
      }
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
  }

  public static class CatchTypeContext extends IncrementalParserRuleContext {
    public List<? extends QualifiedNameContext> qualifiedName() {
      return getRuleContexts(QualifiedNameContext.class);
    }

    public QualifiedNameContext qualifiedName(int i) {
      return getRuleContext(QualifiedNameContext.class, i);
    }

    public List<? extends TerminalNode> BITOR() {
      return getTokens(TestIncrementalJavaParser.BITOR);
    }

    public TerminalNode BITOR(int i) {
      return getToken(TestIncrementalJavaParser.BITOR, i);
    }

    public CatchTypeContext(ParserRuleContext parent, int invokingState) {
      super((IncrementalParserRuleContext) parent, invokingState);
    }

    @Override
    public int getRuleIndex() {
      return RULE_catchType;
    }
  }

  @RuleVersion(0)
  public final CatchTypeContext catchType() throws RecognitionException {
    // Check whether we need to execute this rule.
    CatchTypeContext guardResult = (CatchTypeContext) guardRule((IncrementalParserRuleContext) _ctx, getState(), RULE_catchType);
    // If we found an existing context that is valid, return it.
    if (guardResult != null) {
      this._input.seek(guardResult.stop.getTokenIndex() + 1);
      return guardResult;
    }
    CatchTypeContext _localctx = new CatchTypeContext(_ctx, getState());
    enterRule(_localctx, 178, RULE_catchType);
    int _la;
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(994);
        qualifiedName();
        setState(999);
        _errHandler.sync(this);
        _la = _input.LA(1);
        while (_la == BITOR) {
          {
            {
              setState(995);
              match(BITOR);
              setState(996);
              qualifiedName();
            }
          }
          setState(1001);
          _errHandler.sync(this);
          _la = _input.LA(1);
        }
      }
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
  }

  public static class FinallyBlockContext extends IncrementalParserRuleContext {
    public TerminalNode FINALLY() {
      return getToken(TestIncrementalJavaParser.FINALLY, 0);
    }

    public BlockContext block() {
      return getRuleContext(BlockContext.class, 0);
    }

    public FinallyBlockContext(ParserRuleContext parent, int invokingState) {
      super((IncrementalParserRuleContext) parent, invokingState);
    }

    @Override
    public int getRuleIndex() {
      return RULE_finallyBlock;
    }
  }

  @RuleVersion(0)
  public final FinallyBlockContext finallyBlock() throws RecognitionException {
    // Check whether we need to execute this rule.
    FinallyBlockContext guardResult = (FinallyBlockContext) guardRule((IncrementalParserRuleContext) _ctx, getState(), RULE_finallyBlock);
    // If we found an existing context that is valid, return it.
    if (guardResult != null) {
      this._input.seek(guardResult.stop.getTokenIndex() + 1);
      return guardResult;
    }
    FinallyBlockContext _localctx = new FinallyBlockContext(_ctx, getState());
    enterRule(_localctx, 180, RULE_finallyBlock);
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(1002);
        match(FINALLY);
        setState(1003);
        block();
      }
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
  }

  public static class ResourceSpecificationContext extends IncrementalParserRuleContext {
    public TerminalNode LPAREN() {
      return getToken(TestIncrementalJavaParser.LPAREN, 0);
    }

    public ResourcesContext resources() {
      return getRuleContext(ResourcesContext.class, 0);
    }

    public TerminalNode RPAREN() {
      return getToken(TestIncrementalJavaParser.RPAREN, 0);
    }

    public TerminalNode SEMI() {
      return getToken(TestIncrementalJavaParser.SEMI, 0);
    }

    public ResourceSpecificationContext(ParserRuleContext parent, int invokingState) {
      super((IncrementalParserRuleContext) parent, invokingState);
    }

    @Override
    public int getRuleIndex() {
      return RULE_resourceSpecification;
    }
  }

  @RuleVersion(0)
  public final ResourceSpecificationContext resourceSpecification() throws RecognitionException {
    // Check whether we need to execute this rule.
    ResourceSpecificationContext guardResult = (ResourceSpecificationContext) guardRule((IncrementalParserRuleContext) _ctx, getState(), RULE_resourceSpecification);
    // If we found an existing context that is valid, return it.
    if (guardResult != null) {
      this._input.seek(guardResult.stop.getTokenIndex() + 1);
      return guardResult;
    }
    ResourceSpecificationContext _localctx = new ResourceSpecificationContext(_ctx, getState());
    enterRule(_localctx, 182, RULE_resourceSpecification);
    int _la;
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(1005);
        match(LPAREN);
        setState(1006);
        resources();
        setState(1008);
        _errHandler.sync(this);
        _la = _input.LA(1);
        if (_la == SEMI) {
          {
            setState(1007);
            match(SEMI);
          }
        }

        setState(1010);
        match(RPAREN);
      }
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
  }

  public static class ResourcesContext extends IncrementalParserRuleContext {
    public List<? extends ResourceContext> resource() {
      return getRuleContexts(ResourceContext.class);
    }

    public ResourceContext resource(int i) {
      return getRuleContext(ResourceContext.class, i);
    }

    public List<? extends TerminalNode> SEMI() {
      return getTokens(TestIncrementalJavaParser.SEMI);
    }

    public TerminalNode SEMI(int i) {
      return getToken(TestIncrementalJavaParser.SEMI, i);
    }

    public ResourcesContext(ParserRuleContext parent, int invokingState) {
      super((IncrementalParserRuleContext) parent, invokingState);
    }

    @Override
    public int getRuleIndex() {
      return RULE_resources;
    }
  }

  @RuleVersion(0)
  public final ResourcesContext resources() throws RecognitionException {
    // Check whether we need to execute this rule.
    ResourcesContext guardResult = (ResourcesContext) guardRule((IncrementalParserRuleContext) _ctx, getState(), RULE_resources);
    // If we found an existing context that is valid, return it.
    if (guardResult != null) {
      this._input.seek(guardResult.stop.getTokenIndex() + 1);
      return guardResult;
    }
    ResourcesContext _localctx = new ResourcesContext(_ctx, getState());
    enterRule(_localctx, 184, RULE_resources);
    try {
      int _alt;
      enterOuterAlt(_localctx, 1);
      {
        setState(1012);
        resource();
        setState(1017);
        _errHandler.sync(this);
        _alt = getInterpreter().adaptivePredict(_input, 115, _ctx);
        while (_alt != 2 && _alt != ATN.INVALID_ALT_NUMBER) {
          if (_alt == 1) {
            {
              {
                setState(1013);
                match(SEMI);
                setState(1014);
                resource();
              }
            }
          }
          setState(1019);
          _errHandler.sync(this);
          _alt = getInterpreter().adaptivePredict(_input, 115, _ctx);
        }
      }
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
  }

  public static class ResourceContext extends IncrementalParserRuleContext {
    public VariableModifiersContext variableModifiers() {
      return getRuleContext(VariableModifiersContext.class, 0);
    }

    public ClassOrInterfaceTypeContext classOrInterfaceType() {
      return getRuleContext(ClassOrInterfaceTypeContext.class, 0);
    }

    public VariableDeclaratorIdContext variableDeclaratorId() {
      return getRuleContext(VariableDeclaratorIdContext.class, 0);
    }

    public TerminalNode ASSIGN() {
      return getToken(TestIncrementalJavaParser.ASSIGN, 0);
    }

    public ExpressionContext expression() {
      return getRuleContext(ExpressionContext.class, 0);
    }

    public ResourceContext(ParserRuleContext parent, int invokingState) {
      super((IncrementalParserRuleContext) parent, invokingState);
    }

    @Override
    public int getRuleIndex() {
      return RULE_resource;
    }
  }

  @RuleVersion(0)
  public final ResourceContext resource() throws RecognitionException {
    // Check whether we need to execute this rule.
    ResourceContext guardResult = (ResourceContext) guardRule((IncrementalParserRuleContext) _ctx, getState(), RULE_resource);
    // If we found an existing context that is valid, return it.
    if (guardResult != null) {
      this._input.seek(guardResult.stop.getTokenIndex() + 1);
      return guardResult;
    }
    ResourceContext _localctx = new ResourceContext(_ctx, getState());
    enterRule(_localctx, 186, RULE_resource);
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(1020);
        variableModifiers();
        setState(1021);
        classOrInterfaceType();
        setState(1022);
        variableDeclaratorId();
        setState(1023);
        match(ASSIGN);
        setState(1024);
        expression(0);
      }
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
  }

  public static class FormalParameterContext extends IncrementalParserRuleContext {
    public VariableModifiersContext variableModifiers() {
      return getRuleContext(VariableModifiersContext.class, 0);
    }

    public TypeContext type() {
      return getRuleContext(TypeContext.class, 0);
    }

    public VariableDeclaratorIdContext variableDeclaratorId() {
      return getRuleContext(VariableDeclaratorIdContext.class, 0);
    }

    public FormalParameterContext(ParserRuleContext parent, int invokingState) {
      super((IncrementalParserRuleContext) parent, invokingState);
    }

    @Override
    public int getRuleIndex() {
      return RULE_formalParameter;
    }
  }

  @RuleVersion(0)
  public final FormalParameterContext formalParameter() throws RecognitionException {
    // Check whether we need to execute this rule.
    FormalParameterContext guardResult = (FormalParameterContext) guardRule((IncrementalParserRuleContext) _ctx, getState(), RULE_formalParameter);
    // If we found an existing context that is valid, return it.
    if (guardResult != null) {
      this._input.seek(guardResult.stop.getTokenIndex() + 1);
      return guardResult;
    }
    FormalParameterContext _localctx = new FormalParameterContext(_ctx, getState());
    enterRule(_localctx, 188, RULE_formalParameter);
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(1026);
        variableModifiers();
        setState(1027);
        type();
        setState(1028);
        variableDeclaratorId();
      }
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
  }

  public static class SwitchBlockStatementGroupsContext extends IncrementalParserRuleContext {
    public List<? extends SwitchBlockStatementGroupContext> switchBlockStatementGroup() {
      return getRuleContexts(SwitchBlockStatementGroupContext.class);
    }

    public SwitchBlockStatementGroupContext switchBlockStatementGroup(int i) {
      return getRuleContext(SwitchBlockStatementGroupContext.class, i);
    }

    public SwitchBlockStatementGroupsContext(ParserRuleContext parent, int invokingState) {
      super((IncrementalParserRuleContext) parent, invokingState);
    }

    @Override
    public int getRuleIndex() {
      return RULE_switchBlockStatementGroups;
    }
  }

  @RuleVersion(0)
  public final SwitchBlockStatementGroupsContext switchBlockStatementGroups() throws RecognitionException {
    // Check whether we need to execute this rule.
    SwitchBlockStatementGroupsContext guardResult = (SwitchBlockStatementGroupsContext) guardRule((IncrementalParserRuleContext) _ctx, getState(), RULE_switchBlockStatementGroups);
    // If we found an existing context that is valid, return it.
    if (guardResult != null) {
      this._input.seek(guardResult.stop.getTokenIndex() + 1);
      return guardResult;
    }
    SwitchBlockStatementGroupsContext _localctx = new SwitchBlockStatementGroupsContext(_ctx, getState());
    enterRule(_localctx, 190, RULE_switchBlockStatementGroups);
    int _la;
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(1033);
        _errHandler.sync(this);
        _la = _input.LA(1);
        while (_la == CASE || _la == DEFAULT) {
          {
            {
              setState(1030);
              switchBlockStatementGroup();
            }
          }
          setState(1035);
          _errHandler.sync(this);
          _la = _input.LA(1);
        }
      }
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
  }

  public static class SwitchBlockStatementGroupContext extends IncrementalParserRuleContext {
    public List<? extends SwitchLabelContext> switchLabel() {
      return getRuleContexts(SwitchLabelContext.class);
    }

    public SwitchLabelContext switchLabel(int i) {
      return getRuleContext(SwitchLabelContext.class, i);
    }

    public List<? extends BlockStatementContext> blockStatement() {
      return getRuleContexts(BlockStatementContext.class);
    }

    public BlockStatementContext blockStatement(int i) {
      return getRuleContext(BlockStatementContext.class, i);
    }

    public SwitchBlockStatementGroupContext(ParserRuleContext parent, int invokingState) {
      super((IncrementalParserRuleContext) parent, invokingState);
    }

    @Override
    public int getRuleIndex() {
      return RULE_switchBlockStatementGroup;
    }
  }

  @RuleVersion(0)
  public final SwitchBlockStatementGroupContext switchBlockStatementGroup() throws RecognitionException {
    // Check whether we need to execute this rule.
    SwitchBlockStatementGroupContext guardResult = (SwitchBlockStatementGroupContext) guardRule((IncrementalParserRuleContext) _ctx, getState(), RULE_switchBlockStatementGroup);
    // If we found an existing context that is valid, return it.
    if (guardResult != null) {
      this._input.seek(guardResult.stop.getTokenIndex() + 1);
      return guardResult;
    }
    SwitchBlockStatementGroupContext _localctx = new SwitchBlockStatementGroupContext(_ctx, getState());
    enterRule(_localctx, 192, RULE_switchBlockStatementGroup);
    int _la;
    try {
      int _alt;
      enterOuterAlt(_localctx, 1);
      {
        setState(1037);
        _errHandler.sync(this);
        _alt = 1;
        do {
          switch (_alt) {
            case 1: {
              {
                setState(1036);
                switchLabel();
              }
            }
            break;
            default:
              throw new NoViableAltException(this);
          }
          setState(1039);
          _errHandler.sync(this);
          _alt = getInterpreter().adaptivePredict(_input, 117, _ctx);
        } while (_alt != 2 && _alt != ATN.INVALID_ALT_NUMBER);
        setState(1044);
        _errHandler.sync(this);
        _la = _input.LA(1);
        while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << ABSTRACT) | (1L << ASSERT) | (1L << BOOLEAN) | (1L << BREAK) | (1L << BYTE) | (1L << CHAR) | (1L << CLASS) | (1L << CONTINUE) | (1L << DO) | (1L << DOUBLE) | (1L << ENUM) | (1L << FINAL) | (1L << FLOAT) | (1L << FOR) | (1L << IF) | (1L << INT) | (1L << INTERFACE) | (1L << LONG) | (1L << NEW) | (1L << PRIVATE) | (1L << PROTECTED) | (1L << PUBLIC) | (1L << RETURN) | (1L << SHORT) | (1L << STATIC) | (1L << STRICTFP) | (1L << SUPER) | (1L << SWITCH) | (1L << SYNCHRONIZED) | (1L << THIS) | (1L << THROW) | (1L << TRY) | (1L << VOID) | (1L << WHILE) | (1L << IntegerLiteral) | (1L << FloatingPointLiteral) | (1L << BooleanLiteral) | (1L << CharacterLiteral) | (1L << StringLiteral) | (1L << NullLiteral) | (1L << LPAREN) | (1L << LBRACE) | (1L << SEMI))) != 0) || ((((_la - 68)) & ~0x3f) == 0 && ((1L << (_la - 68)) & ((1L << (LT - 68)) | (1L << (BANG - 68)) | (1L << (TILDE - 68)) | (1L << (INC - 68)) | (1L << (DEC - 68)) | (1L << (ADD - 68)) | (1L << (SUB - 68)) | (1L << (Identifier - 68)) | (1L << (AT - 68)))) != 0)) {
          {
            {
              setState(1041);
              blockStatement();
            }
          }
          setState(1046);
          _errHandler.sync(this);
          _la = _input.LA(1);
        }
      }
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
  }

  public static class SwitchLabelContext extends IncrementalParserRuleContext {
    public TerminalNode CASE() {
      return getToken(TestIncrementalJavaParser.CASE, 0);
    }

    public ConstantExpressionContext constantExpression() {
      return getRuleContext(ConstantExpressionContext.class, 0);
    }

    public TerminalNode COLON() {
      return getToken(TestIncrementalJavaParser.COLON, 0);
    }

    public EnumConstantNameContext enumConstantName() {
      return getRuleContext(EnumConstantNameContext.class, 0);
    }

    public TerminalNode DEFAULT() {
      return getToken(TestIncrementalJavaParser.DEFAULT, 0);
    }

    public SwitchLabelContext(ParserRuleContext parent, int invokingState) {
      super((IncrementalParserRuleContext) parent, invokingState);
    }

    @Override
    public int getRuleIndex() {
      return RULE_switchLabel;
    }
  }

  @RuleVersion(0)
  public final SwitchLabelContext switchLabel() throws RecognitionException {
    // Check whether we need to execute this rule.
    SwitchLabelContext guardResult = (SwitchLabelContext) guardRule((IncrementalParserRuleContext) _ctx, getState(), RULE_switchLabel);
    // If we found an existing context that is valid, return it.
    if (guardResult != null) {
      this._input.seek(guardResult.stop.getTokenIndex() + 1);
      return guardResult;
    }
    SwitchLabelContext _localctx = new SwitchLabelContext(_ctx, getState());
    enterRule(_localctx, 194, RULE_switchLabel);
    try {
      setState(1057);
      _errHandler.sync(this);
      switch (getInterpreter().adaptivePredict(_input, 119, _ctx)) {
        case 1:
          enterOuterAlt(_localctx, 1);
        {
          setState(1047);
          match(CASE);
          setState(1048);
          constantExpression();
          setState(1049);
          match(COLON);
        }
        break;
        case 2:
          enterOuterAlt(_localctx, 2);
        {
          setState(1051);
          match(CASE);
          setState(1052);
          enumConstantName();
          setState(1053);
          match(COLON);
        }
        break;
        case 3:
          enterOuterAlt(_localctx, 3);
        {
          setState(1055);
          match(DEFAULT);
          setState(1056);
          match(COLON);
        }
        break;
      }
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
  }

  public static class ForControlContext extends IncrementalParserRuleContext {
    public EnhancedForControlContext enhancedForControl() {
      return getRuleContext(EnhancedForControlContext.class, 0);
    }

    public List<? extends TerminalNode> SEMI() {
      return getTokens(TestIncrementalJavaParser.SEMI);
    }

    public TerminalNode SEMI(int i) {
      return getToken(TestIncrementalJavaParser.SEMI, i);
    }

    public ForInitContext forInit() {
      return getRuleContext(ForInitContext.class, 0);
    }

    public ExpressionContext expression() {
      return getRuleContext(ExpressionContext.class, 0);
    }

    public ForUpdateContext forUpdate() {
      return getRuleContext(ForUpdateContext.class, 0);
    }

    public ForControlContext(ParserRuleContext parent, int invokingState) {
      super((IncrementalParserRuleContext) parent, invokingState);
    }

    @Override
    public int getRuleIndex() {
      return RULE_forControl;
    }
  }

  @RuleVersion(0)
  public final ForControlContext forControl() throws RecognitionException {
    // Check whether we need to execute this rule.
    ForControlContext guardResult = (ForControlContext) guardRule((IncrementalParserRuleContext) _ctx, getState(), RULE_forControl);
    // If we found an existing context that is valid, return it.
    if (guardResult != null) {
      this._input.seek(guardResult.stop.getTokenIndex() + 1);
      return guardResult;
    }
    ForControlContext _localctx = new ForControlContext(_ctx, getState());
    enterRule(_localctx, 196, RULE_forControl);
    int _la;
    try {
      setState(1071);
      _errHandler.sync(this);
      switch (getInterpreter().adaptivePredict(_input, 123, _ctx)) {
        case 1:
          enterOuterAlt(_localctx, 1);
        {
          setState(1059);
          enhancedForControl();
        }
        break;
        case 2:
          enterOuterAlt(_localctx, 2);
        {
          setState(1061);
          _errHandler.sync(this);
          _la = _input.LA(1);
          if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << BOOLEAN) | (1L << BYTE) | (1L << CHAR) | (1L << DOUBLE) | (1L << FINAL) | (1L << FLOAT) | (1L << INT) | (1L << LONG) | (1L << NEW) | (1L << SHORT) | (1L << SUPER) | (1L << THIS) | (1L << VOID) | (1L << IntegerLiteral) | (1L << FloatingPointLiteral) | (1L << BooleanLiteral) | (1L << CharacterLiteral) | (1L << StringLiteral) | (1L << NullLiteral) | (1L << LPAREN))) != 0) || ((((_la - 68)) & ~0x3f) == 0 && ((1L << (_la - 68)) & ((1L << (LT - 68)) | (1L << (BANG - 68)) | (1L << (TILDE - 68)) | (1L << (INC - 68)) | (1L << (DEC - 68)) | (1L << (ADD - 68)) | (1L << (SUB - 68)) | (1L << (Identifier - 68)) | (1L << (AT - 68)))) != 0)) {
            {
              setState(1060);
              forInit();
            }
          }

          setState(1063);
          match(SEMI);
          setState(1065);
          _errHandler.sync(this);
          _la = _input.LA(1);
          if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << BOOLEAN) | (1L << BYTE) | (1L << CHAR) | (1L << DOUBLE) | (1L << FLOAT) | (1L << INT) | (1L << LONG) | (1L << NEW) | (1L << SHORT) | (1L << SUPER) | (1L << THIS) | (1L << VOID) | (1L << IntegerLiteral) | (1L << FloatingPointLiteral) | (1L << BooleanLiteral) | (1L << CharacterLiteral) | (1L << StringLiteral) | (1L << NullLiteral) | (1L << LPAREN))) != 0) || ((((_la - 68)) & ~0x3f) == 0 && ((1L << (_la - 68)) & ((1L << (LT - 68)) | (1L << (BANG - 68)) | (1L << (TILDE - 68)) | (1L << (INC - 68)) | (1L << (DEC - 68)) | (1L << (ADD - 68)) | (1L << (SUB - 68)) | (1L << (Identifier - 68)))) != 0)) {
            {
              setState(1064);
              expression(0);
            }
          }

          setState(1067);
          match(SEMI);
          setState(1069);
          _errHandler.sync(this);
          _la = _input.LA(1);
          if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << BOOLEAN) | (1L << BYTE) | (1L << CHAR) | (1L << DOUBLE) | (1L << FLOAT) | (1L << INT) | (1L << LONG) | (1L << NEW) | (1L << SHORT) | (1L << SUPER) | (1L << THIS) | (1L << VOID) | (1L << IntegerLiteral) | (1L << FloatingPointLiteral) | (1L << BooleanLiteral) | (1L << CharacterLiteral) | (1L << StringLiteral) | (1L << NullLiteral) | (1L << LPAREN))) != 0) || ((((_la - 68)) & ~0x3f) == 0 && ((1L << (_la - 68)) & ((1L << (LT - 68)) | (1L << (BANG - 68)) | (1L << (TILDE - 68)) | (1L << (INC - 68)) | (1L << (DEC - 68)) | (1L << (ADD - 68)) | (1L << (SUB - 68)) | (1L << (Identifier - 68)))) != 0)) {
            {
              setState(1068);
              forUpdate();
            }
          }

        }
        break;
      }
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
  }

  public static class ForInitContext extends IncrementalParserRuleContext {
    public LocalVariableDeclarationContext localVariableDeclaration() {
      return getRuleContext(LocalVariableDeclarationContext.class, 0);
    }

    public ExpressionListContext expressionList() {
      return getRuleContext(ExpressionListContext.class, 0);
    }

    public ForInitContext(ParserRuleContext parent, int invokingState) {
      super((IncrementalParserRuleContext) parent, invokingState);
    }

    @Override
    public int getRuleIndex() {
      return RULE_forInit;
    }
  }

  @RuleVersion(0)
  public final ForInitContext forInit() throws RecognitionException {
    // Check whether we need to execute this rule.
    ForInitContext guardResult = (ForInitContext) guardRule((IncrementalParserRuleContext) _ctx, getState(), RULE_forInit);
    // If we found an existing context that is valid, return it.
    if (guardResult != null) {
      this._input.seek(guardResult.stop.getTokenIndex() + 1);
      return guardResult;
    }
    ForInitContext _localctx = new ForInitContext(_ctx, getState());
    enterRule(_localctx, 198, RULE_forInit);
    try {
      setState(1075);
      _errHandler.sync(this);
      switch (getInterpreter().adaptivePredict(_input, 124, _ctx)) {
        case 1:
          enterOuterAlt(_localctx, 1);
        {
          setState(1073);
          localVariableDeclaration();
        }
        break;
        case 2:
          enterOuterAlt(_localctx, 2);
        {
          setState(1074);
          expressionList();
        }
        break;
      }
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
  }

  public static class EnhancedForControlContext extends IncrementalParserRuleContext {
    public VariableModifiersContext variableModifiers() {
      return getRuleContext(VariableModifiersContext.class, 0);
    }

    public TypeContext type() {
      return getRuleContext(TypeContext.class, 0);
    }

    public TerminalNode Identifier() {
      return getToken(TestIncrementalJavaParser.Identifier, 0);
    }

    public TerminalNode COLON() {
      return getToken(TestIncrementalJavaParser.COLON, 0);
    }

    public ExpressionContext expression() {
      return getRuleContext(ExpressionContext.class, 0);
    }

    public EnhancedForControlContext(ParserRuleContext parent, int invokingState) {
      super((IncrementalParserRuleContext) parent, invokingState);
    }

    @Override
    public int getRuleIndex() {
      return RULE_enhancedForControl;
    }

  }

  @RuleVersion(0)
  public final EnhancedForControlContext enhancedForControl() throws RecognitionException {
    // Check whether we need to execute this rule.
    EnhancedForControlContext guardResult = (EnhancedForControlContext) guardRule((IncrementalParserRuleContext) _ctx, getState(), RULE_enhancedForControl);
    // If we found an existing context that is valid, return it.
    if (guardResult != null) {
      this._input.seek(guardResult.stop.getTokenIndex() + 1);
      return guardResult;
    }
    EnhancedForControlContext _localctx = new EnhancedForControlContext(_ctx, getState());
    enterRule(_localctx, 200, RULE_enhancedForControl);
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(1077);
        variableModifiers();
        setState(1078);
        type();
        setState(1079);
        match(Identifier);
        setState(1080);
        match(COLON);
        setState(1081);
        expression(0);
      }
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
  }

  public static class ForUpdateContext extends IncrementalParserRuleContext {
    public ExpressionListContext expressionList() {
      return getRuleContext(ExpressionListContext.class, 0);
    }

    public ForUpdateContext(ParserRuleContext parent, int invokingState) {
      super((IncrementalParserRuleContext) parent, invokingState);
    }

    @Override
    public int getRuleIndex() {
      return RULE_forUpdate;
    }
  }

  @RuleVersion(0)
  public final ForUpdateContext forUpdate() throws RecognitionException {
    // Check whether we need to execute this rule.
    ForUpdateContext guardResult = (ForUpdateContext) guardRule((IncrementalParserRuleContext) _ctx, getState(), RULE_forUpdate);
    // If we found an existing context that is valid, return it.
    if (guardResult != null) {
      this._input.seek(guardResult.stop.getTokenIndex() + 1);
      return guardResult;
    }
    ForUpdateContext _localctx = new ForUpdateContext(_ctx, getState());
    enterRule(_localctx, 202, RULE_forUpdate);
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(1083);
        expressionList();
      }
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
  }

  public static class ParExpressionContext extends IncrementalParserRuleContext {
    public TerminalNode LPAREN() {
      return getToken(TestIncrementalJavaParser.LPAREN, 0);
    }

    public ExpressionContext expression() {
      return getRuleContext(ExpressionContext.class, 0);
    }

    public TerminalNode RPAREN() {
      return getToken(TestIncrementalJavaParser.RPAREN, 0);
    }

    public ParExpressionContext(ParserRuleContext parent, int invokingState) {
      super((IncrementalParserRuleContext) parent, invokingState);
    }

    @Override
    public int getRuleIndex() {
      return RULE_parExpression;
    }
  }

  @RuleVersion(0)
  public final ParExpressionContext parExpression() throws RecognitionException {
    // Check whether we need to execute this rule.
    ParExpressionContext guardResult = (ParExpressionContext) guardRule((IncrementalParserRuleContext) _ctx, getState(), RULE_parExpression);
    // If we found an existing context that is valid, return it.
    if (guardResult != null) {
      this._input.seek(guardResult.stop.getTokenIndex() + 1);
      return guardResult;
    }
    ParExpressionContext _localctx = new ParExpressionContext(_ctx, getState());
    enterRule(_localctx, 204, RULE_parExpression);
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(1085);
        match(LPAREN);
        setState(1086);
        expression(0);
        setState(1087);
        match(RPAREN);
      }
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
  }

  public static class ExpressionListContext extends IncrementalParserRuleContext {
    public List<? extends ExpressionContext> expression() {
      return getRuleContexts(ExpressionContext.class);
    }

    public ExpressionContext expression(int i) {
      return getRuleContext(ExpressionContext.class, i);
    }

    public List<? extends TerminalNode> COMMA() {
      return getTokens(TestIncrementalJavaParser.COMMA);
    }

    public TerminalNode COMMA(int i) {
      return getToken(TestIncrementalJavaParser.COMMA, i);
    }

    public ExpressionListContext(ParserRuleContext parent, int invokingState) {
      super((IncrementalParserRuleContext) parent, invokingState);
    }

    @Override
    public int getRuleIndex() {
      return RULE_expressionList;
    }
  }

  @RuleVersion(0)
  public final ExpressionListContext expressionList() throws RecognitionException {
    // Check whether we need to execute this rule.
    ExpressionListContext guardResult = (ExpressionListContext) guardRule((IncrementalParserRuleContext) _ctx, getState(), RULE_expressionList);
    // If we found an existing context that is valid, return it.
    if (guardResult != null) {
      this._input.seek(guardResult.stop.getTokenIndex() + 1);
      return guardResult;
    }
    ExpressionListContext _localctx = new ExpressionListContext(_ctx, getState());
    enterRule(_localctx, 206, RULE_expressionList);
    int _la;
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(1089);
        expression(0);
        setState(1094);
        _errHandler.sync(this);
        _la = _input.LA(1);
        while (_la == COMMA) {
          {
            {
              setState(1090);
              match(COMMA);
              setState(1091);
              expression(0);
            }
          }
          setState(1096);
          _errHandler.sync(this);
          _la = _input.LA(1);
        }
      }
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
  }

  public static class StatementExpressionContext extends IncrementalParserRuleContext {
    public ExpressionContext expression() {
      return getRuleContext(ExpressionContext.class, 0);
    }

    public StatementExpressionContext(ParserRuleContext parent, int invokingState) {
      super((IncrementalParserRuleContext) parent, invokingState);
    }

    @Override
    public int getRuleIndex() {
      return RULE_statementExpression;
    }
  }

  @RuleVersion(0)
  public final StatementExpressionContext statementExpression() throws RecognitionException {
    // Check whether we need to execute this rule.
    StatementExpressionContext guardResult = (StatementExpressionContext) guardRule((IncrementalParserRuleContext) _ctx, getState(), RULE_statementExpression);
    // If we found an existing context that is valid, return it.
    if (guardResult != null) {
      this._input.seek(guardResult.stop.getTokenIndex() + 1);
      return guardResult;
    }
    StatementExpressionContext _localctx = new StatementExpressionContext(_ctx, getState());
    enterRule(_localctx, 208, RULE_statementExpression);
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(1097);
        expression(0);
      }
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
  }

  public static class ConstantExpressionContext extends IncrementalParserRuleContext {
    public ExpressionContext expression() {
      return getRuleContext(ExpressionContext.class, 0);
    }

    public ConstantExpressionContext(ParserRuleContext parent, int invokingState) {
      super((IncrementalParserRuleContext) parent, invokingState);
    }

    @Override
    public int getRuleIndex() {
      return RULE_constantExpression;
    }
  }

  @RuleVersion(0)
  public final ConstantExpressionContext constantExpression() throws RecognitionException {
    // Check whether we need to execute this rule.
    ConstantExpressionContext guardResult = (ConstantExpressionContext) guardRule((IncrementalParserRuleContext) _ctx, getState(), RULE_constantExpression);
    // If we found an existing context that is valid, return it.
    if (guardResult != null) {
      this._input.seek(guardResult.stop.getTokenIndex() + 1);
      return guardResult;
    }
    ConstantExpressionContext _localctx = new ConstantExpressionContext(_ctx, getState());
    enterRule(_localctx, 210, RULE_constantExpression);
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(1099);
        expression(0);
      }
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
  }

  public static class ExpressionContext extends IncrementalParserRuleContext {
    public PrimaryContext primary() {
      return getRuleContext(PrimaryContext.class, 0);
    }

    public List<? extends ExpressionContext> expression() {
      return getRuleContexts(ExpressionContext.class);
    }

    public ExpressionContext expression(int i) {
      return getRuleContext(ExpressionContext.class, i);
    }

    public TerminalNode DOT() {
      return getToken(TestIncrementalJavaParser.DOT, 0);
    }

    public TerminalNode Identifier() {
      return getToken(TestIncrementalJavaParser.Identifier, 0);
    }

    public TerminalNode THIS() {
      return getToken(TestIncrementalJavaParser.THIS, 0);
    }

    public TerminalNode NEW() {
      return getToken(TestIncrementalJavaParser.NEW, 0);
    }

    public InnerCreatorContext innerCreator() {
      return getRuleContext(InnerCreatorContext.class, 0);
    }

    public NonWildcardTypeArgumentsContext nonWildcardTypeArguments() {
      return getRuleContext(NonWildcardTypeArgumentsContext.class, 0);
    }

    public TerminalNode SUPER() {
      return getToken(TestIncrementalJavaParser.SUPER, 0);
    }

    public SuperSuffixContext superSuffix() {
      return getRuleContext(SuperSuffixContext.class, 0);
    }

    public ExplicitGenericInvocationContext explicitGenericInvocation() {
      return getRuleContext(ExplicitGenericInvocationContext.class, 0);
    }

    public CreatorContext creator() {
      return getRuleContext(CreatorContext.class, 0);
    }

    public TerminalNode LBRACK() {
      return getToken(TestIncrementalJavaParser.LBRACK, 0);
    }

    public TerminalNode RBRACK() {
      return getToken(TestIncrementalJavaParser.RBRACK, 0);
    }

    public TerminalNode LPAREN() {
      return getToken(TestIncrementalJavaParser.LPAREN, 0);
    }

    public TypeContext type() {
      return getRuleContext(TypeContext.class, 0);
    }

    public TerminalNode RPAREN() {
      return getToken(TestIncrementalJavaParser.RPAREN, 0);
    }

    public TerminalNode INC() {
      return getToken(TestIncrementalJavaParser.INC, 0);
    }

    public TerminalNode DEC() {
      return getToken(TestIncrementalJavaParser.DEC, 0);
    }

    public ExpressionListContext expressionList() {
      return getRuleContext(ExpressionListContext.class, 0);
    }

    public TerminalNode ADD() {
      return getToken(TestIncrementalJavaParser.ADD, 0);
    }

    public TerminalNode SUB() {
      return getToken(TestIncrementalJavaParser.SUB, 0);
    }

    public TerminalNode TILDE() {
      return getToken(TestIncrementalJavaParser.TILDE, 0);
    }

    public TerminalNode BANG() {
      return getToken(TestIncrementalJavaParser.BANG, 0);
    }

    public TerminalNode MUL() {
      return getToken(TestIncrementalJavaParser.MUL, 0);
    }

    public TerminalNode DIV() {
      return getToken(TestIncrementalJavaParser.DIV, 0);
    }

    public TerminalNode MOD() {
      return getToken(TestIncrementalJavaParser.MOD, 0);
    }

    public List<? extends TerminalNode> LT() {
      return getTokens(TestIncrementalJavaParser.LT);
    }

    public TerminalNode LT(int i) {
      return getToken(TestIncrementalJavaParser.LT, i);
    }

    public List<? extends TerminalNode> GT() {
      return getTokens(TestIncrementalJavaParser.GT);
    }

    public TerminalNode GT(int i) {
      return getToken(TestIncrementalJavaParser.GT, i);
    }

    public TerminalNode LE() {
      return getToken(TestIncrementalJavaParser.LE, 0);
    }

    public TerminalNode GE() {
      return getToken(TestIncrementalJavaParser.GE, 0);
    }

    public TerminalNode INSTANCEOF() {
      return getToken(TestIncrementalJavaParser.INSTANCEOF, 0);
    }

    public TerminalNode EQUAL() {
      return getToken(TestIncrementalJavaParser.EQUAL, 0);
    }

    public TerminalNode NOTEQUAL() {
      return getToken(TestIncrementalJavaParser.NOTEQUAL, 0);
    }

    public TerminalNode BITAND() {
      return getToken(TestIncrementalJavaParser.BITAND, 0);
    }

    public TerminalNode CARET() {
      return getToken(TestIncrementalJavaParser.CARET, 0);
    }

    public TerminalNode BITOR() {
      return getToken(TestIncrementalJavaParser.BITOR, 0);
    }

    public TerminalNode AND() {
      return getToken(TestIncrementalJavaParser.AND, 0);
    }

    public TerminalNode OR() {
      return getToken(TestIncrementalJavaParser.OR, 0);
    }

    public TerminalNode QUESTION() {
      return getToken(TestIncrementalJavaParser.QUESTION, 0);
    }

    public TerminalNode COLON() {
      return getToken(TestIncrementalJavaParser.COLON, 0);
    }

    public TerminalNode ASSIGN() {
      return getToken(TestIncrementalJavaParser.ASSIGN, 0);
    }

    public TerminalNode ADD_ASSIGN() {
      return getToken(TestIncrementalJavaParser.ADD_ASSIGN, 0);
    }

    public TerminalNode SUB_ASSIGN() {
      return getToken(TestIncrementalJavaParser.SUB_ASSIGN, 0);
    }

    public TerminalNode MUL_ASSIGN() {
      return getToken(TestIncrementalJavaParser.MUL_ASSIGN, 0);
    }

    public TerminalNode DIV_ASSIGN() {
      return getToken(TestIncrementalJavaParser.DIV_ASSIGN, 0);
    }

    public TerminalNode AND_ASSIGN() {
      return getToken(TestIncrementalJavaParser.AND_ASSIGN, 0);
    }

    public TerminalNode OR_ASSIGN() {
      return getToken(TestIncrementalJavaParser.OR_ASSIGN, 0);
    }

    public TerminalNode XOR_ASSIGN() {
      return getToken(TestIncrementalJavaParser.XOR_ASSIGN, 0);
    }

    public TerminalNode RSHIFT_ASSIGN() {
      return getToken(TestIncrementalJavaParser.RSHIFT_ASSIGN, 0);
    }

    public TerminalNode URSHIFT_ASSIGN() {
      return getToken(TestIncrementalJavaParser.URSHIFT_ASSIGN, 0);
    }

    public TerminalNode LSHIFT_ASSIGN() {
      return getToken(TestIncrementalJavaParser.LSHIFT_ASSIGN, 0);
    }

    public TerminalNode MOD_ASSIGN() {
      return getToken(TestIncrementalJavaParser.MOD_ASSIGN, 0);
    }

    public ExpressionContext(ParserRuleContext parent, int invokingState) {
      super((IncrementalParserRuleContext) parent, invokingState);
    }

    @Override
    public int getRuleIndex() {
      return RULE_expression;
    }
  }

  @RuleVersion(0)
  public final ExpressionContext expression() throws RecognitionException {
    return expression(0);
  }

  private ExpressionContext expression(int _p) throws RecognitionException {
    ParserRuleContext _parentctx = _ctx;
    int _parentState = getState();
    // Check whether we need to execute this rule.
    ExpressionContext guardResult = (ExpressionContext) guardRule((IncrementalParserRuleContext) _ctx, getState(), RULE_expression);
    // If we found an existing context that is valid, return it.
    if (guardResult != null) {
      this._input.seek(guardResult.stop.getTokenIndex() + 1);
      return guardResult;
    }
    ExpressionContext _localctx = new ExpressionContext(_ctx, _parentState);
    ExpressionContext _prevctx = _localctx;
    int _startState = 212;
    enterRecursionRule(_localctx, 212, RULE_expression, _p);
    int _la;
    try {
      int _alt;
      enterOuterAlt(_localctx, 1);
      {
        setState(1114);
        _errHandler.sync(this);
        switch (getInterpreter().adaptivePredict(_input, 126, _ctx)) {
          case 1: {
            setState(1102);
            primary();
          }
          break;
          case 2: {
            setState(1103);
            match(NEW);
            setState(1104);
            creator();
          }
          break;
          case 3: {
            setState(1105);
            match(LPAREN);
            setState(1106);
            type();
            setState(1107);
            match(RPAREN);
            setState(1108);
            expression(18);
          }
          break;
          case 4: {
            setState(1110);
            _la = _input.LA(1);
            if (!(((((_la - 79)) & ~0x3f) == 0 && ((1L << (_la - 79)) & ((1L << (INC - 79)) | (1L << (DEC - 79)) | (1L << (ADD - 79)) | (1L << (SUB - 79)))) != 0))) {
              _errHandler.recoverInline(this);
            } else {
              if (_input.LA(1) == Token.EOF) {
                matchedEOF = true;
              }

              _errHandler.reportMatch(this);
              consume();
            }
            setState(1111);
            expression(15);
          }
          break;
          case 5: {
            setState(1112);
            _la = _input.LA(1);
            if (!(_la == BANG || _la == TILDE)) {
              _errHandler.recoverInline(this);
            } else {
              if (_input.LA(1) == Token.EOF) {
                matchedEOF = true;
              }

              _errHandler.reportMatch(this);
              consume();
            }
            setState(1113);
            expression(14);
          }
          break;
        }
        _ctx.stop = _input.LT(-1);
        setState(1201);
        _errHandler.sync(this);
        _alt = getInterpreter().adaptivePredict(_input, 131, _ctx);
        while (_alt != 2 && _alt != ATN.INVALID_ALT_NUMBER) {
          if (_alt == 1) {
            if (_parseListeners != null) triggerExitRuleEvent();
            _prevctx = _localctx;
            {
              setState(1199);
              _errHandler.sync(this);
              switch (getInterpreter().adaptivePredict(_input, 130, _ctx)) {
                case 1: {
                  _localctx = new ExpressionContext(_parentctx, _parentState);
                  pushNewRecursionContext(_localctx, _startState, RULE_expression);
                  setState(1116);
                  if (!(precpred(_ctx, 13))) throw createFailedPredicateException("precpred(_ctx, 13)");
                  setState(1117);
                  _la = _input.LA(1);
                  if (!(((((_la - 83)) & ~0x3f) == 0 && ((1L << (_la - 83)) & ((1L << (MUL - 83)) | (1L << (DIV - 83)) | (1L << (MOD - 83)))) != 0))) {
                    _errHandler.recoverInline(this);
                  } else {
                    if (_input.LA(1) == Token.EOF) {
                      matchedEOF = true;
                    }

                    _errHandler.reportMatch(this);
                    consume();
                  }
                  setState(1118);
                  expression(14);
                }
                break;
                case 2: {
                  _localctx = new ExpressionContext(_parentctx, _parentState);
                  pushNewRecursionContext(_localctx, _startState, RULE_expression);
                  setState(1119);
                  if (!(precpred(_ctx, 12))) throw createFailedPredicateException("precpred(_ctx, 12)");
                  setState(1120);
                  _la = _input.LA(1);
                  if (!(_la == ADD || _la == SUB)) {
                    _errHandler.recoverInline(this);
                  } else {
                    if (_input.LA(1) == Token.EOF) {
                      matchedEOF = true;
                    }

                    _errHandler.reportMatch(this);
                    consume();
                  }
                  setState(1121);
                  expression(13);
                }
                break;
                case 3: {
                  _localctx = new ExpressionContext(_parentctx, _parentState);
                  pushNewRecursionContext(_localctx, _startState, RULE_expression);
                  setState(1122);
                  if (!(precpred(_ctx, 11))) throw createFailedPredicateException("precpred(_ctx, 11)");
                  setState(1130);
                  _errHandler.sync(this);
                  switch (getInterpreter().adaptivePredict(_input, 127, _ctx)) {
                    case 1: {
                      setState(1123);
                      match(LT);
                      setState(1124);
                      match(LT);
                    }
                    break;
                    case 2: {
                      setState(1125);
                      match(GT);
                      setState(1126);
                      match(GT);
                      setState(1127);
                      match(GT);
                    }
                    break;
                    case 3: {
                      setState(1128);
                      match(GT);
                      setState(1129);
                      match(GT);
                    }
                    break;
                  }
                  setState(1132);
                  expression(12);
                }
                break;
                case 4: {
                  _localctx = new ExpressionContext(_parentctx, _parentState);
                  pushNewRecursionContext(_localctx, _startState, RULE_expression);
                  setState(1133);
                  if (!(precpred(_ctx, 10))) throw createFailedPredicateException("precpred(_ctx, 10)");
                  setState(1134);
                  _la = _input.LA(1);
                  if (!(((((_la - 67)) & ~0x3f) == 0 && ((1L << (_la - 67)) & ((1L << (GT - 67)) | (1L << (LT - 67)) | (1L << (LE - 67)) | (1L << (GE - 67)))) != 0))) {
                    _errHandler.recoverInline(this);
                  } else {
                    if (_input.LA(1) == Token.EOF) {
                      matchedEOF = true;
                    }

                    _errHandler.reportMatch(this);
                    consume();
                  }
                  setState(1135);
                  expression(11);
                }
                break;
                case 5: {
                  _localctx = new ExpressionContext(_parentctx, _parentState);
                  pushNewRecursionContext(_localctx, _startState, RULE_expression);
                  setState(1136);
                  if (!(precpred(_ctx, 8))) throw createFailedPredicateException("precpred(_ctx, 8)");
                  setState(1137);
                  _la = _input.LA(1);
                  if (!(_la == EQUAL || _la == NOTEQUAL)) {
                    _errHandler.recoverInline(this);
                  } else {
                    if (_input.LA(1) == Token.EOF) {
                      matchedEOF = true;
                    }

                    _errHandler.reportMatch(this);
                    consume();
                  }
                  setState(1138);
                  expression(9);
                }
                break;
                case 6: {
                  _localctx = new ExpressionContext(_parentctx, _parentState);
                  pushNewRecursionContext(_localctx, _startState, RULE_expression);
                  setState(1139);
                  if (!(precpred(_ctx, 7))) throw createFailedPredicateException("precpred(_ctx, 7)");
                  setState(1140);
                  match(BITAND);
                  setState(1141);
                  expression(8);
                }
                break;
                case 7: {
                  _localctx = new ExpressionContext(_parentctx, _parentState);
                  pushNewRecursionContext(_localctx, _startState, RULE_expression);
                  setState(1142);
                  if (!(precpred(_ctx, 6))) throw createFailedPredicateException("precpred(_ctx, 6)");
                  setState(1143);
                  match(CARET);
                  setState(1144);
                  expression(7);
                }
                break;
                case 8: {
                  _localctx = new ExpressionContext(_parentctx, _parentState);
                  pushNewRecursionContext(_localctx, _startState, RULE_expression);
                  setState(1145);
                  if (!(precpred(_ctx, 5))) throw createFailedPredicateException("precpred(_ctx, 5)");
                  setState(1146);
                  match(BITOR);
                  setState(1147);
                  expression(6);
                }
                break;
                case 9: {
                  _localctx = new ExpressionContext(_parentctx, _parentState);
                  pushNewRecursionContext(_localctx, _startState, RULE_expression);
                  setState(1148);
                  if (!(precpred(_ctx, 4))) throw createFailedPredicateException("precpred(_ctx, 4)");
                  setState(1149);
                  match(AND);
                  setState(1150);
                  expression(5);
                }
                break;
                case 10: {
                  _localctx = new ExpressionContext(_parentctx, _parentState);
                  pushNewRecursionContext(_localctx, _startState, RULE_expression);
                  setState(1151);
                  if (!(precpred(_ctx, 3))) throw createFailedPredicateException("precpred(_ctx, 3)");
                  setState(1152);
                  match(OR);
                  setState(1153);
                  expression(4);
                }
                break;
                case 11: {
                  _localctx = new ExpressionContext(_parentctx, _parentState);
                  pushNewRecursionContext(_localctx, _startState, RULE_expression);
                  setState(1154);
                  if (!(precpred(_ctx, 2))) throw createFailedPredicateException("precpred(_ctx, 2)");
                  setState(1155);
                  match(QUESTION);
                  setState(1156);
                  expression(0);
                  setState(1157);
                  match(COLON);
                  setState(1158);
                  expression(3);
                }
                break;
                case 12: {
                  _localctx = new ExpressionContext(_parentctx, _parentState);
                  pushNewRecursionContext(_localctx, _startState, RULE_expression);
                  setState(1160);
                  if (!(precpred(_ctx, 1))) throw createFailedPredicateException("precpred(_ctx, 1)");
                  setState(1161);
                  _la = _input.LA(1);
                  if (!(((((_la - 66)) & ~0x3f) == 0 && ((1L << (_la - 66)) & ((1L << (ASSIGN - 66)) | (1L << (ADD_ASSIGN - 66)) | (1L << (SUB_ASSIGN - 66)) | (1L << (MUL_ASSIGN - 66)) | (1L << (DIV_ASSIGN - 66)) | (1L << (AND_ASSIGN - 66)) | (1L << (OR_ASSIGN - 66)) | (1L << (XOR_ASSIGN - 66)) | (1L << (MOD_ASSIGN - 66)) | (1L << (LSHIFT_ASSIGN - 66)) | (1L << (RSHIFT_ASSIGN - 66)) | (1L << (URSHIFT_ASSIGN - 66)))) != 0))) {
                    _errHandler.recoverInline(this);
                  } else {
                    if (_input.LA(1) == Token.EOF) {
                      matchedEOF = true;
                    }

                    _errHandler.reportMatch(this);
                    consume();
                  }
                  setState(1162);
                  expression(1);
                }
                break;
                case 13: {
                  _localctx = new ExpressionContext(_parentctx, _parentState);
                  pushNewRecursionContext(_localctx, _startState, RULE_expression);
                  setState(1163);
                  if (!(precpred(_ctx, 25))) throw createFailedPredicateException("precpred(_ctx, 25)");
                  setState(1164);
                  match(DOT);
                  setState(1165);
                  match(Identifier);
                }
                break;
                case 14: {
                  _localctx = new ExpressionContext(_parentctx, _parentState);
                  pushNewRecursionContext(_localctx, _startState, RULE_expression);
                  setState(1166);
                  if (!(precpred(_ctx, 24))) throw createFailedPredicateException("precpred(_ctx, 24)");
                  setState(1167);
                  match(DOT);
                  setState(1168);
                  match(THIS);
                }
                break;
                case 15: {
                  _localctx = new ExpressionContext(_parentctx, _parentState);
                  pushNewRecursionContext(_localctx, _startState, RULE_expression);
                  setState(1169);
                  if (!(precpred(_ctx, 23))) throw createFailedPredicateException("precpred(_ctx, 23)");
                  setState(1170);
                  match(DOT);
                  setState(1171);
                  match(NEW);
                  setState(1173);
                  _errHandler.sync(this);
                  _la = _input.LA(1);
                  if (_la == LT) {
                    {
                      setState(1172);
                      nonWildcardTypeArguments();
                    }
                  }

                  setState(1175);
                  innerCreator();
                }
                break;
                case 16: {
                  _localctx = new ExpressionContext(_parentctx, _parentState);
                  pushNewRecursionContext(_localctx, _startState, RULE_expression);
                  setState(1176);
                  if (!(precpred(_ctx, 22))) throw createFailedPredicateException("precpred(_ctx, 22)");
                  setState(1177);
                  match(DOT);
                  setState(1178);
                  match(SUPER);
                  setState(1179);
                  superSuffix();
                }
                break;
                case 17: {
                  _localctx = new ExpressionContext(_parentctx, _parentState);
                  pushNewRecursionContext(_localctx, _startState, RULE_expression);
                  setState(1180);
                  if (!(precpred(_ctx, 21))) throw createFailedPredicateException("precpred(_ctx, 21)");
                  setState(1181);
                  match(DOT);
                  setState(1182);
                  explicitGenericInvocation();
                }
                break;
                case 18: {
                  _localctx = new ExpressionContext(_parentctx, _parentState);
                  pushNewRecursionContext(_localctx, _startState, RULE_expression);
                  setState(1183);
                  if (!(precpred(_ctx, 19))) throw createFailedPredicateException("precpred(_ctx, 19)");
                  setState(1184);
                  match(LBRACK);
                  setState(1185);
                  expression(0);
                  setState(1186);
                  match(RBRACK);
                }
                break;
                case 19: {
                  _localctx = new ExpressionContext(_parentctx, _parentState);
                  pushNewRecursionContext(_localctx, _startState, RULE_expression);
                  setState(1188);
                  if (!(precpred(_ctx, 17))) throw createFailedPredicateException("precpred(_ctx, 17)");
                  setState(1189);
                  _la = _input.LA(1);
                  if (!(_la == INC || _la == DEC)) {
                    _errHandler.recoverInline(this);
                  } else {
                    if (_input.LA(1) == Token.EOF) {
                      matchedEOF = true;
                    }

                    _errHandler.reportMatch(this);
                    consume();
                  }
                }
                break;
                case 20: {
                  _localctx = new ExpressionContext(_parentctx, _parentState);
                  pushNewRecursionContext(_localctx, _startState, RULE_expression);
                  setState(1190);
                  if (!(precpred(_ctx, 16))) throw createFailedPredicateException("precpred(_ctx, 16)");
                  setState(1191);
                  match(LPAREN);
                  setState(1193);
                  _errHandler.sync(this);
                  _la = _input.LA(1);
                  if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << BOOLEAN) | (1L << BYTE) | (1L << CHAR) | (1L << DOUBLE) | (1L << FLOAT) | (1L << INT) | (1L << LONG) | (1L << NEW) | (1L << SHORT) | (1L << SUPER) | (1L << THIS) | (1L << VOID) | (1L << IntegerLiteral) | (1L << FloatingPointLiteral) | (1L << BooleanLiteral) | (1L << CharacterLiteral) | (1L << StringLiteral) | (1L << NullLiteral) | (1L << LPAREN))) != 0) || ((((_la - 68)) & ~0x3f) == 0 && ((1L << (_la - 68)) & ((1L << (LT - 68)) | (1L << (BANG - 68)) | (1L << (TILDE - 68)) | (1L << (INC - 68)) | (1L << (DEC - 68)) | (1L << (ADD - 68)) | (1L << (SUB - 68)) | (1L << (Identifier - 68)))) != 0)) {
                    {
                      setState(1192);
                      expressionList();
                    }
                  }

                  setState(1195);
                  match(RPAREN);
                }
                break;
                case 21: {
                  _localctx = new ExpressionContext(_parentctx, _parentState);
                  pushNewRecursionContext(_localctx, _startState, RULE_expression);
                  setState(1196);
                  if (!(precpred(_ctx, 9))) throw createFailedPredicateException("precpred(_ctx, 9)");
                  setState(1197);
                  match(INSTANCEOF);
                  setState(1198);
                  type();
                }
                break;
              }
            }
          }
          setState(1203);
          _errHandler.sync(this);
          _alt = getInterpreter().adaptivePredict(_input, 131, _ctx);
        }
      }
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      unrollRecursionContexts(_parentctx);
    }
    return _localctx;
  }

  public static class PrimaryContext extends IncrementalParserRuleContext {
    public TerminalNode LPAREN() {
      return getToken(TestIncrementalJavaParser.LPAREN, 0);
    }

    public ExpressionContext expression() {
      return getRuleContext(ExpressionContext.class, 0);
    }

    public TerminalNode RPAREN() {
      return getToken(TestIncrementalJavaParser.RPAREN, 0);
    }

    public TerminalNode THIS() {
      return getToken(TestIncrementalJavaParser.THIS, 0);
    }

    public TerminalNode SUPER() {
      return getToken(TestIncrementalJavaParser.SUPER, 0);
    }

    public LiteralContext literal() {
      return getRuleContext(LiteralContext.class, 0);
    }

    public TerminalNode Identifier() {
      return getToken(TestIncrementalJavaParser.Identifier, 0);
    }

    public TypeContext type() {
      return getRuleContext(TypeContext.class, 0);
    }

    public TerminalNode DOT() {
      return getToken(TestIncrementalJavaParser.DOT, 0);
    }

    public TerminalNode CLASS() {
      return getToken(TestIncrementalJavaParser.CLASS, 0);
    }

    public TerminalNode VOID() {
      return getToken(TestIncrementalJavaParser.VOID, 0);
    }

    public NonWildcardTypeArgumentsContext nonWildcardTypeArguments() {
      return getRuleContext(NonWildcardTypeArgumentsContext.class, 0);
    }

    public ExplicitGenericInvocationSuffixContext explicitGenericInvocationSuffix() {
      return getRuleContext(ExplicitGenericInvocationSuffixContext.class, 0);
    }

    public ArgumentsContext arguments() {
      return getRuleContext(ArgumentsContext.class, 0);
    }

    public PrimaryContext(ParserRuleContext parent, int invokingState) {
      super((IncrementalParserRuleContext) parent, invokingState);
    }

    @Override
    public int getRuleIndex() {
      return RULE_primary;
    }
  }

  @RuleVersion(0)
  public final PrimaryContext primary() throws RecognitionException {
    // Check whether we need to execute this rule.
    PrimaryContext guardResult = (PrimaryContext) guardRule((IncrementalParserRuleContext) _ctx, getState(), RULE_primary);
    // If we found an existing context that is valid, return it.
    if (guardResult != null) {
      this._input.seek(guardResult.stop.getTokenIndex() + 1);
      return guardResult;
    }
    PrimaryContext _localctx = new PrimaryContext(_ctx, getState());
    enterRule(_localctx, 214, RULE_primary);
    try {
      setState(1225);
      _errHandler.sync(this);
      switch (getInterpreter().adaptivePredict(_input, 133, _ctx)) {
        case 1:
          enterOuterAlt(_localctx, 1);
        {
          setState(1204);
          match(LPAREN);
          setState(1205);
          expression(0);
          setState(1206);
          match(RPAREN);
        }
        break;
        case 2:
          enterOuterAlt(_localctx, 2);
        {
          setState(1208);
          match(THIS);
        }
        break;
        case 3:
          enterOuterAlt(_localctx, 3);
        {
          setState(1209);
          match(SUPER);
        }
        break;
        case 4:
          enterOuterAlt(_localctx, 4);
        {
          setState(1210);
          literal();
        }
        break;
        case 5:
          enterOuterAlt(_localctx, 5);
        {
          setState(1211);
          match(Identifier);
        }
        break;
        case 6:
          enterOuterAlt(_localctx, 6);
        {
          setState(1212);
          type();
          setState(1213);
          match(DOT);
          setState(1214);
          match(CLASS);
        }
        break;
        case 7:
          enterOuterAlt(_localctx, 7);
        {
          setState(1216);
          match(VOID);
          setState(1217);
          match(DOT);
          setState(1218);
          match(CLASS);
        }
        break;
        case 8:
          enterOuterAlt(_localctx, 8);
        {
          setState(1219);
          nonWildcardTypeArguments();
          setState(1223);
          _errHandler.sync(this);
          switch (_input.LA(1)) {
            case SUPER:
            case Identifier: {
              setState(1220);
              explicitGenericInvocationSuffix();
            }
            break;
            case THIS: {
              setState(1221);
              match(THIS);
              setState(1222);
              arguments();
            }
            break;
            default:
              throw new NoViableAltException(this);
          }
        }
        break;
      }
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
  }

  public static class CreatorContext extends IncrementalParserRuleContext {
    public NonWildcardTypeArgumentsContext nonWildcardTypeArguments() {
      return getRuleContext(NonWildcardTypeArgumentsContext.class, 0);
    }

    public CreatedNameContext createdName() {
      return getRuleContext(CreatedNameContext.class, 0);
    }

    public ClassCreatorRestContext classCreatorRest() {
      return getRuleContext(ClassCreatorRestContext.class, 0);
    }

    public ArrayCreatorRestContext arrayCreatorRest() {
      return getRuleContext(ArrayCreatorRestContext.class, 0);
    }

    public CreatorContext(ParserRuleContext parent, int invokingState) {
      super((IncrementalParserRuleContext) parent, invokingState);
    }

    @Override
    public int getRuleIndex() {
      return RULE_creator;
    }
  }

  @RuleVersion(0)
  public final CreatorContext creator() throws RecognitionException {
    // Check whether we need to execute this rule.
    CreatorContext guardResult = (CreatorContext) guardRule((IncrementalParserRuleContext) _ctx, getState(), RULE_creator);
    // If we found an existing context that is valid, return it.
    if (guardResult != null) {
      this._input.seek(guardResult.stop.getTokenIndex() + 1);
      return guardResult;
    }
    CreatorContext _localctx = new CreatorContext(_ctx, getState());
    enterRule(_localctx, 216, RULE_creator);
    try {
      setState(1236);
      _errHandler.sync(this);
      switch (_input.LA(1)) {
        case LT:
          enterOuterAlt(_localctx, 1);
        {
          setState(1227);
          nonWildcardTypeArguments();
          setState(1228);
          createdName();
          setState(1229);
          classCreatorRest();
        }
        break;
        case BOOLEAN:
        case BYTE:
        case CHAR:
        case DOUBLE:
        case FLOAT:
        case INT:
        case LONG:
        case SHORT:
        case Identifier:
          enterOuterAlt(_localctx, 2);
        {
          setState(1231);
          createdName();
          setState(1234);
          _errHandler.sync(this);
          switch (_input.LA(1)) {
            case LBRACK: {
              setState(1232);
              arrayCreatorRest();
            }
            break;
            case LPAREN: {
              setState(1233);
              classCreatorRest();
            }
            break;
            default:
              throw new NoViableAltException(this);
          }
        }
        break;
        default:
          throw new NoViableAltException(this);
      }
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
  }

  public static class CreatedNameContext extends IncrementalParserRuleContext {
    public List<? extends TerminalNode> Identifier() {
      return getTokens(TestIncrementalJavaParser.Identifier);
    }

    public TerminalNode Identifier(int i) {
      return getToken(TestIncrementalJavaParser.Identifier, i);
    }

    public List<? extends TypeArgumentsOrDiamondContext> typeArgumentsOrDiamond() {
      return getRuleContexts(TypeArgumentsOrDiamondContext.class);
    }

    public TypeArgumentsOrDiamondContext typeArgumentsOrDiamond(int i) {
      return getRuleContext(TypeArgumentsOrDiamondContext.class, i);
    }

    public List<? extends TerminalNode> DOT() {
      return getTokens(TestIncrementalJavaParser.DOT);
    }

    public TerminalNode DOT(int i) {
      return getToken(TestIncrementalJavaParser.DOT, i);
    }

    public PrimitiveTypeContext primitiveType() {
      return getRuleContext(PrimitiveTypeContext.class, 0);
    }

    public CreatedNameContext(ParserRuleContext parent, int invokingState) {
      super((IncrementalParserRuleContext) parent, invokingState);
    }

    @Override
    public int getRuleIndex() {
      return RULE_createdName;
    }

  }

  @RuleVersion(0)
  public final CreatedNameContext createdName() throws RecognitionException {
    // Check whether we need to execute this rule.
    CreatedNameContext guardResult = (CreatedNameContext) guardRule((IncrementalParserRuleContext) _ctx, getState(), RULE_createdName);
    // If we found an existing context that is valid, return it.
    if (guardResult != null) {
      this._input.seek(guardResult.stop.getTokenIndex() + 1);
      return guardResult;
    }
    CreatedNameContext _localctx = new CreatedNameContext(_ctx, getState());
    enterRule(_localctx, 218, RULE_createdName);
    int _la;
    try {
      setState(1253);
      _errHandler.sync(this);
      switch (_input.LA(1)) {
        case Identifier:
          enterOuterAlt(_localctx, 1);
        {
          setState(1238);
          match(Identifier);
          setState(1240);
          _errHandler.sync(this);
          _la = _input.LA(1);
          if (_la == LT) {
            {
              setState(1239);
              typeArgumentsOrDiamond();
            }
          }

          setState(1249);
          _errHandler.sync(this);
          _la = _input.LA(1);
          while (_la == DOT) {
            {
              {
                setState(1242);
                match(DOT);
                setState(1243);
                match(Identifier);
                setState(1245);
                _errHandler.sync(this);
                _la = _input.LA(1);
                if (_la == LT) {
                  {
                    setState(1244);
                    typeArgumentsOrDiamond();
                  }
                }

              }
            }
            setState(1251);
            _errHandler.sync(this);
            _la = _input.LA(1);
          }
        }
        break;
        case BOOLEAN:
        case BYTE:
        case CHAR:
        case DOUBLE:
        case FLOAT:
        case INT:
        case LONG:
        case SHORT:
          enterOuterAlt(_localctx, 2);
        {
          setState(1252);
          primitiveType();
        }
        break;
        default:
          throw new NoViableAltException(this);
      }
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
  }

  public static class InnerCreatorContext extends IncrementalParserRuleContext {
    public TerminalNode Identifier() {
      return getToken(TestIncrementalJavaParser.Identifier, 0);
    }

    public ClassCreatorRestContext classCreatorRest() {
      return getRuleContext(ClassCreatorRestContext.class, 0);
    }

    public NonWildcardTypeArgumentsOrDiamondContext nonWildcardTypeArgumentsOrDiamond() {
      return getRuleContext(NonWildcardTypeArgumentsOrDiamondContext.class, 0);
    }

    public InnerCreatorContext(ParserRuleContext parent, int invokingState) {
      super((IncrementalParserRuleContext) parent, invokingState);
    }

    @Override
    public int getRuleIndex() {
      return RULE_innerCreator;
    }
  }

  @RuleVersion(0)
  public final InnerCreatorContext innerCreator() throws RecognitionException {
    // Check whether we need to execute this rule.
    InnerCreatorContext guardResult = (InnerCreatorContext) guardRule((IncrementalParserRuleContext) _ctx, getState(), RULE_innerCreator);
    // If we found an existing context that is valid, return it.
    if (guardResult != null) {
      this._input.seek(guardResult.stop.getTokenIndex() + 1);
      return guardResult;
    }
    InnerCreatorContext _localctx = new InnerCreatorContext(_ctx, getState());
    enterRule(_localctx, 220, RULE_innerCreator);
    int _la;
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(1255);
        match(Identifier);
        setState(1257);
        _errHandler.sync(this);
        _la = _input.LA(1);
        if (_la == LT) {
          {
            setState(1256);
            nonWildcardTypeArgumentsOrDiamond();
          }
        }

        setState(1259);
        classCreatorRest();
      }
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
  }

  public static class ArrayCreatorRestContext extends IncrementalParserRuleContext {
    public List<? extends TerminalNode> LBRACK() {
      return getTokens(TestIncrementalJavaParser.LBRACK);
    }

    public TerminalNode LBRACK(int i) {
      return getToken(TestIncrementalJavaParser.LBRACK, i);
    }

    public List<? extends TerminalNode> RBRACK() {
      return getTokens(TestIncrementalJavaParser.RBRACK);
    }

    public TerminalNode RBRACK(int i) {
      return getToken(TestIncrementalJavaParser.RBRACK, i);
    }

    public ArrayInitializerContext arrayInitializer() {
      return getRuleContext(ArrayInitializerContext.class, 0);
    }

    public List<? extends ExpressionContext> expression() {
      return getRuleContexts(ExpressionContext.class);
    }

    public ExpressionContext expression(int i) {
      return getRuleContext(ExpressionContext.class, i);
    }

    public ArrayCreatorRestContext(ParserRuleContext parent, int invokingState) {
      super((IncrementalParserRuleContext) parent, invokingState);
    }

    @Override
    public int getRuleIndex() {
      return RULE_arrayCreatorRest;
    }
  }

  @RuleVersion(0)
  public final ArrayCreatorRestContext arrayCreatorRest() throws RecognitionException {
    // Check whether we need to execute this rule.
    ArrayCreatorRestContext guardResult = (ArrayCreatorRestContext) guardRule((IncrementalParserRuleContext) _ctx, getState(), RULE_arrayCreatorRest);
    // If we found an existing context that is valid, return it.
    if (guardResult != null) {
      this._input.seek(guardResult.stop.getTokenIndex() + 1);
      return guardResult;
    }
    ArrayCreatorRestContext _localctx = new ArrayCreatorRestContext(_ctx, getState());
    enterRule(_localctx, 222, RULE_arrayCreatorRest);
    int _la;
    try {
      int _alt;
      enterOuterAlt(_localctx, 1);
      {
        setState(1261);
        match(LBRACK);
        setState(1289);
        _errHandler.sync(this);
        switch (_input.LA(1)) {
          case RBRACK: {
            setState(1262);
            match(RBRACK);
            setState(1267);
            _errHandler.sync(this);
            _la = _input.LA(1);
            while (_la == LBRACK) {
              {
                {
                  setState(1263);
                  match(LBRACK);
                  setState(1264);
                  match(RBRACK);
                }
              }
              setState(1269);
              _errHandler.sync(this);
              _la = _input.LA(1);
            }
            setState(1270);
            arrayInitializer();
          }
          break;
          case BOOLEAN:
          case BYTE:
          case CHAR:
          case DOUBLE:
          case FLOAT:
          case INT:
          case LONG:
          case NEW:
          case SHORT:
          case SUPER:
          case THIS:
          case VOID:
          case IntegerLiteral:
          case FloatingPointLiteral:
          case BooleanLiteral:
          case CharacterLiteral:
          case StringLiteral:
          case NullLiteral:
          case LPAREN:
          case LT:
          case BANG:
          case TILDE:
          case INC:
          case DEC:
          case ADD:
          case SUB:
          case Identifier: {
            setState(1271);
            expression(0);
            setState(1272);
            match(RBRACK);
            setState(1279);
            _errHandler.sync(this);
            _alt = getInterpreter().adaptivePredict(_input, 142, _ctx);
            while (_alt != 2 && _alt != ATN.INVALID_ALT_NUMBER) {
              if (_alt == 1) {
                {
                  {
                    setState(1273);
                    match(LBRACK);
                    setState(1274);
                    expression(0);
                    setState(1275);
                    match(RBRACK);
                  }
                }
              }
              setState(1281);
              _errHandler.sync(this);
              _alt = getInterpreter().adaptivePredict(_input, 142, _ctx);
            }
            setState(1286);
            _errHandler.sync(this);
            _alt = getInterpreter().adaptivePredict(_input, 143, _ctx);
            while (_alt != 2 && _alt != ATN.INVALID_ALT_NUMBER) {
              if (_alt == 1) {
                {
                  {
                    setState(1282);
                    match(LBRACK);
                    setState(1283);
                    match(RBRACK);
                  }
                }
              }
              setState(1288);
              _errHandler.sync(this);
              _alt = getInterpreter().adaptivePredict(_input, 143, _ctx);
            }
          }
          break;
          default:
            throw new NoViableAltException(this);
        }
      }
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
  }

  public static class ClassCreatorRestContext extends IncrementalParserRuleContext {
    public ArgumentsContext arguments() {
      return getRuleContext(ArgumentsContext.class, 0);
    }

    public ClassBodyContext classBody() {
      return getRuleContext(ClassBodyContext.class, 0);
    }

    public ClassCreatorRestContext(ParserRuleContext parent, int invokingState) {
      super((IncrementalParserRuleContext) parent, invokingState);
    }

    @Override
    public int getRuleIndex() {
      return RULE_classCreatorRest;
    }
  }

  @RuleVersion(0)
  public final ClassCreatorRestContext classCreatorRest() throws RecognitionException {
    // Check whether we need to execute this rule.
    ClassCreatorRestContext guardResult = (ClassCreatorRestContext) guardRule((IncrementalParserRuleContext) _ctx, getState(), RULE_classCreatorRest);
    // If we found an existing context that is valid, return it.
    if (guardResult != null) {
      this._input.seek(guardResult.stop.getTokenIndex() + 1);
      return guardResult;
    }
    ClassCreatorRestContext _localctx = new ClassCreatorRestContext(_ctx, getState());
    enterRule(_localctx, 224, RULE_classCreatorRest);
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(1291);
        arguments();
        setState(1293);
        _errHandler.sync(this);
        switch (getInterpreter().adaptivePredict(_input, 145, _ctx)) {
          case 1: {
            setState(1292);
            classBody();
          }
          break;
        }
      }
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
  }

  public static class ExplicitGenericInvocationContext extends IncrementalParserRuleContext {
    public NonWildcardTypeArgumentsContext nonWildcardTypeArguments() {
      return getRuleContext(NonWildcardTypeArgumentsContext.class, 0);
    }

    public ExplicitGenericInvocationSuffixContext explicitGenericInvocationSuffix() {
      return getRuleContext(ExplicitGenericInvocationSuffixContext.class, 0);
    }

    public ExplicitGenericInvocationContext(ParserRuleContext parent, int invokingState) {
      super((IncrementalParserRuleContext) parent, invokingState);
    }

    @Override
    public int getRuleIndex() {
      return RULE_explicitGenericInvocation;
    }
  }

  @RuleVersion(0)
  public final ExplicitGenericInvocationContext explicitGenericInvocation() throws RecognitionException {
    // Check whether we need to execute this rule.
    ExplicitGenericInvocationContext guardResult = (ExplicitGenericInvocationContext) guardRule((IncrementalParserRuleContext) _ctx, getState(), RULE_explicitGenericInvocation);
    // If we found an existing context that is valid, return it.
    if (guardResult != null) {
      this._input.seek(guardResult.stop.getTokenIndex() + 1);
      return guardResult;
    }
    ExplicitGenericInvocationContext _localctx = new ExplicitGenericInvocationContext(_ctx, getState());
    enterRule(_localctx, 226, RULE_explicitGenericInvocation);
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(1295);
        nonWildcardTypeArguments();
        setState(1296);
        explicitGenericInvocationSuffix();
      }
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
  }

  public static class NonWildcardTypeArgumentsContext extends IncrementalParserRuleContext {
    public TerminalNode LT() {
      return getToken(TestIncrementalJavaParser.LT, 0);
    }

    public TypeListContext typeList() {
      return getRuleContext(TypeListContext.class, 0);
    }

    public TerminalNode GT() {
      return getToken(TestIncrementalJavaParser.GT, 0);
    }

    public NonWildcardTypeArgumentsContext(ParserRuleContext parent, int invokingState) {
      super((IncrementalParserRuleContext) parent, invokingState);
    }

    @Override
    public int getRuleIndex() {
      return RULE_nonWildcardTypeArguments;
    }
  }

  @RuleVersion(0)
  public final NonWildcardTypeArgumentsContext nonWildcardTypeArguments() throws RecognitionException {
    // Check whether we need to execute this rule.
    NonWildcardTypeArgumentsContext guardResult = (NonWildcardTypeArgumentsContext) guardRule((IncrementalParserRuleContext) _ctx, getState(), RULE_nonWildcardTypeArguments);
    // If we found an existing context that is valid, return it.
    if (guardResult != null) {
      this._input.seek(guardResult.stop.getTokenIndex() + 1);
      return guardResult;
    }
    NonWildcardTypeArgumentsContext _localctx = new NonWildcardTypeArgumentsContext(_ctx, getState());
    enterRule(_localctx, 228, RULE_nonWildcardTypeArguments);
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(1298);
        match(LT);
        setState(1299);
        typeList();
        setState(1300);
        match(GT);
      }
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
  }

  public static class TypeArgumentsOrDiamondContext extends IncrementalParserRuleContext {
    public TerminalNode LT() {
      return getToken(TestIncrementalJavaParser.LT, 0);
    }

    public TerminalNode GT() {
      return getToken(TestIncrementalJavaParser.GT, 0);
    }

    public TypeArgumentsContext typeArguments() {
      return getRuleContext(TypeArgumentsContext.class, 0);
    }

    public TypeArgumentsOrDiamondContext(ParserRuleContext parent, int invokingState) {
      super((IncrementalParserRuleContext) parent, invokingState);
    }

    @Override
    public int getRuleIndex() {
      return RULE_typeArgumentsOrDiamond;
    }
  }

  @RuleVersion(0)
  public final TypeArgumentsOrDiamondContext typeArgumentsOrDiamond() throws RecognitionException {
    // Check whether we need to execute this rule.
    TypeArgumentsOrDiamondContext guardResult = (TypeArgumentsOrDiamondContext) guardRule((IncrementalParserRuleContext) _ctx, getState(), RULE_typeArgumentsOrDiamond);
    // If we found an existing context that is valid, return it.
    if (guardResult != null) {
      this._input.seek(guardResult.stop.getTokenIndex() + 1);
      return guardResult;
    }
    TypeArgumentsOrDiamondContext _localctx = new TypeArgumentsOrDiamondContext(_ctx, getState());
    enterRule(_localctx, 230, RULE_typeArgumentsOrDiamond);
    try {
      setState(1305);
      _errHandler.sync(this);
      switch (getInterpreter().adaptivePredict(_input, 146, _ctx)) {
        case 1:
          enterOuterAlt(_localctx, 1);
        {
          setState(1302);
          match(LT);
          setState(1303);
          match(GT);
        }
        break;
        case 2:
          enterOuterAlt(_localctx, 2);
        {
          setState(1304);
          typeArguments();
        }
        break;
      }
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
  }

  public static class NonWildcardTypeArgumentsOrDiamondContext extends IncrementalParserRuleContext {
    public TerminalNode LT() {
      return getToken(TestIncrementalJavaParser.LT, 0);
    }

    public TerminalNode GT() {
      return getToken(TestIncrementalJavaParser.GT, 0);
    }

    public NonWildcardTypeArgumentsContext nonWildcardTypeArguments() {
      return getRuleContext(NonWildcardTypeArgumentsContext.class, 0);
    }

    public NonWildcardTypeArgumentsOrDiamondContext(ParserRuleContext parent, int invokingState) {
      super((IncrementalParserRuleContext) parent, invokingState);
    }

    @Override
    public int getRuleIndex() {
      return RULE_nonWildcardTypeArgumentsOrDiamond;
    }

  }

  @RuleVersion(0)
  public final NonWildcardTypeArgumentsOrDiamondContext nonWildcardTypeArgumentsOrDiamond() throws RecognitionException {
    // Check whether we need to execute this rule.
    NonWildcardTypeArgumentsOrDiamondContext guardResult = (NonWildcardTypeArgumentsOrDiamondContext) guardRule((IncrementalParserRuleContext) _ctx, getState(), RULE_nonWildcardTypeArgumentsOrDiamond);
    // If we found an existing context that is valid, return it.
    if (guardResult != null) {
      this._input.seek(guardResult.stop.getTokenIndex() + 1);
      return guardResult;
    }
    NonWildcardTypeArgumentsOrDiamondContext _localctx = new NonWildcardTypeArgumentsOrDiamondContext(_ctx, getState());
    enterRule(_localctx, 232, RULE_nonWildcardTypeArgumentsOrDiamond);
    try {
      setState(1310);
      _errHandler.sync(this);
      switch (getInterpreter().adaptivePredict(_input, 147, _ctx)) {
        case 1:
          enterOuterAlt(_localctx, 1);
        {
          setState(1307);
          match(LT);
          setState(1308);
          match(GT);
        }
        break;
        case 2:
          enterOuterAlt(_localctx, 2);
        {
          setState(1309);
          nonWildcardTypeArguments();
        }
        break;
      }
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
  }

  public static class SuperSuffixContext extends IncrementalParserRuleContext {
    public ArgumentsContext arguments() {
      return getRuleContext(ArgumentsContext.class, 0);
    }

    public TerminalNode DOT() {
      return getToken(TestIncrementalJavaParser.DOT, 0);
    }

    public TerminalNode Identifier() {
      return getToken(TestIncrementalJavaParser.Identifier, 0);
    }

    public SuperSuffixContext(ParserRuleContext parent, int invokingState) {
      super((IncrementalParserRuleContext) parent, invokingState);
    }

    @Override
    public int getRuleIndex() {
      return RULE_superSuffix;
    }
  }

  @RuleVersion(0)
  public final SuperSuffixContext superSuffix() throws RecognitionException {
    // Check whether we need to execute this rule.
    SuperSuffixContext guardResult = (SuperSuffixContext) guardRule((IncrementalParserRuleContext) _ctx, getState(), RULE_superSuffix);
    // If we found an existing context that is valid, return it.
    if (guardResult != null) {
      this._input.seek(guardResult.stop.getTokenIndex() + 1);
      return guardResult;
    }
    SuperSuffixContext _localctx = new SuperSuffixContext(_ctx, getState());
    enterRule(_localctx, 234, RULE_superSuffix);
    try {
      setState(1318);
      _errHandler.sync(this);
      switch (_input.LA(1)) {
        case LPAREN:
          enterOuterAlt(_localctx, 1);
        {
          setState(1312);
          arguments();
        }
        break;
        case DOT:
          enterOuterAlt(_localctx, 2);
        {
          setState(1313);
          match(DOT);
          setState(1314);
          match(Identifier);
          setState(1316);
          _errHandler.sync(this);
          switch (getInterpreter().adaptivePredict(_input, 148, _ctx)) {
            case 1: {
              setState(1315);
              arguments();
            }
            break;
          }
        }
        break;
        default:
          throw new NoViableAltException(this);
      }
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
  }

  public static class ExplicitGenericInvocationSuffixContext extends IncrementalParserRuleContext {
    public TerminalNode SUPER() {
      return getToken(TestIncrementalJavaParser.SUPER, 0);
    }

    public SuperSuffixContext superSuffix() {
      return getRuleContext(SuperSuffixContext.class, 0);
    }

    public TerminalNode Identifier() {
      return getToken(TestIncrementalJavaParser.Identifier, 0);
    }

    public ArgumentsContext arguments() {
      return getRuleContext(ArgumentsContext.class, 0);
    }

    public ExplicitGenericInvocationSuffixContext(ParserRuleContext parent, int invokingState) {
      super((IncrementalParserRuleContext) parent, invokingState);
    }

    @Override
    public int getRuleIndex() {
      return RULE_explicitGenericInvocationSuffix;
    }
  }

  @RuleVersion(0)
  public final ExplicitGenericInvocationSuffixContext explicitGenericInvocationSuffix() throws RecognitionException {
    // Check whether we need to execute this rule.
    ExplicitGenericInvocationSuffixContext guardResult = (ExplicitGenericInvocationSuffixContext) guardRule((IncrementalParserRuleContext) _ctx, getState(), RULE_explicitGenericInvocationSuffix);
    // If we found an existing context that is valid, return it.
    if (guardResult != null) {
      this._input.seek(guardResult.stop.getTokenIndex() + 1);
      return guardResult;
    }
    ExplicitGenericInvocationSuffixContext _localctx = new ExplicitGenericInvocationSuffixContext(_ctx, getState());
    enterRule(_localctx, 236, RULE_explicitGenericInvocationSuffix);
    try {
      setState(1324);
      _errHandler.sync(this);
      switch (_input.LA(1)) {
        case SUPER:
          enterOuterAlt(_localctx, 1);
        {
          setState(1320);
          match(SUPER);
          setState(1321);
          superSuffix();
        }
        break;
        case Identifier:
          enterOuterAlt(_localctx, 2);
        {
          setState(1322);
          match(Identifier);
          setState(1323);
          arguments();
        }
        break;
        default:
          throw new NoViableAltException(this);
      }
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
  }

  public static class ArgumentsContext extends IncrementalParserRuleContext {
    public TerminalNode LPAREN() {
      return getToken(TestIncrementalJavaParser.LPAREN, 0);
    }

    public TerminalNode RPAREN() {
      return getToken(TestIncrementalJavaParser.RPAREN, 0);
    }

    public ExpressionListContext expressionList() {
      return getRuleContext(ExpressionListContext.class, 0);
    }

    public ArgumentsContext(ParserRuleContext parent, int invokingState) {
      super((IncrementalParserRuleContext) parent, invokingState);
    }

    @Override
    public int getRuleIndex() {
      return RULE_arguments;
    }

  }

  @RuleVersion(0)
  public final ArgumentsContext arguments() throws RecognitionException {
    // Check whether we need to execute this rule.
    ArgumentsContext guardResult = (ArgumentsContext) guardRule((IncrementalParserRuleContext) _ctx, getState(), RULE_arguments);
    // If we found an existing context that is valid, return it.
    if (guardResult != null) {
      this._input.seek(guardResult.stop.getTokenIndex() + 1);
      return guardResult;
    }
    ArgumentsContext _localctx = new ArgumentsContext(_ctx, getState());
    enterRule(_localctx, 238, RULE_arguments);
    int _la;
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(1326);
        match(LPAREN);
        setState(1328);
        _errHandler.sync(this);
        _la = _input.LA(1);
        if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << BOOLEAN) | (1L << BYTE) | (1L << CHAR) | (1L << DOUBLE) | (1L << FLOAT) | (1L << INT) | (1L << LONG) | (1L << NEW) | (1L << SHORT) | (1L << SUPER) | (1L << THIS) | (1L << VOID) | (1L << IntegerLiteral) | (1L << FloatingPointLiteral) | (1L << BooleanLiteral) | (1L << CharacterLiteral) | (1L << StringLiteral) | (1L << NullLiteral) | (1L << LPAREN))) != 0) || ((((_la - 68)) & ~0x3f) == 0 && ((1L << (_la - 68)) & ((1L << (LT - 68)) | (1L << (BANG - 68)) | (1L << (TILDE - 68)) | (1L << (INC - 68)) | (1L << (DEC - 68)) | (1L << (ADD - 68)) | (1L << (SUB - 68)) | (1L << (Identifier - 68)))) != 0)) {
          {
            setState(1327);
            expressionList();
          }
        }

        setState(1330);
        match(RPAREN);
      }
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
  }

  public boolean sempred(RuleContext _localctx, int ruleIndex, int predIndex) {
    switch (ruleIndex) {
      case 106:
        return expression_sempred((ExpressionContext) _localctx, predIndex);
    }
    return true;
  }

  private boolean expression_sempred(ExpressionContext _localctx, int predIndex) {
    switch (predIndex) {
      case 0:
        return precpred(_ctx, 13);
      case 1:
        return precpred(_ctx, 12);
      case 2:
        return precpred(_ctx, 11);
      case 3:
        return precpred(_ctx, 10);
      case 4:
        return precpred(_ctx, 8);
      case 5:
        return precpred(_ctx, 7);
      case 6:
        return precpred(_ctx, 6);
      case 7:
        return precpred(_ctx, 5);
      case 8:
        return precpred(_ctx, 4);
      case 9:
        return precpred(_ctx, 3);
      case 10:
        return precpred(_ctx, 2);
      case 11:
        return precpred(_ctx, 1);
      case 12:
        return precpred(_ctx, 25);
      case 13:
        return precpred(_ctx, 24);
      case 14:
        return precpred(_ctx, 23);
      case 15:
        return precpred(_ctx, 22);
      case 16:
        return precpred(_ctx, 21);
      case 17:
        return precpred(_ctx, 19);
      case 18:
        return precpred(_ctx, 17);
      case 19:
        return precpred(_ctx, 16);
      case 20:
        return precpred(_ctx, 9);
    }
    return true;
  }

  public static final String _serializedATN =
    "\3\uc91d\ucaba\u058d\uafba\u4f53\u0607\uea8b\uc241\3k\u0537\4\2\t\2\4" +
      "\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13\t" +
      "\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22\t\22" +
      "\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27\t\27\4\30\t\30\4\31\t\31" +
      "\4\32\t\32\4\33\t\33\4\34\t\34\4\35\t\35\4\36\t\36\4\37\t\37\4 \t \4!" +
      "\t!\4\"\t\"\4#\t#\4$\t$\4%\t%\4&\t&\4\'\t\'\4(\t(\4)\t)\4*\t*\4+\t+\4" +
      ",\t,\4-\t-\4.\t.\4/\t/\4\60\t\60\4\61\t\61\4\62\t\62\4\63\t\63\4\64\t" +
      "\64\4\65\t\65\4\66\t\66\4\67\t\67\48\t8\49\t9\4:\t:\4;\t;\4<\t<\4=\t=" +
      "\4>\t>\4?\t?\4@\t@\4A\tA\4B\tB\4C\tC\4D\tD\4E\tE\4F\tF\4G\tG\4H\tH\4I" +
      "\tI\4J\tJ\4K\tK\4L\tL\4M\tM\4N\tN\4O\tO\4P\tP\4Q\tQ\4R\tR\4S\tS\4T\tT" +
      "\4U\tU\4V\tV\4W\tW\4X\tX\4Y\tY\4Z\tZ\4[\t[\4\\\t\\\4]\t]\4^\t^\4_\t_\4" +
      "`\t`\4a\ta\4b\tb\4c\tc\4d\td\4e\te\4f\tf\4g\tg\4h\th\4i\ti\4j\tj\4k\t" +
      "k\4l\tl\4m\tm\4n\tn\4o\to\4p\tp\4q\tq\4r\tr\4s\ts\4t\tt\4u\tu\4v\tv\4" +
      "w\tw\4x\tx\4y\ty\3\2\3\2\3\2\7\2\u00f6\n\2\f\2\16\2\u00f9\13\2\3\2\7\2" +
      "\u00fc\n\2\f\2\16\2\u00ff\13\2\3\2\3\2\7\2\u0103\n\2\f\2\16\2\u0106\13" +
      "\2\5\2\u0108\n\2\3\2\3\2\3\2\5\2\u010d\n\2\3\2\7\2\u0110\n\2\f\2\16\2" +
      "\u0113\13\2\3\2\7\2\u0116\n\2\f\2\16\2\u0119\13\2\3\2\5\2\u011c\n\2\3" +
      "\3\3\3\3\3\3\3\3\4\3\4\5\4\u0124\n\4\3\4\3\4\3\4\5\4\u0129\n\4\3\4\3\4" +
      "\3\5\3\5\5\5\u012f\n\5\3\6\3\6\3\6\5\6\u0134\n\6\3\7\7\7\u0137\n\7\f\7" +
      "\16\7\u013a\13\7\3\b\3\b\5\b\u013e\n\b\3\t\7\t\u0141\n\t\f\t\16\t\u0144" +
      "\13\t\3\n\3\n\5\n\u0148\n\n\3\13\3\13\3\13\5\13\u014d\n\13\3\13\3\13\5" +
      "\13\u0151\n\13\3\13\3\13\5\13\u0155\n\13\3\13\3\13\3\f\3\f\3\f\3\f\7\f" +
      "\u015d\n\f\f\f\16\f\u0160\13\f\3\f\3\f\3\r\3\r\3\r\5\r\u0167\n\r\3\16" +
      "\3\16\3\16\7\16\u016c\n\16\f\16\16\16\u016f\13\16\3\17\3\17\3\17\3\17" +
      "\5\17\u0175\n\17\3\17\3\17\3\20\3\20\5\20\u017b\n\20\3\20\5\20\u017e\n" +
      "\20\3\20\5\20\u0181\n\20\3\20\3\20\3\21\3\21\3\21\7\21\u0188\n\21\f\21" +
      "\16\21\u018b\13\21\3\22\5\22\u018e\n\22\3\22\3\22\5\22\u0192\n\22\3\22" +
      "\5\22\u0195\n\22\3\23\3\23\7\23\u0199\n\23\f\23\16\23\u019c\13\23\3\24" +
      "\3\24\5\24\u01a0\n\24\3\25\3\25\3\25\5\25\u01a5\n\25\3\25\3\25\5\25\u01a9" +
      "\n\25\3\25\3\25\3\26\3\26\3\26\7\26\u01b0\n\26\f\26\16\26\u01b3\13\26" +
      "\3\27\3\27\7\27\u01b7\n\27\f\27\16\27\u01ba\13\27\3\27\3\27\3\30\3\30" +
      "\7\30\u01c0\n\30\f\30\16\30\u01c3\13\30\3\30\3\30\3\31\3\31\5\31\u01c9" +
      "\n\31\3\31\3\31\3\31\3\31\5\31\u01cf\n\31\3\32\3\32\3\32\3\32\3\32\3\32" +
      "\3\32\3\32\3\32\5\32\u01da\n\32\3\33\3\33\3\33\5\33\u01df\n\33\3\34\3" +
      "\34\3\34\3\35\3\35\5\35\u01e6\n\35\3\35\3\35\3\35\3\35\5\35\u01ec\n\35" +
      "\3\36\3\36\3\36\3\37\3\37\3\37\3 \3 \3 \3 \5 \u01f8\n \3!\3!\3!\3!\3!" +
      "\3!\3!\5!\u0201\n!\3\"\3\"\3\"\3\"\3#\3#\3#\3#\5#\u020b\n#\3$\3$\3$\7" +
      "$\u0210\n$\f$\16$\u0213\13$\3$\3$\5$\u0217\n$\3$\3$\5$\u021b\n$\3%\3%" +
      "\3%\5%\u0220\n%\3%\3%\5%\u0224\n%\3&\3&\3&\7&\u0229\n&\f&\16&\u022c\13" +
      "&\3&\3&\5&\u0230\n&\3&\3&\3\'\3\'\3\'\5\'\u0237\n\'\3\'\3\'\3\'\3(\3(" +
      "\3(\5(\u023f\n(\3(\3(\3)\3)\3)\5)\u0246\n)\3)\3)\3*\3*\3*\3+\3+\3+\7+" +
      "\u0250\n+\f+\16+\u0253\13+\3,\3,\3,\5,\u0258\n,\3-\3-\3-\7-\u025d\n-\f" +
      "-\16-\u0260\13-\3.\3.\7.\u0264\n.\f.\16.\u0267\13.\3.\3.\3.\3/\3/\3/\7" +
      "/\u026f\n/\f/\16/\u0272\13/\3\60\3\60\5\60\u0276\n\60\3\61\3\61\3\61\3" +
      "\61\7\61\u027c\n\61\f\61\16\61\u027f\13\61\3\61\5\61\u0282\n\61\5\61\u0284" +
      "\n\61\3\61\3\61\3\62\3\62\5\62\u028a\n\62\3\63\3\63\3\64\3\64\3\65\3\65" +
      "\3\66\3\66\3\66\7\66\u0295\n\66\f\66\16\66\u0298\13\66\3\66\3\66\3\66" +
      "\7\66\u029d\n\66\f\66\16\66\u02a0\13\66\5\66\u02a2\n\66\3\67\3\67\5\67" +
      "\u02a6\n\67\3\67\3\67\3\67\5\67\u02ab\n\67\7\67\u02ad\n\67\f\67\16\67" +
      "\u02b0\13\67\38\38\39\39\59\u02b6\n9\3:\3:\3:\3:\7:\u02bc\n:\f:\16:\u02bf" +
      "\13:\3:\3:\3;\3;\3;\3;\5;\u02c7\n;\5;\u02c9\n;\3<\3<\3<\7<\u02ce\n<\f" +
      "<\16<\u02d1\13<\3=\3=\5=\u02d5\n=\3=\3=\3>\3>\3>\3>\3?\3?\3?\5?\u02e0" +
      "\n?\3?\3?\5?\u02e4\n?\3@\3@\3A\3A\3B\3B\3B\7B\u02ed\nB\fB\16B\u02f0\13" +
      "B\3C\3C\3D\6D\u02f5\nD\rD\16D\u02f6\3E\3E\3E\3E\3E\5E\u02fe\nE\3E\5E\u0301" +
      "\nE\3F\3F\3F\7F\u0306\nF\fF\16F\u0309\13F\3G\3G\3G\7G\u030e\nG\fG\16G" +
      "\u0311\13G\3H\3H\3H\3H\3I\3I\3I\5I\u031a\nI\3J\3J\3J\3J\7J\u0320\nJ\f" +
      "J\16J\u0323\13J\5J\u0325\nJ\3J\5J\u0328\nJ\3J\3J\3K\3K\3K\3K\3K\3L\3L" +
      "\7L\u0333\nL\fL\16L\u0336\13L\3L\3L\3M\3M\3M\3M\5M\u033e\nM\3N\3N\3N\3" +
      "N\3N\3N\5N\u0346\nN\3N\3N\5N\u034a\nN\3N\3N\5N\u034e\nN\3N\3N\5N\u0352" +
      "\nN\5N\u0354\nN\3O\3O\5O\u0358\nO\3P\3P\3P\3P\5P\u035e\nP\3Q\3Q\3R\3R" +
      "\3R\3S\3S\7S\u0367\nS\fS\16S\u036a\13S\3S\3S\3T\3T\3T\5T\u0371\nT\3U\3" +
      "U\3U\3V\3V\3V\3V\3W\7W\u037b\nW\fW\16W\u037e\13W\3X\3X\3X\3X\3X\5X\u0385" +
      "\nX\3X\3X\3X\3X\3X\3X\3X\5X\u038e\nX\3X\3X\3X\3X\3X\3X\3X\3X\3X\3X\3X" +
      "\3X\3X\3X\3X\3X\3X\3X\3X\3X\5X\u03a4\nX\3X\5X\u03a7\nX\3X\3X\3X\3X\5X" +
      "\u03ad\nX\3X\5X\u03b0\nX\3X\3X\3X\3X\3X\3X\3X\3X\3X\3X\3X\3X\5X\u03be" +
      "\nX\3X\3X\3X\3X\3X\3X\3X\5X\u03c7\nX\3X\3X\3X\5X\u03cc\nX\3X\3X\3X\3X" +
      "\3X\3X\3X\3X\5X\u03d6\nX\3Y\6Y\u03d9\nY\rY\16Y\u03da\3Z\3Z\3Z\3Z\3Z\3" +
      "Z\3Z\3Z\3[\3[\3[\7[\u03e8\n[\f[\16[\u03eb\13[\3\\\3\\\3\\\3]\3]\3]\5]" +
      "\u03f3\n]\3]\3]\3^\3^\3^\7^\u03fa\n^\f^\16^\u03fd\13^\3_\3_\3_\3_\3_\3" +
      "_\3`\3`\3`\3`\3a\7a\u040a\na\fa\16a\u040d\13a\3b\6b\u0410\nb\rb\16b\u0411" +
      "\3b\7b\u0415\nb\fb\16b\u0418\13b\3c\3c\3c\3c\3c\3c\3c\3c\3c\3c\5c\u0424" +
      "\nc\3d\3d\5d\u0428\nd\3d\3d\5d\u042c\nd\3d\3d\5d\u0430\nd\5d\u0432\nd" +
      "\3e\3e\5e\u0436\ne\3f\3f\3f\3f\3f\3f\3g\3g\3h\3h\3h\3h\3i\3i\3i\7i\u0447" +
      "\ni\fi\16i\u044a\13i\3j\3j\3k\3k\3l\3l\3l\3l\3l\3l\3l\3l\3l\3l\3l\3l\3" +
      "l\5l\u045d\nl\3l\3l\3l\3l\3l\3l\3l\3l\3l\3l\3l\3l\3l\3l\5l\u046d\nl\3" +
      "l\3l\3l\3l\3l\3l\3l\3l\3l\3l\3l\3l\3l\3l\3l\3l\3l\3l\3l\3l\3l\3l\3l\3" +
      "l\3l\3l\3l\3l\3l\3l\3l\3l\3l\3l\3l\3l\3l\3l\3l\3l\3l\5l\u0498\nl\3l\3" +
      "l\3l\3l\3l\3l\3l\3l\3l\3l\3l\3l\3l\3l\3l\3l\3l\3l\5l\u04ac\nl\3l\3l\3" +
      "l\3l\7l\u04b2\nl\fl\16l\u04b5\13l\3m\3m\3m\3m\3m\3m\3m\3m\3m\3m\3m\3m" +
      "\3m\3m\3m\3m\3m\3m\3m\5m\u04ca\nm\5m\u04cc\nm\3n\3n\3n\3n\3n\3n\3n\5n" +
      "\u04d5\nn\5n\u04d7\nn\3o\3o\5o\u04db\no\3o\3o\3o\5o\u04e0\no\7o\u04e2" +
      "\no\fo\16o\u04e5\13o\3o\5o\u04e8\no\3p\3p\5p\u04ec\np\3p\3p\3q\3q\3q\3" +
      "q\7q\u04f4\nq\fq\16q\u04f7\13q\3q\3q\3q\3q\3q\3q\3q\7q\u0500\nq\fq\16" +
      "q\u0503\13q\3q\3q\7q\u0507\nq\fq\16q\u050a\13q\5q\u050c\nq\3r\3r\5r\u0510" +
      "\nr\3s\3s\3s\3t\3t\3t\3t\3u\3u\3u\5u\u051c\nu\3v\3v\3v\5v\u0521\nv\3w" +
      "\3w\3w\3w\5w\u0527\nw\5w\u0529\nw\3x\3x\3x\3x\5x\u052f\nx\3y\3y\5y\u0533" +
      "\ny\3y\3y\3y\2\2\3\u00d6z\2\2\4\2\6\2\b\2\n\2\f\2\16\2\20\2\22\2\24\2" +
      "\26\2\30\2\32\2\34\2\36\2 \2\"\2$\2&\2(\2*\2,\2.\2\60\2\62\2\64\2\66\2" +
      "8\2:\2<\2>\2@\2B\2D\2F\2H\2J\2L\2N\2P\2R\2T\2V\2X\2Z\2\\\2^\2`\2b\2d\2" +
      "f\2h\2j\2l\2n\2p\2r\2t\2v\2x\2z\2|\2~\2\u0080\2\u0082\2\u0084\2\u0086" +
      "\2\u0088\2\u008a\2\u008c\2\u008e\2\u0090\2\u0092\2\u0094\2\u0096\2\u0098" +
      "\2\u009a\2\u009c\2\u009e\2\u00a0\2\u00a2\2\u00a4\2\u00a6\2\u00a8\2\u00aa" +
      "\2\u00ac\2\u00ae\2\u00b0\2\u00b2\2\u00b4\2\u00b6\2\u00b8\2\u00ba\2\u00bc" +
      "\2\u00be\2\u00c0\2\u00c2\2\u00c4\2\u00c6\2\u00c8\2\u00ca\2\u00cc\2\u00ce" +
      "\2\u00d0\2\u00d2\2\u00d4\2\u00d6\2\u00d8\2\u00da\2\u00dc\2\u00de\2\u00e0" +
      "\2\u00e2\2\u00e4\2\u00e6\2\u00e8\2\u00ea\2\u00ec\2\u00ee\2\u00f0\2\2\17" +
      "\6\2\3\3\24\24#%()\n\2\3\3\24\24  #%(),,\60\60\63\63\n\2\5\5\7\7\n\n\20" +
      "\20\26\26\35\35\37\37\'\'\4\2\23\23**\3\2\65:\3\2QT\3\2GH\4\2UVZZ\3\2" +
      "ST\4\2EFLM\4\2KKNN\4\2DD[e\3\2QR\2\u0591\2\u011b\3\2\2\2\4\u011d\3\2\2" +
      "\2\6\u0121\3\2\2\2\b\u012e\3\2\2\2\n\u0130\3\2\2\2\f\u0138\3\2\2\2\16" +
      "\u013d\3\2\2\2\20\u0142\3\2\2\2\22\u0147\3\2\2\2\24\u0149\3\2\2\2\26\u0158" +
      "\3\2\2\2\30\u0163\3\2\2\2\32\u0168\3\2\2\2\34\u0170\3\2\2\2\36\u0178\3" +
      "\2\2\2 \u0184\3\2\2\2\"\u018d\3\2\2\2$\u0196\3\2\2\2&\u019f\3\2\2\2(\u01a1" +
      "\3\2\2\2*\u01ac\3\2\2\2,\u01b4\3\2\2\2.\u01bd\3\2\2\2\60\u01ce\3\2\2\2" +
      "\62\u01d9\3\2\2\2\64\u01db\3\2\2\2\66\u01e0\3\2\2\28\u01eb\3\2\2\2:\u01ed" +
      "\3\2\2\2<\u01f0\3\2\2\2>\u01f7\3\2\2\2@\u0200\3\2\2\2B\u0202\3\2\2\2D" +
      "\u020a\3\2\2\2F\u020c\3\2\2\2H\u021c\3\2\2\2J\u0225\3\2\2\2L\u0233\3\2" +
      "\2\2N\u023b\3\2\2\2P\u0242\3\2\2\2R\u0249\3\2\2\2T\u024c\3\2\2\2V\u0254" +
      "\3\2\2\2X\u0259\3\2\2\2Z\u0265\3\2\2\2\\\u026b\3\2\2\2^\u0275\3\2\2\2" +
      "`\u0277\3\2\2\2b\u0289\3\2\2\2d\u028b\3\2\2\2f\u028d\3\2\2\2h\u028f\3" +
      "\2\2\2j\u02a1\3\2\2\2l\u02a3\3\2\2\2n\u02b1\3\2\2\2p\u02b5\3\2\2\2r\u02b7" +
      "\3\2\2\2t\u02c8\3\2\2\2v\u02ca\3\2\2\2x\u02d2\3\2\2\2z\u02d8\3\2\2\2|" +
      "\u02e3\3\2\2\2~\u02e5\3\2\2\2\u0080\u02e7\3\2\2\2\u0082\u02e9\3\2\2\2" +
      "\u0084\u02f1\3\2\2\2\u0086\u02f4\3\2\2\2\u0088\u02f8\3\2\2\2\u008a\u0302" +
      "\3\2\2\2\u008c\u030a\3\2\2\2\u008e\u0312\3\2\2\2\u0090\u0319\3\2\2\2\u0092" +
      "\u031b\3\2\2\2\u0094\u032b\3\2\2\2\u0096\u0330\3\2\2\2\u0098\u033d\3\2" +
      "\2\2\u009a\u0353\3\2\2\2\u009c\u0357\3\2\2\2\u009e\u0359\3\2\2\2\u00a0" +
      "\u035f\3\2\2\2\u00a2\u0361\3\2\2\2\u00a4\u0364\3\2\2\2\u00a6\u0370\3\2" +
      "\2\2\u00a8\u0372\3\2\2\2\u00aa\u0375\3\2\2\2\u00ac\u037c\3\2\2\2\u00ae" +
      "\u03d5\3\2\2\2\u00b0\u03d8\3\2\2\2\u00b2\u03dc\3\2\2\2\u00b4\u03e4\3\2" +
      "\2\2\u00b6\u03ec\3\2\2\2\u00b8\u03ef\3\2\2\2\u00ba\u03f6\3\2\2\2\u00bc" +
      "\u03fe\3\2\2\2\u00be\u0404\3\2\2\2\u00c0\u040b\3\2\2\2\u00c2\u040f\3\2" +
      "\2\2\u00c4\u0423\3\2\2\2\u00c6\u0431\3\2\2\2\u00c8\u0435\3\2\2\2\u00ca" +
      "\u0437\3\2\2\2\u00cc\u043d\3\2\2\2\u00ce\u043f\3\2\2\2\u00d0\u0443\3\2" +
      "\2\2\u00d2\u044b\3\2\2\2\u00d4\u044d\3\2\2\2\u00d6\u045c\3\2\2\2\u00d8" +
      "\u04cb\3\2\2\2\u00da\u04d6\3\2\2\2\u00dc\u04e7\3\2\2\2\u00de\u04e9\3\2" +
      "\2\2\u00e0\u04ef\3\2\2\2\u00e2\u050d\3\2\2\2\u00e4\u0511\3\2\2\2\u00e6" +
      "\u0514\3\2\2\2\u00e8\u051b\3\2\2\2\u00ea\u0520\3\2\2\2\u00ec\u0528\3\2" +
      "\2\2\u00ee\u052e\3\2\2\2\u00f0\u0530\3\2\2\2\u00f2\u0107\5\u0086D\2\u00f3" +
      "\u00f7\5\4\3\2\u00f4\u00f6\5\6\4\2\u00f5\u00f4\3\2\2\2\u00f6\u00f9\3\2" +
      "\2\2\u00f7\u00f5\3\2\2\2\u00f7\u00f8\3\2\2\2\u00f8\u00fd\3\2\2\2\u00f9" +
      "\u00f7\3\2\2\2\u00fa\u00fc\5\b\5\2\u00fb\u00fa\3\2\2\2\u00fc\u00ff\3\2" +
      "\2\2\u00fd\u00fb\3\2\2\2\u00fd\u00fe\3\2\2\2\u00fe\u0108\3\2\2\2\u00ff" +
      "\u00fd\3\2\2\2\u0100\u0104\5\n\6\2\u0101\u0103\5\b\5\2\u0102\u0101\3\2" +
      "\2\2\u0103\u0106\3\2\2\2\u0104\u0102\3\2\2\2\u0104\u0105\3\2\2\2\u0105" +
      "\u0108\3\2\2\2\u0106\u0104\3\2\2\2\u0107\u00f3\3\2\2\2\u0107\u0100\3\2" +
      "\2\2\u0108\u0109\3\2\2\2\u0109\u010a\7\2\2\3\u010a\u011c\3\2\2\2\u010b" +
      "\u010d\5\4\3\2\u010c\u010b\3\2\2\2\u010c\u010d\3\2\2\2\u010d\u0111\3\2" +
      "\2\2\u010e\u0110\5\6\4\2\u010f\u010e\3\2\2\2\u0110\u0113\3\2\2\2\u0111" +
      "\u010f\3\2\2\2\u0111\u0112\3\2\2\2\u0112\u0117\3\2\2\2\u0113\u0111\3\2" +
      "\2\2\u0114\u0116\5\b\5\2\u0115\u0114\3\2\2\2\u0116\u0119\3\2\2\2\u0117" +
      "\u0115\3\2\2\2\u0117\u0118\3\2\2\2\u0118\u011a\3\2\2\2\u0119\u0117\3\2" +
      "\2\2\u011a\u011c\7\2\2\3\u011b\u00f2\3\2\2\2\u011b\u010c\3\2\2\2\u011c" +
      "\3\3\2\2\2\u011d\u011e\7\"\2\2\u011e\u011f\5\u0082B\2\u011f\u0120\7A\2" +
      "\2\u0120\5\3\2\2\2\u0121\u0123\7\33\2\2\u0122\u0124\7(\2\2\u0123\u0122" +
      "\3\2\2\2\u0123\u0124\3\2\2\2\u0124\u0125\3\2\2\2\u0125\u0128\5\u0082B" +
      "\2\u0126\u0127\7C\2\2\u0127\u0129\7U\2\2\u0128\u0126\3\2\2\2\u0128\u0129" +
      "\3\2\2\2\u0129\u012a\3\2\2\2\u012a\u012b\7A\2\2\u012b\7\3\2\2\2\u012c" +
      "\u012f\5\n\6\2\u012d\u012f\7A\2\2\u012e\u012c\3\2\2\2\u012e\u012d\3\2" +
      "\2\2\u012f\t\3\2\2\2\u0130\u0133\5\f\7\2\u0131\u0134\5\22\n\2\u0132\u0134" +
      "\5&\24\2\u0133\u0131\3\2\2\2\u0133\u0132\3\2\2\2\u0134\13\3\2\2\2\u0135" +
      "\u0137\5\16\b\2\u0136\u0135\3\2\2\2\u0137\u013a\3\2\2\2\u0138\u0136\3" +
      "\2\2\2\u0138\u0139\3\2\2\2\u0139\r\3\2\2\2\u013a\u0138\3\2\2\2\u013b\u013e" +
      "\5\u0088E\2\u013c\u013e\t\2\2\2\u013d\u013b\3\2\2\2\u013d\u013c\3\2\2" +
      "\2\u013e\17\3\2\2\2\u013f\u0141\5b\62\2\u0140\u013f\3\2\2\2\u0141\u0144" +
      "\3\2\2\2\u0142\u0140\3\2\2\2\u0142\u0143\3\2\2\2\u0143\21\3\2\2\2\u0144" +
      "\u0142\3\2\2\2\u0145\u0148\5\24\13\2\u0146\u0148\5\34\17\2\u0147\u0145" +
      "\3\2\2\2\u0147\u0146\3\2\2\2\u0148\23\3\2\2\2\u0149\u014a\7\13\2\2\u014a" +
      "\u014c\7f\2\2\u014b\u014d\5\26\f\2\u014c\u014b\3\2\2\2\u014c\u014d\3\2" +
      "\2\2\u014d\u0150\3\2\2\2\u014e\u014f\7\23\2\2\u014f\u0151\5j\66\2\u0150" +
      "\u014e\3\2\2\2\u0150\u0151\3\2\2\2\u0151\u0154\3\2\2\2\u0152\u0153\7\32" +
      "\2\2\u0153\u0155\5*\26\2\u0154\u0152\3\2\2\2\u0154\u0155\3\2\2\2\u0155" +
      "\u0156\3\2\2\2\u0156\u0157\5,\27\2\u0157\25\3\2\2\2\u0158\u0159\7F\2\2" +
      "\u0159\u015e\5\30\r\2\u015a\u015b\7B\2\2\u015b\u015d\5\30\r\2\u015c\u015a" +
      "\3\2\2\2\u015d\u0160\3\2\2\2\u015e\u015c\3\2\2\2\u015e\u015f\3\2\2\2\u015f" +
      "\u0161\3\2\2\2\u0160\u015e\3\2\2\2\u0161\u0162\7E\2\2\u0162\27\3\2\2\2" +
      "\u0163\u0166\7f\2\2\u0164\u0165\7\23\2\2\u0165\u0167\5\32\16\2\u0166\u0164" +
      "\3\2\2\2\u0166\u0167\3\2\2\2\u0167\31\3\2\2\2\u0168\u016d\5j\66\2\u0169" +
      "\u016a\7W\2\2\u016a\u016c\5j\66\2\u016b\u0169\3\2\2\2\u016c\u016f\3\2" +
      "\2\2\u016d\u016b\3\2\2\2\u016d\u016e\3\2\2\2\u016e\33\3\2\2\2\u016f\u016d" +
      "\3\2\2\2\u0170\u0171\7\22\2\2\u0171\u0174\7f\2\2\u0172\u0173\7\32\2\2" +
      "\u0173\u0175\5*\26\2\u0174\u0172\3\2\2\2\u0174\u0175\3\2\2\2\u0175\u0176" +
      "\3\2\2\2\u0176\u0177\5\36\20\2\u0177\35\3\2\2\2\u0178\u017a\7=\2\2\u0179" +
      "\u017b\5 \21\2\u017a\u0179\3\2\2\2\u017a\u017b\3\2\2\2\u017b\u017d\3\2" +
      "\2\2\u017c\u017e\7B\2\2\u017d\u017c\3\2\2\2\u017d\u017e\3\2\2\2\u017e" +
      "\u0180\3\2\2\2\u017f\u0181\5$\23\2\u0180\u017f\3\2\2\2\u0180\u0181\3\2" +
      "\2\2\u0181\u0182\3\2\2\2\u0182\u0183\7>\2\2\u0183\37\3\2\2\2\u0184\u0189" +
      "\5\"\22\2\u0185\u0186\7B\2\2\u0186\u0188\5\"\22\2\u0187\u0185\3\2\2\2" +
      "\u0188\u018b\3\2\2\2\u0189\u0187\3\2\2\2\u0189\u018a\3\2\2\2\u018a!\3" +
      "\2\2\2\u018b\u0189\3\2\2\2\u018c\u018e\5\u0086D\2\u018d\u018c\3\2\2\2" +
      "\u018d\u018e\3\2\2\2\u018e\u018f\3\2\2\2\u018f\u0191\7f\2\2\u0190\u0192" +
      "\5\u00f0y\2\u0191\u0190\3\2\2\2\u0191\u0192\3\2\2\2\u0192\u0194\3\2\2" +
      "\2\u0193\u0195\5,\27\2\u0194\u0193\3\2\2\2\u0194\u0195\3\2\2\2\u0195#" +
      "\3\2\2\2\u0196\u019a\7A\2\2\u0197\u0199\5\60\31\2\u0198\u0197\3\2\2\2" +
      "\u0199\u019c\3\2\2\2\u019a\u0198\3\2\2\2\u019a\u019b\3\2\2\2\u019b%\3" +
      "\2\2\2\u019c\u019a\3\2\2\2\u019d\u01a0\5(\25\2\u019e\u01a0\5\u0094K\2" +
      "\u019f\u019d\3\2\2\2\u019f\u019e\3\2\2\2\u01a0\'\3\2\2\2\u01a1\u01a2\7" +
      "\36\2\2\u01a2\u01a4\7f\2\2\u01a3\u01a5\5\26\f\2\u01a4\u01a3\3\2\2\2\u01a4" +
      "\u01a5\3\2\2\2\u01a5\u01a8\3\2\2\2\u01a6\u01a7\7\23\2\2\u01a7\u01a9\5" +
      "*\26\2\u01a8\u01a6\3\2\2\2\u01a8\u01a9\3\2\2\2\u01a9\u01aa\3\2\2\2\u01aa" +
      "\u01ab\5.\30\2\u01ab)\3\2\2\2\u01ac\u01b1\5j\66\2\u01ad\u01ae\7B\2\2\u01ae" +
      "\u01b0\5j\66\2\u01af\u01ad\3\2\2\2\u01b0\u01b3\3\2\2\2\u01b1\u01af\3\2" +
      "\2\2\u01b1\u01b2\3\2\2\2\u01b2+\3\2\2\2\u01b3\u01b1\3\2\2\2\u01b4\u01b8" +
      "\7=\2\2\u01b5\u01b7\5\60\31\2\u01b6\u01b5\3\2\2\2\u01b7\u01ba\3\2\2\2" +
      "\u01b8\u01b6\3\2\2\2\u01b8\u01b9\3\2\2\2\u01b9\u01bb\3\2\2\2\u01ba\u01b8" +
      "\3\2\2\2\u01bb\u01bc\7>\2\2\u01bc-\3\2\2\2\u01bd\u01c1\7=\2\2\u01be\u01c0" +
      "\5> \2\u01bf\u01be\3\2\2\2\u01c0\u01c3\3\2\2\2\u01c1\u01bf\3\2\2\2\u01c1" +
      "\u01c2\3\2\2\2\u01c2\u01c4\3\2\2\2\u01c3\u01c1\3\2\2\2\u01c4\u01c5\7>" +
      "\2\2\u01c5/\3\2\2\2\u01c6\u01cf\7A\2\2\u01c7\u01c9\7(\2\2\u01c8\u01c7" +
      "\3\2\2\2\u01c8\u01c9\3\2\2\2\u01c9\u01ca\3\2\2\2\u01ca\u01cf\5\u00a4S" +
      "\2\u01cb\u01cc\5\20\t\2\u01cc\u01cd\5\62\32\2\u01cd\u01cf\3\2\2\2\u01ce" +
      "\u01c6\3\2\2\2\u01ce\u01c8\3\2\2\2\u01ce\u01cb\3\2\2\2\u01cf\61\3\2\2" +
      "\2\u01d0\u01da\5\66\34\2\u01d1\u01da\5\64\33\2\u01d2\u01d3\7\62\2\2\u01d3" +
      "\u01d4\7f\2\2\u01d4\u01da\5H%\2\u01d5\u01d6\7f\2\2\u01d6\u01da\5P)\2\u01d7" +
      "\u01da\5&\24\2\u01d8\u01da\5\22\n\2\u01d9\u01d0\3\2\2\2\u01d9\u01d1\3" +
      "\2\2\2\u01d9\u01d2\3\2\2\2\u01d9\u01d5\3\2\2\2\u01d9\u01d7\3\2\2\2\u01d9" +
      "\u01d8\3\2\2\2\u01da\63\3\2\2\2\u01db\u01de\5j\66\2\u01dc\u01df\5:\36" +
      "\2\u01dd\u01df\5<\37\2\u01de\u01dc\3\2\2\2\u01de\u01dd\3\2\2\2\u01df\65" +
      "\3\2\2\2\u01e0\u01e1\5\26\f\2\u01e1\u01e2\58\35\2\u01e2\67\3\2\2\2\u01e3" +
      "\u01e6\5j\66\2\u01e4\u01e6\7\62\2\2\u01e5\u01e3\3\2\2\2\u01e5\u01e4\3" +
      "\2\2\2\u01e6\u01e7\3\2\2\2\u01e7\u01e8\7f\2\2\u01e8\u01ec\5F$\2\u01e9" +
      "\u01ea\7f\2\2\u01ea\u01ec\5P)\2\u01eb\u01e5\3\2\2\2\u01eb\u01e9\3\2\2" +
      "\2\u01ec9\3\2\2\2\u01ed\u01ee\7f\2\2\u01ee\u01ef\5F$\2\u01ef;\3\2\2\2" +
      "\u01f0\u01f1\5T+\2\u01f1\u01f2\7A\2\2\u01f2=\3\2\2\2\u01f3\u01f4\5\20" +
      "\t\2\u01f4\u01f5\5@!\2\u01f5\u01f8\3\2\2\2\u01f6\u01f8\7A\2\2\u01f7\u01f3" +
      "\3\2\2\2\u01f7\u01f6\3\2\2\2\u01f8?\3\2\2\2\u01f9\u0201\5B\"\2\u01fa\u0201" +
      "\5L\'\2\u01fb\u01fc\7\62\2\2\u01fc\u01fd\7f\2\2\u01fd\u0201\5N(\2\u01fe" +
      "\u0201\5&\24\2\u01ff\u0201\5\22\n\2\u0200\u01f9\3\2\2\2\u0200\u01fa\3" +
      "\2\2\2\u0200\u01fb\3\2\2\2\u0200\u01fe\3\2\2\2\u0200\u01ff\3\2\2\2\u0201" +
      "A\3\2\2\2\u0202\u0203\5j\66\2\u0203\u0204\7f\2\2\u0204\u0205\5D#\2\u0205" +
      "C\3\2\2\2\u0206\u0207\5X-\2\u0207\u0208\7A\2\2\u0208\u020b\3\2\2\2\u0209" +
      "\u020b\5J&\2\u020a\u0206\3\2\2\2\u020a\u0209\3\2\2\2\u020bE\3\2\2\2\u020c" +
      "\u0211\5x=\2\u020d\u020e\7?\2\2\u020e\u0210\7@\2\2\u020f\u020d\3\2\2\2" +
      "\u0210\u0213\3\2\2\2\u0211\u020f\3\2\2\2\u0211\u0212\3\2\2\2\u0212\u0216" +
      "\3\2\2\2\u0213\u0211\3\2\2\2\u0214\u0215\7/\2\2\u0215\u0217\5v<\2\u0216" +
      "\u0214\3\2\2\2\u0216\u0217\3\2\2\2\u0217\u021a\3\2\2\2\u0218\u021b\5~" +
      "@\2\u0219\u021b\7A\2\2\u021a\u0218\3\2\2\2\u021a\u0219\3\2\2\2\u021bG" +
      "\3\2\2\2\u021c\u021f\5x=\2\u021d\u021e\7/\2\2\u021e\u0220\5v<\2\u021f" +
      "\u021d\3\2\2\2\u021f\u0220\3\2\2\2\u0220\u0223\3\2\2\2\u0221\u0224\5~" +
      "@\2\u0222\u0224\7A\2\2\u0223\u0221\3\2\2\2\u0223\u0222\3\2\2\2\u0224I" +
      "\3\2\2\2\u0225\u022a\5x=\2\u0226\u0227\7?\2\2\u0227\u0229\7@\2\2\u0228" +
      "\u0226\3\2\2\2\u0229\u022c\3\2\2\2\u022a\u0228\3\2\2\2\u022a\u022b\3\2" +
      "\2\2\u022b\u022f\3\2\2\2\u022c\u022a\3\2\2\2\u022d\u022e\7/\2\2\u022e" +
      "\u0230\5v<\2\u022f\u022d\3\2\2\2\u022f\u0230\3\2\2\2\u0230\u0231\3\2\2" +
      "\2\u0231\u0232\7A\2\2\u0232K\3\2\2\2\u0233\u0236\5\26\f\2\u0234\u0237" +
      "\5j\66\2\u0235\u0237\7\62\2\2\u0236\u0234\3\2\2\2\u0236\u0235\3\2\2\2" +
      "\u0237\u0238\3\2\2\2\u0238\u0239\7f\2\2\u0239\u023a\5J&\2\u023aM\3\2\2" +
      "\2\u023b\u023e\5x=\2\u023c\u023d\7/\2\2\u023d\u023f\5v<\2\u023e\u023c" +
      "\3\2\2\2\u023e\u023f\3\2\2\2\u023f\u0240\3\2\2\2\u0240\u0241\7A\2\2\u0241" +
      "O\3\2\2\2\u0242\u0245\5x=\2\u0243\u0244\7/\2\2\u0244\u0246\5v<\2\u0245" +
      "\u0243\3\2\2\2\u0245\u0246\3\2\2\2\u0246\u0247\3\2\2\2\u0247\u0248\5\u0080" +
      "A\2\u0248Q\3\2\2\2\u0249\u024a\7f\2\2\u024a\u024b\5Z.\2\u024bS\3\2\2\2" +
      "\u024c\u0251\5V,\2\u024d\u024e\7B\2\2\u024e\u0250\5V,\2\u024f\u024d\3" +
      "\2\2\2\u0250\u0253\3\2\2\2\u0251\u024f\3\2\2\2\u0251\u0252\3\2\2\2\u0252" +
      "U\3\2\2\2\u0253\u0251\3\2\2\2\u0254\u0257\5\\/\2\u0255\u0256\7D\2\2\u0256" +
      "\u0258\5^\60\2\u0257\u0255\3\2\2\2\u0257\u0258\3\2\2\2\u0258W\3\2\2\2" +
      "\u0259\u025e\5Z.\2\u025a\u025b\7B\2\2\u025b\u025d\5R*\2\u025c\u025a\3" +
      "\2\2\2\u025d\u0260\3\2\2\2\u025e\u025c\3\2\2\2\u025e\u025f\3\2\2\2\u025f" +
      "Y\3\2\2\2\u0260\u025e\3\2\2\2\u0261\u0262\7?\2\2\u0262\u0264\7@\2\2\u0263" +
      "\u0261\3\2\2\2\u0264\u0267\3\2\2\2\u0265\u0263\3\2\2\2\u0265\u0266\3\2" +
      "\2\2\u0266\u0268\3\2\2\2\u0267\u0265\3\2\2\2\u0268\u0269\7D\2\2\u0269" +
      "\u026a\5^\60\2\u026a[\3\2\2\2\u026b\u0270\7f\2\2\u026c\u026d\7?\2\2\u026d" +
      "\u026f\7@\2\2\u026e\u026c\3\2\2\2\u026f\u0272\3\2\2\2\u0270\u026e\3\2" +
      "\2\2\u0270\u0271\3\2\2\2\u0271]\3\2\2\2\u0272\u0270\3\2\2\2\u0273\u0276" +
      "\5`\61\2\u0274\u0276\5\u00d6l\2\u0275\u0273\3\2\2\2\u0275\u0274\3\2\2" +
      "\2\u0276_\3\2\2\2\u0277\u0283\7=\2\2\u0278\u027d\5^\60\2\u0279\u027a\7" +
      "B\2\2\u027a\u027c\5^\60\2\u027b\u0279\3\2\2\2\u027c\u027f\3\2\2\2\u027d" +
      "\u027b\3\2\2\2\u027d\u027e\3\2\2\2\u027e\u0281\3\2\2\2\u027f\u027d\3\2" +
      "\2\2\u0280\u0282\7B\2\2\u0281\u0280\3\2\2\2\u0281\u0282\3\2\2\2\u0282" +
      "\u0284\3\2\2\2\u0283\u0278\3\2\2\2\u0283\u0284\3\2\2\2\u0284\u0285\3\2" +
      "\2\2\u0285\u0286\7>\2\2\u0286a\3\2\2\2\u0287\u028a\5\u0088E\2\u0288\u028a" +
      "\t\3\2\2\u0289\u0287\3\2\2\2\u0289\u0288\3\2\2\2\u028ac\3\2\2\2\u028b" +
      "\u028c\5\u0082B\2\u028ce\3\2\2\2\u028d\u028e\7f\2\2\u028eg\3\2\2\2\u028f" +
      "\u0290\5\u0082B\2\u0290i\3\2\2\2\u0291\u0296\5l\67\2\u0292\u0293\7?\2" +
      "\2\u0293\u0295\7@\2\2\u0294\u0292\3\2\2\2\u0295\u0298\3\2\2\2\u0296\u0294" +
      "\3\2\2\2\u0296\u0297\3\2\2\2\u0297\u02a2\3\2\2\2\u0298\u0296\3\2\2\2\u0299" +
      "\u029e\5n8\2\u029a\u029b\7?\2\2\u029b\u029d\7@\2\2\u029c\u029a\3\2\2\2" +
      "\u029d\u02a0\3\2\2\2\u029e\u029c\3\2\2\2\u029e\u029f\3\2\2\2\u029f\u02a2" +
      "\3\2\2\2\u02a0\u029e\3\2\2\2\u02a1\u0291\3\2\2\2\u02a1\u0299\3\2\2\2\u02a2" +
      "k\3\2\2\2\u02a3\u02a5\7f\2\2\u02a4\u02a6\5r:\2\u02a5\u02a4\3\2\2\2\u02a5" +
      "\u02a6\3\2\2\2\u02a6\u02ae\3\2\2\2\u02a7\u02a8\7C\2\2\u02a8\u02aa\7f\2" +
      "\2\u02a9\u02ab\5r:\2\u02aa\u02a9\3\2\2\2\u02aa\u02ab\3\2\2\2\u02ab\u02ad" +
      "\3\2\2\2\u02ac\u02a7\3\2\2\2\u02ad\u02b0\3\2\2\2\u02ae\u02ac\3\2\2\2\u02ae" +
      "\u02af\3\2\2\2\u02afm\3\2\2\2\u02b0\u02ae\3\2\2\2\u02b1\u02b2\t\4\2\2" +
      "\u02b2o\3\2\2\2\u02b3\u02b6\7\24\2\2\u02b4\u02b6\5\u0088E\2\u02b5\u02b3" +
      "\3\2\2\2\u02b5\u02b4\3\2\2\2\u02b6q\3\2\2\2\u02b7\u02b8\7F\2\2\u02b8\u02bd" +
      "\5t;\2\u02b9\u02ba\7B\2\2\u02ba\u02bc\5t;\2\u02bb\u02b9\3\2\2\2\u02bc" +
      "\u02bf\3\2\2\2\u02bd\u02bb\3\2\2\2\u02bd\u02be\3\2\2\2\u02be\u02c0\3\2" +
      "\2\2\u02bf\u02bd\3\2\2\2\u02c0\u02c1\7E\2\2\u02c1s\3\2\2\2\u02c2\u02c9" +
      "\5j\66\2\u02c3\u02c6\7I\2\2\u02c4\u02c5\t\5\2\2\u02c5\u02c7\5j\66\2\u02c6" +
      "\u02c4\3\2\2\2\u02c6\u02c7\3\2\2\2\u02c7\u02c9\3\2\2\2\u02c8\u02c2\3\2" +
      "\2\2\u02c8\u02c3\3\2\2\2\u02c9u\3\2\2\2\u02ca\u02cf\5\u0082B\2\u02cb\u02cc" +
      "\7B\2\2\u02cc\u02ce\5\u0082B\2\u02cd\u02cb\3\2\2\2\u02ce\u02d1\3\2\2\2" +
      "\u02cf\u02cd\3\2\2\2\u02cf\u02d0\3\2\2\2\u02d0w\3\2\2\2\u02d1\u02cf\3" +
      "\2\2\2\u02d2\u02d4\7;\2\2\u02d3\u02d5\5z>\2\u02d4\u02d3\3\2\2\2\u02d4" +
      "\u02d5\3\2\2\2\u02d5\u02d6\3\2\2\2\u02d6\u02d7\7<\2\2\u02d7y\3\2\2\2\u02d8" +
      "\u02d9\5\u00acW\2\u02d9\u02da\5j\66\2\u02da\u02db\5|?\2\u02db{\3\2\2\2" +
      "\u02dc\u02df\5\\/\2\u02dd\u02de\7B\2\2\u02de\u02e0\5z>\2\u02df\u02dd\3" +
      "\2\2\2\u02df\u02e0\3\2\2\2\u02e0\u02e4\3\2\2\2\u02e1\u02e2\7h\2\2\u02e2" +
      "\u02e4\5\\/\2\u02e3\u02dc\3\2\2\2\u02e3\u02e1\3\2\2\2\u02e4}\3\2\2\2\u02e5" +
      "\u02e6\5\u00a4S\2\u02e6\177\3\2\2\2\u02e7\u02e8\5\u00a4S\2\u02e8\u0081" +
      "\3\2\2\2\u02e9\u02ee\7f\2\2\u02ea\u02eb\7C\2\2\u02eb\u02ed\7f\2\2\u02ec" +
      "\u02ea\3\2\2\2\u02ed\u02f0\3\2\2\2\u02ee\u02ec\3\2\2\2\u02ee\u02ef\3\2" +
      "\2\2\u02ef\u0083\3\2\2\2\u02f0\u02ee\3\2\2\2\u02f1\u02f2\t\6\2\2\u02f2" +
      "\u0085\3\2\2\2\u02f3\u02f5\5\u0088E\2\u02f4\u02f3\3\2\2\2\u02f5\u02f6" +
      "\3\2\2\2\u02f6\u02f4\3\2\2\2\u02f6\u02f7\3\2\2\2\u02f7\u0087\3\2\2\2\u02f8" +
      "\u02f9\7g\2\2\u02f9\u0300\5\u008aF\2\u02fa\u02fd\7;\2\2\u02fb\u02fe\5" +
      "\u008cG\2\u02fc\u02fe\5\u0090I\2\u02fd\u02fb\3\2\2\2\u02fd\u02fc\3\2\2" +
      "\2\u02fd\u02fe\3\2\2\2\u02fe\u02ff\3\2\2\2\u02ff\u0301\7<\2\2\u0300\u02fa" +
      "\3\2\2\2\u0300\u0301\3\2\2\2\u0301\u0089\3\2\2\2\u0302\u0307\7f\2\2\u0303" +
      "\u0304\7C\2\2\u0304\u0306\7f\2\2\u0305\u0303\3\2\2\2\u0306\u0309\3\2\2" +
      "\2\u0307\u0305\3\2\2\2\u0307\u0308\3\2\2\2\u0308\u008b\3\2\2\2\u0309\u0307" +
      "\3\2\2\2\u030a\u030f\5\u008eH\2\u030b\u030c\7B\2\2\u030c\u030e\5\u008e" +
      "H\2\u030d\u030b\3\2\2\2\u030e\u0311\3\2\2\2\u030f\u030d\3\2\2\2\u030f" +
      "\u0310\3\2\2\2\u0310\u008d\3\2\2\2\u0311\u030f\3\2\2\2\u0312\u0313\7f" +
      "\2\2\u0313\u0314\7D\2\2\u0314\u0315\5\u0090I\2\u0315\u008f\3\2\2\2\u0316" +
      "\u031a\5\u00d6l\2\u0317\u031a\5\u0088E\2\u0318\u031a\5\u0092J\2\u0319" +
      "\u0316\3\2\2\2\u0319\u0317\3\2\2\2\u0319\u0318\3\2\2\2\u031a\u0091\3\2" +
      "\2\2\u031b\u0324\7=\2\2\u031c\u0321\5\u0090I\2\u031d\u031e\7B\2\2\u031e" +
      "\u0320\5\u0090I\2\u031f\u031d\3\2\2\2\u0320\u0323\3\2\2\2\u0321\u031f" +
      "\3\2\2\2\u0321\u0322\3\2\2\2\u0322\u0325\3\2\2\2\u0323\u0321\3\2\2\2\u0324" +
      "\u031c\3\2\2\2\u0324\u0325\3\2\2\2\u0325\u0327\3\2\2\2\u0326\u0328\7B" +
      "\2\2\u0327\u0326\3\2\2\2\u0327\u0328\3\2\2\2\u0328\u0329\3\2\2\2\u0329" +
      "\u032a\7>\2\2\u032a\u0093\3\2\2\2\u032b\u032c\7g\2\2\u032c\u032d\7\36" +
      "\2\2\u032d\u032e\7f\2\2\u032e\u032f\5\u0096L\2\u032f\u0095\3\2\2\2\u0330" +
      "\u0334\7=\2\2\u0331\u0333\5\u0098M\2\u0332\u0331\3\2\2\2\u0333\u0336\3" +
      "\2\2\2\u0334\u0332\3\2\2\2\u0334\u0335\3\2\2\2\u0335\u0337\3\2\2\2\u0336" +
      "\u0334\3\2\2\2\u0337\u0338\7>\2\2\u0338\u0097\3\2\2\2\u0339\u033a\5\20" +
      "\t\2\u033a\u033b\5\u009aN\2\u033b\u033e\3\2\2\2\u033c\u033e\7A\2\2\u033d" +
      "\u0339\3\2\2\2\u033d\u033c\3\2\2\2\u033e\u0099\3\2\2\2\u033f\u0340\5j" +
      "\66\2\u0340\u0341\5\u009cO\2\u0341\u0342\7A\2\2\u0342\u0354\3\2\2\2\u0343" +
      "\u0345\5\24\13\2\u0344\u0346\7A\2\2\u0345\u0344\3\2\2\2\u0345\u0346\3" +
      "\2\2\2\u0346\u0354\3\2\2\2\u0347\u0349\5(\25\2\u0348\u034a\7A\2\2\u0349" +
      "\u0348\3\2\2\2\u0349\u034a\3\2\2\2\u034a\u0354\3\2\2\2\u034b\u034d\5\34" +
      "\17\2\u034c\u034e\7A\2\2\u034d\u034c\3\2\2\2\u034d\u034e\3\2\2\2\u034e" +
      "\u0354\3\2\2\2\u034f\u0351\5\u0094K\2\u0350\u0352\7A\2\2\u0351\u0350\3" +
      "\2\2\2\u0351\u0352\3\2\2\2\u0352\u0354\3\2\2\2\u0353\u033f\3\2\2\2\u0353" +
      "\u0343\3\2\2\2\u0353\u0347\3\2\2\2\u0353\u034b\3\2\2\2\u0353\u034f\3\2" +
      "\2\2\u0354\u009b\3\2\2\2\u0355\u0358\5\u009eP\2\u0356\u0358\5\u00a0Q\2" +
      "\u0357\u0355\3\2\2\2\u0357\u0356\3\2\2\2\u0358\u009d\3\2\2\2\u0359\u035a" +
      "\7f\2\2\u035a\u035b\7;\2\2\u035b\u035d\7<\2\2\u035c\u035e\5\u00a2R\2\u035d" +
      "\u035c\3\2\2\2\u035d\u035e\3\2\2\2\u035e\u009f\3\2\2\2\u035f\u0360\5T" +
      "+\2\u0360\u00a1\3\2\2\2\u0361\u0362\7\16\2\2\u0362\u0363\5\u0090I\2\u0363" +
      "\u00a3\3\2\2\2\u0364\u0368\7=\2\2\u0365\u0367\5\u00a6T\2\u0366\u0365\3" +
      "\2\2\2\u0367\u036a\3\2\2\2\u0368\u0366\3\2\2\2\u0368\u0369\3\2\2\2\u0369" +
      "\u036b\3\2\2\2\u036a\u0368\3\2\2\2\u036b\u036c\7>\2\2\u036c\u00a5\3\2" +
      "\2\2\u036d\u0371\5\u00a8U\2\u036e\u0371\5\n\6\2\u036f\u0371\5\u00aeX\2" +
      "\u0370\u036d\3\2\2\2\u0370\u036e\3\2\2\2\u0370\u036f\3\2\2\2\u0371\u00a7" +
      "\3\2\2\2\u0372\u0373\5\u00aaV\2\u0373\u0374\7A\2\2\u0374\u00a9\3\2\2\2" +
      "\u0375\u0376\5\u00acW\2\u0376\u0377\5j\66\2\u0377\u0378\5T+\2\u0378\u00ab" +
      "\3\2\2\2\u0379\u037b\5p9\2\u037a\u0379\3\2\2\2\u037b\u037e\3\2\2\2\u037c" +
      "\u037a\3\2\2\2\u037c\u037d\3\2\2\2\u037d\u00ad\3\2\2\2\u037e\u037c\3\2" +
      "\2\2\u037f\u03d6\5\u00a4S\2\u0380\u0381\7\4\2\2\u0381\u0384\5\u00d6l\2" +
      "\u0382\u0383\7J\2\2\u0383\u0385\5\u00d6l\2\u0384\u0382\3\2\2\2\u0384\u0385" +
      "\3\2\2\2\u0385\u0386\3\2\2\2\u0386\u0387\7A\2\2\u0387\u03d6\3\2\2\2\u0388" +
      "\u0389\7\30\2\2\u0389\u038a\5\u00ceh\2\u038a\u038d\5\u00aeX\2\u038b\u038c" +
      "\7\21\2\2\u038c\u038e\5\u00aeX\2\u038d\u038b\3\2\2\2\u038d\u038e\3\2\2" +
      "\2\u038e\u03d6\3\2\2\2\u038f\u0390\7\27\2\2\u0390\u0391\7;\2\2\u0391\u0392" +
      "\5\u00c6d\2\u0392\u0393\7<\2\2\u0393\u0394\5\u00aeX\2\u0394\u03d6\3\2" +
      "\2\2\u0395\u0396\7\64\2\2\u0396\u0397\5\u00ceh\2\u0397\u0398\5\u00aeX" +
      "\2\u0398\u03d6\3\2\2\2\u0399\u039a\7\17\2\2\u039a\u039b\5\u00aeX\2\u039b" +
      "\u039c\7\64\2\2\u039c\u039d\5\u00ceh\2\u039d\u039e\7A\2\2\u039e\u03d6" +
      "\3\2\2\2\u039f\u03a0\7\61\2\2\u03a0\u03a6\5\u00a4S\2\u03a1\u03a3\5\u00b0" +
      "Y\2\u03a2\u03a4\5\u00b6\\\2\u03a3\u03a2\3\2\2\2\u03a3\u03a4\3\2\2\2\u03a4" +
      "\u03a7\3\2\2\2\u03a5\u03a7\5\u00b6\\\2\u03a6\u03a1\3\2\2\2\u03a6\u03a5" +
      "\3\2\2\2\u03a7\u03d6\3\2\2\2\u03a8\u03a9\7\61\2\2\u03a9\u03aa\5\u00b8" +
      "]\2\u03aa\u03ac\5\u00a4S\2\u03ab\u03ad\5\u00b0Y\2\u03ac\u03ab\3\2\2\2" +
      "\u03ac\u03ad\3\2\2\2\u03ad\u03af\3\2\2\2\u03ae\u03b0\5\u00b6\\\2\u03af" +
      "\u03ae\3\2\2\2\u03af\u03b0\3\2\2\2\u03b0\u03d6\3\2\2\2\u03b1\u03b2\7+" +
      "\2\2\u03b2\u03b3\5\u00ceh\2\u03b3\u03b4\7=\2\2\u03b4\u03b5\5\u00c0a\2" +
      "\u03b5\u03b6\7>\2\2\u03b6\u03d6\3\2\2\2\u03b7\u03b8\7,\2\2\u03b8\u03b9" +
      "\5\u00ceh\2\u03b9\u03ba\5\u00a4S\2\u03ba\u03d6\3\2\2\2\u03bb\u03bd\7&" +
      "\2\2\u03bc\u03be\5\u00d6l\2\u03bd\u03bc\3\2\2\2\u03bd\u03be\3\2\2\2\u03be" +
      "\u03bf\3\2\2\2\u03bf\u03d6\7A\2\2\u03c0\u03c1\7.\2\2\u03c1\u03c2\5\u00d6" +
      "l\2\u03c2\u03c3\7A\2\2\u03c3\u03d6\3\2\2\2\u03c4\u03c6\7\6\2\2\u03c5\u03c7" +
      "\7f\2\2\u03c6\u03c5\3\2\2\2\u03c6\u03c7\3\2\2\2\u03c7\u03c8\3\2\2\2\u03c8" +
      "\u03d6\7A\2\2\u03c9\u03cb\7\r\2\2\u03ca\u03cc\7f\2\2\u03cb\u03ca\3\2\2" +
      "\2\u03cb\u03cc\3\2\2\2\u03cc\u03cd\3\2\2\2\u03cd\u03d6\7A\2\2\u03ce\u03d6" +
      "\7A\2\2\u03cf\u03d0\5\u00d2j\2\u03d0\u03d1\7A\2\2\u03d1\u03d6\3\2\2\2" +
      "\u03d2\u03d3\7f\2\2\u03d3\u03d4\7J\2\2\u03d4\u03d6\5\u00aeX\2\u03d5\u037f" +
      "\3\2\2\2\u03d5\u0380\3\2\2\2\u03d5\u0388\3\2\2\2\u03d5\u038f\3\2\2\2\u03d5" +
      "\u0395\3\2\2\2\u03d5\u0399\3\2\2\2\u03d5\u039f\3\2\2\2\u03d5\u03a8\3\2" +
      "\2\2\u03d5\u03b1\3\2\2\2\u03d5\u03b7\3\2\2\2\u03d5\u03bb\3\2\2\2\u03d5" +
      "\u03c0\3\2\2\2\u03d5\u03c4\3\2\2\2\u03d5\u03c9\3\2\2\2\u03d5\u03ce\3\2" +
      "\2\2\u03d5\u03cf\3\2\2\2\u03d5\u03d2\3\2\2\2\u03d6\u00af\3\2\2\2\u03d7" +
      "\u03d9\5\u00b2Z\2\u03d8\u03d7\3\2\2\2\u03d9\u03da\3\2\2\2\u03da\u03d8" +
      "\3\2\2\2\u03da\u03db\3\2\2\2\u03db\u00b1\3\2\2\2\u03dc\u03dd\7\t\2\2\u03dd" +
      "\u03de\7;\2\2\u03de\u03df\5\u00acW\2\u03df\u03e0\5\u00b4[\2\u03e0\u03e1" +
      "\7f\2\2\u03e1\u03e2\7<\2\2\u03e2\u03e3\5\u00a4S\2\u03e3\u00b3\3\2\2\2" +
      "\u03e4\u03e9\5\u0082B\2\u03e5\u03e6\7X\2\2\u03e6\u03e8\5\u0082B\2\u03e7" +
      "\u03e5\3\2\2\2\u03e8\u03eb\3\2\2\2\u03e9\u03e7\3\2\2\2\u03e9\u03ea\3\2" +
      "\2\2\u03ea\u00b5\3\2\2\2\u03eb\u03e9\3\2\2\2\u03ec\u03ed\7\25\2\2\u03ed" +
      "\u03ee\5\u00a4S\2\u03ee\u00b7\3\2\2\2\u03ef\u03f0\7;\2\2\u03f0\u03f2\5" +
      "\u00ba^\2\u03f1\u03f3\7A\2\2\u03f2\u03f1\3\2\2\2\u03f2\u03f3\3\2\2\2\u03f3" +
      "\u03f4\3\2\2\2\u03f4\u03f5\7<\2\2\u03f5\u00b9\3\2\2\2\u03f6\u03fb\5\u00bc" +
      "_\2\u03f7\u03f8\7A\2\2\u03f8\u03fa\5\u00bc_\2\u03f9\u03f7\3\2\2\2\u03fa" +
      "\u03fd\3\2\2\2\u03fb\u03f9\3\2\2\2\u03fb\u03fc\3\2\2\2\u03fc\u00bb\3\2" +
      "\2\2\u03fd\u03fb\3\2\2\2\u03fe\u03ff\5\u00acW\2\u03ff\u0400\5l\67\2\u0400" +
      "\u0401\5\\/\2\u0401\u0402\7D\2\2\u0402\u0403\5\u00d6l\2\u0403\u00bd\3" +
      "\2\2\2\u0404\u0405\5\u00acW\2\u0405\u0406\5j\66\2\u0406\u0407\5\\/\2\u0407" +
      "\u00bf\3\2\2\2\u0408\u040a\5\u00c2b\2\u0409\u0408\3\2\2\2\u040a\u040d" +
      "\3\2\2\2\u040b\u0409\3\2\2\2\u040b\u040c\3\2\2\2\u040c\u00c1\3\2\2\2\u040d" +
      "\u040b\3\2\2\2\u040e\u0410\5\u00c4c\2\u040f\u040e\3\2\2\2\u0410\u0411" +
      "\3\2\2\2\u0411\u040f\3\2\2\2\u0411\u0412\3\2\2\2\u0412\u0416\3\2\2\2\u0413" +
      "\u0415\5\u00a6T\2\u0414\u0413\3\2\2\2\u0415\u0418\3\2\2\2\u0416\u0414" +
      "\3\2\2\2\u0416\u0417\3\2\2\2\u0417\u00c3\3\2\2\2\u0418\u0416\3\2\2\2\u0419" +
      "\u041a\7\b\2\2\u041a\u041b\5\u00d4k\2\u041b\u041c\7J\2\2\u041c\u0424\3" +
      "\2\2\2\u041d\u041e\7\b\2\2\u041e\u041f\5f\64\2\u041f\u0420\7J\2\2\u0420" +
      "\u0424\3\2\2\2\u0421\u0422\7\16\2\2\u0422\u0424\7J\2\2\u0423\u0419\3\2" +
      "\2\2\u0423\u041d\3\2\2\2\u0423\u0421\3\2\2\2\u0424\u00c5\3\2\2\2\u0425" +
      "\u0432\5\u00caf\2\u0426\u0428\5\u00c8e\2\u0427\u0426\3\2\2\2\u0427\u0428" +
      "\3\2\2\2\u0428\u0429\3\2\2\2\u0429\u042b\7A\2\2\u042a\u042c\5\u00d6l\2" +
      "\u042b\u042a\3\2\2\2\u042b\u042c\3\2\2\2\u042c\u042d\3\2\2\2\u042d\u042f" +
      "\7A\2\2\u042e\u0430\5\u00ccg\2\u042f\u042e\3\2\2\2\u042f\u0430\3\2\2\2" +
      "\u0430\u0432\3\2\2\2\u0431\u0425\3\2\2\2\u0431\u0427\3\2\2\2\u0432\u00c7" +
      "\3\2\2\2\u0433\u0436\5\u00aaV\2\u0434\u0436\5\u00d0i\2\u0435\u0433\3\2" +
      "\2\2\u0435\u0434\3\2\2\2\u0436\u00c9\3\2\2\2\u0437\u0438\5\u00acW\2\u0438" +
      "\u0439\5j\66\2\u0439\u043a\7f\2\2\u043a\u043b\7J\2\2\u043b\u043c\5\u00d6" +
      "l\2\u043c\u00cb\3\2\2\2\u043d\u043e\5\u00d0i\2\u043e\u00cd\3\2\2\2\u043f" +
      "\u0440\7;\2\2\u0440\u0441\5\u00d6l\2\u0441\u0442\7<\2\2\u0442\u00cf\3" +
      "\2\2\2\u0443\u0448\5\u00d6l\2\u0444\u0445\7B\2\2\u0445\u0447\5\u00d6l" +
      "\2\u0446\u0444\3\2\2\2\u0447\u044a\3\2\2\2\u0448\u0446\3\2\2\2\u0448\u0449" +
      "\3\2\2\2\u0449\u00d1\3\2\2\2\u044a\u0448\3\2\2\2\u044b\u044c\5\u00d6l" +
      "\2\u044c\u00d3\3\2\2\2\u044d\u044e\5\u00d6l\2\u044e\u00d5\3\2\2\2\u044f" +
      "\u0450\bl\1\2\u0450\u045d\5\u00d8m\2\u0451\u0452\7!\2\2\u0452\u045d\5" +
      "\u00dan\2\u0453\u0454\7;\2\2\u0454\u0455\5j\66\2\u0455\u0456\7<\2\2\u0456" +
      "\u0457\5\u00d6l\24\u0457\u045d\3\2\2\2\u0458\u0459\t\7\2\2\u0459\u045d" +
      "\5\u00d6l\21\u045a\u045b\t\b\2\2\u045b\u045d\5\u00d6l\20\u045c\u044f\3" +
      "\2\2\2\u045c\u0451\3\2\2\2\u045c\u0453\3\2\2\2\u045c\u0458\3\2\2\2\u045c" +
      "\u045a\3\2\2\2\u045d\u04b3\3\2\2\2\u045e\u045f\f\17\2\2\u045f\u0460\t" +
      "\t\2\2\u0460\u04b2\5\u00d6l\20\u0461\u0462\f\16\2\2\u0462\u0463\t\n\2" +
      "\2\u0463\u04b2\5\u00d6l\17\u0464\u046c\f\r\2\2\u0465\u0466\7F\2\2\u0466" +
      "\u046d\7F\2\2\u0467\u0468\7E\2\2\u0468\u0469\7E\2\2\u0469\u046d\7E\2\2" +
      "\u046a\u046b\7E\2\2\u046b\u046d\7E\2\2\u046c\u0465\3\2\2\2\u046c\u0467" +
      "\3\2\2\2\u046c\u046a\3\2\2\2\u046d\u046e\3\2\2\2\u046e\u04b2\5\u00d6l" +
      "\16\u046f\u0470\f\f\2\2\u0470\u0471\t\13\2\2\u0471\u04b2\5\u00d6l\r\u0472" +
      "\u0473\f\n\2\2\u0473\u0474\t\f\2\2\u0474\u04b2\5\u00d6l\13\u0475\u0476" +
      "\f\t\2\2\u0476\u0477\7W\2\2\u0477\u04b2\5\u00d6l\n\u0478\u0479\f\b\2\2" +
      "\u0479\u047a\7Y\2\2\u047a\u04b2\5\u00d6l\t\u047b\u047c\f\7\2\2\u047c\u047d" +
      "\7X\2\2\u047d\u04b2\5\u00d6l\b\u047e\u047f\f\6\2\2\u047f\u0480\7O\2\2" +
      "\u0480\u04b2\5\u00d6l\7\u0481\u0482\f\5\2\2\u0482\u0483\7P\2\2\u0483\u04b2" +
      "\5\u00d6l\6\u0484\u0485\f\4\2\2\u0485\u0486\7I\2\2\u0486\u0487\5\u00d6" +
      "l\2\u0487\u0488\7J\2\2\u0488\u0489\5\u00d6l\5\u0489\u04b2\3\2\2\2\u048a" +
      "\u048b\f\3\2\2\u048b\u048c\t\r\2\2\u048c\u04b2\5\u00d6l\3\u048d\u048e" +
      "\f\33\2\2\u048e\u048f\7C\2\2\u048f\u04b2\7f\2\2\u0490\u0491\f\32\2\2\u0491" +
      "\u0492\7C\2\2\u0492\u04b2\7-\2\2\u0493\u0494\f\31\2\2\u0494\u0495\7C\2" +
      "\2\u0495\u0497\7!\2\2\u0496\u0498\5\u00e6t\2\u0497\u0496\3\2\2\2\u0497" +
      "\u0498\3\2\2\2\u0498\u0499\3\2\2\2\u0499\u04b2\5\u00dep\2\u049a\u049b" +
      "\f\30\2\2\u049b\u049c\7C\2\2\u049c\u049d\7*\2\2\u049d\u04b2\5\u00ecw\2" +
      "\u049e\u049f\f\27\2\2\u049f\u04a0\7C\2\2\u04a0\u04b2\5\u00e4s\2\u04a1" +
      "\u04a2\f\25\2\2\u04a2\u04a3\7?\2\2\u04a3\u04a4\5\u00d6l\2\u04a4\u04a5" +
      "\7@\2\2\u04a5\u04b2\3\2\2\2\u04a6\u04a7\f\23\2\2\u04a7\u04b2\t\16\2\2" +
      "\u04a8\u04a9\f\22\2\2\u04a9\u04ab\7;\2\2\u04aa\u04ac\5\u00d0i\2\u04ab" +
      "\u04aa\3\2\2\2\u04ab\u04ac\3\2\2\2\u04ac\u04ad\3\2\2\2\u04ad\u04b2\7<" +
      "\2\2\u04ae\u04af\f\13\2\2\u04af\u04b0\7\34\2\2\u04b0\u04b2\5j\66\2\u04b1" +
      "\u045e\3\2\2\2\u04b1\u0461\3\2\2\2\u04b1\u0464\3\2\2\2\u04b1\u046f\3\2" +
      "\2\2\u04b1\u0472\3\2\2\2\u04b1\u0475\3\2\2\2\u04b1\u0478\3\2\2\2\u04b1" +
      "\u047b\3\2\2\2\u04b1\u047e\3\2\2\2\u04b1\u0481\3\2\2\2\u04b1\u0484\3\2" +
      "\2\2\u04b1\u048a\3\2\2\2\u04b1\u048d\3\2\2\2\u04b1\u0490\3\2\2\2\u04b1" +
      "\u0493\3\2\2\2\u04b1\u049a\3\2\2\2\u04b1\u049e\3\2\2\2\u04b1\u04a1\3\2" +
      "\2\2\u04b1\u04a6\3\2\2\2\u04b1\u04a8\3\2\2\2\u04b1\u04ae\3\2\2\2\u04b2" +
      "\u04b5\3\2\2\2\u04b3\u04b1\3\2\2\2\u04b3\u04b4\3\2\2\2\u04b4\u00d7\3\2" +
      "\2\2\u04b5\u04b3\3\2\2\2\u04b6\u04b7\7;\2\2\u04b7\u04b8\5\u00d6l\2\u04b8" +
      "\u04b9\7<\2\2\u04b9\u04cc\3\2\2\2\u04ba\u04cc\7-\2\2\u04bb\u04cc\7*\2" +
      "\2\u04bc\u04cc\5\u0084C\2\u04bd\u04cc\7f\2\2\u04be\u04bf\5j\66\2\u04bf" +
      "\u04c0\7C\2\2\u04c0\u04c1\7\13\2\2\u04c1\u04cc\3\2\2\2\u04c2\u04c3\7\62" +
      "\2\2\u04c3\u04c4\7C\2\2\u04c4\u04cc\7\13\2\2\u04c5\u04c9\5\u00e6t\2\u04c6" +
      "\u04ca\5\u00eex\2\u04c7\u04c8\7-\2\2\u04c8\u04ca\5\u00f0y\2\u04c9\u04c6" +
      "\3\2\2\2\u04c9\u04c7\3\2\2\2\u04ca\u04cc\3\2\2\2\u04cb\u04b6\3\2\2\2\u04cb" +
      "\u04ba\3\2\2\2\u04cb\u04bb\3\2\2\2\u04cb\u04bc\3\2\2\2\u04cb\u04bd\3\2" +
      "\2\2\u04cb\u04be\3\2\2\2\u04cb\u04c2\3\2\2\2\u04cb\u04c5\3\2\2\2\u04cc" +
      "\u00d9\3\2\2\2\u04cd\u04ce\5\u00e6t\2\u04ce\u04cf\5\u00dco\2\u04cf\u04d0" +
      "\5\u00e2r\2\u04d0\u04d7\3\2\2\2\u04d1\u04d4\5\u00dco\2\u04d2\u04d5\5\u00e0" +
      "q\2\u04d3\u04d5\5\u00e2r\2\u04d4\u04d2\3\2\2\2\u04d4\u04d3\3\2\2\2\u04d5" +
      "\u04d7\3\2\2\2\u04d6\u04cd\3\2\2\2\u04d6\u04d1\3\2\2\2\u04d7\u00db\3\2" +
      "\2\2\u04d8\u04da\7f\2\2\u04d9\u04db\5\u00e8u\2\u04da\u04d9\3\2\2\2\u04da" +
      "\u04db\3\2\2\2\u04db\u04e3\3\2\2\2\u04dc\u04dd\7C\2\2\u04dd\u04df\7f\2" +
      "\2\u04de\u04e0\5\u00e8u\2\u04df\u04de\3\2\2\2\u04df\u04e0\3\2\2\2\u04e0" +
      "\u04e2\3\2\2\2\u04e1\u04dc\3\2\2\2\u04e2\u04e5\3\2\2\2\u04e3\u04e1\3\2" +
      "\2\2\u04e3\u04e4\3\2\2\2\u04e4\u04e8\3\2\2\2\u04e5\u04e3\3\2\2\2\u04e6" +
      "\u04e8\5n8\2\u04e7\u04d8\3\2\2\2\u04e7\u04e6\3\2\2\2\u04e8\u00dd\3\2\2" +
      "\2\u04e9\u04eb\7f\2\2\u04ea\u04ec\5\u00eav\2\u04eb\u04ea\3\2\2\2\u04eb" +
      "\u04ec\3\2\2\2\u04ec\u04ed\3\2\2\2\u04ed\u04ee\5\u00e2r\2\u04ee\u00df" +
      "\3\2\2\2\u04ef\u050b\7?\2\2\u04f0\u04f5\7@\2\2\u04f1\u04f2\7?\2\2\u04f2" +
      "\u04f4\7@\2\2\u04f3\u04f1\3\2\2\2\u04f4\u04f7\3\2\2\2\u04f5\u04f3\3\2" +
      "\2\2\u04f5\u04f6\3\2\2\2\u04f6\u04f8\3\2\2\2\u04f7\u04f5\3\2\2\2\u04f8" +
      "\u050c\5`\61\2\u04f9\u04fa\5\u00d6l\2\u04fa\u0501\7@\2\2\u04fb\u04fc\7" +
      "?\2\2\u04fc\u04fd\5\u00d6l\2\u04fd\u04fe\7@\2\2\u04fe\u0500\3\2\2\2\u04ff" +
      "\u04fb\3\2\2\2\u0500\u0503\3\2\2\2\u0501\u04ff\3\2\2\2\u0501\u0502\3\2" +
      "\2\2\u0502\u0508\3\2\2\2\u0503\u0501\3\2\2\2\u0504\u0505\7?\2\2\u0505" +
      "\u0507\7@\2\2\u0506\u0504\3\2\2\2\u0507\u050a\3\2\2\2\u0508\u0506\3\2" +
      "\2\2\u0508\u0509\3\2\2\2\u0509\u050c\3\2\2\2\u050a\u0508\3\2\2\2\u050b" +
      "\u04f0\3\2\2\2\u050b\u04f9\3\2\2\2\u050c\u00e1\3\2\2\2\u050d\u050f\5\u00f0" +
      "y\2\u050e\u0510\5,\27\2\u050f\u050e\3\2\2\2\u050f\u0510\3\2\2\2\u0510" +
      "\u00e3\3\2\2\2\u0511\u0512\5\u00e6t\2\u0512\u0513\5\u00eex\2\u0513\u00e5" +
      "\3\2\2\2\u0514\u0515\7F\2\2\u0515\u0516\5*\26\2\u0516\u0517\7E\2\2\u0517" +
      "\u00e7\3\2\2\2\u0518\u0519\7F\2\2\u0519\u051c\7E\2\2\u051a\u051c\5r:\2" +
      "\u051b\u0518\3\2\2\2\u051b\u051a\3\2\2\2\u051c\u00e9\3\2\2\2\u051d\u051e" +
      "\7F\2\2\u051e\u0521\7E\2\2\u051f\u0521\5\u00e6t\2\u0520\u051d\3\2\2\2" +
      "\u0520\u051f\3\2\2\2\u0521\u00eb\3\2\2\2\u0522\u0529\5\u00f0y\2\u0523" +
      "\u0524\7C\2\2\u0524\u0526\7f\2\2\u0525\u0527\5\u00f0y\2\u0526\u0525\3" +
      "\2\2\2\u0526\u0527\3\2\2\2\u0527\u0529\3\2\2\2\u0528\u0522\3\2\2\2\u0528" +
      "\u0523\3\2\2\2\u0529\u00ed\3\2\2\2\u052a\u052b\7*\2\2\u052b\u052f\5\u00ec" +
      "w\2\u052c\u052d\7f\2\2\u052d\u052f\5\u00f0y\2\u052e\u052a\3\2\2\2\u052e" +
      "\u052c\3\2\2\2\u052f\u00ef\3\2\2\2\u0530\u0532\7;\2\2\u0531\u0533\5\u00d0" +
      "i\2\u0532\u0531\3\2\2\2\u0532\u0533\3\2\2\2\u0533\u0534\3\2\2\2\u0534" +
      "\u0535\7<\2\2\u0535\u00f1\3\2\2\2\u009a\u00f7\u00fd\u0104\u0107\u010c" +
      "\u0111\u0117\u011b\u0123\u0128\u012e\u0133\u0138\u013d\u0142\u0147\u014c" +
      "\u0150\u0154\u015e\u0166\u016d\u0174\u017a\u017d\u0180\u0189\u018d\u0191" +
      "\u0194\u019a\u019f\u01a4\u01a8\u01b1\u01b8\u01c1\u01c8\u01ce\u01d9\u01de" +
      "\u01e5\u01eb\u01f7\u0200\u020a\u0211\u0216\u021a\u021f\u0223\u022a\u022f" +
      "\u0236\u023e\u0245\u0251\u0257\u025e\u0265\u0270\u0275\u027d\u0281\u0283" +
      "\u0289\u0296\u029e\u02a1\u02a5\u02aa\u02ae\u02b5\u02bd\u02c6\u02c8\u02cf" +
      "\u02d4\u02df\u02e3\u02ee\u02f6\u02fd\u0300\u0307\u030f\u0319\u0321\u0324" +
      "\u0327\u0334\u033d\u0345\u0349\u034d\u0351\u0353\u0357\u035d\u0368\u0370" +
      "\u037c\u0384\u038d\u03a3\u03a6\u03ac\u03af\u03bd\u03c6\u03cb\u03d5\u03da" +
      "\u03e9\u03f2\u03fb\u040b\u0411\u0416\u0423\u0427\u042b\u042f\u0431\u0435" +
      "\u0448\u045c\u046c\u0497\u04ab\u04b1\u04b3\u04c9\u04cb\u04d4\u04d6\u04da" +
      "\u04df\u04e3\u04e7\u04eb\u04f5\u0501\u0508\u050b\u050f\u051b\u0520\u0526" +
      "\u0528\u052e\u0532";
  public static final ATN _ATN =
    new ATNDeserializer().deserialize(_serializedATN.toCharArray());

  static {
  }
}