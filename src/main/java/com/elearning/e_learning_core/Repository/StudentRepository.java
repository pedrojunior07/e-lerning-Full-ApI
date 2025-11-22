package com.elearning.e_learning_core.Repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.elearning.e_learning_core.model.Course;
import com.elearning.e_learning_core.model.Student;


@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {

    @Query(value = "SELECT COUNT(*) > 0 FROM student_course WHERE student_id = :studentId AND course_id = :courseId", nativeQuery = true)
    boolean isEnrolledInCourse(@Param("studentId") Long studentId, @Param("courseId") Long courseId);

@Query("SELECT c FROM Course c JOIN c.students s WHERE s.id = :studentId")
    Page<Course> findCoursesByStudentId(@Param("studentId") Long studentId, Pageable pageable);
}
