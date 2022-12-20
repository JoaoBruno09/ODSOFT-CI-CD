package com.example.application.data.entity;

import javax.persistence.Entity;
import javax.validation.constraints.NotEmpty;

import com.example.application.data.AbstractEntity;

@Entity
public class Product extends AbstractEntity {

    @NotEmpty
    private String description = "";

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return description;
    }
}
