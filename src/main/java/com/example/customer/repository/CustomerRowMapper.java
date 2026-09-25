package com.example.customer.repository;


import com.example.customer.model.Customer;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;


public class CustomerRowMapper implements RowMapper<Customer> {

    // cette methode se charge de faire la correspondance entre la table Customer en bd et la l'objet Customer dans notre code java
    @Override
    public Customer mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new Customer(
                UUID.fromString(rs.getString("id")),
                rs.getString("name"),
                rs.getString("email"),
                rs.getString("phone")

        );
    }
}
