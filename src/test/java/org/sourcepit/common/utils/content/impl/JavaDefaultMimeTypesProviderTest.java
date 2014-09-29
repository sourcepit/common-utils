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

package org.sourcepit.common.utils.content.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;
import org.sourcepit.common.utils.content.MimeTypesProvider;

public class JavaDefaultMimeTypesProviderTest
{
   @Test
   public void testForFileName()
   {
      MimeTypesProvider provider = new JavaDefaultMimeTypesProvider();
      assertEquals("application/xml", provider.forFileName("hans.xml", null));
      assertEquals("application/xml", provider.forFileName("*.xml", null));
      assertEquals("application/xml", provider.forFileName(".xml", null));
      assertNull(provider.forFileName("xml", null));

      assertEquals("application/xml", provider.forFileName("hans.Xml", null));
      assertEquals("application/xml", provider.forFileName("*.Xml", null));
      assertEquals("application/xml", provider.forFileName(".Xml", null));
      assertNull(provider.forFileName("Xml", null));
   }
}
