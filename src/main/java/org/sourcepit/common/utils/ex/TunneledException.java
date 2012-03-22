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

/**
 * @author Bernd Vogt <bernd.vogt@sourcepit.org>
 */
public class TunneledException extends RuntimeException implements ExceptionCarrier<Exception>
{
   private static final long serialVersionUID = 1L;

   TunneledException(Exception cause)
   {
      super(cause);
   }

   @Override
   public Exception getCause()
   {
      return (Exception) super.getCause();
   }

   private List<Throwable> followers = null;

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
      return followers == null ? Collections.<Throwable> emptyList() : Collections.unmodifiableList(followers);
   }

   public void append(Throwable follower)
   {
      synchronized (followers)
      {
         if (followers == null)
         {
            followers = new ArrayList<Throwable>();
         }
         TunneledThrowable.doAppend(followers, follower);
      }
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
