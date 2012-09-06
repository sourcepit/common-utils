/**
 * Copyright (c) 2012 Sourcepit.org contributors and others. All rights reserved. This program and the accompanying
 * materials are made available under the terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.sourcepit.common.utils.collections;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class FilteredIterator<E> implements Iterator<E>
{
   private final Iterator<E> target;
   private final Collection<?> filter;

   private E next;

   public FilteredIterator(Iterator<E> target, Object... filtered)
   {
      this(target, Arrays.asList(filtered));
   }

   public FilteredIterator(Iterator<E> target, Collection<?> filter)
   {
      this.target = target;
      this.filter = filter;
   }

   public boolean hasNext()
   {
      return getNext(false) != null;
   }

   public E next()
   {
      final E next = getNext(true);
      if (next == null)
      {
         throw new NoSuchElementException();
      }
      return next;
   }

   public void remove()
   {
      target.remove();
   }

   private E getNext(boolean reset)
   {
      if (next == null)
      {
         do
         {
            if (target.hasNext())
            {
               next = target.next();
            }
            else
            {
               next = null;
            }
         }
         while (next != null && filter.contains(next));
      }

      final E result = next;
      if (reset)
      {
         next = null;
      }
      return result;
   }
}