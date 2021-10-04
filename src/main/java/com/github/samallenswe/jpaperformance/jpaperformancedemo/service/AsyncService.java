package com.github.samallenswe.jpaperformance.jpaperformancedemo.service;

import com.github.samallenswe.jpaperformance.jpaperformancedemo.domain.repository.PersonRepository;
import com.github.samallenswe.jpaperformance.jpaperformancedemo.model.AsyncCacheAndDb;
import java.util.concurrent.Semaphore;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.lang.NonNull;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AsyncService {
  @NonNull
  @Autowired
  private PersonRepository repository;

  @NonNull
  @Lazy
  private AsyncCacheAndDb runner;

  @Async
  public void asyncFlush(Semaphore semaphore) {
    while(!Thread.currentThread().isInterrupted() && runner.exit == false) {
      try {
        semaphore.acquire();
        try {
          System.out.println("############################### ASYNC THREAD ###############################");
          runner.flushHelper();
        } finally {
          semaphore.release();
        }
      } catch(final InterruptedException ie) {
        System.out.println(ie.toString());
      }
      try {
        Thread.sleep(50,0);
      } catch(InterruptedException ex) {
        Thread.currentThread().interrupt();
      }
    }
    if (runner.exit == false) {
      Thread.currentThread().interrupt();
    }
  }

  public AsyncService(@NonNull final PersonRepository repository, @NonNull @Lazy AsyncCacheAndDb runner) {
    if (repository == null) {
      throw new NullPointerException("repository is marked non-null but is null");
    } else {
      this.repository = repository;
    }
    if (runner == null) {
      throw new NullPointerException("asyncService is marked non-null but is null");
    } else {
      this.runner = runner;
    }
  }
}
