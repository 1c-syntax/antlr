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

import lombok.Getter;
import org.antlr.v4.runtime.misc.MultiMap;
import org.antlr.v4.runtime.tree.ParseTree;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.Collections;
import java.util.List;

/**
 * Represents the result of matching a {@link ParseTree} against a tree pattern.
 */
public class ParseTreeMatch {

  /**
   * This is the backing field for {@link #getTree()}. -- GETTER -- Get the parse tree we are trying to match to a
   * pattern.
   *
   */
  @Getter
  private final ParseTree tree;

  /**
   * This is the backing field for {@link #getPattern()}. -- GETTER -- Get the tree pattern we are matching against.
   *
   */
  @Getter
  private final ParseTreePattern pattern;

  /**
   * This is the backing field for {@link #getLabels()}. -- GETTER -- Return a mapping from label &rarr; [list of
   * nodes].
   * <p>The map includes special entries corresponding to the names of rules and
   * tokens referenced in tags in the original pattern. For additional information, see the description of .</p>
   */
  @Getter
  private final MultiMap<String, ParseTree> labels;

  /**
   * This is the backing field for {@link #getMismatchedNode()}.
   */
  private final @Nullable ParseTree mismatchedNode;

  /**
   * Constructs a new instance of {@link ParseTreeMatch} from the specified parse tree and pattern.
   *
   * @param tree           The parse tree to match against the pattern.
   * @param pattern        The parse tree pattern.
   * @param labels         A mapping from label names to collections of {@link ParseTree} objects located by the tree
   *                       pattern matching process.
   * @param mismatchedNode The first node which failed to match the tree pattern during the matching process.
   *
   * @throws IllegalArgumentException if {@code tree} is {@code null}
   * @throws IllegalArgumentException if {@code pattern} is {@code null}
   * @throws IllegalArgumentException if {@code labels} is {@code null}
   */
  public ParseTreeMatch(ParseTree tree,
                        ParseTreePattern pattern,
                        MultiMap<String, ParseTree> labels,
                        @Nullable ParseTree mismatchedNode) {

    this.tree = tree;
    this.pattern = pattern;
    this.labels = labels;
    this.mismatchedNode = mismatchedNode;
  }

  /**
   * Get the last node associated with a specific {@code label}.
   *
   * <p>For example, for pattern {@code <id:ID>}, {@code get("id")} returns the
   * node matched for that {@code ID}. If more than one node matched the specified label, only the last is returned. If
   * there is no node associated with the label, this returns {@code null}.</p>
   *
   * <p>Pattern tags like {@code <ID>} and {@code <expr>} without labels are
   * considered to be labeled with {@code ID} and {@code expr}, respectively.</p>
   *
   * @param label The label to check.
   *
   * @return The last {@link ParseTree} to match a tag with the specified label, or {@code null} if no parse tree
   * matched a tag with the label.
   */
  @Nullable
  public ParseTree get(String label) {
    List<ParseTree> parseTrees = labels.get(label);
    if (parseTrees == null || parseTrees.isEmpty()) {
      return null;
    }

    return parseTrees.get(parseTrees.size() - 1); // return last if multiple
  }

  /**
   * Return all nodes matching a rule or token tag with the specified label.
   *
   * <p>If the {@code label} is the name of a parser rule or token in the
   * grammar, the resulting list will contain both the parse trees matching rule or tags explicitly labeled with the
   * label and the complete set of parse trees matching the labeled and unlabeled tags in the pattern for the parser
   * rule or token. For example, if {@code label} is {@code "foo"}, the result will contain <em>all</em> of the
   * following.</p>
   *
   * <ul>
   * <li>Parse tree nodes matching tags of the form {@code <foo:anyRuleName>} and
   * {@code <foo:AnyTokenName>}.</li>
   * <li>Parse tree nodes matching tags of the form {@code <anyLabel:foo>}.</li>
   * <li>Parse tree nodes matching tags of the form {@code <foo>}.</li>
   * </ul>
   *
   * @param label The label.
   *
   * @return A collection of all {@link ParseTree} nodes matching tags with the specified {@code label}. If no nodes
   * matched the label, an empty list is returned.
   */
  public List<ParseTree> getAll(String label) {
    List<ParseTree> nodes = labels.get(label);
    if (nodes == null) {
      return Collections.emptyList();
    }

    return nodes;
  }

  /**
   * Get the node at which we first detected a mismatch.
   *
   * @return the node at which we first detected a mismatch, or {@code null} if the match was successful.
   */
  @Nullable
  public ParseTree getMismatchedNode() {
    return mismatchedNode;
  }

  /**
   * Gets a value indicating whether the match operation succeeded.
   *
   * @return {@code true} if the match operation succeeded; otherwise, {@code false}.
   */
  public boolean succeeded() {
    return mismatchedNode == null;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String toString() {
    return String.format(
      "Match %s; found %d labels",
      succeeded() ? "succeeded" : "failed",
      getLabels().size());
  }
}
