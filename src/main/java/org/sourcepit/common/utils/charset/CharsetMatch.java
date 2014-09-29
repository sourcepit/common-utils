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

package org.sourcepit.common.utils.charset;

import java.nio.charset.Charset;


public final class CharsetMatch implements Comparable<CharsetMatch>
{
   private final String name;

   private final int confidence;

   private Charset charset;

   public CharsetMatch(String name, int confidence)
   {
      this.name = name;
      this.confidence = confidence;
   }

   public String getName()
   {
      return name;
   }

   public Charset getCharset()
   {
      if (charset == null)
      {
         charset = Charset.forName(name);
      }
      return charset;
   }

   public int getConfidence()
   {
      return confidence;
   }

   public int compareTo(CharsetMatch o)
   {
      if (confidence > o.confidence)
      {
         return 1;
      }
      if (confidence < o.confidence)
      {
         return -1;
      }
      return 0;
   }
}