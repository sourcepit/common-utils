/*
 * Copyright 2014 Bernd Vogt and others.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.sourcepit.common.utils.io.handles.impl;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import org.sourcepit.common.utils.io.handles.ByteArrayInputStreamHandle;


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
