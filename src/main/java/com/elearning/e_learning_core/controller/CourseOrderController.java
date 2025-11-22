package com.elearning.e_learning_core.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.elearning.e_learning_core.Dtos.ApiResponse;
import com.elearning.e_learning_core.Dtos.CreateOrderRequest;
import com.elearning.e_learning_core.model.OrderStatus;
import com.elearning.e_learning_core.service.CourseOrderService;
import com.elearning.e_learning_core.service.LocalStorageService;

import java.util.Map;

@RestController
@RequestMapping("/orders")
public class CourseOrderController {

    @Autowired
    private CourseOrderService orderService;

    @Autowired
    private LocalStorageService storageService;

    // ============ STUDENT ENDPOINTS ============

    @PostMapping
    public ResponseEntity<ApiResponse<?>> createOrder(@RequestBody CreateOrderRequest request) {
        ApiResponse<?> response = orderService.createOrder(request);
        return ResponseEntity.status(response.getCode()).body(response);
    }

    @GetMapping("/student/{studentId}")
    public ResponseEntity<ApiResponse<?>> getStudentOrders(@PathVariable Long studentId) {
        ApiResponse<?> response = orderService.getStudentOrders(studentId);
        return ResponseEntity.status(response.getCode()).body(response);
    }

    @PostMapping("/{orderId}/proof")
    public ResponseEntity<ApiResponse<?>> uploadProofOfPayment(
            @PathVariable Long orderId,
            @RequestParam("file") MultipartFile file) {

        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body(
                new ApiResponse<>("error", "Nenhum arquivo enviado", 400, null)
            );
        }

        try {
            Map<String, Object> uploadResult = storageService.uploadThumbnail(file);
            String proofUrl = (String) uploadResult.get("fullUrl");

            ApiResponse<?> response = orderService.uploadProofOfPayment(orderId, proofUrl);
            return ResponseEntity.status(response.getCode()).body(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                new ApiResponse<>("error", "Erro ao enviar comprovativo: " + e.getMessage(), 400, null)
            );
        }
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<ApiResponse<?>> getOrderById(@PathVariable Long orderId) {
        ApiResponse<?> response = orderService.getOrderById(orderId);
        return ResponseEntity.status(response.getCode()).body(response);
    }

    // ============ INSTRUCTOR ENDPOINTS ============

    @GetMapping("/instructor/{instructorId}")
    public ResponseEntity<ApiResponse<?>> getInstructorOrders(@PathVariable Long instructorId) {
        ApiResponse<?> response = orderService.getInstructorOrders(instructorId);
        return ResponseEntity.status(response.getCode()).body(response);
    }

    @GetMapping("/instructor/{instructorId}/status/{status}")
    public ResponseEntity<ApiResponse<?>> getInstructorOrdersByStatus(
            @PathVariable Long instructorId,
            @PathVariable OrderStatus status) {
        ApiResponse<?> response = orderService.getInstructorOrdersByStatus(instructorId, status);
        return ResponseEntity.status(response.getCode()).body(response);
    }

    @PostMapping("/{orderId}/approve")
    public ResponseEntity<ApiResponse<?>> approveOrder(@PathVariable Long orderId) {
        ApiResponse<?> response = orderService.approveOrder(orderId);
        return ResponseEntity.status(response.getCode()).body(response);
    }

    @PostMapping("/{orderId}/reject")
    public ResponseEntity<ApiResponse<?>> rejectOrder(
            @PathVariable Long orderId,
            @RequestBody Map<String, String> body) {
        String reason = body.getOrDefault("reason", "Sem motivo especificado");
        ApiResponse<?> response = orderService.rejectOrder(orderId, reason);
        return ResponseEntity.status(response.getCode()).body(response);
    }
}
