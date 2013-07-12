/*
 * Copyright (C) 2013 Bosch Software Innovations GmbH. All rights reserved.
 */

package org.sourcepit.common.utils.content.impl;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.sourcepit.common.utils.content.EncodingProvider;
import org.sourcepit.common.utils.content.MimeType;

public class MimeTypeEncodingProviderTest
{

   @Test
   public void testDefaultEncoding()
   {
      MimeType text = new MimeType("text/plain", null);
      MimeType xml = new MimeType("application/xml", null);
      MimeType xsd = new MimeType("application/xsd+xml", xml);
      MimeType foo = new MimeType("text/foo", null);

      Map<String, String> map = new HashMap<String, String>();
      map.put(text.getName(), "system");
      map.put(xml.getName(), "UTF-8");

      EncodingProvider provier = MimeTypeEncodingProvider.forMap(map);

      // unknown
      assertEquals(null, provier.getDefaultEncoding(foo, null, null, null));
      assertEquals(null, provier.getDefaultEncoding(foo, null, "ISO-8859-1", null));
      assertEquals(null, provier.getDefaultEncoding(foo, null, "UTF-8", null));

      // system
      assertEquals(null, provier.getDefaultEncoding(text, null, null, null));
      assertEquals("ISO-8859-1", provier.getDefaultEncoding(text, null, "ISO-8859-1", null));
      assertEquals("UTF-8", provier.getDefaultEncoding(text, null, "UTF-8", null));

      // utf-8
      assertEquals("UTF-8", provier.getDefaultEncoding(xml, null, null, null));
      assertEquals("UTF-8", provier.getDefaultEncoding(xml, null, "ISO-8859-1", null));
      assertEquals("UTF-8", provier.getDefaultEncoding(xml, null, "UTF-8", null));

      // inherited from xml
      assertEquals("UTF-8", provier.getDefaultEncoding(xsd, null, null, null));
      assertEquals("UTF-8", provier.getDefaultEncoding(xsd, null, "ISO-8859-1", null));
      assertEquals("UTF-8", provier.getDefaultEncoding(xsd, null, "UTF-8", null));
   }
}
