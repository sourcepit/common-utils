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

import org.sourcepit.common.utils.io.handles.ByteArrayInputStreamHandle;
import org.sourcepit.common.utils.io.handles.ByteArrayOutputStreamHandle;
import org.sourcepit.common.utils.io.handles.FileInputStreamHandle;
import org.sourcepit.common.utils.io.handles.FileOutputStreamHandle;
import org.sourcepit.common.utils.io.handles.InputStreamHandle;
import org.sourcepit.common.utils.io.handles.JarInputStreamHandle;
import org.sourcepit.common.utils.io.handles.JarOutputStreamHandle;
import org.sourcepit.common.utils.io.handles.OutputStreamHandle;
import org.sourcepit.common.utils.io.handles.ZipInputStreamHandle;
import org.sourcepit.common.utils.io.handles.ZipOutputStreamHandle;
import org.sourcepit.common.utils.io.handles.impl.BufferedInputStreamHandleImpl;
import org.sourcepit.common.utils.io.handles.impl.BufferedOutputStreamHandleImpl;
import org.sourcepit.common.utils.io.handles.impl.ByteArrayInputStreamHandleImpl;
import org.sourcepit.common.utils.io.handles.impl.ByteArrayOutputStreamHandleImpl;
import org.sourcepit.common.utils.io.handles.impl.ClassPathInputStreamHandleImpl;
import org.sourcepit.common.utils.io.handles.impl.FileInputStreamHandleImpl;
import org.sourcepit.common.utils.io.handles.impl.FileOutputStreamHandleImpl;
import org.sourcepit.common.utils.io.handles.impl.JarInputStreamHandleImpl;
import org.sourcepit.common.utils.io.handles.impl.JarOutputStreamHandleImpl;
import org.sourcepit.common.utils.io.handles.impl.URLInputStreamHandleImpl;
import org.sourcepit.common.utils.io.handles.impl.ZipEntryInputStreamHandleImpl;
import org.sourcepit.common.utils.io.handles.impl.ZipInputStreamHandleImpl;
import org.sourcepit.common.utils.io.handles.impl.ZipOutputStreamHandleImpl;
import org.sourcepit.common.utils.lang.Exceptions;
import org.sourcepit.common.utils.lang.PipedException;
import org.sourcepit.common.utils.lang.ThrowablePipe;

/**
 * @author Bernd Vogt <bernd.vogt@sourcepit.org>
 */
public class IO
{

   public static <Resource extends Closeable, Content> Content read(Read<Resource, Content> operation,
      IOHandle<? extends Resource> resource) throws PipedException
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
      IOHandle<? extends Resource> resource, Content content) throws PipedException
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


   public static FileInputStreamHandle fileIn(String path)
   {
      return fileIn(new File(path));
   }

   public static FileInputStreamHandle fileIn(File parent, String path)
   {
      return fileIn(new File(parent, path));
   }

   public static FileInputStreamHandle fileIn(File file)
   {
      return new FileInputStreamHandleImpl(file);
   }

   public static FileInputStreamHandle fileIn(String path, boolean createOnDemand)
   {
      return fileIn(new File(path), createOnDemand);
   }

   public static FileInputStreamHandle fileIn(File parent, String path, boolean createOnDemand)
   {
      return fileIn(new File(parent, path), createOnDemand);
   }

   public static FileInputStreamHandle fileIn(File file, boolean createOnDemand)
   {
      return new FileInputStreamHandleImpl(file, createOnDemand);
   }

   public static InputStreamHandle buffIn(IOHandle<? extends InputStream> resource)
   {
      return new BufferedInputStreamHandleImpl(resource);
   }

   public static ByteArrayInputStreamHandle byteIn(byte[] bytes)
   {
      return new ByteArrayInputStreamHandleImpl(bytes);
   }

   public static JarInputStreamHandle jarIn(IOHandle<? extends InputStream> resource)
   {
      return new JarInputStreamHandleImpl(resource);
   }

   public static ZipInputStreamHandle zipIn(IOHandle<? extends InputStream> resource)
   {
      return new ZipInputStreamHandleImpl(resource);
   }

   public static ZipInputStreamHandle zipIn(IOHandle<? extends InputStream> resource, String entryName)
   {
      return new ZipEntryInputStreamHandleImpl(resource, entryName);
   }

   public static InputStreamHandle urlIn(URL url)
   {
      return new URLInputStreamHandleImpl(url);
   }

   public static InputStreamHandle cpIn(ClassLoader classLoader, String name)
   {
      return new ClassPathInputStreamHandleImpl(classLoader, name);
   }

   public static IOHandle<? extends InputStream> osgiIn(File bundleLocation, String entryName)
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

   public static FileOutputStreamHandle fileOut(String path)
   {
      return fileOut(new File(path));
   }

   public static FileOutputStreamHandle fileOut(File parent, String path)
   {
      return fileOut(new File(parent, path));
   }

   public static FileOutputStreamHandle fileOut(File file)
   {
      return new FileOutputStreamHandleImpl(file);
   }

   public static FileOutputStreamHandle fileOut(String path, boolean createOnDemand)
   {
      return fileOut(new File(path), createOnDemand);
   }

   public static FileOutputStreamHandle fileOut(File parent, String path, boolean createOnDemand)
   {
      return fileOut(new File(parent, path), createOnDemand);
   }

   public static FileOutputStreamHandle fileOut(File file, boolean createOnDemand)
   {
      return new FileOutputStreamHandleImpl(file, createOnDemand);
   }

   public static OutputStreamHandle buffOut(IOHandle<? extends OutputStream> resource)
   {
      return new BufferedOutputStreamHandleImpl(resource);
   }

   public static ByteArrayOutputStreamHandle byteOut()
   {
      return new ByteArrayOutputStreamHandleImpl();
   }

   public static ByteArrayOutputStreamHandle byteOut(int size)
   {
      return new ByteArrayOutputStreamHandleImpl(size);
   }

   public static JarOutputStreamHandle jarOut(IOHandle<? extends OutputStream> resource)
   {
      return new JarOutputStreamHandleImpl(resource);
   }

   public static ZipOutputStreamHandle zipOut(IOHandle<? extends OutputStream> resource)
   {
      return new ZipOutputStreamHandleImpl(resource);
   }
}
