/*
 * This file is a part of ANTLR.
 *
 * Copyright (c) 2012-2025 The ANTLR Project. All rights reserved.
 * Copyright (c) 2025-2026 Valery Maximov <maximovvalery@gmail.com> and contributors
 *
 * Use of this file is governed by the BSD-3-Clause license that
 * can be found in the LICENSE.txt file in the project root.
 */
// Generated from TestIncrementalBasic.g4 by ANTLR 4.x
package org.antlr.v4.test.runtime.java.api;

import org.antlr.v4.runtime.FailedPredicateException;
import org.antlr.v4.runtime.IncrementalParser;
import org.antlr.v4.runtime.IncrementalParserData;
import org.antlr.v4.runtime.IncrementalParserRuleContext;
import org.antlr.v4.runtime.IncrementalTokenStream;
import org.antlr.v4.runtime.NoViableAltException;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.RuleVersion;
import org.antlr.v4.runtime.Vocabulary;
import org.antlr.v4.runtime.VocabularyImpl;
import org.antlr.v4.runtime.atn.ATN;
import org.antlr.v4.runtime.atn.ATNDeserializer;
import org.antlr.v4.runtime.atn.ParserATNSimulator;
import org.antlr.v4.runtime.misc.NotNull;
import org.antlr.v4.runtime.misc.Nullable;
import org.antlr.v4.runtime.tree.TerminalNode;

import java.util.List;

public class TestIncrementalBasicParser extends IncrementalParser {
  public static final int
    WS = 1, IDENT = 2, DIGITS = 3;
  public static final int
    RULE_program = 0, RULE_identifier = 1, RULE_digits = 2;

  private static String[] makeRuleNames() {
    return new String[]{
      "program", "identifier", "digits"
    };
  }

  public static final String[] ruleNames = makeRuleNames();

  private static String[] makeLiteralNames() {
    return new String[]{
    };
  }

  private static final String[] _LITERAL_NAMES = makeLiteralNames();

  private static String[] makeSymbolicNames() {
    return new String[]{
      null, "WS", "IDENT", "DIGITS"
    };
  }

  private static final String[] _SYMBOLIC_NAMES = makeSymbolicNames();
  public static final Vocabulary VOCABULARY = new VocabularyImpl(_LITERAL_NAMES, _SYMBOLIC_NAMES);

  @Override
  @NotNull
  public Vocabulary getVocabulary() {
    return VOCABULARY;
  }

  @Override
  public String getGrammarFileName() {
    return "TestIncrementalBasic.g4";
  }

  @Override
  public String[] getRuleNames() {
    return ruleNames;
  }

  @Override
  public String getSerializedATN() {
    return _serializedATN;
  }

  @NotNull
  private FailedPredicateException createFailedPredicateException() {
    return createFailedPredicateException(null);
  }

  @NotNull
  private FailedPredicateException createFailedPredicateException(@Nullable String predicate) {
    return createFailedPredicateException(predicate, null);
  }

  @NotNull
  protected FailedPredicateException createFailedPredicateException(@Nullable String predicate, @Nullable String message) {
    return new FailedPredicateException(this, predicate, message);
  }

  public TestIncrementalBasicParser(IncrementalTokenStream input) {
    this(input, null);
  }

  public TestIncrementalBasicParser(IncrementalTokenStream input, IncrementalParserData data) {
    super(input, data);
    setInterpreter(new ParserATNSimulator(this, _ATN));
  }


  public static class ProgramContext extends IncrementalParserRuleContext {
    public List<? extends IdentifierContext> identifier() {
      return getRuleContexts(IdentifierContext.class);
    }

    public IdentifierContext identifier(int i) {
      return getRuleContext(IdentifierContext.class, i);
    }

    public List<? extends DigitsContext> digits() {
      return getRuleContexts(DigitsContext.class);
    }

    public DigitsContext digits(int i) {
      return getRuleContext(DigitsContext.class, i);
    }

    public ProgramContext(ParserRuleContext parent, int invokingState) {
      super((IncrementalParserRuleContext) parent, invokingState);
    }

    @Override
    public int getRuleIndex() {
      return RULE_program;
    }
  }

