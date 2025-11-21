package com.elearning.e_learning_core.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.elearning.e_learning_core.model.Course;
import com.elearning.e_learning_core.model.CourseModule;

@Repository
public interface CourseModuleRepository extends JpaRepository<CourseModule, Long> {

    List<CourseModule> findByCourseId(Long courseId);

}
