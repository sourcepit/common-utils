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

import org.sourcepit.common.utils.props.PropertiesSource;

public class HtmlEncodingDeclarationParser extends AbstractEncodingDeclarationParser
{
   @Override
   protected int getBufferSize(PropertiesSource options)
   {
      return 512;
   }

   @Override
   protected String parse(BufferedReader reader, PropertiesSource options) throws IOException
   {
      String line = reader.readLine();
      while (line != null)
      {
         String encoding = parseLine(line.toLowerCase(), line);
         if (encoding != null)
         {
            return encoding;
         }
         line = reader.readLine();
      }
      return null;
   }

   private String parseLine(String normalizedLine, String line)
   {
      int start = normalizedLine.indexOf("<meta charset=\"");
      if (start > -1)
      {
         start += 15;

         normalizedLine = normalizedLine.substring(start);
         int end = normalizedLine.indexOf('\"');
         if (end > -1)
         {
            return line.substring(start, start + end);
         }
      }

      start = normalizedLine.indexOf("<meta http-equiv=\"content-type\" content=\"");
      if (start > -1)
      {
         start += 41;
         normalizedLine = normalizedLine.substring(start);

         int tmp = normalizedLine.indexOf("charset=");
         if (tmp > -1)
         {
            start += tmp + 8;
            normalizedLine = normalizedLine.substring(tmp + 8);

            int end = normalizedLine.indexOf('\"');
            if (end > -1)
            {
               return line.substring(start, start + end);
            }
         }
      }

      return null;
   }
}
