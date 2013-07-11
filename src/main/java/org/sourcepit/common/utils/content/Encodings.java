/**
 * Copyright (c) 2013 Sourcepit.org contributors and others. All rights reserved. This program and the accompanying
 * materials are made available under the terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.sourcepit.common.utils.content;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

import org.sourcepit.common.utils.props.LinkedPropertiesMap;
import org.sourcepit.common.utils.props.PropertiesMap;


public class Encodings
{
   private static class Instance
   {
      static final Encodings DEFAULT;
      static
      {
         DEFAULT = create();
      }

      private static Encodings create()
      {
         final String name = Encodings.class.getName().replace(".", "/");
         final String extension = ".properties";
         final PropertiesMap encProps = load(name + extension);
         return new Encodings(Arrays.asList(MimeTypeEncodingProvider.forMap(encProps)));
      }

      private static PropertiesMap load(String resourcePath)
      {
         final PropertiesMap properties = new LinkedPropertiesMap();
         properties.load(MimeTypes.class.getClassLoader(), resourcePath);
         return properties;
      }
   }

   public static Encodings getDefault()
   {
      return Instance.DEFAULT;
   }

   private List<? extends EncodingProvider> encodingProviders;

   public Encodings(List<? extends EncodingProvider> encodingProviders)
   {
      this.encodingProviders = encodingProviders;
   }

   public String detect(MimeType mimeType, String fileName, InputStream content, String targetEncoding)
      throws IOException
   {
      String encoding = getDeclaredEncoding(mimeType, fileName, content);
      if (encoding == null)
      {
         encoding = getDefaultEncoding(mimeType, fileName, targetEncoding);
      }
      return encoding;
   }

   private String getDefaultEncoding(MimeType mimeType, String fileName, String targetEncoding)
   {
      for (EncodingProvider provider : encodingProviders)
      {
         final String encoding = provider.getDefaultEncoding(mimeType, fileName, targetEncoding);
         if (encoding != null)
         {
            return encoding;
         }
      }
      return null;
   }

   private String getDeclaredEncoding(MimeType mimeType, String fileName, InputStream content) throws IOException
   {
      String encoding = null;

      if (fileName != null)
      {
         encoding = getDeclaredEncoding(mimeType, fileName);
      }

      if (encoding == null && content != null)
      {
         encoding = getDeclaredEncoding(mimeType, content);
      }

      return encoding;
   }

   private String getDeclaredEncoding(MimeType mimeType, String fileName)
   {
      for (EncodingProvider provider : encodingProviders)
      {
         final String encoding = provider.getDeclaredEncoding(mimeType, fileName);
         if (encoding != null)
         {
            return encoding;
         }
      }
      return null;
   }

   private String getDeclaredEncoding(MimeType mimeType, InputStream content) throws IOException
   {
      for (EncodingProvider provider : encodingProviders)
      {
         final String encoding = provider.getDeclaredEncoding(mimeType, content);
         if (encoding != null)
         {
            return encoding;
         }
      }
      return null;
   }
}
