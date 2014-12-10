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

package org.sourcepit.common.utils.file;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.commons.io.FileExistsException;

/**
 * @author Bernd Vogt <bernd.vogt@sourcepit.org>
 */
public final class FileUtils
{
   private FileUtils()
   {
      super();
   }

   public static FileInputStream openInputStream(File file, boolean createOnDemand) throws IOException
   {
      if (createOnDemand && !file.exists())
      {
         org.apache.commons.io.FileUtils.forceMkdir(file.getParentFile());
         file.createNewFile();
      }
      return org.apache.commons.io.FileUtils.openInputStream(file);
   }

   public static FileOutputStream openOutputStream(File file, boolean createOnDemand) throws IOException
   {
      if (createOnDemand && !file.exists())
      {
         org.apache.commons.io.FileUtils.forceMkdir(file.getParentFile());
         file.createNewFile();
      }
      return org.apache.commons.io.FileUtils.openOutputStream(file);
   }

   public static void accept(File file, FileVisitor visitor)
   {
      if (visitor.visit(file) && file.isDirectory())
      {
         for (File member : file.listFiles())
         {
            accept(member, visitor);
         }
      }
   }

   public static boolean isParentOf(File dir, File file)
   {
      final File parent = file.getParentFile();
      if (dir.equals(parent))
      {
         return true;
      }
      return parent == null ? false : isParentOf(dir, parent);
   }

   @SuppressWarnings("unchecked")
   public static <E extends Exception> void listFiles(File file, AbstractFileFilter<E> fileFilter) throws E
   {
      try
      {
         file.listFiles(fileFilter);
      }
      catch (ExceptionWrapper e)
      {
         throw (E) e.getCause();
      }
   }

   public abstract static class AbstractFileFilter<E extends Exception> implements FileFilter
   {
      @Override
      public final boolean accept(File file)
      {
         try
         {
            return acceptFile(file);
         }
         catch (Exception e)
         {
            throw new ExceptionWrapper(e);
         }
      }

      protected abstract boolean acceptFile(File file) throws E;
   }

   private static class ExceptionWrapper extends RuntimeException
   {
      private static final long serialVersionUID = 1L;

      public ExceptionWrapper(Exception e)
      {
         super(e);
      }
   }

   public static void deleteFileOrDirectory(File file) throws IOException
   {
      if (!file.exists())
      {
         throw new FileNotFoundException("File does not exist: " + file);
      }
      deleteFileOrDirectory0(file);
   }

   private static void deleteFileOrDirectory0(File file) throws IOException
   {
      if (file.isDirectory() && !org.apache.commons.io.FileUtils.isSymlink(file))
      {
         cleanDirectory0(file);
      }
      if (!file.delete() && file.exists())
      {
         final int attempts = 48;
         final long idleTime = 125L;
         for (int i = 0; i < attempts; i++)
         {
            try
            {
               Thread.sleep(idleTime);
            }
            catch (InterruptedException e)
            {
            }

            try
            {
               deleteFileOrDirectory0(file);
               return;
            }
            catch (IOException e)
            {
            }
         }
         throw new IOException("Unable to delete: " + file);
      }
   }

   private static void cleanDirectory0(File file) throws IOException
   {
      final File[] files = file.listFiles();
      if (files == null)
      { // null if security restricted or file deleted concurrently
         throw new IOException("Failed to list contents of " + file);
      }

      IOException exception = null;
      for (File f : files)
      {
         try
         {
            deleteFileOrDirectory0(f);
         }
         catch (IOException ioe)
         {
            exception = ioe;
         }
      }

      if (null != exception)
      {
         throw exception;
      }
   }

   public static void moveDirectory(File srcDir, File destDir) throws IOException
   {
      if (!srcDir.exists())
      {
         throw new FileNotFoundException("Source '" + srcDir + "' does not exist");
      }
      if (!srcDir.isDirectory())
      {
         throw new IOException("Source '" + srcDir + "' is not a directory");
      }
      if (destDir.exists())
      {
         throw new FileExistsException("Destination '" + destDir + "' already exists");
      }
      final boolean rename = srcDir.renameTo(destDir);
      if (!rename)
      {
         if (destDir.getCanonicalPath().startsWith(srcDir.getCanonicalPath()))
         {
            throw new IOException("Cannot move directory: " + srcDir + " to a subdirectory of itself: " + destDir);
         }
         org.apache.commons.io.FileUtils.copyDirectory(srcDir, destDir);
         deleteFileOrDirectory(srcDir);
      }
   }

   public static void moveFile(File srcFile, File destFile) throws IOException
   {
      if (!srcFile.exists())
      {
         throw new FileNotFoundException("Source '" + srcFile + "' does not exist");
      }
      if (srcFile.isDirectory())
      {
         throw new IOException("Source '" + srcFile + "' is a directory");
      }
      if (destFile.exists())
      {
         throw new FileExistsException("Destination '" + destFile + "' already exists");
      }
      if (destFile.isDirectory())
      {
         throw new IOException("Destination '" + destFile + "' is a directory");
      }
      final boolean rename = srcFile.renameTo(destFile);
      if (!rename)
      {
         org.apache.commons.io.FileUtils.copyFile(srcFile, destFile);
         try
         {
            deleteFileOrDirectory(srcFile);
         }
         catch (IOException e)
         {
            org.apache.commons.io.FileUtils.deleteQuietly(destFile);
            throw new IOException("Failed to delete original file '" + srcFile + "' after copy to '" + destFile + "'");
         }
      }
   }
}
