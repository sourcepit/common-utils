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
