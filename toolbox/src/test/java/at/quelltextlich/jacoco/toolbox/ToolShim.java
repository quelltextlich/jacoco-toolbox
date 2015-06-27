package at.quelltextlich.jacoco.toolbox;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import junit.framework.TestCase;

/**
 * Shim for Tool to make relevant aspects testable
 */
class ToolShim extends Tool implements Environment {
  private Integer exitStatus = null;
  private final PrintStream stderrEnv;
  private final ByteArrayOutputStream stderrStream;
  private Tool tool;

  public <T extends Tool> ToolShim(final Class<T> toolClass) {
    stderrStream = new ByteArrayOutputStream();
    stderrEnv = new PrintStream(stderrStream);
    try {
      tool = toolClass.newInstance();
    } catch (final Exception e) {
      TestCase.fail("Could not generate ToolShim for " + toolClass.getName()
          + "\n" + e);
    }
    tool.setEnvironment(this);
  }

  @Override
  public PrintStream getStderr() {
    return stderrEnv;
  }

  @Override
  public void exit(final int status) {
    TestCase.assertNull("exit status has already been set!", exitStatus);
    exitStatus = status;
    throw new ToolShimExitException();
  }

  @Override
  public void run(final String[] args) {
    try {
      tool.run(args);
      tool.exit(0);
    } catch (final ToolShimExitException e) {
    }
  }

  public void assertExitStatus(final int status) {
    TestCase.assertEquals("Wrong shim exit status", new Integer(status),
        exitStatus);
  }

  public void assertStderrIsEmpty() {
    stderr.flush();
    final String stderrStr = stderrStream.toString();
    TestCase.assertTrue("Stderr is '" + stderrStr + "', but should be empty",
        stderrStr.isEmpty());
  }

  public void assertStderrContains(final String str) {
    stderr.flush();
    final String stderrStr = stderrStream.toString();
    TestCase.assertTrue("Stderr is '" + stderrStr + "', but should contain '"
        + str + "'", stderrStr.contains(str));
  }

  /**
   * Exception thrown by the ToolboxShim when simulating System.exit
   */
  class ToolShimExitException extends RuntimeException {
    private static final long serialVersionUID = 1L;
  }
}