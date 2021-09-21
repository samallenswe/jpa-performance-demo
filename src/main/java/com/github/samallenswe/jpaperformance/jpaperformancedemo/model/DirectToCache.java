package com.github.samallenswe.jpaperformance.jpaperformancedemo.model;

import com.github.samallenswe.jpaperformance.jpaperformancedemo.service.PersistenceService;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

@Order(2)
@Component
public class DirectToCache implements CommandLineRunner {
  @PersistenceContext
  private EntityManager entityManager;

  @NonNull
  private PersistenceService persistenceService;

  @Override
  public void run(String... args) throws Exception {
    //persistenceService.writeToH2DB();
    //persistenceService.readFromH2DB();
  }

  public DirectToCache(@NonNull final PersistenceService persistenceService) {
    if (persistenceService == null) {
      throw new NullPointerException("persistenceService is marked non-null but is null");
    } else {
      this.persistenceService = persistenceService;
    }
  }
}
