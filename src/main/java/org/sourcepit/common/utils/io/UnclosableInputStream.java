/**
 * Copyright (c) 2011 Sourcepit.org contributors and others. All rights reserved. This program and the accompanying
 * materials are made available under the terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.sourcepit.common.utils.io;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;

public abstract class UnclosableInputStream extends InputStream
{
   private InputStream delegate;

   private boolean closed;

   public static UnclosableInputStream wrap(final InputStream inputStream)
   {
      return new UnclosableInputStream()
      {
         @Override
         protected InputStream openInputStream() throws IOException
         {
            return inputStream;
         }
      };
   }

   @Override
   public int read() throws IOException
   {
      if (closed)
      {
         throw new IOException("Stream already closed.");
      }
      if (delegate == null)
      {
         delegate = openInputStream();
      }
      return delegate.read();
   }

   protected abstract InputStream openInputStream() throws IOException;

   public void closeDelegate()
   {
      closed = true;
      IOUtils.closeQuietly(delegate);
      delegate = null;
   }
}