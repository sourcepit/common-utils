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

package org.sourcepit.common.utils.charset;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.inject.Named;

import com.ibm.icu.text.CharsetDetector;
import com.ibm.icu.text.CharsetMatch;

@Named
public class ICUCharsetRecognizer implements CharsetRecognizer
{
   public Collection<org.sourcepit.common.utils.charset.CharsetMatch> recognize(String fileName, String fileExtension,
      InputStream inputStream, Charset declaredCharset) throws IOException
   {
      if (inputStream != null)
      {
         return recognize(inputStream, declaredCharset);
      }
      return null;
   }

   private Collection<org.sourcepit.common.utils.charset.CharsetMatch> recognize(InputStream inputStream,
      Charset declaredCharset) throws IOException
   {
      return filter(detectAll(inputStream, declaredCharset));
   }

   private CharsetMatch[] detectAll(InputStream inputStream, Charset declaredCharset) throws IOException
   {
      final CharsetDetector charsetDetector = new CharsetDetector();
      charsetDetector.setDeclaredEncoding(declaredCharset.name());
      charsetDetector.setText(inputStream);
      return charsetDetector.detectAll();
   }

   private Collection<org.sourcepit.common.utils.charset.CharsetMatch> filter(CharsetMatch[] detected)
   {
      final List<org.sourcepit.common.utils.charset.CharsetMatch> results = new ArrayList<org.sourcepit.common.utils.charset.CharsetMatch>();
      for (CharsetMatch charsetMatch : detected)
      {
         results.add(new org.sourcepit.common.utils.charset.CharsetMatch(charsetMatch.getName(), charsetMatch
            .getConfidence()));
      }
      return results;
   }
}