/*
 * Copyright (C) 2013 Bosch Software Innovations GmbH. All rights reserved.
 */

package org.sourcepit.common.utils.content;

import static org.junit.Assert.*;

import org.junit.Test;

public class JavaDefaultMimeTypesProviderTest
{
   @Test
   public void testForFileName()
   {
      MimeTypesProvider provider = new JavaDefaultMimeTypesProvider();
      assertEquals("application/xml", provider.forFileName("hans.xml"));
      assertEquals("application/xml", provider.forFileName("*.xml"));
      assertEquals("application/xml", provider.forFileName(".xml"));
      assertNull(provider.forFileName("xml"));
      
      assertEquals("application/xml", provider.forFileName("hans.Xml"));
      assertEquals("application/xml", provider.forFileName("*.Xml"));
      assertEquals("application/xml", provider.forFileName(".Xml"));
      assertNull(provider.forFileName("Xml"));
   }
}
