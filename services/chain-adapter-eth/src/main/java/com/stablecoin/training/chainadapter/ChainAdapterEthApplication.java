package com.stablecoin.training.chainadapter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class ChainAdapterEthApplication {
    public static void main(String[] args) {
        SpringApplication.run(ChainAdapterEthApplication.class, args);
    }
}
