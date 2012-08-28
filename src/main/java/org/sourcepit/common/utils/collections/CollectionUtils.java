/**
 * Copyright (c) 2012 Sourcepit.org contributors and others. All rights reserved. This program and the accompanying
 * materials are made available under the terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.sourcepit.common.utils.collections;

import java.util.Collection;

/**
 * @author Bernd Vogt <bernd.vogt@sourcepit.org>
 */
public final class CollectionUtils
{
   private CollectionUtils()
   {
      super();
   }

   public static <E, V> V getValue(Collection<E> collection, ValueLookup<E, V> lookup)
   {
      if (collection == null || lookup == null)
      {
         throw new IllegalArgumentException("Arguments must not be null.");
      }

      for (E element : collection)
      {
         final V value = lookup.lookup(element);
         if (value != null)
         {
            return value;
         }
      }
      return null;
   }

   @SuppressWarnings("unchecked")
   public static <T> void addAll(Collection<T> dest, Collection<? super T> src, Class<? extends T> type)
   {
      if (dest == null || src == null || type == null)
      {
         throw new IllegalArgumentException("Arguments must not be null.");
      }
      for (Object element : src)
      {
         if (element != null && type.isAssignableFrom(element.getClass()))
         {
            dest.add((T) element);
         }
      }
   }
}
