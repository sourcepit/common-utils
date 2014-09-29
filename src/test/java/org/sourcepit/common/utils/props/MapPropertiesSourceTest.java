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
