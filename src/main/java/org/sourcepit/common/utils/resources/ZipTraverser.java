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

package org.sourcepit.common.utils.resources;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;

import org.sourcepit.common.utils.io.UnclosableInputStream;
import org.sourcepit.common.utils.path.Path;
import org.sourcepit.common.utils.zip.FileZipInputStreamFactory;
import org.sourcepit.common.utils.zip.ZipEntryHandler;
import org.sourcepit.common.utils.zip.ZipProcessingRequest;
import org.sourcepit.common.utils.zip.ZipProcessor;

public class ZipTraverser implements ResourceTraverser
{
   private static class ZipEntryHandlerAdapter implements ZipEntryHandler
   {
      private final ResourceVisitor visitor;

      ZipEntryHandlerAdapter(ResourceVisitor visitor)
      {
         this.visitor = visitor;
      }

      @Override
      public void handle(ZipEntry zipEntry, InputStream content) throws IOException
      {
         visitor.visit(toResource(zipEntry), UnclosableInputStream.wrap(content));
      }
   }

   private final File zipFile;

   public ZipTraverser(File zipFile)
   {
      this.zipFile = zipFile;
   }

   @Override
   public void accept(final ResourceVisitor visitor) throws IOException
   {
      final ZipProcessingRequest request = new ZipProcessingRequest();
      request.setStreamFactory(new FileZipInputStreamFactory(zipFile));
      request.setEntryHandler(new ZipEntryHandlerAdapter(visitor));
      new ZipProcessor().process(request);
   }

   static Resource toResource(ZipEntry zipEntry)
   {
      final Path path = new Path(zipEntry.getName());
      final boolean directory = zipEntry.isDirectory();
      return new Resource(path, directory);
   }
}
