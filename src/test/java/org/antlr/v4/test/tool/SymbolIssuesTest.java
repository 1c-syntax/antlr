/*
 * This file is a part of ANTLR.
 *
 * Copyright (c) 2012-2025 The ANTLR Project. All rights reserved.
 * Copyright (c) 2025 Valery Maximov <maximovvalery@gmail.com> and contributors
 *
 * Use of this file is governed by the BSD-3-Clause license that
 * can be found in the LICENSE.txt file in the project root.
 */
package org.antlr.v4.test.tool;

import org.antlr.runtime.RecognitionException;
import org.antlr.v4.tool.ErrorType;
import org.antlr.v4.tool.LexerGrammar;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.antlr.v4.TestUtils.assertEquals;

public class SymbolIssuesTest extends AbstractBaseTest {
  static String[] A = {
    // INPUT
    """
grammar A;
options { opt='sss'; k=3; }

@members {foo}
@members {bar}
@lexer::header {package jj;}
@lexer::header {package kk;}

a[int i] returns [foo f] : X ID a[3] b[34] c ;
b returns [int g] : Y 'y' 'if' a ;
c : FJKD ;

ID : 'a'..'z'+ ID ;""",
    // YIELDS
    "error(" + ErrorType.ACTION_REDEFINITION.code + "): A.g4:5:1: redefinition of 'members' action\n" +
      "error(" + ErrorType.ACTION_REDEFINITION.code + "): A.g4:7:1: redefinition of 'header' action\n"
  };

  static String[] B = {
    // INPUT
    """
parser grammar B;
tokens { ID, FOO, X, Y }

a : s=ID b+=ID X=ID '.' ;

b : x=ID x+=ID ;

s : FOO ;""",
    // YIELDS
    "error(" + ErrorType.LABEL_CONFLICTS_WITH_RULE.code + "): B.g4:4:4: label 's' conflicts with rule with same name\n" +
      "error(" + ErrorType.LABEL_CONFLICTS_WITH_RULE.code + "): B.g4:4:9: label 'b' conflicts with rule with same name\n" +
      "error(" + ErrorType.LABEL_CONFLICTS_WITH_TOKEN.code + "): B.g4:4:15: label 'X' conflicts with token with same name\n" +
      "error(" + ErrorType.LABEL_TYPE_CONFLICT.code + "): B.g4:6:9: label 'x' type mismatch with previous definition: TOKEN_LIST_LABEL!=TOKEN_LABEL\n" +
      "error(" + ErrorType.IMPLICIT_STRING_DEFINITION.code + "): B.g4:4:20: cannot create implicit token for string literal in non-combined grammar: '.'\n"
  };

  static String[] D = {
    // INPUT
    """
parser grammar D;
tokens{ID}
a[int j]\s
        :       i=ID j=ID ;

b[int i] returns [int i] : ID ;

c[int i] returns [String k]
        :       ID ;""",

    // YIELDS
    "error(" + ErrorType.LABEL_CONFLICTS_WITH_ARG.code + "): D.g4:4:21: label 'j' conflicts with parameter with same name\n" +
      "error(" + ErrorType.RETVAL_CONFLICTS_WITH_ARG.code + "): D.g4:6:22: return value 'i' conflicts with parameter with same name\n"
  };

  static String[] E = {
    // INPUT
    """
grammar E;
tokens {
	A, A,
	B,
	C
}
a : A ;
""",

    // YIELDS
    "warning(" + ErrorType.TOKEN_NAME_REASSIGNMENT.code + "): E.g4:3:4: token name 'A' is already defined\n"
  };

  static String[] F = {
    // INPUT
    """
lexer grammar F;
A: 'a';
mode M1;
A1: 'a';
mode M2;
A2: 'a';
M1: 'b';
""",

    // YIELDS
    "error(" + ErrorType.MODE_CONFLICTS_WITH_TOKEN.code + "): F.g4:3:0: mode 'M1' conflicts with token with same name\n"
  };

  @Test
  public void testA() {
    super.testErrors(A, false);
  }

  @Test
  public void testB() {
    super.testErrors(B, false);
  }

  @Test
  public void testD() {
    super.testErrors(D, false);
  }

  @Test
  public void testE() {
    super.testErrors(E, false);
  }

  @Test
  public void testF() {
    super.testErrors(F, false);
  }

