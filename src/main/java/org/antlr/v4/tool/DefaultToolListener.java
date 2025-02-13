/*
 * This file is a part of ANTLR.
 *
 * Copyright (c) 2012-2025 The ANTLR Project. All rights reserved.
 * Copyright (c) 2025 Valery Maximov <maximovvalery@gmail.com> and contributors
 *
 * Use of this file is governed by the BSD-3-Clause license that
 * can be found in the LICENSE.txt file in the project root.
 */
package org.antlr.v4.tool;

import org.antlr.v4.Tool;
import org.stringtemplate.v4.ST;

/**
 *
 */
public class DefaultToolListener implements ANTLRToolListener {
  public Tool tool;

  public DefaultToolListener(Tool tool) {
    this.tool = tool;
  }

  @Override
  public void info(String msg) {
    if (tool.errMgr.formatWantsSingleLineMessage()) {
      msg = msg.replace('\n', ' ');
    }
    System.out.println(msg);
  }

  @Override
  public void error(ANTLRMessage msg) {
    ST msgST = tool.errMgr.getMessageTemplate(msg);
    String outputMsg = msgST.render();
    if (tool.errMgr.formatWantsSingleLineMessage()) {
      outputMsg = outputMsg.replace('\n', ' ');
    }
    System.err.println(outputMsg);
  }

  @Override
  public void warning(ANTLRMessage msg) {
    ST msgST = tool.errMgr.getMessageTemplate(msg);
    String outputMsg = msgST.render();
    if (tool.errMgr.formatWantsSingleLineMessage()) {
      outputMsg = outputMsg.replace('\n', ' ');
    }
    System.err.println(outputMsg);
  }
}
