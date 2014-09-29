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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;
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
         Exceptions.pipe((Throwable) null);
         fail();
      }
      catch (IllegalArgumentException e)
      {
      }

      try
      {
         Exceptions.pipe((Error) null);
         fail();
      }
      catch (IllegalArgumentException e)
      {
      }

      try
      {
         Exceptions.pipe((Exception) null);
         fail();
      }
      catch (IllegalArgumentException e)
      {
      }

      try
      {
         Exceptions.pipe((IOException) null);
         fail();
      }
      catch (IllegalArgumentException e)
      {
      }


      assertNullArgs(Exceptions.pipe(new Error()));

      assertNullArgs(Exceptions.pipe(new Exception()));
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

      PipedException tex = Exceptions.pipe(cause);
      assertNotNull(tex);
      assertSame(cause, tex.getCause());
      assertTrue(tex.getFollowers().isEmpty());

      PipedException tex2 = Exceptions.pipe(tex);
      assertNotNull(tex);
      assertSame(tex, tex2);
      assertSame(cause, tex2.getCause());
      assertTrue(tex.getFollowers().isEmpty());
   }

   @Test
   public void testToPipedError() throws Exception
   {
      Error cause = new Error();

      PipedError tex = Exceptions.pipe(cause);
      assertNotNull(tex);
      assertSame(cause, tex.getCause());
      assertTrue(tex.getFollowers().isEmpty());

      PipedError tex2 = Exceptions.pipe(tex);
      assertNotNull(tex);
      assertSame(tex, tex2);
      assertSame(cause, tex2.getCause());
      assertTrue(tex.getFollowers().isEmpty());
   }

   @Test
   public void testPipeThrowable() throws Exception
   {
      Throwable cause = new Throwable();

      ThrowablePipe tex = Exceptions.pipe(cause);
      assertNotNull(tex);
      assertSame(cause, tex.getCause());
      assertTrue(tex.getFollowers().isEmpty());
      assertFalse(tex instanceof Throwable);

      PipedError pipedError = (PipedError) tex.toPipedThrowable(); // test omitted throwable
      assertSame(cause, pipedError.getCause());
   }

   @Test
   public void testPipedExceptionAdaptAndThrow()
   {
      IllegalStateException initialCause = new IllegalStateException(new NullPointerException());

      ThrowablePipe pipe = Exceptions.pipe(initialCause);
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

      ThrowablePipe pipe = Exceptions.pipe(initialCause);
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
