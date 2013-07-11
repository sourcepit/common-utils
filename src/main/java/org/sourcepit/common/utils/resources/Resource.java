/**
 * Copyright (c) 2013 Sourcepit.org contributors and others. All rights reserved. This program and the accompanying
 * materials are made available under the terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.sourcepit.common.utils.resources;

import org.sourcepit.common.utils.path.Path;

public class Resource
{
   private final Path path;

   private final boolean directory;

   public Resource(Path path, boolean directory)
   {
      this.path = path;
      this.directory = directory;
   }

   public Path getPath()
   {
      return path;
   }

   public boolean isDirectory()
   {
      return directory;
   }

   @Override
   public int hashCode()
   {
      final int prime = 31;
      int result = 1;
      result = prime * result + (directory ? 1231 : 1237);
      result = prime * result + ((path == null) ? 0 : path.hashCode());
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
      Resource other = (Resource) obj;
      if (directory != other.directory)
      {
         return false;
      }
      if (path == null)
      {
         if (other.path != null)
         {
            return false;
         }
      }
      else if (!path.equals(other.path))
      {
         return false;
      }
      return true;
   }

   @Override
   public String toString()
   {
      return "Resource [path=" + path + ", directory=" + directory + "]";
   }
}
