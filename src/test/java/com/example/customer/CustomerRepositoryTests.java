package com.example.customer;

import com.example.customer.model.Customer;
import com.example.customer.repository.CustomerRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.test.autoconfigure.JdbcTest;
import org.springframework.context.annotation.Import;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@Import(CustomerRepository.class)
public class CustomerRepositoryTests {

    @Autowired
    private CustomerRepository customerRepository;

    @Test
    void shouldPersistAndRetrieveCustomer() {
        Customer customer = new Customer(UUID.randomUUID(), "John Doe", "john@test.com", "123-456");

        customerRepository.save(customer);

        Customer found = customerRepository.findById(customer.id());
        assertThat(found).isNotNull();
        assertThat(found.email()).isEqualTo("john@test.com");
    }
}
