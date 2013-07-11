/**
 * Copyright (c) 2013 Sourcepit.org contributors and others. All rights reserved. This program and the accompanying
 * materials are made available under the terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.sourcepit.common.utils.resources;

import static org.apache.commons.io.FileUtils.forceMkdir;
import static org.sourcepit.common.utils.io.IO.buffOut;
import static org.sourcepit.common.utils.io.IO.fileOut;
import static org.sourcepit.common.utils.io.IO.write;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.nio.charset.Charset;

import org.apache.commons.io.IOUtils;
import org.sourcepit.common.utils.io.Write.ToStream;
import org.sourcepit.common.utils.lang.PipedException;

public class FileStorage extends ResourceStorage
{
   private final File baseDir;

   public FileStorage(File baseDir)
   {
      this.baseDir = baseDir;
   }

   @Override
   protected void doOpen() throws IOException
   {
   }

   @Override
   protected void doClose() throws IOException
   {
   }

   @Override
   public void put(Resource resource, InputStream content) throws IOException
   {
      final File file = createFile(resource);
      if (!file.isDirectory())
      {
         copy(content, file);
      }
   }

   @Override
   public void put(Resource resource, Reader content, final Charset charset) throws IOException
   {
      final File file = createFile(resource);
      if (!file.isDirectory())
      {
         copy(content, file, charset);
      }
   }

   protected File createFile(Resource resource) throws IOException
   {
      final File file = new File(baseDir, resource.getPath().toString());
      if (resource.isDirectory())
      {
         forceMkdir(file);
      }
      else
      {
         forceMkdir(file.getParentFile());
         file.createNewFile();
      }
      return file;
   }

   protected void copy(InputStream src, File dest) throws IOException
   {
      final ToStream<InputStream> toStream = new ToStream<InputStream>()
      {
         @Override
         public void write(OutputStream writer, InputStream content) throws Exception
         {
            IOUtils.copy(content, writer);
         }
      };
      try
      {
         write(toStream, buffOut(fileOut(dest)), src);
      }
      catch (PipedException e)
      {
         e.adaptAndThrow(IOException.class);
         throw e;
      }
   }

   protected void copy(Reader src, final File dest, final Charset charset) throws IOException
   {
      final ToStream<Reader> toStream = new ToStream<Reader>()
      {
         @Override
         public void write(OutputStream writer, Reader content) throws Exception
         {
            IOUtils.copy(content, writer, charset);
         }
      };
      try
      {
         write(toStream, buffOut(fileOut(dest)), src);
      }
      catch (PipedException e)
      {
         e.adaptAndThrow(IOException.class);
         throw e;
      }
   }

}
