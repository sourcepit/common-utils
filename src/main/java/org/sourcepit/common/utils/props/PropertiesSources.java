/**
 * Copyright (c) 2014 Sourcepit.org contributors and others. All rights reserved. This program and the accompanying
 * materials are made available under the terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
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
