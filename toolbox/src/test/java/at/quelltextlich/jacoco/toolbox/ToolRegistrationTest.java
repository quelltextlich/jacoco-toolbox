// Copyright 2015 quelltextlich e.U.
//
// This program and the accompanying materials are made available under the
// terms of the Eclipse Public License v1.0 which accompanies this
// distribution, and is available at http://www.eclipse.org/legal/epl-v10.html
//
package at.quelltextlich.jacoco.toolbox;

import junit.framework.TestCase;

public class ToolRegistrationTest extends TestCase {
  public void testGetterCommandName() {
    final ToolRegistration subject = new ToolRegistration("foo",
        "fooDescription", HelpTool.class);

    assertEquals("Command name did not match", "foo", subject.getCommandName());
  }

  public void testGetterDescription() {
    final ToolRegistration subject = new ToolRegistration("foo",
        "fooDescription", HelpTool.class);

    assertEquals("Description did not match", "fooDescription",
        subject.getDescription());
  }

  public void testGetterTool() {
    final ToolRegistration subject = new ToolRegistration("foo",
        "fooDescription", HelpTool.class);

    assertEquals("Tool class did not match", HelpTool.class,
        subject.getToolClass());
  }

  public void testGetterVisibile() {
    final ToolRegistration subject = new ToolRegistration("foo",
        "fooDescription", HelpTool.class);

    assertTrue("Visible registrations is invisible", subject.isVisible());
  }

  public void testGetterVisibileInvisible() {
    final ToolRegistration subject = new ToolRegistration("foo",
        "fooDescription", HelpTool.class, false);

    assertFalse("Invisible registrations is visible", subject.isVisible());
  }

  public void testComparatorLess() {
    final ToolRegistration smaller = new ToolRegistration("0smaller",
        "smallerDescription", HelpTool.class);
    final ToolRegistration bigger = new ToolRegistration("1bigger",
        "biggerDescription", HelpTool.class);

    assertTrue("Smaller is not less than bigger", smaller.compareTo(bigger) < 0);
  }

  public void testComparatorLessThroughDescription() {
    final ToolRegistration smaller = new ToolRegistration("foo",
        "0smallerDescription", HelpTool.class);
    final ToolRegistration bigger = new ToolRegistration("foo",
        "1biggerDescription", HelpTool.class);

    assertTrue("Smaller is not less than bigger", smaller.compareTo(bigger) < 0);
  }

  public void testComparatorGreater() {
    final ToolRegistration smaller = new ToolRegistration("0smaller",
        "smallerDescription", HelpTool.class);
    final ToolRegistration bigger = new ToolRegistration("1bigger",
        "biggerDescription", HelpTool.class);

    assertTrue("Smaller is not less than bigger", bigger.compareTo(smaller) > 0);
  }

  public void testComparatorGreaterThroughDescription() {
    final ToolRegistration smaller = new ToolRegistration("foo",
        "0smallerDescription", HelpTool.class);
    final ToolRegistration bigger = new ToolRegistration("foo",
        "1biggerDescription", HelpTool.class);

    assertTrue("Smaller is not less than bigger", bigger.compareTo(smaller) > 0);
  }
}
