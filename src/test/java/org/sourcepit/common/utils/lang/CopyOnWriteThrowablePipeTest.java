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

import static org.junit.Assert.*;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.junit.Test;

/**
 * @author Bernd Vogt <bernd.vogt@sourcepit.org>
 */
public class CopyOnWriteThrowablePipeTest
{
   @Test
   public void testToThrowable()
   {
      CopyOnWriteThrowablePipe pipe = new CopyOnWriteThrowablePipe();
      assertNull(pipe.toPipedThrowable());

      IOException ioe = new IOException();
      pipe.add(ioe);
      assertTrue(pipe.toPipedThrowable() instanceof PipedIOException);

      NullPointerException npe = new NullPointerException();
      pipe.add(npe);
      assertTrue(pipe.toPipedThrowable() instanceof PipedIOException);

      PipedIOException pioe = (PipedIOException) pipe.toPipedThrowable();
      assertSame(ioe, pioe.getCause());
      assertSame(1, pioe.getFollowers().size());
      assertSame(npe, pioe.getFollowers().get(0));

      pipe = new CopyOnWriteThrowablePipe(new FileNotFoundException());
      assertTrue(pipe.toPipedThrowable() instanceof PipedIOException);

      pipe = new CopyOnWriteThrowablePipe(new NullPointerException());
      assertFalse(pipe.toPipedThrowable() instanceof PipedIOException);
      assertTrue(pipe.toPipedThrowable() instanceof PipedException);

      pipe = new CopyOnWriteThrowablePipe(new OutOfMemoryError());
      assertFalse(pipe.toPipedThrowable() instanceof PipedException);
      assertTrue(pipe.toPipedThrowable() instanceof PipedError);

      pipe = new CopyOnWriteThrowablePipe(new Throwable());
      assertFalse(pipe.toPipedThrowable() instanceof PipedException);
      assertTrue(pipe.toPipedThrowable() instanceof PipedError);
   }
}
