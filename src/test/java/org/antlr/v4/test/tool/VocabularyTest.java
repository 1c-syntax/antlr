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

import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.Vocabulary;
import org.antlr.v4.runtime.VocabularyImpl;
import org.junit.jupiter.api.Test;

import static org.antlr.v4.TestUtils.assertEquals;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Sam Harwell
 */
public class VocabularyTest extends AbstractBaseTest {

  @Test
  public void testEmptyVocabulary() {
    assertThat(VocabularyImpl.EMPTY_VOCABULARY).isNotNull();
    assertEquals("EOF", VocabularyImpl.EMPTY_VOCABULARY.getSymbolicName(Token.EOF));
    assertEquals("0", VocabularyImpl.EMPTY_VOCABULARY.getDisplayName(Token.INVALID_TYPE));
  }

  @Test
  public void testVocabularyFromTokenNames() {
    String[] tokenNames = {
      "<INVALID>",
      "TOKEN_REF", "RULE_REF", "'//'", "'/'", "'*'", "'!'", "ID", "STRING"
    };

    Vocabulary vocabulary = VocabularyImpl.fromTokenNames(tokenNames);
    assertThat(vocabulary).isNotNull();
    assertEquals("EOF", vocabulary.getSymbolicName(Token.EOF));
    for (int i = 0; i < tokenNames.length; i++) {
      assertEquals(tokenNames[i], vocabulary.getDisplayName(i));

      if (tokenNames[i].startsWith("'")) {
        assertEquals(tokenNames[i], vocabulary.getLiteralName(i));
        assertThat(vocabulary.getSymbolicName(i)).isNull();
      } else if (Character.isUpperCase(tokenNames[i].charAt(0))) {
        assertThat(vocabulary.getLiteralName(i)).isNull();
        assertEquals(tokenNames[i], vocabulary.getSymbolicName(i));
      } else {
        assertThat(vocabulary.getLiteralName(i)).isNull();
        assertThat(vocabulary.getSymbolicName(i)).isNull();
      }
    }
  }

}
