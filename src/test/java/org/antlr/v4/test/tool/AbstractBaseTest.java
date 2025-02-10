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

import org.antlr.v4.Tool;
import org.antlr.v4.analysis.AnalysisPipeline;
import org.antlr.v4.automata.ATNFactory;
import org.antlr.v4.automata.ATNPrinter;
import org.antlr.v4.automata.LexerATNFactory;
import org.antlr.v4.automata.ParserATNFactory;
import org.antlr.v4.codegen.CodeGenerator;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonToken;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.IntStream;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.RuleContext;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenSource;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.atn.ATN;
import org.antlr.v4.runtime.atn.ATNDeserializer;
import org.antlr.v4.runtime.atn.ATNSerializer;
import org.antlr.v4.runtime.atn.ATNState;
import org.antlr.v4.runtime.atn.LexerATNSimulator;
import org.antlr.v4.runtime.misc.IntegerList;
import org.antlr.v4.runtime.misc.Interval;
import org.antlr.v4.runtime.misc.NotNull;
import org.antlr.v4.runtime.misc.Nullable;
import org.antlr.v4.runtime.misc.Tuple;
import org.antlr.v4.runtime.misc.Pair;
import org.antlr.v4.runtime.misc.Utils;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.semantics.SemanticPipeline;
import org.antlr.v4.tool.ANTLRMessage;
import org.antlr.v4.tool.DOTGenerator;
import org.antlr.v4.tool.DefaultToolListener;
import org.antlr.v4.tool.Grammar;
import org.antlr.v4.tool.GrammarSemanticsMessage;
import org.antlr.v4.tool.LexerGrammar;
import org.antlr.v4.tool.Rule;
import org.junit.jupiter.api.BeforeEach;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroup;
import org.stringtemplate.v4.STGroupString;

import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.io.PrintStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.antlr.v4.TestUtils.assertEquals;
import static org.assertj.core.api.Assertions.assertThat;

public abstract class AbstractBaseTest {
  // -J-Dorg.antlr.v4.test.BaseTest.level=FINE
  private static final Logger LOGGER = Logger.getLogger(AbstractBaseTest.class.getName());

  public static final String newline = System.lineSeparator();
  public static final String pathSep = File.pathSeparator;

  /**
   * When the {@code antlr.testinprocess} runtime property is set to
   * {@code true}, the test suite will attempt to load generated classes into
   * the test process for direct execution rather than invoking the JVM in a
   * new process for testing.
   *
   * <p>
   * In-process testing results in a substantial performance improvement, but
   * some test environments created by IDEs do not support the mechanisms
   * currently used by the tests to dynamically load compiled code. Therefore,
   * the default behavior (used in all other cases) favors reliable
   * cross-system test execution by executing generated test code in a
   * separate process.</p>
   */
  public static final boolean TEST_IN_SAME_PROCESS = Boolean.parseBoolean(System.getProperty("antlr.testinprocess"));
  public static final boolean STRICT_COMPILE_CHECKS = Boolean.parseBoolean(System.getProperty("antlr.strictcompile"));

  /**
   * When the {@code antlr.preserve-test-dir} runtime property is set to
   * {@code true}, the temporary directories created by the test run will not
   * be removed at the end of the test run, even for tests that completed
   * successfully.
   *
   * <p>
   * The default behavior (used in all other cases) is removing the temporary
   * directories for all tests which completed successfully, and preserving
   * the directories for tests which failed.</p>
   */
  public static final boolean PRESERVE_TEST_DIR = Boolean.parseBoolean(System.getProperty("antlr.preserve-test-dir"));

  /**
   * The base test directory is the directory where generated files get placed
   * during unit test execution.
   *
   * <p>
   * The default value for this property is the {@code java.io.tmpdir} system
   * property, and can be overridden by setting the
   * {@code antlr.java-test-dir} property to a custom location. Note that the
   * {@code antlr.java-test-dir} property directly affects the
   * {@link #CREATE_PER_TEST_DIRECTORIES} value as well.</p>
   */
  public static final String BASE_TEST_DIR;

  /**
   * When {@code true}, a temporary directory will be created for each test
   * executed during the test run.
   *
   * <p>
   * This value is {@code true} when the {@code antlr.java-test-dir} system
   * property is set, and otherwise {@code false}.</p>
   */
  public static final boolean CREATE_PER_TEST_DIRECTORIES;

