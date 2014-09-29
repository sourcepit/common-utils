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
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.sourcepit.common.utils.content.MimeTypesProvider;
import org.sourcepit.common.utils.props.PropertiesSource;

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
   public String forFileName(String fileName, PropertiesSource options)
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
   public String forContent(InputStream content, PropertiesSource options) throws IOException
   {
      return null;
   }

   @Override
   public String getBaseType(String mimeType, PropertiesSource options)
   {
      return null;
   }
}
