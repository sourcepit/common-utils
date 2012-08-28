/**
 * Copyright (c) 2011 Sourcepit.org contributors and others. All rights reserved. This program and the accompanying
 * materials are made available under the terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.sourcepit.common.utils.props;

import junit.framework.TestCase;

public class PropertiesUtilsTest extends TestCase
{
   public void testEscapeJavaProperties() throws Exception
   {
      assertEquals("\\u00FC", PropertiesUtils.escapeJavaProperties("ü"));

      String result = PropertiesUtils.escapeJavaProperties("   Hans\\\n\r im ''Glück''!");
      assertEquals("\\   Hans\\\\\\n\\r im ''Gl\\u00FCck''\\!", result);
   }
}
