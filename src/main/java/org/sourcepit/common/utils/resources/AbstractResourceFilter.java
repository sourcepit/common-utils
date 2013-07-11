/**
 * Copyright (c) 2013 Sourcepit.org contributors and others. All rights reserved. This program and the accompanying
 * materials are made available under the terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.sourcepit.common.utils.resources;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;

import org.sourcepit.common.utils.content.ContentType;
import org.sourcepit.common.utils.props.PropertiesSource;
import org.sourcepit.common.utils.props.PropertyConverter;

public abstract class AbstractResourceFilter
{
   private final ResourceCopier copier;

   public AbstractResourceFilter()
   {
      copier = new ResourceCopier()
      {
         @Override
         protected boolean doCopy(Resource resource)
         {
            return AbstractResourceFilter.this.doCopy(resource);
         }

         @Override
         protected void copy(Resource resource, InputStream content, ResourceStorage dest) throws IOException
         {
            if (doFilter(resource))
            {
               if (!content.markSupported())
               {
                  content = new BufferedInputStream(content);
               }
               filter(resource, content, dest);
            }
            else
            {
               super.copy(resource, content, dest);
            }
         }
      };
   }

   public void copyAndFilter(ResourceTraverser src, final ResourceStorage dest) throws IOException
   {
      copier.copy(src, dest);
   }

   protected boolean doCopy(Resource resource)
   {
      return true;
   }

   protected boolean doFilter(Resource resource)
   {
      return !resource.isDirectory();
   }

   protected void filter(Resource resource, InputStream content, ResourceStorage dest) throws IOException
   {
      final ContentType contentType = getContentType(resource, content);
      if (contentType != null)
      {
         filter(contentType, resource, content, dest);
      }
      else
      {
         copier.copy(resource, content, dest);
      }
   }

   protected void filter(final ContentType contentType, Resource resource, InputStream content, ResourceStorage dest)
      throws IOException
   {
      final Charset charset = Charset.forName(contentType.getEncoding());
      final PropertiesSource properties = getProperties(contentType, resource);
      final PropertyConverter converter = getPropertyConverter(contentType, resource);
      final Reader filter = createFilterReader(new InputStreamReader(content, charset), properties, converter);
      dest.put(resource, filter, charset);
   }

   protected abstract ContentType getContentType(Resource resource, InputStream content) throws IOException;

   protected abstract PropertiesSource getProperties(ContentType contentType, Resource resource);

   protected abstract PropertyConverter getPropertyConverter(ContentType contentType, Resource resource);

   protected abstract Reader createFilterReader(Reader reader, PropertiesSource properties, PropertyConverter converter);
}
