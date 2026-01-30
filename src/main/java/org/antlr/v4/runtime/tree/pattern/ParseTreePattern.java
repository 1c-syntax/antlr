/*
 * This file is a part of ANTLR.
 *
 * Copyright (c) 2012-2025 The ANTLR Project. All rights reserved.
 * Copyright (c) 2025-2026 Valery Maximov <maximovvalery@gmail.com> and contributors
 *
 * Use of this file is governed by the BSD-3-Clause license that
 * can be found in the LICENSE.txt file in the project root.
 */
package org.antlr.v4.runtime.tree.pattern;

import lombok.Value;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.xpath.XPath;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * A pattern like {@code <ID> = <expr>;} converted to a {@link ParseTree} by
 * {@link ParseTreePatternMatcher#compile(String, int)}.
 */
@Value
public class ParseTreePattern {

  /**
   * This is the backing field for {@link #getPatternRuleIndex()}.
   */
  int patternRuleIndex;

  /**
   * This is the backing field for {@link #getPattern()}.
   */
  String pattern;

  /**
   * This is the backing field for {@link #getPatternTree()}.
   */
  ParseTree patternTree;

  /**
   * This is the backing field for {@link #getMatcher()}.
   */
  ParseTreePatternMatcher matcher;

  /**
   * Construct a new instance of the {@link ParseTreePattern} class.
   *
   * @param matcher          The {@link ParseTreePatternMatcher} which created this tree pattern.
   * @param pattern          The tree pattern in concrete syntax form.
   * @param patternRuleIndex The parser rule which serves as the root of the tree pattern.
   * @param patternTree      The tree pattern in {@link ParseTree} form.
   */
  public ParseTreePattern(ParseTreePatternMatcher matcher,
                          String pattern, int patternRuleIndex, ParseTree patternTree) {
    this.matcher = matcher;
    this.patternRuleIndex = patternRuleIndex;
    this.pattern = pattern;
    this.patternTree = patternTree;
  }

  /**
   * Match a specific parse tree against this tree pattern.
   *
   * @param tree The parse tree to match against this tree pattern.
   *
   * @return A {@link ParseTreeMatch} object describing the result of the match operation. The
   * {@link ParseTreeMatch#succeeded()} method can be used to determine whether or not the match was successful.
   */
  public ParseTreeMatch match(ParseTree tree) {
    return matcher.match(tree, this);
  }

  /**
   * Determine whether or not a parse tree matches this tree pattern.
   *
   * @param tree The parse tree to match against this tree pattern.
   *
   * @return {@code true} if {@code tree} is a match for the current tree pattern; otherwise, {@code false}.
   */
  public boolean matches(ParseTree tree) {
    return matcher.match(tree, this).succeeded();
  }

  /**
   * Find all nodes using XPath and then try to match those subtrees against this tree pattern.
   *
   * @param tree  The {@link ParseTree} to match against this pattern.
   * @param xpath An expression matching the nodes
   *
   * @return A collection of {@link ParseTreeMatch} objects describing the successful matches. Unsuccessful matches are
   * omitted from the result, regardless of the reason for the failure.
   */
  public List<ParseTreeMatch> findAll(ParseTree tree, String xpath) {
    Collection<ParseTree> subtrees = XPath.findAll(tree, xpath, matcher.getParser());
    List<ParseTreeMatch> matches = new ArrayList<>();
    for (ParseTree t : subtrees) {
      ParseTreeMatch match = match(t);
      if (match.succeeded()) {
        matches.add(match);
      }
    }
    return matches;
  }

}
