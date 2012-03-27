/**
 * Copyright (c) 2011 Sourcepit.org contributors and others. All rights reserved. This program and the accompanying
 * materials are made available under the terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.sourcepit.common.utils.lang;

import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.List;

/**
 * @author Bernd Vogt <bernd.vogt@sourcepit.org>
 */
public final class PipedError extends Error implements ThrowablePipe
{
   private static final long serialVersionUID = 1L;

   private final ThrowablePipe pipe;

   PipedError(Error cause)
   {
      this(Exceptions.newPipe(cause));
   }

   PipedError(ThrowablePipe pipe)
   {
      this.pipe = pipe;
   }

   public List<Throwable> getThrowables()
   {
      return pipe.getThrowables();
   }
   
   public boolean isEmpty()
   {
      return pipe.isEmpty();
   }

   @Override
   public Error getCause()
   {
      return (Error) pipe.getCause();
   }

   public void throwPipe()
   {
      throw this;
   }

   @Override
   public String getMessage()
   {
      return Exceptions.doGetMessage();
   }

   public List<Throwable> getFollowers()
   {
      return pipe.getFollowers();
   }

   public void add(Throwable throwable)
   {
      pipe.add(throwable);
   }

   public PipedError toThrowable()
   {
      return this;
   }

   public Iterator<Throwable> iterator()
   {
      return pipe.iterator();
   }

   @Override
   public void printStackTrace(PrintWriter printWriter)
   {
      Exceptions.doPrintStackTrace(this, printWriter);
   }

   @Override
   public void printStackTrace(PrintStream printStream)
   {
      Exceptions.doPrintStackTrace(this, printStream);
   }

   public <T extends Throwable> T adapt(Class<T> type)
   {
      return pipe.adapt(type);
   }

   public <T extends Throwable> void adaptAndThrow(Class<T> type) throws T
   {
      pipe.adaptAndThrow(type);
   }

   @Override
   public int hashCode()
   {
      final int prime = 31;
      int result = 1;
      result = prime * result + ((pipe == null) ? 0 : pipe.hashCode());
      return result;
   }

   @Override
   public boolean equals(Object obj)
   {
      if (this == obj)
      {
         return true;
      }
      if (obj == null)
      {
         return false;
      }
      if (getClass() != obj.getClass())
      {
         return false;
      }
      PipedError other = (PipedError) obj;
      if (pipe == null)
      {
         if (other.pipe != null)
         {
            return false;
         }
      }
      else if (!pipe.equals(other.pipe))
      {
         return false;
      }
      return true;
   }
}