  @RuleVersion(0)
  public final ProgramContext program() throws RecognitionException {
    // Check whether we need to execute this rule.
    ProgramContext guardResult = (ProgramContext) guardRule((IncrementalParserRuleContext) _ctx, getState(), RULE_program);
    // If we found an existing context that is valid, return it.
    if (guardResult != null) {
      this._input.seek(guardResult.stop.getTokenIndex() + 1);
      return guardResult;
    }
    ProgramContext _localctx = new ProgramContext(_ctx, getState());
    enterRule(_localctx, 0, RULE_program);
    int _la;
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(8);
        _errHandler.sync(this);
        do {
          {
            setState(8);
            _errHandler.sync(this);
            switch (_input.LA(1)) {
              case IDENT: {
                setState(6);
                identifier();
              }
              break;
              case DIGITS: {
                setState(7);
                digits();
              }
              break;
              default:
                throw new NoViableAltException(this);
            }
          }
          setState(10);
          _errHandler.sync(this);
          _la = _input.LA(1);
        } while (_la == IDENT || _la == DIGITS);
      }
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
  }

  public static class IdentifierContext extends IncrementalParserRuleContext {
    public TerminalNode IDENT() {
      return getToken(TestIncrementalBasicParser.IDENT, 0);
    }

    public IdentifierContext(ParserRuleContext parent, int invokingState) {
      super((IncrementalParserRuleContext) parent, invokingState);
    }

    @Override
    public int getRuleIndex() {
      return RULE_identifier;
    }
  }

  @RuleVersion(0)
  public final IdentifierContext identifier() throws RecognitionException {
    // Check whether we need to execute this rule.
    IdentifierContext guardResult = (IdentifierContext) guardRule((IncrementalParserRuleContext) _ctx, getState(), RULE_identifier);
    // If we found an existing context that is valid, return it.
    if (guardResult != null) {
      this._input.seek(guardResult.stop.getTokenIndex() + 1);
      return guardResult;
    }
    IdentifierContext _localctx = new IdentifierContext(_ctx, getState());
    enterRule(_localctx, 2, RULE_identifier);
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(12);
        match(IDENT);
      }
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
  }

  public static class DigitsContext extends IncrementalParserRuleContext {
    public TerminalNode DIGITS() {
      return getToken(TestIncrementalBasicParser.DIGITS, 0);
    }

    public DigitsContext(ParserRuleContext parent, int invokingState) {
      super((IncrementalParserRuleContext) parent, invokingState);
    }

    @Override
    public int getRuleIndex() {
      return RULE_digits;
    }
  }

  @RuleVersion(0)
  public final DigitsContext digits() throws RecognitionException {
    // Check whether we need to execute this rule.
    DigitsContext guardResult = (DigitsContext) guardRule((IncrementalParserRuleContext) _ctx, getState(), RULE_digits);
    // If we found an existing context that is valid, return it.
    if (guardResult != null) {
      this._input.seek(guardResult.stop.getTokenIndex() + 1);
      return guardResult;
    }
    DigitsContext _localctx = new DigitsContext(_ctx, getState());
    enterRule(_localctx, 4, RULE_digits);
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(14);
        match(DIGITS);
      }
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
  }

  public static final String _serializedATN =
    "\3\uc91d\ucaba\u058d\uafba\u4f53\u0607\uea8b\uc241\3\5\23\4\2\t\2\4\3" +
      "\t\3\4\4\t\4\3\2\3\2\6\2\13\n\2\r\2\16\2\f\3\3\3\3\3\4\3\4\3\4\2\2\2\5" +
      "\2\2\4\2\6\2\2\2\2\21\2\n\3\2\2\2\4\16\3\2\2\2\6\20\3\2\2\2\b\13\5\4\3" +
      "\2\t\13\5\6\4\2\n\b\3\2\2\2\n\t\3\2\2\2\13\f\3\2\2\2\f\n\3\2\2\2\f\r\3" +
      "\2\2\2\r\3\3\2\2\2\16\17\7\4\2\2\17\5\3\2\2\2\20\21\7\5\2\2\21\7\3\2\2" +
      "\2\4\n\f";
  public static final ATN _ATN =
    new ATNDeserializer().deserialize(_serializedATN.toCharArray());

  static {
  }
}