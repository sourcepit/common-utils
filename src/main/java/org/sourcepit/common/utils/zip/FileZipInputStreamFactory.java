/**
 * Copyright (c) 2011 Sourcepit.org contributors and others. All rights reserved. This program and the accompanying
 * materials are made available under the terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
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