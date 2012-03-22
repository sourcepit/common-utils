/**
 * Copyright (c) 2011 Sourcepit.org contributors and others. All rights reserved. This program and the accompanying
 * materials are made available under the terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.sourcepit.common.utils.lang;

import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.List;

/**
 * @author Bernd Vogt <bernd.vogt@sourcepit.org>
 */
public interface ThrowablePipe extends Iterable<Throwable>
{
   <T extends Throwable> T adapt(Class<T> type);

   <T extends Throwable> void adaptAndThrow(Class<T> type) throws T;

   Throwable getCause();

   Throwable toThrowable();

   void throwPipe();

   StackTraceElement[] getStackTrace();

   List<Throwable> getFollowers();

   void add(Throwable follower);

   void printStackTrace();
   
   void printStackTrace(final PrintWriter printWriter);

   void printStackTrace(final PrintStream printStream);

}