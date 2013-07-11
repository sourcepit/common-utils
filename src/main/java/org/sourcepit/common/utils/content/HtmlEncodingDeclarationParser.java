/*
 * Copyright (C) 2013 Bosch Software Innovations GmbH. All rights reserved.
 */

package org.sourcepit.common.utils.content;

import java.io.BufferedReader;
import java.io.IOException;

public class HtmlEncodingDeclarationParser extends AbstractEncodingDeclarationParser
{
   @Override
   protected int getBufferSize()
   {
      return 512;
   }

   @Override
   protected String parse(BufferedReader reader) throws IOException
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
