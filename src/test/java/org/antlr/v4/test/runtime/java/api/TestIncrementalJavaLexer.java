/*
 * This file is a part of ANTLR.
 *
 * Copyright (c) 2012-2025 The ANTLR Project. All rights reserved.
 * Copyright (c) 2025-2026 Valery Maximov <maximovvalery@gmail.com> and contributors
 *
 * Use of this file is governed by the BSD-3-Clause license that
 * can be found in the LICENSE.txt file in the project root.
 */
// Generated from TestIncrementalJava.g4 by ANTLR 4.x
package org.antlr.v4.test.runtime.java.api;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.RuleContext;
import org.antlr.v4.runtime.Vocabulary;
import org.antlr.v4.runtime.VocabularyImpl;
import org.antlr.v4.runtime.atn.ATN;
import org.antlr.v4.runtime.atn.ATNDeserializer;
import org.antlr.v4.runtime.atn.LexerATNSimulator;
import org.antlr.v4.runtime.misc.NotNull;

public class TestIncrementalJavaLexer extends Lexer {
	public static final int
		ABSTRACT=1, ASSERT=2, BOOLEAN=3, BREAK=4, BYTE=5, CASE=6, CATCH=7, CHAR=8, 
		CLASS=9, CONST=10, CONTINUE=11, DEFAULT=12, DO=13, DOUBLE=14, ELSE=15, 
		ENUM=16, EXTENDS=17, FINAL=18, FINALLY=19, FLOAT=20, FOR=21, IF=22, GOTO=23, 
		IMPLEMENTS=24, IMPORT=25, INSTANCEOF=26, INT=27, INTERFACE=28, LONG=29, 
		NATIVE=30, NEW=31, PACKAGE=32, PRIVATE=33, PROTECTED=34, PUBLIC=35, RETURN=36, 
		SHORT=37, STATIC=38, STRICTFP=39, SUPER=40, SWITCH=41, SYNCHRONIZED=42, 
		THIS=43, THROW=44, THROWS=45, TRANSIENT=46, TRY=47, VOID=48, VOLATILE=49, 
		WHILE=50, IntegerLiteral=51, FloatingPointLiteral=52, BooleanLiteral=53, 
		CharacterLiteral=54, StringLiteral=55, NullLiteral=56, LPAREN=57, RPAREN=58, 
		LBRACE=59, RBRACE=60, LBRACK=61, RBRACK=62, SEMI=63, COMMA=64, DOT=65, 
		ASSIGN=66, GT=67, LT=68, BANG=69, TILDE=70, QUESTION=71, COLON=72, EQUAL=73, 
		LE=74, GE=75, NOTEQUAL=76, AND=77, OR=78, INC=79, DEC=80, ADD=81, SUB=82, 
		MUL=83, DIV=84, BITAND=85, BITOR=86, CARET=87, MOD=88, ADD_ASSIGN=89, 
		SUB_ASSIGN=90, MUL_ASSIGN=91, DIV_ASSIGN=92, AND_ASSIGN=93, OR_ASSIGN=94, 
		XOR_ASSIGN=95, MOD_ASSIGN=96, LSHIFT_ASSIGN=97, RSHIFT_ASSIGN=98, URSHIFT_ASSIGN=99, 
		Identifier=100, AT=101, ELLIPSIS=102, WS=103, COMMENT=104, LINE_COMMENT=105;
	public static String[] channelNames = {
		"DEFAULT_TOKEN_CHANNEL", "HIDDEN"
	};

	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	private static String[] makeRuleNames() {
		return new String[] {
			"ABSTRACT", "ASSERT", "BOOLEAN", "BREAK", "BYTE", "CASE", "CATCH", "CHAR", 
			"CLASS", "CONST", "CONTINUE", "DEFAULT", "DO", "DOUBLE", "ELSE", "ENUM", 
			"EXTENDS", "FINAL", "FINALLY", "FLOAT", "FOR", "IF", "GOTO", "IMPLEMENTS", 
			"IMPORT", "INSTANCEOF", "INT", "INTERFACE", "LONG", "NATIVE", "NEW", 
			"PACKAGE", "PRIVATE", "PROTECTED", "PUBLIC", "RETURN", "SHORT", "STATIC", 
			"STRICTFP", "SUPER", "SWITCH", "SYNCHRONIZED", "THIS", "THROW", "THROWS", 
			"TRANSIENT", "TRY", "VOID", "VOLATILE", "WHILE", "IntegerLiteral", "DecimalIntegerLiteral", 
			"HexIntegerLiteral", "OctalIntegerLiteral", "BinaryIntegerLiteral", "IntegerTypeSuffix", 
			"DecimalNumeral", "Digits", "Digit", "NonZeroDigit", "DigitsAndUnderscores", 
			"DigitOrUnderscore", "Underscores", "HexNumeral", "HexDigits", "HexDigit", 
			"HexDigitsAndUnderscores", "HexDigitOrUnderscore", "OctalNumeral", "OctalDigits", 
			"OctalDigit", "OctalDigitsAndUnderscores", "OctalDigitOrUnderscore", 
			"BinaryNumeral", "BinaryDigits", "BinaryDigit", "BinaryDigitsAndUnderscores", 
			"BinaryDigitOrUnderscore", "FloatingPointLiteral", "DecimalFloatingPointLiteral", 
			"ExponentPart", "ExponentIndicator", "SignedInteger", "Sign", "FloatTypeSuffix", 
			"HexadecimalFloatingPointLiteral", "HexSignificand", "BinaryExponent", 
			"BinaryExponentIndicator", "BooleanLiteral", "CharacterLiteral", "SingleCharacter", 
			"StringLiteral", "StringCharacters", "StringCharacter", "EscapeSequence", 
			"OctalEscape", "ZeroToThree", "NullLiteral", "LPAREN", "RPAREN", "LBRACE", 
			"RBRACE", "LBRACK", "RBRACK", "SEMI", "COMMA", "DOT", "ASSIGN", "GT", 
			"LT", "BANG", "TILDE", "QUESTION", "COLON", "EQUAL", "LE", "GE", "NOTEQUAL", 
			"AND", "OR", "INC", "DEC", "ADD", "SUB", "MUL", "DIV", "BITAND", "BITOR", 
			"CARET", "MOD", "ADD_ASSIGN", "SUB_ASSIGN", "MUL_ASSIGN", "DIV_ASSIGN", 
			"AND_ASSIGN", "OR_ASSIGN", "XOR_ASSIGN", "MOD_ASSIGN", "LSHIFT_ASSIGN", 
			"RSHIFT_ASSIGN", "URSHIFT_ASSIGN", "Identifier", "JavaLetter", "JavaLetterOrDigit", 
			"AT", "ELLIPSIS", "WS", "COMMENT", "LINE_COMMENT"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, "'abstract'", "'assert'", "'boolean'", "'break'", "'byte'", "'case'", 
			"'catch'", "'char'", "'class'", "'const'", "'continue'", "'default'", 
			"'do'", "'double'", "'else'", "'enum'", "'extends'", "'final'", "'finally'", 
			"'float'", "'for'", "'if'", "'goto'", "'implements'", "'import'", "'instanceof'", 
			"'int'", "'interface'", "'long'", "'native'", "'new'", "'package'", "'private'", 
			"'protected'", "'public'", "'return'", "'short'", "'static'", "'strictfp'", 
			"'super'", "'switch'", "'synchronized'", "'this'", "'throw'", "'throws'", 
			"'transient'", "'try'", "'void'", "'volatile'", "'while'", null, null, 
			null, null, null, "'null'", "'('", "')'", "'{'", "'}'", "'['", "']'", 
			"';'", "','", "'.'", "'='", "'>'", "'<'", "'!'", "'~'", "'?'", "':'", 
			"'=='", "'<='", "'>='", "'!='", "'&&'", "'||'", "'++'", "'--'", "'+'", 
			"'-'", "'*'", "'/'", "'&'", "'|'", "'^'", "'%'", "'+='", "'-='", "'*='", 
			"'/='", "'&='", "'|='", "'^='", "'%='", "'<<='", "'>>='", "'>>>='", null, 
			"'@'", "'...'"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, "ABSTRACT", "ASSERT", "BOOLEAN", "BREAK", "BYTE", "CASE", "CATCH", 
			"CHAR", "CLASS", "CONST", "CONTINUE", "DEFAULT", "DO", "DOUBLE", "ELSE", 
			"ENUM", "EXTENDS", "FINAL", "FINALLY", "FLOAT", "FOR", "IF", "GOTO", 
			"IMPLEMENTS", "IMPORT", "INSTANCEOF", "INT", "INTERFACE", "LONG", "NATIVE", 
			"NEW", "PACKAGE", "PRIVATE", "PROTECTED", "PUBLIC", "RETURN", "SHORT", 
			"STATIC", "STRICTFP", "SUPER", "SWITCH", "SYNCHRONIZED", "THIS", "THROW", 
			"THROWS", "TRANSIENT", "TRY", "VOID", "VOLATILE", "WHILE", "IntegerLiteral", 
			"FloatingPointLiteral", "BooleanLiteral", "CharacterLiteral", "StringLiteral", 
			"NullLiteral", "LPAREN", "RPAREN", "LBRACE", "RBRACE", "LBRACK", "RBRACK", 
			"SEMI", "COMMA", "DOT", "ASSIGN", "GT", "LT", "BANG", "TILDE", "QUESTION", 
			"COLON", "EQUAL", "LE", "GE", "NOTEQUAL", "AND", "OR", "INC", "DEC", 
			"ADD", "SUB", "MUL", "DIV", "BITAND", "BITOR", "CARET", "MOD", "ADD_ASSIGN", 
			"SUB_ASSIGN", "MUL_ASSIGN", "DIV_ASSIGN", "AND_ASSIGN", "OR_ASSIGN", 
			"XOR_ASSIGN", "MOD_ASSIGN", "LSHIFT_ASSIGN", "RSHIFT_ASSIGN", "URSHIFT_ASSIGN", 
			"Identifier", "AT", "ELLIPSIS", "WS", "COMMENT", "LINE_COMMENT"
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


	public TestIncrementalJavaLexer(CharStream input) {
		super(input);
		_interp = new LexerATNSimulator(this,_ATN);
		validateInputStream(_ATN, input);
	}

	@Override
	public String getGrammarFileName() { return "TestIncrementalJava.g4"; }

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

	@Override
	public boolean sempred(RuleContext _localctx, int ruleIndex, int predIndex) {
		switch (ruleIndex) {
		case 143:
			return JavaLetter_sempred(_localctx, predIndex);
		case 144:
			return JavaLetterOrDigit_sempred(_localctx, predIndex);
		}
		return true;
	}
	private boolean JavaLetter_sempred(RuleContext _localctx, int predIndex) {
		switch (predIndex) {
		case 0:
			return Character.isJavaIdentifierStart(_input.LA(-1));
		case 1:
			return Character.isJavaIdentifierStart(Character.toCodePoint((char)_input.LA(-2), (char)_input.LA(-1)));
		}
		return true;
	}
	private boolean JavaLetterOrDigit_sempred(RuleContext _localctx, int predIndex) {
		switch (predIndex) {
		case 2:
			return Character.isJavaIdentifierPart(_input.LA(-1));
		case 3:
			return Character.isJavaIdentifierPart(Character.toCodePoint((char)_input.LA(-2), (char)_input.LA(-1)));
		}
		return true;
	}

	public static final String _serializedATN =
		"\3\uc91d\ucaba\u058d\uafba\u4f53\u0607\uea8b\uc241\2k\u0434\b\1\4\2\t"+
		"\2\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13"+
		"\t\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22\t\22"+
		"\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27\t\27\4\30\t\30\4\31\t\31"+
		"\4\32\t\32\4\33\t\33\4\34\t\34\4\35\t\35\4\36\t\36\4\37\t\37\4 \t \4!"+
		"\t!\4\"\t\"\4#\t#\4$\t$\4%\t%\4&\t&\4\'\t\'\4(\t(\4)\t)\4*\t*\4+\t+\4"+
		",\t,\4-\t-\4.\t.\4/\t/\4\60\t\60\4\61\t\61\4\62\t\62\4\63\t\63\4\64\t"+
		"\64\4\65\t\65\4\66\t\66\4\67\t\67\48\t8\49\t9\4:\t:\4;\t;\4<\t<\4=\t="+
		"\4>\t>\4?\t?\4@\t@\4A\tA\4B\tB\4C\tC\4D\tD\4E\tE\4F\tF\4G\tG\4H\tH\4I"+
		"\tI\4J\tJ\4K\tK\4L\tL\4M\tM\4N\tN\4O\tO\4P\tP\4Q\tQ\4R\tR\4S\tS\4T\tT"+
		"\4U\tU\4V\tV\4W\tW\4X\tX\4Y\tY\4Z\tZ\4[\t[\4\\\t\\\4]\t]\4^\t^\4_\t_\4"+
		"`\t`\4a\ta\4b\tb\4c\tc\4d\td\4e\te\4f\tf\4g\tg\4h\th\4i\ti\4j\tj\4k\t"+
		"k\4l\tl\4m\tm\4n\tn\4o\to\4p\tp\4q\tq\4r\tr\4s\ts\4t\tt\4u\tu\4v\tv\4"+
		"w\tw\4x\tx\4y\ty\4z\tz\4{\t{\4|\t|\4}\t}\4~\t~\4\177\t\177\4\u0080\t\u0080"+
		"\4\u0081\t\u0081\4\u0082\t\u0082\4\u0083\t\u0083\4\u0084\t\u0084\4\u0085"+
		"\t\u0085\4\u0086\t\u0086\4\u0087\t\u0087\4\u0088\t\u0088\4\u0089\t\u0089"+
		"\4\u008a\t\u008a\4\u008b\t\u008b\4\u008c\t\u008c\4\u008d\t\u008d\4\u008e"+
		"\t\u008e\4\u008f\t\u008f\4\u0090\t\u0090\4\u0091\t\u0091\4\u0092\t\u0092"+
		"\4\u0093\t\u0093\4\u0094\t\u0094\4\u0095\t\u0095\4\u0096\t\u0096\4\u0097"+
		"\t\u0097\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\3\3\3\3\3\3\3\3\3\3\3\3"+
		"\3\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\5\3\5\3\5\3\5\3\5\3\5\3\6\3\6\3\6"+
		"\3\6\3\6\3\7\3\7\3\7\3\7\3\7\3\b\3\b\3\b\3\b\3\b\3\b\3\t\3\t\3\t\3\t\3"+
		"\t\3\n\3\n\3\n\3\n\3\n\3\n\3\13\3\13\3\13\3\13\3\13\3\13\3\f\3\f\3\f\3"+
		"\f\3\f\3\f\3\f\3\f\3\f\3\r\3\r\3\r\3\r\3\r\3\r\3\r\3\r\3\16\3\16\3\16"+
		"\3\17\3\17\3\17\3\17\3\17\3\17\3\17\3\20\3\20\3\20\3\20\3\20\3\21\3\21"+
		"\3\21\3\21\3\21\3\22\3\22\3\22\3\22\3\22\3\22\3\22\3\22\3\23\3\23\3\23"+
		"\3\23\3\23\3\23\3\24\3\24\3\24\3\24\3\24\3\24\3\24\3\24\3\25\3\25\3\25"+
		"\3\25\3\25\3\25\3\26\3\26\3\26\3\26\3\27\3\27\3\27\3\30\3\30\3\30\3\30"+
		"\3\30\3\31\3\31\3\31\3\31\3\31\3\31\3\31\3\31\3\31\3\31\3\31\3\32\3\32"+
		"\3\32\3\32\3\32\3\32\3\32\3\33\3\33\3\33\3\33\3\33\3\33\3\33\3\33\3\33"+
		"\3\33\3\33\3\34\3\34\3\34\3\34\3\35\3\35\3\35\3\35\3\35\3\35\3\35\3\35"+
		"\3\35\3\35\3\36\3\36\3\36\3\36\3\36\3\37\3\37\3\37\3\37\3\37\3\37\3\37"+
		"\3 \3 \3 \3 \3!\3!\3!\3!\3!\3!\3!\3!\3\"\3\"\3\"\3\"\3\"\3\"\3\"\3\"\3"+
		"#\3#\3#\3#\3#\3#\3#\3#\3#\3#\3$\3$\3$\3$\3$\3$\3$\3%\3%\3%\3%\3%\3%\3"+
		"%\3&\3&\3&\3&\3&\3&\3\'\3\'\3\'\3\'\3\'\3\'\3\'\3(\3(\3(\3(\3(\3(\3(\3"+
		"(\3(\3)\3)\3)\3)\3)\3)\3*\3*\3*\3*\3*\3*\3*\3+\3+\3+\3+\3+\3+\3+\3+\3"+
		"+\3+\3+\3+\3+\3,\3,\3,\3,\3,\3-\3-\3-\3-\3-\3-\3.\3.\3.\3.\3.\3.\3.\3"+
		"/\3/\3/\3/\3/\3/\3/\3/\3/\3/\3\60\3\60\3\60\3\60\3\61\3\61\3\61\3\61\3"+
		"\61\3\62\3\62\3\62\3\62\3\62\3\62\3\62\3\62\3\62\3\63\3\63\3\63\3\63\3"+
		"\63\3\63\3\64\3\64\3\64\3\64\5\64\u0287\n\64\3\65\3\65\5\65\u028b\n\65"+
		"\3\66\3\66\5\66\u028f\n\66\3\67\3\67\5\67\u0293\n\67\38\38\58\u0297\n"+
		"8\39\39\3:\3:\3:\5:\u029e\n:\3:\3:\3:\5:\u02a3\n:\5:\u02a5\n:\3;\3;\5"+
		";\u02a9\n;\3;\5;\u02ac\n;\3<\3<\5<\u02b0\n<\3=\3=\3>\6>\u02b5\n>\r>\16"+
		">\u02b6\3?\3?\5?\u02bb\n?\3@\6@\u02be\n@\r@\16@\u02bf\3A\3A\3A\3A\3B\3"+
		"B\5B\u02c8\nB\3B\5B\u02cb\nB\3C\3C\3D\6D\u02d0\nD\rD\16D\u02d1\3E\3E\5"+
		"E\u02d6\nE\3F\3F\5F\u02da\nF\3F\3F\3G\3G\5G\u02e0\nG\3G\5G\u02e3\nG\3"+
		"H\3H\3I\6I\u02e8\nI\rI\16I\u02e9\3J\3J\5J\u02ee\nJ\3K\3K\3K\3K\3L\3L\5"+
		"L\u02f6\nL\3L\5L\u02f9\nL\3M\3M\3N\6N\u02fe\nN\rN\16N\u02ff\3O\3O\5O\u0304"+
		"\nO\3P\3P\5P\u0308\nP\3Q\3Q\3Q\5Q\u030d\nQ\3Q\5Q\u0310\nQ\3Q\5Q\u0313"+
		"\nQ\3Q\3Q\3Q\5Q\u0318\nQ\3Q\5Q\u031b\nQ\3Q\3Q\3Q\5Q\u0320\nQ\3Q\3Q\3Q"+
		"\5Q\u0325\nQ\3R\3R\3R\3S\3S\3T\5T\u032d\nT\3T\3T\3U\3U\3V\3V\3W\3W\3W"+
		"\5W\u0338\nW\3X\3X\5X\u033c\nX\3X\3X\3X\5X\u0341\nX\3X\3X\5X\u0345\nX"+
		"\3Y\3Y\3Y\3Z\3Z\3[\3[\3[\3[\3[\3[\3[\3[\3[\5[\u0355\n[\3\\\3\\\3\\\3\\"+
		"\3\\\3\\\3\\\3\\\5\\\u035f\n\\\3]\3]\3^\3^\5^\u0365\n^\3^\3^\3_\6_\u036a"+
		"\n_\r_\16_\u036b\3`\3`\5`\u0370\n`\3a\3a\3a\5a\u0375\na\3b\3b\3b\3b\3"+
		"b\3b\3b\3b\3b\3b\3b\5b\u0382\nb\3c\3c\3d\3d\3d\3d\3d\3e\3e\3f\3f\3g\3"+
		"g\3h\3h\3i\3i\3j\3j\3k\3k\3l\3l\3m\3m\3n\3n\3o\3o\3p\3p\3q\3q\3r\3r\3"+
		"s\3s\3t\3t\3u\3u\3u\3v\3v\3v\3w\3w\3w\3x\3x\3x\3y\3y\3y\3z\3z\3z\3{\3"+
		"{\3{\3|\3|\3|\3}\3}\3~\3~\3\177\3\177\3\u0080\3\u0080\3\u0081\3\u0081"+
		"\3\u0082\3\u0082\3\u0083\3\u0083\3\u0084\3\u0084\3\u0085\3\u0085\3\u0085"+
		"\3\u0086\3\u0086\3\u0086\3\u0087\3\u0087\3\u0087\3\u0088\3\u0088\3\u0088"+
		"\3\u0089\3\u0089\3\u0089\3\u008a\3\u008a\3\u008a\3\u008b\3\u008b\3\u008b"+
		"\3\u008c\3\u008c\3\u008c\3\u008d\3\u008d\3\u008d\3\u008d\3\u008e\3\u008e"+
		"\3\u008e\3\u008e\3\u008f\3\u008f\3\u008f\3\u008f\3\u008f\3\u0090\3\u0090"+
		"\7\u0090\u03fa\n\u0090\f\u0090\16\u0090\u03fd\13\u0090\3\u0091\3\u0091"+
		"\3\u0091\3\u0091\3\u0091\3\u0091\5\u0091\u0405\n\u0091\3\u0092\3\u0092"+
		"\3\u0092\3\u0092\3\u0092\3\u0092\5\u0092\u040d\n\u0092\3\u0093\3\u0093"+
		"\3\u0094\3\u0094\3\u0094\3\u0094\3\u0095\6\u0095\u0416\n\u0095\r\u0095"+
		"\16\u0095\u0417\3\u0095\3\u0095\3\u0096\3\u0096\3\u0096\3\u0096\7\u0096"+
		"\u0420\n\u0096\f\u0096\16\u0096\u0423\13\u0096\3\u0096\3\u0096\3\u0096"+
		"\3\u0096\3\u0096\3\u0097\3\u0097\3\u0097\3\u0097\7\u0097\u042e\n\u0097"+
		"\f\u0097\16\u0097\u0431\13\u0097\3\u0097\3\u0097\3\u0421\2\2\u0098\3\2"+
		"\3\5\2\4\7\2\5\t\2\6\13\2\7\r\2\b\17\2\t\21\2\n\23\2\13\25\2\f\27\2\r"+
		"\31\2\16\33\2\17\35\2\20\37\2\21!\2\22#\2\23%\2\24\'\2\25)\2\26+\2\27"+
		"-\2\30/\2\31\61\2\32\63\2\33\65\2\34\67\2\359\2\36;\2\37=\2 ?\2!A\2\""+
		"C\2#E\2$G\2%I\2&K\2\'M\2(O\2)Q\2*S\2+U\2,W\2-Y\2.[\2/]\2\60_\2\61a\2\62"+
		"c\2\63e\2\64g\2\65i\2\2k\2\2m\2\2o\2\2q\2\2s\2\2u\2\2w\2\2y\2\2{\2\2}"+
		"\2\2\177\2\2\u0081\2\2\u0083\2\2\u0085\2\2\u0087\2\2\u0089\2\2\u008b\2"+
		"\2\u008d\2\2\u008f\2\2\u0091\2\2\u0093\2\2\u0095\2\2\u0097\2\2\u0099\2"+
		"\2\u009b\2\2\u009d\2\2\u009f\2\66\u00a1\2\2\u00a3\2\2\u00a5\2\2\u00a7"+
		"\2\2\u00a9\2\2\u00ab\2\2\u00ad\2\2\u00af\2\2\u00b1\2\2\u00b3\2\2\u00b5"+
		"\2\67\u00b7\28\u00b9\2\2\u00bb\29\u00bd\2\2\u00bf\2\2\u00c1\2\2\u00c3"+
		"\2\2\u00c5\2\2\u00c7\2:\u00c9\2;\u00cb\2<\u00cd\2=\u00cf\2>\u00d1\2?\u00d3"+
		"\2@\u00d5\2A\u00d7\2B\u00d9\2C\u00db\2D\u00dd\2E\u00df\2F\u00e1\2G\u00e3"+
		"\2H\u00e5\2I\u00e7\2J\u00e9\2K\u00eb\2L\u00ed\2M\u00ef\2N\u00f1\2O\u00f3"+
		"\2P\u00f5\2Q\u00f7\2R\u00f9\2S\u00fb\2T\u00fd\2U\u00ff\2V\u0101\2W\u0103"+
		"\2X\u0105\2Y\u0107\2Z\u0109\2[\u010b\2\\\u010d\2]\u010f\2^\u0111\2_\u0113"+
		"\2`\u0115\2a\u0117\2b\u0119\2c\u011b\2d\u011d\2e\u011f\2f\u0121\2\2\u0123"+
		"\2\2\u0125\2g\u0127\2h\u0129\2i\u012b\2j\u012d\2k\3\2\30\4\2NNnn\3\2\63"+
		";\4\2ZZzz\5\2\62;CHch\3\2\629\4\2DDdd\3\2\62\63\4\2GGgg\4\2--//\6\2FF"+
		"HHffhh\4\2RRrr\4\2))^^\4\2$$^^\n\2$$))^^ddhhppttvv\3\2\62\65\6\2&&C\\"+
		"aac|\4\2\2\u0101\ud802\udc01\3\2\ud802\udc01\3\2\udc02\ue001\7\2&&\62"+
		";C\\aac|\5\2\13\f\16\17\"\"\4\2\f\f\17\17\2\u0442\2\3\3\2\2\2\2\5\3\2"+
		"\2\2\2\7\3\2\2\2\2\t\3\2\2\2\2\13\3\2\2\2\2\r\3\2\2\2\2\17\3\2\2\2\2\21"+
		"\3\2\2\2\2\23\3\2\2\2\2\25\3\2\2\2\2\27\3\2\2\2\2\31\3\2\2\2\2\33\3\2"+
		"\2\2\2\35\3\2\2\2\2\37\3\2\2\2\2!\3\2\2\2\2#\3\2\2\2\2%\3\2\2\2\2\'\3"+
		"\2\2\2\2)\3\2\2\2\2+\3\2\2\2\2-\3\2\2\2\2/\3\2\2\2\2\61\3\2\2\2\2\63\3"+
		"\2\2\2\2\65\3\2\2\2\2\67\3\2\2\2\29\3\2\2\2\2;\3\2\2\2\2=\3\2\2\2\2?\3"+
		"\2\2\2\2A\3\2\2\2\2C\3\2\2\2\2E\3\2\2\2\2G\3\2\2\2\2I\3\2\2\2\2K\3\2\2"+
		"\2\2M\3\2\2\2\2O\3\2\2\2\2Q\3\2\2\2\2S\3\2\2\2\2U\3\2\2\2\2W\3\2\2\2\2"+
		"Y\3\2\2\2\2[\3\2\2\2\2]\3\2\2\2\2_\3\2\2\2\2a\3\2\2\2\2c\3\2\2\2\2e\3"+
		"\2\2\2\2g\3\2\2\2\2\u009f\3\2\2\2\2\u00b5\3\2\2\2\2\u00b7\3\2\2\2\2\u00bb"+
		"\3\2\2\2\2\u00c7\3\2\2\2\2\u00c9\3\2\2\2\2\u00cb\3\2\2\2\2\u00cd\3\2\2"+
		"\2\2\u00cf\3\2\2\2\2\u00d1\3\2\2\2\2\u00d3\3\2\2\2\2\u00d5\3\2\2\2\2\u00d7"+
		"\3\2\2\2\2\u00d9\3\2\2\2\2\u00db\3\2\2\2\2\u00dd\3\2\2\2\2\u00df\3\2\2"+
		"\2\2\u00e1\3\2\2\2\2\u00e3\3\2\2\2\2\u00e5\3\2\2\2\2\u00e7\3\2\2\2\2\u00e9"+
		"\3\2\2\2\2\u00eb\3\2\2\2\2\u00ed\3\2\2\2\2\u00ef\3\2\2\2\2\u00f1\3\2\2"+
		"\2\2\u00f3\3\2\2\2\2\u00f5\3\2\2\2\2\u00f7\3\2\2\2\2\u00f9\3\2\2\2\2\u00fb"+
		"\3\2\2\2\2\u00fd\3\2\2\2\2\u00ff\3\2\2\2\2\u0101\3\2\2\2\2\u0103\3\2\2"+
		"\2\2\u0105\3\2\2\2\2\u0107\3\2\2\2\2\u0109\3\2\2\2\2\u010b\3\2\2\2\2\u010d"+
		"\3\2\2\2\2\u010f\3\2\2\2\2\u0111\3\2\2\2\2\u0113\3\2\2\2\2\u0115\3\2\2"+
		"\2\2\u0117\3\2\2\2\2\u0119\3\2\2\2\2\u011b\3\2\2\2\2\u011d\3\2\2\2\2\u011f"+
		"\3\2\2\2\2\u0125\3\2\2\2\2\u0127\3\2\2\2\2\u0129\3\2\2\2\2\u012b\3\2\2"+
		"\2\2\u012d\3\2\2\2\3\u012f\3\2\2\2\5\u0138\3\2\2\2\7\u013f\3\2\2\2\t\u0147"+
		"\3\2\2\2\13\u014d\3\2\2\2\r\u0152\3\2\2\2\17\u0157\3\2\2\2\21\u015d\3"+
		"\2\2\2\23\u0162\3\2\2\2\25\u0168\3\2\2\2\27\u016e\3\2\2\2\31\u0177\3\2"+
		"\2\2\33\u017f\3\2\2\2\35\u0182\3\2\2\2\37\u0189\3\2\2\2!\u018e\3\2\2\2"+
		"#\u0193\3\2\2\2%\u019b\3\2\2\2\'\u01a1\3\2\2\2)\u01a9\3\2\2\2+\u01af\3"+
		"\2\2\2-\u01b3\3\2\2\2/\u01b6\3\2\2\2\61\u01bb\3\2\2\2\63\u01c6\3\2\2\2"+
		"\65\u01cd\3\2\2\2\67\u01d8\3\2\2\29\u01dc\3\2\2\2;\u01e6\3\2\2\2=\u01eb"+
		"\3\2\2\2?\u01f2\3\2\2\2A\u01f6\3\2\2\2C\u01fe\3\2\2\2E\u0206\3\2\2\2G"+
		"\u0210\3\2\2\2I\u0217\3\2\2\2K\u021e\3\2\2\2M\u0224\3\2\2\2O\u022b\3\2"+
		"\2\2Q\u0234\3\2\2\2S\u023a\3\2\2\2U\u0241\3\2\2\2W\u024e\3\2\2\2Y\u0253"+
		"\3\2\2\2[\u0259\3\2\2\2]\u0260\3\2\2\2_\u026a\3\2\2\2a\u026e\3\2\2\2c"+
		"\u0273\3\2\2\2e\u027c\3\2\2\2g\u0286\3\2\2\2i\u0288\3\2\2\2k\u028c\3\2"+
		"\2\2m\u0290\3\2\2\2o\u0294\3\2\2\2q\u0298\3\2\2\2s\u02a4\3\2\2\2u\u02a6"+
		"\3\2\2\2w\u02af\3\2\2\2y\u02b1\3\2\2\2{\u02b4\3\2\2\2}\u02ba\3\2\2\2\177"+
		"\u02bd\3\2\2\2\u0081\u02c1\3\2\2\2\u0083\u02c5\3\2\2\2\u0085\u02cc\3\2"+
		"\2\2\u0087\u02cf\3\2\2\2\u0089\u02d5\3\2\2\2\u008b\u02d7\3\2\2\2\u008d"+
		"\u02dd\3\2\2\2\u008f\u02e4\3\2\2\2\u0091\u02e7\3\2\2\2\u0093\u02ed\3\2"+
		"\2\2\u0095\u02ef\3\2\2\2\u0097\u02f3\3\2\2\2\u0099\u02fa\3\2\2\2\u009b"+
		"\u02fd\3\2\2\2\u009d\u0303\3\2\2\2\u009f\u0307\3\2\2\2\u00a1\u0324\3\2"+
		"\2\2\u00a3\u0326\3\2\2\2\u00a5\u0329\3\2\2\2\u00a7\u032c\3\2\2\2\u00a9"+
		"\u0330\3\2\2\2\u00ab\u0332\3\2\2\2\u00ad\u0334\3\2\2\2\u00af\u0344\3\2"+
		"\2\2\u00b1\u0346\3\2\2\2\u00b3\u0349\3\2\2\2\u00b5\u0354\3\2\2\2\u00b7"+
		"\u035e\3\2\2\2\u00b9\u0360\3\2\2\2\u00bb\u0362\3\2\2\2\u00bd\u0369\3\2"+
		"\2\2\u00bf\u036f\3\2\2\2\u00c1\u0374\3\2\2\2\u00c3\u0381\3\2\2\2\u00c5"+
		"\u0383\3\2\2\2\u00c7\u0385\3\2\2\2\u00c9\u038a\3\2\2\2\u00cb\u038c\3\2"+
		"\2\2\u00cd\u038e\3\2\2\2\u00cf\u0390\3\2\2\2\u00d1\u0392\3\2\2\2\u00d3"+
		"\u0394\3\2\2\2\u00d5\u0396\3\2\2\2\u00d7\u0398\3\2\2\2\u00d9\u039a\3\2"+
		"\2\2\u00db\u039c\3\2\2\2\u00dd\u039e\3\2\2\2\u00df\u03a0\3\2\2\2\u00e1"+
		"\u03a2\3\2\2\2\u00e3\u03a4\3\2\2\2\u00e5\u03a6\3\2\2\2\u00e7\u03a8\3\2"+
		"\2\2\u00e9\u03aa\3\2\2\2\u00eb\u03ad\3\2\2\2\u00ed\u03b0\3\2\2\2\u00ef"+
		"\u03b3\3\2\2\2\u00f1\u03b6\3\2\2\2\u00f3\u03b9\3\2\2\2\u00f5\u03bc\3\2"+
		"\2\2\u00f7\u03bf\3\2\2\2\u00f9\u03c2\3\2\2\2\u00fb\u03c4\3\2\2\2\u00fd"+
		"\u03c6\3\2\2\2\u00ff\u03c8\3\2\2\2\u0101\u03ca\3\2\2\2\u0103\u03cc\3\2"+
		"\2\2\u0105\u03ce\3\2\2\2\u0107\u03d0\3\2\2\2\u0109\u03d2\3\2\2\2\u010b"+
		"\u03d5\3\2\2\2\u010d\u03d8\3\2\2\2\u010f\u03db\3\2\2\2\u0111\u03de\3\2"+
		"\2\2\u0113\u03e1\3\2\2\2\u0115\u03e4\3\2\2\2\u0117\u03e7\3\2\2\2\u0119"+
		"\u03ea\3\2\2\2\u011b\u03ee\3\2\2\2\u011d\u03f2\3\2\2\2\u011f\u03f7\3\2"+
		"\2\2\u0121\u0404\3\2\2\2\u0123\u040c\3\2\2\2\u0125\u040e\3\2\2\2\u0127"+
		"\u0410\3\2\2\2\u0129\u0415\3\2\2\2\u012b\u041b\3\2\2\2\u012d\u0429\3\2"+
		"\2\2\u012f\u0130\7c\2\2\u0130\u0131\7d\2\2\u0131\u0132\7u\2\2\u0132\u0133"+
		"\7v\2\2\u0133\u0134\7t\2\2\u0134\u0135\7c\2\2\u0135\u0136\7e\2\2\u0136"+
		"\u0137\7v\2\2\u0137\4\3\2\2\2\u0138\u0139\7c\2\2\u0139\u013a\7u\2\2\u013a"+
		"\u013b\7u\2\2\u013b\u013c\7g\2\2\u013c\u013d\7t\2\2\u013d\u013e\7v\2\2"+
		"\u013e\6\3\2\2\2\u013f\u0140\7d\2\2\u0140\u0141\7q\2\2\u0141\u0142\7q"+
		"\2\2\u0142\u0143\7n\2\2\u0143\u0144\7g\2\2\u0144\u0145\7c\2\2\u0145\u0146"+
		"\7p\2\2\u0146\b\3\2\2\2\u0147\u0148\7d\2\2\u0148\u0149\7t\2\2\u0149\u014a"+
		"\7g\2\2\u014a\u014b\7c\2\2\u014b\u014c\7m\2\2\u014c\n\3\2\2\2\u014d\u014e"+
		"\7d\2\2\u014e\u014f\7{\2\2\u014f\u0150\7v\2\2\u0150\u0151\7g\2\2\u0151"+
		"\f\3\2\2\2\u0152\u0153\7e\2\2\u0153\u0154\7c\2\2\u0154\u0155\7u\2\2\u0155"+
		"\u0156\7g\2\2\u0156\16\3\2\2\2\u0157\u0158\7e\2\2\u0158\u0159\7c\2\2\u0159"+
		"\u015a\7v\2\2\u015a\u015b\7e\2\2\u015b\u015c\7j\2\2\u015c\20\3\2\2\2\u015d"+
		"\u015e\7e\2\2\u015e\u015f\7j\2\2\u015f\u0160\7c\2\2\u0160\u0161\7t\2\2"+
		"\u0161\22\3\2\2\2\u0162\u0163\7e\2\2\u0163\u0164\7n\2\2\u0164\u0165\7"+
		"c\2\2\u0165\u0166\7u\2\2\u0166\u0167\7u\2\2\u0167\24\3\2\2\2\u0168\u0169"+
		"\7e\2\2\u0169\u016a\7q\2\2\u016a\u016b\7p\2\2\u016b\u016c\7u\2\2\u016c"+
		"\u016d\7v\2\2\u016d\26\3\2\2\2\u016e\u016f\7e\2\2\u016f\u0170\7q\2\2\u0170"+
		"\u0171\7p\2\2\u0171\u0172\7v\2\2\u0172\u0173\7k\2\2\u0173\u0174\7p\2\2"+
		"\u0174\u0175\7w\2\2\u0175\u0176\7g\2\2\u0176\30\3\2\2\2\u0177\u0178\7"+
		"f\2\2\u0178\u0179\7g\2\2\u0179\u017a\7h\2\2\u017a\u017b\7c\2\2\u017b\u017c"+
		"\7w\2\2\u017c\u017d\7n\2\2\u017d\u017e\7v\2\2\u017e\32\3\2\2\2\u017f\u0180"+
		"\7f\2\2\u0180\u0181\7q\2\2\u0181\34\3\2\2\2\u0182\u0183\7f\2\2\u0183\u0184"+
		"\7q\2\2\u0184\u0185\7w\2\2\u0185\u0186\7d\2\2\u0186\u0187\7n\2\2\u0187"+
		"\u0188\7g\2\2\u0188\36\3\2\2\2\u0189\u018a\7g\2\2\u018a\u018b\7n\2\2\u018b"+
		"\u018c\7u\2\2\u018c\u018d\7g\2\2\u018d \3\2\2\2\u018e\u018f\7g\2\2\u018f"+
		"\u0190\7p\2\2\u0190\u0191\7w\2\2\u0191\u0192\7o\2\2\u0192\"\3\2\2\2\u0193"+
		"\u0194\7g\2\2\u0194\u0195\7z\2\2\u0195\u0196\7v\2\2\u0196\u0197\7g\2\2"+
		"\u0197\u0198\7p\2\2\u0198\u0199\7f\2\2\u0199\u019a\7u\2\2\u019a$\3\2\2"+
		"\2\u019b\u019c\7h\2\2\u019c\u019d\7k\2\2\u019d\u019e\7p\2\2\u019e\u019f"+
		"\7c\2\2\u019f\u01a0\7n\2\2\u01a0&\3\2\2\2\u01a1\u01a2\7h\2\2\u01a2\u01a3"+
		"\7k\2\2\u01a3\u01a4\7p\2\2\u01a4\u01a5\7c\2\2\u01a5\u01a6\7n\2\2\u01a6"+
		"\u01a7\7n\2\2\u01a7\u01a8\7{\2\2\u01a8(\3\2\2\2\u01a9\u01aa\7h\2\2\u01aa"+
		"\u01ab\7n\2\2\u01ab\u01ac\7q\2\2\u01ac\u01ad\7c\2\2\u01ad\u01ae\7v\2\2"+
		"\u01ae*\3\2\2\2\u01af\u01b0\7h\2\2\u01b0\u01b1\7q\2\2\u01b1\u01b2\7t\2"+
		"\2\u01b2,\3\2\2\2\u01b3\u01b4\7k\2\2\u01b4\u01b5\7h\2\2\u01b5.\3\2\2\2"+
		"\u01b6\u01b7\7i\2\2\u01b7\u01b8\7q\2\2\u01b8\u01b9\7v\2\2\u01b9\u01ba"+
		"\7q\2\2\u01ba\60\3\2\2\2\u01bb\u01bc\7k\2\2\u01bc\u01bd\7o\2\2\u01bd\u01be"+
		"\7r\2\2\u01be\u01bf\7n\2\2\u01bf\u01c0\7g\2\2\u01c0\u01c1\7o\2\2\u01c1"+
		"\u01c2\7g\2\2\u01c2\u01c3\7p\2\2\u01c3\u01c4\7v\2\2\u01c4\u01c5\7u\2\2"+
		"\u01c5\62\3\2\2\2\u01c6\u01c7\7k\2\2\u01c7\u01c8\7o\2\2\u01c8\u01c9\7"+
		"r\2\2\u01c9\u01ca\7q\2\2\u01ca\u01cb\7t\2\2\u01cb\u01cc\7v\2\2\u01cc\64"+
		"\3\2\2\2\u01cd\u01ce\7k\2\2\u01ce\u01cf\7p\2\2\u01cf\u01d0\7u\2\2\u01d0"+
		"\u01d1\7v\2\2\u01d1\u01d2\7c\2\2\u01d2\u01d3\7p\2\2\u01d3\u01d4\7e\2\2"+
		"\u01d4\u01d5\7g\2\2\u01d5\u01d6\7q\2\2\u01d6\u01d7\7h\2\2\u01d7\66\3\2"+
		"\2\2\u01d8\u01d9\7k\2\2\u01d9\u01da\7p\2\2\u01da\u01db\7v\2\2\u01db8\3"+
		"\2\2\2\u01dc\u01dd\7k\2\2\u01dd\u01de\7p\2\2\u01de\u01df\7v\2\2\u01df"+
		"\u01e0\7g\2\2\u01e0\u01e1\7t\2\2\u01e1\u01e2\7h\2\2\u01e2\u01e3\7c\2\2"+
		"\u01e3\u01e4\7e\2\2\u01e4\u01e5\7g\2\2\u01e5:\3\2\2\2\u01e6\u01e7\7n\2"+
		"\2\u01e7\u01e8\7q\2\2\u01e8\u01e9\7p\2\2\u01e9\u01ea\7i\2\2\u01ea<\3\2"+
		"\2\2\u01eb\u01ec\7p\2\2\u01ec\u01ed\7c\2\2\u01ed\u01ee\7v\2\2\u01ee\u01ef"+
		"\7k\2\2\u01ef\u01f0\7x\2\2\u01f0\u01f1\7g\2\2\u01f1>\3\2\2\2\u01f2\u01f3"+
		"\7p\2\2\u01f3\u01f4\7g\2\2\u01f4\u01f5\7y\2\2\u01f5@\3\2\2\2\u01f6\u01f7"+
		"\7r\2\2\u01f7\u01f8\7c\2\2\u01f8\u01f9\7e\2\2\u01f9\u01fa\7m\2\2\u01fa"+
		"\u01fb\7c\2\2\u01fb\u01fc\7i\2\2\u01fc\u01fd\7g\2\2\u01fdB\3\2\2\2\u01fe"+
		"\u01ff\7r\2\2\u01ff\u0200\7t\2\2\u0200\u0201\7k\2\2\u0201\u0202\7x\2\2"+
		"\u0202\u0203\7c\2\2\u0203\u0204\7v\2\2\u0204\u0205\7g\2\2\u0205D\3\2\2"+
		"\2\u0206\u0207\7r\2\2\u0207\u0208\7t\2\2\u0208\u0209\7q\2\2\u0209\u020a"+
		"\7v\2\2\u020a\u020b\7g\2\2\u020b\u020c\7e\2\2\u020c\u020d\7v\2\2\u020d"+
		"\u020e\7g\2\2\u020e\u020f\7f\2\2\u020fF\3\2\2\2\u0210\u0211\7r\2\2\u0211"+
		"\u0212\7w\2\2\u0212\u0213\7d\2\2\u0213\u0214\7n\2\2\u0214\u0215\7k\2\2"+
		"\u0215\u0216\7e\2\2\u0216H\3\2\2\2\u0217\u0218\7t\2\2\u0218\u0219\7g\2"+
		"\2\u0219\u021a\7v\2\2\u021a\u021b\7w\2\2\u021b\u021c\7t\2\2\u021c\u021d"+
		"\7p\2\2\u021dJ\3\2\2\2\u021e\u021f\7u\2\2\u021f\u0220\7j\2\2\u0220\u0221"+
		"\7q\2\2\u0221\u0222\7t\2\2\u0222\u0223\7v\2\2\u0223L\3\2\2\2\u0224\u0225"+
		"\7u\2\2\u0225\u0226\7v\2\2\u0226\u0227\7c\2\2\u0227\u0228\7v\2\2\u0228"+
		"\u0229\7k\2\2\u0229\u022a\7e\2\2\u022aN\3\2\2\2\u022b\u022c\7u\2\2\u022c"+
		"\u022d\7v\2\2\u022d\u022e\7t\2\2\u022e\u022f\7k\2\2\u022f\u0230\7e\2\2"+
		"\u0230\u0231\7v\2\2\u0231\u0232\7h\2\2\u0232\u0233\7r\2\2\u0233P\3\2\2"+
		"\2\u0234\u0235\7u\2\2\u0235\u0236\7w\2\2\u0236\u0237\7r\2\2\u0237\u0238"+
		"\7g\2\2\u0238\u0239\7t\2\2\u0239R\3\2\2\2\u023a\u023b\7u\2\2\u023b\u023c"+
		"\7y\2\2\u023c\u023d\7k\2\2\u023d\u023e\7v\2\2\u023e\u023f\7e\2\2\u023f"+
		"\u0240\7j\2\2\u0240T\3\2\2\2\u0241\u0242\7u\2\2\u0242\u0243\7{\2\2\u0243"+
		"\u0244\7p\2\2\u0244\u0245\7e\2\2\u0245\u0246\7j\2\2\u0246\u0247\7t\2\2"+
		"\u0247\u0248\7q\2\2\u0248\u0249\7p\2\2\u0249\u024a\7k\2\2\u024a\u024b"+
		"\7|\2\2\u024b\u024c\7g\2\2\u024c\u024d\7f\2\2\u024dV\3\2\2\2\u024e\u024f"+
		"\7v\2\2\u024f\u0250\7j\2\2\u0250\u0251\7k\2\2\u0251\u0252\7u\2\2\u0252"+
		"X\3\2\2\2\u0253\u0254\7v\2\2\u0254\u0255\7j\2\2\u0255\u0256\7t\2\2\u0256"+
		"\u0257\7q\2\2\u0257\u0258\7y\2\2\u0258Z\3\2\2\2\u0259\u025a\7v\2\2\u025a"+
		"\u025b\7j\2\2\u025b\u025c\7t\2\2\u025c\u025d\7q\2\2\u025d\u025e\7y\2\2"+
		"\u025e\u025f\7u\2\2\u025f\\\3\2\2\2\u0260\u0261\7v\2\2\u0261\u0262\7t"+
		"\2\2\u0262\u0263\7c\2\2\u0263\u0264\7p\2\2\u0264\u0265\7u\2\2\u0265\u0266"+
		"\7k\2\2\u0266\u0267\7g\2\2\u0267\u0268\7p\2\2\u0268\u0269\7v\2\2\u0269"+
		"^\3\2\2\2\u026a\u026b\7v\2\2\u026b\u026c\7t\2\2\u026c\u026d\7{\2\2\u026d"+
		"`\3\2\2\2\u026e\u026f\7x\2\2\u026f\u0270\7q\2\2\u0270\u0271\7k\2\2\u0271"+
		"\u0272\7f\2\2\u0272b\3\2\2\2\u0273\u0274\7x\2\2\u0274\u0275\7q\2\2\u0275"+
		"\u0276\7n\2\2\u0276\u0277\7c\2\2\u0277\u0278\7v\2\2\u0278\u0279\7k\2\2"+
		"\u0279\u027a\7n\2\2\u027a\u027b\7g\2\2\u027bd\3\2\2\2\u027c\u027d\7y\2"+
		"\2\u027d\u027e\7j\2\2\u027e\u027f\7k\2\2\u027f\u0280\7n\2\2\u0280\u0281"+
		"\7g\2\2\u0281f\3\2\2\2\u0282\u0287\5i\65\2\u0283\u0287\5k\66\2\u0284\u0287"+
		"\5m\67\2\u0285\u0287\5o8\2\u0286\u0282\3\2\2\2\u0286\u0283\3\2\2\2\u0286"+
		"\u0284\3\2\2\2\u0286\u0285\3\2\2\2\u0287h\3\2\2\2\u0288\u028a\5s:\2\u0289"+
		"\u028b\5q9\2\u028a\u0289\3\2\2\2\u028a\u028b\3\2\2\2\u028bj\3\2\2\2\u028c"+
		"\u028e\5\u0081A\2\u028d\u028f\5q9\2\u028e\u028d\3\2\2\2\u028e\u028f\3"+
		"\2\2\2\u028fl\3\2\2\2\u0290\u0292\5\u008bF\2\u0291\u0293\5q9\2\u0292\u0291"+
		"\3\2\2\2\u0292\u0293\3\2\2\2\u0293n\3\2\2\2\u0294\u0296\5\u0095K\2\u0295"+
		"\u0297\5q9\2\u0296\u0295\3\2\2\2\u0296\u0297\3\2\2\2\u0297p\3\2\2\2\u0298"+
		"\u0299\t\2\2\2\u0299r\3\2\2\2\u029a\u02a5\7\62\2\2\u029b\u02a2\5y=\2\u029c"+
		"\u029e\5u;\2\u029d\u029c\3\2\2\2\u029d\u029e\3\2\2\2\u029e\u02a3\3\2\2"+
		"\2\u029f\u02a0\5\177@\2\u02a0\u02a1\5u;\2\u02a1\u02a3\3\2\2\2\u02a2\u029d"+
		"\3\2\2\2\u02a2\u029f\3\2\2\2\u02a3\u02a5\3\2\2\2\u02a4\u029a\3\2\2\2\u02a4"+
		"\u029b\3\2\2\2\u02a5t\3\2\2\2\u02a6\u02ab\5w<\2\u02a7\u02a9\5{>\2\u02a8"+
		"\u02a7\3\2\2\2\u02a8\u02a9\3\2\2\2\u02a9\u02aa\3\2\2\2\u02aa\u02ac\5w"+
		"<\2\u02ab\u02a8\3\2\2\2\u02ab\u02ac\3\2\2\2\u02acv\3\2\2\2\u02ad\u02b0"+
		"\7\62\2\2\u02ae\u02b0\5y=\2\u02af\u02ad\3\2\2\2\u02af\u02ae\3\2\2\2\u02b0"+
		"x\3\2\2\2\u02b1\u02b2\t\3\2\2\u02b2z\3\2\2\2\u02b3\u02b5\5}?\2\u02b4\u02b3"+
		"\3\2\2\2\u02b5\u02b6\3\2\2\2\u02b6\u02b4\3\2\2\2\u02b6\u02b7\3\2\2\2\u02b7"+
		"|\3\2\2\2\u02b8\u02bb\5w<\2\u02b9\u02bb\7a\2\2\u02ba\u02b8\3\2\2\2\u02ba"+
		"\u02b9\3\2\2\2\u02bb~\3\2\2\2\u02bc\u02be\7a\2\2\u02bd\u02bc\3\2\2\2\u02be"+
		"\u02bf\3\2\2\2\u02bf\u02bd\3\2\2\2\u02bf\u02c0\3\2\2\2\u02c0\u0080\3\2"+
		"\2\2\u02c1\u02c2\7\62\2\2\u02c2\u02c3\t\4\2\2\u02c3\u02c4\5\u0083B\2\u02c4"+
		"\u0082\3\2\2\2\u02c5\u02ca\5\u0085C\2\u02c6\u02c8\5\u0087D\2\u02c7\u02c6"+
		"\3\2\2\2\u02c7\u02c8\3\2\2\2\u02c8\u02c9\3\2\2\2\u02c9\u02cb\5\u0085C"+
		"\2\u02ca\u02c7\3\2\2\2\u02ca\u02cb\3\2\2\2\u02cb\u0084\3\2\2\2\u02cc\u02cd"+
		"\t\5\2\2\u02cd\u0086\3\2\2\2\u02ce\u02d0\5\u0089E\2\u02cf\u02ce\3\2\2"+
		"\2\u02d0\u02d1\3\2\2\2\u02d1\u02cf\3\2\2\2\u02d1\u02d2\3\2\2\2\u02d2\u0088"+
		"\3\2\2\2\u02d3\u02d6\5\u0085C\2\u02d4\u02d6\7a\2\2\u02d5\u02d3\3\2\2\2"+
		"\u02d5\u02d4\3\2\2\2\u02d6\u008a\3\2\2\2\u02d7\u02d9\7\62\2\2\u02d8\u02da"+
		"\5\177@\2\u02d9\u02d8\3\2\2\2\u02d9\u02da\3\2\2\2\u02da\u02db\3\2\2\2"+
		"\u02db\u02dc\5\u008dG\2\u02dc\u008c\3\2\2\2\u02dd\u02e2\5\u008fH\2\u02de"+
		"\u02e0\5\u0091I\2\u02df\u02de\3\2\2\2\u02df\u02e0\3\2\2\2\u02e0\u02e1"+
		"\3\2\2\2\u02e1\u02e3\5\u008fH\2\u02e2\u02df\3\2\2\2\u02e2\u02e3\3\2\2"+
		"\2\u02e3\u008e\3\2\2\2\u02e4\u02e5\t\6\2\2\u02e5\u0090\3\2\2\2\u02e6\u02e8"+
		"\5\u0093J\2\u02e7\u02e6\3\2\2\2\u02e8\u02e9\3\2\2\2\u02e9\u02e7\3\2\2"+
		"\2\u02e9\u02ea\3\2\2\2\u02ea\u0092\3\2\2\2\u02eb\u02ee\5\u008fH\2\u02ec"+
		"\u02ee\7a\2\2\u02ed\u02eb\3\2\2\2\u02ed\u02ec\3\2\2\2\u02ee\u0094\3\2"+
		"\2\2\u02ef\u02f0\7\62\2\2\u02f0\u02f1\t\7\2\2\u02f1\u02f2\5\u0097L\2\u02f2"+
		"\u0096\3\2\2\2\u02f3\u02f8\5\u0099M\2\u02f4\u02f6\5\u009bN\2\u02f5\u02f4"+
		"\3\2\2\2\u02f5\u02f6\3\2\2\2\u02f6\u02f7\3\2\2\2\u02f7\u02f9\5\u0099M"+
		"\2\u02f8\u02f5\3\2\2\2\u02f8\u02f9\3\2\2\2\u02f9\u0098\3\2\2\2\u02fa\u02fb"+
		"\t\b\2\2\u02fb\u009a\3\2\2\2\u02fc\u02fe\5\u009dO\2\u02fd\u02fc\3\2\2"+
		"\2\u02fe\u02ff\3\2\2\2\u02ff\u02fd\3\2\2\2\u02ff\u0300\3\2\2\2\u0300\u009c"+
		"\3\2\2\2\u0301\u0304\5\u0099M\2\u0302\u0304\7a\2\2\u0303\u0301\3\2\2\2"+
		"\u0303\u0302\3\2\2\2\u0304\u009e\3\2\2\2\u0305\u0308\5\u00a1Q\2\u0306"+
		"\u0308\5\u00adW\2\u0307\u0305\3\2\2\2\u0307\u0306\3\2\2\2\u0308\u00a0"+
		"\3\2\2\2\u0309\u030a\5u;\2\u030a\u030c\7\60\2\2\u030b\u030d\5u;\2\u030c"+
		"\u030b\3\2\2\2\u030c\u030d\3\2\2\2\u030d\u030f\3\2\2\2\u030e\u0310\5\u00a3"+
		"R\2\u030f\u030e\3\2\2\2\u030f\u0310\3\2\2\2\u0310\u0312\3\2\2\2\u0311"+
		"\u0313\5\u00abV\2\u0312\u0311\3\2\2\2\u0312\u0313\3\2\2\2\u0313\u0325"+
		"\3\2\2\2\u0314\u0315\7\60\2\2\u0315\u0317\5u;\2\u0316\u0318\5\u00a3R\2"+
		"\u0317\u0316\3\2\2\2\u0317\u0318\3\2\2\2\u0318\u031a\3\2\2\2\u0319\u031b"+
		"\5\u00abV\2\u031a\u0319\3\2\2\2\u031a\u031b\3\2\2\2\u031b\u0325\3\2\2"+
		"\2\u031c\u031d\5u;\2\u031d\u031f\5\u00a3R\2\u031e\u0320\5\u00abV\2\u031f"+
		"\u031e\3\2\2\2\u031f\u0320\3\2\2\2\u0320\u0325\3\2\2\2\u0321\u0322\5u"+
		";\2\u0322\u0323\5\u00abV\2\u0323\u0325\3\2\2\2\u0324\u0309\3\2\2\2\u0324"+
		"\u0314\3\2\2\2\u0324\u031c\3\2\2\2\u0324\u0321\3\2\2\2\u0325\u00a2\3\2"+
		"\2\2\u0326\u0327\5\u00a5S\2\u0327\u0328\5\u00a7T\2\u0328\u00a4\3\2\2\2"+
		"\u0329\u032a\t\t\2\2\u032a\u00a6\3\2\2\2\u032b\u032d\5\u00a9U\2\u032c"+
		"\u032b\3\2\2\2\u032c\u032d\3\2\2\2\u032d\u032e\3\2\2\2\u032e\u032f\5u"+
		";\2\u032f\u00a8\3\2\2\2\u0330\u0331\t\n\2\2\u0331\u00aa\3\2\2\2\u0332"+
		"\u0333\t\13\2\2\u0333\u00ac\3\2\2\2\u0334\u0335\5\u00afX\2\u0335\u0337"+
		"\5\u00b1Y\2\u0336\u0338\5\u00abV\2\u0337\u0336\3\2\2\2\u0337\u0338\3\2"+
		"\2\2\u0338\u00ae\3\2\2\2\u0339\u033b\5\u0081A\2\u033a\u033c\7\60\2\2\u033b"+
		"\u033a\3\2\2\2\u033b\u033c\3\2\2\2\u033c\u0345\3\2\2\2\u033d\u033e\7\62"+
		"\2\2\u033e\u0340\t\4\2\2\u033f\u0341\5\u0083B\2\u0340\u033f\3\2\2\2\u0340"+
		"\u0341\3\2\2\2\u0341\u0342\3\2\2\2\u0342\u0343\7\60\2\2\u0343\u0345\5"+
		"\u0083B\2\u0344\u0339\3\2\2\2\u0344\u033d\3\2\2\2\u0345\u00b0\3\2\2\2"+
		"\u0346\u0347\5\u00b3Z\2\u0347\u0348\5\u00a7T\2\u0348\u00b2\3\2\2\2\u0349"+
		"\u034a\t\f\2\2\u034a\u00b4\3\2\2\2\u034b\u034c\7v\2\2\u034c\u034d\7t\2"+
		"\2\u034d\u034e\7w\2\2\u034e\u0355\7g\2\2\u034f\u0350\7h\2\2\u0350\u0351"+
		"\7c\2\2\u0351\u0352\7n\2\2\u0352\u0353\7u\2\2\u0353\u0355\7g\2\2\u0354"+
		"\u034b\3\2\2\2\u0354\u034f\3\2\2\2\u0355\u00b6\3\2\2\2\u0356\u0357\7)"+
		"\2\2\u0357\u0358\5\u00b9]\2\u0358\u0359\7)\2\2\u0359\u035f\3\2\2\2\u035a"+
		"\u035b\7)\2\2\u035b\u035c\5\u00c1a\2\u035c\u035d\7)\2\2\u035d\u035f\3"+
		"\2\2\2\u035e\u0356\3\2\2\2\u035e\u035a\3\2\2\2\u035f\u00b8\3\2\2\2\u0360"+
		"\u0361\n\r\2\2\u0361\u00ba\3\2\2\2\u0362\u0364\7$\2\2\u0363\u0365\5\u00bd"+
		"_\2\u0364\u0363\3\2\2\2\u0364\u0365\3\2\2\2\u0365\u0366\3\2\2\2\u0366"+
		"\u0367\7$\2\2\u0367\u00bc\3\2\2\2\u0368\u036a\5\u00bf`\2\u0369\u0368\3"+
		"\2\2\2\u036a\u036b\3\2\2\2\u036b\u0369\3\2\2\2\u036b\u036c\3\2\2\2\u036c"+
		"\u00be\3\2\2\2\u036d\u0370\n\16\2\2\u036e\u0370\5\u00c1a\2\u036f\u036d"+
		"\3\2\2\2\u036f\u036e\3\2\2\2\u0370\u00c0\3\2\2\2\u0371\u0372\7^\2\2\u0372"+
		"\u0375\t\17\2\2\u0373\u0375\5\u00c3b\2\u0374\u0371\3\2\2\2\u0374\u0373"+
		"\3\2\2\2\u0375\u00c2\3\2\2\2\u0376\u0377\7^\2\2\u0377\u0382\5\u008fH\2"+
		"\u0378\u0379\7^\2\2\u0379\u037a\5\u008fH\2\u037a\u037b\5\u008fH\2\u037b"+
		"\u0382\3\2\2\2\u037c\u037d\7^\2\2\u037d\u037e\5\u00c5c\2\u037e\u037f\5"+
		"\u008fH\2\u037f\u0380\5\u008fH\2\u0380\u0382\3\2\2\2\u0381\u0376\3\2\2"+
		"\2\u0381\u0378\3\2\2\2\u0381\u037c\3\2\2\2\u0382\u00c4\3\2\2\2\u0383\u0384"+
		"\t\20\2\2\u0384\u00c6\3\2\2\2\u0385\u0386\7p\2\2\u0386\u0387\7w\2\2\u0387"+
		"\u0388\7n\2\2\u0388\u0389\7n\2\2\u0389\u00c8\3\2\2\2\u038a\u038b\7*\2"+
		"\2\u038b\u00ca\3\2\2\2\u038c\u038d\7+\2\2\u038d\u00cc\3\2\2\2\u038e\u038f"+
		"\7}\2\2\u038f\u00ce\3\2\2\2\u0390\u0391\7\177\2\2\u0391\u00d0\3\2\2\2"+
		"\u0392\u0393\7]\2\2\u0393\u00d2\3\2\2\2\u0394\u0395\7_\2\2\u0395\u00d4"+
		"\3\2\2\2\u0396\u0397\7=\2\2\u0397\u00d6\3\2\2\2\u0398\u0399\7.\2\2\u0399"+
		"\u00d8\3\2\2\2\u039a\u039b\7\60\2\2\u039b\u00da\3\2\2\2\u039c\u039d\7"+
		"?\2\2\u039d\u00dc\3\2\2\2\u039e\u039f\7@\2\2\u039f\u00de\3\2\2\2\u03a0"+
		"\u03a1\7>\2\2\u03a1\u00e0\3\2\2\2\u03a2\u03a3\7#\2\2\u03a3\u00e2\3\2\2"+
		"\2\u03a4\u03a5\7\u0080\2\2\u03a5\u00e4\3\2\2\2\u03a6\u03a7\7A\2\2\u03a7"+
		"\u00e6\3\2\2\2\u03a8\u03a9\7<\2\2\u03a9\u00e8\3\2\2\2\u03aa\u03ab\7?\2"+
		"\2\u03ab\u03ac\7?\2\2\u03ac\u00ea\3\2\2\2\u03ad\u03ae\7>\2\2\u03ae\u03af"+
		"\7?\2\2\u03af\u00ec\3\2\2\2\u03b0\u03b1\7@\2\2\u03b1\u03b2\7?\2\2\u03b2"+
		"\u00ee\3\2\2\2\u03b3\u03b4\7#\2\2\u03b4\u03b5\7?\2\2\u03b5\u00f0\3\2\2"+
		"\2\u03b6\u03b7\7(\2\2\u03b7\u03b8\7(\2\2\u03b8\u00f2\3\2\2\2\u03b9\u03ba"+
		"\7~\2\2\u03ba\u03bb\7~\2\2\u03bb\u00f4\3\2\2\2\u03bc\u03bd\7-\2\2\u03bd"+
		"\u03be\7-\2\2\u03be\u00f6\3\2\2\2\u03bf\u03c0\7/\2\2\u03c0\u03c1\7/\2"+
		"\2\u03c1\u00f8\3\2\2\2\u03c2\u03c3\7-\2\2\u03c3\u00fa\3\2\2\2\u03c4\u03c5"+
		"\7/\2\2\u03c5\u00fc\3\2\2\2\u03c6\u03c7\7,\2\2\u03c7\u00fe\3\2\2\2\u03c8"+
		"\u03c9\7\61\2\2\u03c9\u0100\3\2\2\2\u03ca\u03cb\7(\2\2\u03cb\u0102\3\2"+
		"\2\2\u03cc\u03cd\7~\2\2\u03cd\u0104\3\2\2\2\u03ce\u03cf\7`\2\2\u03cf\u0106"+
		"\3\2\2\2\u03d0\u03d1\7\'\2\2\u03d1\u0108\3\2\2\2\u03d2\u03d3\7-\2\2\u03d3"+
		"\u03d4\7?\2\2\u03d4\u010a\3\2\2\2\u03d5\u03d6\7/\2\2\u03d6\u03d7\7?\2"+
		"\2\u03d7\u010c\3\2\2\2\u03d8\u03d9\7,\2\2\u03d9\u03da\7?\2\2\u03da\u010e"+
		"\3\2\2\2\u03db\u03dc\7\61\2\2\u03dc\u03dd\7?\2\2\u03dd\u0110\3\2\2\2\u03de"+
		"\u03df\7(\2\2\u03df\u03e0\7?\2\2\u03e0\u0112\3\2\2\2\u03e1\u03e2\7~\2"+
		"\2\u03e2\u03e3\7?\2\2\u03e3\u0114\3\2\2\2\u03e4\u03e5\7`\2\2\u03e5\u03e6"+
		"\7?\2\2\u03e6\u0116\3\2\2\2\u03e7\u03e8\7\'\2\2\u03e8\u03e9\7?\2\2\u03e9"+
		"\u0118\3\2\2\2\u03ea\u03eb\7>\2\2\u03eb\u03ec\7>\2\2\u03ec\u03ed\7?\2"+
		"\2\u03ed\u011a\3\2\2\2\u03ee\u03ef\7@\2\2\u03ef\u03f0\7@\2\2\u03f0\u03f1"+
		"\7?\2\2\u03f1\u011c\3\2\2\2\u03f2\u03f3\7@\2\2\u03f3\u03f4\7@\2\2\u03f4"+
		"\u03f5\7@\2\2\u03f5\u03f6\7?\2\2\u03f6\u011e\3\2\2\2\u03f7\u03fb\5\u0121"+
		"\u0091\2\u03f8\u03fa\5\u0123\u0092\2\u03f9\u03f8\3\2\2\2\u03fa\u03fd\3"+
		"\2\2\2\u03fb\u03f9\3\2\2\2\u03fb\u03fc\3\2\2\2\u03fc\u0120\3\2\2\2\u03fd"+
		"\u03fb\3\2\2\2\u03fe\u0405\t\21\2\2\u03ff\u0400\n\22\2\2\u0400\u0405\6"+
		"\u0091\2\2\u0401\u0402\t\23\2\2\u0402\u0403\t\24\2\2\u0403\u0405\6\u0091"+
		"\3\2\u0404\u03fe\3\2\2\2\u0404\u03ff\3\2\2\2\u0404\u0401\3\2\2\2\u0405"+
		"\u0122\3\2\2\2\u0406\u040d\t\25\2\2\u0407\u0408\n\22\2\2\u0408\u040d\6"+
		"\u0092\4\2\u0409\u040a\t\23\2\2\u040a\u040b\t\24\2\2\u040b\u040d\6\u0092"+
		"\5\2\u040c\u0406\3\2\2\2\u040c\u0407\3\2\2\2\u040c\u0409\3\2\2\2\u040d"+
		"\u0124\3\2\2\2\u040e\u040f\7B\2\2\u040f\u0126\3\2\2\2\u0410\u0411\7\60"+
		"\2\2\u0411\u0412\7\60\2\2\u0412\u0413\7\60\2\2\u0413\u0128\3\2\2\2\u0414"+
		"\u0416\t\26\2\2\u0415\u0414\3\2\2\2\u0416\u0417\3\2\2\2\u0417\u0415\3"+
		"\2\2\2\u0417\u0418\3\2\2\2\u0418\u0419\3\2\2\2\u0419\u041a\b\u0095\2\2"+
		"\u041a\u012a\3\2\2\2\u041b\u041c\7\61\2\2\u041c\u041d\7,\2\2\u041d\u0421"+
		"\3\2\2\2\u041e\u0420\13\2\2\2\u041f\u041e\3\2\2\2\u0420\u0423\3\2\2\2"+
		"\u0421\u0422\3\2\2\2\u0421\u041f\3\2\2\2\u0422\u0424\3\2\2\2\u0423\u0421"+
		"\3\2\2\2\u0424\u0425\7,\2\2\u0425\u0426\7\61\2\2\u0426\u0427\3\2\2\2\u0427"+
		"\u0428\b\u0096\2\2\u0428\u012c\3\2\2\2\u0429\u042a\7\61\2\2\u042a\u042b"+
		"\7\61\2\2\u042b\u042f\3\2\2\2\u042c\u042e\n\27\2\2\u042d\u042c\3\2\2\2"+
		"\u042e\u0431\3\2\2\2\u042f\u042d\3\2\2\2\u042f\u0430\3\2\2\2\u0430\u0432"+
		"\3\2\2\2\u0431\u042f\3\2\2\2\u0432\u0433\b\u0097\2\2\u0433\u012e\3\2\2"+
		"\28\2\u0286\u028a\u028e\u0292\u0296\u029d\u02a2\u02a4\u02a8\u02ab\u02af"+
		"\u02b6\u02ba\u02bf\u02c7\u02ca\u02d1\u02d5\u02d9\u02df\u02e2\u02e9\u02ed"+
		"\u02f5\u02f8\u02ff\u0303\u0307\u030c\u030f\u0312\u0317\u031a\u031f\u0324"+
		"\u032c\u0337\u033b\u0340\u0344\u0354\u035e\u0364\u036b\u036f\u0374\u0381"+
		"\u03fb\u0404\u040c\u0417\u0421\u042f\3\b\2\2";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
	}
}