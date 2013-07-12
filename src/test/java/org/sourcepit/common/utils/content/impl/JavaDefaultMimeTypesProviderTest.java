/*
 * Copyright (C) 2013 Bosch Software Innovations GmbH. All rights reserved.
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
