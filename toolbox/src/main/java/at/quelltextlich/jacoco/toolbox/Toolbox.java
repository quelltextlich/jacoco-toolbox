// Copyright 2015 quelltextlich e.U.
//
// This program and the accompanying materials are made available under the
// terms of the Eclipse Public License v1.0 which accompanies this
// distribution, and is available at http://www.eclipse.org/legal/epl-v10.html
//
package at.quelltextlich.jacoco.toolbox;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.LinkedList;
import java.util.List;

import org.jacoco.core.tools.ExecFileLoader;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;
import org.kohsuke.args4j.OptionHandlerFilter;

/**
 * Toolbox for working with Jacoco exec files
 */
public class Toolbox {

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
   * Runs the toolbox' logic for given parameters
   *
   * @param args
   *          Arguments to the toolbox.
   */
  public void run(String[] args) {
    parseArgs(args);

    loadInputs();

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
