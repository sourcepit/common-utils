/**
 * Copyright (c) 2011 Sourcepit.org contributors and others. All rights reserved. This program and the accompanying
 * materials are made available under the terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.sourcepit.common.utils.props;

import java.io.File;
import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
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
}
