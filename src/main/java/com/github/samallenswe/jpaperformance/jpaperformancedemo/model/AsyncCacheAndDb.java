package com.github.samallenswe.jpaperformance.jpaperformancedemo.model;

import static com.github.samallenswe.jpaperformance.jpaperformancedemo.utils.Utils.TEST_SIZE;

import com.github.samallenswe.jpaperformance.jpaperformancedemo.domain.Person;
import com.github.samallenswe.jpaperformance.jpaperformancedemo.domain.repository.PersonRepository;
import com.github.samallenswe.jpaperformance.jpaperformancedemo.service.AsyncService;
import com.github.samallenswe.jpaperformance.jpaperformancedemo.utils.Utils;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CancellationException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TransactionRequiredException;
import org.hibernate.FlushMode;
import org.hibernate.Session;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.lang.NonNull;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.config.Task;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Order(3)
@Transactional
@Component
public class AsyncCacheAndDb implements CommandLineRunner {
  @PersistenceContext
  @NonNull
  private EntityManager entityManager;

  @NonNull
  private PersonRepository repository;

  @NonNull
  private AsyncService asyncService;

  @Override
  public void run(String... args) throws Exception {
    entityManager.unwrap(Session.class).setJdbcBatchSize(10);
    entityManager.unwrap(Session.class).setHibernateFlushMode(FlushMode.MANUAL);

    CompletableFuture<Void> asyncFlushFuture = asyncService.asyncFlush();
    persistToCache();

    CompletableFuture.allOf(asyncFlushFuture).join();

//    asyncFlush();

//    writeToCacheBatch();

//    ExecutorService executorService = Executors.newFixedThreadPool(2);
//
//    List<Callable<String>> callableTasks = new ArrayList<>();
//    callableTasks.add(batchWriteToH2DB(executorService));
//    callableTasks.add(asyncFlush());
//
//    try {
//      List<Future<String>> futures = executorService.invokeAll(callableTasks);
//      for (Future<String> future : futures) {
//        try {
//          if (future.isDone()) {
//            System.out.println("future: call = " + future.get()); // This probably triggers the error. Because the process is not done before its called
//          }
//        } catch (CancellationException ce) {
//          ce.printStackTrace();
//        } catch (InterruptedException ie) {
//          Thread.currentThread().interrupt(); // ignore/reset
//        }
//      }
//    } catch (Exception err) {
//      err.printStackTrace();
//    }
//
//    awaitTerminationAfterShutdown(executorService);
  }

//  @Transactional
//  public void writeToCacheBatch() {
//    for (int i = 1; i < TEST_SIZE; i++) {
//      Person person = Utils.createRandomPerson(i);
//      entityManager.persist(person);
//    }
//    entityManager.flush();
//  }

//  public void awaitTerminationAfterShutdown(ExecutorService threadPool) {
//    threadPool.shutdown();
//    try {
//      if (!threadPool.awaitTermination(60, TimeUnit.SECONDS)) {
//        threadPool.shutdownNow();
//      }
//    } catch (InterruptedException ex) {
//      threadPool.shutdownNow();
//      Thread.currentThread().interrupt();
//    }
//  }

//  @Async
//  @Transactional
//  public void asyncFlush() {
//    Logger log = Logger.getLogger("################################## asyncFlush");
//    long startTime = System.nanoTime();
//
//    for (int i = 0; i < 5; i++) {
//        System.out.println("############################### ASYNC THREAD ###############################");
//        try {
//          entityManager.flush();
//          try {
//            Thread.sleep(0,100000);
//          } catch(InterruptedException ex) {
//            Thread.currentThread().interrupt();
//          }
//        } catch (TransactionRequiredException ex) {
//          // expected
//        }
//      }
//    long endTime = System.nanoTime();
//    log.info("Flushing all batches took: " + (endTime - startTime));
//  }

  public void persistToCache() {
    Logger log = Logger.getLogger("################################## batchWriteToH2DB");
    long startTime = System.nanoTime();
    for (int i = 1; i < TEST_SIZE; i++) {
      System.out.println("############################### PERSIST THREAD ###############################");
      Person person = Utils.createRandomPerson(i);
      entityManager.persist(person);
    }
    long endTime = System.nanoTime();
    log.info("Persisting all Entities took: " + (endTime - startTime));
  }


//  @Async
//  @Transactional
//  public Callable asyncFlush() {
//    Logger log = Logger.getLogger("################################## asyncFlush");
//    long startTime = System.nanoTime();
//
//    for (int i = 0; i < 1; i++) {
//        try {
//          entityManager.flush();
//          try {
//            Thread.sleep(0,100000);
//          } catch(InterruptedException ex) {
//            Thread.currentThread().interrupt();
//          }
//        } catch (TransactionRequiredException ex) {
//          // expected
//        }
//      }
//    long endTime = System.nanoTime();
//    log.info("Flushing all batches took: " + (endTime - startTime));
//    return null;
//  }
//
//  @Async
//  public Callable batchWriteToH2DB(ExecutorService executorService) {
//    Logger log = Logger.getLogger("################################## batchWriteToH2DB");
//    long startTime = System.nanoTime();
//    for (int i = 1; i < TEST_SIZE; i++) {
//      Person person = Utils.createRandomPerson(i);
//      entityManager.persist(person);
//    }
//    long endTime = System.nanoTime();
//    log.info("Persisting all Entities took: " + (endTime - startTime));
//    return null;
//  }

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
