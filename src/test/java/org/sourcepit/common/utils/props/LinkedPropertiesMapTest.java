/**
 * Copyright (c) 2011 Sourcepit.org contributors and others. All rights reserved. This program and the accompanying
 * materials are made available under the terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.sourcepit.common.utils.props;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import junit.framework.TestCase;

public class LinkedPropertiesMapTest extends TestCase
{
   public void testGet() throws Exception
   {
      PropertiesMap propertiesMap = new LinkedPropertiesMap();
      assertEquals(null, propertiesMap.get("key"));
      propertiesMap.put("key", "value");
      assertEquals("value", propertiesMap.get("key"));
      assertEquals(null, propertiesMap.get("key2"));
      assertEquals("value2", propertiesMap.get("key2", "value2"));
   }

   public void testDefaultPropeties() throws Exception
   {
      LinkedPropertiesMap propertiesMap = new LinkedPropertiesMap();
      assertNull(propertiesMap.getDefaultProperties());
      assertNull(propertiesMap.get("key"));

      Map<String, String> defaults = new HashMap<String, String>();
      defaults.put("key", "value");

      propertiesMap.setDefaultProperties(defaults);

      assertSame(defaults, propertiesMap.getDefaultProperties());

      assertEquals("value", propertiesMap.get("key"));

      assertFalse(propertiesMap.containsKey("key"));

      propertiesMap.put("key", "newValue");
      assertEquals("newValue", propertiesMap.get("key"));
      assertTrue(propertiesMap.containsKey("key"));
   }

   public void testToJavaProperties() throws Exception
   {
      LinkedPropertiesMap propertiesMap = new LinkedPropertiesMap();
      propertiesMap.put("key", "value");

      Properties javaProperties = propertiesMap.toJavaProperties();
      assertEquals("value", javaProperties.getProperty("key"));
      assertNull(javaProperties.getProperty("key2"));
      assertTrue(javaProperties.containsKey("key"));
      assertFalse(javaProperties.containsKey("key2"));

      Map<String, String> defaults = new HashMap<String, String>();
      defaults.put("key2", "value2");
      propertiesMap.setDefaultProperties(defaults);

      javaProperties = propertiesMap.toJavaProperties();
      assertEquals("value", javaProperties.getProperty("key"));
      assertEquals("value2", javaProperties.getProperty("key2"));
      assertTrue(javaProperties.containsKey("key"));
      assertFalse(javaProperties.containsKey("key2"));
   }

   public void testBoolean() throws Exception
   {
      LinkedPropertiesMap propertiesMap = new LinkedPropertiesMap();
      assertTrue(propertiesMap.getBoolean("key", true));
      assertFalse(propertiesMap.getBoolean("key", false));

      propertiesMap.put("key", "true");
      assertTrue(propertiesMap.getBoolean("key", false));

      propertiesMap.put("key", "false");
      assertFalse(propertiesMap.getBoolean("key", true));

      propertiesMap.put("key", "murks");
      assertFalse(propertiesMap.getBoolean("key", true));
   }
}
