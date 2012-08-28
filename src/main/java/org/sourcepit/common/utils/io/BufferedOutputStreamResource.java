/**
 * Copyright (c) 2012 Sourcepit.org contributors and others. All rights reserved. This program and the accompanying
 * materials are made available under the terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.sourcepit.common.utils.io;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * @author Bernd Vogt <bernd.vogt@sourcepit.org>
 */
public class BufferedOutputStreamResource implements IOResource<BufferedOutputStream>
{
   private final IOResource<? extends OutputStream> resource;

   public BufferedOutputStreamResource(IOResource<? extends OutputStream> resource)
   {
      this.resource = resource;
   }

   public BufferedOutputStream open() throws IOException
   {
      return new BufferedOutputStream(resource.open());
   }

}
