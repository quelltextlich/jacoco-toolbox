// Copyright 2015 quelltextlich e.U.
//
// This program and the accompanying materials are made available under the
// terms of the Eclipse Public License v1.0 which accompanies this
// distribution, and is available at http://www.eclipse.org/legal/epl-v10.html
//
package at.quelltextlich.jacoco.toolbox;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.List;

import junit.framework.TestCase;

public abstract class ToolTestCase extends TestCase {
  protected File getTemporaryFile() throws IOException {
    return getTemporaryFile(true);
  }

  protected File getTemporaryFile(final boolean create) throws IOException {
    return getTemporaryFile(create, "exec");
  }

  protected File getTemporaryFile(final boolean create, final String ending)
      throws IOException {
    final File file = File.createTempFile("jacoco-toolbox-test", "." + ending);
    file.deleteOnExit();
    if (!create) {
      assertTrue("Could not delete temporary file '" + file + "'",
          file.delete());
    }
    return file;
  }

  protected void assertFileEquals(final String message,
      final String[] expected, final File file) throws IOException {
    final List<String> actual = Files.readAllLines(file.toPath(),
        Charset.defaultCharset());
    int line = 1;
    for (final String actualLine : actual) {
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
}
