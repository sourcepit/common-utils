/**
 * Copyright (c) 2012 Sourcepit.org contributors and others. All rights reserved. This program and the accompanying
 * materials are made available under the terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.sourcepit.common.utils.io;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * @author Bernd Vogt <bernd.vogt@sourcepit.org>
 */
public class IOResources
{
   public static FileInputStreamResource fileIn(File file)
   {
      return new FileInputStreamResource(file);
   }

   public static FileInputStreamResource fileIn(File file, boolean createOnDemand)
   {
      return new FileInputStreamResource(file, createOnDemand);
   }

   public static BufferedInputStreamResource buffIn(IOResource<? extends InputStream> resource)
   {
      return new BufferedInputStreamResource(resource);
   }

   public static JarInputStreamResource jarIn(IOResource<? extends InputStream> resource)
   {
      return new JarInputStreamResource(resource);
   }

   public static FileOutputStreamResource fileOut(File file)
   {
      return new FileOutputStreamResource(file);
   }

   public static FileOutputStreamResource fileOut(File file, boolean createOnDemand)
   {
      return new FileOutputStreamResource(file, createOnDemand);
   }

   public static BufferedOutputStreamResource buffOut(IOResource<? extends OutputStream> resource)
   {
      return new BufferedOutputStreamResource(resource);
   }

   public static JarOutputStreamResource jarOut(IOResource<? extends OutputStream> resource)
   {
      return new JarOutputStreamResource(resource);
   }
}
