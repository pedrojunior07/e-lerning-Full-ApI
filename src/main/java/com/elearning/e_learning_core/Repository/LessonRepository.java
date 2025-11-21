package com.elearning.e_learning_core.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.elearning.e_learning_core.model.Lesson;

@Repository
public interface LessonRepository extends JpaRepository<Lesson, Long> {

}
