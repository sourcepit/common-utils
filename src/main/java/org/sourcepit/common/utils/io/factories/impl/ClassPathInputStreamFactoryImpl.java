/**
 * Copyright (c) 2012 Sourcepit.org contributors and others. All rights reserved. This program and the accompanying
 * materials are made available under the terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.sourcepit.common.utils.io.factories.impl;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import org.sourcepit.common.utils.io.factories.InputStreamFactory;


public class ClassPathInputStreamFactoryImpl implements InputStreamFactory
{
   private ClassLoader classLoader;
   private String name;

   public ClassPathInputStreamFactoryImpl(ClassLoader classLoader, String name)
   {
      this.classLoader = classLoader;
      this.name = name;
   }

   public InputStream open() throws IOException
   {
      final InputStream resourceAsStream = classLoader.getResourceAsStream(name);
      if (resourceAsStream == null)
      {
         throw new FileNotFoundException(name);
      }
      return resourceAsStream;
   }

}
