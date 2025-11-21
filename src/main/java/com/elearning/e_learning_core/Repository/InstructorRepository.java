package com.elearning.e_learning_core.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.elearning.e_learning_core.model.Instructor;

@Repository
public interface InstructorRepository extends JpaRepository<Instructor, Long> {

    Instructor findByEmail(String email);

    Instructor findByUserName(String userName);

}
