/**
 * Copyright (c) 2014 Sourcepit.org contributors and others. All rights reserved. This program and the accompanying
 * materials are made available under the terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.sourcepit.common.utils.props;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

public class MapPropertiesSourceTest
{

   @Test
   public void testStringMap()
   {
      Map<String, String> map = new HashMap<String, String>();

      map.put("prop1", "hallo");
      map.put("prop2", "true");

      PropertiesSource propertiesSource = MapPropertiesSource.toPropertiesSource(map);

      assertEquals("hallo", propertiesSource.get("prop1"));
      assertEquals("true", propertiesSource.get("prop2"));
      assertEquals(true, propertiesSource.getBoolean("prop2", false));
   }

   @Test
   public void testObjectMap()
   {
      Map<Object, Object> map = new HashMap<Object, Object>();

      map.put("prop1", "hallo");
      map.put("prop2", Boolean.TRUE);

      PropertiesSource propertiesSource = MapPropertiesSource.toPropertiesSource(map);

      assertEquals("hallo", propertiesSource.get("prop1"));
      assertNull(propertiesSource.get("prop2"));
      assertEquals(false, propertiesSource.getBoolean("prop2", false));
      
      propertiesSource = MapPropertiesSource.toPropertiesSource(map, true);

      assertEquals("hallo", propertiesSource.get("prop1"));
      assertEquals("true", propertiesSource.get("prop2"));
      assertEquals(true, propertiesSource.getBoolean("prop2", false));
   }

}
