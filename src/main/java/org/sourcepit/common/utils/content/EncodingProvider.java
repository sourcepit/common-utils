/**
 * Copyright (c) 2013 Sourcepit.org contributors and others. All rights reserved. This program and the accompanying
 * materials are made available under the terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.sourcepit.common.utils.content;

import java.io.IOException;
import java.io.InputStream;

import org.sourcepit.common.utils.props.PropertiesSource;


public interface EncodingProvider
{
   String getDefaultEncoding(MimeType mimeType, String fileName, String targetEncoding, PropertiesSource options);

   String getDeclaredEncoding(MimeType mimeType, String fileName, PropertiesSource options);

   String getDeclaredEncoding(MimeType mimeType, InputStream content, PropertiesSource options) throws IOException;
}
