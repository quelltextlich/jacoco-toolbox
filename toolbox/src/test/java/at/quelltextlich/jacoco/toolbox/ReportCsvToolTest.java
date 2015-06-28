// Copyright 2015 quelltextlich e.U.
//
// This program and the accompanying materials are made available under the
// terms of the Eclipse Public License v1.0 which accompanies this
// distribution, and is available at http://www.eclipse.org/legal/epl-v10.html
//
package at.quelltextlich.jacoco.toolbox;

import java.io.File;
import java.io.IOException;

public class ReportCsvToolTest extends ToolTestCase {
  final public static String CSV_HEADER = "GROUP,PACKAGE,CLASS,"
      + "INSTRUCTION_MISSED,INSTRUCTION_COVERED,BRANCH_MISSED,BRANCH_COVERED,"
      + "LINE_MISSED,LINE_COVERED,COMPLEXITY_MISSED,COMPLEXITY_COVERED,"
      + "METHOD_MISSED,METHOD_COVERED";

  final public static String[] FOO_CSV = new String[] {
    CSV_HEADER,
    "Code Coverage Analysis,at.quelltextlich.jacoco.toolbox,Bar,9,0,0,0,5,0,4,0,4,0",
    "Code Coverage Analysis,at.quelltextlich.jacoco.toolbox,Baz,4,0,0,0,2,0,2,0,2,0",
  "Code Coverage Analysis,at.quelltextlich.jacoco.toolbox,Foo,1,8,0,0,1,4,1,3,1,3" };

  final public static String[] MERGED_CSV = new String[] {
    CSV_HEADER,
    "Code Coverage Analysis,at.quelltextlich.jacoco.toolbox,Bar,1,8,0,0,1,4,1,3,1,3",
    "Code Coverage Analysis,at.quelltextlich.jacoco.toolbox,Baz,4,0,0,0,2,0,2,0,2,0",
  "Code Coverage Analysis,at.quelltextlich.jacoco.toolbox,Foo,1,8,0,0,1,4,1,3,1,3" };

  public void testNoArguments() {
    final ToolShim tool = new ToolShim(ReportCsvTool.class);
    tool.run(new String[] {});

    tool.assertStderrContains("help");
    tool.assertExitStatus(1);
  }

  public void testFooArgument() {
    final ToolShim tool = new ToolShim(ReportCsvTool.class);
    tool.run(new String[] { "--foo" });

    tool.assertStderrContains("help");
    tool.assertExitStatus(1);
  }

  public void testNoInput() throws IOException {
    final File output = getTemporaryFile(false, "csv");
    final String[] args = new String[] { "--output", output.getAbsolutePath() };

    final ToolShim tool = new ToolShim(ReportCsvTool.class);
    tool.run(args);

    tool.assertExitStatus(0);

    assertEquivalentCsv("Generated output CSV file did not match expected",
        new String[] { CSV_HEADER }, output);
  }

  public void testEmptyInput() throws IOException {
    final File input = getTemporaryFile();
    final File output = getTemporaryFile(false, "csv");
    final String[] args = new String[] { "--input", input.getAbsolutePath(),
        "--output", output.getAbsolutePath() };

    final ToolShim tool = new ToolShim(ReportCsvTool.class);
    tool.run(args);

    tool.assertExitStatus(0);

    assertEquivalentCsv("Generated output CSV file did not match expected",
        new String[] { CSV_HEADER }, output);
  }

  public void testInputMergedNoAnalyzeFor() throws IOException {
    final File input = new File(getClass().getResource("/jacoco-merged.exec")
        .getPath());
    final File output = getTemporaryFile(false, "csv");
    final String[] args = new String[] { "--input", input.getAbsolutePath(),
        "--output", output.getAbsolutePath() };

    final ToolShim tool = new ToolShim(ReportCsvTool.class);
    tool.run(args);

    tool.assertExitStatus(0);

    assertEquivalentCsv("Generated output CSV file did not match expected",
        new String[] { CSV_HEADER }, output);
  }

  public void testInputMerged() throws IOException {
    final File input = new File(getClass().getResource("/jacoco-merged.exec")
        .getPath());
    final File inputJar = new File(getClass().getResource(
        "/TestDataGroupMerged.jar").getPath());
    final File output = getTemporaryFile(false, "csv");
    final String[] args = new String[] { "--input", input.getAbsolutePath(),
        "--analyze-for", inputJar.getAbsolutePath(), "--output",
        output.getAbsolutePath() };

    final ToolShim tool = new ToolShim(ReportCsvTool.class);
    tool.run(args);

    tool.assertExitStatus(0);

    assertEquivalentCsv("Generated output CSV file did not match expected",
        MERGED_CSV, output);
  }

