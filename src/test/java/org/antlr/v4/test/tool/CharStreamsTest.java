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

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.misc.Utils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.charset.Charset;
import java.nio.charset.CodingErrorAction;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;

class CharStreamsTest {
  @TempDir
  private Path tempDir;
  private Path tempFile;

  @BeforeEach
  void newFile() throws IOException {
    tempFile = Files.createTempFile(tempDir, "tmp", "tmp");
  }

  @Test
  void fromBMPStringHasExpectedSize() {
    CharStream s = CharStreams.fromString("hello");
    assertThat(s.size()).isEqualTo(5);
    assertThat(s.index()).isZero();
    assertThat(s).hasToString("hello");
  }

  @Test
  void fromSMPStringHasExpectedSize() {
    CharStream s = CharStreams.fromString(
      "hello \uD83C\uDF0E");
    assertThat(s.size()).isEqualTo(7);
    assertThat(s.index()).isZero();
    assertThat(s).hasToString("hello \uD83C\uDF0E");
  }

  @Test
  void fromBMPUTF8PathHasExpectedSize() throws Exception {
    File p = tempFile.toFile();
    Utils.writeFile(p, "hello".getBytes(StandardCharsets.UTF_8));
    CharStream s = CharStreams.fromFile(p);
    assertThat(s.size()).isEqualTo(5);
    assertThat(s.index()).isZero();
    assertThat(s).hasToString("hello");
    assertThat(s.getSourceName()).isEqualTo(p.toString());
  }

  @Test
  void fromSMPUTF8PathHasExpectedSize() throws Exception {
    File p = tempFile.toFile();
    Utils.writeFile(p, "hello \uD83C\uDF0E".getBytes(StandardCharsets.UTF_8));
    CharStream s = CharStreams.fromFile(p);
    assertThat(s.size()).isEqualTo(7);
    assertThat(s.index()).isZero();
    assertThat(s).hasToString("hello \uD83C\uDF0E");
    assertThat(s.getSourceName()).isEqualTo(p.toString());
  }

  @Test
  void fromBMPUTF8InputStreamHasExpectedSize() throws Exception {
    File p = tempFile.toFile();
    Utils.writeFile(p, "hello".getBytes(StandardCharsets.UTF_8));
    try (InputStream is = new FileInputStream(p)) {
      CharStream s = CharStreams.fromStream(is);
      assertThat(s.size()).isEqualTo(5);
      assertThat(s.index()).isZero();
      assertThat(s).hasToString("hello");
    }
  }

  @Test
  void fromSMPUTF8InputStreamHasExpectedSize() throws Exception {
    File p = tempFile.toFile();
    Utils.writeFile(p, "hello \uD83C\uDF0E".getBytes(StandardCharsets.UTF_8));
    try (InputStream is = new FileInputStream(p)) {
      CharStream s = CharStreams.fromStream(is);
      assertThat(s.size()).isEqualTo(7);
      assertThat(s.index()).isZero();
      assertThat(s).hasToString("hello \uD83C\uDF0E");
    }
  }

  @Test
  void fromBMPUTF8ChannelHasExpectedSize() throws Exception {
    File p = tempFile.toFile();
    Utils.writeFile(p, "hello".getBytes(StandardCharsets.UTF_8));
    try (ReadableByteChannel c = Channels.newChannel(new FileInputStream(p))) {
      CharStream s = CharStreams.fromChannel(
        c, 4096, CodingErrorAction.REPLACE, "foo");
      assertThat(s.size()).isEqualTo(5);
      assertThat(s.index()).isZero();
      assertThat(s).hasToString("hello");
      assertThat(s.getSourceName()).isEqualTo("foo");
    }
  }

  @Test
  void fromSMPUTF8ChannelHasExpectedSize() throws Exception {
    File p = tempFile.toFile();
    Utils.writeFile(p, "hello \uD83C\uDF0E".getBytes(StandardCharsets.UTF_8));
    try (ReadableByteChannel c = Channels.newChannel(new FileInputStream(p))) {
      CharStream s = CharStreams.fromChannel(
        c, 4096, CodingErrorAction.REPLACE, "foo");
      assertThat(s.size()).isEqualTo(7);
      assertThat(s.index()).isZero();
      assertThat(s).hasToString("hello \uD83C\uDF0E");
      assertThat(s.getSourceName()).isEqualTo("foo");
    }
  }