  static {
    String baseTestDir = System.getProperty("antlr.java-test-dir");
    boolean perTestDirectories = false;
    if (baseTestDir == null || baseTestDir.isEmpty()) {
      baseTestDir = System.getProperty("java.io.tmpdir");
      perTestDirectories = true;
    }

    if (!new File(baseTestDir).isDirectory()) {
      throw new UnsupportedOperationException("The specified base test directory does not exist: " + baseTestDir);
    }

    BASE_TEST_DIR = baseTestDir;
    CREATE_PER_TEST_DIRECTORIES = perTestDirectories;
  }

  /**
   * Build up the full classpath we need, including the surefire path (if present)
   */
  public static final String CLASSPATH = System.getProperty("java.class.path");

  public String tmpdir = null;

  /**
   * If error during parser execution, store stderr here; can't return
   * stdout and stderr.  This doesn't trap errors from running antlr.
   */
  protected String stderrDuringParse;

  @BeforeEach
  public void setUp() throws Exception {
    if (CREATE_PER_TEST_DIRECTORIES) {
      // new output dir for each test
      String testDirectory = getClass().getSimpleName() + "-" + System.currentTimeMillis();
      tmpdir = new File(BASE_TEST_DIR, testDirectory).getAbsolutePath();
    } else {
      tmpdir = new File(BASE_TEST_DIR).getAbsolutePath();
      if (!PRESERVE_TEST_DIR && new File(tmpdir).exists()) {
        eraseFiles();
      }
    }
  }

  protected boolean testInSameProcess() {
    return TEST_IN_SAME_PROCESS;
  }

  protected org.antlr.v4.Tool newTool(String[] args) {
    return new Tool(args);
  }

  protected ATN createATN(Grammar g, boolean useSerializer) {
    if (g.atn == null) {
      semanticProcess(g);
      assertEquals(0, g.tool.getNumErrors());

      ParserATNFactory f;
      if (g.isLexer()) {
        f = new LexerATNFactory((LexerGrammar) g);
      } else {
        f = new ParserATNFactory(g);
      }

      g.atn = f.createATN();
      assertEquals(0, g.tool.getNumErrors());
    }

    ATN atn = g.atn;
    if (useSerializer) {
      char[] serialized = ATNSerializer.getSerializedAsChars(atn, Arrays.asList(g.getRuleNames()));
      return new ATNDeserializer().deserialize(serialized);
    }

    return atn;
  }

  protected void semanticProcess(Grammar g) {
    if (g.ast != null && !g.ast.hasErrors) {
      System.out.println(g.ast.toStringTree());
      Tool antlr = new Tool();
      SemanticPipeline sem = new SemanticPipeline(g);
      sem.process();
      if (g.getImportedGrammars() != null) { // process imported grammars (if any)
        for (Grammar imp : g.getImportedGrammars()) {
          antlr.processNonCombinedGrammar(imp, false);
        }
      }
    }
  }

  public IntegerList getTokenTypesViaATN(String input, LexerATNSimulator lexerATN) {
    CharStream in = CharStreams.fromString(input);
    IntegerList tokenTypes = new IntegerList();
    int ttype;
    do {
      ttype = lexerATN.match(in, Lexer.DEFAULT_MODE);
      tokenTypes.add(ttype);
    } while (ttype != Token.EOF);
    return tokenTypes;
  }

  public List<String> getTokenTypes(LexerGrammar lg,
                                    ATN atn,
                                    CharStream input) {
    LexerATNSimulator interp = new LexerATNSimulator(atn);
    List<String> tokenTypes = new ArrayList<>();
    int ttype;
    boolean hitEOF = false;
    do {
      if (hitEOF) {
        tokenTypes.add("EOF");
        break;
      }
      int t = input.LA(1);
      ttype = interp.match(input, Lexer.DEFAULT_MODE);
      if (ttype == Token.EOF) {
        tokenTypes.add("EOF");
      } else {
        tokenTypes.add(lg.typeToTokenList.get(ttype));
      }

      if (t == IntStream.EOF) {
        hitEOF = true;
      }
    } while (ttype != Token.EOF);
    return tokenTypes;
  }

