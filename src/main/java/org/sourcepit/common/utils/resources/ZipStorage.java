/**
 * Copyright (c) 2013 Sourcepit.org contributors and others. All rights reserved. This program and the accompanying
 * materials are made available under the terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
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
