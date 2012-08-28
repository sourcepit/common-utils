/**
 * Copyright (c) 2012 Sourcepit.org contributors and others. All rights reserved. This program and the accompanying
 * materials are made available under the terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.sourcepit.common.utils.charset;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Collection;
import java.util.Collections;

import javax.inject.Named;

import org.sourcepit.common.utils.lang.PipedException;
import org.sourcepit.common.utils.xml.SAXEncodingDetector;

@Named
public class XMLCharsetRecognizer implements CharsetRecognizer
{
   public Collection<CharsetMatch> recognize(String fileName, String fileExtension, InputStream inputStream,
      Charset declaredCharset) throws IOException
   {
      if (inputStream != null)
      {
         try
         {
            String encoding = SAXEncodingDetector.parse(inputStream);
            CharsetMatch match = new CharsetMatch(encoding, 100);
            return Collections.singleton(match);
         }
         catch (PipedException e)
         {
            e.adaptAndThrow(IOException.class);
         }
      }
      return null;
   }
}
