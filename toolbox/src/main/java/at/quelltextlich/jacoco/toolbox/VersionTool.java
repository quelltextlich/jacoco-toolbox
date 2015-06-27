// Copyright 2015 quelltextlich e.U.
//
// This program and the accompanying materials are made available under the
// terms of the Eclipse Public License v1.0 which accompanies this
// distribution, and is available at http://www.eclipse.org/legal/epl-v10.html
//
package at.quelltextlich.jacoco.toolbox;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Properties;
import java.util.TimeZone;

import org.jacoco.core.data.ExecutionDataWriter;

public class VersionTool extends Tool {
  public Properties properties = null;

  private String getProperty(final String key) {
    if (properties == null) {
      properties = new Properties();
      final String resourceName = "application.properties";
      try {
        properties.load(getClass().getResourceAsStream(resourceName));
      } catch (final IOException e) {
        exit("Could not load '" + resourceName + "'", e);
      }
    }
    return properties.getProperty(key);
  }

  @Override
  public void run(final String[] args) {
    super.run(args);
    stdout.print(getProperty("application.name"));
    stdout.print(" ");
    stdout.println(getProperty("application.version"));

    final String timestamp = getProperty("build.timestamp");
    String formattedTimestamp;
    if (timestamp == null || "${timestamp}".equals(timestamp)) {
      formattedTimestamp = "?";
    } else {
      final SimpleDateFormat format = new SimpleDateFormat(
          "yyyy-MM-dd'T'HH:mm:ssZ", Locale.US);
      format.setTimeZone(TimeZone.getTimeZone("UTC"));
      formattedTimestamp = format.format(new Date(Long.parseLong(timestamp)));
    }
    stdout.print("Built on ");
    stdout.print(formattedTimestamp);
    stdout.print(" using commit ");
    stdout.print(getProperty("git.hash").substring(0, 7));
    stdout.print(" (branch: ");
    stdout.print(getProperty("git.branch"));
    stdout.println(")");

    stdout.print("Supported exec format versions: ");
    stdout.print(Integer.toString(ExecutionDataWriter.FORMAT_VERSION));
    stdout.print(" (0x");
    stdout.print(Integer.toHexString(ExecutionDataWriter.FORMAT_VERSION));
    stdout.println(")");
  }
}
