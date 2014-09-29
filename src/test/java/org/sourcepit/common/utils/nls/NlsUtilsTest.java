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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;

import org.junit.Rule;
import org.junit.Test;
import org.sourcepit.common.testing.Environment;
import org.sourcepit.common.testing.Workspace;

public class NlsUtilsTest
{
   @Test
   public void testExtractLocale() throws Exception
   {
      try
      {
         NlsUtils.extractLocale(null, null, null);
         fail();
      }
      catch (IllegalArgumentException e)
      {
      }

      Locale locale;

      locale = NlsUtils.extractLocale("module.properties", "module", ".properties");
      assertNull(locale);

      locale = NlsUtils.extractLocale("de", null, null);
      assertEquals(new Locale("de"), locale);

      locale = NlsUtils.extractLocale("de_DE", null, null);
      assertEquals(new Locale("de", "DE"), locale);

      locale = NlsUtils.extractLocale("th_TH_TH", null, null);
      assertEquals(new Locale("th", "TH", "TH"), locale);

      locale = NlsUtils.extractLocale("hans_th_TH_TH.properties", "hans", ".properties");
      assertEquals(new Locale("th", "TH", "TH"), locale);

      // test hidden regex
      locale = NlsUtils.extractLocale("ha([a-z]+)ns_de.properties", "ha([a-z]+)ns", ".properties");
      assertEquals(new Locale("de"), locale);
   }

   private Environment env = Environment.get("env-test.properties");

   @Rule
   public Workspace ws = new Workspace(env.getBuildDir(), "test-ws", false);

   @Test
   public void testGetNlsPropertyFiles() throws Exception
   {
      File moduleDir = ws.importFileOrDir(new File(env.getResourcesDir(), "NlsUtilsTest"));

      final Map<Locale, File> fileToLocaleMap = NlsUtils.getNlsPropertyFiles(moduleDir, "module", "properties");
      assertNotNull(fileToLocaleMap);
      assertEquals(2, fileToLocaleMap.size());

      File defaultProperties = fileToLocaleMap.remove(NlsUtils.DEFAULT_LOCALE);
      assertNotNull(defaultProperties);
      assertEquals("module.properties", defaultProperties.getName());

      File deProperties = fileToLocaleMap.remove(new Locale("de"));
      assertNotNull(deProperties);
      assertEquals("module_de.properties", deProperties.getName());

      assertTrue(fileToLocaleMap.isEmpty());
   }

   @Test
   public void testInjectNlsProperties() throws Exception
   {
      File moduleDir = ws.importFileOrDir(new File(env.getResourcesDir(), "NlsUtilsTest"));

      Properties result = new Properties();
      NlsUtils.injectNlsProperties(result, moduleDir, "module", "properties");

      assertEquals(6, result.size());
      assertEquals("Hello", result.getProperty("name"));
      assertEquals("Hallo", result.getProperty("nls_de.name"));
      assertEquals("This is text belongs to ${name}", result.getProperty("description"));
      assertEquals("Dieser Text gehört zu ${nls_de.name}", result.getProperty("nls_de.description"));
      assertEquals("This key is missing in the de properties file", result.getProperty("defaultOnly"));
      assertEquals("This key is missing in the de properties file", result.getProperty("nls_de.defaultOnly"));
   }
}
