// Copyright 2015 quelltextlich e.U.
//
// This program and the accompanying materials are made available under the
// terms of the Eclipse Public License v1.0 which accompanies this
// distribution, and is available at http://www.eclipse.org/legal/epl-v10.html
//
package at.quelltextlich.jacoco.toolbox;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.LinkedList;
import java.util.List;

import org.jacoco.core.analysis.Analyzer;
import org.jacoco.core.analysis.CoverageBuilder;
import org.jacoco.core.analysis.IBundleCoverage;
import org.jacoco.core.tools.ExecFileLoader;
import org.jacoco.report.DirectorySourceFileLocator;
import org.jacoco.report.FileMultiReportOutput;
import org.jacoco.report.IReportVisitor;
import org.jacoco.report.MultiSourceFileLocator;
import org.jacoco.report.csv.CSVFormatter;
import org.jacoco.report.html.HTMLFormatter;
import org.jacoco.report.xml.XMLFormatter;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;
import org.kohsuke.args4j.OptionHandlerFilter;

/**
 * Toolbox for working with Jacoco exec files
 */
public class Toolbox {
  final private static int TAB_WIDTH = 2;

  protected PrintStream stderr = System.err;
  private ExecFileLoader loader = new ExecFileLoader();

  @Option(name = "--help", aliases = { "-help", "-h", "-?" }, help = true, usage = "print this help screen")
  private boolean help;

  private List<File> inputs = new LinkedList<File>();

  @Option(name = "--input", usage = "Adds an input to the toolbox")
  void addInput(final String inputStr) {
    File input = new File(inputStr);
    if (!input.exists()) {
      exit("The file '" + input + "' does not exist");
    }
    if (!input.canRead()) {
      exit("The file '" + input + "' is not readable");
    }
    if (!inputs.add(input)) {
      exit("Could not add '" + input + "' to inputs");
    }
  }

  private List<File> analyzeFors = new LinkedList<File>();

  @Option(name = "--analyze-for", usage = "Add a file to analyze for. This can be a plain class file, a jar file, or a directory.")
  void addAnalyze(final String analyzeFor) {
    File file = new File(analyzeFor);
    if (!file.exists()) {
      exit("The file '" + file + "' does not exist");
    }
    if (!analyzeFors.add(file)) {
      exit("Could not add '" + file + "' to analyzes");
    }
  }

  private List<File> sources = new LinkedList<File>();

  @Option(name = "--source", usage = "Add a directory to search sources in.")
  void addSource(final String source) {
    File file = new File(source);
    if (!file.exists()) {
      exit("The file '" + file + "' does not exist");
    }
    if (!file.isDirectory()) {
      exit("The file '" + file + "' is not a directory");
    }
    if (!sources.add(file)) {
      exit("Could not add '" + file + "' to sources");
    }
  }

  private List<File> outputsCsv = new LinkedList<File>();

  @Option(name = "--output-csv", usage = "Adds an output in CSV format.")
  void addOutputCsv(final String outputStr) {
    File output = new File(outputStr);
    if (!outputsCsv.add(output)) {
      exit("Could not add '" + output + "' to CSV outputs");
    }
  }

  private List<File> outputsExec = new LinkedList<File>();

  @Option(name = "--output-exec", usage = "Adds an output in Jacoco's exec format.")
  void addOutputExec(final String outputStr) {
    File output = new File(outputStr);
    if (!outputsExec.add(output)) {
      exit("Could not add '" + output + "' to exec outputs");
    }
  }

  private List<File> outputsHtml = new LinkedList<File>();

  @Option(name = "--output-html", usage = "Adds an output in HTML format.")
  void addOutputHtml(final String outputStr) {
    File output = new File(outputStr);
    if (!outputsHtml.add(output)) {
      exit("Could not add '" + output + "' to HTML outputs");
    }
  }

  private List<File> outputsXml = new LinkedList<File>();

  @Option(name = "--output-xml", usage = "Adds an output in XML format.")
  void addOutputXml(final String outputStr) {
    File output = new File(outputStr);
    if (!outputsXml.add(output)) {
      exit("Could not add '" + output + "' to XML outputs");
    }
  }

  @Option(name = "--title", usage = "Title used for reports")
  private String title = "bundle";

  private MultiSourceFileLocator locator = new MultiSourceFileLocator(TAB_WIDTH);
  private IBundleCoverage bundle;

  /**
   * Parses the toolbox' arguments
   *
   * @param args
   *          Arguments to parse.
   */
  private void parseArgs(String[] args) {
    CmdLineParser parser = new CmdLineParser(this);
    try {
      parser.parseArgument(args);
    } catch (CmdLineException e) {
      parser.printUsage(stderr);
      exit("Failed to parse args", e);
    }

    if (help) {
      // User asked for help screen
      parser.printUsage(stderr);
      stderr.println();
      stderr.print(parser.printExample(OptionHandlerFilter.REQUIRED));
      exit(0);
    }
  }

