// Copyright 2015 quelltextlich e.U.
//
// This program and the accompanying materials are made available under the
// terms of the Eclipse Public License v1.0 which accompanies this
// distribution, and is available at http://www.eclipse.org/legal/epl-v10.html
//
package at.quelltextlich.jacoco.toolbox;

import java.io.File;
import java.io.IOException;

public class ReportXmlToolTest extends ToolTestCase {
  public void testNoArguments() {
    final ToolShim tool = new ToolShim(ReportXmlTool.class);
    tool.run(new String[] {});

    tool.assertStderrContains("help");
    tool.assertExitStatus(1);
  }

  public void testFooArgument() {
    final ToolShim tool = new ToolShim(ReportXmlTool.class);
    tool.run(new String[] { "--foo" });

    tool.assertStderrContains("help");
    tool.assertExitStatus(1);
  }

  public void testNoInput() throws IOException {
    final File output = getTemporaryFile(false, "xml");
    final String[] args = new String[] { "--output", output.getAbsolutePath() };

    final ToolShim tool = new ToolShim(ReportXmlTool.class);
    tool.run(args);

    tool.assertExitStatus(0);

    assertFileEquals(
        "Generated output XML file did not match expected",
        new String[] { "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>"
            + "<!DOCTYPE report PUBLIC \"-//JACOCO//DTD Report 1.0//EN\" \"report.dtd\"><report name=\"Code Coverage Analysis\"/>" },
            output);
  }

  public void testEmptyInput() throws IOException {
    final File input = getTemporaryFile();
    final File output = getTemporaryFile(false, "xml");
    final String[] args = new String[] { "--input", input.getAbsolutePath(),
        "--output", output.getAbsolutePath() };

    final ToolShim tool = new ToolShim(ReportXmlTool.class);
    tool.run(args);

    tool.assertExitStatus(0);

    assertFileEquals(
        "Generated output XML file did not match expected",
        new String[] { "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>"
            + "<!DOCTYPE report PUBLIC \"-//JACOCO//DTD Report 1.0//EN\" \"report.dtd\"><report name=\"Code Coverage Analysis\"/>" },
            output);
  }

  public void testEmptyInputTitle() throws IOException {
    final File input = getTemporaryFile();
    final File output = getTemporaryFile(false, "xml");
    final String[] args = new String[] { "--input", input.getAbsolutePath(),
        "--title", "quux", "--output", output.getAbsolutePath() };

    final ToolShim tool = new ToolShim(ReportXmlTool.class);
    tool.run(args);

    tool.assertExitStatus(0);

    assertFileEquals(
        "Generated output XML file did not match expected",
        new String[] { "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>"
            + "<!DOCTYPE report PUBLIC \"-//JACOCO//DTD Report 1.0//EN\" \"report.dtd\"><report name=\"quux\"/>" },
            output);
  }

  public void testInputFooBar() throws IOException {
    final File inputFoo = new File(getClass().getResource("/jacoco-foo.exec")
        .getPath());
    final File inputBar = new File(getClass().getResource("/jacoco-bar.exec")
        .getPath());
    final File inputJar = new File(getClass().getResource(
        "/TestDataGroupMerged.jar").getPath());
    final File output = getTemporaryFile(false, "xml");
    final String[] args = new String[] { "--input", inputFoo.getAbsolutePath(),
        "--input", inputBar.getAbsolutePath(), "--analyze-for",
        inputJar.getAbsolutePath(), "--output", output.getAbsolutePath() };

    final ToolShim tool = new ToolShim(ReportXmlTool.class);
    tool.run(args);

    tool.assertExitStatus(0);

    assertFileEquals(
        "Generated output XML file did not match expected",
        new String[] { "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>"
            + "<!DOCTYPE report PUBLIC \"-//JACOCO//DTD Report 1.0//EN\" \"report.dtd\">"
            + "<report name=\"Code Coverage Analysis\"><sessioninfo id=\"TestDataGroupFoo\" start=\"1435267361990\" dump=\"1435267362599\"/>"
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
}
