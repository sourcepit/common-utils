/**
 * Copyright (c) 2011 Sourcepit.org contributors and others. All rights reserved. This program and the accompanying
 * materials are made available under the terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.sourcepit.common.utils.ex;

import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.List;

/**
 * @author Bernd Vogt <bernd.vogt@sourcepit.org>
 */
public interface ThrowableCarrier<T extends Throwable>
{
   <E extends T> E adapt(Class<E> type);

   T getCause();
   
   Throwable toThrowable();

   StackTraceElement[] getStackTrace();

   List<Throwable> getFollowers();
   
   void append(Throwable follower);

   void printStackTrace(final PrintWriter printWriter);

   void printStackTrace(final PrintStream printStream);
}