  protected String load(String fileName, @Nullable String encoding)
    throws IOException {
    if (fileName == null) {
      return null;
    }

    String fullFileName = getClass().getPackage().getName().replace('.', '/') + '/' + fileName;
    int size = 65000;
    InputStreamReader isr;
    InputStream fis = getClass().getClassLoader().getResourceAsStream(fullFileName);
    if (encoding != null) {
      isr = new InputStreamReader(fis, encoding);
    } else {
      isr = new InputStreamReader(fis);
    }
    try {
      char[] data = new char[size];
      int n = isr.read(data);
      return new String(data, 0, n);
    } finally {
      isr.close();
    }
  }

  /**
   * Wow! much faster than compiling outside of VM. Finicky though.
   * Had rules called r and modulo. Wouldn't compile til I changed to 'a'.
   */
  protected boolean compile(String... fileNames) {
    List<File> files = new ArrayList<>();
    for (String fileName : fileNames) {
      File f = new File(tmpdir, fileName);
      files.add(f);
    }

    JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();

    StandardJavaFileManager fileManager =
      compiler.getStandardFileManager(null, null, StandardCharsets.UTF_8);

    Iterable<? extends JavaFileObject> compilationUnits =
      fileManager.getJavaFileObjectsFromFiles(files);

    List<String> compileOptions = getCompileOptions();
    JavaCompiler.CompilationTask task =
      compiler.getTask(null, fileManager, null, compileOptions, null,
        compilationUnits);
    boolean ok = task.call();

    try {
      fileManager.close();
    } catch (IOException ioe) {
      ioe.printStackTrace(System.err);
    }

    return ok;
  }

  // todo переделать, см ANTLR runtime/Generator
  public List<String> getCompileOptions() {
    List<String> compileOptions = new ArrayList<>();
    compileOptions.add("-g");
    compileOptions.add("-source");
    compileOptions.add("1.6");
    compileOptions.add("-target");
    compileOptions.add("1.6");
    compileOptions.add("-implicit:class");
    compileOptions.add("-Xlint:-options");

    String bootclasspath = getBootClassPath();
    if (bootclasspath != null) {
      compileOptions.add("-bootclasspath");
      compileOptions.add(bootclasspath);
    }

    if (STRICT_COMPILE_CHECKS) {
      compileOptions.add("-Xlint");
      compileOptions.add("-Xlint:-serial");
      compileOptions.add("-Werror");
    }

    compileOptions.addAll(Arrays.asList("-d", tmpdir, "-cp", tmpdir + pathSep + CLASSPATH));
    return compileOptions;
  }

  public String getBootClassPath() {
    String path = System.getProperty("bootclasspath.java6");
    if (path != null) {
      return path;
    }

    path = System.getProperty("java6.home");
    if (path == null) {
      path = System.getenv("JAVA6_HOME");
    }

    if (path != null) {
      return path + File.separatorChar + "lib" + File.separatorChar + "rt.jar";
    }

    return null;
  }

  protected ErrorQueue antlr(String grammarFileName, boolean defaultListener, String... extraOptions) {
    final List<String> options = new ArrayList<>();
    Collections.addAll(options, extraOptions);
    if (!options.contains("-o")) {
      options.add("-o");
      options.add(tmpdir);
    }
    if (!options.contains("-lib")) {
      options.add("-lib");
      options.add(tmpdir);
    }
    if (!options.contains("-encoding")) {
      options.add("-encoding");
      options.add("UTF-8");
    }
    options.add(new File(tmpdir, grammarFileName).toString());

    final String[] optionsA = new String[options.size()];
    options.toArray(optionsA);
    Tool antlr = newTool(optionsA);
    ErrorQueue equeue = new ErrorQueue(antlr);
    antlr.addListener(equeue);
    if (defaultListener) {
      antlr.addListener(new DefaultToolListener(antlr));
    }
    antlr.processGrammarsOnCommandLine();

    if (!defaultListener && !equeue.errors.isEmpty()) {
      System.err.println("antlr reports errors from " + options);
      for (int i = 0; i < equeue.errors.size(); i++) {
        ANTLRMessage msg = equeue.errors.get(i);
        System.err.println(msg);
      }
      System.out.println("!!!\ngrammar:");
      try {
        System.out.println(new String(Utils.readFile(tmpdir + "/" + grammarFileName)));
      } catch (IOException ioe) {
        System.err.println(ioe.toString());
      }
      System.out.println("###");
    }
    if (!defaultListener && !equeue.warnings.isEmpty()) {
      System.err.println("antlr reports warnings from " + options);
      for (int i = 0; i < equeue.warnings.size(); i++) {
        ANTLRMessage msg = equeue.warnings.get(i);
        System.err.println(msg);
      }
    }

    if (!defaultListener && !equeue.warnings.isEmpty()) {
      System.err.println("antlr reports warnings from " + options);
      for (int i = 0; i < equeue.warnings.size(); i++) {
        ANTLRMessage msg = equeue.warnings.get(i);
        System.err.println(msg);
      }
    }

    return equeue;
  }

