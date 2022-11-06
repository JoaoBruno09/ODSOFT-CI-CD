package com.example.application.views.list;

import com.example.application.data.entity.Contact;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.data.provider.ListDataProvider;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
public class ListViewIT {

    @Autowired
    private ListView listView;

    @Test
    public void formShownWhenContactSelected() {
        Grid<Contact> grid = listView.grid;
        Contact firstContact = getFirstItem(grid);

        ContactForm form = listView.form;

        assertFalse(form.isVisible());
        grid.asSingleSelect().setValue(firstContact);
        assertTrue(form.isVisible());
        assertEquals(firstContact.getFirstName(), form.firstName.getValue());
    }

    private Contact getFirstItem(Grid<Contact> grid) {
        return ((ListDataProvider<Contact>) grid.getDataProvider()).getItems()
                .iterator().next();
    }


    @Test
    public void formShownWhenContactDeselected() {
        Grid<Contact> grid = listView.grid;
        Contact emptyContact = new Contact();

        ContactForm form = listView.form;

        form.setVisible(true);

        assertTrue(form.isVisible());
        grid.asSingleSelect().setValue(emptyContact);
        form.setVisible(false);
        assertFalse(form.isVisible());
        assertEquals(emptyContact.getFirstName(), form.firstName.getValue());
    }
}