// Copyright 2015 quelltextlich e.U.
//
// This program and the accompanying materials are made available under the
// terms of the Eclipse Public License v1.0 which accompanies this
// distribution, and is available at http://www.eclipse.org/legal/epl-v10.html
//
package at.quelltextlich.jacoco.toolbox;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import org.jacoco.core.tools.ExecFileLoader;
import org.kohsuke.args4j.Option;

/**
 * Tool that deals with exec inputs
 */
public abstract class InputTool extends Tool {
  protected ExecFileLoader loader = new ExecFileLoader();

  private final List<File> inputs = new LinkedList<File>();

  @Option(name = "--input", usage = "Adds an input to the toolbox")
  void addInput(final String inputStr) {
    final File input = new File(inputStr);
    if (!input.exists()) {
      exit("The file '" + input + "' does not exist");
    }
    if (!input.canRead()) {
      exit("The file '" + input + "' is not readable");
    }
    if (!inputs.add(input)) {
      exit("Could not add '" + input + "' to inputs");
    }
  }

  /**
   * Loads the JaCoCo inputs
   */
  public void loadInputs() {
    for (final File input : inputs) {
      try {
        loader.load(input);
      } catch (final IOException e) {
        exit("Error loading '" + input + "'", e);
      }
    }
  }

  @Override
  public void run(final String[] args) {
    super.run(args);

    loadInputs();
  }
}
