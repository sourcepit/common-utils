/*
 * Copyright (C) 2013 Bosch Software Innovations GmbH. All rights reserved.
 */

package org.sourcepit.common.utils.content.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

import org.sourcepit.common.utils.content.EncodingDeclarationParser;
import org.sourcepit.common.utils.props.PropertiesSource;

import com.ibm.icu.text.CharsetDetector;
import com.ibm.icu.text.CharsetMatch;


public abstract class AbstractEncodingDeclarationParser implements EncodingDeclarationParser
{
   @Override
   public String parse(InputStream content, PropertiesSource options) throws IOException
   {
      final String preliminaryEncoding = detectPreliminaryEncoding(content);
      content.mark(getBufferSize(options));
      try
      {
         return parse(new BufferedReader(new InputStreamReader(content, preliminaryEncoding)), options);
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

   protected abstract int getBufferSize(PropertiesSource options);

   protected abstract String parse(BufferedReader reader, PropertiesSource options) throws IOException;

}