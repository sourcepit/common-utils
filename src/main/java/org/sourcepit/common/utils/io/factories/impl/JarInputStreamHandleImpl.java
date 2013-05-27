/**
 * Copyright (c) 2012 Sourcepit.org contributors and others. All rights reserved. This program and the accompanying
 * materials are made available under the terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.sourcepit.common.utils.io.factories.impl;

import java.io.IOException;
import java.io.InputStream;
import java.util.jar.JarInputStream;

import org.sourcepit.common.utils.io.IOHandle;
import org.sourcepit.common.utils.io.factories.JarInputStreamHandle;


/**
 * @author Bernd Vogt <bernd.vogt@sourcepit.org>
 */
public class JarInputStreamHandleImpl implements JarInputStreamHandle
{
   private final IOHandle<? extends InputStream> resource;

   public JarInputStreamHandleImpl(IOHandle<? extends InputStream> resource)
   {
      this.resource = resource;
   }

   public JarInputStream open() throws IOException
   {
      return new JarInputStream(resource.open());
   }
}
