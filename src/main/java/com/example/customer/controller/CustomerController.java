package com.example.customer.controller;

import com.example.customer.config.CrmProperties;
import com.example.customer.model.Customer;
import com.example.customer.repository.CustomerRepository;
import jakarta.validation.Valid;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.Locale;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/customers")
public class CustomerController {

    private final CustomerRepository customerRepository;
    private final CrmProperties crmProperties;
    private final MessageSource messageSource;


    public CustomerController(CustomerRepository customerRepository, CrmProperties crmProperties,  MessageSource messageSource) {
        this.customerRepository = customerRepository;
        this.crmProperties = crmProperties;
        this.messageSource = messageSource;
    }

    @GetMapping("/welcome")
    public String getCustomerById() {
        return crmProperties.welcomeMessage();
    }

    @GetMapping("/greet/{name}")
    public String getCustomerById(@PathVariable String name) {
        Locale local = LocaleContextHolder.getLocale();
        return messageSource.getMessage("welcome.message", new Object[] {name}, local);
    }

    @GetMapping
    public Iterable<com.example.customer.model.Customer> getAllCustomers() {
        return customerRepository.findAll();
    }


    @GetMapping("/{id}")
    public ResponseEntity<Customer> getCustomerById(@Valid @PathVariable UUID id) {
        Customer customer = customerRepository.findById(id);
        return customer!=null?ResponseEntity.ok(customer):ResponseEntity.notFound().build();
    }

    /**
     * Crée un nouveau client, le persiste et retourne une réponse HTTP 201
     * avec l'URI de la ressource créée dans l'en-tête {@code Location}.
     *
     * @param customer le client à créer (validé via {@code @Valid})
     * @return 201 Created + Location + le client persisté en JSON
     */

    @PostMapping
    public ResponseEntity<Customer> save(@Valid @RequestBody Customer customer) {
        Customer savedCustomer = customerRepository.save(customer);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedCustomer.id())
                .toUri();
        return ResponseEntity.created(location).body(savedCustomer);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Customer> update(@PathVariable UUID id, @Valid @RequestBody Customer customer) {
        Customer updatedCustomer = customerRepository.findById(id);
        if(updatedCustomer == null){
            return ResponseEntity.notFound().build();
        }
        Customer toUpdate = new Customer(id, customer.name(), customer.
                email(), customer.phone());
        customerRepository.save(toUpdate);
        return ResponseEntity.ok(toUpdate);
    }

    @DeleteMapping("/{id}")
    ResponseEntity<Void> deleteById(@PathVariable UUID id) {
        if(customerRepository.findById(id) == null){
            return ResponseEntity.notFound().build();
        }
        customerRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
