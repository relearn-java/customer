package com.example.customer.config;


import com.example.customer.model.Customer;
import com.example.customer.repository.Repository;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import static java.lang.System.out;

import java.util.UUID;

@Configuration
@Profile("!prod")
public class CustomerConfiguration {

    @Bean
    ApplicationListener<ApplicationReadyEvent> customerAppReady(Repository customerRepository) {
        return event -> {
            customerRepository.save(new Customer("John", "john@example.com", "1-800-PHONE"));
            customerRepository.save(new Customer("Jane", "jane@example.com", "1-800-PHONE"));
            customerRepository.save(new Customer("Jack", "jack@example.com", "1-800-PHONE"));

            customerRepository.findAll().forEach(out::println);
        };
    }
}
