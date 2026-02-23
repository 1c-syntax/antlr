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
import lombok.experimental.Accessors;
import org.antlr.v4.runtime.atn.ATN;
import org.antlr.v4.runtime.atn.LexerATNSimulator;
import org.antlr.v4.runtime.misc.IntegerStack;
import org.antlr.v4.runtime.misc.Interval;
import org.antlr.v4.runtime.misc.Pair;
import org.antlr.v4.runtime.misc.Tuple;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.EmptyStackException;

/**
 * A lexer is recognizer that draws input symbols from a character stream. lexer grammars result in a subclass of this
 * object. A Lexer object uses simplified match() and error recovery mechanisms in the interest of speed.
 */
@NullMarked
public abstract class Lexer extends Recognizer<Integer, LexerATNSimulator> implements TokenSource {
  public static final int DEFAULT_MODE = 0;
  public static final int MORE = -2;
  public static final int SKIP = -3;

  public static final int DEFAULT_TOKEN_CHANNEL = Token.DEFAULT_CHANNEL;
  public static final int HIDDEN = Token.HIDDEN_CHANNEL;
  public static final int MIN_CHAR_VALUE = 0x0000;
  public static final int MAX_CHAR_VALUE = 0x10FFFF;

  private final IntegerStack modeStack = new IntegerStack();

  @Getter
  private CharStream inputStream;

  private Pair<? extends TokenSource, CharStream> tokenFactorySourcePair;

  /**
   * How to create token objects
   */
  @Setter
  @Getter
  private TokenFactory tokenFactory = CommonTokenFactory.DEFAULT;

  /**
   * The goal of all lexer rules/methods is to create a token object. This is an instance variable as multiple rules may
   * collaborate to create a single token.  nextToken will return this object after matching lexer rule(s).  If you
   * subclass to allow multiple token emissions, then set this to the last token to be matched or something nonnull so
   * that the auto token emit mechanism will not emit another token.
   */
  @Nullable
  private Token token;

  /**
   * What character index in the stream did the current token start at? Needed, for example, to get the text for current
   * token.  Set at the start of nextToken.
   */
  @Getter
  private int tokenStartCharIndex = -1;

  /**
   * The line on which the first character of the token resides
   */
  @Getter
  private int tokenStartLine;

  /**
   * The character position of first character within the line
   */
  @Getter
  private int tokenStartCharPositionInLine;

  /**
   * Once we see EOF on char stream, next token will be EOF. If you have DONE : EOF ; then you see DONE EOF.
   */
  @Getter
  private boolean hitEOF;

  /**
   * The channel number for the current token
   */
  @Setter
  private int channel;

  /**
   * The token type for the current token
   */
  @Setter
  @Getter
  private int type = Token.INVALID_TYPE;

  @Setter
  @Accessors(fluent = true)
  private int mode = Lexer.DEFAULT_MODE;

  /**
   * You can set the text for the current token to override what is in the input char buffer.  Use setText() or can set
   * this instance var.
   */
  private @Nullable String text;

  public abstract String[] getChannelNames();

  public abstract String[] getModeNames();

  protected Lexer(CharStream inputStream) {
    this.inputStream = inputStream;
    this.tokenFactorySourcePair = Tuple.create(this, inputStream);
  }

  @Override
  public Token nextToken() {
    // Mark start location in char stream so unbuffered streams are
    // guaranteed at least have text of current token
    int tokenStartMarker = inputStream.mark();
    try {
      outer:
      while (true) {
        if (hitEOF) {
          return emitEOF();
        }

        token = null;
        channel = Token.DEFAULT_CHANNEL;
        tokenStartCharIndex = inputStream.index();
        tokenStartCharPositionInLine = getInterpreter().getCharPositionInLine();
        tokenStartLine = getInterpreter().getLine();
        text = null;
        do {
          type = Token.INVALID_TYPE;
          int ttype;
          try {
            ttype = getInterpreter().match(inputStream, mode);
          } catch (LexerNoViableAltException e) {
            notifyListeners(e);    // report error
            recover(e);
            ttype = SKIP;
          }
          if (inputStream.LA(1) == IntStream.EOF) {
            hitEOF = true;
          }
          if (type == Token.INVALID_TYPE) {
            type = ttype;
          }
          if (type == SKIP) {
            continue outer;
          }
        } while (type == MORE);
        if (token == null) {
          emit();
        }
        return token;
      }
    } finally {
      // make sure we release marker after match or
      // unbuffered char stream will keep buffering
      inputStream.release(tokenStartMarker);
    }
  }

