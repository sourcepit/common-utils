/**
 * Copyright (c) 2011 Sourcepit.org contributors and others. All rights reserved. This program and the accompanying
 * materials are made available under the terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.sourcepit.common.utils.path;

import java.util.HashSet;
import java.util.Set;

import junit.framework.TestCase;

/**
 * @author Bernd
 */
public class PathMatcherTest extends TestCase
{
   public void testToRegex() throws Exception
   {
      assertEquals(".*\\.sdk\\..*", PathMatcher.toSegmentRegex("**.sdk.**", "."));
      assertEquals("[^\\.]*\\.[^\\.]*\\.sdk\\.[^\\.]*\\.[^\\.]*", PathMatcher.toSegmentRegex("*.*.sdk.*.*", "."));

      assertEquals(".*\\.sdk\\..*", PathMatcher.toSegmentRegex("**.sdk.**", "\\"));
      assertEquals("[^\\\\]*\\.[^\\\\]*\\.sdk\\.[^\\\\]*\\.[^\\\\]*", PathMatcher.toSegmentRegex("*.*.sdk.*.*", "\\"));

      assertEquals(".*\\.sdk\\..*", PathMatcher.toSegmentRegex("**.sdk.**", "/"));
      assertEquals("[^/]*\\.[^/]*\\.sdk\\.[^/]*\\.[^/]*", PathMatcher.toSegmentRegex("*.*.sdk.*.*", "/"));
   }

   public void testParse() throws Exception
   {
      PathMatcher pathMatcher = PathMatcher.parsePackagePatterns("*.foo, !bar.**, !*.murks.foo");
      assertEquals(1, pathMatcher.getIncludes().size());
      assertEquals(2, pathMatcher.getExcludes().size());

      final Set<String> includes = new HashSet<String>();
      includes.add("[^\\.]*\\.foo");// PathMatcher.toPathRegex("*.foo", "."));

      assertEquals(includes, pathMatcher.getIncludes());

      final Set<String> excludes = new HashSet<String>();
      excludes.add("bar\\..*");
      excludes.add("[^\\.]*\\.murks\\.foo");

      assertEquals(excludes, pathMatcher.getExcludes());
   }

   public void testMatch() throws Exception
   {
      PathMatcher matcher = PathMatcher.parsePackagePatterns("*.foo");
      assertFalse(matcher.isExclude("murks.foo"));
      assertFalse(matcher.isExclude(""));

      assertTrue(matcher.isInclude("murks.foo"));
      assertTrue(matcher.isInclude("*.foo"));
      assertFalse(matcher.isInclude("foo.murks.foo"));

      matcher = PathMatcher.parsePackagePatterns("**.sdk.**");

      assertTrue(matcher.isMatch("foo.sdk.bar"));
      assertFalse(matcher.isMatch("foo.sdk"));

      matcher = PathMatcher.parsePackagePatterns("*.sdk.*");

      assertTrue(matcher.isMatch("foo.sdk.bar"));
      assertFalse(matcher.isMatch("foo.foo.sdk.bar"));

      matcher = PathMatcher.parsePackagePatterns("*.sdk.*,!*.sdk.*");

      assertFalse(matcher.isMatch("foo.sdk.bar"));
      assertFalse(matcher.isMatch("foo.foo.sdk.bar"));
   }

   public void testNull() throws Exception
   {
      try
      {
         PathMatcher.parsePackagePatterns(null);
         fail();
      }
      catch (NullPointerException e)
      {
      }
   }

   public void testEmpty() throws Exception
   {
      PathMatcher matcher = PathMatcher.parsePackagePatterns("");
      assertTrue(matcher.getExcludes().isEmpty());
      assertTrue(matcher.getIncludes().isEmpty());

      assertTrue(matcher.isMatch("foo.sdk.bar"));
      assertTrue(matcher.isMatch(""));
      assertTrue(matcher.isMatch("bar"));
   }

   public void testAll() throws Exception
   {
      PathMatcher matcher = PathMatcher.parsePackagePatterns("**");
      assertTrue(matcher.getExcludes().isEmpty());
      assertEquals(1, matcher.getIncludes().size());

      assertTrue(matcher.isMatch("foo.sdk.bar"));
      assertTrue(matcher.isMatch(""));
      assertTrue(matcher.isMatch("bar"));
   }
}
