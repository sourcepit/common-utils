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

import java.util.Map;

public class MapPropertiesSource extends AbstractPropertiesSource implements PropertiesSource
{
   @SuppressWarnings("rawtypes")
   private final Map map;

   private final boolean useToString;

   public static PropertiesSource toPropertiesSource(@SuppressWarnings("rawtypes") Map map)
   {
      return new MapPropertiesSource(map);
   }

   public static PropertiesSource toPropertiesSource(@SuppressWarnings("rawtypes") Map map, boolean useToString)
   {
      return new MapPropertiesSource(map, useToString);
   }

   public MapPropertiesSource(@SuppressWarnings("rawtypes") Map map)
   {
      this(map, false);
   }

   public MapPropertiesSource(@SuppressWarnings("rawtypes") Map map, boolean useToString)
   {
      this.map = map;
      this.useToString = useToString;
   }

   @Override
   public String get(String key)
   {
      final Object value = map.get(key);
      return value instanceof String ? value.toString() : (useToString && value != null) ? value.toString() : null;
   }

}
