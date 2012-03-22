/**
 * Copyright (c) 2012 Sourcepit.org contributors and others. All rights reserved. This program and the accompanying
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
public class TunneledException extends RuntimeException implements ExceptionCarrier<Exception>
{
   private static final long serialVersionUID = 1L;

   private final List<Throwable> followers = new CopyOnWriteArrayList<Throwable>();

   public static TunneledException toTunneledException(Exception exception)
   {
      if (exception instanceof TunneledException)
      {
         return (TunneledException) exception;
      }

      TunneledThrowable.argNotNull(exception, 0);
      return new TunneledException(exception);
   }

   private TunneledException(Exception cause)
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
      return TunneledThrowable.doGetMessage();
   }

   public <E extends Exception> E adapt(Class<E> type)
   {
      return TunneledThrowable.doAdapt(this, type);
   }

   public List<Throwable> getFollowers()
   {
      return Collections.unmodifiableList(followers);
   }

   public void append(Throwable follower)
   {
      TunneledThrowable.doAppend(followers, follower);
   }

   public TunneledException toThrowable()
   {
      return this;
   }

   @Override
   public void printStackTrace(PrintStream printStream)
   {
      TunneledThrowable.doPrintStackTrace(this, printStream);
   }

   @Override
   public void printStackTrace(PrintWriter printWriter)
   {
      TunneledThrowable.doPrintStackTrace(this, printWriter);
   }
}
