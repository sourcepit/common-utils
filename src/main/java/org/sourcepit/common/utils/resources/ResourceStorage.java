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

package org.sourcepit.common.utils.resources;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.nio.charset.Charset;

public abstract class ResourceStorage implements Closeable
{
   private boolean open;

   public final synchronized void open() throws IOException
   {
      if (open)
      {
         throw new IOException("Resource storage already open.");
      }
      else
      {
         doOpen();
         open = true;
      }
   }

   protected abstract void doOpen() throws IOException;

   @Override
   public final synchronized void close() throws IOException
   {
      if (open)
      {
         doClose();
         open = false;
      }
      else
      {
         throw new IOException("Resource storage not open.");
      }
   }

   protected abstract void doClose() throws IOException;

   public abstract void put(Resource resource, InputStream content) throws IOException;

   public abstract void put(Resource resource, Reader content, Charset charset) throws IOException;
}
