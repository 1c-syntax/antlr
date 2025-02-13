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

import org.antlr.v4.runtime.misc.IntervalSet;
import org.antlr.v4.runtime.misc.MurmurHash;
import org.antlr.v4.runtime.misc.Utils;
import org.antlr.v4.unicode.UnicodeData;

/**
 * Utility class to parse escapes like:
 * \\n
 * \\uABCD
 * \\u{10ABCD}
 * \\p{Foo}
 * \\P{Bar}
 * \\p{Baz=Blech}
 * \\P{Baz=Blech}
 */
public abstract class EscapeSequenceParsing {
  public static class Result {
    public enum Type {
      INVALID,
      CODE_POINT,
      PROPERTY
    }

    public final Type type;
    public final int codePoint;
    public final IntervalSet propertyIntervalSet;
    public final int startOffset;
    public final int parseLength;

    public Result(Type type, int codePoint, IntervalSet propertyIntervalSet, int startOffset, int parseLength) {
      this.type = type;
      this.codePoint = codePoint;
      this.propertyIntervalSet = propertyIntervalSet;
      this.startOffset = startOffset;
      this.parseLength = parseLength;
    }

    @Override
    public String toString() {
      return String.format(
        "%s type=%s codePoint=%d propertyIntervalSet=%s parseLength=%d",
        super.toString(),
        type,
        codePoint,
        propertyIntervalSet,
        parseLength);
    }

    @Override
    public boolean equals(Object other) {
      if (!(other instanceof Result that)) {
        return false;
      }
      if (this == that) {
        return true;
      }
      return Utils.equals(this.type, that.type) &&
        Utils.equals(this.codePoint, that.codePoint) &&
        Utils.equals(this.propertyIntervalSet, that.propertyIntervalSet) &&
        Utils.equals(this.parseLength, that.parseLength);
    }

    @Override
    public int hashCode() {
      int hash = MurmurHash.initialize();
      hash = MurmurHash.update(hash, type);
      hash = MurmurHash.update(hash, codePoint);
      hash = MurmurHash.update(hash, propertyIntervalSet);
      hash = MurmurHash.update(hash, parseLength);
      return MurmurHash.finish(hash, 4);
    }
  }

  /**
   * Parses a single escape sequence starting at {@code startOff}.
   * <p>
   * Returns a type of INVALID if no valid escape sequence was found, a Result otherwise.
   */
  public static Result parseEscape(String s, int startOff) {
    int offset = startOff;
    if (offset + 2 > s.length() || s.codePointAt(offset) != '\\') {
      return invalid(startOff, s.length() - 1);
    }
    // Move past backslash
    offset++;
    int escaped = s.codePointAt(offset);
    // Move past escaped code point
    offset += Character.charCount(escaped);
    if (escaped == 'u') {
      // \\u{1} is the shortest we support
      if (offset + 3 > s.length()) {
        return invalid(startOff, s.length() - 1);
      }
      int hexStartOffset;
      int hexEndOffset; // appears to be exclusive
      if (s.codePointAt(offset) == '{') {
        hexStartOffset = offset + 1;
        hexEndOffset = s.indexOf('}', hexStartOffset);
        if (hexEndOffset == -1) {
          return invalid(startOff, s.length() - 1);
        }
        offset = hexEndOffset + 1;
      } else {
        if (offset + 4 > s.length()) {
          return invalid(startOff, s.length() - 1);
        }
        hexStartOffset = offset;
        hexEndOffset = offset + 4;
        offset = hexEndOffset;
      }
      int codePointValue = CharSupport.parseHexValue(s, hexStartOffset, hexEndOffset);
      if (codePointValue == -1 || codePointValue > Character.MAX_CODE_POINT) {
        return invalid(startOff, startOff + 6 - 1);
      }
      return new Result(
        Result.Type.CODE_POINT,
        codePointValue,
        IntervalSet.EMPTY_SET,
        startOff,
        offset - startOff);
    } else if (escaped == 'p' || escaped == 'P') {
      // \p{L} is the shortest we support
      if (offset + 3 > s.length()) {
        return invalid(startOff, s.length() - 1);
      }
      if (s.codePointAt(offset) != '{') {
        return invalid(startOff, offset);
      }
      int openBraceOffset = offset;
      int closeBraceOffset = s.indexOf('}', openBraceOffset);
      if (closeBraceOffset == -1) {
        return invalid(startOff, s.length() - 1);
      }
      String propertyName = s.substring(openBraceOffset + 1, closeBraceOffset);
      IntervalSet propertyIntervalSet = UnicodeData.getPropertyCodePoints(propertyName);
      if (propertyIntervalSet == null) {
        return invalid(startOff, closeBraceOffset);
      }
      offset = closeBraceOffset + 1;
      if (escaped == 'P') {
        propertyIntervalSet = propertyIntervalSet.complement(IntervalSet.COMPLETE_CHAR_SET);
      }
      return new Result(
        Result.Type.PROPERTY,
        -1,
        propertyIntervalSet,
        startOff,
        offset - startOff);
    } else if (escaped < CharSupport.ANTLRLiteralEscapedCharValue.length) {
      int codePoint = CharSupport.ANTLRLiteralEscapedCharValue[escaped];
      if (codePoint == 0) {
        if (escaped != ']' && escaped != '-') { // escape ']' and '-' only in char sets.
          return invalid(startOff, startOff + 1);
        } else {
          codePoint = escaped;
        }
      }
      return new Result(
        Result.Type.CODE_POINT,
        codePoint,
        IntervalSet.EMPTY_SET,
        startOff,
        offset - startOff);
    } else {
      return invalid(startOff, s.length() - 1);
    }
  }

  private static Result invalid(int start, int stop) { // start..stop is inclusive
    return new Result(
      Result.Type.INVALID,
      0,
      IntervalSet.EMPTY_SET,
      start,
      stop - start + 1);
  }
}
