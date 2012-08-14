/**
 * Copyright (c) 2012 Sourcepit.org contributors and others. All rights reserved. This program and the accompanying
 * materials are made available under the terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
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
