package com.elearning.e_learning_core.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.elearning.e_learning_core.Dtos.*;
import com.elearning.e_learning_core.service.MpesaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * REST Controller for M-Pesa payment operations
 */
@RestController
@RequestMapping("/mpesa")
@Tag(name = "M-Pesa", description = "M-Pesa payment integration endpoints")
public class MpesaController {

    private static final Logger logger = LoggerFactory.getLogger(MpesaController.class);

    @Autowired
    private MpesaService mpesaService;

    /**
     * Process C2B payment (Customer to Business)
     * Customer pays to the business
     */
    @PostMapping("/c2b/payment")
    @Operation(summary = "Process C2B Payment", description = "Customer pays to business")
    public ResponseEntity<ApiResponse<MpesaResponse>> processC2BPayment(@RequestBody MpesaC2BRequest request) {
        logger.info("Recebida requisição C2B: phoneNumber={}, amount={}",
            request.getPhoneNumber(), request.getAmount());

        try {
            ApiResponse<MpesaResponse> response = mpesaService.processC2BPayment(request);

            if ("success".equals(response.getStatus())) {
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }
        } catch (Exception e) {
            logger.error("Erro ao processar pagamento C2B: {}", e.getMessage(), e);
            ApiResponse<MpesaResponse> errorResponse = new ApiResponse<>(
                "error",
                "Erro interno ao processar pagamento: " + e.getMessage(),
                500,
                null
            );
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    /**
     * Process B2C payment (Business to Customer)
     * Business pays to the customer
     */
    @PostMapping("/b2c/payment")
    @Operation(summary = "Process B2C Payment", description = "Business pays to customer")
    public ResponseEntity<ApiResponse<MpesaResponse>> processB2CPayment(@RequestBody MpesaB2CRequest request) {
        logger.info("Recebida requisição B2C: phoneNumber={}, amount={}",
            request.getPhoneNumber(), request.getAmount());

        try {
            ApiResponse<MpesaResponse> response = mpesaService.processB2CPayment(request);

            if ("success".equals(response.getStatus())) {
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }
        } catch (Exception e) {
            logger.error("Erro ao processar pagamento B2C: {}", e.getMessage(), e);
            ApiResponse<MpesaResponse> errorResponse = new ApiResponse<>(
                "error",
                "Erro interno ao processar pagamento: " + e.getMessage(),
                500,
                null
            );
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    /**
     * Query transaction status
     */
    @GetMapping("/transaction/status")
    @Operation(summary = "Query Transaction Status", description = "Check the status of a transaction")
    public ResponseEntity<ApiResponse<MpesaResponse>> queryTransactionStatus(
            @RequestParam String transactionID,
            @RequestParam String thirdPartyReference) {

        logger.info("Consultando status: transactionID={}, reference={}",
            transactionID, thirdPartyReference);

        try {
            ApiResponse<MpesaResponse> response = mpesaService.queryTransactionStatus(
                transactionID, thirdPartyReference);

            if ("success".equals(response.getStatus())) {
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }
        } catch (Exception e) {
            logger.error("Erro ao consultar status: {}", e.getMessage(), e);
            ApiResponse<MpesaResponse> errorResponse = new ApiResponse<>(
                "error",
                "Erro interno ao consultar status: " + e.getMessage(),
                500,
                null
            );
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    /**
     * M-Pesa callback endpoint
     * Receives notifications from M-Pesa API
     */
    @PostMapping("/callback")
    @Operation(summary = "M-Pesa Callback", description = "Receives payment notifications from M-Pesa")
    public ResponseEntity<ApiResponse<String>> mpesaCallback(@RequestBody MpesaCallbackRequest callback) {
        logger.info("Callback M-Pesa recebido: transactionID={}, resultCode={}",
            callback.getTransactionID(), callback.getResultCode());

        try {
            // Process the callback here
            // You can update order status, send notifications, etc.

            logger.info("Callback processado com sucesso: {}", callback.getThirdPartyReference());

            ApiResponse<String> response = new ApiResponse<>(
                "success",
                "Callback processado com sucesso",
                200,
                "OK"
            );
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Erro ao processar callback: {}", e.getMessage(), e);
            ApiResponse<String> errorResponse = new ApiResponse<>(
                "error",
                "Erro ao processar callback: " + e.getMessage(),
                500,
                null
            );
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    /**
     * M-Pesa timeout endpoint
     * Receives timeout notifications from M-Pesa API
     */
    @PostMapping("/timeout")
    @Operation(summary = "M-Pesa Timeout", description = "Receives timeout notifications from M-Pesa")
    public ResponseEntity<ApiResponse<String>> mpesaTimeout(@RequestBody MpesaCallbackRequest timeout) {
        logger.warn("Timeout M-Pesa recebido: transactionID={}", timeout.getTransactionID());

        try {
            // Handle timeout - cancel order, refund, etc.

            ApiResponse<String> response = new ApiResponse<>(
                "success",
                "Timeout processado",
                200,
                "OK"
            );
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Erro ao processar timeout: {}", e.getMessage(), e);
            ApiResponse<String> errorResponse = new ApiResponse<>(
                "error",
                "Erro ao processar timeout: " + e.getMessage(),
                500,
                null
            );
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
}
