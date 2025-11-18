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
import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.atn.ATN;
import org.antlr.v4.runtime.misc.NotNull;
import org.antlr.v4.runtime.misc.Nullable;
import org.antlr.v4.runtime.misc.Predicate;
import org.antlr.v4.runtime.misc.Utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.IntStream;

/**
 * A set of utility routines useful for all kinds of ANTLR trees.
 */
public class Trees {

  private Trees() {
    // utility class
  }

  /**
   * Print out a whole tree in LISP form. {@link #getNodeText} is used on the
   * node payloads to get the text for the nodes.  Detect
   * parse trees and extract data appropriately.
   */
  public static String toStringTree(@NotNull Tree t) {
    return toStringTree(t, (List<String>) null);
  }

  /**
   * Print out a whole tree in LISP form. {@link #getNodeText} is used on the
   * node payloads to get the text for the nodes.  Detect
   * parse trees and extract data appropriately.
   */
  public static String toStringTree(@NotNull Tree t, @Nullable Parser recog) {
    var ruleNames = recog != null ? recog.getRuleNames() : null;
    var ruleNamesList = ruleNames != null ? Arrays.asList(ruleNames) : null;
    return toStringTree(t, ruleNamesList);
  }

  /**
   * Print out a whole tree in LISP form. {@link #getNodeText} is used on the
   * node payloads to get the text for the nodes.
   */
  public static String toStringTree(@NotNull final Tree t, @Nullable final List<String> ruleNames) {
    var s = Utils.escapeWhitespace(getNodeText(t, ruleNames), false);
    if (t.getChildCount() == 0) {
      return s;
    }
    var buf = new StringBuilder();
    buf.append("(")
      .append(s)
      .append(' ');
    for (int i = 0; i < t.getChildCount(); i++) {
      if (i > 0) {
        buf.append(' ');
      }
      buf.append(toStringTree(t.getChild(i), ruleNames));
    }
    buf.append(")");
    return buf.toString();
  }

  /**
   * Возвращает строковое представление узла дерева
   *
   * @param t     Узел дерева
   * @param recog Парсер, которым построено дерево
   * @return Строковое представление узла
   */
  public static String getNodeText(@NotNull Tree t, @Nullable Parser recog) {
    var ruleNames = recog != null ? recog.getRuleNames() : null;
    var ruleNamesList = ruleNames != null ? Arrays.asList(ruleNames) : null;
    return getNodeText(t, ruleNamesList);
  }

  /**
   * Возвращает строковое представление узла дерева
   *
   * @param t         Узел дерева
   * @param ruleNames Список имен правил (rule) построения дерева.
   * @return Строковое представление узла
   */
  public static String getNodeText(@NotNull Tree t, @Nullable List<String> ruleNames) {
    if (ruleNames != null) {
      if (t instanceof RuleNode ruleNode) {
        var ruleContext = ruleNode.getRuleContext();
        var ruleIndex = ruleContext.getIndex();
        var ruleName = ruleNames.get(ruleIndex);
        var altNumber = ruleContext.getAltNumber();
        if (altNumber != ATN.INVALID_ALT_NUMBER) {
          return ruleName + ":" + altNumber;
        }
        return ruleName;
      } else if (t instanceof ErrorNode) {
        return t.toString();
      } else if (t instanceof TerminalNode terminalNode) {
        var symbol = terminalNode.getSymbol();
        if (symbol != null) {
          return symbol.getText();
        }
      }
    }

    // no recog for rule names
    var payload = t.getPayload();
    if (payload instanceof Token token) {
      return token.getText();
    }
    return t.getPayload().toString();
  }

  /**
   * Return ordered list of all children of this node
   */
  public static List<Tree> getChildren(Tree t) {
    List<Tree> kids = new ArrayList<>();
    for (int i = 0; i < t.getChildCount(); i++) {
      kids.add(t.getChild(i));
    }
    return kids;
  }

  /**
   * Возвращает список детей нужных типов
   *
   * @param tree        Узел дерева, дочерние которого интересуют
   * @param ruleIndexes Список индексов дочерних
   * @return Список дочерних
   */
  public static List<ParserRuleContext> getChildren(Tree tree, Integer... ruleIndexes) {
    var indexes = Set.copyOf(Arrays.asList(ruleIndexes));
    List<ParserRuleContext> kids = new ArrayList<>();
    for (int i = 0; i < tree.getChildCount(); i++) {
      var child = tree.getChild(i);
      if (child instanceof ParserRuleContext parserRuleContext
        && indexes.contains(parserRuleContext.getIndex())) {
        kids.add(parserRuleContext);
      }
    }
    return kids;
  }

