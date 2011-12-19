/**
 * Copyright (c) 2011 Sourcepit.org contributors and others. All rights reserved. This program and the accompanying
 * materials are made available under the terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
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
            processZipEntry(zipIn, zipEntry, zipEntryIdx);
            processed++;
         }
         zipEntry = zipIn.getNextEntry();
         zipEntryIdx++;
      }
      return processed;
   }

   protected void processZipEntry(ZipInputStream zipIn, ZipEntry zipEntry, int zipEntryIdx) throws IOException
   {
      entryHandler.handle(zipIn, zipEntry, zipEntryIdx);
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