/**
 * Copyright (c) 2012 Sourcepit.org contributors and others. All rights reserved. This program and the accompanying
 * materials are made available under the terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.sourcepit.common.utils.collections;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class FilteredIterableTest
{
   @Test
   public void testNull()
   {
      List<Object> input = new ArrayList<Object>();
      input.add(new Object());
      input.add(new Object());
      input.add(new Object());

      List<Object> result = new ArrayList<Object>();
      for (Object element : new FilteredIterable<Object>(input, (Object) null))
      {
         result.add(element);
      }
      assertEquals(input, result);

      result = new ArrayList<Object>();
      for (Object element : new FilteredIterable<Object>(input, (Object[]) null))
      {
         result.add(element);
      }
      assertEquals(input, result);
   }

   @Test
   public void testEmptyFilter()
   {
      List<Object> input = new ArrayList<Object>();
      input.add(new Object());
      input.add(new Object());
      input.add(new Object());

      List<Object> result = new ArrayList<Object>();
      for (Object element : new FilteredIterable<Object>(input, new ArrayList<Object>()))
      {
         result.add(element);
      }
      assertEquals(input, result);
   }

   @Test
   public void testFilterSome()
   {
      List<Object> input = new ArrayList<Object>();
      input.add(new Object());
      input.add(new Object());
      input.add(new Object());
      input.add(new Object());

      List<Object> filter = new ArrayList<Object>();
      filter.add(input.get(1));
      filter.add(input.get(3));

      List<Object> result = new ArrayList<Object>();
      for (Object element : new FilteredIterable<Object>(input, filter))
      {
         result.add(element);
      }
      
      List<Object> expected = new ArrayList<Object>(input);
      expected.removeAll(filter);
      
      assertEquals(expected, result);
   }
   
   @Test
   public void testFilterAll()
   {
      List<Object> input = new ArrayList<Object>();
      input.add(new Object());
      input.add(new Object());
      input.add(new Object());
      input.add(new Object());

      List<Object> result = new ArrayList<Object>();
      for (Object element : new FilteredIterable<Object>(input, input))
      {
         result.add(element);
      }
      
      assertTrue(result.isEmpty());
   }

}
