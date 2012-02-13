/**
 * Copyright (c) 2011 Sourcepit.org contributors and others. All rights reserved. This program and the accompanying
 * materials are made available under the terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.sourcepit.common.utils.props;

import java.io.File;
import java.io.InputStream;
import java.util.Collection;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

public class DelegatingPropertiesMap implements PropertiesMap
{
   private final Map<String, String> delegate;

   public DelegatingPropertiesMap(Map<String, String> delegate)
   {
      this.delegate = delegate;
   }

   public Map<String, String> getDefaultProperties()
   {
      if (delegate instanceof PropertiesMap)
      {
         return ((PropertiesMap) delegate).getDefaultProperties();
      }
      return null;
   }

   public Properties toJavaProperties()
   {
      if (delegate instanceof PropertiesMap)
      {
         return ((PropertiesMap) delegate).toJavaProperties();
      }
      else
      {
         final Properties javaProperties = new Properties();
         javaProperties.putAll(this);
         return javaProperties;
      }
   }

   public <K, V> void putMap(Map<K, V> map)
   {
      if (delegate instanceof PropertiesMap)
      {
         ((PropertiesMap) delegate).putMap(map);
      }
      else
      {
         PropertiesUtils.putMap(delegate, map);
      }
   }

   public String get(String key, String defaultValue)
   {
      if (delegate instanceof PropertiesMap)
      {
         return ((PropertiesMap) delegate).get(key, defaultValue);
      }
      else
      {
         return PropertiesUtils.getProperty(delegate, key, defaultValue);
      }
   }

   public void load(InputStream inputStream)
   {
      if (delegate instanceof PropertiesMap)
      {
         ((PropertiesMap) delegate).load(inputStream);
      }
      else
      {
         PropertiesUtils.load(inputStream, delegate);
      }
   }

   public void load(ClassLoader classLoader, String resourcePath)
   {
      if (delegate instanceof PropertiesMap)
      {
         ((PropertiesMap) delegate).load(classLoader, resourcePath);
      }
      else
      {
         PropertiesUtils.load(classLoader, resourcePath, delegate);
      }
   }

   public void load(File file)
   {
      if (delegate instanceof PropertiesMap)
      {
         ((PropertiesMap) delegate).load(file);
      }
      else
      {
         PropertiesUtils.load(file, delegate);
      }
   }

   public int size()
   {
      return delegate.size();
   }

   public boolean isEmpty()
   {
      return delegate.isEmpty();
   }

   public boolean containsKey(Object key)
   {
      return delegate.containsKey(key);
   }

   public boolean containsValue(Object value)
   {
      return delegate.containsValue(value);
   }

   public String get(Object key)
   {
      return delegate.get(key);
   }

   public String put(String key, String value)
   {
      return delegate.put(key, value);
   }

   public boolean getBoolean(String key, boolean defaultValue)
   {
      if (delegate instanceof PropertiesMap)
      {
         return ((PropertiesMap) delegate).getBoolean(key, defaultValue);
      }
      else
      {
         return PropertiesUtils.getBoolean(this, key, defaultValue);
      }
   }

   public void setBoolean(String key, boolean value)
   {
      if (delegate instanceof PropertiesMap)
      {
         ((PropertiesMap) delegate).setBoolean(key, value);
      }
      else
      {
         PropertiesUtils.setBoolean(this, key, value);
      }
   }

   public String remove(Object key)
   {
      return delegate.remove(key);
   }

   public void putAll(Map<? extends String, ? extends String> t)
   {
      delegate.putAll(t);
   }

   public void clear()
   {
      delegate.clear();
   }

   public Set<String> keySet()
   {
      return delegate.keySet();
   }

   public Collection<String> values()
   {
      return delegate.values();
   }

   public Set<java.util.Map.Entry<String, String>> entrySet()
   {
      return delegate.entrySet();
   }

   @Override
   public boolean equals(Object obj)
   {
      return delegate.equals(obj);
   }

   @Override
   public int hashCode()
   {
      return delegate.hashCode();
   }

   @Override
   public String toString()
   {
      return delegate.toString();
   }
}
