/**
 * Copyright (c) 2011 Sourcepit.org contributors and others. All rights reserved. This program and the accompanying
 * materials are made available under the terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.sourcepit.common.utils.props;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import org.junit.Test;

public class PropertiesUtilsTest extends TestCase
{
   public void testEscapeJavaProperties() throws Exception
   {
      assertEquals("\\u00FC", PropertiesUtils.escapeJavaProperties("ü"));

      String result = PropertiesUtils.escapeJavaProperties("   Hans\\\n\r im ''Glück''!");
      assertEquals("\\   Hans\\\\\\n\\r im ''Gl\\u00FCck''\\!", result);
   }

   @Test
   public void testSplit() throws Exception
   {
      List<String> dest = new ArrayList<String>();

      PropertiesUtils.split(dest, ",foo, , bar", ",", true, true);
      assertEquals(2, dest.size());
      assertEquals("foo", dest.get(0));
      assertEquals("bar", dest.get(1));

      dest.clear();

      PropertiesUtils.split(dest, ",foo, , bar", ",", false, false);
      assertEquals(4, dest.size());
      assertEquals("", dest.get(0));
      assertEquals("foo", dest.get(1));
      assertEquals(" ", dest.get(2));
      assertEquals(" bar", dest.get(3));

      dest.clear();

      PropertiesUtils.split(dest, ",foo, , bar", ",", true, false);
      assertEquals(4, dest.size());
      assertEquals("", dest.get(0));
      assertEquals("foo", dest.get(1));
      assertEquals("", dest.get(2));
      assertEquals("bar", dest.get(3));

      dest.clear();

      PropertiesUtils.split(dest, ",foo, , bar", ",", false, true);
      assertEquals(3, dest.size());
      assertEquals("foo", dest.get(0));
      assertEquals(" ", dest.get(1));
      assertEquals(" bar", dest.get(2));
   }
}
