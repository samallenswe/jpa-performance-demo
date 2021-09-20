package com.github.samallenswe.jpaperformance.jpaperformancedemo.domain.repository;

import com.github.samallenswe.jpaperformance.jpaperformancedemo.domain.Person;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

@Repository
@Component
public interface PersonRepository extends CrudRepository<Person, Long> {
}
