package com.example.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import software.amazon.awssdk.services.cloudfront.CloudFrontClient;
import software.amazon.awssdk.services.cloudfront.model.CreateInvalidationRequest;
import software.amazon.awssdk.services.cloudfront.model.CreateInvalidationResponse;

@RestController
public class CloudFrontController {

    private final CloudFrontClient cloudFrontClient;

    public CloudFrontController(CloudFrontClient cloudFrontClient) {
        this.cloudFrontClient = cloudFrontClient;
    }

    @GetMapping("/cloudfront/invalidate")
    public String invalidateCache(@RequestParam String path) {
        CreateInvalidationRequest request = CreateInvalidationRequest.builder()
                .distributionId("YOUR_CLOUDFRONT_DISTRIBUTION_ID")
                .invalidationBatch(builder -> builder
                        .paths(paths -> paths.items(path).quantity(1))
                        .callerReference("invalidate-" + System.currentTimeMillis()))
                .build();

        CreateInvalidationResponse response = cloudFrontClient.createInvalidation(request);
        return "Invalidation for path " + path + " created with ID: " + response.invalidation().id();
    }
}
