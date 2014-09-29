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
