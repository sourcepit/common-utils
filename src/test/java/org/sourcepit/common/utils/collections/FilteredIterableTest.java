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
