/**
 * Copyright (c) 2012 Sourcepit.org contributors and others. All rights reserved. This program and the accompanying
 * materials are made available under the terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.sourcepit.common.utils.io.factories.impl;

import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipInputStream;

import org.sourcepit.common.utils.io.IOFactory;
import org.sourcepit.common.utils.io.factories.ZipInputStreamFactory;


/**
 * @author Bernd Vogt <bernd.vogt@sourcepit.org>
 */
public class ZipInputStreamFactoryImpl implements ZipInputStreamFactory
{
   private final IOFactory<? extends InputStream> resource;

   public ZipInputStreamFactoryImpl(IOFactory<? extends InputStream> resource)
   {
      this.resource = resource;
   }

   public ZipInputStream open() throws IOException
   {
      return new ZipInputStream(resource.open());
   }
}
