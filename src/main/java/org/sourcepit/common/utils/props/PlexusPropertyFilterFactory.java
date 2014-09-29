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

package org.sourcepit.common.utils.props;

import java.io.Reader;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;

import org.codehaus.plexus.interpolation.AbstractValueSource;
import org.codehaus.plexus.interpolation.InterpolationPostProcessor;
import org.codehaus.plexus.interpolation.SimpleRecursionInterceptor;
import org.codehaus.plexus.interpolation.multi.MultiDelimiterInterpolatorFilterReader;
import org.codehaus.plexus.interpolation.multi.MultiDelimiterStringSearchInterpolator;

public class PlexusPropertyFilterFactory implements PropertyFilterFactory
{
   private List<String> delimiters;
   private final String escapeString;

   public PlexusPropertyFilterFactory()
   {
      this(Arrays.asList("${*}", "@"), null);
   }

   public PlexusPropertyFilterFactory(String escapeString)
   {
      this(Arrays.asList("${*}", "@"), escapeString);
   }

   public PlexusPropertyFilterFactory(List<String> delimiters, String escapeString)
   {
      this.delimiters = delimiters;
      this.escapeString = escapeString;
   }

   @Override
   public Reader createReader(Reader reader, PropertiesSource properties, PropertyConverter propertyConverter)
   {
      final MultiDelimiterStringSearchInterpolator interpolator = new MultiDelimiterStringSearchInterpolator();
      interpolator.setDelimiterSpecs(new LinkedHashSet<String>(delimiters));
      interpolator.setEscapeString(escapeString);
      interpolator.addValueSource(new PropertiesValueSource(properties));
      if (propertyConverter != null)
      {
         interpolator.addPostProcessor(new PropertyConvertingPostProcessor(propertyConverter));
      }
      MultiDelimiterInterpolatorFilterReader filterReader = new MultiDelimiterInterpolatorFilterReader(reader,
         interpolator, new SimpleRecursionInterceptor());
      filterReader.setInterpolateWithPrefixPattern(false);
      return filterReader;
   }

   class PropertiesValueSource extends AbstractValueSource
   {
      private final PropertiesSource properties;

      PropertiesValueSource(PropertiesSource properties)
      {
         super(false);
         this.properties = properties;
      }

      @Override
      public Object getValue(String expression)
      {
         return properties.get(expression);
      }
   }

   class PropertyConvertingPostProcessor implements InterpolationPostProcessor
   {
      private final PropertyConverter propertyConverter;

      public PropertyConvertingPostProcessor(PropertyConverter propertyConverter)
      {
         this.propertyConverter = propertyConverter;
      }

      @Override
      public Object execute(String expression, Object value)
      {
         return propertyConverter.convert(expression, value == null ? null : value.toString());
      }
   }
}
