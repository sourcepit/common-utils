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
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;

public class LinkedPropertiesMap extends LinkedHashMap<String, String> implements PropertiesMap
{
   private static final long serialVersionUID = -3020824767316453217L;

   private Map<String, String> defaultProperties;

   public LinkedPropertiesMap(int initialCapacity, float loadFactor)
   {
      super(initialCapacity, loadFactor);
   }

   public LinkedPropertiesMap(int initialCapacity)
   {
      super(initialCapacity);
   }

   public LinkedPropertiesMap()
   {
      super();
   }

   public LinkedPropertiesMap(Map<String, String> copyMe)
   {
      super(copyMe);
   }

   public LinkedPropertiesMap(int initialCapacity, float loadFactor, boolean accessOrder)
   {
      super(initialCapacity, loadFactor);
   }

   public void setDefaultProperties(Map<String, String> defaulProperties)
   {
      this.defaultProperties = defaulProperties;
   }

   public Map<String, String> getDefaultProperties()
   {
      return defaultProperties;
   }

   public Properties toJavaProperties()
   {
      final Properties defaults;
      if (defaultProperties == null)
      {
         defaults = null;
      }
      else if (defaultProperties instanceof PropertiesMap)
      {
         defaults = ((PropertiesMap) defaultProperties).toJavaProperties();
      }
      else
      {
         defaults = new Properties();
         defaults.putAll(defaultProperties);
      }
      final Properties javaProperties = new Properties(defaults);
      javaProperties.putAll(this);
      return javaProperties;
   }

   public <K, V> void putMap(Map<K, V> map)
   {
      for (Entry<K, V> entry : map.entrySet())
      {
         put(entry.getKey().toString(), entry.getValue().toString());
      }
   }

   public String get(String key)
   {
      return get((Object) key);
   }

   @Override
   public String get(Object key)
   {
      final String value = super.get(key);
      if (value == null && defaultProperties != null)
      {
         return defaultProperties.get(key);
      }
      return value;
   }

   public String get(String key, String defaultValue)
   {
      final String value = get(key);
      if (value == null)
      {
         return defaultValue;
      }
      return value;
   }

   public boolean getBoolean(String key, boolean defaultValue)
   {
      return PropertiesUtils.getBoolean(this, key, defaultValue);
   }

   public void setBoolean(String key, boolean value)
   {
      PropertiesUtils.setBoolean(this, key, value);
   }

   public int getInt(String key, int defaultValue)
   {
      return PropertiesUtils.getInt(this, key, defaultValue);
   }

   public void setInt(String key, int value)
   {
      PropertiesUtils.setInt(this, key, value);
   }

   public void load(InputStream inputStream)
   {
      PropertiesUtils.load(inputStream, this);
   }

   public void load(File file)
   {
      PropertiesUtils.load(file, this);
   }

   public void load(ClassLoader classLoader, String resourcePath)
   {
      PropertiesUtils.load(classLoader, resourcePath, this);
   }

   public void store(File file)
   {
      PropertiesUtils.store(this, file);
   }

   public void store(OutputStream outputStream)
   {
      PropertiesUtils.store(this, outputStream);
   }
}
