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

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.sourcepit.common.utils.content.MimeTypesProvider;
import org.sourcepit.common.utils.path.PathMatcher;
import org.sourcepit.common.utils.props.PropertiesSource;

public class BaseMimeTypesProvider implements MimeTypesProvider
{
   private final Map<PathMatcher, String> matcherToParent;

   public BaseMimeTypesProvider(Map<String, String> typeToBaseTypeMap)
   {
      matcherToParent = new HashMap<PathMatcher, String>();
      for (Entry<String, String> entry : typeToBaseTypeMap.entrySet())
      {
         final String key = entry.getKey();
         final String value = entry.getValue();
         PathMatcher matcher = PathMatcher.parse(key, "/", null);
         matcherToParent.put(matcher, value);
      }
   }

   @Override
   public String forFileName(String fileName, PropertiesSource options)
   {
      return null;
   }

   @Override
   public String forContent(InputStream content, PropertiesSource options) throws IOException
   {
      return null;
   }

   @Override
   public String getBaseType(String mimeType, PropertiesSource options)
   {
      for (Entry<PathMatcher, String> entry : matcherToParent.entrySet())
      {
         if (entry.getKey().isMatch(mimeType))
         {
            return entry.getValue();
         }
      }
      return null;
   }

}
