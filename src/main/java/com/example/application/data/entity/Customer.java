package com.example.application.data.entity;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.validation.constraints.NotEmpty;

import org.hibernate.annotations.CreationTimestamp;

import com.example.application.data.AbstractEntity;

@Entity
public class Customer extends AbstractEntity {

    @NotEmpty
    private String name = "";

    @NotEmpty
    private String address = "";

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime fContactDate;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public LocalDateTime getfContactDate() {
        return fContactDate;
    }

    public void setfContactDate(LocalDateTime fContactDate) {
        this.fContactDate = fContactDate;
    }
}