  @Test
  public void testStringLiteralRedefs() throws RecognitionException {
    String grammar =
      """
        lexer grammar L;
        A : 'a' ;
        mode X;
        B : 'a' ;
        mode Y;
        C : 'a' ;
        """;

    LexerGrammar g = new LexerGrammar(grammar);

    String expectedTokenIDToTypeMap = "{EOF=-1, A=1, B=2, C=3}";
    String expectedStringLiteralToTypeMap = "{}";
    String expectedTypeToTokenList = "[A, B, C]";

    assertEquals(expectedTokenIDToTypeMap, g.tokenNameToTypeMap.toString());
    assertEquals(expectedStringLiteralToTypeMap, g.stringLiteralToTypeMap.toString());
    assertEquals(expectedTypeToTokenList, realElements(g.typeToTokenList).toString());
  }

  @Test
  public void testEmptyLexerModeDetection() {
    String[] test = {
      """
lexer grammar L;
A : 'a';
mode X;
fragment B : 'b';""",

      "error(" + ErrorType.MODE_WITHOUT_RULES.code + "): L.g4:3:5: lexer mode 'X' must contain at least one non-fragment rule\n"
    };

    testErrors(test, false);
  }

  @Test
  public void testEmptyLexerRuleDetection() {
    String[] test = {
      """
lexer grammar L;
A : 'a';
WS : [ \t]* -> skip;
mode X;
  B : C;
  fragment C : A | (A C)?;""",

      "warning(" + ErrorType.EPSILON_TOKEN.code + "): L.g4:3:0: non-fragment lexer rule 'WS' can match the empty string\n" +
        "warning(" + ErrorType.EPSILON_TOKEN.code + "): L.g4:5:2: non-fragment lexer rule 'B' can match the empty string\n"
    };

    testErrors(test, false);
  }

  @Test
  public void testTokensModesChannelsDeclarationConflictsWithReserved() {
    String[] test = {
      """
lexer grammar L;
channels { SKIP, HIDDEN, channel0 }
A: 'a';
mode MAX_CHAR_VALUE;
MIN_CHAR_VALUE: 'a';
mode DEFAULT_MODE;
B: 'b';
mode M;
C: 'c';""",

      "error(" + ErrorType.RESERVED_RULE_NAME.code + "): L.g4:5:0: cannot declare a rule with reserved name 'MIN_CHAR_VALUE'\n" +
        "error(" + ErrorType.MODE_CONFLICTS_WITH_COMMON_CONSTANTS.code + "): L.g4:4:0: cannot use or declare mode with reserved name 'MAX_CHAR_VALUE'\n" +
        "error(" + ErrorType.CHANNEL_CONFLICTS_WITH_COMMON_CONSTANTS.code + "): L.g4:2:11: cannot use or declare channel with reserved name 'SKIP'\n" +
        "error(" + ErrorType.CHANNEL_CONFLICTS_WITH_COMMON_CONSTANTS.code + "): L.g4:2:17: cannot use or declare channel with reserved name 'HIDDEN'\n"
    };

    testErrors(test, false);
  }

  @Test
  public void testTokensModesChannelsUsingConflictsWithReserved() {
    String[] test = {
      """
lexer grammar L;
A: 'a' -> channel(SKIP);
B: 'b' -> type(MORE);
C: 'c' -> mode(SKIP);
D: 'd' -> channel(HIDDEN);
E: 'e' -> type(EOF);
F: 'f' -> pushMode(DEFAULT_MODE);""",

      "error(" + ErrorType.CHANNEL_CONFLICTS_WITH_COMMON_CONSTANTS.code + "): L.g4:2:18: cannot use or declare channel with reserved name 'SKIP'\n" +
        "error(" + ErrorType.TOKEN_CONFLICTS_WITH_COMMON_CONSTANTS.code + "): L.g4:3:15: cannot use or declare token with reserved name 'MORE'\n" +
        "error(" + ErrorType.MODE_CONFLICTS_WITH_COMMON_CONSTANTS.code + "): L.g4:4:15: cannot use or declare mode with reserved name 'SKIP'\n"
    };

    testErrors(test, false);
  }

