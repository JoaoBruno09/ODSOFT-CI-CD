package com.example.application.data.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.application.data.entity.Customer;
import com.example.application.data.repository.CustomerRepository;

@Service
public class CustomerService {
    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public void saveCustomer(Customer customer) {
        if (customer == null) {
            System.err.println(
                    "Customer is null. Are you sure you have connected your form to the application?");
            return;
        }
        customerRepository.save(customer);
    }

    public List<Customer> findAllCustomers() {
        return customerRepository.findAll();
    }

}