package com.example.application.views.list;

import com.example.application.data.entity.Customer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CustomerFormTest {
    private Customer customer;

    @BeforeEach
    public void setupData() {
        customer = new Customer();
        customer.setName("Jose");
        customer.setAddress("Rua das Rosinhas");
    }

    @Test
    public void formFieldsPopulated() {
        CustomerForm form = new CustomerForm();
        form.setCustomer(customer);
        assertEquals("Jose", form.name.getValue());
        assertEquals("Rua das Rosinhas", form.address.getValue());
    }
}