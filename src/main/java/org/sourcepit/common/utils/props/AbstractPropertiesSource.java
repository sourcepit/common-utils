/**
 * Copyright (c) 2012 Sourcepit.org contributors and others. All rights reserved. This program and the accompanying
 * materials are made available under the terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
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
