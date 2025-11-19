/**
 * This file is a part of ANTLR.
 *
 * Copyright (c) 2012-2025 The ANTLR Project. All rights reserved.
 * Copyright (c) 2025 Valery Maximov <maximovvalery@gmail.com> and contributors
 *
 * Use of this file is governed by the BSD-3-Clause license that
 * can be found in the LICENSE.txt file in the project root.
 */
package org.antlr.v4.runtime;

/**
 * Classes that represents a change to a single token
 *
 * For change type ADDED, newToken is required.
 *
 * For change type REMOVED, oldToken is required.
 *
 * For change type CHANGED, oldToken and newToken are required.
 *
 * Token changes may *not* overlap. You also need to account for hidden tokens
 * (but not *skipped* ones).
 */
public class TokenChange {
  TokenChangeType changeType;
  CommonToken oldToken;
  CommonToken newToken;

  TokenChange(TokenChangeType changeType, CommonToken oldToken, CommonToken newToken) {
    this.changeType = changeType;
    this.oldToken = oldToken;
    this.newToken = newToken;
  }
}