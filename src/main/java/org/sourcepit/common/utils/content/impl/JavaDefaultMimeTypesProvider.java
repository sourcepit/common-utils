/**
 * Copyright (c) 2013 Sourcepit.org contributors and others. All rights reserved. This program and the accompanying
 * materials are made available under the terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.sourcepit.common.utils.content.impl;

import java.io.IOException;
import java.io.InputStream;
import java.net.URLConnection;

import org.sourcepit.common.utils.content.MimeTypesProvider;
import org.sourcepit.common.utils.props.PropertiesSource;

public class JavaDefaultMimeTypesProvider implements MimeTypesProvider
{
   @Override
   public String forFileName(String fileName, PropertiesSource options)
   {
      return URLConnection.guessContentTypeFromName(fileName);
   }

   @Override
   public String forContent(InputStream content, PropertiesSource options) throws IOException
   {
      return URLConnection.guessContentTypeFromStream(content);
   }

   @Override
   public String getBaseType(String mimeType, PropertiesSource options)
   {
      return null;
   }
}
