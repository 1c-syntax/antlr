/*
 * This file is a part of ANTLR.
 *
 * Copyright (c) 2012-2025 The ANTLR Project. All rights reserved.
 * Copyright (c) 2025 Valery Maximov <maximovvalery@gmail.com> and contributors
 *
 * Use of this file is governed by the BSD-3-Clause license that
 * can be found in the LICENSE.txt file in the project root.
 */
package org.antlr.v4;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Вспомогательный класс с методами тестирования
 */
public class TestUtils {
  private TestUtils() {
    // static
  }

  /**
   * Обертка а-ля junit4 для упрощения запуска старых тестов
   *
   * @param expected - Эталон проверки
   * @param actual   - Проверяемое значение
   */
  public static <T> void assertEquals(T expected, T actual) {
    assertThat(actual).isEqualTo(expected);
  }

  /**
   * Обертка а-ля junit4 для упрощения запуска старых тестов
   *
   * @param message  - Сообщение об ошибке
   * @param expected - Эталон проверки
   * @param actual   - Проверяемое значение
   */
  public static <T> void assertEquals(String message, T expected, T actual) {
    assertThat(actual).as(message).isEqualTo(expected);
  }
}
