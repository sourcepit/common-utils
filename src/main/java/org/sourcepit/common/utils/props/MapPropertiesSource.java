/**
 * Copyright (c) 2014 Sourcepit.org contributors and others. All rights reserved. This program and the accompanying
 * materials are made available under the terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
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
