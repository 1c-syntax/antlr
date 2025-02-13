/*
 * This file is a part of ANTLR.
 *
 * Copyright (c) 2012-2025 The ANTLR Project. All rights reserved.
 * Copyright (c) 2025 Valery Maximov <maximovvalery@gmail.com> and contributors
 *
 * Use of this file is governed by the BSD-3-Clause license that
 * can be found in the LICENSE.txt file in the project root.
 */
package org.antlr.v4.misc;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * I need the get-element-i functionality so I'm subclassing
 * LinkedHashMap.
 */
public class OrderedHashMap<K, V> extends LinkedHashMap<K, V> {
  private static final long serialVersionUID = -4127551298268351889L;

  /**
   * Track the elements as they are added to the set
   */
  protected List<K> elements = new ArrayList<K>();

  public K getKey(int i) {
    return elements.get(i);
  }

  public V getElement(int i) {
    return get(elements.get(i));
  }

  @Override
  public V put(K key, V value) {
    elements.add(key);
    return super.put(key, value);
  }

  @Override
  public void putAll(Map<? extends K, ? extends V> m) {
    for (Map.Entry<? extends K, ? extends V> entry : m.entrySet()) {
      put(entry.getKey(), entry.getValue());
    }
  }

  @Override
  public V remove(Object key) {
    elements.remove(key);
    return super.remove(key);
  }

  @Override
  public void clear() {
    elements.clear();
    super.clear();
  }
}
