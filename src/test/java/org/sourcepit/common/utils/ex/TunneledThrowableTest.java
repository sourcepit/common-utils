/**
 * Copyright (c) 2012 Sourcepit.org contributors and others. All rights reserved. This program and the accompanying
 * materials are made available under the terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.sourcepit.common.utils.ex;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.PrintStream;
import java.io.PrintWriter;

import org.junit.Test;

/**
 * @author Bernd Vogt <bernd.vogt@sourcepit.org>
 */
public class TunneledThrowableTest
{

   @Test
   public void testNullArgs()
   {
      try
      {
         TunneledThrowable.toThrowableCarrier(null);
         fail();
      }
      catch (IllegalArgumentException e)
      {
      }

      try
      {
         TunneledThrowable.toTunneledError(null);
         fail();
      }
      catch (IllegalArgumentException e)
      {
      }

      try
      {
         TunneledError.toTunneledError(null);
         fail();
      }
      catch (IllegalArgumentException e)
      {
      }

      try
      {
         TunneledThrowable.toTunneledException(null);
         fail();
      }
      catch (IllegalArgumentException e)
      {
      }

      try
      {
         TunneledException.toTunneledException(null);
         fail();
      }
      catch (IllegalArgumentException e)
      {
      }


      assertNullArgs(TunneledThrowable.toTunneledError(new Error()));
      assertNullArgs(TunneledError.toTunneledError(new Error()));

      assertNullArgs(TunneledThrowable.toTunneledException(new Exception()));
      assertNullArgs(TunneledException.toTunneledException(new Exception()));
   }

   private void assertNullArgs(ThrowableCarrier tunneledError)
   {
      try
      {
         tunneledError.adapt(null);
         fail();
      }
      catch (IllegalArgumentException e)
      {
      }
      try
      {
         tunneledError.append(null);
         fail();
      }
      catch (IllegalArgumentException e)
      {
      }
      try
      {
         tunneledError.printStackTrace((PrintStream) null);
         fail();
      }
      catch (IllegalArgumentException e)
      {
      }
      try
      {
         tunneledError.printStackTrace((PrintWriter) null);
         fail();
      }
      catch (IllegalArgumentException e)
      {
      }
   }

   @Test
   public void testToTunneledException() throws Exception
   {
      Exception cause = new Exception();

      TunneledException tex = TunneledThrowable.toTunneledException(cause);
      assertNotNull(tex);
      assertSame(cause, tex.getCause());
      assertTrue(tex.getFollowers().isEmpty());

      TunneledException tex2 = TunneledThrowable.toTunneledException(tex);
      assertNotNull(tex);
      assertSame(tex, tex2);
      assertSame(cause, tex2.getCause());
      assertTrue(tex.getFollowers().isEmpty());

      tex = TunneledException.toTunneledException(cause);
      assertNotNull(tex);
      assertSame(cause, tex.getCause());
      assertTrue(tex.getFollowers().isEmpty());

      tex2 = TunneledException.toTunneledException(tex);
      assertNotNull(tex);
      assertSame(tex, tex2);
      assertSame(cause, tex2.getCause());
      assertTrue(tex.getFollowers().isEmpty());
   }

   @Test
   public void testToTunneledError() throws Exception
   {
      Error cause = new Error();

      TunneledError tex = TunneledThrowable.toTunneledError(cause);
      assertNotNull(tex);
      assertSame(cause, tex.getCause());
      assertTrue(tex.getFollowers().isEmpty());

      TunneledError tex2 = TunneledThrowable.toTunneledError(tex);
      assertNotNull(tex);
      assertSame(tex, tex2);
      assertSame(cause, tex2.getCause());
      assertTrue(tex.getFollowers().isEmpty());

      tex = TunneledError.toTunneledError(cause);
      assertNotNull(tex);
      assertSame(cause, tex.getCause());
      assertTrue(tex.getFollowers().isEmpty());

      tex2 = TunneledError.toTunneledError(tex);
      assertNotNull(tex);
      assertSame(tex, tex2);
      assertSame(cause, tex2.getCause());
      assertTrue(tex.getFollowers().isEmpty());
   }
}
