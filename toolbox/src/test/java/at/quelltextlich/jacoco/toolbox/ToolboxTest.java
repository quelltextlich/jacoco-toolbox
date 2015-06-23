// Copyright 2015 quelltextlich e.U.
//
// This program and the accompanying materials are made available under the
// terms of the Eclipse Public License v1.0 which accompanies this
// distribution, and is available at http://www.eclipse.org/legal/epl-v10.html
//
package at.quelltextlich.jacoco.toolbox;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import junit.framework.TestCase;

public class ToolboxTest extends TestCase {

  public void testUnknownArgument() {
    String[] args = new String[] { "--foo" };

    ToolboxShim toolbox = new ToolboxShim();
    toolbox.run(args);

    toolbox.assertStderrContains("--help");
    toolbox.assertExitStatus(1);
  }

  public void testHelpArgument() {
    String[] args = new String[] { "--help" };

    ToolboxShim toolbox = new ToolboxShim();
    toolbox.run(args);

    toolbox.assertStderrContains("--help");
    toolbox.assertExitStatus(0);
  }

  /**
   * Exception thrown by the ToolboxShim when simulating System.exit
   */
  class ShimExitException extends RuntimeException {
    private static final long serialVersionUID = 1L;
  }

  /**
   * Shim for Toolbox to make relevant aspects testable
   */
  class ToolboxShim extends Toolbox {
    private Integer exitStatus = null;
    private ByteArrayOutputStream stderrStream;

    public ToolboxShim() {
      stderrStream = new ByteArrayOutputStream();
      stderr = new PrintStream(stderrStream);
    }

    @Override
    protected void exit(int status) {
      assertNull("exit status has already been set!", exitStatus);
      exitStatus = status;
      throw new ShimExitException();
    }

    @Override
    public void run(String[] args) {
      try {
        super.run(args);
      } catch (ShimExitException e) {
      }
    }

    public void assertExitStatus(int status) {
      assertEquals("Wrong shim exit status", new Integer(status), exitStatus);
    }

    public void assertStderrIsEmpty() {
      stderr.flush();
      String stderrStr = stderrStream.toString();
      assertTrue("Stderr is '" + stderrStr + "', but should be empty",
          stderrStr.isEmpty());
    }

    public void assertStderrContains(String str) {
      stderr.flush();
      String stderrStr = stderrStream.toString();
      assertTrue("Stderr is '" + stderrStr + "', but should contain '" + str
          + "'", stderrStr.contains(str));
    }
  }
}
