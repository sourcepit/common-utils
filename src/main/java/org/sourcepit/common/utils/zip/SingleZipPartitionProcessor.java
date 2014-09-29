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

import java.io.IOException;
import java.util.concurrent.Callable;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.apache.commons.io.IOUtils;

public class SingleZipPartitionProcessor implements Callable<Integer>
{
   protected final ZipInputStreamFactory streamFactory;

   protected final ZipEntryHandler entryHandler;

   public SingleZipPartitionProcessor(ZipInputStreamFactory streamFactory, ZipEntryHandler entryHandler)
   {
      this.streamFactory = streamFactory;
      this.entryHandler = entryHandler;
   }

   public final Integer call() throws Exception
   {
      final ZipInputStream zipIn = newZipInputStream();
      try
      {
         return Integer.valueOf(processZip(newZipInputStream()));
      }
      finally
      {
         IOUtils.closeQuietly(zipIn);
      }
   }

   protected int processZip(ZipInputStream zipIn) throws IOException
   {
      int processed = 0;
      int zipEntryIdx = 0;
      ZipEntry zipEntry = zipIn.getNextEntry();
      while (zipEntry != null)
      {
         if (isUnzip(zipEntry, zipEntryIdx))
         {
            processZipEntry(zipIn, zipEntry);
            processed++;
         }
         zipEntry = zipIn.getNextEntry();
         zipEntryIdx++;
      }
      return processed;
   }

   protected void processZipEntry(ZipInputStream zipIn, ZipEntry zipEntry) throws IOException
   {
      entryHandler.handle(zipEntry, zipIn);
   }

   protected boolean isUnzip(ZipEntry zipEntry, int zipEntryIdx)
   {
      return true;
   }

   protected ZipInputStream newZipInputStream() throws IOException
   {
      return streamFactory.newZipInputStream();
   }
}