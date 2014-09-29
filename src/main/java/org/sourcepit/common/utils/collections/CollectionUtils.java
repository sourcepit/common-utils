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
