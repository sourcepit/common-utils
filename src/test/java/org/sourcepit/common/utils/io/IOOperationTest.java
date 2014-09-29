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

package org.sourcepit.common.utils.io;

import static org.hamcrest.core.IsSame.sameInstance;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.Closeable;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

import org.hamcrest.core.Is;
import org.junit.Test;
import org.sourcepit.common.utils.lang.PipedError;
import org.sourcepit.common.utils.lang.PipedIOException;

/**
 * @author Bernd Vogt <bernd.vogt@sourcepit.org>
 */
public class IOOperationTest
{

   @Test
   public void testRunOperation() throws Exception
   {
      @SuppressWarnings("unchecked")
      final IOHandle<Closeable> resource = mock(IOHandle.class);

      final Closeable closeable = mock(Closeable.class);
      when(resource.open()).thenReturn(closeable);

      final AtomicInteger operationCalls = new AtomicInteger();

      new IOOperation<Closeable>(resource)
      {
         @Override
         protected void run(Closeable openResource) throws IOException
         {
            assertThat(openResource, sameInstance(closeable));
            operationCalls.incrementAndGet();
         }
      }.run();

      assertThat(operationCalls.get(), Is.is(1));
      verify(resource).open();
      verify(closeable).close();
   }

   @Test
   public void testThrowIOException() throws Exception
   {
      final Closeable closeable = mock(Closeable.class);
      doThrow(new IOException()).when(closeable).close();

      @SuppressWarnings("unchecked")
      final IOHandle<Closeable> resource = mock(IOHandle.class);
      when(resource.open()).thenReturn(closeable);

      final AtomicInteger operationCalls = new AtomicInteger();

      final IOOperation<Closeable> ioop = new IOOperation<Closeable>(resource)
      {
         @Override
         protected void run(Closeable openResource) throws IOException
         {
            assertThat(openResource, sameInstance(closeable));
            operationCalls.incrementAndGet();

            throw new IOException();
         }
      };

      try
      {
         ioop.run();
         fail();
      }
      catch (PipedIOException e)
      {
      }

      assertThat(operationCalls.get(), Is.is(1));
      verify(resource).open();
      verify(closeable).close();
   }

   @Test
   public void testThrowError() throws Exception
   {
      final Closeable closeable = mock(Closeable.class);
      doThrow(new IOException()).when(closeable).close();

      @SuppressWarnings("unchecked")
      final IOHandle<Closeable> resource = mock(IOHandle.class);
      when(resource.open()).thenReturn(closeable);

      final AtomicInteger operationCalls = new AtomicInteger();

      final IOOperation<Closeable> ioop = new IOOperation<Closeable>(resource)
      {
         @Override
         protected void run(Closeable openResource) throws IOException
         {
            assertThat(openResource, sameInstance(closeable));
            operationCalls.incrementAndGet();

            throw new OutOfMemoryError();
         }
      };

      try
      {
         ioop.run();
         fail();
      }
      catch (PipedError e)
      {
      }

      assertThat(operationCalls.get(), Is.is(1));
      verify(resource).open();
      verify(closeable).close();
   }

   @Test
   public void testThrowIOExceptionOnOpenResource() throws Exception
   {
      @SuppressWarnings("unchecked")
      final IOHandle<Closeable> resource = mock(IOHandle.class);
      doThrow(new IOException()).when(resource).open();

      final AtomicInteger operationCalls = new AtomicInteger();

      final IOOperation<Closeable> ioop = new IOOperation<Closeable>(resource)
      {
         @Override
         protected void run(Closeable openResource) throws IOException
         {
            operationCalls.incrementAndGet();
         }
      };

      try
      {
         ioop.run();
         fail();
      }
      catch (PipedIOException e)
      {
      }

      assertThat(operationCalls.get(), Is.is(0));
      verify(resource).open();
   }

   @Test
   public void testThrowErrorOnOpenResource() throws Exception
   {
      @SuppressWarnings("unchecked")
      final IOHandle<Closeable> resource = mock(IOHandle.class);
      doThrow(new OutOfMemoryError()).when(resource).open();

      final AtomicInteger operationCalls = new AtomicInteger();

      final IOOperation<Closeable> ioop = new IOOperation<Closeable>(resource)
      {
         @Override
         protected void run(Closeable openResource) throws IOException
         {
            operationCalls.incrementAndGet();
         }
      };

      try
      {
         ioop.run();
         fail();
      }
      catch (PipedError e)
      {
      }

      assertThat(operationCalls.get(), Is.is(0));
      verify(resource).open();
   }
}
