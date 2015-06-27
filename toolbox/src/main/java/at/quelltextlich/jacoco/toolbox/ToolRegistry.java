// Copyright 2015 quelltextlich e.U.
//
// This program and the accompanying materials are made available under the
// terms of the Eclipse Public License v1.0 which accompanies this
// distribution, and is available at http://www.eclipse.org/legal/epl-v10.html
//
package at.quelltextlich.jacoco.toolbox;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

/**
 * Toolbox for working with Jacoco exec files
 */
public class ToolRegistry {

  Map<String, ToolRegistration> registeredTools;

  public ToolRegistry() {
    registerTools();
  }

  private <T extends Tool> void registerTool(final String name,
      final String description, final Class<T> cls) {
    registerTool(name, description, cls, null);
  }

  private <T extends Tool> void registerTool(final String name,
      final String description, final Class<T> cls, final String[] hiddenNames) {

    // Publicly visible registration
    ToolRegistration registration = new ToolRegistration(name, description, cls);
    registeredTools.put(registration.getCommandName(), registration);

    // Hidden alias registrations
    if (hiddenNames != null) {
      for (final String hiddenName : hiddenNames) {
        registration = new ToolRegistration(hiddenName, description, cls, false);
        registeredTools.put(registration.getCommandName(), registration);
      }
    }
  }

  private void registerTools() {
    registeredTools = new HashMap<String, ToolRegistration>();

    final String helpDescription = "prints this help page";
    registerTool("help", helpDescription, HelpTool.class, new String[] {
      "--help", "-help", "-h", "-?" });

    registerTool("identify", "shows format version of exec files",
        IdentifyVersionTool.class);
    registerTool("merge", "merges multiple exec files into a single one",
        MergeTool.class);
    registerTool("report-csv", "writes a CSV report", ReportCsvTool.class);
    registerTool("report-xml", "writes an XML report", ReportXmlTool.class);
    registerTool("report-html", "writes an HTML report", ReportHtmlTool.class);
  }

  public ToolRegistration getToolRegistration(final String command) {
    return registeredTools.get(command);
  }

  public Collection<ToolRegistration> getRegistrations() {
    final LinkedList<ToolRegistration> ret = new LinkedList<ToolRegistration>(
        registeredTools.values());
    Collections.sort(ret);
    return ret;
  }
}
