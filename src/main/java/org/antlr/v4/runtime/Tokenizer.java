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
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.InvocationTargetException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static java.util.Objects.requireNonNull;
import static org.antlr.v4.runtime.Token.EOF;

/**
 * Базовая реализация токенайзера
 *
 * @param <CONTEXT> Класс контекста
 * @param <PARSER> Класс реализации парсера
 */
public abstract class Tokenizer<CONTEXT extends ParserRuleContext, PARSER extends Parser> {

  private final InputStream content;
  private final Lexer lexer;
  private final Lazy<CommonTokenStream> tokenStream = new Lazy<>(this::computeTokenStream);
  private final Lazy<List<Token>> tokens = new Lazy<>(this::computeTokens);
  private final Lazy<CONTEXT> ast = new Lazy<>(this::computeAST);
  private final Class<PARSER> parserClass;
  protected PARSER parser;

  protected Tokenizer(String content, Lexer lexer, Class<PARSER> parserClass) {
    this(IOUtils.toInputStream(content, StandardCharsets.UTF_8), lexer, parserClass);
  }

  protected Tokenizer(InputStream content, Lexer lexer, Class<PARSER> parserClass) {
    requireNonNull(content);
    requireNonNull(lexer);
    this.content = content;
    this.lexer = lexer;
    this.parserClass = parserClass;
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

  private List<Token> computeTokens() {
    var tokensTemp = new ArrayList<>(getTokenStream().getTokens());
    var lastToken = tokensTemp.get(tokensTemp.size() - 1);
    if (lastToken.getType() == EOF && lastToken instanceof CommonToken commonToken) {
      commonToken.setChannel(Lexer.HIDDEN);
    }

    return tokensTemp;
  }

  private CONTEXT computeAST() {
    parser = createParser(getTokenStream());
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

  protected abstract CONTEXT rootAST();

  private CommonTokenStream computeTokenStream() {

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

    var tempTokenStream = new CommonTokenStream(lexer);
    tempTokenStream.fill();
    return tempTokenStream;
  }

  protected CommonTokenStream getTokenStream() {
    final CommonTokenStream tokenStreamUnboxed = tokenStream.getOrCompute();
    tokenStreamUnboxed.seek(0);
    return tokenStreamUnboxed;
  }

  private PARSER createParser(CommonTokenStream tokenStream) {
    try {
      return parserClass.getDeclaredConstructor(TokenStream.class)
        .newInstance(tokenStream);
    } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
      throw new RuntimeException(e);
    }
  }
}
