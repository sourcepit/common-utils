/**
 * Copyright (c) 2011 Sourcepit.org contributors and others. All rights reserved. This program and the accompanying
 * materials are made available under the terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.sourcepit.common.utils.zip;

import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author Bernd
 */
public class UnzipRequest
{
   private ZipInputStreamFactory streamFactory;

   private ZipPartitioner zipPartitioner;

   private ZipEntryHandler entryHandler;

   private ExecutorService executorService;

   private int nrOfThreads;

   public static UnzipRequest newUnzipRequest(File zipFile, File dstDir)
   {
      final UnzipRequest request = new UnzipRequest();
      request.setStreamFactory(new FileZipInputStreamFactory(zipFile));
      request.setEntryHandler(new ZipEntryUnpacker(dstDir));
      return request;
   }

   public ZipInputStreamFactory getStreamFactory()
   {
      return streamFactory;
   }

   public void setStreamFactory(ZipInputStreamFactory streamFactory)
   {
      this.streamFactory = streamFactory;
   }

   public ZipPartitioner getZipPartitioner()
   {
      if (zipPartitioner == null)
      {
         zipPartitioner = new EntryCountBasedZipPartitioner();
      }
      return zipPartitioner;
   }

   public void setZipPartitioner(ZipPartitioner zipPartitioner)
   {
      this.zipPartitioner = zipPartitioner;
   }

   public ZipEntryHandler getEntryHandler()
   {
      return entryHandler;
   }

   public void setEntryHandler(ZipEntryHandler entryHandler)
   {
      this.entryHandler = entryHandler;
   }

   public ExecutorService getExecutorService()
   {
      if (executorService == null)
      {
         executorService = Executors.newFixedThreadPool(getNrOfThreads());
      }
      return executorService;
   }

   public void setExecutorService(ExecutorService executorService)
   {
      this.executorService = executorService;
   }

   public int getNrOfThreads()
   {
      if (nrOfThreads < 1)
      {
         nrOfThreads = Runtime.getRuntime().availableProcessors();
      }
      return nrOfThreads;
   }

   public void setNrOfThreads(int nrOfThreads)
   {
      this.nrOfThreads = nrOfThreads;
   }
}
