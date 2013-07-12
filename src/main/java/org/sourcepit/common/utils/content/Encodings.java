/**
 * Copyright (c) 2013 Sourcepit.org contributors and others. All rights reserved. This program and the accompanying
 * materials are made available under the terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.sourcepit.common.utils.content;

import java.io.IOException;
import java.io.InputStream;

import org.sourcepit.common.utils.content.impl.EncodingsImpl;
import org.sourcepit.common.utils.props.PropertiesSource;

public interface Encodings
{
   Encodings DEFAULT = EncodingsImpl.getDefault();

   String detect(MimeType mimeType, String fileName, InputStream content, String targetEncoding,
      PropertiesSource options) throws IOException;
}