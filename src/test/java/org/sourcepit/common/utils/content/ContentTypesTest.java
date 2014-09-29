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
