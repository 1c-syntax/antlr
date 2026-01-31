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

import lombok.Getter;
import org.antlr.v4.runtime.atn.ATN;
import org.antlr.v4.runtime.atn.ATNType;
import org.antlr.v4.runtime.atn.LexerATNSimulator;
import org.jspecify.annotations.NullMarked;

import java.util.Collection;

@NullMarked
public class LexerInterpreter extends Lexer {
  @Getter
  protected final String grammarFileName;
  protected final ATN atn;

  @Getter
  protected final String[] ruleNames;
  @Getter
  protected final String[] channelNames;
  @Getter
  protected final String[] modeNames;

  @Getter
  protected final Vocabulary vocabulary;

  public LexerInterpreter(String grammarFileName,
                          Vocabulary vocabulary,
                          Collection<String> ruleNames,
                          Collection<String> channelNames,
                          Collection<String> modeNames,
                          ATN atn,
                          CharStream input) {
    super(input);

    if (atn.grammarType != ATNType.LEXER) {
      throw new IllegalArgumentException("The ATN must be a lexer ATN.");
    }

    this.grammarFileName = grammarFileName;
    this.atn = atn;

    this.ruleNames = ruleNames.toArray(new String[0]);
    this.channelNames = channelNames.toArray(new String[0]);
    this.modeNames = modeNames.toArray(new String[0]);
    this.vocabulary = vocabulary;
    this.setInterpreter(new LexerATNSimulator(this, atn));
  }

  @Override
  public ATN getATN() {
    return atn;
  }

}
