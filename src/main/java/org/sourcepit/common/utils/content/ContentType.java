/**
 * Copyright (c) 2013 Sourcepit.org contributors and others. All rights reserved. This program and the accompanying
 * materials are made available under the terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.sourcepit.common.utils.content;


public final class ContentType
{
   private final MimeType mimeType;
   private final String encoding;

   public ContentType(MimeType mimeType, String encoding)
   {
      this.mimeType = mimeType;
      this.encoding = encoding;
   }

   public MimeType getMimeType()
   {
      return mimeType;
   }

   public String getEncoding()
   {
      return encoding;
   }

   @Override
   public int hashCode()
   {
      final int prime = 31;
      int result = 1;
      result = prime * result + ((encoding == null) ? 0 : encoding.hashCode());
      result = prime * result + ((mimeType == null) ? 0 : mimeType.hashCode());
      return result;
   }

   @Override
   public boolean equals(Object obj)
   {
      if (this == obj)
      {
         return true;
      }
      if (obj == null)
      {
         return false;
      }
      if (getClass() != obj.getClass())
      {
         return false;
      }
      ContentType other = (ContentType) obj;
      if (encoding == null)
      {
         if (other.encoding != null)
         {
            return false;
         }
      }
      else if (!encoding.equals(other.encoding))
      {
         return false;
      }
      if (mimeType == null)
      {
         if (other.mimeType != null)
         {
            return false;
         }
      }
      else if (!mimeType.equals(other.mimeType))
      {
         return false;
      }
      return true;
   }

   @Override
   public String toString()
   {
      return "ContentType [mimeType=" + mimeType + ", encoding=" + encoding + "]";
   }
}
