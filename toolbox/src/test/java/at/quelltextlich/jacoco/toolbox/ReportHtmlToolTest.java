// Copyright 2015 quelltextlich e.U.
//
// This program and the accompanying materials are made available under the
// terms of the Eclipse Public License v1.0 which accompanies this
// distribution, and is available at http://www.eclipse.org/legal/epl-v10.html
//
package at.quelltextlich.jacoco.toolbox;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.List;

public class ReportHtmlToolTest extends ToolTestCase {
  public void testNoArguments() {
    final ToolShim tool = new ToolShim(ReportHtmlTool.class);
    tool.run(new String[] {});

    tool.assertStderrContains("help");
    tool.assertExitStatus(1);
  }

  public void testFooArgument() {
    final ToolShim tool = new ToolShim(ReportHtmlTool.class);
    tool.run(new String[] { "--foo" });

    tool.assertStderrContains("help");
    tool.assertExitStatus(1);
  }

  public void testNoInput() throws IOException {
    final File output = getTemporaryFile(false, "dir");
    final String[] args = new String[] { "--output", output.getAbsolutePath() };

    final ToolShim tool = new ToolShim(ReportHtmlTool.class);
    tool.run(args);

    tool.assertExitStatus(0);

    assertTrue("Output file does not exist", output.exists());
    assertTrue("Output file is not a directory", output.isDirectory());
  }

  public void testEmptyInput() throws IOException {
    final File input = getTemporaryFile();
    final File output = getTemporaryFile(false, "dir");
    final String[] args = new String[] { "--input", input.getAbsolutePath(),
        "--output", output.getAbsolutePath() };

    final ToolShim tool = new ToolShim(ReportHtmlTool.class);
    tool.run(args);

    tool.assertExitStatus(0);

    assertTrue("Output file does not exist", output.exists());
    assertTrue("Output file is not a directory", output.isDirectory());
  }

  public void testOutputCsvEmptyInputTitle() throws IOException {
    final File input = getTemporaryFile();
    final File output = getTemporaryFile(false, "dir");
    final String[] args = new String[] { "--input", input.getAbsolutePath(),
        "--title", "quux", "--output", output.getAbsolutePath() };

    final ToolShim tool = new ToolShim(ReportHtmlTool.class);
    tool.run(args);

    tool.assertExitStatus(0);

    assertTrue("Output file does not exist", output.exists());
    assertTrue("Output file is not a directory", output.isDirectory());
  }

  public void testInputFooNoSource() throws IOException {
    final File input = new File(getClass().getResource("/jacoco-foo.exec")
        .getPath());
    final File inputJar = new File(getClass().getResource(
        "/TestDataGroupFoo.jar").getPath());
    final File output = getTemporaryFile(false, "dir");
    final String[] args = new String[] { "--input", input.getAbsolutePath(),
        "--analyze-for", inputJar.getAbsolutePath(), "--output",
        output.getAbsolutePath() };

    final ToolShim tool = new ToolShim(ReportHtmlTool.class);
    tool.run(args);

    tool.assertExitStatus(0);

    assertTrue("Output file does not exist", output.exists());
    assertTrue("Output file is not a directory", output.isDirectory());

    final List<String> indexLines = Files.readAllLines(new File(output,
        "index.html").toPath(), Charset.defaultCharset());
    assertTrue("Generated index file does not contain any lines",
        indexLines.size() > 0);
    assertTrue("Generated index file does not cointan "
        + "'at.quelltextlich.jacoco.toolbox' marker", indexLines.get(0)
        .contains("at.quelltextlich.jacoco.toolbox"));

    final File foo = new File(output,
        "at.quelltextlich.jacoco.toolbox/Foo.html");
    assertTrue("No 'Foo' HTML file got generated", foo.exists());

    final File bazSource = new File(output,
        "at.quelltextlich.jacoco.toolbox/Baz.java.html");
    assertFalse("'Baz' source HTML file got generated", bazSource.exists());
  }

