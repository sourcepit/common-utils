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

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Arrays;

import org.sourcepit.common.utils.content.EncodingDeclarationParser;
import org.sourcepit.common.utils.props.PropertiesSource;

public class CssEncodingDeclarationParser extends AbstractEncodingDeclarationParser
   implements
      EncodingDeclarationParser
{
   private final char[] prefix = "@charset \"".toCharArray();

   @Override
   protected int getBufferSize(PropertiesSource options)
   {
      return 200;
   }

   @Override
   protected String parse(BufferedReader reader, PropertiesSource options) throws IOException
   {
      final char[] cbuf = new char[10];
      int length = reader.read(cbuf);
      if (length == 10 && Arrays.equals(prefix, cbuf))
      {
         StringBuilder sb = new StringBuilder();
         int c = reader.read();
         while (c != -1 && c != '\"' && c != '\n')
         {
            sb.append((char) c);
            c = reader.read();
         }

         if (c == '\"')
         {
            return sb.toString();
         }
      }
      return null;
   }
}