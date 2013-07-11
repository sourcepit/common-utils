/**
 * Copyright (c) 2013 Sourcepit.org contributors and others. All rights reserved. This program and the accompanying
 * materials are made available under the terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.sourcepit.common.utils.content;


public final class MimeType
{
   private final String name;

   private final MimeType baseType;

   public MimeType(String name, MimeType baseType)
   {
      this.name = name;
      this.baseType = baseType;
   }

   public String getName()
   {
      return name;
   }

   public MimeType getBaseType()
   {
      return baseType;
   }

   @Override
   public int hashCode()
   {
      final int prime = 31;
      int result = 1;
      result = prime * result + ((baseType == null) ? 0 : baseType.hashCode());
      result = prime * result + ((name == null) ? 0 : name.hashCode());
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
      MimeType other = (MimeType) obj;
      if (baseType == null)
      {
         if (other.baseType != null)
         {
            return false;
         }
      }
      else if (!baseType.equals(other.baseType))
      {
         return false;
      }
      if (name == null)
      {
         if (other.name != null)
         {
            return false;
         }
      }
      else if (!name.equals(other.name))
      {
         return false;
      }
      return true;
   }

   @Override
   public String toString()
   {
      return "MimeType [name=" + name + ", baseType=" + baseType + "]";
   }
}
