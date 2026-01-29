/*
 * This file is a part of ANTLR.
 *
 * Copyright (c) 2012-2025 The ANTLR Project. All rights reserved.
 * Copyright (c) 2025-2026 Valery Maximov <maximovvalery@gmail.com> and contributors
 *
 * Use of this file is governed by the BSD-3-Clause license that
 * can be found in the LICENSE.txt file in the project root.
 */
package org.antlr.v4.runtime;

import lombok.Getter;
import lombok.Setter;
import org.antlr.v4.runtime.atn.ATN;
import org.antlr.v4.runtime.atn.ATNSimulator;
import org.antlr.v4.runtime.atn.ParseInfo;
import org.antlr.v4.runtime.misc.Args;
import org.antlr.v4.runtime.misc.Utils;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

@NullMarked
public abstract class Recognizer<Symbol, ATNInterpreter extends ATNSimulator> {
  public static final int EOF = -1;

  private static final Map<Vocabulary, Map<String, Integer>> tokenTypeMapCache = new WeakHashMap<>();
  private static final Map<String[], Map<String, Integer>> ruleIndexMapCache = new WeakHashMap<>();

  private final List<ANTLRErrorListener<? super Symbol>> _listeners =
    new CopyOnWriteArrayList<>() {{
      add(ConsoleErrorListener.INSTANCE);
    }};

  @Getter
  @Setter
  private ATNInterpreter interpreter;

  @Getter
  @Setter
  private int state = -1;

  public abstract String getGrammarFileName();

  public abstract IntStream getInputStream();

  public abstract String[] getRuleNames();

  public abstract Vocabulary getVocabulary();

  /**
   * Get the {@link ATN} used by the recognizer for prediction.
   *
   * @return The {@link ATN} used by the recognizer for prediction.
   */
  public ATN getATN() {
    return interpreter.atn;
  }

  /**
   * Get a map from rule names to rule indexes.
   *
   * <p>Used for XPath and tree pattern compilation.</p>
   */
  public Map<String, Integer> getRuleIndexMap() {
    String[] ruleNames = getRuleNames();
    synchronized (ruleIndexMapCache) {
      return ruleIndexMapCache.computeIfAbsent(ruleNames, key -> Collections.unmodifiableMap(Utils.toMap(key)));
    }
  }

  /**
   * Get a map from token names to token types.
   *
   * <p>Used for XPath and tree pattern compilation.</p>
   */
  public Map<String, Integer> getTokenTypeMap() {
    Vocabulary vocabulary = getVocabulary();

    synchronized (tokenTypeMapCache) {
      Map<String, Integer> result = tokenTypeMapCache.get(vocabulary);
      if (result == null) {
        result = new HashMap<>();
        for (int i = 0; i <= getATN().maxTokenType; i++) {
          String literalName = vocabulary.getLiteralName(i);
          if (literalName != null) {
            result.put(literalName, i);
          }

          String symbolicName = vocabulary.getSymbolicName(i);
          if (symbolicName != null) {
            result.put(symbolicName, i);
          }
        }

        result.put("EOF", Token.EOF);
        result = Collections.unmodifiableMap(result);
        tokenTypeMapCache.put(vocabulary, result);
      }

      return result;
    }
  }

  public int getTokenType(String tokenName) {
    var ttype = getTokenTypeMap().get(tokenName);
    if (ttype != null) {
      return ttype;
    }
    return Token.INVALID_TYPE;
  }

  public List<? extends ANTLRErrorListener<? super Symbol>> getErrorListeners() {
    return new ArrayList<>(_listeners);
  }

  public ANTLRErrorListener<? super Symbol> getErrorListenerDispatch() {
    return new ProxyErrorListener<>(getErrorListeners());
  }

  /**
   * If this recognizer was generated, it will have a serialized ATN representation of the grammar.
   *
   * <p>For interpreters, we don't know their serialized ATN despite having
   * created the interpreter from it.</p>
   */
  public String getSerializedATN() {
    throw new UnsupportedOperationException("there is no serialized ATN");
  }

  /**
   * If profiling during the parse/lex, this will return DecisionInfo records for each decision in recognizer in a
   * ParseInfo object.
   *
   * @since 4.3
   */
  @Nullable
  public ParseInfo getParseInfo() {
    return null;
  }

  /**
   * What is the error header, normally line/character position information?
   */
  public String getErrorHeader(RecognitionException e) {
    int line = e.getOffendingToken().getLine();
    int charPositionInLine = e.getOffendingToken().getCharPositionInLine();
    return "line " + line + ":" + charPositionInLine;
  }

  /**
   * @throws NullPointerException if {@code listener} is {@code null}.
   */
  public void addErrorListener(ANTLRErrorListener<? super Symbol> listener) {
    Args.notNull("listener", listener);
    _listeners.add(listener);
  }

  public void removeErrorListener(ANTLRErrorListener<? super Symbol> listener) {
    _listeners.remove(listener);
  }

  public void removeErrorListeners() {
    _listeners.clear();
  }

  // subclass needs to override these if there are sempreds or actions
  // that the ATN interp needs to execute
  public void action(@Nullable RuleContext _localctx, int ruleIndex, int actionIndex) {
  }

  public boolean precpred(@Nullable RuleContext localctx, int precedence) {
    return true;
  }

  public boolean sempred(@Nullable RuleContext _localctx, int ruleIndex, int actionIndex) {
    return true;
  }
}
