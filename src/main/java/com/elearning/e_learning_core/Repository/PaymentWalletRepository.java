package com.elearning.e_learning_core.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.elearning.e_learning_core.model.PaymentWallet;
import com.elearning.e_learning_core.model.WalletType;

@Repository
public interface PaymentWalletRepository extends JpaRepository<PaymentWallet, Long> {
    List<PaymentWallet> findByInstructorIdAndActiveTrue(Long instructorId);
    List<PaymentWallet> findByInstructorId(Long instructorId);
    List<PaymentWallet> findByInstructorIdAndWalletTypeAndActiveTrue(Long instructorId, WalletType walletType);
}
