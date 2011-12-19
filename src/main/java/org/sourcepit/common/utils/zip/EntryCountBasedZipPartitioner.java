/**
 * Copyright (c) 2011 Sourcepit.org contributors and others. All rights reserved. This program and the accompanying
 * materials are made available under the terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.sourcepit.common.utils.zip;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.apache.commons.io.IOUtils;

public class EntryCountBasedZipPartitioner implements ZipPartitioner
{
   public Collection<Callable<Integer>> computePartitions(ZipInputStreamFactory streamFactory,
      ZipEntryHandler entryHandler, int nrOfThreads) throws IOException
   {
      final List<String> entryNames = getEntryNames(streamFactory);

      int size = entryNames.size();
      System.out.println("files: " + size);

      int jobsSize = size / nrOfThreads;
      int lastJobSize = size % nrOfThreads;

      System.out.println("tasks: " + nrOfThreads);
      System.out.println("files per task: " + jobsSize);

      final Collection<Callable<Integer>> tasks = new ArrayList<Callable<Integer>>();
      if (jobsSize > 0)
      {
         int startIdx = 0;
         int endIdx = -1;
         for (int i = 0; i < nrOfThreads; i++)
         {
            startIdx = endIdx + 1;
            endIdx += jobsSize;
            if (lastJobSize > 0)
            {
               endIdx++;
               lastJobSize--;
            }
            RangedZipPartitionProcessor task = new RangedZipPartitionProcessor(streamFactory, entryHandler, startIdx,
               endIdx);
            tasks.add(task);
         }
      }
      return tasks;
   }

   private List<String> getEntryNames(ZipInputStreamFactory streamFactory) throws IOException
   {
      final List<String> entryNames = new ArrayList<String>();
      ZipInputStream zipStream = streamFactory.newZipInputStream();
      try
      {
         ZipEntry zipEntry = zipStream.getNextEntry();
         while (zipEntry != null)
         {
            entryNames.add(zipEntry.getName());
            zipEntry = zipStream.getNextEntry();
         }
         return entryNames;
      }
      finally
      {
         IOUtils.closeQuietly(zipStream);
      }
   }
}