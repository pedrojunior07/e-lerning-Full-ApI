package com.elearning.e_learning_core.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.elearning.e_learning_core.Dtos.ApiResponse;
import com.elearning.e_learning_core.service.InstructorService;

@RestController
@RequestMapping("/instructors")
public class InstructorController {

    @Autowired
    private InstructorService instructorService;

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<?>> getInstructorById(@PathVariable Long id) {
        ApiResponse<?> response = instructorService.getInstructorById(id);
        return ResponseEntity.status(response.getCode()).body(response);
    }
}
