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

import org.antlr.runtime.Token;
import org.antlr.v4.misc.Utils;
import org.antlr.v4.parse.ANTLRParser;
import org.antlr.v4.runtime.misc.IntervalSet;
import org.antlr.v4.tool.Grammar;
import org.antlr.v4.tool.ast.GrammarAST;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.antlr.v4.TestUtils.assertEquals;

public class TokenPositionOptionsTest extends AbstractBaseTest {
  @Test
  public void testLeftRecursionRewrite() throws Exception {
    Grammar g = new Grammar(
      """
        grammar T;
        s : e ';' ;
        e : e '*' e
          | e '+' e
          | e '.' ID
          | '-' e
          | ID
          ;
        ID : [a-z]+ ;
        """
    );

    String expectedTree =
      "(COMBINED_GRAMMAR T (RULES (RULE s (BLOCK (ALT e ';'))) (RULE e (BLOCK (ALT (BLOCK (ALT {} ('-' (ELEMENT_OPTIONS (= tokenIndex 43))) (e (ELEMENT_OPTIONS (= tokenIndex 45) (= p 2)))) (ALT (ID (ELEMENT_OPTIONS (= tokenIndex 49))))) (* (BLOCK (ALT ({precpred(_ctx, 5)}? (ELEMENT_OPTIONS (= p 5))) ('*' (ELEMENT_OPTIONS (= tokenIndex 21))) (e (ELEMENT_OPTIONS (= tokenIndex 23) (= p 6)))) (ALT ({precpred(_ctx, 4)}? (ELEMENT_OPTIONS (= p 4))) ('+' (ELEMENT_OPTIONS (= tokenIndex 29))) (e (ELEMENT_OPTIONS (= tokenIndex 31) (= p 5)))) (ALT ({precpred(_ctx, 3)}? (ELEMENT_OPTIONS (= p 3))) ('.' (ELEMENT_OPTIONS (= tokenIndex 37))) (ID (ELEMENT_OPTIONS (= tokenIndex 39)))))))))))";
    assertEquals(expectedTree, g.ast.toStringTree());

    String expectedElementTokens =
      """
        [@5,11:11='s',<57>,2:0]
        [@9,15:15='e',<57>,2:4]
        [@11,17:19='';'',<62>,2:6]
        [@15,23:23='e',<57>,3:0]
        [@43,64:66=''-'',<62>,6:4]
        [@45,68:68='e',<57>,6:8]
        [@49,74:75='ID',<66>,7:4]
        [@21,29:31=''*'',<62>,3:6]
        [@23,33:33='e',<57>,3:10]
        [@29,41:43=''+'',<62>,4:6]
        [@31,45:45='e',<57>,4:10]
        [@37,53:55=''.'',<62>,5:6]
        [@39,57:58='ID',<66>,5:10]""";

    IntervalSet types =
      new IntervalSet(ANTLRParser.TOKEN_REF,
        ANTLRParser.STRING_LITERAL,
        ANTLRParser.RULE_REF);
    List<GrammarAST> nodes = g.ast.getNodesWithTypePreorderDFS(types);
    List<Token> tokens = new ArrayList<>();
    for (GrammarAST node : nodes) {
      tokens.add(node.getToken());
    }
    assertEquals(expectedElementTokens, Utils.join(tokens.toArray(), "\n"));
  }

