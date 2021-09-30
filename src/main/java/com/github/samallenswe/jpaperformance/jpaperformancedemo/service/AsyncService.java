package com.github.samallenswe.jpaperformance.jpaperformancedemo.service;

import com.github.samallenswe.jpaperformance.jpaperformancedemo.domain.repository.PersonRepository;
import java.util.concurrent.Semaphore;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AsyncService {
  @NonNull
  @Autowired
  private PersonRepository repository;

  @Async
  @Transactional
  public void asyncFlush(EntityManager entityManager, Semaphore semaphore) {
    try {
        Thread.sleep(50,0);
      } catch(InterruptedException ex) {
        Thread.currentThread().interrupt();
      }

    while(true) {
      try {
        semaphore.acquire();
        try {
          entityManager.flush();
        } finally {
          semaphore.release();
        }
      } catch(final InterruptedException ie) {
        // handle acquire failure here
      }

      try {
        Thread.sleep(5000,0);
      } catch(InterruptedException ex) {
        Thread.currentThread().interrupt();
      }
    }
  }

  public AsyncService(@NonNull final PersonRepository repository) {
    if (repository == null) {
      throw new NullPointerException("repository is marked non-null but is null");
    } else {
      this.repository = repository;
    }
  }
}
