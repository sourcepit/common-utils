/**
 * Copyright (c) 2013 Sourcepit.org contributors and others. All rights reserved. This program and the accompanying
 * materials are made available under the terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.sourcepit.common.utils.io;

import java.io.Closeable;
import java.io.InputStream;
import java.io.Reader;

public interface Read<Resource extends Closeable, Content>
{
   Content read(Resource openResource) throws Exception;

   public interface FromReader<Content> extends Read<java.io.Reader, Content>
   {
      @Override
      public Content read(Reader reader) throws Exception;
   }

   public interface FromStream<Content> extends Read<InputStream, Content>
   {
      @Override
      public Content read(InputStream inputStream) throws Exception;
   }
}
