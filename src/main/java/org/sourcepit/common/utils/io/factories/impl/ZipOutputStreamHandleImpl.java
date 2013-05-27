/**
 * Copyright (c) 2012 Sourcepit.org contributors and others. All rights reserved. This program and the accompanying
 * materials are made available under the terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.sourcepit.common.utils.io.factories.impl;

import java.io.IOException;
import java.io.OutputStream;
import java.util.zip.ZipOutputStream;

import org.sourcepit.common.utils.io.IOHandle;
import org.sourcepit.common.utils.io.factories.ZipOutputStreamHandle;


/**
 * @author Bernd Vogt <bernd.vogt@sourcepit.org>
 */
public class ZipOutputStreamHandleImpl implements ZipOutputStreamHandle
{
   private final IOHandle<? extends OutputStream> resource;

   public ZipOutputStreamHandleImpl(IOHandle<? extends OutputStream> resource)
   {
      this.resource = resource;
   }

   public ZipOutputStream open() throws IOException
   {
      return new ZipOutputStream(resource.open());
   }
}
