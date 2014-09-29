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

import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import java.util.List;

import org.hamcrest.core.Is;
import org.hamcrest.core.IsEqual;
import org.hamcrest.core.IsNot;
import org.hamcrest.core.IsNull;
import org.junit.Test;

/**
 * PathTest
 * 
 * @author Bernd
 */
public class PathTest
{

   @Test
   public void testNull()
   {
      try
      {
         new Path(null);
         fail();
      }
      catch (IllegalArgumentException e)
      {
      }
   }

   @Test
   public void testToString()
   {
      Path path;

      path = new Path("");
      assertThat(path.toString(), IsEqual.equalTo(""));

      path = new Path("foo");
      assertThat(path.toString(), IsEqual.equalTo("foo"));

      path = new Path("/foo/bar");
      assertThat(path.toString(), IsEqual.equalTo("/foo/bar"));

      path = new Path("/foo/bar/");
      assertThat(path.toString(), IsEqual.equalTo("/foo/bar"));

      path = new Path("/foo//bar/");
      assertThat(path.toString(), IsEqual.equalTo("/foo/bar"));

      path = new Path("\\foo\\bar");
      assertThat(path.toString(), IsEqual.equalTo("/foo/bar"));

      path = new Path("\\foo\\bar\\");
      assertThat(path.toString(), IsEqual.equalTo("/foo/bar"));

      path = new Path("\\foo\\\\bar\\");
      assertThat(path.toString(), IsEqual.equalTo("/foo/bar"));
   }

   @Test
   public void testGetSegments()
   {
      Path path;
      List<String> segments;

      path = new Path("");
      segments = path.getSegments();
      assertThat(segments.size(), Is.is(1));
      assertThat(segments.get(0), IsEqual.equalTo(""));

      path = new Path("foo");
      segments = path.getSegments();
      assertThat(segments.size(), Is.is(1));
      assertThat(segments.get(0), IsEqual.equalTo("foo"));

      path = new Path("/foo/bar");
      segments = path.getSegments();
      assertThat(segments.size(), Is.is(2));
      assertThat(segments.get(0), IsEqual.equalTo("foo"));
      assertThat(segments.get(1), IsEqual.equalTo("bar"));

      path = new Path("/foo/bar/");
      segments = path.getSegments();
      assertThat(segments.size(), Is.is(2));
      assertThat(segments.get(0), IsEqual.equalTo("foo"));
      assertThat(segments.get(1), IsEqual.equalTo("bar"));

      path = new Path("/foo//bar/");
      segments = path.getSegments();
      assertThat(segments.size(), Is.is(2));
      assertThat(segments.get(0), IsEqual.equalTo("foo"));
      assertThat(segments.get(1), IsEqual.equalTo("bar"));
   }

   @Test
   public void testGetFileNameAndExtension()
   {
      Path path;

      path = new Path("");
      assertThat(path.getFileName(), IsNull.nullValue());
      assertThat(path.getFileExtension(), IsNull.nullValue());

      path = new Path(".");
      assertThat(path.getFileName(), IsNull.nullValue());
      assertThat(path.getFileExtension(), IsNull.nullValue());

      path = new Path("foo");
      assertThat(path.getFileName(), IsNull.nullValue());
      assertThat(path.getFileExtension(), IsNull.nullValue());

      path = new Path(".txt");
      assertThat(path.getFileName(), IsNull.nullValue());
      assertThat(path.getFileExtension(), IsEqual.equalTo("txt"));

      path = new Path("foo.txt");
      assertThat(path.getFileName(), IsEqual.equalTo("foo"));
      assertThat(path.getFileExtension(), IsEqual.equalTo("txt"));

      path = new Path("foo.bar.txt");
      assertThat(path.getFileName(), IsEqual.equalTo("foo.bar"));
      assertThat(path.getFileExtension(), IsEqual.equalTo("txt"));

      path = new Path("foo/foo.bar.txt");
      assertThat(path.getFileName(), IsEqual.equalTo("foo.bar"));
      assertThat(path.getFileExtension(), IsEqual.equalTo("txt"));
   }

   @Test
   public void testGetParent()
   {
      Path path;

      path = new Path("");
      assertThat(path.getParent(), IsNull.nullValue());

      path = new Path("foo");
      assertThat(path.getParent(), IsNull.nullValue());

      path = new Path("/foo");
      assertThat(path.getParent(), IsNull.nullValue());

      path = new Path("/foo//bar");
      assertThat(path.getParent().getFirstSegment(), IsEqual.equalTo("foo"));

      path = new Path("foo/bar");
      assertThat(path.getParent().getLastSegment(), IsEqual.equalTo("foo"));
   }

   @Test
   public void testEquals()
   {
      assertThat(new Path(""), IsEqual.equalTo(new Path("")));
      assertThat(new Path("foo"), IsEqual.equalTo(new Path("foo")));
      assertThat(new Path("/foo"), IsEqual.equalTo(new Path("/foo")));
      assertThat(new Path("\\foo"), IsEqual.equalTo(new Path("/foo")));
      assertThat(new Path("foo/"), IsEqual.equalTo(new Path("foo/")));
      assertThat(new Path("foo/bar"), IsEqual.equalTo(new Path("foo/bar")));
      assertThat(new Path("foo/bar/"), IsEqual.equalTo(new Path("foo/bar")));
      assertThat(new Path("foo\\bar"), IsEqual.equalTo(new Path("foo/bar")));
      assertThat(new Path("foo\\bar"), IsEqual.equalTo(new Path("foo/bar")));

      assertThat(new Path("/foo"), IsNot.not(IsEqual.equalTo(new Path("foo"))));
      assertThat(new Path("\\foo"), IsNot.not(IsEqual.equalTo(new Path("foo"))));
      assertThat(new Path("foo"), IsNot.not(IsEqual.equalTo(new Path("bar"))));
   }
}
