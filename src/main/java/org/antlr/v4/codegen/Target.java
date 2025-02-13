/*
 * This file is a part of ANTLR.
 *
 * Copyright (c) 2012-2025 The ANTLR Project. All rights reserved.
 * Copyright (c) 2025 Valery Maximov <maximovvalery@gmail.com> and contributors
 *
 * Use of this file is governed by the BSD-3-Clause license that
 * can be found in the LICENSE.txt file in the project root.
 */
package org.antlr.v4.codegen;

import org.antlr.v4.codegen.model.RuleFunction;
import org.antlr.v4.codegen.model.SerializedATN;
import org.antlr.v4.misc.Utils;
import org.antlr.v4.parse.ANTLRParser;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.misc.NotNull;
import org.antlr.v4.tool.ErrorType;
import org.antlr.v4.tool.Grammar;
import org.antlr.v4.tool.Rule;
import org.antlr.v4.tool.ast.GrammarAST;
import org.stringtemplate.v4.NumberRenderer;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STErrorListener;
import org.stringtemplate.v4.STGroup;
import org.stringtemplate.v4.STGroupFile;
import org.stringtemplate.v4.StringRenderer;
import org.stringtemplate.v4.misc.STMessage;

/**
 *
 */
public abstract class Target {
  /**
   * For pure strings of Java 16-bit Unicode char, how can we display
   * it in the target language as a literal.  Useful for dumping
   * predicates and such that may refer to chars that need to be escaped
   * when represented as strings.  Also, templates need to be escaped so
   * that the target language can hold them as a string.
   * <p>
   * I have defined (via the constructor) the set of typical escapes,
   * but your {@link Target} subclass is free to alter the translated chars
   * or add more definitions.  This is non-static so each target can have
   * a different set in memory at same time.
   */
  protected String[] targetCharValueEscape = new String[255];

  protected final CodeGenerator gen;
  private final String language;
  private STGroup templates;

  protected Target(CodeGenerator gen, String language) {
    targetCharValueEscape['\n'] = "\\n";
    targetCharValueEscape['\r'] = "\\r";
    targetCharValueEscape['\t'] = "\\t";
    targetCharValueEscape['\b'] = "\\b";
    targetCharValueEscape['\f'] = "\\f";
    targetCharValueEscape['\\'] = "\\\\";
    targetCharValueEscape['\''] = "\\'";
    targetCharValueEscape['"'] = "\\\"";
    this.gen = gen;
    this.language = language;
  }

  public CodeGenerator getCodeGenerator() {
    return gen;
  }

  public String getLanguage() {
    return language;
  }

  @NotNull
  public STGroup getTemplates() {
    if (templates == null) {
      templates = loadTemplates();
    }

    return templates;
  }

  protected void genFile(Grammar g, ST outputFileST, String fileName) {
    getCodeGenerator().write(outputFileST, fileName);
  }

  /**
   * Get a meaningful name for a token type useful during code generation.
   * Literals without associated names are converted to the string equivalent
   * of their integer values. Used to generate x==ID and x==34 type comparisons
   * etc...  Essentially we are looking for the most obvious way to refer
   * to a token type in the generated code.
   */
  public String getTokenTypeAsTargetLabel(Grammar g, int ttype) {
    String name = g.getTokenName(ttype);
    // If name is not valid, return the token type instead
    if (Grammar.INVALID_TOKEN_NAME.equals(name)) {
      return String.valueOf(ttype);
    }

    return name;
  }

  public String[] getTokenTypesAsTargetLabels(Grammar g, int[] ttypes) {
    String[] labels = new String[ttypes.length];
    for (int i = 0; i < ttypes.length; i++) {
      labels[i] = getTokenTypeAsTargetLabel(g, ttypes[i]);
    }
    return labels;
  }

  /**
   * Given a random string of Java unicode chars, return a new string with
   * optionally appropriate quote characters for target language and possibly
   * with some escaped characters.  For example, if the incoming string has
   * actual newline characters, the output of this method would convert them
   * to the two char sequence \n for Java, C, C++, ...  The new string has
   * double-quotes around it as well.  Example String in memory:
   * <p>
   * a"[newlinechar]b'c[carriagereturnchar]d[tab]e\f
   * <p>
   * would be converted to the valid Java s:
   * <p>
   * "a\"\nb'c\rd\te\\f"
   * <p>
   * or
   * <p>
   * a\"\nb'c\rd\te\\f
   * <p>
   * depending on the quoted arg.
   */
  public String getTargetStringLiteralFromString(String s, boolean quoted) {
    if (s == null) {
      return null;
    }

    StringBuilder buf = new StringBuilder();
    if (quoted) {
      buf.append('"');
    }
    for (int i = 0; i < s.length(); ) {
      int c = s.codePointAt(i);
      if (c != '\'' && // don't escape single quotes in strings for java
        c < targetCharValueEscape.length &&
        targetCharValueEscape[c] != null) {
        buf.append(targetCharValueEscape[c]);
      } else if (shouldUseUnicodeEscapeForCodePointInDoubleQuotedString(c)) {
        appendUnicodeEscapedCodePoint(i, buf);
      } else {
        buf.appendCodePoint(c);
      }
      i += Character.charCount(c);
    }
    if (quoted) {
      buf.append('"');
    }
    return buf.toString();
  }

