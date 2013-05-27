/**
 * Copyright (c) 2012 Sourcepit.org contributors and others. All rights reserved. This program and the accompanying
 * materials are made available under the terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.sourcepit.common.utils.io.factories.impl;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import org.sourcepit.common.utils.io.factories.ByteArrayInputStreamHandle;


public class ByteArrayInputStreamHandleImpl implements ByteArrayInputStreamHandle
{
   private final byte[] bytes;

   public ByteArrayInputStreamHandleImpl(byte[] bytes)
   {
      this.bytes = bytes;
   }

   public ByteArrayInputStream open() throws IOException
   {
      return new ByteArrayInputStream(bytes);
   }

}
