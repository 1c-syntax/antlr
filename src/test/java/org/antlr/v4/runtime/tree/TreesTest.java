/**
 * This file is a part of ANTLR.
 *
 * Copyright (c) 2012-2025 The ANTLR Project. All rights reserved.
 * Copyright (c) 2025 Valery Maximov <maximovvalery@gmail.com> and contributors
 *
 * Use of this file is governed by the BSD-3-Clause license that
 * can be found in the LICENSE.txt file in the project root.
 */
package org.antlr.v4.runtime.tree;

import org.antlr.v4.runtime.CommonToken;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Тесты для новых методов класса Trees и ParserRuleContext
 */
public class TreesTest {

  private ParserRuleContext root;
  private ParserRuleContext child1;
  private ParserRuleContext child2;
  private ParserRuleContext grandChild;
  private TerminalNode terminal1;
  private TerminalNode terminal2;
  private Token token1;
  private Token token2;
  private Token token3;

  @BeforeEach
  public void setUp() {
    // Создаем простую структуру дерева для тестов
    // root
    //   ├─ child1 (rule index = 1)
    //   │   ├─ terminal1
    //   │   └─ grandChild (rule index = 2)
    //   └─ child2 (rule index = 1)
    //       └─ terminal2

    token1 = new CommonToken(1, "token1");
    token2 = new CommonToken(2, "token2");
    token3 = new CommonToken(3, "token3");

    terminal1 = new TerminalNodeImpl(token1);
    terminal2 = new TerminalNodeImpl(token2);

    root = new TestParserRuleContext(null, 0, 0);
    child1 = new TestParserRuleContext(root, -1, 1);
    child2 = new TestParserRuleContext(root, -1, 1);
    grandChild = new TestParserRuleContext(child1, -1, 2);

    root.addChild(child1);
    root.addChild(child2);

    child1.addChild(terminal1);
    child1.addChild(grandChild);

    child2.addChild(terminal2);
  }

  @Test
  public void testGetChildren() {
    // Тест получения детей по индексу правила
    List<ParserRuleContext> children = Trees.getChildren(root, 1);
    assertThat(children).hasSize(2);
    assertThat(children).containsExactly(child1, child2);
  }

  @Test
  public void testGetChildrenMultipleIndexes() {
    List<ParserRuleContext> children = Trees.getChildren(root, 1, 2);
    assertThat(children).hasSize(2);
    assertThat(children).containsExactly(child1, child2);
  }

  @Test
  public void testGetChildrenNoMatch() {
    List<ParserRuleContext> children = Trees.getChildren(root, 99);
    assertThat(children).isEmpty();
  }

  @Test
  public void testGetFirstChild() {
    Optional<ParserRuleContext> firstChild = Trees.getFirstChild(root, 1);
    assertThat(firstChild).isPresent();
    assertThat(firstChild.get()).isEqualTo(child1);
  }

  @Test
  public void testGetFirstChildNoMatch() {
    Optional<ParserRuleContext> firstChild = Trees.getFirstChild(root, 99);
    assertThat(firstChild).isEmpty();
  }

  @Test
  public void testGetFirstChildMultipleIndexes() {
    Optional<ParserRuleContext> firstChild = Trees.getFirstChild(root, 99, 1);
    assertThat(firstChild).isPresent();
    assertThat(firstChild.get()).isEqualTo(child1);
  }

  @Test
  public void testGetAncestorByType() {
    ParserRuleContext ancestor = Trees.getAncestor(grandChild, 1);
    assertThat(ancestor).isEqualTo(child1);
  }

  @Test
  public void testGetAncestorByTypeNotFound() {
    ParserRuleContext ancestor = Trees.getAncestor(grandChild, 99);
    assertThat(ancestor).isNull();
  }

  @Test
  public void testGetAncestorByTypeCollection() {
    ParserRuleContext ancestor = Trees.getAncestor(grandChild, Arrays.asList(0, 1));
    assertThat(ancestor).isEqualTo(child1);
  }

