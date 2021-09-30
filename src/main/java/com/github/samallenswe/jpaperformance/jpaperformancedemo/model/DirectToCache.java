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
    entityManager.unwrap(Session.class).setHibernateFlushMode(FlushMode.MANUAL);
//    writeToCache();
  }

  public void writeToCache() {
    Logger log = Logger.getLogger("################################## writeToCache");
    long startTime = System.nanoTime();
    for (int i = 1; i < TEST_SIZE; i++) {
      Person person = Utils.createRandomPerson(i);
      entityManager.persist(person);
    }
    long endTime = System.nanoTime();
    log.info("Persisting all Entities took: " + TimeUnit.NANOSECONDS.toMillis(endTime - startTime));

    startTime = System.nanoTime();
    entityManager.flush();
    endTime = System.nanoTime();
    log.info("Flushing all Entities took: " + TimeUnit.NANOSECONDS.toMillis(endTime - startTime));
  }

  public DirectToCache(@NonNull final EntityManager entityManager) {
    if (entityManager == null) {
      throw new NullPointerException("entityManager is marked non-null but is null");
    } else {
      this.entityManager = entityManager;
    }
  }
}
