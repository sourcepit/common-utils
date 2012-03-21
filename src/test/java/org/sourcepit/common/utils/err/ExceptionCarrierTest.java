/**
 * Copyright (c) 2012 Sourcepit.org contributors and others. All rights reserved. This program and the accompanying
 * materials are made available under the terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.sourcepit.common.utils.err;

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * @author Bernd Vogt <bernd.vogt@sourcepit.org>
 */
public class ExceptionCarrierTest
{

   @Test
   public void testJava7()
   {
      Class c = null;

      Throwable root = null;

      try
      {
         c.toString();
      }
      catch (Throwable e)
      {
         root = new IllegalStateException("foo",e);
      }

      nestedMEthod(c, root);

      try
      {
         c.toString();
      }
      catch (Throwable e)
      {
         root.addSuppressed(new IllegalStateException(e));
      }
      
      root.printStackTrace();

   }

   private void nestedMEthod(Class c, Throwable root)
   {
      try
      {
         c.toString();
      }
      catch (Throwable e)
      {
         root.addSuppressed(e);
      }
   }
   
   @Test
   public void testCarrier()
   {
      Class c = null;

      TunneledException root = null;

      try
      {
         c.toString();
      }
      catch (Exception e)
      {
         root = new TunneledException(new IllegalStateException("foo",e));
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

   private void nestedMEthod(Class c, TunneledException root)
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
