package com.stablecoin.training.chainadapter.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class EthClientConfig {

    @Bean
    RestClient ethRestClient(EthRpcProperties ethRpcProperties) {
        return RestClient.builder()
            .baseUrl(ethRpcProperties.baseUrl())
            .build();
    }
}
