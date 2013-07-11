/**
 * Copyright (c) 2013 Sourcepit.org contributors and others. All rights reserved. This program and the accompanying
 * materials are made available under the terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.sourcepit.common.utils.resources;

import static org.apache.commons.io.FileUtils.forceMkdir;
import static org.apache.commons.io.IOUtils.closeQuietly;
import static org.apache.commons.io.IOUtils.copy;
import static org.junit.Assert.*;
import static org.sourcepit.common.utils.io.IO.buffIn;
import static org.sourcepit.common.utils.io.IO.buffOut;
import static org.sourcepit.common.utils.io.IO.byteIn;
import static org.sourcepit.common.utils.io.IO.fileOut;
import static org.sourcepit.common.utils.io.IO.zipOut;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.ZipOutputStream;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.junit.Rule;
import org.junit.Test;
import org.sourcepit.common.testing.Environment;
import org.sourcepit.common.testing.Workspace;
import org.sourcepit.common.utils.io.IO;
import org.sourcepit.common.utils.io.IOHandle;
import org.sourcepit.common.utils.io.handles.InputStreamHandle;
import org.sourcepit.common.utils.io.handles.OutputStreamHandle;

public class ResourceCopierTest
{
   private Environment env = Environment.get("env-test.properties");

   @Rule
   public Workspace ws = new Workspace(env.getBuildDir(), "test-ws", false);

   @Test
   public void test() throws IOException
   {
      final File baseDir = ws.getRoot();

      File zipDir = new File(baseDir, "zip");
      createFile(zipDir, "1/a.txt", "1/a.txt");
      createFile(zipDir, "1/1_1/a.txt", "1/1_1/a.txt");
      createFile(zipDir, "1/1_1/b.txt", "1/1_1/b.txt");
      createFile(zipDir, "2/a.txt", "2/a.txt");
      createFile(zipDir, "2/b.txt", "2/b.txt");
      forceMkdir(new File(zipDir, "3"));

      File zipFile = new File(baseDir, "zip.zip");

      ResourceCopier resourceCopier = new ResourceCopier();

      resourceCopier.copy(new FileTraverser(zipDir), new ZipStorage(zipFile));

      resourceCopier.copy(new ZipTraverser(zipFile), new ZipStorage(new File(baseDir, "zip2.zip")));

      resourceCopier.copy(new ZipTraverser(zipFile), new FileStorage(new File(baseDir, "zip2")));
   }

   private static File createFile(File baseDir, String path, String content) throws IOException
   {
      final File file = new File(baseDir, path);
      forceMkdir(file.getParentFile());

      InputStream in = null;
      OutputStream out = null;
      try
      {
         in = buffIn(byteIn(content.getBytes("UTF-8"))).open();
         out = buffOut(fileOut(file)).open();
         copy(in, out);
      }
      finally
      {
         closeQuietly(out);
         closeQuietly(in);
      }

      return file;
   }

}
