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
      if (prio2 == null)
      {
         return prio1 == null ? 0 : -1 * (prio1.ordinal() + 1);
      }
      return prio1.compareTo(prio2);
   }
}
