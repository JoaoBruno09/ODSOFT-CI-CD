package com.example.application.views.list;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.example.application.data.entity.Product;
import com.example.application.views.ProductView;
import com.vaadin.flow.component.grid.Grid;

@SpringBootTest
public class ProductViewIT {
    @Autowired
    private ProductView productView;
    @Test
    public void formShownWhenProductSelected(){
        Grid<Product> grid = productView.grid;
        Product firstProduct = getFirstItem(grid);

        ProductForm form  = productView.form;

        assertFalse(form.isVisible());
        grid.asSingleSelect().setValue(firstProduct);
        assertTrue(form.isVisible());
        assertEquals(firstProduct, form.binder.getBean());
    }

    private Product getFirstItem(Grid<Product> grid) {
        return ((ListDataProvider<Product>) grid.getDataProvider()).getItems().iterator().next();
    }
}
