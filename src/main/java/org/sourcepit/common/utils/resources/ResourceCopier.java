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

package org.sourcepit.common.utils.resources;

import static org.apache.commons.io.IOUtils.closeQuietly;

import java.io.IOException;
import java.io.InputStream;

public class ResourceCopier
{
   public void copy(ResourceTraverser src, final ResourceStorage dest) throws IOException
   {
      final ResourceVisitor visitor = new ResourceVisitor()
      {
         @Override
         public void visit(Resource resource, InputStream content) throws IOException
         {
            if (doCopy(resource))
            {
               copy(resource, content, dest);
            }
         }
      };

      dest.open();
      try
      {
         src.accept(visitor);
      }
      finally
      {
         closeQuietly(dest);
      }
   }

   protected boolean doCopy(Resource resource)
   {
      return true;
   }

   protected void copy(Resource resource, InputStream content, ResourceStorage dest) throws IOException
   {
      dest.put(resource, content);
   }
}
