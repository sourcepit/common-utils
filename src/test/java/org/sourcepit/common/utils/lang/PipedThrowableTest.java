/**
 * Copyright (c) 2012 Sourcepit.org contributors and others. All rights reserved. This program and the accompanying
 * materials are made available under the terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.sourcepit.common.utils.lang;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.PrintStream;
import java.io.PrintWriter;
import java.sql.SQLException;

import org.junit.Test;

/**
 * @author Bernd Vogt <bernd.vogt@sourcepit.org>
 */
public class PipedThrowableTest
{

   @Test
   public void testNullArgs()
   {
      try
      {
         Exceptions.toThrowablePipe(null);
         fail();
      }
      catch (IllegalArgumentException e)
      {
      }

      try
      {
         Exceptions.toPipedError(null);
         fail();
      }
      catch (IllegalArgumentException e)
      {
      }

      try
      {
         Exceptions.toPipedException(null);
         fail();
      }
      catch (IllegalArgumentException e)
      {
      }


      assertNullArgs(Exceptions.toPipedError(new Error()));

      assertNullArgs(Exceptions.toPipedException(new Exception()));
   }

   private void assertNullArgs(ThrowablePipe pipe)
   {
      try
      {
         pipe.adapt(null);
         fail();
      }
      catch (IllegalArgumentException e)
      {
      }
      try
      {
         pipe.add(null);
         fail();
      }
      catch (IllegalArgumentException e)
      {
      }
      try
      {
         pipe.printStackTrace((PrintStream) null);
         fail();
      }
      catch (IllegalArgumentException e)
      {
      }
      try
      {
         pipe.printStackTrace((PrintWriter) null);
         fail();
      }
      catch (IllegalArgumentException e)
      {
      }
   }

   @Test
   public void testToPipedException() throws Exception
   {
      Exception cause = new Exception();

      PipedException tex = Exceptions.toPipedException(cause);
      assertNotNull(tex);
      assertSame(cause, tex.getCause());
      assertTrue(tex.getFollowers().isEmpty());

      PipedException tex2 = Exceptions.toPipedException(tex);
      assertNotNull(tex);
      assertSame(tex, tex2);
      assertSame(cause, tex2.getCause());
      assertTrue(tex.getFollowers().isEmpty());
   }

   @Test
   public void testToPipedError() throws Exception
   {
      Error cause = new Error();

      PipedError tex = Exceptions.toPipedError(cause);
      assertNotNull(tex);
      assertSame(cause, tex.getCause());
      assertTrue(tex.getFollowers().isEmpty());

      PipedError tex2 = Exceptions.toPipedError(tex);
      assertNotNull(tex);
      assertSame(tex, tex2);
      assertSame(cause, tex2.getCause());
      assertTrue(tex.getFollowers().isEmpty());
   }

   @Test
   public void testPipedExceptionAdaptAndThrow()
   {
      IllegalStateException initialCause = new IllegalStateException(new NullPointerException());

      ThrowablePipe pipe = Exceptions.toThrowablePipe(initialCause);
      pipe.add(new NullPointerException());

      try
      {
         pipe.adaptAndThrow(IllegalStateException.class);
         fail();
      }
      catch (IllegalStateException e)
      {
         assertSame(initialCause, e);
      }

      try
      {
         pipe.adaptAndThrow(RuntimeException.class);
         fail();
      }
      catch (IllegalStateException e)
      {
         assertSame(initialCause, e);
      }

      try
      {
         pipe.adaptAndThrow(Exception.class);
         fail();
      }
      catch (Exception e)
      {
         assertSame(initialCause, e);
      }

      try
      {
         pipe.adaptAndThrow(PipedException.class);
         fail();
      }
      catch (PipedException e)
      {
         assertEquals(pipe, e);
      }

      try
      {
         pipe.adaptAndThrow(NullPointerException.class);
      }
      catch (Exception e)
      {
         fail();
      }

      try
      {
         pipe.adaptAndThrow(SQLException.class);
      }
      catch (Exception e)
      {
         fail();
      }
   }

   private static class MyError extends Error
   {
      private static final long serialVersionUID = 1L;

      public MyError(Throwable cause)
      {
         super(cause);
      }
   }

   @Test
   public void testPipedErrorAdaptAndThrow()
   {
      MyError initialCause = new MyError(new NullPointerException());

      ThrowablePipe pipe = Exceptions.toThrowablePipe(initialCause);
      pipe.add(new NullPointerException());

      try
      {
         pipe.adaptAndThrow(MyError.class);
         fail();
      }
      catch (MyError e)
      {
         assertSame(initialCause, e);
      }

      try
      {
         pipe.adaptAndThrow(Error.class);
         fail();
      }
      catch (MyError e)
      {
         assertSame(initialCause, e);
      }

      try
      {
         pipe.adaptAndThrow(PipedError.class);
         fail();
      }
      catch (PipedError e)
      {
         assertEquals(pipe, e);
      }

      try
      {
         pipe.adaptAndThrow(NullPointerException.class);
      }
      catch (Exception e)
      {
         fail();
      }

      try
      {
         pipe.adaptAndThrow(SQLException.class);
      }
      catch (Exception e)
      {
         fail();
      }
   }
}
