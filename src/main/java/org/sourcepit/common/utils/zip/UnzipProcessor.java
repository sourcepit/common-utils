/**
 * Copyright (c) 2011 Sourcepit.org contributors and others. All rights reserved. This program and the accompanying
 * materials are made available under the terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
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
public class UnzipProcessor
{
   public int process(UnzipRequest request) throws IOException
   {
      final ZipPartitioner zipPartitioner = request.getZipPartitioner();

      final ZipInputStreamFactory streamFactory = request.getStreamFactory();

      final ZipEntryHandler entryHandler = request.getEntryHandler();

      int nrOfThreads = request.getNrOfThreads();

      final Collection<Callable<Integer>> partitionProcessors = zipPartitioner.computePartitions(streamFactory,
         entryHandler, nrOfThreads);

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
