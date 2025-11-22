package com.elearning.e_learning_core.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.elearning.e_learning_core.Dtos.ApiResponse;
import com.elearning.e_learning_core.Dtos.PaymentRequest;
import com.elearning.e_learning_core.Dtos.PurchaseDTO;
import com.elearning.e_learning_core.Dtos.Transaction;
import com.elearning.e_learning_core.Repository.CourseRepository;
import com.elearning.e_learning_core.Repository.PurchaseRepository;
import com.elearning.e_learning_core.Repository.PersonRepository;
import com.elearning.e_learning_core.config.CallbackConfig;
import com.elearning.e_learning_core.model.Course;
import com.elearning.e_learning_core.model.PaymentMethod;
import com.elearning.e_learning_core.model.Purchase;
import com.elearning.e_learning_core.model.enums.PaymentStatus;
import com.fasterxml.jackson.databind.JsonNode;
import com.elearning.e_learning_core.model.Person;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class PurchaseService {

    private static final Logger log = LoggerFactory.getLogger(PurchaseService.class);

    private final PurchaseRepository purchaseRepository;
    private final CallbackConfig callbackConfig;

    public PurchaseService(PurchaseRepository purchaseRepository, CallbackConfig callbackConfig) {
        this.purchaseRepository = purchaseRepository;
        this.callbackConfig = callbackConfig;
    }

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private PaymentService paymentService;

    public ApiResponse<?> purchaseSave(PurchaseDTO dto, Long personId) {
        log.info("Iniciando processo de compra para personId={}", personId);
        ApiResponse<JsonNode> paymentResponse;
        Person person = personRepository.findById(personId)
                .orElseThrow(() -> {
                    log.error("Estudante com ID {} não encontrado", personId);
                    return new RuntimeException("Student not found");
                });

        List<Course> courses = new ArrayList<>();
        BigDecimal amount = BigDecimal.ZERO;

        for (Long courseId : dto.getCourseIds()) {
            Course course = courseRepository.findById(courseId)
                    .orElseThrow(() -> {
                        log.error("Curso com ID {} não encontrado", courseId);
                        return new RuntimeException("Course not found");
                    });
            courses.add(course);
            amount = amount.add(course.getPrice());
        }

        log.info("Total a pagar calculado: {} Mts", amount);

        // Gerar externalId único para a compra
        String externalId = UUID.randomUUID().toString();

        // Construir a URL de callback

        PaymentRequest paymentRequest = new PaymentRequest();
        paymentRequest.setAmount(amount);
        paymentRequest.setCurrency("Mts");
        paymentRequest.setCustomerId("IQIQIQIQIQI");
        paymentRequest.setCustomer_phone(dto.getPaymentMethod().getPhone());
        paymentRequest.setExternal_transaction_id(externalId);
        paymentRequest.setDescription("Compra de curso na e-learning");
        Purchase purchase = new Purchase();
        purchase.setStudent(person);
        purchase.setCourses(courses);
        purchase.setExternalId(externalId);
        purchase.setAmount(amount);
        purchase.setPaymentStatus("PENDING");
        purchase.setDataCompra(LocalDateTime.now());
        purchase = purchaseRepository.save(purchase);
        log.info("Compra salva com ID: {} e externalId: {}", purchase.getId(), externalId);
        String callbackUrl = buildCallbackUrl(purchase.getId().toString());
        Transaction transaction = new Transaction(
                amount,
                "mts",
                externalId,
                new com.elearning.e_learning_core.Dtos.PaymentMethodDTO("MPESA", paymentRequest.getCustomer_phone()),
                callbackUrl);

        try {
            log.info("Enviando solicitação de pagamento para o serviço externo...");
            paymentResponse = paymentService.createPayment(transaction);

            purchase.setExternalId(paymentResponse.getData().get("id").textValue());

            if (paymentResponse.getCode() != 200) {
                log.warn("Pagamento recusado: {}", paymentResponse.getMessage());
                // Atualizar status para falha
                purchase.setPaymentStatus("FAILED");
                purchaseRepository.save(purchase);
                return new ApiResponse<>("Erro ao processar pagamento", paymentResponse.getMessage(), 400, null);
            }

            log.info("Pagamento autorizado com sucesso.");

            // Atualizar status para processando
            purchase.setPaymentStatus(PaymentStatus.PENDING.toString());
            purchaseRepository.save(purchase);

        } catch (IllegalArgumentException e) {
            log.error("Erro ao comunicar com serviço de pagamento: {}", e.getMessage());
            purchase.setPaymentStatus("FAILED");
            purchaseRepository.save(purchase);
            return new ApiResponse<>("Falha na comunicação com serviço de pagamento", e.getMessage(), 500, null);
        }

        log.info("Compra registrada com sucesso. ID da compra: {}", purchase.getId());

        return new ApiResponse<>("success", "Compra realizada com sucesso", 200,
                new PurchaseResponse(paymentResponse.getData().get("id").textValue(), externalId, callbackUrl));
    }

    private String buildCallbackUrl(String externalId) {
        return callbackConfig.getPaymentCallbackUrl() + "/" + externalId;
    }

    // Classe interna para resposta
    private static class PurchaseResponse {
        private String purchaseId;
        private String externalId;
        private String callbackUrl;

        public PurchaseResponse(String purchaseId, String externalId, String callbackUrl) {
            this.purchaseId = purchaseId;
            this.externalId = externalId;
            this.callbackUrl = callbackUrl;
        }

        // Getters
        public String getPurchaseId() {
            return purchaseId;
        }

        public String getExternalId() {
            return externalId;
        }

        public String getCallbackUrl() {
            return callbackUrl;
        }
    }

    public List<PurchaseDTO> listarCompras() {
        log.info("Listando todas as compras...");
        return purchaseRepository.findAll().stream()
                .map(purchase -> new PurchaseDTO(
                        purchase.getId(),
                        purchase.getAmount(),
                        purchase.getDataCompra(),
                        "M-PESA",
                        purchase.getStudent() != null
                                ? purchase.getStudent().getFirstName() + " " + purchase.getStudent().getLastName()
                                : null))
                .collect(Collectors.toList());
    }

    public List<PurchaseDTO> getStudentPurchases(Long studentId) {
        log.info("Listando compras do estudante {}", studentId);
        return purchaseRepository.findByStudentId(studentId).stream()
                .map(purchase -> new PurchaseDTO(
                        purchase.getId(),
                        purchase.getAmount(),
                        purchase.getDataCompra(),
                        "M-PESA",
                        purchase.getStudent() != null
                                ? purchase.getStudent().getFirstName() + " " + purchase.getStudent().getLastName()
                                : null))
                .collect(Collectors.toList());
    }

    public ApiResponse<?> validate(String id) {
        log.info("Validando pagamento com externalId={}", id);

        try {
            ApiResponse<?> paymentResponse = paymentService.authorize(id);

            if (paymentResponse.getCode() != 200) {
                log.warn("Autorização de pagamento falhou: {}", paymentResponse.getMessage());
                return new ApiResponse<>("Erro ao processar pagamento", paymentResponse.getMessage(), 400, null);
            }

            log.info("Pagamento autorizado com sucesso para externalId={}", id);
        } catch (IllegalArgumentException e) {
            log.error("Erro ao autorizar pagamento: {}", e.getMessage());
            return new ApiResponse<>("Falha na comunicação com serviço de pagamento", e.getMessage(), 500, null);
        }

        Purchase purchase = purchaseRepository.findByExternalId(id

        );
        if (purchase == null) {
            log.warn("Compra não encontrada com externalId={}", id);
            return new ApiResponse<>("Compra não encontrada", null, 404, null);
        }

        purchase.setPaymentStatus(PaymentStatus.PROCESSING.toString());
        purchase.setDataCompra(LocalDateTime.now());

        purchaseRepository.save(purchase);

        log.info("Status de pagamento atualizado para PROCESSING. ID da compra: {}", purchase.getId());

        return new ApiResponse<>("success", "Compra validada com sucesso", 200, purchase.getId());
    }
}