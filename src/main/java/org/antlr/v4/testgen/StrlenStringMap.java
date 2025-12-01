/*
 * This file is a part of ANTLR.
 *
 * Copyright (c) 2012-2025 The ANTLR Project. All rights reserved.
 * Copyright (c) 2025 Valery Maximov <maximovvalery@gmail.com> and contributors
 *
 * Use of this file is governed by the BSD-3-Clause license that
 * can be found in the LICENSE.txt file in the project root.
 */
package org.antlr.v4.testgen;

import java.util.AbstractMap;
import java.util.Collections;
import java.util.Set;

/**
 * @author Sam Harwell
 */
public class StrlenStringMap extends AbstractMap<String, Object> {

  @Override
  public Object get(Object key) {
    if (key instanceof String str) {
      return str.length();
    }

    return super.get(key);
  }

  @Override
  public boolean containsKey(Object key) {
    return key instanceof String;
  }

  @Override
  public Set<Entry<String, Object>> entrySet() {
    return Collections.emptySet();
  }

}
