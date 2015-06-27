// Copyright 2015 quelltextlich e.U.
//
// This program and the accompanying materials are made available under the
// terms of the Eclipse Public License v1.0 which accompanies this
// distribution, and is available at http://www.eclipse.org/legal/epl-v10.html
//
package at.quelltextlich.jacoco.toolbox;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.DocumentType;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

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

    final Node report = assertXmlReport(output);
    removeAttribute(report, "name", "Code Coverage Analysis");
    assertEmptyNode(report);
  }

  public void testEmptyInput() throws IOException {
    final File input = getTemporaryFile();
    final File output = getTemporaryFile(false, "xml");
    final String[] args = new String[] { "--input", input.getAbsolutePath(),
        "--output", output.getAbsolutePath() };

    final ToolShim tool = new ToolShim(ReportXmlTool.class);
    tool.run(args);

    tool.assertExitStatus(0);

    final Node report = assertXmlReport(output);
    removeAttribute(report, "name", "Code Coverage Analysis");
    assertEmptyNode(report);
  }

  public void testEmptyInputTitle() throws IOException {
    final File input = getTemporaryFile();
    final File output = getTemporaryFile(false, "xml");
    final String[] args = new String[] { "--input", input.getAbsolutePath(),
        "--title", "quux", "--output", output.getAbsolutePath() };

    final ToolShim tool = new ToolShim(ReportXmlTool.class);
    tool.run(args);

    tool.assertExitStatus(0);

    final Node report = assertXmlReport(output);
    removeAttribute(report, "name", "quux");
    assertEmptyNode(report);
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

    final Node report = assertXmlReport(output);
    removeAttribute(report, "name", "Code Coverage Analysis");

    removeSessionInfoChild(report, "TestDataGroupFoo");
    removeSessionInfoChild(report, "TestDataGroupBar");

    removeCounterChildren(report, true);

    final Node packageNode = removeChildNode(report, "name",
        "at/quelltextlich/jacoco/toolbox");

    assertEmptyNode(report);

    assertElement(packageNode, "package");

    Node classNode = removeChildNode(packageNode, "name",
        "at/quelltextlich/jacoco/toolbox/Bar");
    removeMethodChild(classNode, "<init>", "V");
    removeMethodChild(classNode, "innerMethod", "V");
    removeMethodChild(classNode, "outerMethod", "I");
    removeMethodChild(classNode, "untestedMethod", "V");
    removeCounterChildren(classNode, true);
    assertEmptyNode(classNode);

    classNode = removeChildNode(packageNode, "name",
        "at/quelltextlich/jacoco/toolbox/Baz");
    removeMethodChild(classNode, "<init>", "V");
    removeMethodChild(classNode, "untestedMethod", "V");
    removeCounterChildren(classNode, true);
    assertEmptyNode(classNode);

    classNode = removeChildNode(packageNode, "name",
        "at/quelltextlich/jacoco/toolbox/Foo");
    removeMethodChild(classNode, "<init>", "V");
    removeMethodChild(classNode, "innerMethod", "V");
    removeMethodChild(classNode, "outerMethod", "Z");
    removeMethodChild(classNode, "untestedMethod", "V");
    removeCounterChildren(classNode, true);
    assertEmptyNode(classNode);

    removeSourceFileChild(packageNode, "Bar.java", new String[] { "16", "18",
        "21", "22", "26" });
    removeSourceFileChild(packageNode, "Baz.java", new String[] { "16", "18" });
    removeSourceFileChild(packageNode, "Foo.java", new String[] { "16", "18",
        "21", "22", "26" });

    removeCounterChildren(packageNode, true);

    assertEmptyNode(packageNode);
  }

  public Node assertXmlReport(final File file) {
    Document document = null;
    try {
      final DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory
          .newInstance();
      final DocumentBuilder documentBuilder = documentBuilderFactory
          .newDocumentBuilder();
      // Using a dummy entity resolver to avoid fetching of external resources
      documentBuilder.setEntityResolver(new EntityResolver() {
        @Override
        public InputSource resolveEntity(final String publicId,
            final String systemId) throws SAXException, IOException {
          return new InputSource(new StringReader(""));
        }
      });
      document = documentBuilder.parse(file);
    } catch (final SAXException e) {
      fail("Could not parse file '" + file + "' as XML\n" + e.toString());
    } catch (final IOException e) {
      fail("Could not parse file '" + file + "' as XML\n" + e.toString());
    } catch (final ParserConfigurationException e) {
      fail("Could not parse file '" + file + "' as XML\n" + e.toString());
    }

    final DocumentType docType = document.getDoctype();
    assertEquals("DocType name does not match", "report", docType.getName());
    assertEquals("DocType public id does not match",
        "-//JACOCO//DTD Report 1.0//EN", docType.getPublicId());
    assertEquals("DocType system id does not match", "report.dtd",
        docType.getSystemId());
    assertNull("DocType internal subset does not match",
        docType.getInternalSubset());

    final NodeList root = document.getChildNodes();
    // Two elements. First the doctype. Then the report node.
    assertEquals("root node does not have two elements", 2, root.getLength());

    final Node report = root.item(1);
    assertElement(report, "report");
    return report;
  }

  public void assertElement(final Node node, final String name) {
    assertEquals("Node '" + node + "' is not an element", Node.ELEMENT_NODE,
        node.getNodeType());
    assertEquals("Node name of '" + node + "' does not match", name,
        node.getNodeName());
  }

  public void assertEmptyNode(final Node node) {
    final NamedNodeMap attributes = node.getAttributes();
    assertEquals("Node '" + node + "' still has attributes", 0,
        attributes.getLength());

    final NodeList children = node.getChildNodes();
    assertEquals("Node '" + node + "' still has children", 0,
        children.getLength());
  }

  public void removeAttribute(final Node node, final String name) {
    final NamedNodeMap attributes = node.getAttributes();
    final Node attribute = attributes.getNamedItem(name);
    assertNotNull("Node '" + node.toString() + "' does not have a '" + name
        + "' attribute", attribute);
    attributes.removeNamedItem(name);
  }

  public void removeAttribute(final Node node, final String name,
      final String value) {
    final NamedNodeMap attributes = node.getAttributes();
    final Node attribute = attributes.getNamedItem(name);
    assertNotNull("Node '" + node.toString() + "' does not have a '" + name
        + "' attribute", attribute);
    assertEquals("Attribute values do not match", value,
        attribute.getNodeValue());
    attributes.removeNamedItem(name);
  }

  public Node removeChildNode(final Node node, final String name,
      final String value) {
    final NodeList children = node.getChildNodes();
    final int len = children.getLength();
    for (int i = 0; i < len; i++) {
      final Node child = children.item(i);

      final NamedNodeMap attributes = child.getAttributes();
      final Node attribute = attributes.getNamedItem(name);
      if (attribute != null) {
        if (value.equals(attribute.getNodeValue())) {
          node.removeChild(child);
          removeAttribute(child, name, value);
          return child;
        }
      }
    }
    fail("Could not find child of node '" + node.toString()
        + "' with attribute '" + name + "' having value '" + value + "'");
    return null;
  }

  public void removeSessionInfoChild(final Node node, final String id) {
    final Node child = removeChildNode(node, "id", id);
    removeAttribute(child, "start");
    removeAttribute(child, "dump");
    assertEmptyNode(child);
  }

  public void removeCounterChild(final Node node, final String type) {
    final Node child = removeChildNode(node, "type", type);
    removeAttribute(child, "missed");
    removeAttribute(child, "covered");
    assertEmptyNode(child);
  }

  public void removeCounterChildren(final Node node, final boolean removeClass) {
    removeCounterChild(node, "INSTRUCTION");
    removeCounterChild(node, "LINE");
    removeCounterChild(node, "COMPLEXITY");
    removeCounterChild(node, "METHOD");
    if (removeClass) {
      removeCounterChild(node, "CLASS");
    }
  }

  public void removeMethodChild(final Node node, final String name,
      final String desc) {
    final Node method = removeChildNode(node, "name", name);
    assertElement(method, "method");
    removeAttribute(method, "desc", "()" + desc);
    removeAttribute(method, "line");
    removeCounterChildren(method, false);
    assertEmptyNode(method);
  }

  public void removeSourceFileChild(final Node node, final String name,
      final String[] lines) {
    final Node sourceFile = removeChildNode(node, "name", name);
    assertElement(sourceFile, "sourcefile");
    for (final String line : lines) {
      final Node lineNode = removeChildNode(sourceFile, "nr", line);
      assertElement(lineNode, "line");
      removeAttribute(lineNode, "mi");
      removeAttribute(lineNode, "ci");
      removeAttribute(lineNode, "mb");
      removeAttribute(lineNode, "cb");
      assertEmptyNode(lineNode);
    }
    removeCounterChildren(sourceFile, true);
    assertEmptyNode(sourceFile);
  }
}
