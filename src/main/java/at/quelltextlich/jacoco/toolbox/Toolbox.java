// Copyright 2015 quelltextlich e.U.
//
// This program and the accompanying materials are made available under the
// terms of the Eclipse Public License v1.0 which accompanies this
// distribution, and is available at http://www.eclipse.org/legal/epl-v10.html
//
package at.quelltextlich.jacoco.toolbox;

import java.io.PrintStream;

import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;
import org.kohsuke.args4j.OptionHandlerFilter;

/**
 * Toolbox for working with Jacoco exec files
 */
public class Toolbox {

  protected PrintStream stderr = System.err;

  @Option(name = "--help", aliases = { "-help", "-h", "-?" }, help = true, usage = "print this help screen")
  private boolean help;

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
      exitWithException("Failed to parse args", e);
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
   * Exits the toolbox with an exception
   *
   * @param reason
   *          Reason for exit (may be null).
   * @param e
   *          Exception to exit with.
   */
  private void exitWithException(String reason, Exception e) {
    if (reason != null) {
      stderr.println(reason);
    }
    e.printStackTrace(stderr);
    exit(1);
  }

  /**
   * Runs the toolbox' logic for given parameters
   *
   * @param args
   *          Arguments to the toolbox.
   */
  public void run(String[] args) {
    parseArgs(args);

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