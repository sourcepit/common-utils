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

package org.sourcepit.common.utils.zip;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.ZipEntry;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

public class ZipEntryUnpacker implements ZipEntryHandler
{
   protected final File dstDir;

   public ZipEntryUnpacker(File dstDir)
   {
      this.dstDir = dstDir;
   }

   public void handle(ZipEntry zipEntry, InputStream content) throws IOException
   {
      final File newFile = newFile(zipEntry);
      if (!newFile.isDirectory())
      {
         final OutputStream out = new BufferedOutputStream(new FileOutputStream(newFile));
         try
         {
            IOUtils.copy(content, out);
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