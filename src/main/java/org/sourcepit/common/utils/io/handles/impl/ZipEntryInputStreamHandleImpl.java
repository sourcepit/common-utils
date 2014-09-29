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
