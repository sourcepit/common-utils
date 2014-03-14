/**
 * Copyright (c) 2013 Sourcepit.org contributors and others. All rights reserved. This program and the accompanying
 * materials are made available under the terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.sourcepit.common.utils.content;

import java.io.IOException;
import java.io.InputStream;

import org.sourcepit.common.utils.props.PropertiesSource;

public interface MimeTypesProvider
{
   String forFileName(String fileName, PropertiesSource options);

   String forContent(InputStream content, PropertiesSource options) throws IOException;

   String getBaseType(String mimeType, PropertiesSource options);
}