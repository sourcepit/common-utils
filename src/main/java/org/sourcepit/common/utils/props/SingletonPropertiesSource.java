/**
 * Copyright (c) 2014 Sourcepit.org contributors and others. All rights reserved. This program and the accompanying
 * materials are made available under the terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.sourcepit.common.utils.props;
public class SingletonPropertiesSource extends AbstractPropertiesSource implements PropertiesSource
{
   private final String key, value;

   public static SingletonPropertiesSource singletonPropertiesSource(String key, String value)
   {
      return new SingletonPropertiesSource(key, value);
   }

   public SingletonPropertiesSource(String key, String value)
   {
      this.key = key;
      this.value = value;
   }

   @Override
   public String get(String key)
   {
      return this.key.equals(key) ? value : null;
   }
}