  // https://github.com/antlr/antlr4/issues/1411
  @Test
  public void testWrongIdForTypeChannelModeCommand() {
    String[] test = {
      """
lexer grammar L;
tokens { TOKEN1 }
channels { CHANNEL1 }
TOKEN: 'asdf' -> type(CHANNEL1), channel(MODE1), mode(TOKEN1);
mode MODE1;
MODE1_TOKEN: 'qwer';""",

      "warning(" + ErrorType.CONSTANT_VALUE_IS_NOT_A_RECOGNIZED_TOKEN_NAME.code + "): L.g4:4:22: 'CHANNEL1' is not a recognized token name\n" +
        "warning(" + ErrorType.CONSTANT_VALUE_IS_NOT_A_RECOGNIZED_CHANNEL_NAME.code + "): L.g4:4:41: 'MODE1' is not a recognized channel name\n" +
        "warning(" + ErrorType.CONSTANT_VALUE_IS_NOT_A_RECOGNIZED_MODE_NAME.code + "): L.g4:4:54: 'TOKEN1' is not a recognized mode name\n"
    };

    testErrors(test, false);
  }

  // https://github.com/antlr/antlr4/issues/1388
  @Test
  public void testDuplicatedCommands() {
    String[] test = {
      """
lexer grammar Lexer;
channels { CHANNEL1, CHANNEL2 }
tokens { TEST1, TEST2 }
TOKEN: 'a' -> mode(MODE1), mode(MODE2);
TOKEN1: 'b' -> pushMode(MODE1), mode(MODE2);
TOKEN2: 'c' -> pushMode(MODE1), pushMode(MODE2); // pushMode is not duplicate
TOKEN3: 'd' -> popMode, popMode;                 // popMode is not duplicate
mode MODE1;
MODE1_TOKEN: 'e';
mode MODE2;
MODE2_TOKEN: 'f';
MODE2_TOKEN1: 'g' -> type(TEST1), type(TEST2);
MODE2_TOKEN2: 'h' -> channel(CHANNEL1), channel(CHANNEL2), channel(DEFAULT_TOKEN_CHANNEL);""",

      "warning(" + ErrorType.DUPLICATED_COMMAND.code + "): Lexer.g4:4:27: duplicated command 'mode'\n" +
        "warning(" + ErrorType.DUPLICATED_COMMAND.code + "): Lexer.g4:12:34: duplicated command 'type'\n" +
        "warning(" + ErrorType.DUPLICATED_COMMAND.code + "): Lexer.g4:13:40: duplicated command 'channel'\n" +
        "warning(" + ErrorType.DUPLICATED_COMMAND.code + "): Lexer.g4:13:59: duplicated command 'channel'\n"
    };

    testErrors(test, false);
  }

  // https://github.com/antlr/antlr4/issues/1388
  @Test
  public void testIncompatibleCommands() {
    String[] test = {
      """
lexer grammar L;
channels { CHANNEL1 }
tokens { TYPE1 }
// Incompatible
T00: 'a00' -> skip, more;
T01: 'a01' -> skip, type(TYPE1);
T02: 'a02' -> skip, channel(CHANNEL1);
T03: 'a03' -> more, type(TYPE1);
T04: 'a04' -> more, channel(CHANNEL1);
T05: 'a05' -> more, skip;
T06: 'a06' -> type(TYPE1), skip;
T07: 'a07' -> type(TYPE1), more;
T08: 'a08' -> channel(CHANNEL1), skip;
T09: 'a09' -> channel(CHANNEL1), more;
// Allowed
T10: 'a10' -> type(TYPE1), channel(CHANNEL1);
T11: 'a11' -> channel(CHANNEL1), type(TYPE1);""",

      "warning(" + ErrorType.INCOMPATIBLE_COMMANDS.code + "): L.g4:5:20: incompatible commands 'skip' and 'more'\n" +
        "warning(" + ErrorType.INCOMPATIBLE_COMMANDS.code + "): L.g4:6:20: incompatible commands 'skip' and 'type'\n" +
        "warning(" + ErrorType.INCOMPATIBLE_COMMANDS.code + "): L.g4:7:20: incompatible commands 'skip' and 'channel'\n" +
        "warning(" + ErrorType.INCOMPATIBLE_COMMANDS.code + "): L.g4:8:20: incompatible commands 'more' and 'type'\n" +
        "warning(" + ErrorType.INCOMPATIBLE_COMMANDS.code + "): L.g4:9:20: incompatible commands 'more' and 'channel'\n" +
        "warning(" + ErrorType.INCOMPATIBLE_COMMANDS.code + "): L.g4:10:20: incompatible commands 'more' and 'skip'\n" +
        "warning(" + ErrorType.INCOMPATIBLE_COMMANDS.code + "): L.g4:11:27: incompatible commands 'type' and 'skip'\n" +
        "warning(" + ErrorType.INCOMPATIBLE_COMMANDS.code + "): L.g4:12:27: incompatible commands 'type' and 'more'\n" +
        "warning(" + ErrorType.INCOMPATIBLE_COMMANDS.code + "): L.g4:13:33: incompatible commands 'channel' and 'skip'\n" +
        "warning(" + ErrorType.INCOMPATIBLE_COMMANDS.code + "): L.g4:14:33: incompatible commands 'channel' and 'more'\n"
    };

    testErrors(test, false);
  }