  /**
   * Exits the toolbox.
   *
   * @param exitCode
   *          Code to exit with. 0 for success, non-zero for failure.
   */
  protected void exit(int exitCode) {
    System.exit(exitCode);
  }

  /**
   * Exits the toolbox with a reason
   *
   * @param reason
   *          Reason for exit (may be null).
   * @param e
   *          Exception to exit with.
   */
  private void exit(String reason) {
    exit(reason, null);
  }

  /**
   * Exits the toolbox with a reason and exception
   *
   * @param reason
   *          Reason for exit (may be null).
   * @param e
   *          Exception to exit with.
   */
  private void exit(String reason, Exception e) {
    if (reason != null) {
      stderr.println(reason);
    }
    if (e != null) {
      e.printStackTrace(stderr);
    }
    exit(1);
  }

  /**
   * Loads the JaCoCo inputs
   */
  public void loadInputs() {
    for (File input : inputs) {
      try {
        loader.load(input);
      } catch (IOException e) {
        exit("Error loading '" + input + "'", e);
      }
    }
  }

  /**
   * Builds a bundle for all loaded inputs
   */
  private void buildBundle() {
    CoverageBuilder builder = new CoverageBuilder();
    Analyzer analyzer = new Analyzer(loader.getExecutionDataStore(), builder);
    for (File file : analyzeFors) {
      try {
        analyzer.analyzeAll(file);
      } catch (IOException e) {
        exit("Could not analyze for '" + file + "'", e);
      }
    }
    bundle = builder.getBundle(title);
  }

  /**
   * Configure the locator to find source files
   */
  private void configureLocator() {
    for (File file : sources) {
      locator.add(new DirectorySourceFileLocator(file, "utf-8", TAB_WIDTH));
    }
  }

  /**
   * Lets a visitor visit the bundle
   *
   * @param visitor
   *          Visitor to visit the bundle
   * @throws IOException
   */
  private void visit(IReportVisitor visitor) throws IOException {
    visitor.visitInfo(loader.getSessionInfoStore().getInfos(), loader
        .getExecutionDataStore().getContents());
    visitor.visitBundle(bundle, locator);
    visitor.visitEnd();
  }

  /**
   * Outputs the CSV files
   */
  public void outputCsvs() {
    CSVFormatter formatter = new CSVFormatter();
    for (File file : outputsCsv) {
      OutputStream stream;
      try {
        stream = new FileOutputStream(file);
        try {
          IReportVisitor visitor = formatter.createVisitor(stream);
          visit(visitor);
        } catch (IOException e) {
          exit("Failed to write CSV to '" + file + "'");
        } finally {
          try {
            stream.close();
          } catch (IOException e) {
            exit("Cannot close file '" + file + "'", e);
          }
        }
      } catch (FileNotFoundException e) {
        exit("Cannot write to '" + file + "'", e);
      }
    }
  }

  /**
   * Outputs the Exec files
   */
  public void outputExecs() {
    for (File file : outputsExec) {
      try {
        loader.save(file, false);
      } catch (IOException e) {
        exit("Failed to write exec to '" + file + "'", e);
      }
    }
  }

  /**
   * Outputs the HTML files
   */
  public void outputHtmls() {
    HTMLFormatter formatter = new HTMLFormatter();
    for (File file : outputsHtml) {
      FileMultiReportOutput output;
      output = new FileMultiReportOutput(file);
      try {
        IReportVisitor visitor = formatter.createVisitor(output);
        visit(visitor);
      } catch (IOException e) {
        exit("Failed to write HTML to '" + file + "'");
      }
    }
  }

  /**
   * Outputs the XML files
   */
  public void outputXmls() {
    XMLFormatter formatter = new XMLFormatter();
    for (File file : outputsXml) {
      OutputStream stream;
      try {
        stream = new FileOutputStream(file);
        try {
          IReportVisitor visitor = formatter.createVisitor(stream);
          visit(visitor);
        } catch (IOException e) {
          exit("Failed to write XML to '" + file + "'");
        } finally {
          try {
            stream.close();
          } catch (IOException e) {
            exit("Cannot close file '" + file + "'", e);
          }
        }
      } catch (FileNotFoundException e) {
        exit("Cannot write to '" + file + "'", e);
      }
    }
  }

  /**
   * Runs the toolbox' logic for given parameters
   *
   * @param args
   *          Arguments to the toolbox.
   */
  public void run(String[] args) {
    parseArgs(args);

    loadInputs();

    outputExecs();

    buildBundle();
    configureLocator();

    outputCsvs();
    outputHtmls();
    outputXmls();

    exit(0);
  }

  /**
   * Static entry point for the toolbox
   *
   * @param args
   *          Arguments to the toolbox.
   */
  public static void main(String[] args) {
    Toolbox toolbox = new Toolbox();
    toolbox.run(args);
  }
}
