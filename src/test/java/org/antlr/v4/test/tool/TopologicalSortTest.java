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

import org.antlr.v4.misc.Graph;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.antlr.v4.TestUtils.assertEquals;

/**
 * Test topo sort in GraphNode.
 */
public class TopologicalSortTest extends AbstractBaseTest {
  @Test
  public void testFairlyLargeGraph() {
    Graph<String> g = new Graph<>();
    g.addEdge("C", "F");
    g.addEdge("C", "G");
    g.addEdge("C", "A");
    g.addEdge("C", "B");
    g.addEdge("A", "D");
    g.addEdge("A", "E");
    g.addEdge("B", "E");
    g.addEdge("D", "E");
    g.addEdge("D", "F");
    g.addEdge("F", "H");
    g.addEdge("E", "F");

    String expecting = "[H, F, G, E, D, A, B, C]";
    List<String> nodes = g.sort();
    String result = nodes.toString();
    assertEquals(expecting, result);
  }

  @Test
  public void testCyclicGraph() {
    Graph<String> g = new Graph<>();
    g.addEdge("A", "B");
    g.addEdge("B", "C");
    g.addEdge("C", "A");
    g.addEdge("C", "D");

    String expecting = "[D, C, B, A]";
    List<String> nodes = g.sort();
    String result = nodes.toString();
    assertEquals(expecting, result);
  }

  @Test
  public void testRepeatedEdges() {
    Graph<String> g = new Graph<>();
    g.addEdge("A", "B");
    g.addEdge("B", "C");
    g.addEdge("A", "B"); // dup
    g.addEdge("C", "D");

    String expecting = "[D, C, B, A]";
    List<String> nodes = g.sort();
    String result = nodes.toString();
    assertEquals(expecting, result);
  }

  @Test
  public void testSimpleTokenDependence() {
    Graph<String> g = new Graph<>();
    g.addEdge("Java.g4", "MyJava.tokens"); // Java feeds off manual token file
    g.addEdge("Java.tokens", "Java.g4");
    g.addEdge("Def.g4", "Java.tokens");    // walkers feed off generated tokens
    g.addEdge("Ref.g4", "Java.tokens");

    String expecting = "[MyJava.tokens, Java.g4, Java.tokens, Def.g4, Ref.g4]";
    List<String> nodes = g.sort();
    String result = nodes.toString();
    assertEquals(expecting, result);
  }

  @Test
  public void testParserLexerCombo() {
    Graph<String> g = new Graph<>();
    g.addEdge("JavaLexer.tokens", "JavaLexer.g4");
    g.addEdge("JavaParser.g4", "JavaLexer.tokens");
    g.addEdge("Def.g4", "JavaLexer.tokens");
    g.addEdge("Ref.g4", "JavaLexer.tokens");

    String expecting = "[JavaLexer.g4, JavaLexer.tokens, JavaParser.g4, Def.g4, Ref.g4]";
    List<String> nodes = g.sort();
    String result = nodes.toString();
    assertEquals(expecting, result);
  }
}
