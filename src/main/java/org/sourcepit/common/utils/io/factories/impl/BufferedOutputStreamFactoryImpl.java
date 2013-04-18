/**
 * Copyright (c) 2012 Sourcepit.org contributors and others. All rights reserved. This program and the accompanying
 * materials are made available under the terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.sourcepit.common.utils.io.factories.impl;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.sourcepit.common.utils.io.IOFactory;
import org.sourcepit.common.utils.io.factories.OutputStreamFactory;


/**
 * @author Bernd Vogt <bernd.vogt@sourcepit.org>
 */
public class BufferedOutputStreamFactoryImpl implements OutputStreamFactory
{
   private final IOFactory<? extends OutputStream> resource;

   public BufferedOutputStreamFactoryImpl(IOFactory<? extends OutputStream> resource)
   {
      this.resource = resource;
   }

   public BufferedOutputStream open() throws IOException
   {
      return new BufferedOutputStream(resource.open());
   }

}
