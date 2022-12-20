package com.example.application.views.list;

import java.util.List;
import com.example.application.data.entity.Product;
import com.example.application.data.entity.Supplier;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.MultiSelectComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.shared.Registration;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;

public class SupplierForm extends FormLayout{

    TextField name = new TextField("Name");
    TextField address = new TextField("Address");
    MultiSelectComboBox<Product> productsComboBox = new MultiSelectComboBox<>("Products");

    Button save = new Button("Save");
    Button delete = new Button("Delete");
    Button close = new Button("Cancel");

    Binder<Supplier> binder = new BeanValidationBinder<Supplier>(Supplier.class);



    public SupplierForm(List<Product> products) {
        addClassName("supplier-form");

        binder.bindInstanceFields(this);
        binder.bind(productsComboBox, "product");

        productsComboBox.setItems(products);
        productsComboBox.setItemLabelGenerator(Product :: getDescription);

        add(name, address, productsComboBox, createButtonsLayout());

    }
    public void setSupplier(Supplier supplier){
        binder.setBean(supplier);
    }

    private Component createButtonsLayout() {
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        delete.addThemeVariants(ButtonVariant.LUMO_ERROR);
        close.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        save.addClickShortcut(Key.ENTER);
        close.addClickShortcut(Key.ESCAPE);

        save.addClickListener(click -> validateAndSave());
        delete.addClickListener(click -> fireEvent(new DeleteEvent(this, binder.getBean())));
        close.addClickListener(clieck -> fireEvent(new CloseEvent(this)));

        binder.addStatusChangeListener(evt -> save.setEnabled(binder.isValid()));

        return new HorizontalLayout(save, delete, close);
    }

    private void validateAndSave(){
        if(binder.isValid()){
            Supplier supplier = new Supplier();
            supplier.setId(binder.getBean().getId());
            supplier.setAddress(binder.getBean().getAddress());
            supplier.setName(binder.getBean().getName());
            supplier.setProduct(productsComboBox.getValue());

            fireEvent(new SaveEvent(this, supplier));
        }
    }

    public static abstract class SupplierFormEvent extends ComponentEvent<SupplierForm>{
        private Supplier supplier;

        protected SupplierFormEvent(SupplierForm source, Supplier supplier){
            super(source, false);
            this.supplier = supplier;
        }

        public Supplier getSupplier(){
            return supplier;
        }
    }

    public static class SaveEvent extends SupplierFormEvent {
        SaveEvent(SupplierForm source, Supplier supplier){
            super(source, supplier);
        }
    }

    public static class DeleteEvent extends SupplierFormEvent {
        DeleteEvent(SupplierForm source, Supplier supplier){
            super(source, supplier);
        }
    }

    public static class CloseEvent extends SupplierFormEvent {
        CloseEvent(SupplierForm source){
            super(source, null);
        }
    }

    public <T extends ComponentEvent<?>> Registration addListener(
            Class<T> eventType, ComponentEventListener<T> listener) {
        return getEventBus().addListener(eventType, listener);
    }
}

