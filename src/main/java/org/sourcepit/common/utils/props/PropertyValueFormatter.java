
package org.sourcepit.common.utils.props;

import javax.inject.Named;

/**
 * Copyright (c) 2011 Sourcepit.org contributors and others. All rights reserved. This program and the accompanying
 * materials are made available under the terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

@Named("application/x-java-properties")
public class PropertyValueFormatter implements PropertyConverter
{
   @Override
   public String convert(String name, String value)
   {
      return value == null ? null : PropertiesUtils.escapeJavaProperties(value);
   }
}
