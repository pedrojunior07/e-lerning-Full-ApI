package com.elearning.e_learning_core.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.elearning.e_learning_core.Dtos.ApiResponse;
import com.elearning.e_learning_core.Dtos.InstructorDto;
import com.elearning.e_learning_core.service.InstructorService;

@RestController
@RequestMapping("/instructors")
public class InstructorController {

    @Autowired
    private InstructorService instructorService;

    @GetMapping
    public Page<InstructorDto> listInstructors(Pageable pageable) {
        return instructorService.listInstructors(pageable);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<?>> getInstructorById(@PathVariable Long id) {
        ApiResponse<?> response = instructorService.getInstructorById(id);
        return ResponseEntity.status(response.getCode()).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<?>> updateInstructor(
            @PathVariable Long id,
            @RequestBody InstructorDto dto) {
        ApiResponse<?> response = instructorService.updateInstructor(id, dto);
        return ResponseEntity.status(response.getCode()).body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<?>> deleteInstructor(@PathVariable Long id) {
        ApiResponse<?> response = instructorService.deleteInstructor(id);
        return ResponseEntity.status(response.getCode()).body(response);
    }

    @GetMapping("/{id}/courses")
    public ResponseEntity<ApiResponse<?>> getInstructorCourses(@PathVariable Long id) {
        ApiResponse<?> response = instructorService.getInstructorCourses(id);
        return ResponseEntity.status(response.getCode()).body(response);
    }

    @GetMapping("/{id}/stats")
    public ResponseEntity<ApiResponse<?>> getInstructorStats(@PathVariable Long id) {
        ApiResponse<?> response = instructorService.getInstructorStats(id);
        return ResponseEntity.status(response.getCode()).body(response);
    }

    @GetMapping("/count")
    public ResponseEntity<?> getInstructorsCount() {
        return ResponseEntity.ok(instructorService.countInstructors());
    }
}
