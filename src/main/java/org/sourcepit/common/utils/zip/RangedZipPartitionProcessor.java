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

package org.sourcepit.common.utils.zip;

import java.util.zip.ZipEntry;

public class RangedZipPartitionProcessor extends SingleZipPartitionProcessor
{
   private final int startIdx, endIdx;
   private Boolean unzip;

   public RangedZipPartitionProcessor(ZipInputStreamFactory streamFactory, ZipEntryHandler entryHandler, int startIdx,
      int endIdx)
   {
      super(streamFactory, entryHandler);
      this.startIdx = startIdx;
      this.endIdx = endIdx;
   }

   protected boolean isUnzip(ZipEntry zipEntry, int zipEntryIdx)
   {
      if (unzip == null)
      {
         if (zipEntryIdx == startIdx)
         {
            unzip = Boolean.TRUE;
         }
      }
      if (Boolean.TRUE.equals(unzip))
      {
         if (zipEntryIdx == endIdx)
         {
            unzip = Boolean.FALSE;
         }
         return true;
      }
      return false;
   }
}