  /**
   * Escape the Unicode code point appropriately for this language
   * and append the escaped value to {@code sb}.
   */
  abstract protected void appendUnicodeEscapedCodePoint(int codePoint, StringBuilder sb);

  public String getTargetStringLiteralFromString(String s) {
    return getTargetStringLiteralFromString(s, true);
  }

  /**
   * Convert from an ANTLR string literal found in a grammar file to an
   * equivalent string literal in the target language.
   */
  public abstract String getTargetStringLiteralFromANTLRStringLiteral(
    CodeGenerator generator,
    String literal, boolean addQuotes);

  protected boolean shouldUseUnicodeEscapeForCodePointInDoubleQuotedString(int codePoint) {
    // We don't want anyone passing 0x0A (newline) or 0x22
    // (double-quote) here because Java treats \\u000A as
    // a literal newline and \\u0022 as a literal
    // double-quote, so Unicode escaping doesn't help.
    assert codePoint != 0x0A && codePoint != 0x22;

    return
      codePoint < 0x20 || // control characters up to but not including space
        codePoint == 0x5C || // backslash
        codePoint >= 0x7F;   // DEL and beyond (keeps source code 7-bit US-ASCII)
  }

  /**
   * Assume 16-bit char
   */
  public abstract String encodeIntAsCharEscape(int v);

  public String getLoopLabel(GrammarAST ast) {
    return "loop" + ast.token.getTokenIndex();
  }

  public String getLoopCounter(GrammarAST ast) {
    return "cnt" + ast.token.getTokenIndex();
  }

  public String getListLabel(String label) {
    ST st = getTemplates().getInstanceOf("ListLabelName");
    st.add("label", label);
    return st.render();
  }

  public String getRuleFunctionContextStructName(Rule r) {
    if (r.g.isLexer()) {
      return getTemplates().getInstanceOf("LexerRuleContext").render();
    }

    String baseName = r.getBaseContext();
    return Utils.capitalize(baseName) + getTemplates().getInstanceOf("RuleContextNameSuffix").render();
  }

  public String getAltLabelContextStructName(String label) {
    return Utils.capitalize(label) + getTemplates().getInstanceOf("RuleContextNameSuffix").render();
  }

  /**
   * If we know which actual function, we can provide the actual ctx type.
   * This will contain implicit labels etc...  From outside, though, we
   * see only ParserRuleContext unless there are externally visible stuff
   * like args, locals, explicit labels, etc...
   */
  public String getRuleFunctionContextStructName(RuleFunction function) {
    Rule r = function.rule;
    if (r.g.isLexer()) {
      return getTemplates().getInstanceOf("LexerRuleContext").render();
    }

    String baseName = r.getBaseContext();
    return Utils.capitalize(baseName) + getTemplates().getInstanceOf("RuleContextNameSuffix").render();
  }

  // should be same for all refs to same token like ctx.ID within single rule function
  // for literals like 'while', we gen _s<ttype>
  public String getImplicitTokenLabel(String tokenName) {
    ST st = getTemplates().getInstanceOf("ImplicitTokenLabel");
    int ttype = getCodeGenerator().g.getTokenType(tokenName);
    if (tokenName.startsWith("'")) {
      return "s" + ttype;
    }
    String text = getTokenTypeAsTargetLabel(getCodeGenerator().g, ttype);
    st.add("tokenName", text);
    return st.render();
  }

  // x=(A|B)
  public String getImplicitSetLabel(String id) {
    ST st = getTemplates().getInstanceOf("ImplicitSetLabel");
    st.add("id", id);
    return st.render();
  }

  public String getImplicitRuleLabel(String ruleName) {
    ST st = getTemplates().getInstanceOf("ImplicitRuleLabel");
    st.add("ruleName", ruleName);
    return st.render();
  }

  public String getElementListName(String name) {
    ST st = getTemplates().getInstanceOf("ElementListName");
    st.add("elemName", getElementName(name));
    return st.render();
  }

  public String getElementName(String name) {
    if (".".equals(name)) {
      return "_wild";
    }

    if (getCodeGenerator().g.getRule(name) != null) return name;
    int ttype = getCodeGenerator().g.getTokenType(name);
    if (ttype == Token.INVALID_TYPE) return name;
    return getTokenTypeAsTargetLabel(getCodeGenerator().g, ttype);
  }

