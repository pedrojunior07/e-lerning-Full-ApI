package com.elearning.e_learning_core.Dtos;

import com.elearning.e_learning_core.model.WalletType;

public record PaymentWalletDto(
    Long id,
    WalletType walletType,
    String accountName,
    String accountNumber,
    String bankName,
    boolean active
) {}
