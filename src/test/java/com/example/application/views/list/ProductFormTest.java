package com.example.application.views.list;

import com.example.application.data.entity.Product;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ProductFormTest {
    private Product product;

    @BeforeEach
    public void setupData(){
        product = new Product();
        product.setDescription("Shoes");
    }

    @Test
    public void formFieldsPopulated(){
        ProductForm form = new ProductForm();
        form.setProduct(product);
        assertEquals("Shoes", form.description.getValue());
    }


}
