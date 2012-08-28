/**
 * Copyright (c) 2012 Sourcepit.org contributors and others. All rights reserved. This program and the accompanying
 * materials are made available under the terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.sourcepit.common.utils.io;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.apache.commons.io.IOUtils;

public class ZipEntryResource implements IOResource<InputStream>
{
   private final IOResource<? extends ZipInputStream> resource;

   private final String entryName;

   public ZipEntryResource(IOResource<? extends InputStream> resource, String entryName)
   {
      this.resource = resource instanceof ZipInputStreamResource ? (ZipInputStreamResource) resource : IOResources
         .zipIn(resource);
      this.entryName = entryName;
   }

   public ZipInputStream open() throws IOException
   {
      final ZipInputStream zipIn = resource.open();
      try
      {
         ZipEntry zipEntry = zipIn.getNextEntry();
         while (zipEntry != null)
         {
            if (entryName.equals(zipEntry.getName()))
            {
               return zipIn;
            }
            zipEntry = zipIn.getNextEntry();
         }
         throw new FileNotFoundException(entryName);
      }
      catch (Throwable e)
      {
         IOUtils.closeQuietly(zipIn);
         if (e instanceof IOException)
         {
            throw (IOException) e;
         }
         if (e instanceof RuntimeException)
         {
            throw (RuntimeException) e;
         }
         if (e instanceof Error)
         {
            throw (Error) e;
         }
         throw new IllegalStateException(e);
      }
   }

}
