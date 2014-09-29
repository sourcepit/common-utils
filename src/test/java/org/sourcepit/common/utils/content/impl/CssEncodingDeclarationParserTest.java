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

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.UnsupportedEncodingException;

import org.junit.Test;
import org.sourcepit.common.utils.content.EncodingDeclarationParser;

public class CssEncodingDeclarationParserTest
{
   @Test
   public void testname() throws Exception
   {
      EncodingDeclarationParser parser = new CssEncodingDeclarationParser();

      String encoding = parser.parse(asStream("@charset \"UTF-8\"", "UTF-8"), null);
      assertEquals("UTF-8", encoding);

      encoding = parser.parse(asStream("fooooo", "UTF-8"), null);
      assertEquals(null, encoding);

      encoding = parser.parse(asStream("@charset \"UTF-8", "UTF-8"), null);
      assertEquals(null, encoding);

      encoding = parser.parse(asStream("@charset \"UTF-8\n", "UTF-8"), null);
      assertEquals(null, encoding);

      encoding = parser.parse(asStream("@charset \"UTF-8\"", "UTF-32BE-BOM"), null);
      assertEquals("UTF-8", encoding);
   }

   static BufferedInputStream asStream(String string, String encoding) throws UnsupportedEncodingException
   {
      return new BufferedInputStream(new ByteArrayInputStream(string.getBytes(encoding)));
   }
}