  /**
   * Получает первого ребенка с одним из нужных типов
   *
   * @param tree        - нода, для которой ищем ребенка
   * @param ruleIndexes - Список типов
   * @return Первый найденный ребенок, если не найден, вернет пустой Optional
   */
  public static Optional<ParserRuleContext> getFirstChild(Tree tree, Integer... ruleIndexes) {
    var indexes = Set.copyOf(Arrays.asList(ruleIndexes));
    for (int i = 0; i < tree.getChildCount(); i++) {
      var child = tree.getChild(i);
      if (child instanceof ParserRuleContext parserRuleContext
        && indexes.contains(parserRuleContext.getIndex())) {
        return Optional.of(parserRuleContext);
      }
    }
    return Optional.empty();
  }

  /**
   * Return a list of all ancestors of this node.  The first node of
   * list is the root and the last is the parent of this node.
   *
   * @since 4.5.1
   */
  @NotNull
  public static List<? extends Tree> getAncestors(@NotNull Tree t) {
    if (t.getParent() == null) {
      return Collections.emptyList();
    }
    List<Tree> ancestors = new ArrayList<>();
    t = t.getParent();
    while (t != null) {
      ancestors.add(0, t); // insert at start
      t = t.getParent();
    }
    return ancestors;
  }

  /**
   * Возвращает предка нужно типа
   *
   * @param element Элемент, для которого ищется предок
   * @param type    Тип предка
   * @param <T>     Тип значения предка, если выбранный тип значения не будет соответствовать type,
   *                то будет ClassCastException
   * @return Найденный предок. Если не найден, то вернется NULL
   */
  @Nullable
  public static <T extends ParserRuleContext> T getAncestor(ParserRuleContext element, int type) {
    var parent = element.getParent();
    if (parent == null) {
      return null;
    }
    while (parent != null) {
      if (parent.getIndex() == type) {
        @SuppressWarnings("unchecked") // Считаем, что разработчик верно указал T, в соответствии с type
        T result = (T) parent;
        return result;
      }
      parent = parent.getParent();
    }

    return null;
  }

  /**
   * Возвращает первого предка одного из указанных типов
   *
   * @param element Элемент, для которого ищется предок
   * @param types   Типы предков
   * @return Найденный предок. Если не найден, то вернется NULL
   */
  @Nullable
  public static ParserRuleContext getAncestor(ParserRuleContext element, Collection<Integer> types) {
    var parent = element.getParent();
    if (parent == null) {
      return null;
    }
    while (parent != null) {
      if (types.contains(parent.getIndex())) {
        return parent;
      }
      parent = parent.getParent();
    }

    return null;
  }

  /**
   * Возвращает самого верхнего родителя
   *
   * @param element Элемент, для которого ищется предок
   * @return Найденный предок. Если не найден, то вернется текущий
   */
  @Nullable
  public static ParserRuleContext getAncestor(ParserRuleContext element) {
    var parent = element.getParent();
    if (parent == null) {
      return element;
    }
    while (parent.getParent() != null) {
      parent = parent.getParent();
    }
    return parent;
  }

  /**
   * Return true if ancestor is node's parent or a node on path to root from node.
   * Use == not equals().
   *
   * @since 4.5.1
   */
  public static boolean isAncestorOf(Tree ancestor, Tree node) {
    if (ancestor == null || node == null) {
      return false;
    }
    var parent = node.getParent();
    while (parent != null) {
      if (ancestor == parent) {
        return true;
      }
      parent = parent.getParent();
    }
    return false;
  }

  /**
   * Выбирает из дерева токены нужного типа, перебирая все узлы дерева
   *
   * @param tree      Узел дерева
   * @param tokenType Тип токена
   * @param <T>       Тип возвращаемых токенов, зависит от tokenType
   * @return Коллекция токенов нужного типа
   */
  public static <T extends ParseTree> Collection<T> findAllTokenNodes(ParseTree tree, int tokenType) {
    return findAllNodes(tree, tokenType, true);
  }

