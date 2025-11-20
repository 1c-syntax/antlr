/**
 * This file is a part of ANTLR.
 * <p>
 * Copyright (c) 2012-2025 The ANTLR Project. All rights reserved.
 * Copyright (c) 2025 Valery Maximov <maximovvalery@gmail.com> and contributors
 * <p>
 * Use of this file is governed by the BSD-3-Clause license that
 * can be found in the LICENSE.txt file in the project root.
 */
package org.antlr.v4.runtime;

import org.antlr.v4.runtime.misc.NotNull;

/**
 * Classes that represents a change to a single token
 * For change type ADDED, newToken is required.
 * For change type REMOVED, oldToken is required.
 * For change type CHANGED, oldToken and newToken are required.
 * Token changes may *not* overlap. You also need to account for hidden tokens
 * (but not *skipped* ones).
 */
public record TokenChange(
  @NotNull TokenChangeType changeType,
  @NotNull CommonToken oldToken,
  @NotNull CommonToken newToken
) {

  /**
   * Создает билдер для TokenChange
   *
   * @return билдер
   */
  public static TokenChangeBuilder builder() {
    return new TokenChangeBuilder();
  }

  /**
   * Simple builder class for TokenChange
   */
  public static class TokenChangeBuilder {
    private TokenChangeType changeType;
    private CommonToken oldToken;
    private CommonToken newToken;

    private TokenChangeBuilder() {
    }

    public TokenChangeBuilder setChangeType(@NotNull TokenChangeType changeType) {
      this.changeType = changeType;
      return this;
    }

    public TokenChangeBuilder setOldToken(@NotNull CommonToken oldToken) {
      this.oldToken = oldToken;
      return this;
    }

    public TokenChangeBuilder setNewToken(@NotNull CommonToken newToken) {
      this.newToken = newToken;
      return this;
    }

    public TokenChange build() {
      return new TokenChange(changeType, oldToken, newToken);
    }
  }
}
