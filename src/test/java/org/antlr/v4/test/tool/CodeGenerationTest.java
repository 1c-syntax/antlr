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
import org.antlr.v4.automata.ATNFactory;
import org.antlr.v4.automata.LexerATNFactory;
import org.antlr.v4.automata.ParserATNFactory;
import org.antlr.v4.codegen.CodeGenerator;
import org.antlr.v4.semantics.SemanticPipeline;
import org.antlr.v4.tool.Grammar;
import org.antlr.v4.tool.LexerGrammar;
import org.junit.jupiter.api.Test;
import org.stringtemplate.v4.AutoIndentWriter;
import org.stringtemplate.v4.InstanceScope;
import org.stringtemplate.v4.Interpreter;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroup;
import org.stringtemplate.v4.STWriter;
import org.stringtemplate.v4.misc.ErrorManager;
import org.stringtemplate.v4.misc.ErrorType;

import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class CodeGenerationTest extends AbstractBaseTest {

  @Test
  public void testArgDecl() throws Exception { // should use template not string
    String g =
      """
        grammar T;
        a[int xyz] : 'a' ;
        """;
    List<String> evals = getEvalInfoForString(g, "int xyz");
    System.out.println(evals);
    for (String eval : evals) {
      assertThat(eval.startsWith("<pojo:")).as("eval should not be POJO: " + eval).isFalse();
    }
  }

  @Test
  public void AssignTokenNamesToStringLiteralsInGeneratedParserRuleContexts() throws Exception {
    String g =
      """
        grammar T;
        root: 't1';
        Token: 't1';""";
    List<String> evals = getEvalInfoForString(g, "() { return getToken(");
    assertThat(evals.size()).isNotEqualTo(0);
  }

  @Test
  public void AssignTokenNamesToStringLiteralArraysInGeneratedParserRuleContexts() throws Exception {
    String g =
      """
        grammar T;
        root: 't1' 't1';
        Token: 't1';""";
    List<String> evals = getEvalInfoForString(g, "() { return getTokens(");
    assertThat(evals.size()).isNotEqualTo(0);
  }

  /**
   * Add tags around each attribute/template/value write
   */
  public static class DebugInterpreter extends Interpreter {
    List<String> evals = new ArrayList<>();
    ErrorManager myErrMgrCopy;

    public DebugInterpreter(STGroup group, ErrorManager errMgr, boolean debug) {
      super(group, errMgr, debug);
      myErrMgrCopy = errMgr;
    }

    @Override
    protected int writeObject(STWriter out, InstanceScope scope, Object o, String[] options) {
      if (o instanceof ST) {
        String name = ((ST) o).getName();
        name = name.substring(1);
        if (!name.startsWith("_sub")) {
          try {
            out.write("<ST:" + name + ">");
            evals.add("<ST:" + name + ">");
            int r = super.writeObject(out, scope, o, options);
            out.write("</ST:" + name + ">");
            evals.add("</ST:" + name + ">");
            return r;
          } catch (IOException ioe) {
            myErrMgrCopy.IOError(scope.st, ErrorType.WRITE_IO_ERROR, ioe);
          }
        }
      }
      return super.writeObject(out, scope, o, options);
    }

    @Override
    protected int writePOJO(STWriter out, InstanceScope scope, Object o, String[] options) throws IOException {
      Class<?> type = o.getClass();
      String name = type.getSimpleName();
      out.write("<pojo:" + name + ">" + o + "</pojo:" + name + ">");
      evals.add("<pojo:" + name + ">" + o + "</pojo:" + name + ">");
      return super.writePOJO(out, scope, o, options);
    }
  }

  public List<String> getEvalInfoForString(String grammarString, String pattern) throws RecognitionException {
    ErrorQueue equeue = new ErrorQueue();
    Grammar g = new Grammar(grammarString);
    List<String> evals = new ArrayList<>();
    if (g.ast != null && !g.ast.hasErrors) {
      SemanticPipeline sem = new SemanticPipeline(g);
      sem.process();

      ATNFactory factory = new ParserATNFactory(g);
      if (g.isLexer()) factory = new LexerATNFactory((LexerGrammar) g);
      g.atn = factory.createATN();

      CodeGenerator gen = new CodeGenerator(g);
      ST outputFileST = gen.generateParser();

      boolean debug = false;
      DebugInterpreter interp =
        new DebugInterpreter(outputFileST.groupThatCreatedThisInstance,
          outputFileST.impl.nativeGroup.errMgr,
          debug);
      InstanceScope scope = new InstanceScope(null, outputFileST);
      StringWriter sw = new StringWriter();
      AutoIndentWriter out = new AutoIndentWriter(sw);
      interp.exec(out, scope);

      for (String e : interp.evals) {
        if (e.contains(pattern)) {
          evals.add(e);
        }
      }
    }
    if (equeue.size() > 0) {
      System.err.println(equeue);
    }
    return evals;
  }
}
