package com.elearning.e_learning_core.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CallbackConfig {

    @Value("${app.callback.base-url:http://localhost:9090/e-learning/api/callbacks}")
    private String callbackBaseUrl;

    public String getCallbackBaseUrl() {
        return callbackBaseUrl;
    }

    public String getPaymentCallbackUrl() {
        return callbackBaseUrl + "/payment";
    }

    public String getPurchaseCallbackUrl() {
        return callbackBaseUrl + "/purchase";
    }
}