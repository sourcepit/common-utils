/**
 * Copyright (c) 2012 Sourcepit.org contributors and others. All rights reserved. This program and the accompanying
 * materials are made available under the terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
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
   private final IOFactory<? extends R> resource;

   public IOOperation(IOFactory<? extends R> resource)
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
