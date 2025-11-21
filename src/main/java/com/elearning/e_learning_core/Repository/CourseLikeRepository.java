package com.elearning.e_learning_core.Repository;

import com.elearning.e_learning_core.model.Course;
import com.elearning.e_learning_core.model.CourseLike;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CourseLikeRepository extends JpaRepository<CourseLike, Long> {

    Optional<CourseLike> findByStudentIdAndCourseId(Long studentId, Long courseId);

    boolean existsByStudentIdAndCourseId(Long studentId, Long courseId);

    Long countByCourseId(Long courseId);

    @Query("SELECT cl.course.id FROM CourseLike cl WHERE cl.student.id = :studentId AND cl.course.id IN :courseIds")
    List<Long> findLikedCourseIdsByStudentAndCourses(@Param("studentId") Long studentId,
            @Param("courseIds") List<Long> courseIds);

    @Query("SELECT cl.course.id, COUNT(cl) FROM CourseLike cl WHERE cl.course.id IN :courseIds GROUP BY cl.course.id")
    List<Object[]> countLikesByCourseIds(@Param("courseIds") List<Long> courseIds);

    @Query("SELECT COUNT(cl) FROM CourseLike cl WHERE cl.course.id = :courseId")
    Long getLikeCountByCourseId(@Param("courseId") Long courseId);

    // NO CourseLikeRepository - MÉTODO CORRIGIDO
    @Query("SELECT cl.course FROM CourseLike cl " +
            "JOIN FETCH cl.course.instructor " +
            "JOIN FETCH cl.course.category " +
            "WHERE cl.student.id = :studentId " +
            "ORDER BY cl.likedAt DESC")
    List<Course> findLikedCoursesByStudent(@Param("studentId") Long studentId);

    @Query("SELECT cl.course FROM CourseLike cl WHERE cl.student.id = :studentId ORDER BY cl.likedAt DESC")
    Page<Course> findLikedCoursesByStudentPaged(@Param("studentId") Long studentId, Pageable pageable);

    @Query("SELECT COUNT(cl) FROM CourseLike cl WHERE cl.student.id = :studentId")
    Long countLikedCoursesByStudent(@Param("studentId") Long studentId);

}