  @Override
  public String getSourceName() {
    return inputStream.getSourceName();
  }

  public void more() {
    type = MORE;
  }

  public void pushMode(int m) {
    if (LexerATNSimulator.debug) {
      System.out.println("pushMode " + m);
    }
    modeStack.push(mode);
    mode(m);
  }

  public int popMode() {
    if (modeStack.isEmpty()) {
      throw new EmptyStackException();
    }
    if (LexerATNSimulator.debug) {
      System.out.println("popMode back to " + modeStack.peek());
    }
    mode(modeStack.pop());
    return mode;
  }

  public void reset() {
    // wack Lexer state variables
    inputStream.seek(0); // rewind the input
    token = null;
    type = Token.INVALID_TYPE;
    channel = Token.DEFAULT_CHANNEL;
    tokenStartCharIndex = -1;
    tokenStartCharPositionInLine = -1;
    tokenStartLine = -1;
    text = null;

    hitEOF = false;
    mode = Lexer.DEFAULT_MODE;
    modeStack.clear();

    getInterpreter().reset();
  }

  /**
   * Set the char stream and reset the lexer
   */
  public void setInputStream(CharStream inputStream) {
    if (hitEOF || !modeStack.isEmpty() || type != Token.INVALID_TYPE) {
      reset();
    }
    this.inputStream = inputStream;
    this.tokenFactorySourcePair = Tuple.create(this, this.inputStream);
  }

  public void skip() {
    type = SKIP;
  }

  /**
   * The standard method called to automatically emit a token at the outermost lexical rule.  The token object should
   * point into the char buffer start..stop.  If there is a text override in 'text', use that to set the token's text.
   * Override this method to emit custom Token objects or provide a new factory.
   */
  protected void emit() {
    token = tokenFactory.create(tokenFactorySourcePair, type, text, channel, tokenStartCharIndex, getCharIndex() - 1,
      tokenStartLine, tokenStartCharPositionInLine);
  }

  private Token emitEOF() {
    int cpos = getCharPositionInLine();
    int line = getLine();
    Token eof = tokenFactory.create(tokenFactorySourcePair,
      Token.EOF,
      null,
      Token.DEFAULT_CHANNEL,
      inputStream.index(),
      inputStream.index() - 1,
      line,
      cpos);
    token = eof;
    return eof;
  }

  @Override
  public int getLine() {
    return getInterpreter().getLine();
  }

  @Override
  public int getCharPositionInLine() {
    return getInterpreter().getCharPositionInLine();
  }

  /**
   * What is the index of the current character of lookahead?
   */
  private int getCharIndex() {
    return inputStream.index();
  }

  /**
   * Return the text matched so far for the current token or any text override.
   */
  protected String getText() {
    if (text != null) {
      return text;
    }
    return getInterpreter().getText(inputStream);
  }

  protected void recover(LexerNoViableAltException e) {
    if (inputStream.LA(1) != IntStream.EOF) {
      // skip a char and try again
      getInterpreter().consume(inputStream);
    }
  }

  /**
   * Lexers can normally match any char in it's vocabulary after matching a token, so do the easy thing and just kill a
   * character and hope it all works out.  You can instead use the rule invocation stack to do sophisticated error
   * recovery if you are in a fragment rule.
   */
  protected void recover(RecognitionException re) {
    // TODO: Do we lose character or line position information?
    inputStream.consume();
  }

  private void notifyListeners(LexerNoViableAltException e) {
    var inputText = inputStream.getText(Interval.of(tokenStartCharIndex, inputStream.index()));
    String msg = "token recognition error at: '" + getErrorDisplay(inputText) + "'";

    ANTLRErrorListener<? super Integer> listener = getErrorListenerDispatch();
    listener.syntaxError(this, null, tokenStartLine, tokenStartCharPositionInLine, msg, e);
  }

  private String getErrorDisplay(String s) {
    StringBuilder buf = new StringBuilder();
    for (char c : s.toCharArray()) {
      buf.append(getErrorDisplay(c));
    }
    return buf.toString();
  }

  private String getErrorDisplay(int c) {
    return switch (c) {
      case Token.EOF -> "<EOF>";
      case '\n' -> "\\n";
      case '\t' -> "\\t";
      case '\r' -> "\\r";
      default -> String.valueOf((char) c);
    };
  }

  @Deprecated(since = "Не используется")
  protected void validateInputStream(ATN atn, CharStream inputStream) {
    // метод оставлен для совместимости
  }
}
