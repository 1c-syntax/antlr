/**
 * This file is a part of ANTLR.
 *
 * Copyright (c) 2012-2025 The ANTLR Project. All rights reserved.
 * Copyright (c) 2025 Valery Maximov <maximovvalery@gmail.com> and contributors
 *
 * Use of this file is governed by the BSD-3-Clause license that
 * can be found in the LICENSE.txt file in the project root.
 */
package org.antlr.v4.runtime;

import com.github._1c_syntax.utils.Lazy;
import org.antlr.v4.runtime.misc.Interval;
import org.antlr.v4.runtime.misc.Nullable;
import org.antlr.v4.runtime.tree.ErrorNode;
import org.antlr.v4.runtime.tree.ErrorNodeImpl;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeListener;
import org.antlr.v4.runtime.tree.TerminalNode;
import org.antlr.v4.runtime.tree.Trees;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A rule invocation record for parsing.
 * <p>
 * Contains all of the information about the current rule not stored in the
 * RuleContext. It handles parse tree children list, Any ATN state
 * tracing, and the default values available for rule invocations:
 * start, stop, rule index, current alt number.
 * <p>
 * Subclasses made for each rule and grammar track the parameters,
 * return values, locals, and labels specific to that rule. These
 * are the objects that are returned from rules.
 * <p>
 * Note text is not an actual field of a rule return value; it is computed
 * from start and stop using the input stream's toString() method.  I
 * could add a ctor to this so that we can pass in and store the input
 * stream, but I'm not sure we want to do that.  It would seem to be undefined
 * to get the .text property anyway if the rule matches tokens from multiple
 * input streams.
 * <p>
 * I do not use getters for fields of objects that are used simply to
 * group values such as this aggregate.  The getters/setters are there to
 * satisfy the superclass interface.
 */
public class ParserRuleContext extends RuleContext {
  private static final ParserRuleContext EMPTY = new ParserRuleContext();

  /**
   * Ленивое хранение текста.
   * Несколько увеличивает потребление памяти в угоду ускорения получения содержимого
   */
  private final Lazy<String> text = new Lazy<>(super::getText);

  /**
   * Ленивое хранение токенов.
   * Несколько увеличивает потребление памяти в угоду ускорения получения содержимого
   */
  private final Lazy<List<Token>> tokens = new Lazy<>(this::computeTokens);

  /**
   * If we are debugging or building a parse tree for a visitor,
   * we need to track all of the tokens and rule invocations associated
   * with this rule's context. This is empty for parsing w/o tree constr.
   * operation because we don't the need to track the details about
   * how we parse this rule.
   */
  public List<ParseTree> children;

  /**
   * Стартовая позиция (токен начала)
   */
  public Token start;

  /**
   * Конечная позиция (токен конца)
   */
  public Token stop;

  /**
   * The exception that forced this rule to return. If the rule successfully
   * completed, this is {@code null}.
   */
  public RecognitionException exception;

  public ParserRuleContext() {
  }

  public static ParserRuleContext emptyContext() {
    return EMPTY;
  }

  /**
   * COPY a ctx (I'm deliberately not using copy constructor) to avoid
   * confusion with creating node with parent. Does not copy children
   * (except error leaves).
   *
   * <p>This is used in the generated parser code to flip a generic XContext
   * node for rule X to a YContext for alt label Y. In that sense, it is not
   * really a generic copy function.</p>
   *
   * <p>If we do an error sync() at start of a rule, we might add error nodes
   * to the generic XContext so this function must copy those nodes to the
   * YContext as well else they are lost!</p>
   */
  public void copyFrom(ParserRuleContext ctx) {
    this.parent = ctx.parent;
    this.invokingState = ctx.invokingState;

    this.start = ctx.start;
    this.stop = ctx.stop;

    // copy any error nodes to alt label node
    if (ctx.children != null) {
      this.children = new ArrayList<>();
      // reset parent pointer for any error nodes
      for (ParseTree child : ctx.children) {
        if (child instanceof ErrorNodeImpl) {
          ((ErrorNodeImpl) child).setParent(this);
        }

        addAnyChild(child);
      }
    }
  }

  public ParserRuleContext(@Nullable ParserRuleContext parent, int invokingStateNumber) {
    super(parent, invokingStateNumber);
  }

  // Double dispatch methods for listeners

  public void enterRule(ParseTreeListener listener) {
  }

  public void exitRule(ParseTreeListener listener) {
  }

  /**
   * Add a parse tree node to this as a child.  Works for
   * internal and leaf nodes. Does not set parent link;
   * other add methods must do that. Other addChild methods
   * call this.
   * <p>
   * We cannot set the parent pointer of the incoming node
   * because the existing interfaces do not have a setParent()
   * method and I don't want to break backward compatibility for this.
   *
   * @since 4.7
   */
  public <T extends ParseTree> T addAnyChild(T t) {
    assert t.getParent() == null || t.getParent() == this;

    if (children == null) {
      children = new ArrayList<>();
    }
    children.add(t);
    return t;
  }

