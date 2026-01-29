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

import org.antlr.v4.runtime.atn.ATN;
import org.antlr.v4.runtime.atn.ATNType;
import org.antlr.v4.runtime.atn.LexerATNSimulator;
import org.antlr.v4.runtime.misc.NotNull;
import org.antlr.v4.runtime.misc.Nullable;

import java.util.Collection;

public class LexerInterpreter extends Lexer {
  protected final String grammarFileName;
  protected final ATN atn;

  protected final String[] ruleNames;
  protected final String[] channelNames;
  protected final String[] modeNames;
  @NotNull
  private final Vocabulary vocabulary;

  public LexerInterpreter(String grammarFileName,
                          @NotNull Vocabulary vocabulary,
                          Collection<String> ruleNames,
                          @Nullable Collection<String> channelNames,
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
    this.channelNames = channelNames != null ? channelNames.toArray(new String[0]) : null;
    this.modeNames = modeNames.toArray(new String[0]);
    this.vocabulary = vocabulary;
    this.setInterpreter(new LexerATNSimulator(this, atn));
  }

  @Override
  public ATN getATN() {
    return atn;
  }

  @Override
  public String getGrammarFileName() {
    return grammarFileName;
  }

  @Override
  public String[] getRuleNames() {
    return ruleNames;
  }

  @Override
  public String[] getChannelNames() {
    return channelNames;
  }

  @Override
  public String[] getModeNames() {
    return modeNames;
  }

  @Override
  public Vocabulary getVocabulary() {
    return vocabulary;
  }
}
