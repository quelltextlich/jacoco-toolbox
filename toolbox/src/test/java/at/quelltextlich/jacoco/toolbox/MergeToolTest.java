// Copyright 2015 quelltextlich e.U.
//
// This program and the accompanying materials are made available under the
// terms of the Eclipse Public License v1.0 which accompanies this
// distribution, and is available at http://www.eclipse.org/legal/epl-v10.html
//
package at.quelltextlich.jacoco.toolbox;

import java.io.File;
import java.io.IOException;

public class MergeToolTest extends ToolTestCase {
  final public static String[] FOO_CSV_ALL_MISSING = new String[] {
    ReportCsvToolTest.CSV_HEADER,
    "Code Coverage Analysis,at.quelltextlich.jacoco.toolbox,Bar,9,0,0,0,5,0,4,0,4,0",
    "Code Coverage Analysis,at.quelltextlich.jacoco.toolbox,Baz,4,0,0,0,2,0,2,0,2,0",
  "Code Coverage Analysis,at.quelltextlich.jacoco.toolbox,Foo,9,0,0,0,5,0,4,0,4,0" };

  public void testNoArguments() {
    final ToolShim tool = new ToolShim(MergeTool.class);
    tool.run(new String[] {});

    tool.assertStderrContains("help");
    tool.assertExitStatus(1);
  }

  public void testFooArgument() {
    final ToolShim tool = new ToolShim(MergeTool.class);
    tool.run(new String[] { "--foo" });

    tool.assertStderrContains("help");
    tool.assertExitStatus(1);
  }

  public void testNoInput() throws IOException {
    final File output = getTemporaryFile(false, "exec");
    final String[] args = new String[] { "--output", output.getAbsolutePath() };

    final ToolShim tool = new ToolShim(MergeTool.class);
    tool.run(args);

    tool.assertExitStatus(0);

    assertFileEqualsAsCsv(FOO_CSV_ALL_MISSING, output);
  }

  public void testEmptyInput() throws IOException {
    final File input = getTemporaryFile();
    final File output = getTemporaryFile(false, "exec");
    final String[] args = new String[] { "--input", input.getAbsolutePath(),
        "--output", output.getAbsolutePath() };

    final ToolShim tool = new ToolShim(MergeTool.class);
    tool.run(args);

    tool.assertExitStatus(0);

    assertFileEqualsAsCsv(FOO_CSV_ALL_MISSING, output);
  }

  public void testInputFoo() throws IOException {
    final File input = new File(getClass().getResource("/jacoco-foo.exec")
        .getPath());
    final File output = getTemporaryFile(false, "csv");
    final String[] args = new String[] { "--input", input.getAbsolutePath(),
        "--output", output.getAbsolutePath() };

    final ToolShim tool = new ToolShim(MergeTool.class);
    tool.run(args);

    tool.assertExitStatus(0);

    assertFileEqualsAsCsv(ReportCsvToolTest.FOO_CSV, output);
  }

  public void testInputFooBar() throws IOException {
    final File inputFoo = new File(getClass().getResource("/jacoco-foo.exec")
        .getPath());
    final File inputBar = new File(getClass().getResource("/jacoco-bar.exec")
        .getPath());
    final File output = getTemporaryFile(false, "csv");
    final String[] args = new String[] { "--input", inputFoo.getAbsolutePath(),
        "--input", inputBar.getAbsolutePath(), "--output",
        output.getAbsolutePath() };

    final ToolShim tool = new ToolShim(MergeTool.class);
    tool.run(args);

    tool.assertExitStatus(0);

    assertFileEqualsAsCsv(ReportCsvToolTest.MERGED_CSV, output);
  }

  public void assertFileEqualsAsCsv(final String[] expected, final File actual)
      throws IOException {
    assertTrue("Exec file does not exist", actual.exists());

    final File output = getTemporaryFile(false, "csv");
    final File inputJar = new File(getClass().getResource(
        "/TestDataGroupFoo.jar").getPath());
    final String[] args = new String[] { "--input", actual.getAbsolutePath(),
        "--analyze-for", inputJar.getAbsolutePath(), "--output",
        output.getAbsolutePath() };
    final ToolShim tool = new ToolShim(ReportCsvTool.class);
    tool.run(args);

    tool.assertExitStatus(0);

    assertFileEquals("Generated file did not match expected as CSV", expected,
        output);
  }
}
