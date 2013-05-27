/**
 * Copyright (c) 2012 Sourcepit.org contributors and others. All rights reserved. This program and the accompanying
 * materials are made available under the terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.sourcepit.common.utils.io.handles.impl;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.sourcepit.common.utils.io.handles.ByteArrayOutputStreamHandle;


public class ByteArrayOutputStreamHandleImpl implements ByteArrayOutputStreamHandle
{
   private final int size;
   
   public ByteArrayOutputStreamHandleImpl()
   {
      this(32);
   }

   public ByteArrayOutputStreamHandleImpl(int size)
   {
      this.size = size;
   }

   public ByteArrayOutputStream open() throws IOException
   {
      return new ByteArrayOutputStream(size);
   }

}
