package com.elearning.e_learning_core.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.elearning.e_learning_core.Dtos.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * Service for integrating with M-Pesa API
 * Supports C2B (Customer to Business) and B2C (Business to Customer) transactions
 */
@Service
public class MpesaService {

    private static final Logger logger = LoggerFactory.getLogger(MpesaService.class);
    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Value("${mpesa.api.base-url}")
    private String baseUrl;

    @Value("${mpesa.api.key}")
    private String apiKey;

    @Value("${mpesa.organization.name}")
    private String organizationName;

    @Value("${mpesa.organization.sortcode}")
    private String organizationSortCode;

    @Value("${mpesa.initiator.identifier}")
    private String initiatorIdentifier;

    @Value("${mpesa.security.credential}")
    private String securityCredential;

    @Value("${mpesa.callback.url}")
    private String callbackUrl;

    @Value("${mpesa.timeout.url}")
    private String timeoutUrl;

    /**
     * Process a C2B (Customer to Business) payment
     * Customer pays to the business
     */
    public ApiResponse<MpesaResponse> processC2BPayment(MpesaC2BRequest request) {
        String url = baseUrl + "/ipg/v1x/c2bPayment/singleStage/";

        logger.info("=== Iniciando pagamento C2B ===");
        logger.info("URL: {}", url);
        logger.info("Telefone: {}", request.getPhoneNumber());
        logger.info("Valor: {}", request.getAmount());
        logger.info("Referencia: {}", request.getReference());

        try {
            ObjectNode requestBody = objectMapper.createObjectNode();
            requestBody.put("input_ServiceProviderCode", organizationSortCode);
            requestBody.put("input_CustomerMSISDN", formatPhoneNumber(request.getPhoneNumber()));
            requestBody.put("input_Amount", request.getAmount().toString());
            requestBody.put("input_TransactionReference", request.getReference());
            requestBody.put("input_ThirdPartyReference", request.getReference());

            logger.info("Request Body: {}", requestBody.toString());
            logger.info("API Key (primeiros 10 chars): {}...", apiKey.substring(0, Math.min(10, apiKey.length())));
            logger.info("Service Provider Code: {}", organizationSortCode);

            HttpHeaders headers = createHeaders();
            HttpEntity<String> entity = new HttpEntity<>(requestBody.toString(), headers);

            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);

            logger.info("=== Resposta M-Pesa C2B ===");
            logger.info("Status: {}", response.getStatusCode());
            logger.info("Body: {}", response.getBody());

            if (response.getStatusCode().is2xxSuccessful()) {
                MpesaResponse mpesaResponse = parseMpesaResponse(response.getBody());
                return new ApiResponse<>("success", "Pagamento C2B iniciado com sucesso", 200, mpesaResponse);
            } else {
                logger.warn("Falha no pagamento C2B. Status: {}", response.getStatusCodeValue());
                return new ApiResponse<>("error", "Falha ao processar pagamento C2B", response.getStatusCodeValue(), null);
            }
        } catch (org.springframework.web.client.HttpClientErrorException e) {
            logger.error("=== Erro HTTP ao processar pagamento C2B ===");
            logger.error("Status Code: {}", e.getStatusCode());
            logger.error("Response Body: {}", e.getResponseBodyAsString());
            logger.error("Headers: {}", e.getResponseHeaders());
            return new ApiResponse<>("error", "Erro ao processar pagamento: " + e.getStatusCode() + " " + e.getResponseBodyAsString(), e.getStatusCode().value(), null);
        } catch (Exception e) {
            logger.error("Erro ao processar pagamento C2B: {}", e.getMessage(), e);
            return new ApiResponse<>("error", "Erro ao processar pagamento: " + e.getMessage(), 500, null);
        }
    }

    /**
     * Process a B2C (Business to Customer) payment
     * Business pays to the customer
     */
    public ApiResponse<MpesaResponse> processB2CPayment(MpesaB2CRequest request) {
        String url = baseUrl + "/ipg/v1x/b2cPayment/";

        logger.info("Iniciando pagamento B2C para: {}", request.getPhoneNumber());

        try {
            ObjectNode requestBody = objectMapper.createObjectNode();
            requestBody.put("input_ServiceProviderCode", organizationSortCode);
            requestBody.put("input_InitiatorIdentifier", initiatorIdentifier);
            requestBody.put("input_SecurityCredential", securityCredential);
            requestBody.put("input_CustomerMSISDN", formatPhoneNumber(request.getPhoneNumber()));
            requestBody.put("input_Amount", request.getAmount().toString());
            requestBody.put("input_TransactionReference", request.getReference());
            requestBody.put("input_ThirdPartyReference", request.getReference());
            requestBody.put("input_CommandID", "BusinessPayment");

            HttpHeaders headers = createHeaders();
            HttpEntity<String> entity = new HttpEntity<>(requestBody.toString(), headers);

            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);

            logger.info("Resposta M-Pesa B2C: status={}, body={}", response.getStatusCode(), response.getBody());

            if (response.getStatusCode().is2xxSuccessful()) {
                MpesaResponse mpesaResponse = parseMpesaResponse(response.getBody());
                return new ApiResponse<>("success", "Pagamento B2C iniciado com sucesso", 200, mpesaResponse);
            } else {
                logger.warn("Falha no pagamento B2C. Status: {}", response.getStatusCodeValue());
                return new ApiResponse<>("error", "Falha ao processar pagamento B2C", response.getStatusCodeValue(), null);
            }
        } catch (Exception e) {
            logger.error("Erro ao processar pagamento B2C: {}", e.getMessage(), e);
            return new ApiResponse<>("error", "Erro ao processar pagamento: " + e.getMessage(), 500, null);
        }
    }

    /**
     * Query transaction status
     */
    public ApiResponse<MpesaResponse> queryTransactionStatus(String transactionID, String thirdPartyReference) {
        String url = baseUrl + "/ipg/v1x/queryTransactionStatus/";

        logger.info("Consultando status da transação: {}", transactionID);

        try {
            ObjectNode requestBody = objectMapper.createObjectNode();
            requestBody.put("input_QueryReference", transactionID);
            requestBody.put("input_ThirdPartyReference", thirdPartyReference);
            requestBody.put("input_ServiceProviderCode", organizationSortCode);

            HttpHeaders headers = createHeaders();
            HttpEntity<String> entity = new HttpEntity<>(requestBody.toString(), headers);

            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);

            logger.info("Status da transação: {}", response.getBody());

            if (response.getStatusCode().is2xxSuccessful()) {
                MpesaResponse mpesaResponse = parseMpesaResponse(response.getBody());
                return new ApiResponse<>("success", "Status consultado com sucesso", 200, mpesaResponse);
            } else {
                return new ApiResponse<>("error", "Falha ao consultar status", response.getStatusCodeValue(), null);
            }
        } catch (Exception e) {
            logger.error("Erro ao consultar status: {}", e.getMessage(), e);
            return new ApiResponse<>("error", "Erro ao consultar status: " + e.getMessage(), 500, null);
        }
    }

    /**
     * Create HTTP headers with API key
     */
    private HttpHeaders createHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + apiKey);
        return headers;
    }

    /**
     * Format phone number to M-Pesa format (258XXXXXXXXX)
     */
    private String formatPhoneNumber(String phoneNumber) {
        phoneNumber = phoneNumber.replaceAll("[^0-9]", "");

        if (phoneNumber.startsWith("258")) {
            return phoneNumber;
        } else if (phoneNumber.startsWith("84") || phoneNumber.startsWith("85") ||
                   phoneNumber.startsWith("86") || phoneNumber.startsWith("87")) {
            return "258" + phoneNumber;
        }

        return phoneNumber;
    }

    /**
     * Parse M-Pesa API response
     */
    private MpesaResponse parseMpesaResponse(String responseBody) throws Exception {
        JsonNode jsonNode = objectMapper.readTree(responseBody);

        MpesaResponse response = new MpesaResponse();
        response.setTransactionID(jsonNode.has("output_TransactionID") ?
            jsonNode.get("output_TransactionID").asText() : null);
        response.setConversationID(jsonNode.has("output_ConversationID") ?
            jsonNode.get("output_ConversationID").asText() : null);
        response.setResponseCode(jsonNode.has("output_ResponseCode") ?
            jsonNode.get("output_ResponseCode").asText() : null);
        response.setResponseDescription(jsonNode.has("output_ResponseDesc") ?
            jsonNode.get("output_ResponseDesc").asText() : null);
        response.setThirdPartyReference(jsonNode.has("output_ThirdPartyReference") ?
            jsonNode.get("output_ThirdPartyReference").asText() : null);

        return response;
    }
}
