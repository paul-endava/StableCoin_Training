package com.stablecoin.training.chainadapter.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "eth.rpc")
public record EthRpcProperties(String baseUrl, int minConfirmations) {
    public EthRpcProperties {
        if (baseUrl == null || baseUrl.isBlank()) {
            baseUrl = "http://localhost:8545";
        }
        if (minConfirmations < 0) {
            minConfirmations = 12;
        }
    }
}
