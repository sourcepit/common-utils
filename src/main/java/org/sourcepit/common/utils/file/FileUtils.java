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
import java.io.FileOutputStream;
import java.io.IOException;

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

}
