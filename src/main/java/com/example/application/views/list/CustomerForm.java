package com.example.application.views.list;

import com.example.application.data.entity.Customer;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.shared.Registration;

public class CustomerForm extends FormLayout {
    TextField address = new TextField("Address");
    TextField name = new TextField("Name");
    Binder<Customer> binder = new BeanValidationBinder<Customer>(Customer.class);
    Button save = new Button("Save");
    private Customer customer;

    public CustomerForm() {
        addClassName("contact-form");
        setSizeFull();
        binder.bindInstanceFields(this);
        binder.bind(address, "address");
        binder.bind(name, "name");
        save.addClickListener(event -> validateAndSave());
        add(address, name, createButtonsLayout());
    }

    private HorizontalLayout createButtonsLayout() {
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        save.addClickShortcut(Key.ENTER);

        save.addClickListener(event -> validateAndSave());
        binder.addStatusChangeListener(event -> save.setEnabled(binder.isValid()));

        return new HorizontalLayout(save);
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
        binder.setBean(customer);
    }

    private void validateAndSave() {
        if (binder.isValid()) {
            Customer customer = new Customer();
            customer.setId(binder.getBean().getId());
            customer.setAddress(binder.getBean().getAddress());
            customer.setName(binder.getBean().getName());

            fireEvent(new SaveEvent(this, customer));
        }
    }

    public <T extends ComponentEvent<?>> Registration addListener(
            Class<T> eventType, ComponentEventListener<T> listener) {
        return getEventBus().addListener(eventType, listener);
    }

    // Events
    public static abstract class CustomerFormEvent
            extends ComponentEvent<CustomerForm> {
        private final Customer customer;

        protected CustomerFormEvent(CustomerForm source, Customer customer) {
            super(source, false);
            this.customer = customer;
        }

        public Customer getCustomer() {
            return customer;
        }
    }

    public static class SaveEvent extends CustomerFormEvent {
        SaveEvent(CustomerForm source, Customer customer) {
            super(source, customer);
        }
    }
}