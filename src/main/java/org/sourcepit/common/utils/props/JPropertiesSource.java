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

import java.util.Properties;

public class JPropertiesSource extends AbstractPropertiesSource implements PropertiesSource
{
   private final Properties properties;

   public static PropertiesSource toPropertiesSource(Properties properties)
   {
      return new JPropertiesSource(properties);
   }

   public JPropertiesSource(Properties properties)
   {
      this.properties = properties;
   }

   @Override
   public String get(String key)
   {
      return properties.getProperty(key);
   }

}
