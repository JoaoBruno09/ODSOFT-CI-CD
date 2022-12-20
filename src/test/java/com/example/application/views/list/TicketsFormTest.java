package com.example.application.views.list;

import com.example.application.data.entity.Customer;
import com.example.application.data.entity.Ticket;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TicketsFormTest {

    private ArrayList<Ticket> tickets;
    private Customer dummyCustomer;

    private Ticket dummyTicket;

    @BeforeEach
    public void setupData(){
        dummyTicket = new Ticket();
        dummyCustomer = new Customer();
        dummyCustomer.setAddress("Street of happiness");
        dummyCustomer.setName("Carl");

        LocalDateTime currentTime = LocalDateTime.now();
        dummyTicket.setReportDate(currentTime);
        dummyTicket.setDescription("The website is not responding");
        dummyTicket.setCustomer(dummyCustomer);

    }

    @Test
    public void formFieldsPopulated(){
        TicketForm form = new TicketForm(tickets);
        form.setTicket(dummyTicket);

        assertEquals(dummyTicket.getDescription(), form.description.getValue());
    }



}
