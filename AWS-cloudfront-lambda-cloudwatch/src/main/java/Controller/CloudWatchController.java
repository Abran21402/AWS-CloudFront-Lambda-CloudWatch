package com.example.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import software.amazon.awssdk.services.cloudwatch.CloudWatchClient;
import software.amazon.awssdk.services.cloudwatch.model.MetricDatum;
import software.amazon.awssdk.services.cloudwatch.model.PutMetricDataRequest;
import software.amazon.awssdk.services.cloudwatch.model.StandardUnit;

@RestController
public class CloudWatchController {

    private final CloudWatchClient cloudWatchClient;

    public CloudWatchController(CloudWatchClient cloudWatchClient) {
        this.cloudWatchClient = cloudWatchClient;
    }

    @GetMapping("/cloudwatch/metric")
    public String publishMetric(@RequestParam String metricName, @RequestParam double value) {
        PutMetricDataRequest request = PutMetricDataRequest.builder()
                .namespace("ProductUpdateMetrics")
                .metricData(MetricDatum.builder()
                        .metricName(metricName)
                        .value(value)
                        .unit(StandardUnit.COUNT)
                        .build())
                .build();

        cloudWatchClient.putMetricData(request);
        return "Metric " + metricName + " with value " + value + " published!";
    }
}
