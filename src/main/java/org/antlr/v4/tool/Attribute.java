/*
 * This file is a part of ANTLR.
 *
 * Copyright (c) 2012-2025 The ANTLR Project. All rights reserved.
 * Copyright (c) 2025-2026 Valery Maximov <maximovvalery@gmail.com> and contributors
 *
 * Use of this file is governed by the BSD-3-Clause license that
 * can be found in the LICENSE.txt file in the project root.
 */
package org.antlr.v4.tool;

import org.antlr.runtime.Token;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

/**
 * Track the names of attributes defined in arg lists, return values,
 * scope blocks etc...
 */
@NullMarked
public class Attribute {
  /**
   * The entire declaration such as "String foo" or "x:int"
   */
  public @Nullable String decl;

  /**
   * The type; might be empty such as for Python which has no static typing
   */
  public @Nullable String type;

  /**
   * The name of the attribute "foo"
   */
  public String name;

  /**
   * A {@link Token} giving the position of the name of this attribute in the grammar.
   */
  public Token token;

  /**
   * The optional attribute initialization expression
   */
  public @Nullable String initValue;

  /**
   * Who contains us?
   */
  public AttributeDict dict;

  public Attribute() {
  }

  public Attribute(String name) {
    this(name, null);
  }

  public Attribute(String name, @Nullable String decl) {
    this.name = name;
    this.decl = decl;
  }

  @Override
  public String toString() {
    if (initValue != null) {
      return name + ":" + type + "=" + initValue;
    }
    if (type != null) {
      return name + ":" + type;
    }
    return name;
  }
}
