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

package org.sourcepit.common.utils.resources;

import static org.apache.commons.io.IOUtils.copy;
import static org.sourcepit.common.utils.io.IO.buffOut;
import static org.sourcepit.common.utils.io.IO.fileOut;
import static org.sourcepit.common.utils.io.IO.zipOut;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.nio.charset.Charset;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.sourcepit.common.utils.io.IOHandle;

public class ZipStorage extends ResourceStorage
{
   private final IOHandle<ZipOutputStream> zipHandle;

   private ZipOutputStream zipStream;

   public ZipStorage(File zipFile)
   {
      this(zipOut(buffOut(fileOut(zipFile))));
   }

   public ZipStorage(IOHandle<ZipOutputStream> zipHandle)
   {
      this.zipHandle = zipHandle;
   }

   @Override
   protected void doOpen() throws IOException
   {
      zipStream = zipHandle.open();
   }

   @Override
   protected void doClose() throws IOException
   {
      zipStream.close();
      zipStream = null;
   }

   @Override
   public void put(Resource resource, InputStream content) throws IOException
   {
      if (zipStream == null)
      {
         throw new IOException("Zip storage is not open.");
      }
      final ZipEntry zipEntry = toZipEntry(resource);
      zipStream.putNextEntry(zipEntry);
      try
      {
         if (!zipEntry.isDirectory())
         {
            copy(content, zipStream);
         }
      }
      finally
      {
         zipStream.closeEntry();
      }
   }

   @Override
   public void put(Resource resource, Reader content, Charset charset) throws IOException
   {
      if (zipStream == null)
      {
         throw new IOException("Zip storage is not open.");
      }
      final ZipEntry zipEntry = toZipEntry(resource);
      zipStream.putNextEntry(zipEntry);
      try
      {
         if (!zipEntry.isDirectory())
         {
            copy(content, zipStream, charset);
         }
      }
      finally
      {
         zipStream.closeEntry();
      }
   }

   private static ZipEntry toZipEntry(Resource resource)
   {
      String name = resource.getPath().toString();
      if (resource.isDirectory() && !name.endsWith("/"))
      {
         name += "/";
      }
      return new ZipEntry(name);
   }
}
