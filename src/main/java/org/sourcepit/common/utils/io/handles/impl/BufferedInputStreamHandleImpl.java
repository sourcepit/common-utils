/**
 * Copyright (c) 2012 Sourcepit.org contributors and others. All rights reserved. This program and the accompanying
 * materials are made available under the terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.sourcepit.common.utils.io.handles.impl;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.sourcepit.common.utils.io.IOHandle;
import org.sourcepit.common.utils.io.handles.InputStreamHandle;


/**
 * @author Bernd Vogt <bernd.vogt@sourcepit.org>
 */
public class BufferedInputStreamHandleImpl implements InputStreamHandle
{
   private final IOHandle<? extends InputStream> resource;

   public BufferedInputStreamHandleImpl(IOHandle<? extends InputStream> resource)
   {
      this.resource = resource;
   }

   public BufferedInputStream open() throws IOException
   {
      return new BufferedInputStream(resource.open());
   }

}
