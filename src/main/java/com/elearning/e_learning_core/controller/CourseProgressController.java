package com.elearning.e_learning_core.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.elearning.e_learning_core.Dtos.ModuleWithProgressDTO;
import com.elearning.e_learning_core.config.security.AuthenticationFacade;
import com.elearning.e_learning_core.service.CourseProgressService;

@RestController
@RequestMapping("/courses-progress")

public class CourseProgressController {
    private final CourseProgressService courseProgressService;
    private final AuthenticationFacade authenticationFacade;

    public CourseProgressController(CourseProgressService courseProgressService, AuthenticationFacade authenticationFacade) {
        this.courseProgressService = courseProgressService;
        this.authenticationFacade = authenticationFacade;
    }

    @GetMapping("/{courseId}/students/{studentId}/modules-with-progress")
    public ResponseEntity<List<ModuleWithProgressDTO>> getCourseModulesWithStudentProgress(

            @PathVariable Long courseId,

            @PathVariable Long studentId) {

        List<ModuleWithProgressDTO> modules = courseProgressService
                .getCourseModulesWithStudentProgress(courseId, studentId);

        return ResponseEntity.ok(modules);
    }

    @GetMapping("/{courseId}/students/{studentId}/progress")
    public ResponseEntity<Map<String, Object>> getCourseProgress(

            @PathVariable Long courseId,

            @PathVariable Long studentId) {

        double progress = courseProgressService.getCourseProgressPercentage(courseId, studentId);

        return ResponseEntity.ok(Map.of(
                "courseId", courseId,
                "studentId", studentId,
                "progressPercentage", progress,
                "completed", progress >= 100.0));
    }

    @GetMapping("/{courseId}/my-modules-with-progress")
    public ResponseEntity<List<ModuleWithProgressDTO>> getMyCourseModulesWithProgress(
            @PathVariable Long courseId) {

        Long studentId = authenticationFacade.getPrincipal().getPerson().getId();

        List<ModuleWithProgressDTO> modules = courseProgressService
                .getCourseModulesWithStudentProgress(courseId, studentId);

        return ResponseEntity.ok(modules);
    }
}