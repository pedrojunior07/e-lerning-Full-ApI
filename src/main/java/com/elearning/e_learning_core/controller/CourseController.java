package com.elearning.e_learning_core.controller;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.hibernate.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.elearning.e_learning_core.Dtos.ApiResponse;
import com.elearning.e_learning_core.Dtos.CourseCardDto;
import com.elearning.e_learning_core.Dtos.CourseDto;
import com.elearning.e_learning_core.Dtos.CourseDtoStep1;
import com.elearning.e_learning_core.Dtos.CourseDtoStep2;
import com.elearning.e_learning_core.Dtos.CourseDtoStep5;
import com.elearning.e_learning_core.Dtos.CourseStatsDTO;
import com.elearning.e_learning_core.Dtos.LevelCountDTO;
import com.elearning.e_learning_core.Dtos.ModuleDto;
import com.elearning.e_learning_core.Dtos.PriceTypeCountDTO;
import com.elearning.e_learning_core.Dtos.TopCategoryDTO;
import com.elearning.e_learning_core.model.Course.StatusCourse;
import com.elearning.e_learning_core.model.Certificate;
import com.elearning.e_learning_core.model.LevelCurseType;
import com.elearning.e_learning_core.model.StatusCurseType;
import com.elearning.e_learning_core.model.Usr;
import com.elearning.e_learning_core.service.CourseLikeService;
import com.elearning.e_learning_core.service.CourseProgressService;
import com.elearning.e_learning_core.service.CourseService;

@RestController
@RequestMapping("/courses")
public class CourseController {

    @Autowired
    private final CourseService courseService;
    @Autowired
    private CourseLikeService courseLikeService;
    @Autowired
    private final CourseProgressService courseProgressService;

    public CourseController(CourseService courseService, CourseProgressService courseProgressService) {
        this.courseService = courseService;
        this.courseProgressService = courseProgressService;
    }

