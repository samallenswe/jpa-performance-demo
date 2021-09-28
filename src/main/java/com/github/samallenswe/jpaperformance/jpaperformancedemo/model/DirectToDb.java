package com.github.samallenswe.jpaperformance.jpaperformancedemo.model;

import static com.github.samallenswe.jpaperformance.jpaperformancedemo.utils.Utils.TEST_SIZE;

import com.github.samallenswe.jpaperformance.jpaperformancedemo.domain.Person;
import com.github.samallenswe.jpaperformance.jpaperformancedemo.domain.repository.PersonRepository;
import com.github.samallenswe.jpaperformance.jpaperformancedemo.utils.Utils;
import java.util.Scanner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

@Order(1)
@Component
public class DirectToDb implements CommandLineRunner {
  @NonNull
  private PersonRepository repository;

  @Override
  public void run(String... args) throws Exception {
//    savePersons();
//    deletePersons();
  }

  public void savePersons() {
    for (int i = 1; i < TEST_SIZE; i++) {
      Person person = Utils.createRandomPerson(i);
      repository.save(person);
    }
  }

  public void deletePersons() {
//    Scanner scan = new Scanner(System.in);
//    scan.nextLine();
    repository.deleteAll();
  }

  public DirectToDb(@NonNull final PersonRepository repository) {
    if (repository == null) {
      throw new NullPointerException("persistenceService is marked non-null but is null");
    } else {
      this.repository = repository;
    }
  }
}
