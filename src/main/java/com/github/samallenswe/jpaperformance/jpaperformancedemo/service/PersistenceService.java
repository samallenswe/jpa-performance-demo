package com.github.samallenswe.jpaperformance.jpaperformancedemo.service;

import static  com.github.samallenswe.jpaperformance.jpaperformancedemo.utils.Utils.TEST_SIZE;

import com.github.samallenswe.jpaperformance.jpaperformancedemo.domain.Person;
import com.github.samallenswe.jpaperformance.jpaperformancedemo.domain.repository.PersonRepository;
import com.github.samallenswe.jpaperformance.jpaperformancedemo.utils.Utils;
import java.util.Scanner;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PersistenceService {
  @PersistenceContext
  @NonNull
  private EntityManager entityManager;

  @NonNull
  private PersonRepository repository;

  public void savePersons() {
    for (int i = 0; i < TEST_SIZE; i++) {
      Person person = Utils.createRandomPerson(i + 1);
      repository.save(person);
    }
  }

  public void deletePersons() {
    //Scanner scan = new Scanner(System.in);
    //scan.nextLine();
    repository.deleteAll();
  }

  @Transactional
  public void writeToH2DB() {
    for (int i = 1; i < TEST_SIZE; i++) {
      Person person = Utils.createRandomPerson(i);
      entityManager.persist(person);
    }
  }

  @Transactional
  public void readFromH2DB() {
    for (int i = 1; i < TEST_SIZE; i++) {
        Person person = entityManager.find(Person.class, (long) i);
        if (person == null) {
          throw new NullPointerException("person entity is null");
        } else {
          System.out.println(("Found person " + i + " with " + person.getEmail()));
        }
    }
  }
  public PersistenceService(@NonNull final EntityManager entityManager) {
    if (entityManager == null) {
      throw new NullPointerException("entityManager is marked non-null but is null");
    } else {
      this.entityManager = entityManager;
    }
  }
//  public PersistenceService(@NonNull final PersonRepository repository, @NonNull final EntityManager entityManager) {
//    if (entityManager == null) {
//      throw new NullPointerException("entityManager is marked non-null but is null");
//    } else {
//      this.entityManager = entityManager;
//    }
//    if (repository == null) {
//      throw new NullPointerException("repository is marked non-null but is null");
//    } else {
//      this.repository = repository;
//    }
//  }
}
