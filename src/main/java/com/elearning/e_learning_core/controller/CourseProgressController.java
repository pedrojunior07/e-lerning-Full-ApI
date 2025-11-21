package com.elearning.e_learning_core.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.elearning.e_learning_core.Dtos.ModuleWithProgressDTO;
import com.elearning.e_learning_core.service.CourseProgressService;

@RestController
@RequestMapping("/courses-progress")

public class CourseProgressController {
    private final CourseProgressService courseProgressService;

    public CourseProgressController(CourseProgressService courseProgressService) {
        this.courseProgressService = courseProgressService;
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
            @PathVariable Long courseId,

            @RequestParam(required = false) Long studentId) {
        // Se studentId não for fornecido, usa o estudante autenticado
        // Você precisará implementar a lógica para obter o studentId do usuário
        // autenticado
        // Por enquanto, vamos usar um valor padrão para demonstração
        Long actualStudentId = studentId != null ? studentId : 1L; // Substitua pela lógica de autenticação

        List<ModuleWithProgressDTO> modules = courseProgressService
                .getCourseModulesWithStudentProgress(courseId, actualStudentId);

        return ResponseEntity.ok(modules);
    }
}