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

/**
 * Definition of a token change:
 * - ADDED = A new token that did not exist before
 * - CHANGED = A token that was in the stream before but changed in some way.
 * - REMOVED = A token that no longer exists in the stream.
 */
public enum TokenChangeType {
  ADDED, CHANGED, REMOVED,
}