/**
 * Copyright (c) 2012 Sourcepit.org contributors and others. All rights reserved. This program and the accompanying
 * materials are made available under the terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.sourcepit.common.utils.io;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

import org.sourcepit.common.utils.lang.Exceptions;
import org.sourcepit.common.utils.lang.PipedException;
import org.sourcepit.common.utils.lang.ThrowablePipe;

/**
 * @author Bernd Vogt <bernd.vogt@sourcepit.org>
 */
public class IOResources
{

   public static <Resource extends Closeable, Content> Content read(IOResource<? extends Resource> resource,
      Read<Resource, Content> operation) throws PipedException
   {
      ThrowablePipe error = null;
      Resource openResource = null;
      try
      {
         openResource = resource.open();
         return operation.read(openResource);
      }
      catch (Throwable e)
      {
         error = Exceptions.pipe(e);
         throw new Error(); // fake compiler
      }
      finally
      {
         closeAndThrow(openResource, error);
      }
   }

   public static <Resource extends Closeable, Content> void write(Content content,
      IOResource<? extends Resource> resource, Write<Resource, Content> operation) throws PipedException
   {
      ThrowablePipe error = null;
      Resource openResource = null;
      try
      {
         openResource = resource.open();
         operation.write(openResource, content);
      }
      catch (Throwable e)
      {
         error = Exceptions.pipe(e);
      }
      finally
      {
         closeAndThrow(openResource, error);
      }
   }

   private static void closeAndThrow(Closeable closeable, ThrowablePipe errors) throws PipedException
   {
      if (closeable != null)
      {
         try
         {
            closeable.close();
         }
         catch (IOException e)
         {
            if (errors == null)
            {
               errors = Exceptions.pipe(e);
            }
            else
            {
               errors.add(e);
            }
         }
      }

      if (errors != null)
      {
         errors.throwPipe();
      }
   }


   public static FileInputStreamResource fileIn(String path)
   {
      return fileIn(new File(path));
   }

   public static FileInputStreamResource fileIn(File parent, String path)
   {
      return fileIn(new File(parent, path));
   }

   public static FileInputStreamResource fileIn(File file)
   {
      return new FileInputStreamResource(file);
   }

   public static FileInputStreamResource fileIn(String path, boolean createOnDemand)
   {
      return fileIn(new File(path), createOnDemand);
   }

   public static FileInputStreamResource fileIn(File parent, String path, boolean createOnDemand)
   {
      return fileIn(new File(parent, path), createOnDemand);
   }

   public static FileInputStreamResource fileIn(File file, boolean createOnDemand)
   {
      return new FileInputStreamResource(file, createOnDemand);
   }

   public static BufferedInputStreamResource buffIn(IOResource<? extends InputStream> resource)
   {
      return new BufferedInputStreamResource(resource);
   }

   public static ByteArrayInputStreamResource byteIn(byte[] bytes)
   {
      return new ByteArrayInputStreamResource(bytes);
   }

   public static JarInputStreamResource jarIn(IOResource<? extends InputStream> resource)
   {
      return new JarInputStreamResource(resource);
   }

   public static ZipInputStreamResource zipIn(IOResource<? extends InputStream> resource)
   {
      return new ZipInputStreamResource(resource);
   }

   public static ZipEntryResource zipIn(IOResource<? extends InputStream> resource, String entryName)
   {
      return new ZipEntryResource(resource, entryName);
   }

   public static URLResource urlIn(URL url)
   {
      return new URLResource(url);
   }

   public static ClassPathResource cpIn(ClassLoader classLoader, String name)
   {
      return new ClassPathResource(classLoader, name);
   }

   public static IOResource<? extends InputStream> osgiIn(File bundleLocation, String entryName)
   {
      if (bundleLocation.isDirectory())
      {
         return buffIn(fileIn(new File(bundleLocation, entryName)));
      }
      else
      {
         return zipIn(buffIn(fileIn(bundleLocation)), entryName);
      }
   }

   public static FileOutputStreamResource fileOut(String path)
   {
      return fileOut(new File(path));
   }

   public static FileOutputStreamResource fileOut(File parent, String path)
   {
      return fileOut(new File(parent, path));
   }

   public static FileOutputStreamResource fileOut(File file)
   {
      return new FileOutputStreamResource(file);
   }

   public static FileOutputStreamResource fileOut(String path, boolean createOnDemand)
   {
      return fileOut(new File(path), createOnDemand);
   }

   public static FileOutputStreamResource fileOut(File parent, String path, boolean createOnDemand)
   {
      return fileOut(new File(parent, path), createOnDemand);
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

   public static ZipOutputStreamResource zipOut(IOResource<? extends OutputStream> resource)
   {
      return new ZipOutputStreamResource(resource);
   }
}