  // https://github.com/antlr/antlr4/issues/1409
  @Test
  public void testLabelsForTokensWithMixedTypes() {
    String[] test = {
      """
grammar L;

rule1                                      // Correct (Alternatives)
    : t1=a  #aLabel
    | t1=b  #bLabel
    ;
rule2                         //Incorrect type casting in generated code (RULE_LABEL)
    : t2=a | t2=b
    ;
rule3
    : t3+=a+ b t3+=c+     //Incorrect type casting in generated code (RULE_LIST_LABEL)
    ;
rule4
    : a t4=A b t4=B c                  // Correct (TOKEN_LABEL)
    ;
rule5
    : a t5+=A b t5+=B c                // Correct (TOKEN_LIST_LABEL)
    ;
rule6                     // Correct (https://github.com/antlr/antlr4/issues/1543)
    : t6=a                          #t6_1_Label
    | t6=rule6 b (t61=c)? t62=rule6 #t6_2_Label
    | t6=A     a (t61=B)? t62=A     #t6_3_Label
    ;
rule7                     // Incorrect (https://github.com/antlr/antlr4/issues/1543)
    : a
    | t7=rule7 b (t71=c)? t72=rule7\s
    | t7=A     a (t71=B)? t72=A    \s
    ;
rule8                     // Correct (https://github.com/antlr/antlr4/issues/1543)
    : a
    | t8=rule8 a t8=rule8
    | t8=rule8 b t8=rule8
    ;
a: A;
b: B;
c: C;
A: 'a';
B: 'b';
C: 'c';
""",

      "error(" + ErrorType.LABEL_TYPE_CONFLICT.code + "): L.g4:8:13: label 't2=b' type mismatch with previous definition: t2=a\n" +
        "error(" + ErrorType.LABEL_TYPE_CONFLICT.code + "): L.g4:11:15: label 't3+=c' type mismatch with previous definition: t3+=a\n" +

        "error(" + ErrorType.LABEL_TYPE_CONFLICT.code + "): L.g4:24:0: label 't7' type mismatch with previous definition: TOKEN_LABEL!=RULE_LABEL\n" +
        "error(" + ErrorType.LABEL_TYPE_CONFLICT.code + "): L.g4:24:0: label 't71' type mismatch with previous definition: RULE_LABEL!=TOKEN_LABEL\n" +
        "error(" + ErrorType.LABEL_TYPE_CONFLICT.code + "): L.g4:24:0: label 't72' type mismatch with previous definition: RULE_LABEL!=TOKEN_LABEL\n"
    };

    testErrors(test, false);
  }

  // https://github.com/antlr/antlr4/issues/1543
  @Test
  public void testLabelsForTokensWithMixedTypesLRWithLabels() {
    String[] test = {
      """
grammar L;

expr
    : left=A '+' right=A        #primary
    | left=expr '-' right=expr  #sub
    ;

A: 'a';
B: 'b';
C: 'c';
""",

      ""
    };

    testErrors(test, false);
  }

  // https://github.com/antlr/antlr4/issues/1543
  @Test
  public void testLabelsForTokensWithMixedTypesLRWithoutLabels() {
    String[] test = {
      """
grammar L;

expr
    : left=A '+' right=A
    | left=expr '-' right=expr
    ;

A: 'a';
B: 'b';
C: 'c';
""",

      "error(" + ErrorType.LABEL_TYPE_CONFLICT.code + "): L.g4:3:0: label 'left' type mismatch with previous definition: TOKEN_LABEL!=RULE_LABEL\n" +
        "error(" + ErrorType.LABEL_TYPE_CONFLICT.code + "): L.g4:3:0: label 'right' type mismatch with previous definition: RULE_LABEL!=TOKEN_LABEL\n"
    };

    testErrors(test, false);
  }