  public void addChild(RuleContext ruleInvocation) {
    addAnyChild(ruleInvocation);
  }

  /**
   * Add a token leaf node child.
   */
  public void addChild(TerminalNode t) {
    addAnyChild(t);
  }

  /**
   * Add an error node child.
   *
   * @since 4.7
   */
  public ErrorNode addErrorNode(ErrorNode errorNode) {
    return addAnyChild(errorNode);
  }

  /**
   * Used by enterOuterAlt to toss out a RuleContext previously added as
   * we entered a rule. If we have # label, we will need to remove
   * generic ruleContext object.
   */
  public void removeLastChild() {
    if (children != null) {
      children.remove(children.size() - 1);
    }
  }

  /**
   * Override to make type more specific
   */
  @Override
  public ParserRuleContext getParent() {
    return (ParserRuleContext) super.getParent();
  }

  @Override
  public ParseTree getChild(int i) {
    return children != null && i >= 0 && i < children.size() ? children.get(i) : null;
  }

  public <T extends ParseTree> T getChild(Class<? extends T> ctxType, int i) {
    if (children == null || i < 0 || i >= children.size()) {
      return null;
    }

    int j = -1; // what element have we found with ctxType?
    for (ParseTree o : children) {
      if (ctxType.isInstance(o)) {
        j++;
        if (j == i) {
          return ctxType.cast(o);
        }
      }
    }
    return null;
  }

  public TerminalNode getToken(int ttype, int i) {
    if (children == null || i < 0 || i >= children.size()) {
      return null;
    }

    int j = -1; // what token with ttype have we found?
    for (ParseTree o : children) {
      if (o instanceof TerminalNode tnode) {
        var symbol = tnode.getSymbol();
        if (symbol.getType() == ttype) {
          j++;
          if (j == i) {
            return tnode;
          }
        }
      }
    }

    return null;
  }

  public List<? extends TerminalNode> getTokens(int ttype) {
    if (children == null) {
      return Collections.emptyList();
    }

    List<TerminalNode> tokens = null;
    for (ParseTree o : children) {
      if (o instanceof TerminalNode tnode) {
        var symbol = tnode.getSymbol();
        if (symbol.getType() == ttype) {
          if (tokens == null) {
            tokens = new ArrayList<>();
          }
          tokens.add(tnode);
        }
      }
    }

    if (tokens == null) {
      return Collections.emptyList();
    }

    return tokens;
  }

  public <T extends ParserRuleContext> T getRuleContext(Class<? extends T> ctxType, int i) {
    return getChild(ctxType, i);
  }

  public <T extends ParserRuleContext> List<? extends T> getRuleContexts(Class<? extends T> ctxType) {
    if (children == null) {
      return Collections.emptyList();
    }

    List<T> contexts = null;
    for (ParseTree o : children) {
      if (ctxType.isInstance(o)) {
        if (contexts == null) {
          contexts = new ArrayList<>();
        }

        contexts.add(ctxType.cast(o));
      }
    }

    if (contexts == null) {
      return Collections.emptyList();
    }

    return contexts;
  }

  @Override
  public int getChildCount() {
    return children != null ? children.size() : 0;
  }

  @Override
  public Interval getSourceInterval() {
    if (start == null) {
      return Interval.INVALID;
    }
    if (stop == null || stop.getTokenIndex() < start.getTokenIndex()) {
      return Interval.of(start.getTokenIndex(), start.getTokenIndex() - 1); // empty
    }
    return Interval.of(start.getTokenIndex(), stop.getTokenIndex());
  }

  /**
   * Get the initial token in this context.
   * Note that the range from start to stop is inclusive, so for rules that do not consume anything
   * (for example, zero length or error productions) this token may exceed stop.
   */
  public Token getStart() {
    return start;
  }

  /**
   * Get the final token in this context.
   * Note that the range from start to stop is inclusive, so for rules that do not consume anything
   * (for example, zero length or error productions) this token may precede start.
   */
  public Token getStop() {
    return stop;
  }

  /**
   * Used for rule context info debugging during parse-time, not so much for ATN debugging
   */
  public String toInfoString(Parser recognizer) {
    List<String> rules = recognizer.getRuleInvocationStack(this);
    Collections.reverse(rules);
    return "ParserRuleContext" + rules + "{" +
      "start=" + start +
      ", stop=" + stop +
      '}';
  }

  @Override
  public String getText() {
    return text.getOrCompute();
  }

  @Override
  public ParserRuleContext getRuleContext() {
    return (ParserRuleContext) super.getRuleContext();
  }

  public List<Token> getTokens() {
    return tokens.getOrCompute();
  }

  private List<Token> computeTokens() {
    return Trees.getTokensFromParseTree(this);
  }
}

