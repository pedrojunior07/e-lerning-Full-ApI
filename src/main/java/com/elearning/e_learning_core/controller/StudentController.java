package com.elearning.e_learning_core.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.elearning.e_learning_core.Dtos.ApiResponse;
import com.elearning.e_learning_core.Dtos.StudentDTO;
import com.elearning.e_learning_core.Dtos.StudentSummaryDTO;
import com.elearning.e_learning_core.service.StudentService;

@RestController
@RequestMapping("/students")
public class StudentController {

    @Autowired
    private StudentService service;

    @GetMapping("/summary")
    public Page<StudentSummaryDTO> getStudentsSummary(Pageable pageable) {
        return service.listStudentsSummary(pageable);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<?>> getStudentById(@PathVariable Long id) {
        ApiResponse<?> response = service.getStudentById(id);
        return ResponseEntity.status(response.getCode()).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<?>> updateStudent(
            @PathVariable Long id,
            @RequestBody StudentDTO dto) {
        ApiResponse<?> response = service.updateStudent(id, dto);
        return ResponseEntity.status(response.getCode()).body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<?>> deleteStudent(@PathVariable Long id) {
        ApiResponse<?> response = service.deleteStudent(id);
        return ResponseEntity.status(response.getCode()).body(response);
    }

    @GetMapping("/{id}/courses")
    public ResponseEntity<ApiResponse<?>> getStudentCourses(@PathVariable Long id) {
        ApiResponse<?> response = service.getStudentCourses(id);
        return ResponseEntity.status(response.getCode()).body(response);
    }

    @PostMapping("/{studentId}/courses/{courseId}/enroll")
    public ResponseEntity<ApiResponse<?>> enrollInCourse(
            @PathVariable Long studentId,
            @PathVariable Long courseId) {
        ApiResponse<?> response = service.enrollStudentInCourse(studentId, courseId);
        return ResponseEntity.status(response.getCode()).body(response);
    }

    @DeleteMapping("/{studentId}/courses/{courseId}/unenroll")
    public ResponseEntity<ApiResponse<?>> unenrollFromCourse(
            @PathVariable Long studentId,
            @PathVariable Long courseId) {
        ApiResponse<?> response = service.unenrollStudentFromCourse(studentId, courseId);
        return ResponseEntity.status(response.getCode()).body(response);
    }

    @GetMapping("/count")
    public ResponseEntity<?> getStudentsCount() {
        return ResponseEntity.ok(service.countStudents());
    }
}
