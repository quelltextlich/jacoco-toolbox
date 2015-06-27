// Copyright 2015 quelltextlich e.U.
//
// This program and the accompanying materials are made available under the
// terms of the Eclipse Public License v1.0 which accompanies this
// distribution, and is available at http://www.eclipse.org/legal/epl-v10.html
//
package at.quelltextlich.jacoco.toolbox;

import java.io.IOException;

import org.jacoco.report.FileMultiReportOutput;
import org.jacoco.report.IReportVisitor;
import org.jacoco.report.html.HTMLFormatter;

/**
 * Tool to write an HTML reports
 */
public class ReportHtmlTool extends ReportTool {
  @Override
  public void writeReport() {
    final HTMLFormatter formatter = new HTMLFormatter();
    FileMultiReportOutput multiOutput;
    multiOutput = new FileMultiReportOutput(output);
    try {
      final IReportVisitor visitor = formatter.createVisitor(multiOutput);
      visit(visitor);
    } catch (final IOException e) {
      exit("Failed to write HTML to '" + output + "'", e);
    }
  }
}
