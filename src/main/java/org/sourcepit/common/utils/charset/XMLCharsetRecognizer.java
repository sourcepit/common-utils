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
import java.util.Collection;
import java.util.Collections;

import javax.inject.Named;

import org.sourcepit.common.utils.lang.PipedException;
import org.sourcepit.common.utils.xml.SAXEncodingDetector;

@Named
public class XMLCharsetRecognizer implements CharsetRecognizer
{
   public Collection<CharsetMatch> recognize(String fileName, String fileExtension, InputStream inputStream,
      Charset declaredCharset) throws IOException
   {
      if (inputStream != null)
      {
         try
         {
            String encoding = SAXEncodingDetector.parse(inputStream);
            CharsetMatch match = new CharsetMatch(encoding, 100);
            return Collections.singleton(match);
         }
         catch (PipedException e)
         {
            e.adaptAndThrow(IOException.class);
         }
      }
      return null;
   }
}
