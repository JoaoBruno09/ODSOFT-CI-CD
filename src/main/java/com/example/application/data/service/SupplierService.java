package com.example.application.data.service;

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import org.springframework.stereotype.Service;
import com.example.application.data.entity.Supplier;
import com.example.application.data.repository.SupplierRepository;
import java.util.List;

@Service
public class SupplierService {
    private final SupplierRepository supplierRepository;

    public SupplierService( SupplierRepository supplierRepository){
        this.supplierRepository = supplierRepository;

    }

    public List<Supplier> findAllSuppliers(String filterText){
        if(filterText == null || filterText.isEmpty()){
            return supplierRepository.findAll();
        }
        else{
            return supplierRepository.search(filterText);
        }
    }

    public void deleteSupplier(Supplier supplier) {
        supplierRepository.delete(supplier);
    }

    public Supplier findSupplier(Integer id){
        return supplierRepository.getReferenceById(id);
    }

    public void saveSupplier(Supplier supplier) {
        if (supplier == null) {
            System.err.println(
                    "Supplier is null. Are you sure you have connected your form to the application?");
            return;
        }else if (supplierRepository.existsByName(supplier.getName())){
            Notification notification = new Notification();
            notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
            Div text = new Div(new Text("Supplier already exists"));
            Button closeButton = new Button(new Icon("lumo", "cross"));
            closeButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY_INLINE);
            closeButton.getElement().setAttribute("aria-label", "Close");
            closeButton.addClickListener(event -> {
                notification.close();
            });

            HorizontalLayout layout = new HorizontalLayout(text, closeButton);
            layout.setAlignItems(FlexComponent.Alignment.CENTER);
            notification.add(layout);
            notification.open();
        }else{
            Notification notification = new Notification();
            notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
            Div text = new Div(new Text("Supplier added successfuly"));
            Button closeButton = new Button(new Icon("lumo", "cross"));
            closeButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY_INLINE);
            closeButton.getElement().setAttribute("aria-label", "Close");
            closeButton.addClickListener(event -> {
                notification.close();
            });

            HorizontalLayout layout = new HorizontalLayout(text, closeButton);
            layout.setAlignItems(FlexComponent.Alignment.CENTER);

            notification.add(layout);
            notification.setDuration(3000);
            notification.open();
            supplierRepository.save(supplier);
        }

    }

    public long countSupplier() {
        return supplierRepository.count();
    }
}
