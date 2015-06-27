// Copyright 2015 quelltextlich e.U.
//
// This program and the accompanying materials are made available under the
// terms of the Eclipse Public License v1.0 which accompanies this
// distribution, and is available at http://www.eclipse.org/legal/epl-v10.html
//
package at.quelltextlich.jacoco.toolbox;

import java.util.Collection;

import junit.framework.TestCase;

public class ToolRegistryTest extends TestCase {
  public void testHelpToolIsContained() {
    final ToolRegistry registry = new ToolRegistry();
    final ToolRegistration registration = registry.getToolRegistration("help");

    assertEquals("Command name of returned registration does not match",
        "help", registration.getCommandName());
    assertEquals("Tool class of returned registration does not match",
        HelpTool.class, registration.getToolClass());
  }

  public void testGetRegstrationsAreSorted() {
    final ToolRegistry registry = new ToolRegistry();
    final Collection<ToolRegistration> registrations = registry
        .getRegistrations();

    String lastName = null;
    for (final ToolRegistration registration : registrations) {
      assertNotNull("Null registration is contained", registration);
      if (lastName != null) {
        assertTrue("Registration are not sorted (" + lastName + " >= "
            + registration.getCommandName(),
            lastName.compareTo(registration.getCommandName()) < 0);
      }
      lastName = registration.getCommandName();
    }
  }

  public void testGetRegstrationsNotTrivial() {
    final ToolRegistry registry = new ToolRegistry();
    final Collection<ToolRegistration> registrations = registry
        .getRegistrations();
    assertTrue("Too few tools registered (found only " + registrations.size()
        + ")", registrations.size() > 5);
  }
}
