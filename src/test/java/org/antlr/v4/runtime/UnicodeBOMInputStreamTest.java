/*
 * This file is a part of ANTLR.
 *
 * Copyright (c) 2012-2025 The ANTLR Project. All rights reserved.
 * Copyright (c) 2025-2026 Valery Maximov <maximovvalery@gmail.com> and contributors
 *
 * Use of this file is governed by the BSD-3-Clause license that
 * can be found in the LICENSE.txt file in the project root.
 */
package org.antlr.v4.runtime;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Тесты для UnicodeBOMInputStream
 */
public class UnicodeBOMInputStreamTest {

  @Test
  public void testDetectUTF8BOM() throws IOException {
    byte[] content = {(byte) 0xEF, (byte) 0xBB, (byte) 0xBF, 't', 'e', 's', 't'};
    try (UnicodeBOMInputStream ubis = new UnicodeBOMInputStream(new ByteArrayInputStream(content))) {
      assertThat(ubis.getBOM()).isEqualTo(UnicodeBOMInputStream.BOM.UTF_8);
      assertThat(ubis.getBOM().toString()).isEqualTo("UTF-8");
    }
  }

  @Test
  public void testDetectUTF16LEBOM() throws IOException {
    byte[] content = {(byte) 0xFF, (byte) 0xFE, 't', 'e', 's', 't'};
    try (UnicodeBOMInputStream ubis = new UnicodeBOMInputStream(new ByteArrayInputStream(content))) {
      assertThat(ubis.getBOM()).isEqualTo(UnicodeBOMInputStream.BOM.UTF_16_LE);
      assertThat(ubis.getBOM().toString()).isEqualTo("UTF-16 little-endian");
    }
  }

  @Test
  public void testDetectUTF16BEBOM() throws IOException {
    byte[] content = {(byte) 0xFE, (byte) 0xFF, 't', 'e', 's', 't'};
    try (UnicodeBOMInputStream ubis = new UnicodeBOMInputStream(new ByteArrayInputStream(content))) {
      assertThat(ubis.getBOM()).isEqualTo(UnicodeBOMInputStream.BOM.UTF_16_BE);
      assertThat(ubis.getBOM().toString()).isEqualTo("UTF-16 big-endian");
    }
  }

  @Test
  public void testDetectUTF32LEBOM() throws IOException {
    byte[] content = {(byte) 0xFF, (byte) 0xFE, (byte) 0x00, (byte) 0x00, 't', 'e', 's', 't'};
    try (UnicodeBOMInputStream ubis = new UnicodeBOMInputStream(new ByteArrayInputStream(content))) {
      assertThat(ubis.getBOM()).isEqualTo(UnicodeBOMInputStream.BOM.UTF_32_LE);
      assertThat(ubis.getBOM().toString()).isEqualTo("UTF-32 little-endian");
    }
  }

  @Test
  public void testDetectUTF32BEBOM() throws IOException {
    byte[] content = {(byte) 0x00, (byte) 0x00, (byte) 0xFE, (byte) 0xFF, 't', 'e', 's', 't'};
    try (UnicodeBOMInputStream ubis = new UnicodeBOMInputStream(new ByteArrayInputStream(content))) {
      assertThat(ubis.getBOM()).isEqualTo(UnicodeBOMInputStream.BOM.UTF_32_BE);
      assertThat(ubis.getBOM().toString()).isEqualTo("UTF-32 big-endian");
    }
  }

  @Test
  public void testNoBOM() throws IOException {
    byte[] content = "test".getBytes(StandardCharsets.UTF_8);
    try (UnicodeBOMInputStream ubis = new UnicodeBOMInputStream(new ByteArrayInputStream(content))) {
      assertThat(ubis.getBOM()).isEqualTo(UnicodeBOMInputStream.BOM.NONE);
      assertThat(ubis.getBOM().toString()).isEqualTo("NONE");
    }
  }

  @Test
  public void testSkipBOM() throws IOException {
    byte[] content = {(byte) 0xEF, (byte) 0xBB, (byte) 0xBF, 't', 'e', 's', 't'};
    try (UnicodeBOMInputStream ubis = new UnicodeBOMInputStream(new ByteArrayInputStream(content))) {
      ubis.skipBOM();

      byte[] buffer = new byte[4];
      int read = ubis.read(buffer);

      assertThat(read).isEqualTo(4);
      assertThat(new String(buffer, StandardCharsets.UTF_8)).isEqualTo("test");
    }
  }

  @Test
  public void testSkipBOMTwice() throws IOException {
    byte[] content = {(byte) 0xEF, (byte) 0xBB, (byte) 0xBF, 't', 'e', 's', 't'};
    try (UnicodeBOMInputStream ubis = new UnicodeBOMInputStream(new ByteArrayInputStream(content))) {
      ubis.skipBOM();
      ubis.skipBOM(); // Второй вызов не должен вызывать проблем

      byte[] buffer = new byte[4];
      int read = ubis.read(buffer);

      assertThat(read).isEqualTo(4);
      assertThat(new String(buffer, StandardCharsets.UTF_8)).isEqualTo("test");
    }
  }

