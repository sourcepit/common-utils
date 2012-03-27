/**
 * Copyright (c) 2012 Sourcepit.org contributors and others. All rights reserved. This program and the accompanying
 * materials are made available under the terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
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

   public CopyOnWriteThrowablePipe()
   {
      final StackTraceElement[] src = new Exception().getStackTrace();
      final StackTraceElement[] dest = new  StackTraceElement[src.length-1];
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

   public Throwable toThrowable()
   {
      final Throwable cause = getCause();
      if (cause instanceof Error)
      {
         return new PipedError(this);
      }
      if (cause instanceof Exception)
      {
         return new PipedException(this);
      }
      return null;
   }

   public void throwPipe()
   {
      final Throwable throwable = toThrowable();
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
