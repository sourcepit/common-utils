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

package org.sourcepit.common.utils.xml;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.sourcepit.common.utils.lang.Exceptions;
import org.xml.sax.InputSource;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.ext.Locator2;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;

public final class SAXEncodingDetector
{
   private SAXEncodingDetector()
   {
      super();
   }

   public static String parse(InputStream inputStream)
   {
      try
      {
         final LocatorHandler handler = new LocatorHandler();

         final XMLReader parser = XMLReaderFactory.createXMLReader();
         parser.setContentHandler(handler);
         try
         {
            parser.parse(new InputSource(new FilterInputStream(inputStream)
            {
               @Override
               public void close() throws IOException
               { // unclosable
               }
            }));
         }
         catch (SAXTerminationException e)
         { // expected
         }

         final Locator locator = handler.getLocator();
         if (locator instanceof Locator2)
         {
            return ((Locator2) locator).getEncoding();
         }
         throw Exceptions.pipe(new IllegalStateException());
      }
      catch (SAXException e)
      {
         throw Exceptions.pipe(e);
      }
      catch (IOException e)
      {
         throw Exceptions.pipe(e);
      }
   }

   private static final class LocatorHandler extends DefaultHandler
   {
      private Locator locator;

      public Locator getLocator()
      {
         return locator;
      }

      @Override
      public void setDocumentLocator(Locator locator)
      {
         this.locator = locator;
      }

      @Override
      public void startDocument() throws SAXException
      {
         throw new SAXTerminationException();
      }
   }

   private static class SAXTerminationException extends SAXException
   {
      private static final long serialVersionUID = 1L;
   }
}