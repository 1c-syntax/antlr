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

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class DollarParserTest extends AbstractBaseTest {

  @Test
  @Disabled("Переделать на ANTLR runtime/Generator")
  public void testSimpleCall() {
    String grammar = """
      grammar T;
      a : ID  { System.out.println( $parser.getSourceName() ); }
        ;
      ID : 'a'..'z'+ ;
      """;
    String found = execParser("T.g4", grammar, "TParser", "TLexer", "a", "x", true);
    assertThat(found.contains(this.getClass().getSimpleName())).isTrue();
    assertThat(this.stderrDuringParse).isNull();
  }

}
