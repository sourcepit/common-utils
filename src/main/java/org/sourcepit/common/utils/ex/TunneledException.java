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
public class TunneledException extends RuntimeException implements ExceptionCarrier
{
   private static final long serialVersionUID = 1L;

   private final List<Throwable> followers = new CopyOnWriteArrayList<Throwable>();

   TunneledException(Exception cause)
   {
      super(cause);
   }

   @Override
   public Exception getCause()
   {
      return (Exception) super.getCause();
   }

   public void doThow()
   {
      throw this;
   }

   @Override
   public String getMessage()
   {
      return Exceptions.doGetMessage();
   }

   public <A> A adapt(Class<A> type)
   {
      return Exceptions.doAdapt(this, type);
   }

   public List<Throwable> getFollowers()
   {
      return Collections.unmodifiableList(followers);
   }

   public void append(Throwable follower)
   {
      Exceptions.doAppend(followers, follower);
   }

   public TunneledException toThrowable()
   {
      return this;
   }

   @Override
   public void printStackTrace(PrintStream printStream)
   {
      Exceptions.doPrintStackTrace(this, printStream);
   }

   @Override
   public void printStackTrace(PrintWriter printWriter)
   {
      Exceptions.doPrintStackTrace(this, printWriter);
   }
}
