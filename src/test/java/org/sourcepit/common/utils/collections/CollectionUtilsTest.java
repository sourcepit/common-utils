/**
 * Copyright (c) 2012 Sourcepit.org contributors and others. All rights reserved. This program and the accompanying
 * materials are made available under the terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.sourcepit.common.utils.collections;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.hamcrest.core.IsEqual;
import org.hamcrest.core.IsNull;
import org.junit.Test;

/**
 * @author Bernd Vogt <bernd.vogt@sourcepit.org>
 */
public class CollectionUtilsTest
{
   @Test
   public void testGetValue()
   {
      try
      {
         CollectionUtils.getValue(null, null);
         fail();
      }
      catch (IllegalArgumentException e)
      {
      }

      try
      {
         CollectionUtils.getValue(new ArrayList<Object>(), null);
         fail();
      }
      catch (IllegalArgumentException e)
      {
      }

      List<Person> persons = new ArrayList<Person>();
      persons.add(new Person("Sascha", 1));
      persons.add(new Person("Robert", 2));
      persons.add(new Person("Sebastian", 3));

      assertThat(CollectionUtils.getValue(persons, new AgeLookup("Sascha")), IsEqual.equalTo(1));
      assertThat(CollectionUtils.getValue(persons, new AgeLookup("Robert")), IsEqual.equalTo(2));
      assertThat(CollectionUtils.getValue(persons, new AgeLookup("Sebastian")), IsEqual.equalTo(3));
      assertThat(CollectionUtils.getValue(persons, new AgeLookup("Bernd")), IsNull.nullValue());
      
   }

   /**
    * @author Bernd Vogt <bernd.vogt@sourcepit.org>
    */
   private final class AgeLookup implements ValueLookup<Person, Integer>
   {
      private String name;

      public AgeLookup(String name)
      {
         this.name = name;
      }

      public Integer lookup(Person element)
      {
         if (name.equals(element.getName()))
         {
            return Integer.valueOf(element.getAge());
         }
         return null;
      }
   }

   private static class Person
   {
      private String name;
      private int age;

      public Person(String name, int age)
      {
         this.name = name;
         this.age = age;
      }

      public String getName()
      {
         return name;
      }

      public int getAge()
      {
         return age;
      }
   }
}
