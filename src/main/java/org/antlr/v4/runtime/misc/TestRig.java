/*
 * This file is a part of ANTLR.
 *
 * Copyright (c) 2012-2025 The ANTLR Project. All rights reserved.
 * Copyright (c) 2025 Valery Maximov <maximovvalery@gmail.com> and contributors
 *
 * Use of this file is governed by the BSD-3-Clause license that
 * can be found in the LICENSE.txt file in the project root.
 */
package org.antlr.v4.runtime.misc;

import java.lang.reflect.Method;

/**
 * A proxy for the real org.antlr.v4.gui.TestRig that we moved to tool
 * artifact from runtime.
 *
 * @since 4.5.1
 * @deprecated
 */
@Deprecated
public class TestRig {
  public static void main(String[] args) {
    try {
      Class<?> testRigClass = Class.forName("org.antlr.v4.gui.TestRig");
      System.err.println("Warning: TestRig moved to org.antlr.v4.gui.TestRig; calling automatically");
      try {
        Method mainMethod = testRigClass.getMethod("main", String[].class);
        mainMethod.invoke(null, (Object) args);
      } catch (Exception nsme) {
        System.err.println("Problems calling org.antlr.v4.gui.TestRig.main(args)");
      }
    } catch (ClassNotFoundException cnfe) {
      System.err.println("Use of TestRig now requires the use of the tool jar, antlr-4.X-complete.jar");
      System.err.println("Maven users need group ID org.antlr and artifact ID antlr4");
    }
  }
}
