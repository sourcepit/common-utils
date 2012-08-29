/**
 * Copyright (c) 2012 Sourcepit.org contributors and others. All rights reserved. This program and the accompanying
 * materials are made available under the terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.sourcepit.common.utils.props;


public interface PropertiesSource
{
   String get(String key);
   
   String get(String key, String defaultValue);

   boolean getBoolean(String key, boolean defaultValue);

   int getInt(String key, int defaultValue);

}