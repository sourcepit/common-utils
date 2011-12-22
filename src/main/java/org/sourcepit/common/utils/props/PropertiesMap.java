/**
 * Copyright (c) 2011 Sourcepit.org contributors and others. All rights reserved. This program and the accompanying
 * materials are made available under the terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.sourcepit.common.utils.props;

import java.io.File;
import java.io.InputStream;
import java.util.Map;
import java.util.Properties;

public interface PropertiesMap extends Map<String, String>
{
   Map<String, String> getDefaultProperties();

   String get(String key, String defaultValue);

   void setBoolean(String key, boolean value);

   boolean getBoolean(String key, boolean defaultValue);

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
}
