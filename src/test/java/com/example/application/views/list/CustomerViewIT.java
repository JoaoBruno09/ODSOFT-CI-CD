package com.example.application.views.list;

import com.example.application.data.entity.Customer;
import com.example.application.data.repository.CustomerRepository;
import com.example.application.views.AdminCustomerView;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.data.provider.ListDataProvider;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class CustomerViewIT {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private AdminCustomerView adminCustomerView;

    @Test
    public void gridShowLastCustomerAdded() {
        Grid<Customer> grid = adminCustomerView.grid;
        Customer customer = new Customer();
        customer.setName("Joao");
        customer.setAddress("Rua das Rosas");
        customerRepository.save(customer);

        grid.getDataProvider().refreshAll();
        Customer lastCustomer = (Customer) grid.getDataProvider().getId(customer);
        grid.asSingleSelect().setValue(lastCustomer);
        assertEquals(lastCustomer.getName(), customer.getName());
        assertEquals(lastCustomer.getAddress(), customer.getAddress());
    }
}