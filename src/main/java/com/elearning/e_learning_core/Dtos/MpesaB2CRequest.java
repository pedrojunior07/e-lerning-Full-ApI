package com.elearning.e_learning_core.Dtos;

/**
 * DTO for M-Pesa Business to Customer (B2C) payment request
 */
public class MpesaB2CRequest {
    
    private String phoneNumber;
    private Double amount;
    private String reference;
    private String description;
    
    public MpesaB2CRequest() {
    }
    
    public MpesaB2CRequest(String phoneNumber, Double amount, String reference, String description) {
        this.phoneNumber = phoneNumber;
        this.amount = amount;
        this.reference = reference;
        this.description = description;
    }
    
    public String getPhoneNumber() {
        return phoneNumber;
    }
    
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
    
    public Double getAmount() {
        return amount;
    }
    
    public void setAmount(Double amount) {
        this.amount = amount;
    }
    
    public String getReference() {
        return reference;
    }
    
    public void setReference(String reference) {
        this.reference = reference;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
}
