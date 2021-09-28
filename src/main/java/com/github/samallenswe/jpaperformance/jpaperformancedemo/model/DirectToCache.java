package com.github.samallenswe.jpaperformance.jpaperformancedemo.model;

import static com.github.samallenswe.jpaperformance.jpaperformancedemo.utils.Utils.TEST_SIZE;

import com.github.samallenswe.jpaperformance.jpaperformancedemo.domain.Person;
import com.github.samallenswe.jpaperformance.jpaperformancedemo.utils.Utils;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.hibernate.FlushMode;
import org.hibernate.Session;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Order(2)
@Transactional
@Component
public class DirectToCache implements CommandLineRunner {
  @PersistenceContext
  private EntityManager entityManager;

  @Override
  public void run(String... args) throws Exception {
    Logger log = Logger.getLogger("DirectToCache");
    entityManager.unwrap(Session.class).setHibernateFlushMode(FlushMode.MANUAL);
    long startTime = System.nanoTime();
//    writeToCache();
    long endTime = System.nanoTime();
    log.info("Asynchronous Batch Write took: " + TimeUnit.NANOSECONDS.toMillis(endTime - startTime));
  }

  public void writeToCache() {
    for (int i = 1; i < TEST_SIZE; i++) {
      Person person = Utils.createRandomPerson(i);
      entityManager.persist(person);
    }
  }

//  @Transactional
//  public void readFromH2DB() {
//    for (int i = 1; i < TEST_SIZE; i++) {
//      Person person = entityManager.find(Person.class, (long) i);
//      if (person == null) {
//        throw new NullPointerException("person entity is null");
//      } else {
//        System.out.println(("Found person " + i + " with " + person.getEmail()));
//      }
//    }
//  }

  public DirectToCache(@NonNull final EntityManager entityManager) {
    if (entityManager == null) {
      throw new NullPointerException("entityManager is marked non-null but is null");
    } else {
      this.entityManager = entityManager;
    }
  }
}
