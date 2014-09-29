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

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import org.junit.Test;

public class PropertiesUtilsTest extends TestCase
{
   public void testEscapeJavaProperties() throws Exception
   {
      assertEquals("\\u00FC", PropertiesUtils.escapeJavaProperties("ü"));

      String result = PropertiesUtils.escapeJavaProperties("   Hans\\\n\r im ''Glück''!");
      assertEquals("\\   Hans\\\\\\n\\r im ''Gl\\u00FCck''\\!", result);
   }

   @Test
   public void testSplit() throws Exception
   {
      List<String> dest = new ArrayList<String>();

      PropertiesUtils.split(dest, ",foo, , bar", ",", true, true);
      assertEquals(2, dest.size());
      assertEquals("foo", dest.get(0));
      assertEquals("bar", dest.get(1));

      dest.clear();

      PropertiesUtils.split(dest, ",foo, , bar", ",", false, false);
      assertEquals(4, dest.size());
      assertEquals("", dest.get(0));
      assertEquals("foo", dest.get(1));
      assertEquals(" ", dest.get(2));
      assertEquals(" bar", dest.get(3));

      dest.clear();

      PropertiesUtils.split(dest, ",foo, , bar", ",", true, false);
      assertEquals(4, dest.size());
      assertEquals("", dest.get(0));
      assertEquals("foo", dest.get(1));
      assertEquals("", dest.get(2));
      assertEquals("bar", dest.get(3));

      dest.clear();

      PropertiesUtils.split(dest, ",foo, , bar", ",", false, true);
      assertEquals(3, dest.size());
      assertEquals("foo", dest.get(0));
      assertEquals(" ", dest.get(1));
      assertEquals(" bar", dest.get(2));
   }
}
