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
import java.io.InterruptedIOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

/**
 * @author Bernd
 */
public class ZipProcessor
{
   public int process(ZipProcessingRequest request) throws IOException
   {
      final ZipPartitioner zipPartitioner = request.getZipPartitioner();

      final ZipInputStreamFactory streamFactory = request.getStreamFactory();

      final ZipEntryHandler entryHandler = request.getEntryHandler();

      int nrOfThreads = request.getNrOfThreads();

      final Collection<Callable<Integer>> partitionProcessors = zipPartitioner.computePartitions(streamFactory,
         entryHandler, nrOfThreads);

      if (partitionProcessors.size() == 1)
      {
         try
         {
            return partitionProcessors.iterator().next().call().intValue();
         }
         catch (Exception e)
         {
            throw new IllegalStateException(e);
         }
      }

      final ExecutorService executor = request.getExecutorService();

      final List<Future<Integer>> futures;
      try
      {
         futures = executor.invokeAll(partitionProcessors);
      }
      catch (InterruptedException e)
      {
         throw new InterruptedIOException(e.getLocalizedMessage());
      }
      finally
      {
         executor.shutdown();
      }

      int processedEntries = 0;
      final List<Throwable> errors = new ArrayList<Throwable>();
      for (Future<Integer> future : futures)
      {
         try
         {
            processedEntries += future.get().intValue();
         }
         catch (InterruptedException e)
         {
            errors.add(e);
         }
         catch (ExecutionException e)
         {
            errors.add(e.getCause());
         }
      }

      for (Throwable error : errors)
      {
         if (error instanceof Error)
         {
            throw (Error) error;
         }
         if (error instanceof RuntimeException)
         {
            throw (RuntimeException) error;
         }
         if (error instanceof IOException)
         {
            throw (IOException) error;
         }
         throw new IllegalStateException(error);
      }

      return processedEntries;
   }
}
