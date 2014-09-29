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

import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author Bernd
 */
public class ZipProcessingRequest
{
   private ZipInputStreamFactory streamFactory;

   private ZipPartitioner zipPartitioner;

   private ZipEntryHandler entryHandler;

   private ExecutorService executorService;

   private int nrOfThreads;

   public static ZipProcessingRequest newUnzipRequest(File zipFile, File dstDir)
   {
      final ZipProcessingRequest request = new ZipProcessingRequest();
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
         nrOfThreads = (int) Math.ceil(Runtime.getRuntime().availableProcessors() * 1.25f);
      }
      return nrOfThreads;
   }

   public void setNrOfThreads(int nrOfThreads)
   {
      this.nrOfThreads = nrOfThreads;
   }
}
