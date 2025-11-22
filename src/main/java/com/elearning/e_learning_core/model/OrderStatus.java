package com.elearning.e_learning_core.model;

public enum OrderStatus {
    PENDING("Pendente"),
    PROOF_UPLOADED("Comprovativo Enviado"),
    APPROVED("Aprovado"),
    REJECTED("Rejeitado");

    private final String displayName;

    OrderStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
