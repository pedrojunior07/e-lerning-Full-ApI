package com.elearning.e_learning_core.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.elearning.e_learning_core.model.Question;
import com.elearning.e_learning_core.model.Quiz;

public interface QuizRepository extends JpaRepository<Quiz, Long> {

}
