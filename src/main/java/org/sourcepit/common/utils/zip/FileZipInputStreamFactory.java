/**
 * Copyright (c) 2011 Sourcepit.org contributors and others. All rights reserved. This program and the accompanying
 * materials are made available under the terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.sourcepit.common.utils.zip;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.zip.ZipInputStream;

public class FileZipInputStreamFactory implements ZipInputStreamFactory
{
   protected final File zipFile;

   protected final int bufferSize;

   public FileZipInputStreamFactory(File zipFile)
   {
      this(zipFile, 4096);
   }

   public FileZipInputStreamFactory(File zipFile, int bufferSize)
   {
      this.zipFile = zipFile;
      this.bufferSize = bufferSize;
   }

   public ZipInputStream newZipInputStream() throws IOException
   {
      return new ZipInputStream(new BufferedInputStream(new FileInputStream(zipFile), bufferSize));
   }
}