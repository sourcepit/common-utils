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

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;

/**
 * @author Bernd Vogt <bernd.vogt@sourcepit.org>
 */
public class LinkedMultiValuHashMap<K, V> extends LinkedHashMap<K, Collection<V>> implements MultiValueMap<K, V>
{
   private static final long serialVersionUID = 1L;

   private final Class<? extends Collection<V>> implType;

   public LinkedMultiValuHashMap()
   {
      this(ArrayList.class);
   }

   @SuppressWarnings("unchecked")
   public LinkedMultiValuHashMap(@SuppressWarnings("rawtypes") Class<? extends Collection> implType)
   {
      this.implType = (Class<? extends Collection<V>>) implType;
   }

   public Collection<V> get(K key, boolean createOnDemand)
   {
      Collection<V> values = super.get(key);
      if (values == null && createOnDemand)
      {
         values = newValueCollection();
         put((K) key, values);
      }
      return values;

   };

   private Collection<V> newValueCollection()
   {
      try
      {
         return implType.newInstance();
      }
      catch (InstantiationException e)
      {
         throw new IllegalStateException(e);
      }
      catch (IllegalAccessException e)
      {
         throw new IllegalStateException(e);
      }
   }

}
