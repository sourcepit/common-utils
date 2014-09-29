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

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.Map;

import org.sourcepit.common.utils.content.ContentType;
import org.sourcepit.common.utils.content.ContentTypes;
import org.sourcepit.common.utils.content.MimeType;
import org.sourcepit.common.utils.props.PropertiesSource;
import org.sourcepit.common.utils.props.PropertyConverter;
import org.sourcepit.common.utils.props.PropertyFilterFactory;

public class DefaultResourceFilter extends AbstractResourceFilter
{
   private final ContentTypes contentTypes;

   private final String targetEncoding;

   private final PropertiesSource properties;

   private final Map<String, PropertyConverter> propertyConverters;

   private final PropertyFilterFactory filterFactory;

   public DefaultResourceFilter(ContentTypes contentTypes, String targetEncoding, PropertiesSource properties,
      Map<String, PropertyConverter> propertyConverters, PropertyFilterFactory filterFactory)
   {
      this.contentTypes = contentTypes;
      this.targetEncoding = targetEncoding;
      this.properties = properties;
      this.filterFactory = filterFactory;
      this.propertyConverters = propertyConverters;
   }

   @Override
   protected ContentType getContentType(Resource resource, InputStream content) throws IOException
   {
      final String fileName = resource.getPath().getLastSegment();
      final ContentType contentType = contentTypes.detect(fileName, content, targetEncoding, properties);
      return contentType == null || contentType.getEncoding() == null ? null : contentType;
   }

   @Override
   protected PropertiesSource getProperties(ContentType contentType, Resource resource)
   {
      return properties;
   }

   @Override
   protected PropertyConverter getPropertyConverter(ContentType contentType, Resource resource)
   {
      MimeType mimeType = contentType.getMimeType();
      while (mimeType != null)
      {
         final PropertyConverter converter = propertyConverters.get(mimeType.getName());
         if (converter != null)
         {
            return converter;
         }
         mimeType = mimeType.getBaseType();
      }
      return null;
   }

   @Override
   protected Reader createFilterReader(Reader reader, PropertiesSource props, PropertyConverter propertyConverter)
   {
      return filterFactory.createReader(reader, props, propertyConverter);
   }

}
