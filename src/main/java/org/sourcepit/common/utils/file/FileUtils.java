/**
 * Copyright (c) 2012 Sourcepit.org contributors and others. All rights reserved. This program and the accompanying
 * materials are made available under the terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
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
