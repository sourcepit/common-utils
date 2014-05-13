/**
 * Copyright (c) 2014 Sourcepit.org contributors and others. All rights reserved. This program and the accompanying
 * materials are made available under the terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.sourcepit.common.utils.props;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class ChainedPropertiesSourceTest
{

   @Test
   public void test()
   {
      PropertiesMap source1 = new LinkedPropertiesMap();
      source1.put("foo", "foo1");

      PropertiesMap source2 = new LinkedPropertiesMap();
      source2.put("foo", "foo2");
      source2.put("bar", "bar2");

      PropertiesSource ps = new ChainedPropertiesSource(source1, source2);

      assertEquals("foo1", ps.get("foo"));
      assertEquals("bar2", ps.get("bar"));

      assertEquals("default", ps.get("foobar", "default"));
   }
}
