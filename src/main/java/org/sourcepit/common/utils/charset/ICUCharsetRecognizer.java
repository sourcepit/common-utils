/**
 * Copyright (c) 2012 Sourcepit.org contributors and others. All rights reserved. This program and the accompanying
 * materials are made available under the terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.sourcepit.common.utils.charset;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.inject.Named;

import com.ibm.icu.text.CharsetDetector;
import com.ibm.icu.text.CharsetMatch;

@Named
public class ICUCharsetRecognizer implements CharsetRecognizer
{
   public Collection<org.sourcepit.common.utils.charset.CharsetMatch> recognize(String fileName, String fileExtension,
      InputStream inputStream, Charset declaredCharset) throws IOException
   {
      if (inputStream != null)
      {
         return recognize(inputStream, declaredCharset);
      }
      return null;
   }

   private Collection<org.sourcepit.common.utils.charset.CharsetMatch> recognize(InputStream inputStream,
      Charset declaredCharset) throws IOException
   {
      return filter(detectAll(inputStream, declaredCharset));
   }

   private CharsetMatch[] detectAll(InputStream inputStream, Charset declaredCharset) throws IOException
   {
      final CharsetDetector charsetDetector = new CharsetDetector();
      charsetDetector.setDeclaredEncoding(declaredCharset.name());
      charsetDetector.setText(inputStream);
      return charsetDetector.detectAll();
   }

   private Collection<org.sourcepit.common.utils.charset.CharsetMatch> filter(CharsetMatch[] detected)
   {
      final List<org.sourcepit.common.utils.charset.CharsetMatch> results = new ArrayList<org.sourcepit.common.utils.charset.CharsetMatch>();
      for (CharsetMatch charsetMatch : detected)
      {
         results.add(new org.sourcepit.common.utils.charset.CharsetMatch(charsetMatch.getName(), charsetMatch
            .getConfidence()));
      }
      return results;
   }
}