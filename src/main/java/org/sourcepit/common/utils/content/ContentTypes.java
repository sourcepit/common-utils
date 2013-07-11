/**
 * Copyright (c) 2013 Sourcepit.org contributors and others. All rights reserved. This program and the accompanying
 * materials are made available under the terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.sourcepit.common.utils.content;

import java.io.IOException;
import java.io.InputStream;

public class ContentTypes
{
   private static class Instance
   {
      static final ContentTypes DEFAULT;
      static
      {
         DEFAULT = new ContentTypes(MimeTypes.getDefault(), Encodings.getDefault());
      }
   }

   public static ContentTypes getDefault()
   {
      return Instance.DEFAULT;
   }

   private final MimeTypes mimeTypes;

   private final Encodings encodings;

   public ContentTypes(MimeTypes mimeTypes, Encodings encodings)
   {
      this.mimeTypes = mimeTypes;
      this.encodings = encodings;
   }

   public ContentType detect(String fileName, InputStream content, String targetEncoding) throws IOException
   {
      final MimeType mimeType = mimeTypes.detect(fileName, content);
      if (mimeType != null && isSupported(mimeType))
      {
         final String encoding = encodings.detect(mimeType, fileName, content, targetEncoding);
         return new ContentType(mimeType, encoding);
      }
      return null;
   }

   private boolean isSupported(MimeType mimeType)
   {
      return true;
   }
}
