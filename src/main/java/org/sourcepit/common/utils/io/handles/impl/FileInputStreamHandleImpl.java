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

package org.sourcepit.common.utils.io.handles.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.sourcepit.common.utils.file.FileUtils;
import org.sourcepit.common.utils.io.handles.FileInputStreamHandle;

/**
 * @author Bernd Vogt <bernd.vogt@sourcepit.org>
 */
public class FileInputStreamHandleImpl implements FileInputStreamHandle
{
   protected final File file;
   protected final boolean createOnDemand;

   public FileInputStreamHandleImpl(File file)
   {
      this(file, false);
   }

   public FileInputStreamHandleImpl(File file, boolean createOnDemand)
   {
      this.file = file;
      this.createOnDemand = createOnDemand;
   }

   public FileInputStream open() throws IOException
   {
      return (FileInputStream) FileUtils.openInputStream(file, createOnDemand);
   }

}
