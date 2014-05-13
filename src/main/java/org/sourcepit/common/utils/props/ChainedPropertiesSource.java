/**
 * Copyright (c) 2014 Sourcepit.org contributors and others. All rights reserved. This program and the accompanying
 * materials are made available under the terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.sourcepit.common.utils.props;

import java.util.Arrays;
import java.util.Collection;

public class ChainedPropertiesSource extends AbstractPropertiesSource implements PropertiesSource
{
   private final Collection<PropertiesSource> propertiesSources;

   public ChainedPropertiesSource(PropertiesSource... propertiesSources)
   {
      this(Arrays.asList(propertiesSources));
   }

   public ChainedPropertiesSource(Collection<PropertiesSource> propertiesSources)
   {
      this.propertiesSources = propertiesSources;
   }

   @Override
   public String get(String key)
   {
      for (PropertiesSource propertiesSource : propertiesSources)
      {
         final String value = propertiesSource.get(key);
         if (value != null)
         {
            return value;
         }
      }
      return null;
   }
}
