// Copyright 2015 quelltextlich e.U.
//
// This program and the accompanying materials are made available under the
// terms of the Eclipse Public License v1.0 which accompanies this
// distribution, and is available at http://www.eclipse.org/legal/epl-v10.html
//
package at.quelltextlich.jacoco.toolbox;

public class VersionToolTest extends ToolTestCase {
  public void testInputFoo() {
    final String[] args = new String[] { "--foo" };

    final ToolShim tool = new ToolShim(VersionTool.class);
    tool.run(args);

    tool.assertStderrContains("help");
    tool.assertExitStatus(1);
  }

  public void testNoInput() {
    final String[] args = new String[] {};

    final ToolShim tool = new ToolShim(VersionTool.class);
    tool.run(args);

    tool.assertStdoutContains("JaCoCo Toolbox");
    tool.assertStdoutContains("on");
    tool.assertStdoutContains("Supported");
    tool.assertExitStatus(0);
  }
}
