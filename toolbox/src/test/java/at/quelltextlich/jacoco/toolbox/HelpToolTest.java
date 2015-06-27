// Copyright 2015 quelltextlich e.U.
//
// This program and the accompanying materials are made available under the
// terms of the Eclipse Public License v1.0 which accompanies this
// distribution, and is available at http://www.eclipse.org/legal/epl-v10.html
//
package at.quelltextlich.jacoco.toolbox;

public class HelpToolTest extends ToolTestCase {
  public void testNoArguments() {
    final ToolShim tool = new ToolShim(HelpTool.class);
    tool.run(new String[] {});

    tool.assertStderrContains("help");
    tool.assertStderrContains("report-csv");
    tool.assertExitStatus(0);
  }

  public void testHelpArgument() {
    final ToolShim tool = new ToolShim(HelpTool.class);
    tool.run(new String[] { "help" });

    tool.assertStderrContains("help");
    tool.assertStderrContains("report-csv");
    tool.assertExitStatus(0);
  }

  public void testFooArgument() {
    final ToolShim tool = new ToolShim(HelpTool.class);
    tool.run(new String[] { "--foo" });

    tool.assertStderrContains("help");
    tool.assertStderrContains("report-csv");
    tool.assertExitStatus(0);
  }
}
