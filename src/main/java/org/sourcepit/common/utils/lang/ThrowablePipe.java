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

import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.Serializable;
import java.util.List;

/**
 * @author Bernd Vogt <bernd.vogt@sourcepit.org>
 */
public interface ThrowablePipe extends Iterable<Throwable>, Serializable
{
   List<Throwable> getThrowables();

   boolean isEmpty();

   Throwable getCause();

   List<Throwable> getFollowers();

   void add(Throwable throwable);

   <T extends Throwable> T adapt(Class<T> type);

   <T extends Throwable> void adaptAndThrow(Class<T> type) throws T;

   Throwable toPipedThrowable();

   void throwPipe();

   StackTraceElement[] getStackTrace();

   void printStackTrace();

   void printStackTrace(final PrintWriter printWriter);

   void printStackTrace(final PrintStream printStream);
}