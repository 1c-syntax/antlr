/*
 * This file is a part of ANTLR.
 *
 * Copyright (c) 2012-2025 The ANTLR Project. All rights reserved.
 * Copyright (c) 2025 Valery Maximov <maximovvalery@gmail.com> and contributors
 *
 * Use of this file is governed by the BSD-3-Clause license that
 * can be found in the LICENSE.txt file in the project root.
 */
package org.antlr.v4.tool;

import org.antlr.runtime.Token;
import org.antlr.v4.runtime.misc.NotNull;
import org.antlr.v4.runtime.misc.Nullable;
import org.stringtemplate.v4.ST;

import java.util.Arrays;

public class ANTLRMessage {
  private static final Object[] EMPTY_ARGS = new Object[0];

  @NotNull
  private final ErrorType errorType;
  @Nullable
  private final Object[] args;
  @Nullable
  private final Throwable e;

  // used for location template
  public String fileName;
  public int line = -1;
  public int charPosition = -1;

  public Grammar g;
  /**
   * Most of the time, we'll have a token such as an undefined rule ref
   * and so this will be set.
   */
  public Token offendingToken;

  public ANTLRMessage(@NotNull ErrorType errorType) {
    this(errorType, (Throwable) null, Token.INVALID_TOKEN);
  }

  public ANTLRMessage(@NotNull ErrorType errorType, Token offendingToken, Object... args) {
    this(errorType, null, offendingToken, args);
  }

  public ANTLRMessage(@NotNull ErrorType errorType, @Nullable Throwable e, Token offendingToken, Object... args) {
    this.errorType = errorType;
    this.e = e;
    this.args = args;
    this.offendingToken = offendingToken;
  }

  @NotNull
  public ErrorType getErrorType() {
    return errorType;
  }

  @NotNull
  public Object[] getArgs() {
    if (args == null) {
      return EMPTY_ARGS;
    }

    return args;
  }

  public ST getMessageTemplate(boolean verbose) {
    ST messageST = new ST(getErrorType().msg);
    messageST.impl.name = errorType.name();

    messageST.add("verbose", verbose);
    Object[] args = getArgs();
    for (int i = 0; i < args.length; i++) {
      String attr = "arg";
      if (i > 0) attr += i + 1;
      messageST.add(attr, args[i]);
    }
    if (args.length < 2) messageST.add("arg2", null); // some messages ref arg2

    Throwable cause = getCause();
    if (cause != null) {
      messageST.add("exception", cause);
      messageST.add("stackTrace", cause.getStackTrace());
    } else {
      messageST.add("exception", null); // avoid ST error msg
      messageST.add("stackTrace", null);
    }

    return messageST;
  }

  @Nullable
  public Throwable getCause() {
    return e;
  }

  @Override
  public String toString() {
    return "Message{" +
      "errorType=" + getErrorType() +
      ", args=" + Arrays.asList(getArgs()) +
      ", e=" + getCause() +
      ", fileName='" + fileName + '\'' +
      ", line=" + line +
      ", charPosition=" + charPosition +
      '}';
  }
}
