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

package org.sourcepit.common.utils.props;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
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
         return ((PropertiesSource) delegate).get(key, defaultValue);
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

   public void store(OutputStream outputStream)
   {
      if (delegate instanceof PropertiesMap)
      {
         ((PropertiesMap) delegate).store(outputStream);
      }
      else
      {
         PropertiesUtils.store(delegate, outputStream);
      }
   }

   public void store(File file)
   {
      if (delegate instanceof PropertiesMap)
      {
         ((PropertiesMap) delegate).store(file);
      }
      else
      {
         PropertiesUtils.store(delegate, file);
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

   public String get(String key)
   {
      return get((Object) key);
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
         return ((PropertiesSource) delegate).getBoolean(key, defaultValue);
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

   public int getInt(String key, int defaultValue)
   {
      if (delegate instanceof PropertiesMap)
      {
         return ((PropertiesSource) delegate).getInt(key, defaultValue);
      }
      else
      {
         return PropertiesUtils.getInt(this, key, defaultValue);
      }
   }

   public void setInt(String key, int value)
   {
      if (delegate instanceof PropertiesMap)
      {
         ((PropertiesMap) delegate).setInt(key, value);
      }
      else
      {
         PropertiesUtils.setInt(this, key, value);
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
