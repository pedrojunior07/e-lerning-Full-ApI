package com.elearning.e_learning_core.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.elearning.e_learning_core.service.PaymentCallbackReceiverService;
import com.elearning.e_learning_core.util.HmacUtils;

import java.time.Instant;

@RestController
@RequestMapping("/callbacks")
public class PaymentCallbackController {

    private static final Logger logger = LoggerFactory.getLogger(PaymentCallbackController.class);
    private final PaymentCallbackReceiverService callbackReceiverService;
    private final String SECRET_KEY = "fae7634cff17c537dd3ee4198cb0ddddfee8c07b92f19f23e860b8d9a1b2c3d4";

    public PaymentCallbackController(PaymentCallbackReceiverService callbackReceiverService) {
        this.callbackReceiverService = callbackReceiverService;
    }

    @PostMapping("/payment/{id}")
    public ResponseEntity<String> receivePaymentCallback(
            @RequestHeader("X-Timestamp") String timestampHeader,
            @RequestHeader("X-Signature") String signatureHeader, @PathVariable String id,
            @RequestBody String rawBody) {

        logger.info("🔄 Recebendo callback de pagamento");
        logger.debug("📨 Headers - Timestamp: {}, Signature: {}", timestampHeader, signatureHeader);
        logger.debug("📦 Raw Body: {}", rawBody);

        try {
            // 1. Validar assinatura
            if (!isValidSignature(timestampHeader, signatureHeader, rawBody)) {
                logger.error("❌ Assinatura inválida para o callback");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("Invalid signature");
            }

            // 2. Validar timestamp (prevenção contra replay attacks)
            if (!isValidTimestamp(timestampHeader)) {
                logger.error("❌ Timestamp inválido ou expirado: {}", timestampHeader);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Invalid or expired timestamp");
            }

            // 3. Processar o callback
            callbackReceiverService.processCallback(rawBody, id);

            logger.info("✅ Callback processado com sucesso");
            return ResponseEntity.ok("Callback received successfully");

        } catch (Exception e) {
            logger.error("❌ Erro ao processar callback: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error processing callback: " + e.getMessage());
        }
    }

    private boolean isValidSignature(String timestamp, String signature, String body) {
        try {
            // Remover prefixo "v1=" se existir
            String receivedSignature = signature.replace("v1=", "");

            String signingString = timestamp + "." + body;
            String calculatedSignature = HmacUtils.generateHmacSHA256(signingString, SECRET_KEY);

            boolean isValid = calculatedSignature.equals(receivedSignature);

            if (!isValid) {
                logger.warn("⚠️ Assinatura não corresponde. Esperado: {}, Recebido: {}",
                        calculatedSignature, receivedSignature);
            }

            return isValid;
        } catch (Exception e) {
            logger.error("Erro ao validar assinatura: {}", e.getMessage(), e);
            return false;
        }
    }

    private boolean isValidTimestamp(String timestampHeader) {
        try {
            long timestamp = Long.parseLong(timestampHeader);
            long currentTime = Instant.now().getEpochSecond();
            long difference = Math.abs(currentTime - timestamp);

            // Aceitar apenas timestamps dentro de 5 minutos (300 segundos)
            boolean isValid = difference <= 300;

            if (!isValid) {
                logger.warn("⚠️ Timestamp fora da janela válida. Diferença: {} segundos", difference);
            }

            return isValid;
        } catch (NumberFormatException e) {
            logger.error("Timestamp inválido: {}", timestampHeader);
            return false;
        }
    }
}