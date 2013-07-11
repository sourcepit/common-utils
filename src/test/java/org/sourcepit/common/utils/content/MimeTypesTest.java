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
      MimeTypes mimeTypes = MimeTypes.getDefault();

      assertNull(mimeTypes.detectByFileName("."));
      assertNull(mimeTypes.detectByFileName("foo."));
      assertNull(mimeTypes.detectByFileName(".foo"));
      
      assertNull(mimeTypes.detectByFileName(".mf"));
      assertEquals("application/x-java-manifest", mimeTypes.detectByFileName("manifest.mf").getName());

      assertEquals("application/xml", mimeTypes.detectByFileName("hans.xml").getName());
      assertEquals("application/xml", mimeTypes.detectByFileName("*.xml").getName());
      assertEquals("application/xml", mimeTypes.detectByFileName(".xml").getName());
      assertNull(mimeTypes.detectByFileName("xml"));

      assertEquals("application/xml", mimeTypes.detectByFileName("hans.Xml").getName());
      assertEquals("application/xml", mimeTypes.detectByFileName("*.Xml").getName());
      assertEquals("application/xml", mimeTypes.detectByFileName(".Xml").getName());
      assertNull(mimeTypes.detectByFileName("Xml"));

      assertEquals("application/x-java-properties", mimeTypes.detectByFileName("Hans.properties").getName());
      assertEquals("application/x-java-properties", mimeTypes.detectByFileName("*.properties").getName());
      assertEquals("application/x-java-properties", mimeTypes.detectByFileName(".properties").getName());
      assertNull(mimeTypes.detectByFileName("properties"));

      assertEquals("application/x-java-properties", mimeTypes.detectByFileName("Hans.Properties").getName());
      assertEquals("application/x-java-properties", mimeTypes.detectByFileName("*.Properties").getName());
      assertEquals("application/x-java-properties", mimeTypes.detectByFileName(".Properties").getName());
      assertNull(mimeTypes.detectByFileName("Properties"));
   }

   @Test
   public void testBaseType()
   {
      MimeTypes mimeTypes = MimeTypes.getDefault();
      MimeType mimeType = mimeTypes.detectByFileName("*.xsd");
      assertEquals(mimeType.getName(), "application/xsd+xml");

      MimeType baseType = mimeType.getBaseType();
      assertNotNull(baseType);
      assertEquals(baseType.getName(), "application/xml");
   }
}
