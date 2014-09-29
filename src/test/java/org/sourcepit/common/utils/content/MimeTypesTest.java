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

package org.sourcepit.common.utils.content;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class MimeTypesTest
{
   @Test
   public void testForFileName()
   {
      MimeTypes mimeTypes = MimeTypes.DEFAULT;

      assertNull(mimeTypes.detectByFileName(".", null));
      assertNull(mimeTypes.detectByFileName("foo.", null));
      assertNull(mimeTypes.detectByFileName(".foo", null));

      assertNull(mimeTypes.detectByFileName(".mf", null));
      assertEquals("application/x-java-manifest", mimeTypes.detectByFileName("manifest.mf", null).getName());

      assertEquals("application/xml", mimeTypes.detectByFileName("hans.xml", null).getName());
      assertEquals("application/xml", mimeTypes.detectByFileName("*.xml", null).getName());
      assertEquals("application/xml", mimeTypes.detectByFileName(".xml", null).getName());
      assertNull(mimeTypes.detectByFileName("xml", null));

      assertEquals("application/xml", mimeTypes.detectByFileName("hans.Xml", null).getName());
      assertEquals("application/xml", mimeTypes.detectByFileName("*.Xml", null).getName());
      assertEquals("application/xml", mimeTypes.detectByFileName(".Xml", null).getName());
      assertNull(mimeTypes.detectByFileName("Xml", null));

      assertEquals("application/x-java-properties", mimeTypes.detectByFileName("Hans.properties", null).getName());
      assertEquals("application/x-java-properties", mimeTypes.detectByFileName("*.properties", null).getName());
      assertEquals("application/x-java-properties", mimeTypes.detectByFileName(".properties", null).getName());
      assertNull(mimeTypes.detectByFileName("properties", null));

      assertEquals("application/x-java-properties", mimeTypes.detectByFileName("Hans.Properties", null).getName());
      assertEquals("application/x-java-properties", mimeTypes.detectByFileName("*.Properties", null).getName());
      assertEquals("application/x-java-properties", mimeTypes.detectByFileName(".Properties", null).getName());
      assertNull(mimeTypes.detectByFileName("Properties", null));
   }

   @Test
   public void testBaseType()
   {
      MimeTypes mimeTypes = MimeTypes.DEFAULT;
      MimeType mimeType = mimeTypes.detectByFileName("*.xsd", null);
      assertEquals(mimeType.getName(), "application/xsd+xml");

      MimeType baseType = mimeType.getBaseType();
      assertNotNull(baseType);
      assertEquals(baseType.getName(), "application/xml");
   }
}