    @PostMapping("/{courseId}/like")
    public ResponseEntity<Map<String, Object>> likeCourse(
            @PathVariable Long courseId

    ) {

        Map<String, Object> response = courseLikeService.likeCourse(courseId);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{courseId}/like")
    public ResponseEntity<Map<String, Object>> unlikeCourse(
            @PathVariable Long courseId) {

        Map<String, Object> response = courseLikeService.unlikeCourse(courseId);
        return ResponseEntity.ok(response);

    }

    public ResponseEntity<Map<String, Object>> toggleLike(
            @PathVariable Long courseId,

            @RequestParam Long studentId) {

        Map<String, Object> response = courseLikeService.toggleLike(courseId, studentId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{courseId}")
    public ResponseEntity<ApiResponse<?>> getCourseById(@PathVariable Long courseId) {
        ApiResponse<?> response = courseService.getCourseById(courseId);
        return ResponseEntity.status(response.getCode()).body(response);
    }

    @GetMapping("/my-liked-courses")
    public ResponseEntity<ApiResponse<?>> getMyLikedCourses() {

        return ResponseEntity.ok(courseService.getMyLikedCourses());
    }

    @PostMapping("/{courseId}/lessons/{lessonId}/complete")
    public ResponseEntity<?> completeLesson(@PathVariable Long courseId,
            @PathVariable Long lessonId) {

        courseService.completeLesson(lessonId);
        return ResponseEntity.ok(Map.of("status", "success", "message", "Aula concluída"));
    }

    @GetMapping("/{courseId}/progress")
    public ResponseEntity<?> getCourseProgress(@PathVariable Long courseId) {

        double progress = courseService.getCourseProgress(courseId.toString());
        return ResponseEntity.ok(Map.of("progress", progress));
    }

    @PostMapping("/{courseId}/certificate")
    public ResponseEntity<?> issueCertificate(@PathVariable Long courseId) {

        Certificate cert = courseService.issueCertificate(courseId);
        return ResponseEntity.ok(cert);
    }

    @PostMapping("/step1/basic-info")
    public ResponseEntity<?> saveStep1(@RequestBody CourseDtoStep1 dto) {
        ApiResponse apiResponse = courseService.saveStep1(dto);
        return ResponseEntity.status(apiResponse.getCode())
                .body(apiResponse);
    }

    @PutMapping("/{courseId}/step-4/pricing")
    public ResponseEntity<?> updateStep5Pricing(
            @PathVariable Long courseId,
            @RequestBody CourseDtoStep5 dto) {
        try {
            courseService.saveStep5Pricing(courseId, dto);
            return ResponseEntity.ok().body(Map.of(
                    "status", "success",
                    "message", "Informações de preço atualizadas com sucesso"));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of(
                    "status", "error",
                    "message", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "status", "error",
                    "message", "Erro ao atualizar informações de preço"));
        }
    }

    @GetMapping("/cards")
    public ResponseEntity<ApiResponse<?>> getCourseCards(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice,
            Pageable pageable) {
        return ResponseEntity.ok(courseService.listCourseCards(category, title, minPrice, maxPrice, pageable));
    }

    @GetMapping("/mudules-course/{courseId}")
    public ResponseEntity<ApiResponse<?>> getCourseCards(@PathVariable Long courseId) {
        return ResponseEntity.ok(courseService.getModelesByCourseId(courseId));
    }

    @GetMapping("/students-cources/{id}")
    public ResponseEntity<ApiResponse<?>> getStudentsCourses(@PathVariable Long id, Pageable pageable) {

        return ResponseEntity.ok(courseService.getStudentCourses(id, pageable));
    }

    @GetMapping("/instructor-courses")
    public ResponseEntity<ApiResponse<?>> getCoursesByInstructorAndStatus(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice,
            @RequestParam(required = false) StatusCourse status,
            @RequestParam(required = false) Long instructorId,
            Pageable pageable) {
        return ResponseEntity.ok(courseService.listInstructorCourseCards(
                title, category, minPrice, maxPrice, status, instructorId, pageable));
    }

    @GetMapping("/stats")
    public CourseStatsDTO getCourseStats() {
        return courseService.getCourseStatistics();
    }

    @GetMapping("/all-courses")
    public ResponseEntity<ApiResponse<?>> getAllCourses() {
        ApiResponse<?> response = courseService.getAllCourses();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<?>> searchCourseCards(
            @RequestParam(required = false) Long instructorId,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) LevelCurseType level,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice,
            @PageableDefault(size = 10, sort = "title") Pageable pageable) {

        return ResponseEntity.ok(courseService.searchCourseCards(
                categoryId, instructorId, minPrice, maxPrice, StatusCourse.PUBLISHED, pageable));
    }

    @GetMapping("/my")
    public ResponseEntity<?> getMyCourses(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String status,
            @PageableDefault(size = 10, sort = "title") Pageable pageable) {

        StatusCourse statusEnum = null;
        if (status != null) {
            try {
                statusEnum = StatusCourse.valueOf(status.toUpperCase());
            } catch (IllegalArgumentException e) {
                return ResponseEntity.badRequest()
                        .body(new ApiResponse<>("error", "Status inválido. Use: PUBLISHED, DRAFT, INACTIVE, ARCHIVED",
                                400, null));
            }
        }

        ApiResponse<?> response = courseService.getMyCourses(title, statusEnum, pageable);
        return ResponseEntity.status(response.getCode()).body(response);
    }

    @PutMapping("/{id}/step-2/media")
    public ResponseEntity<?> saveStep2Media(@PathVariable Long id, @RequestBody CourseDtoStep2 dto) {
        ApiResponse apiResponse = courseService.saveStep2Media(id, dto);
        return ResponseEntity.status(apiResponse.getCode())
                .body(apiResponse);
    }

    @PutMapping("/{id}/step-3/curriculum")
    public ResponseEntity<?> saveCurriculum(@PathVariable Long id, @RequestBody List<ModuleDto> modules) {
        courseService.saveCurriculum(id, modules);
        return ResponseEntity.ok("Curriculum saved successfully.");
    }

    @GetMapping("/top-categories")
    public ApiResponse<?> getTopCategories() {
        return courseService.getTopCategories();
    }

    @GetMapping("/top-10-cources")
    public ApiResponse<?> getTop10Couses() {
        return courseService.getTop10Courses();
    }

    @GetMapping("/levels/count")
    public ApiResponse<List<LevelCountDTO>> getCourseLevelsWithCount() {

        ApiResponse levelsCount = courseService.getCourseLevelsWithCount();

        return levelsCount;
    }

    @GetMapping("/prices/count")
    public ApiResponse<List<PriceTypeCountDTO>> getCoursePriceTypesCount() {
        List<PriceTypeCountDTO> priceCounts = courseService.getCoursePriceTypesCount();
        return new ApiResponse<>("success", "Price types count retrieved successfully", 200, priceCounts);
    }
}
