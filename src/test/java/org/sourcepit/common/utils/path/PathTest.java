/**
 * Copyright (c) 2011 Sourcepit.org contributors and others. All rights reserved. This program and the accompanying
 * materials are made available under the terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.sourcepit.common.utils.path;

import static org.junit.Assert.*;

import java.util.List;

import org.hamcrest.core.Is;
import org.hamcrest.core.IsEqual;
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

}
