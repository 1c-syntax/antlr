/*
 * This file is a part of ANTLR.
 *
 * Copyright (c) 2012-2025 The ANTLR Project. All rights reserved.
 * Copyright (c) 2025-2026 Valery Maximov <maximovvalery@gmail.com> and contributors
 *
 * Use of this file is governed by the BSD-3-Clause license that
 * can be found in the LICENSE.txt file in the project root.
 */
package org.antlr.v4.tool.ast;

import lombok.Getter;
import org.antlr.runtime.Token;
import org.antlr.v4.misc.CharSupport;
import org.antlr.v4.tool.ErrorType;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

@NullMarked
public abstract class GrammarASTWithOptions extends GrammarAST {
  @Getter
  protected Map<String, @Nullable GrammarAST> options = new HashMap<>();

  public GrammarASTWithOptions(GrammarASTWithOptions node) {
    super(node);
    this.options = node.options;
  }

  public GrammarASTWithOptions(Token t) {
    super(t);
  }

  public GrammarASTWithOptions(int type) {
    super(type);
  }

  public GrammarASTWithOptions(int type, Token t) {
    super(type, t);
  }

  public GrammarASTWithOptions(int type, Token t, String text) {
    super(type, t, text);
  }

  public void setOption(String key, @Nullable GrammarAST node) {
    options.put(key, node);
  }

  @Nullable
  public String getOptionString(String key) {
    GrammarAST value = getOptionAST(key);
    if (value == null) {
      return null;
    }
    if (value instanceof ActionAST) {
      return value.getText();
    } else {
      String v = value.getText();
      if (v.startsWith("'") || v.startsWith("\"")) {
        v = CharSupport.getStringFromGrammarStringLiteral(v);
        if (v == null) {
          g.tool.errMgr.grammarError(ErrorType.INVALID_ESCAPE_SEQUENCE, g.fileName, value.getToken(), value.getText());
          v = "";
        }
      }
      return v;
    }
  }

  public int getOptionInt(String key) {
    var option = getOptionString(key);
    if (option == null) {
      return 0;
    }
    return Integer.parseInt(option);
  }

  /**
   * Gets AST node holding value for option key; ignores default options and command-line forced options.
   */
  @Nullable
  public GrammarAST getOptionAST(String key) {
    return options.get(key);
  }

  public int getNumberOfOptions() {
    return options.size();
  }

  @Override
  public abstract GrammarASTWithOptions dupNode();

}
