package com.elearning.e_learning_core.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.elearning.e_learning_core.Dtos.ApiResponse;
import com.elearning.e_learning_core.Dtos.PurchaseDTO;
import com.elearning.e_learning_core.service.PurchaseService;

@RestController
@RequestMapping("/purchases")
public class PurchaseController {

    @Autowired
    private PurchaseService purchaseService;

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
}