  /**
   * Выбирает из дерева узлы (rule) нужного типа, перебирая все узлы дерева
   *
   * @param tree      Узел дерева
   * @param ruleIndex Индекс правила (rule)
   * @param <T>       Тип возвращаемых узлов, зависит от ruleIndex
   * @return Коллекция узлов нужного типа
   */
  public static <T extends ParseTree> Collection<T> findAllRuleNodes(ParseTree tree, int ruleIndex) {
    return findAllNodes(tree, ruleIndex, false);
  }

  /**
   * Выбирает из дерева узлы (rule) нужных типов, перебирая все узлы дерева
   *
   * @param tree        Узел дерева
   * @param ruleIndexes Индексы правил (rule)
   * @return Коллекция узлов нужных типов
   */
  public static Collection<ParserRuleContext> findAllRuleNodes(ParseTree tree, Integer... ruleIndexes) {
    return findAllRuleNodes(tree, Arrays.asList(ruleIndexes));
  }

  /**
   * Выбирает из дерева узлы (rule) нужных типов, перебирая все узлы дерева
   *
   * @param tree        Узел дерева
   * @param ruleIndexes Индексы правил (rule)
   * @return Коллекция узлов нужных типов
   */
  public static Collection<ParserRuleContext> findAllRuleNodes(ParseTree tree, Collection<Integer> ruleIndexes) {
    List<ParserRuleContext> nodes = new ArrayList<>();
    flatten(tree, nodes, ruleIndexes);
    return nodes;
  }

  /**
   * Выбирает из дерева элементы нужного типа, перебирая все узлы дерева
   *
   * @param tree       Узел дерева
   * @param index      Индекс правила (rule) или тип токена
   * @param findTokens Переключатель, управляющий поиском токенов или узлов
   * @param <T>        Тип возвращаемых элементов, зависит от index и findTokens
   * @return Коллекция элементов нужного типа
   */
  public static <T extends ParseTree> List<T> findAllNodes(ParseTree tree, int index, boolean findTokens) {
    List<ParseTree> nodes = new ArrayList<>();
    _findAllNodes(tree, index, findTokens, nodes);
    if (nodes.isEmpty()) {
      return Collections.emptyList();
    } else {
      // Считаем, что программист снаружи корректно указал тип, т.е. index + findTokens соответствуют T
      @SuppressWarnings("unchecked")
      List<T> result = (List<T>) nodes;
      return result;
    }
  }

  /**
   * Выбирает из дерева элементы нужного типа, перебирая все узлы дерева
   *
   * @param tree      Узел дерева
   * @param nodeClass Класс искомого элемента
   * @param <T>       Тип искомого элемента
   * @return Список элементов нужного типа
   */
  public static <T extends ParseTree> List<T> findAllNodes(ParseTree tree, Class<T> nodeClass) {
    List<T> nodes = new ArrayList<>();
    findAllNodesReq(tree, nodeClass, nodes);
    return nodes;
  }

  /**
   * Получает "первые" дочерние ноды с нужными типами
   * ВАЖНО: поиск вглубь найденной ноды с нужными индексами не выполняется
   * Например, если указать RULE_codeBlock, то найдется только самый верхнеуровневый блок кода, все
   * вложенные найдены не будут
   * ВАЖНО: начальная нода не проверяется на условие, т.к. тогда она единственная и вернется в результате
   *
   * @param root    - начальный узел дерева
   * @param indexes - коллекция индексов
   * @return найденные узлы
   */
  public static Collection<ParserRuleContext> findAllTopLevelDescendantNodes(ParserRuleContext root,
                                                                             Collection<Integer> indexes) {
    List<ParserRuleContext> result = new ArrayList<>();
    if (root.getChildCount() == 0) {
      return Collections.emptyList();
    }
    root.children.stream()
      .map(node -> findAllTopLevelDescendantNodesInner(node, indexes))
      .forEach(result::addAll);
    return result;
  }

