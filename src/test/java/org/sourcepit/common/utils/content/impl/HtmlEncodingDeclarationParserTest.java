/*
 * Copyright (C) 2013 Bosch Software Innovations GmbH. All rights reserved.
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
