package com.elearning.e_learning_core.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.elearning.e_learning_core.Dtos.ApiResponse;
import com.elearning.e_learning_core.Dtos.PaymentWalletDto;
import com.elearning.e_learning_core.model.WalletType;
import com.elearning.e_learning_core.service.CourseOrderService;

@RestController
@RequestMapping("/wallets")
public class PaymentWalletController {

    @Autowired
    private CourseOrderService orderService;

    @PostMapping("/instructor/{instructorId}")
    public ResponseEntity<ApiResponse<?>> createWallet(
            @PathVariable Long instructorId,
            @RequestBody PaymentWalletDto dto) {
        ApiResponse<?> response = orderService.createWallet(instructorId, dto);
        return ResponseEntity.status(response.getCode()).body(response);
    }

    @GetMapping("/instructor/{instructorId}")
    public ResponseEntity<ApiResponse<?>> getInstructorWallets(@PathVariable Long instructorId) {
        ApiResponse<?> response = orderService.getInstructorWallets(instructorId);
        return ResponseEntity.status(response.getCode()).body(response);
    }

    @GetMapping("/instructor/{instructorId}/type/{type}")
    public ResponseEntity<ApiResponse<?>> getWalletsByType(
            @PathVariable Long instructorId,
            @PathVariable WalletType type) {
        ApiResponse<?> response = orderService.getActiveWalletsByType(instructorId, type);
        return ResponseEntity.status(response.getCode()).body(response);
    }

    @PutMapping("/{walletId}")
    public ResponseEntity<ApiResponse<?>> updateWallet(
            @PathVariable Long walletId,
            @RequestBody PaymentWalletDto dto) {
        ApiResponse<?> response = orderService.updateWallet(walletId, dto);
        return ResponseEntity.status(response.getCode()).body(response);
    }

    @DeleteMapping("/{walletId}")
    public ResponseEntity<ApiResponse<?>> deleteWallet(@PathVariable Long walletId) {
        ApiResponse<?> response = orderService.deleteWallet(walletId);
        return ResponseEntity.status(response.getCode()).body(response);
    }
}
