// Copyright 2015 quelltextlich e.U.
//
// This program and the accompanying materials are made available under the
// terms of the Eclipse Public License v1.0 which accompanies this
// distribution, and is available at http://www.eclipse.org/legal/epl-v10.html
//
package at.quelltextlich.jacoco.toolbox;

import java.io.PrintStream;
import java.util.Arrays;

import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;
import org.kohsuke.args4j.OptionHandlerFilter;

/**
 * Single tool for the JaCoCo toolbox
 */
public abstract class Tool {
  private Environment environment;
  protected PrintStream stderr;
  protected PrintStream stdout;

  @Option(name = "--help", aliases = { "-help", "-h", "-?" }, help = true, usage = "print this help screen")
  private boolean help;

  @Option(name = "--verbose", usage = "Turn on verbose mode")
  protected boolean verbose;

  public Tool() {
    setEnvironment(new SystemEnvironment());
  }

  /**
   * Sets the environment to be used by this tool
   *
   * @param environment
   *          environment to be used by this tool
   */
  void setEnvironment(final Environment environment) {
    this.environment = environment;
    stderr = this.environment.getStderr();
    stdout = this.environment.getStdout();
  }

  /**
   * Parses the tool' arguments
   *
   * @param args
   *          Arguments to parse.
   */
  protected void parseArgs(final String[] args) {
    final CmdLineParser parser = new CmdLineParser(this);
    try {
      parser.parseArgument(args);
    } catch (final CmdLineException e) {
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
   * Exits the tool.
   *
   * @param exitCode
   *          Code to exit with. 0 for success, non-zero for failure.
   */
  protected void exit(final int exitCode) {
    environment.exit(exitCode);
  }

  /**
   * Exits the tool with a reason
   *
   * @param reason
   *          Reason for exit (may be null).
   * @param e
   *          Exception to exit with.
   */
  protected void exit(final String reason) {
    exit(reason, null);
  }

  /**
   * Exits the tool with a reason and exception
   *
   * @param reason
   *          Reason for exit (may be null).
   * @param e
   *          Exception to exit with.
   */
  protected void exit(final String reason, final Exception e) {
    if (reason != null) {
      stderr.println(reason);
    }
    if (e != null) {
      e.printStackTrace(stderr);
    }
    exit(1);
  }

  /**
   * Runs the tool's logic for given parameters
   *
   * @param args
   *          Arguments to the toolbox.
   */
  public void run(final String[] args) {
    parseArgs(args);

    if (verbose && !(this instanceof VersionTool)) {
      final Tool versionTool = new VersionTool();
      versionTool.setEnvironment(environment);
      versionTool.run(new String[] {});
      stdout.println("Running " + this.getClass() + " with arguments "
          + Arrays.asList(args));
    }
  }
}
