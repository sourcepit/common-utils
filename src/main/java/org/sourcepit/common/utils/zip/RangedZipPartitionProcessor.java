/**
 * Copyright (c) 2011 Sourcepit.org contributors and others. All rights reserved. This program and the accompanying
 * materials are made available under the terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
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