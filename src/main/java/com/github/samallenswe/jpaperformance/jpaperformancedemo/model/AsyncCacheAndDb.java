package com.github.samallenswe.jpaperformance.jpaperformancedemo.model;

import static com.github.samallenswe.jpaperformance.jpaperformancedemo.utils.Utils.TEST_SIZE;

import com.github.samallenswe.jpaperformance.jpaperformancedemo.domain.Person;
import com.github.samallenswe.jpaperformance.jpaperformancedemo.service.PersistenceService;
import com.github.samallenswe.jpaperformance.jpaperformancedemo.utils.Utils;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Order(3)
@Component
public class AsyncCacheAndDb implements CommandLineRunner {
  @PersistenceContext
  @NonNull
  private EntityManager entityManager;

  @Override
  public void run(String... args) throws Exception {
    writeToH2DB();
  }

  @Transactional
  public void writeToH2DB() {
    for (int i = 1; i < TEST_SIZE; i++) {
      Person person = Utils.createRandomPerson(i);
      this.entityManager.persist(person);
    }
  }

  public AsyncCacheAndDb(@NonNull final EntityManager entityManager) {
    if (entityManager == null) {
      throw new NullPointerException("entityManager is marked non-null but is null");
    } else {
      this.entityManager = entityManager;
    }
  }
}
