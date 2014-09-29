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
public abstract class AbstractPropertiesSource implements PropertiesSource
{
   public int getInt(String key, int defaultValue)
   {
      return PropertiesUtils.toInt(get(key), defaultValue);
   }

   public boolean getBoolean(String key, boolean defaultValue)
   {
      return PropertiesUtils.toBoolean(get(key), defaultValue);
   }

   public String get(String key, String defaultValue)
   {
      final String value = get(key);
      return value == null ? defaultValue : value;
   }
}
