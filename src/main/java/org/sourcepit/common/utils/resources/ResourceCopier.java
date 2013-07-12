/**
 * Copyright (c) 2013 Sourcepit.org contributors and others. All rights reserved. This program and the accompanying
 * materials are made available under the terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
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
