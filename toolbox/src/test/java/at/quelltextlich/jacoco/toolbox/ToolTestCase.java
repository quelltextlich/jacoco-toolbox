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
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
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

  protected void assertEquivalentCsv(final String message,
      final String[] expected, final File file) throws IOException {
    final List<String> actualLines = Files.readAllLines(file.toPath(),
        Charset.defaultCharset());

    final List<String> expectedLines = new LinkedList<String>(
        Arrays.asList(expected));

    final Comparator<String> comparator = new CsvComparator();
    Collections.sort(actualLines, comparator);
    Collections.sort(expectedLines, comparator);

    int line = 1;
    final Iterator<String> expectedIterator = expectedLines.iterator();
    for (final String actualLine : actualLines) {
      assertTrue(message + "\n" + "Expected only " + expectedLines.size()
          + " lines, but found " + actualLines.size() + ".\n"
          + "First unexpected line is:\n" + actualLine,
          expectedIterator.hasNext());
      final String expectedLine = expectedIterator.next();
      assertEquals(message + " (line # " + line + ")", expectedLine, actualLine);
      line++;
    }
    assertTrue(message + " (expected " + expected.length + " lines, but found "
        + line + ")", expected.length == line - 1);
  }

  class CsvComparator implements Comparator<String> {
    @Override
    public int compare(final String o1, final String o2) {
      if (o1.equals(ReportCsvToolTest.CSV_HEADER)) {
        if (o2.equals(ReportCsvToolTest.CSV_HEADER)) {
          return 0;
        } else {
          return -1;
        }
      } else {
        if (o2.equals(ReportCsvToolTest.CSV_HEADER)) {
          return 1;
        } else {
          return o1.compareTo(o2);
        }
      }
    }

  }
}
