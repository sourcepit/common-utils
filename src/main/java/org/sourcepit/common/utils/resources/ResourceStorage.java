/**
 * Copyright (c) 2013 Sourcepit.org contributors and others. All rights reserved. This program and the accompanying
 * materials are made available under the terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
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
