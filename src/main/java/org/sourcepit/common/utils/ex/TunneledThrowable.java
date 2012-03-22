/**
 * Copyright (c) 2011 Sourcepit.org contributors and others. All rights reserved. This program and the accompanying
 * materials are made available under the terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.sourcepit.common.utils.ex;

import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author Bernd Vogt <bernd.vogt@sourcepit.org>
 */
public class TunneledThrowable extends Throwable implements ThrowableCarrier<Throwable>
{
   private static final long serialVersionUID = 1L;

   private final List<Throwable> followers = new CopyOnWriteArrayList<Throwable>();

   public static TunneledException toTunneledException(Exception exception)
   {
      return TunneledException.toTunneledException(exception);
   }

   public static TunneledError toTunneledError(Error error)
   {
      return TunneledError.toTunneledError(error);
   }

   public static ThrowableCarrier<?> toThrowableCarrier(Throwable throwable)
   {
      if (throwable instanceof Error)
      {
         return toTunneledError((Error) throwable);
      }
      else if (throwable instanceof Exception)
      {
         return toTunneledException((Exception) throwable);
      }
      else if (throwable instanceof TunneledThrowable)
      {
         return (TunneledThrowable) throwable;
      }

      argNotNull(throwable, 0);
      return new TunneledThrowable(throwable);
   }

   private TunneledThrowable(Throwable cause)
   {
      super(cause);
   }

   public TunneledThrowable toThrowable()
   {
      return this;
   }

   @Override
   public String getMessage()
   {
      return doGetMessage();
   }

   public <E extends Throwable> E adapt(Class<E> type)
   {
      return doAdapt(this, type);
   }

   public void append(Throwable follower)
   {
      synchronized (followers)
      {
         doAppend(followers, follower);
      }
   }

   public List<Throwable> getFollowers()
   {
      return Collections.unmodifiableList(followers);
   }

   @Override
   public void printStackTrace(final PrintWriter printWriter)
   {
      doPrintStackTrace(this, printWriter);
   }

   @Override
   public void printStackTrace(final PrintStream printStream)
   {
      doPrintStackTrace(this, printStream);
   }

   static void argNotNull(Object arg, int idx)
   {
      if (arg == null)
      {
         throw new IllegalArgumentException("Argument number " + (idx + 1) + " may not be null.");
      }
   }

   static String doGetMessage()
   {
      return "I'm just the messenger... see below for actual cause.";
   }

   @SuppressWarnings("unchecked")
   static <E> E doAdapt(ThrowableCarrier<? extends Throwable> carrier, Class<E> type)
   {
      argNotNull(carrier, 0);
      argNotNull(type, 1);
      final Throwable cause = carrier.getCause();
      if (type.isAssignableFrom(cause.getClass()))
      {
         return (E) cause;
      }
      return null;
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
      if (follower instanceof ThrowableCarrier)
      {
         @SuppressWarnings({ "rawtypes", "unchecked" })
         final ThrowableCarrier<? extends Throwable> other = ((ThrowableCarrier) follower);
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

   static void doPrintStackTrace(ThrowableCarrier<? extends Throwable> throwable, final PrintWriter printWriter)
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

   static void doPrintStackTrace(ThrowableCarrier<? extends Throwable> throwable, final PrintStream printStream)
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

   private static void doPrintStackTrace(ThrowableCarrier<? extends Throwable> throwable, final Print print)
   {
      print.ln(throwable);

      final StackTraceElement[] trace = throwable.getStackTrace();

      final int traceLength = trace.length;
      final int count = Math.min(3, traceLength);
      for (int i = 0; i < count; i++)
      {
         print.ln("\tat " + trace[i]);
      }

      final int remaining = traceLength - count;
      if (remaining > 0)
      {
         print.ln("\t... " + remaining + " more");
      }

      final Throwable cause = throwable.getCause();
      print.ln("Actually caused by: " + cause);

      for (StackTraceElement traceElement : cause.getStackTrace())
      {
         print.ln("\tat " + traceElement);
      }

      for (Throwable follower : throwable.getFollowers())
      {
         doPrintStackTrace(print, "\t", "Followed by: ", follower, trace);
      }

      doPrintStackTrace(print, "", "Caused by: ", cause.getCause(), trace);
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
            print.ln(prefix + "\tat " + trace[i]);
         }

         final int framesInCommon = trace.length - 1 - m;
         if (framesInCommon != 0)
         {
            print.ln(prefix + "\t... " + framesInCommon + " more");
         }

         doPrintStackTrace(print, prefix, "Caused by: ", cause.getCause(), trace);
      }
   }

   private static interface Print
   {
      void ln(Object o);
   }
}
