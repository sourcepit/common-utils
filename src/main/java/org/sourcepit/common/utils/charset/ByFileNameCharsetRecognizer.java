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
import java.util.Map.Entry;

import javax.inject.Named;

import org.sourcepit.common.utils.props.LinkedPropertiesMap;
import org.sourcepit.common.utils.props.PropertiesMap;

@Named
public class ByFileNameCharsetRecognizer implements CharsetRecognizer
{
   private final static PropertiesMap MAPPINGS = new LinkedPropertiesMap();
   static
   {
      MAPPINGS.load(getClassLoader(), "META-INF/srcpit/charset.properties");
   }

   private static ClassLoader getClassLoader()
   {
      ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
      if (classLoader == null)
      {
         classLoader = ByFileNameCharsetRecognizer.class.getClassLoader();
      }
      return classLoader;
   }

   public boolean isImprecise()
   {
      return false;
   }


   public Collection<CharsetMatch> recognize(String fileName, String fileExtension, InputStream inputStream,
      Charset declaredCharset) throws IOException
   {
      final List<CharsetMatch> results = new ArrayList<CharsetMatch>();
      if (fileName != null || fileExtension != null)
      {
         for (Entry<String, String> entry : MAPPINGS.entrySet())
         {
            if (isMatch(entry.getKey(), fileName, fileExtension))
            {
               results.add(new CharsetMatch(entry.getValue(), 100));
            }
         }
      }
      return results;
   }

   private boolean isMatch(String pattern, String fileName, String fileExtension)
   {
      if (pattern.startsWith("*.") && fileExtension != null)
      {
         return fileExtension.equalsIgnoreCase(pattern.substring(2));
      }
      return pattern.equalsIgnoreCase(fileName);
   }
}
