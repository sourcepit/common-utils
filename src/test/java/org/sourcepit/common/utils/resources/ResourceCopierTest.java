/*
 * Copyright 2014 Bernd Vogt and others.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.sourcepit.common.utils.resources;

import static org.apache.commons.io.FileUtils.forceMkdir;
import static org.apache.commons.io.IOUtils.closeQuietly;
import static org.apache.commons.io.IOUtils.copy;
import static org.sourcepit.common.utils.io.IO.buffIn;
import static org.sourcepit.common.utils.io.IO.buffOut;
import static org.sourcepit.common.utils.io.IO.byteIn;
import static org.sourcepit.common.utils.io.IO.fileOut;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.junit.Rule;
import org.junit.Test;
import org.sourcepit.common.testing.Environment;
import org.sourcepit.common.testing.Workspace;

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
