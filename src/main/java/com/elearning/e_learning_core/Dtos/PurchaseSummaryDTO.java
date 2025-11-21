package com.elearning.e_learning_core.Dtos;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.elearning.e_learning_core.model.Purchase;

public record PurchaseSummaryDTO(
        Long id,
        String studentName,
        BigDecimal amount,
        LocalDateTime paymentDate,

        String paymentMethodName) {

    public PurchaseSummaryDTO(Purchase p) {
        this(
                p.getId(),

                p.getStudent().getFirstName() + " " + p.getStudent().getLastName(),

                p.getCourses()
                        .stream()
                        .map(c -> c.getPrice() == null ? BigDecimal.ZERO : c.getPrice())
                        .reduce(BigDecimal.ZERO, BigDecimal::add),

                p.getDataCompra(),

                p.getPaymentMethod().getTipo());
    }
}