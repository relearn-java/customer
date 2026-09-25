package com.example.customer;

import com.example.customer.model.Customer;
import com.example.customer.repository.CustomerRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.test.autoconfigure.JdbcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
public class CustomerRepositoryIntegrationTests {

    @Autowired
    private CustomerRepository customerRepository;

    @Test
    void shouldPerformCrudOperations(){


        // 1. Create
        Customer customer = new Customer(UUID.randomUUID(), "Integration Test", "it@test.com", "123-456");
        customerRepository.save(customer);

        // 2. Read (Find By ID)
        Customer found = customerRepository.findById(customer.id());
        assertThat(found).isNotNull();
        assertThat(found.name()).isEqualTo("Integration Test");

        // 3. Update
        Customer toUpdate = new Customer(customer.id(), "Updated Name", "it@test.com", "123-456");
        customerRepository.save(toUpdate);

        Customer updated = customerRepository.findById(customer.id());
        assertThat(updated.name()).isEqualTo("Updated Name");

        // 4. Read (Find All)
        // Note: The DB might contain initial data from CustomerConfiguration
        Iterable<Customer> all = customerRepository.findAll();
        assertThat(all).isNotEmpty();

        // 5. Delete
        customerRepository.deleteById(customer.id());
        Customer deleted = customerRepository.findById(customer.id());
        assertThat(deleted).isNull();
    }




}
