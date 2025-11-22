package com.elearning.e_learning_core.Dtos;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.elearning.e_learning_core.model.OrderStatus;
import com.elearning.e_learning_core.model.WalletType;

public record CourseOrderDto(
    Long id,
    Long studentId,
    String studentName,
    Long courseId,
    String courseTitle,
    String courseThumbnail,
    OrderStatus status,
    WalletType paymentMethod,
    BigDecimal amount,
    String proofOfPaymentUrl,
    LocalDateTime orderDate,
    LocalDateTime validatedDate,
    String rejectionReason,
    PaymentWalletDto selectedWallet
) {}
