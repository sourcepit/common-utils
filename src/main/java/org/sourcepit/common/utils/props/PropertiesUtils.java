/**
 * Copyright (c) 2011 Sourcepit.org contributors and others. All rights reserved. This program and the accompanying
 * materials are made available under the terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.sourcepit.common.utils.props;

import static org.sourcepit.common.utils.io.IOResources.buffOut;
import static org.sourcepit.common.utils.io.IOResources.fileOut;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.util.Collections;
import java.util.Dictionary;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.io.IOUtils;
import org.sourcepit.common.utils.io.IOOperation;
import org.sourcepit.common.utils.io.IOResource;
import org.sourcepit.common.utils.lang.Exceptions;

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

   @SuppressWarnings("rawtypes")
   public static void store(final Map/* <String, String> */properties, File file)
   {
      store(properties, (IOResource<? extends OutputStream>) buffOut(fileOut(file, true)));
   }

   @SuppressWarnings("rawtypes")
   public static void store(final Map/* <String, String> */properties, IOResource<? extends OutputStream> resource)
   {
      new IOOperation<OutputStream>(resource)
      {
         @Override
         protected void run(OutputStream outputStream) throws IOException
         {
            store(properties, outputStream);
         }
      }.run();
   }

   @SuppressWarnings({ "unchecked", "rawtypes" })
   public static void store(final Map/* <String, String> */properties, OutputStream outputStream)
   {
      try
      {
         final BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(outputStream, "8859_1"));
         for (Entry<?, ?> entry : (Set<Entry<?, ?>>) properties.entrySet())
         {
            final String key = saveConvert(entry.getKey().toString(), true, true);
            final String value = saveConvert(entry.getValue().toString(), false, true);
            bw.write(key + "=" + value);
            bw.newLine();
         }
         bw.flush();
      }
      catch (IOException e)
      {
         throw Exceptions.pipe(e);
      }
   }

   private static String saveConvert(String theString, boolean escapeSpace, boolean escapeUnicode)
   {
      int len = theString.length();
      int bufLen = len * 2;
      if (bufLen < 0)
      {
         bufLen = Integer.MAX_VALUE;
      }
      StringBuilder outBuffer = new StringBuilder(bufLen);

      for (int x = 0; x < len; x++)
      {
         char aChar = theString.charAt(x);
         // Handle common case first, selecting largest block that
         // avoids the specials below
         if ((aChar > 61) && (aChar < 127))
         {
            if (aChar == '\\')
            {
               outBuffer.append('\\');
               outBuffer.append('\\');
               continue;
            }
            outBuffer.append(aChar);
            continue;
         }
         switch (aChar)
         {
            case ' ' :
               if (x == 0 || escapeSpace)
                  outBuffer.append('\\');
               outBuffer.append(' ');
               break;
            case '\t' :
               outBuffer.append('\\');
               outBuffer.append('t');
               break;
            case '\n' :
               outBuffer.append('\\');
               outBuffer.append('n');
               break;
            case '\r' :
               outBuffer.append('\\');
               outBuffer.append('r');
               break;
            case '\f' :
               outBuffer.append('\\');
               outBuffer.append('f');
               break;
            case '=' : // Fall through
            case ':' : // Fall through
            case '#' : // Fall through
            case '!' :
               outBuffer.append('\\');
               outBuffer.append(aChar);
               break;
            default :
               if (((aChar < 0x0020) || (aChar > 0x007e)) & escapeUnicode)
               {
                  outBuffer.append('\\');
                  outBuffer.append('u');
                  outBuffer.append(toHex((aChar >> 12) & 0xF));
                  outBuffer.append(toHex((aChar >> 8) & 0xF));
                  outBuffer.append(toHex((aChar >> 4) & 0xF));
                  outBuffer.append(toHex(aChar & 0xF));
               }
               else
               {
                  outBuffer.append(aChar);
               }
         }
      }
      return outBuffer.toString();
   }

   /**
    * Convert a nibble to a hex character
    * 
    * @param nibble the nibble to convert.
    */
   private static char toHex(int nibble)
   {
      return hexDigit[(nibble & 0xF)];
   }

   /** A table of hex digits */
   private static final char[] hexDigit = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E',
      'F' };

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
      return toBoolean(map.get(key), defaultValue);
   }

   public static boolean toBoolean(String value, boolean defaultValue)
   {
      return value == null ? defaultValue : Boolean.valueOf(value).booleanValue();
   }

   public static void setBoolean(Map<String, String> map, String key, boolean value)
   {
      map.put(key, Boolean.toString(value));
   }

   public static int getInt(Map<String, String> map, String key, int defaultValue)
   {
      return toInt(map.get(key), defaultValue);
   }

   public static int toInt(String value, int defaultValue)
   {
      return value == null ? defaultValue : Integer.valueOf(value).intValue();
   }

   public static void setInt(Map<String, String> map, String key, int value)
   {
      map.put(key, Integer.toString(value));
   }
}