  @Test
  public void testReadWithoutSkippingBOM() throws IOException {
    byte[] content = {(byte) 0xEF, (byte) 0xBB, (byte) 0xBF, 't', 'e', 's', 't'};
    try (UnicodeBOMInputStream ubis = new UnicodeBOMInputStream(new ByteArrayInputStream(content))) {
      byte[] buffer = new byte[7];
      int read = ubis.read(buffer);

      assertThat(read).isEqualTo(7);
      // BOM все еще присутствует в потоке
      assertThat(buffer[0]).isEqualTo((byte) 0xEF);
      assertThat(buffer[1]).isEqualTo((byte) 0xBB);
      assertThat(buffer[2]).isEqualTo((byte) 0xBF);
    }
  }

  @Test
  public void testReadSingleByte() throws IOException {
    byte[] content = {(byte) 0xEF, (byte) 0xBB, (byte) 0xBF, 't'};
    try (UnicodeBOMInputStream ubis = new UnicodeBOMInputStream(new ByteArrayInputStream(content))) {
      ubis.skipBOM();

      int firstByte = ubis.read();
      assertThat(firstByte).isEqualTo('t');
    }
  }

  @Test
  public void testReadWithOffsetAndLength() throws IOException {
    byte[] content = {(byte) 0xEF, (byte) 0xBB, (byte) 0xBF, 't', 'e', 's', 't'};
    try (UnicodeBOMInputStream ubis = new UnicodeBOMInputStream(new ByteArrayInputStream(content))) {
      ubis.skipBOM();

      byte[] buffer = new byte[10];
      int read = ubis.read(buffer, 2, 4);

      assertThat(read).isEqualTo(4);
      assertThat(new String(buffer, 2, 4, StandardCharsets.UTF_8)).isEqualTo("test");
    }
  }

  @Test
  public void testSkip() throws IOException {
    byte[] content = {(byte) 0xEF, (byte) 0xBB, (byte) 0xBF, 'a', 'b', 'c', 'd'};
    try (UnicodeBOMInputStream ubis = new UnicodeBOMInputStream(new ByteArrayInputStream(content))) {
      ubis.skipBOM();

      long skipped = ubis.skip(2);
      assertThat(skipped).isEqualTo(2);

      int nextByte = ubis.read();
      assertThat(nextByte).isEqualTo('c');
    }
  }

  @Test
  public void testAvailable() throws IOException {
    byte[] content = {(byte) 0xEF, (byte) 0xBB, (byte) 0xBF, 't', 'e', 's', 't'};
    try (UnicodeBOMInputStream ubis = new UnicodeBOMInputStream(new ByteArrayInputStream(content))) {
      assertThat(ubis.available()).isEqualTo(7);

      ubis.skipBOM();
      assertThat(ubis.available()).isEqualTo(4);
    }
  }

  @Test
  public void testMarkAndReset() throws IOException {
    byte[] content = {(byte) 0xEF, (byte) 0xBB, (byte) 0xBF, 't', 'e', 's', 't'};
    try (UnicodeBOMInputStream ubis = new UnicodeBOMInputStream(new ByteArrayInputStream(content))) {
      ubis.skipBOM();

      if (ubis.markSupported()) {
        ubis.mark(10);
        ubis.read();
        ubis.read();
        ubis.reset();

        int firstByte = ubis.read();
        assertThat(firstByte).isEqualTo('t');
      }
    }
  }

  @Test
  public void testNullInputStream() {
    assertThatThrownBy(() -> new UnicodeBOMInputStream(null))
      .isInstanceOf(NullPointerException.class);
  }

  @Test
  public void testEmptyStream() throws IOException {
    byte[] content = {};
    try (UnicodeBOMInputStream ubis = new UnicodeBOMInputStream(new ByteArrayInputStream(content))) {
      assertThat(ubis.getBOM()).isEqualTo(UnicodeBOMInputStream.BOM.NONE);
    }
  }

  @Test
  public void testBOMGetBytes() {
    byte[] utf8Bytes = UnicodeBOMInputStream.BOM.UTF_8.getBytes();
    assertThat(utf8Bytes).hasSize(3);
    assertThat(utf8Bytes[0]).isEqualTo((byte) 0xEF);
    assertThat(utf8Bytes[1]).isEqualTo((byte) 0xBB);
    assertThat(utf8Bytes[2]).isEqualTo((byte) 0xBF);

    byte[] noneBytes = UnicodeBOMInputStream.BOM.NONE.getBytes();
    assertThat(noneBytes).isEmpty();
  }

  @Test
  public void testBOMGetBytesDefensiveCopy() {
    byte[] bytes1 = UnicodeBOMInputStream.BOM.UTF_8.getBytes();
    byte[] bytes2 = UnicodeBOMInputStream.BOM.UTF_8.getBytes();

    // Должны быть разные массивы (defensive copy)
    assertThat(bytes1).isNotSameAs(bytes2);
    assertThat(bytes1).isEqualTo(bytes2);
  }
}
