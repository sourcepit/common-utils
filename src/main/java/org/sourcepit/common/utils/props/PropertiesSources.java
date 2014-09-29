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

import java.util.Collection;
import java.util.Map;
import java.util.Properties;

public final class PropertiesSources
{
   private PropertiesSources()
   {
      super();
   }

   public static PropertiesSource emptyPropertiesSource()
   {
      return new AbstractPropertiesSource()
      {
         @Override
         public String get(String key)
         {
            return null;
         }
      };
   }

   public static SingletonPropertiesSource singletonPropertiesSource(String key, String value)
   {
      return SingletonPropertiesSource.singletonPropertiesSource(key, value);
   }

   public static PropertiesSource toPropertiesSource(Properties properties)
   {
      return JPropertiesSource.toPropertiesSource(properties);
   }

   public static PropertiesSource toPropertiesSource(@SuppressWarnings("rawtypes") Map map)
   {
      return MapPropertiesSource.toPropertiesSource(map);
   }

   public static PropertiesSource toPropertiesSource(@SuppressWarnings("rawtypes") Map map, boolean useToString)
   {
      return MapPropertiesSource.toPropertiesSource(map, useToString);
   }

   public static PropertiesSource chain(Collection<PropertiesSource> propertiesSources)
   {
      return new ChainedPropertiesSource(propertiesSources);
   }

   public static PropertiesSource chain(PropertiesSource... propertiesSources)
   {
      return new ChainedPropertiesSource(propertiesSources);
   }

}
