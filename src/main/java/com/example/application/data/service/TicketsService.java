package com.example.application.data.service;

import com.example.application.data.entity.Ticket;
import com.example.application.data.repository.CustomerRepository;
import com.example.application.data.repository.TicketRepository;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TicketsService {
    private TicketRepository ticketRepository;

    private CustomerRepository customerRepository;

    public TicketsService(TicketRepository ticketRepository, CustomerRepository customerRepository){
        this.ticketRepository = ticketRepository;
        this.customerRepository = customerRepository;
    }

    public List<Ticket> findAll(){
        return ticketRepository.findAll();
    }

    public List<Ticket> findAll(String filterText, Integer id){
        if (filterText == null || filterText.isEmpty()){
            return ticketRepository.findTicketsByUser(id);
        }else{
            return  ticketRepository.search(filterText, id);
        }

    }

    public Integer descriptionExists(Integer id, String description) {
        return ticketRepository.existsByDescription(id, description);
    }

    public Long count(){
        return ticketRepository.count();
    }

    public void delete(Ticket ticket){
        ticketRepository.delete(ticket);
    }

    public void saveTicket(Ticket ticket){
        if (ticket == null){
            System.err.println(
                    "Ticket is null. Are you sure you have connected your form to the application?");
            return;
        }else if (ticketRepository.existsByDescription(ticket.getCustomer().getId(),ticket.getDescription()) > 0){
            Notification notification = new Notification();
            notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
            Div text = new Div(new Text("Ticket with that description already exists"));
            Button closeButton = new Button(new Icon("lumo", "cross"));
            closeButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY_INLINE);
            closeButton.getElement().setAttribute("aria-label", "Close");
            closeButton.addClickListener(event -> {
                notification.close();
            });

            HorizontalLayout layout = new HorizontalLayout(text, closeButton);
            layout.setAlignItems(FlexComponent.Alignment.CENTER);

            notification.add(layout);
            notification.open();
            notification.setDuration(3000);
        }else{
            Notification notification = new Notification();
            notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
            Div text = new Div(new Text("Ticket created successfuly"));
            Button closeButton = new Button(new Icon("lumo", "cross"));
            closeButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY_INLINE);
            closeButton.getElement().setAttribute("aria-label", "Close");
            closeButton.addClickListener(event -> {
                notification.close();
            });

            HorizontalLayout layout = new HorizontalLayout(text, closeButton);
            layout.setAlignItems(FlexComponent.Alignment.CENTER);

            notification.add(layout);
            notification.open();
            notification.setDuration(3000);
            ticketRepository.save(ticket);
        }
    }


}

