/**
 * Copyright (c) 2011 Sourcepit.org contributors and others. All rights reserved. This program and the accompanying
 * materials are made available under the terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.sourcepit.common.utils.zip;

import static org.junit.Assert.assertThat;

import org.hamcrest.core.Is;
import org.junit.Test;

/**
 * EntryCountBasedZipPartitionerTest
 * 
 * @author Bernd
 */
public class EntryCountBasedZipPartitionerTest
{

   @Test
   public void test()
   {
      EntryCountBasedZipPartitioner partitioner = new EntryCountBasedZipPartitioner();

      int numberOfPartitions = partitioner.computeNumberOfPartitions(-1, -1, 1);
      assertThat(numberOfPartitions, Is.is(1));

      numberOfPartitions = partitioner.computeNumberOfPartitions(-1, -1, 0);
      assertThat(numberOfPartitions, Is.is(1));

      numberOfPartitions = partitioner.computeNumberOfPartitions(8, -1, 1);
      assertThat(numberOfPartitions, Is.is(1));

      numberOfPartitions = partitioner.computeNumberOfPartitions(8, 8 * 60 * 1000, 7 * 10);
      assertThat(numberOfPartitions, Is.is(8));

      numberOfPartitions = partitioner.computeNumberOfPartitions(8, 8 * 80 * 1000, 7 * 100);
      assertThat(numberOfPartitions, Is.is(12));

      numberOfPartitions = partitioner.computeNumberOfPartitions(8, 8 * 1024 * 1000, 2);
      assertThat(numberOfPartitions, Is.is(2));

      numberOfPartitions = partitioner.computeNumberOfPartitions(8, 256 * 100, 20);
      assertThat(numberOfPartitions, Is.is(1));

      numberOfPartitions = partitioner.computeNumberOfPartitions(8, 2048 * 100, 200);
      assertThat(numberOfPartitions, Is.is(8));

      numberOfPartitions = partitioner.computeNumberOfPartitions(8, Integer.MAX_VALUE, Integer.MAX_VALUE);
      assertThat(numberOfPartitions, Is.is(16));

      numberOfPartitions = partitioner.computeNumberOfPartitions(2, Integer.MAX_VALUE, Integer.MAX_VALUE);
      assertThat(numberOfPartitions, Is.is(4));
   }

}
