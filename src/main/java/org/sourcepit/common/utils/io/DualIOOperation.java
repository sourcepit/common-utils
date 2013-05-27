/**
 * Copyright (c) 2012 Sourcepit.org contributors and others. All rights reserved. This program and the accompanying
 * materials are made available under the terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.sourcepit.common.utils.io;

import java.io.Closeable;
import java.io.IOException;

public abstract class DualIOOperation<I extends Closeable, O extends Closeable> extends IOOperation<I> implements Runnable
{
   private final IOHandle<? extends O> outputResource;

   public DualIOOperation(IOHandle<? extends I> inputResource, IOHandle<? extends O> outputResource)
   {
      super(inputResource);
      this.outputResource = outputResource;
   }

   @Override
   protected void run(final I input) throws IOException
   {
      new IOOperation<O>(outputResource)
      {
         @Override
         protected void run(O output) throws IOException
         {
            DualIOOperation.this.run(input, output);
         }
      }.run();
   }

   protected abstract void run(I input, O output) throws IOException;
}
