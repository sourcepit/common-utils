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

import java.io.Closeable;
import java.io.IOException;

import org.sourcepit.common.utils.lang.Exceptions;
import org.sourcepit.common.utils.lang.PipedIOException;
import org.sourcepit.common.utils.lang.ThrowablePipe;

/**
 * @author Bernd Vogt <bernd.vogt@sourcepit.org>
 */
public abstract class IOOperation<R extends Closeable> implements Runnable
{
   private final IOHandle<? extends R> resource;

   public IOOperation(IOHandle<? extends R> resource)
   {
      this.resource = resource;
   }

   public void run() throws PipedIOException
   {
      ThrowablePipe error = null;
      R openResource = null;
      try
      {
         openResource = resource.open();
         run(openResource);
      }
      catch (Throwable e)
      {
         error = Exceptions.pipe(e);
      }
      finally
      {
         closeAndThrow(openResource, error);
      }
   }

   protected abstract void run(R openResource) throws IOException;

   private static void closeAndThrow(Closeable closeable, ThrowablePipe errors) throws PipedIOException
   {
      if (closeable != null)
      {
         try
         {
            closeable.close();
         }
         catch (IOException e)
         {
            if (errors == null)
            {
               errors = Exceptions.pipe(e);
            }
            else
            {
               errors.add(e);
            }
         }
      }

      if (errors != null)
      {
         errors.throwPipe();
      }
   }

}