  /**
   * НЕ ИСПОЛЬЗОВАТЬ СНАРУЖИ
   * ДЛЯ ВНУТРЕННЕГО ПРИМЕНЕНИЯ
   * TODO перевести в private
   */
  public static void _findAllNodes(ParseTree t, int index, boolean findTokens,
                                   List<? super ParseTree> nodes) {
    // check this node (the root) first
    if (findTokens && t instanceof TerminalNode tnode) {
      if (tnode.getSymbol().getType() == index) {
        nodes.add(t);
      }
    } else if (!findTokens && t instanceof ParserRuleContext ctx) {
      if (ctx.getIndex() == index) {
        nodes.add(t);
      }
    }
    // check children
    for (int i = 0; i < t.getChildCount(); i++) {
      _findAllNodes(t.getChild(i), index, findTokens, nodes);
    }
  }

  /**
   * Get all descendents; includes t itself.
   *
   * @since 4.5.1
   */
  public static List<ParseTree> getDescendants(ParseTree t) {
    List<ParseTree> nodes = new ArrayList<>(t.getChildCount());
    flatten(t, nodes);
    return nodes;
  }

  /**
   * Find smallest subtree of t enclosing range startTokenIndex..stopTokenIndex
   * inclusively using postorder traversal.  Recursive depth-first-search.
   *
   * @since 4.5
   */
  @Nullable
  public static ParserRuleContext getRootOfSubtreeEnclosingRegion(@NotNull ParseTree tree,
                                                                  int startTokenIndex, // inclusive
                                                                  int stopTokenIndex)  // inclusive
  {
    var count = tree.getChildCount();
    for (var i = 0; i < count; i++) {
      var child = tree.getChild(i);
      var root = getRootOfSubtreeEnclosingRegion(child, startTokenIndex, stopTokenIndex);
      if (root != null) {
        return root;
      }
    }

    if (tree instanceof ParserRuleContext root
      && startTokenIndex >= root.getStart().getTokenIndex() // is range fully contained in t?
      && (root.getStop() == null || stopTokenIndex <= root.getStop().getTokenIndex())) {
      return root;
    }
    return null;
  }

  /**
   * Replace any subtree siblings of root that are completely to left
   * or right of lookahead range with a CommonToken(Token.INVALID_TYPE,"...")
   * node. The source interval for t is not altered to suit smaller range!
   * <p>
   * WARNING: destructive to t.
   *
   * @since 4.5.1
   */
  public static void stripChildrenOutOfRange(ParserRuleContext ctx,
                                             ParserRuleContext root,
                                             int startIndex,
                                             int stopIndex) {
    if (ctx == null) {
      return;
    }

    for (var i = 0; i < ctx.getChildCount(); i++) {
      var child = ctx.getChild(i);
      var range = child.getSourceInterval();
      if (child instanceof ParserRuleContext parserRuleContext && (range.b < startIndex || range.a > stopIndex)) {
        if (isAncestorOf(parserRuleContext, root)) { // replace only if subtree doesn't have displayed root
          var abbrev = new CommonToken(Token.INVALID_TYPE, "...");
          ctx.children.set(i, new TerminalNodeImpl(abbrev));
        }
      }
    }
  }

  /**
   * Return first node satisfying the pred
   *
   * @since 4.5.1
   */
  public static Tree findNodeSuchThat(Tree t, Predicate<Tree> pred) {
    if (t == null) {
      return null;
    }

    if (pred.eval(t)) {
      return t;
    }

    int n = t.getChildCount();
    for (int i = 0; i < n; i++) {
      var node = findNodeSuchThat(t.getChild(i), pred);
      if (node != null) {
        return node;
      }
    }
    return null;
  }

  /**
   * Список токенов дерева разбора.
   * <p>
   * Токены формируются на основании всех потомков вида {@link TerminalNode} переданного дерева.
   *
   * @param tree Дерево разбора
   * @return Список токенов
   */
  public static List<Token> getTokens(ParseTree tree) {
    if (tree instanceof ParserRuleContext parserRuleContext) {
      return parserRuleContext.getTokens();
    } else if (tree instanceof TerminalNode node) {
      return List.of(node.getSymbol());
    } else {
      return getTokensFromParseTree(tree);
    }
  }

  /**
   * Возвращает список токенов из дерева
   *
   * @param tree Дерево
   * @return tokens Список токенов дерева
   */
  public static List<Token> getTokensFromParseTree(ParseTree tree) {
    if (tree.getChildCount() == 0) {
      return Collections.emptyList();
    }

    List<Token> results = new ArrayList<>();
    getTokensFromParseTreeReq(tree, results);
    return Collections.unmodifiableList(results);
  }

