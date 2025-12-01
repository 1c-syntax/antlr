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

/**
 * Defines behavior of object able to handle error messages from ANTLR including
 * both tool errors like "can't write file" and grammar ambiguity warnings.
 * To avoid having to change tools that use ANTLR (like GUIs), I am
 * wrapping error data in Message objects and passing them to the listener.
 * In this way, users of this interface are less sensitive to changes in
 * the info I need for error messages.
 */
public interface ANTLRToolListener {
  void info(String msg);

  void error(ANTLRMessage msg);

  void warning(ANTLRMessage msg);
}
