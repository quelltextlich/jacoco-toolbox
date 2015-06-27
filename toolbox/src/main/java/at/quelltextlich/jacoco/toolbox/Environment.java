// Copyright 2015 quelltextlich e.U.
//
// This program and the accompanying materials are made available under the
// terms of the Eclipse Public License v1.0 which accompanies this
// distribution, and is available at http://www.eclipse.org/legal/epl-v10.html
//
package at.quelltextlich.jacoco.toolbox;

import java.io.PrintStream;

public interface Environment {
  /**
   * Gets the stderr for this environment
   *
   * @return stderr for this environment
   */
  public PrintStream getStderr();

  /**
   * Gets the stdout for this environment
   *
   * @return stdout for this environment
   */
  public PrintStream getStdout();

  /**
   * Exits the tool.
   *
   * @param exitCode
   *          Code to exit with. 0 for success, non-zero for failure.
   */
  public void exit(final int exitCode);
}
