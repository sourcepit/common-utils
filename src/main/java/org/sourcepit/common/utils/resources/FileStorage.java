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
