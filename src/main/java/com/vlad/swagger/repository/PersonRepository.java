package com.vlad.swagger.repository;

import com.vlad.swagger.entity.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface PersonRepository extends JpaRepository<Person, Integer> {
    Person findByName(String name);
}
