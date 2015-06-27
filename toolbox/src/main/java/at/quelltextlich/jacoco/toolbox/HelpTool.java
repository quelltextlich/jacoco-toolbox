// Copyright 2015 quelltextlich e.U.
//
// This program and the accompanying materials are made available under the
// terms of the Eclipse Public License v1.0 which accompanies this
// distribution, and is available at http://www.eclipse.org/legal/epl-v10.html
//
package at.quelltextlich.jacoco.toolbox;

/**
 * Tool that prints help and command overview
 */
public class HelpTool extends Tool {
  @Override
  public void run(final String[] args) {
    final String BLANKS = "                ";

    stderr.println("Available tools:");

    final ToolRegistry toolRegistry = new ToolRegistry();
    for (final ToolRegistration registration : toolRegistry.getRegistrations()) {
      if (registration.isVisible()) {
        stderr.print("  ");
        final String commandName = registration.getCommandName();
        stderr.print(commandName);
        if (commandName.length() < 16) {
          stderr.print(BLANKS.substring(commandName.length()));
        } else {
          stderr.print("\n  ");
          stderr.print(BLANKS);
        }
        stderr.print("- ");
        stderr.println(registration.getDescription());
      }
    }
  }
}
