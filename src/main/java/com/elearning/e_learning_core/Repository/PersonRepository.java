package com.elearning.e_learning_core.Repository;

import com.elearning.e_learning_core.model.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonRepository extends JpaRepository<Person, Long> {
    boolean existsByEmail(String email);

    Person findByEmail(String email);

}