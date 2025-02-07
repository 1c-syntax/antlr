/*
 * This file is a part of ANTLR.
 *
 * Copyright (c) 2012-2025 The ANTLR Project. All rights reserved.
 * Copyright (c) 2025 Valery Maximov <maximovvalery@gmail.com> and contributors
 *
 * Use of this file is governed by the BSD-3-Clause license that
 * can be found in the LICENSE.txt file in the project root.
 */
package org.antlr.v4.runtime;

public interface WritableToken extends Token {
  void setText(String text);

  void setType(int ttype);

  void setLine(int line);

  void setCharPositionInLine(int pos);

  void setChannel(int channel);

  void setTokenIndex(int index);
}
