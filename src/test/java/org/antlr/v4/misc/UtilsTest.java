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

import org.antlr.runtime.Token;
import org.antlr.v4.runtime.misc.Func1;
import org.antlr.v4.runtime.misc.Predicate;
import org.antlr.v4.tool.ast.GrammarAST;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;

public class UtilsTest {

  @Test
  public void testStripFileExtension() {
    assertThat(Utils.stripFileExtension(null)).isNull();
    assertThat(Utils.stripFileExtension("foo")).isEqualTo("foo");
    assertThat(Utils.stripFileExtension("foo.txt")).isEqualTo("foo");
  }

  @Test
  public void testJoin() {
    assertThat(Utils.join(new String[]{"foo", "bar"}, "b")).isEqualTo("foobbar");
    assertThat(Utils.join(new String[]{"foo", "bar"}, ",")).isEqualTo("foo,bar");
  }

  @Test
  public void testSortLinesInString() {
    assertThat(Utils.sortLinesInString("foo\nbar\nbaz")).isEqualTo("bar\nbaz\nfoo\n");
  }

  @Test
  public void testNodesToStrings() {
    ArrayList<GrammarAST> values = new ArrayList<>();
    values.add(new GrammarAST(Token.EOR_TOKEN_TYPE));
    values.add(new GrammarAST(Token.DOWN));
    values.add(new GrammarAST(Token.UP));

    assertThat(Utils.nodesToStrings(null)).isNull();
    assertThat(Utils.nodesToStrings(values)).isNotNull();
  }

  @Test
  public void testCapitalize() {
    assertThat(Utils.capitalize("foo")).isEqualTo("Foo");
  }

  @Test
  public void testDecapitalize() {
    assertThat(Utils.decapitalize("FOO")).isEqualTo("fOO");
  }

  @Test
  public void testSelect() {
    ArrayList<String> strings = new ArrayList<>();
    strings.add("foo");
    strings.add("bar");

    var func1 = new Func1<String, String>() {
      @Override
      public String eval(String arg1) {
        return "baz";
      }
    };

    ArrayList<String> retval = new ArrayList<>();
    retval.add("baz");
    retval.add("baz");

    assertThat(Utils.select(strings, func1)).isEqualTo(retval);
    assertThat(Utils.select(null, null)).isNull();
  }

  @Test
  public void testFind() {
    ArrayList<String> strings = new ArrayList<>();
    strings.add("foo");
    strings.add("bar");
    assertThat(Utils.find(strings, String.class)).isEqualTo("foo");

    assertThat(Utils.find(new ArrayList<>(), String.class)).isNull();
  }

  @Test
  public void testIndexOf() {
    ArrayList<String> strings = new ArrayList<>();
    strings.add("foo");
    strings.add("bar");
    var filter = new Predicate<>() {
      @Override
      public boolean eval(Object o) {
        return true;
      }
    };
    assertThat(Utils.indexOf(strings, filter)).isEqualTo(0);
    assertThat(Utils.indexOf(new ArrayList<>(), null)).isEqualTo(-1);
  }

  @Test
  public void testLastIndexOf() {
    ArrayList<String> strings = new ArrayList<>();
    strings.add("foo");
    strings.add("bar");
    var filter = new Predicate<>() {
      @Override
      public boolean eval(Object o) {
        return true;
      }
    };
    assertThat(Utils.lastIndexOf(strings, filter)).isEqualTo(1);
    assertThat(Utils.lastIndexOf(new ArrayList<>(), null)).isEqualTo(-1);
  }

  @Test
  public void testSetSize() {
    ArrayList<String> strings = new ArrayList<>();
    strings.add("foo");
    strings.add("bar");
    strings.add("baz");
    assertThat(strings.size()).isEqualTo(3);

    Utils.setSize(strings, 2);
    assertThat(strings.size()).isEqualTo(2);

    Utils.setSize(strings, 4);
    assertThat(strings.size()).isEqualTo(4);
  }
}
