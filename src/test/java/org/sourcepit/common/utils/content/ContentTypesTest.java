/*
 * Copyright (C) 2013 Bosch Software Innovations GmbH. All rights reserved.
 */

package org.sourcepit.common.utils.content;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.junit.Test;

public class ContentTypesTest
{

   @Test
   public void testJsonUtf32() throws IOException
   {
      final ContentTypes contentTypes = ContentTypes.DEFAULT;
      InputStream content = new BufferedInputStream(new ByteArrayInputStream("ü".getBytes("UTF-32LE")));
      ContentType contentType = contentTypes.detect("*.json", content, "ISO-8859-1", null);
      assertNotNull(contentType);
      assertEquals("UTF-32LE", contentType.getEncoding());
   }
}
