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
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.sourcepit.common.utils.content.MimeType;
import org.sourcepit.common.utils.content.MimeTypes;
import org.sourcepit.common.utils.content.MimeTypesProvider;
import org.sourcepit.common.utils.props.LinkedPropertiesMap;
import org.sourcepit.common.utils.props.PropertiesMap;
import org.sourcepit.common.utils.props.PropertiesSource;

public class MimeTypesImpl implements MimeTypes
{
   private static class Default
   {
      final static MimeTypes INSTANCE = create();

      private static MimeTypes create()
      {
         final String prefix = MimeTypes.class.getPackage().getName().replace(".", "/") + "/";
         final String name = MimeTypes.class.getSimpleName();
         final String extension = ".properties";

         final PropertiesMap mimeTypes = load(prefix + name + extension);
         final PropertiesMap baseMimeTypes = load(prefix + "Base" + name + extension);

         final List<MimeTypesProvider> providers = new ArrayList<MimeTypesProvider>(4);
         providers.add(FileNameMimeTypesProvider.forMimeTypeToFileNames(mimeTypes));
         providers.add(new JavaDefaultMimeTypesProvider());
         providers.add(new BaseMimeTypesProvider(baseMimeTypes));

         return new MimeTypesImpl(providers);
      }

      private static PropertiesMap load(String resourcePath)
      {
         final PropertiesMap properties = new LinkedPropertiesMap();
         properties.load(MimeTypesImpl.class.getClassLoader(), resourcePath);
         return properties;
      }
   }

   public static MimeTypes getDefault()
   {
      return Default.INSTANCE;
   }

   private final List<MimeTypesProvider> mimeTypeProviders;

   @Inject
   public MimeTypesImpl(List<MimeTypesProvider> mimeTypeProviders)
   {
      this.mimeTypeProviders = mimeTypeProviders;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public MimeType detect(String fileName, InputStream content, PropertiesSource options) throws IOException
   {
      MimeType mimeType = null;
      if (fileName != null)
      {
         mimeType = detectByFileName(fileName, options);
      }
      if (mimeType == null && content != null)
      {
         mimeType = detectByContent(content, options);
      }
      return mimeType;
   }


   /**
    * {@inheritDoc}
    */
   @Override
   public MimeType detectByFileName(String fileName, PropertiesSource options)
   {
      final String name = getMimeTypeNameForFileName(fileName, options);
      if (name != null)
      {
         return createMimeType(name, options);
      }
      return null;
   }

   private MimeType createMimeType(final String name, PropertiesSource options)
   {
      return new MimeType(name, getBaseType(name, options));
   }

   private MimeType getBaseType(final String mimeTypeName, PropertiesSource options)
   {
      final String baseTypeName = getBaseTypeName(mimeTypeName, options);
      if (baseTypeName != null)
      {
         return createMimeType(baseTypeName, options);
      }
      return null;
   }

   private String getBaseTypeName(String mimeTypeName, PropertiesSource options)
   {
      for (MimeTypesProvider provider : mimeTypeProviders)
      {
         final String baseTypeName = provider.getBaseType(mimeTypeName, options);
         if (baseTypeName != null)
         {
            return baseTypeName;
         }
      }
      return null;
   }

   private String getMimeTypeNameForFileName(String fileName, PropertiesSource options)
   {
      for (MimeTypesProvider provider : mimeTypeProviders)
      {
         final String mimeType = provider.forFileName(fileName, options);
         if (mimeType != null)
         {
            return mimeType;
         }
      }
      return null;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public MimeType detectByContent(InputStream content, PropertiesSource options) throws IOException
   {
      final String name = getMimeTypeNameForContent(content, options);
      if (name != null)
      {
         return createMimeType(name, options);
      }
      return null;
   }

   private String getMimeTypeNameForContent(InputStream content, PropertiesSource options) throws IOException
   {
      for (MimeTypesProvider provider : mimeTypeProviders)
      {
         final String mimeType = provider.forContent(content, options);
         if (mimeType != null)
         {
            return mimeType;
         }
      }
      return null;
   }
}
