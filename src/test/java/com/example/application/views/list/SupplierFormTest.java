package com.example.application.views.list;

import com.example.application.data.entity.Product;
import com.example.application.data.entity.Supplier;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SupplierFormTest {

    private ArrayList<Product> products;
    private Set<Product> products2;
    private Supplier supplier;
    private Product product1;
    private Product product2;

    @BeforeEach
    public void setupData(){

        products = new ArrayList<>();
        products2 = new HashSet<>();
        product1 = new Product();
        product1.setDescription("banana");
        product2 = new Product();
        product2.setDescription("pera");
        products.add(product1);
        products.add(product2);
        products2.add(product1);
        products2.add(product2);


        supplier = new Supplier();
        supplier.setAddress("R. Dr. António Bernardino de Almeida 431");
        supplier.setName("ISEP");
        supplier.setProduct(products2);

    }

    @Test
    public void formFieldsPopulated(){
        SupplierForm form = new SupplierForm(products);
        form.setSupplier(supplier);

        assertEquals("R. Dr. António Bernardino de Almeida 431", form.address.getValue());
        assertEquals("ISEP", form.name.getValue());
        assertEquals(products2, form.productsComboBox.getValue());
    }



}
