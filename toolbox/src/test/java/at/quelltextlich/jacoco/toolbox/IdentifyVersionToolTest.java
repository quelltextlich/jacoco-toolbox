// Copyright 2015 quelltextlich e.U.
//
// This program and the accompanying materials are made available under the
// terms of the Eclipse Public License v1.0 which accompanies this
// distribution, and is available at http://www.eclipse.org/legal/epl-v10.html
//
package at.quelltextlich.jacoco.toolbox;

import java.io.File;
import java.io.IOException;

public class IdentifyVersionToolTest extends ToolTestCase {
  public void testNoArguments() {
    final ToolShim tool = new ToolShim(IdentifyVersionTool.class);
    tool.run(new String[] {});

    tool.assertExitStatus(0);
  }

  public void testFooArgument() {
    final ToolShim tool = new ToolShim(IdentifyVersionTool.class);
    tool.run(new String[] { "--foo" });

    tool.assertStderrContains("help");
    tool.assertExitStatus(1);
  }

  public void testNoInput() throws IOException {
    final String[] args = new String[] {};

    final ToolShim tool = new ToolShim(IdentifyVersionTool.class);
    tool.run(args);

    tool.assertExitStatus(0);
  }

  public void testEmptyInput() throws IOException {
    final File input = getTemporaryFile();
    final File output = getTemporaryFile(false, "exec");
    final String[] args = new String[] { "--input", input.getAbsolutePath(),
        "--output", output.getAbsolutePath() };

    final ToolShim tool = new ToolShim(IdentifyVersionTool.class);
    tool.run(args);

    tool.assertExitStatus(1);
  }

  public void testInputMerged4103() throws IOException {
    final File input = new File(getClass().getResource(
        "/jacoco-merged-4103.exec").getPath());
    final String[] args = new String[] { "--input", input.getAbsolutePath() };

    final ToolShim tool = new ToolShim(IdentifyVersionTool.class);
    tool.run(args);

    tool.assertExitStatus(0);
    tool.assertStderrIsEmpty();
    tool.assertStdoutContains("jacoco-merged-4103.exec,0x1007,4103");
  }

  public void testInputMerged4102() throws IOException {
    final File input = new File(getClass().getResource(
        "/jacoco-merged-4102.exec").getPath());
    final String[] args = new String[] { "--input", input.getAbsolutePath() };

    final ToolShim tool = new ToolShim(IdentifyVersionTool.class);
    tool.run(args);

    tool.assertExitStatus(0);
    tool.assertStderrIsEmpty();
    tool.assertStdoutContains("jacoco-merged-4102.exec,0x1006,4102");
  }

  public void testInputMultipleVersions() throws IOException {
    final File input4102 = new File(getClass().getResource(
        "/jacoco-merged-4102.exec").getPath());
    final File input4103 = new File(getClass().getResource(
        "/jacoco-merged-4103.exec").getPath());
    final String[] args = new String[] { "--input",
        input4102.getAbsolutePath(), "--input", input4103.getAbsolutePath() };

    final ToolShim tool = new ToolShim(IdentifyVersionTool.class);
    tool.run(args);

    tool.assertExitStatus(0);
    tool.assertStderrIsEmpty();
    tool.assertStdoutContains("jacoco-merged-4102.exec,0x1006,4102");
    tool.assertStdoutContains("jacoco-merged-4103.exec,0x1007,4103");
  }
}