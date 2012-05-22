/**
 * Copyright (c) 2011 Sourcepit.org contributors and others. All rights reserved. This program and the accompanying
 * materials are made available under the terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.sourcepit.common.utils.props;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.util.Collections;
import java.util.Dictionary;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import org.apache.commons.io.IOUtils;

public final class PropertiesUtils
{
   private PropertiesUtils()
   {
      super();
   }

   public static Properties load(File propertiesFile)
   {
      final Properties properties = new Properties();
      load(propertiesFile, properties);
      return properties;
   }

   public static void store(Properties properties, File propertiesFile)
   {
      final OutputStream out;
      try
      {
         out = new FileOutputStream(propertiesFile);
      }
      catch (FileNotFoundException e)
      {
         throw new IllegalArgumentException(e);
      }

      try
      {
         properties.store(out, null);
      }
      catch (IOException e)
      {
         throw new IllegalStateException(e);
      }
      finally
      {
         IOUtils.closeQuietly(out);
      }

   }

   public static void load(File propertiesFile, Properties properties)
   {
      InputStream in = null;
      try
      {
         in = new FileInputStream(propertiesFile);
         load(in, properties);
      }
      catch (FileNotFoundException e)
      {
         throw new IllegalArgumentException(e);
      }
      finally
      {
         IOUtils.closeQuietly(in);
      }
   }

   public static void load(InputStream inputStream, Properties properties)
   {
      try
      {
         properties.load(inputStream);
      }
      catch (IOException e)
      {
         throw new IllegalStateException(e);
      }
   }

   @SuppressWarnings("rawtypes")
   public static void load(File propertiesFile, final Dictionary/* <String, String> */properties)
   {
      final Properties delegate = new Properties()
      {
         private static final long serialVersionUID = 1L;

         @Override
         @SuppressWarnings("unchecked")
         public synchronized Object put(Object key, Object value)
         {
            properties.put(key.toString(), value.toString());
            return super.put(key, value);
         }
      };
      load(propertiesFile, delegate);
   }

   @SuppressWarnings("rawtypes")
   public static void load(InputStream inputStream, final Dictionary/* <String, String> */properties)
   {
      final Properties delegate = new Properties()
      {
         private static final long serialVersionUID = 1L;

         @Override
         @SuppressWarnings("unchecked")
         public synchronized Object put(Object key, Object value)
         {
            properties.put(key.toString(), value.toString());
            return super.put(key, value);
         }
      };
      load(inputStream, delegate);
   }

   @SuppressWarnings("rawtypes")
   public static void load(File propertiesFile, final Map/* <String, String> */properties)
   {
      final Properties delegate = new Properties()
      {
         private static final long serialVersionUID = 1L;

         @Override
         @SuppressWarnings("unchecked")
         public synchronized Object put(Object key, Object value)
         {
            properties.put(key.toString(), value.toString());
            return super.put(key, value);
         }
      };
      load(propertiesFile, delegate);
   }

   @SuppressWarnings("rawtypes")
   public static void load(InputStream inputStream, final Map/* <String, String> */properties)
   {
      final Properties delegate = new Properties()
      {
         private static final long serialVersionUID = 1L;

         @Override
         @SuppressWarnings("unchecked")
         public synchronized Object put(Object key, Object value)
         {
            properties.put(key.toString(), value.toString());
            return super.put(key, value);
         }
      };
      load(inputStream, delegate);
   }

   @SuppressWarnings("rawtypes")
   public static void load(ClassLoader classLoader, String resourcePath, final Map/* <String, String> */properties)
   {
      final InputStream inputStream = classLoader.getResourceAsStream(resourcePath);
      if (inputStream == null)
      {
         try
         {
            throw new FileNotFoundException(resourcePath);
         }
         catch (FileNotFoundException e)
         {
            throw new IllegalArgumentException(e);
         }
      }
      load(inputStream, properties);
   }

   @SuppressWarnings({ "unchecked", "rawtypes" })
   public static <K, V> void putMap(Map/* <String, String> */target, Map<K, V> map)
   {
      for (Entry<K, V> entry : map.entrySet())
      {
         target.put(entry.getKey().toString(), entry.getValue().toString());
      }
   }

   public static PropertiesMap unmodifiablePropertiesMap(final Map<String, String> map)
   {
      return new DelegatingPropertiesMap(Collections.unmodifiableMap(map));
   }

   public static String escapeJavaProperties(String string)
   {
      final Properties properties = new Properties();
      properties.put("", string);

      final ByteArrayOutputStream out = new ByteArrayOutputStream();
      try
      {
         properties.store(out, null);
      }
      catch (IOException e)
      {
         throw new IllegalStateException(e);
      }

      final String result;
      try
      {
         result = new String(out.toByteArray(), "8859_1");
      }
      catch (UnsupportedEncodingException e)
      {
         throw new IllegalStateException(e);
      }

      final String propertyLine;
      try
      {
         final BufferedReader br = new BufferedReader(new StringReader(result));
         br.readLine();
         propertyLine = br.readLine();
      }
      catch (IOException e)
      {
         throw new IllegalStateException(e);
      }

      return propertyLine.substring(1);
   }

   public static String getProperty(Map<String, String> map, String key, String defaultValue)
   {
      final String property = map.get(key);
      if (property == null)
      {
         return defaultValue;
      }
      return property;
   }

   public static boolean getBoolean(Map<String, String> map, String key, boolean defaultValue)
   {
      return Boolean.valueOf(getProperty(map, key, Boolean.toString(defaultValue))).booleanValue();
   }

   public static void setBoolean(Map<String, String> map, String key, boolean value)
   {
      map.put(key, Boolean.toString(value));
   }
}
