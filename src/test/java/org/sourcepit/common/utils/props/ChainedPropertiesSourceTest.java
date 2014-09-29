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
