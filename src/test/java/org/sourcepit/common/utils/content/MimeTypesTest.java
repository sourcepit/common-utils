/**
 * Copyright (c) 2013 Sourcepit.org contributors and others. All rights reserved. This program and the accompanying
 * materials are made available under the terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
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
