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
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author Bernd Vogt <bernd.vogt@sourcepit.org>
 */
public class CopyOnWriteThrowablePipe implements ThrowablePipe
{
   private static final long serialVersionUID = 1L;

   private final List<Throwable> throwables = new CopyOnWriteArrayList<Throwable>();

   private final StackTraceElement[] stackTrace;

   public CopyOnWriteThrowablePipe(Throwable cause)
   {
      this();
      add(cause);
   }

   public CopyOnWriteThrowablePipe()
   {
      final StackTraceElement[] src = new Exception().getStackTrace();
      final StackTraceElement[] dest = new StackTraceElement[src.length - 1];
      System.arraycopy(src, 1, dest, 0, dest.length);
      stackTrace = dest;
   }

   public List<Throwable> getThrowables()
   {
      return Collections.unmodifiableList(throwables);
   }

   public boolean isEmpty()
   {
      return throwables.isEmpty();
   }

   public Iterator<Throwable> iterator()
   {
      return getThrowables().iterator();
   }

   public Throwable getCause()
   {
      return throwables.isEmpty() ? null : throwables.get(0);
   }

   public List<Throwable> getFollowers()
   {
      final List<Throwable> followers = new ArrayList<Throwable>(throwables);
      if (!followers.isEmpty())
      {
         followers.remove(0);
      }
      return Collections.unmodifiableList(followers);
   }

   public void add(Throwable throwable)
   {
      Exceptions.doAdd(throwables, throwable);
   }

   public <T extends Throwable> T adapt(Class<T> type)
   {
      return Exceptions.doAdapt(this, type);
   }

   public <T extends Throwable> void adaptAndThrow(Class<T> type) throws T
   {
      Exceptions.doAdaptAndThrow(this, type);
   }

   public Throwable toPipedThrowable()
   {
      return Exceptions.toPipedThrowable(this);
   }

   public void throwPipe()
   {
      final Throwable throwable = toPipedThrowable();
      if (throwable != null)
      {
         if (throwable instanceof Error)
         {
            throw (Error) throwable;
         }
         if (throwable instanceof RuntimeException)
         {
            throw (RuntimeException) throwable;
         }
         throw new IllegalStateException();
      }
   }

   public StackTraceElement[] getStackTrace()
   {
      return stackTrace;
   }

   public void printStackTrace()
   {
      printStackTrace(System.err);
   }

   public void printStackTrace(PrintWriter printWriter)
   {
      Exceptions.doPrintStackTrace(this, printWriter);
   }

   public void printStackTrace(PrintStream printStream)
   {
      Exceptions.doPrintStackTrace(this, printStream);
   }

   public String toString()
   {
      final String s = getClass().getName();
      final Throwable cause = getCause();
      final String message = cause == null ? null : cause.getLocalizedMessage();
      return (message != null) ? (s + ": " + message) : s;
   }
}
