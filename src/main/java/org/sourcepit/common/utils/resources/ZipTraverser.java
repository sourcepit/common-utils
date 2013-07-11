/**
 * Copyright (c) 2013 Sourcepit.org contributors and others. All rights reserved. This program and the accompanying
 * materials are made available under the terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
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
