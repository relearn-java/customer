package com.example.customer.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "crm")
public record CrmProperties(String welcomeMessage, Integer maxResult) {
}
