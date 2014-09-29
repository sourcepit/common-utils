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

import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.lang.reflect.Constructor;
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

   public static ThrowablePipe newThrowablePipe(Error cause)
   {
      return new CopyOnWriteThrowablePipe(cause);
   }

   public static ThrowablePipe newThrowablePipe(Exception cause)
   {
      return new CopyOnWriteThrowablePipe(cause);
   }

   public static ThrowablePipe newThrowablePipe()
   {
      return new CopyOnWriteThrowablePipe();
   }

   public static PipedIOException pipe(IOException exception)
   {
      return (PipedIOException) doPipe(exception);
   }

   public static PipedException pipe(Exception exception)
   {
      return (PipedException) doPipe(exception);
   }

   public static PipedError pipe(Error error)
   {
      return (PipedError) doPipe(error);
   }

   public static ThrowablePipe pipe(Throwable cause)
   {
      return doPipe(cause);
   }

   private static ThrowablePipe doPipe(Throwable cause)
   {
      argNotNull(cause, 0);

      final Class<? extends Throwable> causeType = cause.getClass();
      if (ThrowablePipe.class.isAssignableFrom(causeType))
      {
         return (ThrowablePipe) cause;
      }

      final Class<? extends ThrowablePipe> pipeType = getPipeType(causeType);
      return newInstance(pipeType, cause);
   }

   static Throwable toPipedThrowable(ThrowablePipe throwablePipe)
   {
      argNotNull(throwablePipe, 0);
      if (Throwable.class.isAssignableFrom(throwablePipe.getClass()))
      {
         return (Throwable) throwablePipe;
      }

      final Throwable cause = throwablePipe.getCause();
      if (cause == null) // empty pipe
      {
         return null;
      }

      final Class<? extends Throwable> causeType;
      if (cause instanceof Exception || cause instanceof Error) // omit Throwable
      {
         causeType = cause.getClass();
      }
      else
      {
         causeType = Error.class;
      }

      return (Throwable) newInstance(getPipeType(causeType), throwablePipe);
   }

   private static ThrowablePipe newInstance(Class<? extends ThrowablePipe> pipeType, Object arg)
   {
      try
      {
         return getConstructor(pipeType, arg.getClass()).newInstance(arg);
      }
      catch (Exception e)
      {
         throw new IllegalArgumentException(e);
      }
   }

   @SuppressWarnings("unchecked")
   private static Constructor<? extends ThrowablePipe> getConstructor(Class<? extends ThrowablePipe> pipeType,
      Class<? extends Object> argClass) throws NoSuchMethodException
   {
      if (argClass != null)
      {
         for (Constructor<?> constructor : pipeType.getDeclaredConstructors())
         {
            Class<?>[] parameterTypes = constructor.getParameterTypes();
            if (parameterTypes.length == 1)
            {
               final Class<?> parameterType = parameterTypes[0];
               if (parameterType.isAssignableFrom(argClass))
               {
                  return (Constructor<? extends ThrowablePipe>) constructor;
               }

               for (Class<?> interfaze : argClass.getInterfaces())
               {
                  if (parameterType.isAssignableFrom(interfaze))
                  {
                     return (Constructor<? extends ThrowablePipe>) constructor;
                  }
               }
            }
         }
      }
      return argClass == null ? null : getConstructor(pipeType, argClass.getSuperclass());
   }

   @SuppressWarnings("unchecked")
   private static Class<? extends ThrowablePipe> getPipeType(Class<? extends Throwable> causeType)
   {
      if (Exception.class.isAssignableFrom(causeType))
      {
         return getPipedExceptionType((Class<? extends Exception>) causeType);
      }
      else if (Error.class.isAssignableFrom(causeType))
      {
         return getPipedErrorType((Class<? extends Error>) causeType);
      }
      return CopyOnWriteThrowablePipe.class;
   }

   private static Class<? extends PipedError> getPipedErrorType(Class<? extends Error> causeType)
   {
      return PipedError.class;
   }

   private static Class<? extends PipedException> getPipedExceptionType(Class<? extends Exception> causeType)
   {
      if (IOException.class.isAssignableFrom(causeType))
      {
         return PipedIOException.class;
      }
      return PipedException.class;
   }

   public static void throwPipe(ThrowablePipe pipe)
   {
      if (pipe != null)
      {
         pipe.throwPipe();
      }
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
      if (type.isAssignableFrom(pipe.getClass()))
      {
         return (A) pipe;
      }
      final Throwable throwable = pipe.toPipedThrowable();
      if (throwable != null && type.isAssignableFrom(throwable.getClass()))
      {
         return (A) throwable;
      }
      return null;
   }

   static <T extends Throwable> void doAdaptAndThrow(ThrowablePipe pipe, Class<T> type) throws T
   {
      final T throwable = doAdapt(pipe, type);
      if (throwable != null)
      {
         throw throwable;
      }
   }

   static void doAdd(List<Throwable> throwables, Throwable throwable)
   {
      argNotNull(throwables, 0);
      argNotNull(throwable, 1);

      if (!throwables.contains(throwable))
      {
         if (throwable instanceof ThrowablePipe)
         {
            final ThrowablePipe other = ((ThrowablePipe) throwable);
            for (Throwable t : other)
            {
               doAdd(throwables, t);
            }
         }
         else
         {
            throwables.add(throwable);
         }
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

   private static void doPrintStackTrace(ThrowablePipe pipe, final Print print)
   {
      print.ln(pipe);

      final StackTraceElement[] trace = pipe.getStackTrace();

      int min = Math.min(6, trace.length);
      for (int i = 0; i < min; i++)
      {
         print.ln("        at " + trace[i]);
      }

      int left = trace.length - min;
      if (left > 0)
      {
         print.ln("        ... " + left + " more");
      }

      final Throwable cause = pipe.getCause();
      if (cause != null)
      {
         print.ln("Initially caused by: " + cause);

         for (StackTraceElement traceElement : cause.getStackTrace())
         {
            print.ln("        at " + traceElement);
         }

         doPrintStackTrace(print, "    ", "Caused by: ", cause.getCause(), trace);

         for (Throwable follower : pipe.getFollowers())
         {
            doPrintStackTrace(print, "", "Followed by: ", follower, trace);
         }
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
