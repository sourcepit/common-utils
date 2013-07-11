/*
 * Copyright (C) 2013 Bosch Software Innovations GmbH. All rights reserved.
 */

package org.sourcepit.common.utils.content;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

import com.ibm.icu.text.CharsetDetector;
import com.ibm.icu.text.CharsetMatch;


public abstract class AbstractEncodingDeclarationParser implements EncodingDeclarationParser
{
   @Override
   public String parse(InputStream content) throws IOException
   {
      final String preliminaryEncoding = detectPreliminaryEncoding(content);
      content.mark(getBufferSize());
      try
      {
         return parse(new BufferedReader(new InputStreamReader(content, preliminaryEncoding)));
      }
      finally
      {
         content.reset();
      }
   }

   private String detectPreliminaryEncoding(InputStream content) throws IOException
   {
      final CharsetDetector charsetDetector = new CharsetDetector();
      charsetDetector.setText(content);
      final CharsetMatch match = charsetDetector.detect();
      return match == null ? Charset.defaultCharset().name() : match.getName();
   }

   protected abstract int getBufferSize();

   protected abstract String parse(BufferedReader reader) throws IOException;

}