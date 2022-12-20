package com.example.application.data.entity;

import javax.persistence.ManyToOne;
import javax.validation.constraints.NotEmpty;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.validation.constraints.NotNull;

import com.example.application.data.AbstractEntity;

@Entity
public class Ticket extends AbstractEntity{

    @NotEmpty
    private String description = "";

    @NotNull
    private LocalDateTime reportDate;

    @ManyToOne()
    private Customer customer;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getReportDate() {
        return reportDate;
    }

    public void setReportDate(LocalDateTime reportDate) {
        this.reportDate = reportDate;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }
}

