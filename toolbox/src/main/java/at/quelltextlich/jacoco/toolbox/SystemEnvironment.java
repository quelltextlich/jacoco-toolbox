// Copyright 2015 quelltextlich e.U.
//
// This program and the accompanying materials are made available under the
// terms of the Eclipse Public License v1.0 which accompanies this
// distribution, and is available at http://www.eclipse.org/legal/epl-v10.html
//
package at.quelltextlich.jacoco.toolbox;

import java.io.PrintStream;

public class SystemEnvironment implements Environment {

  @Override
  public PrintStream getStderr() {
    return System.err;
  }

  @Override
  public void exit(final int exitCode) {
    System.exit(exitCode);
  }

}
