/**
 * Copyright (c) 2012 Sourcepit.org contributors and others. All rights reserved. This program and the accompanying
 * materials are made available under the terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.sourcepit.common.utils.lang;

import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author Bernd Vogt <bernd.vogt@sourcepit.org>
 */
public class PipedException extends RuntimeException implements ExceptionPipe
{
   private static final long serialVersionUID = 1L;

   private final List<Throwable> followers = new CopyOnWriteArrayList<Throwable>();

   PipedException(Exception cause)
   {
      super(cause);
   }

   @Override
   public Exception getCause()
   {
      return (Exception) super.getCause();
   }

   public void throwPipe()
   {
      throw this;
   }

   @Override
   public String getMessage()
   {
      return Exceptions.doGetMessage();
   }

   public List<Throwable> getFollowers()
   {
      return Collections.unmodifiableList(followers);
   }

   public void add(Throwable follower)
   {
      Exceptions.doAppend(followers, follower);
   }

   public PipedException toThrowable()
   {
      return this;
   }

   public Iterator<Throwable> iterator()
   {
      return Exceptions.iterator(this);
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

   public <T extends Throwable> T adapt(Class<T> type)
   {
      return Exceptions.doAdapt(this, type);
   }

   public <T extends Throwable> void adaptAndThrow(Class<T> type) throws T
   {
      Exceptions.doAdaptAndThrow(this, type);
   }
}
