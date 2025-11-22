package com.elearning.e_learning_core.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.elearning.e_learning_core.Dtos.ApiResponse;
import com.elearning.e_learning_core.Dtos.PurchaseDTO;
import com.elearning.e_learning_core.Repository.CourseRepository;
import com.elearning.e_learning_core.Repository.StudentRepository;
import com.elearning.e_learning_core.model.Course;
import com.elearning.e_learning_core.service.PurchaseService;

@RestController
@RequestMapping("/purchases")
public class PurchaseController {

    @Autowired
    private PurchaseService purchaseService;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private CourseRepository courseRepository;

    @PostMapping("/course/{studentId}")
    public ResponseEntity<ApiResponse<?>> savePurchase(
            @PathVariable Long studentId,
            @RequestBody PurchaseDTO dto) {
        ApiResponse<?> response = purchaseService.purchaseSave(dto, studentId);
        return ResponseEntity.status(response.getCode()).body(response);
    }

    @PostMapping("/valide/{paymentId}")
    public ResponseEntity<ApiResponse<?>> valid(
            @PathVariable String paymentId) {
        ApiResponse<?> response = purchaseService.validate(paymentId);
        return ResponseEntity.status(response.getCode()).body(response);
    }

    @GetMapping
    public List<PurchaseDTO> listarCompras() {
        return purchaseService.listarCompras();
    }

    @GetMapping("/check-access/{studentId}/{courseId}")
    public ResponseEntity<?> checkAccess(
            @PathVariable Long studentId,
            @PathVariable Long courseId) {

        Course course = courseRepository.findById(courseId).orElse(null);
        if (course == null) {
            return ResponseEntity.notFound().build();
        }

        boolean isFree = course.isFree();
        boolean hasAccess = isFree || studentRepository.isEnrolledInCourse(studentId, courseId);

        return ResponseEntity.ok(Map.of(
            "success", true,
            "data", Map.of(
                "hasAccess", hasAccess,
                "courseTitle", course.getTitle(),
                "isFree", isFree
            )
        ));
    }

    @GetMapping("/student-purchases/{studentId}")
    public ResponseEntity<?> getStudentPurchases(@PathVariable Long studentId) {
        List<PurchaseDTO> purchases = purchaseService.getStudentPurchases(studentId);
        return ResponseEntity.ok(Map.of(
            "success", true,
            "data", purchases
        ));
    }
}
