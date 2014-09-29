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

package org.sourcepit.common.utils.nls;

import java.io.File;
import java.io.FileFilter;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.sourcepit.common.utils.props.PropertiesUtils;

public final class NlsUtils
{
   public static final Locale DEFAULT_LOCALE = new Locale("");

   private NlsUtils()
   {
      super();
   }

   public static Map<Locale, File> getNlsPropertyFiles(File directory, final String fileName, String fileExtension)
   {
      final String dotFileExt;
      if (fileExtension != null && !fileExtension.startsWith("."))
      {
         dotFileExt = "." + fileExtension;
      }
      else
      {
         dotFileExt = fileExtension;
      }

      final Map<Locale, File> fileToLocaleMap = new LinkedHashMap<Locale, File>();

      final File defaultProperties = new File(directory, fileName + dotFileExt);
      if (defaultProperties.isFile() && defaultProperties.canRead())
      {
         fileToLocaleMap.put(DEFAULT_LOCALE, defaultProperties);
      }

      directory.listFiles(new FileFilter()
      {
         public boolean accept(File file)
         {
            if (file.isFile())
            {
               final Locale locale = extractLocale(file.getName(), fileName, dotFileExt);
               if (locale != null)
               {
                  fileToLocaleMap.put(locale, file);
                  // fileToLocaleMap.put(file, locale);
               }
            }
            return false;
         }
      });
      return fileToLocaleMap;
   }

   public static Locale extractLocale(String string, String prefix, String suffix)
   {
      if (string == null)
      {
         throw new IllegalArgumentException("string must not be null");
      }
      StringBuilder sb = new StringBuilder();
      if (prefix != null)
      {
         sb.append(Pattern.quote(prefix));
         sb.append("(_");
      }
      else
      {
         sb.append("(");
      }
      sb.append("[a-zA-Z]{1,2}){1}(_[A-Z]{1,2})?(_[A-Z]{1,2})?");
      if (suffix != null)
      {
         sb.append(Pattern.quote(suffix));
      }
      Pattern pattern = Pattern.compile(sb.toString());
      Matcher matcher = pattern.matcher(string);
      boolean res = matcher.matches();
      if (res)
      {
         String[] segments = new String[3];
         for (int i = 0; i < segments.length; i++)
         {
            String group = matcher.group(i + 1);
            segments[i] = group == null ? "" : group.startsWith("_") ? group.substring(1) : group;
         }
         return new Locale(segments[0], segments[1], segments[2]);
      }
      return null;
   }


   public static Set<Locale> injectNlsProperties(Properties target, final File directory, final String fileName,
      final String fileExtension)
   {
      final Map<Locale, File> localeToFileMap = NlsUtils.getNlsPropertyFiles(directory, fileName, fileExtension);

      final Set<Locale> detectedLocales = new LinkedHashSet<Locale>(localeToFileMap.keySet());

      // load default properties
      final Map<String, String> defaultProperties = new LinkedHashMap<String, String>();
      final File defaultPropertiesFile = localeToFileMap.remove(NlsUtils.DEFAULT_LOCALE);
      if (defaultPropertiesFile != null)
      {
         PropertiesUtils.load(defaultPropertiesFile, defaultProperties);
      }

      // inject default properties
      target.putAll(defaultProperties);

      // prepare and inject nls properties
      for (Entry<Locale, File> entry : localeToFileMap.entrySet())
      {
         final Map<String, String> nlsProperties = loadNlsProperties(defaultProperties, entry.getValue(),
            entry.getKey());
         target.putAll(nlsProperties);
      }

      return detectedLocales;
   }

   private static Map<String, String> loadNlsProperties(Map<String, String> defaultProperties, File propertiesFile,
      Locale locale)
   {
      final Map<String, String> nlsProperties = new LinkedHashMap<String, String>();

      final Map<String, String> rawProperties = new LinkedHashMap<String, String>(defaultProperties);
      PropertiesUtils.load(propertiesFile, rawProperties);

      final String nlsPrefix = "nls_" + locale.toString() + ".";
      for (Entry<String, String> entry : rawProperties.entrySet())
      {
         nlsProperties.put(nlsPrefix + entry.getKey(),
            convertPropertyVariables(rawProperties.keySet(), nlsPrefix, entry.getValue()));
      }

      return nlsProperties;
   }

   private static String convertPropertyVariables(Set<String> propertyKeys, String nlsPrefix, String value)
   {
      for (Object object : propertyKeys)
      {
         value = value.replace("${" + object + "}", "${" + nlsPrefix + object + "}");
      }
      return value;
   }
}
