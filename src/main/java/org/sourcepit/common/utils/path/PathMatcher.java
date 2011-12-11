/**
 * Copyright (c) 2011 Sourcepit.org contributors and others. All rights reserved. This program and the accompanying
 * materials are made available under the terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.sourcepit.common.utils.path;

import java.io.File;
import java.util.HashSet;
import java.util.Set;
import java.util.StringTokenizer;

import org.apache.commons.io.FilenameUtils;

public class PathMatcher
{
   private static class PatternParser
   {
      private String patternString;
      private String patternSeparator;
      private String segmentSeparator;

      public void setPatternString(String patterns)
      {
         this.patternString = patterns;
      }

      public void setPatternSeparator(String pathSeparator)
      {
         this.patternSeparator = pathSeparator;
      }

      public void setSegmentSeparator(String segmentSeparator)
      {
         this.segmentSeparator = segmentSeparator;
      }

      public PathMatcher parse()
      {
         final Set<String> includes = new HashSet<String>();
         final Set<String> excludes = new HashSet<String>();
         if (patternString.length() != 0)
         {
            final String[] rawPatterns = split();
            for (String rawPattern : rawPatterns)
            {
               rawPattern = rawPattern.trim();
               final boolean isExclude = isExclude(rawPattern);
               final Set<String> target = isExclude ? excludes : includes;
               target.add(toSegmentRegex(prepareRawPattern(rawPattern, isExclude), segmentSeparator));
            }
         }
         return createMacher(includes, excludes);
      }

      protected boolean isExclude(String rawPattern)
      {
         return rawPattern.startsWith("!");
      }

      protected String[] split()
      {
         if (patternSeparator == null)
         {
            return new String[] {patternString};
         }
         else
         {
            return patternString.split(patternSeparator);
         }
      }

      protected String prepareRawPattern(String rawPattern, boolean isExclude)
      {
         if (isExclude)
         {
            rawPattern = rawPattern.length() > 1 ? rawPattern.substring(1) : "";
         }
         return rawPattern;
      }

      protected PathMatcher createMacher(final Set<String> includes, final Set<String> excludes)
      {
         return new PathMatcher(includes, excludes);
      }
   }

   private final Set<String> includes;
   private final Set<String> excludes;

   public PathMatcher(Set<String> includes, Set<String> excludes)
   {
      this.includes = includes;
      this.excludes = excludes;
   }

   public static PathMatcher parsePackagePatterns(String patterns)
   {
      final PatternParser parser = new PatternParser();
      parser.setPatternString(patterns);
      parser.setPatternSeparator(",");
      parser.setSegmentSeparator(".");
      return parser.parse();
   }

   public static PathMatcher parseFilePatterns(final File baseDir, String filePatterns)
   {
      final PatternParser parser = new PatternParser()
      {
         protected String prepareRawPattern(String rawPattern, boolean isExclude)
         {
            String result = super.prepareRawPattern(rawPattern, isExclude);
            if (!result.startsWith("**") && !new File(result).isAbsolute())
            {
               result = new File(baseDir, result).getAbsolutePath();
            }
            result = FilenameUtils.separatorsToUnix(result);
            return result;
         };

         @Override
         protected PathMatcher createMacher(Set<String> includes, Set<String> excludes)
         {
            return new PathMatcher(includes, excludes)
            {
               @Override
               public boolean isMatch(String path)
               {
                  return super.isMatch(FilenameUtils.separatorsToUnix(new File(path).getAbsolutePath()));
               }
            };
         }
      };
      parser.setPatternString(FilenameUtils.separatorsToUnix(filePatterns));
      parser.setPatternSeparator(",");
      parser.setSegmentSeparator("/");
      return parser.parse();
   }

   public static PathMatcher parse(String patterns, String segmentSeparator, String patternSeparator)
   {
      final PatternParser parser = new PatternParser();
      parser.setPatternString(patterns);
      parser.setPatternSeparator(patternSeparator);
      parser.setSegmentSeparator(segmentSeparator);
      return parser.parse();
   }

   public Set<String> getExcludes()
   {
      return excludes;
   }

   public Set<String> getIncludes()
   {
      return includes;
   }

   public boolean isInclude(String path)
   {
      return isPatternMatch(includes, path, true);
   }

   public boolean isExclude(String path)
   {
      return isPatternMatch(excludes, path, false);
   }

   public boolean isMatch(String path)
   {
      return isInclude(path) && !isExclude(path);
   }

   private boolean isPatternMatch(Set<String> patterns, String path, boolean defaultValue)
   {
      if (patterns != null && !patterns.isEmpty())
      {
         for (String exclude : patterns)
         {
            if (path.matches(exclude))
            {
               return true;
            }
         }
         return false;
      }
      return defaultValue;
   }

   public static String toSegmentRegex(String pattern, String separator)
   {
      final String segmentPattern = "[^" + escRegEx(separator) + "]*";
      final int segmentPatternLength = segmentPattern.length();
      final StringTokenizer t = new StringTokenizer(pattern, "\\*", true);
      final StringBuilder sb = new StringBuilder();
      boolean skip = false;
      while (t.hasMoreTokens())
      {
         String token = t.nextToken();
         if ("*".equals(token))
         {
            if (skip)
            {
               sb.delete(sb.length() - segmentPatternLength, sb.length());
               sb.append(".*");
            }
            else
            {
               sb.append(segmentPattern);
            }
            skip = true;
         }
         else
         {
            sb.append(escRegEx(token));
            skip = false;
         }
      }
      return sb.toString();
   }

   public static String escRegEx(String inStr)
   {
      // will replace the char with \char
      return inStr.replaceAll("([\\\\*+\\[\\](){}\\$.?\\^|])", "\\\\$1");
   }
}
