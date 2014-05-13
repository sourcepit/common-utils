/**
 * Copyright (c) 2014 Sourcepit.org contributors and others. All rights reserved. This program and the accompanying
 * materials are made available under the terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
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
