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

import org.antlr.v4.unicode.UnicodeData;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class UnicodeDataTest {
  @Test
  public void testUnicodeGeneralCategoriesLatin() {
    assertThat(UnicodeData.getPropertyCodePoints("Lu").contains('X')).isTrue();
    assertThat(UnicodeData.getPropertyCodePoints("Lu").contains('x')).isFalse();
    assertThat(UnicodeData.getPropertyCodePoints("Ll").contains('x')).isTrue();
    assertThat(UnicodeData.getPropertyCodePoints("Ll").contains('X')).isFalse();
    assertThat(UnicodeData.getPropertyCodePoints("L").contains('X')).isTrue();
    assertThat(UnicodeData.getPropertyCodePoints("L").contains('x')).isTrue();
    assertThat(UnicodeData.getPropertyCodePoints("N").contains('0')).isTrue();
    assertThat(UnicodeData.getPropertyCodePoints("Z").contains(' ')).isTrue();
  }

  @Test
  public void testUnicodeGeneralCategoriesBMP() {
    assertThat(UnicodeData.getPropertyCodePoints("Lu").contains('\u1E3A')).isTrue();
    assertThat(UnicodeData.getPropertyCodePoints("Lu").contains('\u1E3B')).isFalse();
    assertThat(UnicodeData.getPropertyCodePoints("Ll").contains('\u1E3B')).isTrue();
    assertThat(UnicodeData.getPropertyCodePoints("Ll").contains('\u1E3A')).isFalse();
    assertThat(UnicodeData.getPropertyCodePoints("L").contains('\u1E3A')).isTrue();
    assertThat(UnicodeData.getPropertyCodePoints("L").contains('\u1E3B')).isTrue();
    assertThat(UnicodeData.getPropertyCodePoints("N").contains('\u1BB0')).isTrue();
    assertThat(UnicodeData.getPropertyCodePoints("N").contains('\u1E3A')).isFalse();
    assertThat(UnicodeData.getPropertyCodePoints("Z").contains('\u2028')).isTrue();
    assertThat(UnicodeData.getPropertyCodePoints("Z").contains('\u1E3A')).isFalse();
  }

  @Test
  public void testUnicodeGeneralCategoriesSMP() {
    assertThat(UnicodeData.getPropertyCodePoints("Lu").contains(0x1D5D4)).isTrue();
    assertThat(UnicodeData.getPropertyCodePoints("Lu").contains(0x1D770)).isFalse();
    assertThat(UnicodeData.getPropertyCodePoints("Ll").contains(0x1D770)).isTrue();
    assertThat(UnicodeData.getPropertyCodePoints("Ll").contains(0x1D5D4)).isFalse();
    assertThat(UnicodeData.getPropertyCodePoints("L").contains(0x1D5D4)).isTrue();
    assertThat(UnicodeData.getPropertyCodePoints("L").contains(0x1D770)).isTrue();
    assertThat(UnicodeData.getPropertyCodePoints("N").contains(0x11C50)).isTrue();
    assertThat(UnicodeData.getPropertyCodePoints("N").contains(0x1D5D4)).isFalse();
  }

  @Test
  public void testUnicodeCategoryAliases() {
    assertThat(UnicodeData.getPropertyCodePoints("Lowercase_Letter").contains('x')).isTrue();
    assertThat(UnicodeData.getPropertyCodePoints("Lowercase_Letter").contains('X')).isFalse();
    assertThat(UnicodeData.getPropertyCodePoints("Letter").contains('x')).isTrue();
    assertThat(UnicodeData.getPropertyCodePoints("Letter").contains('0')).isFalse();
    assertThat(UnicodeData.getPropertyCodePoints("Enclosing_Mark").contains(0x20E2)).isTrue();
    assertThat(UnicodeData.getPropertyCodePoints("Enclosing_Mark").contains('x')).isFalse();
  }

  @Test
  public void testUnicodeBinaryProperties() {
    assertThat(UnicodeData.getPropertyCodePoints("Emoji").contains(0x1F4A9)).isTrue();
    assertThat(UnicodeData.getPropertyCodePoints("Emoji").contains('X')).isFalse();
    assertThat(UnicodeData.getPropertyCodePoints("alnum").contains('9')).isTrue();
    assertThat(UnicodeData.getPropertyCodePoints("alnum").contains(0x1F4A9)).isFalse();
    assertThat(UnicodeData.getPropertyCodePoints("Dash").contains('-')).isTrue();
    assertThat(UnicodeData.getPropertyCodePoints("Hex").contains('D')).isTrue();
    assertThat(UnicodeData.getPropertyCodePoints("Hex").contains('Q')).isFalse();
  }

  @Test
  public void testUnicodeBinaryPropertyAliases() {
    assertThat(UnicodeData.getPropertyCodePoints("Ideo").contains('\u611B')).isTrue();
    assertThat(UnicodeData.getPropertyCodePoints("Ideo").contains('X')).isFalse();
    assertThat(UnicodeData.getPropertyCodePoints("Soft_Dotted").contains('\u0456')).isTrue();
    assertThat(UnicodeData.getPropertyCodePoints("Soft_Dotted").contains('X')).isFalse();
    assertThat(UnicodeData.getPropertyCodePoints("Noncharacter_Code_Point").contains('\uFFFF')).isTrue();
    assertThat(UnicodeData.getPropertyCodePoints("Noncharacter_Code_Point").contains('X')).isFalse();
  }

  @Test
  public void testUnicodeScripts() {
    assertThat(UnicodeData.getPropertyCodePoints("Zyyy").contains('0')).isTrue();
    assertThat(UnicodeData.getPropertyCodePoints("Latn").contains('X')).isTrue();
    assertThat(UnicodeData.getPropertyCodePoints("Hani").contains(0x4E04)).isTrue();
    assertThat(UnicodeData.getPropertyCodePoints("Cyrl").contains(0x0404)).isTrue();
  }

  @Test
  public void testUnicodeScriptEquals() {
    assertThat(UnicodeData.getPropertyCodePoints("Script=Zyyy").contains('0')).isTrue();
    assertThat(UnicodeData.getPropertyCodePoints("Script=Latn").contains('X')).isTrue();
    assertThat(UnicodeData.getPropertyCodePoints("Script=Hani").contains(0x4E04)).isTrue();
    assertThat(UnicodeData.getPropertyCodePoints("Script=Cyrl").contains(0x0404)).isTrue();
  }

  @Test
  public void testUnicodeScriptAliases() {
    assertThat(UnicodeData.getPropertyCodePoints("Common").contains('0')).isTrue();
    assertThat(UnicodeData.getPropertyCodePoints("Latin").contains('X')).isTrue();
    assertThat(UnicodeData.getPropertyCodePoints("Han").contains(0x4E04)).isTrue();
    assertThat(UnicodeData.getPropertyCodePoints("Cyrillic").contains(0x0404)).isTrue();
  }

  @Test
  public void testUnicodeBlocks() {
    assertThat(UnicodeData.getPropertyCodePoints("InASCII").contains('0')).isTrue();
    assertThat(UnicodeData.getPropertyCodePoints("InCJK").contains(0x4E04)).isTrue();
    assertThat(UnicodeData.getPropertyCodePoints("InCyrillic").contains(0x0404)).isTrue();
    assertThat(UnicodeData.getPropertyCodePoints("InMisc_Pictographs").contains(0x1F4A9)).isTrue();
  }

  @Test
  public void testUnicodeBlockEquals() {
    assertThat(UnicodeData.getPropertyCodePoints("Block=ASCII").contains('0')).isTrue();
    assertThat(UnicodeData.getPropertyCodePoints("Block=CJK").contains(0x4E04)).isTrue();
    assertThat(UnicodeData.getPropertyCodePoints("Block=Cyrillic").contains(0x0404)).isTrue();
    assertThat(UnicodeData.getPropertyCodePoints("Block=Misc_Pictographs").contains(0x1F4A9)).isTrue();
  }

  @Test
  public void testUnicodeBlockAliases() {
    assertThat(UnicodeData.getPropertyCodePoints("InBasic_Latin").contains('0')).isTrue();
    assertThat(UnicodeData.getPropertyCodePoints("InMiscellaneous_Mathematical_Symbols_B")
      .contains(0x29BE)).isTrue();
  }

  @Test
  public void testEnumeratedPropertyEquals() {
    assertThat(UnicodeData.getPropertyCodePoints("Grapheme_Cluster_Break=E_Base").contains(0x1F481))
      .as("U+1F481 INFORMATION DESK PERSON is an emoji modifier base")
      .isTrue();

    assertThat(UnicodeData.getPropertyCodePoints("Grapheme_Cluster_Break=E_Base").contains(0x1F47E))
      .as("U+1F47E ALIEN MONSTER is not an emoji modifier")
      .isFalse();

    assertThat(UnicodeData.getPropertyCodePoints("Grapheme_Cluster_Break=E_Base").contains(0x1F481))
      .as("U+0E33 THAI CHARACTER SARA AM is a spacing mark")
      .isTrue();

    assertThat(UnicodeData.getPropertyCodePoints("Grapheme_Cluster_Break=E_Base").contains(0x1038))
      .as("U+1038 MYANMAR SIGN VISARGA is not a spacing mark")
      .isFalse();

    assertThat(UnicodeData.getPropertyCodePoints("East_Asian_Width=Ambiguous").contains(0x00A1))
      .as("U+00A1 INVERTED EXCLAMATION MARK has ambiguous East Asian Width")
      .isTrue();

    assertThat(UnicodeData.getPropertyCodePoints("East_Asian_Width=Ambiguous").contains(0x00A2))
      .as("U+00A2 CENT SIGN does not have ambiguous East Asian Width")
      .isFalse();
  }

  @Test
  public void extendedPictographic() {
    assertThat(UnicodeData.getPropertyCodePoints("Extended_Pictographic").contains(0x1F588))
      .as("U+1F588 BLACK PUSHPIN is in Extended Pictographic")
      .isTrue();

    assertThat(UnicodeData.getPropertyCodePoints("Extended_Pictographic").contains('0'))
      .as("0 is not in Extended Pictographic")
      .isFalse();
  }

  @Test
  public void emojiPresentation() {
    assertThat(UnicodeData.getPropertyCodePoints("EmojiPresentation=EmojiDefault").contains(0x1F4A9))
      .as("U+1F4A9 PILE OF POO is in EmojiPresentation=EmojiDefault")
      .isTrue();

    assertThat(UnicodeData.getPropertyCodePoints("EmojiPresentation=EmojiDefault").contains('0'))
      .as("0 is not in EmojiPresentation=EmojiDefault")
      .isFalse();

    assertThat(UnicodeData.getPropertyCodePoints("EmojiPresentation=EmojiDefault").contains('A'))
      .as("A is not in EmojiPresentation=EmojiDefault")
      .isFalse();

    assertThat(UnicodeData.getPropertyCodePoints("EmojiPresentation=TextDefault").contains(0x1F4A9))
      .as("U+1F4A9 PILE OF POO is not in EmojiPresentation=TextDefault")
      .isFalse();

    assertThat(UnicodeData.getPropertyCodePoints("EmojiPresentation=TextDefault").contains('0'))
      .as("0 is in EmojiPresentation=TextDefault")
      .isTrue();

    assertThat(UnicodeData.getPropertyCodePoints("EmojiPresentation=TextDefault").contains('A'))
      .as("A is not in EmojiPresentation=TextDefault")
      .isFalse();
  }

  @Test
  public void testPropertyCaseInsensitivity() {
    assertThat(UnicodeData.getPropertyCodePoints("l").contains('x')).isTrue();
    assertThat(UnicodeData.getPropertyCodePoints("l").contains('0')).isFalse();
    assertThat(UnicodeData.getPropertyCodePoints("common").contains('0')).isTrue();
    assertThat(UnicodeData.getPropertyCodePoints("Alnum").contains('0')).isTrue();
  }

  @Test
  public void testPropertyDashSameAsUnderscore() {
    assertThat(UnicodeData.getPropertyCodePoints("InLatin-1").contains('\u00F0')).isTrue();
  }
}
