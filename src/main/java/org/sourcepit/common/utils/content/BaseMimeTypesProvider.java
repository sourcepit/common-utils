/**
 * Copyright (c) 2012 Sourcepit.org contributors and others. All rights reserved. This program and the accompanying
 * materials are made available under the terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.sourcepit.common.utils.content;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.sourcepit.common.utils.path.PathMatcher;

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
   public String forFileName(String fileName)
   {
      return null;
   }

   @Override
   public String forContent(InputStream content) throws IOException
   {
      return null;
   }

   @Override
   public String getBaseType(String mimeType)
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
