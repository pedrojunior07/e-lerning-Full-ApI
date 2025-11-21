package com.elearning.e_learning_core.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.elearning.e_learning_core.Dtos.CategoryDTO;
import com.elearning.e_learning_core.Dtos.TopCategoryDTO;
import com.elearning.e_learning_core.model.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    @Query("SELECT new com.elearning.e_learning_core.Dtos.CategoryDTO(c.id, c.name, COUNT(cs)) " +
            "FROM Category c LEFT JOIN c.courses cs WITH cs.status = 'DRAFT' " +
            "GROUP BY c.id, c.name ORDER BY COUNT(cs) DESC")
    List<CategoryDTO> countCoursesByCategory();

}
