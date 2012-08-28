/**
 * Copyright (c) 2011 Sourcepit.org contributors and others. All rights reserved. This program and the accompanying
 * materials are made available under the terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.sourcepit.common.utils.zip;

import java.util.Set;
import java.util.zip.ZipEntry;

public class IndexBasedZipPartitionProcessor extends SingleZipPartitionProcessor
{
   private Set<Integer> work;

   public IndexBasedZipPartitionProcessor(ZipInputStreamFactory streamFactory, ZipEntryHandler entryHandler,
      Set<Integer> work)
   {
      super(streamFactory, entryHandler);
      this.work = work;
   }

   @Override
   protected boolean isUnzip(ZipEntry zipEntry, int zipEntryIdx)
   {
      return !work.isEmpty() && work.remove(Integer.valueOf(zipEntryIdx));
   }

}