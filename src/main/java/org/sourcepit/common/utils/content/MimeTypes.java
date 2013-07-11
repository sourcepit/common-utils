/**
 * Copyright (c) 2013 Sourcepit.org contributors and others. All rights reserved. This program and the accompanying
 * materials are made available under the terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.sourcepit.common.utils.content;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.sourcepit.common.utils.props.LinkedPropertiesMap;
import org.sourcepit.common.utils.props.PropertiesMap;

public class MimeTypes
{
   private static class Instance
   {
      final static MimeTypes DEFAULT = create();

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

         return new MimeTypes(providers);
      }

      private static PropertiesMap load(String resourcePath)
      {
         final PropertiesMap properties = new LinkedPropertiesMap();
         properties.load(MimeTypes.class.getClassLoader(), resourcePath);
         return properties;
      }
   }

   public static MimeTypes getDefault()
   {
      return Instance.DEFAULT;
   }

   private final List<MimeTypesProvider> mimeTypeProviders;

   @Inject
   public MimeTypes(List<MimeTypesProvider> mimeTypeProviders)
   {
      this.mimeTypeProviders = mimeTypeProviders;
   }
   
   public MimeType detect(String fileName, InputStream content) throws IOException
   {
      MimeType mimeType = null;
      if (fileName != null)
      {
         mimeType = detectByFileName(fileName);
      }
      if (mimeType == null && content != null)
      {
         mimeType = detectByContent(content);
      }
      return mimeType;
   }


   public MimeType detectByFileName(String fileName)
   {
      final String name = getMimeTypeNameForFileName(fileName);
      if (name != null)
      {
         return createMimeType(name);
      }
      return null;
   }

   private MimeType createMimeType(final String name)
   {
      return new MimeType(name, getBaseType(name));
   }

   private MimeType getBaseType(final String mimeTypeName)
   {
      final String baseTypeName = getBaseTypeName(mimeTypeName);
      if (baseTypeName != null)
      {
         return createMimeType(baseTypeName);
      }
      return null;
   }

   private String getBaseTypeName(String mimeTypeName)
   {
      for (MimeTypesProvider provider : mimeTypeProviders)
      {
         final String baseTypeName = provider.getBaseType(mimeTypeName);
         if (baseTypeName != null)
         {
            return baseTypeName;
         }
      }
      return null;
   }

   private String getMimeTypeNameForFileName(String fileName)
   {
      for (MimeTypesProvider provider : mimeTypeProviders)
      {
         final String mimeType = provider.forFileName(fileName);
         if (mimeType != null)
         {
            return mimeType;
         }
      }
      return null;
   }

   public MimeType detectByContent(InputStream content) throws IOException
   {
      final String name = getMimeTypeNameForContent(content);
      if (name != null)
      {
         return createMimeType(name);
      }
      return null;
   }

   private String getMimeTypeNameForContent(InputStream content) throws IOException
   {
      for (MimeTypesProvider provider : mimeTypeProviders)
      {
         final String mimeType = provider.forContent(content);
         if (mimeType != null)
         {
            return mimeType;
         }
      }
      return null;
   }
}
