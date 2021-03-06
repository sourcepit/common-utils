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

import java.io.IOException;
import java.io.OutputStream;
import java.util.zip.ZipOutputStream;

import org.sourcepit.common.utils.io.IOHandle;
import org.sourcepit.common.utils.io.handles.ZipOutputStreamHandle;


/**
 * @author Bernd Vogt <bernd.vogt@sourcepit.org>
 */
public class ZipOutputStreamHandleImpl implements ZipOutputStreamHandle
{
   private final IOHandle<? extends OutputStream> resource;

   public ZipOutputStreamHandleImpl(IOHandle<? extends OutputStream> resource)
   {
      this.resource = resource;
   }

   public ZipOutputStream open() throws IOException
   {
      return new ZipOutputStream(resource.open());
   }
}
