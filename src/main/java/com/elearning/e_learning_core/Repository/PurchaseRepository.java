package com.elearning.e_learning_core.Repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.elearning.e_learning_core.model.Course;
import com.elearning.e_learning_core.model.Purchase;
import java.util.List;

@Repository
public interface PurchaseRepository extends JpaRepository<Purchase, Long> {
    List<Purchase> findByStudentId(Long student);

    @Query("SELECT c FROM Course c LEFT JOIN Purchase p  WHERE p.student.id = :id ")
    Page<Course> fin(@Param("id") Long id, Pageable pageable);

    Purchase findByExternalId(String id);
}