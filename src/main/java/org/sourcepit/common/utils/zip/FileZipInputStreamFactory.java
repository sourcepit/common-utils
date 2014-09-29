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

package org.sourcepit.common.utils.zip;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.zip.ZipInputStream;

import org.apache.commons.io.IOUtils;

public class FileZipInputStreamFactory implements ZipInputStreamFactory
{
   private static final int MAX_BUFF = 1024 * 1000 * 12;

   protected final File zipFile;

   protected final int bufferSize;

   public FileZipInputStreamFactory(File zipFile)
   {
      this(zipFile, 8192);
   }

   public FileZipInputStreamFactory(File zipFile, int bufferSize)
   {
      this.zipFile = zipFile;
      this.bufferSize = bufferSize;
   }

   private volatile byte[] buffer = null;

   public ZipInputStream newZipInputStream() throws IOException
   {
      final int numberOfBytes = getNumberOfBytes();
      if (numberOfBytes > 0 && numberOfBytes < MAX_BUFF)
      {
         if (buffer == null)
         {
            synchronized (this)
            {
               if (buffer == null)
               {
                  buffer = new byte[numberOfBytes];

                  final FileInputStream fis = new FileInputStream(zipFile);
                  try
                  {
                     fis.read(buffer);
                  }
                  finally
                  {
                     IOUtils.closeQuietly(fis);
                  }
               }
            }
         }
         return new ZipInputStream(new ByteArrayInputStream(buffer));
      }

      return new ZipInputStream(new BufferedInputStream(new FileInputStream(zipFile), bufferSize));
   }

   public int getNumberOfBytes()
   {
      final int length = (int) zipFile.length();
      return length > 0 ? length : -1;
   }
}