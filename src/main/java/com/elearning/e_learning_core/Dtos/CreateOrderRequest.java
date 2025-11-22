package com.elearning.e_learning_core.Dtos;

import com.elearning.e_learning_core.model.WalletType;

public record CreateOrderRequest(
    Long courseId,
    Long studentId,
    WalletType paymentMethod,
    Long walletId
) {}