  @Test
  public void testGetAncestorTopMost() {
    ParserRuleContext topAncestor = Trees.getAncestor(grandChild);
    assertThat(topAncestor).isEqualTo(root);
  }

  @Test
  public void testGetAncestorTopMostForRoot() {
    ParserRuleContext topAncestor = Trees.getAncestor(root);
    assertThat(topAncestor).isEqualTo(root);
  }

  @Test
  public void testGetTokensFromParserRuleContext() {
    List<Token> tokens = Trees.getTokens(child1);
    assertThat(tokens).hasSize(1);
    assertThat(tokens.get(0)).isEqualTo(token1);
  }

  @Test
  public void testGetTokensFromTerminalNode() {
    List<Token> tokens = Trees.getTokens(terminal1);
    assertThat(tokens).hasSize(1);
    assertThat(tokens.get(0)).isEqualTo(token1);
  }

  @Test
  public void testGetTokensFromParseTree() {
    List<Token> tokens = Trees.getTokensFromParseTree(root);
    assertThat(tokens).hasSize(2);
    assertThat(tokens).containsExactly(token1, token2);
  }

  @Test
  public void testGetTokensFromParseTreeEmpty() {
    ParserRuleContext emptyContext = new TestParserRuleContext(null, 0, 0);
    List<Token> tokens = Trees.getTokensFromParseTree(emptyContext);
    assertThat(tokens).isEmpty();
  }

  @Test
  public void testNodeContains() {
    boolean contains = Trees.nodeContains(root, 1);
    assertThat(contains).isTrue();
  }

  @Test
  public void testNodeContainsNotFound() {
    boolean contains = Trees.nodeContains(root, 99);
    assertThat(contains).isFalse();
  }

  @Test
  public void testNodeContainsMultipleTypes() {
    boolean contains = Trees.nodeContains(root, 1, 2);
    assertThat(contains).isTrue();
  }

  @Test
  public void testNodeContainsWithExclude() {
    boolean contains = Trees.nodeContains(root, child1, 1);
    assertThat(contains).isTrue(); // child2 все еще имеет индекс 1
  }

  @Test
  public void testGetPreviousTokenFromDefaultChannel() {
    List<Token> tokens = Arrays.asList(
      createToken(1, Token.DEFAULT_CHANNEL, "token1"),
      createToken(2, Token.DEFAULT_CHANNEL, "token2"),
      createToken(3, Token.HIDDEN_CHANNEL, "hidden"),
      createToken(4, Token.DEFAULT_CHANNEL, "token3")
    );

    Optional<Token> prevToken = Trees.getPreviousTokenFromDefaultChannel(tokens, 3);
    assertThat(prevToken).isPresent();
    assertThat(prevToken.get().getText()).isEqualTo("token2");
  }

  @Test
  public void testGetPreviousTokenFromDefaultChannelWithType() {
    List<Token> tokens = List.of(
      createToken(1, Token.DEFAULT_CHANNEL, "token1"),
      createToken(2, Token.DEFAULT_CHANNEL, "token2")
    );

    Optional<Token> prevToken = Trees.getPreviousTokenFromDefaultChannel(tokens, 1, 1);
    assertThat(prevToken).isPresent();
    assertThat(prevToken.get()).isEqualTo(tokens.get(0));
  }

  @Test
  public void testGetPreviousTokenFromDefaultChannelNotFound() {
    List<Token> tokens = List.of(
      createToken(1, Token.DEFAULT_CHANNEL, "token1")
    );

    Optional<Token> prevToken = Trees.getPreviousTokenFromDefaultChannel(tokens, 0);
    assertThat(prevToken).isEmpty();
  }

  @Test
  public void testGetNextNode() {
    ParseTree nextNode = Trees.getNextNode(root, child1, 1);
    assertThat(nextNode).isEqualTo(child2);
  }

  @Test
  public void testGetNextNodeNotFound() {
    ParseTree nextNode = Trees.getNextNode(root, child2, 1);
    assertThat(nextNode).isEqualTo(child2); // Возвращает текущий узел
  }

