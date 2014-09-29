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

package org.sourcepit.common.utils.path;

import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author Bernd
 */
public final class Path
{
   public static final String SEPARATOR = "/";

   private final String path;

   private String normalizedPath;

   private String osPath;

   private List<String> segments;

   private String fileExtension;

   private Boolean hasFileExtension;

   private String fileName;

   public Path(String path)
   {
      if (path == null)
      {
         throw new IllegalArgumentException();
      }
      this.path = path;
   }

   private String getNormalizedPath()
   {
      if (normalizedPath == null)
      {
         normalizedPath = PathUtils.normalize(path, SEPARATOR);
      }
      return normalizedPath;
   }

   private String getOSPath()
   {
      if (osPath == null)
      {
         osPath = PathUtils.normalize(path, File.separator);
      }
      return osPath;
   }

   private Path parent;

   public Path getParent()
   {
      if (parent == null)
      {
         final String normalizedPath = getNormalizedPath();
         int idx = normalizedPath.lastIndexOf(SEPARATOR);
         if (idx > 0)
         {
            parent = new Path(normalizedPath.substring(0, idx));
         }
      }
      return parent;
   }

   public List<String> getSegments()
   {
      if (segments == null)
      {
         String normalized = getNormalizedPath();
         if (normalized.startsWith(SEPARATOR))
         {
            normalized = normalized.substring(1);
         }
         segments = Collections.unmodifiableList(Arrays.asList(normalized.split(SEPARATOR)));
      }
      return segments;
   }

   public String getFirstSegment()
   {
      final List<String> segments = getSegments();
      return segments.get(0);
   }

   public String getLastSegment()
   {
      final List<String> segments = getSegments();
      return segments.get(segments.size() - 1);
   }

   public String getFileName()
   {
      getFileExtension();
      return fileName;
   }

   public String getFileExtension()
   {
      if (hasFileExtension == null)
      {
         final String normalizedPath = getLastSegment();
         final int idx = normalizedPath.lastIndexOf('.');
         if (idx > -1 && idx < normalizedPath.length() - 1)
         {
            fileName = idx == 0 ? null : normalizedPath.substring(0, idx);
            fileExtension = normalizedPath.substring(idx + 1);
         }
         hasFileExtension = fileExtension == null ? Boolean.FALSE : Boolean.TRUE;
      }
      return fileExtension;
   }

   @Override
   public String toString()
   {
      return getNormalizedPath();
   }

   public String toOSString()
   {
      return getOSPath();
   }

   @Override
   public int hashCode()
   {
      final int prime = 31;
      int result = 1;
      result = prime * result + getNormalizedPath().hashCode();
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
      final Path other = (Path) obj;
      if (!getNormalizedPath().equals(other.getNormalizedPath()))
      {
         return false;
      }
      return true;

   }
}
