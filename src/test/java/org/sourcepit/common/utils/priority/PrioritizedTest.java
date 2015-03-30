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

package org.sourcepit.common.utils.priority;

import static org.junit.Assert.*;
import static org.sourcepit.common.utils.priority.Priority.HIGH;
import static org.sourcepit.common.utils.priority.Priority.LOW;
import static org.sourcepit.common.utils.priority.Priority.MAXIMUM;
import static org.sourcepit.common.utils.priority.Priority.MINIMUM;
import static org.sourcepit.common.utils.priority.Priority.NORMAL;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.hamcrest.core.IsEqual;
import org.junit.Test;

/**
 * @author Bernd
 */
public class PrioritizedTest
{
   private static final class PriorizedImpl extends AbstractPriorized
   {
      private final Priority priority;

      public PriorizedImpl(Priority priority)
      {
         this.priority = priority;
      }

      public Priority getPriority()
      {
         return priority;
      }
      
      @Override
      public String toString()
      {
         return priority==null ? "null" : priority.toString();
      }
   }

   @Test
   public void testSort()
   {
      Priorized max = new PriorizedImpl(MAXIMUM);

      Priorized high = new PriorizedImpl(HIGH);

      Priorized normal = new PriorizedImpl(NORMAL);

      Priorized low = new PriorizedImpl(LOW);

      Priorized min = new PriorizedImpl(MINIMUM);

      Priorized nill = new PriorizedImpl(null);

      List<Priorized> ref = new ArrayList<Priorized>();
      ref.add(max);
      ref.add(high);
      ref.add(normal);
      ref.add(low);
      ref.add(min);
      ref.add(nill);

      List<Priorized> priorized = new ArrayList<Priorized>();
      priorized.add(nill);
      priorized.add(min);
      priorized.add(low);
      priorized.add(normal);
      priorized.add(high);
      priorized.add(max);

      Collections.sort(priorized);

      assertThat(priorized, IsEqual.equalTo(ref));
   }

   @Test
   public void testNull() throws Exception
   {
      Priorized normal = new PriorizedImpl(NORMAL);
      Priorized nill = new PriorizedImpl(null);
      
      
      assertTrue(normal.compareTo(nill) < 0);
      
      assertTrue(nill.compareTo(normal) > 0);
   }

}
