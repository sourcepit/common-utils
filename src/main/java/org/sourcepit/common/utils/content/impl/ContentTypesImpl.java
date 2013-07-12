/**
 * Copyright (c) 2013 Sourcepit.org contributors and others. All rights reserved. This program and the accompanying
 * materials are made available under the terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.sourcepit.common.utils.content.impl;

import java.io.IOException;
import java.io.InputStream;

import org.sourcepit.common.utils.content.ContentType;
import org.sourcepit.common.utils.content.ContentTypes;
import org.sourcepit.common.utils.content.Encodings;
import org.sourcepit.common.utils.content.MimeType;
import org.sourcepit.common.utils.content.MimeTypes;
import org.sourcepit.common.utils.props.PropertiesSource;

public class ContentTypesImpl implements ContentTypes
{
   private static class Default
   {
      static final ContentTypes INSTANCE;
      static
      {
         INSTANCE = new ContentTypesImpl(MimeTypes.DEFAULT, Encodings.DEFAULT);
      }
   }

   public static ContentTypes getDefault()
   {
      return Default.INSTANCE;
   }

   private final MimeTypes mimeTypes;

   private final Encodings encodings;

   public ContentTypesImpl(MimeTypes mimeTypes, Encodings encodings)
   {
      this.mimeTypes = mimeTypes;
      this.encodings = encodings;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public ContentType detect(String fileName, InputStream content, String targetEncoding, PropertiesSource options)
      throws IOException
   {
      final MimeType mimeType = mimeTypes.detect(fileName, content, options);
      if (mimeType != null && isSupported(mimeType))
      {
         final String encoding = encodings.detect(mimeType, fileName, content, targetEncoding, options);
         return new ContentType(mimeType, encoding);
      }
      return null;
   }

   private boolean isSupported(MimeType mimeType)
   {
      return true;
   }
}
