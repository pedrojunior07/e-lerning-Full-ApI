package com.elearning.e_learning_core.service;

import com.elearning.e_learning_core.Dtos.PaymentCallbackRequestDTO;
import com.elearning.e_learning_core.Repository.PaymentRepository;
import com.elearning.e_learning_core.Repository.PurchaseRepository;
import com.elearning.e_learning_core.model.Payment;
import com.elearning.e_learning_core.model.Purchase;
import com.elearning.e_learning_core.model.enums.PaymentStatus;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class PaymentCallbackReceiverService {

    private static final Logger logger = LoggerFactory.getLogger(PaymentCallbackReceiverService.class);

    private final PaymentRepository paymentRepository;
    private final ObjectMapper objectMapper;
    private final PurchaseRepository purchaseRepository;

    public PaymentCallbackReceiverService(PaymentRepository paymentRepository,
            ObjectMapper objectMapper, PurchaseRepository purchaseRepository) {
        this.paymentRepository = paymentRepository;
        this.purchaseRepository = purchaseRepository;
        this.objectMapper = objectMapper;
    }

    @Transactional
    public void processCallback(String rawBody, String id) {
        try {
            // Converter JSON para DTO
            PaymentCallbackRequestDTO callbackDTO = objectMapper.readValue(
                    rawBody, PaymentCallbackRequestDTO.class);

            logger.info("🔍 Processando callback para package: {}", callbackDTO.getPackageId());
            logger.debug("📋 Dados do callback: {}", callbackDTO);

            // Extrair paymentId do metadata
            String paymentId = id;
            if (paymentId == null) {
                logger.error("❌ PaymentId não encontrado no metadata");
                throw new IllegalArgumentException("PaymentId not found in metadata");
            }

            // Buscar o pagamento
            Optional<Purchase> paymentOpt = purchaseRepository.findById(Long.parseLong(paymentId));
            if (paymentOpt.isEmpty()) {
                logger.error("❌ Pagamento não encontrado: {}", paymentId);
                throw new IllegalArgumentException("Payment not found: " + paymentId);
            }

            Purchase payment = paymentOpt.get();

            // Atualizar status do pagamento
            updatePaymentStatus(payment, callbackDTO.getStatus());

            // Salvar alterações
            purchaseRepository.save(payment);

        } catch (Exception e) {
            logger.error("❌ Erro ao processar callback: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to process callback", e);
        }
    }

    private String extractPaymentId(PaymentCallbackRequestDTO callbackDTO) {
        if (callbackDTO.getMetadata() != null) {
            return callbackDTO.getMetadata().get("paymentId");
        }
        return null;
    }

    private void updatePaymentStatus(Purchase payment, String status) {
        try {
            PaymentStatus newStatus = PaymentStatus.valueOf(status.toUpperCase());
            payment.setPaymentStatus(status);
            logger.debug("🔄 Status do pagamento {} alterado para: {}",
                    payment.getId(), newStatus);
        } catch (IllegalArgumentException e) {
            logger.error("❌ Status inválido recebido: {}", status);
            throw new IllegalArgumentException("Invalid payment status: " + status);
        }
    }
}