  @Test
  public void testLeftRecursionWithLabels() throws Exception {
    Grammar g = new Grammar(
      """
        grammar T;
        s : e ';' ;
        e : e '*' x=e
          | e '+' e
          | e '.' y=ID
          | '-' e
          | ID
          ;
        ID : [a-z]+ ;
        """
    );

    String expectedTree =
      "(COMBINED_GRAMMAR T (RULES (RULE s (BLOCK (ALT e ';'))) (RULE e (BLOCK (ALT (BLOCK (ALT {} ('-' (ELEMENT_OPTIONS (= tokenIndex 47))) (e (ELEMENT_OPTIONS (= tokenIndex 49) (= p 2)))) (ALT (ID (ELEMENT_OPTIONS (= tokenIndex 53))))) (* (BLOCK (ALT ({precpred(_ctx, 5)}? (ELEMENT_OPTIONS (= p 5))) ('*' (ELEMENT_OPTIONS (= tokenIndex 21))) (= x (e (ELEMENT_OPTIONS (= tokenIndex 25) (= p 6))))) (ALT ({precpred(_ctx, 4)}? (ELEMENT_OPTIONS (= p 4))) ('+' (ELEMENT_OPTIONS (= tokenIndex 31))) (e (ELEMENT_OPTIONS (= tokenIndex 33) (= p 5)))) (ALT ({precpred(_ctx, 3)}? (ELEMENT_OPTIONS (= p 3))) ('.' (ELEMENT_OPTIONS (= tokenIndex 39))) (= y (ID (ELEMENT_OPTIONS (= tokenIndex 43))))))))))))";
    assertEquals(expectedTree, g.ast.toStringTree());

    String expectedElementTokens =
      """
        [@5,11:11='s',<57>,2:0]
        [@9,15:15='e',<57>,2:4]
        [@11,17:19='';'',<62>,2:6]
        [@15,23:23='e',<57>,3:0]
        [@47,68:70=''-'',<62>,6:4]
        [@49,72:72='e',<57>,6:8]
        [@53,78:79='ID',<66>,7:4]
        [@21,29:31=''*'',<62>,3:6]
        [@25,35:35='e',<57>,3:12]
        [@31,43:45=''+'',<62>,4:6]
        [@33,47:47='e',<57>,4:10]
        [@39,55:57=''.'',<62>,5:6]
        [@43,61:62='ID',<66>,5:12]""";

    IntervalSet types =
      new IntervalSet(ANTLRParser.TOKEN_REF,
        ANTLRParser.STRING_LITERAL,
        ANTLRParser.RULE_REF);
    List<GrammarAST> nodes = g.ast.getNodesWithTypePreorderDFS(types);
    List<Token> tokens = new ArrayList<>();
    for (GrammarAST node : nodes) {
      tokens.add(node.getToken());
    }
    assertEquals(expectedElementTokens, Utils.join(tokens.toArray(), "\n"));
  }

  @Test
  public void testLeftRecursionWithSet() throws Exception {
    Grammar g = new Grammar(
      """
        grammar T;
        s : e ';' ;
        e : e op=('*'|'/') e
          | e '+' e
          | e '.' ID
          | '-' e
          | ID
          ;
        ID : [a-z]+ ;
        """
    );

    String expectedTree =
      "(COMBINED_GRAMMAR T (RULES (RULE s (BLOCK (ALT e ';'))) (RULE e (BLOCK (ALT (BLOCK (ALT {} ('-' (ELEMENT_OPTIONS (= tokenIndex 49))) (e (ELEMENT_OPTIONS (= tokenIndex 51) (= p 2)))) (ALT (ID (ELEMENT_OPTIONS (= tokenIndex 55))))) (* (BLOCK (ALT ({precpred(_ctx, 5)}? (ELEMENT_OPTIONS (= p 5))) (= op (SET ('*' (ELEMENT_OPTIONS (= tokenIndex 24))) ('/' (ELEMENT_OPTIONS (= tokenIndex 26))))) (e (ELEMENT_OPTIONS (= tokenIndex 29) (= p 6)))) (ALT ({precpred(_ctx, 4)}? (ELEMENT_OPTIONS (= p 4))) ('+' (ELEMENT_OPTIONS (= tokenIndex 35))) (e (ELEMENT_OPTIONS (= tokenIndex 37) (= p 5)))) (ALT ({precpred(_ctx, 3)}? (ELEMENT_OPTIONS (= p 3))) ('.' (ELEMENT_OPTIONS (= tokenIndex 43))) (ID (ELEMENT_OPTIONS (= tokenIndex 45)))))))))))";
    assertEquals(expectedTree, g.ast.toStringTree());

    String expectedElementTokens =
      """
        [@5,11:11='s',<57>,2:0]
        [@9,15:15='e',<57>,2:4]
        [@11,17:19='';'',<62>,2:6]
        [@15,23:23='e',<57>,3:0]
        [@49,73:75=''-'',<62>,6:4]
        [@51,77:77='e',<57>,6:8]
        [@55,83:84='ID',<66>,7:4]
        [@24,33:35=''*'',<62>,3:10]
        [@26,37:39=''/'',<62>,3:14]
        [@29,42:42='e',<57>,3:19]
        [@35,50:52=''+'',<62>,4:6]
        [@37,54:54='e',<57>,4:10]
        [@43,62:64=''.'',<62>,5:6]
        [@45,66:67='ID',<66>,5:10]""";

    IntervalSet types =
      new IntervalSet(ANTLRParser.TOKEN_REF,
        ANTLRParser.STRING_LITERAL,
        ANTLRParser.RULE_REF);
    List<GrammarAST> nodes = g.ast.getNodesWithTypePreorderDFS(types);
    List<Token> tokens = new ArrayList<>();
    for (GrammarAST node : nodes) {
      tokens.add(node.getToken());
    }
    assertEquals(expectedElementTokens, Utils.join(tokens.toArray(), "\n"));
  }

}
