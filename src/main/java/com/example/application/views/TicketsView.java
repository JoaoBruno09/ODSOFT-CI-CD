package com.example.application.views;

import com.example.application.data.entity.Customer;
import com.example.application.data.entity.Ticket;
import com.example.application.data.repository.CustomerRepository;
import com.example.application.data.service.CustomerService;
import com.example.application.data.service.TicketsService;
import com.example.application.views.list.TicketForm;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
@Route(value = "tickets/:name")
@PageTitle("Tickets | Vaadin CRM")
@CssImport("./themes/flowcrmtutorial/styles.css")
@AnonymousAllowed
public class TicketsView extends VerticalLayout implements BeforeEnterObserver {
    private final CustomerRepository customerRepository;

    public Grid<Ticket> grid = new Grid<> (Ticket.class);

    public TicketForm form;
    private TicketsService ticketsService;
    TextField filterText = new TextField();

    Customer loggedCustomer;

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        // Get the query parameters from the URL
        if (event.getRouteParameters().get("name").get() != null){
            loggedCustomer = customerRepository.getByName(event.getRouteParameters().get("name").get());
            updateList();
        }
    }

    public TicketsView(TicketsService ticketsService, CustomerService customerService,
                       CustomerRepository customerRepository) {
        this.ticketsService = ticketsService;
        addClassName("list-view");
        setSizeFull();
        configureGrid();
        getToolbar();

        form = new TicketForm(ticketsService.findAll());
        form.addListener(TicketForm.SaveEvent.class, this::saveTicket);
        form.addListener(TicketForm.DeleteEvent.class, this::deleteTicket);
        form.addListener(TicketForm.CloseEvent.class, e -> closeEditor());

        Div content = new Div(grid, form);
        content.addClassName("content");
        content.setSizeFull();

        add(getToolbar(), content);
        closeEditor();
        this.customerRepository = customerRepository;
    }

    private void deleteTicket(TicketForm.DeleteEvent evt){
        ticketsService.delete(evt.getTicket());
        updateList();
        closeEditor();
    }

    private void saveTicket (TicketForm.SaveEvent evt){
        evt.getTicket().setCustomer(loggedCustomer);
        ticketsService.saveTicket(evt.getTicket());
        updateList();
        closeEditor();
    }


    private void closeEditor() {
        form.setTicket(null);
        form.setVisible(false);
        removeClassName("editing");
    }


    private HorizontalLayout getToolbar() {
        filterText.setPlaceholder("Filter by description...");
        filterText.setClearButtonVisible(true);
        filterText.setValueChangeMode(ValueChangeMode.LAZY);
        filterText.addValueChangeListener(e -> updateList());

        Button addSupplierButton = new Button("Submit a ticket", click -> addTicket());

        HorizontalLayout toolbar = new HorizontalLayout(filterText, addSupplierButton);
        toolbar.addClassName("toolbar");
        return toolbar;
    }

    private void addTicket(){
        grid.asSingleSelect().clear();
        editTicket(new Ticket());
    }


    private void configureGrid(){
        grid.addClassName("ticket-grid");
        grid.setSizeFull();
        grid.setColumns("description", "reportDate");

        grid.getColumns().forEach(col -> col.setAutoWidth(true));

        grid.asSingleSelect().addValueChangeListener(evt -> editTicket(evt.getValue()));
    }

    private void editTicket (Ticket ticket){
        if( ticket == null){
            closeEditor();
        }
        else{
            form.setTicket(ticket);
            form.setVisible(true);
            addClassName("editing");
        }
    }

    private void updateList(){
        grid.setItems(ticketsService.findAll(filterText.getValue(), loggedCustomer.getId()));
    }
}
