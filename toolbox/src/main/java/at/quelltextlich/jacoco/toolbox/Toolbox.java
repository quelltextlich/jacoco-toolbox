// Copyright 2015 quelltextlich e.U.
//
// This program and the accompanying materials are made available under the
// terms of the Eclipse Public License v1.0 which accompanies this
// distribution, and is available at http://www.eclipse.org/legal/epl-v10.html
//
package at.quelltextlich.jacoco.toolbox;

import java.util.Arrays;

/**
 * Toolbox for working with Jacoco exec files
 */
public class Toolbox extends Tool {
  ToolRegistry toolRegistry = new ToolRegistry();

  private <T extends Tool> void runTool(final Class<T> toolClass,
      final String[] args, final String commandName) {
    try {
      final Tool tool = toolClass.newInstance();
      tool.run(args);
    } catch (final InstantiationException e) {
      exit("Could not instantiate tool '" + commandName + "'", e);
    } catch (final IllegalAccessException e) {
      exit("Could not instantiate tool '" + commandName + "'", e);
    }
  }

  private void printHelp() {
    runTool(HelpTool.class, new String[] {}, "help");
  }

  /**
   * Runs the toolbox' logic for given parameters
   *
   * @param args
   *          Arguments to the toolbox.
   */
  @Override
  public void run(final String[] args) {
    if (args == null || args.length == 0) {
      printHelp();
      exit("No command given");
    }
    final String commandName = args[0];

    final ToolRegistration toolRegistration = toolRegistry
        .getToolRegistration(commandName);
    if (toolRegistration == null) {
      printHelp();
      exit("Unknown tool '" + commandName + "'");
    }

    final String[] strippedArgs;
    if (args.length > 1) {
      strippedArgs = Arrays.copyOfRange(args, 1, args.length);
    } else {
      strippedArgs = new String[] {};
    }
    runTool(toolRegistration.getToolClass(), strippedArgs, commandName);
  }

  /**
   * Static entry point for the toolbox
   *
   * @param args
   *          Arguments to the toolbox.
   */
  public static void main(final String[] args) {
    final Toolbox toolbox = new Toolbox();
    toolbox.run(args);
  }
}