  public void testInputFoo() throws IOException {
    final File input = new File(getClass().getResource("/jacoco-foo.exec")
        .getPath());
    final File inputJar = new File(getClass().getResource(
        "/TestDataGroupFoo.jar").getPath());
    final File source = new File(getClass().getResource("/sources").getPath());
    final File output = getTemporaryFile(false, "dir");
    final String[] args = new String[] { "--input", input.getAbsolutePath(),
        "--analyze-for", inputJar.getAbsolutePath(), "--source",
        source.getAbsolutePath(), "--output", output.getAbsolutePath() };

    final ToolShim tool = new ToolShim(ReportHtmlTool.class);
    tool.run(args);

    tool.assertExitStatus(0);

    assertTrue("Output file does not exist", output.exists());
    assertTrue("Output file is not a directory", output.isDirectory());

    final List<String> indexLines = Files.readAllLines(new File(output,
        "index.html").toPath(), Charset.defaultCharset());
    assertTrue("Generated index file does not contain any lines",
        indexLines.size() > 0);
    assertTrue("Generated index file does not cointan "
        + "'at.quelltextlich.jacoco.toolbox' marker", indexLines.get(0)
        .contains("at.quelltextlich.jacoco.toolbox"));

    final File foo = new File(output,
        "at.quelltextlich.jacoco.toolbox/Foo.html");
    assertTrue("No 'Foo' HTML file got generated", foo.exists());

    final File bazSource = new File(output,
        "at.quelltextlich.jacoco.toolbox/Baz.java.html");
    assertTrue("No 'Baz' source HTML file got generated", bazSource.exists());
  }

  public void testInputFooBar() throws IOException {
    final File inputFoo = new File(getClass().getResource("/jacoco-foo.exec")
        .getPath());
    final File inputBar = new File(getClass().getResource("/jacoco-bar.exec")
        .getPath());
    final File inputFooJar = new File(getClass().getResource(
        "/TestDataGroupFoo.jar").getPath());
    final File inputBarJar = new File(getClass().getResource(
        "/TestDataGroupBar.jar").getPath());
    final File source = new File(getClass().getResource("/sources").getPath());
    final File output = getTemporaryFile(false, "dir");
    final String[] args = new String[] { "--input", inputFoo.getAbsolutePath(),
        "--input", inputBar.getAbsolutePath(), "--analyze-for",
        inputFooJar.getAbsolutePath(), "--analyze-for",
        inputBarJar.getAbsolutePath(), "--source", source.getAbsolutePath(),
        "--output", output.getAbsolutePath() };

    final ToolShim tool = new ToolShim(ReportHtmlTool.class);
    tool.run(args);

    tool.assertExitStatus(0);

    assertTrue("Output file does not exist", output.exists());
    assertTrue("Output file is not a directory", output.isDirectory());

    final List<String> indexLines = Files.readAllLines(new File(output,
        "index.html").toPath(), Charset.defaultCharset());
    assertTrue("Generated index file does not contain any lines",
        indexLines.size() > 0);
    assertTrue("Generated index file does not cointan "
        + "'at.quelltextlich.jacoco.toolbox' marker", indexLines.get(0)
        .contains("at.quelltextlich.jacoco.toolbox"));

    final File foo = new File(output,
        "at.quelltextlich.jacoco.toolbox/Foo.html");
    assertTrue("No 'Foo' HTML file got generated", foo.exists());

    final File bazSource = new File(output,
        "at.quelltextlich.jacoco.toolbox/Baz.java.html");
    assertTrue("No 'Baz' source HTML file got generated", bazSource.exists());
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
    final File source = new File(getClass().getResource("/sources").getPath());
    final File output = getTemporaryFile(false, "dir");
    final String[] args = new String[] { "--input",
        inputFoo.getAbsolutePath() + ":" + inputBar.getAbsolutePath(),
        "--analyze-for",
        inputFooJar.getAbsolutePath() + ":" + inputBarJar.getAbsolutePath(),
        "--source", source.getAbsolutePath() + ":" + source.getAbsolutePath(),
        "--output", output.getAbsolutePath() };

    final ToolShim tool = new ToolShim(ReportHtmlTool.class);
    tool.run(args);

    tool.assertExitStatus(0);

    assertTrue("Output file does not exist", output.exists());
    assertTrue("Output file is not a directory", output.isDirectory());

    final List<String> indexLines = Files.readAllLines(new File(output,
        "index.html").toPath(), Charset.defaultCharset());
    assertTrue("Generated index file does not contain any lines",
        indexLines.size() > 0);
    assertTrue("Generated index file does not cointan "
        + "'at.quelltextlich.jacoco.toolbox' marker", indexLines.get(0)
        .contains("at.quelltextlich.jacoco.toolbox"));

    final File foo = new File(output,
        "at.quelltextlich.jacoco.toolbox/Foo.html");
    assertTrue("No 'Foo' HTML file got generated", foo.exists());

    final File bazSource = new File(output,
        "at.quelltextlich.jacoco.toolbox/Baz.java.html");
    assertTrue("No 'Baz' source HTML file got generated", bazSource.exists());
  }
}
