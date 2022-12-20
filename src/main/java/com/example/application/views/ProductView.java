package com.example.application.views;

import javax.annotation.security.RolesAllowed;

import com.vaadin.flow.router.PageTitle;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.example.application.data.entity.Product;
import com.example.application.data.service.ProductService;
import com.example.application.views.list.ProductForm;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.Route;

@Component
@Scope("prototype")
@Route(value = "product", layout = MainLayout.class)
@RolesAllowed("ROLE_ADMIN")
@PageTitle("Product Categories | Vaadin CRM")
@CssImport("./themes/flowcrmtutorial/styles.css")
public class ProductView extends VerticalLayout{
    public ProductForm form;
    public Grid<Product> grid = new Grid<>(Product.class);
    TextField filterText = new TextField();
    ProductService productService;
    public ProductView(ProductService productService){
        this.productService = productService;
        addClassName("list-view");
        setSizeFull();
        configureGrid();

        form = new ProductForm();
        form.addListener(ProductForm.SaveEvent.class, this::saveProduct);
        form.addListener(ProductForm.DeleteEvent.class, this::deleteProduct);
        form.addListener(ProductForm.CloseEvent.class, e -> closeEditor());
        Div content = new Div(grid, form);
        content.addClassName("content");
        content.setSizeFull();

        add(getToolbar(), content);
        updateList();
        closeEditor();
    }

    private void deleteProduct(ProductForm.DeleteEvent evt) {
        productService.delete(evt.getProduct());
        updateList();
        closeEditor();
    }

    private void saveProduct(ProductForm.SaveEvent evt) {
        productService.saveProduct(evt.getProduct());
        updateList();
        closeEditor();
    }

    private void closeEditor() {
        form.setProduct(null);
        form.setVisible(false);
        removeClassName("editing");
    }

    private HorizontalLayout getToolbar() {
        filterText.setPlaceholder("Filter by description");
        filterText.setClearButtonVisible(true);
        filterText.setValueChangeMode(ValueChangeMode.LAZY);
        filterText.addValueChangeListener(e-> updateList());

        Button addProductButton =  new Button("Add product", click -> addProduct());

        HorizontalLayout toolbar = new HorizontalLayout(filterText, addProductButton);

        toolbar.addClassName("toolbar");
        return toolbar;
    }

    private void addProduct() {
        grid.asSingleSelect().clear();
        editProduct(new Product());
    }

    private void updateList() {
        grid.setItems(productService.findAll(filterText.getValue()));
    }

    private void configureGrid(){
        grid.addClassName("product-grid");
        grid.setSizeFull();
        grid.setColumns("id", "description");
        grid.getColumns().forEach(col-> col.setAutoWidth(true));
        grid.asSingleSelect().addValueChangeListener(evt -> editProduct(evt.getValue()));
    }

    private void editProduct(Product product) {
        if (product == null){
            closeEditor();
        }else{
            form.setProduct(product);
            form.setVisible(true);
            addClassName("editing");
        }
    }
}
