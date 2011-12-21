/**
 * Copyright (c) 2011 Sourcepit.org contributors and others. All rights reserved. This program and the accompanying
 * materials are made available under the terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
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
}
