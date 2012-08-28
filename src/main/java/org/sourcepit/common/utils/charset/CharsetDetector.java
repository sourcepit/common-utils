/**
 * Copyright (c) 2012 Sourcepit.org contributors and others. All rights reserved. This program and the accompanying
 * materials are made available under the terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.sourcepit.common.utils.charset;

import java.io.BufferedInputStream;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.IllegalCharsetNameException;
import java.nio.charset.UnsupportedCharsetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;

import org.sourcepit.common.utils.lang.CopyOnWriteThrowablePipe;
import org.sourcepit.common.utils.lang.ThrowablePipe;
import org.sourcepit.common.utils.path.Path;

@Named
public class CharsetDetector
{
   private final Collection<CharsetRecognizer> csRecognizers;

   @Inject
   public CharsetDetector(List<CharsetRecognizer> csRecognizers)
   {
      this.csRecognizers = csRecognizers;
   }

   public CharsetDetectionResult detect(String fileName, InputStream inputStream, String declaredCharset)
   {
      final Charset dcs = declaredCharset == null ? null : Charset.forName(declaredCharset);
      final String fileExtension = fileName == null ? null : new Path(fileName).getFileExtension();
      return detect(fileName, fileExtension, inputStream, dcs);
   }

   private CharsetDetectionResult detect(String fileName, String fileExtension, InputStream inputStream,
      final Charset declaredCharset)
   {
      final ThrowablePipe errors = new CopyOnWriteThrowablePipe();

      final Map<Charset, CharsetMatch> matches = new LinkedHashMap<Charset, CharsetMatch>();
      recognize(matches, fileName, fileExtension, inputStream, declaredCharset, errors);

      final List<CharsetMatch> result = new ArrayList<CharsetMatch>(matches.values());
      Collections.sort(result);
      Collections.reverse(result);
      return new CharsetDetectionResult(result, declaredCharset, errors);
   }

   private void recognize(Map<Charset, CharsetMatch> recognitionResults, String fileName, String fileExtension,
      InputStream inputStream, Charset declaredCharset, ThrowablePipe errors)
   {
      final BufferedInputStream buffer = inputStream == null ? null : buffer(unclosable(inputStream));
      for (CharsetRecognizer csRecognizer : csRecognizers)
      {
         try
         {
            final Collection<CharsetMatch> matches = csRecognizer.recognize(fileName, fileExtension, buffer,
               declaredCharset);
            if (matches != null)
            {
               addrecognitionResults(recognitionResults, matches, errors);
            }
         }
         catch (IOException e)
         {
            errors.add(e);
         }
         reset(buffer);
      }
   }

   private static void reset(final BufferedInputStream buffer)
   {
      try
      {
         if (buffer != null)
         {
            buffer.reset();
         }
      }
      catch (IOException e)
      {
         throw new IllegalStateException(e);
      }
   }

   private void addrecognitionResults(Map<Charset, CharsetMatch> recognitionResults,
      final Collection<CharsetMatch> charsetMatches, ThrowablePipe errors)
   {
      for (CharsetMatch charsetMatch : charsetMatches)
      {
         try
         {
            final Charset charset = Charset.forName(charsetMatch.getName());

            final CharsetMatch existingMatch = recognitionResults.get(charset);
            if (existingMatch == null || existingMatch.compareTo(charsetMatch) < 0)
            {
               recognitionResults.put(charset, charsetMatch);
            }
         }
         catch (IllegalCharsetNameException e)
         {
            errors.add(e);
         }
         catch (UnsupportedCharsetException e)
         {
            errors.add(e);
         }
      }
   }

   private static BufferedInputStream buffer(InputStream inputStream)
   {
      final int bufferSize = 8192;

      final BufferedInputStream buffer = new BufferedInputStream(inputStream, bufferSize);
      buffer.mark(bufferSize * 2);

      return buffer;
   }

   private static FilterInputStream unclosable(InputStream inputStream)
   {
      return new FilterInputStream(inputStream)
      {
         @Override
         public void close() throws IOException
         { // unclosable
         }
      };
   }
}
