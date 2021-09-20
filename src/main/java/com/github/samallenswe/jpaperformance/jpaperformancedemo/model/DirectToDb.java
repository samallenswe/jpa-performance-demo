package com.github.samallenswe.jpaperformance.jpaperformancedemo.model;

import com.github.samallenswe.jpaperformance.jpaperformancedemo.service.PersistenceService;
import java.util.Scanner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

@Order(1)
@Component
public class DirectToDb implements CommandLineRunner {
  @NonNull
  private PersistenceService persistenceService;

  @Override
  public void run(String... args) throws Exception {
    persistenceService.savePersons();
    persistenceService.deletePersons();
    //persistenceService.writeToH2DB();
    //persistenceService.readFromH2DB();
  }

  public DirectToDb(@NonNull final PersistenceService persistenceService) {
    if (persistenceService == null) {
      throw new NullPointerException("persistenceService is marked non-null but is null");
    } else {
      this.persistenceService = persistenceService;
    }
  }
}
