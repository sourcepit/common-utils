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

import org.sourcepit.common.utils.io.factories.ByteArrayInputStreamFactory;
import org.sourcepit.common.utils.io.factories.ByteArrayOutputStreamFactory;
import org.sourcepit.common.utils.io.factories.FileInputStreamFactory;
import org.sourcepit.common.utils.io.factories.FileOutputStreamFactory;
import org.sourcepit.common.utils.io.factories.InputStreamFactory;
import org.sourcepit.common.utils.io.factories.JarInputStreamFactory;
import org.sourcepit.common.utils.io.factories.JarOutputStreamFactory;
import org.sourcepit.common.utils.io.factories.OutputStreamFactory;
import org.sourcepit.common.utils.io.factories.ZipInputStreamFactory;
import org.sourcepit.common.utils.io.factories.ZipOutputStreamFactory;
import org.sourcepit.common.utils.io.factories.impl.BufferedInputStreamFactoryImpl;
import org.sourcepit.common.utils.io.factories.impl.BufferedOutputStreamFactoryImpl;
import org.sourcepit.common.utils.io.factories.impl.ByteArrayInputStreamFactoryImpl;
import org.sourcepit.common.utils.io.factories.impl.ByteArrayOutputStreamFactoryImpl;
import org.sourcepit.common.utils.io.factories.impl.ClassPathInputStreamFactoryImpl;
import org.sourcepit.common.utils.io.factories.impl.FileInputStreamFactoryImpl;
import org.sourcepit.common.utils.io.factories.impl.FileOutputStreamFactoryImpl;
import org.sourcepit.common.utils.io.factories.impl.JarInputStreamFactoryImpl;
import org.sourcepit.common.utils.io.factories.impl.JarOutputStreamFactoryImpl;
import org.sourcepit.common.utils.io.factories.impl.URLInputStreamFactoryImpl;
import org.sourcepit.common.utils.io.factories.impl.ZipEntryInputStreamFactoryImpl;
import org.sourcepit.common.utils.io.factories.impl.ZipInputStreamFactoryImpl;
import org.sourcepit.common.utils.io.factories.impl.ZipOutputStreamFactoryImpl;
import org.sourcepit.common.utils.lang.Exceptions;
import org.sourcepit.common.utils.lang.PipedException;
import org.sourcepit.common.utils.lang.ThrowablePipe;

/**
 * @author Bernd Vogt <bernd.vogt@sourcepit.org>
 */
public class IO
{

   public static <Resource extends Closeable, Content> Content read(Read<Resource, Content> operation,
      IOFactory<? extends Resource> resource) throws PipedException
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

   public static <Resource extends Closeable, Content> void write(Write<Resource, Content> operation,
      IOFactory<? extends Resource> resource, Content content) throws PipedException
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


   public static FileInputStreamFactory fileIn(String path)
   {
      return fileIn(new File(path));
   }

   public static FileInputStreamFactory fileIn(File parent, String path)
   {
      return fileIn(new File(parent, path));
   }

   public static FileInputStreamFactory fileIn(File file)
   {
      return new FileInputStreamFactoryImpl(file);
   }

   public static FileInputStreamFactory fileIn(String path, boolean createOnDemand)
   {
      return fileIn(new File(path), createOnDemand);
   }

   public static FileInputStreamFactory fileIn(File parent, String path, boolean createOnDemand)
   {
      return fileIn(new File(parent, path), createOnDemand);
   }

   public static FileInputStreamFactory fileIn(File file, boolean createOnDemand)
   {
      return new FileInputStreamFactoryImpl(file, createOnDemand);
   }

   public static InputStreamFactory buffIn(IOFactory<? extends InputStream> resource)
   {
      return new BufferedInputStreamFactoryImpl(resource);
   }

   public static ByteArrayInputStreamFactory byteIn(byte[] bytes)
   {
      return new ByteArrayInputStreamFactoryImpl(bytes);
   }

   public static JarInputStreamFactory jarIn(IOFactory<? extends InputStream> resource)
   {
      return new JarInputStreamFactoryImpl(resource);
   }

   public static ZipInputStreamFactory zipIn(IOFactory<? extends InputStream> resource)
   {
      return new ZipInputStreamFactoryImpl(resource);
   }

   public static ZipInputStreamFactory zipIn(IOFactory<? extends InputStream> resource, String entryName)
   {
      return new ZipEntryInputStreamFactoryImpl(resource, entryName);
   }

   public static InputStreamFactory urlIn(URL url)
   {
      return new URLInputStreamFactoryImpl(url);
   }

   public static InputStreamFactory cpIn(ClassLoader classLoader, String name)
   {
      return new ClassPathInputStreamFactoryImpl(classLoader, name);
   }

   public static IOFactory<? extends InputStream> osgiIn(File bundleLocation, String entryName)
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

   public static FileOutputStreamFactory fileOut(String path)
   {
      return fileOut(new File(path));
   }

   public static FileOutputStreamFactory fileOut(File parent, String path)
   {
      return fileOut(new File(parent, path));
   }

   public static FileOutputStreamFactory fileOut(File file)
   {
      return new FileOutputStreamFactoryImpl(file);
   }

   public static FileOutputStreamFactory fileOut(String path, boolean createOnDemand)
   {
      return fileOut(new File(path), createOnDemand);
   }

   public static FileOutputStreamFactory fileOut(File parent, String path, boolean createOnDemand)
   {
      return fileOut(new File(parent, path), createOnDemand);
   }

   public static FileOutputStreamFactory fileOut(File file, boolean createOnDemand)
   {
      return new FileOutputStreamFactoryImpl(file, createOnDemand);
   }

   public static OutputStreamFactory buffOut(IOFactory<? extends OutputStream> resource)
   {
      return new BufferedOutputStreamFactoryImpl(resource);
   }

   public static ByteArrayOutputStreamFactory byteOut()
   {
      return new ByteArrayOutputStreamFactoryImpl();
   }

   public static ByteArrayOutputStreamFactory byteOut(int size)
   {
      return new ByteArrayOutputStreamFactoryImpl(size);
   }

   public static JarOutputStreamFactory jarOut(IOFactory<? extends OutputStream> resource)
   {
      return new JarOutputStreamFactoryImpl(resource);
   }

   public static ZipOutputStreamFactory zipOut(IOFactory<? extends OutputStream> resource)
   {
      return new ZipOutputStreamFactoryImpl(resource);
   }
}
