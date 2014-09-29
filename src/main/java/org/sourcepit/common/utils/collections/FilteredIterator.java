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