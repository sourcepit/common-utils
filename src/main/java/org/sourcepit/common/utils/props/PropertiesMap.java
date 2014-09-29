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
import java.util.Map;
import java.util.Properties;

public interface PropertiesMap extends Map<String, String>, PropertiesSource
{
   Map<String, String> getDefaultProperties();

   void setBoolean(String key, boolean value);

   void setInt(String key, int value);

   Properties toJavaProperties();

   <K extends Object, V extends Object> void putMap(Map<K, V> map);

   /**
    * @throws IllegalArgumentException if the input stream contains a malformed Unicode escape sequence.
    * @throws IllegalStateException if an error occurred when reading from the input stream.
    */
   void load(InputStream inputStream);

   void load(ClassLoader classLoader, String resourcePath);

   /**
    * @throws IllegalArgumentException if the input stream contains a malformed Unicode escape sequence, or the file
    *            cannot be foud
    * @throws IllegalStateException if an error occurred when reading from the input stream.
    */
   void load(File file);

   void store(File file);

   void store(OutputStream outputStream);
}
