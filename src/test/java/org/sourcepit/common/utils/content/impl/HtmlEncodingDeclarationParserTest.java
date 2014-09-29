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

import static org.junit.Assert.assertEquals;
import static org.sourcepit.common.utils.content.impl.CssEncodingDeclarationParserTest.asStream;

import java.io.IOException;

import org.junit.Test;
import org.sourcepit.common.utils.content.EncodingDeclarationParser;

public class HtmlEncodingDeclarationParserTest
{

   @Test
   public void test() throws IOException, IOException
   {
      EncodingDeclarationParser parser = new HtmlEncodingDeclarationParser();

      StringBuilder html = new StringBuilder();
      html.append("<!DOCTYPE HTML>");
      html.append("<HTML>");
      html.append(" <HEAD>");
      html.append("  <BASE HREF=\"http://www.example.com/\">");
      html.append("  <TITLE>An application with a long head</TITLE>");
      html.append("  <LINK REL=\"STYLESHEET\" HREF=\"default.css\">");
      html.append("  <META CHARSET=\"UTF-8\">");
      html.append("  <LINK REL=\"STYLESHEET ALTERNATE\" HREF=\"big.css\" TITLE=\"Big Text\">");
      html.append("  <SCRIPT SRC=\"support.js\"></SCRIPT>");
      html.append("  <META NAME=\"APPLICATION-NAME\" CONTENT=\"Long headed application\">");
      html.append(" </HEAD>");
      html.append(" <BODY>");

      assertEquals("UTF-8", parser.parse(asStream(html.toString(), "UTF-8"), null));

      html = new StringBuilder();
      html.append("<!DOCTYPE HTML>");
      html.append("<HTML>");
      html.append(" <HEAD>");
      html.append("  <BASE HREF=\"http://www.example.com/\">");
      html.append("  <TITLE>An application with a long head</TITLE>");
      html.append("  <link rel=\"stylesheet\" href=\"default.css\">");
      html.append("  <meta charset=\"utf-8\">");
      html.append("  <LINK REL=\"STYLESHEET ALTERNATE\" HREF=\"big.css\" TITLE=\"Big Text\">");
      html.append("  <script src=\"support.js\"></script>");
      html.append("  <META NAME=\"APPLICATION-NAME\" CONTENT=\"Long headed application\">");
      html.append(" </HEAD>");
      html.append(" <BODY>");

      assertEquals("utf-8", parser.parse(asStream(html.toString(), "UTF-8"), null));

      html = new StringBuilder();
      html.append("<!DOCTYPE HTML>");
      html.append("<HTML>");
      html.append(" <HEAD>");
      html.append("  <BASE HREF=\"http://www.example.com/\">");
      html.append("  <TITLE>An application with a long head</TITLE>");
      html.append("  <LINK REL=\"STYLESHEET\" HREF=\"default.css\">");
      html.append("  <meta http-equiv=\"Content-Type\" content=\"text/html; charset=8859_1\">");
      html.append("  <LINK REL=\"STYLESHEET ALTERNATE\" HREF=\"big.css\" TITLE=\"Big Text\">");
      html.append("  <SCRIPT SRC=\"support.js\"></SCRIPT>");
      html.append("  <META NAME=\"APPLICATION-NAME\" CONTENT=\"Long headed application\">");
      html.append(" </HEAD>");
      html.append(" <BODY>");

      assertEquals("8859_1", parser.parse(asStream(html.toString(), "8859_1"), null));
   }

}
