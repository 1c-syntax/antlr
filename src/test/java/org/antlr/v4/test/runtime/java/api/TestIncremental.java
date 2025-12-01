/*
 * This file is a part of ANTLR.
 *
 * Copyright (c) 2012-2025 The ANTLR Project. All rights reserved.
 * Copyright (c) 2025 Valery Maximov <maximovvalery@gmail.com> and contributors
 *
 * Use of this file is governed by the BSD-3-Clause license that
 * can be found in the LICENSE.txt file in the project root.
 */
package org.antlr.v4.test.runtime.java.api;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonToken;
import org.antlr.v4.runtime.IncrementalParser;
import org.antlr.v4.runtime.IncrementalParserData;
import org.antlr.v4.runtime.IncrementalParserRuleContext;
import org.antlr.v4.runtime.IncrementalTokenStream;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenChange;
import org.antlr.v4.runtime.TokenChangeType;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.xpath.XPath;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestIncremental {

  String SAMPLE_TEXT_1 = "foo 5555 foo 5555 foo";
  String EXPECTED_TREE_1 =
    "(program (identifier foo) (digits 5555) (identifier foo) (digits 5555) (identifier foo))";

  String SAMPLE_TEXT_2 = "foo 5555 5555 foo";
  String EXPECTED_TREE_2 =
    "(program (identifier foo) (digits 5555) (digits 5555) (identifier foo))";

  String SAMPLE_TEXT_3 = "foo 5555 foo 5555 foo foo";
  String EXPECTED_TREE_3 =
    "(program (identifier foo) (digits 5555) (identifier foo) (digits 5555) (identifier foo) (identifier foo))";

  String JAVA_PROGRAM_1 =
    "\npublic class HelloWorld {\n\n    public static void main(String[] args) {\n        // Prints \"Hello, World\" to the terminal window.\n        System.out.println(\"Hello, World\");\n    }\n\n}\n";
  String JAVA_EXPECTED_TREE_1 =
    "(compilationUnit (typeDeclaration (classOrInterfaceDeclaration (classOrInterfaceModifiers (classOrInterfaceModifier public)) (classDeclaration (normalClassDeclaration class HelloWorld (classBody { (classBodyDeclaration (modifiers (modifier public) (modifier static)) (memberDecl void main (voidMethodDeclaratorRest (formalParameters ( (formalParameterDecls variableModifiers (type (classOrInterfaceType String) [ ]) (formalParameterDeclsRest (variableDeclaratorId args))) )) (methodBody (block { (blockStatement (statement (statementExpression (expression (expression (expression (expression (primary System)) . out) . println) ( (expressionList (expression (primary (literal \"Hello, World\")))) ))) ;)) }))))) }))))) <EOF>)";
  String JAVA_PROGRAM_2 =
    "\npublic class HelloWorld {\n\n    public static void main(String[] args) {\n        // Prints \"Hello, World\" to the terminal window.\n        System.out.println(\"Hello\");\n    }\n\n}\n";
  String JAVA_EXPECTED_TREE_2 =
    "(compilationUnit (typeDeclaration (classOrInterfaceDeclaration (classOrInterfaceModifiers (classOrInterfaceModifier public)) (classDeclaration (normalClassDeclaration class HelloWorld (classBody { (classBodyDeclaration (modifiers (modifier public) (modifier static)) (memberDecl void main (voidMethodDeclaratorRest (formalParameters ( (formalParameterDecls variableModifiers (type (classOrInterfaceType String) [ ]) (formalParameterDeclsRest (variableDeclaratorId args))) )) (methodBody (block { (blockStatement (statement (statementExpression (expression (expression (expression (expression (primary System)) . out) . println) ( (expressionList (expression (primary (literal \"Hello\")))) ))) ;)) }))))) }))))) <EOF>)";

  /**
   * This test verifies the behavior of the incremental parser as a non-incremental parser.
   */
  @Test
  public void testBasicIncrementalParse() {
    TestIncrementalBasicLexer lexer = new TestIncrementalBasicLexer(CharStreams.fromString(SAMPLE_TEXT_1));
    TestIncrementalBasicParser parser = new TestIncrementalBasicParser(new IncrementalTokenStream(lexer));
    int startingEpoch = parser.getParserEpoch();

    IncrementalParserRuleContext tree = parser.program();
    assertEquals(EXPECTED_TREE_1, tree.toStringTree(parser));
    // Should have been created by the first parser.
    assertEquals(startingEpoch, tree.epoch);
  }

  /**
   * This test reparses text and asserts that the context was reused.
   */
  @Test
  public void testBasicIncrementalReparse() {
    TestIncrementalBasicLexer lexer = new TestIncrementalBasicLexer(CharStreams.fromString(SAMPLE_TEXT_1));
    IncrementalTokenStream tokenStream = new IncrementalTokenStream(lexer);
    TestIncrementalBasicParser parser = new TestIncrementalBasicParser(tokenStream);
    int startingEpoch = parser.getParserEpoch();
    IncrementalParserRuleContext firstTree = parser.program();
    assertEquals(EXPECTED_TREE_1, firstTree.toStringTree(parser));
    // Should have been created by the first parser.
    assertEquals(startingEpoch, firstTree.epoch);

    // Parse the same text with the old tree.
    lexer = new TestIncrementalBasicLexer(CharStreams.fromString(SAMPLE_TEXT_1));
    tokenStream = new IncrementalTokenStream(lexer);
    IncrementalParserData parserData = new IncrementalParserData(tokenStream, new ArrayList<>(), firstTree);
    parser = new TestIncrementalBasicParser(tokenStream, parserData);
    int secondEpoch = parser.getParserEpoch();
    IncrementalParserRuleContext secondTree = parser.program();
    assertEquals(EXPECTED_TREE_1, secondTree.toStringTree(parser));
    // Should have been created by the first parser.
    assertEquals(startingEpoch, secondTree.epoch);

  }

  // Test that reparsing with a delete reuses data not deleted.
  @Test
  public void testBasicIncrementalDeleteWithWhitespace() {
    TestIncrementalBasicLexer lexer = new TestIncrementalBasicLexer(CharStreams.fromString(SAMPLE_TEXT_1));
    IncrementalTokenStream tokenStream = new IncrementalTokenStream(lexer);
    TestIncrementalBasicParser parser = new TestIncrementalBasicParser(tokenStream);
    int startingEpoch = parser.getParserEpoch();
    IncrementalParserRuleContext firstTree = parser.program();
    assertEquals(EXPECTED_TREE_1, firstTree.toStringTree(parser));
    // Should have been created by the first parser.
    assertEquals(startingEpoch, firstTree.epoch);

    // Delete a token and incrementally parse with the old tree.
    List<Token> oldTokens = tokenStream.getTokens();
    lexer = new TestIncrementalBasicLexer(CharStreams.fromString(SAMPLE_TEXT_2));
    tokenStream = new IncrementalTokenStream(lexer);
    TokenChange firstChange = TokenChange.builder()
      .setChangeType(TokenChangeType.REMOVED)
      .setOldToken((CommonToken) oldTokens.get(3))
      .build();
    TokenChange secondChange = TokenChange.builder()
      .setChangeType(TokenChangeType.REMOVED)
      .setOldToken((CommonToken) oldTokens.get(4))
      .build();

    ArrayList<TokenChange> changes = new ArrayList<TokenChange>(Arrays.asList(firstChange, secondChange));
    IncrementalParserData parserData = new IncrementalParserData(tokenStream, changes, firstTree);
    parser = new TestIncrementalBasicParser(tokenStream, parserData);
    int secondEpoch = parser.getParserEpoch();
    IncrementalParserRuleContext secondTree = parser.program();
    assertEquals(EXPECTED_TREE_2, secondTree.toStringTree(parser));
    // Should have been created by the second parser.
    assertEquals(secondEpoch, secondTree.epoch);
    // But all child nodes should have come from the old parse tree
    for (ParseTree child : secondTree.children) {
      IncrementalParserRuleContext incChild = (IncrementalParserRuleContext) child;
      assertEquals(startingEpoch, incChild.epoch);
    }
  }

  // Test that reparsing with a add reuses data not added.
  @Test
  public void testBasicIncrementalAddWithWhitespace() {
    TestIncrementalBasicLexer lexer = new TestIncrementalBasicLexer(CharStreams.fromString(SAMPLE_TEXT_1));
    IncrementalTokenStream tokenStream = new IncrementalTokenStream(lexer);
    TestIncrementalBasicParser parser = new TestIncrementalBasicParser(tokenStream);
    int startingEpoch = parser.getParserEpoch();
    IncrementalParserRuleContext firstTree = parser.program();
    assertEquals(EXPECTED_TREE_1, firstTree.toStringTree(parser));
    // Should have been created by the first parser.
    assertEquals(startingEpoch, firstTree.epoch);

    // Add some tokens and incrementally reparse.
    lexer = new TestIncrementalBasicLexer(CharStreams.fromString(SAMPLE_TEXT_3));
    tokenStream = new IncrementalTokenStream(lexer);
    tokenStream.fill();
    TokenChange firstChange = TokenChange.builder()
      .setChangeType(TokenChangeType.ADDED)
      .setNewToken((CommonToken) tokenStream.get(9))
      .build();
    TokenChange secondChange = TokenChange.builder()
      .setChangeType(TokenChangeType.ADDED)
      .setNewToken((CommonToken) tokenStream.get(10))
      .build();

    ArrayList<TokenChange> changes = new ArrayList<TokenChange>(Arrays.asList(firstChange, secondChange));
    IncrementalParserData parserData = new IncrementalParserData(tokenStream, changes, firstTree);
    parser = new TestIncrementalBasicParser(tokenStream, parserData);
    int secondEpoch = parser.getParserEpoch();
    IncrementalParserRuleContext secondTree = parser.program();

    assertEquals(EXPECTED_TREE_3, secondTree.toStringTree(parser));
    // Should have been created by the second parser.
    assertEquals(secondEpoch, secondTree.epoch);
    // All but the last child nodes should have come from the old parse tree
    for (int i = 0; i < secondTree.getChildCount() - 1; ++i) {
      IncrementalParserRuleContext incChild = (IncrementalParserRuleContext) secondTree.getChild(i);
      assertEquals(startingEpoch, incChild.epoch);
    }
    int lastChildIdx = secondTree.getChildCount() - 1;
    IncrementalParserRuleContext incChild = (IncrementalParserRuleContext) secondTree.getChild(lastChildIdx);
    assertEquals(secondEpoch, incChild.epoch);
  }

  /**
   * This test verifies the behavior of the incremental parser as a non-incremental parser.
   */
  @Test
  public void testJavaIncrementalParse() {
    TestIncrementalJavaLexer lexer = new TestIncrementalJavaLexer(CharStreams.fromString(JAVA_PROGRAM_1));
    TestIncrementalJavaParser parser = new TestIncrementalJavaParser(new IncrementalTokenStream(lexer));
    int startingEpoch = parser.getParserEpoch();

    IncrementalParserRuleContext tree = parser.compilationUnit();
    assertEquals(JAVA_EXPECTED_TREE_1, tree.toStringTree(parser));
    // Should have been created by the first parser.
    assertEquals(startingEpoch, tree.epoch);
  }

  /**
   * This test reparses text and asserts that the context was reused.
   */
  @Test
  public void testJavaIncrementalReparse() {
    TestIncrementalJavaLexer lexer = new TestIncrementalJavaLexer(CharStreams.fromString(JAVA_PROGRAM_1));
    IncrementalTokenStream tokenStream = new IncrementalTokenStream(lexer);
    TestIncrementalJavaParser parser = new TestIncrementalJavaParser(tokenStream);
    int startingEpoch = parser.getParserEpoch();
    IncrementalParserRuleContext firstTree = parser.compilationUnit();
    assertEquals(JAVA_EXPECTED_TREE_1, firstTree.toStringTree(parser));
    // Should have been created by the first parser.
    assertEquals(startingEpoch, firstTree.epoch);

    // Parse the same text with the old tree.
    lexer = new TestIncrementalJavaLexer(CharStreams.fromString(JAVA_PROGRAM_1));
    tokenStream = new IncrementalTokenStream(lexer);
    IncrementalParserData parserData = new IncrementalParserData(tokenStream, new ArrayList<TokenChange>(), firstTree);
    parser = new TestIncrementalJavaParser(tokenStream, parserData);
    int secondEpoch = parser.getParserEpoch();
    IncrementalParserRuleContext secondTree = parser.compilationUnit();
    assertEquals(JAVA_EXPECTED_TREE_1, secondTree.toStringTree(parser));
    // Should have been created by the first parser.
    assertEquals(startingEpoch, secondTree.epoch);

  }

  /**
   * This test changes a token in the java program and asserts that the right contexts were reused.
   */
  @Test
  public void testJavaIncrementalReparseWithChange() {
    TestIncrementalJavaLexer lexer = new TestIncrementalJavaLexer(CharStreams.fromString(JAVA_PROGRAM_1));
    IncrementalTokenStream tokenStream = new IncrementalTokenStream(lexer);
    TestIncrementalJavaParser parser = new TestIncrementalJavaParser(tokenStream);
    int startingEpoch = parser.getParserEpoch();
    IncrementalParserRuleContext firstTree = parser.compilationUnit();
    assertEquals(JAVA_EXPECTED_TREE_1, firstTree.toStringTree(parser));
    // Should have been created by the first parser.
    assertEquals(startingEpoch, firstTree.epoch);
    List<Token> oldTokens = tokenStream.getTokens();

    // Parse slightly changed text
    lexer = new TestIncrementalJavaLexer(CharStreams.fromString(JAVA_PROGRAM_2));
    tokenStream = new IncrementalTokenStream(lexer);
    tokenStream.fill();
    TokenChange firstChange = TokenChange.builder()
      .setChangeType(TokenChangeType.CHANGED)
      .setOldToken((CommonToken) oldTokens.get(21))
      .setNewToken((CommonToken) tokenStream.get(21))
      .build();

    List<TokenChange> changes = new ArrayList<>();
    changes.add(firstChange);
    IncrementalParserData parserData = new IncrementalParserData(tokenStream, changes, firstTree);
    parser = new TestIncrementalJavaParser(tokenStream, parserData);
    int secondEpoch = parser.getParserEpoch();
    IncrementalParserRuleContext secondTree = parser.compilationUnit();
    assertEquals(JAVA_EXPECTED_TREE_2, secondTree.toStringTree(parser));

    // Should have been created by the second parser.
    assertEquals(secondEpoch, secondTree.epoch);
    // Verify we reused contexts that are reusable
    ArrayList<XPathExpectation> expectations = new ArrayList<>(Arrays.asList(
      new XPathExpectationBuilder()
        .setClassType(TestIncrementalJavaParser.ClassOrInterfaceModifiersContext.class)
        .setTree("(classOrInterfaceModifiers (classOrInterfaceModifier public))")
        .setXPathRule("//classOrInterfaceModifiers")
        .setEpoch(startingEpoch)
        .createXPathExpectation(),
      new XPathExpectationBuilder()
        .setClassType(TestIncrementalJavaParser.FormalParametersContext.class)
        .setTree("(formalParameters ( (formalParameterDecls variableModifiers (type (classOrInterfaceType String) [ ]) (formalParameterDeclsRest (variableDeclaratorId args))) ))")
        .setXPathRule("//formalParameters")
        .setEpoch(startingEpoch)
        .createXPathExpectation(),
      new XPathExpectationBuilder()
        .setClassType(TestIncrementalJavaParser.ModifiersContext.class)
        .setTree("(modifiers (modifier public) (modifier static))")
        .setXPathRule("//modifiers")
        .setEpoch(startingEpoch)
        .createXPathExpectation(),
      new XPathExpectationBuilder()
        .setClassType(TestIncrementalJavaParser.LiteralContext.class)
        .setTree("(literal \"Hello\")")
        .setXPathRule("//expression/primary/literal")
        .setEpoch(secondEpoch)
        .createXPathExpectation()));
    /* This requires reusing individual recursion contexts */
	/*
	{
		class: ExpressionContext,
		tree: "System.out.println",
		xpathRule: "//statementExpression/expression/expression",
	},*/
    verifyXPathExpectations(parser, secondTree, expectations);
  }

  // Verify a set of xpath expectations against the parse tree
  private void verifyXPathExpectations(IncrementalParser parser,
                                       IncrementalParserRuleContext parseTree,
                                       List<XPathExpectation> expectations) {
    for (XPathExpectation expectation : expectations) {
      for (ParseTree XPathMatch : XPath.findAll(parseTree, expectation.XPathRule, parser)) {
        assertTrue(expectation.classType.isInstance(XPathMatch));
        IncrementalParserRuleContext incCtx = (IncrementalParserRuleContext) XPathMatch;
        assertEquals(incCtx.toStringTree(parser), expectation.tree);
        assertEquals(incCtx.epoch, expectation.epoch);
      }
    }
  }

  private static class XPathExpectation {
    public String XPathRule;
    public String tree;
    public Class<? extends IncrementalParserRuleContext> classType;
    public int epoch;

    public XPathExpectation(String XPathRule, String tree, Class<? extends IncrementalParserRuleContext> classType,
                            int epoch) {
      this.XPathRule = XPathRule;
      this.tree = tree;
      this.classType = classType;
      this.epoch = epoch;
    }
  }

  private static class XPathExpectationBuilder {
    private String XPathRule;
    private String tree;
    private Class<? extends IncrementalParserRuleContext> classType;
    private int epoch;

    public XPathExpectationBuilder setXPathRule(String XPathRule) {
      this.XPathRule = XPathRule;
      return this;
    }

    public XPathExpectationBuilder setTree(String tree) {
      this.tree = tree;
      return this;
    }

    public XPathExpectationBuilder setClassType(Class<? extends IncrementalParserRuleContext> classType) {
      this.classType = classType;
      return this;
    }

    public XPathExpectationBuilder setEpoch(int epoch) {
      this.epoch = epoch;
      return this;
    }

    public XPathExpectation createXPathExpectation() {
      return new XPathExpectation(XPathRule, tree, classType, epoch);
    }
  }

}