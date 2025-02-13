/*
 * This file is a part of ANTLR.
 *
 * Copyright (c) 2012-2025 The ANTLR Project. All rights reserved.
 * Copyright (c) 2025 Valery Maximov <maximovvalery@gmail.com> and contributors
 *
 * Use of this file is governed by the BSD-3-Clause license that
 * can be found in the LICENSE.txt file in the project root.
 */
package org.antlr.v4.codegen.model;

import org.antlr.v4.codegen.OutputModelFactory;

import java.util.ArrayList;
import java.util.List;

public class Parser extends Recognizer {
  public ParserFile file;

  @ModelElement
  public List<RuleFunction> funcs = new ArrayList<RuleFunction>();

  public Parser(OutputModelFactory factory, ParserFile file) {
    super(factory);
    this.file = file; // who contains us?
  }
}
