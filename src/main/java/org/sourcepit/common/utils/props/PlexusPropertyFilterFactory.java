/**
 * Copyright (c) 2013 Sourcepit.org contributors and others. All rights reserved. This program and the accompanying
 * materials are made available under the terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
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
