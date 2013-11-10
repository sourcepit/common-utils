/**
 * Copyright (c) 2012 Sourcepit.org contributors and others. All rights reserved. This program and the accompanying
 * materials are made available under the terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.sourcepit.common.utils.collections;

import java.util.Collection;
import java.util.Iterator;

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

   public static <T, IterableException extends Exception, FunctorException extends Exception> void foreach(
      Iterable2<T, IterableException> iterable, Functor<T, FunctorException> functor) throws IterableException,
      FunctorException
   {
      final Iterator2<T, IterableException> iterator = iterable.iterator();
      while (iterator.hasNext())
      {
         functor.apply(iterator.next());
      }
   }

   public static <T, FunctorException extends Exception> void foreach(Iterable<T> iterable,
      Functor<T, FunctorException> functor) throws FunctorException
   {
      final Iterator<T> iterator = iterable.iterator();
      while (iterator.hasNext())
      {
         functor.apply(iterator.next());
      }
   }

   public static <T, FunctorException extends Exception> void foreach(T[] iterable, Functor<T, FunctorException> functor)
      throws FunctorException
   {
      for (T value : iterable)
      {
         functor.apply(value);
      }
   }
}
