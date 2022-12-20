package com.example.application.data.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.application.data.entity.Supplier;

public interface SupplierRepository extends JpaRepository<Supplier, Integer> {


    @Query("select s from Supplier s where lower(s.name) like lower(concat('%', :filterText, '%'))")
    List<Supplier> search(@Param("filterText")String filterText);

    boolean existsByName(String name);
}

