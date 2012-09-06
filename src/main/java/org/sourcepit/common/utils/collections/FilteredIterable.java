/**
 * Copyright (c) 2012 Sourcepit.org contributors and others. All rights reserved. This program and the accompanying
 * materials are made available under the terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.sourcepit.common.utils.collections;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;

public class FilteredIterable<E> implements Iterable<E>
{
   private final Iterable<E> target;
   private final Collection<?> filter;

   public FilteredIterable(Iterable<E> target, Object... filtered)
   {
      this(target, filtered == null ? Collections.emptyList() : Arrays.asList(filtered));
   }

   public FilteredIterable(Iterable<E> target, Collection<?> filter)
   {
      this.target = target;
      this.filter = filter;
   }

   public Iterator<E> iterator()
   {
      return new FilteredIterator<E>(target.iterator(), filter);
   }
}
