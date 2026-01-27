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

import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.misc.IntervalSet;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class IntervalSetTest extends AbstractBaseTest {

  @Test
  void testSingleElement() {
    IntervalSet s = IntervalSet.of(99);
    String expecting = "99";
    assertThat(s).hasToString(expecting);
  }

  @Test
  void testMin() {
    assertThat(IntervalSet.COMPLETE_CHAR_SET.getMinElement()).isEqualTo(0);
    assertThat(IntervalSet.COMPLETE_CHAR_SET.or(IntervalSet.of(Token.EPSILON)).getMinElement()).isEqualTo(Token.EPSILON);
    assertThat(IntervalSet.COMPLETE_CHAR_SET.or(IntervalSet.of(Token.EOF)).getMinElement()).isEqualTo(Token.EOF);
  }

  @Test
  void testIsolatedElements() {
    IntervalSet s = new IntervalSet();
    s.add(1);
    s.add('z');
    s.add('\uFFF0');
    String expecting = "{1, 122, 65520}";
    assertThat(s).hasToString(expecting);
  }

  @Test
  void testMixedRangesAndElements() {
    IntervalSet s = new IntervalSet();
    s.add(1);
    s.add('a', 'z');
    s.add('0', '9');
    String expecting = "{1, 48..57, 97..122}";
    assertThat(s).hasToString(expecting);
  }

  @Test
  void testSimpleAnd() {
    IntervalSet s = IntervalSet.of(10, 20);
    IntervalSet s2 = IntervalSet.of(13, 15);
    String expecting = "{13..15}";
    assertThat(s.and(s2)).hasToString(expecting);
  }

  @Test
  void testRangeAndIsolatedElement() {
    IntervalSet s = IntervalSet.of('a', 'z');
    IntervalSet s2 = IntervalSet.of('d');
    String expecting = "100";
    assertThat(s.and(s2)).hasToString(expecting);
  }

  @Test
  void testEmptyIntersection() {
    IntervalSet s = IntervalSet.of('a', 'z');
    IntervalSet s2 = IntervalSet.of('0', '9');
    String expecting = "{}";
    assertThat(s.and(s2)).hasToString(expecting);
  }

  @Test
  void testEmptyIntersectionSingleElements() {
    IntervalSet s = IntervalSet.of('a');
    IntervalSet s2 = IntervalSet.of('d');
    String expecting = "{}";
    assertThat(s.and(s2)).hasToString(expecting);
  }

  @Test
  void testNotSingleElement() {
    IntervalSet vocabulary = IntervalSet.of(1, 1000);
    vocabulary.add(2000, 3000);
    IntervalSet s = IntervalSet.of(50, 50);
    String expecting = "{1..49, 51..1000, 2000..3000}";
    assertThat(s.complement(vocabulary)).hasToString(expecting);
  }

  @Test
  void testNotSet() {
    IntervalSet vocabulary = IntervalSet.of(1, 1000);
    IntervalSet s = IntervalSet.of(50, 60);
    s.add(5);
    s.add(250, 300);
    String expecting = "{1..4, 6..49, 61..249, 301..1000}";
    assertThat(s.complement(vocabulary)).hasToString(expecting);
  }

  @Test
  void testNotEqualSet() {
    IntervalSet vocabulary = IntervalSet.of(1, 1000);
    IntervalSet s = IntervalSet.of(1, 1000);
    String expecting = "{}";
    assertThat(s.complement(vocabulary)).hasToString(expecting);
  }

  @Test
  void testNotSetEdgeElement() {
    IntervalSet vocabulary = IntervalSet.of(1, 2);
    IntervalSet s = IntervalSet.of(1);
    String expecting = "2";
    assertThat(s.complement(vocabulary)).hasToString(expecting);
  }

  @Test
  void testNotSetFragmentedVocabulary() {
    IntervalSet vocabulary = IntervalSet.of(1, 255);
    vocabulary.add(1000, 2000);
    vocabulary.add(9999);
    IntervalSet s = IntervalSet.of(50, 60);
    s.add(3);
    s.add(250, 300);
    s.add(10000); // this is outside range of vocab and should be ignored
    String expecting = "{1..2, 4..49, 61..249, 1000..2000, 9999}";
    assertThat(s.complement(vocabulary)).hasToString(expecting);
  }

  @Test
  void testSubtractOfCompletelyContainedRange() {
    IntervalSet s = IntervalSet.of(10, 20);
    IntervalSet s2 = IntervalSet.of(12, 15);
    String expecting = "{10..11, 16..20}";
    assertThat(s.subtract(s2)).hasToString(expecting);
  }

  @Test
  void testSubtractFromSetWithEOF() {
    IntervalSet s = IntervalSet.of(10, 20);
    s.add(Token.EOF);
    IntervalSet s2 = IntervalSet.of(12, 15);
    String expecting = "{<EOF>, 10..11, 16..20}";
    assertThat(s.subtract(s2)).hasToString(expecting);
  }

  @Test
  void testSubtractOfOverlappingRangeFromLeft() {
    IntervalSet s = IntervalSet.of(10, 20);
    IntervalSet s2 = IntervalSet.of(5, 11);
    String expecting = "{12..20}";
    assertThat(s.subtract(s2)).hasToString(expecting);

    IntervalSet s3 = IntervalSet.of(5, 10);
    expecting = "{11..20}";
    assertThat(s.subtract(s3)).hasToString(expecting);
  }

  @Test
  void testSubtractOfOverlappingRangeFromRight() {
    IntervalSet s = IntervalSet.of(10, 20);
    IntervalSet s2 = IntervalSet.of(15, 25);
    String expecting = "{10..14}";
    assertThat(s.subtract(s2)).hasToString(expecting);

    IntervalSet s3 = IntervalSet.of(20, 25);
    expecting = "{10..19}";
    assertThat(s.subtract(s3)).hasToString(expecting);
  }

  @Test
  void testSubtractOfCompletelyCoveredRange() {
    IntervalSet s = IntervalSet.of(10, 20);
    IntervalSet s2 = IntervalSet.of(1, 25);
    String expecting = "{}";
    assertThat(s.subtract(s2)).hasToString(expecting);
  }

  @Test
  void testSubtractOfRangeSpanningMultipleRanges() {
    IntervalSet s = IntervalSet.of(10, 20);
    s.add(30, 40);
    s.add(50, 60); // s has 3 ranges now: 10..20, 30..40, 50..60
    IntervalSet s2 = IntervalSet.of(5, 55); // covers one and touches 2nd range
    String expecting = "{56..60}";
    assertThat(s.subtract(s2)).hasToString(expecting);

    IntervalSet s3 = IntervalSet.of(15, 55); // touches both
    expecting = "{10..14, 56..60}";
    assertThat(s.subtract(s3)).hasToString(expecting);
  }

  /**
   * The following was broken:
   * {0..113, 115..65534}-{0..115, 117..65534}=116..65534
   */
  @Test
  void testSubtractOfWackyRange() {
    IntervalSet s = IntervalSet.of(0, 113);
    s.add(115, 200);
    IntervalSet s2 = IntervalSet.of(0, 115);
    s2.add(117, 200);
    String expecting = "116";
    assertThat(s.subtract(s2)).hasToString(expecting);
  }

  @Test
  void testSimpleEquals() {
    IntervalSet s = IntervalSet.of(10, 20);
    IntervalSet s2 = IntervalSet.of(10, 20);
    assertThat(s).isEqualTo(s2);

    IntervalSet s3 = IntervalSet.of(15, 55);
    assertThat(s).isNotEqualTo(s3);
  }

  @Test
  void testEquals() {
    IntervalSet s = IntervalSet.of(10, 20);
    s.add(2);
    s.add(499, 501);
    IntervalSet s2 = IntervalSet.of(10, 20);
    s2.add(2);
    s2.add(499, 501);
    assertThat(s).isEqualTo(s2);

    IntervalSet s3 = IntervalSet.of(10, 20);
    s3.add(2);
    assertThat(s).isNotEqualTo(s3);
  }

  @Test
  void testSingleElementMinusDisjointSet() {
    IntervalSet s = IntervalSet.of(15, 15);
    IntervalSet s2 = IntervalSet.of(1, 5);
    s2.add(10, 20);
    String expecting = "{}"; // 15 - {1..5, 10..20} = {}
    assertThat(s.subtract(s2)).hasToString(expecting);
  }

  @Test
  void testMembership() {
    IntervalSet s = IntervalSet.of(15, 15);
    s.add(50, 60);
    assertThat(!s.contains(0)).isTrue();
    assertThat(!s.contains(20)).isTrue();
    assertThat(!s.contains(100)).isTrue();
    assertThat(s.contains(15)).isTrue();
    assertThat(s.contains(55)).isTrue();
    assertThat(s.contains(50)).isTrue();
    assertThat(s.contains(60)).isTrue();
  }

  // {2,15,18} & 10..20
  @Test
  void testIntersectionWithTwoContainedElements() {
    IntervalSet s = IntervalSet.of(10, 20);
    IntervalSet s2 = IntervalSet.of(2, 2);
    s2.add(15);
    s2.add(18);
    String expecting = "{15, 18}";
    assertThat(s.and(s2)).hasToString(expecting);
  }

  @Test
  void testIntersectionWithTwoContainedElementsReversed() {
    IntervalSet s = IntervalSet.of(10, 20);
    IntervalSet s2 = IntervalSet.of(2, 2);
    s2.add(15);
    s2.add(18);
    String expecting = "{15, 18}";
    assertThat(s2.and(s)).hasToString(expecting);
  }

  @Test
  void testComplement() {
    IntervalSet s = IntervalSet.of(100, 100);
    s.add(101, 101);
    IntervalSet s2 = IntervalSet.of(100, 102);
    String expecting = "102";
    assertThat(s.complement(s2)).hasToString(expecting);
  }

  @Test
  void testComplement2() {
    IntervalSet s = IntervalSet.of(100, 101);
    IntervalSet s2 = IntervalSet.of(100, 102);
    String expecting = "102";
    assertThat(s.complement(s2)).hasToString(expecting);
  }

  @Test
  void testComplement3() {
    IntervalSet s = IntervalSet.of(1, 96);
    s.add(99, Lexer.MAX_CHAR_VALUE);
    String expecting = "{97..98}";
    assertThat(s.complement(1, Lexer.MAX_CHAR_VALUE)).hasToString(expecting);
  }

  @Test
  void testMergeOfRangesAndSingleValues() {
    // {0..41, 42, 43..65534}
    IntervalSet s = IntervalSet.of(0, 41);
    s.add(42);
    s.add(43, 65534);
    String expecting = "{0..65534}";
    assertThat(s).hasToString(expecting);
  }

  @Test
  void testMergeOfRangesAndSingleValuesReverse() {
    IntervalSet s = IntervalSet.of(43, 65534);
    s.add(42);
    s.add(0, 41);
    String expecting = "{0..65534}";
    assertThat(s).hasToString(expecting);
  }

  @Test
  void testMergeWhereAdditionMergesTwoExistingIntervals() {
    // 42, 10, {0..9, 11..41, 43..65534}
    IntervalSet s = IntervalSet.of(42);
    s.add(10);
    s.add(0, 9);
    s.add(43, 65534);
    s.add(11, 41);
    String expecting = "{0..65534}";
    assertThat(s).hasToString(expecting);
  }

  /**
   * This case is responsible for antlr/antlr4#153.
   * <a href="https://github.com/antlr/antlr4/issues/153">...</a>
   */
  @Test
  void testMergeWhereAdditionMergesThreeExistingIntervals() {
    IntervalSet s = new IntervalSet();
    s.add(0);
    s.add(3);
    s.add(5);
    s.add(0, 7);
    String expecting = "{0..7}";
    assertThat(s).hasToString(expecting);
  }

  @Test
  void testMergeWithDoubleOverlap() {
    IntervalSet s = IntervalSet.of(1, 10);
    s.add(20, 30);
    s.add(5, 25); // overlaps two!
    String expecting = "{1..30}";
    assertThat(s).hasToString(expecting);
  }

  @Test
  void testSize() {
    IntervalSet s = IntervalSet.of(20, 30);
    s.add(50, 55);
    s.add(5, 19);
    assertThat(s.size()).isEqualTo(32);
  }

  @Test
  void testToList() {
    IntervalSet s = IntervalSet.of(20, 25);
    s.add(50, 55);
    s.add(5, 5);
    String expecting = "[5, 20, 21, 22, 23, 24, 25, 50, 51, 52, 53, 54, 55]";
    assertThat(s.toList().toString()).isEqualTo(expecting);
  }

  /**
   * The following was broken:
   * {'\u0000'..'s', 'u'..'\uFFFE'} & {'\u0000'..'q', 's'..'\uFFFE'}=
   * {'\u0000'..'q', 's'}!!!! broken...
   * 'q' is 113 ascii
   * 'u' is 117
   */
  @Test
  void testNotRIntersectionNotT() {
    IntervalSet s = IntervalSet.of(0, 's');
    s.add('u', 200);
    IntervalSet s2 = IntervalSet.of(0, 'q');
    s2.add('s', 200);
    String expecting = "{0..113, 115, 117..200}";
    String result = (s.and(s2)).toString();
    assertThat(result).isEqualTo(expecting);
  }

  @Test
  void testRmSingleElement() {
    IntervalSet s = IntervalSet.of(1, 10);
    s.add(-3, -3);
    s.remove(-3);
    String expecting = "{1..10}";
    String result = s.toString();
    assertThat(result).isEqualTo(expecting);
  }

  @Test
  void testRmLeftSide() {
    IntervalSet s = IntervalSet.of(1, 10);
    s.add(-3, -3);
    s.remove(1);
    String expecting = "{-3, 2..10}";
    String result = s.toString();
    assertThat(result).isEqualTo(expecting);
  }

  @Test
  void testRmRightSide() {
    IntervalSet s = IntervalSet.of(1, 10);
    s.add(-3, -3);
    s.remove(10);
    String expecting = "{-3, 1..9}";
    String result = s.toString();
    assertThat(result).isEqualTo(expecting);
  }

  @Test
  void testRmMiddleRange() {
    IntervalSet s = IntervalSet.of(1, 10);
    s.add(-3, -3);
    s.remove(5);
    String expecting = "{-3, 1..4, 6..10}";
    String result = s.toString();
    assertThat(result).isEqualTo(expecting);
  }


}
