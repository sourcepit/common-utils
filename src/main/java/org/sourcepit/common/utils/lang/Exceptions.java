/**
 * Copyright (c) 2012 Sourcepit.org contributors and others. All rights reserved. This program and the accompanying
 * materials are made available under the terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.sourcepit.common.utils.lang;

import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author Bernd Vogt <bernd.vogt@sourcepit.org>
 */
public final class Exceptions
{
   private Exceptions()
   {
      super();
   }

   public static PipedException toPipedException(Exception exception)
   {
      if (exception instanceof PipedException)
      {
         return (PipedException) exception;
      }
      argNotNull(exception, 0);
      return new PipedException(exception);
   }

   public static PipedError toPipedError(Error error)
   {
      if (error instanceof PipedError)
      {
         return (PipedError) error;
      }
      argNotNull(error, 0);
      return new PipedError(error);
   }

   public static ThrowablePipe pipe(ThrowablePipe pipe, Throwable throwable)
   {
      if (pipe == null)
      {
         pipe = toThrowablePipe(throwable);
      }
      else
      {
         pipe.add(throwable);
      }
      return pipe;
   }

   public static void throwIfNotNull(ThrowablePipe carrier)
   {
      if (carrier != null)
      {
         carrier.throwPipe();
      }
   }

   public static ThrowablePipe toThrowablePipe(Throwable throwable)
   {
      if (throwable instanceof Error)
      {
         return toPipedError((Error) throwable);
      }
      else if (throwable instanceof Exception)
      {
         return toPipedException((Exception) throwable);
      }

      argNotNull(throwable, 0);
      return toPipedError(new Error(throwable));
   }

   static void argNotNull(Object arg, int idx)
   {
      if (arg == null)
      {
         throw new IllegalArgumentException("Argument number " + (idx + 1) + " may not be null.");
      }
   }

   static Iterator<Throwable> iterator(ThrowablePipe pipe)
   {
      final List<Throwable> followers = pipe.getFollowers();
      final List<Throwable> all = new ArrayList<Throwable>(1 + followers.size());
      all.add(pipe.getCause());
      all.addAll(followers);
      return all.iterator();
   }

   static String doGetMessage()
   {
      return "See initial cause for actual problem:";
   }

   @SuppressWarnings("unchecked")
   static <A> A doAdapt(ThrowablePipe pipe, Class<A> type)
   {
      argNotNull(pipe, 0);
      argNotNull(type, 1);
      final Throwable cause = pipe.getCause();
      if (type.isAssignableFrom(cause.getClass()))
      {
         return (A) cause;
      }
      return type.isAssignableFrom(pipe.getClass()) ? (A) pipe : null;
   }

   static <T extends Throwable> void doAdaptAndThrow(ThrowablePipe pipe, Class<T> type) throws T
   {
      final T throwable = doAdapt(pipe, type);
      if (throwable != null)
      {
         throw throwable;
      }
   }

   static void doAppendAll(List<Throwable> target, List<Throwable> followers)
   {
      for (Throwable follower : followers)
      {
         doAppend(target, follower);
      }
   }

   static void doAppend(List<Throwable> followers, Throwable follower)
   {
      argNotNull(followers, 0);
      argNotNull(follower, 1);
      if (follower instanceof ThrowablePipe)
      {
         final ThrowablePipe other = ((ThrowablePipe) follower);
         followers.add(other.getCause());
         for (Throwable throwable : other.getFollowers())
         {
            doAppend(followers, throwable);
         }
      }
      else
      {
         followers.add(follower);
      }
   }

   static void doPrintStackTrace(ThrowablePipe throwable, final PrintWriter printWriter)
   {
      argNotNull(throwable, 0);
      argNotNull(printWriter, 1);
      final Print print = new Print()
      {
         public void ln(Object o)
         {
            printWriter.println(o);
         }
      };
      doPrintStackTrace(throwable, print);
   }

   static void doPrintStackTrace(ThrowablePipe throwable, final PrintStream printStream)
   {
      argNotNull(throwable, 0);
      argNotNull(printStream, 1);
      final Print print = new Print()
      {
         public void ln(Object o)
         {
            printStream.println(o);
         }
      };
      doPrintStackTrace(throwable, print);
   }

   private static void doPrintStackTrace(ThrowablePipe throwable, final Print print)
   {
      print.ln(throwable);

      final StackTraceElement[] trace = throwable.getStackTrace();

      final Throwable cause = throwable.getCause();
      print.ln("Initially caused by: " + cause);

      for (StackTraceElement traceElement : cause.getStackTrace())
      {
         print.ln("        at " + traceElement);
      }

      doPrintStackTrace(print, "    ", "Caused by: ", cause.getCause(), trace);

      for (Throwable follower : throwable.getFollowers())
      {
         doPrintStackTrace(print, "", "Followed by: ", follower, trace);
      }
   }

   private static void doPrintStackTrace(Print print, String prefix, String caption, Throwable cause,
      StackTraceElement[] enclosingTrace)
   {
      if (cause != null)
      {
         print.ln(prefix + caption + cause);

         // Compute number of frames in common between this and enclosing trace
         final StackTraceElement[] trace = cause.getStackTrace();
         int m = trace.length - 1;
         int n = enclosingTrace.length - 1;
         while (m >= 0 && n >= 0 && trace[m].equals(enclosingTrace[n]))
         {
            m--;
            n--;
         }

         for (int i = 0; i <= m; i++)
         {
            print.ln(prefix + "        at " + trace[i]);
         }

         final int framesInCommon = trace.length - 1 - m;
         if (framesInCommon != 0)
         {
            print.ln(prefix + "        ... " + framesInCommon + " more");
         }

         doPrintStackTrace(print, prefix, "Caused by: ", cause.getCause(), trace);
      }
   }

   private static interface Print
   {
      void ln(Object o);
   }
}
