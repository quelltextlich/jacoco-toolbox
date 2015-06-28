// Copyright 2015 quelltextlich e.U.
//
// This program and the accompanying materials are made available under the
// terms of the Eclipse Public License v1.0 which accompanies this
// distribution, and is available at http://www.eclipse.org/legal/epl-v10.html
//
package at.quelltextlich.jacoco.toolbox;

import java.io.File;
import java.io.IOException;

public class InputToolTest extends ToolTestCase {
  public void testInputNonExisting() {
    final String[] args = new String[] { "--input", "foo.exec" };

    final ToolShim tool = new ToolShim(DummyInputTool.class);
    tool.run(args);

    tool.assertStderrContains("not exist");
    tool.assertExitStatus(1);
  }

  public void testInputNonReadable() throws IOException {
    final File file = getTemporaryFile();
    file.setReadable(false);

    final String[] args = new String[] { "--input", file.getAbsolutePath() };

    final ToolShim tool = new ToolShim(DummyInputTool.class);
    tool.run(args);

    tool.assertStderrContains("not readable");
    tool.assertExitStatus(1);
  }

  public void testInputSingle() throws IOException {
    final File file = getTemporaryFile();

    final String[] args = new String[] { "--input", file.getAbsolutePath() };

    final ToolShim tool = new ToolShim(DummyInputTool.class);
    tool.run(args);

    tool.assertExitStatus(0);
  }

  public void testInputMultiple() throws IOException {
    final File fileFoo = getTemporaryFile();
    final File fileBar = getTemporaryFile();

    final String[] args = new String[] { "--input", fileFoo.getAbsolutePath(),
        "--input", fileBar.getAbsolutePath() };

    final ToolShim tool = new ToolShim(DummyInputTool.class);
    tool.run(args);

    tool.assertExitStatus(0);
  }

  public void testInputMerged() throws IOException {
    final File file = new File(getClass().getResource("/jacoco-merged.exec")
        .getPath());
    final String[] args = new String[] { "--input", file.getAbsolutePath() };

    final ToolShim tool = new ToolShim(DummyInputTool.class);
    tool.run(args);

    tool.assertExitStatus(0);
  }

  public void testInputFoo() throws IOException {
    final File file = new File(getClass().getResource("/jacoco-foo.exec")
        .getPath());
    final String[] args = new String[] { "--input", file.getAbsolutePath() };

    final ToolShim tool = new ToolShim(DummyInputTool.class);
    tool.run(args);

    tool.assertExitStatus(0);
  }

  public void testInputBar() throws IOException {
    final File file = new File(getClass().getResource("/jacoco-bar.exec")
        .getPath());
    final String[] args = new String[] { "--input", file.getAbsolutePath() };

    final ToolShim tool = new ToolShim(DummyInputTool.class);
    tool.run(args);

    tool.assertExitStatus(0);
    tool.assertStderrIsEmpty();
    tool.assertStdoutIsEmpty();
  }

  public void testInputFooBar() throws IOException {
    final File fileFoo = new File(getClass().getResource("/jacoco-foo.exec")
        .getPath());
    final File fileBar = new File(getClass().getResource("/jacoco-bar.exec")
        .getPath());
    final String[] args = new String[] { "--input", fileFoo.getAbsolutePath(),
        "--input", fileBar.getAbsolutePath() };

    final ToolShim tool = new ToolShim(DummyInputTool.class);
    tool.run(args);

    tool.assertExitStatus(0);
  }

  public void testInputFooBarColon() throws IOException {
    final File fileFoo = new File(getClass().getResource("/jacoco-foo.exec")
        .getPath());
    final File fileBar = new File(getClass().getResource("/jacoco-bar.exec")
        .getPath());
    final String[] args = new String[] { "--input",
        fileFoo.getAbsolutePath() + ":" + fileBar.getAbsolutePath() };

    final ToolShim tool = new ToolShim(DummyInputTool.class);
    tool.run(args);

    tool.assertExitStatus(0);
  }

  public void testInputFooBarColonWEmpty() throws IOException {
    final File fileFoo = new File(getClass().getResource("/jacoco-foo.exec")
        .getPath());
    final File fileBar = new File(getClass().getResource("/jacoco-bar.exec")
        .getPath());
    final String[] args = new String[] {
        "--input",
        ":" + fileFoo.getAbsolutePath() + ":" + fileBar.getAbsolutePath()
            + "::" };

    final ToolShim tool = new ToolShim(DummyInputTool.class);
    tool.run(args);

    tool.assertExitStatus(0);
  }

  public void testInputVerbose() throws IOException {
    final File file = new File(getClass().getResource("/jacoco-bar.exec")
        .getPath());
    final String[] args = new String[] { "--input", file.getAbsolutePath(),
        "--verbose" };

    final ToolShim tool = new ToolShim(DummyInputTool.class);
    tool.run(args);

    tool.assertExitStatus(0);
    tool.assertStderrIsEmpty();
    tool.assertStdoutContains("JaCoCo Toolbox");
    tool.assertStdoutContains("on");
    tool.assertStdoutContains("Supported");
    tool.assertStdoutContains("input");
  }
}
