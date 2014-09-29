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

import java.io.ByteArrayInputStream;
import java.io.IOException;

import org.junit.Test;

public class XMLEncodingDeclarationParserTest
{

   @Test
   public void test() throws IOException
   {
      String encoding = new XMLEncodingDeclarationParser().parse(new ByteArrayInputStream("".getBytes()), null);
      assertEquals("UTF-8", encoding);

      encoding = new XMLEncodingDeclarationParser().parse(new ByteArrayInputStream("<doc></doc>".getBytes()), null);
      assertEquals("UTF-8", encoding);

      encoding = new XMLEncodingDeclarationParser().parse(new ByteArrayInputStream(
         "<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>\n<doc></doc>".getBytes("ISO-8859-1")), null);
      assertEquals("ISO-8859-1", encoding);
   }

}
