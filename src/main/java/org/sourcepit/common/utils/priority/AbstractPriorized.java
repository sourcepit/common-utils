/**
 * Copyright (c) 2011 Sourcepit.org contributors and others. All rights reserved. This program and the accompanying
 * materials are made available under the terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.sourcepit.common.utils.priority;
/**
 * @author Bernd
 */
public abstract class AbstractPriorized implements Priorized
{
   public int compareTo(Priorized prioritized)
   {
      final Priority prio1 = getPriority();
      final Priority prio2 = prioritized.getPriority();
      if (prio1 == null)
      {
         return prio2 == null ? 0 : prio2.ordinal() + 1;
      }
      return prio1.compareTo(prio2);
   }
}
