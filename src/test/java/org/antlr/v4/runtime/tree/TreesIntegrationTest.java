/*
 * This file is a part of ANTLR.
 *
 * Copyright (c) 2012-2025 The ANTLR Project. All rights reserved.
 * Copyright (c) 2025-2026 Valery Maximov <maximovvalery@gmail.com> and contributors
 *
 * Use of this file is governed by the BSD-3-Clause license that
 * can be found in the LICENSE.txt file in the project root.
 */
package org.antlr.v4.runtime.tree;

import org.antlr.v4.runtime.CommonToken;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Интеграционные тесты для сложных сценариев работы с деревом
 */
public class TreesIntegrationTest {

  @Test
  public void testComplexTreeStructure() {
    // Создаем более сложную структуру дерева
    //        root (0)
    //       /    \
    //    child1  child2 (1)
    //    (1)     /  |  \
    //   / \    t3  gc1 t4
    //  t1  t2      (2)

    ParserRuleContext root = createContext(null, 0);
    ParserRuleContext child1 = createContext(root, 1);
    ParserRuleContext child2 = createContext(root, 1);
    ParserRuleContext grandChild1 = createContext(child2, 2);

    Token token1 = new CommonToken(1, "t1");
    Token token2 = new CommonToken(2, "t2");
    Token token3 = new CommonToken(3, "t3");
    Token token4 = new CommonToken(4, "t4");

    root.addChild(child1);
    root.addChild(child2);

    child1.addChild(new TerminalNodeImpl(token1));
    child1.addChild(new TerminalNodeImpl(token2));

    child2.addChild(new TerminalNodeImpl(token3));
    child2.addChild(grandChild1);
    child2.addChild(new TerminalNodeImpl(token4));

    // Тесты
    List<Token> allTokens = Trees.getTokensFromParseTree(root);
    assertThat(allTokens).hasSize(4);

    Collection<ParserRuleContext> allRuleNodes = Trees.findAllRuleNodes(root, 1, 2);
    assertThat(allRuleNodes).hasSize(3); // child1, child2, grandChild1

    List<ParserRuleContext> children = Trees.getChildren(root, 1);
    assertThat(children).hasSize(2);
  }

  @Test
  public void testTokenNavigation() {
    Token t1 = createToken(Token.DEFAULT_CHANNEL, 1, "token1");
    Token t2 = createToken(Token.HIDDEN_CHANNEL, 2, "hidden");
    Token t3 = createToken(Token.DEFAULT_CHANNEL, 3, "token3");
    Token t4 = createToken(Token.DEFAULT_CHANNEL, 4, "token4");
    Token t5 = createToken(Token.DEFAULT_CHANNEL, 5, "token5");

    List<Token> tokens = Arrays.asList(t1, t2, t3, t4, t5);

    // Предыдущий токен с пропуском скрытых
    var prevToken = Trees.getPreviousTokenFromDefaultChannel(tokens, 2);
    assertThat(prevToken).isPresent();
    assertThat(prevToken.get()).isEqualTo(t1);

    // Предыдущий токен определенного типа
    var prevTokenType = Trees.getPreviousTokenFromDefaultChannel(tokens, 3, 3);
    assertThat(prevTokenType).isPresent();
    assertThat(prevTokenType.get()).isEqualTo(t3);
  }

  @Test
  public void testAncestorNavigation() {
    // Создаем цепочку узлов разной глубины
    ParserRuleContext level0 = createContext(null, 0);
    ParserRuleContext level1 = createContext(level0, 1);
    ParserRuleContext level2 = createContext(level1, 2);
    ParserRuleContext level3 = createContext(level2, 3);

    level0.addChild(level1);
    level1.addChild(level2);
    level2.addChild(level3);

    // Поиск предка конкретного типа
    ParserRuleContext ancestor = Trees.getAncestor(level3, 1);
    assertThat(ancestor).isEqualTo(level1);

    // Поиск предка из коллекции типов
    ParserRuleContext ancestorFromCollection = Trees.getAncestor(level3, Arrays.asList(0, 1));
    assertThat(ancestorFromCollection).isEqualTo(level1); // Первый найденный

    // Поиск самого верхнего предка
    ParserRuleContext topAncestor = Trees.getAncestor(level3);
    assertThat(topAncestor).isEqualTo(level0);
  }

