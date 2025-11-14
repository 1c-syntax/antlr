/**
 * This file is a part of ANTLR.
 * <p>
 * Copyright (c) 2012-2025 The ANTLR Project. All rights reserved.
 * Copyright (c) 2025 Valery Maximov <maximovvalery@gmail.com> and contributors
 * <p>
 * Use of this file is governed by the BSD-3-Clause license that
 * can be found in the LICENSE.txt file in the project root.
 */
package org.antlr.v4.runtime.tree;

import org.antlr.v4.runtime.CommonToken;
import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.atn.ATN;
import org.antlr.v4.runtime.misc.Interval;
import org.antlr.v4.runtime.misc.NotNull;
import org.antlr.v4.runtime.misc.Nullable;
import org.antlr.v4.runtime.misc.Predicate;
import org.antlr.v4.runtime.misc.Utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * A set of utility routines useful for all kinds of ANTLR trees.
 */
public class Trees {
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
    buf.append("(");
    s = Utils.escapeWhitespace(getNodeText(t, ruleNames), false);
    buf.append(s);
    buf.append(' ');
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
        var ruleIndex = ruleContext.getRuleIndex();
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
   * Return true if ancestor is node's parent or a node on path to root from node.
   * Use == not equals().
   *
   * @since 4.5.1
   */
  public static boolean isAncestorOf(Tree ancestor, Tree node) {
    if (ancestor == null || node == null || ancestor.getParent() == null) {
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
      if (ctx.getRuleIndex() == index) {
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
    List<ParseTree> nodes = new ArrayList<>();
    nodes.add(t);

    int n = t.getChildCount();
    for (int i = 0; i < n; i++) {
      nodes.addAll(getDescendants(t.getChild(i)));
    }
    return nodes;
  }

  /**
   * @deprecated
   */
  @Deprecated
  public static List<ParseTree> descendants(ParseTree t) {
    return getDescendants(t);
  }

  /**
   * Find smallest subtree of t enclosing range startTokenIndex..stopTokenIndex
   * inclusively using postorder traversal.  Recursive depth-first-search.
   *
   * @since 4.5
   */
  @Nullable
  public static ParserRuleContext getRootOfSubtreeEnclosingRegion(@NotNull ParseTree t,
                                                                  int startTokenIndex, // inclusive
                                                                  int stopTokenIndex)  // inclusive
  {
    int n = t.getChildCount();
    for (int i = 0; i < n; i++) {
      ParseTree child = t.getChild(i);
      ParserRuleContext r = getRootOfSubtreeEnclosingRegion(child, startTokenIndex, stopTokenIndex);
      if (r != null) return r;
    }
    if (t instanceof ParserRuleContext r) {
      if (startTokenIndex >= r.getStart().getTokenIndex() && // is range fully contained in t?
        (r.getStop() == null || stopTokenIndex <= r.getStop().getTokenIndex())) {
        // note: r.getStop()==null likely implies that we bailed out of parser and there's nothing to the right
        return r;
      }
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
  public static void stripChildrenOutOfRange(ParserRuleContext t,
                                             ParserRuleContext root,
                                             int startIndex,
                                             int stopIndex) {
    if (t == null) return;
    for (int i = 0; i < t.getChildCount(); i++) {
      ParseTree child = t.getChild(i);
      Interval range = child.getSourceInterval();
      if (child instanceof ParserRuleContext && (range.b < startIndex || range.a > stopIndex)) {
        if (isAncestorOf(child, root)) { // replace only if subtree doesn't have displayed root
          CommonToken abbrev = new CommonToken(Token.INVALID_TYPE, "...");
          t.children.set(i, new TerminalNodeImpl(abbrev));
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
    if (pred.eval(t)) return t;

    if (t == null) return null;

    int n = t.getChildCount();
    for (int i = 0; i < n; i++) {
      Tree u = findNodeSuchThat(t.getChild(i), pred);
      if (u != null) return u;
    }
    return null;
  }

  private Trees() {
  }

  private static <T extends ParseTree> void findAllNodesReq(ParseTree tree, Class<T> clazz, List<T> nodes) {
    if (clazz.isInstance(tree)) {
      nodes.add(clazz.cast(tree));
    }
    for (int i = 0; i < tree.getChildCount(); i++) {
      findAllNodesReq(tree.getChild(i), clazz, nodes);
    }
  }
}