  protected ErrorQueue antlr(String grammarFileName,
                             String grammarStr,
                             boolean defaultListener,
                             String... extraOptions) {
    System.out.println("dir " + tmpdir);
    mkdir(tmpdir);
    writeFile(tmpdir, grammarFileName, grammarStr);
    return antlr(grammarFileName, defaultListener, extraOptions);
  }

  protected String execLexer(String grammarFileName,
                             String grammarStr,
                             String lexerName,
                             String input) {
    return execLexer(grammarFileName, grammarStr, lexerName, input, false);
  }

  protected String execLexer(String grammarFileName,
                             String grammarStr,
                             String lexerName,
                             String input,
                             boolean showDFA) {
    boolean success = rawGenerateAndBuildRecognizer(grammarFileName,
      grammarStr,
      null,
      lexerName);
    assertThat(success).isTrue();
    writeFile(tmpdir, "input", input);
    writeLexerTestFile(lexerName, showDFA);
    compile("Test.java");
    String output = execClass("Test");
    if (stderrDuringParse != null && !stderrDuringParse.isEmpty()) {
      System.err.println(stderrDuringParse);
    }
    return output;
  }

  public ParseTree execParser(String startRuleName, String input,
                              String parserName, String lexerName)
    throws Exception {
    Pair<Parser, Lexer> pl = getParserAndLexer(input, parserName, lexerName);
    Parser parser = pl.getItem1();
    return execStartRule(startRuleName, parser);
  }

  public ParseTree execStartRule(String startRuleName, Parser parser)
    throws IllegalAccessException, InvocationTargetException,
    NoSuchMethodException {
    Method startRule;
    Object[] args = null;
    try {
      startRule = parser.getClass().getMethod(startRuleName);
    } catch (NoSuchMethodException nsme) {
      // try with int _p arg for recursive func
      startRule = parser.getClass().getMethod(startRuleName, int.class);
      args = new Integer[]{0};
    }
    return (ParseTree) startRule.invoke(parser, args);
  }

  public Pair<Parser, Lexer> getParserAndLexer(String input,
                                               String parserName, String lexerName)
    throws Exception {
    final Class<? extends Lexer> lexerClass = loadLexerClassFromTempDir(lexerName);
    final Class<? extends Parser> parserClass = loadParserClassFromTempDir(parserName);

    Class<? extends Lexer> c = lexerClass.asSubclass(Lexer.class);
    Constructor<? extends Lexer> ctor = c.getConstructor(CharStream.class);
    Lexer lexer = ctor.newInstance(CharStreams.fromString(input));

    Class<? extends Parser> pc = parserClass.asSubclass(Parser.class);
    Constructor<? extends Parser> pctor = pc.getConstructor(TokenStream.class);
    CommonTokenStream tokens = new CommonTokenStream(lexer);
    Parser parser = pctor.newInstance(tokens);
    return Tuple.create(parser, lexer);
  }

  public Class<?> loadClassFromTempDir(String name) throws Exception {
    ClassLoader loader =
      new URLClassLoader(new URL[]{new File(tmpdir).toURI().toURL()},
        ClassLoader.getSystemClassLoader());
    return loader.loadClass(name);
  }

  public Class<? extends Lexer> loadLexerClassFromTempDir(String name) throws Exception {
    return loadClassFromTempDir(name).asSubclass(Lexer.class);
  }

  public Class<? extends Parser> loadParserClassFromTempDir(String name) throws Exception {
    return loadClassFromTempDir(name).asSubclass(Parser.class);
  }

  protected String execParser(String grammarFileName,
                              String grammarStr,
                              String parserName,
                              String lexerName,
                              String startRuleName,
                              String input, boolean debug) {
    return execParser(grammarFileName, grammarStr, parserName,
      lexerName, startRuleName, input, debug, false);
  }

