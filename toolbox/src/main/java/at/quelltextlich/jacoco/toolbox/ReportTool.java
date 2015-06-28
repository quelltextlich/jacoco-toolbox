// Copyright 2015 quelltextlich e.U.
//
// This program and the accompanying materials are made available under the
// terms of the Eclipse Public License v1.0 which accompanies this
// distribution, and is available at http://www.eclipse.org/legal/epl-v10.html
//
package at.quelltextlich.jacoco.toolbox;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import org.jacoco.core.analysis.Analyzer;
import org.jacoco.core.analysis.CoverageBuilder;
import org.jacoco.core.analysis.IBundleCoverage;
import org.jacoco.report.DirectorySourceFileLocator;
import org.jacoco.report.IReportVisitor;
import org.jacoco.report.MultiSourceFileLocator;
import org.kohsuke.args4j.Option;

/**
 * Tool to generate reports
 */
public abstract class ReportTool extends InputTool {
  final private static int TAB_WIDTH = 2;

  private final List<File> analyzeFors = new LinkedList<File>();

  @Option(name = "--analyze-for", usage = "Add a file to analyze for. This "
      + "can be a plain class file, a jar file, or a directory. Multiple "
      + "paths can be provided in one string by concatenating them using "
      + "colons (':').")
  void addAnalyze(final String analyzeFor) {
    for (final String singleAnalyzeFor : analyzeFor.split(":")) {
      final File file = new File(singleAnalyzeFor);
      if (!file.exists()) {
        exit("The file '" + file + "' does not exist");
      }
      if (!analyzeFors.add(file)) {
        exit("Could not add '" + file + "' to analyzes");
      }
    }
  }

  private final List<File> sources = new LinkedList<File>();

  @Option(name = "--source", usage = "Add a directory to search sources in. "
      + "Multiple paths can be provided in one string by concatenating them "
      + "using colons (':').")
  void addSource(final String source) {
    for (final String singleSource : source.split(":")) {
      final File file = new File(singleSource);
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
  }

  @Option(name = "--output", usage = "Place to write the report to.", required = true)
  public File output;

  @Option(name = "--title", usage = "Title used for reports")
  private String title = "Code Coverage Analysis";

  private final MultiSourceFileLocator locator = new MultiSourceFileLocator(
      TAB_WIDTH);
  private IBundleCoverage bundle;

  /**
   * Builds a bundle for all loaded inputs
   */
  private void buildBundle() {
    final CoverageBuilder builder = new CoverageBuilder();
    final Analyzer analyzer = new Analyzer(loader.getExecutionDataStore(),
        builder);
    for (final File file : analyzeFors) {
      try {
        analyzer.analyzeAll(file);
      } catch (final IOException e) {
        exit("Could not analyze for '" + file + "'", e);
      }
    }
    bundle = builder.getBundle(title);
  }

  /**
   * Configure the locator to find source files
   */
  private void configureLocator() {
    for (final File file : sources) {
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
  protected void visit(final IReportVisitor visitor) throws IOException {
    visitor.visitInfo(loader.getSessionInfoStore().getInfos(), loader
        .getExecutionDataStore().getContents());
    visitor.visitBundle(bundle, locator);
    visitor.visitEnd();
  }

  /**
   * Write the report to the output
   */
  public abstract void writeReport();

  @Override
  public void run(final String[] args) {
    super.run(args);

    buildBundle();
    configureLocator();

    writeReport();
  }
}
