package com.example.application.data.repository;

import com.example.application.data.entity.Customer;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
    boolean existsByName(String name);
    @Query(value = "SELECT * FROM customer where name=:name", nativeQuery = true)
    Customer getByName(String name);
}
