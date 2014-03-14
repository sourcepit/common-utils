/**
 * Copyright (c) 2012 Sourcepit.org contributors and others. All rights reserved. This program and the accompanying
 * materials are made available under the terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.sourcepit.common.utils.io.handles.impl;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.apache.commons.io.IOUtils;
import org.sourcepit.common.utils.io.IOHandle;
import org.sourcepit.common.utils.io.IO;
import org.sourcepit.common.utils.io.handles.ZipInputStreamHandle;

public class ZipEntryInputStreamHandleImpl implements ZipInputStreamHandle
{
   private final IOHandle<? extends ZipInputStream> resource;

   private final String entryName;

   public ZipEntryInputStreamHandleImpl(IOHandle<? extends InputStream> resource, String entryName)
   {
      this.resource = resource instanceof ZipInputStreamHandleImpl ? (ZipInputStreamHandle) resource : IO
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