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
