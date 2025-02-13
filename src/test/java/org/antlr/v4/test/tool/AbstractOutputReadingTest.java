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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public abstract class AbstractOutputReadingTest {
  public static void append(BufferedReader in, StringBuilder buf) throws IOException {
    String line = in.readLine();
    while (line != null) {
      buf.append(line);
      // NOTE: This appends a newline at EOF
      // regardless of whether or not the
      // input actually ended with a
      // newline.
      //
      // We should revisit this and read a
      // block at a time rather than a line
      // at a time, and change all tests
      // which rely on this behavior to
      // remove the trailing newline at EOF.
      //
      // When we fix this, we can remove the
      // TestOutputReading class entirely.
      buf.append('\n');
      line = in.readLine();
    }
  }

  /**
   * Read in the UTF-8 bytes at {@code path}, convert all
   * platform-specific line terminators to NL, and append NL
   * if the file was non-empty and didn't already end with one.
   * <p>
   * {@see StreamVacuum#run()} for why this method exists.
   * <p>
   * Returns {@code null} if the file does not exist or the output
   * was empty.
   */
  public static String read(File file) throws IOException {
    // Mimic StreamVacuum.run()'s behavior of replacing all platform-specific
    // EOL sequences with NL.
    StringBuilder buf = new StringBuilder();
    try (BufferedReader in = new BufferedReader(
      new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8))) {

      append(in, buf);
    } catch (FileNotFoundException e) {
      return null;
    }
    if (!buf.isEmpty()) {
      return buf.toString();
    } else {
      return null;
    }
  }
}
