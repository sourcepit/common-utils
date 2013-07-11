/*
 * Copyright (C) 2013 Bosch Software Innovations GmbH. All rights reserved.
 */

package org.sourcepit.common.utils.content;

import static org.junit.Assert.assertEquals;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.UnsupportedEncodingException;

import org.junit.Test;

public class CssEncodingDeclarationParserTest
{
   @Test
   public void testname() throws Exception
   {
      EncodingDeclarationParser parser = new CssEncodingDeclarationParser();

      String encoding = parser.parse(asStream("@charset \"UTF-8\"", "UTF-8"));
      assertEquals("UTF-8", encoding);

      encoding = parser.parse(asStream("fooooo", "UTF-8"));
      assertEquals(null, encoding);

      encoding = parser.parse(asStream("@charset \"UTF-8", "UTF-8"));
      assertEquals(null, encoding);

      encoding = parser.parse(asStream("@charset \"UTF-8\n", "UTF-8"));
      assertEquals(null, encoding);

      encoding = parser.parse(asStream("@charset \"UTF-8\"", "UTF-32BE-BOM"));
      assertEquals("UTF-8", encoding);
   }

   static BufferedInputStream asStream(String string, String encoding) throws UnsupportedEncodingException
   {
      return new BufferedInputStream(new ByteArrayInputStream(string.getBytes(encoding)));
   }
}
