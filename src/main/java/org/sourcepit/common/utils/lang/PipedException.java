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

package org.sourcepit.common.utils.lang;

import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.List;

/**
 * @author Bernd Vogt <bernd.vogt@sourcepit.org>
 */
public class PipedException extends RuntimeException implements ThrowablePipe
{
   private static final long serialVersionUID = 1L;

   private final ThrowablePipe pipe;

   PipedException(Exception cause)
   {
      this(Exceptions.newThrowablePipe(cause));
   }

   PipedException(ThrowablePipe pipe)
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
   public Exception getCause()
   {
      return (Exception) pipe.getCause();
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

   public PipedException toPipedThrowable()
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
      PipedException other = (PipedException) obj;
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
