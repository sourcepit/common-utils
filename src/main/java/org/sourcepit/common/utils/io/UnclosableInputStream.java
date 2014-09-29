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