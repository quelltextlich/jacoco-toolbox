// Copyright 2015 quelltextlich e.U.
//
// This program and the accompanying materials are made available under the
// terms of the Eclipse Public License v1.0 which accompanies this
// distribution, and is available at http://www.eclipse.org/legal/epl-v10.html
//
package at.quelltextlich.jacoco.toolbox;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.jacoco.report.IReportVisitor;
import org.jacoco.report.xml.XMLFormatter;

/**
 * Tool to write an XML report
 */
public class ReportXmlTool extends ReportTool {
  @Override
  public void writeReport() {
    final XMLFormatter formatter = new XMLFormatter();
    OutputStream stream;
    try {
      stream = new FileOutputStream(output);
      try {
        final IReportVisitor visitor = formatter.createVisitor(stream);
        visit(visitor);
      } catch (final IOException e) {
        exit("Failed to write XML to '" + output + "'", e);
      } finally {
        try {
          stream.close();
        } catch (final IOException e) {
          exit("Cannot close file '" + output + "'", e);
        }
      }
    } catch (final FileNotFoundException e) {
      exit("Cannot write to '" + output + "'", e);
    }
  }
}