  protected String execParser(String grammarFileName,
                              String grammarStr,
                              String parserName,
                              String lexerName,
                              String startRuleName,
                              String input, boolean debug,
                              boolean profile) {
    boolean success = rawGenerateAndBuildRecognizer(grammarFileName,
      grammarStr,
      parserName,
      lexerName,
      "-visitor");
    assertThat(success).isTrue();
    writeFile(tmpdir, "input", input);
    return rawExecRecognizer(parserName,
      lexerName,
      startRuleName,
      debug,
      profile);
  }

  /**
   * Return true if all is well
   */
  protected boolean rawGenerateAndBuildRecognizer(String grammarFileName,
                                                  String grammarStr,
                                                  @Nullable String parserName,
                                                  String lexerName,
                                                  String... extraOptions) {
    return rawGenerateAndBuildRecognizer(grammarFileName,
      grammarStr,
      parserName,
      lexerName,
      false,
      extraOptions);
  }

  /**
   * Return true if all is well
   */
  protected boolean rawGenerateAndBuildRecognizer(String grammarFileName,
                                                  String grammarStr,
                                                  @Nullable String parserName,
                                                  String lexerName,
                                                  boolean defaultListener,
                                                  String... extraOptions) {
    ErrorQueue equeue = antlr(grammarFileName, grammarStr, defaultListener, extraOptions);
    if (!equeue.errors.isEmpty()) {
      return false;
    }

    List<String> files = new ArrayList<>();
    if (lexerName != null) {
      files.add(lexerName + ".java");
    }
    if (parserName != null) {
      files.add(parserName + ".java");
      Set<String> optionsSet = new HashSet<>(Arrays.asList(extraOptions));
      String grammarName = grammarFileName.substring(0, grammarFileName.lastIndexOf('.'));
      if (!optionsSet.contains("-no-listener")) {
        files.add(grammarName + "Listener.java");
        files.add(grammarName + "BaseListener.java");
      }
      if (optionsSet.contains("-visitor")) {
        files.add(grammarName + "Visitor.java");
        files.add(grammarName + "BaseVisitor.java");
      }
    }
    return compile(files.toArray(new String[files.size()]));
  }

  protected String rawExecRecognizer(String parserName,
                                     String lexerName,
                                     String parserStartRuleName,
                                     boolean debug,
                                     boolean profile) {
    this.stderrDuringParse = null;
    if (parserName == null) {
      writeLexerTestFile(lexerName, false);
    } else {
      writeTestFile(parserName,
        lexerName,
        parserStartRuleName,
        debug,
        profile);
    }

    compile("Test.java");
    return execClass("Test");
  }

  public String execRecognizer() {
    return execClass("Test");
  }

  public String execClass(String className) {
    if (testInSameProcess()) {
      try {
        ClassLoader loader = new URLClassLoader(
          new URL[]{new File(tmpdir).toURI().toURL()}, ClassLoader.getSystemClassLoader());

        final Class<?> mainClass = loader.loadClass(className);
        final Method mainMethod = mainClass.getDeclaredMethod("main", String[].class);
        PipedInputStream stdoutIn = new PipedInputStream();
        PipedInputStream stderrIn = new PipedInputStream();
        PipedOutputStream stdoutOut = new PipedOutputStream(stdoutIn);
        PipedOutputStream stderrOut = new PipedOutputStream(stderrIn);
        StreamVacuum stdoutVacuum = new StreamVacuum(stdoutIn);
        StreamVacuum stderrVacuum = new StreamVacuum(stderrIn);

        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(stdoutOut, false, StandardCharsets.UTF_8));
        try {
          PrintStream originalErr = System.err;
          try {
            System.setErr(new PrintStream(stderrOut, false, StandardCharsets.UTF_8));
            stdoutVacuum.start();
            stderrVacuum.start();
            mainMethod.invoke(null, (Object) new String[]{new File(tmpdir, "input").getAbsolutePath()});
          } finally {
            System.setErr(originalErr);
          }
        } finally {
          System.setOut(originalOut);
        }

        stdoutOut.close();
        stderrOut.close();
        stdoutVacuum.join();
        stderrVacuum.join();
        String output = stdoutVacuum.toString();
        if (!stderrVacuum.toString().isEmpty()) {
          this.stderrDuringParse = stderrVacuum.toString();
          System.err.println("exec stderrVacuum: " + stderrVacuum);
        }
        return output;
      } catch (IOException | InterruptedException | IllegalAccessException | IllegalArgumentException |
               InvocationTargetException | NoSuchMethodException | SecurityException | ClassNotFoundException ex) {
        LOGGER.log(Level.SEVERE, null, ex);
        throw new RuntimeException(ex);
      }
    }

