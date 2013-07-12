/**
 * Copyright (c) 2013 Sourcepit.org contributors and others. All rights reserved. This program and the accompanying
 * materials are made available under the terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.sourcepit.common.utils.content.impl;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

import org.sourcepit.common.utils.content.EncodingProvider;
import org.sourcepit.common.utils.content.Encodings;
import org.sourcepit.common.utils.content.MimeType;
import org.sourcepit.common.utils.props.LinkedPropertiesMap;
import org.sourcepit.common.utils.props.PropertiesMap;
import org.sourcepit.common.utils.props.PropertiesSource;


public class EncodingsImpl implements Encodings
{
   private static class Default
   {
      static final Encodings INSTANCE;
      static
      {
         INSTANCE = create();
      }

      private static Encodings create()
      {
         final String name = Encodings.class.getName().replace(".", "/");
         final String extension = ".properties";
         final PropertiesMap encProps = load(name + extension);
         return new EncodingsImpl(Arrays.asList(MimeTypeEncodingProvider.forMap(encProps)));
      }

      private static PropertiesMap load(String resourcePath)
      {
         final PropertiesMap properties = new LinkedPropertiesMap();
         properties.load(MimeTypesImpl.class.getClassLoader(), resourcePath);
         return properties;
      }
   }

   public static Encodings getDefault()
   {
      return Default.INSTANCE;
   }

   private List<? extends EncodingProvider> encodingProviders;

   public EncodingsImpl(List<? extends EncodingProvider> encodingProviders)
   {
      this.encodingProviders = encodingProviders;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public String detect(MimeType mimeType, String fileName, InputStream content, String targetEncoding,
      PropertiesSource options) throws IOException
   {
      String encoding = getDeclaredEncoding(mimeType, fileName, content, options);
      if (encoding == null)
      {
         encoding = getDefaultEncoding(mimeType, fileName, targetEncoding, options);
      }
      return encoding;
   }

   private String getDefaultEncoding(MimeType mimeType, String fileName, String targetEncoding, PropertiesSource options)
   {
      for (EncodingProvider provider : encodingProviders)
      {
         final String encoding = provider.getDefaultEncoding(mimeType, fileName, targetEncoding, options);
         if (encoding != null)
         {
            return encoding;
         }
      }
      return null;
   }

   private String getDeclaredEncoding(MimeType mimeType, String fileName, InputStream content, PropertiesSource options)
      throws IOException
   {
      String encoding = null;

      if (fileName != null)
      {
         encoding = getDeclaredEncoding(mimeType, fileName, options);
      }

      if (encoding == null && content != null)
      {
         encoding = getDeclaredEncoding(mimeType, content, options);
      }

      return encoding;
   }

   private String getDeclaredEncoding(MimeType mimeType, String fileName, PropertiesSource options)
   {
      for (EncodingProvider provider : encodingProviders)
      {
         final String encoding = provider.getDeclaredEncoding(mimeType, fileName, options);
         if (encoding != null)
         {
            return encoding;
         }
      }
      return null;
   }

   private String getDeclaredEncoding(MimeType mimeType, InputStream content, PropertiesSource options)
      throws IOException
   {
      for (EncodingProvider provider : encodingProviders)
      {
         final String encoding = provider.getDeclaredEncoding(mimeType, content, options);
         if (encoding != null)
         {
            return encoding;
         }
      }
      return null;
   }
}
