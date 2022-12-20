package com.example.application.views;

import javax.annotation.security.RolesAllowed;

import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.router.PageTitle;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import com.example.application.data.entity.Supplier;
import com.example.application.data.service.ProductService;
import com.example.application.data.service.SupplierService;
import com.example.application.views.list.SupplierForm;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.Route;

@Component
@Scope("prototype")
@Route(value = "suppliers", layout = MainLayout.class)
@RolesAllowed("ROLE_ADMIN")
@PageTitle("Suppliers | Vaadin CRM")
@CssImport("./themes/flowcrmtutorial/styles.css")
public class SuppliersView extends VerticalLayout{

    public Grid<Supplier> grid = new Grid<> (Supplier.class);

    public SupplierForm form;
    private SupplierService supplierService;
    TextField filterText = new TextField();

    public SuppliersView(SupplierService supplierService, ProductService productService) {
        this.supplierService = supplierService;
        addClassName("list-view");
        setSizeFull();
        configureGrid();
        getToolbar();

        form = new SupplierForm(productService.findAll());
        form.addListener(SupplierForm.SaveEvent.class, this::saveSupplier);
        form.addListener(SupplierForm.DeleteEvent.class, this::deleteSupplier);
        form.addListener(SupplierForm.CloseEvent.class, e -> closeEditor());

        Div content = new Div(grid, form);
        content.addClassName("content");
        content.setSizeFull();

        add(getToolbar(), content);
        updateList();
        closeEditor();
    }

    private void deleteSupplier(SupplierForm.DeleteEvent evt){
        supplierService.deleteSupplier(evt.getSupplier());
        updateList();
        closeEditor();
    }

    private void saveSupplier (SupplierForm.SaveEvent evt){
        supplierService.saveSupplier(evt.getSupplier());
        updateList();
        closeEditor();
    }


    private void closeEditor() {
        form.setSupplier(null);
        form.setVisible(false);
        removeClassName("editing");
    }


    private HorizontalLayout getToolbar() {
        filterText.setPlaceholder("Filter by name...");
        filterText.setClearButtonVisible(true);
        filterText.setValueChangeMode(ValueChangeMode.LAZY);
        filterText.addValueChangeListener(e -> updateList());

        Button addSupplierButton = new Button("Add supplier", click -> addSupplier());

        HorizontalLayout toolbar = new HorizontalLayout(filterText, addSupplierButton);
        toolbar.addClassName("toolbar");
        return toolbar;
    }

    private void addSupplier(){
        grid.asSingleSelect().clear();
        editSupplier(new Supplier());
    }


    private void configureGrid(){
        grid.addClassName("suppliers-grid");
        grid.setSizeFull();
        grid.setColumns("name", "address", "product");

        grid.getColumns().forEach(col -> col.setAutoWidth(true));

        grid.asSingleSelect().addValueChangeListener(evt -> editSupplier(evt.getValue()));
    }

    private void editSupplier (Supplier supplier){
        if( supplier == null){
            closeEditor();
        }
        else{
            form.setSupplier(supplier);
            form.setVisible(true);
            addClassName("editing");
        }
    }

    private void updateList(){
        grid.setItems(supplierService.findAllSuppliers(filterText.getValue()));
    }
}
