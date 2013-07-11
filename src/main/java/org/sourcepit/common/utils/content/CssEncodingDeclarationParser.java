/*
 * Copyright (C) 2013 Bosch Software Innovations GmbH. All rights reserved.
 */

package org.sourcepit.common.utils.content;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Arrays;

public class CssEncodingDeclarationParser extends AbstractEncodingDeclarationParser
   implements
      EncodingDeclarationParser
{
   private final char[] prefix = "@charset \"".toCharArray();

   @Override
   protected int getBufferSize()
   {
      return 200;
   }

   @Override
   protected String parse(BufferedReader reader) throws IOException
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