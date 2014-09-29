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

package org.sourcepit.common.utils.path;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import org.sourcepit.common.utils.path.PathUtils.PathResolutionException;

/**
 * @author Bernd
 * 
 */
public class PathUtilsTest extends TestCase
{
   public void testTrimFileExtension() throws Exception
   {
      try
      {
         PathUtils.trimFileExtension(null);
         fail();
      }
      catch (IllegalArgumentException e)
      {
      }
      assertEquals("hans", PathUtils.trimFileExtension("hans"));
      assertEquals("hans", PathUtils.trimFileExtension("hans.txt"));
      assertEquals("hans.txt", PathUtils.trimFileExtension("hans.txt.xml"));
   }

   public void testGetRelativePathsEqualPaths() throws Exception
   {
      assertEquals("", PathUtils.getRelativePath("/var", "/var", "/"));
   }

   public void testGetRelativePathsUnix()
   {
      assertEquals("stuff/xyz.dat", PathUtils.getRelativePath("/var/data/stuff/xyz.dat", "/var/data/", "/"));
      assertEquals("../../b/c", PathUtils.getRelativePath("/a/b/c", "/a/x/y/", "/"));
      assertEquals("../../b/c", PathUtils.getRelativePath("/m/n/o/a/b/c", "/m/n/o/a/x/y/", "/"));
   }

   public void testGetRelativePathFileToFile()
   {
      String target = "C:\\Windows\\Boot\\Fonts\\chs_boot.ttf";
      String base = "C:\\Windows\\Speech\\Common\\sapisvr.exe";

      String relPath = PathUtils.getRelativePath(target, base, "\\");
      assertEquals("..\\..\\Boot\\Fonts\\chs_boot.ttf", relPath);
   }

   public void testGetRelativePathDirectoryToFile()
   {
      String target = "C:\\Windows\\Boot\\Fonts\\chs_boot.ttf";
      String base = "C:\\Windows\\Speech\\Common\\";

      String relPath = PathUtils.getRelativePath(target, base, "\\");
      assertEquals("..\\..\\Boot\\Fonts\\chs_boot.ttf", relPath);
   }

   public void testGetRelativePathFileToDirectory()
   {
      String target = "C:\\Windows\\Boot\\Fonts";
      String base = "C:\\Windows\\Speech\\Common\\foo.txt";

      String relPath = PathUtils.getRelativePath(target, base, "\\");
      assertEquals("..\\..\\Boot\\Fonts", relPath);
   }

   public void testGetRelativePathDirectoryToDirectory()
   {
      String target = "C:\\Windows\\Boot\\";
      String base = "C:\\Windows\\Speech\\Common\\";
      String expected = "..\\..\\Boot";

      String relPath = PathUtils.getRelativePath(target, base, "\\");
      assertEquals(expected, relPath);
   }

   public void testGetRelativePathDifferentDriveLetters()
   {
      String target = "D:\\sources\\recovery\\RecEnv.exe";
      String base = "C:\\Java\\workspace\\AcceptanceTests\\Standard test data\\geo\\";

      try
      {
         PathUtils.getRelativePath(target, base, "\\");
         fail();

      }
      catch (PathResolutionException ex)
      {
         // expected exception
      }
   }

   public void testToJavaList() throws Exception
   {
      assertNull(PathUtils.toJavaList(null, ","));

      List<String> list = PathUtils.toJavaList("", ",");
      assertEquals(1, list.size());
      assertEquals("", list.get(0));

      list = PathUtils.toJavaList("1", ",");
      assertEquals(1, list.size());
      assertEquals("1", list.get(0));

      list = PathUtils.toJavaList("1, 2", ",");
      assertEquals(2, list.size());
      assertEquals("1", list.get(0));
      assertEquals("2", list.get(1));

      list = PathUtils.toJavaList("1,,, 2", ",,,");
      assertEquals(2, list.size());
      assertEquals("1", list.get(0));
      assertEquals("2", list.get(1));
   }

   public void testToStringList() throws Exception
   {
      assertNull(PathUtils.toStringList((String[]) null, ","));
      assertNull(PathUtils.toStringList((List<String>) null, ","));

      List<String> list = new ArrayList<String>();
      assertEquals("", PathUtils.toStringList(list, ","));

      list = new ArrayList<String>();
      list.add("1");
      assertEquals("1", PathUtils.toStringList(list, ","));

      list = new ArrayList<String>();
      list.add("1");
      list.add("2");
      assertEquals("1,2", PathUtils.toStringList(list, ","));

      list = new ArrayList<String>();
      list.add("1");
      list.add("2");
      assertEquals("1,,,2", PathUtils.toStringList(list, ",,,"));
   }
}
