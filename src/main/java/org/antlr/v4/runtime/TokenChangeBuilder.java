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
 * Simple builder class for TokenChange
 */
public class TokenChangeBuilder {
  private TokenChangeType changeType;
  private CommonToken oldToken;
  private CommonToken newToken;

  public TokenChangeBuilder setChangeType(TokenChangeType changeType) {
    this.changeType = changeType;
    return this;
  }

  public TokenChangeBuilder setOldToken(CommonToken oldToken) {
    this.oldToken = oldToken;
    return this;
  }

  public TokenChangeBuilder setNewToken(CommonToken newToken) {
    this.newToken = newToken;
    return this;
  }

  public TokenChange createTokenChange() {
    return new TokenChange(changeType, oldToken, newToken);
  }
}