  /**
   * Проверяет среди всех дочерних элементов (рекурсивно) наличие узла с ошибкой
   *
   * @return true - если есть узел с ошибкой
   */
  public static boolean treeContainsErrors(ParseTree tnc) {
    return treeContainsErrors(tnc, true);
  }

  /**
   * Проверяет среди дочерних элементов узла наличие узла с ошибкой.
   * В отличие от treeContainsErrors не спускается вниз.
   *
   * @return true - если есть узел с ошибкой
   */
  public static boolean nodeContainsErrors(ParseTree tnc) {
    return treeContainsErrors(tnc, false);
  }

  /**
   * Проверяет наличие дочернего узла нужного типа
   *
   * @param tree  Текущий узел
   * @param types Нужные типы
   * @return Признак наличия дочернего узла
   */
  public static boolean nodeContains(ParseTree tree, Integer... types) {
    if (types.length == 0) {
      return false;
    }

    if (types.length == 1) {
      if (tree instanceof ParserRuleContext rule && rule.getIndex() == types[0]) {
        return true;
      }
      return IntStream.range(0, tree.getChildCount())
        .anyMatch(i -> nodeContains(tree.getChild(i), types));
    } else {
      var indexes = Set.copyOf(Arrays.asList(types));
      if (tree instanceof ParserRuleContext rule && indexes.contains(rule.getIndex())) {
        return true;
      }
      return IntStream.range(0, tree.getChildCount())
        .anyMatch(i -> nodeContains(tree.getChild(i), indexes));
    }
  }

  /**
   * Проверяет наличие дочернего узла нужного типа, исключая переданный узел
   *
   * @param tree    Текущий узел
   * @param exclude Исключаемый узел
   * @param types   Нужные типы
   * @return Признак наличия дочернего узла
   */
  public static boolean nodeContains(ParseTree tree, ParseTree exclude, Integer... types) {
    var indexes = Set.copyOf(Arrays.asList(types));

    if (tree instanceof ParserRuleContext rule && !tree.equals(exclude) && indexes.contains(rule.getIndex())) {
      return true;
    }

    return IntStream.range(0, tree.getChildCount())
      .anyMatch(i -> nodeContains(tree.getChild(i), exclude, indexes));
  }

  /**
   * Выполняет поиск предыдущей ноды нужного типа
   *
   * @param parent - родительская нода, среди дочерних которой производится поиск
   * @param tnc    - нода, для которой ищем предыдущую
   * @param index  - BSLParser.RULE_*
   * @return tnc - если предыдущая нода не найдена, вернет текущую
   */
  public static ParseTree getPreviousNode(ParseTree parent, ParseTree tnc, int index) {
    List<ParseTree> descendants = getDescendantsWithFilter(parent, tnc, index);
    int pos = descendants.indexOf(tnc);
    if (pos > 0) {
      return descendants.get(pos - 1);
    }
    return tnc;
  }

  /**
   * @param tokens     - список токенов
   * @param tokenIndex - индекс текущего токена в переданном списке токенов
   * @param tokenType  - тип искомого токена
   * @return предыдущий токен, если он был найден
   */
  public static Optional<Token> getPreviousTokenFromDefaultChannel(List<Token> tokens, int tokenIndex, int tokenType) {
    if (tokenIndex < 0 || tokenIndex >= tokens.size()) {
      throw new IllegalArgumentException("tokenIndex out of range: " + tokenIndex);
    }
    
    while (true) {
      if (tokenIndex == 0) {
        return Optional.empty();
      }
      var token = tokens.get(--tokenIndex);
      if (token.getChannel() == Token.DEFAULT_CHANNEL && token.getType() == tokenType) {
        return Optional.of(token);
      }
    }
  }

  /**
   * @param tokens     - полный список токенов
   * @param tokenIndex - индекс текущего токена в переданном списке токенов
   * @return предыдущий токен, если он был найден
   */
  public static Optional<Token> getPreviousTokenFromDefaultChannel(List<Token> tokens, int tokenIndex) {
    while (true) {
      if (tokenIndex == 0) {
        return Optional.empty();
      }
      var token = tokens.get(--tokenIndex);
      if (token.getChannel() == Token.DEFAULT_CHANNEL) {
        return Optional.of(token);
      }
    }
  }

