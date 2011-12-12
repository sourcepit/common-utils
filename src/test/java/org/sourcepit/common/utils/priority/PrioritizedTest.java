/**
 * Copyright (c) 2011 Sourcepit.org contributors and others. All rights reserved. This program and the accompanying
 * materials are made available under the terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.sourcepit.common.utils.priority;

import static org.junit.Assert.assertThat;
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

}