  @Test
  public void testTopLevelDescendants() {
    // root
    //  ├─ child1 (type 1)
    //  │   └─ grandChild1 (type 2)
    //  │       └─ greatGrandChild (type 2)
    //  └─ child2 (type 2)
    //      └─ grandChild2 (type 3)

    ParserRuleContext root = createContext(null, 0);
    ParserRuleContext child1 = createContext(root, 1);
    ParserRuleContext child2 = createContext(root, 2);
    ParserRuleContext grandChild1 = createContext(child1, 2);
    ParserRuleContext greatGrandChild = createContext(grandChild1, 2);
    ParserRuleContext grandChild2 = createContext(child2, 3);

    root.addChild(child1);
    root.addChild(child2);
    child1.addChild(grandChild1);
    grandChild1.addChild(greatGrandChild);
    child2.addChild(grandChild2);

    // Ищем только верхнеуровневые узлы типа 2
    Collection<ParserRuleContext> topLevel = Trees.findAllTopLevelDescendantNodes(root, List.of(2));

    // Должны найти child2 и grandChild1, но НЕ greatGrandChild
    assertThat(topLevel).hasSize(2);
    assertThat(topLevel).contains(child2, grandChild1);
    assertThat(topLevel).doesNotContain(greatGrandChild);
  }

  @Test
  public void testNodeContainsWithExclusion() {
    ParserRuleContext root = createContext(null, 0);
    ParserRuleContext child1 = createContext(root, 1);
    ParserRuleContext child2 = createContext(root, 1);

    root.addChild(child1);
    root.addChild(child2);

    // Без исключения
    assertThat(Trees.nodeContains(root, 1)).isTrue();

    // С исключением child1 - должен все равно найти child2
    assertThat(Trees.nodeContains(root, child1, 1)).isTrue();

    // С исключением обоих - не должен найти
    assertThat(Trees.nodeContains(child1, child1, 1)).isFalse();
  }

  @Test
  public void testNodeNavigation() {
    ParserRuleContext root = createContext(null, 0);
    ParserRuleContext node1 = createContext(root, 1);
    ParserRuleContext node2 = createContext(root, 1);
    ParserRuleContext node3 = createContext(root, 1);

    root.addChild(node1);
    root.addChild(node2);
    root.addChild(node3);

    // Следующий узел
    ParseTree nextNode = Trees.getNextNode(root, node1, 1);
    assertThat(nextNode).isEqualTo(node2);

    // Предыдущий узел
    ParseTree prevNode = Trees.getPreviousNode(root, node2, 1);
    assertThat(prevNode).isEqualTo(node1);

    // Последний узел - следующего нет
    ParseTree noNext = Trees.getNextNode(root, node3, 1);
    assertThat(noNext).isEqualTo(node3);

    // Первый узел - предыдущего нет
    ParseTree noPrev = Trees.getPreviousNode(root, node1, 1);
    assertThat(noPrev).isEqualTo(node1);
  }

  @Test
  public void testLazyInitializationInParserRuleContext() {
    ParserRuleContext ctx = createContext(null, 0);
    Token token = new CommonToken(1, "test");
    ctx.addChild(new TerminalNodeImpl(token));

    // Первый вызов - инициализация
    List<Token> tokens1 = ctx.getTokens();
    assertThat(tokens1).hasSize(1);

    // Второй вызов - должен вернуть кешированное значение
    List<Token> tokens2 = ctx.getTokens();
    assertThat(tokens2).isSameAs(tokens1);

    // То же для getText
    String text1 = ctx.getText();
    String text2 = ctx.getText();
    assertThat(text2).isSameAs(text1);
  }

  private ParserRuleContext createContext(ParserRuleContext parent, int ruleIndex) {
    return new TestParserRuleContext(parent, -1, ruleIndex);
  }

  private Token createToken(int channel, int type, String text) {
    CommonToken token = new CommonToken(type, text);
    token.setChannel(channel);
    return token;
  }

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
}