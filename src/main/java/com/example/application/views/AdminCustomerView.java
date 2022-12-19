package com.example.application.views;

import javax.annotation.security.PermitAll;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.example.application.data.entity.Customer;
import com.example.application.data.service.CustomerService;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Component
@Scope("prototype")
@Route(value = "customers", layout = MainLayout.class)
@PageTitle("Customers | Vaadin CRM")
@PermitAll
public class AdminCustomerView extends VerticalLayout {
    public Grid<Customer> grid = new Grid<>(Customer.class);
    CustomerService service;

    public AdminCustomerView(CustomerService service) {
        this.service = service;
        addClassName("list-view");
        setSizeFull();
        configureGrid();

        FlexLayout content = new FlexLayout(grid);
        content.setFlexGrow(2, grid);
        content.addClassNames("content", "gap-m");
        content.setSizeFull();

        add(content);
    }

    private void configureGrid() {
        grid.addClassNames("contact-grid");
        grid.setSizeFull();
        grid.setColumns("name", "address", "fContactDate");
        grid.getColumns().forEach(col -> col.setAutoWidth(true));

        grid.setItems(service.findAllCustomers());
    }
}
