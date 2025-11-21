package com.elearning.e_learning_core.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.elearning.e_learning_core.model.Course;
import com.elearning.e_learning_core.model.CourseModule;
import com.elearning.e_learning_core.model.Lesson;
import com.elearning.e_learning_core.model.LessonProgress;
import com.elearning.e_learning_core.model.Student;

public interface LessonProgressRepository extends JpaRepository<LessonProgress, Long> {
    Optional<LessonProgress> findByStudentAndLesson(Student student, Lesson lesson);

    long countByStudentAndLessonModuleAndCompletedTrue(Student student, CourseModule module);

    long countByStudentAndLessonModuleCourseAndCompletedTrue(Student student, Course course);

    List<LessonProgress> findByStudentIdAndLessonModuleCourseId(Long studentId, Long courseId);

    @Query("SELECT lp FROM LessonProgress lp WHERE lp.student.id = :studentId AND lp.lesson.id IN :lessonIds")
    List<LessonProgress> findByStudentIdAndLessonIds(@Param("studentId") Long studentId,
            @Param("lessonIds") List<Long> lessonIds);

    @Query("SELECT lp FROM LessonProgress lp WHERE lp.student.id = :studentId AND lp.lesson.module.course.id = :courseId")
    List<LessonProgress> findProgressByStudentAndCourse(@Param("studentId") Long studentId,
            @Param("courseId") Long courseId);

}