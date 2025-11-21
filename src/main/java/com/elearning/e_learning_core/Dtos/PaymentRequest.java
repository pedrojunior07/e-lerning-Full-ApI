package com.elearning.e_learning_core.Dtos;

import java.math.BigDecimal;

public class PaymentRequest {

    private BigDecimal amount;
    private String customer_phone;
    private String external_transaction_id;
    private String description;

    private String currency;
    private String customerId;

    public PaymentRequest(BigDecimal amount, String customer_phone, String external_transaction_id,
            String description) {
        this.amount = amount;
        this.customer_phone = customer_phone;
        this.external_transaction_id = external_transaction_id;
        this.description = description;
    }

    public PaymentRequest() {
        // TODO Auto-generated constructor stub
    }

    public PaymentRequest(BigDecimal amount, String customer_phone, String external_transaction_id, String description,
            String currency, String customerId) {
        this.amount = amount;
        this.customer_phone = customer_phone;
        this.external_transaction_id = external_transaction_id;
        this.description = description;
        this.currency = currency;
        this.customerId = customerId;
    }

    public String getCurrency() {
        return currency;
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

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getCustomer_phone() {
        return customer_phone;
    }

    public void setCustomer_phone(String customer_phone) {
        this.customer_phone = customer_phone;
    }

    public String getExternal_transaction_id() {
        return external_transaction_id;
    }

    public void setExternal_transaction_id(String external_transaction_id) {
        this.external_transaction_id = external_transaction_id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
