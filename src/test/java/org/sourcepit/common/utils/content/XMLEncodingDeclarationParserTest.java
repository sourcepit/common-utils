/**
 * Copyright (c) 2012 Sourcepit.org contributors and others. All rights reserved. This program and the accompanying
 * materials are made available under the terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.sourcepit.common.utils.content;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import org.junit.Test;

public class XMLEncodingDeclarationParserTest
{

   @Test
   public void test() throws IOException
   {
      String encoding = new XMLEncodingDeclarationParser().parse(new ByteArrayInputStream("".getBytes()));
      assertEquals("UTF-8", encoding);

      encoding = new XMLEncodingDeclarationParser().parse(new ByteArrayInputStream("<doc></doc>".getBytes()));
      assertEquals("UTF-8", encoding);

      encoding = new XMLEncodingDeclarationParser().parse(new ByteArrayInputStream(
         "<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>\n<doc></doc>".getBytes("ISO-8859-1")));
      assertEquals("ISO-8859-1", encoding);
   }

}
