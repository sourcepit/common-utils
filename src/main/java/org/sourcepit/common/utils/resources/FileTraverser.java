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

import static org.sourcepit.common.utils.io.IO.buffIn;
import static org.sourcepit.common.utils.io.IO.fileIn;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import org.sourcepit.common.utils.io.UnclosableInputStream;
import org.sourcepit.common.utils.path.Path;
import org.sourcepit.common.utils.path.PathUtils;

public class FileTraverser implements ResourceTraverser
{
   private final File baseDir;

   public FileTraverser(File baseDir)
   {
      this.baseDir = baseDir;
   }

   @Override
   public void accept(ResourceVisitor visitor) throws IOException
   {
      for (final File file : baseDir.listFiles())
      {
         accept(visitor, file);
      }
   }

   private void accept(ResourceVisitor visitor, final File file) throws IOException
   {
      visit(visitor, file);
      if (file.isDirectory())
      {
         for (final File child : file.listFiles())
         {
            accept(visitor, child);
         }
      }
   }

   private void visit(ResourceVisitor visitor, final File file) throws IOException
   {
      final UnclosableInputStream content = file.isDirectory() ? null : toStream(file);
      try
      {
         visitor.visit(toResource(baseDir, file), content);
      }
      finally
      {
         if (content != null)
         {
            content.closeDelegate();
         }
      }
   }

   private static UnclosableInputStream toStream(final File file)
   {
      return new UnclosableInputStream()
      {
         @Override
         protected InputStream openInputStream() throws IOException
         {
            return buffIn(fileIn(file)).open();
         }
      };
   }

   private static Resource toResource(File baseDir, File file)
   {
      final String path = PathUtils.getRelativePath(file, baseDir, "/");
      return new Resource(new Path(path), file.isDirectory());
   }
}
