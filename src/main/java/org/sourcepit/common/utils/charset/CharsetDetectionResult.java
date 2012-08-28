/**
 * Copyright (c) 2012 Sourcepit.org contributors and others. All rights reserved. This program and the accompanying
 * materials are made available under the terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.sourcepit.common.utils.charset;

import java.nio.charset.Charset;
import java.util.Collections;
import java.util.List;

import org.sourcepit.common.utils.lang.ThrowablePipe;

public final class CharsetDetectionResult
{
   private final List<CharsetMatch> recognitionResults;

   private final Charset declaredCharset;

   private final ThrowablePipe errors;

   private Charset recommendedCharset;

   CharsetDetectionResult(List<CharsetMatch> recognitionResults, Charset declaredCharset, ThrowablePipe errors)
   {
      this.recognitionResults = Collections.unmodifiableList(recognitionResults);
      this.declaredCharset = declaredCharset;
      this.errors = errors;
   }

   public List<CharsetMatch> getCharsetMatches()
   {
      return recognitionResults;
   }

   public Charset getRecommendedCharset()
   {
      if (recommendedCharset == null)
      {
         recommendedCharset = determineRecommendedCharset();
      }
      return recommendedCharset;
   }

   private Charset determineRecommendedCharset()
   {
      boolean declaredUnderImprecise = false;

      final List<CharsetMatch> matches = getCharsetMatches();
      if (declaredCharset != null)
      {
         for (CharsetMatch match : matches)
         {
            if (declaredCharset.equals(match.getCharset()))
            {
               if (match.getConfidence() == 100)
               {
                  return declaredCharset;
               }
               declaredUnderImprecise = true;
            }
         }
      }

      for (CharsetMatch match : matches)
      {
         if (match.getConfidence() == 100)
         {
            return match.getCharset();
         }
      }

      if (declaredUnderImprecise)
      {
         return declaredCharset;
      }

      final Charset detected = getDetectedCharset();
      return detected == null ? declaredCharset : detected;
   }

   public Charset getDetectedCharset()
   {
      return getCharsetMatches().isEmpty() ? null : getCharsetMatches().get(0).getCharset();
   }

   public Charset getDeclaredCharset()
   {
      return declaredCharset;
   }

   public ThrowablePipe getErrors()
   {
      return errors;
   }
}
