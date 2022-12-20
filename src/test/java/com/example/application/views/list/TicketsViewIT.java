package com.example.application.views.list;

import com.example.application.data.entity.Ticket;
import com.example.application.views.TicketsView;
import com.vaadin.flow.component.grid.Grid;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
public class TicketsViewIT {

    @Autowired
    private TicketsView ticketsView;
    @Test
    public void formShownWhenTicketDeselected() {
        Grid<Ticket> grid = ticketsView.grid;
        Ticket emptyTicket = new Ticket();

        TicketForm form = ticketsView.form;

        form.setVisible(true);

        assertTrue(form.isVisible());
        grid.asSingleSelect().setValue(emptyTicket);
        form.setVisible(false);
        assertFalse(form.isVisible());
        assertEquals(emptyTicket.getDescription(), form.description.getValue());
    }
}
