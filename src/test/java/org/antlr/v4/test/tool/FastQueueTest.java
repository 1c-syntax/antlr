/*
 * This file is a part of ANTLR.
 *
 * Copyright (c) 2012-2025 The ANTLR Project. All rights reserved.
 * Copyright (c) 2025 Valery Maximov <maximovvalery@gmail.com> and contributors
 *
 * Use of this file is governed by the BSD-3-Clause license that
 * can be found in the LICENSE.txt file in the project root.
 */
package org.antlr.v4.test.tool;

import org.antlr.runtime.misc.FastQueue;
import org.junit.jupiter.api.Test;

import java.util.NoSuchElementException;

import static org.antlr.v4.TestUtils.assertEquals;

public class FastQueueTest {
  @Test
  public void testQueueNoRemove() {
    FastQueue<String> q = new FastQueue<>();
    q.add("a");
    q.add("b");
    q.add("c");
    q.add("d");
    q.add("e");
    String expecting = "a b c d e";
    String found = q.toString();
    assertEquals(expecting, found);
  }

  @Test
  public void testQueueThenRemoveAll() {
    FastQueue<String> q = new FastQueue<>();
    q.add("a");
    q.add("b");
    q.add("c");
    q.add("d");
    q.add("e");
    StringBuilder buf = new StringBuilder();
    while (q.size() > 0) {
      String o = q.remove();
      buf.append(o);
      if (q.size() > 0) buf.append(" ");
    }
    assertEquals("queue should be empty", 0, q.size());
    String expecting = "a b c d e";
    String found = buf.toString();
    assertEquals(expecting, found);
  }

  @Test
  public void testQueueThenRemoveOneByOne() {
    StringBuilder buf = new StringBuilder();
    FastQueue<String> q = new FastQueue<>();
    q.add("a");
    buf.append(q.remove());
    q.add("b");
    buf.append(q.remove());
    q.add("c");
    buf.append(q.remove());
    q.add("d");
    buf.append(q.remove());
    q.add("e");
    buf.append(q.remove());
    assertEquals("queue should be empty", 0, q.size());
    String expecting = "abcde";
    String found = buf.toString();
    assertEquals(expecting, found);
  }

  // E r r o r s

  @Test
  public void testGetFromEmptyQueue() {
    FastQueue<String> q = new FastQueue<>();
    String msg = null;
    try {
      q.remove();
    } catch (NoSuchElementException nsee) {
      msg = nsee.getMessage();
    }
    String expecting = "queue index 0 > last index -1";
    String found = msg;
    assertEquals(expecting, found);
  }

  @Test
  public void testGetFromEmptyQueueAfterSomeAdds() {
    FastQueue<String> q = new FastQueue<>();
    q.add("a");
    q.add("b");
    q.remove();
    q.remove();
    String msg = null;
    try {
      q.remove();
    } catch (NoSuchElementException nsee) {
      msg = nsee.getMessage();
    }
    String expecting = "queue index 0 > last index -1";
    String found = msg;
    assertEquals(expecting, found);
  }

  @Test
  public void testGetFromEmptyQueueAfterClear() {
    FastQueue<String> q = new FastQueue<>();
    q.add("a");
    q.add("b");
    q.clear();
    String msg = null;
    try {
      q.remove();
    } catch (NoSuchElementException nsee) {
      msg = nsee.getMessage();
    }
    String expecting = "queue index 0 > last index -1";
    String found = msg;
    assertEquals(expecting, found);
  }
}
