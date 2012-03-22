/**
 * Copyright (c) 2012 Sourcepit.org contributors and others. All rights reserved. This program and the accompanying
 * materials are made available under the terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.sourcepit.common.utils.err;

import org.junit.Test;
import org.sourcepit.common.utils.ex.TunneledException;
import org.sourcepit.common.utils.ex.TunneledRuntimeException;
import org.sourcepit.common.utils.ex.TunneledThrowable;

/**
 * @author Bernd Vogt <bernd.vogt@sourcepit.org>
 */
public class ExceptionCarrierTest
{

   // @Test
   // public void testJava7()
   // {
   // Class c = null;
   //
   // Throwable root = null;
   //
   // try
   // {
   // c.toString();
   // }
   // catch (Throwable e)
   // {
   // root = new IllegalStateException("foo",e);
   // }
   //
   // nestedMEthod(c, root);
   //
   // try
   // {
   // c.toString();
   // }
   // catch (Throwable e)
   // {
   // root.addSuppressed(new IllegalStateException(e));
   // }
   //
   // root.printStackTrace();
   //
   // }
   //
   // private void nestedMEthod(Class c, Throwable root)
   // {
   // try
   // {
   // c.toString();
   // }
   // catch (Throwable e)
   // {
   // root.addSuppressed(e);
   // }
   // }
   //
   @Test
   public void testCarrier()
   {
      Class c = null;

      TunneledRuntimeException root = null;

      try
      {
         c.toString();
      }
      catch (Exception e)
      {
         root = TunneledThrowable.tunnel(new IllegalStateException("foo", e)).toThrowable();
      }

      nestedMEthod(c, root);

      try
      {
         c.toString();
      }
      catch (Throwable e)
      {
         root.append(new IllegalStateException(e));
      }

      root.printStackTrace();

   }

   private void nestedMEthod(Class c, TunneledRuntimeException root)
   {
      try
      {
         c.toString();
      }
      catch (Throwable e)
      {
         root.append(e);
      }
   }

}
