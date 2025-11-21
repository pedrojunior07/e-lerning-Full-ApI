package com.elearning.e_learning_core.Repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.elearning.e_learning_core.model.Payment;
import com.elearning.e_learning_core.model.enums.PaymentStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {

    // Buscar payment por externalId
    Optional<Payment> findByExternalId(String externalId);

    // Buscar payment por externalId ignorando case
    Optional<Payment> findByExternalIdIgnoreCase(String externalId);

    // Buscar payment com paymentMethod (EAGER loading)
    @Query("SELECT p FROM Payment p LEFT JOIN FETCH p.paymentMethod WHERE p.id = :id")
    Optional<Payment> findByIdWithMethod(@Param("id") Long id);


    // Verificar se existe payment por externalId
    boolean existsByExternalId(String externalId);

    // Buscar payments por status
    List<Payment> findByStatus(PaymentStatus status);

    // Buscar payments por status com paginação
    Page<Payment> findByStatus(PaymentStatus status, Pageable pageable);

    // Buscar payments por múltiplos status
    List<Payment> findByStatusIn(List<PaymentStatus> statuses);

    // Buscar payments por múltiplos status com paginação
    Page<Payment> findByStatusIn(List<PaymentStatus> statuses, Pageable pageable);

    // Buscar payments por customerEmail
    List<Payment> findByCustomerEmail(String customerEmail);

    // Buscar payments por customerEmail com paginação
    Page<Payment> findByCustomerEmail(String customerEmail, Pageable pageable);

    // Buscar payments por customerEmail e status
    List<Payment> findByCustomerEmailAndStatus(String customerEmail, PaymentStatus status);

    // Buscar payments criados após uma data específica
    List<Payment> findByCreatedAtAfter(LocalDateTime date);

    // Buscar payments criados antes de uma data específica
    List<Payment> findByCreatedAtBefore(LocalDateTime date);

    // Buscar payments entre duas datas
    List<Payment> findByCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate);

    // Buscar payments por status e entre duas datas
    List<Payment> findByStatusAndCreatedAtBetween(PaymentStatus status, LocalDateTime startDate, LocalDateTime endDate);

    // Buscar payments por amount maior que
    List<Payment> findByAmountGreaterThanEqual(Double amount);

    // Buscar payments por amount menor que
    List<Payment> findByAmountLessThanEqual(Double amount);

    // Buscar payments por amount entre dois valores
    List<Payment> findByAmountBetween(Double minAmount, Double maxAmount);

    // Buscar payments por currency
    List<Payment> findByCurrency(String currency);

    // Buscar payments por currency e status
    List<Payment> findByCurrencyAndStatus(String currency, PaymentStatus status);

    // Buscar payments que têm callbackUrl
    List<Payment> findByCallbackUrlIsNotNull();

    // Buscar payments que não têm callbackUrl
    List<Payment> findByCallbackUrlIsNull();

    // Buscar payments com failureReason (payments falhados com motivo)
    List<Payment> findByFailureReasonIsNotNull();

    // Contar payments por status
    long countByStatus(PaymentStatus status);

    // Contar payments por customerEmail
    long countByCustomerEmail(String customerEmail);

    // Contar payments criados após uma data
    long countByCreatedAtAfter(LocalDateTime date);

    // Buscar payments para callback (status finalizados com callbackUrl)
    @Query("SELECT p FROM Payment p WHERE p.callbackUrl IS NOT NULL AND p.status IN (:statuses)")
    List<Payment> findPaymentsForCallback(@Param("statuses") List<PaymentStatus> statuses);

    // Buscar payments pendentes de processamento
    @Query("SELECT p FROM Payment p WHERE p.status IN ('PENDING', 'PROCESSING') AND p.createdAt < :threshold")
    List<Payment> findStalePayments(@Param("threshold") LocalDateTime threshold);

    // Buscar payments com estatísticas por status
    @Query("SELECT p.status, COUNT(p) FROM Payment p GROUP BY p.status")
    List<Object[]> findPaymentCountByStatus();

    // Buscar payments com estatísticas por período
    @Query("SELECT FUNCTION('DATE', p.createdAt), COUNT(p) FROM Payment p WHERE p.createdAt BETWEEN :startDate AND :endDate GROUP BY FUNCTION('DATE', p.createdAt)")
    List<Object[]> findDailyPaymentCount(@Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);

    // Buscar total amount por status
    @Query("SELECT p.status, SUM(p.amount) FROM Payment p GROUP BY p.status")
    List<Object[]> findTotalAmountByStatus();

    // Buscar payments ordenados por createdAt (mais recentes primeiro)
    List<Payment> findByOrderByCreatedAtDesc();

    // Buscar payments por status ordenados por createdAt
    List<Payment> findByStatusOrderByCreatedAtDesc(PaymentStatus status);

    // Buscar top N payments mais recentes
    List<Payment> findTop10ByOrderByCreatedAtDesc();

    // Buscar payments com paginação ordenada
    Page<Payment> findAllByOrderByCreatedAtDesc(Pageable pageable);

    // Buscar payments por customerName (usando LIKE para busca parcial)
    @Query("SELECT p FROM Payment p WHERE LOWER(p.customerName) LIKE LOWER(CONCAT('%', :customerName, '%'))")
    List<Payment> findByCustomerNameContainingIgnoreCase(@Param("customerName") String customerName);

    // Buscar payments por descrição (usando LIKE para busca parcial)
    @Query("SELECT p FROM Payment p WHERE LOWER(p.description) LIKE LOWER(CONCAT('%', :description, '%'))")
    List<Payment> findByDescriptionContainingIgnoreCase(@Param("description") String description);

    // Buscar payments que precisam ser reprocessados (falhados com menos de X
    // tentativas)
    @Query("SELECT p FROM Payment p WHERE p.status = 'FAILED' AND p.updatedAt > :sinceDate")
    List<Payment> findFailedPaymentsForRetry(@Param("sinceDate") LocalDateTime sinceDate);

    // Deletar payments antigos (para cleanup)
    void deleteByCreatedAtBefore(LocalDateTime date);

    // Deletar payments por status antigos
    void deleteByStatusAndCreatedAtBefore(PaymentStatus status, LocalDateTime date);
}