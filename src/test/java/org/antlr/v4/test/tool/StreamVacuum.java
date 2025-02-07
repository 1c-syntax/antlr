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
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public final class StreamVacuum implements Runnable {
  private final StringBuilder buf = new StringBuilder();
  private final BufferedReader in;
  private Thread sucker;

  public StreamVacuum(InputStream in) {
    this.in = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));
  }

  public void start() {
    sucker = new Thread(this);
    sucker.start();
  }

  @Override
  public void run() {
    try {
      AbstractOutputReadingTest.append(in, buf);
    } catch (IOException ioe) {
      System.err.println("can't read output from process");
    }
  }

  /**
   * wait for the thread to finish
   */
  public void join() throws InterruptedException {
    sucker.join();
  }

  @Override
  public String toString() {
    return buf.toString();
  }
}
