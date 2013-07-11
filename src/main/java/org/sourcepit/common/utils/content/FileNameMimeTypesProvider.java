/**
 * Copyright (c) 2013 Sourcepit.org contributors and others. All rights reserved. This program and the accompanying
 * materials are made available under the terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.sourcepit.common.utils.content;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class FileNameMimeTypesProvider implements MimeTypesProvider
{
   private final Map<String, String> fileNameToMimeTypeMap;

   public static FileNameMimeTypesProvider forMimeTypeToFileNames(Map<String, String> mimeTypeToFileNamesMap)
   {
      final Map<String, String> fileNameToMimeTypeMap = new HashMap<String, String>();
      for (Entry<String, String> entry : mimeTypeToFileNamesMap.entrySet())
      {
         for (String fileName : entry.getValue().split(","))
         {
            fileNameToMimeTypeMap.put(fileName.trim().toLowerCase(), entry.getKey());
         }
      }
      return forFileNameToMimeType(fileNameToMimeTypeMap);
   }

   public static FileNameMimeTypesProvider forFileNameToMimeType(Map<String, String> fileNameToMimeTypeMap)
   {
      return new FileNameMimeTypesProvider(fileNameToMimeTypeMap);
   }

   public FileNameMimeTypesProvider(Map<String, String> fileNameToMimeTypeMap)
   {
      this.fileNameToMimeTypeMap = fileNameToMimeTypeMap;
   }

   @Override
   public String forFileName(String fileName)
   {
      fileName = fileName.toLowerCase();

      String mimeType = fileNameToMimeTypeMap.get(fileName);
      if (mimeType != null)
      {
         return mimeType;
      }

      final int idx = fileName.lastIndexOf(".");
      if (idx > -1 && idx < fileName.length() - 1)
      {
         mimeType = forFileExtension(fileName.substring(idx + 1));
         if (mimeType != null)
         {
            return mimeType;
         }
      }
      return null;
   }

   private String forFileExtension(String fileExtension)
   {
      return fileNameToMimeTypeMap.get("*." + fileExtension);
   }

   @Override
   public String forContent(InputStream content) throws IOException
   {
      return null;
   }

   @Override
   public String getBaseType(String mimeType)
   {
      return null;
   }
}
