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
public final class TunneledError extends Error implements ErrorCarrier
{
   private static final long serialVersionUID = 1L;

   private final List<Throwable> followers = new CopyOnWriteArrayList<Throwable>();

   public static TunneledError toTunneledError(Error error)
   {
      if (error instanceof TunneledError)
      {
         return (TunneledError) error;
      }
      TunneledThrowable.argNotNull(error, 0);
      return new TunneledError(error);
   }

   private TunneledError(Error cause)
   {
      super(cause);
   }

   @Override
   public Error getCause()
   {
      return (Error) super.getCause();
   }

   public Error toThrowable()
   {
      return this;
   }

   @Override
   public String getMessage()
   {
      return TunneledThrowable.doGetMessage();
   }

   public <A> A adapt(Class<A> type)
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
