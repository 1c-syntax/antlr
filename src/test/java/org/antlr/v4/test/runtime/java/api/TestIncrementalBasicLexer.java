/**
 * This file is a part of ANTLR.
 *
 * Copyright (c) 2012-2025 The ANTLR Project. All rights reserved.
 * Copyright (c) 2025 Valery Maximov <maximovvalery@gmail.com> and contributors
 *
 * Use of this file is governed by the BSD-3-Clause license that
 * can be found in the LICENSE.txt file in the project root.
 */
// Generated from TestIncrementalBasic.g4 by ANTLR 4.x
package org.antlr.v4.test.runtime.java.api;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.Vocabulary;
import org.antlr.v4.runtime.VocabularyImpl;
import org.antlr.v4.runtime.atn.ATN;
import org.antlr.v4.runtime.atn.ATNDeserializer;
import org.antlr.v4.runtime.atn.LexerATNSimulator;
import org.antlr.v4.runtime.misc.NotNull;

public class TestIncrementalBasicLexer extends Lexer {
	public static final int
		WS=1, IDENT=2, DIGITS=3;
	public static String[] channelNames = {
		"DEFAULT_TOKEN_CHANNEL", "HIDDEN"
	};

	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	private static String[] makeRuleNames() {
		return new String[] {
			"WS", "IDENT", "DIGITS"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, "WS", "IDENT", "DIGITS"
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


	public TestIncrementalBasicLexer(CharStream input) {
		super(input);
		_interp = new LexerATNSimulator(this,_ATN);
		validateInputStream(_ATN, input);
	}

	@Override
	public String getGrammarFileName() { return "TestIncrementalBasic.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	@NotNull
	public String[] getChannelNames() { return channelNames; }

	@Override
	@NotNull
	public String[] getModeNames() { return modeNames; }

	public static final String _serializedATN =
		"\3\uc91d\ucaba\u058d\uafba\u4f53\u0607\uea8b\uc241\2\5\32\b\1\4\2\t\2"+
		"\4\3\t\3\4\4\t\4\3\2\6\2\13\n\2\r\2\16\2\f\3\2\3\2\3\3\6\3\22\n\3\r\3"+
		"\16\3\23\3\4\6\4\27\n\4\r\4\16\4\30\2\2\2\5\3\2\3\5\2\4\7\2\5\3\2\5\5"+
		"\2\13\f\16\17\"\"\4\2C\\c|\3\2\62;\2\34\2\3\3\2\2\2\2\5\3\2\2\2\2\7\3"+
		"\2\2\2\3\n\3\2\2\2\5\21\3\2\2\2\7\26\3\2\2\2\t\13\t\2\2\2\n\t\3\2\2\2"+
		"\13\f\3\2\2\2\f\n\3\2\2\2\f\r\3\2\2\2\r\16\3\2\2\2\16\17\b\2\2\2\17\4"+
		"\3\2\2\2\20\22\t\3\2\2\21\20\3\2\2\2\22\23\3\2\2\2\23\21\3\2\2\2\23\24"+
		"\3\2\2\2\24\6\3\2\2\2\25\27\t\4\2\2\26\25\3\2\2\2\27\30\3\2\2\2\30\26"+
		"\3\2\2\2\30\31\3\2\2\2\31\b\3\2\2\2\6\2\f\23\30\3\2\3\2";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
	}
}