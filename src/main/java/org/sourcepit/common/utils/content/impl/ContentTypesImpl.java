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
