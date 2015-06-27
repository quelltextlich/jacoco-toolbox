// Copyright 2015 quelltextlich e.U.
//
// This program and the accompanying materials are made available under the
// terms of the Eclipse Public License v1.0 which accompanies this
// distribution, and is available at http://www.eclipse.org/legal/epl-v10.html
//
package at.quelltextlich.jacoco.toolbox;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.jacoco.core.data.ExecutionDataWriter;

public class IdentifyVersionTool extends InputTool {
  public IdentifyVersionTool() {
    super(false);
  }

  @Override
  public void run(final String[] args) {
    super.run(args);

    for (final File file : inputs) {
      int version = 0;
      DataInputStream stream;
      try {
        stream = new DataInputStream(new FileInputStream(file));
        try {
          final byte chunk1 = stream.readByte();
          if (chunk1 == ExecutionDataWriter.BLOCK_HEADER) {
            final int chunk2 = stream.readChar();
            if (chunk2 == ExecutionDataWriter.MAGIC_NUMBER) {
              version = stream.readChar();
            } else {
              exit("Could not find magic number at beginning of file '" + file
                  + "' (found " + chunk2 + ")");
            }
          } else {
            exit("Could not find exec block header at beginning of file '"
                + file + "'");
          }
        } catch (final IOException e) {
          exit("Could not read from file '" + file + "+", e);
        } finally {
          try {
            stream.close();
          } catch (final IOException e) {
            exit("Could not close stream for '" + file + "'", e);
          }
        }
      } catch (final FileNotFoundException e) {
        exit("Could not find file '" + file + "'", e);
      }
      stdout.println(file + ",0x" + Integer.toHexString(version) + ","
          + version);
    }
  }
}
