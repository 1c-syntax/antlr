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
import org.antlr.v4.runtime.atn.PredictionMode;
import org.antlr.v4.runtime.misc.NotNull;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.InvocationTargetException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

import static org.antlr.v4.runtime.Token.EOF;

/**
 * Базовая реализация токенайзера
 * <p>
 * Требования к парсеру: класс PARSER должен иметь публичный конструктор,
 * принимающий единственный параметр типа {@link TokenStream}.
 *
 * @param <CONTEXT> Класс контекста
 * @param <PARSER>  Класс реализации парсера
 */
public abstract class Tokenizer<CONTEXT extends ParserRuleContext, PARSER extends Parser> {

  private InputStream content;
  private final Lexer lexer;
  private final Lazy<IncrementalTokenStream> tokenStream = new Lazy<>(this::computeTokenStream);
  private final Lazy<List<Token>> tokens = new Lazy<>(this::computeTokens);
  private final Lazy<CONTEXT> ast = new Lazy<>(this::computeAST);
  private final Class<PARSER> parserClass;
  protected PARSER parser;

  /**
   * Признак возможности ребилда (инкрементальный парсинг).
   * Заполняется только для инкрементальных парсеров {@link IncrementalParser}
   */
  private final boolean supportRebuild;
  private final ReentrantLock rebuildLock = new ReentrantLock();
  private CONTEXT oldAst;

  protected Tokenizer(@NotNull String content, @NotNull Lexer lexer, @NotNull Class<PARSER> parserClass) {
    this(IOUtils.toInputStream(content, StandardCharsets.UTF_8), lexer, parserClass);
  }

  protected Tokenizer(@NotNull InputStream content, @NotNull Lexer lexer, @NotNull Class<PARSER> parserClass) {
    this.content = content;
    this.lexer = lexer;
    this.parserClass = parserClass;
    this.supportRebuild = IncrementalParser.class.isAssignableFrom(parserClass);
  }

  /**
   * Возвращает список токенов, полученных при парсинге
   *
   * @return Список токенов
   */
  public List<Token> getTokens() {
    return tokens.getOrCompute();
  }

  /**
   * Возвращает абстрактное синтаксическое дерево, полученное на основании парсинга
   *
   * @return AST
   */
  public CONTEXT getAst() {
    return ast.getOrCompute();
  }

  /**
   * Выполняет обновление дерева на основании нового контента.
   * <p>
   * Актуально только для парсеров, поддерживающих инкрементальный анализ {@link IncrementalParserData}
   *
   * @param newContent Новый контент
   */
  public void rebuild(String newContent) {
    if (!supportRebuild) {
      return;
    }
    rebuild(IOUtils.toInputStream(newContent, StandardCharsets.UTF_8));
  }

  /**
   * Выполняет обновление дерева на основании нового контента.
   * <p>
   * Актуально только для парсеров, поддерживающих инкрементальный анализ {@link IncrementalParserData}
   *
   * @param newContent Новый контент
   */
  public void rebuild(@NotNull InputStream newContent) {
    if (!supportRebuild) {
      return;
    }

    rebuildLock.lock();

    try {
      oldAst = getAst(); // запоминаем дерево, которое было ДО
      tokens.clear();
      tokenStream.clear();
      content = newContent;
      ast.clear();
    } finally {
      rebuildLock.unlock();
    }
  }

  private List<Token> computeTokens() {
    var tokensTemp = new ArrayList<>(getTokenStream().getTokens());
    if (tokensTemp.isEmpty()) {
      return tokensTemp;
    }
    var lastToken = tokensTemp.get(tokensTemp.size() - 1);
    // спрячем токен в скрытый канал, чтобы не мешался
    if (lastToken.getType() == EOF && lastToken instanceof CommonToken commonToken) {
      commonToken.setChannel(Lexer.HIDDEN);
    }

    return tokensTemp;
  }

  private CONTEXT computeAST() {
    var thatTokenStream = getTokenStream();
    if (parser == null || !supportRebuild) {
      parser = createParser(thatTokenStream);
    } else {
      var parserData = new IncrementalParserData(thatTokenStream, oldAst);
      oldAst = null;
      parser = createParser(thatTokenStream, parserData);
    }

    parser.removeErrorListener(ConsoleErrorListener.INSTANCE);
    try {
      parser.getInterpreter().setPredictionMode(PredictionMode.SLL);
      return rootAST();
    } catch (Exception ex) {
      parser.reset(); // rewind input stream
      parser.getInterpreter().setPredictionMode(PredictionMode.LL);
    }
    return rootAST();
  }

  /**
   * Возвращает корневой узел AST.
   * <p>
   * Этот метод должен вызывать соответствующий rule-метод парсера
   * для построения дерева разбора (например, {@code parser.compilationUnit()}).
   *
   * @return корневой контекст AST
   */
  protected abstract CONTEXT rootAST();

  private IncrementalTokenStream computeTokenStream() {

    CharStream input;

    try (
      var ubis = new UnicodeBOMInputStream(content);
      Reader inputStreamReader = new InputStreamReader(ubis, StandardCharsets.UTF_8)
    ) {
      ubis.skipBOM();
      input = CharStreams.fromReader(inputStreamReader);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    lexer.setInputStream(input);
    lexer.removeErrorListener(ConsoleErrorListener.INSTANCE);

    var tempTokenStream = new IncrementalTokenStream(lexer);
    tempTokenStream.fill();
    return tempTokenStream;
  }

  protected IncrementalTokenStream getTokenStream() {
    final var tokenStreamUnboxed = tokenStream.getOrCompute();
    tokenStreamUnboxed.seek(0);
    return tokenStreamUnboxed;
  }

  private PARSER createParser(IncrementalTokenStream tokenStream) {
    try {
      if (supportRebuild) {
        return parserClass.getDeclaredConstructor(IncrementalTokenStream.class)
          .newInstance(tokenStream);
      } else {
        return parserClass.getDeclaredConstructor(TokenStream.class)
          .newInstance(tokenStream);
      }
    } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
      throw new RuntimeException(e);
    }
  }

  private PARSER createParser(IncrementalTokenStream tokenStream, IncrementalParserData parserData) {
    try {
      return parserClass.getDeclaredConstructor(IncrementalTokenStream.class, IncrementalParserData.class)
        .newInstance(tokenStream, parserData);
    } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
      throw new RuntimeException(e);
    }
  }
}
