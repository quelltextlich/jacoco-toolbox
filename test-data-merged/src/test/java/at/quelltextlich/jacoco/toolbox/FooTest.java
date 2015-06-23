// Copyright 2015 quelltextlich e.U.
//
// This program and the accompanying materials are made available under the
// terms of the Eclipse Public License v1.0 which accompanies this
// distribution, and is available at http://www.eclipse.org/legal/epl-v10.html
//
package at.quelltextlich.jacoco.toolbox;

import org.junit.experimental.categories.Category;

import junit.framework.TestCase;

@Category(at.quelltextlich.jacoco.toolbox.TestDataGroupFoo.class)
public class FooTest extends TestCase {
  public void testFoo() {
    Foo foo = new Foo();
    assertTrue(foo.outerMethod());
  }
}
