/**
 * Copyright (c) 2011 Sourcepit.org contributors and others. All rights reserved. This program and the accompanying
 * materials are made available under the terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.sourcepit.common.utils.zip;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.Callable;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipInputStream;

import org.apache.commons.io.IOUtils;

public class EntrySizeBasedZipPartitioner implements ZipPartitioner
{
   public Collection<Callable<Integer>> computePartitions(ZipInputStreamFactory streamFactory,
      ZipEntryHandler entryHandler, int nrOfThreads) throws IOException
   {
      List<Set<Integer>> workScopes = new ArrayList<Set<Integer>>();

      for (int i = 0; i < nrOfThreads; i++)
      {
         workScopes.add(new HashSet<Integer>());
      }

      TreeMap<ZipEntry, Integer> zipEntryNames = getZipEntryNames(streamFactory);

      Collection<Integer> workIndices = zipEntryNames.values();

      int idx = 0;
      for (Integer integer : workIndices)
      {
         workScopes.get(idx).add(integer);
         idx = idx < nrOfThreads - 1 ? idx + 1 : 0;
      }

      final Collection<Callable<Integer>> tasks = new ArrayList<Callable<Integer>>();
      for (Set<Integer> work : workScopes)
      {
         IndexBasedZipPartitionProcessor task = new IndexBasedZipPartitionProcessor(streamFactory, entryHandler, work);
         tasks.add(task);
      }
      return tasks;
   }

   private TreeMap<ZipEntry, Integer> getZipEntryNames(ZipInputStreamFactory streamFactory) throws ZipException,
      IOException
   {
      final TreeMap<ZipEntry, Integer> entryToIndex = new TreeMap<ZipEntry, Integer>(new Comparator<ZipEntry>()
      {
         public int compare(ZipEntry ze1, ZipEntry ze2)
         {
            long s1 = ze1.getSize();
            long s2 = ze2.getSize();
            int r = (int) -(s1 - s2);
            return r == 0 ? -1 : r;
         }
      });

      final ZipInputStream zipStream = streamFactory.newZipInputStream();
      try
      {
         int idx = 0;
         ZipEntry zipEntry = zipStream.getNextEntry();
         while (zipEntry != null)
         {
            entryToIndex.put(zipEntry, Integer.valueOf(idx));
            zipEntry = zipStream.getNextEntry();
            idx++;
         }
      }
      finally
      {
         IOUtils.closeQuietly(zipStream);
      }

      return entryToIndex;
   }
}