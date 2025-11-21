package com.elearning.e_learning_core.service;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.elearning.e_learning_core.Dtos.ApiResponse;
import com.elearning.e_learning_core.Dtos.Transaction;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class PaymentService {

    private static final Logger logger = LoggerFactory.getLogger(PaymentService.class);
    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public ApiResponse<JsonNode> createPayment(Transaction request) {
        String url = "http://192.250.224.214:8090/api/payments";
        String key = "Ya96CxD-hhGHH7W5MZKs2u_fUUSJwHNstUziJ7kjw5U";

        logger.info("Iniciando criação de pagamento para: {}", request.toString());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("X-API-KEY", key);

        HttpEntity<Transaction> entity = new HttpEntity<>(request, headers);

        try {
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
            logger.info("Resposta da API: status={}, body={}", response.getStatusCode(), response.getBody());

            if (response.getStatusCode().is2xxSuccessful()) {
                JsonNode jsonBody = objectMapper.readTree(response.getBody());
                logger.info("Pagamento criado com sucesso.");
                return new ApiResponse<>("success", "Pagamento enviado com sucesso", 200, jsonBody);
            } else {
                logger.warn("Falha ao enviar pagamento. Status HTTP: {}", response.getStatusCodeValue());
                return new ApiResponse<>("error", "Falha ao enviar pagamento: " + response.getStatusCodeValue(),
                        response.getStatusCodeValue(), null);
            }
        } catch (Exception e) {
            logger.error("Erro ao enviar pagamento", e);
            return new ApiResponse<>("error", "Erro ao enviar pagamento: " + e.getMessage(), 500, null);
        }
    }

    public ApiResponse<?> authorize(String id) {
        String url = "http://192.250.224.214:8090/api/payments/" + id + "/validate";
        String key = "Ya96CxD-hhGHH7W5MZKs2u_fUUSJwHNstUziJ7kjw5U";

        logger.info("Autorizando pagamento com ID: {}", id);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("X-API-KEY", key);

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        try {
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
            logger.debug("Resposta da API de autorização: status={}, body={}", response.getStatusCode(),
                    response.getBody());

            if (response.getStatusCode().is2xxSuccessful()) {
                JsonNode jsonBody = objectMapper.readTree(response.getBody());
                logger.info("Pagamento confirmado com sucesso.");
                return new ApiResponse<>("success", "Pagamento confirmado com sucesso", 200, jsonBody);
            } else {
                logger.warn("Falha ao confirmar pagamento. Status HTTP: {}", response.getStatusCodeValue());
                return new ApiResponse<>("error", "Falha ao confirmar pagamento: " + response.getStatusCodeValue(),
                        response.getStatusCodeValue(), null);
            }
        } catch (Exception e) {
            logger.error("Erro ao confirmar pagamento", e);
            return new ApiResponse<>("error", "Erro ao confirmar pagamento: " + e.getMessage(), 500, null);
        }
    }
}
