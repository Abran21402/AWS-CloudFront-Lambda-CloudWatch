package com.example.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import software.amazon.awssdk.core.SdkBytes;
import software.amazon.awssdk.services.lambda.LambdaClient;
import software.amazon.awssdk.services.lambda.model.InvokeRequest;
import software.amazon.awssdk.services.lambda.model.InvokeResponse;

import java.util.Map;

@RestController
public class LambdaController {

    private final LambdaClient lambdaClient;

    public LambdaController(LambdaClient lambdaClient) {
        this.lambdaClient = lambdaClient;
    }

    @PostMapping("/lambda/invoke")
    public String invokeLambda(@RequestBody Map<String, Object> payload) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            String payloadJson = objectMapper.writeValueAsString(payload);

            InvokeRequest request = InvokeRequest.builder()
                    .functionName("YOUR_LAMBDA_FUNCTION_NAME")
                    .payload(SdkBytes.fromUtf8String(payloadJson))
                    .build();

            InvokeResponse response = lambdaClient.invoke(request);
            return "Lambda Response: " + new String(response.payload().asByteArray());
        } catch (Exception e) {
            return "Failed to invoke Lambda: " + e.getMessage();
        }
    }
}
