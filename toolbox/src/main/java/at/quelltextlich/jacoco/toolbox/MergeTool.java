// Copyright 2015 quelltextlich e.U.
//
// This program and the accompanying materials are made available under the
// terms of the Eclipse Public License v1.0 which accompanies this
// distribution, and is available at http://www.eclipse.org/legal/epl-v10.html
//
package at.quelltextlich.jacoco.toolbox;

import java.io.File;
import java.io.IOException;

import org.kohsuke.args4j.Option;

public class MergeTool extends InputTool {
  @Option(name = "--output", usage = "Place to write the report to.", required = true)
  public File output;

  @Override
  public void run(final String[] args) {
    super.run(args);

    try {
      loader.save(output, false);
    } catch (final IOException e) {
      exit("Failed to write exec to '" + output + "'", e);
    }
  }
}
