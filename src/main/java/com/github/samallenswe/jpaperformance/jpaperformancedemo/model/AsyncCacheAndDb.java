package com.github.samallenswe.jpaperformance.jpaperformancedemo.model;

import static com.github.samallenswe.jpaperformance.jpaperformancedemo.utils.Utils.TEST_SIZE;

import com.github.samallenswe.jpaperformance.jpaperformancedemo.domain.Person;
import com.github.samallenswe.jpaperformance.jpaperformancedemo.domain.repository.PersonRepository;
import com.github.samallenswe.jpaperformance.jpaperformancedemo.service.AsyncService;
import com.github.samallenswe.jpaperformance.jpaperformancedemo.utils.Utils;
import java.util.concurrent.Executor;
import java.util.concurrent.Future;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import org.apache.catalina.mbeans.UserMBean;
import org.hibernate.FlushMode;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Order(3)
@Transactional
@Component
public class AsyncCacheAndDb implements CommandLineRunner {
  @PersistenceContext(type = PersistenceContextType.EXTENDED)
  @NonNull
  private EntityManager entityManager;

  @NonNull
  @Autowired
  private PersonRepository repository;

  @NonNull
  private AsyncService asyncService;

  public static boolean exit = false;

  static int asyncFlushTime = 0;

  @Override
  public void run(String... args) throws Exception {
    entityManager.unwrap(Session.class).setJdbcBatchSize(10);
    entityManager.unwrap(Session.class).setHibernateFlushMode(FlushMode.MANUAL);
//    persistToCacheBatchFlush();
    Semaphore semaphore = new Semaphore(1);
    asyncService.asyncFlush(semaphore);
    persistToCacheNoFlush(semaphore);
  }
  @Transactional
  public void flushHelper () {
    Logger log = Logger.getLogger("################################## persistToCacheNoFlush");
    long startTime = System.nanoTime();
    entityManager.flush();
    long endTime = System.nanoTime();
    log.info("Flushing managed entities took: " + TimeUnit.NANOSECONDS.toMillis(endTime - startTime));
    asyncFlushTime = asyncFlushTime+(int)(TimeUnit.NANOSECONDS.toMillis(endTime - startTime));
    entityManager.clear();
  }

  public void persistToCacheNoFlush(Semaphore semaphore) {
    Logger log = Logger.getLogger("################################## persistToCacheNoFlush");
    long startTime = System.nanoTime();
    for (int i = 1; i < TEST_SIZE*2; i++) {
      System.out.println("############################### PERSIST THREAD ###############################");
      Person person = Utils.createRandomPerson(i);
      try {
        semaphore.acquire();
        try {
          System.out.println("PERSON # " + i);
          entityManager.persist(person);
        } finally {
          semaphore.release();
        }
      } catch(final InterruptedException ie) {
        System.out.println(ie.toString());
      }
    }
    long endTime = System.nanoTime();
    log.info("Persisting all Entities took: " + TimeUnit.NANOSECONDS.toMillis(endTime - startTime));

    try {
      Thread.sleep(100,0);
    } catch(InterruptedException ex) {
      Thread.currentThread().interrupt();
    }
    exit = true;
    log.info("FLUSHING ALL ENTITIES TOOK: " + asyncFlushTime);
  }

  @Transactional
  public void persistToCacheBatchFlush() {
    Logger log = Logger.getLogger("################################## persistToCacheBatchFlush");
    long startTime = System.nanoTime();
    for (int i = 1; i < TEST_SIZE*2; i++) {
      Person person = Utils.createRandomPerson(i);
      entityManager.persist(person);
    }
    entityManager.flush();
    long endTime = System.nanoTime();
    log.info("Persisting and batch flushing all Entities took: " + TimeUnit.NANOSECONDS.toMillis(endTime - startTime));
  }

  public AsyncCacheAndDb(@NonNull final EntityManager entityManager, @NonNull final PersonRepository repository,
      @NonNull AsyncService asyncService) {
    if (entityManager == null) {
      throw new NullPointerException("entityManager is marked non-null but is null");
    } else {
      this.entityManager = entityManager;
    }
    if (repository == null) {
      throw new NullPointerException("repository is marked non-null but is null");
    } else {
      this.repository = repository;
    }
    if (asyncService == null) {
      throw new NullPointerException("asyncService is marked non-null but is null");
    } else {
      this.asyncService = asyncService;
    }
  }
}
