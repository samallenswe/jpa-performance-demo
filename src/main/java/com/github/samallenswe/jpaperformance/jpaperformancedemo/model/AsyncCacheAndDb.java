package com.github.samallenswe.jpaperformance.jpaperformancedemo.model;

import static com.github.samallenswe.jpaperformance.jpaperformancedemo.utils.Utils.TEST_SIZE;

import com.github.samallenswe.jpaperformance.jpaperformancedemo.domain.Person;
import com.github.samallenswe.jpaperformance.jpaperformancedemo.domain.repository.PersonRepository;
import com.github.samallenswe.jpaperformance.jpaperformancedemo.service.PersistenceService;
import com.github.samallenswe.jpaperformance.jpaperformancedemo.utils.Utils;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.hibernate.Session;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Order(3)
@Transactional
@Component
public class AsyncCacheAndDb implements CommandLineRunner {
  @PersistenceContext
  @NonNull
  private EntityManager entityManager;

  @Override
  public void run(String... args) throws Exception {
    batchWriteToH2DB();
  }

  @Transactional
  public void batchWriteToH2DB() {
    entityManager.unwrap(Session.class).setJdbcBatchSize(10);
    for (int i = 1; i < TEST_SIZE; i++) {
      Person person = Utils.createRandomPerson(i);
      entityManager.persist(person);
    }
    entityManager.flush();
  }

  public AsyncCacheAndDb(@NonNull final EntityManager entityManager) {
    if (entityManager == null) {
      throw new NullPointerException("entityManager is marked non-null but is null");
    } else {
      this.entityManager = entityManager;
    }
  }
}
