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

import org.antlr.runtime.RecognitionException;
import org.antlr.runtime.Token;

/**
 * A problem with the syntax of your antlr grammar such as
 * "The '{' came as a complete surprise to me at this point in your program"
 */
public class GrammarSyntaxMessage extends ANTLRMessage {
  public GrammarSyntaxMessage(ErrorType etype,
                              String fileName,
                              Token offendingToken,
                              RecognitionException antlrException,
                              Object... args) {
    super(etype, antlrException, offendingToken, args);
    this.fileName = fileName;
    this.offendingToken = offendingToken;
    if (offendingToken != null) {
      line = offendingToken.getLine();
      charPosition = offendingToken.getCharPositionInLine();
    }
  }

  @Override
  public RecognitionException getCause() {
    return (RecognitionException) super.getCause();
  }
}
