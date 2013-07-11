/*
 * Copyright (C) 2013 Bosch Software Innovations GmbH. All rights reserved.
 */

package org.sourcepit.common.utils.content;

import java.io.IOException;
import java.io.InputStream;

import com.ibm.icu.text.CharsetDetector;
import com.ibm.icu.text.CharsetMatch;

public class UTFEncodingDetector implements EncodingDeclarationParser
{
   @Override
   public String parse(InputStream content) throws IOException
   {
      final CharsetDetector charsetDetector = new CharsetDetector();
      charsetDetector.setDeclaredEncoding("UTF-8");
      charsetDetector.setText(content);
      CharsetMatch[] matches = charsetDetector.detectAll();
      for (CharsetMatch match : matches)
      {
         if (match.getName().toLowerCase().startsWith("utf-"))
         {
            return match.getName();
         }
      }
      return null;
   }
}
