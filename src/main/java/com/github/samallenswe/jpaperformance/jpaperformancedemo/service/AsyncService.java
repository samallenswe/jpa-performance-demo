package com.github.samallenswe.jpaperformance.jpaperformancedemo.service;

import com.github.samallenswe.jpaperformance.jpaperformancedemo.domain.Person;
import com.github.samallenswe.jpaperformance.jpaperformancedemo.model.AsyncCacheAndDb;
import java.util.List;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TransactionRequiredException;
import org.hibernate.FlushMode;
import org.hibernate.Session;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.NonNull;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.CompletableFuture;

@Service
public class AsyncService {
  @PersistenceContext
  @NonNull
  private EntityManager entityManager;

  @Async
  @Transactional
  public CompletableFuture<Void> asyncFlush() {
//    entityManager.unwrap(Session.class).setJdbcBatchSize(10);
//    entityManager.unwrap(Session.class).setHibernateFlushMode(FlushMode.MANUAL);

    try {
        Thread.sleep(50,0);
      } catch(InterruptedException ex) {
        Thread.currentThread().interrupt();
      }

    java.util.logging.Logger log = Logger.getLogger("################################## asyncFlush");
    long startTime = System.nanoTime();
    entityManager.flush();
//    for (int i = 0; i < 10; i++) {
////      try {
////        entityManager.flush();
////        System.out.println("############################### ASYNC THREAD ###############################");
////        try {
////          Thread.sleep(1,0);
////        } catch(InterruptedException ex) {
////          Thread.currentThread().interrupt();
////        }
////      } catch (TransactionRequiredException ex) {
////        // expected
////      }
//    }
    long endTime = System.nanoTime();
    log.info("Flushing all batches took: " + (endTime - startTime));

    return null;
  }

  public AsyncService(@NonNull final EntityManager entityManager) {
    if (entityManager == null) {
      throw new NullPointerException("entityManager is marked non-null but is null");
    } else {
      this.entityManager = entityManager;
    }
  }
}
