/*
 * This file is a part of ANTLR.
 *
 * Copyright (c) 2012-2025 The ANTLR Project. All rights reserved.
 * Copyright (c) 2025 Valery Maximov <maximovvalery@gmail.com> and contributors
 *
 * Use of this file is governed by the BSD-3-Clause license that
 * can be found in the LICENSE.txt file in the project root.
 */
// Generated from org/antlr/v4/runtime/tree/xpath/XPathLexer.g4 by ANTLR 4.7.2
package org.antlr.v4.runtime.tree.xpath;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.RuleContext;
import org.antlr.v4.runtime.Vocabulary;
import org.antlr.v4.runtime.VocabularyImpl;
import org.antlr.v4.runtime.atn.ATN;
import org.antlr.v4.runtime.atn.ATNDeserializer;
import org.antlr.v4.runtime.atn.LexerATNSimulator;
import org.antlr.v4.runtime.misc.NotNull;

public class XPathLexer extends Lexer {
  public static final int
    TOKEN_REF = 1, RULE_REF = 2, ANYWHERE = 3, ROOT = 4, WILDCARD = 5, BANG = 6, ID = 7,
    STRING = 8;
  public static String[] channelNames = {
    "DEFAULT_TOKEN_CHANNEL", "HIDDEN"
  };

  public static String[] modeNames = {
    "DEFAULT_MODE"
  };

  private static String[] makeRuleNames() {
    return new String[]{
      "ANYWHERE", "ROOT", "WILDCARD", "BANG", "ID", "NameChar", "NameStartChar",
      "STRING"
    };
  }

  public static final String[] ruleNames = makeRuleNames();

  private static String[] makeLiteralNames() {
    return new String[]{
      null, null, null, "'//'", "'/'", "'*'", "'!'"
    };
  }

  private static final String[] _LITERAL_NAMES = makeLiteralNames();

  private static String[] makeSymbolicNames() {
    return new String[]{
      null, "TOKEN_REF", "RULE_REF", "ANYWHERE", "ROOT", "WILDCARD", "BANG",
      "ID", "STRING"
    };
  }

  private static final String[] _SYMBOLIC_NAMES = makeSymbolicNames();
  public static final Vocabulary VOCABULARY = new VocabularyImpl(_LITERAL_NAMES, _SYMBOLIC_NAMES);

  /**
   * @deprecated Use {@link #VOCABULARY} instead.
   */
  @Deprecated
  public static final String[] tokenNames;

  static {
    tokenNames = new String[_SYMBOLIC_NAMES.length];
    for (int i = 0; i < tokenNames.length; i++) {
      tokenNames[i] = VOCABULARY.getLiteralName(i);
      if (tokenNames[i] == null) {
        tokenNames[i] = VOCABULARY.getSymbolicName(i);
      }

      if (tokenNames[i] == null) {
        tokenNames[i] = "<INVALID>";
      }
    }
  }

  @Override
  @Deprecated
  public String[] getTokenNames() {
    return tokenNames;
  }

  @Override
  @NotNull
  public Vocabulary getVocabulary() {
    return VOCABULARY;
  }


  public XPathLexer(CharStream input) {
    super(input);
    _interp = new LexerATNSimulator(this, _ATN);
    validateInputStream(_ATN, input);
  }

  @Override
  public String getGrammarFileName() {
    return "XPathLexer.g4";
  }

  @Override
  public String[] getRuleNames() {
    return ruleNames;
  }

  @Override
  public String getSerializedATN() {
    return _serializedATN;
  }

  @Override
  @NotNull
  public String[] getChannelNames() {
    return channelNames;
  }

  @Override
  @NotNull
  public String[] getModeNames() {
    return modeNames;
  }

  @Override
  public void action(RuleContext _localctx, int ruleIndex, int actionIndex) {
    switch (ruleIndex) {
      case 4:
        ID_action(_localctx, actionIndex);
        break;
    }
  }

  private void ID_action(RuleContext _localctx, int actionIndex) {
    switch (actionIndex) {
      case 0:

        String text = getText();
        if (Character.isUpperCase(text.charAt(0))) setType(TOKEN_REF);
        else setType(RULE_REF);

        break;
    }
  }

  @Override
  public boolean sempred(RuleContext _localctx, int ruleIndex, int predIndex) {
    switch (ruleIndex) {
      case 5:
        return NameChar_sempred(_localctx, predIndex);

      case 6:
        return NameStartChar_sempred(_localctx, predIndex);
    }
    return true;
  }

  private boolean NameChar_sempred(RuleContext _localctx, int predIndex) {
    switch (predIndex) {
      case 0:
        return Character.isUnicodeIdentifierPart(_input.LA(-1));
    }
    return true;
  }

  private boolean NameStartChar_sempred(RuleContext _localctx, int predIndex) {
    switch (predIndex) {
      case 1:
        return Character.isUnicodeIdentifierStart(_input.LA(-1));
    }
    return true;
  }

  public static final String _serializedATN =
    "\3\uc91d\ucaba\u058d\uafba\u4f53\u0607\uea8b\uc241\2\n8\b\1\4\2\t\2\4" +
      "\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\3\2\3\2\3\2\3\3" +
      "\3\3\3\4\3\4\3\5\3\5\3\6\3\6\7\6\37\n\6\f\6\16\6\"\13\6\3\6\3\6\3\7\3" +
      "\7\3\7\5\7)\n\7\3\b\3\b\3\b\5\b.\n\b\3\t\3\t\7\t\62\n\t\f\t\16\t\65\13" +
      "\t\3\t\3\t\3\63\2\2\n\3\2\5\5\2\6\7\2\7\t\2\b\13\2\t\r\2\2\17\2\2\21\2" +
      "\n\3\2\5\6\2\62;C\\aac|\3\2\2\u0081\4\2C\\c|\29\2\3\3\2\2\2\2\5\3\2\2" +
      "\2\2\7\3\2\2\2\2\t\3\2\2\2\2\13\3\2\2\2\2\21\3\2\2\2\3\23\3\2\2\2\5\26" +
      "\3\2\2\2\7\30\3\2\2\2\t\32\3\2\2\2\13\34\3\2\2\2\r(\3\2\2\2\17-\3\2\2" +
      "\2\21/\3\2\2\2\23\24\7\61\2\2\24\25\7\61\2\2\25\4\3\2\2\2\26\27\7\61\2" +
      "\2\27\6\3\2\2\2\30\31\7,\2\2\31\b\3\2\2\2\32\33\7#\2\2\33\n\3\2\2\2\34" +
      " \5\17\b\2\35\37\5\r\7\2\36\35\3\2\2\2\37\"\3\2\2\2 \36\3\2\2\2 !\3\2" +
      "\2\2!#\3\2\2\2\" \3\2\2\2#$\b\6\2\2$\f\3\2\2\2%)\t\2\2\2&'\n\3\2\2'" +
      ")\6\7\2\2(%\3\2\2\2(&\3\2\2\2)\16\3\2\2\2*.\t\4\2\2+,\n\3\2\2,.\6\b\3" +
      "\2-*\3\2\2\2-+\3\2\2\2.\20\3\2\2\2/\63\7)\2\2\60\62\13\2\2\2\61\60\3\2" +
      "\2\2\62\65\3\2\2\2\63\64\3\2\2\2\63\61\3\2\2\2\64\66\3\2\2\2\65\63\3\2" +
      "\2\2\66\67\7)\2\2\67\22\3\2\2\2\7\2 (-\63\3\3\6\2";
  public static final ATN _ATN =
    new ATNDeserializer().deserialize(_serializedATN.toCharArray());

  static {
  }
}
