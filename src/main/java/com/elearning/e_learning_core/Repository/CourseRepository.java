package com.elearning.e_learning_core.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.elearning.e_learning_core.Dtos.CourseCardDto;
import com.elearning.e_learning_core.Dtos.CourseDto;
import com.elearning.e_learning_core.Dtos.LessonDTO;
import com.elearning.e_learning_core.Dtos.ModuleDto;
import com.elearning.e_learning_core.Dtos.TopCategoryDTO;
import com.elearning.e_learning_core.model.Course;
import com.elearning.e_learning_core.model.CourseModule;
import com.elearning.e_learning_core.model.Course.StatusCourse;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {

        boolean existsByIdAndInstructorId(Long courseId, Long id);

        List<Course> findByInstructorId(Long instructorId);

        @Query("SELECT c FROM Course c WHERE " +
                        "LOWER(c.title) LIKE LOWER(CONCAT('%', :title, '%')) " +
                        "AND (:status IS NULL OR c.status = :status)")
        Page<Course> findByTitleCategoryAndOptionalStatus(
                        @Param("title") String title,
                        @Param("status") StatusCourse status,
                        Pageable pageable);

        @Query("""
                            SELECT new com.elearning.e_learning_core.Dtos.CourseCardDto(
                                c.thumbnailPath,
                                CONCAT(i.firstName, ' ', i.lastName),
                                c.category,
                                c.title,
                                c.price,
                                c.id,
                                COUNT(l.id),
                                COUNT(s.id),
                                c.status
                            )
                            FROM Course c
                            JOIN c.instructor i
                            LEFT JOIN c.modules m
                            LEFT JOIN m.lessons l
                            LEFT JOIN c.students s
                            WHERE (:title IS NULL OR LOWER(c.title) LIKE LOWER(CONCAT('%', :title, '%')))
                              AND (:category IS NULL OR c.category.name = :category)
                              AND (:minPrice IS NULL OR c.price >= :minPrice)
                              AND (:maxPrice IS NULL OR c.price <= :maxPrice)
                            GROUP BY
                                c.thumbnailPath,
                                i.firstName,
                                i.lastName,
                                c.category,
                                c.title,
                                c.price,
                                c.id
                        """)
        Page<CourseCardDto> findAllCourseCardsWithLessonCount(
                        @Param("title") String title,
                        @Param("category") String category,
                        @Param("minPrice") Double minPrice,
                        @Param("maxPrice") Double maxPrice,
                        Pageable pageable);

        @Query("""
                            SELECT new com.elearning.e_learning_core.Dtos.CourseCardDto(
                                c.thumbnailPath,
                                CONCAT(i.firstName, ' ', i.lastName),
                                c.category,
                                c.title,
                                c.price,
                                c.id,
                                COUNT(l.id),
                                COUNT(s.id),
                                c.status
                            )
                            FROM Course c
                            JOIN c.instructor i
                            LEFT JOIN c.modules m
                            LEFT JOIN m.lessons l
                            LEFT JOIN c.students s
                            WHERE (:title IS NULL OR LOWER(c.title) LIKE LOWER(CONCAT('%', :title, '%')))
                              AND (:category IS NULL OR c.category.name = :category)
                              AND (:minPrice IS NULL OR c.price >= :minPrice)
                              AND (:maxPrice IS NULL OR c.price <= :maxPrice)
                              AND (:status IS NULL OR c.status = :status)
                              AND (:instructorId IS NULL OR i.id = :instructorId)
                            GROUP BY
                                c.thumbnailPath,
                                i.firstName,
                                i.lastName,
                                c.category,
                                c.title,
                                c.price,
                                c.id
                        """)
        Page<CourseCardDto> findCourseCardsByInstructorAndStatus(
                        @Param("title") String title,
                        @Param("category") String category,
                        @Param("minPrice") Double minPrice,
                        @Param("maxPrice") Double maxPrice,
                        @Param("status") StatusCourse status,
                        @Param("instructorId") Long instructorId,
                        Pageable pageable);

        @Query("""
                            SELECT new com.elearning.e_learning_core.Dtos.ModuleDto(
                                m.id,
                                m.title,
                                m.description
                            )
                            FROM CourseModule m
                            WHERE m.course.id = :courseId
                        """)
        List<ModuleDto> findModulesByCourseId(@Param("courseId") Long courseId);

        @Query("""
                            SELECT new com.elearning.e_learning_core.Dtos.LessonDTO(
                                l.id,
                                l.title,
                                l.content
                            )
                            FROM Lesson l
                            WHERE l.module.id IN :moduleIds
                        """)
        List<LessonDTO> findLessonsByModuleIds(@Param("moduleIds") List<Long> moduleIds);

        Long countByStatus(StatusCourse status);

        @Query("SELECT new com.elearning.e_learning_core.Dtos.TopCategoryDTO(c.category.id, c.category.name, COUNT(c.id)) "
                        +
                        "FROM Course c " +
                        "WHERE c.status = 'DRAFT' " +
                        "GROUP BY c.category.id, c.category.name " +
                        "ORDER BY COUNT(c.id) DESC")
        List<TopCategoryDTO> findTopCategories();

        @Query("""
                            SELECT new com.elearning.e_learning_core.Dtos.CourseCardDto(
                                c.thumbnailPath,
                                CONCAT(i.firstName, ' ', i.lastName),
                                c.category,
                                c.title,
                                c.price,
                                c.id,
                                COUNT(DISTINCT l.id),
                                COUNT(DISTINCT s.id),
                                c.status
                            )
                            FROM Course c
                            JOIN c.instructor i
                            LEFT JOIN c.modules m
                            LEFT JOIN m.lessons l
                            LEFT JOIN c.students s
                            WHERE c.status = 'DRAFT'
                            GROUP BY c.id, c.thumbnailPath, i.firstName, i.lastName, c.category, c.title, c.price, c.status
                            ORDER BY COUNT(DISTINCT s.id) DESC
                        """)
        List<CourseCardDto> findTopCoursesWithMostStudents(Pageable pageable);

        @Query("""
                            SELECT new com.elearning.e_learning_core.Dtos.CourseCardDto(
                                c.thumbnailPath,
                                CONCAT(i.firstName, ' ', i.lastName),
                                c.category,
                                c.title,
                                c.price,
                                c.id,
                                COUNT(DISTINCT l.id),
                                COUNT(DISTINCT s.id),
                                c.status
                            )
                            FROM Course c
                            JOIN c.instructor i
                            LEFT JOIN c.modules m
                            LEFT JOIN m.lessons l
                            LEFT JOIN c.students s
                            WHERE c.status = 'DRAFT'
                              AND (:categoryId IS NULL OR c.category.id = :categoryId)
                              AND (:instructorId IS NULL OR i.id = :instructorId)
                              AND (:minPrice IS NULL OR c.price >= :minPrice)
                              AND (:maxPrice IS NULL OR c.price <= :maxPrice)
                            GROUP BY
                                c.thumbnailPath,
                                i.firstName,
                                i.lastName,
                                c.category,
                                c.title,
                                c.price,
                                c.id,
                                c.status
                        """)
        Page<CourseCardDto> searchCourseCards(
                        @Param("categoryId") Long categoryId,
                        @Param("instructorId") Long instructorId,
                        @Param("minPrice") Double minPrice,
                        @Param("maxPrice") Double maxPrice,
                        Pageable pageable);

        @Query("SELECT c.level, COUNT(c) FROM Course c GROUP BY c.level")
        List<Object[]> countCoursesByLevel();

        @Query("SELECT c.isFree, COUNT(c) FROM Course c WHERE c.status = 'DRAFT' GROUP BY c.isFree")
        List<Object[]> countCoursesByPriceType();

        @Query("SELECT COUNT(c) FROM Course c WHERE c.price > 0")
        long countPaidCourses();

        @Query("SELECT COUNT(c) FROM Course c WHERE c.isFree = true")
        long countFreeCourses();

}