package com.elearning.e_learning_core.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.elearning.e_learning_core.Dtos.ApiResponse;
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

}
