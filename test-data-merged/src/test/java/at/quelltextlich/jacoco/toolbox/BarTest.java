// Copyright 2015 quelltextlich e.U.
//
// This program and the accompanying materials are made available under the
// terms of the Eclipse Public License v1.0 which accompanies this
// distribution, and is available at http://www.eclipse.org/legal/epl-v10.html
//
package at.quelltextlich.jacoco.toolbox;

import org.junit.experimental.categories.Category;

import junit.framework.TestCase;

@Category(at.quelltextlich.jacoco.toolbox.TestDataGroupBar.class)
public class BarTest extends TestCase {
  public void testBar() {
    Bar bar = new Bar();
    assertEquals(42, bar.outerMethod());
  }
}