  @Test
  @Disabled("надо разбираться")
  public void testCharsCollision() {
    String[] test = {
      """
lexer grammar L;
TOKEN_RANGE:      [aa-f];
TOKEN_RANGE_2:    [A-FD-J];
TOKEN_RANGE_3:    'Z' | 'K'..'R' | 'O'..'V';
TOKEN_RANGE_4:    'g'..'l' | [g-l];
TOKEN_RANGE_WITHOUT_COLLISION: '_' | [a-zA-Z];
TOKEN_RANGE_WITH_ESCAPED_CHARS: [\\n-\\r] | '\\n'..'\\r';""",

      "warning(" + ErrorType.CHARACTERS_COLLISION_IN_SET.code + "): L.g4:2:18: chars ''a'..'f'' used multiple times in set: [aa-f]\n" +
        "warning(" + ErrorType.CHARACTERS_COLLISION_IN_SET.code + "): L.g4:3:18: chars ''D'..'J'' used multiple times in set: [A-FD-J]\n" +
        "warning(" + ErrorType.CHARACTERS_COLLISION_IN_SET.code + "): L.g4:4:13: chars ''O'..'V'' used multiple times in set: 'Z' | 'K'..'R' | 'O'..'V'\n" +
        "warning(" + ErrorType.CHARACTERS_COLLISION_IN_SET.code + "): L.g4::: chars ''g'' used multiple times in set: 'g'..'l'\n" +
        "warning(" + ErrorType.CHARACTERS_COLLISION_IN_SET.code + "): L.g4::: chars ''\\n'' used multiple times in set: '\\n'..'\\r'\n"
    };

    testErrors(test, false);
  }

  @Test
  public void testUnreachableTokens() {
    String[] test = {
      """
lexer grammar Test;
TOKEN1: 'as' 'df' | 'qwer';
TOKEN2: [0-9];
TOKEN3: 'asdf';
TOKEN4: 'q' 'w' 'e' 'r' | A;
TOKEN5: 'aaaa';
TOKEN6: 'asdf';
TOKEN7: 'qwer'+;
TOKEN8: 'a' 'b' | 'b' | 'a' 'b';
fragment
TOKEN9: 'asdf' | 'qwer' | 'qwer';
TOKEN10: '\\r\\n' | '\\r\\n';
TOKEN11: '\\r\\n';

mode MODE1;
TOKEN12: 'asdf';

fragment A: 'A';""",

      "warning(" + ErrorType.TOKEN_UNREACHABLE.code + "): Test.g4:4:0: One of the token 'TOKEN3' values unreachable. 'asdf' is always overlapped by token 'TOKEN1'\n" +
        "warning(" + ErrorType.TOKEN_UNREACHABLE.code + "): Test.g4:5:0: One of the token 'TOKEN4' values unreachable. 'qwer' is always overlapped by token 'TOKEN1'\n" +
        "warning(" + ErrorType.TOKEN_UNREACHABLE.code + "): Test.g4:7:0: One of the token 'TOKEN6' values unreachable. 'asdf' is always overlapped by token 'TOKEN1'\n" +
        "warning(" + ErrorType.TOKEN_UNREACHABLE.code + "): Test.g4:7:0: One of the token 'TOKEN6' values unreachable. 'asdf' is always overlapped by token 'TOKEN3'\n" +
        "warning(" + ErrorType.TOKEN_UNREACHABLE.code + "): Test.g4:9:0: One of the token 'TOKEN8' values unreachable. 'ab' is always overlapped by token 'TOKEN8'\n" +
        "warning(" + ErrorType.TOKEN_UNREACHABLE.code + "): Test.g4:11:0: One of the token 'TOKEN9' values unreachable. 'qwer' is always overlapped by token 'TOKEN9'\n" +
        "warning(" + ErrorType.TOKEN_UNREACHABLE.code + "): Test.g4:12:0: One of the token 'TOKEN10' values unreachable. '\\r\\n' is always overlapped by token 'TOKEN10'\n" +
        "warning(" + ErrorType.TOKEN_UNREACHABLE.code + "): Test.g4:13:0: One of the token 'TOKEN11' values unreachable. '\\r\\n' is always overlapped by token 'TOKEN10'\n" +
        "warning(" + ErrorType.TOKEN_UNREACHABLE.code + "): Test.g4:13:0: One of the token 'TOKEN11' values unreachable. '\\r\\n' is always overlapped by token 'TOKEN10'\n"
    };

    testErrors(test, false);
  }
}
