package com.stan.payment.config;

import org.springframework.stereotype.Component;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;

@Component
public class TestConfig {
    @Value("${spring.kafka.bootstrap-servers:NOT_SET}")
    private String kafkaServer;

    @PostConstruct
    public void print() {
        System.out.println("KAFKA SERVER => " + kafkaServer);
    }
}
