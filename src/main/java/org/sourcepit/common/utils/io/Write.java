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
