/**
 * Copyright (c) 2011 Sourcepit.org contributors and others. All rights reserved. This program and the accompanying
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
public final class TunneledError extends Error implements ErrorCarrier<Error>
{
   private static final long serialVersionUID = 1L;

   private List<Throwable> followers = null;

   TunneledError(Error cause)
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

   public <E extends Error> E adapt(Class<E> type)
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
