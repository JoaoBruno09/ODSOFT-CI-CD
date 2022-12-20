package com.example.application.data.generator;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.example.application.data.entity.*;
import com.example.application.data.repository.*;
import com.vaadin.flow.spring.annotation.SpringComponent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.vaadin.artur.exampledata.DataType;
import org.vaadin.artur.exampledata.ExampleDataGenerator;

@SpringComponent
public class DataGenerator {

    @Bean
    public CommandLineRunner loadData(ContactRepository contactRepository, CompanyRepository companyRepository,
                                      StatusRepository statusRepository, CustomerRepository customerRepository, TicketRepository ticketRepository, ProductRepository productRepository,
                                      SupplierRepository supplierRepository) {

        return args -> {
            Logger logger = LoggerFactory.getLogger(getClass());
            if (contactRepository.count() != 0L) {
                logger.info("Using existing database");
                return;
            }
            int seed = 123;

            logger.info("Generating demo data");
            ExampleDataGenerator<Company> companyGenerator = new ExampleDataGenerator<>(Company.class,
                    LocalDateTime.now());
            companyGenerator.setData(Company::setName, DataType.COMPANY_NAME);
            List<Company> companies = companyRepository.saveAll(companyGenerator.create(5, seed));

            List<Status> statuses = statusRepository
                    .saveAll(Stream.of("Imported lead", "Not contacted", "Contacted", "Customer", "Closed (lost)")
                            .map(Status::new).collect(Collectors.toList()));

            logger.info("... generating 50 Contact entities...");
            ExampleDataGenerator<Contact> contactGenerator = new ExampleDataGenerator<>(Contact.class,
                    LocalDateTime.now());
            contactGenerator.setData(Contact::setFirstName, DataType.FIRST_NAME);
            contactGenerator.setData(Contact::setLastName, DataType.LAST_NAME);
            contactGenerator.setData(Contact::setEmail, DataType.EMAIL);

            Random r = new Random(seed);
            List<Contact> contacts = contactGenerator.create(50, seed).stream().peek(contact -> {
                contact.setCompany(companies.get(r.nextInt(companies.size())));
                contact.setStatus(statuses.get(r.nextInt(statuses.size())));
            }).collect(Collectors.toList());

            contactRepository.saveAll(contacts);

            //GENERATING PRODUCTS
            ExampleDataGenerator<Product> productGenerator = new ExampleDataGenerator<>(Product.class, LocalDateTime.now());
            productGenerator.setData(Product::setDescription, DataType.FOOD_PRODUCT_NAME);

            List<Product> products = productGenerator.create(3, seed).stream().collect(Collectors.toList());

            productRepository.saveAll(products);
            //END OF PRODUCT GENERATION

            //GENERATING Customers
            ExampleDataGenerator<Customer> customerGenerator = new ExampleDataGenerator<>(Customer.class,
                    LocalDateTime.now());
            customerGenerator.setData(Customer::setName, DataType.FIRST_NAME);
            customerGenerator.setData(Customer::setAddress, DataType.ADDRESS);
            List<Customer> customers = customerGenerator.create(3, seed).stream().collect(Collectors.toList());

            customerRepository.saveAll(customers);
            //END OF GENERATING Customers

            //GENERATING Tickets
            ExampleDataGenerator<Ticket> ticketGenerator = new ExampleDataGenerator<>(Ticket.class,
                    LocalDateTime.now());
            ticketGenerator.setData(Ticket::setDescription, DataType.TWO_WORDS);

            List<Customer> custList = customerRepository.findAll();
            Customer customer = custList.get(0);

            List<Ticket> tickets = ticketGenerator.create(3, seed).stream().peek(ticket -> {
                ticket.setCustomer(customer);
                LocalDateTime currentTime = LocalDateTime.now();
                ticket.setReportDate(currentTime);
            }).collect(Collectors.toList());

            ticketRepository.saveAll(tickets);
            //END OF GENERATING Tickets

            // GENERATING Suppliers
            if (supplierRepository.count() != 0L) {
                logger.info("Using existing database");
                return;
            }
            ExampleDataGenerator<Supplier> supplierGenerator = new ExampleDataGenerator<>(Supplier.class,
                    LocalDateTime.now());
            supplierGenerator.setData(Supplier::setName, DataType.FIRST_NAME);
            supplierGenerator.setData(Supplier::setAddress, DataType.ADDRESS);
            Set<Product> productsSet = new HashSet<>();
            ArrayList<Product> productsArrayList = new ArrayList<>(productRepository.findAll());
            for (Product prod : productsArrayList) {
                productsSet.add(prod);
            }
            List<Supplier> suppliers = supplierGenerator.create(3, seed).stream().peek(supplier -> {

                supplier.setProduct(productsSet);
            }).collect(Collectors.toList());

            supplierRepository.saveAll(suppliers);
            //END OF GENERATING Suppliers

            logger.info("Generated demo data");
        };
    }

}
