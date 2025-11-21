package com.elearning.e_learning_core.Dtos;

import java.math.BigDecimal;

public class Transaction {
    private BigDecimal amount;
    private String currency;
    private String customerId;
    private String callback;
    private PaymentMethodDTO method;

    // Construtor
    public Transaction(BigDecimal amount, String currency, String customerId, PaymentMethodDTO method,
            String callback) {
        this.amount = amount;
        this.currency = currency;
        this.customerId = customerId;
        this.method = method;
        this.callback = callback;
    }

    // Getters e setters
    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }

    public String getCallback() {
        return this.callback;
    }

    public void setCallback(String callback) {
        this.callback = callback;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public PaymentMethodDTO getMethod() {
        return method;
    }

    public void setMethod(PaymentMethodDTO method) {
        this.method = method;
    }

    @Override
    public String toString() {
        return "Transaction [amount=" + amount + ", currency=" + currency + ", customerId=" + customerId + ", method="
                + method.toString() + "]";
    }
}
