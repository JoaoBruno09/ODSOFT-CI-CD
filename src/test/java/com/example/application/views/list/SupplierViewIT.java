package com.example.application.views.list;

import com.example.application.data.entity.Supplier;
import com.example.application.views.SuppliersView;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.data.provider.ListDataProvider;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
public class SupplierViewIT {

    @Autowired
    private SuppliersView suppliersView;

    @Test
    public void formShownWhenSupplierSelected() {
        Grid<Supplier> grid = suppliersView.grid;
        Supplier firstSupplier = getFirstItem(grid);

        SupplierForm form = suppliersView.form;

        assertFalse(form.isVisible());
        grid.asSingleSelect().setValue(firstSupplier);
        assertTrue(form.isVisible());
        assertEquals(firstSupplier, form.binder.getBean());

    }

    private Supplier getFirstItem(Grid<Supplier> grid) {
        return ((ListDataProvider<Supplier>) grid.getDataProvider()).getItems().iterator().next();
    }
}