package com.elearning.e_learning_core.Dtos;

/**
 * DTO for M-Pesa Customer to Business (C2B) payment request
 */
public class MpesaC2BRequest {
    
    private String phoneNumber;
    private Double amount;
    private String reference;
    private String description;
    
    public MpesaC2BRequest() {
    }
    
    public MpesaC2BRequest(String phoneNumber, Double amount, String reference, String description) {
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
