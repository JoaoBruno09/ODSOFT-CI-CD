package com.example.application.data.entity;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import javax.validation.constraints.NotEmpty;

import com.example.application.data.AbstractEntity;

@Entity
public class Supplier extends AbstractEntity{

    @NotEmpty
    private String name = "";

    @NotEmpty
    private String address = "";

    @ManyToMany(fetch = FetchType.EAGER)
    private Set<Product> product = new HashSet<>();

    public Set<Product> getProduct() {
        return product;
    }

    public void setProduct(Set<Product> product) {
        this.product = product;
    }

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

}
