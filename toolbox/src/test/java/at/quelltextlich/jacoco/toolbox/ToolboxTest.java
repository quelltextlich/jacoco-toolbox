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
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.List;

import junit.framework.TestCase;

public class ToolboxTest extends TestCase {
  final private static String CSV_HEADER = "GROUP,PACKAGE,CLASS,"
      + "INSTRUCTION_MISSED,INSTRUCTION_COVERED,BRANCH_MISSED,BRANCH_COVERED,"
      + "LINE_MISSED,LINE_COVERED,COMPLEXITY_MISSED,COMPLEXITY_COVERED,"
      + "METHOD_MISSED,METHOD_COVERED";

  private File getTemporaryFile() throws IOException {
    return getTemporaryFile(true);
  }

  private File getTemporaryFile(boolean create) throws IOException {
    return getTemporaryFile(create, "exec");
  }

  private File getTemporaryFile(boolean create, String ending)
      throws IOException {
    File file = File.createTempFile("jacoco-toolbox-test", "." + ending);
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
    File file = new File(getClass().getResource("/jacoco-merged.exec")
        .getPath());
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

  public void testOutputCsvNoInput() throws IOException {
    File output = getTemporaryFile(false, "csv");
    String[] args = new String[] { "--output-csv", output.getAbsolutePath() };

    ToolboxShim toolbox = new ToolboxShim();
    toolbox.run(args);

    toolbox.assertExitStatus(0);

    assertFileEquals("Generated output csv file did not match expected",
        new String[] { CSV_HEADER }, output);
  }

  public void testOutputCsvEmptyInput() throws IOException {
    File input = getTemporaryFile();
    File output = getTemporaryFile(false, "csv");
    String[] args = new String[] { "--input", input.getAbsolutePath(),
        "--output-csv", output.getAbsolutePath() };

    ToolboxShim toolbox = new ToolboxShim();
    toolbox.run(args);

    toolbox.assertExitStatus(0);

    assertFileEquals("Generated output csv file did not match expected",
        new String[] { CSV_HEADER }, output);
  }

  public void testOutputCsvInputMergedNoAnalyzeFor() throws IOException {
    File input = new File(getClass().getResource("/jacoco-merged.exec")
        .getPath());
    File output = getTemporaryFile(false, "csv");
    String[] args = new String[] { "--input", input.getAbsolutePath(),
        "--output-csv", output.getAbsolutePath() };

    ToolboxShim toolbox = new ToolboxShim();
    toolbox.run(args);

    toolbox.assertExitStatus(0);

    assertFileEquals("Generated output csv file did not match expected",
        new String[] { CSV_HEADER }, output);
  }

  public void testOutputCsvInputMerged() throws IOException {
    File input = new File(getClass().getResource("/jacoco-merged.exec")
        .getPath());
    File inputJar = new File(getClass().getResource("/TestDataGroupMerged.jar")
        .getPath());
    File output = getTemporaryFile(false, "csv");
    String[] args = new String[] { "--input", input.getAbsolutePath(),
        "--analyze-for", inputJar.getAbsolutePath(), "--output-csv",
        output.getAbsolutePath() };

    ToolboxShim toolbox = new ToolboxShim();
    toolbox.run(args);

    toolbox.assertExitStatus(0);

    assertFileEquals("Generated output csv file did not match expected",
        new String[] { CSV_HEADER,
            "bundle,at.quelltextlich.jacoco.toolbox,Bar,1,8,0,0,1,4,1,3,1,3",
            "bundle,at.quelltextlich.jacoco.toolbox,Baz,4,0,0,0,2,0,2,0,2,0",
            "bundle,at.quelltextlich.jacoco.toolbox,Foo,1,8,0,0,1,4,1,3,1,3" },
        output);
  }

  public void testOutputCsvInputFoo() throws IOException {
    File input = new File(getClass().getResource("/jacoco-foo.exec").getPath());
    File inputJar = new File(getClass().getResource("/TestDataGroupFoo.jar")
        .getPath());
    File output = getTemporaryFile(false, "csv");
    String[] args = new String[] { "--input", input.getAbsolutePath(),
        "--analyze-for", inputJar.getAbsolutePath(), "--output-csv",
        output.getAbsolutePath() };

    ToolboxShim toolbox = new ToolboxShim();
    toolbox.run(args);

    toolbox.assertExitStatus(0);

    assertFileEquals("Generated output csv file did not match expected",
        new String[] { CSV_HEADER,
            "bundle,at.quelltextlich.jacoco.toolbox,Bar,9,0,0,0,5,0,4,0,4,0",
            "bundle,at.quelltextlich.jacoco.toolbox,Baz,4,0,0,0,2,0,2,0,2,0",
            "bundle,at.quelltextlich.jacoco.toolbox,Foo,1,8,0,0,1,4,1,3,1,3" },
        output);
  }

  public void testOutputCsvInputBar() throws IOException {
    File input = new File(getClass().getResource("/jacoco-bar.exec").getPath());
    File inputJar = new File(getClass().getResource("/TestDataGroupBar.jar")
        .getPath());
    File output = getTemporaryFile(false, "csv");
    String[] args = new String[] { "--input", input.getAbsolutePath(),
        "--analyze-for", inputJar.getAbsolutePath(), "--output-csv",
        output.getAbsolutePath() };

    ToolboxShim toolbox = new ToolboxShim();
    toolbox.run(args);

    toolbox.assertExitStatus(0);

    assertFileEquals("Generated output csv file did not match expected",
        new String[] { CSV_HEADER,
            "bundle,at.quelltextlich.jacoco.toolbox,Bar,1,8,0,0,1,4,1,3,1,3",
            "bundle,at.quelltextlich.jacoco.toolbox,Baz,4,0,0,0,2,0,2,0,2,0",
            "bundle,at.quelltextlich.jacoco.toolbox,Foo,9,0,0,0,5,0,4,0,4,0" },
        output);
  }

  public void testOutputCsvInputFooBar() throws IOException {
    File inputFoo = new File(getClass().getResource("/jacoco-foo.exec")
        .getPath());
    File inputBar = new File(getClass().getResource("/jacoco-bar.exec")
        .getPath());
    File inputJar = new File(getClass().getResource("/TestDataGroupMerged.jar")
        .getPath());
    File output = getTemporaryFile(false, "csv");
    String[] args = new String[] { "--input", inputFoo.getAbsolutePath(),
        "--input", inputBar.getAbsolutePath(), "--analyze-for",
        inputJar.getAbsolutePath(), "--output-csv", output.getAbsolutePath() };

    ToolboxShim toolbox = new ToolboxShim();
    toolbox.run(args);

    toolbox.assertExitStatus(0);

    assertFileEquals("Generated output csv file did not match expected",
        new String[] { CSV_HEADER,
            "bundle,at.quelltextlich.jacoco.toolbox,Bar,1,8,0,0,1,4,1,3,1,3",
            "bundle,at.quelltextlich.jacoco.toolbox,Baz,4,0,0,0,2,0,2,0,2,0",
            "bundle,at.quelltextlich.jacoco.toolbox,Foo,1,8,0,0,1,4,1,3,1,3" },
        output);
  }

  public void testOutputExecInputFooBar() throws IOException {
    File inputFoo = new File(getClass().getResource("/jacoco-foo.exec")
        .getPath());
    File inputBar = new File(getClass().getResource("/jacoco-bar.exec")
        .getPath());
    File outputExec = getTemporaryFile(false, "exec");
    String[] argsExec = new String[] { "--input", inputFoo.getAbsolutePath(),
        "--input", inputBar.getAbsolutePath(), "--output-exec",
        outputExec.getAbsolutePath() };

    ToolboxShim toolboxExec = new ToolboxShim();
    toolboxExec.run(argsExec);
    toolboxExec.assertExitStatus(0);

    // Since execs need not agree byte for byte, we instead generate a CSV from
    // the merged exec, and assert on this CSV.

    File inputJar = new File(getClass().getResource("/TestDataGroupMerged.jar")
        .getPath());
    File outputCsv = getTemporaryFile(false, "exec");
    String[] argsCsv = new String[] { "--input", outputExec.getAbsolutePath(),
        "--analyze-for", inputJar.getAbsolutePath(), "--output-csv",
        outputCsv.getAbsolutePath() };

    ToolboxShim toolboxCsv = new ToolboxShim();
    toolboxCsv.run(argsCsv);
    toolboxCsv.assertExitStatus(0);

    assertFileEquals("Generated output csv file did not match expected",
        new String[] { CSV_HEADER,
            "bundle,at.quelltextlich.jacoco.toolbox,Bar,1,8,0,0,1,4,1,3,1,3",
            "bundle,at.quelltextlich.jacoco.toolbox,Baz,4,0,0,0,2,0,2,0,2,0",
            "bundle,at.quelltextlich.jacoco.toolbox,Foo,1,8,0,0,1,4,1,3,1,3" },
        outputCsv);
  }

  public void testOutputHtmlInputFooNoSource() throws IOException {
    File input = new File(getClass().getResource("/jacoco-foo.exec").getPath());
    File inputJar = new File(getClass().getResource("/TestDataGroupFoo.jar")
        .getPath());
    File output = getTemporaryFile(false, "dir");
    String[] args = new String[] { "--input", input.getAbsolutePath(),
        "--analyze-for", inputJar.getAbsolutePath(), "--output-html",
        output.getAbsolutePath() };

    ToolboxShim toolbox = new ToolboxShim();
    toolbox.run(args);

    toolbox.assertExitStatus(0);

    List<String> indexLines = Files.readAllLines(
        new File(output, "index.html").toPath(), Charset.defaultCharset());
    assertTrue("Generated index file does not contain any lines",
        indexLines.size() > 0);
    assertTrue("Generated index file does not cointan "
        + "'at.quelltextlich.jacoco.toolbox' marker", indexLines.get(0)
        .contains("at.quelltextlich.jacoco.toolbox"));

    File foo = new File(output, "at.quelltextlich.jacoco.toolbox/Foo.html");
    assertTrue("No 'Foo' HTML file got generated", foo.exists());

    File bazSource = new File(output,
        "at.quelltextlich.jacoco.toolbox/Baz.java.html");
    assertFalse("'Baz' source HTML file got generated", bazSource.exists());
  }

  public void testOutputHtmlInputFoo() throws IOException {
    File input = new File(getClass().getResource("/jacoco-foo.exec").getPath());
    File inputJar = new File(getClass().getResource("/TestDataGroupFoo.jar")
        .getPath());
    File source = new File(getClass().getResource("/sources").getPath());
    File output = getTemporaryFile(false, "dir");
    String[] args = new String[] { "--input", input.getAbsolutePath(),
        "--analyze-for", inputJar.getAbsolutePath(), "--source",
        source.getAbsolutePath(), "--output-html", output.getAbsolutePath() };

    ToolboxShim toolbox = new ToolboxShim();
    toolbox.run(args);

    toolbox.assertExitStatus(0);

    List<String> indexLines = Files.readAllLines(
        new File(output, "index.html").toPath(), Charset.defaultCharset());
    assertTrue("Generated index file does not contain any lines",
        indexLines.size() > 0);
    assertTrue("Generated index file does not cointan "
        + "'at.quelltextlich.jacoco.toolbox' marker", indexLines.get(0)
        .contains("at.quelltextlich.jacoco.toolbox"));

    File foo = new File(output, "at.quelltextlich.jacoco.toolbox/Foo.html");
    assertTrue("No 'Foo' HTML file got generated", foo.exists());

    File bazSource = new File(output,
        "at.quelltextlich.jacoco.toolbox/Baz.java.html");
    assertTrue("No 'Baz' source HTML file got generated", bazSource.exists());
  }

  public void testOutputXmlInputFooBar() throws IOException {
    File inputFoo = new File(getClass().getResource("/jacoco-foo.exec")
        .getPath());
    File inputBar = new File(getClass().getResource("/jacoco-bar.exec")
        .getPath());
    File inputJar = new File(getClass().getResource("/TestDataGroupMerged.jar")
        .getPath());
    File output = getTemporaryFile(false, "xml");
    String[] args = new String[] { "--input", inputFoo.getAbsolutePath(),
        "--input", inputBar.getAbsolutePath(), "--analyze-for",
        inputJar.getAbsolutePath(), "--output-xml", output.getAbsolutePath() };

    ToolboxShim toolbox = new ToolboxShim();
    toolbox.run(args);

    toolbox.assertExitStatus(0);

    assertFileEquals(
        "Generated output XML file did not match expected",
        new String[] { "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>"
            + "<!DOCTYPE report PUBLIC \"-//JACOCO//DTD Report 1.0//EN\" \"report.dtd\">"
            + "<report name=\"bundle\"><sessioninfo id=\"TestDataGroupFoo\" start=\"1435267361990\" dump=\"1435267362599\"/>"
            + "<sessioninfo id=\"TestDataGroupBar\" start=\"1435267362876\" dump=\"1435267363481\"/>"
            + "<package name=\"at/quelltextlich/jacoco/toolbox\">"
            + "<class name=\"at/quelltextlich/jacoco/toolbox/Bar\">"
            + "<method name=\"&lt;init&gt;\" desc=\"()V\" line=\"16\">"
            + "<counter type=\"INSTRUCTION\" missed=\"0\" covered=\"3\"/>"
            + "<counter type=\"LINE\" missed=\"0\" covered=\"1\"/>"
            + "<counter type=\"COMPLEXITY\" missed=\"0\" covered=\"1\"/>"
            + "<counter type=\"METHOD\" missed=\"0\" covered=\"1\"/>"
            + "</method>"
            + "<method name=\"innerMethod\" desc=\"()V\" line=\"18\">"
            + "<counter type=\"INSTRUCTION\" missed=\"0\" covered=\"1\"/>"
            + "<counter type=\"LINE\" missed=\"0\" covered=\"1\"/>"
            + "<counter type=\"COMPLEXITY\" missed=\"0\" covered=\"1\"/>"
            + "<counter type=\"METHOD\" missed=\"0\" covered=\"1\"/>"
            + "</method>"
            + "<method name=\"outerMethod\" desc=\"()I\" line=\"21\">"
            + "<counter type=\"INSTRUCTION\" missed=\"0\" covered=\"4\"/>"
            + "<counter type=\"LINE\" missed=\"0\" covered=\"2\"/>"
            + "<counter type=\"COMPLEXITY\" missed=\"0\" covered=\"1\"/>"
            + "<counter type=\"METHOD\" missed=\"0\" covered=\"1\"/>"
            + "</method>"
            + "<method name=\"untestedMethod\" desc=\"()V\" line=\"26\">"
            + "<counter type=\"INSTRUCTION\" missed=\"1\" covered=\"0\"/>"
            + "<counter type=\"LINE\" missed=\"1\" covered=\"0\"/>"
            + "<counter type=\"COMPLEXITY\" missed=\"1\" covered=\"0\"/>"
            + "<counter type=\"METHOD\" missed=\"1\" covered=\"0\"/>"
            + "</method>"
            + "<counter type=\"INSTRUCTION\" missed=\"1\" covered=\"8\"/>"
            + "<counter type=\"LINE\" missed=\"1\" covered=\"4\"/>"
            + "<counter type=\"COMPLEXITY\" missed=\"1\" covered=\"3\"/>"
            + "<counter type=\"METHOD\" missed=\"1\" covered=\"3\"/>"
            + "<counter type=\"CLASS\" missed=\"0\" covered=\"1\"/>"
            + "</class>"
            + "<class name=\"at/quelltextlich/jacoco/toolbox/Baz\">"
            + "<method name=\"&lt;init&gt;\" desc=\"()V\" line=\"16\">"
            + "<counter type=\"INSTRUCTION\" missed=\"3\" covered=\"0\"/>"
            + "<counter type=\"LINE\" missed=\"1\" covered=\"0\"/>"
            + "<counter type=\"COMPLEXITY\" missed=\"1\" covered=\"0\"/>"
            + "<counter type=\"METHOD\" missed=\"1\" covered=\"0\"/>"
            + "</method>"
            + "<method name=\"untestedMethod\" desc=\"()V\" line=\"18\">"
            + "<counter type=\"INSTRUCTION\" missed=\"1\" covered=\"0\"/>"
            + "<counter type=\"LINE\" missed=\"1\" covered=\"0\"/>"
            + "<counter type=\"COMPLEXITY\" missed=\"1\" covered=\"0\"/>"
            + "<counter type=\"METHOD\" missed=\"1\" covered=\"0\"/>"
            + "</method>"
            + "<counter type=\"INSTRUCTION\" missed=\"4\" covered=\"0\"/>"
            + "<counter type=\"LINE\" missed=\"2\" covered=\"0\"/>"
            + "<counter type=\"COMPLEXITY\" missed=\"2\" covered=\"0\"/>"
            + "<counter type=\"METHOD\" missed=\"2\" covered=\"0\"/>"
            + "<counter type=\"CLASS\" missed=\"1\" covered=\"0\"/>"
            + "</class>"
            + "<class name=\"at/quelltextlich/jacoco/toolbox/Foo\">"
            + "<method name=\"&lt;init&gt;\" desc=\"()V\" line=\"16\">"
            + "<counter type=\"INSTRUCTION\" missed=\"0\" covered=\"3\"/>"
            + "<counter type=\"LINE\" missed=\"0\" covered=\"1\"/>"
            + "<counter type=\"COMPLEXITY\" missed=\"0\" covered=\"1\"/>"
            + "<counter type=\"METHOD\" missed=\"0\" covered=\"1\"/>"
            + "</method>"
            + "<method name=\"innerMethod\" desc=\"()V\" line=\"18\">"
            + "<counter type=\"INSTRUCTION\" missed=\"0\" covered=\"1\"/>"
            + "<counter type=\"LINE\" missed=\"0\" covered=\"1\"/>"
            + "<counter type=\"COMPLEXITY\" missed=\"0\" covered=\"1\"/>"
            + "<counter type=\"METHOD\" missed=\"0\" covered=\"1\"/>"
            + "</method>"
            + "<method name=\"outerMethod\" desc=\"()Z\" line=\"21\">"
            + "<counter type=\"INSTRUCTION\" missed=\"0\" covered=\"4\"/>"
            + "<counter type=\"LINE\" missed=\"0\" covered=\"2\"/>"
            + "<counter type=\"COMPLEXITY\" missed=\"0\" covered=\"1\"/>"
            + "<counter type=\"METHOD\" missed=\"0\" covered=\"1\"/>"
            + "</method><method name=\"untestedMethod\" desc=\"()V\" line=\"26\">"
            + "<counter type=\"INSTRUCTION\" missed=\"1\" covered=\"0\"/>"
            + "<counter type=\"LINE\" missed=\"1\" covered=\"0\"/>"
            + "<counter type=\"COMPLEXITY\" missed=\"1\" covered=\"0\"/>"
            + "<counter type=\"METHOD\" missed=\"1\" covered=\"0\"/>"
            + "</method>"
            + "<counter type=\"INSTRUCTION\" missed=\"1\" covered=\"8\"/>"
            + "<counter type=\"LINE\" missed=\"1\" covered=\"4\"/>"
            + "<counter type=\"COMPLEXITY\" missed=\"1\" covered=\"3\"/>"
            + "<counter type=\"METHOD\" missed=\"1\" covered=\"3\"/>"
            + "<counter type=\"CLASS\" missed=\"0\" covered=\"1\"/>"
            + "</class>"
            + "<sourcefile name=\"Foo.java\">"
            + "<line nr=\"16\" mi=\"0\" ci=\"3\" mb=\"0\" cb=\"0\"/>"
            + "<line nr=\"18\" mi=\"0\" ci=\"1\" mb=\"0\" cb=\"0\"/>"
            + "<line nr=\"21\" mi=\"0\" ci=\"2\" mb=\"0\" cb=\"0\"/>"
            + "<line nr=\"22\" mi=\"0\" ci=\"2\" mb=\"0\" cb=\"0\"/>"
            + "<line nr=\"26\" mi=\"1\" ci=\"0\" mb=\"0\" cb=\"0\"/>"
            + "<counter type=\"INSTRUCTION\" missed=\"1\" covered=\"8\"/>"
            + "<counter type=\"LINE\" missed=\"1\" covered=\"4\"/>"
            + "<counter type=\"COMPLEXITY\" missed=\"1\" covered=\"3\"/>"
            + "<counter type=\"METHOD\" missed=\"1\" covered=\"3\"/>"
            + "<counter type=\"CLASS\" missed=\"0\" covered=\"1\"/>"
            + "</sourcefile><sourcefile name=\"Baz.java\">"
            + "<line nr=\"16\" mi=\"3\" ci=\"0\" mb=\"0\" cb=\"0\"/>"
            + "<line nr=\"18\" mi=\"1\" ci=\"0\" mb=\"0\" cb=\"0\"/>"
            + "<counter type=\"INSTRUCTION\" missed=\"4\" covered=\"0\"/>"
            + "<counter type=\"LINE\" missed=\"2\" covered=\"0\"/>"
            + "<counter type=\"COMPLEXITY\" missed=\"2\" covered=\"0\"/>"
            + "<counter type=\"METHOD\" missed=\"2\" covered=\"0\"/>"
            + "<counter type=\"CLASS\" missed=\"1\" covered=\"0\"/>"
            + "</sourcefile>"
            + "<sourcefile name=\"Bar.java\">"
            + "<line nr=\"16\" mi=\"0\" ci=\"3\" mb=\"0\" cb=\"0\"/>"
            + "<line nr=\"18\" mi=\"0\" ci=\"1\" mb=\"0\" cb=\"0\"/>"
            + "<line nr=\"21\" mi=\"0\" ci=\"2\" mb=\"0\" cb=\"0\"/>"
            + "<line nr=\"22\" mi=\"0\" ci=\"2\" mb=\"0\" cb=\"0\"/>"
            + "<line nr=\"26\" mi=\"1\" ci=\"0\" mb=\"0\" cb=\"0\"/>"
            + "<counter type=\"INSTRUCTION\" missed=\"1\" covered=\"8\"/>"
            + "<counter type=\"LINE\" missed=\"1\" covered=\"4\"/>"
            + "<counter type=\"COMPLEXITY\" missed=\"1\" covered=\"3\"/>"
            + "<counter type=\"METHOD\" missed=\"1\" covered=\"3\"/>"
            + "<counter type=\"CLASS\" missed=\"0\" covered=\"1\"/>"
            + "</sourcefile>"
            + "<counter type=\"INSTRUCTION\" missed=\"6\" covered=\"16\"/>"
            + "<counter type=\"LINE\" missed=\"4\" covered=\"8\"/>"
            + "<counter type=\"COMPLEXITY\" missed=\"4\" covered=\"6\"/>"
            + "<counter type=\"METHOD\" missed=\"4\" covered=\"6\"/>"
            + "<counter type=\"CLASS\" missed=\"1\" covered=\"2\"/>"
            + "</package>"
            + "<counter type=\"INSTRUCTION\" missed=\"6\" covered=\"16\"/>"
            + "<counter type=\"LINE\" missed=\"4\" covered=\"8\"/>"
            + "<counter type=\"COMPLEXITY\" missed=\"4\" covered=\"6\"/>"
            + "<counter type=\"METHOD\" missed=\"4\" covered=\"6\"/>"
            + "<counter type=\"CLASS\" missed=\"1\" covered=\"2\"/>"
            + "</report>" }, output);
  }

  private void assertFileEquals(String message, String[] expected, File file)
      throws IOException {
    List<String> actual = Files.readAllLines(file.toPath(),
        Charset.defaultCharset());
    int line = 1;
    for (String actualLine : actual) {
      assertTrue(message + "\n" + "Expected only " + expected.length
          + " lines, but found " + actual.size() + ".\n"
          + "First unexpected line is:\n" + actualLine, expected.length >= line);
      assertEquals(message + " (line # " + line + ")", expected[line - 1],
          actualLine);
      line++;
    }
    assertTrue(message + " (expected " + expected.length + " lines, but found "
        + line + ")", expected.length == line - 1);
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
