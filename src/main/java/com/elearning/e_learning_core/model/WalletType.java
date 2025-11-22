package com.elearning.e_learning_core.model;

public enum WalletType {
    MPESA("M-Pesa"),
    EMOLA("E-Mola"),
    BANK("Banco");

    private final String displayName;

    WalletType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
