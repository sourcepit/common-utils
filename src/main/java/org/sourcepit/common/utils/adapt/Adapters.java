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

package org.sourcepit.common.utils.adapt;

import java.util.ArrayList;
import java.util.List;
import java.util.WeakHashMap;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public final class Adapters
{
   private static final WeakHashMap<Object, List<Object>> ADAPTERS = new WeakHashMap<Object, List<Object>>();

   private static final ReadWriteLock RW_LOCK = new ReentrantReadWriteLock();

   private Adapters()
   {
      super();
   }

   public static List<Object> getAdapters(Object adaptable)
   {
      RW_LOCK.readLock().lock();
      try
      {
         final List<Object> adapters = ADAPTERS.get(adaptable);
         if (adapters != null)
         {
            return new ArrayList<Object>(adapters);
         }
         return new ArrayList<Object>(0);
      }
      finally
      {
         RW_LOCK.readLock().unlock();
      }
   }

   public static <A> List<A> getAdapters(Object adaptable, Class<A> adapterType)
   {
      RW_LOCK.readLock().lock();
      try
      {
         final List<Object> adapters = ADAPTERS.get(adaptable);
         return findAdapters(adapters, adapterType);
      }
      finally
      {
         RW_LOCK.readLock().unlock();
      }
   }

   public static <A> A getAdapter(Object adaptable, Class<A> adapterType)
   {
      RW_LOCK.readLock().lock();
      try
      {
         final List<Object> adapters = ADAPTERS.get(adaptable);
         if (adapters != null)
         {
            return findAdapter(adapters, adapterType);
         }
         return null;
      }
      finally
      {
         RW_LOCK.readLock().unlock();
      }
   }

   public static <A> A adapt(AbstractAdapterFactory adapterFactory, Object adaptable, Class<A> adapterType)
   {
      final A adapter = getAdapter(adaptable, adapterType);
      if (adapter != null)
      {
         return adapter;
      }
      return newAdapter(adapterFactory, adaptable, adapterType);
   }

   public static void addAdapter(Object adaptable, Object adapter)
   {
      RW_LOCK.writeLock().lock();
      try
      {
         List<Object> adapters = ADAPTERS.get(adaptable);
         if (adapters == null)
         {
            adapters = new ArrayList<Object>();
            ADAPTERS.put(adaptable, adapters);
         }
         adapters.add(adapter);
      }
      finally
      {
         RW_LOCK.writeLock().unlock();
      }
   }

   public static <A> void removeAdapters(Object adaptable, Class<A> adapterType)
   {
      RW_LOCK.writeLock().lock();
      try
      {
         final List<Object> adapters = ADAPTERS.get(adaptable);
         if (adapters != null)
         {
            adapters.removeAll(findAdapters(adapters, adapterType));
         }
      }
      finally
      {
         RW_LOCK.writeLock().unlock();
      }
   }

   private static <A> A newAdapter(AbstractAdapterFactory adapterFactory, Object adaptable, Class<A> adapterType)
   {
      RW_LOCK.writeLock().lock();
      try
      {
         List<Object> adapters = ADAPTERS.get(adaptable);
         if (adapters == null)
         {
            adapters = new ArrayList<Object>();
            ADAPTERS.put(adaptable, adapters);
         }
         else
         {
            // double check
            A adapter = findAdapter(adapters, adapterType);
            if (adapter != null)
            {
               return adapter;
            }
         }

         A adapter = adapterFactory.newAdapter(adaptable, adapterType);
         if (adapter != null)
         {
            adapters.add(adapter);
         }
         return adapter;
      }
      finally
      {
         RW_LOCK.writeLock().unlock();
      }
   }

   @SuppressWarnings("unchecked")
   public static <A> A findAdapter(List<Object> adapters, Class<A> adapterType)
   {
      for (Object adapter : adapters)
      {
         if (isAdapterForType(adapter, adapterType))
         {
            return (A) adapter;
         }
      }
      return null;
   }

   @SuppressWarnings("unchecked")
   public static <A> List<A> findAdapters(final List<Object> adapters, Class<A> adapterType)
   {
      final List<A> result = new ArrayList<A>();
      if (adapters != null)
      {
         for (Object adapter : adapters)
         {
            if (isAdapterForType(adapter, adapterType))
            {
               result.add((A) adapter);
            }
         }
      }
      return result;
   }

   public static <A> boolean isAdapterForType(Object adapter, Class<A> adapterType)
   {
      return adapterType.isAssignableFrom(adapter.getClass());
   }
}
