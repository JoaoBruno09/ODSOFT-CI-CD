package com.example.application.views.list;

import com.example.application.data.entity.Ticket;
import com.example.application.data.repository.CustomerRepository;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.shared.Registration;

import java.time.LocalDateTime;
import java.util.List;

public class TicketForm extends FormLayout{

    TextField description = new TextField("Ticket Description");

    CustomerRepository customerRepository;
    Button save = new Button("Save");
    Button delete = new Button("Delete");
    Button close = new Button("Cancel");

    Binder<Ticket> binder = new BeanValidationBinder<Ticket>(Ticket.class);



    public TicketForm(List<Ticket> tickets) {
        addClassName("tickets-form");

        binder.bindInstanceFields(this);

        add(description, createButtonsLayout());

    }
    public void setTicket(Ticket ticket){
        binder.setBean(ticket);
    }

    private Component createButtonsLayout() {
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        delete.addThemeVariants(ButtonVariant.LUMO_ERROR);
        close.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        save.addClickShortcut(Key.ENTER);
        close.addClickShortcut(Key.ESCAPE);

        save.addClickListener(click -> validateAndSave());
        delete.addClickListener(click -> fireEvent(new DeleteEvent(this, binder.getBean())));
        close.addClickListener(clieck -> fireEvent(new CloseEvent(this)));

        binder.addStatusChangeListener(evt -> save.setEnabled(binder.isValid()));

        return new HorizontalLayout(save, delete, close);
    }

    private void validateAndSave(){
        if(binder.isValid()){
            Ticket ticket = new Ticket();
            ticket.setDescription(binder.getBean().getDescription());
            LocalDateTime currentTime = LocalDateTime.now();
            ticket.setReportDate(currentTime);

            fireEvent(new SaveEvent(this, ticket));
        }
    }

    public static abstract class TicketFormEvent extends ComponentEvent<TicketForm>{
        private Ticket ticket;

        protected TicketFormEvent(TicketForm source, Ticket ticket){
            super(source, false);
            this.ticket = ticket;
        }

        public Ticket getTicket(){
            return ticket;
        }
    }

    public static class SaveEvent extends TicketFormEvent {
        SaveEvent(TicketForm source, Ticket ticket){
            super(source, ticket);
        }
    }

    public static class DeleteEvent extends TicketFormEvent {
        DeleteEvent(TicketForm source, Ticket ticket){
            super(source, ticket);
        }
    }

    public static class CloseEvent extends TicketFormEvent {
        CloseEvent(TicketForm source){
            super(source, null);
        }
    }

    public <T extends ComponentEvent<?>> Registration addListener(
            Class<T> eventType, ComponentEventListener<T> listener) {
        return getEventBus().addListener(eventType, listener);
    }
}


