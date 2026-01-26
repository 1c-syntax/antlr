/*
 * This file is a part of ANTLR.
 *
 * Copyright (c) 2012-2025 The ANTLR Project. All rights reserved.
 * Copyright (c) 2025-2026 Valery Maximov <maximovvalery@gmail.com> and contributors
 *
 * Use of this file is governed by the BSD-3-Clause license that
 * can be found in the LICENSE.txt file in the project root.
 */
package org.antlr.v4.test.tool;

import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.Vocabulary;
import org.antlr.v4.runtime.VocabularyImpl;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Sam Harwell
 */
class VocabularyTest extends AbstractBaseTest {

  @Test
  void testEmptyVocabulary() {
    assertThat(VocabularyImpl.EMPTY_VOCABULARY).isNotNull();
    assertThat(VocabularyImpl.EMPTY_VOCABULARY.getSymbolicName(Token.EOF)).isEqualTo("EOF");
    assertThat(VocabularyImpl.EMPTY_VOCABULARY.getDisplayName(Token.INVALID_TYPE)).isEqualTo("0");
  }

  @Test
  void testVocabularyFromTokenNames() {
    String[] tokenNames = {
      "<INVALID>",
      "TOKEN_REF", "RULE_REF", "'//'", "'/'", "'*'", "'!'", "ID", "STRING"
    };

    Vocabulary vocabulary = VocabularyImpl.fromTokenNames(tokenNames);
    assertThat(vocabulary).isNotNull();
    assertThat(vocabulary.getSymbolicName(Token.EOF)).isEqualTo("EOF");
    for (int i = 0; i < tokenNames.length; i++) {
      assertThat(vocabulary.getDisplayName(i)).isEqualTo(tokenNames[i]);

      if (tokenNames[i].startsWith("'")) {
        assertThat(vocabulary.getLiteralName(i)).isEqualTo(tokenNames[i]);
        assertThat(vocabulary.getSymbolicName(i)).isNull();
      } else if (Character.isUpperCase(tokenNames[i].charAt(0))) {
        assertThat(vocabulary.getLiteralName(i)).isNull();
        assertThat(vocabulary.getSymbolicName(i)).isEqualTo(tokenNames[i]);
      } else {
        assertThat(vocabulary.getLiteralName(i)).isNull();
        assertThat(vocabulary.getSymbolicName(i)).isNull();
      }
    }
  }

}