  /**
   * Выполняет поиск следующей узла нужного типа
   *
   * @param parent - родительский узел, среди дочерних которой производится поиск
   * @param tnc    - текущий узел, для которого ищем следующий
   * @param index  - BSLParser.RULE_*
   * @return tnc - если следующий узел не найден, вернет текущий
   */
  public static ParseTree getNextNode(ParseTree parent, ParseTree tnc, int index) {
    List<ParseTree> descendants = getDescendantsWithFilter(parent, tnc, index);
    int pos = descendants.indexOf(tnc);
    if (pos + 1 < descendants.size()) {
      return descendants.get(pos + 1);
    }
    return tnc;
  }

  private static <T extends ParseTree> void findAllNodesReq(ParseTree tree, Class<T> clazz, List<T> nodes) {
    if (clazz.isInstance(tree)) {
      nodes.add(clazz.cast(tree));
    }
    for (int i = 0; i < tree.getChildCount(); i++) {
      findAllNodesReq(tree.getChild(i), clazz, nodes);
    }
  }

  private static void getTokensFromParseTreeReq(ParseTree tree, List<Token> tokens) {
    for (var i = 0; i < tree.getChildCount(); i++) {
      var child = tree.getChild(i);
      if (child instanceof TerminalNode node) {
        tokens.add(node.getSymbol());
      } else {
        getTokensFromParseTreeReq(child, tokens);
      }
    }
  }

  private static void flatten(ParseTree t, List<ParseTree> flatList) {
    flatList.add(t);

    int n = t.getChildCount();
    for (var i = 0; i < n; i++) {
      flatten(t.getChild(i), flatList);
    }
  }

  private static void flatten(ParseTree tree, List<ParserRuleContext> flatList, Collection<Integer> ruleIndexes) {
    if (tree instanceof ParserRuleContext parserRuleContext && ruleIndexes.contains(parserRuleContext.getIndex())) {
      flatList.add(parserRuleContext);
    }

    int n = tree.getChildCount();
    for (var i = 0; i < n; i++) {
      flatten(tree.getChild(i), flatList, ruleIndexes);
    }
  }

  private static boolean treeContainsErrors(ParseTree tnc, boolean recursive) {
    if (!(tnc instanceof ParserRuleContext ruleContext)) {
      return false;
    }

    if (ruleContext.exception != null) {
      return true;
    }

    return recursive
      && ruleContext.children != null
      && ruleContext.children.stream().anyMatch(Trees::treeContainsErrors);
  }

  private static List<ParseTree> getDescendantsWithFilter(ParseTree parent, ParseTree tnc, int ruleindex) {
    List<ParseTree> descendants;
    if (tnc instanceof ParserRuleContext && tnc.getIndex() == ruleindex) {
      descendants = new ArrayList<>(findAllRuleNodes(parent, ruleindex));
    } else {
      descendants = getDescendants(parent)
        .stream()
        .filter(ParserRuleContext.class::isInstance)
        .filter(node -> (node.equals(tnc) || node.getIndex() == ruleindex))
        .toList();
    }
    return descendants;
  }

  private static Collection<ParserRuleContext> findAllTopLevelDescendantNodesInner(ParseTree root,
                                                                                   Collection<Integer> indexes) {
    if (root instanceof ParserRuleContext rule && indexes.contains(rule.getIndex())) {
      return List.of(rule);
    }

    List<ParserRuleContext> result = new ArrayList<>();
    IntStream.range(0, root.getChildCount())
      .mapToObj(i -> findAllTopLevelDescendantNodesInner(root.getChild(i), indexes))
      .forEachOrdered(result::addAll);

    return result;
  }

  private static boolean nodeContains(ParseTree tree, ParseTree exclude, Set<Integer> indexes) {
    if (tree instanceof ParserRuleContext rule && !tree.equals(exclude) && indexes.contains(rule.getIndex())) {
      return true;
    }

    return IntStream.range(0, tree.getChildCount())
      .anyMatch(i -> nodeContains(tree.getChild(i), exclude, indexes));
  }

  private static boolean nodeContains(ParseTree tree, Set<Integer> indexes) {
    if (tree instanceof ParserRuleContext rule && indexes.contains(rule.getIndex())) {
      return true;
    }
    return IntStream.range(0, tree.getChildCount())
      .anyMatch(i -> nodeContains(tree.getChild(i), indexes));
  }
}
