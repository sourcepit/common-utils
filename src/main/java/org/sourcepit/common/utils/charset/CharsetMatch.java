/**
 * Copyright (c) 2012 Sourcepit.org contributors and others. All rights reserved. This program and the accompanying
 * materials are made available under the terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
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