package com.trancha.demo.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.trancha.demo.model.Person;
public interface PersonRepository extends JpaRepository<Person, Long> {
  List<Person> findByFirstname(String firstname);
  List<Person> findByBirthdateContaining(String birthdate);
}
