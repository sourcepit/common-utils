/*
 * Copyright 2014 Bernd Vogt and others.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.sourcepit.common.utils.props;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import junit.framework.TestCase;

public class DelegatingPropertiesMapTest extends TestCase
{
   public void testToJavaProperties() throws Exception
   {
      Map<String, String> delegate = new HashMap<String, String>();
      delegate.put("key", "value");

      PropertiesMap propertiesMap = new DelegatingPropertiesMap(delegate);
      assertEquals("value", propertiesMap.get("key"));
      assertEquals("value2", propertiesMap.get("key2", "value2"));
      assertTrue(propertiesMap.containsKey("key"));
      assertFalse(propertiesMap.containsKey("key2"));

      Properties javaProperties = propertiesMap.toJavaProperties();
      assertEquals("value", javaProperties.getProperty("key"));
      assertEquals("value2", javaProperties.getProperty("key2", "value2"));
      assertTrue(javaProperties.containsKey("key"));
      assertFalse(javaProperties.containsKey("key2"));
   }

   public void testToJavaProperties2() throws Exception
   {
      PropertiesMap defaults = new LinkedPropertiesMap();
      defaults.put("key2", "value2");

      LinkedPropertiesMap delegate = new LinkedPropertiesMap();
      delegate.setDefaultProperties(defaults);
      delegate.put("key", "value");

      PropertiesMap propertiesMap = new DelegatingPropertiesMap(delegate);
      assertEquals("value", propertiesMap.get("key"));
      assertEquals("value2", propertiesMap.get("key2", "value2"));
      assertTrue(propertiesMap.containsKey("key"));
      assertFalse(propertiesMap.containsKey("key2"));

      Properties javaProperties = propertiesMap.toJavaProperties();
      assertEquals("value", javaProperties.getProperty("key"));
      assertEquals("value2", javaProperties.getProperty("key2", "value2"));
      assertTrue(javaProperties.containsKey("key"));
      assertFalse(javaProperties.containsKey("key2"));
   }
}
