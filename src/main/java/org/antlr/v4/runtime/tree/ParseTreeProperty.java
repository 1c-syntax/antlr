/*
 * This file is a part of ANTLR.
 *
 * Copyright (c) 2012-2025 The ANTLR Project. All rights reserved.
 * Copyright (c) 2025 Valery Maximov <maximovvalery@gmail.com> and contributors
 *
 * Use of this file is governed by the BSD-3-Clause license that
 * can be found in the LICENSE.txt file in the project root.
 */
package org.antlr.v4.runtime.tree;

import java.util.IdentityHashMap;
import java.util.Map;

/**
 * Associate a property with a parse tree node. Useful with parse tree listeners
 * that need to associate values with particular tree nodes, kind of like
 * specifying a return value for the listener event method that visited a
 * particular node. Example:
 *
 * <pre>
 * ParseTreeProperty&lt;Integer&gt; values = new ParseTreeProperty&lt;Integer&gt;();
 * values.put(tree, 36);
 * int x = values.get(tree);
 * values.removeFrom(tree);
 * </pre>
 * <p>
 * You would make one decl (values here) in the listener and use lots of times
 * in your event methods.
 */
public class ParseTreeProperty<V> {
  protected Map<ParseTree, V> annotations = new IdentityHashMap<ParseTree, V>();

  public V get(ParseTree node) {
    return annotations.get(node);
  }

  public void put(ParseTree node, V value) {
    annotations.put(node, value);
  }

  public V removeFrom(ParseTree node) {
    return annotations.remove(node);
  }
}
