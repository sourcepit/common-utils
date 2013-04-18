/**
 * Copyright (c) 2013 Sourcepit.org contributors and others. All rights reserved. This program and the accompanying
 * materials are made available under the terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.sourcepit.common.utils.io;

import java.io.Closeable;
import java.io.OutputStream;
import java.io.Writer;

public interface Write<Resource extends Closeable, Content>
{
   void write(Resource openResource, Content content) throws Exception;
   
   public interface ToWriter<Content> extends Write<Writer, Content>
   {
      @Override
      public void write(Writer writer, Content content) throws Exception;
   }

   public interface ToStream<Content> extends Write<OutputStream, Content>
   {
      @Override
      public void write(OutputStream writer, Content content) throws Exception;
   }
}
