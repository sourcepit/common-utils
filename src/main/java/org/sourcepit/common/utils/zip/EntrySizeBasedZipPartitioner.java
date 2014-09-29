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