/*
 * This file is a part of ANTLR.
 *
 * Copyright (c) 2012-2025 The ANTLR Project. All rights reserved.
 * Copyright (c) 2025 Valery Maximov <maximovvalery@gmail.com> and contributors
 *
 * Use of this file is governed by the BSD-3-Clause license that
 * can be found in the LICENSE.txt file in the project root.
 */
// $ANTLR 3.5.3 org/antlr/v4/parse/ANTLRParser.g 2025-02-05 09:25:53

/*
 * Copyright (c) 2012 The ANTLR Project. All rights reserved.
 * Use of this file is governed by the BSD-3-Clause license that
 * can be found in the LICENSE.txt file in the project root.
 */
package org.antlr.v4.parse;

import org.antlr.runtime.BitSet;
import org.antlr.runtime.EarlyExitException;
import org.antlr.runtime.NoViableAltException;
import org.antlr.runtime.Parser;
import org.antlr.runtime.ParserRuleReturnScope;
import org.antlr.runtime.RecognitionException;
import org.antlr.runtime.RecognizerSharedState;
import org.antlr.runtime.RuleReturnScope;
import org.antlr.runtime.Token;
import org.antlr.runtime.TokenStream;
import org.antlr.runtime.tree.CommonTreeAdaptor;
import org.antlr.runtime.tree.RewriteEarlyExitException;
import org.antlr.runtime.tree.RewriteRuleSubtreeStream;
import org.antlr.runtime.tree.RewriteRuleTokenStream;
import org.antlr.runtime.tree.TreeAdaptor;
import org.antlr.v4.tool.ErrorType;
import org.antlr.v4.tool.Grammar;
import org.antlr.v4.tool.ast.ActionAST;
import org.antlr.v4.tool.ast.AltAST;
import org.antlr.v4.tool.ast.BlockAST;
import org.antlr.v4.tool.ast.GrammarAST;
import org.antlr.v4.tool.ast.GrammarRootAST;
import org.antlr.v4.tool.ast.NotAST;
import org.antlr.v4.tool.ast.OptionalBlockAST;
import org.antlr.v4.tool.ast.PlusBlockAST;
import org.antlr.v4.tool.ast.PredAST;
import org.antlr.v4.tool.ast.RangeAST;
import org.antlr.v4.tool.ast.RuleAST;
import org.antlr.v4.tool.ast.RuleRefAST;
import org.antlr.v4.tool.ast.SetAST;
import org.antlr.v4.tool.ast.StarBlockAST;
import org.antlr.v4.tool.ast.TerminalAST;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;


/**
 * The definitive ANTLR v3 grammar to parse ANTLR v4 grammars.
 * The grammar builds ASTs that are sniffed by subsequent stages.
 */
@SuppressWarnings("all")
public class ANTLRParser extends Parser {
  public static final String[] tokenNames = new String[]{
    "<invalid>", "<EOR>", "<DOWN>", "<UP>", "ACTION", "ACTION_CHAR_LITERAL",
    "ACTION_ESC", "ACTION_STRING_LITERAL", "ARG_ACTION", "ARG_OR_CHARSET",
    "ASSIGN", "AT", "CATCH", "CHANNELS", "COLON", "COLONCOLON", "COMMA", "COMMENT",
    "DOC_COMMENT", "DOLLAR", "DOT", "ERRCHAR", "ESC_SEQ", "FINALLY", "FRAGMENT",
    "GRAMMAR", "GT", "HEX_DIGIT", "ID", "IMPORT", "INT", "LEXER", "LEXER_CHAR_SET",
    "LOCALS", "LPAREN", "LT", "MODE", "NESTED_ACTION", "NLCHARS", "NOT", "NameChar",
    "NameStartChar", "OPTIONS", "OR", "PARSER", "PLUS", "PLUS_ASSIGN", "POUND",
    "PRIVATE", "PROTECTED", "PUBLIC", "QUESTION", "RANGE", "RARROW", "RBRACE",
    "RETURNS", "RPAREN", "RULE_REF", "SEMI", "SEMPRED", "SRC", "STAR", "STRING_LITERAL",
    "SYNPRED", "THROWS", "TOKENS_SPEC", "TOKEN_REF", "TREE_GRAMMAR", "UNICODE_ESC",
    "UNICODE_EXTENDED_ESC", "UnicodeBOM", "WS", "WSCHARS", "WSNLCHARS", "ALT",
    "ALTLIST", "ARG", "ARGLIST", "BLOCK", "CHAR_RANGE", "CLOSURE", "COMBINED",
    "ELEMENT_OPTIONS", "EPSILON", "INITACTION", "LABEL", "LEXER_ACTION_CALL",
    "LEXER_ALT_ACTION", "LIST", "OPTIONAL", "POSITIVE_CLOSURE", "PREC_RULE",
    "RESULT", "RET", "RULE", "RULEACTIONS", "RULEMODIFIERS", "RULES", "SET",
    "TEMPLATE", "WILDCARD"
  };
  public static final int EOF = -1;
  public static final int ACTION = 4;
  public static final int ACTION_CHAR_LITERAL = 5;
  public static final int ACTION_ESC = 6;
  public static final int ACTION_STRING_LITERAL = 7;
  public static final int ARG_ACTION = 8;
  public static final int ARG_OR_CHARSET = 9;
  public static final int ASSIGN = 10;
  public static final int AT = 11;
  public static final int CATCH = 12;
  public static final int CHANNELS = 13;
  public static final int COLON = 14;
  public static final int COLONCOLON = 15;
  public static final int COMMA = 16;
  public static final int COMMENT = 17;
  public static final int DOC_COMMENT = 18;
  public static final int DOLLAR = 19;
  public static final int DOT = 20;
  public static final int ERRCHAR = 21;
  public static final int ESC_SEQ = 22;
  public static final int FINALLY = 23;
  public static final int FRAGMENT = 24;
  public static final int GRAMMAR = 25;
  public static final int GT = 26;
  public static final int HEX_DIGIT = 27;
  public static final int ID = 28;
  public static final int IMPORT = 29;
  public static final int INT = 30;
  public static final int LEXER = 31;
  public static final int LEXER_CHAR_SET = 32;
  public static final int LOCALS = 33;
  public static final int LPAREN = 34;
  public static final int LT = 35;
  public static final int MODE = 36;
  public static final int NESTED_ACTION = 37;
  public static final int NLCHARS = 38;
  public static final int NOT = 39;
  public static final int NameChar = 40;
  public static final int NameStartChar = 41;
  public static final int OPTIONS = 42;
  public static final int OR = 43;
  public static final int PARSER = 44;
  public static final int PLUS = 45;
  public static final int PLUS_ASSIGN = 46;
  public static final int POUND = 47;
  public static final int PRIVATE = 48;
  public static final int PROTECTED = 49;
  public static final int PUBLIC = 50;
  public static final int QUESTION = 51;
  public static final int RANGE = 52;
  public static final int RARROW = 53;
  public static final int RBRACE = 54;
  public static final int RETURNS = 55;
  public static final int RPAREN = 56;
  public static final int RULE_REF = 57;
  public static final int SEMI = 58;
  public static final int SEMPRED = 59;
  public static final int SRC = 60;
  public static final int STAR = 61;
  public static final int STRING_LITERAL = 62;
  public static final int SYNPRED = 63;
  public static final int THROWS = 64;
  public static final int TOKENS_SPEC = 65;
  public static final int TOKEN_REF = 66;
  public static final int TREE_GRAMMAR = 67;
  public static final int UNICODE_ESC = 68;
  public static final int UNICODE_EXTENDED_ESC = 69;
  public static final int UnicodeBOM = 70;
  public static final int WS = 71;
  public static final int WSCHARS = 72;
  public static final int WSNLCHARS = 73;
  public static final int ALT = 74;
  public static final int ALTLIST = 75;
  public static final int ARG = 76;
  public static final int ARGLIST = 77;
  public static final int BLOCK = 78;
  public static final int CHAR_RANGE = 79;
  public static final int CLOSURE = 80;
  public static final int COMBINED = 81;
  public static final int ELEMENT_OPTIONS = 82;
  public static final int EPSILON = 83;
  public static final int INITACTION = 84;
  public static final int LABEL = 85;
  public static final int LEXER_ACTION_CALL = 86;
  public static final int LEXER_ALT_ACTION = 87;
  public static final int LIST = 88;
  public static final int OPTIONAL = 89;
  public static final int POSITIVE_CLOSURE = 90;
  public static final int PREC_RULE = 91;
  public static final int RESULT = 92;
  public static final int RET = 93;
  public static final int RULE = 94;
  public static final int RULEACTIONS = 95;
  public static final int RULEMODIFIERS = 96;
  public static final int RULES = 97;
  public static final int SET = 98;
  public static final int TEMPLATE = 99;
  public static final int WILDCARD = 100;

  // delegates
  public Parser[] getDelegates() {
    return new Parser[]{};
  }

  // delegators


  public ANTLRParser(TokenStream input) {
    this(input, new RecognizerSharedState());
  }

  public ANTLRParser(TokenStream input, RecognizerSharedState state) {
    super(input, state);
  }

  protected TreeAdaptor adaptor = new CommonTreeAdaptor();

  public void setTreeAdaptor(TreeAdaptor adaptor) {
    this.adaptor = adaptor;
  }

  public TreeAdaptor getTreeAdaptor() {
    return adaptor;
  }

  @Override
  public String[] getTokenNames() {
    return ANTLRParser.tokenNames;
  }

  @Override
  public String getGrammarFileName() {
    return "org/antlr/v4/parse/ANTLRParser.g";
  }


  Deque<String> paraphrases = new ArrayDeque<String>();

  public void grammarError(ErrorType etype, org.antlr.runtime.Token token, Object... args) {
  }


  public static class grammarSpec_return extends ParserRuleReturnScope {
    GrammarAST tree;

    @Override
    public GrammarAST getTree() {
      return tree;
    }
  }

  ;


  // $ANTLR start "grammarSpec"
  // org/antlr/v4/parse/ANTLRParser.g:100:1: grammarSpec : grammarType id SEMI sync ( prequelConstruct sync )* rules ( modeSpec )* EOF -> ^( grammarType id ( prequelConstruct )* rules ( modeSpec )* ) ;
  public final ANTLRParser.grammarSpec_return grammarSpec() throws RecognitionException {
    ANTLRParser.grammarSpec_return retval = new ANTLRParser.grammarSpec_return();
    retval.start = input.LT(1);

    GrammarAST root_0 = null;

    Token SEMI3 = null;
    Token EOF9 = null;
    ParserRuleReturnScope grammarType1 = null;
    ParserRuleReturnScope id2 = null;
    ParserRuleReturnScope sync4 = null;
    ParserRuleReturnScope prequelConstruct5 = null;
    ParserRuleReturnScope sync6 = null;
    ParserRuleReturnScope rules7 = null;
    ParserRuleReturnScope modeSpec8 = null;

    GrammarAST SEMI3_tree = null;
    GrammarAST EOF9_tree = null;
    RewriteRuleTokenStream stream_SEMI = new RewriteRuleTokenStream(adaptor, "token SEMI");
    RewriteRuleTokenStream stream_EOF = new RewriteRuleTokenStream(adaptor, "token EOF");
    RewriteRuleSubtreeStream stream_modeSpec = new RewriteRuleSubtreeStream(adaptor, "rule modeSpec");
    RewriteRuleSubtreeStream stream_grammarType = new RewriteRuleSubtreeStream(adaptor, "rule grammarType");
    RewriteRuleSubtreeStream stream_rules = new RewriteRuleSubtreeStream(adaptor, "rule rules");
    RewriteRuleSubtreeStream stream_id = new RewriteRuleSubtreeStream(adaptor, "rule id");
    RewriteRuleSubtreeStream stream_prequelConstruct = new RewriteRuleSubtreeStream(adaptor, "rule prequelConstruct");
    RewriteRuleSubtreeStream stream_sync = new RewriteRuleSubtreeStream(adaptor, "rule sync");

    try {
      // org/antlr/v4/parse/ANTLRParser.g:107:5: ( grammarType id SEMI sync ( prequelConstruct sync )* rules ( modeSpec )* EOF -> ^( grammarType id ( prequelConstruct )* rules ( modeSpec )* ) )
      // org/antlr/v4/parse/ANTLRParser.g:110:7: grammarType id SEMI sync ( prequelConstruct sync )* rules ( modeSpec )* EOF
      {
        pushFollow(FOLLOW_grammarType_in_grammarSpec396);
        grammarType1 = grammarType();
        state._fsp--;

        stream_grammarType.add(grammarType1.getTree());
        pushFollow(FOLLOW_id_in_grammarSpec398);
        id2 = id();
        state._fsp--;

        stream_id.add(id2.getTree());
        SEMI3 = (Token) match(input, SEMI, FOLLOW_SEMI_in_grammarSpec400);
        stream_SEMI.add(SEMI3);

        pushFollow(FOLLOW_sync_in_grammarSpec438);
        sync4 = sync();
        state._fsp--;

        stream_sync.add(sync4.getTree());
        // org/antlr/v4/parse/ANTLRParser.g:123:12: ( prequelConstruct sync )*
        loop1:
        while (true) {
          int alt1 = 2;
          int LA1_0 = input.LA(1);
          if ((LA1_0 == AT || LA1_0 == CHANNELS || LA1_0 == IMPORT || LA1_0 == OPTIONS || LA1_0 == TOKENS_SPEC)) {
            alt1 = 1;
          }

          switch (alt1) {
            case 1:
              // org/antlr/v4/parse/ANTLRParser.g:123:14: prequelConstruct sync
            {
              pushFollow(FOLLOW_prequelConstruct_in_grammarSpec442);
              prequelConstruct5 = prequelConstruct();
              state._fsp--;

              stream_prequelConstruct.add(prequelConstruct5.getTree());
              pushFollow(FOLLOW_sync_in_grammarSpec444);
              sync6 = sync();
              state._fsp--;

              stream_sync.add(sync6.getTree());
            }
            break;

            default:
              break loop1;
          }
        }

        pushFollow(FOLLOW_rules_in_grammarSpec469);
        rules7 = rules();
        state._fsp--;

        stream_rules.add(rules7.getTree());
        // org/antlr/v4/parse/ANTLRParser.g:131:4: ( modeSpec )*
        loop2:
        while (true) {
          int alt2 = 2;
          int LA2_0 = input.LA(1);
          if ((LA2_0 == MODE)) {
            alt2 = 1;
          }

          switch (alt2) {
            case 1:
              // org/antlr/v4/parse/ANTLRParser.g:131:4: modeSpec
            {
              pushFollow(FOLLOW_modeSpec_in_grammarSpec475);
              modeSpec8 = modeSpec();
              state._fsp--;

              stream_modeSpec.add(modeSpec8.getTree());
            }
            break;

            default:
              break loop2;
          }
        }

        EOF9 = (Token) match(input, EOF, FOLLOW_EOF_in_grammarSpec513);
        stream_EOF.add(EOF9);


        // AST REWRITE
        // elements: grammarType, prequelConstruct, rules, modeSpec, id
        // token labels:
        // rule labels: retval
        // token list labels:
        // rule list labels:
        // wildcard labels:
        retval.tree = root_0;
        RewriteRuleSubtreeStream stream_retval = new RewriteRuleSubtreeStream(adaptor, "rule retval", retval != null ? retval.getTree() : null);

        root_0 = (GrammarAST) adaptor.nil();
        // 144:7: -> ^( grammarType id ( prequelConstruct )* rules ( modeSpec )* )
        {
          // org/antlr/v4/parse/ANTLRParser.g:144:10: ^( grammarType id ( prequelConstruct )* rules ( modeSpec )* )
          {
            GrammarAST root_1 = (GrammarAST) adaptor.nil();
            root_1 = (GrammarAST) adaptor.becomeRoot(stream_grammarType.nextNode(), root_1);
            adaptor.addChild(root_1, stream_id.nextTree());
            // org/antlr/v4/parse/ANTLRParser.g:146:14: ( prequelConstruct )*
            while (stream_prequelConstruct.hasNext()) {
              adaptor.addChild(root_1, stream_prequelConstruct.nextTree());
            }
            stream_prequelConstruct.reset();

            adaptor.addChild(root_1, stream_rules.nextTree());
            // org/antlr/v4/parse/ANTLRParser.g:148:14: ( modeSpec )*
            while (stream_modeSpec.hasNext()) {
              adaptor.addChild(root_1, stream_modeSpec.nextTree());
            }
            stream_modeSpec.reset();

            adaptor.addChild(root_0, root_1);
          }

        }


        retval.tree = root_0;

      }

      retval.stop = input.LT(-1);

      retval.tree = (GrammarAST) adaptor.rulePostProcessing(root_0);
      adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);


      GrammarAST options = (GrammarAST) retval.tree.getFirstChildWithType(ANTLRParser.OPTIONS);
      if (options != null) {
        Grammar.setNodeOptions(retval.tree, options);
      }

    } catch (RecognitionException re) {
      reportError(re);
      recover(input, re);
      retval.tree = (GrammarAST) adaptor.errorNode(input, retval.start, input.LT(-1), re);
    } finally {
      // do for sure before leaving
    }
    return retval;
  }
  // $ANTLR end "grammarSpec"


  public static class grammarType_return extends ParserRuleReturnScope {
    GrammarAST tree;

    @Override
    public GrammarAST getTree() {
      return tree;
    }
  }

  ;


  // $ANTLR start "grammarType"
  // org/antlr/v4/parse/ANTLRParser.g:152:1: grammarType : (t= LEXER g= GRAMMAR -> GRAMMAR[$g, \"LEXER_GRAMMAR\", getTokenStream()] |t= PARSER g= GRAMMAR -> GRAMMAR[$g, \"PARSER_GRAMMAR\", getTokenStream()] |g= GRAMMAR -> GRAMMAR[$g, \"COMBINED_GRAMMAR\", getTokenStream()] |tg= TREE_GRAMMAR ) ;
  public final ANTLRParser.grammarType_return grammarType() throws RecognitionException {
    ANTLRParser.grammarType_return retval = new ANTLRParser.grammarType_return();
    retval.start = input.LT(1);

    GrammarAST root_0 = null;

    Token t = null;
    Token g = null;
    Token tg = null;

    GrammarAST t_tree = null;
    GrammarAST g_tree = null;
    GrammarAST tg_tree = null;
    RewriteRuleTokenStream stream_PARSER = new RewriteRuleTokenStream(adaptor, "token PARSER");
    RewriteRuleTokenStream stream_LEXER = new RewriteRuleTokenStream(adaptor, "token LEXER");
    RewriteRuleTokenStream stream_GRAMMAR = new RewriteRuleTokenStream(adaptor, "token GRAMMAR");
    RewriteRuleTokenStream stream_TREE_GRAMMAR = new RewriteRuleTokenStream(adaptor, "token TREE_GRAMMAR");

    try {
      // org/antlr/v4/parse/ANTLRParser.g:158:5: ( (t= LEXER g= GRAMMAR -> GRAMMAR[$g, \"LEXER_GRAMMAR\", getTokenStream()] |t= PARSER g= GRAMMAR -> GRAMMAR[$g, \"PARSER_GRAMMAR\", getTokenStream()] |g= GRAMMAR -> GRAMMAR[$g, \"COMBINED_GRAMMAR\", getTokenStream()] |tg= TREE_GRAMMAR ) )
      // org/antlr/v4/parse/ANTLRParser.g:158:7: (t= LEXER g= GRAMMAR -> GRAMMAR[$g, \"LEXER_GRAMMAR\", getTokenStream()] |t= PARSER g= GRAMMAR -> GRAMMAR[$g, \"PARSER_GRAMMAR\", getTokenStream()] |g= GRAMMAR -> GRAMMAR[$g, \"COMBINED_GRAMMAR\", getTokenStream()] |tg= TREE_GRAMMAR )
      {
        // org/antlr/v4/parse/ANTLRParser.g:158:7: (t= LEXER g= GRAMMAR -> GRAMMAR[$g, \"LEXER_GRAMMAR\", getTokenStream()] |t= PARSER g= GRAMMAR -> GRAMMAR[$g, \"PARSER_GRAMMAR\", getTokenStream()] |g= GRAMMAR -> GRAMMAR[$g, \"COMBINED_GRAMMAR\", getTokenStream()] |tg= TREE_GRAMMAR )
        int alt3 = 4;
        switch (input.LA(1)) {
          case LEXER: {
            alt3 = 1;
          }
          break;
          case PARSER: {
            alt3 = 2;
          }
          break;
          case GRAMMAR: {
            alt3 = 3;
          }
          break;
          case TREE_GRAMMAR: {
            alt3 = 4;
          }
          break;
          default:
            NoViableAltException nvae =
              new NoViableAltException("", 3, 0, input);
            throw nvae;
        }
        switch (alt3) {
          case 1:
            // org/antlr/v4/parse/ANTLRParser.g:158:9: t= LEXER g= GRAMMAR
          {
            t = (Token) match(input, LEXER, FOLLOW_LEXER_in_grammarType683);
            stream_LEXER.add(t);

            g = (Token) match(input, GRAMMAR, FOLLOW_GRAMMAR_in_grammarType687);
            stream_GRAMMAR.add(g);


            // AST REWRITE
            // elements: GRAMMAR
            // token labels:
            // rule labels: retval
            // token list labels:
            // rule list labels:
            // wildcard labels:
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval = new RewriteRuleSubtreeStream(adaptor, "rule retval", retval != null ? retval.getTree() : null);

            root_0 = (GrammarAST) adaptor.nil();
            // 158:28: -> GRAMMAR[$g, \"LEXER_GRAMMAR\", getTokenStream()]
            {
              adaptor.addChild(root_0, new GrammarRootAST(GRAMMAR, g, "LEXER_GRAMMAR", getTokenStream()));
            }


            retval.tree = root_0;

          }
          break;
          case 2:
            // org/antlr/v4/parse/ANTLRParser.g:160:6: t= PARSER g= GRAMMAR
          {
            t = (Token) match(input, PARSER, FOLLOW_PARSER_in_grammarType710);
            stream_PARSER.add(t);

            g = (Token) match(input, GRAMMAR, FOLLOW_GRAMMAR_in_grammarType714);
            stream_GRAMMAR.add(g);


            // AST REWRITE
            // elements: GRAMMAR
            // token labels:
            // rule labels: retval
            // token list labels:
            // rule list labels:
            // wildcard labels:
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval = new RewriteRuleSubtreeStream(adaptor, "rule retval", retval != null ? retval.getTree() : null);

            root_0 = (GrammarAST) adaptor.nil();
            // 160:25: -> GRAMMAR[$g, \"PARSER_GRAMMAR\", getTokenStream()]
            {
              adaptor.addChild(root_0, new GrammarRootAST(GRAMMAR, g, "PARSER_GRAMMAR", getTokenStream()));
            }


            retval.tree = root_0;

          }
          break;
          case 3:
            // org/antlr/v4/parse/ANTLRParser.g:163:6: g= GRAMMAR
          {
            g = (Token) match(input, GRAMMAR, FOLLOW_GRAMMAR_in_grammarType735);
            stream_GRAMMAR.add(g);


            // AST REWRITE
            // elements: GRAMMAR
            // token labels:
            // rule labels: retval
            // token list labels:
            // rule list labels:
            // wildcard labels:
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval = new RewriteRuleSubtreeStream(adaptor, "rule retval", retval != null ? retval.getTree() : null);

            root_0 = (GrammarAST) adaptor.nil();
            // 163:25: -> GRAMMAR[$g, \"COMBINED_GRAMMAR\", getTokenStream()]
            {
              adaptor.addChild(root_0, new GrammarRootAST(GRAMMAR, g, "COMBINED_GRAMMAR", getTokenStream()));
            }


            retval.tree = root_0;

          }
          break;
          case 4:
            // org/antlr/v4/parse/ANTLRParser.g:164:7: tg= TREE_GRAMMAR
          {
            tg = (Token) match(input, TREE_GRAMMAR, FOLLOW_TREE_GRAMMAR_in_grammarType762);
            stream_TREE_GRAMMAR.add(tg);

          }
          break;

        }

      }

      retval.stop = input.LT(-1);

      retval.tree = (GrammarAST) adaptor.rulePostProcessing(root_0);
      adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);


      if (tg != null) throw new v3TreeGrammarException(tg);
      if (t != null) ((GrammarRootAST) retval.tree).grammarType = (t != null ? t.getType() : 0);
      else ((GrammarRootAST) retval.tree).grammarType = COMBINED;

    } catch (RecognitionException re) {
      reportError(re);
      recover(input, re);
      retval.tree = (GrammarAST) adaptor.errorNode(input, retval.start, input.LT(-1), re);
    } finally {
      // do for sure before leaving
    }
    return retval;
  }
  // $ANTLR end "grammarType"


  public static class prequelConstruct_return extends ParserRuleReturnScope {
    GrammarAST tree;

    @Override
    public GrammarAST getTree() {
      return tree;
    }
  }

  ;


  // $ANTLR start "prequelConstruct"
  // org/antlr/v4/parse/ANTLRParser.g:172:1: prequelConstruct : ( optionsSpec | delegateGrammars | tokensSpec | channelsSpec | action );
  public final ANTLRParser.prequelConstruct_return prequelConstruct() throws RecognitionException {
    ANTLRParser.prequelConstruct_return retval = new ANTLRParser.prequelConstruct_return();
    retval.start = input.LT(1);

    GrammarAST root_0 = null;

    ParserRuleReturnScope optionsSpec10 = null;
    ParserRuleReturnScope delegateGrammars11 = null;
    ParserRuleReturnScope tokensSpec12 = null;
    ParserRuleReturnScope channelsSpec13 = null;
    ParserRuleReturnScope action14 = null;


    try {
      // org/antlr/v4/parse/ANTLRParser.g:173:2: ( optionsSpec | delegateGrammars | tokensSpec | channelsSpec | action )
      int alt4 = 5;
      switch (input.LA(1)) {
        case OPTIONS: {
          alt4 = 1;
        }
        break;
        case IMPORT: {
          alt4 = 2;
        }
        break;
        case TOKENS_SPEC: {
          alt4 = 3;
        }
        break;
        case CHANNELS: {
          alt4 = 4;
        }
        break;
        case AT: {
          alt4 = 5;
        }
        break;
        default:
          NoViableAltException nvae =
            new NoViableAltException("", 4, 0, input);
          throw nvae;
      }
      switch (alt4) {
        case 1:
          // org/antlr/v4/parse/ANTLRParser.g:174:4: optionsSpec
        {
          root_0 = (GrammarAST) adaptor.nil();


          pushFollow(FOLLOW_optionsSpec_in_prequelConstruct788);
          optionsSpec10 = optionsSpec();
          state._fsp--;

          adaptor.addChild(root_0, optionsSpec10.getTree());

        }
        break;
        case 2:
          // org/antlr/v4/parse/ANTLRParser.g:178:7: delegateGrammars
        {
          root_0 = (GrammarAST) adaptor.nil();


          pushFollow(FOLLOW_delegateGrammars_in_prequelConstruct811);
          delegateGrammars11 = delegateGrammars();
          state._fsp--;

          adaptor.addChild(root_0, delegateGrammars11.getTree());

        }
        break;
        case 3:
          // org/antlr/v4/parse/ANTLRParser.g:185:7: tokensSpec
        {
          root_0 = (GrammarAST) adaptor.nil();


          pushFollow(FOLLOW_tokensSpec_in_prequelConstruct855);
          tokensSpec12 = tokensSpec();
          state._fsp--;

          adaptor.addChild(root_0, tokensSpec12.getTree());

        }
        break;
        case 4:
          // org/antlr/v4/parse/ANTLRParser.g:188:4: channelsSpec
        {
          root_0 = (GrammarAST) adaptor.nil();


          pushFollow(FOLLOW_channelsSpec_in_prequelConstruct865);
          channelsSpec13 = channelsSpec();
          state._fsp--;

          adaptor.addChild(root_0, channelsSpec13.getTree());

        }
        break;
        case 5:
          // org/antlr/v4/parse/ANTLRParser.g:194:7: action
        {
          root_0 = (GrammarAST) adaptor.nil();


          pushFollow(FOLLOW_action_in_prequelConstruct902);
          action14 = action();
          state._fsp--;

          adaptor.addChild(root_0, action14.getTree());

        }
        break;

      }
      retval.stop = input.LT(-1);

      retval.tree = (GrammarAST) adaptor.rulePostProcessing(root_0);
      adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

    } catch (RecognitionException re) {
      reportError(re);
      recover(input, re);
      retval.tree = (GrammarAST) adaptor.errorNode(input, retval.start, input.LT(-1), re);
    } finally {
      // do for sure before leaving
    }
    return retval;
  }
  // $ANTLR end "prequelConstruct"


  public static class optionsSpec_return extends ParserRuleReturnScope {
    GrammarAST tree;

    @Override
    public GrammarAST getTree() {
      return tree;
    }
  }

  ;


  // $ANTLR start "optionsSpec"
  // org/antlr/v4/parse/ANTLRParser.g:198:1: optionsSpec : OPTIONS ( option SEMI )* RBRACE -> ^( OPTIONS[$OPTIONS, \"OPTIONS\"] ( option )* ) ;
  public final ANTLRParser.optionsSpec_return optionsSpec() throws RecognitionException {
    ANTLRParser.optionsSpec_return retval = new ANTLRParser.optionsSpec_return();
    retval.start = input.LT(1);

    GrammarAST root_0 = null;

    Token OPTIONS15 = null;
    Token SEMI17 = null;
    Token RBRACE18 = null;
    ParserRuleReturnScope option16 = null;

    GrammarAST OPTIONS15_tree = null;
    GrammarAST SEMI17_tree = null;
    GrammarAST RBRACE18_tree = null;
    RewriteRuleTokenStream stream_RBRACE = new RewriteRuleTokenStream(adaptor, "token RBRACE");
    RewriteRuleTokenStream stream_SEMI = new RewriteRuleTokenStream(adaptor, "token SEMI");
    RewriteRuleTokenStream stream_OPTIONS = new RewriteRuleTokenStream(adaptor, "token OPTIONS");
    RewriteRuleSubtreeStream stream_option = new RewriteRuleSubtreeStream(adaptor, "rule option");

    try {
      // org/antlr/v4/parse/ANTLRParser.g:199:2: ( OPTIONS ( option SEMI )* RBRACE -> ^( OPTIONS[$OPTIONS, \"OPTIONS\"] ( option )* ) )
      // org/antlr/v4/parse/ANTLRParser.g:199:4: OPTIONS ( option SEMI )* RBRACE
      {
        OPTIONS15 = (Token) match(input, OPTIONS, FOLLOW_OPTIONS_in_optionsSpec917);
        stream_OPTIONS.add(OPTIONS15);

        // org/antlr/v4/parse/ANTLRParser.g:199:12: ( option SEMI )*
        loop5:
        while (true) {
          int alt5 = 2;
          int LA5_0 = input.LA(1);
          if ((LA5_0 == RULE_REF || LA5_0 == TOKEN_REF)) {
            alt5 = 1;
          }

          switch (alt5) {
            case 1:
              // org/antlr/v4/parse/ANTLRParser.g:199:13: option SEMI
            {
              pushFollow(FOLLOW_option_in_optionsSpec920);
              option16 = option();
              state._fsp--;

              stream_option.add(option16.getTree());
              SEMI17 = (Token) match(input, SEMI, FOLLOW_SEMI_in_optionsSpec922);
              stream_SEMI.add(SEMI17);

            }
            break;

            default:
              break loop5;
          }
        }

        RBRACE18 = (Token) match(input, RBRACE, FOLLOW_RBRACE_in_optionsSpec926);
        stream_RBRACE.add(RBRACE18);


        // AST REWRITE
        // elements: option, OPTIONS
        // token labels:
        // rule labels: retval
        // token list labels:
        // rule list labels:
        // wildcard labels:
        retval.tree = root_0;
        RewriteRuleSubtreeStream stream_retval = new RewriteRuleSubtreeStream(adaptor, "rule retval", retval != null ? retval.getTree() : null);

        root_0 = (GrammarAST) adaptor.nil();
        // 199:34: -> ^( OPTIONS[$OPTIONS, \"OPTIONS\"] ( option )* )
        {
          // org/antlr/v4/parse/ANTLRParser.g:199:37: ^( OPTIONS[$OPTIONS, \"OPTIONS\"] ( option )* )
          {
            GrammarAST root_1 = (GrammarAST) adaptor.nil();
            root_1 = (GrammarAST) adaptor.becomeRoot((GrammarAST) adaptor.create(OPTIONS, OPTIONS15, "OPTIONS"), root_1);
            // org/antlr/v4/parse/ANTLRParser.g:199:68: ( option )*
            while (stream_option.hasNext()) {
              adaptor.addChild(root_1, stream_option.nextTree());
            }
            stream_option.reset();

            adaptor.addChild(root_0, root_1);
          }

        }


        retval.tree = root_0;

      }

      retval.stop = input.LT(-1);

      retval.tree = (GrammarAST) adaptor.rulePostProcessing(root_0);
      adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

    } catch (RecognitionException re) {
      reportError(re);
      recover(input, re);
      retval.tree = (GrammarAST) adaptor.errorNode(input, retval.start, input.LT(-1), re);
    } finally {
      // do for sure before leaving
    }
    return retval;
  }
  // $ANTLR end "optionsSpec"


  public static class option_return extends ParserRuleReturnScope {
    GrammarAST tree;

    @Override
    public GrammarAST getTree() {
      return tree;
    }
  }

  ;


  // $ANTLR start "option"
  // org/antlr/v4/parse/ANTLRParser.g:202:1: option : id ASSIGN ^ optionValue ;
  public final ANTLRParser.option_return option() throws RecognitionException {
    ANTLRParser.option_return retval = new ANTLRParser.option_return();
    retval.start = input.LT(1);

    GrammarAST root_0 = null;

    Token ASSIGN20 = null;
    ParserRuleReturnScope id19 = null;
    ParserRuleReturnScope optionValue21 = null;

    GrammarAST ASSIGN20_tree = null;

    try {
      // org/antlr/v4/parse/ANTLRParser.g:203:5: ( id ASSIGN ^ optionValue )
      // org/antlr/v4/parse/ANTLRParser.g:203:9: id ASSIGN ^ optionValue
      {
        root_0 = (GrammarAST) adaptor.nil();


        pushFollow(FOLLOW_id_in_option955);
        id19 = id();
        state._fsp--;

        adaptor.addChild(root_0, id19.getTree());

        ASSIGN20 = (Token) match(input, ASSIGN, FOLLOW_ASSIGN_in_option957);
        ASSIGN20_tree = (GrammarAST) adaptor.create(ASSIGN20);
        root_0 = (GrammarAST) adaptor.becomeRoot(ASSIGN20_tree, root_0);

        pushFollow(FOLLOW_optionValue_in_option960);
        optionValue21 = optionValue();
        state._fsp--;

        adaptor.addChild(root_0, optionValue21.getTree());

      }

      retval.stop = input.LT(-1);

      retval.tree = (GrammarAST) adaptor.rulePostProcessing(root_0);
      adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

    } catch (RecognitionException re) {
      reportError(re);
      recover(input, re);
      retval.tree = (GrammarAST) adaptor.errorNode(input, retval.start, input.LT(-1), re);
    } finally {
      // do for sure before leaving
    }
    return retval;
  }
  // $ANTLR end "option"


  public static class optionValue_return extends ParserRuleReturnScope {
    GrammarAST tree;

    @Override
    public GrammarAST getTree() {
      return tree;
    }
  }

  ;


  // $ANTLR start "optionValue"
  // org/antlr/v4/parse/ANTLRParser.g:211:1: optionValue : ( qid | STRING_LITERAL | ACTION | INT );
  public final ANTLRParser.optionValue_return optionValue() throws RecognitionException {
    ANTLRParser.optionValue_return retval = new ANTLRParser.optionValue_return();
    retval.start = input.LT(1);

    GrammarAST root_0 = null;

    Token STRING_LITERAL23 = null;
    Token ACTION24 = null;
    Token INT25 = null;
    ParserRuleReturnScope qid22 = null;

    GrammarAST STRING_LITERAL23_tree = null;
    GrammarAST ACTION24_tree = null;
    GrammarAST INT25_tree = null;

    try {
      // org/antlr/v4/parse/ANTLRParser.g:212:5: ( qid | STRING_LITERAL | ACTION | INT )
      int alt6 = 4;
      switch (input.LA(1)) {
        case RULE_REF:
        case TOKEN_REF: {
          alt6 = 1;
        }
        break;
        case STRING_LITERAL: {
          alt6 = 2;
        }
        break;
        case ACTION: {
          alt6 = 3;
        }
        break;
        case INT: {
          alt6 = 4;
        }
        break;
        default:
          NoViableAltException nvae =
            new NoViableAltException("", 6, 0, input);
          throw nvae;
      }
      switch (alt6) {
        case 1:
          // org/antlr/v4/parse/ANTLRParser.g:215:7: qid
        {
          root_0 = (GrammarAST) adaptor.nil();


          pushFollow(FOLLOW_qid_in_optionValue1003);
          qid22 = qid();
          state._fsp--;

          adaptor.addChild(root_0, qid22.getTree());

        }
        break;
        case 2:
          // org/antlr/v4/parse/ANTLRParser.g:216:7: STRING_LITERAL
        {
          root_0 = (GrammarAST) adaptor.nil();


          STRING_LITERAL23 = (Token) match(input, STRING_LITERAL, FOLLOW_STRING_LITERAL_in_optionValue1011);
          STRING_LITERAL23_tree = (GrammarAST) adaptor.create(STRING_LITERAL23);
          adaptor.addChild(root_0, STRING_LITERAL23_tree);

        }
        break;
        case 3:
          // org/antlr/v4/parse/ANTLRParser.g:217:4: ACTION
        {
          root_0 = (GrammarAST) adaptor.nil();


          ACTION24 = (Token) match(input, ACTION, FOLLOW_ACTION_in_optionValue1016);
          ACTION24_tree = new ActionAST(ACTION24);
          adaptor.addChild(root_0, ACTION24_tree);

        }
        break;
        case 4:
          // org/antlr/v4/parse/ANTLRParser.g:218:7: INT
        {
          root_0 = (GrammarAST) adaptor.nil();


          INT25 = (Token) match(input, INT, FOLLOW_INT_in_optionValue1027);
          INT25_tree = (GrammarAST) adaptor.create(INT25);
          adaptor.addChild(root_0, INT25_tree);

        }
        break;

      }
      retval.stop = input.LT(-1);

      retval.tree = (GrammarAST) adaptor.rulePostProcessing(root_0);
      adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

    } catch (RecognitionException re) {
      reportError(re);
      recover(input, re);
      retval.tree = (GrammarAST) adaptor.errorNode(input, retval.start, input.LT(-1), re);
    } finally {
      // do for sure before leaving
    }
    return retval;
  }
  // $ANTLR end "optionValue"


  public static class delegateGrammars_return extends ParserRuleReturnScope {
    GrammarAST tree;

    @Override
    public GrammarAST getTree() {
      return tree;
    }
  }

  ;


  // $ANTLR start "delegateGrammars"
  // org/antlr/v4/parse/ANTLRParser.g:223:1: delegateGrammars : IMPORT delegateGrammar ( COMMA delegateGrammar )* SEMI -> ^( IMPORT ( delegateGrammar )+ ) ;
  public final ANTLRParser.delegateGrammars_return delegateGrammars() throws RecognitionException {
    ANTLRParser.delegateGrammars_return retval = new ANTLRParser.delegateGrammars_return();
    retval.start = input.LT(1);

    GrammarAST root_0 = null;

    Token IMPORT26 = null;
    Token COMMA28 = null;
    Token SEMI30 = null;
    ParserRuleReturnScope delegateGrammar27 = null;
    ParserRuleReturnScope delegateGrammar29 = null;

    GrammarAST IMPORT26_tree = null;
    GrammarAST COMMA28_tree = null;
    GrammarAST SEMI30_tree = null;
    RewriteRuleTokenStream stream_COMMA = new RewriteRuleTokenStream(adaptor, "token COMMA");
    RewriteRuleTokenStream stream_IMPORT = new RewriteRuleTokenStream(adaptor, "token IMPORT");
    RewriteRuleTokenStream stream_SEMI = new RewriteRuleTokenStream(adaptor, "token SEMI");
    RewriteRuleSubtreeStream stream_delegateGrammar = new RewriteRuleSubtreeStream(adaptor, "rule delegateGrammar");

    try {
      // org/antlr/v4/parse/ANTLRParser.g:224:2: ( IMPORT delegateGrammar ( COMMA delegateGrammar )* SEMI -> ^( IMPORT ( delegateGrammar )+ ) )
      // org/antlr/v4/parse/ANTLRParser.g:224:4: IMPORT delegateGrammar ( COMMA delegateGrammar )* SEMI
      {
        IMPORT26 = (Token) match(input, IMPORT, FOLLOW_IMPORT_in_delegateGrammars1043);
        stream_IMPORT.add(IMPORT26);

        pushFollow(FOLLOW_delegateGrammar_in_delegateGrammars1045);
        delegateGrammar27 = delegateGrammar();
        state._fsp--;

        stream_delegateGrammar.add(delegateGrammar27.getTree());
        // org/antlr/v4/parse/ANTLRParser.g:224:27: ( COMMA delegateGrammar )*
        loop7:
        while (true) {
          int alt7 = 2;
          int LA7_0 = input.LA(1);
          if ((LA7_0 == COMMA)) {
            alt7 = 1;
          }

          switch (alt7) {
            case 1:
              // org/antlr/v4/parse/ANTLRParser.g:224:28: COMMA delegateGrammar
            {
              COMMA28 = (Token) match(input, COMMA, FOLLOW_COMMA_in_delegateGrammars1048);
              stream_COMMA.add(COMMA28);

              pushFollow(FOLLOW_delegateGrammar_in_delegateGrammars1050);
              delegateGrammar29 = delegateGrammar();
              state._fsp--;

              stream_delegateGrammar.add(delegateGrammar29.getTree());
            }
            break;

            default:
              break loop7;
          }
        }

        SEMI30 = (Token) match(input, SEMI, FOLLOW_SEMI_in_delegateGrammars1054);
        stream_SEMI.add(SEMI30);


        // AST REWRITE
        // elements: IMPORT, delegateGrammar
        // token labels:
        // rule labels: retval
        // token list labels:
        // rule list labels:
        // wildcard labels:
        retval.tree = root_0;
        RewriteRuleSubtreeStream stream_retval = new RewriteRuleSubtreeStream(adaptor, "rule retval", retval != null ? retval.getTree() : null);

        root_0 = (GrammarAST) adaptor.nil();
        // 224:57: -> ^( IMPORT ( delegateGrammar )+ )
        {
          // org/antlr/v4/parse/ANTLRParser.g:224:60: ^( IMPORT ( delegateGrammar )+ )
          {
            GrammarAST root_1 = (GrammarAST) adaptor.nil();
            root_1 = (GrammarAST) adaptor.becomeRoot(stream_IMPORT.nextNode(), root_1);
            if (!(stream_delegateGrammar.hasNext())) {
              throw new RewriteEarlyExitException();
            }
            while (stream_delegateGrammar.hasNext()) {
              adaptor.addChild(root_1, stream_delegateGrammar.nextTree());
            }
            stream_delegateGrammar.reset();

            adaptor.addChild(root_0, root_1);
          }

        }


        retval.tree = root_0;

      }

      retval.stop = input.LT(-1);

      retval.tree = (GrammarAST) adaptor.rulePostProcessing(root_0);
      adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

    } catch (RecognitionException re) {
      reportError(re);
      recover(input, re);
      retval.tree = (GrammarAST) adaptor.errorNode(input, retval.start, input.LT(-1), re);
    } finally {
      // do for sure before leaving
    }
    return retval;
  }
  // $ANTLR end "delegateGrammars"


  public static class delegateGrammar_return extends ParserRuleReturnScope {
    GrammarAST tree;

    @Override
    public GrammarAST getTree() {
      return tree;
    }
  }

  ;


  // $ANTLR start "delegateGrammar"
  // org/antlr/v4/parse/ANTLRParser.g:229:1: delegateGrammar : ( id ASSIGN ^ id | id );
  public final ANTLRParser.delegateGrammar_return delegateGrammar() throws RecognitionException {
    ANTLRParser.delegateGrammar_return retval = new ANTLRParser.delegateGrammar_return();
    retval.start = input.LT(1);

    GrammarAST root_0 = null;

    Token ASSIGN32 = null;
    ParserRuleReturnScope id31 = null;
    ParserRuleReturnScope id33 = null;
    ParserRuleReturnScope id34 = null;

    GrammarAST ASSIGN32_tree = null;

    try {
      // org/antlr/v4/parse/ANTLRParser.g:230:5: ( id ASSIGN ^ id | id )
      int alt8 = 2;
      int LA8_0 = input.LA(1);
      if ((LA8_0 == RULE_REF)) {
        int LA8_1 = input.LA(2);
        if ((LA8_1 == ASSIGN)) {
          alt8 = 1;
        } else if ((LA8_1 == COMMA || LA8_1 == SEMI)) {
          alt8 = 2;
        } else {
          int nvaeMark = input.mark();
          try {
            input.consume();
            NoViableAltException nvae =
              new NoViableAltException("", 8, 1, input);
            throw nvae;
          } finally {
            input.rewind(nvaeMark);
          }
        }

      } else if ((LA8_0 == TOKEN_REF)) {
        int LA8_2 = input.LA(2);
        if ((LA8_2 == ASSIGN)) {
          alt8 = 1;
        } else if ((LA8_2 == COMMA || LA8_2 == SEMI)) {
          alt8 = 2;
        } else {
          int nvaeMark = input.mark();
          try {
            input.consume();
            NoViableAltException nvae =
              new NoViableAltException("", 8, 2, input);
            throw nvae;
          } finally {
            input.rewind(nvaeMark);
          }
        }

      } else {
        NoViableAltException nvae =
          new NoViableAltException("", 8, 0, input);
        throw nvae;
      }

      switch (alt8) {
        case 1:
          // org/antlr/v4/parse/ANTLRParser.g:230:9: id ASSIGN ^ id
        {
          root_0 = (GrammarAST) adaptor.nil();


          pushFollow(FOLLOW_id_in_delegateGrammar1081);
          id31 = id();
          state._fsp--;

          adaptor.addChild(root_0, id31.getTree());

          ASSIGN32 = (Token) match(input, ASSIGN, FOLLOW_ASSIGN_in_delegateGrammar1083);
          ASSIGN32_tree = (GrammarAST) adaptor.create(ASSIGN32);
          root_0 = (GrammarAST) adaptor.becomeRoot(ASSIGN32_tree, root_0);

          pushFollow(FOLLOW_id_in_delegateGrammar1086);
          id33 = id();
          state._fsp--;

          adaptor.addChild(root_0, id33.getTree());

        }
        break;
        case 2:
          // org/antlr/v4/parse/ANTLRParser.g:231:9: id
        {
          root_0 = (GrammarAST) adaptor.nil();


          pushFollow(FOLLOW_id_in_delegateGrammar1096);
          id34 = id();
          state._fsp--;

          adaptor.addChild(root_0, id34.getTree());

        }
        break;

      }
      retval.stop = input.LT(-1);

      retval.tree = (GrammarAST) adaptor.rulePostProcessing(root_0);
      adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

    } catch (RecognitionException re) {
      reportError(re);
      recover(input, re);
      retval.tree = (GrammarAST) adaptor.errorNode(input, retval.start, input.LT(-1), re);
    } finally {
      // do for sure before leaving
    }
    return retval;
  }
  // $ANTLR end "delegateGrammar"


  public static class tokensSpec_return extends ParserRuleReturnScope {
    GrammarAST tree;

    @Override
    public GrammarAST getTree() {
      return tree;
    }
  }

  ;


  // $ANTLR start "tokensSpec"
  // org/antlr/v4/parse/ANTLRParser.g:234:1: tokensSpec : ( TOKENS_SPEC id ( COMMA id )* ( COMMA )? RBRACE -> ^( TOKENS_SPEC ( id )+ ) | TOKENS_SPEC RBRACE ->| TOKENS_SPEC ^ ( v3tokenSpec )+ RBRACE !);
  public final ANTLRParser.tokensSpec_return tokensSpec() throws RecognitionException {
    ANTLRParser.tokensSpec_return retval = new ANTLRParser.tokensSpec_return();
    retval.start = input.LT(1);

    GrammarAST root_0 = null;

    Token TOKENS_SPEC35 = null;
    Token COMMA37 = null;
    Token COMMA39 = null;
    Token RBRACE40 = null;
    Token TOKENS_SPEC41 = null;
    Token RBRACE42 = null;
    Token TOKENS_SPEC43 = null;
    Token RBRACE45 = null;
    ParserRuleReturnScope id36 = null;
    ParserRuleReturnScope id38 = null;
    ParserRuleReturnScope v3tokenSpec44 = null;

    GrammarAST TOKENS_SPEC35_tree = null;
    GrammarAST COMMA37_tree = null;
    GrammarAST COMMA39_tree = null;
    GrammarAST RBRACE40_tree = null;
    GrammarAST TOKENS_SPEC41_tree = null;
    GrammarAST RBRACE42_tree = null;
    GrammarAST TOKENS_SPEC43_tree = null;
    GrammarAST RBRACE45_tree = null;
    RewriteRuleTokenStream stream_COMMA = new RewriteRuleTokenStream(adaptor, "token COMMA");
    RewriteRuleTokenStream stream_RBRACE = new RewriteRuleTokenStream(adaptor, "token RBRACE");
    RewriteRuleTokenStream stream_TOKENS_SPEC = new RewriteRuleTokenStream(adaptor, "token TOKENS_SPEC");
    RewriteRuleSubtreeStream stream_id = new RewriteRuleSubtreeStream(adaptor, "rule id");

    try {
      // org/antlr/v4/parse/ANTLRParser.g:235:2: ( TOKENS_SPEC id ( COMMA id )* ( COMMA )? RBRACE -> ^( TOKENS_SPEC ( id )+ ) | TOKENS_SPEC RBRACE ->| TOKENS_SPEC ^ ( v3tokenSpec )+ RBRACE !)
      int alt12 = 3;
      int LA12_0 = input.LA(1);
      if ((LA12_0 == TOKENS_SPEC)) {
        switch (input.LA(2)) {
          case RBRACE: {
            alt12 = 2;
          }
          break;
          case RULE_REF: {
            int LA12_3 = input.LA(3);
            if ((LA12_3 == COMMA || LA12_3 == RBRACE)) {
              alt12 = 1;
            } else if ((LA12_3 == ASSIGN || LA12_3 == SEMI)) {
              alt12 = 3;
            } else {
              int nvaeMark = input.mark();
              try {
                for (int nvaeConsume = 0; nvaeConsume < 3 - 1; nvaeConsume++) {
                  input.consume();
                }
                NoViableAltException nvae =
                  new NoViableAltException("", 12, 3, input);
                throw nvae;
              } finally {
                input.rewind(nvaeMark);
              }
            }

          }
          break;
          case TOKEN_REF: {
            int LA12_4 = input.LA(3);
            if ((LA12_4 == COMMA || LA12_4 == RBRACE)) {
              alt12 = 1;
            } else if ((LA12_4 == ASSIGN || LA12_4 == SEMI)) {
              alt12 = 3;
            } else {
              int nvaeMark = input.mark();
              try {
                for (int nvaeConsume = 0; nvaeConsume < 3 - 1; nvaeConsume++) {
                  input.consume();
                }
                NoViableAltException nvae =
                  new NoViableAltException("", 12, 4, input);
                throw nvae;
              } finally {
                input.rewind(nvaeMark);
              }
            }

          }
          break;
          default:
            int nvaeMark = input.mark();
            try {
              input.consume();
              NoViableAltException nvae =
                new NoViableAltException("", 12, 1, input);
              throw nvae;
            } finally {
              input.rewind(nvaeMark);
            }
        }
      } else {
        NoViableAltException nvae =
          new NoViableAltException("", 12, 0, input);
        throw nvae;
      }

      switch (alt12) {
        case 1:
          // org/antlr/v4/parse/ANTLRParser.g:235:4: TOKENS_SPEC id ( COMMA id )* ( COMMA )? RBRACE
        {
          TOKENS_SPEC35 = (Token) match(input, TOKENS_SPEC, FOLLOW_TOKENS_SPEC_in_tokensSpec1110);
          stream_TOKENS_SPEC.add(TOKENS_SPEC35);

          pushFollow(FOLLOW_id_in_tokensSpec1112);
          id36 = id();
          state._fsp--;

          stream_id.add(id36.getTree());
          // org/antlr/v4/parse/ANTLRParser.g:235:19: ( COMMA id )*
          loop9:
          while (true) {
            int alt9 = 2;
            int LA9_0 = input.LA(1);
            if ((LA9_0 == COMMA)) {
              int LA9_1 = input.LA(2);
              if ((LA9_1 == RULE_REF || LA9_1 == TOKEN_REF)) {
                alt9 = 1;
              }

            }

            switch (alt9) {
              case 1:
                // org/antlr/v4/parse/ANTLRParser.g:235:20: COMMA id
              {
                COMMA37 = (Token) match(input, COMMA, FOLLOW_COMMA_in_tokensSpec1115);
                stream_COMMA.add(COMMA37);

                pushFollow(FOLLOW_id_in_tokensSpec1117);
                id38 = id();
                state._fsp--;

                stream_id.add(id38.getTree());
              }
              break;

              default:
                break loop9;
            }
          }

          // org/antlr/v4/parse/ANTLRParser.g:235:31: ( COMMA )?
          int alt10 = 2;
          int LA10_0 = input.LA(1);
          if ((LA10_0 == COMMA)) {
            alt10 = 1;
          }
          switch (alt10) {
            case 1:
              // org/antlr/v4/parse/ANTLRParser.g:235:31: COMMA
            {
              COMMA39 = (Token) match(input, COMMA, FOLLOW_COMMA_in_tokensSpec1121);
              stream_COMMA.add(COMMA39);

            }
            break;

          }

          RBRACE40 = (Token) match(input, RBRACE, FOLLOW_RBRACE_in_tokensSpec1124);
          stream_RBRACE.add(RBRACE40);


          // AST REWRITE
          // elements: id, TOKENS_SPEC
          // token labels:
          // rule labels: retval
          // token list labels:
          // rule list labels:
          // wildcard labels:
          retval.tree = root_0;
          RewriteRuleSubtreeStream stream_retval = new RewriteRuleSubtreeStream(adaptor, "rule retval", retval != null ? retval.getTree() : null);

          root_0 = (GrammarAST) adaptor.nil();
          // 235:45: -> ^( TOKENS_SPEC ( id )+ )
          {
            // org/antlr/v4/parse/ANTLRParser.g:235:48: ^( TOKENS_SPEC ( id )+ )
            {
              GrammarAST root_1 = (GrammarAST) adaptor.nil();
              root_1 = (GrammarAST) adaptor.becomeRoot(stream_TOKENS_SPEC.nextNode(), root_1);
              if (!(stream_id.hasNext())) {
                throw new RewriteEarlyExitException();
              }
              while (stream_id.hasNext()) {
                adaptor.addChild(root_1, stream_id.nextTree());
              }
              stream_id.reset();

              adaptor.addChild(root_0, root_1);
            }

          }


          retval.tree = root_0;

        }
        break;
        case 2:
          // org/antlr/v4/parse/ANTLRParser.g:236:7: TOKENS_SPEC RBRACE
        {
          TOKENS_SPEC41 = (Token) match(input, TOKENS_SPEC, FOLLOW_TOKENS_SPEC_in_tokensSpec1141);
          stream_TOKENS_SPEC.add(TOKENS_SPEC41);

          RBRACE42 = (Token) match(input, RBRACE, FOLLOW_RBRACE_in_tokensSpec1143);
          stream_RBRACE.add(RBRACE42);


          // AST REWRITE
          // elements:
          // token labels:
          // rule labels: retval
          // token list labels:
          // rule list labels:
          // wildcard labels:
          retval.tree = root_0;
          RewriteRuleSubtreeStream stream_retval = new RewriteRuleSubtreeStream(adaptor, "rule retval", retval != null ? retval.getTree() : null);

          root_0 = (GrammarAST) adaptor.nil();
          // 236:26: ->
          {
            root_0 = null;
          }


          retval.tree = root_0;

        }
        break;
        case 3:
          // org/antlr/v4/parse/ANTLRParser.g:237:7: TOKENS_SPEC ^ ( v3tokenSpec )+ RBRACE !
        {
          root_0 = (GrammarAST) adaptor.nil();


          TOKENS_SPEC43 = (Token) match(input, TOKENS_SPEC, FOLLOW_TOKENS_SPEC_in_tokensSpec1153);
          TOKENS_SPEC43_tree = (GrammarAST) adaptor.create(TOKENS_SPEC43);
          root_0 = (GrammarAST) adaptor.becomeRoot(TOKENS_SPEC43_tree, root_0);

          // org/antlr/v4/parse/ANTLRParser.g:237:20: ( v3tokenSpec )+
          int cnt11 = 0;
          loop11:
          while (true) {
            int alt11 = 2;
            int LA11_0 = input.LA(1);
            if ((LA11_0 == RULE_REF || LA11_0 == TOKEN_REF)) {
              alt11 = 1;
            }

            switch (alt11) {
              case 1:
                // org/antlr/v4/parse/ANTLRParser.g:237:20: v3tokenSpec
              {
                pushFollow(FOLLOW_v3tokenSpec_in_tokensSpec1156);
                v3tokenSpec44 = v3tokenSpec();
                state._fsp--;

                adaptor.addChild(root_0, v3tokenSpec44.getTree());

              }
              break;

              default:
                if (cnt11 >= 1) break loop11;
                EarlyExitException eee = new EarlyExitException(11, input);
                throw eee;
            }
            cnt11++;
          }

          RBRACE45 = (Token) match(input, RBRACE, FOLLOW_RBRACE_in_tokensSpec1159);
          grammarError(ErrorType.V3_TOKENS_SYNTAX, TOKENS_SPEC43);
        }
        break;

      }
      retval.stop = input.LT(-1);

      retval.tree = (GrammarAST) adaptor.rulePostProcessing(root_0);
      adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

    } catch (RecognitionException re) {
      reportError(re);
      recover(input, re);
      retval.tree = (GrammarAST) adaptor.errorNode(input, retval.start, input.LT(-1), re);
    } finally {
      // do for sure before leaving
    }
    return retval;
  }
  // $ANTLR end "tokensSpec"


  public static class v3tokenSpec_return extends ParserRuleReturnScope {
    GrammarAST tree;

    @Override
    public GrammarAST getTree() {
      return tree;
    }
  }

  ;


  // $ANTLR start "v3tokenSpec"
  // org/antlr/v4/parse/ANTLRParser.g:241:1: v3tokenSpec : id ( ASSIGN lit= STRING_LITERAL -> id | -> id ) SEMI ;
  public final ANTLRParser.v3tokenSpec_return v3tokenSpec() throws RecognitionException {
    ANTLRParser.v3tokenSpec_return retval = new ANTLRParser.v3tokenSpec_return();
    retval.start = input.LT(1);

    GrammarAST root_0 = null;

    Token lit = null;
    Token ASSIGN47 = null;
    Token SEMI48 = null;
    ParserRuleReturnScope id46 = null;

    GrammarAST lit_tree = null;
    GrammarAST ASSIGN47_tree = null;
    GrammarAST SEMI48_tree = null;
    RewriteRuleTokenStream stream_SEMI = new RewriteRuleTokenStream(adaptor, "token SEMI");
    RewriteRuleTokenStream stream_STRING_LITERAL = new RewriteRuleTokenStream(adaptor, "token STRING_LITERAL");
    RewriteRuleTokenStream stream_ASSIGN = new RewriteRuleTokenStream(adaptor, "token ASSIGN");
    RewriteRuleSubtreeStream stream_id = new RewriteRuleSubtreeStream(adaptor, "rule id");

    try {
      // org/antlr/v4/parse/ANTLRParser.g:242:2: ( id ( ASSIGN lit= STRING_LITERAL -> id | -> id ) SEMI )
      // org/antlr/v4/parse/ANTLRParser.g:242:4: id ( ASSIGN lit= STRING_LITERAL -> id | -> id ) SEMI
      {
        pushFollow(FOLLOW_id_in_v3tokenSpec1179);
        id46 = id();
        state._fsp--;

        stream_id.add(id46.getTree());
        // org/antlr/v4/parse/ANTLRParser.g:243:3: ( ASSIGN lit= STRING_LITERAL -> id | -> id )
        int alt13 = 2;
        int LA13_0 = input.LA(1);
        if ((LA13_0 == ASSIGN)) {
          alt13 = 1;
        } else if ((LA13_0 == SEMI)) {
          alt13 = 2;
        } else {
          NoViableAltException nvae =
            new NoViableAltException("", 13, 0, input);
          throw nvae;
        }

        switch (alt13) {
          case 1:
            // org/antlr/v4/parse/ANTLRParser.g:243:5: ASSIGN lit= STRING_LITERAL
          {
            ASSIGN47 = (Token) match(input, ASSIGN, FOLLOW_ASSIGN_in_v3tokenSpec1185);
            stream_ASSIGN.add(ASSIGN47);

            lit = (Token) match(input, STRING_LITERAL, FOLLOW_STRING_LITERAL_in_v3tokenSpec1189);
            stream_STRING_LITERAL.add(lit);


            grammarError(ErrorType.V3_ASSIGN_IN_TOKENS, (id46 != null ? (id46.start) : null),
              (id46 != null ? input.toString(id46.start, id46.stop) : null), lit.getText());


            // AST REWRITE
            // elements: id
            // token labels:
            // rule labels: retval
            // token list labels:
            // rule list labels:
            // wildcard labels:
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval = new RewriteRuleSubtreeStream(adaptor, "rule retval", retval != null ? retval.getTree() : null);

            root_0 = (GrammarAST) adaptor.nil();
            // 248:20: -> id
            {
              adaptor.addChild(root_0, stream_id.nextTree());
            }


            retval.tree = root_0;

          }
          break;
          case 2:
            // org/antlr/v4/parse/ANTLRParser.g:249:12:
          {

            // AST REWRITE
            // elements: id
            // token labels:
            // rule labels: retval
            // token list labels:
            // rule list labels:
            // wildcard labels:
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval = new RewriteRuleSubtreeStream(adaptor, "rule retval", retval != null ? retval.getTree() : null);

            root_0 = (GrammarAST) adaptor.nil();
            // 249:12: -> id
            {
              adaptor.addChild(root_0, stream_id.nextTree());
            }


            retval.tree = root_0;

          }
          break;

        }

        SEMI48 = (Token) match(input, SEMI, FOLLOW_SEMI_in_v3tokenSpec1250);
        stream_SEMI.add(SEMI48);

      }

      retval.stop = input.LT(-1);

      retval.tree = (GrammarAST) adaptor.rulePostProcessing(root_0);
      adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

    } catch (RecognitionException re) {
      reportError(re);
      recover(input, re);
      retval.tree = (GrammarAST) adaptor.errorNode(input, retval.start, input.LT(-1), re);
    } finally {
      // do for sure before leaving
    }
    return retval;
  }
  // $ANTLR end "v3tokenSpec"


  public static class channelsSpec_return extends ParserRuleReturnScope {
    GrammarAST tree;

    @Override
    public GrammarAST getTree() {
      return tree;
    }
  }

  ;


  // $ANTLR start "channelsSpec"
  // org/antlr/v4/parse/ANTLRParser.g:254:1: channelsSpec : CHANNELS ^ ( id ( COMMA ! id )* ( COMMA )? )? RBRACE !;
  public final ANTLRParser.channelsSpec_return channelsSpec() throws RecognitionException {
    ANTLRParser.channelsSpec_return retval = new ANTLRParser.channelsSpec_return();
    retval.start = input.LT(1);

    GrammarAST root_0 = null;

    Token CHANNELS49 = null;
    Token COMMA51 = null;
    Token COMMA53 = null;
    Token RBRACE54 = null;
    ParserRuleReturnScope id50 = null;
    ParserRuleReturnScope id52 = null;

    GrammarAST CHANNELS49_tree = null;
    GrammarAST COMMA51_tree = null;
    GrammarAST COMMA53_tree = null;
    GrammarAST RBRACE54_tree = null;

    try {
      // org/antlr/v4/parse/ANTLRParser.g:255:2: ( CHANNELS ^ ( id ( COMMA ! id )* ( COMMA )? )? RBRACE !)
      // org/antlr/v4/parse/ANTLRParser.g:255:4: CHANNELS ^ ( id ( COMMA ! id )* ( COMMA )? )? RBRACE !
      {
        root_0 = (GrammarAST) adaptor.nil();


        CHANNELS49 = (Token) match(input, CHANNELS, FOLLOW_CHANNELS_in_channelsSpec1261);
        CHANNELS49_tree = (GrammarAST) adaptor.create(CHANNELS49);
        root_0 = (GrammarAST) adaptor.becomeRoot(CHANNELS49_tree, root_0);

        // org/antlr/v4/parse/ANTLRParser.g:255:14: ( id ( COMMA ! id )* ( COMMA )? )?
        int alt16 = 2;
        int LA16_0 = input.LA(1);
        if ((LA16_0 == RULE_REF || LA16_0 == TOKEN_REF)) {
          alt16 = 1;
        }
        switch (alt16) {
          case 1:
            // org/antlr/v4/parse/ANTLRParser.g:255:15: id ( COMMA ! id )* ( COMMA )?
          {
            pushFollow(FOLLOW_id_in_channelsSpec1265);
            id50 = id();
            state._fsp--;

            adaptor.addChild(root_0, id50.getTree());

            // org/antlr/v4/parse/ANTLRParser.g:255:18: ( COMMA ! id )*
            loop14:
            while (true) {
              int alt14 = 2;
              int LA14_0 = input.LA(1);
              if ((LA14_0 == COMMA)) {
                int LA14_1 = input.LA(2);
                if ((LA14_1 == RULE_REF || LA14_1 == TOKEN_REF)) {
                  alt14 = 1;
                }

              }

              switch (alt14) {
                case 1:
                  // org/antlr/v4/parse/ANTLRParser.g:255:19: COMMA ! id
                {
                  COMMA51 = (Token) match(input, COMMA, FOLLOW_COMMA_in_channelsSpec1268);
                  pushFollow(FOLLOW_id_in_channelsSpec1271);
                  id52 = id();
                  state._fsp--;

                  adaptor.addChild(root_0, id52.getTree());

                }
                break;

                default:
                  break loop14;
              }
            }

            // org/antlr/v4/parse/ANTLRParser.g:255:31: ( COMMA )?
            int alt15 = 2;
            int LA15_0 = input.LA(1);
            if ((LA15_0 == COMMA)) {
              alt15 = 1;
            }
            switch (alt15) {
              case 1:
                // org/antlr/v4/parse/ANTLRParser.g:255:31: COMMA
              {
                COMMA53 = (Token) match(input, COMMA, FOLLOW_COMMA_in_channelsSpec1275);
                COMMA53_tree = (GrammarAST) adaptor.create(COMMA53);
                adaptor.addChild(root_0, COMMA53_tree);

              }
              break;

            }

          }
          break;

        }

        RBRACE54 = (Token) match(input, RBRACE, FOLLOW_RBRACE_in_channelsSpec1280);
      }

      retval.stop = input.LT(-1);

      retval.tree = (GrammarAST) adaptor.rulePostProcessing(root_0);
      adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

    } catch (RecognitionException re) {
      reportError(re);
      recover(input, re);
      retval.tree = (GrammarAST) adaptor.errorNode(input, retval.start, input.LT(-1), re);
    } finally {
      // do for sure before leaving
    }
    return retval;
  }
  // $ANTLR end "channelsSpec"


  public static class action_return extends ParserRuleReturnScope {
    GrammarAST tree;

    @Override
    public GrammarAST getTree() {
      return tree;
    }
  }

  ;


  // $ANTLR start "action"
  // org/antlr/v4/parse/ANTLRParser.g:262:1: action : AT ( actionScopeName COLONCOLON )? id ACTION -> ^( AT ( actionScopeName )? id ACTION ) ;
  public final ANTLRParser.action_return action() throws RecognitionException {
    ANTLRParser.action_return retval = new ANTLRParser.action_return();
    retval.start = input.LT(1);

    GrammarAST root_0 = null;

    Token AT55 = null;
    Token COLONCOLON57 = null;
    Token ACTION59 = null;
    ParserRuleReturnScope actionScopeName56 = null;
    ParserRuleReturnScope id58 = null;

    GrammarAST AT55_tree = null;
    GrammarAST COLONCOLON57_tree = null;
    GrammarAST ACTION59_tree = null;
    RewriteRuleTokenStream stream_AT = new RewriteRuleTokenStream(adaptor, "token AT");
    RewriteRuleTokenStream stream_ACTION = new RewriteRuleTokenStream(adaptor, "token ACTION");
    RewriteRuleTokenStream stream_COLONCOLON = new RewriteRuleTokenStream(adaptor, "token COLONCOLON");
    RewriteRuleSubtreeStream stream_id = new RewriteRuleSubtreeStream(adaptor, "rule id");
    RewriteRuleSubtreeStream stream_actionScopeName = new RewriteRuleSubtreeStream(adaptor, "rule actionScopeName");

    try {
      // org/antlr/v4/parse/ANTLRParser.g:263:2: ( AT ( actionScopeName COLONCOLON )? id ACTION -> ^( AT ( actionScopeName )? id ACTION ) )
      // org/antlr/v4/parse/ANTLRParser.g:263:4: AT ( actionScopeName COLONCOLON )? id ACTION
      {
        AT55 = (Token) match(input, AT, FOLLOW_AT_in_action1297);
        stream_AT.add(AT55);

        // org/antlr/v4/parse/ANTLRParser.g:263:7: ( actionScopeName COLONCOLON )?
        int alt17 = 2;
        switch (input.LA(1)) {
          case RULE_REF: {
            int LA17_1 = input.LA(2);
            if ((LA17_1 == COLONCOLON)) {
              alt17 = 1;
            }
          }
          break;
          case TOKEN_REF: {
            int LA17_2 = input.LA(2);
            if ((LA17_2 == COLONCOLON)) {
              alt17 = 1;
            }
          }
          break;
          case LEXER:
          case PARSER: {
            alt17 = 1;
          }
          break;
        }
        switch (alt17) {
          case 1:
            // org/antlr/v4/parse/ANTLRParser.g:263:8: actionScopeName COLONCOLON
          {
            pushFollow(FOLLOW_actionScopeName_in_action1300);
            actionScopeName56 = actionScopeName();
            state._fsp--;

            stream_actionScopeName.add(actionScopeName56.getTree());
            COLONCOLON57 = (Token) match(input, COLONCOLON, FOLLOW_COLONCOLON_in_action1302);
            stream_COLONCOLON.add(COLONCOLON57);

          }
          break;

        }

        pushFollow(FOLLOW_id_in_action1306);
        id58 = id();
        state._fsp--;

        stream_id.add(id58.getTree());
        ACTION59 = (Token) match(input, ACTION, FOLLOW_ACTION_in_action1308);
        stream_ACTION.add(ACTION59);


        // AST REWRITE
        // elements: ACTION, id, AT, actionScopeName
        // token labels:
        // rule labels: retval
        // token list labels:
        // rule list labels:
        // wildcard labels:
        retval.tree = root_0;
        RewriteRuleSubtreeStream stream_retval = new RewriteRuleSubtreeStream(adaptor, "rule retval", retval != null ? retval.getTree() : null);

        root_0 = (GrammarAST) adaptor.nil();
        // 263:47: -> ^( AT ( actionScopeName )? id ACTION )
        {
          // org/antlr/v4/parse/ANTLRParser.g:263:50: ^( AT ( actionScopeName )? id ACTION )
          {
            GrammarAST root_1 = (GrammarAST) adaptor.nil();
            root_1 = (GrammarAST) adaptor.becomeRoot(stream_AT.nextNode(), root_1);
            // org/antlr/v4/parse/ANTLRParser.g:263:55: ( actionScopeName )?
            if (stream_actionScopeName.hasNext()) {
              adaptor.addChild(root_1, stream_actionScopeName.nextTree());
            }
            stream_actionScopeName.reset();

            adaptor.addChild(root_1, stream_id.nextTree());
            adaptor.addChild(root_1, new ActionAST(stream_ACTION.nextToken()));
            adaptor.addChild(root_0, root_1);
          }

        }


        retval.tree = root_0;

      }

      retval.stop = input.LT(-1);

      retval.tree = (GrammarAST) adaptor.rulePostProcessing(root_0);
      adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

    } catch (RecognitionException re) {
      reportError(re);
      recover(input, re);
      retval.tree = (GrammarAST) adaptor.errorNode(input, retval.start, input.LT(-1), re);
    } finally {
      // do for sure before leaving
    }
    return retval;
  }
  // $ANTLR end "action"


  public static class actionScopeName_return extends ParserRuleReturnScope {
    GrammarAST tree;

    @Override
    public GrammarAST getTree() {
      return tree;
    }
  }

  ;


  // $ANTLR start "actionScopeName"
  // org/antlr/v4/parse/ANTLRParser.g:269:1: actionScopeName : ( id | LEXER -> ID[$LEXER] | PARSER -> ID[$PARSER] );
  public final ANTLRParser.actionScopeName_return actionScopeName() throws RecognitionException {
    ANTLRParser.actionScopeName_return retval = new ANTLRParser.actionScopeName_return();
    retval.start = input.LT(1);

    GrammarAST root_0 = null;

    Token LEXER61 = null;
    Token PARSER62 = null;
    ParserRuleReturnScope id60 = null;

    GrammarAST LEXER61_tree = null;
    GrammarAST PARSER62_tree = null;
    RewriteRuleTokenStream stream_PARSER = new RewriteRuleTokenStream(adaptor, "token PARSER");
    RewriteRuleTokenStream stream_LEXER = new RewriteRuleTokenStream(adaptor, "token LEXER");

    try {
      // org/antlr/v4/parse/ANTLRParser.g:270:2: ( id | LEXER -> ID[$LEXER] | PARSER -> ID[$PARSER] )
      int alt18 = 3;
      switch (input.LA(1)) {
        case RULE_REF:
        case TOKEN_REF: {
          alt18 = 1;
        }
        break;
        case LEXER: {
          alt18 = 2;
        }
        break;
        case PARSER: {
          alt18 = 3;
        }
        break;
        default:
          NoViableAltException nvae =
            new NoViableAltException("", 18, 0, input);
          throw nvae;
      }
      switch (alt18) {
        case 1:
          // org/antlr/v4/parse/ANTLRParser.g:270:4: id
        {
          root_0 = (GrammarAST) adaptor.nil();


          pushFollow(FOLLOW_id_in_actionScopeName1337);
          id60 = id();
          state._fsp--;

          adaptor.addChild(root_0, id60.getTree());

        }
        break;
        case 2:
          // org/antlr/v4/parse/ANTLRParser.g:271:4: LEXER
        {
          LEXER61 = (Token) match(input, LEXER, FOLLOW_LEXER_in_actionScopeName1342);
          stream_LEXER.add(LEXER61);


          // AST REWRITE
          // elements:
          // token labels:
          // rule labels: retval
          // token list labels:
          // rule list labels:
          // wildcard labels:
          retval.tree = root_0;
          RewriteRuleSubtreeStream stream_retval = new RewriteRuleSubtreeStream(adaptor, "rule retval", retval != null ? retval.getTree() : null);

          root_0 = (GrammarAST) adaptor.nil();
          // 271:10: -> ID[$LEXER]
          {
            adaptor.addChild(root_0, (GrammarAST) adaptor.create(ID, LEXER61));
          }


          retval.tree = root_0;

        }
        break;
        case 3:
          // org/antlr/v4/parse/ANTLRParser.g:272:9: PARSER
        {
          PARSER62 = (Token) match(input, PARSER, FOLLOW_PARSER_in_actionScopeName1357);
          stream_PARSER.add(PARSER62);


          // AST REWRITE
          // elements:
          // token labels:
          // rule labels: retval
          // token list labels:
          // rule list labels:
          // wildcard labels:
          retval.tree = root_0;
          RewriteRuleSubtreeStream stream_retval = new RewriteRuleSubtreeStream(adaptor, "rule retval", retval != null ? retval.getTree() : null);

          root_0 = (GrammarAST) adaptor.nil();
          // 272:16: -> ID[$PARSER]
          {
            adaptor.addChild(root_0, (GrammarAST) adaptor.create(ID, PARSER62));
          }


          retval.tree = root_0;

        }
        break;

      }
      retval.stop = input.LT(-1);

      retval.tree = (GrammarAST) adaptor.rulePostProcessing(root_0);
      adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

    } catch (RecognitionException re) {
      reportError(re);
      recover(input, re);
      retval.tree = (GrammarAST) adaptor.errorNode(input, retval.start, input.LT(-1), re);
    } finally {
      // do for sure before leaving
    }
    return retval;
  }
  // $ANTLR end "actionScopeName"


  public static class modeSpec_return extends ParserRuleReturnScope {
    GrammarAST tree;

    @Override
    public GrammarAST getTree() {
      return tree;
    }
  }

  ;


  // $ANTLR start "modeSpec"
  // org/antlr/v4/parse/ANTLRParser.g:275:1: modeSpec : MODE id SEMI sync ( lexerRule sync )* -> ^( MODE id ( lexerRule )* ) ;
  public final ANTLRParser.modeSpec_return modeSpec() throws RecognitionException {
    ANTLRParser.modeSpec_return retval = new ANTLRParser.modeSpec_return();
    retval.start = input.LT(1);

    GrammarAST root_0 = null;

    Token MODE63 = null;
    Token SEMI65 = null;
    ParserRuleReturnScope id64 = null;
    ParserRuleReturnScope sync66 = null;
    ParserRuleReturnScope lexerRule67 = null;
    ParserRuleReturnScope sync68 = null;

    GrammarAST MODE63_tree = null;
    GrammarAST SEMI65_tree = null;
    RewriteRuleTokenStream stream_MODE = new RewriteRuleTokenStream(adaptor, "token MODE");
    RewriteRuleTokenStream stream_SEMI = new RewriteRuleTokenStream(adaptor, "token SEMI");
    RewriteRuleSubtreeStream stream_lexerRule = new RewriteRuleSubtreeStream(adaptor, "rule lexerRule");
    RewriteRuleSubtreeStream stream_id = new RewriteRuleSubtreeStream(adaptor, "rule id");
    RewriteRuleSubtreeStream stream_sync = new RewriteRuleSubtreeStream(adaptor, "rule sync");

    try {
      // org/antlr/v4/parse/ANTLRParser.g:276:5: ( MODE id SEMI sync ( lexerRule sync )* -> ^( MODE id ( lexerRule )* ) )
      // org/antlr/v4/parse/ANTLRParser.g:276:7: MODE id SEMI sync ( lexerRule sync )*
      {
        MODE63 = (Token) match(input, MODE, FOLLOW_MODE_in_modeSpec1376);
        stream_MODE.add(MODE63);

        pushFollow(FOLLOW_id_in_modeSpec1378);
        id64 = id();
        state._fsp--;

        stream_id.add(id64.getTree());
        SEMI65 = (Token) match(input, SEMI, FOLLOW_SEMI_in_modeSpec1380);
        stream_SEMI.add(SEMI65);

        pushFollow(FOLLOW_sync_in_modeSpec1382);
        sync66 = sync();
        state._fsp--;

        stream_sync.add(sync66.getTree());
        // org/antlr/v4/parse/ANTLRParser.g:276:25: ( lexerRule sync )*
        loop19:
        while (true) {
          int alt19 = 2;
          int LA19_0 = input.LA(1);
          if ((LA19_0 == FRAGMENT || LA19_0 == TOKEN_REF)) {
            alt19 = 1;
          }

          switch (alt19) {
            case 1:
              // org/antlr/v4/parse/ANTLRParser.g:276:26: lexerRule sync
            {
              pushFollow(FOLLOW_lexerRule_in_modeSpec1385);
              lexerRule67 = lexerRule();
              state._fsp--;

              stream_lexerRule.add(lexerRule67.getTree());
              pushFollow(FOLLOW_sync_in_modeSpec1387);
              sync68 = sync();
              state._fsp--;

              stream_sync.add(sync68.getTree());
            }
            break;

            default:
              break loop19;
          }
        }


        // AST REWRITE
        // elements: MODE, id, lexerRule
        // token labels:
        // rule labels: retval
        // token list labels:
        // rule list labels:
        // wildcard labels:
        retval.tree = root_0;
        RewriteRuleSubtreeStream stream_retval = new RewriteRuleSubtreeStream(adaptor, "rule retval", retval != null ? retval.getTree() : null);

        root_0 = (GrammarAST) adaptor.nil();
        // 276:44: -> ^( MODE id ( lexerRule )* )
        {
          // org/antlr/v4/parse/ANTLRParser.g:276:47: ^( MODE id ( lexerRule )* )
          {
            GrammarAST root_1 = (GrammarAST) adaptor.nil();
            root_1 = (GrammarAST) adaptor.becomeRoot(stream_MODE.nextNode(), root_1);
            adaptor.addChild(root_1, stream_id.nextTree());
            // org/antlr/v4/parse/ANTLRParser.g:276:57: ( lexerRule )*
            while (stream_lexerRule.hasNext()) {
              adaptor.addChild(root_1, stream_lexerRule.nextTree());
            }
            stream_lexerRule.reset();

            adaptor.addChild(root_0, root_1);
          }

        }


        retval.tree = root_0;

      }

      retval.stop = input.LT(-1);

      retval.tree = (GrammarAST) adaptor.rulePostProcessing(root_0);
      adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

    } catch (RecognitionException re) {
      reportError(re);
      recover(input, re);
      retval.tree = (GrammarAST) adaptor.errorNode(input, retval.start, input.LT(-1), re);
    } finally {
      // do for sure before leaving
    }
    return retval;
  }
  // $ANTLR end "modeSpec"


  public static class rules_return extends ParserRuleReturnScope {
    GrammarAST tree;

    @Override
    public GrammarAST getTree() {
      return tree;
    }
  }

  ;


  // $ANTLR start "rules"
  // org/antlr/v4/parse/ANTLRParser.g:279:1: rules : sync ( rule sync )* -> ^( RULES ( rule )* ) ;
  public final ANTLRParser.rules_return rules() throws RecognitionException {
    ANTLRParser.rules_return retval = new ANTLRParser.rules_return();
    retval.start = input.LT(1);

    GrammarAST root_0 = null;

    ParserRuleReturnScope sync69 = null;
    ParserRuleReturnScope rule70 = null;
    ParserRuleReturnScope sync71 = null;

    RewriteRuleSubtreeStream stream_rule = new RewriteRuleSubtreeStream(adaptor, "rule rule");
    RewriteRuleSubtreeStream stream_sync = new RewriteRuleSubtreeStream(adaptor, "rule sync");

    try {
      // org/antlr/v4/parse/ANTLRParser.g:280:5: ( sync ( rule sync )* -> ^( RULES ( rule )* ) )
      // org/antlr/v4/parse/ANTLRParser.g:280:7: sync ( rule sync )*
      {
        pushFollow(FOLLOW_sync_in_rules1418);
        sync69 = sync();
        state._fsp--;

        stream_sync.add(sync69.getTree());
        // org/antlr/v4/parse/ANTLRParser.g:280:12: ( rule sync )*
        loop20:
        while (true) {
          int alt20 = 2;
          int LA20_0 = input.LA(1);
          if ((LA20_0 == FRAGMENT || LA20_0 == RULE_REF || LA20_0 == TOKEN_REF)) {
            alt20 = 1;
          }

          switch (alt20) {
            case 1:
              // org/antlr/v4/parse/ANTLRParser.g:280:13: rule sync
            {
              pushFollow(FOLLOW_rule_in_rules1421);
              rule70 = rule();
              state._fsp--;

              stream_rule.add(rule70.getTree());
              pushFollow(FOLLOW_sync_in_rules1423);
              sync71 = sync();
              state._fsp--;

              stream_sync.add(sync71.getTree());
            }
            break;

            default:
              break loop20;
          }
        }


        // AST REWRITE
        // elements: rule
        // token labels:
        // rule labels: retval
        // token list labels:
        // rule list labels:
        // wildcard labels:
        retval.tree = root_0;
        RewriteRuleSubtreeStream stream_retval = new RewriteRuleSubtreeStream(adaptor, "rule retval", retval != null ? retval.getTree() : null);

        root_0 = (GrammarAST) adaptor.nil();
        // 284:7: -> ^( RULES ( rule )* )
        {
          // org/antlr/v4/parse/ANTLRParser.g:284:9: ^( RULES ( rule )* )
          {
            GrammarAST root_1 = (GrammarAST) adaptor.nil();
            root_1 = (GrammarAST) adaptor.becomeRoot((GrammarAST) adaptor.create(RULES, "RULES"), root_1);
            // org/antlr/v4/parse/ANTLRParser.g:284:17: ( rule )*
            while (stream_rule.hasNext()) {
              adaptor.addChild(root_1, stream_rule.nextTree());
            }
            stream_rule.reset();

            adaptor.addChild(root_0, root_1);
          }

        }


        retval.tree = root_0;

      }

      retval.stop = input.LT(-1);

      retval.tree = (GrammarAST) adaptor.rulePostProcessing(root_0);
      adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

    } catch (RecognitionException re) {
      reportError(re);
      recover(input, re);
      retval.tree = (GrammarAST) adaptor.errorNode(input, retval.start, input.LT(-1), re);
    } finally {
      // do for sure before leaving
    }
    return retval;
  }
  // $ANTLR end "rules"


  public static class sync_return extends ParserRuleReturnScope {
    GrammarAST tree;

    @Override
    public GrammarAST getTree() {
      return tree;
    }
  }

  ;


  // $ANTLR start "sync"
  // org/antlr/v4/parse/ANTLRParser.g:287:1: sync :;
  public final ANTLRParser.sync_return sync() throws RecognitionException {
    ANTLRParser.sync_return retval = new ANTLRParser.sync_return();
    retval.start = input.LT(1);

    GrammarAST root_0 = null;


    BitSet followSet = computeErrorRecoverySet();
    if (input.LA(1) != Token.EOF && !followSet.member(input.LA(1))) {
      reportError(new NoViableAltException("", 0, 0, input));
      beginResync();
      consumeUntil(input, followSet);
      endResync();
    }

    try {
      // org/antlr/v4/parse/ANTLRParser.g:296:3: ()
      // org/antlr/v4/parse/ANTLRParser.g:297:2:
      {
        root_0 = (GrammarAST) adaptor.nil();


      }

      retval.stop = input.LT(-1);

      retval.tree = (GrammarAST) adaptor.rulePostProcessing(root_0);
      adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

    } finally {
      // do for sure before leaving
    }
    return retval;
  }
  // $ANTLR end "sync"


  public static class rule_return extends ParserRuleReturnScope {
    GrammarAST tree;

    @Override
    public GrammarAST getTree() {
      return tree;
    }
  }

  ;


  // $ANTLR start "rule"
  // org/antlr/v4/parse/ANTLRParser.g:299:1: rule : ( parserRule | lexerRule );
  public final ANTLRParser.rule_return rule() throws RecognitionException {
    ANTLRParser.rule_return retval = new ANTLRParser.rule_return();
    retval.start = input.LT(1);

    GrammarAST root_0 = null;

    ParserRuleReturnScope parserRule72 = null;
    ParserRuleReturnScope lexerRule73 = null;


    try {
      // org/antlr/v4/parse/ANTLRParser.g:299:5: ( parserRule | lexerRule )
      int alt21 = 2;
      int LA21_0 = input.LA(1);
      if ((LA21_0 == RULE_REF)) {
        alt21 = 1;
      } else if ((LA21_0 == FRAGMENT || LA21_0 == TOKEN_REF)) {
        alt21 = 2;
      } else {
        NoViableAltException nvae =
          new NoViableAltException("", 21, 0, input);
        throw nvae;
      }

      switch (alt21) {
        case 1:
          // org/antlr/v4/parse/ANTLRParser.g:299:7: parserRule
        {
          root_0 = (GrammarAST) adaptor.nil();


          pushFollow(FOLLOW_parserRule_in_rule1485);
          parserRule72 = parserRule();
          state._fsp--;

          adaptor.addChild(root_0, parserRule72.getTree());

        }
        break;
        case 2:
          // org/antlr/v4/parse/ANTLRParser.g:300:4: lexerRule
        {
          root_0 = (GrammarAST) adaptor.nil();


          pushFollow(FOLLOW_lexerRule_in_rule1490);
          lexerRule73 = lexerRule();
          state._fsp--;

          adaptor.addChild(root_0, lexerRule73.getTree());

        }
        break;

      }
      retval.stop = input.LT(-1);

      retval.tree = (GrammarAST) adaptor.rulePostProcessing(root_0);
      adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

    } catch (RecognitionException re) {
      reportError(re);
      recover(input, re);
      retval.tree = (GrammarAST) adaptor.errorNode(input, retval.start, input.LT(-1), re);
    } finally {
      // do for sure before leaving
    }
    return retval;
  }
  // $ANTLR end "rule"


  public static class parserRule_return extends ParserRuleReturnScope {
    GrammarAST tree;

    @Override
    public GrammarAST getTree() {
      return tree;
    }
  }

  ;


  // $ANTLR start "parserRule"
  // org/antlr/v4/parse/ANTLRParser.g:312:1: parserRule : RULE_REF ( ARG_ACTION )? ( ruleReturns )? ( throwsSpec )? ( localsSpec )? rulePrequels COLON ruleBlock SEMI exceptionGroup -> ^( RULE RULE_REF ( ARG_ACTION )? ( ruleReturns )? ( throwsSpec )? ( localsSpec )? ( rulePrequels )? ruleBlock ( exceptionGroup )* ) ;
  public final ANTLRParser.parserRule_return parserRule() throws RecognitionException {
    ANTLRParser.parserRule_return retval = new ANTLRParser.parserRule_return();
    retval.start = input.LT(1);

    GrammarAST root_0 = null;

    Token RULE_REF74 = null;
    Token ARG_ACTION75 = null;
    Token COLON80 = null;
    Token SEMI82 = null;
    ParserRuleReturnScope ruleReturns76 = null;
    ParserRuleReturnScope throwsSpec77 = null;
    ParserRuleReturnScope localsSpec78 = null;
    ParserRuleReturnScope rulePrequels79 = null;
    ParserRuleReturnScope ruleBlock81 = null;
    ParserRuleReturnScope exceptionGroup83 = null;

    GrammarAST RULE_REF74_tree = null;
    GrammarAST ARG_ACTION75_tree = null;
    GrammarAST COLON80_tree = null;
    GrammarAST SEMI82_tree = null;
    RewriteRuleTokenStream stream_ARG_ACTION = new RewriteRuleTokenStream(adaptor, "token ARG_ACTION");
    RewriteRuleTokenStream stream_SEMI = new RewriteRuleTokenStream(adaptor, "token SEMI");
    RewriteRuleTokenStream stream_COLON = new RewriteRuleTokenStream(adaptor, "token COLON");
    RewriteRuleTokenStream stream_RULE_REF = new RewriteRuleTokenStream(adaptor, "token RULE_REF");
    RewriteRuleSubtreeStream stream_throwsSpec = new RewriteRuleSubtreeStream(adaptor, "rule throwsSpec");
    RewriteRuleSubtreeStream stream_localsSpec = new RewriteRuleSubtreeStream(adaptor, "rule localsSpec");
    RewriteRuleSubtreeStream stream_ruleBlock = new RewriteRuleSubtreeStream(adaptor, "rule ruleBlock");
    RewriteRuleSubtreeStream stream_ruleReturns = new RewriteRuleSubtreeStream(adaptor, "rule ruleReturns");
    RewriteRuleSubtreeStream stream_rulePrequels = new RewriteRuleSubtreeStream(adaptor, "rule rulePrequels");
    RewriteRuleSubtreeStream stream_exceptionGroup = new RewriteRuleSubtreeStream(adaptor, "rule exceptionGroup");

    paraphrases.push("matching a rule");
    try {
      // org/antlr/v4/parse/ANTLRParser.g:321:5: ( RULE_REF ( ARG_ACTION )? ( ruleReturns )? ( throwsSpec )? ( localsSpec )? rulePrequels COLON ruleBlock SEMI exceptionGroup -> ^( RULE RULE_REF ( ARG_ACTION )? ( ruleReturns )? ( throwsSpec )? ( localsSpec )? ( rulePrequels )? ruleBlock ( exceptionGroup )* ) )
      // org/antlr/v4/parse/ANTLRParser.g:325:4: RULE_REF ( ARG_ACTION )? ( ruleReturns )? ( throwsSpec )? ( localsSpec )? rulePrequels COLON ruleBlock SEMI exceptionGroup
      {
        RULE_REF74 = (Token) match(input, RULE_REF, FOLLOW_RULE_REF_in_parserRule1539);
        stream_RULE_REF.add(RULE_REF74);

        // org/antlr/v4/parse/ANTLRParser.g:333:4: ( ARG_ACTION )?
        int alt22 = 2;
        int LA22_0 = input.LA(1);
        if ((LA22_0 == ARG_ACTION)) {
          alt22 = 1;
        }
        switch (alt22) {
          case 1:
            // org/antlr/v4/parse/ANTLRParser.g:333:4: ARG_ACTION
          {
            ARG_ACTION75 = (Token) match(input, ARG_ACTION, FOLLOW_ARG_ACTION_in_parserRule1569);
            stream_ARG_ACTION.add(ARG_ACTION75);

          }
          break;

        }

        // org/antlr/v4/parse/ANTLRParser.g:335:4: ( ruleReturns )?
        int alt23 = 2;
        int LA23_0 = input.LA(1);
        if ((LA23_0 == RETURNS)) {
          alt23 = 1;
        }
        switch (alt23) {
          case 1:
            // org/antlr/v4/parse/ANTLRParser.g:335:4: ruleReturns
          {
            pushFollow(FOLLOW_ruleReturns_in_parserRule1576);
            ruleReturns76 = ruleReturns();
            state._fsp--;

            stream_ruleReturns.add(ruleReturns76.getTree());
          }
          break;

        }

        // org/antlr/v4/parse/ANTLRParser.g:337:4: ( throwsSpec )?
        int alt24 = 2;
        int LA24_0 = input.LA(1);
        if ((LA24_0 == THROWS)) {
          alt24 = 1;
        }
        switch (alt24) {
          case 1:
            // org/antlr/v4/parse/ANTLRParser.g:337:4: throwsSpec
          {
            pushFollow(FOLLOW_throwsSpec_in_parserRule1583);
            throwsSpec77 = throwsSpec();
            state._fsp--;

            stream_throwsSpec.add(throwsSpec77.getTree());
          }
          break;

        }

        // org/antlr/v4/parse/ANTLRParser.g:339:4: ( localsSpec )?
        int alt25 = 2;
        int LA25_0 = input.LA(1);
        if ((LA25_0 == LOCALS)) {
          alt25 = 1;
        }
        switch (alt25) {
          case 1:
            // org/antlr/v4/parse/ANTLRParser.g:339:4: localsSpec
          {
            pushFollow(FOLLOW_localsSpec_in_parserRule1590);
            localsSpec78 = localsSpec();
            state._fsp--;

            stream_localsSpec.add(localsSpec78.getTree());
          }
          break;

        }

        pushFollow(FOLLOW_rulePrequels_in_parserRule1628);
        rulePrequels79 = rulePrequels();
        state._fsp--;

        stream_rulePrequels.add(rulePrequels79.getTree());
        COLON80 = (Token) match(input, COLON, FOLLOW_COLON_in_parserRule1637);
        stream_COLON.add(COLON80);

        pushFollow(FOLLOW_ruleBlock_in_parserRule1660);
        ruleBlock81 = ruleBlock();
        state._fsp--;

        stream_ruleBlock.add(ruleBlock81.getTree());
        SEMI82 = (Token) match(input, SEMI, FOLLOW_SEMI_in_parserRule1669);
        stream_SEMI.add(SEMI82);

        pushFollow(FOLLOW_exceptionGroup_in_parserRule1678);
        exceptionGroup83 = exceptionGroup();
        state._fsp--;

        stream_exceptionGroup.add(exceptionGroup83.getTree());

        // AST REWRITE
        // elements: ruleReturns, rulePrequels, ruleBlock, RULE_REF, ARG_ACTION, localsSpec, exceptionGroup, throwsSpec
        // token labels:
        // rule labels: retval
        // token list labels:
        // rule list labels:
        // wildcard labels:
        retval.tree = root_0;
        RewriteRuleSubtreeStream stream_retval = new RewriteRuleSubtreeStream(adaptor, "rule retval", retval != null ? retval.getTree() : null);

        root_0 = (GrammarAST) adaptor.nil();
        // 366:7: -> ^( RULE RULE_REF ( ARG_ACTION )? ( ruleReturns )? ( throwsSpec )? ( localsSpec )? ( rulePrequels )? ruleBlock ( exceptionGroup )* )
        {
          // org/antlr/v4/parse/ANTLRParser.g:366:10: ^( RULE RULE_REF ( ARG_ACTION )? ( ruleReturns )? ( throwsSpec )? ( localsSpec )? ( rulePrequels )? ruleBlock ( exceptionGroup )* )
          {
            GrammarAST root_1 = (GrammarAST) adaptor.nil();
            root_1 = (GrammarAST) adaptor.becomeRoot(new RuleAST(RULE), root_1);
            adaptor.addChild(root_1, stream_RULE_REF.nextNode());
            // org/antlr/v4/parse/ANTLRParser.g:366:36: ( ARG_ACTION )?
            if (stream_ARG_ACTION.hasNext()) {
              adaptor.addChild(root_1, new ActionAST(stream_ARG_ACTION.nextToken()));
            }
            stream_ARG_ACTION.reset();

            // org/antlr/v4/parse/ANTLRParser.g:367:9: ( ruleReturns )?
            if (stream_ruleReturns.hasNext()) {
              adaptor.addChild(root_1, stream_ruleReturns.nextTree());
            }
            stream_ruleReturns.reset();

            // org/antlr/v4/parse/ANTLRParser.g:367:22: ( throwsSpec )?
            if (stream_throwsSpec.hasNext()) {
              adaptor.addChild(root_1, stream_throwsSpec.nextTree());
            }
            stream_throwsSpec.reset();

            // org/antlr/v4/parse/ANTLRParser.g:367:34: ( localsSpec )?
            if (stream_localsSpec.hasNext()) {
              adaptor.addChild(root_1, stream_localsSpec.nextTree());
            }
            stream_localsSpec.reset();

            // org/antlr/v4/parse/ANTLRParser.g:367:46: ( rulePrequels )?
            if (stream_rulePrequels.hasNext()) {
              adaptor.addChild(root_1, stream_rulePrequels.nextTree());
            }
            stream_rulePrequels.reset();

            adaptor.addChild(root_1, stream_ruleBlock.nextTree());
            // org/antlr/v4/parse/ANTLRParser.g:367:70: ( exceptionGroup )*
            while (stream_exceptionGroup.hasNext()) {
              adaptor.addChild(root_1, stream_exceptionGroup.nextTree());
            }
            stream_exceptionGroup.reset();

            adaptor.addChild(root_0, root_1);
          }

        }


        retval.tree = root_0;

      }

      retval.stop = input.LT(-1);

      retval.tree = (GrammarAST) adaptor.rulePostProcessing(root_0);
      adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);


      paraphrases.pop();
      GrammarAST options = (GrammarAST) retval.tree.getFirstChildWithType(ANTLRParser.OPTIONS);
      if (options != null) {
        Grammar.setNodeOptions(retval.tree, options);
      }

    } catch (RecognitionException re) {
      reportError(re);
      recover(input, re);
      retval.tree = (GrammarAST) adaptor.errorNode(input, retval.start, input.LT(-1), re);
    } finally {
      // do for sure before leaving
    }
    return retval;
  }
  // $ANTLR end "parserRule"


  public static class exceptionGroup_return extends ParserRuleReturnScope {
    GrammarAST tree;

    @Override
    public GrammarAST getTree() {
      return tree;
    }
  }

  ;


  // $ANTLR start "exceptionGroup"
  // org/antlr/v4/parse/ANTLRParser.g:377:1: exceptionGroup : ( exceptionHandler )* ( finallyClause )? ;
  public final ANTLRParser.exceptionGroup_return exceptionGroup() throws RecognitionException {
    ANTLRParser.exceptionGroup_return retval = new ANTLRParser.exceptionGroup_return();
    retval.start = input.LT(1);

    GrammarAST root_0 = null;

    ParserRuleReturnScope exceptionHandler84 = null;
    ParserRuleReturnScope finallyClause85 = null;


    try {
      // org/antlr/v4/parse/ANTLRParser.g:378:5: ( ( exceptionHandler )* ( finallyClause )? )
      // org/antlr/v4/parse/ANTLRParser.g:378:7: ( exceptionHandler )* ( finallyClause )?
      {
        root_0 = (GrammarAST) adaptor.nil();


        // org/antlr/v4/parse/ANTLRParser.g:378:7: ( exceptionHandler )*
        loop26:
        while (true) {
          int alt26 = 2;
          int LA26_0 = input.LA(1);
          if ((LA26_0 == CATCH)) {
            alt26 = 1;
          }

          switch (alt26) {
            case 1:
              // org/antlr/v4/parse/ANTLRParser.g:378:7: exceptionHandler
            {
              pushFollow(FOLLOW_exceptionHandler_in_exceptionGroup1761);
              exceptionHandler84 = exceptionHandler();
              state._fsp--;

              adaptor.addChild(root_0, exceptionHandler84.getTree());

            }
            break;

            default:
              break loop26;
          }
        }

        // org/antlr/v4/parse/ANTLRParser.g:378:25: ( finallyClause )?
        int alt27 = 2;
        int LA27_0 = input.LA(1);
        if ((LA27_0 == FINALLY)) {
          alt27 = 1;
        }
        switch (alt27) {
          case 1:
            // org/antlr/v4/parse/ANTLRParser.g:378:25: finallyClause
          {
            pushFollow(FOLLOW_finallyClause_in_exceptionGroup1764);
            finallyClause85 = finallyClause();
            state._fsp--;

            adaptor.addChild(root_0, finallyClause85.getTree());

          }
          break;

        }

      }

      retval.stop = input.LT(-1);

      retval.tree = (GrammarAST) adaptor.rulePostProcessing(root_0);
      adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

    } catch (RecognitionException re) {
      reportError(re);
      recover(input, re);
      retval.tree = (GrammarAST) adaptor.errorNode(input, retval.start, input.LT(-1), re);
    } finally {
      // do for sure before leaving
    }
    return retval;
  }
  // $ANTLR end "exceptionGroup"


  public static class exceptionHandler_return extends ParserRuleReturnScope {
    GrammarAST tree;

    @Override
    public GrammarAST getTree() {
      return tree;
    }
  }

  ;


  // $ANTLR start "exceptionHandler"
  // org/antlr/v4/parse/ANTLRParser.g:383:1: exceptionHandler : CATCH ARG_ACTION ACTION -> ^( CATCH ARG_ACTION ACTION ) ;
  public final ANTLRParser.exceptionHandler_return exceptionHandler() throws RecognitionException {
    ANTLRParser.exceptionHandler_return retval = new ANTLRParser.exceptionHandler_return();
    retval.start = input.LT(1);

    GrammarAST root_0 = null;

    Token CATCH86 = null;
    Token ARG_ACTION87 = null;
    Token ACTION88 = null;

    GrammarAST CATCH86_tree = null;
    GrammarAST ARG_ACTION87_tree = null;
    GrammarAST ACTION88_tree = null;
    RewriteRuleTokenStream stream_ACTION = new RewriteRuleTokenStream(adaptor, "token ACTION");
    RewriteRuleTokenStream stream_CATCH = new RewriteRuleTokenStream(adaptor, "token CATCH");
    RewriteRuleTokenStream stream_ARG_ACTION = new RewriteRuleTokenStream(adaptor, "token ARG_ACTION");

    try {
      // org/antlr/v4/parse/ANTLRParser.g:384:2: ( CATCH ARG_ACTION ACTION -> ^( CATCH ARG_ACTION ACTION ) )
      // org/antlr/v4/parse/ANTLRParser.g:384:4: CATCH ARG_ACTION ACTION
      {
        CATCH86 = (Token) match(input, CATCH, FOLLOW_CATCH_in_exceptionHandler1781);
        stream_CATCH.add(CATCH86);

        ARG_ACTION87 = (Token) match(input, ARG_ACTION, FOLLOW_ARG_ACTION_in_exceptionHandler1783);
        stream_ARG_ACTION.add(ARG_ACTION87);

        ACTION88 = (Token) match(input, ACTION, FOLLOW_ACTION_in_exceptionHandler1785);
        stream_ACTION.add(ACTION88);


        // AST REWRITE
        // elements: ARG_ACTION, CATCH, ACTION
        // token labels:
        // rule labels: retval
        // token list labels:
        // rule list labels:
        // wildcard labels:
        retval.tree = root_0;
        RewriteRuleSubtreeStream stream_retval = new RewriteRuleSubtreeStream(adaptor, "rule retval", retval != null ? retval.getTree() : null);

        root_0 = (GrammarAST) adaptor.nil();
        // 384:28: -> ^( CATCH ARG_ACTION ACTION )
        {
          // org/antlr/v4/parse/ANTLRParser.g:384:31: ^( CATCH ARG_ACTION ACTION )
          {
            GrammarAST root_1 = (GrammarAST) adaptor.nil();
            root_1 = (GrammarAST) adaptor.becomeRoot(stream_CATCH.nextNode(), root_1);
            adaptor.addChild(root_1, new ActionAST(stream_ARG_ACTION.nextToken()));
            adaptor.addChild(root_1, new ActionAST(stream_ACTION.nextToken()));
            adaptor.addChild(root_0, root_1);
          }

        }


        retval.tree = root_0;

      }

      retval.stop = input.LT(-1);

      retval.tree = (GrammarAST) adaptor.rulePostProcessing(root_0);
      adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

    } catch (RecognitionException re) {
      reportError(re);
      recover(input, re);
      retval.tree = (GrammarAST) adaptor.errorNode(input, retval.start, input.LT(-1), re);
    } finally {
      // do for sure before leaving
    }
    return retval;
  }
  // $ANTLR end "exceptionHandler"


  public static class finallyClause_return extends ParserRuleReturnScope {
    GrammarAST tree;

    @Override
    public GrammarAST getTree() {
      return tree;
    }
  }

  ;


  // $ANTLR start "finallyClause"
  // org/antlr/v4/parse/ANTLRParser.g:387:1: finallyClause : FINALLY ACTION -> ^( FINALLY ACTION ) ;
  public final ANTLRParser.finallyClause_return finallyClause() throws RecognitionException {
    ANTLRParser.finallyClause_return retval = new ANTLRParser.finallyClause_return();
    retval.start = input.LT(1);

    GrammarAST root_0 = null;

    Token FINALLY89 = null;
    Token ACTION90 = null;

    GrammarAST FINALLY89_tree = null;
    GrammarAST ACTION90_tree = null;
    RewriteRuleTokenStream stream_ACTION = new RewriteRuleTokenStream(adaptor, "token ACTION");
    RewriteRuleTokenStream stream_FINALLY = new RewriteRuleTokenStream(adaptor, "token FINALLY");

    try {
      // org/antlr/v4/parse/ANTLRParser.g:388:2: ( FINALLY ACTION -> ^( FINALLY ACTION ) )
      // org/antlr/v4/parse/ANTLRParser.g:388:4: FINALLY ACTION
      {
        FINALLY89 = (Token) match(input, FINALLY, FOLLOW_FINALLY_in_finallyClause1812);
        stream_FINALLY.add(FINALLY89);

        ACTION90 = (Token) match(input, ACTION, FOLLOW_ACTION_in_finallyClause1814);
        stream_ACTION.add(ACTION90);


        // AST REWRITE
        // elements: FINALLY, ACTION
        // token labels:
        // rule labels: retval
        // token list labels:
        // rule list labels:
        // wildcard labels:
        retval.tree = root_0;
        RewriteRuleSubtreeStream stream_retval = new RewriteRuleSubtreeStream(adaptor, "rule retval", retval != null ? retval.getTree() : null);

        root_0 = (GrammarAST) adaptor.nil();
        // 388:19: -> ^( FINALLY ACTION )
        {
          // org/antlr/v4/parse/ANTLRParser.g:388:22: ^( FINALLY ACTION )
          {
            GrammarAST root_1 = (GrammarAST) adaptor.nil();
            root_1 = (GrammarAST) adaptor.becomeRoot(stream_FINALLY.nextNode(), root_1);
            adaptor.addChild(root_1, new ActionAST(stream_ACTION.nextToken()));
            adaptor.addChild(root_0, root_1);
          }

        }


        retval.tree = root_0;

      }

      retval.stop = input.LT(-1);

      retval.tree = (GrammarAST) adaptor.rulePostProcessing(root_0);
      adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

    } catch (RecognitionException re) {
      reportError(re);
      recover(input, re);
      retval.tree = (GrammarAST) adaptor.errorNode(input, retval.start, input.LT(-1), re);
    } finally {
      // do for sure before leaving
    }
    return retval;
  }
  // $ANTLR end "finallyClause"


  public static class rulePrequels_return extends ParserRuleReturnScope {
    GrammarAST tree;

    @Override
    public GrammarAST getTree() {
      return tree;
    }
  }

  ;


  // $ANTLR start "rulePrequels"
  // org/antlr/v4/parse/ANTLRParser.g:391:1: rulePrequels : sync ( rulePrequel sync )* -> ( rulePrequel )* ;
  public final ANTLRParser.rulePrequels_return rulePrequels() throws RecognitionException {
    ANTLRParser.rulePrequels_return retval = new ANTLRParser.rulePrequels_return();
    retval.start = input.LT(1);

    GrammarAST root_0 = null;

    ParserRuleReturnScope sync91 = null;
    ParserRuleReturnScope rulePrequel92 = null;
    ParserRuleReturnScope sync93 = null;

    RewriteRuleSubtreeStream stream_sync = new RewriteRuleSubtreeStream(adaptor, "rule sync");
    RewriteRuleSubtreeStream stream_rulePrequel = new RewriteRuleSubtreeStream(adaptor, "rule rulePrequel");

    paraphrases.push("matching rule preamble");
    try {
      // org/antlr/v4/parse/ANTLRParser.g:394:2: ( sync ( rulePrequel sync )* -> ( rulePrequel )* )
      // org/antlr/v4/parse/ANTLRParser.g:394:4: sync ( rulePrequel sync )*
      {
        pushFollow(FOLLOW_sync_in_rulePrequels1846);
        sync91 = sync();
        state._fsp--;

        stream_sync.add(sync91.getTree());
        // org/antlr/v4/parse/ANTLRParser.g:394:9: ( rulePrequel sync )*
        loop28:
        while (true) {
          int alt28 = 2;
          int LA28_0 = input.LA(1);
          if ((LA28_0 == AT || LA28_0 == OPTIONS)) {
            alt28 = 1;
          }

          switch (alt28) {
            case 1:
              // org/antlr/v4/parse/ANTLRParser.g:394:10: rulePrequel sync
            {
              pushFollow(FOLLOW_rulePrequel_in_rulePrequels1849);
              rulePrequel92 = rulePrequel();
              state._fsp--;

              stream_rulePrequel.add(rulePrequel92.getTree());
              pushFollow(FOLLOW_sync_in_rulePrequels1851);
              sync93 = sync();
              state._fsp--;

              stream_sync.add(sync93.getTree());
            }
            break;

            default:
              break loop28;
          }
        }


        // AST REWRITE
        // elements: rulePrequel
        // token labels:
        // rule labels: retval
        // token list labels:
        // rule list labels:
        // wildcard labels:
        retval.tree = root_0;
        RewriteRuleSubtreeStream stream_retval = new RewriteRuleSubtreeStream(adaptor, "rule retval", retval != null ? retval.getTree() : null);

        root_0 = (GrammarAST) adaptor.nil();
        // 394:29: -> ( rulePrequel )*
        {
          // org/antlr/v4/parse/ANTLRParser.g:394:32: ( rulePrequel )*
          while (stream_rulePrequel.hasNext()) {
            adaptor.addChild(root_0, stream_rulePrequel.nextTree());
          }
          stream_rulePrequel.reset();

        }


        retval.tree = root_0;

      }

      retval.stop = input.LT(-1);

      retval.tree = (GrammarAST) adaptor.rulePostProcessing(root_0);
      adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

      paraphrases.pop();
    } catch (RecognitionException re) {
      reportError(re);
      recover(input, re);
      retval.tree = (GrammarAST) adaptor.errorNode(input, retval.start, input.LT(-1), re);
    } finally {
      // do for sure before leaving
    }
    return retval;
  }
  // $ANTLR end "rulePrequels"


  public static class rulePrequel_return extends ParserRuleReturnScope {
    GrammarAST tree;

    @Override
    public GrammarAST getTree() {
      return tree;
    }
  }

  ;


  // $ANTLR start "rulePrequel"
  // org/antlr/v4/parse/ANTLRParser.g:400:1: rulePrequel : ( optionsSpec | ruleAction );
  public final ANTLRParser.rulePrequel_return rulePrequel() throws RecognitionException {
    ANTLRParser.rulePrequel_return retval = new ANTLRParser.rulePrequel_return();
    retval.start = input.LT(1);

    GrammarAST root_0 = null;

    ParserRuleReturnScope optionsSpec94 = null;
    ParserRuleReturnScope ruleAction95 = null;


    try {
      // org/antlr/v4/parse/ANTLRParser.g:401:5: ( optionsSpec | ruleAction )
      int alt29 = 2;
      int LA29_0 = input.LA(1);
      if ((LA29_0 == OPTIONS)) {
        alt29 = 1;
      } else if ((LA29_0 == AT)) {
        alt29 = 2;
      } else {
        NoViableAltException nvae =
          new NoViableAltException("", 29, 0, input);
        throw nvae;
      }

      switch (alt29) {
        case 1:
          // org/antlr/v4/parse/ANTLRParser.g:401:7: optionsSpec
        {
          root_0 = (GrammarAST) adaptor.nil();


          pushFollow(FOLLOW_optionsSpec_in_rulePrequel1875);
          optionsSpec94 = optionsSpec();
          state._fsp--;

          adaptor.addChild(root_0, optionsSpec94.getTree());

        }
        break;
        case 2:
          // org/antlr/v4/parse/ANTLRParser.g:402:7: ruleAction
        {
          root_0 = (GrammarAST) adaptor.nil();


          pushFollow(FOLLOW_ruleAction_in_rulePrequel1883);
          ruleAction95 = ruleAction();
          state._fsp--;

          adaptor.addChild(root_0, ruleAction95.getTree());

        }
        break;

      }
      retval.stop = input.LT(-1);

      retval.tree = (GrammarAST) adaptor.rulePostProcessing(root_0);
      adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

    } catch (RecognitionException re) {
      reportError(re);
      recover(input, re);
      retval.tree = (GrammarAST) adaptor.errorNode(input, retval.start, input.LT(-1), re);
    } finally {
      // do for sure before leaving
    }
    return retval;
  }
  // $ANTLR end "rulePrequel"


  public static class ruleReturns_return extends ParserRuleReturnScope {
    GrammarAST tree;

    @Override
    public GrammarAST getTree() {
      return tree;
    }
  }

  ;


  // $ANTLR start "ruleReturns"
  // org/antlr/v4/parse/ANTLRParser.g:411:1: ruleReturns : RETURNS ^ ARG_ACTION ;
  public final ANTLRParser.ruleReturns_return ruleReturns() throws RecognitionException {
    ANTLRParser.ruleReturns_return retval = new ANTLRParser.ruleReturns_return();
    retval.start = input.LT(1);

    GrammarAST root_0 = null;

    Token RETURNS96 = null;
    Token ARG_ACTION97 = null;

    GrammarAST RETURNS96_tree = null;
    GrammarAST ARG_ACTION97_tree = null;

    try {
      // org/antlr/v4/parse/ANTLRParser.g:412:2: ( RETURNS ^ ARG_ACTION )
      // org/antlr/v4/parse/ANTLRParser.g:412:4: RETURNS ^ ARG_ACTION
      {
        root_0 = (GrammarAST) adaptor.nil();


        RETURNS96 = (Token) match(input, RETURNS, FOLLOW_RETURNS_in_ruleReturns1903);
        RETURNS96_tree = (GrammarAST) adaptor.create(RETURNS96);
        root_0 = (GrammarAST) adaptor.becomeRoot(RETURNS96_tree, root_0);

        ARG_ACTION97 = (Token) match(input, ARG_ACTION, FOLLOW_ARG_ACTION_in_ruleReturns1906);
        ARG_ACTION97_tree = new ActionAST(ARG_ACTION97);
        adaptor.addChild(root_0, ARG_ACTION97_tree);

      }

      retval.stop = input.LT(-1);

      retval.tree = (GrammarAST) adaptor.rulePostProcessing(root_0);
      adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

    } catch (RecognitionException re) {
      reportError(re);
      recover(input, re);
      retval.tree = (GrammarAST) adaptor.errorNode(input, retval.start, input.LT(-1), re);
    } finally {
      // do for sure before leaving
    }
    return retval;
  }
  // $ANTLR end "ruleReturns"


  public static class throwsSpec_return extends ParserRuleReturnScope {
    GrammarAST tree;

    @Override
    public GrammarAST getTree() {
      return tree;
    }
  }

  ;


  // $ANTLR start "throwsSpec"
  // org/antlr/v4/parse/ANTLRParser.g:426:1: throwsSpec : THROWS qid ( COMMA qid )* -> ^( THROWS ( qid )+ ) ;
  public final ANTLRParser.throwsSpec_return throwsSpec() throws RecognitionException {
    ANTLRParser.throwsSpec_return retval = new ANTLRParser.throwsSpec_return();
    retval.start = input.LT(1);

    GrammarAST root_0 = null;

    Token THROWS98 = null;
    Token COMMA100 = null;
    ParserRuleReturnScope qid99 = null;
    ParserRuleReturnScope qid101 = null;

    GrammarAST THROWS98_tree = null;
    GrammarAST COMMA100_tree = null;
    RewriteRuleTokenStream stream_COMMA = new RewriteRuleTokenStream(adaptor, "token COMMA");
    RewriteRuleTokenStream stream_THROWS = new RewriteRuleTokenStream(adaptor, "token THROWS");
    RewriteRuleSubtreeStream stream_qid = new RewriteRuleSubtreeStream(adaptor, "rule qid");

    try {
      // org/antlr/v4/parse/ANTLRParser.g:427:5: ( THROWS qid ( COMMA qid )* -> ^( THROWS ( qid )+ ) )
      // org/antlr/v4/parse/ANTLRParser.g:427:7: THROWS qid ( COMMA qid )*
      {
        THROWS98 = (Token) match(input, THROWS, FOLLOW_THROWS_in_throwsSpec1934);
        stream_THROWS.add(THROWS98);

        pushFollow(FOLLOW_qid_in_throwsSpec1936);
        qid99 = qid();
        state._fsp--;

        stream_qid.add(qid99.getTree());
        // org/antlr/v4/parse/ANTLRParser.g:427:18: ( COMMA qid )*
        loop30:
        while (true) {
          int alt30 = 2;
          int LA30_0 = input.LA(1);
          if ((LA30_0 == COMMA)) {
            alt30 = 1;
          }

          switch (alt30) {
            case 1:
              // org/antlr/v4/parse/ANTLRParser.g:427:19: COMMA qid
            {
              COMMA100 = (Token) match(input, COMMA, FOLLOW_COMMA_in_throwsSpec1939);
              stream_COMMA.add(COMMA100);

              pushFollow(FOLLOW_qid_in_throwsSpec1941);
              qid101 = qid();
              state._fsp--;

              stream_qid.add(qid101.getTree());
            }
            break;

            default:
              break loop30;
          }
        }


        // AST REWRITE
        // elements: THROWS, qid
        // token labels:
        // rule labels: retval
        // token list labels:
        // rule list labels:
        // wildcard labels:
        retval.tree = root_0;
        RewriteRuleSubtreeStream stream_retval = new RewriteRuleSubtreeStream(adaptor, "rule retval", retval != null ? retval.getTree() : null);

        root_0 = (GrammarAST) adaptor.nil();
        // 427:31: -> ^( THROWS ( qid )+ )
        {
          // org/antlr/v4/parse/ANTLRParser.g:427:34: ^( THROWS ( qid )+ )
          {
            GrammarAST root_1 = (GrammarAST) adaptor.nil();
            root_1 = (GrammarAST) adaptor.becomeRoot(stream_THROWS.nextNode(), root_1);
            if (!(stream_qid.hasNext())) {
              throw new RewriteEarlyExitException();
            }
            while (stream_qid.hasNext()) {
              adaptor.addChild(root_1, stream_qid.nextTree());
            }
            stream_qid.reset();

            adaptor.addChild(root_0, root_1);
          }

        }


        retval.tree = root_0;

      }

      retval.stop = input.LT(-1);

      retval.tree = (GrammarAST) adaptor.rulePostProcessing(root_0);
      adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

    } catch (RecognitionException re) {
      reportError(re);
      recover(input, re);
      retval.tree = (GrammarAST) adaptor.errorNode(input, retval.start, input.LT(-1), re);
    } finally {
      // do for sure before leaving
    }
    return retval;
  }
  // $ANTLR end "throwsSpec"


  public static class localsSpec_return extends ParserRuleReturnScope {
    GrammarAST tree;

    @Override
    public GrammarAST getTree() {
      return tree;
    }
  }

  ;


  // $ANTLR start "localsSpec"
  // org/antlr/v4/parse/ANTLRParser.g:431:1: localsSpec : LOCALS ^ ARG_ACTION ;
  public final ANTLRParser.localsSpec_return localsSpec() throws RecognitionException {
    ANTLRParser.localsSpec_return retval = new ANTLRParser.localsSpec_return();
    retval.start = input.LT(1);

    GrammarAST root_0 = null;

    Token LOCALS102 = null;
    Token ARG_ACTION103 = null;

    GrammarAST LOCALS102_tree = null;
    GrammarAST ARG_ACTION103_tree = null;

    try {
      // org/antlr/v4/parse/ANTLRParser.g:431:12: ( LOCALS ^ ARG_ACTION )
      // org/antlr/v4/parse/ANTLRParser.g:431:14: LOCALS ^ ARG_ACTION
      {
        root_0 = (GrammarAST) adaptor.nil();


        LOCALS102 = (Token) match(input, LOCALS, FOLLOW_LOCALS_in_localsSpec1966);
        LOCALS102_tree = (GrammarAST) adaptor.create(LOCALS102);
        root_0 = (GrammarAST) adaptor.becomeRoot(LOCALS102_tree, root_0);

        ARG_ACTION103 = (Token) match(input, ARG_ACTION, FOLLOW_ARG_ACTION_in_localsSpec1969);
        ARG_ACTION103_tree = new ActionAST(ARG_ACTION103);
        adaptor.addChild(root_0, ARG_ACTION103_tree);

      }

      retval.stop = input.LT(-1);

      retval.tree = (GrammarAST) adaptor.rulePostProcessing(root_0);
      adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

    } catch (RecognitionException re) {
      reportError(re);
      recover(input, re);
      retval.tree = (GrammarAST) adaptor.errorNode(input, retval.start, input.LT(-1), re);
    } finally {
      // do for sure before leaving
    }
    return retval;
  }
  // $ANTLR end "localsSpec"


  public static class ruleAction_return extends ParserRuleReturnScope {
    GrammarAST tree;

    @Override
    public GrammarAST getTree() {
      return tree;
    }
  }

  ;


  // $ANTLR start "ruleAction"
  // org/antlr/v4/parse/ANTLRParser.g:442:1: ruleAction : AT id ACTION -> ^( AT id ACTION ) ;
  public final ANTLRParser.ruleAction_return ruleAction() throws RecognitionException {
    ANTLRParser.ruleAction_return retval = new ANTLRParser.ruleAction_return();
    retval.start = input.LT(1);

    GrammarAST root_0 = null;

    Token AT104 = null;
    Token ACTION106 = null;
    ParserRuleReturnScope id105 = null;

    GrammarAST AT104_tree = null;
    GrammarAST ACTION106_tree = null;
    RewriteRuleTokenStream stream_AT = new RewriteRuleTokenStream(adaptor, "token AT");
    RewriteRuleTokenStream stream_ACTION = new RewriteRuleTokenStream(adaptor, "token ACTION");
    RewriteRuleSubtreeStream stream_id = new RewriteRuleSubtreeStream(adaptor, "rule id");

    try {
      // org/antlr/v4/parse/ANTLRParser.g:443:2: ( AT id ACTION -> ^( AT id ACTION ) )
      // org/antlr/v4/parse/ANTLRParser.g:443:4: AT id ACTION
      {
        AT104 = (Token) match(input, AT, FOLLOW_AT_in_ruleAction1992);
        stream_AT.add(AT104);

        pushFollow(FOLLOW_id_in_ruleAction1994);
        id105 = id();
        state._fsp--;

        stream_id.add(id105.getTree());
        ACTION106 = (Token) match(input, ACTION, FOLLOW_ACTION_in_ruleAction1996);
        stream_ACTION.add(ACTION106);


        // AST REWRITE
        // elements: id, AT, ACTION
        // token labels:
        // rule labels: retval
        // token list labels:
        // rule list labels:
        // wildcard labels:
        retval.tree = root_0;
        RewriteRuleSubtreeStream stream_retval = new RewriteRuleSubtreeStream(adaptor, "rule retval", retval != null ? retval.getTree() : null);

        root_0 = (GrammarAST) adaptor.nil();
        // 443:17: -> ^( AT id ACTION )
        {
          // org/antlr/v4/parse/ANTLRParser.g:443:20: ^( AT id ACTION )
          {
            GrammarAST root_1 = (GrammarAST) adaptor.nil();
            root_1 = (GrammarAST) adaptor.becomeRoot(stream_AT.nextNode(), root_1);
            adaptor.addChild(root_1, stream_id.nextTree());
            adaptor.addChild(root_1, new ActionAST(stream_ACTION.nextToken()));
            adaptor.addChild(root_0, root_1);
          }

        }


        retval.tree = root_0;

      }

      retval.stop = input.LT(-1);

      retval.tree = (GrammarAST) adaptor.rulePostProcessing(root_0);
      adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

    } catch (RecognitionException re) {
      reportError(re);
      recover(input, re);
      retval.tree = (GrammarAST) adaptor.errorNode(input, retval.start, input.LT(-1), re);
    } finally {
      // do for sure before leaving
    }
    return retval;
  }
  // $ANTLR end "ruleAction"


  public static class ruleBlock_return extends ParserRuleReturnScope {
    GrammarAST tree;

    @Override
    public GrammarAST getTree() {
      return tree;
    }
  }

  ;


  // $ANTLR start "ruleBlock"
  // org/antlr/v4/parse/ANTLRParser.g:452:1: ruleBlock : ruleAltList -> ^( BLOCK[colon,\"BLOCK\"] ruleAltList ) ;
  public final ANTLRParser.ruleBlock_return ruleBlock() throws RecognitionException {
    ANTLRParser.ruleBlock_return retval = new ANTLRParser.ruleBlock_return();
    retval.start = input.LT(1);

    GrammarAST root_0 = null;

    ParserRuleReturnScope ruleAltList107 = null;

    RewriteRuleSubtreeStream stream_ruleAltList = new RewriteRuleSubtreeStream(adaptor, "rule ruleAltList");

    Token colon = input.LT(-1);
    try {
      // org/antlr/v4/parse/ANTLRParser.g:454:5: ( ruleAltList -> ^( BLOCK[colon,\"BLOCK\"] ruleAltList ) )
      // org/antlr/v4/parse/ANTLRParser.g:454:7: ruleAltList
      {
        pushFollow(FOLLOW_ruleAltList_in_ruleBlock2034);
        ruleAltList107 = ruleAltList();
        state._fsp--;

        stream_ruleAltList.add(ruleAltList107.getTree());

        // AST REWRITE
        // elements: ruleAltList
        // token labels:
        // rule labels: retval
        // token list labels:
        // rule list labels:
        // wildcard labels:
        retval.tree = root_0;
        RewriteRuleSubtreeStream stream_retval = new RewriteRuleSubtreeStream(adaptor, "rule retval", retval != null ? retval.getTree() : null);

        root_0 = (GrammarAST) adaptor.nil();
        // 454:19: -> ^( BLOCK[colon,\"BLOCK\"] ruleAltList )
        {
          // org/antlr/v4/parse/ANTLRParser.g:454:22: ^( BLOCK[colon,\"BLOCK\"] ruleAltList )
          {
            GrammarAST root_1 = (GrammarAST) adaptor.nil();
            root_1 = (GrammarAST) adaptor.becomeRoot(new BlockAST(BLOCK, colon, "BLOCK"), root_1);
            adaptor.addChild(root_1, stream_ruleAltList.nextTree());
            adaptor.addChild(root_0, root_1);
          }

        }


        retval.tree = root_0;

      }

      retval.stop = input.LT(-1);

      retval.tree = (GrammarAST) adaptor.rulePostProcessing(root_0);
      adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

    } catch (ResyncToEndOfRuleBlock e) {

      // just resyncing; ignore error
      retval.tree = (GrammarAST) adaptor.errorNode(input, retval.start, input.LT(-1), null);

    } finally {
      // do for sure before leaving
    }
    return retval;
  }
  // $ANTLR end "ruleBlock"


  public static class ruleAltList_return extends ParserRuleReturnScope {
    GrammarAST tree;

    @Override
    public GrammarAST getTree() {
      return tree;
    }
  }

  ;


  // $ANTLR start "ruleAltList"
  // org/antlr/v4/parse/ANTLRParser.g:461:1: ruleAltList : labeledAlt ( OR labeledAlt )* -> ( labeledAlt )+ ;
  public final ANTLRParser.ruleAltList_return ruleAltList() throws RecognitionException {
    ANTLRParser.ruleAltList_return retval = new ANTLRParser.ruleAltList_return();
    retval.start = input.LT(1);

    GrammarAST root_0 = null;

    Token OR109 = null;
    ParserRuleReturnScope labeledAlt108 = null;
    ParserRuleReturnScope labeledAlt110 = null;

    GrammarAST OR109_tree = null;
    RewriteRuleTokenStream stream_OR = new RewriteRuleTokenStream(adaptor, "token OR");
    RewriteRuleSubtreeStream stream_labeledAlt = new RewriteRuleSubtreeStream(adaptor, "rule labeledAlt");

    try {
      // org/antlr/v4/parse/ANTLRParser.g:462:2: ( labeledAlt ( OR labeledAlt )* -> ( labeledAlt )+ )
      // org/antlr/v4/parse/ANTLRParser.g:462:4: labeledAlt ( OR labeledAlt )*
      {
        pushFollow(FOLLOW_labeledAlt_in_ruleAltList2070);
        labeledAlt108 = labeledAlt();
        state._fsp--;

        stream_labeledAlt.add(labeledAlt108.getTree());
        // org/antlr/v4/parse/ANTLRParser.g:462:15: ( OR labeledAlt )*
        loop31:
        while (true) {
          int alt31 = 2;
          int LA31_0 = input.LA(1);
          if ((LA31_0 == OR)) {
            alt31 = 1;
          }

          switch (alt31) {
            case 1:
              // org/antlr/v4/parse/ANTLRParser.g:462:16: OR labeledAlt
            {
              OR109 = (Token) match(input, OR, FOLLOW_OR_in_ruleAltList2073);
              stream_OR.add(OR109);

              pushFollow(FOLLOW_labeledAlt_in_ruleAltList2075);
              labeledAlt110 = labeledAlt();
              state._fsp--;

              stream_labeledAlt.add(labeledAlt110.getTree());
            }
            break;

            default:
              break loop31;
          }
        }


        // AST REWRITE
        // elements: labeledAlt
        // token labels:
        // rule labels: retval
        // token list labels:
        // rule list labels:
        // wildcard labels:
        retval.tree = root_0;
        RewriteRuleSubtreeStream stream_retval = new RewriteRuleSubtreeStream(adaptor, "rule retval", retval != null ? retval.getTree() : null);

        root_0 = (GrammarAST) adaptor.nil();
        // 462:32: -> ( labeledAlt )+
        {
          if (!(stream_labeledAlt.hasNext())) {
            throw new RewriteEarlyExitException();
          }
          while (stream_labeledAlt.hasNext()) {
            adaptor.addChild(root_0, stream_labeledAlt.nextTree());
          }
          stream_labeledAlt.reset();

        }


        retval.tree = root_0;

      }

      retval.stop = input.LT(-1);

      retval.tree = (GrammarAST) adaptor.rulePostProcessing(root_0);
      adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

    } catch (RecognitionException re) {
      reportError(re);
      recover(input, re);
      retval.tree = (GrammarAST) adaptor.errorNode(input, retval.start, input.LT(-1), re);
    } finally {
      // do for sure before leaving
    }
    return retval;
  }
  // $ANTLR end "ruleAltList"


  public static class labeledAlt_return extends ParserRuleReturnScope {
    GrammarAST tree;

    @Override
    public GrammarAST getTree() {
      return tree;
    }
  }

  ;


  // $ANTLR start "labeledAlt"
  // org/antlr/v4/parse/ANTLRParser.g:465:1: labeledAlt : alternative ( POUND ! id !)? ;
  public final ANTLRParser.labeledAlt_return labeledAlt() throws RecognitionException {
    ANTLRParser.labeledAlt_return retval = new ANTLRParser.labeledAlt_return();
    retval.start = input.LT(1);

    GrammarAST root_0 = null;

    Token POUND112 = null;
    ParserRuleReturnScope alternative111 = null;
    ParserRuleReturnScope id113 = null;

    GrammarAST POUND112_tree = null;

    try {
      // org/antlr/v4/parse/ANTLRParser.g:466:2: ( alternative ( POUND ! id !)? )
      // org/antlr/v4/parse/ANTLRParser.g:466:4: alternative ( POUND ! id !)?
      {
        root_0 = (GrammarAST) adaptor.nil();


        pushFollow(FOLLOW_alternative_in_labeledAlt2093);
        alternative111 = alternative();
        state._fsp--;

        adaptor.addChild(root_0, alternative111.getTree());

        // org/antlr/v4/parse/ANTLRParser.g:467:3: ( POUND ! id !)?
        int alt32 = 2;
        int LA32_0 = input.LA(1);
        if ((LA32_0 == POUND)) {
          alt32 = 1;
        }
        switch (alt32) {
          case 1:
            // org/antlr/v4/parse/ANTLRParser.g:467:5: POUND ! id !
          {
            POUND112 = (Token) match(input, POUND, FOLLOW_POUND_in_labeledAlt2099);
            pushFollow(FOLLOW_id_in_labeledAlt2102);
            id113 = id();
            state._fsp--;

            ((AltAST) (alternative111 != null ? ((GrammarAST) alternative111.getTree()) : null)).altLabel = (id113 != null ? ((GrammarAST) id113.getTree()) : null);
          }
          break;

        }

      }

      retval.stop = input.LT(-1);

      retval.tree = (GrammarAST) adaptor.rulePostProcessing(root_0);
      adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

    } catch (RecognitionException re) {
      reportError(re);
      recover(input, re);
      retval.tree = (GrammarAST) adaptor.errorNode(input, retval.start, input.LT(-1), re);
    } finally {
      // do for sure before leaving
    }
    return retval;
  }
  // $ANTLR end "labeledAlt"


  public static class lexerRule_return extends ParserRuleReturnScope {
    GrammarAST tree;

    @Override
    public GrammarAST getTree() {
      return tree;
    }
  }

  ;


  // $ANTLR start "lexerRule"
  // org/antlr/v4/parse/ANTLRParser.g:471:1: lexerRule : ( FRAGMENT )? TOKEN_REF COLON lexerRuleBlock SEMI -> ^( RULE TOKEN_REF ( ^( RULEMODIFIERS FRAGMENT ) )? lexerRuleBlock ) ;
  public final ANTLRParser.lexerRule_return lexerRule() throws RecognitionException {
    ANTLRParser.lexerRule_return retval = new ANTLRParser.lexerRule_return();
    retval.start = input.LT(1);

    GrammarAST root_0 = null;

    Token FRAGMENT114 = null;
    Token TOKEN_REF115 = null;
    Token COLON116 = null;
    Token SEMI118 = null;
    ParserRuleReturnScope lexerRuleBlock117 = null;

    GrammarAST FRAGMENT114_tree = null;
    GrammarAST TOKEN_REF115_tree = null;
    GrammarAST COLON116_tree = null;
    GrammarAST SEMI118_tree = null;
    RewriteRuleTokenStream stream_TOKEN_REF = new RewriteRuleTokenStream(adaptor, "token TOKEN_REF");
    RewriteRuleTokenStream stream_FRAGMENT = new RewriteRuleTokenStream(adaptor, "token FRAGMENT");
    RewriteRuleTokenStream stream_SEMI = new RewriteRuleTokenStream(adaptor, "token SEMI");
    RewriteRuleTokenStream stream_COLON = new RewriteRuleTokenStream(adaptor, "token COLON");
    RewriteRuleSubtreeStream stream_lexerRuleBlock = new RewriteRuleSubtreeStream(adaptor, "rule lexerRuleBlock");

    paraphrases.push("matching a lexer rule");
    try {
      // org/antlr/v4/parse/ANTLRParser.g:476:5: ( ( FRAGMENT )? TOKEN_REF COLON lexerRuleBlock SEMI -> ^( RULE TOKEN_REF ( ^( RULEMODIFIERS FRAGMENT ) )? lexerRuleBlock ) )
      // org/antlr/v4/parse/ANTLRParser.g:476:7: ( FRAGMENT )? TOKEN_REF COLON lexerRuleBlock SEMI
      {
        // org/antlr/v4/parse/ANTLRParser.g:476:7: ( FRAGMENT )?
        int alt33 = 2;
        int LA33_0 = input.LA(1);
        if ((LA33_0 == FRAGMENT)) {
          alt33 = 1;
        }
        switch (alt33) {
          case 1:
            // org/antlr/v4/parse/ANTLRParser.g:476:7: FRAGMENT
          {
            FRAGMENT114 = (Token) match(input, FRAGMENT, FOLLOW_FRAGMENT_in_lexerRule2134);
            stream_FRAGMENT.add(FRAGMENT114);

          }
          break;

        }

        TOKEN_REF115 = (Token) match(input, TOKEN_REF, FOLLOW_TOKEN_REF_in_lexerRule2140);
        stream_TOKEN_REF.add(TOKEN_REF115);

        COLON116 = (Token) match(input, COLON, FOLLOW_COLON_in_lexerRule2142);
        stream_COLON.add(COLON116);

        pushFollow(FOLLOW_lexerRuleBlock_in_lexerRule2144);
        lexerRuleBlock117 = lexerRuleBlock();
        state._fsp--;

        stream_lexerRuleBlock.add(lexerRuleBlock117.getTree());
        SEMI118 = (Token) match(input, SEMI, FOLLOW_SEMI_in_lexerRule2146);
        stream_SEMI.add(SEMI118);


        // AST REWRITE
        // elements: TOKEN_REF, FRAGMENT, lexerRuleBlock
        // token labels:
        // rule labels: retval
        // token list labels:
        // rule list labels:
        // wildcard labels:
        retval.tree = root_0;
        RewriteRuleSubtreeStream stream_retval = new RewriteRuleSubtreeStream(adaptor, "rule retval", retval != null ? retval.getTree() : null);

        root_0 = (GrammarAST) adaptor.nil();
        // 478:7: -> ^( RULE TOKEN_REF ( ^( RULEMODIFIERS FRAGMENT ) )? lexerRuleBlock )
        {
          // org/antlr/v4/parse/ANTLRParser.g:478:10: ^( RULE TOKEN_REF ( ^( RULEMODIFIERS FRAGMENT ) )? lexerRuleBlock )
          {
            GrammarAST root_1 = (GrammarAST) adaptor.nil();
            root_1 = (GrammarAST) adaptor.becomeRoot(new RuleAST(RULE), root_1);
            adaptor.addChild(root_1, stream_TOKEN_REF.nextNode());
            // org/antlr/v4/parse/ANTLRParser.g:479:9: ( ^( RULEMODIFIERS FRAGMENT ) )?
            if (stream_FRAGMENT.hasNext()) {
              // org/antlr/v4/parse/ANTLRParser.g:479:9: ^( RULEMODIFIERS FRAGMENT )
              {
                GrammarAST root_2 = (GrammarAST) adaptor.nil();
                root_2 = (GrammarAST) adaptor.becomeRoot((GrammarAST) adaptor.create(RULEMODIFIERS, "RULEMODIFIERS"), root_2);
                adaptor.addChild(root_2, stream_FRAGMENT.nextNode());
                adaptor.addChild(root_1, root_2);
              }

            }
            stream_FRAGMENT.reset();

            adaptor.addChild(root_1, stream_lexerRuleBlock.nextTree());
            adaptor.addChild(root_0, root_1);
          }

        }


        retval.tree = root_0;

      }

      retval.stop = input.LT(-1);

      retval.tree = (GrammarAST) adaptor.rulePostProcessing(root_0);
      adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);


      paraphrases.pop();

    } catch (RecognitionException re) {
      reportError(re);
      recover(input, re);
      retval.tree = (GrammarAST) adaptor.errorNode(input, retval.start, input.LT(-1), re);
    } finally {
      // do for sure before leaving
    }
    return retval;
  }
  // $ANTLR end "lexerRule"


  public static class lexerRuleBlock_return extends ParserRuleReturnScope {
    GrammarAST tree;

    @Override
    public GrammarAST getTree() {
      return tree;
    }
  }

  ;


  // $ANTLR start "lexerRuleBlock"
  // org/antlr/v4/parse/ANTLRParser.g:483:1: lexerRuleBlock : lexerAltList -> ^( BLOCK[colon,\"BLOCK\"] lexerAltList ) ;
  public final ANTLRParser.lexerRuleBlock_return lexerRuleBlock() throws RecognitionException {
    ANTLRParser.lexerRuleBlock_return retval = new ANTLRParser.lexerRuleBlock_return();
    retval.start = input.LT(1);

    GrammarAST root_0 = null;

    ParserRuleReturnScope lexerAltList119 = null;

    RewriteRuleSubtreeStream stream_lexerAltList = new RewriteRuleSubtreeStream(adaptor, "rule lexerAltList");

    Token colon = input.LT(-1);
    try {
      // org/antlr/v4/parse/ANTLRParser.g:485:5: ( lexerAltList -> ^( BLOCK[colon,\"BLOCK\"] lexerAltList ) )
      // org/antlr/v4/parse/ANTLRParser.g:485:7: lexerAltList
      {
        pushFollow(FOLLOW_lexerAltList_in_lexerRuleBlock2210);
        lexerAltList119 = lexerAltList();
        state._fsp--;

        stream_lexerAltList.add(lexerAltList119.getTree());

        // AST REWRITE
        // elements: lexerAltList
        // token labels:
        // rule labels: retval
        // token list labels:
        // rule list labels:
        // wildcard labels:
        retval.tree = root_0;
        RewriteRuleSubtreeStream stream_retval = new RewriteRuleSubtreeStream(adaptor, "rule retval", retval != null ? retval.getTree() : null);

        root_0 = (GrammarAST) adaptor.nil();
        // 485:20: -> ^( BLOCK[colon,\"BLOCK\"] lexerAltList )
        {
          // org/antlr/v4/parse/ANTLRParser.g:485:23: ^( BLOCK[colon,\"BLOCK\"] lexerAltList )
          {
            GrammarAST root_1 = (GrammarAST) adaptor.nil();
            root_1 = (GrammarAST) adaptor.becomeRoot(new BlockAST(BLOCK, colon, "BLOCK"), root_1);
            adaptor.addChild(root_1, stream_lexerAltList.nextTree());
            adaptor.addChild(root_0, root_1);
          }

        }


        retval.tree = root_0;

      }

      retval.stop = input.LT(-1);

      retval.tree = (GrammarAST) adaptor.rulePostProcessing(root_0);
      adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

    } catch (ResyncToEndOfRuleBlock e) {

      // just resyncing; ignore error
      retval.tree = (GrammarAST) adaptor.errorNode(input, retval.start, input.LT(-1), null);

    } finally {
      // do for sure before leaving
    }
    return retval;
  }
  // $ANTLR end "lexerRuleBlock"


  public static class lexerAltList_return extends ParserRuleReturnScope {
    GrammarAST tree;

    @Override
    public GrammarAST getTree() {
      return tree;
    }
  }

  ;


  // $ANTLR start "lexerAltList"
  // org/antlr/v4/parse/ANTLRParser.g:492:1: lexerAltList : lexerAlt ( OR lexerAlt )* -> ( lexerAlt )+ ;
  public final ANTLRParser.lexerAltList_return lexerAltList() throws RecognitionException {
    ANTLRParser.lexerAltList_return retval = new ANTLRParser.lexerAltList_return();
    retval.start = input.LT(1);

    GrammarAST root_0 = null;

    Token OR121 = null;
    ParserRuleReturnScope lexerAlt120 = null;
    ParserRuleReturnScope lexerAlt122 = null;

    GrammarAST OR121_tree = null;
    RewriteRuleTokenStream stream_OR = new RewriteRuleTokenStream(adaptor, "token OR");
    RewriteRuleSubtreeStream stream_lexerAlt = new RewriteRuleSubtreeStream(adaptor, "rule lexerAlt");

    try {
      // org/antlr/v4/parse/ANTLRParser.g:493:2: ( lexerAlt ( OR lexerAlt )* -> ( lexerAlt )+ )
      // org/antlr/v4/parse/ANTLRParser.g:493:4: lexerAlt ( OR lexerAlt )*
      {
        pushFollow(FOLLOW_lexerAlt_in_lexerAltList2246);
        lexerAlt120 = lexerAlt();
        state._fsp--;

        stream_lexerAlt.add(lexerAlt120.getTree());
        // org/antlr/v4/parse/ANTLRParser.g:493:13: ( OR lexerAlt )*
        loop34:
        while (true) {
          int alt34 = 2;
          int LA34_0 = input.LA(1);
          if ((LA34_0 == OR)) {
            alt34 = 1;
          }

          switch (alt34) {
            case 1:
              // org/antlr/v4/parse/ANTLRParser.g:493:14: OR lexerAlt
            {
              OR121 = (Token) match(input, OR, FOLLOW_OR_in_lexerAltList2249);
              stream_OR.add(OR121);

              pushFollow(FOLLOW_lexerAlt_in_lexerAltList2251);
              lexerAlt122 = lexerAlt();
              state._fsp--;

              stream_lexerAlt.add(lexerAlt122.getTree());
            }
            break;

            default:
              break loop34;
          }
        }


        // AST REWRITE
        // elements: lexerAlt
        // token labels:
        // rule labels: retval
        // token list labels:
        // rule list labels:
        // wildcard labels:
        retval.tree = root_0;
        RewriteRuleSubtreeStream stream_retval = new RewriteRuleSubtreeStream(adaptor, "rule retval", retval != null ? retval.getTree() : null);

        root_0 = (GrammarAST) adaptor.nil();
        // 493:28: -> ( lexerAlt )+
        {
          if (!(stream_lexerAlt.hasNext())) {
            throw new RewriteEarlyExitException();
          }
          while (stream_lexerAlt.hasNext()) {
            adaptor.addChild(root_0, stream_lexerAlt.nextTree());
          }
          stream_lexerAlt.reset();

        }


        retval.tree = root_0;

      }

      retval.stop = input.LT(-1);

      retval.tree = (GrammarAST) adaptor.rulePostProcessing(root_0);
      adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

    } catch (RecognitionException re) {
      reportError(re);
      recover(input, re);
      retval.tree = (GrammarAST) adaptor.errorNode(input, retval.start, input.LT(-1), re);
    } finally {
      // do for sure before leaving
    }
    return retval;
  }
  // $ANTLR end "lexerAltList"


  public static class lexerAlt_return extends ParserRuleReturnScope {
    GrammarAST tree;

    @Override
    public GrammarAST getTree() {
      return tree;
    }
  }

  ;


  // $ANTLR start "lexerAlt"
  // org/antlr/v4/parse/ANTLRParser.g:496:1: lexerAlt : lexerElements ( lexerCommands -> ^( LEXER_ALT_ACTION lexerElements lexerCommands ) | -> lexerElements ) ;
  public final ANTLRParser.lexerAlt_return lexerAlt() throws RecognitionException {
    ANTLRParser.lexerAlt_return retval = new ANTLRParser.lexerAlt_return();
    retval.start = input.LT(1);

    GrammarAST root_0 = null;

    ParserRuleReturnScope lexerElements123 = null;
    ParserRuleReturnScope lexerCommands124 = null;

    RewriteRuleSubtreeStream stream_lexerCommands = new RewriteRuleSubtreeStream(adaptor, "rule lexerCommands");
    RewriteRuleSubtreeStream stream_lexerElements = new RewriteRuleSubtreeStream(adaptor, "rule lexerElements");

    try {
      // org/antlr/v4/parse/ANTLRParser.g:497:2: ( lexerElements ( lexerCommands -> ^( LEXER_ALT_ACTION lexerElements lexerCommands ) | -> lexerElements ) )
      // org/antlr/v4/parse/ANTLRParser.g:497:4: lexerElements ( lexerCommands -> ^( LEXER_ALT_ACTION lexerElements lexerCommands ) | -> lexerElements )
      {
        pushFollow(FOLLOW_lexerElements_in_lexerAlt2269);
        lexerElements123 = lexerElements();
        state._fsp--;

        stream_lexerElements.add(lexerElements123.getTree());
        // org/antlr/v4/parse/ANTLRParser.g:498:3: ( lexerCommands -> ^( LEXER_ALT_ACTION lexerElements lexerCommands ) | -> lexerElements )
        int alt35 = 2;
        int LA35_0 = input.LA(1);
        if ((LA35_0 == RARROW)) {
          alt35 = 1;
        } else if ((LA35_0 == OR || LA35_0 == RPAREN || LA35_0 == SEMI)) {
          alt35 = 2;
        } else {
          NoViableAltException nvae =
            new NoViableAltException("", 35, 0, input);
          throw nvae;
        }

        switch (alt35) {
          case 1:
            // org/antlr/v4/parse/ANTLRParser.g:498:5: lexerCommands
          {
            pushFollow(FOLLOW_lexerCommands_in_lexerAlt2275);
            lexerCommands124 = lexerCommands();
            state._fsp--;

            stream_lexerCommands.add(lexerCommands124.getTree());

            // AST REWRITE
            // elements: lexerElements, lexerCommands
            // token labels:
            // rule labels: retval
            // token list labels:
            // rule list labels:
            // wildcard labels:
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval = new RewriteRuleSubtreeStream(adaptor, "rule retval", retval != null ? retval.getTree() : null);

            root_0 = (GrammarAST) adaptor.nil();
            // 498:19: -> ^( LEXER_ALT_ACTION lexerElements lexerCommands )
            {
              // org/antlr/v4/parse/ANTLRParser.g:498:22: ^( LEXER_ALT_ACTION lexerElements lexerCommands )
              {
                GrammarAST root_1 = (GrammarAST) adaptor.nil();
                root_1 = (GrammarAST) adaptor.becomeRoot(new AltAST(LEXER_ALT_ACTION), root_1);
                adaptor.addChild(root_1, stream_lexerElements.nextTree());
                adaptor.addChild(root_1, stream_lexerCommands.nextTree());
                adaptor.addChild(root_0, root_1);
              }

            }


            retval.tree = root_0;

          }
          break;
          case 2:
            // org/antlr/v4/parse/ANTLRParser.g:499:9:
          {

            // AST REWRITE
            // elements: lexerElements
            // token labels:
            // rule labels: retval
            // token list labels:
            // rule list labels:
            // wildcard labels:
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval = new RewriteRuleSubtreeStream(adaptor, "rule retval", retval != null ? retval.getTree() : null);

            root_0 = (GrammarAST) adaptor.nil();
            // 499:9: -> lexerElements
            {
              adaptor.addChild(root_0, stream_lexerElements.nextTree());
            }


            retval.tree = root_0;

          }
          break;

        }

      }

      retval.stop = input.LT(-1);

      retval.tree = (GrammarAST) adaptor.rulePostProcessing(root_0);
      adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

    } catch (RecognitionException re) {
      reportError(re);
      recover(input, re);
      retval.tree = (GrammarAST) adaptor.errorNode(input, retval.start, input.LT(-1), re);
    } finally {
      // do for sure before leaving
    }
    return retval;
  }
  // $ANTLR end "lexerAlt"


  public static class lexerElements_return extends ParserRuleReturnScope {
    GrammarAST tree;

    @Override
    public GrammarAST getTree() {
      return tree;
    }
  }

  ;


  // $ANTLR start "lexerElements"
  // org/antlr/v4/parse/ANTLRParser.g:503:1: lexerElements : ( ( lexerElement )+ -> ^( ALT ( lexerElement )+ ) | -> ^( ALT EPSILON ) );
  public final ANTLRParser.lexerElements_return lexerElements() throws RecognitionException {
    ANTLRParser.lexerElements_return retval = new ANTLRParser.lexerElements_return();
    retval.start = input.LT(1);

    GrammarAST root_0 = null;

    ParserRuleReturnScope lexerElement125 = null;

    RewriteRuleSubtreeStream stream_lexerElement = new RewriteRuleSubtreeStream(adaptor, "rule lexerElement");

    try {
      // org/antlr/v4/parse/ANTLRParser.g:504:5: ( ( lexerElement )+ -> ^( ALT ( lexerElement )+ ) | -> ^( ALT EPSILON ) )
      int alt37 = 2;
      int LA37_0 = input.LA(1);
      if ((LA37_0 == ACTION || LA37_0 == DOT || LA37_0 == LEXER_CHAR_SET || LA37_0 == LPAREN || LA37_0 == NOT || LA37_0 == RULE_REF || LA37_0 == SEMPRED || LA37_0 == STRING_LITERAL || LA37_0 == TOKEN_REF)) {
        alt37 = 1;
      } else if ((LA37_0 == OR || LA37_0 == RARROW || LA37_0 == RPAREN || LA37_0 == SEMI)) {
        alt37 = 2;
      } else {
        NoViableAltException nvae =
          new NoViableAltException("", 37, 0, input);
        throw nvae;
      }

      switch (alt37) {
        case 1:
          // org/antlr/v4/parse/ANTLRParser.g:504:7: ( lexerElement )+
        {
          // org/antlr/v4/parse/ANTLRParser.g:504:7: ( lexerElement )+
          int cnt36 = 0;
          loop36:
          while (true) {
            int alt36 = 2;
            int LA36_0 = input.LA(1);
            if ((LA36_0 == ACTION || LA36_0 == DOT || LA36_0 == LEXER_CHAR_SET || LA36_0 == LPAREN || LA36_0 == NOT || LA36_0 == RULE_REF || LA36_0 == SEMPRED || LA36_0 == STRING_LITERAL || LA36_0 == TOKEN_REF)) {
              alt36 = 1;
            }

            switch (alt36) {
              case 1:
                // org/antlr/v4/parse/ANTLRParser.g:504:7: lexerElement
              {
                pushFollow(FOLLOW_lexerElement_in_lexerElements2318);
                lexerElement125 = lexerElement();
                state._fsp--;

                stream_lexerElement.add(lexerElement125.getTree());
              }
              break;

              default:
                if (cnt36 >= 1) break loop36;
                EarlyExitException eee = new EarlyExitException(36, input);
                throw eee;
            }
            cnt36++;
          }


          // AST REWRITE
          // elements: lexerElement
          // token labels:
          // rule labels: retval
          // token list labels:
          // rule list labels:
          // wildcard labels:
          retval.tree = root_0;
          RewriteRuleSubtreeStream stream_retval = new RewriteRuleSubtreeStream(adaptor, "rule retval", retval != null ? retval.getTree() : null);

          root_0 = (GrammarAST) adaptor.nil();
          // 504:21: -> ^( ALT ( lexerElement )+ )
          {
            // org/antlr/v4/parse/ANTLRParser.g:504:24: ^( ALT ( lexerElement )+ )
            {
              GrammarAST root_1 = (GrammarAST) adaptor.nil();
              root_1 = (GrammarAST) adaptor.becomeRoot(new AltAST(ALT), root_1);
              if (!(stream_lexerElement.hasNext())) {
                throw new RewriteEarlyExitException();
              }
              while (stream_lexerElement.hasNext()) {
                adaptor.addChild(root_1, stream_lexerElement.nextTree());
              }
              stream_lexerElement.reset();

              adaptor.addChild(root_0, root_1);
            }

          }


          retval.tree = root_0;

        }
        break;
        case 2:
          // org/antlr/v4/parse/ANTLRParser.g:505:8:
        {

          // AST REWRITE
          // elements:
          // token labels:
          // rule labels: retval
          // token list labels:
          // rule list labels:
          // wildcard labels:
          retval.tree = root_0;
          RewriteRuleSubtreeStream stream_retval = new RewriteRuleSubtreeStream(adaptor, "rule retval", retval != null ? retval.getTree() : null);

          root_0 = (GrammarAST) adaptor.nil();
          // 505:8: -> ^( ALT EPSILON )
          {
            // org/antlr/v4/parse/ANTLRParser.g:505:11: ^( ALT EPSILON )
            {
              GrammarAST root_1 = (GrammarAST) adaptor.nil();
              root_1 = (GrammarAST) adaptor.becomeRoot(new AltAST(ALT), root_1);
              adaptor.addChild(root_1, (GrammarAST) adaptor.create(EPSILON, "EPSILON"));
              adaptor.addChild(root_0, root_1);
            }

          }


          retval.tree = root_0;

        }
        break;

      }
      retval.stop = input.LT(-1);

      retval.tree = (GrammarAST) adaptor.rulePostProcessing(root_0);
      adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

    } catch (RecognitionException re) {
      reportError(re);
      recover(input, re);
      retval.tree = (GrammarAST) adaptor.errorNode(input, retval.start, input.LT(-1), re);
    } finally {
      // do for sure before leaving
    }
    return retval;
  }
  // $ANTLR end "lexerElements"


  public static class lexerElement_return extends ParserRuleReturnScope {
    GrammarAST tree;

    @Override
    public GrammarAST getTree() {
      return tree;
    }
  }

  ;


  // $ANTLR start "lexerElement"
  // org/antlr/v4/parse/ANTLRParser.g:508:1: lexerElement : ( labeledLexerElement ( ebnfSuffix -> ^( ebnfSuffix ^( BLOCK[$labeledLexerElement.start,\"BLOCK\"] ^( ALT labeledLexerElement ) ) ) | -> labeledLexerElement ) | lexerAtom ( ebnfSuffix -> ^( ebnfSuffix ^( BLOCK[$lexerAtom.start,\"BLOCK\"] ^( ALT lexerAtom ) ) ) | -> lexerAtom ) | lexerBlock ( ebnfSuffix -> ^( ebnfSuffix lexerBlock ) | -> lexerBlock ) | actionElement );
  public final ANTLRParser.lexerElement_return lexerElement() throws RecognitionException {
    ANTLRParser.lexerElement_return retval = new ANTLRParser.lexerElement_return();
    retval.start = input.LT(1);

    GrammarAST root_0 = null;

    ParserRuleReturnScope labeledLexerElement126 = null;
    ParserRuleReturnScope ebnfSuffix127 = null;
    ParserRuleReturnScope lexerAtom128 = null;
    ParserRuleReturnScope ebnfSuffix129 = null;
    ParserRuleReturnScope lexerBlock130 = null;
    ParserRuleReturnScope ebnfSuffix131 = null;
    ParserRuleReturnScope actionElement132 = null;

    RewriteRuleSubtreeStream stream_labeledLexerElement = new RewriteRuleSubtreeStream(adaptor, "rule labeledLexerElement");
    RewriteRuleSubtreeStream stream_lexerBlock = new RewriteRuleSubtreeStream(adaptor, "rule lexerBlock");
    RewriteRuleSubtreeStream stream_ebnfSuffix = new RewriteRuleSubtreeStream(adaptor, "rule ebnfSuffix");
    RewriteRuleSubtreeStream stream_lexerAtom = new RewriteRuleSubtreeStream(adaptor, "rule lexerAtom");


    paraphrases.push("looking for lexer rule element");
    int m = input.mark();

    try {
      // org/antlr/v4/parse/ANTLRParser.g:514:2: ( labeledLexerElement ( ebnfSuffix -> ^( ebnfSuffix ^( BLOCK[$labeledLexerElement.start,\"BLOCK\"] ^( ALT labeledLexerElement ) ) ) | -> labeledLexerElement ) | lexerAtom ( ebnfSuffix -> ^( ebnfSuffix ^( BLOCK[$lexerAtom.start,\"BLOCK\"] ^( ALT lexerAtom ) ) ) | -> lexerAtom ) | lexerBlock ( ebnfSuffix -> ^( ebnfSuffix lexerBlock ) | -> lexerBlock ) | actionElement )
      int alt41 = 4;
      switch (input.LA(1)) {
        case RULE_REF: {
          int LA41_1 = input.LA(2);
          if ((LA41_1 == ASSIGN || LA41_1 == PLUS_ASSIGN)) {
            alt41 = 1;
          } else if ((LA41_1 == ACTION || LA41_1 == DOT || LA41_1 == LEXER_CHAR_SET || LA41_1 == LPAREN || LA41_1 == NOT || LA41_1 == OR || LA41_1 == PLUS || LA41_1 == QUESTION || LA41_1 == RARROW || (LA41_1 >= RPAREN && LA41_1 <= SEMPRED) || (LA41_1 >= STAR && LA41_1 <= STRING_LITERAL) || LA41_1 == TOKEN_REF)) {
            alt41 = 2;
          } else {
            int nvaeMark = input.mark();
            try {
              input.consume();
              NoViableAltException nvae =
                new NoViableAltException("", 41, 1, input);
              throw nvae;
            } finally {
              input.rewind(nvaeMark);
            }
          }

        }
        break;
        case TOKEN_REF: {
          int LA41_2 = input.LA(2);
          if ((LA41_2 == ASSIGN || LA41_2 == PLUS_ASSIGN)) {
            alt41 = 1;
          } else if ((LA41_2 == ACTION || LA41_2 == DOT || LA41_2 == LEXER_CHAR_SET || (LA41_2 >= LPAREN && LA41_2 <= LT) || LA41_2 == NOT || LA41_2 == OR || LA41_2 == PLUS || LA41_2 == QUESTION || LA41_2 == RARROW || (LA41_2 >= RPAREN && LA41_2 <= SEMPRED) || (LA41_2 >= STAR && LA41_2 <= STRING_LITERAL) || LA41_2 == TOKEN_REF)) {
            alt41 = 2;
          } else {
            int nvaeMark = input.mark();
            try {
              input.consume();
              NoViableAltException nvae =
                new NoViableAltException("", 41, 2, input);
              throw nvae;
            } finally {
              input.rewind(nvaeMark);
            }
          }

        }
        break;
        case DOT:
        case LEXER_CHAR_SET:
        case NOT:
        case STRING_LITERAL: {
          alt41 = 2;
        }
        break;
        case LPAREN: {
          alt41 = 3;
        }
        break;
        case ACTION:
        case SEMPRED: {
          alt41 = 4;
        }
        break;
        default:
          NoViableAltException nvae =
            new NoViableAltException("", 41, 0, input);
          throw nvae;
      }
      switch (alt41) {
        case 1:
          // org/antlr/v4/parse/ANTLRParser.g:514:4: labeledLexerElement ( ebnfSuffix -> ^( ebnfSuffix ^( BLOCK[$labeledLexerElement.start,\"BLOCK\"] ^( ALT labeledLexerElement ) ) ) | -> labeledLexerElement )
        {
          pushFollow(FOLLOW_labeledLexerElement_in_lexerElement2374);
          labeledLexerElement126 = labeledLexerElement();
          state._fsp--;

          stream_labeledLexerElement.add(labeledLexerElement126.getTree());
          // org/antlr/v4/parse/ANTLRParser.g:515:3: ( ebnfSuffix -> ^( ebnfSuffix ^( BLOCK[$labeledLexerElement.start,\"BLOCK\"] ^( ALT labeledLexerElement ) ) ) | -> labeledLexerElement )
          int alt38 = 2;
          int LA38_0 = input.LA(1);
          if ((LA38_0 == PLUS || LA38_0 == QUESTION || LA38_0 == STAR)) {
            alt38 = 1;
          } else if ((LA38_0 == ACTION || LA38_0 == DOT || LA38_0 == LEXER_CHAR_SET || LA38_0 == LPAREN || LA38_0 == NOT || LA38_0 == OR || LA38_0 == RARROW || (LA38_0 >= RPAREN && LA38_0 <= SEMPRED) || LA38_0 == STRING_LITERAL || LA38_0 == TOKEN_REF)) {
            alt38 = 2;
          } else {
            NoViableAltException nvae =
              new NoViableAltException("", 38, 0, input);
            throw nvae;
          }

          switch (alt38) {
            case 1:
              // org/antlr/v4/parse/ANTLRParser.g:515:5: ebnfSuffix
            {
              pushFollow(FOLLOW_ebnfSuffix_in_lexerElement2380);
              ebnfSuffix127 = ebnfSuffix();
              state._fsp--;

              stream_ebnfSuffix.add(ebnfSuffix127.getTree());

              // AST REWRITE
              // elements: ebnfSuffix, labeledLexerElement
              // token labels:
              // rule labels: retval
              // token list labels:
              // rule list labels:
              // wildcard labels:
              retval.tree = root_0;
              RewriteRuleSubtreeStream stream_retval = new RewriteRuleSubtreeStream(adaptor, "rule retval", retval != null ? retval.getTree() : null);

              root_0 = (GrammarAST) adaptor.nil();
              // 515:16: -> ^( ebnfSuffix ^( BLOCK[$labeledLexerElement.start,\"BLOCK\"] ^( ALT labeledLexerElement ) ) )
              {
                // org/antlr/v4/parse/ANTLRParser.g:515:19: ^( ebnfSuffix ^( BLOCK[$labeledLexerElement.start,\"BLOCK\"] ^( ALT labeledLexerElement ) ) )
                {
                  GrammarAST root_1 = (GrammarAST) adaptor.nil();
                  root_1 = (GrammarAST) adaptor.becomeRoot(stream_ebnfSuffix.nextNode(), root_1);
                  // org/antlr/v4/parse/ANTLRParser.g:515:33: ^( BLOCK[$labeledLexerElement.start,\"BLOCK\"] ^( ALT labeledLexerElement ) )
                  {
                    GrammarAST root_2 = (GrammarAST) adaptor.nil();
                    root_2 = (GrammarAST) adaptor.becomeRoot(new BlockAST(BLOCK, (labeledLexerElement126 != null ? (labeledLexerElement126.start) : null), "BLOCK"), root_2);
                    // org/antlr/v4/parse/ANTLRParser.g:515:87: ^( ALT labeledLexerElement )
                    {
                      GrammarAST root_3 = (GrammarAST) adaptor.nil();
                      root_3 = (GrammarAST) adaptor.becomeRoot(new AltAST(ALT), root_3);
                      adaptor.addChild(root_3, stream_labeledLexerElement.nextTree());
                      adaptor.addChild(root_2, root_3);
                    }

                    adaptor.addChild(root_1, root_2);
                  }

                  adaptor.addChild(root_0, root_1);
                }

              }


              retval.tree = root_0;

            }
            break;
            case 2:
              // org/antlr/v4/parse/ANTLRParser.g:516:8:
            {

              // AST REWRITE
              // elements: labeledLexerElement
              // token labels:
              // rule labels: retval
              // token list labels:
              // rule list labels:
              // wildcard labels:
              retval.tree = root_0;
              RewriteRuleSubtreeStream stream_retval = new RewriteRuleSubtreeStream(adaptor, "rule retval", retval != null ? retval.getTree() : null);

              root_0 = (GrammarAST) adaptor.nil();
              // 516:8: -> labeledLexerElement
              {
                adaptor.addChild(root_0, stream_labeledLexerElement.nextTree());
              }


              retval.tree = root_0;

            }
            break;

          }

        }
        break;
        case 2:
          // org/antlr/v4/parse/ANTLRParser.g:518:4: lexerAtom ( ebnfSuffix -> ^( ebnfSuffix ^( BLOCK[$lexerAtom.start,\"BLOCK\"] ^( ALT lexerAtom ) ) ) | -> lexerAtom )
        {
          pushFollow(FOLLOW_lexerAtom_in_lexerElement2426);
          lexerAtom128 = lexerAtom();
          state._fsp--;

          stream_lexerAtom.add(lexerAtom128.getTree());
          // org/antlr/v4/parse/ANTLRParser.g:519:3: ( ebnfSuffix -> ^( ebnfSuffix ^( BLOCK[$lexerAtom.start,\"BLOCK\"] ^( ALT lexerAtom ) ) ) | -> lexerAtom )
          int alt39 = 2;
          int LA39_0 = input.LA(1);
          if ((LA39_0 == PLUS || LA39_0 == QUESTION || LA39_0 == STAR)) {
            alt39 = 1;
          } else if ((LA39_0 == ACTION || LA39_0 == DOT || LA39_0 == LEXER_CHAR_SET || LA39_0 == LPAREN || LA39_0 == NOT || LA39_0 == OR || LA39_0 == RARROW || (LA39_0 >= RPAREN && LA39_0 <= SEMPRED) || LA39_0 == STRING_LITERAL || LA39_0 == TOKEN_REF)) {
            alt39 = 2;
          } else {
            NoViableAltException nvae =
              new NoViableAltException("", 39, 0, input);
            throw nvae;
          }

          switch (alt39) {
            case 1:
              // org/antlr/v4/parse/ANTLRParser.g:519:5: ebnfSuffix
            {
              pushFollow(FOLLOW_ebnfSuffix_in_lexerElement2432);
              ebnfSuffix129 = ebnfSuffix();
              state._fsp--;

              stream_ebnfSuffix.add(ebnfSuffix129.getTree());

              // AST REWRITE
              // elements: lexerAtom, ebnfSuffix
              // token labels:
              // rule labels: retval
              // token list labels:
              // rule list labels:
              // wildcard labels:
              retval.tree = root_0;
              RewriteRuleSubtreeStream stream_retval = new RewriteRuleSubtreeStream(adaptor, "rule retval", retval != null ? retval.getTree() : null);

              root_0 = (GrammarAST) adaptor.nil();
              // 519:16: -> ^( ebnfSuffix ^( BLOCK[$lexerAtom.start,\"BLOCK\"] ^( ALT lexerAtom ) ) )
              {
                // org/antlr/v4/parse/ANTLRParser.g:519:19: ^( ebnfSuffix ^( BLOCK[$lexerAtom.start,\"BLOCK\"] ^( ALT lexerAtom ) ) )
                {
                  GrammarAST root_1 = (GrammarAST) adaptor.nil();
                  root_1 = (GrammarAST) adaptor.becomeRoot(stream_ebnfSuffix.nextNode(), root_1);
                  // org/antlr/v4/parse/ANTLRParser.g:519:33: ^( BLOCK[$lexerAtom.start,\"BLOCK\"] ^( ALT lexerAtom ) )
                  {
                    GrammarAST root_2 = (GrammarAST) adaptor.nil();
                    root_2 = (GrammarAST) adaptor.becomeRoot(new BlockAST(BLOCK, (lexerAtom128 != null ? (lexerAtom128.start) : null), "BLOCK"), root_2);
                    // org/antlr/v4/parse/ANTLRParser.g:519:77: ^( ALT lexerAtom )
                    {
                      GrammarAST root_3 = (GrammarAST) adaptor.nil();
                      root_3 = (GrammarAST) adaptor.becomeRoot(new AltAST(ALT), root_3);
                      adaptor.addChild(root_3, stream_lexerAtom.nextTree());
                      adaptor.addChild(root_2, root_3);
                    }

                    adaptor.addChild(root_1, root_2);
                  }

                  adaptor.addChild(root_0, root_1);
                }

              }


              retval.tree = root_0;

            }
            break;
            case 2:
              // org/antlr/v4/parse/ANTLRParser.g:520:8:
            {

              // AST REWRITE
              // elements: lexerAtom
              // token labels:
              // rule labels: retval
              // token list labels:
              // rule list labels:
              // wildcard labels:
              retval.tree = root_0;
              RewriteRuleSubtreeStream stream_retval = new RewriteRuleSubtreeStream(adaptor, "rule retval", retval != null ? retval.getTree() : null);

              root_0 = (GrammarAST) adaptor.nil();
              // 520:8: -> lexerAtom
              {
                adaptor.addChild(root_0, stream_lexerAtom.nextTree());
              }


              retval.tree = root_0;

            }
            break;

          }

        }
        break;
        case 3:
          // org/antlr/v4/parse/ANTLRParser.g:522:4: lexerBlock ( ebnfSuffix -> ^( ebnfSuffix lexerBlock ) | -> lexerBlock )
        {
          pushFollow(FOLLOW_lexerBlock_in_lexerElement2478);
          lexerBlock130 = lexerBlock();
          state._fsp--;

          stream_lexerBlock.add(lexerBlock130.getTree());
          // org/antlr/v4/parse/ANTLRParser.g:523:3: ( ebnfSuffix -> ^( ebnfSuffix lexerBlock ) | -> lexerBlock )
          int alt40 = 2;
          int LA40_0 = input.LA(1);
          if ((LA40_0 == PLUS || LA40_0 == QUESTION || LA40_0 == STAR)) {
            alt40 = 1;
          } else if ((LA40_0 == ACTION || LA40_0 == DOT || LA40_0 == LEXER_CHAR_SET || LA40_0 == LPAREN || LA40_0 == NOT || LA40_0 == OR || LA40_0 == RARROW || (LA40_0 >= RPAREN && LA40_0 <= SEMPRED) || LA40_0 == STRING_LITERAL || LA40_0 == TOKEN_REF)) {
            alt40 = 2;
          } else {
            NoViableAltException nvae =
              new NoViableAltException("", 40, 0, input);
            throw nvae;
          }

          switch (alt40) {
            case 1:
              // org/antlr/v4/parse/ANTLRParser.g:523:5: ebnfSuffix
            {
              pushFollow(FOLLOW_ebnfSuffix_in_lexerElement2484);
              ebnfSuffix131 = ebnfSuffix();
              state._fsp--;

              stream_ebnfSuffix.add(ebnfSuffix131.getTree());

              // AST REWRITE
              // elements: lexerBlock, ebnfSuffix
              // token labels:
              // rule labels: retval
              // token list labels:
              // rule list labels:
              // wildcard labels:
              retval.tree = root_0;
              RewriteRuleSubtreeStream stream_retval = new RewriteRuleSubtreeStream(adaptor, "rule retval", retval != null ? retval.getTree() : null);

              root_0 = (GrammarAST) adaptor.nil();
              // 523:16: -> ^( ebnfSuffix lexerBlock )
              {
                // org/antlr/v4/parse/ANTLRParser.g:523:19: ^( ebnfSuffix lexerBlock )
                {
                  GrammarAST root_1 = (GrammarAST) adaptor.nil();
                  root_1 = (GrammarAST) adaptor.becomeRoot(stream_ebnfSuffix.nextNode(), root_1);
                  adaptor.addChild(root_1, stream_lexerBlock.nextTree());
                  adaptor.addChild(root_0, root_1);
                }

              }


              retval.tree = root_0;

            }
            break;
            case 2:
              // org/antlr/v4/parse/ANTLRParser.g:524:8:
            {

              // AST REWRITE
              // elements: lexerBlock
              // token labels:
              // rule labels: retval
              // token list labels:
              // rule list labels:
              // wildcard labels:
              retval.tree = root_0;
              RewriteRuleSubtreeStream stream_retval = new RewriteRuleSubtreeStream(adaptor, "rule retval", retval != null ? retval.getTree() : null);

              root_0 = (GrammarAST) adaptor.nil();
              // 524:8: -> lexerBlock
              {
                adaptor.addChild(root_0, stream_lexerBlock.nextTree());
              }


              retval.tree = root_0;

            }
            break;

          }

        }
        break;
        case 4:
          // org/antlr/v4/parse/ANTLRParser.g:526:4: actionElement
        {
          root_0 = (GrammarAST) adaptor.nil();


          pushFollow(FOLLOW_actionElement_in_lexerElement2512);
          actionElement132 = actionElement();
          state._fsp--;

          adaptor.addChild(root_0, actionElement132.getTree());

        }
        break;

      }
      retval.stop = input.LT(-1);

      retval.tree = (GrammarAST) adaptor.rulePostProcessing(root_0);
      adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

      paraphrases.pop();
    } catch (RecognitionException re) {

      retval.tree = (GrammarAST) adaptor.errorNode(input, retval.start, input.LT(-1), re);
      int ttype = input.get(input.range()).getType(); // seems to be next token
      // look for anything that really belongs at the start of the rule minus the initial ID
      if (ttype == COLON || ttype == RETURNS || ttype == CATCH || ttype == FINALLY || ttype == AT || ttype == EOF) {
        RecognitionException missingSemi =
          new v4ParserException("unterminated rule (missing ';') detected at '" +
            input.LT(1).getText() + " " + input.LT(2).getText() + "'", input);
        reportError(missingSemi);
        if (ttype == EOF) {
          input.seek(input.index() + 1);
        } else if (ttype == CATCH || ttype == FINALLY) {
          input.seek(input.range()); // ignore what's before rule trailer stuff
        } else if (ttype == RETURNS || ttype == AT) { // scan back looking for ID of rule header
          int p = input.index();
          Token t = input.get(p);
          while (t.getType() != RULE_REF && t.getType() != TOKEN_REF) {
            p--;
            t = input.get(p);
          }
          input.seek(p);
        }
        throw new ResyncToEndOfRuleBlock(); // make sure it goes back to rule block level to recover
      }
      reportError(re);
      recover(input, re);

    } finally {
      // do for sure before leaving
    }
    return retval;
  }
  // $ANTLR end "lexerElement"


  public static class labeledLexerElement_return extends ParserRuleReturnScope {
    GrammarAST tree;

    @Override
    public GrammarAST getTree() {
      return tree;
    }
  }

  ;


  // $ANTLR start "labeledLexerElement"
  // org/antlr/v4/parse/ANTLRParser.g:559:1: labeledLexerElement : id (ass= ASSIGN |ass= PLUS_ASSIGN ) ( lexerAtom -> ^( $ass id lexerAtom ) | lexerBlock -> ^( $ass id lexerBlock ) ) ;
  public final ANTLRParser.labeledLexerElement_return labeledLexerElement() throws RecognitionException {
    ANTLRParser.labeledLexerElement_return retval = new ANTLRParser.labeledLexerElement_return();
    retval.start = input.LT(1);

    GrammarAST root_0 = null;

    Token ass = null;
    ParserRuleReturnScope id133 = null;
    ParserRuleReturnScope lexerAtom134 = null;
    ParserRuleReturnScope lexerBlock135 = null;

    GrammarAST ass_tree = null;
    RewriteRuleTokenStream stream_PLUS_ASSIGN = new RewriteRuleTokenStream(adaptor, "token PLUS_ASSIGN");
    RewriteRuleTokenStream stream_ASSIGN = new RewriteRuleTokenStream(adaptor, "token ASSIGN");
    RewriteRuleSubtreeStream stream_lexerBlock = new RewriteRuleSubtreeStream(adaptor, "rule lexerBlock");
    RewriteRuleSubtreeStream stream_id = new RewriteRuleSubtreeStream(adaptor, "rule id");
    RewriteRuleSubtreeStream stream_lexerAtom = new RewriteRuleSubtreeStream(adaptor, "rule lexerAtom");

    try {
      // org/antlr/v4/parse/ANTLRParser.g:560:2: ( id (ass= ASSIGN |ass= PLUS_ASSIGN ) ( lexerAtom -> ^( $ass id lexerAtom ) | lexerBlock -> ^( $ass id lexerBlock ) ) )
      // org/antlr/v4/parse/ANTLRParser.g:560:4: id (ass= ASSIGN |ass= PLUS_ASSIGN ) ( lexerAtom -> ^( $ass id lexerAtom ) | lexerBlock -> ^( $ass id lexerBlock ) )
      {
        pushFollow(FOLLOW_id_in_labeledLexerElement2542);
        id133 = id();
        state._fsp--;

        stream_id.add(id133.getTree());
        // org/antlr/v4/parse/ANTLRParser.g:560:7: (ass= ASSIGN |ass= PLUS_ASSIGN )
        int alt42 = 2;
        int LA42_0 = input.LA(1);
        if ((LA42_0 == ASSIGN)) {
          alt42 = 1;
        } else if ((LA42_0 == PLUS_ASSIGN)) {
          alt42 = 2;
        } else {
          NoViableAltException nvae =
            new NoViableAltException("", 42, 0, input);
          throw nvae;
        }

        switch (alt42) {
          case 1:
            // org/antlr/v4/parse/ANTLRParser.g:560:8: ass= ASSIGN
          {
            ass = (Token) match(input, ASSIGN, FOLLOW_ASSIGN_in_labeledLexerElement2547);
            stream_ASSIGN.add(ass);

          }
          break;
          case 2:
            // org/antlr/v4/parse/ANTLRParser.g:560:19: ass= PLUS_ASSIGN
          {
            ass = (Token) match(input, PLUS_ASSIGN, FOLLOW_PLUS_ASSIGN_in_labeledLexerElement2551);
            stream_PLUS_ASSIGN.add(ass);

          }
          break;

        }

        // org/antlr/v4/parse/ANTLRParser.g:561:3: ( lexerAtom -> ^( $ass id lexerAtom ) | lexerBlock -> ^( $ass id lexerBlock ) )
        int alt43 = 2;
        int LA43_0 = input.LA(1);
        if ((LA43_0 == DOT || LA43_0 == LEXER_CHAR_SET || LA43_0 == NOT || LA43_0 == RULE_REF || LA43_0 == STRING_LITERAL || LA43_0 == TOKEN_REF)) {
          alt43 = 1;
        } else if ((LA43_0 == LPAREN)) {
          alt43 = 2;
        } else {
          NoViableAltException nvae =
            new NoViableAltException("", 43, 0, input);
          throw nvae;
        }

        switch (alt43) {
          case 1:
            // org/antlr/v4/parse/ANTLRParser.g:561:5: lexerAtom
          {
            pushFollow(FOLLOW_lexerAtom_in_labeledLexerElement2558);
            lexerAtom134 = lexerAtom();
            state._fsp--;

            stream_lexerAtom.add(lexerAtom134.getTree());

            // AST REWRITE
            // elements: id, lexerAtom, ass
            // token labels: ass
            // rule labels: retval
            // token list labels:
            // rule list labels:
            // wildcard labels:
            retval.tree = root_0;
            RewriteRuleTokenStream stream_ass = new RewriteRuleTokenStream(adaptor, "token ass", ass);
            RewriteRuleSubtreeStream stream_retval = new RewriteRuleSubtreeStream(adaptor, "rule retval", retval != null ? retval.getTree() : null);

            root_0 = (GrammarAST) adaptor.nil();
            // 561:15: -> ^( $ass id lexerAtom )
            {
              // org/antlr/v4/parse/ANTLRParser.g:561:18: ^( $ass id lexerAtom )
              {
                GrammarAST root_1 = (GrammarAST) adaptor.nil();
                root_1 = (GrammarAST) adaptor.becomeRoot(stream_ass.nextNode(), root_1);
                adaptor.addChild(root_1, stream_id.nextTree());
                adaptor.addChild(root_1, stream_lexerAtom.nextTree());
                adaptor.addChild(root_0, root_1);
              }

            }


            retval.tree = root_0;

          }
          break;
          case 2:
            // org/antlr/v4/parse/ANTLRParser.g:562:5: lexerBlock
          {
            pushFollow(FOLLOW_lexerBlock_in_labeledLexerElement2575);
            lexerBlock135 = lexerBlock();
            state._fsp--;

            stream_lexerBlock.add(lexerBlock135.getTree());

            // AST REWRITE
            // elements: ass, lexerBlock, id
            // token labels: ass
            // rule labels: retval
            // token list labels:
            // rule list labels:
            // wildcard labels:
            retval.tree = root_0;
            RewriteRuleTokenStream stream_ass = new RewriteRuleTokenStream(adaptor, "token ass", ass);
            RewriteRuleSubtreeStream stream_retval = new RewriteRuleSubtreeStream(adaptor, "rule retval", retval != null ? retval.getTree() : null);

            root_0 = (GrammarAST) adaptor.nil();
            // 562:16: -> ^( $ass id lexerBlock )
            {
              // org/antlr/v4/parse/ANTLRParser.g:562:19: ^( $ass id lexerBlock )
              {
                GrammarAST root_1 = (GrammarAST) adaptor.nil();
                root_1 = (GrammarAST) adaptor.becomeRoot(stream_ass.nextNode(), root_1);
                adaptor.addChild(root_1, stream_id.nextTree());
                adaptor.addChild(root_1, stream_lexerBlock.nextTree());
                adaptor.addChild(root_0, root_1);
              }

            }


            retval.tree = root_0;

          }
          break;

        }

      }

      retval.stop = input.LT(-1);

      retval.tree = (GrammarAST) adaptor.rulePostProcessing(root_0);
      adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

    } catch (RecognitionException re) {
      reportError(re);
      recover(input, re);
      retval.tree = (GrammarAST) adaptor.errorNode(input, retval.start, input.LT(-1), re);
    } finally {
      // do for sure before leaving
    }
    return retval;
  }
  // $ANTLR end "labeledLexerElement"


  public static class lexerBlock_return extends ParserRuleReturnScope {
    GrammarAST tree;

    @Override
    public GrammarAST getTree() {
      return tree;
    }
  }

  ;


  // $ANTLR start "lexerBlock"
  // org/antlr/v4/parse/ANTLRParser.g:567:1: lexerBlock : LPAREN ( optionsSpec COLON )? lexerAltList RPAREN -> ^( BLOCK[$LPAREN,\"BLOCK\"] ( optionsSpec )? lexerAltList ) ;
  public final ANTLRParser.lexerBlock_return lexerBlock() throws RecognitionException {
    ANTLRParser.lexerBlock_return retval = new ANTLRParser.lexerBlock_return();
    retval.start = input.LT(1);

    GrammarAST root_0 = null;

    Token LPAREN136 = null;
    Token COLON138 = null;
    Token RPAREN140 = null;
    ParserRuleReturnScope optionsSpec137 = null;
    ParserRuleReturnScope lexerAltList139 = null;

    GrammarAST LPAREN136_tree = null;
    GrammarAST COLON138_tree = null;
    GrammarAST RPAREN140_tree = null;
    RewriteRuleTokenStream stream_LPAREN = new RewriteRuleTokenStream(adaptor, "token LPAREN");
    RewriteRuleTokenStream stream_COLON = new RewriteRuleTokenStream(adaptor, "token COLON");
    RewriteRuleTokenStream stream_RPAREN = new RewriteRuleTokenStream(adaptor, "token RPAREN");
    RewriteRuleSubtreeStream stream_optionsSpec = new RewriteRuleSubtreeStream(adaptor, "rule optionsSpec");
    RewriteRuleSubtreeStream stream_lexerAltList = new RewriteRuleSubtreeStream(adaptor, "rule lexerAltList");

    try {
      // org/antlr/v4/parse/ANTLRParser.g:574:3: ( LPAREN ( optionsSpec COLON )? lexerAltList RPAREN -> ^( BLOCK[$LPAREN,\"BLOCK\"] ( optionsSpec )? lexerAltList ) )
      // org/antlr/v4/parse/ANTLRParser.g:574:5: LPAREN ( optionsSpec COLON )? lexerAltList RPAREN
      {
        LPAREN136 = (Token) match(input, LPAREN, FOLLOW_LPAREN_in_lexerBlock2608);
        stream_LPAREN.add(LPAREN136);

        // org/antlr/v4/parse/ANTLRParser.g:575:9: ( optionsSpec COLON )?
        int alt44 = 2;
        int LA44_0 = input.LA(1);
        if ((LA44_0 == OPTIONS)) {
          alt44 = 1;
        }
        switch (alt44) {
          case 1:
            // org/antlr/v4/parse/ANTLRParser.g:575:11: optionsSpec COLON
          {
            pushFollow(FOLLOW_optionsSpec_in_lexerBlock2620);
            optionsSpec137 = optionsSpec();
            state._fsp--;

            stream_optionsSpec.add(optionsSpec137.getTree());
            COLON138 = (Token) match(input, COLON, FOLLOW_COLON_in_lexerBlock2622);
            stream_COLON.add(COLON138);

          }
          break;

        }

        pushFollow(FOLLOW_lexerAltList_in_lexerBlock2635);
        lexerAltList139 = lexerAltList();
        state._fsp--;

        stream_lexerAltList.add(lexerAltList139.getTree());
        RPAREN140 = (Token) match(input, RPAREN, FOLLOW_RPAREN_in_lexerBlock2645);
        stream_RPAREN.add(RPAREN140);


        // AST REWRITE
        // elements: optionsSpec, lexerAltList
        // token labels:
        // rule labels: retval
        // token list labels:
        // rule list labels:
        // wildcard labels:
        retval.tree = root_0;
        RewriteRuleSubtreeStream stream_retval = new RewriteRuleSubtreeStream(adaptor, "rule retval", retval != null ? retval.getTree() : null);

        root_0 = (GrammarAST) adaptor.nil();
        // 578:7: -> ^( BLOCK[$LPAREN,\"BLOCK\"] ( optionsSpec )? lexerAltList )
        {
          // org/antlr/v4/parse/ANTLRParser.g:578:10: ^( BLOCK[$LPAREN,\"BLOCK\"] ( optionsSpec )? lexerAltList )
          {
            GrammarAST root_1 = (GrammarAST) adaptor.nil();
            root_1 = (GrammarAST) adaptor.becomeRoot(new BlockAST(BLOCK, LPAREN136, "BLOCK"), root_1);
            // org/antlr/v4/parse/ANTLRParser.g:578:45: ( optionsSpec )?
            if (stream_optionsSpec.hasNext()) {
              adaptor.addChild(root_1, stream_optionsSpec.nextTree());
            }
            stream_optionsSpec.reset();

            adaptor.addChild(root_1, stream_lexerAltList.nextTree());
            adaptor.addChild(root_0, root_1);
          }

        }


        retval.tree = root_0;

      }

      retval.stop = input.LT(-1);

      retval.tree = (GrammarAST) adaptor.rulePostProcessing(root_0);
      adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);


      GrammarAST options = (GrammarAST) retval.tree.getFirstChildWithType(ANTLRParser.OPTIONS);
      if (options != null) {
        Grammar.setNodeOptions(retval.tree, options);
      }

    } catch (RecognitionException re) {
      reportError(re);
      recover(input, re);
      retval.tree = (GrammarAST) adaptor.errorNode(input, retval.start, input.LT(-1), re);
    } finally {
      // do for sure before leaving
    }
    return retval;
  }
  // $ANTLR end "lexerBlock"


  public static class lexerCommands_return extends ParserRuleReturnScope {
    GrammarAST tree;

    @Override
    public GrammarAST getTree() {
      return tree;
    }
  }

  ;


  // $ANTLR start "lexerCommands"
  // org/antlr/v4/parse/ANTLRParser.g:582:1: lexerCommands : RARROW lexerCommand ( COMMA lexerCommand )* -> ( lexerCommand )+ ;
  public final ANTLRParser.lexerCommands_return lexerCommands() throws RecognitionException {
    ANTLRParser.lexerCommands_return retval = new ANTLRParser.lexerCommands_return();
    retval.start = input.LT(1);

    GrammarAST root_0 = null;

    Token RARROW141 = null;
    Token COMMA143 = null;
    ParserRuleReturnScope lexerCommand142 = null;
    ParserRuleReturnScope lexerCommand144 = null;

    GrammarAST RARROW141_tree = null;
    GrammarAST COMMA143_tree = null;
    RewriteRuleTokenStream stream_RARROW = new RewriteRuleTokenStream(adaptor, "token RARROW");
    RewriteRuleTokenStream stream_COMMA = new RewriteRuleTokenStream(adaptor, "token COMMA");
    RewriteRuleSubtreeStream stream_lexerCommand = new RewriteRuleSubtreeStream(adaptor, "rule lexerCommand");

    try {
      // org/antlr/v4/parse/ANTLRParser.g:583:2: ( RARROW lexerCommand ( COMMA lexerCommand )* -> ( lexerCommand )+ )
      // org/antlr/v4/parse/ANTLRParser.g:583:4: RARROW lexerCommand ( COMMA lexerCommand )*
      {
        RARROW141 = (Token) match(input, RARROW, FOLLOW_RARROW_in_lexerCommands2682);
        stream_RARROW.add(RARROW141);

        pushFollow(FOLLOW_lexerCommand_in_lexerCommands2684);
        lexerCommand142 = lexerCommand();
        state._fsp--;

        stream_lexerCommand.add(lexerCommand142.getTree());
        // org/antlr/v4/parse/ANTLRParser.g:583:24: ( COMMA lexerCommand )*
        loop45:
        while (true) {
          int alt45 = 2;
          int LA45_0 = input.LA(1);
          if ((LA45_0 == COMMA)) {
            alt45 = 1;
          }

          switch (alt45) {
            case 1:
              // org/antlr/v4/parse/ANTLRParser.g:583:25: COMMA lexerCommand
            {
              COMMA143 = (Token) match(input, COMMA, FOLLOW_COMMA_in_lexerCommands2687);
              stream_COMMA.add(COMMA143);

              pushFollow(FOLLOW_lexerCommand_in_lexerCommands2689);
              lexerCommand144 = lexerCommand();
              state._fsp--;

              stream_lexerCommand.add(lexerCommand144.getTree());
            }
            break;

            default:
              break loop45;
          }
        }


        // AST REWRITE
        // elements: lexerCommand
        // token labels:
        // rule labels: retval
        // token list labels:
        // rule list labels:
        // wildcard labels:
        retval.tree = root_0;
        RewriteRuleSubtreeStream stream_retval = new RewriteRuleSubtreeStream(adaptor, "rule retval", retval != null ? retval.getTree() : null);

        root_0 = (GrammarAST) adaptor.nil();
        // 583:46: -> ( lexerCommand )+
        {
          if (!(stream_lexerCommand.hasNext())) {
            throw new RewriteEarlyExitException();
          }
          while (stream_lexerCommand.hasNext()) {
            adaptor.addChild(root_0, stream_lexerCommand.nextTree());
          }
          stream_lexerCommand.reset();

        }


        retval.tree = root_0;

      }

      retval.stop = input.LT(-1);

      retval.tree = (GrammarAST) adaptor.rulePostProcessing(root_0);
      adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

    } catch (RecognitionException re) {
      reportError(re);
      recover(input, re);
      retval.tree = (GrammarAST) adaptor.errorNode(input, retval.start, input.LT(-1), re);
    } finally {
      // do for sure before leaving
    }
    return retval;
  }
  // $ANTLR end "lexerCommands"


  public static class lexerCommand_return extends ParserRuleReturnScope {
    GrammarAST tree;

    @Override
    public GrammarAST getTree() {
      return tree;
    }
  }

  ;


  // $ANTLR start "lexerCommand"
  // org/antlr/v4/parse/ANTLRParser.g:586:1: lexerCommand : ( lexerCommandName LPAREN lexerCommandExpr RPAREN -> ^( LEXER_ACTION_CALL lexerCommandName lexerCommandExpr ) | lexerCommandName );
  public final ANTLRParser.lexerCommand_return lexerCommand() throws RecognitionException {
    ANTLRParser.lexerCommand_return retval = new ANTLRParser.lexerCommand_return();
    retval.start = input.LT(1);

    GrammarAST root_0 = null;

    Token LPAREN146 = null;
    Token RPAREN148 = null;
    ParserRuleReturnScope lexerCommandName145 = null;
    ParserRuleReturnScope lexerCommandExpr147 = null;
    ParserRuleReturnScope lexerCommandName149 = null;

    GrammarAST LPAREN146_tree = null;
    GrammarAST RPAREN148_tree = null;
    RewriteRuleTokenStream stream_LPAREN = new RewriteRuleTokenStream(adaptor, "token LPAREN");
    RewriteRuleTokenStream stream_RPAREN = new RewriteRuleTokenStream(adaptor, "token RPAREN");
    RewriteRuleSubtreeStream stream_lexerCommandName = new RewriteRuleSubtreeStream(adaptor, "rule lexerCommandName");
    RewriteRuleSubtreeStream stream_lexerCommandExpr = new RewriteRuleSubtreeStream(adaptor, "rule lexerCommandExpr");

    try {
      // org/antlr/v4/parse/ANTLRParser.g:587:2: ( lexerCommandName LPAREN lexerCommandExpr RPAREN -> ^( LEXER_ACTION_CALL lexerCommandName lexerCommandExpr ) | lexerCommandName )
      int alt46 = 2;
      switch (input.LA(1)) {
        case RULE_REF: {
          int LA46_1 = input.LA(2);
          if ((LA46_1 == LPAREN)) {
            alt46 = 1;
          } else if ((LA46_1 == COMMA || LA46_1 == OR || LA46_1 == RPAREN || LA46_1 == SEMI)) {
            alt46 = 2;
          } else {
            int nvaeMark = input.mark();
            try {
              input.consume();
              NoViableAltException nvae =
                new NoViableAltException("", 46, 1, input);
              throw nvae;
            } finally {
              input.rewind(nvaeMark);
            }
          }

        }
        break;
        case TOKEN_REF: {
          int LA46_2 = input.LA(2);
          if ((LA46_2 == LPAREN)) {
            alt46 = 1;
          } else if ((LA46_2 == COMMA || LA46_2 == OR || LA46_2 == RPAREN || LA46_2 == SEMI)) {
            alt46 = 2;
          } else {
            int nvaeMark = input.mark();
            try {
              input.consume();
              NoViableAltException nvae =
                new NoViableAltException("", 46, 2, input);
              throw nvae;
            } finally {
              input.rewind(nvaeMark);
            }
          }

        }
        break;
        case MODE: {
          int LA46_3 = input.LA(2);
          if ((LA46_3 == LPAREN)) {
            alt46 = 1;
          } else if ((LA46_3 == COMMA || LA46_3 == OR || LA46_3 == RPAREN || LA46_3 == SEMI)) {
            alt46 = 2;
          } else {
            int nvaeMark = input.mark();
            try {
              input.consume();
              NoViableAltException nvae =
                new NoViableAltException("", 46, 3, input);
              throw nvae;
            } finally {
              input.rewind(nvaeMark);
            }
          }

        }
        break;
        default:
          NoViableAltException nvae =
            new NoViableAltException("", 46, 0, input);
          throw nvae;
      }
      switch (alt46) {
        case 1:
          // org/antlr/v4/parse/ANTLRParser.g:587:4: lexerCommandName LPAREN lexerCommandExpr RPAREN
        {
          pushFollow(FOLLOW_lexerCommandName_in_lexerCommand2707);
          lexerCommandName145 = lexerCommandName();
          state._fsp--;

          stream_lexerCommandName.add(lexerCommandName145.getTree());
          LPAREN146 = (Token) match(input, LPAREN, FOLLOW_LPAREN_in_lexerCommand2709);
          stream_LPAREN.add(LPAREN146);

          pushFollow(FOLLOW_lexerCommandExpr_in_lexerCommand2711);
          lexerCommandExpr147 = lexerCommandExpr();
          state._fsp--;

          stream_lexerCommandExpr.add(lexerCommandExpr147.getTree());
          RPAREN148 = (Token) match(input, RPAREN, FOLLOW_RPAREN_in_lexerCommand2713);
          stream_RPAREN.add(RPAREN148);


          // AST REWRITE
          // elements: lexerCommandName, lexerCommandExpr
          // token labels:
          // rule labels: retval
          // token list labels:
          // rule list labels:
          // wildcard labels:
          retval.tree = root_0;
          RewriteRuleSubtreeStream stream_retval = new RewriteRuleSubtreeStream(adaptor, "rule retval", retval != null ? retval.getTree() : null);

          root_0 = (GrammarAST) adaptor.nil();
          // 587:52: -> ^( LEXER_ACTION_CALL lexerCommandName lexerCommandExpr )
          {
            // org/antlr/v4/parse/ANTLRParser.g:587:55: ^( LEXER_ACTION_CALL lexerCommandName lexerCommandExpr )
            {
              GrammarAST root_1 = (GrammarAST) adaptor.nil();
              root_1 = (GrammarAST) adaptor.becomeRoot((GrammarAST) adaptor.create(LEXER_ACTION_CALL, "LEXER_ACTION_CALL"), root_1);
              adaptor.addChild(root_1, stream_lexerCommandName.nextTree());
              adaptor.addChild(root_1, stream_lexerCommandExpr.nextTree());
              adaptor.addChild(root_0, root_1);
            }

          }


          retval.tree = root_0;

        }
        break;
        case 2:
          // org/antlr/v4/parse/ANTLRParser.g:588:4: lexerCommandName
        {
          root_0 = (GrammarAST) adaptor.nil();


          pushFollow(FOLLOW_lexerCommandName_in_lexerCommand2728);
          lexerCommandName149 = lexerCommandName();
          state._fsp--;

          adaptor.addChild(root_0, lexerCommandName149.getTree());

        }
        break;

      }
      retval.stop = input.LT(-1);

      retval.tree = (GrammarAST) adaptor.rulePostProcessing(root_0);
      adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

    } catch (RecognitionException re) {
      reportError(re);
      recover(input, re);
      retval.tree = (GrammarAST) adaptor.errorNode(input, retval.start, input.LT(-1), re);
    } finally {
      // do for sure before leaving
    }
    return retval;
  }
  // $ANTLR end "lexerCommand"


  public static class lexerCommandExpr_return extends ParserRuleReturnScope {
    GrammarAST tree;

    @Override
    public GrammarAST getTree() {
      return tree;
    }
  }

  ;


  // $ANTLR start "lexerCommandExpr"
  // org/antlr/v4/parse/ANTLRParser.g:591:1: lexerCommandExpr : ( id | INT );
  public final ANTLRParser.lexerCommandExpr_return lexerCommandExpr() throws RecognitionException {
    ANTLRParser.lexerCommandExpr_return retval = new ANTLRParser.lexerCommandExpr_return();
    retval.start = input.LT(1);

    GrammarAST root_0 = null;

    Token INT151 = null;
    ParserRuleReturnScope id150 = null;

    GrammarAST INT151_tree = null;

    try {
      // org/antlr/v4/parse/ANTLRParser.g:592:2: ( id | INT )
      int alt47 = 2;
      int LA47_0 = input.LA(1);
      if ((LA47_0 == RULE_REF || LA47_0 == TOKEN_REF)) {
        alt47 = 1;
      } else if ((LA47_0 == INT)) {
        alt47 = 2;
      } else {
        NoViableAltException nvae =
          new NoViableAltException("", 47, 0, input);
        throw nvae;
      }

      switch (alt47) {
        case 1:
          // org/antlr/v4/parse/ANTLRParser.g:592:4: id
        {
          root_0 = (GrammarAST) adaptor.nil();


          pushFollow(FOLLOW_id_in_lexerCommandExpr2739);
          id150 = id();
          state._fsp--;

          adaptor.addChild(root_0, id150.getTree());

        }
        break;
        case 2:
          // org/antlr/v4/parse/ANTLRParser.g:593:4: INT
        {
          root_0 = (GrammarAST) adaptor.nil();


          INT151 = (Token) match(input, INT, FOLLOW_INT_in_lexerCommandExpr2744);
          INT151_tree = (GrammarAST) adaptor.create(INT151);
          adaptor.addChild(root_0, INT151_tree);

        }
        break;

      }
      retval.stop = input.LT(-1);

      retval.tree = (GrammarAST) adaptor.rulePostProcessing(root_0);
      adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

    } catch (RecognitionException re) {
      reportError(re);
      recover(input, re);
      retval.tree = (GrammarAST) adaptor.errorNode(input, retval.start, input.LT(-1), re);
    } finally {
      // do for sure before leaving
    }
    return retval;
  }
  // $ANTLR end "lexerCommandExpr"


  public static class lexerCommandName_return extends ParserRuleReturnScope {
    GrammarAST tree;

    @Override
    public GrammarAST getTree() {
      return tree;
    }
  }

  ;


  // $ANTLR start "lexerCommandName"
  // org/antlr/v4/parse/ANTLRParser.g:596:1: lexerCommandName : ( id | MODE -> ID[$MODE] );
  public final ANTLRParser.lexerCommandName_return lexerCommandName() throws RecognitionException {
    ANTLRParser.lexerCommandName_return retval = new ANTLRParser.lexerCommandName_return();
    retval.start = input.LT(1);

    GrammarAST root_0 = null;

    Token MODE153 = null;
    ParserRuleReturnScope id152 = null;

    GrammarAST MODE153_tree = null;
    RewriteRuleTokenStream stream_MODE = new RewriteRuleTokenStream(adaptor, "token MODE");

    try {
      // org/antlr/v4/parse/ANTLRParser.g:597:9: ( id | MODE -> ID[$MODE] )
      int alt48 = 2;
      int LA48_0 = input.LA(1);
      if ((LA48_0 == RULE_REF || LA48_0 == TOKEN_REF)) {
        alt48 = 1;
      } else if ((LA48_0 == MODE)) {
        alt48 = 2;
      } else {
        NoViableAltException nvae =
          new NoViableAltException("", 48, 0, input);
        throw nvae;
      }

      switch (alt48) {
        case 1:
          // org/antlr/v4/parse/ANTLRParser.g:597:17: id
        {
          root_0 = (GrammarAST) adaptor.nil();


          pushFollow(FOLLOW_id_in_lexerCommandName2768);
          id152 = id();
          state._fsp--;

          adaptor.addChild(root_0, id152.getTree());

        }
        break;
        case 2:
          // org/antlr/v4/parse/ANTLRParser.g:598:17: MODE
        {
          MODE153 = (Token) match(input, MODE, FOLLOW_MODE_in_lexerCommandName2786);
          stream_MODE.add(MODE153);


          // AST REWRITE
          // elements:
          // token labels:
          // rule labels: retval
          // token list labels:
          // rule list labels:
          // wildcard labels:
          retval.tree = root_0;
          RewriteRuleSubtreeStream stream_retval = new RewriteRuleSubtreeStream(adaptor, "rule retval", retval != null ? retval.getTree() : null);

          root_0 = (GrammarAST) adaptor.nil();
          // 598:25: -> ID[$MODE]
          {
            adaptor.addChild(root_0, (GrammarAST) adaptor.create(ID, MODE153));
          }


          retval.tree = root_0;

        }
        break;

      }
      retval.stop = input.LT(-1);

      retval.tree = (GrammarAST) adaptor.rulePostProcessing(root_0);
      adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

    } catch (RecognitionException re) {
      reportError(re);
      recover(input, re);
      retval.tree = (GrammarAST) adaptor.errorNode(input, retval.start, input.LT(-1), re);
    } finally {
      // do for sure before leaving
    }
    return retval;
  }
  // $ANTLR end "lexerCommandName"


  public static class altList_return extends ParserRuleReturnScope {
    GrammarAST tree;

    @Override
    public GrammarAST getTree() {
      return tree;
    }
  }

  ;


  // $ANTLR start "altList"
  // org/antlr/v4/parse/ANTLRParser.g:601:1: altList : alternative ( OR alternative )* -> ( alternative )+ ;
  public final ANTLRParser.altList_return altList() throws RecognitionException {
    ANTLRParser.altList_return retval = new ANTLRParser.altList_return();
    retval.start = input.LT(1);

    GrammarAST root_0 = null;

    Token OR155 = null;
    ParserRuleReturnScope alternative154 = null;
    ParserRuleReturnScope alternative156 = null;

    GrammarAST OR155_tree = null;
    RewriteRuleTokenStream stream_OR = new RewriteRuleTokenStream(adaptor, "token OR");
    RewriteRuleSubtreeStream stream_alternative = new RewriteRuleSubtreeStream(adaptor, "rule alternative");

    try {
      // org/antlr/v4/parse/ANTLRParser.g:602:5: ( alternative ( OR alternative )* -> ( alternative )+ )
      // org/antlr/v4/parse/ANTLRParser.g:602:7: alternative ( OR alternative )*
      {
        pushFollow(FOLLOW_alternative_in_altList2814);
        alternative154 = alternative();
        state._fsp--;

        stream_alternative.add(alternative154.getTree());
        // org/antlr/v4/parse/ANTLRParser.g:602:19: ( OR alternative )*
        loop49:
        while (true) {
          int alt49 = 2;
          int LA49_0 = input.LA(1);
          if ((LA49_0 == OR)) {
            alt49 = 1;
          }

          switch (alt49) {
            case 1:
              // org/antlr/v4/parse/ANTLRParser.g:602:20: OR alternative
            {
              OR155 = (Token) match(input, OR, FOLLOW_OR_in_altList2817);
              stream_OR.add(OR155);

              pushFollow(FOLLOW_alternative_in_altList2819);
              alternative156 = alternative();
              state._fsp--;

              stream_alternative.add(alternative156.getTree());
            }
            break;

            default:
              break loop49;
          }
        }


        // AST REWRITE
        // elements: alternative
        // token labels:
        // rule labels: retval
        // token list labels:
        // rule list labels:
        // wildcard labels:
        retval.tree = root_0;
        RewriteRuleSubtreeStream stream_retval = new RewriteRuleSubtreeStream(adaptor, "rule retval", retval != null ? retval.getTree() : null);

        root_0 = (GrammarAST) adaptor.nil();
        // 602:37: -> ( alternative )+
        {
          if (!(stream_alternative.hasNext())) {
            throw new RewriteEarlyExitException();
          }
          while (stream_alternative.hasNext()) {
            adaptor.addChild(root_0, stream_alternative.nextTree());
          }
          stream_alternative.reset();

        }


        retval.tree = root_0;

      }

      retval.stop = input.LT(-1);

      retval.tree = (GrammarAST) adaptor.rulePostProcessing(root_0);
      adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

    } catch (RecognitionException re) {
      reportError(re);
      recover(input, re);
      retval.tree = (GrammarAST) adaptor.errorNode(input, retval.start, input.LT(-1), re);
    } finally {
      // do for sure before leaving
    }
    return retval;
  }
  // $ANTLR end "altList"


  public static class alternative_return extends ParserRuleReturnScope {
    GrammarAST tree;

    @Override
    public GrammarAST getTree() {
      return tree;
    }
  }

  ;


  // $ANTLR start "alternative"
  // org/antlr/v4/parse/ANTLRParser.g:606:1: alternative : (o= elementOptions )? ( (e+= element )+ -> ^( ALT ( elementOptions )? ( $e)+ ) | -> ^( ALT ( elementOptions )? EPSILON ) ) ;
  public final ANTLRParser.alternative_return alternative() throws RecognitionException {
    ANTLRParser.alternative_return retval = new ANTLRParser.alternative_return();
    retval.start = input.LT(1);

    GrammarAST root_0 = null;

    List<Object> list_e = null;
    ParserRuleReturnScope o = null;
    RuleReturnScope e = null;
    RewriteRuleSubtreeStream stream_elementOptions = new RewriteRuleSubtreeStream(adaptor, "rule elementOptions");
    RewriteRuleSubtreeStream stream_element = new RewriteRuleSubtreeStream(adaptor, "rule element");

    paraphrases.push("matching alternative");
    try {
      // org/antlr/v4/parse/ANTLRParser.g:612:2: ( (o= elementOptions )? ( (e+= element )+ -> ^( ALT ( elementOptions )? ( $e)+ ) | -> ^( ALT ( elementOptions )? EPSILON ) ) )
      // org/antlr/v4/parse/ANTLRParser.g:612:4: (o= elementOptions )? ( (e+= element )+ -> ^( ALT ( elementOptions )? ( $e)+ ) | -> ^( ALT ( elementOptions )? EPSILON ) )
      {
        // org/antlr/v4/parse/ANTLRParser.g:612:5: (o= elementOptions )?
        int alt50 = 2;
        int LA50_0 = input.LA(1);
        if ((LA50_0 == LT)) {
          alt50 = 1;
        }
        switch (alt50) {
          case 1:
            // org/antlr/v4/parse/ANTLRParser.g:612:5: o= elementOptions
          {
            pushFollow(FOLLOW_elementOptions_in_alternative2853);
            o = elementOptions();
            state._fsp--;

            stream_elementOptions.add(o.getTree());
          }
          break;

        }

        // org/antlr/v4/parse/ANTLRParser.g:613:3: ( (e+= element )+ -> ^( ALT ( elementOptions )? ( $e)+ ) | -> ^( ALT ( elementOptions )? EPSILON ) )
        int alt52 = 2;
        int LA52_0 = input.LA(1);
        if ((LA52_0 == ACTION || LA52_0 == DOT || LA52_0 == LPAREN || LA52_0 == NOT || LA52_0 == RULE_REF || LA52_0 == SEMPRED || LA52_0 == STRING_LITERAL || LA52_0 == TOKEN_REF)) {
          alt52 = 1;
        } else if ((LA52_0 == EOF || LA52_0 == OR || LA52_0 == POUND || LA52_0 == RPAREN || LA52_0 == SEMI)) {
          alt52 = 2;
        } else {
          NoViableAltException nvae =
            new NoViableAltException("", 52, 0, input);
          throw nvae;
        }

        switch (alt52) {
          case 1:
            // org/antlr/v4/parse/ANTLRParser.g:613:5: (e+= element )+
          {
            // org/antlr/v4/parse/ANTLRParser.g:613:6: (e+= element )+
            int cnt51 = 0;
            loop51:
            while (true) {
              int alt51 = 2;
              int LA51_0 = input.LA(1);
              if ((LA51_0 == ACTION || LA51_0 == DOT || LA51_0 == LPAREN || LA51_0 == NOT || LA51_0 == RULE_REF || LA51_0 == SEMPRED || LA51_0 == STRING_LITERAL || LA51_0 == TOKEN_REF)) {
                alt51 = 1;
              }

              switch (alt51) {
                case 1:
                  // org/antlr/v4/parse/ANTLRParser.g:613:6: e+= element
                {
                  pushFollow(FOLLOW_element_in_alternative2862);
                  e = element();
                  state._fsp--;

                  stream_element.add(e.getTree());
                  if (list_e == null) list_e = new ArrayList<Object>();
                  list_e.add(e.getTree());
                }
                break;

                default:
                  if (cnt51 >= 1) break loop51;
                  EarlyExitException eee = new EarlyExitException(51, input);
                  throw eee;
              }
              cnt51++;
            }


            // AST REWRITE
            // elements: e, elementOptions
            // token labels:
            // rule labels: retval
            // token list labels:
            // rule list labels: e
            // wildcard labels:
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval = new RewriteRuleSubtreeStream(adaptor, "rule retval", retval != null ? retval.getTree() : null);
            RewriteRuleSubtreeStream stream_e = new RewriteRuleSubtreeStream(adaptor, "token e", list_e);
            root_0 = (GrammarAST) adaptor.nil();
            // 613:37: -> ^( ALT ( elementOptions )? ( $e)+ )
            {
              // org/antlr/v4/parse/ANTLRParser.g:613:40: ^( ALT ( elementOptions )? ( $e)+ )
              {
                GrammarAST root_1 = (GrammarAST) adaptor.nil();
                root_1 = (GrammarAST) adaptor.becomeRoot(new AltAST(ALT), root_1);
                // org/antlr/v4/parse/ANTLRParser.g:613:54: ( elementOptions )?
                if (stream_elementOptions.hasNext()) {
                  adaptor.addChild(root_1, stream_elementOptions.nextTree());
                }
                stream_elementOptions.reset();

                if (!(stream_e.hasNext())) {
                  throw new RewriteEarlyExitException();
                }
                while (stream_e.hasNext()) {
                  adaptor.addChild(root_1, stream_e.nextTree());
                }
                stream_e.reset();

                adaptor.addChild(root_0, root_1);
              }

            }


            retval.tree = root_0;

          }
          break;
          case 2:
            // org/antlr/v4/parse/ANTLRParser.g:614:39:
          {

            // AST REWRITE
            // elements: elementOptions
            // token labels:
            // rule labels: retval
            // token list labels:
            // rule list labels:
            // wildcard labels:
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval = new RewriteRuleSubtreeStream(adaptor, "rule retval", retval != null ? retval.getTree() : null);

            root_0 = (GrammarAST) adaptor.nil();
            // 614:39: -> ^( ALT ( elementOptions )? EPSILON )
            {
              // org/antlr/v4/parse/ANTLRParser.g:614:42: ^( ALT ( elementOptions )? EPSILON )
              {
                GrammarAST root_1 = (GrammarAST) adaptor.nil();
                root_1 = (GrammarAST) adaptor.becomeRoot(new AltAST(ALT), root_1);
                // org/antlr/v4/parse/ANTLRParser.g:614:56: ( elementOptions )?
                if (stream_elementOptions.hasNext()) {
                  adaptor.addChild(root_1, stream_elementOptions.nextTree());
                }
                stream_elementOptions.reset();

                adaptor.addChild(root_1, (GrammarAST) adaptor.create(EPSILON, "EPSILON"));
                adaptor.addChild(root_0, root_1);
              }

            }


            retval.tree = root_0;

          }
          break;

        }

      }

      retval.stop = input.LT(-1);

      retval.tree = (GrammarAST) adaptor.rulePostProcessing(root_0);
      adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);


      paraphrases.pop();
      Grammar.setNodeOptions(retval.tree, (o != null ? ((GrammarAST) o.getTree()) : null));

    } catch (RecognitionException re) {
      reportError(re);
      recover(input, re);
      retval.tree = (GrammarAST) adaptor.errorNode(input, retval.start, input.LT(-1), re);
    } finally {
      // do for sure before leaving
    }
    return retval;
  }
  // $ANTLR end "alternative"


  public static class element_return extends ParserRuleReturnScope {
    GrammarAST tree;

    @Override
    public GrammarAST getTree() {
      return tree;
    }
  }

  ;


  // $ANTLR start "element"
  // org/antlr/v4/parse/ANTLRParser.g:618:1: element : ( labeledElement ( ebnfSuffix -> ^( ebnfSuffix ^( BLOCK[$labeledElement.start,\"BLOCK\"] ^( ALT labeledElement ) ) ) | -> labeledElement ) | atom ( ebnfSuffix -> ^( ebnfSuffix ^( BLOCK[$atom.start,\"BLOCK\"] ^( ALT atom ) ) ) | -> atom ) | ebnf | actionElement );
  public final ANTLRParser.element_return element() throws RecognitionException {
    ANTLRParser.element_return retval = new ANTLRParser.element_return();
    retval.start = input.LT(1);

    GrammarAST root_0 = null;

    ParserRuleReturnScope labeledElement157 = null;
    ParserRuleReturnScope ebnfSuffix158 = null;
    ParserRuleReturnScope atom159 = null;
    ParserRuleReturnScope ebnfSuffix160 = null;
    ParserRuleReturnScope ebnf161 = null;
    ParserRuleReturnScope actionElement162 = null;

    RewriteRuleSubtreeStream stream_labeledElement = new RewriteRuleSubtreeStream(adaptor, "rule labeledElement");
    RewriteRuleSubtreeStream stream_atom = new RewriteRuleSubtreeStream(adaptor, "rule atom");
    RewriteRuleSubtreeStream stream_ebnfSuffix = new RewriteRuleSubtreeStream(adaptor, "rule ebnfSuffix");


    paraphrases.push("looking for rule element");
    int m = input.mark();

    try {
      // org/antlr/v4/parse/ANTLRParser.g:624:2: ( labeledElement ( ebnfSuffix -> ^( ebnfSuffix ^( BLOCK[$labeledElement.start,\"BLOCK\"] ^( ALT labeledElement ) ) ) | -> labeledElement ) | atom ( ebnfSuffix -> ^( ebnfSuffix ^( BLOCK[$atom.start,\"BLOCK\"] ^( ALT atom ) ) ) | -> atom ) | ebnf | actionElement )
      int alt55 = 4;
      switch (input.LA(1)) {
        case RULE_REF: {
          int LA55_1 = input.LA(2);
          if ((LA55_1 == ASSIGN || LA55_1 == PLUS_ASSIGN)) {
            alt55 = 1;
          } else if ((LA55_1 == EOF || LA55_1 == ACTION || LA55_1 == ARG_ACTION || LA55_1 == DOT || (LA55_1 >= LPAREN && LA55_1 <= LT) || LA55_1 == NOT || LA55_1 == OR || LA55_1 == PLUS || LA55_1 == POUND || LA55_1 == QUESTION || (LA55_1 >= RPAREN && LA55_1 <= SEMPRED) || (LA55_1 >= STAR && LA55_1 <= STRING_LITERAL) || LA55_1 == TOKEN_REF)) {
            alt55 = 2;
          } else {
            int nvaeMark = input.mark();
            try {
              input.consume();
              NoViableAltException nvae =
                new NoViableAltException("", 55, 1, input);
              throw nvae;
            } finally {
              input.rewind(nvaeMark);
            }
          }

        }
        break;
        case TOKEN_REF: {
          int LA55_2 = input.LA(2);
          if ((LA55_2 == ASSIGN || LA55_2 == PLUS_ASSIGN)) {
            alt55 = 1;
          } else if ((LA55_2 == EOF || LA55_2 == ACTION || LA55_2 == DOT || (LA55_2 >= LPAREN && LA55_2 <= LT) || LA55_2 == NOT || LA55_2 == OR || LA55_2 == PLUS || LA55_2 == POUND || LA55_2 == QUESTION || (LA55_2 >= RPAREN && LA55_2 <= SEMPRED) || (LA55_2 >= STAR && LA55_2 <= STRING_LITERAL) || LA55_2 == TOKEN_REF)) {
            alt55 = 2;
          } else {
            int nvaeMark = input.mark();
            try {
              input.consume();
              NoViableAltException nvae =
                new NoViableAltException("", 55, 2, input);
              throw nvae;
            } finally {
              input.rewind(nvaeMark);
            }
          }

        }
        break;
        case DOT:
        case NOT:
        case STRING_LITERAL: {
          alt55 = 2;
        }
        break;
        case LPAREN: {
          alt55 = 3;
        }
        break;
        case ACTION:
        case SEMPRED: {
          alt55 = 4;
        }
        break;
        default:
          NoViableAltException nvae =
            new NoViableAltException("", 55, 0, input);
          throw nvae;
      }
      switch (alt55) {
        case 1:
          // org/antlr/v4/parse/ANTLRParser.g:624:4: labeledElement ( ebnfSuffix -> ^( ebnfSuffix ^( BLOCK[$labeledElement.start,\"BLOCK\"] ^( ALT labeledElement ) ) ) | -> labeledElement )
        {
          pushFollow(FOLLOW_labeledElement_in_element2977);
          labeledElement157 = labeledElement();
          state._fsp--;

          stream_labeledElement.add(labeledElement157.getTree());
          // org/antlr/v4/parse/ANTLRParser.g:625:3: ( ebnfSuffix -> ^( ebnfSuffix ^( BLOCK[$labeledElement.start,\"BLOCK\"] ^( ALT labeledElement ) ) ) | -> labeledElement )
          int alt53 = 2;
          int LA53_0 = input.LA(1);
          if ((LA53_0 == PLUS || LA53_0 == QUESTION || LA53_0 == STAR)) {
            alt53 = 1;
          } else if ((LA53_0 == EOF || LA53_0 == ACTION || LA53_0 == DOT || LA53_0 == LPAREN || LA53_0 == NOT || LA53_0 == OR || LA53_0 == POUND || (LA53_0 >= RPAREN && LA53_0 <= SEMPRED) || LA53_0 == STRING_LITERAL || LA53_0 == TOKEN_REF)) {
            alt53 = 2;
          } else {
            NoViableAltException nvae =
              new NoViableAltException("", 53, 0, input);
            throw nvae;
          }

          switch (alt53) {
            case 1:
              // org/antlr/v4/parse/ANTLRParser.g:625:5: ebnfSuffix
            {
              pushFollow(FOLLOW_ebnfSuffix_in_element2983);
              ebnfSuffix158 = ebnfSuffix();
              state._fsp--;

              stream_ebnfSuffix.add(ebnfSuffix158.getTree());

              // AST REWRITE
              // elements: labeledElement, ebnfSuffix
              // token labels:
              // rule labels: retval
              // token list labels:
              // rule list labels:
              // wildcard labels:
              retval.tree = root_0;
              RewriteRuleSubtreeStream stream_retval = new RewriteRuleSubtreeStream(adaptor, "rule retval", retval != null ? retval.getTree() : null);

              root_0 = (GrammarAST) adaptor.nil();
              // 625:16: -> ^( ebnfSuffix ^( BLOCK[$labeledElement.start,\"BLOCK\"] ^( ALT labeledElement ) ) )
              {
                // org/antlr/v4/parse/ANTLRParser.g:625:19: ^( ebnfSuffix ^( BLOCK[$labeledElement.start,\"BLOCK\"] ^( ALT labeledElement ) ) )
                {
                  GrammarAST root_1 = (GrammarAST) adaptor.nil();
                  root_1 = (GrammarAST) adaptor.becomeRoot(stream_ebnfSuffix.nextNode(), root_1);
                  // org/antlr/v4/parse/ANTLRParser.g:625:33: ^( BLOCK[$labeledElement.start,\"BLOCK\"] ^( ALT labeledElement ) )
                  {
                    GrammarAST root_2 = (GrammarAST) adaptor.nil();
                    root_2 = (GrammarAST) adaptor.becomeRoot(new BlockAST(BLOCK, (labeledElement157 != null ? (labeledElement157.start) : null), "BLOCK"), root_2);
                    // org/antlr/v4/parse/ANTLRParser.g:625:82: ^( ALT labeledElement )
                    {
                      GrammarAST root_3 = (GrammarAST) adaptor.nil();
                      root_3 = (GrammarAST) adaptor.becomeRoot(new AltAST(ALT), root_3);
                      adaptor.addChild(root_3, stream_labeledElement.nextTree());
                      adaptor.addChild(root_2, root_3);
                    }

                    adaptor.addChild(root_1, root_2);
                  }

                  adaptor.addChild(root_0, root_1);
                }

              }


              retval.tree = root_0;

            }
            break;
            case 2:
              // org/antlr/v4/parse/ANTLRParser.g:626:8:
            {

              // AST REWRITE
              // elements: labeledElement
              // token labels:
              // rule labels: retval
              // token list labels:
              // rule list labels:
              // wildcard labels:
              retval.tree = root_0;
              RewriteRuleSubtreeStream stream_retval = new RewriteRuleSubtreeStream(adaptor, "rule retval", retval != null ? retval.getTree() : null);

              root_0 = (GrammarAST) adaptor.nil();
              // 626:8: -> labeledElement
              {
                adaptor.addChild(root_0, stream_labeledElement.nextTree());
              }


              retval.tree = root_0;

            }
            break;

          }

        }
        break;
        case 2:
          // org/antlr/v4/parse/ANTLRParser.g:628:4: atom ( ebnfSuffix -> ^( ebnfSuffix ^( BLOCK[$atom.start,\"BLOCK\"] ^( ALT atom ) ) ) | -> atom )
        {
          pushFollow(FOLLOW_atom_in_element3029);
          atom159 = atom();
          state._fsp--;

          stream_atom.add(atom159.getTree());
          // org/antlr/v4/parse/ANTLRParser.g:629:3: ( ebnfSuffix -> ^( ebnfSuffix ^( BLOCK[$atom.start,\"BLOCK\"] ^( ALT atom ) ) ) | -> atom )
          int alt54 = 2;
          int LA54_0 = input.LA(1);
          if ((LA54_0 == PLUS || LA54_0 == QUESTION || LA54_0 == STAR)) {
            alt54 = 1;
          } else if ((LA54_0 == EOF || LA54_0 == ACTION || LA54_0 == DOT || LA54_0 == LPAREN || LA54_0 == NOT || LA54_0 == OR || LA54_0 == POUND || (LA54_0 >= RPAREN && LA54_0 <= SEMPRED) || LA54_0 == STRING_LITERAL || LA54_0 == TOKEN_REF)) {
            alt54 = 2;
          } else {
            NoViableAltException nvae =
              new NoViableAltException("", 54, 0, input);
            throw nvae;
          }

          switch (alt54) {
            case 1:
              // org/antlr/v4/parse/ANTLRParser.g:629:5: ebnfSuffix
            {
              pushFollow(FOLLOW_ebnfSuffix_in_element3035);
              ebnfSuffix160 = ebnfSuffix();
              state._fsp--;

              stream_ebnfSuffix.add(ebnfSuffix160.getTree());

              // AST REWRITE
              // elements: atom, ebnfSuffix
              // token labels:
              // rule labels: retval
              // token list labels:
              // rule list labels:
              // wildcard labels:
              retval.tree = root_0;
              RewriteRuleSubtreeStream stream_retval = new RewriteRuleSubtreeStream(adaptor, "rule retval", retval != null ? retval.getTree() : null);

              root_0 = (GrammarAST) adaptor.nil();
              // 629:16: -> ^( ebnfSuffix ^( BLOCK[$atom.start,\"BLOCK\"] ^( ALT atom ) ) )
              {
                // org/antlr/v4/parse/ANTLRParser.g:629:19: ^( ebnfSuffix ^( BLOCK[$atom.start,\"BLOCK\"] ^( ALT atom ) ) )
                {
                  GrammarAST root_1 = (GrammarAST) adaptor.nil();
                  root_1 = (GrammarAST) adaptor.becomeRoot(stream_ebnfSuffix.nextNode(), root_1);
                  // org/antlr/v4/parse/ANTLRParser.g:629:33: ^( BLOCK[$atom.start,\"BLOCK\"] ^( ALT atom ) )
                  {
                    GrammarAST root_2 = (GrammarAST) adaptor.nil();
                    root_2 = (GrammarAST) adaptor.becomeRoot(new BlockAST(BLOCK, (atom159 != null ? (atom159.start) : null), "BLOCK"), root_2);
                    // org/antlr/v4/parse/ANTLRParser.g:629:72: ^( ALT atom )
                    {
                      GrammarAST root_3 = (GrammarAST) adaptor.nil();
                      root_3 = (GrammarAST) adaptor.becomeRoot(new AltAST(ALT), root_3);
                      adaptor.addChild(root_3, stream_atom.nextTree());
                      adaptor.addChild(root_2, root_3);
                    }

                    adaptor.addChild(root_1, root_2);
                  }

                  adaptor.addChild(root_0, root_1);
                }

              }


              retval.tree = root_0;

            }
            break;
            case 2:
              // org/antlr/v4/parse/ANTLRParser.g:630:8:
            {

              // AST REWRITE
              // elements: atom
              // token labels:
              // rule labels: retval
              // token list labels:
              // rule list labels:
              // wildcard labels:
              retval.tree = root_0;
              RewriteRuleSubtreeStream stream_retval = new RewriteRuleSubtreeStream(adaptor, "rule retval", retval != null ? retval.getTree() : null);

              root_0 = (GrammarAST) adaptor.nil();
              // 630:8: -> atom
              {
                adaptor.addChild(root_0, stream_atom.nextTree());
              }


              retval.tree = root_0;

            }
            break;

          }

        }
        break;
        case 3:
          // org/antlr/v4/parse/ANTLRParser.g:632:4: ebnf
        {
          root_0 = (GrammarAST) adaptor.nil();


          pushFollow(FOLLOW_ebnf_in_element3081);
          ebnf161 = ebnf();
          state._fsp--;

          adaptor.addChild(root_0, ebnf161.getTree());

        }
        break;
        case 4:
          // org/antlr/v4/parse/ANTLRParser.g:633:4: actionElement
        {
          root_0 = (GrammarAST) adaptor.nil();


          pushFollow(FOLLOW_actionElement_in_element3086);
          actionElement162 = actionElement();
          state._fsp--;

          adaptor.addChild(root_0, actionElement162.getTree());

        }
        break;

      }
      retval.stop = input.LT(-1);

      retval.tree = (GrammarAST) adaptor.rulePostProcessing(root_0);
      adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

      paraphrases.pop();
    } catch (RecognitionException re) {

      retval.tree = (GrammarAST) adaptor.errorNode(input, retval.start, input.LT(-1), re);
      int ttype = input.get(input.range()).getType();
      // look for anything that really belongs at the start of the rule minus the initial ID
      if (ttype == COLON || ttype == RETURNS || ttype == CATCH || ttype == FINALLY || ttype == AT) {
        RecognitionException missingSemi =
          new v4ParserException("unterminated rule (missing ';') detected at '" +
            input.LT(1).getText() + " " + input.LT(2).getText() + "'", input);
        reportError(missingSemi);
        if (ttype == CATCH || ttype == FINALLY) {
          input.seek(input.range()); // ignore what's before rule trailer stuff
        }
        if (ttype == RETURNS || ttype == AT) { // scan back looking for ID of rule header
          int p = input.index();
          Token t = input.get(p);
          while (t.getType() != RULE_REF && t.getType() != TOKEN_REF) {
            p--;
            t = input.get(p);
          }
          input.seek(p);
        }
        throw new ResyncToEndOfRuleBlock(); // make sure it goes back to rule block level to recover
      }
      reportError(re);
      recover(input, re);

    } finally {
      // do for sure before leaving
    }
    return retval;
  }
  // $ANTLR end "element"


  public static class actionElement_return extends ParserRuleReturnScope {
    GrammarAST tree;

    @Override
    public GrammarAST getTree() {
      return tree;
    }
  }

  ;


  // $ANTLR start "actionElement"
  // org/antlr/v4/parse/ANTLRParser.g:662:1: actionElement : ( ACTION | ACTION elementOptions -> ^( ACTION elementOptions ) | SEMPRED | SEMPRED elementOptions -> ^( SEMPRED elementOptions ) );
  public final ANTLRParser.actionElement_return actionElement() throws RecognitionException {
    ANTLRParser.actionElement_return retval = new ANTLRParser.actionElement_return();
    retval.start = input.LT(1);

    GrammarAST root_0 = null;

    Token ACTION163 = null;
    Token ACTION164 = null;
    Token SEMPRED166 = null;
    Token SEMPRED167 = null;
    ParserRuleReturnScope elementOptions165 = null;
    ParserRuleReturnScope elementOptions168 = null;

    GrammarAST ACTION163_tree = null;
    GrammarAST ACTION164_tree = null;
    GrammarAST SEMPRED166_tree = null;
    GrammarAST SEMPRED167_tree = null;
    RewriteRuleTokenStream stream_ACTION = new RewriteRuleTokenStream(adaptor, "token ACTION");
    RewriteRuleTokenStream stream_SEMPRED = new RewriteRuleTokenStream(adaptor, "token SEMPRED");
    RewriteRuleSubtreeStream stream_elementOptions = new RewriteRuleSubtreeStream(adaptor, "rule elementOptions");

    try {
      // org/antlr/v4/parse/ANTLRParser.g:669:2: ( ACTION | ACTION elementOptions -> ^( ACTION elementOptions ) | SEMPRED | SEMPRED elementOptions -> ^( SEMPRED elementOptions ) )
      int alt56 = 4;
      int LA56_0 = input.LA(1);
      if ((LA56_0 == ACTION)) {
        int LA56_1 = input.LA(2);
        if ((LA56_1 == EOF || LA56_1 == ACTION || LA56_1 == DOT || LA56_1 == LEXER_CHAR_SET || LA56_1 == LPAREN || LA56_1 == NOT || LA56_1 == OR || LA56_1 == POUND || LA56_1 == RARROW || (LA56_1 >= RPAREN && LA56_1 <= SEMPRED) || LA56_1 == STRING_LITERAL || LA56_1 == TOKEN_REF)) {
          alt56 = 1;
        } else if ((LA56_1 == LT)) {
          alt56 = 2;
        } else {
          int nvaeMark = input.mark();
          try {
            input.consume();
            NoViableAltException nvae =
              new NoViableAltException("", 56, 1, input);
            throw nvae;
          } finally {
            input.rewind(nvaeMark);
          }
        }

      } else if ((LA56_0 == SEMPRED)) {
        int LA56_2 = input.LA(2);
        if ((LA56_2 == EOF || LA56_2 == ACTION || LA56_2 == DOT || LA56_2 == LEXER_CHAR_SET || LA56_2 == LPAREN || LA56_2 == NOT || LA56_2 == OR || LA56_2 == POUND || LA56_2 == RARROW || (LA56_2 >= RPAREN && LA56_2 <= SEMPRED) || LA56_2 == STRING_LITERAL || LA56_2 == TOKEN_REF)) {
          alt56 = 3;
        } else if ((LA56_2 == LT)) {
          alt56 = 4;
        } else {
          int nvaeMark = input.mark();
          try {
            input.consume();
            NoViableAltException nvae =
              new NoViableAltException("", 56, 2, input);
            throw nvae;
          } finally {
            input.rewind(nvaeMark);
          }
        }

      } else {
        NoViableAltException nvae =
          new NoViableAltException("", 56, 0, input);
        throw nvae;
      }

      switch (alt56) {
        case 1:
          // org/antlr/v4/parse/ANTLRParser.g:669:4: ACTION
        {
          root_0 = (GrammarAST) adaptor.nil();


          ACTION163 = (Token) match(input, ACTION, FOLLOW_ACTION_in_actionElement3112);
          ACTION163_tree = new ActionAST(ACTION163);
          adaptor.addChild(root_0, ACTION163_tree);

        }
        break;
        case 2:
          // org/antlr/v4/parse/ANTLRParser.g:670:6: ACTION elementOptions
        {
          ACTION164 = (Token) match(input, ACTION, FOLLOW_ACTION_in_actionElement3122);
          stream_ACTION.add(ACTION164);

          pushFollow(FOLLOW_elementOptions_in_actionElement3124);
          elementOptions165 = elementOptions();
          state._fsp--;

          stream_elementOptions.add(elementOptions165.getTree());

          // AST REWRITE
          // elements: ACTION, elementOptions
          // token labels:
          // rule labels: retval
          // token list labels:
          // rule list labels:
          // wildcard labels:
          retval.tree = root_0;
          RewriteRuleSubtreeStream stream_retval = new RewriteRuleSubtreeStream(adaptor, "rule retval", retval != null ? retval.getTree() : null);

          root_0 = (GrammarAST) adaptor.nil();
          // 670:28: -> ^( ACTION elementOptions )
          {
            // org/antlr/v4/parse/ANTLRParser.g:670:31: ^( ACTION elementOptions )
            {
              GrammarAST root_1 = (GrammarAST) adaptor.nil();
              root_1 = (GrammarAST) adaptor.becomeRoot(new ActionAST(stream_ACTION.nextToken()), root_1);
              adaptor.addChild(root_1, stream_elementOptions.nextTree());
              adaptor.addChild(root_0, root_1);
            }

          }


          retval.tree = root_0;

        }
        break;
        case 3:
          // org/antlr/v4/parse/ANTLRParser.g:671:6: SEMPRED
        {
          root_0 = (GrammarAST) adaptor.nil();


          SEMPRED166 = (Token) match(input, SEMPRED, FOLLOW_SEMPRED_in_actionElement3142);
          SEMPRED166_tree = new PredAST(SEMPRED166);
          adaptor.addChild(root_0, SEMPRED166_tree);

        }
        break;
        case 4:
          // org/antlr/v4/parse/ANTLRParser.g:672:6: SEMPRED elementOptions
        {
          SEMPRED167 = (Token) match(input, SEMPRED, FOLLOW_SEMPRED_in_actionElement3152);
          stream_SEMPRED.add(SEMPRED167);

          pushFollow(FOLLOW_elementOptions_in_actionElement3154);
          elementOptions168 = elementOptions();
          state._fsp--;

          stream_elementOptions.add(elementOptions168.getTree());

          // AST REWRITE
          // elements: elementOptions, SEMPRED
          // token labels:
          // rule labels: retval
          // token list labels:
          // rule list labels:
          // wildcard labels:
          retval.tree = root_0;
          RewriteRuleSubtreeStream stream_retval = new RewriteRuleSubtreeStream(adaptor, "rule retval", retval != null ? retval.getTree() : null);

          root_0 = (GrammarAST) adaptor.nil();
          // 672:29: -> ^( SEMPRED elementOptions )
          {
            // org/antlr/v4/parse/ANTLRParser.g:672:32: ^( SEMPRED elementOptions )
            {
              GrammarAST root_1 = (GrammarAST) adaptor.nil();
              root_1 = (GrammarAST) adaptor.becomeRoot(new PredAST(stream_SEMPRED.nextToken()), root_1);
              adaptor.addChild(root_1, stream_elementOptions.nextTree());
              adaptor.addChild(root_0, root_1);
            }

          }


          retval.tree = root_0;

        }
        break;

      }
      retval.stop = input.LT(-1);

      retval.tree = (GrammarAST) adaptor.rulePostProcessing(root_0);
      adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);


      GrammarAST options = (GrammarAST) retval.tree.getFirstChildWithType(ANTLRParser.ELEMENT_OPTIONS);
      if (options != null) {
        Grammar.setNodeOptions(retval.tree, options);
      }

    } catch (RecognitionException re) {
      reportError(re);
      recover(input, re);
      retval.tree = (GrammarAST) adaptor.errorNode(input, retval.start, input.LT(-1), re);
    } finally {
      // do for sure before leaving
    }
    return retval;
  }
  // $ANTLR end "actionElement"


  public static class labeledElement_return extends ParserRuleReturnScope {
    GrammarAST tree;

    @Override
    public GrammarAST getTree() {
      return tree;
    }
  }

  ;


  // $ANTLR start "labeledElement"
  // org/antlr/v4/parse/ANTLRParser.g:675:1: labeledElement : id (ass= ASSIGN |ass= PLUS_ASSIGN ) ( atom -> ^( $ass id atom ) | block -> ^( $ass id block ) ) ;
  public final ANTLRParser.labeledElement_return labeledElement() throws RecognitionException {
    ANTLRParser.labeledElement_return retval = new ANTLRParser.labeledElement_return();
    retval.start = input.LT(1);

    GrammarAST root_0 = null;

    Token ass = null;
    ParserRuleReturnScope id169 = null;
    ParserRuleReturnScope atom170 = null;
    ParserRuleReturnScope block171 = null;

    GrammarAST ass_tree = null;
    RewriteRuleTokenStream stream_PLUS_ASSIGN = new RewriteRuleTokenStream(adaptor, "token PLUS_ASSIGN");
    RewriteRuleTokenStream stream_ASSIGN = new RewriteRuleTokenStream(adaptor, "token ASSIGN");
    RewriteRuleSubtreeStream stream_block = new RewriteRuleSubtreeStream(adaptor, "rule block");
    RewriteRuleSubtreeStream stream_id = new RewriteRuleSubtreeStream(adaptor, "rule id");
    RewriteRuleSubtreeStream stream_atom = new RewriteRuleSubtreeStream(adaptor, "rule atom");

    try {
      // org/antlr/v4/parse/ANTLRParser.g:676:2: ( id (ass= ASSIGN |ass= PLUS_ASSIGN ) ( atom -> ^( $ass id atom ) | block -> ^( $ass id block ) ) )
      // org/antlr/v4/parse/ANTLRParser.g:676:4: id (ass= ASSIGN |ass= PLUS_ASSIGN ) ( atom -> ^( $ass id atom ) | block -> ^( $ass id block ) )
      {
        pushFollow(FOLLOW_id_in_labeledElement3176);
        id169 = id();
        state._fsp--;

        stream_id.add(id169.getTree());
        // org/antlr/v4/parse/ANTLRParser.g:676:7: (ass= ASSIGN |ass= PLUS_ASSIGN )
        int alt57 = 2;
        int LA57_0 = input.LA(1);
        if ((LA57_0 == ASSIGN)) {
          alt57 = 1;
        } else if ((LA57_0 == PLUS_ASSIGN)) {
          alt57 = 2;
        } else {
          NoViableAltException nvae =
            new NoViableAltException("", 57, 0, input);
          throw nvae;
        }

        switch (alt57) {
          case 1:
            // org/antlr/v4/parse/ANTLRParser.g:676:8: ass= ASSIGN
          {
            ass = (Token) match(input, ASSIGN, FOLLOW_ASSIGN_in_labeledElement3181);
            stream_ASSIGN.add(ass);

          }
          break;
          case 2:
            // org/antlr/v4/parse/ANTLRParser.g:676:19: ass= PLUS_ASSIGN
          {
            ass = (Token) match(input, PLUS_ASSIGN, FOLLOW_PLUS_ASSIGN_in_labeledElement3185);
            stream_PLUS_ASSIGN.add(ass);

          }
          break;

        }

        // org/antlr/v4/parse/ANTLRParser.g:677:3: ( atom -> ^( $ass id atom ) | block -> ^( $ass id block ) )
        int alt58 = 2;
        int LA58_0 = input.LA(1);
        if ((LA58_0 == DOT || LA58_0 == NOT || LA58_0 == RULE_REF || LA58_0 == STRING_LITERAL || LA58_0 == TOKEN_REF)) {
          alt58 = 1;
        } else if ((LA58_0 == LPAREN)) {
          alt58 = 2;
        } else {
          NoViableAltException nvae =
            new NoViableAltException("", 58, 0, input);
          throw nvae;
        }

        switch (alt58) {
          case 1:
            // org/antlr/v4/parse/ANTLRParser.g:677:5: atom
          {
            pushFollow(FOLLOW_atom_in_labeledElement3192);
            atom170 = atom();
            state._fsp--;

            stream_atom.add(atom170.getTree());

            // AST REWRITE
            // elements: ass, atom, id
            // token labels: ass
            // rule labels: retval
            // token list labels:
            // rule list labels:
            // wildcard labels:
            retval.tree = root_0;
            RewriteRuleTokenStream stream_ass = new RewriteRuleTokenStream(adaptor, "token ass", ass);
            RewriteRuleSubtreeStream stream_retval = new RewriteRuleSubtreeStream(adaptor, "rule retval", retval != null ? retval.getTree() : null);

            root_0 = (GrammarAST) adaptor.nil();
            // 677:15: -> ^( $ass id atom )
            {
              // org/antlr/v4/parse/ANTLRParser.g:677:18: ^( $ass id atom )
              {
                GrammarAST root_1 = (GrammarAST) adaptor.nil();
                root_1 = (GrammarAST) adaptor.becomeRoot(stream_ass.nextNode(), root_1);
                adaptor.addChild(root_1, stream_id.nextTree());
                adaptor.addChild(root_1, stream_atom.nextTree());
                adaptor.addChild(root_0, root_1);
              }

            }


            retval.tree = root_0;

          }
          break;
          case 2:
            // org/antlr/v4/parse/ANTLRParser.g:678:5: block
          {
            pushFollow(FOLLOW_block_in_labeledElement3214);
            block171 = block();
            state._fsp--;

            stream_block.add(block171.getTree());

            // AST REWRITE
            // elements: ass, block, id
            // token labels: ass
            // rule labels: retval
            // token list labels:
            // rule list labels:
            // wildcard labels:
            retval.tree = root_0;
            RewriteRuleTokenStream stream_ass = new RewriteRuleTokenStream(adaptor, "token ass", ass);
            RewriteRuleSubtreeStream stream_retval = new RewriteRuleSubtreeStream(adaptor, "rule retval", retval != null ? retval.getTree() : null);

            root_0 = (GrammarAST) adaptor.nil();
            // 678:16: -> ^( $ass id block )
            {
              // org/antlr/v4/parse/ANTLRParser.g:678:19: ^( $ass id block )
              {
                GrammarAST root_1 = (GrammarAST) adaptor.nil();
                root_1 = (GrammarAST) adaptor.becomeRoot(stream_ass.nextNode(), root_1);
                adaptor.addChild(root_1, stream_id.nextTree());
                adaptor.addChild(root_1, stream_block.nextTree());
                adaptor.addChild(root_0, root_1);
              }

            }


            retval.tree = root_0;

          }
          break;

        }

      }

      retval.stop = input.LT(-1);

      retval.tree = (GrammarAST) adaptor.rulePostProcessing(root_0);
      adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

    } catch (RecognitionException re) {
      reportError(re);
      recover(input, re);
      retval.tree = (GrammarAST) adaptor.errorNode(input, retval.start, input.LT(-1), re);
    } finally {
      // do for sure before leaving
    }
    return retval;
  }
  // $ANTLR end "labeledElement"


  public static class ebnf_return extends ParserRuleReturnScope {
    GrammarAST tree;

    @Override
    public GrammarAST getTree() {
      return tree;
    }
  }

  ;


  // $ANTLR start "ebnf"
  // org/antlr/v4/parse/ANTLRParser.g:684:1: ebnf : block ( blockSuffix -> ^( blockSuffix block ) | -> block ) ;
  public final ANTLRParser.ebnf_return ebnf() throws RecognitionException {
    ANTLRParser.ebnf_return retval = new ANTLRParser.ebnf_return();
    retval.start = input.LT(1);

    GrammarAST root_0 = null;

    ParserRuleReturnScope block172 = null;
    ParserRuleReturnScope blockSuffix173 = null;

    RewriteRuleSubtreeStream stream_blockSuffix = new RewriteRuleSubtreeStream(adaptor, "rule blockSuffix");
    RewriteRuleSubtreeStream stream_block = new RewriteRuleSubtreeStream(adaptor, "rule block");

    try {
      // org/antlr/v4/parse/ANTLRParser.g:685:5: ( block ( blockSuffix -> ^( blockSuffix block ) | -> block ) )
      // org/antlr/v4/parse/ANTLRParser.g:685:7: block ( blockSuffix -> ^( blockSuffix block ) | -> block )
      {
        pushFollow(FOLLOW_block_in_ebnf3250);
        block172 = block();
        state._fsp--;

        stream_block.add(block172.getTree());
        // org/antlr/v4/parse/ANTLRParser.g:688:7: ( blockSuffix -> ^( blockSuffix block ) | -> block )
        int alt59 = 2;
        int LA59_0 = input.LA(1);
        if ((LA59_0 == PLUS || LA59_0 == QUESTION || LA59_0 == STAR)) {
          alt59 = 1;
        } else if ((LA59_0 == EOF || LA59_0 == ACTION || LA59_0 == DOT || LA59_0 == LPAREN || LA59_0 == NOT || LA59_0 == OR || LA59_0 == POUND || (LA59_0 >= RPAREN && LA59_0 <= SEMPRED) || LA59_0 == STRING_LITERAL || LA59_0 == TOKEN_REF)) {
          alt59 = 2;
        } else {
          NoViableAltException nvae =
            new NoViableAltException("", 59, 0, input);
          throw nvae;
        }

        switch (alt59) {
          case 1:
            // org/antlr/v4/parse/ANTLRParser.g:688:9: blockSuffix
          {
            pushFollow(FOLLOW_blockSuffix_in_ebnf3274);
            blockSuffix173 = blockSuffix();
            state._fsp--;

            stream_blockSuffix.add(blockSuffix173.getTree());

            // AST REWRITE
            // elements: blockSuffix, block
            // token labels:
            // rule labels: retval
            // token list labels:
            // rule list labels:
            // wildcard labels:
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval = new RewriteRuleSubtreeStream(adaptor, "rule retval", retval != null ? retval.getTree() : null);

            root_0 = (GrammarAST) adaptor.nil();
            // 688:21: -> ^( blockSuffix block )
            {
              // org/antlr/v4/parse/ANTLRParser.g:688:24: ^( blockSuffix block )
              {
                GrammarAST root_1 = (GrammarAST) adaptor.nil();
                root_1 = (GrammarAST) adaptor.becomeRoot(stream_blockSuffix.nextNode(), root_1);
                adaptor.addChild(root_1, stream_block.nextTree());
                adaptor.addChild(root_0, root_1);
              }

            }


            retval.tree = root_0;

          }
          break;
          case 2:
            // org/antlr/v4/parse/ANTLRParser.g:689:12:
          {

            // AST REWRITE
            // elements: block
            // token labels:
            // rule labels: retval
            // token list labels:
            // rule list labels:
            // wildcard labels:
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval = new RewriteRuleSubtreeStream(adaptor, "rule retval", retval != null ? retval.getTree() : null);

            root_0 = (GrammarAST) adaptor.nil();
            // 689:12: -> block
            {
              adaptor.addChild(root_0, stream_block.nextTree());
            }


            retval.tree = root_0;

          }
          break;

        }

      }

      retval.stop = input.LT(-1);

      retval.tree = (GrammarAST) adaptor.rulePostProcessing(root_0);
      adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

    } catch (RecognitionException re) {
      reportError(re);
      recover(input, re);
      retval.tree = (GrammarAST) adaptor.errorNode(input, retval.start, input.LT(-1), re);
    } finally {
      // do for sure before leaving
    }
    return retval;
  }
  // $ANTLR end "ebnf"


  public static class blockSuffix_return extends ParserRuleReturnScope {
    GrammarAST tree;

    @Override
    public GrammarAST getTree() {
      return tree;
    }
  }

  ;


  // $ANTLR start "blockSuffix"
  // org/antlr/v4/parse/ANTLRParser.g:695:1: blockSuffix : ebnfSuffix ;
  public final ANTLRParser.blockSuffix_return blockSuffix() throws RecognitionException {
    ANTLRParser.blockSuffix_return retval = new ANTLRParser.blockSuffix_return();
    retval.start = input.LT(1);

    GrammarAST root_0 = null;

    ParserRuleReturnScope ebnfSuffix174 = null;


    try {
      // org/antlr/v4/parse/ANTLRParser.g:696:5: ( ebnfSuffix )
      // org/antlr/v4/parse/ANTLRParser.g:696:7: ebnfSuffix
      {
        root_0 = (GrammarAST) adaptor.nil();


        pushFollow(FOLLOW_ebnfSuffix_in_blockSuffix3324);
        ebnfSuffix174 = ebnfSuffix();
        state._fsp--;

        adaptor.addChild(root_0, ebnfSuffix174.getTree());

      }

      retval.stop = input.LT(-1);

      retval.tree = (GrammarAST) adaptor.rulePostProcessing(root_0);
      adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

    } catch (RecognitionException re) {
      reportError(re);
      recover(input, re);
      retval.tree = (GrammarAST) adaptor.errorNode(input, retval.start, input.LT(-1), re);
    } finally {
      // do for sure before leaving
    }
    return retval;
  }
  // $ANTLR end "blockSuffix"


  public static class ebnfSuffix_return extends ParserRuleReturnScope {
    GrammarAST tree;

    @Override
    public GrammarAST getTree() {
      return tree;
    }
  }

  ;


  // $ANTLR start "ebnfSuffix"
  // org/antlr/v4/parse/ANTLRParser.g:699:1: ebnfSuffix : ( QUESTION (nongreedy= QUESTION )? -> OPTIONAL[$start, $nongreedy] | STAR (nongreedy= QUESTION )? -> CLOSURE[$start, $nongreedy] | PLUS (nongreedy= QUESTION )? -> POSITIVE_CLOSURE[$start, $nongreedy] );
  public final ANTLRParser.ebnfSuffix_return ebnfSuffix() throws RecognitionException {
    ANTLRParser.ebnfSuffix_return retval = new ANTLRParser.ebnfSuffix_return();
    retval.start = input.LT(1);

    GrammarAST root_0 = null;

    Token nongreedy = null;
    Token QUESTION175 = null;
    Token STAR176 = null;
    Token PLUS177 = null;

    GrammarAST nongreedy_tree = null;
    GrammarAST QUESTION175_tree = null;
    GrammarAST STAR176_tree = null;
    GrammarAST PLUS177_tree = null;
    RewriteRuleTokenStream stream_STAR = new RewriteRuleTokenStream(adaptor, "token STAR");
    RewriteRuleTokenStream stream_QUESTION = new RewriteRuleTokenStream(adaptor, "token QUESTION");
    RewriteRuleTokenStream stream_PLUS = new RewriteRuleTokenStream(adaptor, "token PLUS");

    try {
      // org/antlr/v4/parse/ANTLRParser.g:700:2: ( QUESTION (nongreedy= QUESTION )? -> OPTIONAL[$start, $nongreedy] | STAR (nongreedy= QUESTION )? -> CLOSURE[$start, $nongreedy] | PLUS (nongreedy= QUESTION )? -> POSITIVE_CLOSURE[$start, $nongreedy] )
      int alt63 = 3;
      switch (input.LA(1)) {
        case QUESTION: {
          alt63 = 1;
        }
        break;
        case STAR: {
          alt63 = 2;
        }
        break;
        case PLUS: {
          alt63 = 3;
        }
        break;
        default:
          NoViableAltException nvae =
            new NoViableAltException("", 63, 0, input);
          throw nvae;
      }
      switch (alt63) {
        case 1:
          // org/antlr/v4/parse/ANTLRParser.g:700:4: QUESTION (nongreedy= QUESTION )?
        {
          QUESTION175 = (Token) match(input, QUESTION, FOLLOW_QUESTION_in_ebnfSuffix3339);
          stream_QUESTION.add(QUESTION175);

          // org/antlr/v4/parse/ANTLRParser.g:700:22: (nongreedy= QUESTION )?
          int alt60 = 2;
          int LA60_0 = input.LA(1);
          if ((LA60_0 == QUESTION)) {
            alt60 = 1;
          }
          switch (alt60) {
            case 1:
              // org/antlr/v4/parse/ANTLRParser.g:700:22: nongreedy= QUESTION
            {
              nongreedy = (Token) match(input, QUESTION, FOLLOW_QUESTION_in_ebnfSuffix3343);
              stream_QUESTION.add(nongreedy);

            }
            break;

          }


          // AST REWRITE
          // elements:
          // token labels:
          // rule labels: retval
          // token list labels:
          // rule list labels:
          // wildcard labels:
          retval.tree = root_0;
          RewriteRuleSubtreeStream stream_retval = new RewriteRuleSubtreeStream(adaptor, "rule retval", retval != null ? retval.getTree() : null);

          root_0 = (GrammarAST) adaptor.nil();
          // 700:33: -> OPTIONAL[$start, $nongreedy]
          {
            adaptor.addChild(root_0, new OptionalBlockAST(OPTIONAL, (retval.start), nongreedy));
          }


          retval.tree = root_0;

        }
        break;
        case 2:
          // org/antlr/v4/parse/ANTLRParser.g:701:6: STAR (nongreedy= QUESTION )?
        {
          STAR176 = (Token) match(input, STAR, FOLLOW_STAR_in_ebnfSuffix3359);
          stream_STAR.add(STAR176);

          // org/antlr/v4/parse/ANTLRParser.g:701:20: (nongreedy= QUESTION )?
          int alt61 = 2;
          int LA61_0 = input.LA(1);
          if ((LA61_0 == QUESTION)) {
            alt61 = 1;
          }
          switch (alt61) {
            case 1:
              // org/antlr/v4/parse/ANTLRParser.g:701:20: nongreedy= QUESTION
            {
              nongreedy = (Token) match(input, QUESTION, FOLLOW_QUESTION_in_ebnfSuffix3363);
              stream_QUESTION.add(nongreedy);

            }
            break;

          }


          // AST REWRITE
          // elements:
          // token labels:
          // rule labels: retval
          // token list labels:
          // rule list labels:
          // wildcard labels:
          retval.tree = root_0;
          RewriteRuleSubtreeStream stream_retval = new RewriteRuleSubtreeStream(adaptor, "rule retval", retval != null ? retval.getTree() : null);

          root_0 = (GrammarAST) adaptor.nil();
          // 701:32: -> CLOSURE[$start, $nongreedy]
          {
            adaptor.addChild(root_0, new StarBlockAST(CLOSURE, (retval.start), nongreedy));
          }


          retval.tree = root_0;

        }
        break;
        case 3:
          // org/antlr/v4/parse/ANTLRParser.g:702:7: PLUS (nongreedy= QUESTION )?
        {
          PLUS177 = (Token) match(input, PLUS, FOLLOW_PLUS_in_ebnfSuffix3381);
          stream_PLUS.add(PLUS177);

          // org/antlr/v4/parse/ANTLRParser.g:702:21: (nongreedy= QUESTION )?
          int alt62 = 2;
          int LA62_0 = input.LA(1);
          if ((LA62_0 == QUESTION)) {
            alt62 = 1;
          }
          switch (alt62) {
            case 1:
              // org/antlr/v4/parse/ANTLRParser.g:702:21: nongreedy= QUESTION
            {
              nongreedy = (Token) match(input, QUESTION, FOLLOW_QUESTION_in_ebnfSuffix3385);
              stream_QUESTION.add(nongreedy);

            }
            break;

          }


          // AST REWRITE
          // elements:
          // token labels:
          // rule labels: retval
          // token list labels:
          // rule list labels:
          // wildcard labels:
          retval.tree = root_0;
          RewriteRuleSubtreeStream stream_retval = new RewriteRuleSubtreeStream(adaptor, "rule retval", retval != null ? retval.getTree() : null);

          root_0 = (GrammarAST) adaptor.nil();
          // 702:33: -> POSITIVE_CLOSURE[$start, $nongreedy]
          {
            adaptor.addChild(root_0, new PlusBlockAST(POSITIVE_CLOSURE, (retval.start), nongreedy));
          }


          retval.tree = root_0;

        }
        break;

      }
      retval.stop = input.LT(-1);

      retval.tree = (GrammarAST) adaptor.rulePostProcessing(root_0);
      adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

    } catch (RecognitionException re) {
      reportError(re);
      recover(input, re);
      retval.tree = (GrammarAST) adaptor.errorNode(input, retval.start, input.LT(-1), re);
    } finally {
      // do for sure before leaving
    }
    return retval;
  }
  // $ANTLR end "ebnfSuffix"


  public static class lexerAtom_return extends ParserRuleReturnScope {
    GrammarAST tree;

    @Override
    public GrammarAST getTree() {
      return tree;
    }
  }

  ;


  // $ANTLR start "lexerAtom"
  // org/antlr/v4/parse/ANTLRParser.g:705:1: lexerAtom : ( range | terminal | RULE_REF | notSet | wildcard | LEXER_CHAR_SET );
  public final ANTLRParser.lexerAtom_return lexerAtom() throws RecognitionException {
    ANTLRParser.lexerAtom_return retval = new ANTLRParser.lexerAtom_return();
    retval.start = input.LT(1);

    GrammarAST root_0 = null;

    Token RULE_REF180 = null;
    Token LEXER_CHAR_SET183 = null;
    ParserRuleReturnScope range178 = null;
    ParserRuleReturnScope terminal179 = null;
    ParserRuleReturnScope notSet181 = null;
    ParserRuleReturnScope wildcard182 = null;

    GrammarAST RULE_REF180_tree = null;
    GrammarAST LEXER_CHAR_SET183_tree = null;

    try {
      // org/antlr/v4/parse/ANTLRParser.g:706:2: ( range | terminal | RULE_REF | notSet | wildcard | LEXER_CHAR_SET )
      int alt64 = 6;
      switch (input.LA(1)) {
        case STRING_LITERAL: {
          int LA64_1 = input.LA(2);
          if ((LA64_1 == RANGE)) {
            alt64 = 1;
          } else if ((LA64_1 == ACTION || LA64_1 == DOT || LA64_1 == LEXER_CHAR_SET || (LA64_1 >= LPAREN && LA64_1 <= LT) || LA64_1 == NOT || LA64_1 == OR || LA64_1 == PLUS || LA64_1 == QUESTION || LA64_1 == RARROW || (LA64_1 >= RPAREN && LA64_1 <= SEMPRED) || (LA64_1 >= STAR && LA64_1 <= STRING_LITERAL) || LA64_1 == TOKEN_REF)) {
            alt64 = 2;
          } else {
            int nvaeMark = input.mark();
            try {
              input.consume();
              NoViableAltException nvae =
                new NoViableAltException("", 64, 1, input);
              throw nvae;
            } finally {
              input.rewind(nvaeMark);
            }
          }

        }
        break;
        case TOKEN_REF: {
          alt64 = 2;
        }
        break;
        case RULE_REF: {
          alt64 = 3;
        }
        break;
        case NOT: {
          alt64 = 4;
        }
        break;
        case DOT: {
          alt64 = 5;
        }
        break;
        case LEXER_CHAR_SET: {
          alt64 = 6;
        }
        break;
        default:
          NoViableAltException nvae =
            new NoViableAltException("", 64, 0, input);
          throw nvae;
      }
      switch (alt64) {
        case 1:
          // org/antlr/v4/parse/ANTLRParser.g:706:4: range
        {
          root_0 = (GrammarAST) adaptor.nil();


          pushFollow(FOLLOW_range_in_lexerAtom3406);
          range178 = range();
          state._fsp--;

          adaptor.addChild(root_0, range178.getTree());

        }
        break;
        case 2:
          // org/antlr/v4/parse/ANTLRParser.g:707:4: terminal
        {
          root_0 = (GrammarAST) adaptor.nil();


          pushFollow(FOLLOW_terminal_in_lexerAtom3411);
          terminal179 = terminal();
          state._fsp--;

          adaptor.addChild(root_0, terminal179.getTree());

        }
        break;
        case 3:
          // org/antlr/v4/parse/ANTLRParser.g:708:9: RULE_REF
        {
          root_0 = (GrammarAST) adaptor.nil();


          RULE_REF180 = (Token) match(input, RULE_REF, FOLLOW_RULE_REF_in_lexerAtom3421);
          RULE_REF180_tree = new RuleRefAST(RULE_REF180);
          adaptor.addChild(root_0, RULE_REF180_tree);

        }
        break;
        case 4:
          // org/antlr/v4/parse/ANTLRParser.g:709:7: notSet
        {
          root_0 = (GrammarAST) adaptor.nil();


          pushFollow(FOLLOW_notSet_in_lexerAtom3432);
          notSet181 = notSet();
          state._fsp--;

          adaptor.addChild(root_0, notSet181.getTree());

        }
        break;
        case 5:
          // org/antlr/v4/parse/ANTLRParser.g:710:7: wildcard
        {
          root_0 = (GrammarAST) adaptor.nil();


          pushFollow(FOLLOW_wildcard_in_lexerAtom3440);
          wildcard182 = wildcard();
          state._fsp--;

          adaptor.addChild(root_0, wildcard182.getTree());

        }
        break;
        case 6:
          // org/antlr/v4/parse/ANTLRParser.g:711:7: LEXER_CHAR_SET
        {
          root_0 = (GrammarAST) adaptor.nil();


          LEXER_CHAR_SET183 = (Token) match(input, LEXER_CHAR_SET, FOLLOW_LEXER_CHAR_SET_in_lexerAtom3448);
          LEXER_CHAR_SET183_tree = (GrammarAST) adaptor.create(LEXER_CHAR_SET183);
          adaptor.addChild(root_0, LEXER_CHAR_SET183_tree);

        }
        break;

      }
      retval.stop = input.LT(-1);

      retval.tree = (GrammarAST) adaptor.rulePostProcessing(root_0);
      adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

    } catch (RecognitionException re) {
      reportError(re);
      recover(input, re);
      retval.tree = (GrammarAST) adaptor.errorNode(input, retval.start, input.LT(-1), re);
    } finally {
      // do for sure before leaving
    }
    return retval;
  }
  // $ANTLR end "lexerAtom"


  public static class atom_return extends ParserRuleReturnScope {
    GrammarAST tree;

    @Override
    public GrammarAST getTree() {
      return tree;
    }
  }

  ;


  // $ANTLR start "atom"
  // org/antlr/v4/parse/ANTLRParser.g:714:1: atom : ( range | terminal | ruleref | notSet | wildcard );
  public final ANTLRParser.atom_return atom() throws RecognitionException {
    ANTLRParser.atom_return retval = new ANTLRParser.atom_return();
    retval.start = input.LT(1);

    GrammarAST root_0 = null;

    ParserRuleReturnScope range184 = null;
    ParserRuleReturnScope terminal185 = null;
    ParserRuleReturnScope ruleref186 = null;
    ParserRuleReturnScope notSet187 = null;
    ParserRuleReturnScope wildcard188 = null;


    try {
      // org/antlr/v4/parse/ANTLRParser.g:715:2: ( range | terminal | ruleref | notSet | wildcard )
      int alt65 = 5;
      switch (input.LA(1)) {
        case STRING_LITERAL: {
          int LA65_1 = input.LA(2);
          if ((LA65_1 == RANGE)) {
            alt65 = 1;
          } else if ((LA65_1 == EOF || LA65_1 == ACTION || LA65_1 == DOT || (LA65_1 >= LPAREN && LA65_1 <= LT) || LA65_1 == NOT || LA65_1 == OR || LA65_1 == PLUS || LA65_1 == POUND || LA65_1 == QUESTION || (LA65_1 >= RPAREN && LA65_1 <= SEMPRED) || (LA65_1 >= STAR && LA65_1 <= STRING_LITERAL) || LA65_1 == TOKEN_REF)) {
            alt65 = 2;
          } else {
            int nvaeMark = input.mark();
            try {
              input.consume();
              NoViableAltException nvae =
                new NoViableAltException("", 65, 1, input);
              throw nvae;
            } finally {
              input.rewind(nvaeMark);
            }
          }

        }
        break;
        case TOKEN_REF: {
          alt65 = 2;
        }
        break;
        case RULE_REF: {
          alt65 = 3;
        }
        break;
        case NOT: {
          alt65 = 4;
        }
        break;
        case DOT: {
          alt65 = 5;
        }
        break;
        default:
          NoViableAltException nvae =
            new NoViableAltException("", 65, 0, input);
          throw nvae;
      }
      switch (alt65) {
        case 1:
          // org/antlr/v4/parse/ANTLRParser.g:729:9: range
        {
          root_0 = (GrammarAST) adaptor.nil();


          pushFollow(FOLLOW_range_in_atom3493);
          range184 = range();
          state._fsp--;

          adaptor.addChild(root_0, range184.getTree());

        }
        break;
        case 2:
          // org/antlr/v4/parse/ANTLRParser.g:730:4: terminal
        {
          root_0 = (GrammarAST) adaptor.nil();


          pushFollow(FOLLOW_terminal_in_atom3500);
          terminal185 = terminal();
          state._fsp--;

          adaptor.addChild(root_0, terminal185.getTree());

        }
        break;
        case 3:
          // org/antlr/v4/parse/ANTLRParser.g:731:9: ruleref
        {
          root_0 = (GrammarAST) adaptor.nil();


          pushFollow(FOLLOW_ruleref_in_atom3510);
          ruleref186 = ruleref();
          state._fsp--;

          adaptor.addChild(root_0, ruleref186.getTree());

        }
        break;
        case 4:
          // org/antlr/v4/parse/ANTLRParser.g:732:7: notSet
        {
          root_0 = (GrammarAST) adaptor.nil();


          pushFollow(FOLLOW_notSet_in_atom3518);
          notSet187 = notSet();
          state._fsp--;

          adaptor.addChild(root_0, notSet187.getTree());

        }
        break;
        case 5:
          // org/antlr/v4/parse/ANTLRParser.g:733:7: wildcard
        {
          root_0 = (GrammarAST) adaptor.nil();


          pushFollow(FOLLOW_wildcard_in_atom3526);
          wildcard188 = wildcard();
          state._fsp--;

          adaptor.addChild(root_0, wildcard188.getTree());

        }
        break;

      }
      retval.stop = input.LT(-1);

      retval.tree = (GrammarAST) adaptor.rulePostProcessing(root_0);
      adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

    } catch (RecognitionException re) {
      throw re;
    } finally {
      // do for sure before leaving
    }
    return retval;
  }
  // $ANTLR end "atom"


  public static class wildcard_return extends ParserRuleReturnScope {
    GrammarAST tree;

    @Override
    public GrammarAST getTree() {
      return tree;
    }
  }

  ;


  // $ANTLR start "wildcard"
  // org/antlr/v4/parse/ANTLRParser.g:737:1: wildcard : DOT ( elementOptions )? -> ^( WILDCARD[$DOT] ( elementOptions )? ) ;
  public final ANTLRParser.wildcard_return wildcard() throws RecognitionException {
    ANTLRParser.wildcard_return retval = new ANTLRParser.wildcard_return();
    retval.start = input.LT(1);

    GrammarAST root_0 = null;

    Token DOT189 = null;
    ParserRuleReturnScope elementOptions190 = null;

    GrammarAST DOT189_tree = null;
    RewriteRuleTokenStream stream_DOT = new RewriteRuleTokenStream(adaptor, "token DOT");
    RewriteRuleSubtreeStream stream_elementOptions = new RewriteRuleSubtreeStream(adaptor, "rule elementOptions");

    try {
      // org/antlr/v4/parse/ANTLRParser.g:744:2: ( DOT ( elementOptions )? -> ^( WILDCARD[$DOT] ( elementOptions )? ) )
      // org/antlr/v4/parse/ANTLRParser.g:749:6: DOT ( elementOptions )?
      {
        DOT189 = (Token) match(input, DOT, FOLLOW_DOT_in_wildcard3574);
        stream_DOT.add(DOT189);

        // org/antlr/v4/parse/ANTLRParser.g:749:10: ( elementOptions )?
        int alt66 = 2;
        int LA66_0 = input.LA(1);
        if ((LA66_0 == LT)) {
          alt66 = 1;
        }
        switch (alt66) {
          case 1:
            // org/antlr/v4/parse/ANTLRParser.g:749:10: elementOptions
          {
            pushFollow(FOLLOW_elementOptions_in_wildcard3576);
            elementOptions190 = elementOptions();
            state._fsp--;

            stream_elementOptions.add(elementOptions190.getTree());
          }
          break;

        }


        // AST REWRITE
        // elements: elementOptions
        // token labels:
        // rule labels: retval
        // token list labels:
        // rule list labels:
        // wildcard labels:
        retval.tree = root_0;
        RewriteRuleSubtreeStream stream_retval = new RewriteRuleSubtreeStream(adaptor, "rule retval", retval != null ? retval.getTree() : null);

        root_0 = (GrammarAST) adaptor.nil();
        // 750:6: -> ^( WILDCARD[$DOT] ( elementOptions )? )
        {
          // org/antlr/v4/parse/ANTLRParser.g:750:9: ^( WILDCARD[$DOT] ( elementOptions )? )
          {
            GrammarAST root_1 = (GrammarAST) adaptor.nil();
            root_1 = (GrammarAST) adaptor.becomeRoot(new TerminalAST(WILDCARD, DOT189), root_1);
            // org/antlr/v4/parse/ANTLRParser.g:750:39: ( elementOptions )?
            if (stream_elementOptions.hasNext()) {
              adaptor.addChild(root_1, stream_elementOptions.nextTree());
            }
            stream_elementOptions.reset();

            adaptor.addChild(root_0, root_1);
          }

        }


        retval.tree = root_0;

      }

      retval.stop = input.LT(-1);

      retval.tree = (GrammarAST) adaptor.rulePostProcessing(root_0);
      adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);


      GrammarAST options = (GrammarAST) retval.tree.getFirstChildWithType(ANTLRParser.ELEMENT_OPTIONS);
      if (options != null) {
        Grammar.setNodeOptions(retval.tree, options);
      }

    } catch (RecognitionException re) {
      reportError(re);
      recover(input, re);
      retval.tree = (GrammarAST) adaptor.errorNode(input, retval.start, input.LT(-1), re);
    } finally {
      // do for sure before leaving
    }
    return retval;
  }
  // $ANTLR end "wildcard"


  public static class notSet_return extends ParserRuleReturnScope {
    GrammarAST tree;

    @Override
    public GrammarAST getTree() {
      return tree;
    }
  }

  ;


  // $ANTLR start "notSet"
  // org/antlr/v4/parse/ANTLRParser.g:758:1: notSet : ( NOT setElement -> ^( NOT[$NOT] ^( SET[$setElement.start,\"SET\"] setElement ) ) | NOT blockSet -> ^( NOT[$NOT] blockSet ) );
  public final ANTLRParser.notSet_return notSet() throws RecognitionException {
    ANTLRParser.notSet_return retval = new ANTLRParser.notSet_return();
    retval.start = input.LT(1);

    GrammarAST root_0 = null;

    Token NOT191 = null;
    Token NOT193 = null;
    ParserRuleReturnScope setElement192 = null;
    ParserRuleReturnScope blockSet194 = null;

    GrammarAST NOT191_tree = null;
    GrammarAST NOT193_tree = null;
    RewriteRuleTokenStream stream_NOT = new RewriteRuleTokenStream(adaptor, "token NOT");
    RewriteRuleSubtreeStream stream_blockSet = new RewriteRuleSubtreeStream(adaptor, "rule blockSet");
    RewriteRuleSubtreeStream stream_setElement = new RewriteRuleSubtreeStream(adaptor, "rule setElement");

    try {
      // org/antlr/v4/parse/ANTLRParser.g:759:5: ( NOT setElement -> ^( NOT[$NOT] ^( SET[$setElement.start,\"SET\"] setElement ) ) | NOT blockSet -> ^( NOT[$NOT] blockSet ) )
      int alt67 = 2;
      int LA67_0 = input.LA(1);
      if ((LA67_0 == NOT)) {
        int LA67_1 = input.LA(2);
        if ((LA67_1 == LEXER_CHAR_SET || LA67_1 == STRING_LITERAL || LA67_1 == TOKEN_REF)) {
          alt67 = 1;
        } else if ((LA67_1 == LPAREN)) {
          alt67 = 2;
        } else {
          int nvaeMark = input.mark();
          try {
            input.consume();
            NoViableAltException nvae =
              new NoViableAltException("", 67, 1, input);
            throw nvae;
          } finally {
            input.rewind(nvaeMark);
          }
        }

      } else {
        NoViableAltException nvae =
          new NoViableAltException("", 67, 0, input);
        throw nvae;
      }

      switch (alt67) {
        case 1:
          // org/antlr/v4/parse/ANTLRParser.g:759:7: NOT setElement
        {
          NOT191 = (Token) match(input, NOT, FOLLOW_NOT_in_notSet3614);
          stream_NOT.add(NOT191);

          pushFollow(FOLLOW_setElement_in_notSet3616);
          setElement192 = setElement();
          state._fsp--;

          stream_setElement.add(setElement192.getTree());

          // AST REWRITE
          // elements: setElement, NOT
          // token labels:
          // rule labels: retval
          // token list labels:
          // rule list labels:
          // wildcard labels:
          retval.tree = root_0;
          RewriteRuleSubtreeStream stream_retval = new RewriteRuleSubtreeStream(adaptor, "rule retval", retval != null ? retval.getTree() : null);

          root_0 = (GrammarAST) adaptor.nil();
          // 759:22: -> ^( NOT[$NOT] ^( SET[$setElement.start,\"SET\"] setElement ) )
          {
            // org/antlr/v4/parse/ANTLRParser.g:759:25: ^( NOT[$NOT] ^( SET[$setElement.start,\"SET\"] setElement ) )
            {
              GrammarAST root_1 = (GrammarAST) adaptor.nil();
              root_1 = (GrammarAST) adaptor.becomeRoot(new NotAST(NOT, NOT191), root_1);
              // org/antlr/v4/parse/ANTLRParser.g:759:45: ^( SET[$setElement.start,\"SET\"] setElement )
              {
                GrammarAST root_2 = (GrammarAST) adaptor.nil();
                root_2 = (GrammarAST) adaptor.becomeRoot(new SetAST(SET, (setElement192 != null ? (setElement192.start) : null), "SET"), root_2);
                adaptor.addChild(root_2, stream_setElement.nextTree());
                adaptor.addChild(root_1, root_2);
              }

              adaptor.addChild(root_0, root_1);
            }

          }


          retval.tree = root_0;

        }
        break;
        case 2:
          // org/antlr/v4/parse/ANTLRParser.g:760:7: NOT blockSet
        {
          NOT193 = (Token) match(input, NOT, FOLLOW_NOT_in_notSet3644);
          stream_NOT.add(NOT193);

          pushFollow(FOLLOW_blockSet_in_notSet3646);
          blockSet194 = blockSet();
          state._fsp--;

          stream_blockSet.add(blockSet194.getTree());

          // AST REWRITE
          // elements: blockSet, NOT
          // token labels:
          // rule labels: retval
          // token list labels:
          // rule list labels:
          // wildcard labels:
          retval.tree = root_0;
          RewriteRuleSubtreeStream stream_retval = new RewriteRuleSubtreeStream(adaptor, "rule retval", retval != null ? retval.getTree() : null);

          root_0 = (GrammarAST) adaptor.nil();
          // 760:21: -> ^( NOT[$NOT] blockSet )
          {
            // org/antlr/v4/parse/ANTLRParser.g:760:24: ^( NOT[$NOT] blockSet )
            {
              GrammarAST root_1 = (GrammarAST) adaptor.nil();
              root_1 = (GrammarAST) adaptor.becomeRoot(new NotAST(NOT, NOT193), root_1);
              adaptor.addChild(root_1, stream_blockSet.nextTree());
              adaptor.addChild(root_0, root_1);
            }

          }


          retval.tree = root_0;

        }
        break;

      }
      retval.stop = input.LT(-1);

      retval.tree = (GrammarAST) adaptor.rulePostProcessing(root_0);
      adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

    } catch (RecognitionException re) {
      reportError(re);
      recover(input, re);
      retval.tree = (GrammarAST) adaptor.errorNode(input, retval.start, input.LT(-1), re);
    } finally {
      // do for sure before leaving
    }
    return retval;
  }
  // $ANTLR end "notSet"


  public static class blockSet_return extends ParserRuleReturnScope {
    GrammarAST tree;

    @Override
    public GrammarAST getTree() {
      return tree;
    }
  }

  ;


  // $ANTLR start "blockSet"
  // org/antlr/v4/parse/ANTLRParser.g:763:1: blockSet : LPAREN setElement ( OR setElement )* RPAREN -> ^( SET[$LPAREN,\"SET\"] ( setElement )+ ) ;
  public final ANTLRParser.blockSet_return blockSet() throws RecognitionException {
    ANTLRParser.blockSet_return retval = new ANTLRParser.blockSet_return();
    retval.start = input.LT(1);

    GrammarAST root_0 = null;

    Token LPAREN195 = null;
    Token OR197 = null;
    Token RPAREN199 = null;
    ParserRuleReturnScope setElement196 = null;
    ParserRuleReturnScope setElement198 = null;

    GrammarAST LPAREN195_tree = null;
    GrammarAST OR197_tree = null;
    GrammarAST RPAREN199_tree = null;
    RewriteRuleTokenStream stream_OR = new RewriteRuleTokenStream(adaptor, "token OR");
    RewriteRuleTokenStream stream_LPAREN = new RewriteRuleTokenStream(adaptor, "token LPAREN");
    RewriteRuleTokenStream stream_RPAREN = new RewriteRuleTokenStream(adaptor, "token RPAREN");
    RewriteRuleSubtreeStream stream_setElement = new RewriteRuleSubtreeStream(adaptor, "rule setElement");


    Token t;
    boolean ebnf = false;

    try {
      // org/antlr/v4/parse/ANTLRParser.g:768:5: ( LPAREN setElement ( OR setElement )* RPAREN -> ^( SET[$LPAREN,\"SET\"] ( setElement )+ ) )
      // org/antlr/v4/parse/ANTLRParser.g:768:7: LPAREN setElement ( OR setElement )* RPAREN
      {
        LPAREN195 = (Token) match(input, LPAREN, FOLLOW_LPAREN_in_blockSet3681);
        stream_LPAREN.add(LPAREN195);

        pushFollow(FOLLOW_setElement_in_blockSet3683);
        setElement196 = setElement();
        state._fsp--;

        stream_setElement.add(setElement196.getTree());
        // org/antlr/v4/parse/ANTLRParser.g:768:25: ( OR setElement )*
        loop68:
        while (true) {
          int alt68 = 2;
          int LA68_0 = input.LA(1);
          if ((LA68_0 == OR)) {
            alt68 = 1;
          }

          switch (alt68) {
            case 1:
              // org/antlr/v4/parse/ANTLRParser.g:768:26: OR setElement
            {
              OR197 = (Token) match(input, OR, FOLLOW_OR_in_blockSet3686);
              stream_OR.add(OR197);

              pushFollow(FOLLOW_setElement_in_blockSet3688);
              setElement198 = setElement();
              state._fsp--;

              stream_setElement.add(setElement198.getTree());
            }
            break;

            default:
              break loop68;
          }
        }

        RPAREN199 = (Token) match(input, RPAREN, FOLLOW_RPAREN_in_blockSet3692);
        stream_RPAREN.add(RPAREN199);


        // AST REWRITE
        // elements: setElement
        // token labels:
        // rule labels: retval
        // token list labels:
        // rule list labels:
        // wildcard labels:
        retval.tree = root_0;
        RewriteRuleSubtreeStream stream_retval = new RewriteRuleSubtreeStream(adaptor, "rule retval", retval != null ? retval.getTree() : null);

        root_0 = (GrammarAST) adaptor.nil();
        // 769:3: -> ^( SET[$LPAREN,\"SET\"] ( setElement )+ )
        {
          // org/antlr/v4/parse/ANTLRParser.g:769:6: ^( SET[$LPAREN,\"SET\"] ( setElement )+ )
          {
            GrammarAST root_1 = (GrammarAST) adaptor.nil();
            root_1 = (GrammarAST) adaptor.becomeRoot(new SetAST(SET, LPAREN195, "SET"), root_1);
            if (!(stream_setElement.hasNext())) {
              throw new RewriteEarlyExitException();
            }
            while (stream_setElement.hasNext()) {
              adaptor.addChild(root_1, stream_setElement.nextTree());
            }
            stream_setElement.reset();

            adaptor.addChild(root_0, root_1);
          }

        }


        retval.tree = root_0;

      }

      retval.stop = input.LT(-1);

      retval.tree = (GrammarAST) adaptor.rulePostProcessing(root_0);
      adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

    } catch (RecognitionException re) {
      reportError(re);
      recover(input, re);
      retval.tree = (GrammarAST) adaptor.errorNode(input, retval.start, input.LT(-1), re);
    } finally {
      // do for sure before leaving
    }
    return retval;
  }
  // $ANTLR end "blockSet"


  public static class setElement_return extends ParserRuleReturnScope {
    GrammarAST tree;

    @Override
    public GrammarAST getTree() {
      return tree;
    }
  }

  ;


  // $ANTLR start "setElement"
  // org/antlr/v4/parse/ANTLRParser.g:772:1: setElement : ( TOKEN_REF ^ ( elementOptions )? | STRING_LITERAL ^ ( elementOptions )? | range | LEXER_CHAR_SET );
  public final ANTLRParser.setElement_return setElement() throws RecognitionException {
    ANTLRParser.setElement_return retval = new ANTLRParser.setElement_return();
    retval.start = input.LT(1);

    GrammarAST root_0 = null;

    Token TOKEN_REF200 = null;
    Token STRING_LITERAL202 = null;
    Token LEXER_CHAR_SET205 = null;
    ParserRuleReturnScope elementOptions201 = null;
    ParserRuleReturnScope elementOptions203 = null;
    ParserRuleReturnScope range204 = null;

    GrammarAST TOKEN_REF200_tree = null;
    GrammarAST STRING_LITERAL202_tree = null;
    GrammarAST LEXER_CHAR_SET205_tree = null;

    try {
      // org/antlr/v4/parse/ANTLRParser.g:773:2: ( TOKEN_REF ^ ( elementOptions )? | STRING_LITERAL ^ ( elementOptions )? | range | LEXER_CHAR_SET )
      int alt71 = 4;
      switch (input.LA(1)) {
        case TOKEN_REF: {
          alt71 = 1;
        }
        break;
        case STRING_LITERAL: {
          int LA71_2 = input.LA(2);
          if ((LA71_2 == RANGE)) {
            alt71 = 3;
          } else if ((LA71_2 == EOF || LA71_2 == ACTION || LA71_2 == DOT || LA71_2 == LEXER_CHAR_SET || (LA71_2 >= LPAREN && LA71_2 <= LT) || LA71_2 == NOT || LA71_2 == OR || LA71_2 == PLUS || LA71_2 == POUND || LA71_2 == QUESTION || LA71_2 == RARROW || (LA71_2 >= RPAREN && LA71_2 <= SEMPRED) || (LA71_2 >= STAR && LA71_2 <= STRING_LITERAL) || LA71_2 == TOKEN_REF)) {
            alt71 = 2;
          } else {
            int nvaeMark = input.mark();
            try {
              input.consume();
              NoViableAltException nvae =
                new NoViableAltException("", 71, 2, input);
              throw nvae;
            } finally {
              input.rewind(nvaeMark);
            }
          }

        }
        break;
        case LEXER_CHAR_SET: {
          alt71 = 4;
        }
        break;
        default:
          NoViableAltException nvae =
            new NoViableAltException("", 71, 0, input);
          throw nvae;
      }
      switch (alt71) {
        case 1:
          // org/antlr/v4/parse/ANTLRParser.g:773:4: TOKEN_REF ^ ( elementOptions )?
        {
          root_0 = (GrammarAST) adaptor.nil();


          TOKEN_REF200 = (Token) match(input, TOKEN_REF, FOLLOW_TOKEN_REF_in_setElement3722);
          TOKEN_REF200_tree = new TerminalAST(TOKEN_REF200);
          root_0 = (GrammarAST) adaptor.becomeRoot(TOKEN_REF200_tree, root_0);

          // org/antlr/v4/parse/ANTLRParser.g:773:28: ( elementOptions )?
          int alt69 = 2;
          int LA69_0 = input.LA(1);
          if ((LA69_0 == LT)) {
            alt69 = 1;
          }
          switch (alt69) {
            case 1:
              // org/antlr/v4/parse/ANTLRParser.g:773:28: elementOptions
            {
              pushFollow(FOLLOW_elementOptions_in_setElement3728);
              elementOptions201 = elementOptions();
              state._fsp--;

              adaptor.addChild(root_0, elementOptions201.getTree());

            }
            break;

          }

        }
        break;
        case 2:
          // org/antlr/v4/parse/ANTLRParser.g:774:4: STRING_LITERAL ^ ( elementOptions )?
        {
          root_0 = (GrammarAST) adaptor.nil();


          STRING_LITERAL202 = (Token) match(input, STRING_LITERAL, FOLLOW_STRING_LITERAL_in_setElement3734);
          STRING_LITERAL202_tree = new TerminalAST(STRING_LITERAL202);
          root_0 = (GrammarAST) adaptor.becomeRoot(STRING_LITERAL202_tree, root_0);

          // org/antlr/v4/parse/ANTLRParser.g:774:33: ( elementOptions )?
          int alt70 = 2;
          int LA70_0 = input.LA(1);
          if ((LA70_0 == LT)) {
            alt70 = 1;
          }
          switch (alt70) {
            case 1:
              // org/antlr/v4/parse/ANTLRParser.g:774:33: elementOptions
            {
              pushFollow(FOLLOW_elementOptions_in_setElement3740);
              elementOptions203 = elementOptions();
              state._fsp--;

              adaptor.addChild(root_0, elementOptions203.getTree());

            }
            break;

          }

        }
        break;
        case 3:
          // org/antlr/v4/parse/ANTLRParser.g:775:4: range
        {
          root_0 = (GrammarAST) adaptor.nil();


          pushFollow(FOLLOW_range_in_setElement3746);
          range204 = range();
          state._fsp--;

          adaptor.addChild(root_0, range204.getTree());

        }
        break;
        case 4:
          // org/antlr/v4/parse/ANTLRParser.g:776:9: LEXER_CHAR_SET
        {
          root_0 = (GrammarAST) adaptor.nil();


          LEXER_CHAR_SET205 = (Token) match(input, LEXER_CHAR_SET, FOLLOW_LEXER_CHAR_SET_in_setElement3756);
          LEXER_CHAR_SET205_tree = (GrammarAST) adaptor.create(LEXER_CHAR_SET205);
          adaptor.addChild(root_0, LEXER_CHAR_SET205_tree);

        }
        break;

      }
      retval.stop = input.LT(-1);

      retval.tree = (GrammarAST) adaptor.rulePostProcessing(root_0);
      adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

    } catch (RecognitionException re) {
      reportError(re);
      recover(input, re);
      retval.tree = (GrammarAST) adaptor.errorNode(input, retval.start, input.LT(-1), re);
    } finally {
      // do for sure before leaving
    }
    return retval;
  }
  // $ANTLR end "setElement"


  public static class block_return extends ParserRuleReturnScope {
    GrammarAST tree;

    @Override
    public GrammarAST getTree() {
      return tree;
    }
  }

  ;


  // $ANTLR start "block"
  // org/antlr/v4/parse/ANTLRParser.g:786:1: block : LPAREN ( ( optionsSpec )? (ra+= ruleAction )* COLON )? altList RPAREN -> ^( BLOCK[$LPAREN,\"BLOCK\"] ( optionsSpec )? ( $ra)* altList ) ;
  public final ANTLRParser.block_return block() throws RecognitionException {
    ANTLRParser.block_return retval = new ANTLRParser.block_return();
    retval.start = input.LT(1);

    GrammarAST root_0 = null;

    Token LPAREN206 = null;
    Token COLON208 = null;
    Token RPAREN210 = null;
    List<Object> list_ra = null;
    ParserRuleReturnScope optionsSpec207 = null;
    ParserRuleReturnScope altList209 = null;
    RuleReturnScope ra = null;
    GrammarAST LPAREN206_tree = null;
    GrammarAST COLON208_tree = null;
    GrammarAST RPAREN210_tree = null;
    RewriteRuleTokenStream stream_LPAREN = new RewriteRuleTokenStream(adaptor, "token LPAREN");
    RewriteRuleTokenStream stream_COLON = new RewriteRuleTokenStream(adaptor, "token COLON");
    RewriteRuleTokenStream stream_RPAREN = new RewriteRuleTokenStream(adaptor, "token RPAREN");
    RewriteRuleSubtreeStream stream_ruleAction = new RewriteRuleSubtreeStream(adaptor, "rule ruleAction");
    RewriteRuleSubtreeStream stream_optionsSpec = new RewriteRuleSubtreeStream(adaptor, "rule optionsSpec");
    RewriteRuleSubtreeStream stream_altList = new RewriteRuleSubtreeStream(adaptor, "rule altList");

    try {
      // org/antlr/v4/parse/ANTLRParser.g:793:3: ( LPAREN ( ( optionsSpec )? (ra+= ruleAction )* COLON )? altList RPAREN -> ^( BLOCK[$LPAREN,\"BLOCK\"] ( optionsSpec )? ( $ra)* altList ) )
      // org/antlr/v4/parse/ANTLRParser.g:793:5: LPAREN ( ( optionsSpec )? (ra+= ruleAction )* COLON )? altList RPAREN
      {
        LPAREN206 = (Token) match(input, LPAREN, FOLLOW_LPAREN_in_block3780);
        stream_LPAREN.add(LPAREN206);

        // org/antlr/v4/parse/ANTLRParser.g:794:9: ( ( optionsSpec )? (ra+= ruleAction )* COLON )?
        int alt74 = 2;
        int LA74_0 = input.LA(1);
        if ((LA74_0 == AT || LA74_0 == COLON || LA74_0 == OPTIONS)) {
          alt74 = 1;
        }
        switch (alt74) {
          case 1:
            // org/antlr/v4/parse/ANTLRParser.g:794:11: ( optionsSpec )? (ra+= ruleAction )* COLON
          {
            // org/antlr/v4/parse/ANTLRParser.g:794:11: ( optionsSpec )?
            int alt72 = 2;
            int LA72_0 = input.LA(1);
            if ((LA72_0 == OPTIONS)) {
              alt72 = 1;
            }
            switch (alt72) {
              case 1:
                // org/antlr/v4/parse/ANTLRParser.g:794:11: optionsSpec
              {
                pushFollow(FOLLOW_optionsSpec_in_block3792);
                optionsSpec207 = optionsSpec();
                state._fsp--;

                stream_optionsSpec.add(optionsSpec207.getTree());
              }
              break;

            }

            // org/antlr/v4/parse/ANTLRParser.g:794:26: (ra+= ruleAction )*
            loop73:
            while (true) {
              int alt73 = 2;
              int LA73_0 = input.LA(1);
              if ((LA73_0 == AT)) {
                alt73 = 1;
              }

              switch (alt73) {
                case 1:
                  // org/antlr/v4/parse/ANTLRParser.g:794:26: ra+= ruleAction
                {
                  pushFollow(FOLLOW_ruleAction_in_block3797);
                  ra = ruleAction();
                  state._fsp--;

                  stream_ruleAction.add(ra.getTree());
                  if (list_ra == null) list_ra = new ArrayList<Object>();
                  list_ra.add(ra.getTree());
                }
                break;

                default:
                  break loop73;
              }
            }

            COLON208 = (Token) match(input, COLON, FOLLOW_COLON_in_block3800);
            stream_COLON.add(COLON208);

          }
          break;

        }

        pushFollow(FOLLOW_altList_in_block3813);
        altList209 = altList();
        state._fsp--;

        stream_altList.add(altList209.getTree());
        RPAREN210 = (Token) match(input, RPAREN, FOLLOW_RPAREN_in_block3817);
        stream_RPAREN.add(RPAREN210);


        // AST REWRITE
        // elements: optionsSpec, ra, altList
        // token labels:
        // rule labels: retval
        // token list labels:
        // rule list labels: ra
        // wildcard labels:
        retval.tree = root_0;
        RewriteRuleSubtreeStream stream_retval = new RewriteRuleSubtreeStream(adaptor, "rule retval", retval != null ? retval.getTree() : null);
        RewriteRuleSubtreeStream stream_ra = new RewriteRuleSubtreeStream(adaptor, "token ra", list_ra);
        root_0 = (GrammarAST) adaptor.nil();
        // 797:7: -> ^( BLOCK[$LPAREN,\"BLOCK\"] ( optionsSpec )? ( $ra)* altList )
        {
          // org/antlr/v4/parse/ANTLRParser.g:797:10: ^( BLOCK[$LPAREN,\"BLOCK\"] ( optionsSpec )? ( $ra)* altList )
          {
            GrammarAST root_1 = (GrammarAST) adaptor.nil();
            root_1 = (GrammarAST) adaptor.becomeRoot(new BlockAST(BLOCK, LPAREN206, "BLOCK"), root_1);
            // org/antlr/v4/parse/ANTLRParser.g:797:45: ( optionsSpec )?
            if (stream_optionsSpec.hasNext()) {
              adaptor.addChild(root_1, stream_optionsSpec.nextTree());
            }
            stream_optionsSpec.reset();

            // org/antlr/v4/parse/ANTLRParser.g:797:59: ( $ra)*
            while (stream_ra.hasNext()) {
              adaptor.addChild(root_1, stream_ra.nextTree());
            }
            stream_ra.reset();

            adaptor.addChild(root_1, stream_altList.nextTree());
            adaptor.addChild(root_0, root_1);
          }

        }


        retval.tree = root_0;

      }

      retval.stop = input.LT(-1);

      retval.tree = (GrammarAST) adaptor.rulePostProcessing(root_0);
      adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);


      GrammarAST options = (GrammarAST) retval.tree.getFirstChildWithType(ANTLRParser.OPTIONS);
      if (options != null) {
        Grammar.setNodeOptions(retval.tree, options);
      }

    } catch (RecognitionException re) {
      reportError(re);
      recover(input, re);
      retval.tree = (GrammarAST) adaptor.errorNode(input, retval.start, input.LT(-1), re);
    } finally {
      // do for sure before leaving
    }
    return retval;
  }
  // $ANTLR end "block"


  public static class ruleref_return extends ParserRuleReturnScope {
    GrammarAST tree;

    @Override
    public GrammarAST getTree() {
      return tree;
    }
  }

  ;


  // $ANTLR start "ruleref"
  // org/antlr/v4/parse/ANTLRParser.g:806:1: ruleref : RULE_REF ( ARG_ACTION )? ( elementOptions )? -> ^( RULE_REF ( ARG_ACTION )? ( elementOptions )? ) ;
  public final ANTLRParser.ruleref_return ruleref() throws RecognitionException {
    ANTLRParser.ruleref_return retval = new ANTLRParser.ruleref_return();
    retval.start = input.LT(1);

    GrammarAST root_0 = null;

    Token RULE_REF211 = null;
    Token ARG_ACTION212 = null;
    ParserRuleReturnScope elementOptions213 = null;

    GrammarAST RULE_REF211_tree = null;
    GrammarAST ARG_ACTION212_tree = null;
    RewriteRuleTokenStream stream_ARG_ACTION = new RewriteRuleTokenStream(adaptor, "token ARG_ACTION");
    RewriteRuleTokenStream stream_RULE_REF = new RewriteRuleTokenStream(adaptor, "token RULE_REF");
    RewriteRuleSubtreeStream stream_elementOptions = new RewriteRuleSubtreeStream(adaptor, "rule elementOptions");

    try {
      // org/antlr/v4/parse/ANTLRParser.g:813:5: ( RULE_REF ( ARG_ACTION )? ( elementOptions )? -> ^( RULE_REF ( ARG_ACTION )? ( elementOptions )? ) )
      // org/antlr/v4/parse/ANTLRParser.g:813:7: RULE_REF ( ARG_ACTION )? ( elementOptions )?
      {
        RULE_REF211 = (Token) match(input, RULE_REF, FOLLOW_RULE_REF_in_ruleref3871);
        stream_RULE_REF.add(RULE_REF211);

        // org/antlr/v4/parse/ANTLRParser.g:813:16: ( ARG_ACTION )?
        int alt75 = 2;
        int LA75_0 = input.LA(1);
        if ((LA75_0 == ARG_ACTION)) {
          alt75 = 1;
        }
        switch (alt75) {
          case 1:
            // org/antlr/v4/parse/ANTLRParser.g:813:16: ARG_ACTION
          {
            ARG_ACTION212 = (Token) match(input, ARG_ACTION, FOLLOW_ARG_ACTION_in_ruleref3873);
            stream_ARG_ACTION.add(ARG_ACTION212);

          }
          break;

        }

        // org/antlr/v4/parse/ANTLRParser.g:813:28: ( elementOptions )?
        int alt76 = 2;
        int LA76_0 = input.LA(1);
        if ((LA76_0 == LT)) {
          alt76 = 1;
        }
        switch (alt76) {
          case 1:
            // org/antlr/v4/parse/ANTLRParser.g:813:28: elementOptions
          {
            pushFollow(FOLLOW_elementOptions_in_ruleref3876);
            elementOptions213 = elementOptions();
            state._fsp--;

            stream_elementOptions.add(elementOptions213.getTree());
          }
          break;

        }


        // AST REWRITE
        // elements: RULE_REF, elementOptions, ARG_ACTION
        // token labels:
        // rule labels: retval
        // token list labels:
        // rule list labels:
        // wildcard labels:
        retval.tree = root_0;
        RewriteRuleSubtreeStream stream_retval = new RewriteRuleSubtreeStream(adaptor, "rule retval", retval != null ? retval.getTree() : null);

        root_0 = (GrammarAST) adaptor.nil();
        // 813:44: -> ^( RULE_REF ( ARG_ACTION )? ( elementOptions )? )
        {
          // org/antlr/v4/parse/ANTLRParser.g:813:47: ^( RULE_REF ( ARG_ACTION )? ( elementOptions )? )
          {
            GrammarAST root_1 = (GrammarAST) adaptor.nil();
            root_1 = (GrammarAST) adaptor.becomeRoot(new RuleRefAST(stream_RULE_REF.nextToken()), root_1);
            // org/antlr/v4/parse/ANTLRParser.g:813:70: ( ARG_ACTION )?
            if (stream_ARG_ACTION.hasNext()) {
              adaptor.addChild(root_1, new ActionAST(stream_ARG_ACTION.nextToken()));
            }
            stream_ARG_ACTION.reset();

            // org/antlr/v4/parse/ANTLRParser.g:813:93: ( elementOptions )?
            if (stream_elementOptions.hasNext()) {
              adaptor.addChild(root_1, stream_elementOptions.nextTree());
            }
            stream_elementOptions.reset();

            adaptor.addChild(root_0, root_1);
          }

        }


        retval.tree = root_0;

      }

      retval.stop = input.LT(-1);

      retval.tree = (GrammarAST) adaptor.rulePostProcessing(root_0);
      adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);


      GrammarAST options = (GrammarAST) retval.tree.getFirstChildWithType(ANTLRParser.ELEMENT_OPTIONS);
      if (options != null) {
        Grammar.setNodeOptions(retval.tree, options);
      }

    } catch (RecognitionException re) {
      throw re;
    } finally {
      // do for sure before leaving
    }
    return retval;
  }
  // $ANTLR end "ruleref"


  public static class range_return extends ParserRuleReturnScope {
    GrammarAST tree;

    @Override
    public GrammarAST getTree() {
      return tree;
    }
  }

  ;


  // $ANTLR start "range"
  // org/antlr/v4/parse/ANTLRParser.g:826:1: range : STRING_LITERAL RANGE ^ STRING_LITERAL ;
  public final ANTLRParser.range_return range() throws RecognitionException {
    ANTLRParser.range_return retval = new ANTLRParser.range_return();
    retval.start = input.LT(1);

    GrammarAST root_0 = null;

    Token STRING_LITERAL214 = null;
    Token RANGE215 = null;
    Token STRING_LITERAL216 = null;

    GrammarAST STRING_LITERAL214_tree = null;
    GrammarAST RANGE215_tree = null;
    GrammarAST STRING_LITERAL216_tree = null;

    try {
      // org/antlr/v4/parse/ANTLRParser.g:827:5: ( STRING_LITERAL RANGE ^ STRING_LITERAL )
      // org/antlr/v4/parse/ANTLRParser.g:827:7: STRING_LITERAL RANGE ^ STRING_LITERAL
      {
        root_0 = (GrammarAST) adaptor.nil();


        STRING_LITERAL214 = (Token) match(input, STRING_LITERAL, FOLLOW_STRING_LITERAL_in_range3932);
        STRING_LITERAL214_tree = new TerminalAST(STRING_LITERAL214);
        adaptor.addChild(root_0, STRING_LITERAL214_tree);

        RANGE215 = (Token) match(input, RANGE, FOLLOW_RANGE_in_range3937);
        RANGE215_tree = new RangeAST(RANGE215);
        root_0 = (GrammarAST) adaptor.becomeRoot(RANGE215_tree, root_0);

        STRING_LITERAL216 = (Token) match(input, STRING_LITERAL, FOLLOW_STRING_LITERAL_in_range3943);
        STRING_LITERAL216_tree = new TerminalAST(STRING_LITERAL216);
        adaptor.addChild(root_0, STRING_LITERAL216_tree);

      }

      retval.stop = input.LT(-1);

      retval.tree = (GrammarAST) adaptor.rulePostProcessing(root_0);
      adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

    } catch (RecognitionException re) {
      reportError(re);
      recover(input, re);
      retval.tree = (GrammarAST) adaptor.errorNode(input, retval.start, input.LT(-1), re);
    } finally {
      // do for sure before leaving
    }
    return retval;
  }
  // $ANTLR end "range"


  public static class terminal_return extends ParserRuleReturnScope {
    GrammarAST tree;

    @Override
    public GrammarAST getTree() {
      return tree;
    }
  }

  ;


  // $ANTLR start "terminal"
  // org/antlr/v4/parse/ANTLRParser.g:830:1: terminal : ( TOKEN_REF ( elementOptions )? -> ^( TOKEN_REF ( elementOptions )? ) | STRING_LITERAL ( elementOptions )? -> ^( STRING_LITERAL ( elementOptions )? ) );
  public final ANTLRParser.terminal_return terminal() throws RecognitionException {
    ANTLRParser.terminal_return retval = new ANTLRParser.terminal_return();
    retval.start = input.LT(1);

    GrammarAST root_0 = null;

    Token TOKEN_REF217 = null;
    Token STRING_LITERAL219 = null;
    ParserRuleReturnScope elementOptions218 = null;
    ParserRuleReturnScope elementOptions220 = null;

    GrammarAST TOKEN_REF217_tree = null;
    GrammarAST STRING_LITERAL219_tree = null;
    RewriteRuleTokenStream stream_TOKEN_REF = new RewriteRuleTokenStream(adaptor, "token TOKEN_REF");
    RewriteRuleTokenStream stream_STRING_LITERAL = new RewriteRuleTokenStream(adaptor, "token STRING_LITERAL");
    RewriteRuleSubtreeStream stream_elementOptions = new RewriteRuleSubtreeStream(adaptor, "rule elementOptions");

    try {
      // org/antlr/v4/parse/ANTLRParser.g:837:2: ( TOKEN_REF ( elementOptions )? -> ^( TOKEN_REF ( elementOptions )? ) | STRING_LITERAL ( elementOptions )? -> ^( STRING_LITERAL ( elementOptions )? ) )
      int alt79 = 2;
      int LA79_0 = input.LA(1);
      if ((LA79_0 == TOKEN_REF)) {
        alt79 = 1;
      } else if ((LA79_0 == STRING_LITERAL)) {
        alt79 = 2;
      } else {
        NoViableAltException nvae =
          new NoViableAltException("", 79, 0, input);
        throw nvae;
      }

      switch (alt79) {
        case 1:
          // org/antlr/v4/parse/ANTLRParser.g:837:6: TOKEN_REF ( elementOptions )?
        {
          TOKEN_REF217 = (Token) match(input, TOKEN_REF, FOLLOW_TOKEN_REF_in_terminal3967);
          stream_TOKEN_REF.add(TOKEN_REF217);

          // org/antlr/v4/parse/ANTLRParser.g:837:16: ( elementOptions )?
          int alt77 = 2;
          int LA77_0 = input.LA(1);
          if ((LA77_0 == LT)) {
            alt77 = 1;
          }
          switch (alt77) {
            case 1:
              // org/antlr/v4/parse/ANTLRParser.g:837:16: elementOptions
            {
              pushFollow(FOLLOW_elementOptions_in_terminal3969);
              elementOptions218 = elementOptions();
              state._fsp--;

              stream_elementOptions.add(elementOptions218.getTree());
            }
            break;

          }


          // AST REWRITE
          // elements: TOKEN_REF, elementOptions
          // token labels:
          // rule labels: retval
          // token list labels:
          // rule list labels:
          // wildcard labels:
          retval.tree = root_0;
          RewriteRuleSubtreeStream stream_retval = new RewriteRuleSubtreeStream(adaptor, "rule retval", retval != null ? retval.getTree() : null);

          root_0 = (GrammarAST) adaptor.nil();
          // 837:33: -> ^( TOKEN_REF ( elementOptions )? )
          {
            // org/antlr/v4/parse/ANTLRParser.g:837:36: ^( TOKEN_REF ( elementOptions )? )
            {
              GrammarAST root_1 = (GrammarAST) adaptor.nil();
              root_1 = (GrammarAST) adaptor.becomeRoot(new TerminalAST(stream_TOKEN_REF.nextToken()), root_1);
              // org/antlr/v4/parse/ANTLRParser.g:837:61: ( elementOptions )?
              if (stream_elementOptions.hasNext()) {
                adaptor.addChild(root_1, stream_elementOptions.nextTree());
              }
              stream_elementOptions.reset();

              adaptor.addChild(root_0, root_1);
            }

          }


          retval.tree = root_0;

        }
        break;
        case 2:
          // org/antlr/v4/parse/ANTLRParser.g:838:6: STRING_LITERAL ( elementOptions )?
        {
          STRING_LITERAL219 = (Token) match(input, STRING_LITERAL, FOLLOW_STRING_LITERAL_in_terminal3990);
          stream_STRING_LITERAL.add(STRING_LITERAL219);

          // org/antlr/v4/parse/ANTLRParser.g:838:21: ( elementOptions )?
          int alt78 = 2;
          int LA78_0 = input.LA(1);
          if ((LA78_0 == LT)) {
            alt78 = 1;
          }
          switch (alt78) {
            case 1:
              // org/antlr/v4/parse/ANTLRParser.g:838:21: elementOptions
            {
              pushFollow(FOLLOW_elementOptions_in_terminal3992);
              elementOptions220 = elementOptions();
              state._fsp--;

              stream_elementOptions.add(elementOptions220.getTree());
            }
            break;

          }


          // AST REWRITE
          // elements: STRING_LITERAL, elementOptions
          // token labels:
          // rule labels: retval
          // token list labels:
          // rule list labels:
          // wildcard labels:
          retval.tree = root_0;
          RewriteRuleSubtreeStream stream_retval = new RewriteRuleSubtreeStream(adaptor, "rule retval", retval != null ? retval.getTree() : null);

          root_0 = (GrammarAST) adaptor.nil();
          // 838:37: -> ^( STRING_LITERAL ( elementOptions )? )
          {
            // org/antlr/v4/parse/ANTLRParser.g:838:40: ^( STRING_LITERAL ( elementOptions )? )
            {
              GrammarAST root_1 = (GrammarAST) adaptor.nil();
              root_1 = (GrammarAST) adaptor.becomeRoot(new TerminalAST(stream_STRING_LITERAL.nextToken()), root_1);
              // org/antlr/v4/parse/ANTLRParser.g:838:70: ( elementOptions )?
              if (stream_elementOptions.hasNext()) {
                adaptor.addChild(root_1, stream_elementOptions.nextTree());
              }
              stream_elementOptions.reset();

              adaptor.addChild(root_0, root_1);
            }

          }


          retval.tree = root_0;

        }
        break;

      }
      retval.stop = input.LT(-1);

      retval.tree = (GrammarAST) adaptor.rulePostProcessing(root_0);
      adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);


      GrammarAST options = (GrammarAST) retval.tree.getFirstChildWithType(ANTLRParser.ELEMENT_OPTIONS);
      if (options != null) {
        Grammar.setNodeOptions(retval.tree, options);
      }

    } catch (RecognitionException re) {
      reportError(re);
      recover(input, re);
      retval.tree = (GrammarAST) adaptor.errorNode(input, retval.start, input.LT(-1), re);
    } finally {
      // do for sure before leaving
    }
    return retval;
  }
  // $ANTLR end "terminal"


  public static class elementOptions_return extends ParserRuleReturnScope {
    GrammarAST tree;

    @Override
    public GrammarAST getTree() {
      return tree;
    }
  }

  ;


  // $ANTLR start "elementOptions"
  // org/antlr/v4/parse/ANTLRParser.g:843:1: elementOptions : LT ( elementOption ( COMMA elementOption )* )? GT -> ^( ELEMENT_OPTIONS[$LT,\"ELEMENT_OPTIONS\"] ( elementOption )* ) ;
  public final ANTLRParser.elementOptions_return elementOptions() throws RecognitionException {
    ANTLRParser.elementOptions_return retval = new ANTLRParser.elementOptions_return();
    retval.start = input.LT(1);

    GrammarAST root_0 = null;

    Token LT221 = null;
    Token COMMA223 = null;
    Token GT225 = null;
    ParserRuleReturnScope elementOption222 = null;
    ParserRuleReturnScope elementOption224 = null;

    GrammarAST LT221_tree = null;
    GrammarAST COMMA223_tree = null;
    GrammarAST GT225_tree = null;
    RewriteRuleTokenStream stream_COMMA = new RewriteRuleTokenStream(adaptor, "token COMMA");
    RewriteRuleTokenStream stream_LT = new RewriteRuleTokenStream(adaptor, "token LT");
    RewriteRuleTokenStream stream_GT = new RewriteRuleTokenStream(adaptor, "token GT");
    RewriteRuleSubtreeStream stream_elementOption = new RewriteRuleSubtreeStream(adaptor, "rule elementOption");

    try {
      // org/antlr/v4/parse/ANTLRParser.g:844:5: ( LT ( elementOption ( COMMA elementOption )* )? GT -> ^( ELEMENT_OPTIONS[$LT,\"ELEMENT_OPTIONS\"] ( elementOption )* ) )
      // org/antlr/v4/parse/ANTLRParser.g:844:9: LT ( elementOption ( COMMA elementOption )* )? GT
      {
        LT221 = (Token) match(input, LT, FOLLOW_LT_in_elementOptions4023);
        stream_LT.add(LT221);

        // org/antlr/v4/parse/ANTLRParser.g:844:12: ( elementOption ( COMMA elementOption )* )?
        int alt81 = 2;
        int LA81_0 = input.LA(1);
        if ((LA81_0 == RULE_REF || LA81_0 == TOKEN_REF)) {
          alt81 = 1;
        }
        switch (alt81) {
          case 1:
            // org/antlr/v4/parse/ANTLRParser.g:844:13: elementOption ( COMMA elementOption )*
          {
            pushFollow(FOLLOW_elementOption_in_elementOptions4026);
            elementOption222 = elementOption();
            state._fsp--;

            stream_elementOption.add(elementOption222.getTree());
            // org/antlr/v4/parse/ANTLRParser.g:844:27: ( COMMA elementOption )*
            loop80:
            while (true) {
              int alt80 = 2;
              int LA80_0 = input.LA(1);
              if ((LA80_0 == COMMA)) {
                alt80 = 1;
              }

              switch (alt80) {
                case 1:
                  // org/antlr/v4/parse/ANTLRParser.g:844:28: COMMA elementOption
                {
                  COMMA223 = (Token) match(input, COMMA, FOLLOW_COMMA_in_elementOptions4029);
                  stream_COMMA.add(COMMA223);

                  pushFollow(FOLLOW_elementOption_in_elementOptions4031);
                  elementOption224 = elementOption();
                  state._fsp--;

                  stream_elementOption.add(elementOption224.getTree());
                }
                break;

                default:
                  break loop80;
              }
            }

          }
          break;

        }

        GT225 = (Token) match(input, GT, FOLLOW_GT_in_elementOptions4037);
        stream_GT.add(GT225);


        // AST REWRITE
        // elements: elementOption
        // token labels:
        // rule labels: retval
        // token list labels:
        // rule list labels:
        // wildcard labels:
        retval.tree = root_0;
        RewriteRuleSubtreeStream stream_retval = new RewriteRuleSubtreeStream(adaptor, "rule retval", retval != null ? retval.getTree() : null);

        root_0 = (GrammarAST) adaptor.nil();
        // 845:13: -> ^( ELEMENT_OPTIONS[$LT,\"ELEMENT_OPTIONS\"] ( elementOption )* )
        {
          // org/antlr/v4/parse/ANTLRParser.g:845:16: ^( ELEMENT_OPTIONS[$LT,\"ELEMENT_OPTIONS\"] ( elementOption )* )
          {
            GrammarAST root_1 = (GrammarAST) adaptor.nil();
            root_1 = (GrammarAST) adaptor.becomeRoot((GrammarAST) adaptor.create(ELEMENT_OPTIONS, LT221, "ELEMENT_OPTIONS"), root_1);
            // org/antlr/v4/parse/ANTLRParser.g:845:57: ( elementOption )*
            while (stream_elementOption.hasNext()) {
              adaptor.addChild(root_1, stream_elementOption.nextTree());
            }
            stream_elementOption.reset();

            adaptor.addChild(root_0, root_1);
          }

        }


        retval.tree = root_0;

      }

      retval.stop = input.LT(-1);

      retval.tree = (GrammarAST) adaptor.rulePostProcessing(root_0);
      adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

    } catch (RecognitionException re) {
      reportError(re);
      recover(input, re);
      retval.tree = (GrammarAST) adaptor.errorNode(input, retval.start, input.LT(-1), re);
    } finally {
      // do for sure before leaving
    }
    return retval;
  }
  // $ANTLR end "elementOptions"


  public static class elementOption_return extends ParserRuleReturnScope {
    GrammarAST tree;

    @Override
    public GrammarAST getTree() {
      return tree;
    }
  }

  ;


  // $ANTLR start "elementOption"
  // org/antlr/v4/parse/ANTLRParser.g:850:1: elementOption : ( qid | id ASSIGN ^ optionValue );
  public final ANTLRParser.elementOption_return elementOption() throws RecognitionException {
    ANTLRParser.elementOption_return retval = new ANTLRParser.elementOption_return();
    retval.start = input.LT(1);

    GrammarAST root_0 = null;

    Token ASSIGN228 = null;
    ParserRuleReturnScope qid226 = null;
    ParserRuleReturnScope id227 = null;
    ParserRuleReturnScope optionValue229 = null;

    GrammarAST ASSIGN228_tree = null;

    try {
      // org/antlr/v4/parse/ANTLRParser.g:851:5: ( qid | id ASSIGN ^ optionValue )
      int alt82 = 2;
      int LA82_0 = input.LA(1);
      if ((LA82_0 == RULE_REF)) {
        int LA82_1 = input.LA(2);
        if ((LA82_1 == COMMA || LA82_1 == DOT || LA82_1 == GT)) {
          alt82 = 1;
        } else if ((LA82_1 == ASSIGN)) {
          alt82 = 2;
        } else {
          int nvaeMark = input.mark();
          try {
            input.consume();
            NoViableAltException nvae =
              new NoViableAltException("", 82, 1, input);
            throw nvae;
          } finally {
            input.rewind(nvaeMark);
          }
        }

      } else if ((LA82_0 == TOKEN_REF)) {
        int LA82_2 = input.LA(2);
        if ((LA82_2 == COMMA || LA82_2 == DOT || LA82_2 == GT)) {
          alt82 = 1;
        } else if ((LA82_2 == ASSIGN)) {
          alt82 = 2;
        } else {
          int nvaeMark = input.mark();
          try {
            input.consume();
            NoViableAltException nvae =
              new NoViableAltException("", 82, 2, input);
            throw nvae;
          } finally {
            input.rewind(nvaeMark);
          }
        }

      } else {
        NoViableAltException nvae =
          new NoViableAltException("", 82, 0, input);
        throw nvae;
      }

      switch (alt82) {
        case 1:
          // org/antlr/v4/parse/ANTLRParser.g:852:7: qid
        {
          root_0 = (GrammarAST) adaptor.nil();


          pushFollow(FOLLOW_qid_in_elementOption4085);
          qid226 = qid();
          state._fsp--;

          adaptor.addChild(root_0, qid226.getTree());

        }
        break;
        case 2:
          // org/antlr/v4/parse/ANTLRParser.g:853:7: id ASSIGN ^ optionValue
        {
          root_0 = (GrammarAST) adaptor.nil();


          pushFollow(FOLLOW_id_in_elementOption4093);
          id227 = id();
          state._fsp--;

          adaptor.addChild(root_0, id227.getTree());

          ASSIGN228 = (Token) match(input, ASSIGN, FOLLOW_ASSIGN_in_elementOption4095);
          ASSIGN228_tree = (GrammarAST) adaptor.create(ASSIGN228);
          root_0 = (GrammarAST) adaptor.becomeRoot(ASSIGN228_tree, root_0);

          pushFollow(FOLLOW_optionValue_in_elementOption4098);
          optionValue229 = optionValue();
          state._fsp--;

          adaptor.addChild(root_0, optionValue229.getTree());

        }
        break;

      }
      retval.stop = input.LT(-1);

      retval.tree = (GrammarAST) adaptor.rulePostProcessing(root_0);
      adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

    } catch (RecognitionException re) {
      reportError(re);
      recover(input, re);
      retval.tree = (GrammarAST) adaptor.errorNode(input, retval.start, input.LT(-1), re);
    } finally {
      // do for sure before leaving
    }
    return retval;
  }
  // $ANTLR end "elementOption"


  public static class id_return extends ParserRuleReturnScope {
    GrammarAST tree;

    @Override
    public GrammarAST getTree() {
      return tree;
    }
  }

  ;


  // $ANTLR start "id"
  // org/antlr/v4/parse/ANTLRParser.g:860:1: id : ( RULE_REF -> ID[$RULE_REF] | TOKEN_REF -> ID[$TOKEN_REF] );
  public final ANTLRParser.id_return id() throws RecognitionException {
    ANTLRParser.id_return retval = new ANTLRParser.id_return();
    retval.start = input.LT(1);

    GrammarAST root_0 = null;

    Token RULE_REF230 = null;
    Token TOKEN_REF231 = null;

    GrammarAST RULE_REF230_tree = null;
    GrammarAST TOKEN_REF231_tree = null;
    RewriteRuleTokenStream stream_TOKEN_REF = new RewriteRuleTokenStream(adaptor, "token TOKEN_REF");
    RewriteRuleTokenStream stream_RULE_REF = new RewriteRuleTokenStream(adaptor, "token RULE_REF");

    paraphrases.push("looking for an identifier");
    try {
      // org/antlr/v4/parse/ANTLRParser.g:863:5: ( RULE_REF -> ID[$RULE_REF] | TOKEN_REF -> ID[$TOKEN_REF] )
      int alt83 = 2;
      int LA83_0 = input.LA(1);
      if ((LA83_0 == RULE_REF)) {
        alt83 = 1;
      } else if ((LA83_0 == TOKEN_REF)) {
        alt83 = 2;
      } else {
        NoViableAltException nvae =
          new NoViableAltException("", 83, 0, input);
        throw nvae;
      }

      switch (alt83) {
        case 1:
          // org/antlr/v4/parse/ANTLRParser.g:863:7: RULE_REF
        {
          RULE_REF230 = (Token) match(input, RULE_REF, FOLLOW_RULE_REF_in_id4129);
          stream_RULE_REF.add(RULE_REF230);


          // AST REWRITE
          // elements:
          // token labels:
          // rule labels: retval
          // token list labels:
          // rule list labels:
          // wildcard labels:
          retval.tree = root_0;
          RewriteRuleSubtreeStream stream_retval = new RewriteRuleSubtreeStream(adaptor, "rule retval", retval != null ? retval.getTree() : null);

          root_0 = (GrammarAST) adaptor.nil();
          // 863:17: -> ID[$RULE_REF]
          {
            adaptor.addChild(root_0, (GrammarAST) adaptor.create(ID, RULE_REF230));
          }


          retval.tree = root_0;

        }
        break;
        case 2:
          // org/antlr/v4/parse/ANTLRParser.g:864:7: TOKEN_REF
        {
          TOKEN_REF231 = (Token) match(input, TOKEN_REF, FOLLOW_TOKEN_REF_in_id4142);
          stream_TOKEN_REF.add(TOKEN_REF231);


          // AST REWRITE
          // elements:
          // token labels:
          // rule labels: retval
          // token list labels:
          // rule list labels:
          // wildcard labels:
          retval.tree = root_0;
          RewriteRuleSubtreeStream stream_retval = new RewriteRuleSubtreeStream(adaptor, "rule retval", retval != null ? retval.getTree() : null);

          root_0 = (GrammarAST) adaptor.nil();
          // 864:17: -> ID[$TOKEN_REF]
          {
            adaptor.addChild(root_0, (GrammarAST) adaptor.create(ID, TOKEN_REF231));
          }


          retval.tree = root_0;

        }
        break;

      }
      retval.stop = input.LT(-1);

      retval.tree = (GrammarAST) adaptor.rulePostProcessing(root_0);
      adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

      paraphrases.pop();
    } catch (RecognitionException re) {
      reportError(re);
      recover(input, re);
      retval.tree = (GrammarAST) adaptor.errorNode(input, retval.start, input.LT(-1), re);
    } finally {
      // do for sure before leaving
    }
    return retval;
  }
  // $ANTLR end "id"


  public static class qid_return extends ParserRuleReturnScope {
    GrammarAST tree;

    @Override
    public GrammarAST getTree() {
      return tree;
    }
  }

  ;


  // $ANTLR start "qid"
  // org/antlr/v4/parse/ANTLRParser.g:867:1: qid : id ( DOT id )* -> ID[$qid.start, $text] ;
  public final ANTLRParser.qid_return qid() throws RecognitionException {
    ANTLRParser.qid_return retval = new ANTLRParser.qid_return();
    retval.start = input.LT(1);

    GrammarAST root_0 = null;

    Token DOT233 = null;
    ParserRuleReturnScope id232 = null;
    ParserRuleReturnScope id234 = null;

    GrammarAST DOT233_tree = null;
    RewriteRuleTokenStream stream_DOT = new RewriteRuleTokenStream(adaptor, "token DOT");
    RewriteRuleSubtreeStream stream_id = new RewriteRuleSubtreeStream(adaptor, "rule id");

    paraphrases.push("looking for a qualified identifier");
    try {
      // org/antlr/v4/parse/ANTLRParser.g:870:2: ( id ( DOT id )* -> ID[$qid.start, $text] )
      // org/antlr/v4/parse/ANTLRParser.g:870:4: id ( DOT id )*
      {
        pushFollow(FOLLOW_id_in_qid4170);
        id232 = id();
        state._fsp--;

        stream_id.add(id232.getTree());
        // org/antlr/v4/parse/ANTLRParser.g:870:7: ( DOT id )*
        loop84:
        while (true) {
          int alt84 = 2;
          int LA84_0 = input.LA(1);
          if ((LA84_0 == DOT)) {
            alt84 = 1;
          }

          switch (alt84) {
            case 1:
              // org/antlr/v4/parse/ANTLRParser.g:870:8: DOT id
            {
              DOT233 = (Token) match(input, DOT, FOLLOW_DOT_in_qid4173);
              stream_DOT.add(DOT233);

              pushFollow(FOLLOW_id_in_qid4175);
              id234 = id();
              state._fsp--;

              stream_id.add(id234.getTree());
            }
            break;

            default:
              break loop84;
          }
        }


        // AST REWRITE
        // elements:
        // token labels:
        // rule labels: retval
        // token list labels:
        // rule list labels:
        // wildcard labels:
        retval.tree = root_0;
        RewriteRuleSubtreeStream stream_retval = new RewriteRuleSubtreeStream(adaptor, "rule retval", retval != null ? retval.getTree() : null);

        root_0 = (GrammarAST) adaptor.nil();
        // 870:17: -> ID[$qid.start, $text]
        {
          adaptor.addChild(root_0, (GrammarAST) adaptor.create(ID, (retval.start), input.toString(retval.start, input.LT(-1))));
        }


        retval.tree = root_0;

      }

      retval.stop = input.LT(-1);

      retval.tree = (GrammarAST) adaptor.rulePostProcessing(root_0);
      adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

      paraphrases.pop();
    } catch (RecognitionException re) {
      reportError(re);
      recover(input, re);
      retval.tree = (GrammarAST) adaptor.errorNode(input, retval.start, input.LT(-1), re);
    } finally {
      // do for sure before leaving
    }
    return retval;
  }
  // $ANTLR end "qid"


  public static class alternativeEntry_return extends ParserRuleReturnScope {
    GrammarAST tree;

    @Override
    public GrammarAST getTree() {
      return tree;
    }
  }

  ;


  // $ANTLR start "alternativeEntry"
  // org/antlr/v4/parse/ANTLRParser.g:873:1: alternativeEntry : alternative EOF ;
  public final ANTLRParser.alternativeEntry_return alternativeEntry() throws RecognitionException {
    ANTLRParser.alternativeEntry_return retval = new ANTLRParser.alternativeEntry_return();
    retval.start = input.LT(1);

    GrammarAST root_0 = null;

    Token EOF236 = null;
    ParserRuleReturnScope alternative235 = null;

    GrammarAST EOF236_tree = null;

    try {
      // org/antlr/v4/parse/ANTLRParser.g:873:18: ( alternative EOF )
      // org/antlr/v4/parse/ANTLRParser.g:873:20: alternative EOF
      {
        root_0 = (GrammarAST) adaptor.nil();


        pushFollow(FOLLOW_alternative_in_alternativeEntry4192);
        alternative235 = alternative();
        state._fsp--;

        adaptor.addChild(root_0, alternative235.getTree());

        EOF236 = (Token) match(input, EOF, FOLLOW_EOF_in_alternativeEntry4194);
        EOF236_tree = (GrammarAST) adaptor.create(EOF236);
        adaptor.addChild(root_0, EOF236_tree);

      }

      retval.stop = input.LT(-1);

      retval.tree = (GrammarAST) adaptor.rulePostProcessing(root_0);
      adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

    } catch (RecognitionException re) {
      reportError(re);
      recover(input, re);
      retval.tree = (GrammarAST) adaptor.errorNode(input, retval.start, input.LT(-1), re);
    } finally {
      // do for sure before leaving
    }
    return retval;
  }
  // $ANTLR end "alternativeEntry"


  public static class elementEntry_return extends ParserRuleReturnScope {
    GrammarAST tree;

    @Override
    public GrammarAST getTree() {
      return tree;
    }
  }

  ;


  // $ANTLR start "elementEntry"
  // org/antlr/v4/parse/ANTLRParser.g:874:1: elementEntry : element EOF ;
  public final ANTLRParser.elementEntry_return elementEntry() throws RecognitionException {
    ANTLRParser.elementEntry_return retval = new ANTLRParser.elementEntry_return();
    retval.start = input.LT(1);

    GrammarAST root_0 = null;

    Token EOF238 = null;
    ParserRuleReturnScope element237 = null;

    GrammarAST EOF238_tree = null;

    try {
      // org/antlr/v4/parse/ANTLRParser.g:874:14: ( element EOF )
      // org/antlr/v4/parse/ANTLRParser.g:874:16: element EOF
      {
        root_0 = (GrammarAST) adaptor.nil();


        pushFollow(FOLLOW_element_in_elementEntry4203);
        element237 = element();
        state._fsp--;

        adaptor.addChild(root_0, element237.getTree());

        EOF238 = (Token) match(input, EOF, FOLLOW_EOF_in_elementEntry4205);
        EOF238_tree = (GrammarAST) adaptor.create(EOF238);
        adaptor.addChild(root_0, EOF238_tree);

      }

      retval.stop = input.LT(-1);

      retval.tree = (GrammarAST) adaptor.rulePostProcessing(root_0);
      adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

    } catch (RecognitionException re) {
      reportError(re);
      recover(input, re);
      retval.tree = (GrammarAST) adaptor.errorNode(input, retval.start, input.LT(-1), re);
    } finally {
      // do for sure before leaving
    }
    return retval;
  }
  // $ANTLR end "elementEntry"


  public static class ruleEntry_return extends ParserRuleReturnScope {
    GrammarAST tree;

    @Override
    public GrammarAST getTree() {
      return tree;
    }
  }

  ;


  // $ANTLR start "ruleEntry"
  // org/antlr/v4/parse/ANTLRParser.g:875:1: ruleEntry : rule EOF ;
  public final ANTLRParser.ruleEntry_return ruleEntry() throws RecognitionException {
    ANTLRParser.ruleEntry_return retval = new ANTLRParser.ruleEntry_return();
    retval.start = input.LT(1);

    GrammarAST root_0 = null;

    Token EOF240 = null;
    ParserRuleReturnScope rule239 = null;

    GrammarAST EOF240_tree = null;

    try {
      // org/antlr/v4/parse/ANTLRParser.g:875:11: ( rule EOF )
      // org/antlr/v4/parse/ANTLRParser.g:875:13: rule EOF
      {
        root_0 = (GrammarAST) adaptor.nil();


        pushFollow(FOLLOW_rule_in_ruleEntry4213);
        rule239 = rule();
        state._fsp--;

        adaptor.addChild(root_0, rule239.getTree());

        EOF240 = (Token) match(input, EOF, FOLLOW_EOF_in_ruleEntry4215);
        EOF240_tree = (GrammarAST) adaptor.create(EOF240);
        adaptor.addChild(root_0, EOF240_tree);

      }

      retval.stop = input.LT(-1);

      retval.tree = (GrammarAST) adaptor.rulePostProcessing(root_0);
      adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

    } catch (RecognitionException re) {
      reportError(re);
      recover(input, re);
      retval.tree = (GrammarAST) adaptor.errorNode(input, retval.start, input.LT(-1), re);
    } finally {
      // do for sure before leaving
    }
    return retval;
  }
  // $ANTLR end "ruleEntry"


  public static class blockEntry_return extends ParserRuleReturnScope {
    GrammarAST tree;

    @Override
    public GrammarAST getTree() {
      return tree;
    }
  }

  ;


  // $ANTLR start "blockEntry"
  // org/antlr/v4/parse/ANTLRParser.g:876:1: blockEntry : block EOF ;
  public final ANTLRParser.blockEntry_return blockEntry() throws RecognitionException {
    ANTLRParser.blockEntry_return retval = new ANTLRParser.blockEntry_return();
    retval.start = input.LT(1);

    GrammarAST root_0 = null;

    Token EOF242 = null;
    ParserRuleReturnScope block241 = null;

    GrammarAST EOF242_tree = null;

    try {
      // org/antlr/v4/parse/ANTLRParser.g:876:12: ( block EOF )
      // org/antlr/v4/parse/ANTLRParser.g:876:14: block EOF
      {
        root_0 = (GrammarAST) adaptor.nil();


        pushFollow(FOLLOW_block_in_blockEntry4223);
        block241 = block();
        state._fsp--;

        adaptor.addChild(root_0, block241.getTree());

        EOF242 = (Token) match(input, EOF, FOLLOW_EOF_in_blockEntry4225);
        EOF242_tree = (GrammarAST) adaptor.create(EOF242);
        adaptor.addChild(root_0, EOF242_tree);

      }

      retval.stop = input.LT(-1);

      retval.tree = (GrammarAST) adaptor.rulePostProcessing(root_0);
      adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

    } catch (RecognitionException re) {
      reportError(re);
      recover(input, re);
      retval.tree = (GrammarAST) adaptor.errorNode(input, retval.start, input.LT(-1), re);
    } finally {
      // do for sure before leaving
    }
    return retval;
  }
  // $ANTLR end "blockEntry"

  // Delegated rules


  public static final BitSet FOLLOW_grammarType_in_grammarSpec396 = new BitSet(new long[]{0x0200000000000000L, 0x0000000000000004L});
  public static final BitSet FOLLOW_id_in_grammarSpec398 = new BitSet(new long[]{0x0400000000000000L});
  public static final BitSet FOLLOW_SEMI_in_grammarSpec400 = new BitSet(new long[]{0x0200040021002800L, 0x0000000000000006L});
  public static final BitSet FOLLOW_sync_in_grammarSpec438 = new BitSet(new long[]{0x0200040021002800L, 0x0000000000000006L});
  public static final BitSet FOLLOW_prequelConstruct_in_grammarSpec442 = new BitSet(new long[]{0x0200040021002800L, 0x0000000000000006L});
  public static final BitSet FOLLOW_sync_in_grammarSpec444 = new BitSet(new long[]{0x0200040021002800L, 0x0000000000000006L});
  public static final BitSet FOLLOW_rules_in_grammarSpec469 = new BitSet(new long[]{0x0000001000000000L});
  public static final BitSet FOLLOW_modeSpec_in_grammarSpec475 = new BitSet(new long[]{0x0000001000000000L});
  public static final BitSet FOLLOW_EOF_in_grammarSpec513 = new BitSet(new long[]{0x0000000000000002L});
  public static final BitSet FOLLOW_LEXER_in_grammarType683 = new BitSet(new long[]{0x0000000002000000L});
  public static final BitSet FOLLOW_GRAMMAR_in_grammarType687 = new BitSet(new long[]{0x0000000000000002L});
  public static final BitSet FOLLOW_PARSER_in_grammarType710 = new BitSet(new long[]{0x0000000002000000L});
  public static final BitSet FOLLOW_GRAMMAR_in_grammarType714 = new BitSet(new long[]{0x0000000000000002L});
  public static final BitSet FOLLOW_GRAMMAR_in_grammarType735 = new BitSet(new long[]{0x0000000000000002L});
  public static final BitSet FOLLOW_TREE_GRAMMAR_in_grammarType762 = new BitSet(new long[]{0x0000000000000002L});
  public static final BitSet FOLLOW_optionsSpec_in_prequelConstruct788 = new BitSet(new long[]{0x0000000000000002L});
  public static final BitSet FOLLOW_delegateGrammars_in_prequelConstruct811 = new BitSet(new long[]{0x0000000000000002L});
  public static final BitSet FOLLOW_tokensSpec_in_prequelConstruct855 = new BitSet(new long[]{0x0000000000000002L});
  public static final BitSet FOLLOW_channelsSpec_in_prequelConstruct865 = new BitSet(new long[]{0x0000000000000002L});
  public static final BitSet FOLLOW_action_in_prequelConstruct902 = new BitSet(new long[]{0x0000000000000002L});
  public static final BitSet FOLLOW_OPTIONS_in_optionsSpec917 = new BitSet(new long[]{0x0240000000000000L, 0x0000000000000004L});
  public static final BitSet FOLLOW_option_in_optionsSpec920 = new BitSet(new long[]{0x0400000000000000L});
  public static final BitSet FOLLOW_SEMI_in_optionsSpec922 = new BitSet(new long[]{0x0240000000000000L, 0x0000000000000004L});
  public static final BitSet FOLLOW_RBRACE_in_optionsSpec926 = new BitSet(new long[]{0x0000000000000002L});
  public static final BitSet FOLLOW_id_in_option955 = new BitSet(new long[]{0x0000000000000400L});
  public static final BitSet FOLLOW_ASSIGN_in_option957 = new BitSet(new long[]{0x4200000040000010L, 0x0000000000000004L});
  public static final BitSet FOLLOW_optionValue_in_option960 = new BitSet(new long[]{0x0000000000000002L});
  public static final BitSet FOLLOW_qid_in_optionValue1003 = new BitSet(new long[]{0x0000000000000002L});
  public static final BitSet FOLLOW_STRING_LITERAL_in_optionValue1011 = new BitSet(new long[]{0x0000000000000002L});
  public static final BitSet FOLLOW_ACTION_in_optionValue1016 = new BitSet(new long[]{0x0000000000000002L});
  public static final BitSet FOLLOW_INT_in_optionValue1027 = new BitSet(new long[]{0x0000000000000002L});
  public static final BitSet FOLLOW_IMPORT_in_delegateGrammars1043 = new BitSet(new long[]{0x0200000000000000L, 0x0000000000000004L});
  public static final BitSet FOLLOW_delegateGrammar_in_delegateGrammars1045 = new BitSet(new long[]{0x0400000000010000L});
  public static final BitSet FOLLOW_COMMA_in_delegateGrammars1048 = new BitSet(new long[]{0x0200000000000000L, 0x0000000000000004L});
  public static final BitSet FOLLOW_delegateGrammar_in_delegateGrammars1050 = new BitSet(new long[]{0x0400000000010000L});
  public static final BitSet FOLLOW_SEMI_in_delegateGrammars1054 = new BitSet(new long[]{0x0000000000000002L});
  public static final BitSet FOLLOW_id_in_delegateGrammar1081 = new BitSet(new long[]{0x0000000000000400L});
  public static final BitSet FOLLOW_ASSIGN_in_delegateGrammar1083 = new BitSet(new long[]{0x0200000000000000L, 0x0000000000000004L});
  public static final BitSet FOLLOW_id_in_delegateGrammar1086 = new BitSet(new long[]{0x0000000000000002L});
  public static final BitSet FOLLOW_id_in_delegateGrammar1096 = new BitSet(new long[]{0x0000000000000002L});
  public static final BitSet FOLLOW_TOKENS_SPEC_in_tokensSpec1110 = new BitSet(new long[]{0x0200000000000000L, 0x0000000000000004L});
  public static final BitSet FOLLOW_id_in_tokensSpec1112 = new BitSet(new long[]{0x0040000000010000L});
  public static final BitSet FOLLOW_COMMA_in_tokensSpec1115 = new BitSet(new long[]{0x0200000000000000L, 0x0000000000000004L});
  public static final BitSet FOLLOW_id_in_tokensSpec1117 = new BitSet(new long[]{0x0040000000010000L});
  public static final BitSet FOLLOW_COMMA_in_tokensSpec1121 = new BitSet(new long[]{0x0040000000000000L});
  public static final BitSet FOLLOW_RBRACE_in_tokensSpec1124 = new BitSet(new long[]{0x0000000000000002L});
  public static final BitSet FOLLOW_TOKENS_SPEC_in_tokensSpec1141 = new BitSet(new long[]{0x0040000000000000L});
  public static final BitSet FOLLOW_RBRACE_in_tokensSpec1143 = new BitSet(new long[]{0x0000000000000002L});
  public static final BitSet FOLLOW_TOKENS_SPEC_in_tokensSpec1153 = new BitSet(new long[]{0x0200000000000000L, 0x0000000000000004L});
  public static final BitSet FOLLOW_v3tokenSpec_in_tokensSpec1156 = new BitSet(new long[]{0x0240000000000000L, 0x0000000000000004L});
  public static final BitSet FOLLOW_RBRACE_in_tokensSpec1159 = new BitSet(new long[]{0x0000000000000002L});
  public static final BitSet FOLLOW_id_in_v3tokenSpec1179 = new BitSet(new long[]{0x0400000000000400L});
  public static final BitSet FOLLOW_ASSIGN_in_v3tokenSpec1185 = new BitSet(new long[]{0x4000000000000000L});
  public static final BitSet FOLLOW_STRING_LITERAL_in_v3tokenSpec1189 = new BitSet(new long[]{0x0400000000000000L});
  public static final BitSet FOLLOW_SEMI_in_v3tokenSpec1250 = new BitSet(new long[]{0x0000000000000002L});
  public static final BitSet FOLLOW_CHANNELS_in_channelsSpec1261 = new BitSet(new long[]{0x0240000000000000L, 0x0000000000000004L});
  public static final BitSet FOLLOW_id_in_channelsSpec1265 = new BitSet(new long[]{0x0040000000010000L});
  public static final BitSet FOLLOW_COMMA_in_channelsSpec1268 = new BitSet(new long[]{0x0200000000000000L, 0x0000000000000004L});
  public static final BitSet FOLLOW_id_in_channelsSpec1271 = new BitSet(new long[]{0x0040000000010000L});
  public static final BitSet FOLLOW_COMMA_in_channelsSpec1275 = new BitSet(new long[]{0x0040000000000000L});
  public static final BitSet FOLLOW_RBRACE_in_channelsSpec1280 = new BitSet(new long[]{0x0000000000000002L});
  public static final BitSet FOLLOW_AT_in_action1297 = new BitSet(new long[]{0x0200100080000000L, 0x0000000000000004L});
  public static final BitSet FOLLOW_actionScopeName_in_action1300 = new BitSet(new long[]{0x0000000000008000L});
  public static final BitSet FOLLOW_COLONCOLON_in_action1302 = new BitSet(new long[]{0x0200000000000000L, 0x0000000000000004L});
  public static final BitSet FOLLOW_id_in_action1306 = new BitSet(new long[]{0x0000000000000010L});
  public static final BitSet FOLLOW_ACTION_in_action1308 = new BitSet(new long[]{0x0000000000000002L});
  public static final BitSet FOLLOW_id_in_actionScopeName1337 = new BitSet(new long[]{0x0000000000000002L});
  public static final BitSet FOLLOW_LEXER_in_actionScopeName1342 = new BitSet(new long[]{0x0000000000000002L});
  public static final BitSet FOLLOW_PARSER_in_actionScopeName1357 = new BitSet(new long[]{0x0000000000000002L});
  public static final BitSet FOLLOW_MODE_in_modeSpec1376 = new BitSet(new long[]{0x0200000000000000L, 0x0000000000000004L});
  public static final BitSet FOLLOW_id_in_modeSpec1378 = new BitSet(new long[]{0x0400000000000000L});
  public static final BitSet FOLLOW_SEMI_in_modeSpec1380 = new BitSet(new long[]{0x0000000001000000L, 0x0000000000000004L});
  public static final BitSet FOLLOW_sync_in_modeSpec1382 = new BitSet(new long[]{0x0000000001000002L, 0x0000000000000004L});
  public static final BitSet FOLLOW_lexerRule_in_modeSpec1385 = new BitSet(new long[]{0x0000000001000000L, 0x0000000000000004L});
  public static final BitSet FOLLOW_sync_in_modeSpec1387 = new BitSet(new long[]{0x0000000001000002L, 0x0000000000000004L});
  public static final BitSet FOLLOW_sync_in_rules1418 = new BitSet(new long[]{0x0200000001000002L, 0x0000000000000004L});
  public static final BitSet FOLLOW_rule_in_rules1421 = new BitSet(new long[]{0x0200000001000000L, 0x0000000000000004L});
  public static final BitSet FOLLOW_sync_in_rules1423 = new BitSet(new long[]{0x0200000001000002L, 0x0000000000000004L});
  public static final BitSet FOLLOW_parserRule_in_rule1485 = new BitSet(new long[]{0x0000000000000002L});
  public static final BitSet FOLLOW_lexerRule_in_rule1490 = new BitSet(new long[]{0x0000000000000002L});
  public static final BitSet FOLLOW_RULE_REF_in_parserRule1539 = new BitSet(new long[]{0x0080040200000900L, 0x0000000000000001L});
  public static final BitSet FOLLOW_ARG_ACTION_in_parserRule1569 = new BitSet(new long[]{0x0080040200000800L, 0x0000000000000001L});
  public static final BitSet FOLLOW_ruleReturns_in_parserRule1576 = new BitSet(new long[]{0x0000040200000800L, 0x0000000000000001L});
  public static final BitSet FOLLOW_throwsSpec_in_parserRule1583 = new BitSet(new long[]{0x0000040200000800L});
  public static final BitSet FOLLOW_localsSpec_in_parserRule1590 = new BitSet(new long[]{0x0000040000000800L});
  public static final BitSet FOLLOW_rulePrequels_in_parserRule1628 = new BitSet(new long[]{0x0000000000004000L});
  public static final BitSet FOLLOW_COLON_in_parserRule1637 = new BitSet(new long[]{0x4A00808C00100010L, 0x0000000000000004L});
  public static final BitSet FOLLOW_ruleBlock_in_parserRule1660 = new BitSet(new long[]{0x0400000000000000L});
  public static final BitSet FOLLOW_SEMI_in_parserRule1669 = new BitSet(new long[]{0x0000000000801000L});
  public static final BitSet FOLLOW_exceptionGroup_in_parserRule1678 = new BitSet(new long[]{0x0000000000000002L});
  public static final BitSet FOLLOW_exceptionHandler_in_exceptionGroup1761 = new BitSet(new long[]{0x0000000000801002L});
  public static final BitSet FOLLOW_finallyClause_in_exceptionGroup1764 = new BitSet(new long[]{0x0000000000000002L});
  public static final BitSet FOLLOW_CATCH_in_exceptionHandler1781 = new BitSet(new long[]{0x0000000000000100L});
  public static final BitSet FOLLOW_ARG_ACTION_in_exceptionHandler1783 = new BitSet(new long[]{0x0000000000000010L});
  public static final BitSet FOLLOW_ACTION_in_exceptionHandler1785 = new BitSet(new long[]{0x0000000000000002L});
  public static final BitSet FOLLOW_FINALLY_in_finallyClause1812 = new BitSet(new long[]{0x0000000000000010L});
  public static final BitSet FOLLOW_ACTION_in_finallyClause1814 = new BitSet(new long[]{0x0000000000000002L});
  public static final BitSet FOLLOW_sync_in_rulePrequels1846 = new BitSet(new long[]{0x0000040000000802L});
  public static final BitSet FOLLOW_rulePrequel_in_rulePrequels1849 = new BitSet(new long[]{0x0000040000000800L});
  public static final BitSet FOLLOW_sync_in_rulePrequels1851 = new BitSet(new long[]{0x0000040000000802L});
  public static final BitSet FOLLOW_optionsSpec_in_rulePrequel1875 = new BitSet(new long[]{0x0000000000000002L});
  public static final BitSet FOLLOW_ruleAction_in_rulePrequel1883 = new BitSet(new long[]{0x0000000000000002L});
  public static final BitSet FOLLOW_RETURNS_in_ruleReturns1903 = new BitSet(new long[]{0x0000000000000100L});
  public static final BitSet FOLLOW_ARG_ACTION_in_ruleReturns1906 = new BitSet(new long[]{0x0000000000000002L});
  public static final BitSet FOLLOW_THROWS_in_throwsSpec1934 = new BitSet(new long[]{0x0200000000000000L, 0x0000000000000004L});
  public static final BitSet FOLLOW_qid_in_throwsSpec1936 = new BitSet(new long[]{0x0000000000010002L});
  public static final BitSet FOLLOW_COMMA_in_throwsSpec1939 = new BitSet(new long[]{0x0200000000000000L, 0x0000000000000004L});
  public static final BitSet FOLLOW_qid_in_throwsSpec1941 = new BitSet(new long[]{0x0000000000010002L});
  public static final BitSet FOLLOW_LOCALS_in_localsSpec1966 = new BitSet(new long[]{0x0000000000000100L});
  public static final BitSet FOLLOW_ARG_ACTION_in_localsSpec1969 = new BitSet(new long[]{0x0000000000000002L});
  public static final BitSet FOLLOW_AT_in_ruleAction1992 = new BitSet(new long[]{0x0200000000000000L, 0x0000000000000004L});
  public static final BitSet FOLLOW_id_in_ruleAction1994 = new BitSet(new long[]{0x0000000000000010L});
  public static final BitSet FOLLOW_ACTION_in_ruleAction1996 = new BitSet(new long[]{0x0000000000000002L});
  public static final BitSet FOLLOW_ruleAltList_in_ruleBlock2034 = new BitSet(new long[]{0x0000000000000002L});
  public static final BitSet FOLLOW_labeledAlt_in_ruleAltList2070 = new BitSet(new long[]{0x0000080000000002L});
  public static final BitSet FOLLOW_OR_in_ruleAltList2073 = new BitSet(new long[]{0x4A00808C00100010L, 0x0000000000000004L});
  public static final BitSet FOLLOW_labeledAlt_in_ruleAltList2075 = new BitSet(new long[]{0x0000080000000002L});
  public static final BitSet FOLLOW_alternative_in_labeledAlt2093 = new BitSet(new long[]{0x0000800000000002L});
  public static final BitSet FOLLOW_POUND_in_labeledAlt2099 = new BitSet(new long[]{0x0200000000000000L, 0x0000000000000004L});
  public static final BitSet FOLLOW_id_in_labeledAlt2102 = new BitSet(new long[]{0x0000000000000002L});
  public static final BitSet FOLLOW_FRAGMENT_in_lexerRule2134 = new BitSet(new long[]{0x0000000000000000L, 0x0000000000000004L});
  public static final BitSet FOLLOW_TOKEN_REF_in_lexerRule2140 = new BitSet(new long[]{0x0000000000004000L});
  public static final BitSet FOLLOW_COLON_in_lexerRule2142 = new BitSet(new long[]{0x4A20008500100010L, 0x0000000000000004L});
  public static final BitSet FOLLOW_lexerRuleBlock_in_lexerRule2144 = new BitSet(new long[]{0x0400000000000000L});
  public static final BitSet FOLLOW_SEMI_in_lexerRule2146 = new BitSet(new long[]{0x0000000000000002L});
  public static final BitSet FOLLOW_lexerAltList_in_lexerRuleBlock2210 = new BitSet(new long[]{0x0000000000000002L});
  public static final BitSet FOLLOW_lexerAlt_in_lexerAltList2246 = new BitSet(new long[]{0x0000080000000002L});
  public static final BitSet FOLLOW_OR_in_lexerAltList2249 = new BitSet(new long[]{0x4A20008500100010L, 0x0000000000000004L});
  public static final BitSet FOLLOW_lexerAlt_in_lexerAltList2251 = new BitSet(new long[]{0x0000080000000002L});
  public static final BitSet FOLLOW_lexerElements_in_lexerAlt2269 = new BitSet(new long[]{0x0020000000000002L});
  public static final BitSet FOLLOW_lexerCommands_in_lexerAlt2275 = new BitSet(new long[]{0x0000000000000002L});
  public static final BitSet FOLLOW_lexerElement_in_lexerElements2318 = new BitSet(new long[]{0x4A00008500100012L, 0x0000000000000004L});
  public static final BitSet FOLLOW_labeledLexerElement_in_lexerElement2374 = new BitSet(new long[]{0x2008200000000002L});
  public static final BitSet FOLLOW_ebnfSuffix_in_lexerElement2380 = new BitSet(new long[]{0x0000000000000002L});
  public static final BitSet FOLLOW_lexerAtom_in_lexerElement2426 = new BitSet(new long[]{0x2008200000000002L});
  public static final BitSet FOLLOW_ebnfSuffix_in_lexerElement2432 = new BitSet(new long[]{0x0000000000000002L});
  public static final BitSet FOLLOW_lexerBlock_in_lexerElement2478 = new BitSet(new long[]{0x2008200000000002L});
  public static final BitSet FOLLOW_ebnfSuffix_in_lexerElement2484 = new BitSet(new long[]{0x0000000000000002L});
  public static final BitSet FOLLOW_actionElement_in_lexerElement2512 = new BitSet(new long[]{0x0000000000000002L});
  public static final BitSet FOLLOW_id_in_labeledLexerElement2542 = new BitSet(new long[]{0x0000400000000400L});
  public static final BitSet FOLLOW_ASSIGN_in_labeledLexerElement2547 = new BitSet(new long[]{0x4200008500100000L, 0x0000000000000004L});
  public static final BitSet FOLLOW_PLUS_ASSIGN_in_labeledLexerElement2551 = new BitSet(new long[]{0x4200008500100000L, 0x0000000000000004L});
  public static final BitSet FOLLOW_lexerAtom_in_labeledLexerElement2558 = new BitSet(new long[]{0x0000000000000002L});
  public static final BitSet FOLLOW_lexerBlock_in_labeledLexerElement2575 = new BitSet(new long[]{0x0000000000000002L});
  public static final BitSet FOLLOW_LPAREN_in_lexerBlock2608 = new BitSet(new long[]{0x4A20048500100010L, 0x0000000000000004L});
  public static final BitSet FOLLOW_optionsSpec_in_lexerBlock2620 = new BitSet(new long[]{0x0000000000004000L});
  public static final BitSet FOLLOW_COLON_in_lexerBlock2622 = new BitSet(new long[]{0x4A20008500100010L, 0x0000000000000004L});
  public static final BitSet FOLLOW_lexerAltList_in_lexerBlock2635 = new BitSet(new long[]{0x0100000000000000L});
  public static final BitSet FOLLOW_RPAREN_in_lexerBlock2645 = new BitSet(new long[]{0x0000000000000002L});
  public static final BitSet FOLLOW_RARROW_in_lexerCommands2682 = new BitSet(new long[]{0x0200001000000000L, 0x0000000000000004L});
  public static final BitSet FOLLOW_lexerCommand_in_lexerCommands2684 = new BitSet(new long[]{0x0000000000010002L});
  public static final BitSet FOLLOW_COMMA_in_lexerCommands2687 = new BitSet(new long[]{0x0200001000000000L, 0x0000000000000004L});
  public static final BitSet FOLLOW_lexerCommand_in_lexerCommands2689 = new BitSet(new long[]{0x0000000000010002L});
  public static final BitSet FOLLOW_lexerCommandName_in_lexerCommand2707 = new BitSet(new long[]{0x0000000400000000L});
  public static final BitSet FOLLOW_LPAREN_in_lexerCommand2709 = new BitSet(new long[]{0x0200000040000000L, 0x0000000000000004L});
  public static final BitSet FOLLOW_lexerCommandExpr_in_lexerCommand2711 = new BitSet(new long[]{0x0100000000000000L});
  public static final BitSet FOLLOW_RPAREN_in_lexerCommand2713 = new BitSet(new long[]{0x0000000000000002L});
  public static final BitSet FOLLOW_lexerCommandName_in_lexerCommand2728 = new BitSet(new long[]{0x0000000000000002L});
  public static final BitSet FOLLOW_id_in_lexerCommandExpr2739 = new BitSet(new long[]{0x0000000000000002L});
  public static final BitSet FOLLOW_INT_in_lexerCommandExpr2744 = new BitSet(new long[]{0x0000000000000002L});
  public static final BitSet FOLLOW_id_in_lexerCommandName2768 = new BitSet(new long[]{0x0000000000000002L});
  public static final BitSet FOLLOW_MODE_in_lexerCommandName2786 = new BitSet(new long[]{0x0000000000000002L});
  public static final BitSet FOLLOW_alternative_in_altList2814 = new BitSet(new long[]{0x0000080000000002L});
  public static final BitSet FOLLOW_OR_in_altList2817 = new BitSet(new long[]{0x4A00088C00100010L, 0x0000000000000004L});
  public static final BitSet FOLLOW_alternative_in_altList2819 = new BitSet(new long[]{0x0000080000000002L});
  public static final BitSet FOLLOW_elementOptions_in_alternative2853 = new BitSet(new long[]{0x4A00008400100012L, 0x0000000000000004L});
  public static final BitSet FOLLOW_element_in_alternative2862 = new BitSet(new long[]{0x4A00008400100012L, 0x0000000000000004L});
  public static final BitSet FOLLOW_labeledElement_in_element2977 = new BitSet(new long[]{0x2008200000000002L});
  public static final BitSet FOLLOW_ebnfSuffix_in_element2983 = new BitSet(new long[]{0x0000000000000002L});
  public static final BitSet FOLLOW_atom_in_element3029 = new BitSet(new long[]{0x2008200000000002L});
  public static final BitSet FOLLOW_ebnfSuffix_in_element3035 = new BitSet(new long[]{0x0000000000000002L});
  public static final BitSet FOLLOW_ebnf_in_element3081 = new BitSet(new long[]{0x0000000000000002L});
  public static final BitSet FOLLOW_actionElement_in_element3086 = new BitSet(new long[]{0x0000000000000002L});
  public static final BitSet FOLLOW_ACTION_in_actionElement3112 = new BitSet(new long[]{0x0000000000000002L});
  public static final BitSet FOLLOW_ACTION_in_actionElement3122 = new BitSet(new long[]{0x0000000800000000L});
  public static final BitSet FOLLOW_elementOptions_in_actionElement3124 = new BitSet(new long[]{0x0000000000000002L});
  public static final BitSet FOLLOW_SEMPRED_in_actionElement3142 = new BitSet(new long[]{0x0000000000000002L});
  public static final BitSet FOLLOW_SEMPRED_in_actionElement3152 = new BitSet(new long[]{0x0000000800000000L});
  public static final BitSet FOLLOW_elementOptions_in_actionElement3154 = new BitSet(new long[]{0x0000000000000002L});
  public static final BitSet FOLLOW_id_in_labeledElement3176 = new BitSet(new long[]{0x0000400000000400L});
  public static final BitSet FOLLOW_ASSIGN_in_labeledElement3181 = new BitSet(new long[]{0x4200008400100000L, 0x0000000000000004L});
  public static final BitSet FOLLOW_PLUS_ASSIGN_in_labeledElement3185 = new BitSet(new long[]{0x4200008400100000L, 0x0000000000000004L});
  public static final BitSet FOLLOW_atom_in_labeledElement3192 = new BitSet(new long[]{0x0000000000000002L});
  public static final BitSet FOLLOW_block_in_labeledElement3214 = new BitSet(new long[]{0x0000000000000002L});
  public static final BitSet FOLLOW_block_in_ebnf3250 = new BitSet(new long[]{0x2008200000000002L});
  public static final BitSet FOLLOW_blockSuffix_in_ebnf3274 = new BitSet(new long[]{0x0000000000000002L});
  public static final BitSet FOLLOW_ebnfSuffix_in_blockSuffix3324 = new BitSet(new long[]{0x0000000000000002L});
  public static final BitSet FOLLOW_QUESTION_in_ebnfSuffix3339 = new BitSet(new long[]{0x0008000000000002L});
  public static final BitSet FOLLOW_QUESTION_in_ebnfSuffix3343 = new BitSet(new long[]{0x0000000000000002L});
  public static final BitSet FOLLOW_STAR_in_ebnfSuffix3359 = new BitSet(new long[]{0x0008000000000002L});
  public static final BitSet FOLLOW_QUESTION_in_ebnfSuffix3363 = new BitSet(new long[]{0x0000000000000002L});
  public static final BitSet FOLLOW_PLUS_in_ebnfSuffix3381 = new BitSet(new long[]{0x0008000000000002L});
  public static final BitSet FOLLOW_QUESTION_in_ebnfSuffix3385 = new BitSet(new long[]{0x0000000000000002L});
  public static final BitSet FOLLOW_range_in_lexerAtom3406 = new BitSet(new long[]{0x0000000000000002L});
  public static final BitSet FOLLOW_terminal_in_lexerAtom3411 = new BitSet(new long[]{0x0000000000000002L});
  public static final BitSet FOLLOW_RULE_REF_in_lexerAtom3421 = new BitSet(new long[]{0x0000000000000002L});
  public static final BitSet FOLLOW_notSet_in_lexerAtom3432 = new BitSet(new long[]{0x0000000000000002L});
  public static final BitSet FOLLOW_wildcard_in_lexerAtom3440 = new BitSet(new long[]{0x0000000000000002L});
  public static final BitSet FOLLOW_LEXER_CHAR_SET_in_lexerAtom3448 = new BitSet(new long[]{0x0000000000000002L});
  public static final BitSet FOLLOW_range_in_atom3493 = new BitSet(new long[]{0x0000000000000002L});
  public static final BitSet FOLLOW_terminal_in_atom3500 = new BitSet(new long[]{0x0000000000000002L});
  public static final BitSet FOLLOW_ruleref_in_atom3510 = new BitSet(new long[]{0x0000000000000002L});
  public static final BitSet FOLLOW_notSet_in_atom3518 = new BitSet(new long[]{0x0000000000000002L});
  public static final BitSet FOLLOW_wildcard_in_atom3526 = new BitSet(new long[]{0x0000000000000002L});
  public static final BitSet FOLLOW_DOT_in_wildcard3574 = new BitSet(new long[]{0x0000000800000002L});
  public static final BitSet FOLLOW_elementOptions_in_wildcard3576 = new BitSet(new long[]{0x0000000000000002L});
  public static final BitSet FOLLOW_NOT_in_notSet3614 = new BitSet(new long[]{0x4000000100000000L, 0x0000000000000004L});
  public static final BitSet FOLLOW_setElement_in_notSet3616 = new BitSet(new long[]{0x0000000000000002L});
  public static final BitSet FOLLOW_NOT_in_notSet3644 = new BitSet(new long[]{0x0000000400000000L});
  public static final BitSet FOLLOW_blockSet_in_notSet3646 = new BitSet(new long[]{0x0000000000000002L});
  public static final BitSet FOLLOW_LPAREN_in_blockSet3681 = new BitSet(new long[]{0x4000000100000000L, 0x0000000000000004L});
  public static final BitSet FOLLOW_setElement_in_blockSet3683 = new BitSet(new long[]{0x0100080000000000L});
  public static final BitSet FOLLOW_OR_in_blockSet3686 = new BitSet(new long[]{0x4000000100000000L, 0x0000000000000004L});
  public static final BitSet FOLLOW_setElement_in_blockSet3688 = new BitSet(new long[]{0x0100080000000000L});
  public static final BitSet FOLLOW_RPAREN_in_blockSet3692 = new BitSet(new long[]{0x0000000000000002L});
  public static final BitSet FOLLOW_TOKEN_REF_in_setElement3722 = new BitSet(new long[]{0x0000000800000002L});
  public static final BitSet FOLLOW_elementOptions_in_setElement3728 = new BitSet(new long[]{0x0000000000000002L});
  public static final BitSet FOLLOW_STRING_LITERAL_in_setElement3734 = new BitSet(new long[]{0x0000000800000002L});
  public static final BitSet FOLLOW_elementOptions_in_setElement3740 = new BitSet(new long[]{0x0000000000000002L});
  public static final BitSet FOLLOW_range_in_setElement3746 = new BitSet(new long[]{0x0000000000000002L});
  public static final BitSet FOLLOW_LEXER_CHAR_SET_in_setElement3756 = new BitSet(new long[]{0x0000000000000002L});
  public static final BitSet FOLLOW_LPAREN_in_block3780 = new BitSet(new long[]{0x4A000C8C00104810L, 0x0000000000000004L});
  public static final BitSet FOLLOW_optionsSpec_in_block3792 = new BitSet(new long[]{0x0000000000004800L});
  public static final BitSet FOLLOW_ruleAction_in_block3797 = new BitSet(new long[]{0x0000000000004800L});
  public static final BitSet FOLLOW_COLON_in_block3800 = new BitSet(new long[]{0x4A00088C00100010L, 0x0000000000000004L});
  public static final BitSet FOLLOW_altList_in_block3813 = new BitSet(new long[]{0x0100000000000000L});
  public static final BitSet FOLLOW_RPAREN_in_block3817 = new BitSet(new long[]{0x0000000000000002L});
  public static final BitSet FOLLOW_RULE_REF_in_ruleref3871 = new BitSet(new long[]{0x0000000800000102L});
  public static final BitSet FOLLOW_ARG_ACTION_in_ruleref3873 = new BitSet(new long[]{0x0000000800000002L});
  public static final BitSet FOLLOW_elementOptions_in_ruleref3876 = new BitSet(new long[]{0x0000000000000002L});
  public static final BitSet FOLLOW_STRING_LITERAL_in_range3932 = new BitSet(new long[]{0x0010000000000000L});
  public static final BitSet FOLLOW_RANGE_in_range3937 = new BitSet(new long[]{0x4000000000000000L});
  public static final BitSet FOLLOW_STRING_LITERAL_in_range3943 = new BitSet(new long[]{0x0000000000000002L});
  public static final BitSet FOLLOW_TOKEN_REF_in_terminal3967 = new BitSet(new long[]{0x0000000800000002L});
  public static final BitSet FOLLOW_elementOptions_in_terminal3969 = new BitSet(new long[]{0x0000000000000002L});
  public static final BitSet FOLLOW_STRING_LITERAL_in_terminal3990 = new BitSet(new long[]{0x0000000800000002L});
  public static final BitSet FOLLOW_elementOptions_in_terminal3992 = new BitSet(new long[]{0x0000000000000002L});
  public static final BitSet FOLLOW_LT_in_elementOptions4023 = new BitSet(new long[]{0x0200000004000000L, 0x0000000000000004L});
  public static final BitSet FOLLOW_elementOption_in_elementOptions4026 = new BitSet(new long[]{0x0000000004010000L});
  public static final BitSet FOLLOW_COMMA_in_elementOptions4029 = new BitSet(new long[]{0x0200000000000000L, 0x0000000000000004L});
  public static final BitSet FOLLOW_elementOption_in_elementOptions4031 = new BitSet(new long[]{0x0000000004010000L});
  public static final BitSet FOLLOW_GT_in_elementOptions4037 = new BitSet(new long[]{0x0000000000000002L});
  public static final BitSet FOLLOW_qid_in_elementOption4085 = new BitSet(new long[]{0x0000000000000002L});
  public static final BitSet FOLLOW_id_in_elementOption4093 = new BitSet(new long[]{0x0000000000000400L});
  public static final BitSet FOLLOW_ASSIGN_in_elementOption4095 = new BitSet(new long[]{0x4200000040000010L, 0x0000000000000004L});
  public static final BitSet FOLLOW_optionValue_in_elementOption4098 = new BitSet(new long[]{0x0000000000000002L});
  public static final BitSet FOLLOW_RULE_REF_in_id4129 = new BitSet(new long[]{0x0000000000000002L});
  public static final BitSet FOLLOW_TOKEN_REF_in_id4142 = new BitSet(new long[]{0x0000000000000002L});
  public static final BitSet FOLLOW_id_in_qid4170 = new BitSet(new long[]{0x0000000000100002L});
  public static final BitSet FOLLOW_DOT_in_qid4173 = new BitSet(new long[]{0x0200000000000000L, 0x0000000000000004L});
  public static final BitSet FOLLOW_id_in_qid4175 = new BitSet(new long[]{0x0000000000100002L});
  public static final BitSet FOLLOW_alternative_in_alternativeEntry4192 = new BitSet(new long[]{0x0000000000000000L});
  public static final BitSet FOLLOW_EOF_in_alternativeEntry4194 = new BitSet(new long[]{0x0000000000000002L});
  public static final BitSet FOLLOW_element_in_elementEntry4203 = new BitSet(new long[]{0x0000000000000000L});
  public static final BitSet FOLLOW_EOF_in_elementEntry4205 = new BitSet(new long[]{0x0000000000000002L});
  public static final BitSet FOLLOW_rule_in_ruleEntry4213 = new BitSet(new long[]{0x0000000000000000L});
  public static final BitSet FOLLOW_EOF_in_ruleEntry4215 = new BitSet(new long[]{0x0000000000000002L});
  public static final BitSet FOLLOW_block_in_blockEntry4223 = new BitSet(new long[]{0x0000000000000000L});
  public static final BitSet FOLLOW_EOF_in_blockEntry4225 = new BitSet(new long[]{0x0000000000000002L});
}
