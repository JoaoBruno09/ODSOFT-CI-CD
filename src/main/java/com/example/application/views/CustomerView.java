package com.example.application.views;

import com.example.application.data.repository.CustomerRepository;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.example.application.data.entity.Customer;
import com.example.application.data.service.CustomerService;
import com.example.application.views.list.CustomerForm;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Component
@Scope("prototype")
@Route(value = "customer")
@PageTitle("Customer | Vaadin CRM")
@AnonymousAllowed

public class CustomerView extends VerticalLayout {
    private final CustomerRepository customerRepository;
    CustomerForm customerForm;
    CustomerService customerService;

    Dialog dialog = new Dialog();

    public CustomerView(CustomerService customerService,
            CustomerRepository customerRepository) {
        this.customerService = customerService;
        addClassName("list-view");
        setSizeFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);

        VerticalLayout header = new VerticalLayout();
        header.add(new H1("Customer"));
        header.setAlignItems(Alignment.CENTER);

        customerForm = new CustomerForm();
        customerForm.setWidth("25em");
        customerForm.addListener(CustomerForm.SaveEvent.class, this::saveCustomer);
        add(header, customerForm);
        addCustomer();
        this.customerRepository = customerRepository;

    }

    private void saveCustomer(CustomerForm.SaveEvent evt) {
        if (evt.getCustomer() != null) {
            if (customerRepository.existsByName(evt.getCustomer().getName())) {
                UI.getCurrent().navigate("/tickets/" + evt.getCustomer().getName());
            } else {
                customerService.saveCustomer(evt.getCustomer());
                UI.getCurrent().navigate("/tickets/" + evt.getCustomer().getName());
            }

        }
    }

    public void editCustomer(Customer customer) {
        customerForm.setCustomer(customer);
        customerForm.setVisible(true);
        addClassName("editing");
    }

    void addCustomer() {
        editCustomer(new Customer());
    }
}
