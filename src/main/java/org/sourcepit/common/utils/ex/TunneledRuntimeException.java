/**
 * Copyright (c) 2012 Sourcepit.org contributors and others. All rights reserved. This program and the accompanying
 * materials are made available under the terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.sourcepit.common.utils.ex;

import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TunneledRuntimeException extends RuntimeException implements RuntimeExceptionCarrier<RuntimeException>
{
   private static final long serialVersionUID = 1L;

   private final List<Throwable> followers = new ArrayList<Throwable>(1);

   TunneledRuntimeException(RuntimeException cause)
   {
      super(cause instanceof RuntimeExceptionCarrier ? ((RuntimeExceptionCarrier<?>) cause).getCause() : cause);
   }

   @Override
   public RuntimeException getCause()
   {
      return (RuntimeException) super.getCause();
   }

   public TunneledRuntimeException toThrowable()
   {
      return this;
   }

   @Override
   public String getMessage()
   {
      return TunneledThrowable.doGetMessage();
   }

   public <E extends RuntimeException> E adapt(Class<E> type)
   {
      return TunneledThrowable.doAdapt(this, type);
   }

   public List<Throwable> getFollowers()
   {
      return followers == null ? Collections.<Throwable> emptyList() : Collections.unmodifiableList(followers);
   }

   public void append(Throwable follower)
   {
      synchronized (followers)
      {
         TunneledThrowable.doAppend(followers, follower);
      }
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
