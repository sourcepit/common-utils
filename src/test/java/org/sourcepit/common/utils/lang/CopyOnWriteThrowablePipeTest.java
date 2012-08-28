/**
 * Copyright (c) 2012 Sourcepit.org contributors and others. All rights reserved. This program and the accompanying
 * materials are made available under the terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
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
