/**
 * Copyright (c) 2012 Sourcepit.org contributors and others. All rights reserved. This program and the accompanying
 * materials are made available under the terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.sourcepit.common.utils.io;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.sourcepit.common.utils.file.FileUtils;

/**
 * @author Bernd Vogt <bernd.vogt@sourcepit.org>
 */
public class FileOutputStreamResource implements IOResource<FileOutputStream>
{
   protected final File file;
   protected final boolean createOnDemand;

   public FileOutputStreamResource(File file)
   {
      this(file, true);
   }

   public FileOutputStreamResource(File file, boolean createOnDemand)
   {
      this.file = file;
      this.createOnDemand = createOnDemand;
   }

   public FileOutputStream open() throws IOException
   {
      return FileUtils.openOutputStream(file, createOnDemand);
   }

}