  /**
   * Generate TParser.java and TLexer.java from T.g4 if combined, else
   * just use T.java as output regardless of type.
   */
  public String getRecognizerFileName(boolean header) {
    ST extST = getTemplates().getInstanceOf("codeFileExtension");
    String recognizerName = gen.g.getRecognizerName();
    return recognizerName + extST.render();
  }

  /**
   * A given grammar T, return the listener name such as
   * TListener.java, if we're using the Java target.
   */
  public String getListenerFileName(boolean header) {
    assert gen.g.name != null;
    ST extST = getTemplates().getInstanceOf("codeFileExtension");
    String listenerName = gen.g.name + "Listener";
    return listenerName + extST.render();
  }

  /**
   * A given grammar T, return the visitor name such as
   * TVisitor.java, if we're using the Java target.
   */
  public String getVisitorFileName(boolean header) {
    assert gen.g.name != null;
    ST extST = getTemplates().getInstanceOf("codeFileExtension");
    String listenerName = gen.g.name + "Visitor";
    return listenerName + extST.render();
  }

  /**
   * A given grammar T, return a blank listener implementation
   * such as TBaseListener.java, if we're using the Java target.
   */
  public String getBaseListenerFileName(boolean header) {
    assert gen.g.name != null;
    ST extST = getTemplates().getInstanceOf("codeFileExtension");
    String listenerName = gen.g.name + "BaseListener";
    return listenerName + extST.render();
  }

  /**
   * A given grammar T, return a blank listener implementation
   * such as TBaseListener.java, if we're using the Java target.
   */
  public String getBaseVisitorFileName(boolean header) {
    assert gen.g.name != null;
    ST extST = getTemplates().getInstanceOf("codeFileExtension");
    String listenerName = gen.g.name + "BaseVisitor";
    return listenerName + extST.render();
  }

  /**
   * Gets the maximum number of 16-bit unsigned integers that can be encoded
   * in a single segment of the serialized ATN.
   *
   * @return the serialized ATN segment limit
   * @see SerializedATN#getSegments
   */
  public int getSerializedATNSegmentLimit() {
    return Integer.MAX_VALUE;
  }

  /**
   * How many bits should be used to do inline token type tests? Java assumes
   * a 64-bit word for bitsets.  Must be a valid wordsize for your target like
   * 8, 16, 32, 64, etc...
   *
   * @since 4.5
   */
  public int getInlineTestSetWordSize() {
    return 64;
  }

  public boolean grammarSymbolCausesIssueInGeneratedCode(GrammarAST idNode) {
    switch (idNode.getParent().getType()) {
      case ANTLRParser.ASSIGN:
        switch (idNode.getParent().getParent().getType()) {
          case ANTLRParser.ELEMENT_OPTIONS:
          case ANTLRParser.OPTIONS:
            return false;

          default:
            break;
        }

        break;

      case ANTLRParser.AT:
      case ANTLRParser.ELEMENT_OPTIONS:
        return false;

      case ANTLRParser.LEXER_ACTION_CALL:
        if (idNode.getChildIndex() == 0) {
          // first child is the command name which is part of the ANTLR language
          return false;
        }

        // arguments to the command should be checked
        break;

      default:
        break;
    }

    return visibleGrammarSymbolCausesIssueInGeneratedCode(idNode);
  }

  protected abstract boolean visibleGrammarSymbolCausesIssueInGeneratedCode(GrammarAST idNode);

  @NotNull
  protected STGroup loadTemplates() {
    STGroup result = new STGroupFile(CodeGenerator.TEMPLATE_ROOT + "/" + getLanguage() + "/" + getLanguage() + STGroup.GROUP_FILE_EXTENSION);
    result.registerRenderer(Integer.class, new NumberRenderer());
    result.registerRenderer(String.class, new StringRenderer());
    result.setListener(new STErrorListener() {
      @Override
      public void compileTimeError(STMessage msg) {
        reportError(msg);
      }

      @Override
      public void runTimeError(STMessage msg) {
        reportError(msg);
      }

      @Override
      public void IOError(STMessage msg) {
        reportError(msg);
      }

      @Override
      public void internalError(STMessage msg) {
        reportError(msg);
      }

      private void reportError(STMessage msg) {
        getCodeGenerator().tool.errMgr.toolError(ErrorType.STRING_TEMPLATE_WARNING, msg.cause, msg.toString());
      }
    });

    return result;
  }

  /**
   * @since 4.3
   */
  public boolean wantsBaseListener() {
    return true;
  }

  /**
   * @since 4.3
   */
  public boolean wantsBaseVisitor() {
    return true;
  }

  /**
   * @since 4.3
   */
  public boolean supportsOverloadedMethods() {
    return true;
  }

  /**
   * @since 4.6
   */
  public boolean needsHeader() {
    return false;
  }

  // Override in targets that need header files.
}
