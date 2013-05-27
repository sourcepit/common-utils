/**
 * Copyright (c) 2012 Sourcepit.org contributors and others. All rights reserved. This program and the accompanying
 * materials are made available under the terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.sourcepit.common.utils.io.factories.impl;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import org.sourcepit.common.utils.io.factories.InputStreamHandle;


public class URLInputStreamHandleImpl implements InputStreamHandle
{
   private final URL url;
   
   public URLInputStreamHandleImpl(URL url)
   {
      this.url = url;
   }

   public InputStream open() throws IOException
   {
      return url.openStream();
   }
}
