/**
 * Copyright (c) 2013 Sourcepit.org contributors and others. All rights reserved. This program and the accompanying
 * materials are made available under the terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.sourcepit.common.utils.content.impl;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.sourcepit.common.utils.content.EncodingDeclarationParser;
import org.sourcepit.common.utils.content.EncodingProvider;
import org.sourcepit.common.utils.content.MimeType;
import org.sourcepit.common.utils.props.PropertiesSource;

public class MimeTypeEncodingProvider implements EncodingProvider
{
   public static MimeTypeEncodingProvider forMap(final Map<String, String> mimeTypeToEncodingSettingsMap)
   {
      final Map<String, String> defaultEncodings = new HashMap<String, String>(mimeTypeToEncodingSettingsMap.size());
      final Map<String, EncodingDeclarationParser> declarationParsers = new HashMap<String, EncodingDeclarationParser>(
         mimeTypeToEncodingSettingsMap.size());
      for (Entry<String, String> entry : mimeTypeToEncodingSettingsMap.entrySet())
      {
         final String mimeType = entry.getKey();

         final String[] values = entry.getValue().split(",");
         if (values.length > 0)
         {
            defaultEncodings.put(mimeType, values[0].trim());
            if (values.length > 1)
            {
               final String clazz = values[1].trim();
               declarationParsers.put(mimeType, newInstance(clazz));
            }
         }
      }
      return new MimeTypeEncodingProvider(defaultEncodings, declarationParsers);
   }


   @SuppressWarnings("unchecked")
   private static EncodingDeclarationParser newInstance(String name)
   {
      ClassLoader loader = Thread.currentThread().getContextClassLoader();

      Class<EncodingDeclarationParser> clazz = null;
      if (loader != null)
      {
         try
         {
            clazz = (Class<EncodingDeclarationParser>) loader.loadClass(name);
         }
         catch (ClassNotFoundException e)
         {
         }
      }

      if (clazz == null)
      {
         try
         {
            clazz = (Class<EncodingDeclarationParser>) EncodingsImpl.class.getClassLoader().loadClass(name);
         }
         catch (ClassNotFoundException e)
         {
            throw new IllegalStateException(e);
         }
      }

      try
      {
         return clazz.newInstance();
      }
      catch (InstantiationException e)
      {
         throw new IllegalStateException(e);
      }
      catch (IllegalAccessException e)
      {
         throw new IllegalStateException(e);
      }
   }

   private final Map<String, String> defaultEncodings;
   private final Map<String, EncodingDeclarationParser> declarationParsers;

   public MimeTypeEncodingProvider(Map<String, String> defaultEncodings,
      Map<String, EncodingDeclarationParser> declarationParsers)
   {
      this.defaultEncodings = defaultEncodings;
      this.declarationParsers = declarationParsers;
   }

   @Override
   public String getDefaultEncoding(MimeType mimeType, String fileName, String systemEncoding, PropertiesSource options)
   {
      String encoding = null;
      MimeType current = mimeType;
      while (current != null)
      {
         encoding = defaultEncodings.get(current.getName());
         if (encoding != null)
         {
            break;
         }
         current = current.getBaseType();
      }
      return "system".equals(encoding) ? systemEncoding : encoding;
   }

   @Override
   public String getDeclaredEncoding(MimeType mimeType, String fileName, PropertiesSource options)
   {
      return null;
   }

   @Override
   public String getDeclaredEncoding(MimeType mimeType, InputStream content, PropertiesSource options)
      throws IOException
   {
      EncodingDeclarationParser parser = null;
      MimeType current = mimeType;
      while (current != null)
      {
         parser = declarationParsers.get(current.getName());
         if (parser != null)
         {
            break;
         }
         current = current.getBaseType();
      }
      return parser == null ? null : parser.parse(content, options);
   }

}
