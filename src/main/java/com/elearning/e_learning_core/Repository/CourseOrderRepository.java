package com.elearning.e_learning_core.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.elearning.e_learning_core.model.CourseOrder;
import com.elearning.e_learning_core.model.OrderStatus;

@Repository
public interface CourseOrderRepository extends JpaRepository<CourseOrder, Long> {
    List<CourseOrder> findByStudentId(Long studentId);
    List<CourseOrder> findByStudentIdOrderByOrderDateDesc(Long studentId);

    @Query("SELECT o FROM CourseOrder o WHERE o.course.instructor.id = :instructorId ORDER BY o.orderDate DESC")
    List<CourseOrder> findByInstructorId(@Param("instructorId") Long instructorId);

    @Query("SELECT o FROM CourseOrder o WHERE o.course.instructor.id = :instructorId AND o.status = :status ORDER BY o.orderDate DESC")
    List<CourseOrder> findByInstructorIdAndStatus(@Param("instructorId") Long instructorId, @Param("status") OrderStatus status);

    Optional<CourseOrder> findByStudentIdAndCourseIdAndStatusIn(Long studentId, Long courseId, List<OrderStatus> statuses);

    boolean existsByStudentIdAndCourseIdAndStatus(Long studentId, Long courseId, OrderStatus status);
}
