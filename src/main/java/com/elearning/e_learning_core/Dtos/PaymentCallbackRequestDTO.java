package com.elearning.e_learning_core.Dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.OffsetDateTime;
import java.util.Map;

public class PaymentCallbackRequestDTO {

    @JsonProperty("package")
    private String packageId;

    private String amount;
    private String currency;
    private String status;
    private OffsetDateTime statusChangedAt;
    private Map<String, String> metadata;

    // Construtores
    public PaymentCallbackRequestDTO() {
    }

    public PaymentCallbackRequestDTO(String packageId, String amount, String currency,
            String status, OffsetDateTime statusChangedAt,
            Map<String, String> metadata) {
        this.packageId = packageId;
        this.amount = amount;
        this.currency = currency;
        this.status = status;
        this.statusChangedAt = statusChangedAt;
        this.metadata = metadata;
    }

    // Getters e Setters
    public String getPackageId() {
        return packageId;
    }

    public void setPackageId(String packageId) {
        this.packageId = packageId;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public OffsetDateTime getStatusChangedAt() {
        return statusChangedAt;
    }

    public void setStatusChangedAt(OffsetDateTime statusChangedAt) {
        this.statusChangedAt = statusChangedAt;
    }

    public Map<String, String> getMetadata() {
        return metadata;
    }

    public void setMetadata(Map<String, String> metadata) {
        this.metadata = metadata;
    }

    @Override
    public String toString() {
        return "PaymentCallbackRequestDTO{" +
                "packageId='" + packageId + '\'' +
                ", amount='" + amount + '\'' +
                ", currency='" + currency + '\'' +
                ", status='" + status + '\'' +
                ", statusChangedAt=" + statusChangedAt +
                ", metadata=" + metadata +
                '}';
    }
}