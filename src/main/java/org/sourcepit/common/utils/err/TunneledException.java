/**
 * Copyright (c) 2012 Sourcepit.org contributors and others. All rights reserved. This program and the accompanying
 * materials are made available under the terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.sourcepit.common.utils.err;

import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Bernd Vogt <bernd.vogt@sourcepit.org>
 */
public final class TunneledException extends RuntimeException
{
   private static final long serialVersionUID = 1L;

   private List<Throwable> followers;

   public TunneledException(Exception cause)
   {
      super(cause);
   }

   @Override
   public Exception getCause()
   {
      return (Exception) super.getCause();
   }

   @Override
   public String getMessage()
   {
      return "I'm just the messenger... see below for actual cause";
   }

   @SuppressWarnings("unchecked")
   public <E extends Exception> E adapt(Class<E> type)
   {
      if (type.isAssignableFrom(getCause().getClass()))
      {
         return (E) getCause();
      }
      return null;
   }

   public synchronized void append(Throwable follower)
   {
      if (followers == null)
      {
         followers = new ArrayList<Throwable>();
      }

      if (follower instanceof TunneledException)
      {
         final TunneledException other = (TunneledException) follower;
         followers.add(other.getCause());
         if (other.followers != null)
         {
            followers.addAll(other.followers);
         }
      }
      else
      {
         followers.add(follower);
      }
   }

   @Override
   public void printStackTrace(PrintStream s)
   {
      s.println(this);
      StackTraceElement[] trace = super.getStackTrace();

      int count = Math.min(3, trace.length);
      for (int i = 0; i < count; i++)
      {
         s.println("\tat " + trace[i]);
      }

      int foo = trace.length - count;
      if (foo > 0)
      {
         s.println("\t... " + foo + " more");
      }

      Exception cause = getCause();

      s.println("Actually caused by: " + cause);

      StackTraceElement[] causeTrace = cause.getStackTrace();
      for (StackTraceElement causeElement : causeTrace)
      {
         s.println("\tat " + causeElement);
      }

      if (followers != null)
      {
         for (Throwable follower : followers)
         {
            printStackTrace("\t", "Followed by: ", follower, s);
         }
      }

      printStackTrace("", "Caused by: ", cause.getCause(), s);
   }

   private void printStackTrace(String prefix, String caption, Throwable cause, PrintStream s)
   {
      if (cause == null)
      {
         return;
      }
      // Compute number of frames in common between this and enclosing trace
      StackTraceElement[] trace = cause.getStackTrace();
      StackTraceElement[] enclosingTrace = getStackTrace();
      int m = trace.length - 1;
      int n = enclosingTrace.length - 1;
      while (m >= 0 && n >= 0 && trace[m].equals(enclosingTrace[n]))
      {
         m--;
         n--;
      }
      int framesInCommon = trace.length - 1 - m;

      // Print our stack trace
      s.println(prefix + caption + cause);
      for (int i = 0; i <= m; i++)
         s.println(prefix + "\tat " + trace[i]);
      if (framesInCommon != 0)
         s.println(prefix + "\t... " + framesInCommon + " more");


      // s.println(prefix + caption + cause);
      //
      // StackTraceElement[] causeTrace = cause.getStackTrace();
      // for (StackTraceElement causeElement : causeTrace)
      // {
      // s.println(prefix + "\tat " + causeElement);
      // }
      printStackTrace(prefix, "Caused by: ", cause.getCause(), s);
   }

   @Override
   public void printStackTrace(PrintWriter s)
   {
      getCause().printStackTrace(s);
   }
}
