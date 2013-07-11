/**
 * Copyright (c) 2013 Sourcepit.org contributors and others. All rights reserved. This program and the accompanying
 * materials are made available under the terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
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