  @Test
  public void testGetPreviousNode() {
    ParseTree prevNode = Trees.getPreviousNode(root, child2, 1);
    assertThat(prevNode).isEqualTo(child1);
  }

  @Test
  public void testGetPreviousNodeNotFound() {
    ParseTree prevNode = Trees.getPreviousNode(root, child1, 1);
    assertThat(prevNode).isEqualTo(child1); // Возвращает текущий узел
  }

  @Test
  public void testFindAllNodesGeneric() {
    List<ParserRuleContext> nodes = Trees.findAllNodes(root, 1, false);
    assertThat(nodes).hasSize(2);
    assertThat(nodes).containsExactly(child1, child2);
  }

  @Test
  public void testFindAllNodesTokens() {
    List<TerminalNode> terminals = Trees.findAllNodes(root, 1, true);
    assertThat(terminals).hasSize(1);
    assertThat(terminals.get(0)).isEqualTo(terminal1);
  }

  @Test
  public void testFindAllNodesByClass() {
    List<TerminalNode> terminals = Trees.findAllNodes(root, TerminalNode.class);
    assertThat(terminals).hasSize(2);
    assertThat(terminals).containsExactly(terminal1, terminal2);
  }

  @Test
  public void testFindAllRuleNodesMultipleIndexes() {
    Collection<ParserRuleContext> nodes = Trees.findAllRuleNodes(root, 1, 2);
    assertThat(nodes).hasSize(3);
    assertThat(nodes).contains(child1, child2, grandChild);
  }

  @Test
  public void testFindAllRuleNodesCollection() {
    Collection<ParserRuleContext> nodes = Trees.findAllRuleNodes(root, Arrays.asList(1, 2));
    assertThat(nodes).hasSize(3);
  }

  @Test
  public void testFindAllTopLevelDescendantNodes() {
    Collection<ParserRuleContext> nodes = Trees.findAllTopLevelDescendantNodes(root, List.of(2));
    assertThat(nodes).hasSize(1);
    assertThat(nodes).contains(grandChild);
  }

  @Test
  public void testFindAllTopLevelDescendantNodesEmpty() {
    Collection<ParserRuleContext> nodes = Trees.findAllTopLevelDescendantNodes(grandChild, List.of(1));
    assertThat(nodes).isEmpty();
  }

  @Test
  public void testParserRuleContextGetTokens() {
    // Тест нового метода getTokens() в ParserRuleContext
    List<Token> tokens = child1.getTokens();
    assertThat(tokens).hasSize(1);
    assertThat(tokens.get(0)).isEqualTo(token1);
  }

  @Test
  public void testParserRuleContextGetText() {
    // Тест переопределенного метода getText() с lazy инициализацией
    String text = child1.getText();
    assertThat(text).isNotNull();

    // Повторный вызов должен вернуть кешированное значение
    String text2 = child1.getText();
    assertThat(text2).isEqualTo(text);
  }

  @Test
  public void testGetIndex() {
    // Тест нового метода getIndex() в ParserRuleContext
    assertThat(child1.getIndex()).isEqualTo(1);
    assertThat(grandChild.getIndex()).isEqualTo(2);
  }

  @Test
  public void testTerminalNodeGetIndex() {
    // Тест метода getIndex() в TerminalNode
    assertThat(terminal1.getIndex()).isEqualTo(1);
    assertThat(terminal2.getIndex()).isEqualTo(2);
  }

  /**
   * Тестовый класс ParserRuleContext для упрощения создания узлов
   */
  private static class TestParserRuleContext extends ParserRuleContext {
    private final int ruleIndex;

    public TestParserRuleContext(ParserRuleContext parent, int invokingState, int ruleIndex) {
      super(parent, invokingState);
      this.ruleIndex = ruleIndex;
    }

    @Override
    public int getRuleIndex() {
      return ruleIndex;
    }
  }

  private static CommonToken createToken(int type, int channel, String text) {
    var token = new CommonToken(type, text);
    token.setChannel(channel);
    return token;
  }
}