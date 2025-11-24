package com.elearning.e_learning_core.Dtos;

/**
 * DTO for M-Pesa callback request
 */
public class MpesaCallbackRequest {
    
    private String transactionID;
    private String conversationID;
    private String thirdPartyReference;
    private String resultCode;
    private String resultDescription;
    private Double amount;
    private String msisdn;
    
    public MpesaCallbackRequest() {
    }
    
    public String getTransactionID() {
        return transactionID;
    }
    
    public void setTransactionID(String transactionID) {
        this.transactionID = transactionID;
    }
    
    public String getConversationID() {
        return conversationID;
    }
    
    public void setConversationID(String conversationID) {
        this.conversationID = conversationID;
    }
    
    public String getThirdPartyReference() {
        return thirdPartyReference;
    }
    
    public void setThirdPartyReference(String thirdPartyReference) {
        this.thirdPartyReference = thirdPartyReference;
    }
    
    public String getResultCode() {
        return resultCode;
    }
    
    public void setResultCode(String resultCode) {
        this.resultCode = resultCode;
    }
    
    public String getResultDescription() {
        return resultDescription;
    }
    
    public void setResultDescription(String resultDescription) {
        this.resultDescription = resultDescription;
    }
    
    public Double getAmount() {
        return amount;
    }
    
    public void setAmount(Double amount) {
        this.amount = amount;
    }
    
    public String getMsisdn() {
        return msisdn;
    }
    
    public void setMsisdn(String msisdn) {
        this.msisdn = msisdn;
    }
}
