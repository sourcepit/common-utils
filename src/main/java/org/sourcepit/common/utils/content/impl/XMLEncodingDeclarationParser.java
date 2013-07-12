/**
 * Copyright (c) 2012 Sourcepit.org contributors and others. All rights reserved. This program and the accompanying
 * materials are made available under the terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.sourcepit.common.utils.content.impl;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import org.apache.commons.io.IOUtils;
import org.sourcepit.common.utils.content.EncodingDeclarationParser;
import org.sourcepit.common.utils.props.PropertiesSource;

public class XMLEncodingDeclarationParser implements EncodingDeclarationParser
{
   @Override
   public String parse(InputStream content, PropertiesSource options) throws IOException
   {
      return getXMLEncoding(read(content, 200));
   }

   private byte[] read(InputStream content, int max) throws IOException
   {
      byte[] buffer = new byte[max];
      final int length = IOUtils.read(content, buffer);

      byte[] result;
      if (length < buffer.length)
      {
         result = new byte[length];
         System.arraycopy(buffer, 0, result, 0, length);
      }
      else
      {
         result = buffer;
      }
      return result;
   }

   public static String getXMLEncoding(byte[] bytes)
   {
      String javaEncoding = null;

      if (bytes.length >= 4)
      {
         if (((bytes[0] == -2) && (bytes[1] == -1)) || ((bytes[0] == 0) && (bytes[1] == 60)))
            javaEncoding = "UnicodeBig";
         else if (((bytes[0] == -1) && (bytes[1] == -2)) || ((bytes[0] == 60) && (bytes[1] == 0)))
            javaEncoding = "UnicodeLittle";
         else if ((bytes[0] == -17) && (bytes[1] == -69) && (bytes[2] == -65))
            javaEncoding = "UTF8";
      }

      String header = null;

      try
      {
         if (javaEncoding != null)
            header = new String(bytes, 0, bytes.length, javaEncoding);
         else
            header = new String(bytes, 0, bytes.length);
      }
      catch (UnsupportedEncodingException e)
      {
         return null;
      }

      if (!header.startsWith("<?xml"))
         return "UTF-8";

      int endOfXMLPI = header.indexOf("?>");
      int encodingIndex = header.indexOf("encoding", 6);

      if ((encodingIndex == -1) || (encodingIndex > endOfXMLPI))
         return "UTF-8";

      int firstQuoteIndex = header.indexOf('"', encodingIndex);
      int lastQuoteIndex;

      if ((firstQuoteIndex == -1) || (firstQuoteIndex > endOfXMLPI))
      {
         firstQuoteIndex = header.indexOf('\'', encodingIndex);
         lastQuoteIndex = header.indexOf('\'', firstQuoteIndex + 1);
      }
      else
         lastQuoteIndex = header.indexOf('"', firstQuoteIndex + 1);

      return header.substring(firstQuoteIndex + 1, lastQuoteIndex);
   }
}
