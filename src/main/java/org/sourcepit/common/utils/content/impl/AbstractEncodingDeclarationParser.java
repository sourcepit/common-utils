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