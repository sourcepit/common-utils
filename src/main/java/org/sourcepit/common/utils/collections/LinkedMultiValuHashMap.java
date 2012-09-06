/**
 * Copyright (c) 2012 Sourcepit.org contributors and others. All rights reserved. This program and the accompanying
 * materials are made available under the terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
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
