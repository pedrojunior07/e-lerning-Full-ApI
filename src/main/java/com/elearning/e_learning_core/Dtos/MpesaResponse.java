package com.elearning.e_learning_core.Dtos;

/**
 * DTO for M-Pesa API response
 */
public class MpesaResponse {
    
    private String transactionID;
    private String conversationID;
    private String responseCode;
    private String responseDescription;
    private String thirdPartyReference;
    
    public MpesaResponse() {
    }
    
    public MpesaResponse(String transactionID, String conversationID, String responseCode, 
                        String responseDescription, String thirdPartyReference) {
        this.transactionID = transactionID;
        this.conversationID = conversationID;
        this.responseCode = responseCode;
        this.responseDescription = responseDescription;
        this.thirdPartyReference = thirdPartyReference;
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
    
    public String getResponseCode() {
        return responseCode;
    }
    
    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }
    
    public String getResponseDescription() {
        return responseDescription;
    }
    
    public void setResponseDescription(String responseDescription) {
        this.responseDescription = responseDescription;
    }
    
    public String getThirdPartyReference() {
        return thirdPartyReference;
    }
    
    public void setThirdPartyReference(String thirdPartyReference) {
        this.thirdPartyReference = thirdPartyReference;
    }
}