    try {
      String[] args = new String[]{
        "java", "-classpath", tmpdir + pathSep + CLASSPATH,
        "-Dfile.encoding=UTF-8",
        className, new File(tmpdir, "input").getAbsolutePath()
      };

      Process process =
        Runtime.getRuntime().exec(args, null, new File(tmpdir));
      StreamVacuum stdoutVacuum = new StreamVacuum(process.getInputStream());
      StreamVacuum stderrVacuum = new StreamVacuum(process.getErrorStream());
      stdoutVacuum.start();
      stderrVacuum.start();
      process.waitFor();
      stdoutVacuum.join();
      stderrVacuum.join();
      String output = stdoutVacuum.toString();
      if (!stderrVacuum.toString().isEmpty()) {
        this.stderrDuringParse = stderrVacuum.toString();
        System.err.println("exec stderrVacuum: " + stderrVacuum);
      }
      return output;
    } catch (Exception e) {
      System.err.println("can't exec recognizer");
      e.printStackTrace(System.err);
    }
    return null;
  }

  public void testErrors(String[] pairs, boolean printTree) {
    for (int i = 0; i < pairs.length; i += 2) {
      String input = pairs[i];
      String expect = pairs[i + 1];

      String[] lines = input.split("\n");
      String fileName = getFilenameFromFirstLineOfGrammar(lines[0]);
      ErrorQueue equeue = antlr(fileName, input, false);

      String actual = equeue.toString(true);
      actual = actual.replace(tmpdir + File.separator, "");
      System.err.println(actual);
      String msg = input;
      msg = msg.replace("\n", "\\n");
      msg = msg.replace("\r", "\\r");
      msg = msg.replace("\t", "\\t");

      assertThat(actual)
        .as("error in: " + msg)
        .isEqualTo(expect);
    }
  }

  public String getFilenameFromFirstLineOfGrammar(String line) {
    String fileName = "A" + Tool.GRAMMAR_EXTENSION;
    int grIndex = line.lastIndexOf("grammar");
    int semi = line.lastIndexOf(';');
    if (grIndex >= 0 && semi >= 0) {
      int space = line.indexOf(' ', grIndex);
      fileName = line.substring(space + 1, semi) + Tool.GRAMMAR_EXTENSION;
    }
    if (fileName.length() == Tool.GRAMMAR_EXTENSION.length()) fileName = "A" + Tool.GRAMMAR_EXTENSION;
    return fileName;
  }


  void checkRuleATN(Grammar g, String ruleName, String expecting) {
    DOTGenerator dot = new DOTGenerator(g);
    System.out.println(dot.getDOT(g.atn.ruleToStartState[g.getRule(ruleName).index]));

    Rule r = g.getRule(ruleName);
    ATNState startState = g.getATN().ruleToStartState[r.index];
    ATNPrinter serializer = new ATNPrinter(g, startState);
    String result = serializer.asString();

    assertEquals(expecting, result);
  }

  public void testActions(String templates,
                          String actionName,
                          String action,
                          String expected) throws org.antlr.runtime.RecognitionException {

    int lp = templates.indexOf('(');
    String name = templates.substring(0, lp);
    STGroup group = new STGroupString(templates);
    ST st = group.getInstanceOf(name);
    st.add(actionName, action);
    String grammar = st.render();
    ErrorQueue equeue = new ErrorQueue();
    Grammar g = new Grammar(grammar, equeue);
    if (g.ast != null && !g.ast.hasErrors) {
      SemanticPipeline sem = new SemanticPipeline(g);
      sem.process();

      ATNFactory factory = new ParserATNFactory(g);
      if (g.isLexer()) factory = new LexerATNFactory((LexerGrammar) g);
      g.atn = factory.createATN();

      AnalysisPipeline anal = new AnalysisPipeline(g);
      anal.process();

      CodeGenerator gen = new CodeGenerator(g);
      ST outputFileST = gen.generateParser(false);
      String output = outputFileST.render();
      String b = "#" + actionName + "#";
      int start = output.indexOf(b);
      String e = "#end-" + actionName + "#";
      int end = output.indexOf(e);
      String snippet = output.substring(start + b.length(), end);
      assertEquals(expected, snippet);
    }
    if (equeue.size() > 0) {
      System.err.println(equeue);
    }
  }

  protected void checkGrammarSemanticsWarning(ErrorQueue equeue, GrammarSemanticsMessage expectedMessage) {
    ANTLRMessage foundMsg = null;
    for (int i = 0; i < equeue.warnings.size(); i++) {
      ANTLRMessage m = equeue.warnings.get(i);
      if (m.getErrorType() == expectedMessage.getErrorType()) {
        foundMsg = m;
      }
    }
    assertThat(foundMsg)
      .as("no error; " + expectedMessage.getErrorType() + " expected")
      .isNotNull();
    assertThat(foundMsg instanceof GrammarSemanticsMessage)
      .as("error is not a GrammarSemanticsMessage")
      .isTrue();
    assertEquals(Arrays.toString(expectedMessage.getArgs()), Arrays.toString(foundMsg.getArgs()));
    if (equeue.size() != 1) {
      System.err.println(equeue);
    }
  }

  public static void writeFile(String dir, String fileName, String content) {
    try {
      Utils.writeFile(dir + "/" + fileName, content, "UTF-8");
    } catch (IOException ioe) {
      System.err.println("can't write file");
      ioe.printStackTrace(System.err);
    }
  }

  protected void mkdir(String dir) {
    File f = new File(dir);
    f.mkdirs();
  }

  protected void writeTestFile(String parserName,
                               String lexerName,
                               String parserStartRuleName,
                               boolean debug,
                               boolean profile) {
    ST outputFileST = new ST(
      """
        import org.antlr.v4.runtime.*;
        import org.antlr.v4.runtime.tree.*;
        import org.antlr.v4.runtime.atn.*;
        import java.io.File;
        import java.util.Arrays;
        
        public class Test {
            public static void main(String[] args) throws Exception {
                CharStream input = CharStreams.fromFile(new File(args[0]));
                <lexerName> lex = new <lexerName>(input);
                CommonTokenStream tokens = new CommonTokenStream(lex);
                <createParser>
        		 parser.setBuildParseTree(true);
        		 parser.getInterpreter().reportAmbiguities = true;
        		 <profile>
                ParserRuleContext tree = parser.<parserStartRuleName>();
        		 <if(profile)>System.out.println(Arrays.toString(profiler.getDecisionInfo()));<endif>
                ParseTreeWalker.DEFAULT.walk(new TreeShapeListener(), tree);
            }
        
        	static class TreeShapeListener implements ParseTreeListener {
        		@Override public void visitTerminal(TerminalNode node) { }
        		@Override public void visitErrorNode(ErrorNode node) { }
        		@Override public void exitEveryRule(ParserRuleContext ctx) { }
        
        		@Override
        		public void enterEveryRule(ParserRuleContext ctx) {
        			for (int i = 0; i \\< ctx.getChildCount(); i++) {
        				ParseTree parent = ctx.getChild(i).getParent();
        				if (!(parent instanceof RuleNode) || ((RuleNode)parent).getRuleContext() != ctx) {
        					throw new IllegalStateException("Invalid parse tree shape detected.");
        				}
        			}
        		}
        	}
        }"""
    );
    ST createParserST = new ST("        <parserName> parser = new <parserName>(tokens);\n");
    if (debug) {
      createParserST =
        new ST(
          """
                    <parserName> parser = new <parserName>(tokens);
                    parser.addErrorListener(new DiagnosticErrorListener());
            """);
    }
    if (profile) {
      outputFileST.add("profile",
        """
          ProfilingATNSimulator profiler = new ProfilingATNSimulator(parser);
          parser.setInterpreter(profiler);""");
    } else {
      outputFileST.add("profile", new ArrayList<>());
    }
    outputFileST.add("createParser", createParserST);
    outputFileST.add("parserName", parserName);
    outputFileST.add("lexerName", lexerName);
    outputFileST.add("parserStartRuleName", parserStartRuleName);
    writeFile(tmpdir, "Test.java", outputFileST.render());
  }

  protected void writeLexerTestFile(String lexerName, boolean showDFA) {
    ST outputFileST = new ST(
      "import java.io.File;\n" +
        "import org.antlr.v4.runtime.*;\n" +
        "\n" +
        "public class Test {\n" +
        "    public static void main(String[] args) throws Exception {\n" +
        "        CharStream input = CharStreams.fromFile(new File(args[0]));\n" +
        "        <lexerName> lex = new <lexerName>(input);\n" +
        "        CommonTokenStream tokens = new CommonTokenStream(lex);\n" +
        "        tokens.fill();\n" +
        "        for (Object t : tokens.getTokens()) System.out.println(t);\n" +
        (showDFA ? "System.out.print(lex.getInterpreter().getDFA(Lexer.DEFAULT_MODE).toLexerString());\n" : "") +
        "    }\n" +
        "}"
    );

    outputFileST.add("lexerName", lexerName);
    writeFile(tmpdir, "Test.java", outputFileST.render());
  }

  public void writeRecognizerAndCompile(String parserName, String lexerName,
                                        String parserStartRuleName,
                                        boolean debug,
                                        boolean profile) {
    if (parserName == null) {
      writeLexerTestFile(lexerName, debug);
    } else {
      writeTestFile(parserName,
        lexerName,
        parserStartRuleName,
        debug,
        profile);
    }

    compile("Test.java");
  }

  protected void eraseFiles() {
    if (tmpdir == null) {
      return;
    }

    File tmpdirF = new File(tmpdir);
    String[] files = tmpdirF.list();
    for (int i = 0; files != null && i < files.length; i++) {
      new File(tmpdir + "/" + files[i]).delete();
    }
  }

  protected void eraseTempDir() {
    File tmpdirF = new File(tmpdir);
    if (tmpdirF.exists()) {
      eraseFiles();
      tmpdirF.delete();
    }
  }

  public List<String> realElements(List<String> elements) {
    return elements.subList(Token.MIN_USER_TOKEN_TYPE, elements.size());
  }

  public void assertNotNullOrEmpty(String text) {
    assertThat(text).isNotNull();
    assertThat(text.isEmpty()).isFalse();
  }

  public static class IntTokenStream implements TokenStream {
    IntegerList types;
    int p = 0;

    public IntTokenStream(IntegerList types) {
      this.types = types;
    }

    @Override
    public void consume() {
      p++;
    }

    @Override
    public int LA(int i) {
      return LT(i).getType();
    }

    @Override
    public int mark() {
      return index();
    }

    @Override
    public int index() {
      return p;
    }

    @Override
    public void release(int marker) {
      seek(marker);
    }

    @Override
    public void seek(int index) {
      p = index;
    }

    @Override
    public int size() {
      return types.size();
    }

    @Override
    public String getSourceName() {
      return UNKNOWN_SOURCE_NAME;
    }

    @Override
    public Token LT(int i) {
      CommonToken t;
      int rawIndex = p + i - 1;
      if (rawIndex >= types.size()) t = new CommonToken(Token.EOF);
      else t = new CommonToken(types.get(rawIndex));
      t.setTokenIndex(rawIndex);
      return t;
    }

    @Override
    public Token get(int i) {
      return new org.antlr.v4.runtime.CommonToken(types.get(i));
    }

    @Override
    public TokenSource getTokenSource() {
      return null;
    }

    @NotNull
    @Override
    public String getText() {
      throw new UnsupportedOperationException("can't give strings");
    }

    @NotNull
    @Override
    public String getText(Interval interval) {
      throw new UnsupportedOperationException("can't give strings");
    }

    @NotNull
    @Override
    public String getText(RuleContext ctx) {
      throw new UnsupportedOperationException("can't give strings");
    }

    @NotNull
    @Override
    public String getText(Object start, Object stop) {
      throw new UnsupportedOperationException("can't give strings");
    }
  }

  /**
   * Return map sorted by key
   */
  public <K extends Comparable<? super K>, V> LinkedHashMap<K, V> sort(Map<K, V> data) {
    LinkedHashMap<K, V> dup = new LinkedHashMap<>();
    List<K> keys = new ArrayList<>(data.keySet());
    Collections.sort(keys);
    for (K k : keys) {
      dup.put(k, data.get(k));
    }
    return dup;
  }
}
