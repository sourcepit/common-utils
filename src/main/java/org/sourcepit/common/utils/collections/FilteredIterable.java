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
