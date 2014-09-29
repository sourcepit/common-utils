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

package org.sourcepit.common.utils.content.impl;

import java.io.IOException;
import java.io.InputStream;
import java.net.URLConnection;

import org.sourcepit.common.utils.content.MimeTypesProvider;
import org.sourcepit.common.utils.props.PropertiesSource;

public class JavaDefaultMimeTypesProvider implements MimeTypesProvider
{
   @Override
   public String forFileName(String fileName, PropertiesSource options)
   {
      return URLConnection.guessContentTypeFromName(fileName);
   }

   @Override
   public String forContent(InputStream content, PropertiesSource options) throws IOException
   {
      return URLConnection.guessContentTypeFromStream(content);
   }

   @Override
   public String getBaseType(String mimeType, PropertiesSource options)
   {
      return null;
   }
}
