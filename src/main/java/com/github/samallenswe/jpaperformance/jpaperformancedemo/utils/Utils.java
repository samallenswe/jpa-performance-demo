package com.github.samallenswe.jpaperformance.jpaperformancedemo.utils;

import com.github.samallenswe.jpaperformance.jpaperformancedemo.domain.Person;

public class Utils {
  public static final int TEST_SIZE = 500;
  public static final int POOL_SIZE = 5;

  public static Person createRandomPerson(int i) {
    Person person = new Person();
    person.setFirstName("firstName" + i);
    person.setLastName("lastName" + i);
    person.setEmail(person.getFirstName() + "." + person.getLastName() + "@xyz.com");
    return person;
  }
}
