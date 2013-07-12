/**
 * Copyright (c) 2012 Sourcepit.org contributors and others. All rights reserved. This program and the accompanying
 * materials are made available under the terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.sourcepit.common.utils.adapt;


public abstract class AbstractAdapterFactory
{
   public final <A> A adapt(Object adaptable, Class<A> adapterType)
   {
      return Adapters.adapt(this, adaptable, adapterType);
   }

   protected abstract <A> A newAdapter(Object adaptable, Class<A> adapterType);
}
