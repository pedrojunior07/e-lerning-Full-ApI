package com.elearning.e_learning_core.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.elearning.e_learning_core.model.Student;


@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {

}