  @Test
  void fromInvalidUTF8BytesChannelReplacesWithSubstCharInReplaceMode()
    throws Exception {
    File p = tempFile.toFile();
    byte[] toWrite = new byte[]{(byte) 0xCA, (byte) 0xFE, (byte) 0xFE, (byte) 0xED};
    Utils.writeFile(p, toWrite);
    try (ReadableByteChannel c = Channels.newChannel(new FileInputStream(p))) {
      CharStream s = CharStreams.fromChannel(
        c, 4096, CodingErrorAction.REPLACE, "foo");
      assertThat(s.size()).isEqualTo(4);
      assertThat(s.index()).isZero();
      assertThat(s).hasToString("\uFFFD\uFFFD\uFFFD\uFFFD");
    }
  }

  @Test
  void fromSMPUTF8SequenceStraddlingBufferBoundary() throws Exception {
    File p = tempFile.toFile();
    Utils.writeFile(p, "hello \uD83C\uDF0E".getBytes(StandardCharsets.UTF_8));
    try (ReadableByteChannel c = Channels.newChannel(new FileInputStream(p))) {
      CharStream s = CharStreams.fromChannel(
        c,
        // Note this buffer size ensures the SMP code point
        // straddles the boundary of two buffers
        8,
        CodingErrorAction.REPLACE,
        "foo");
      assertThat(s.size()).isEqualTo(7);
      assertThat(s.index()).isZero();
      assertThat(s).hasToString("hello \uD83C\uDF0E");
    }
  }

  @Test
  void fromFileName() throws Exception {
    File p = tempFile.toFile();
    Utils.writeFile(p, "hello \uD83C\uDF0E".getBytes(StandardCharsets.UTF_8));
    CharStream s = CharStreams.fromFileName(p.toString());
    assertThat(s.size()).isEqualTo(7);
    assertThat(s.index()).isZero();
    assertThat(s).hasToString("hello \uD83C\uDF0E");
    assertThat(s.getSourceName()).isEqualTo(p.toString());

  }

  @Test
  void fromFileNameWithLatin1() throws Exception {
    File p = tempFile.toFile();
    Utils.writeFile(p, "hello Êþ".getBytes(StandardCharsets.ISO_8859_1));
    CharStream s = CharStreams.fromFileName(p.toString(), StandardCharsets.ISO_8859_1);
    assertThat(s.size()).isEqualTo(8);
    assertThat(s.index()).isZero();
    assertThat(s).hasToString("hello Êþ");
    assertThat(s.getSourceName()).isEqualTo(p.toString());

  }

  @Test
  void fromReader() throws Exception {
    File p = tempFile.toFile();
    Utils.writeFile(p, "hello \uD83C\uDF0E".getBytes(StandardCharsets.UTF_8));
    try (Reader r = new InputStreamReader(new FileInputStream(p), StandardCharsets.UTF_8)) {
      CharStream s = CharStreams.fromReader(r);
      assertThat(s.size()).isEqualTo(7);
      assertThat(s.index()).isZero();
      assertThat(s).hasToString("hello \uD83C\uDF0E");
    }
  }

  @Test
  void fromSMPUTF16LEPathSMPHasExpectedSize() throws Exception {
    File p = tempFile.toFile();
    Utils.writeFile(p, "hello \uD83C\uDF0E".getBytes(StandardCharsets.UTF_16LE));
    CharStream s = CharStreams.fromFile(p, StandardCharsets.UTF_16LE);
    assertThat(s.size()).isEqualTo(7);
    assertThat(s.index()).isZero();
    assertThat(s).hasToString("hello \uD83C\uDF0E");
    assertThat(s.getSourceName()).isEqualTo(p.toString());
  }

  @Test
  void fromSMPUTF32LEPathSMPHasExpectedSize() throws Exception {
    File p = tempFile.toFile();
    Utils.writeFile(p, "hello \uD83C\uDF0E".getBytes(Charset.forName("UTF-32LE")));
    CharStream s = CharStreams.fromFile(p, Charset.forName("UTF-32LE"));
    assertThat(s.size()).isEqualTo(7);
    assertThat(s.index()).isZero();
    assertThat(s).hasToString("hello \uD83C\uDF0E");
    assertThat(s.getSourceName()).isEqualTo(p.toString());
  }
}
