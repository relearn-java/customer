package com.example.customer.repository;

import com.example.customer.model.Customer;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class CustomerRepository implements Repository<Customer, UUID> {

//    C'est cette classe qui est generer par JPA pour que l'on puissent interagire avec la BD

    private final JdbcTemplate jdbcTemplate;
    private final CustomerRowMapper rowMapper = new CustomerRowMapper();

    public CustomerRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    @Override
    public Iterable<Customer> findAll() {
        return jdbcTemplate.query("select * from customer", rowMapper);
    }

    @Override
    public Customer findById(UUID uuid) {
        String sql = "SELECT * FROM customer WHERE id = ?";
        List<Customer> result = jdbcTemplate.query(sql, rowMapper, uuid);
        return result.stream().findFirst().orElse(null);
    }


    private void  create(Customer customer){
        String sql= "INSERT INTO Customer (id,name,email,phone) VALUES (?,?,?,?)";
        jdbcTemplate.update(sql,customer.id(),customer.name(),customer.email(),customer.phone());
    }

    private void  update(Customer customer){
        String sql = "UPDATE Customer SET name=?,email=?,phone=? WHERE id=?";
        jdbcTemplate.update(sql,customer.name(),customer.email(),customer.phone(),customer.id());
    }

    @Override
    public Customer save(Customer entity) {

        if(entity.id() == null){
            Customer newCustomer = new Customer(
                    UUID.randomUUID(),
                    entity.name(),
                    entity.email(),
                    entity.phone()
            );
            create(newCustomer);
            return newCustomer;
        }

        Customer existing = findById(entity.id());
        if (existing != null) {
            update(entity);
        } else {
            create(entity);
        }
        return entity;
    }

    @Override
    public void deleteById(UUID uuid) {

        String sql = "DELETE FROM customer WHERE id = ?";
        jdbcTemplate.update(sql, uuid);

    }
}