  public void testInputMergedWithTitle() throws IOException {
    final File input = new File(getClass().getResource("/jacoco-merged.exec")
        .getPath());
    final File inputJar = new File(getClass().getResource(
        "/TestDataGroupMerged.jar").getPath());
    final File output = getTemporaryFile(false, "csv");
    final String[] args = new String[] { "--input", input.getAbsolutePath(),
        "--analyze-for", inputJar.getAbsolutePath(), "--output",
        output.getAbsolutePath(), "--title", "quux" };

    final ToolShim tool = new ToolShim(ReportCsvTool.class);
    tool.run(args);

    tool.assertExitStatus(0);

    assertEquivalentCsv("Generated output CSV file did not match expected",
        new String[] { CSV_HEADER,
        "quux,at.quelltextlich.jacoco.toolbox,Bar,1,8,0,0,1,4,1,3,1,3",
        "quux,at.quelltextlich.jacoco.toolbox,Baz,4,0,0,0,2,0,2,0,2,0",
    "quux,at.quelltextlich.jacoco.toolbox,Foo,1,8,0,0,1,4,1,3,1,3" },
    output);
  }

  public void testInputFoo() throws IOException {
    final File input = new File(getClass().getResource("/jacoco-foo.exec")
        .getPath());
    final File inputJar = new File(getClass().getResource(
        "/TestDataGroupFoo.jar").getPath());
    final File output = getTemporaryFile(false, "csv");
    final String[] args = new String[] { "--input", input.getAbsolutePath(),
        "--analyze-for", inputJar.getAbsolutePath(), "--output",
        output.getAbsolutePath() };

    final ToolShim tool = new ToolShim(ReportCsvTool.class);
    tool.run(args);

    tool.assertExitStatus(0);

    assertEquivalentCsv("Generated output CSV file did not match expected",
        FOO_CSV, output);
  }

  public void testInputBar() throws IOException {
    final File input = new File(getClass().getResource("/jacoco-bar.exec")
        .getPath());
    final File inputJar = new File(getClass().getResource(
        "/TestDataGroupBar.jar").getPath());
    final File output = getTemporaryFile(false, "csv");
    final String[] args = new String[] { "--input", input.getAbsolutePath(),
        "--analyze-for", inputJar.getAbsolutePath(), "--output",
        output.getAbsolutePath() };

    final ToolShim tool = new ToolShim(ReportCsvTool.class);
    tool.run(args);

    tool.assertExitStatus(0);

    assertEquivalentCsv(
        "Generated output CSV file did not match expected",
        new String[] {
            CSV_HEADER,
            "Code Coverage Analysis,at.quelltextlich.jacoco.toolbox,Bar,1,8,0,0,1,4,1,3,1,3",
            "Code Coverage Analysis,at.quelltextlich.jacoco.toolbox,Baz,4,0,0,0,2,0,2,0,2,0",
        "Code Coverage Analysis,at.quelltextlich.jacoco.toolbox,Foo,9,0,0,0,5,0,4,0,4,0" },
        output);
  }

  public void testInputFooBar() throws IOException {
    final File inputFoo = new File(getClass().getResource("/jacoco-foo.exec")
        .getPath());
    final File inputBar = new File(getClass().getResource("/jacoco-bar.exec")
        .getPath());
    final File inputJar = new File(getClass().getResource(
        "/TestDataGroupMerged.jar").getPath());
    final File output = getTemporaryFile(false, "csv");
    final String[] args = new String[] { "--input", inputFoo.getAbsolutePath(),
        "--input", inputBar.getAbsolutePath(), "--analyze-for",
        inputJar.getAbsolutePath(), "--output", output.getAbsolutePath() };

    final ToolShim tool = new ToolShim(ReportCsvTool.class);
    tool.run(args);

    tool.assertExitStatus(0);

    assertEquivalentCsv("Generated output CSV file did not match expected",
        MERGED_CSV, output);
  }

  public void testInputFooBarColon() throws IOException {
    final File inputFoo = new File(getClass().getResource("/jacoco-foo.exec")
        .getPath());
    final File inputBar = new File(getClass().getResource("/jacoco-bar.exec")
        .getPath());
    final File inputFooJar = new File(getClass().getResource(
        "/TestDataGroupFoo.jar").getPath());
    final File inputBarJar = new File(getClass().getResource(
        "/TestDataGroupBar.jar").getPath());
    final File output = getTemporaryFile(false, "csv");
    final String[] args = new String[] { "--input",
        inputFoo.getAbsolutePath() + ":" + inputBar.getAbsolutePath(),
        "--analyze-for",
        inputFooJar.getAbsolutePath() + ":" + inputBarJar.getAbsolutePath(),
        "--output", output.getAbsolutePath() };

    final ToolShim tool = new ToolShim(ReportCsvTool.class);
    tool.run(args);

    tool.assertExitStatus(0);

    assertEquivalentCsv("Generated output CSV file did not match expected",
        MERGED_CSV, output);
  }
}
