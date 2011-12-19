/**
 * Copyright (c) 2011 Sourcepit.org contributors and others. All rights reserved. This program and the accompanying
 * materials are made available under the terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.sourcepit.common.utils.zip;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

public class ZipEntryUnpacker implements ZipEntryHandler
{
   protected final File dstDir;

   public ZipEntryUnpacker(File dstDir)
   {
      this.dstDir = dstDir;
   }

   public void handle(ZipInputStream zipIn, ZipEntry zipEntry, int zipEntryIdx) throws IOException
   {
      final File newFile = newFile(zipEntry);
      if (!newFile.isDirectory())
      {
         final OutputStream out = new BufferedOutputStream(new FileOutputStream(newFile));
         try
         {
            IOUtils.copy(zipIn, out);
         }
         finally
         {
            IOUtils.closeQuietly(out);
         }
      }
   }

   protected File newFile(ZipEntry zipEntry) throws IOException
   {
      IOException ioe = null;
      for (int i = 0; i < 14; i++)
      {
         try
         {
            File newFile = new File(dstDir, zipEntry.getName());
            if (zipEntry.isDirectory())
            {
               FileUtils.forceMkdir(newFile);
            }
            else
            {
               FileUtils.forceMkdir(newFile.getParentFile());
               newFile.createNewFile();
            }
            return newFile;
         }
         catch (IOException e)
         {
            ioe = e;
         }
      }
      throw ioe;
   }
}