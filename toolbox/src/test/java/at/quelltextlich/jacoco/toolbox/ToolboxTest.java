// Copyright 2015 quelltextlich e.U.
//
// This program and the accompanying materials are made available under the
// terms of the Eclipse Public License v1.0 which accompanies this
// distribution, and is available at http://www.eclipse.org/legal/epl-v10.html
//
package at.quelltextlich.jacoco.toolbox;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;

import junit.framework.TestCase;

public class ToolboxTest extends TestCase {
  private File getTemporaryFile() throws IOException {
    return getTemporaryFile(true);
  }

  private File getTemporaryFile(boolean create) throws IOException {
    File file = File.createTempFile("jacoco-toolbox-test", ".exec");
    file.deleteOnExit();
    if (!create) {
      assertTrue("Could not delete temporary file '" + file + "'",
          file.delete());
    }
    return file;
  }

  public void testUnknownArgument() {
    String[] args = new String[] { "--foo" };

    ToolboxShim toolbox = new ToolboxShim();
    toolbox.run(args);

    toolbox.assertStderrContains("--help");
    toolbox.assertExitStatus(1);
  }

  public void testHelp() {
    String[] args = new String[] { "--help" };

    ToolboxShim toolbox = new ToolboxShim();
    toolbox.run(args);

    toolbox.assertStderrContains("--help");
    toolbox.assertExitStatus(0);
  }

  public void testInputNonExisting() {
    String[] args = new String[] { "--input", "foo.exec" };

    ToolboxShim toolbox = new ToolboxShim();
    toolbox.run(args);

    toolbox.assertStderrContains("not exist");
    toolbox.assertExitStatus(1);
  }

  public void testInputNonReadable() throws IOException {
    File file = getTemporaryFile();
    file.setReadable(false);

    String[] args = new String[] { "--input", file.getAbsolutePath() };

    ToolboxShim toolbox = new ToolboxShim();
    toolbox.run(args);

    toolbox.assertStderrContains("not readable");
    toolbox.assertExitStatus(1);
  }

  public void testInputSingle() throws IOException {
    File file = getTemporaryFile();

    String[] args = new String[] { "--input", file.getAbsolutePath() };

    ToolboxShim toolbox = new ToolboxShim();
    toolbox.run(args);

    toolbox.assertExitStatus(0);
  }

  public void testInputMultiple() throws IOException {
    File fileFoo = getTemporaryFile();
    File fileBar = getTemporaryFile();

    String[] args = new String[] { "--input", fileFoo.getAbsolutePath(),
        "--input", fileBar.getAbsolutePath() };

    ToolboxShim toolbox = new ToolboxShim();
    toolbox.run(args);

    toolbox.assertExitStatus(0);
  }

  public void testInputMerged() throws IOException {
    File file = new File(
        getClass().getResource("/jacoco-merged.exec").getPath());
    String[] args = new String[] { "--input", file.getAbsolutePath() };

    ToolboxShim toolbox = new ToolboxShim();
    toolbox.run(args);

    toolbox.assertExitStatus(0);
  }

  public void testInputFoo() throws IOException {
    File file = new File(getClass().getResource("/jacoco-foo.exec").getPath());
    String[] args = new String[] { "--input", file.getAbsolutePath() };

    ToolboxShim toolbox = new ToolboxShim();
    toolbox.run(args);

    toolbox.assertExitStatus(0);
  }

  public void testInputBar() throws IOException {
    File file = new File(getClass().getResource("/jacoco-bar.exec").getPath());
    String[] args = new String[] { "--input", file.getAbsolutePath() };

    ToolboxShim toolbox = new ToolboxShim();
    toolbox.run(args);

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
