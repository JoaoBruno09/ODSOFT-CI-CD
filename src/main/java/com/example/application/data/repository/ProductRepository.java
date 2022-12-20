package com.example.application.data.repository;

import com.example.application.data.entity.Product;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProductRepository extends JpaRepository<Product, Long>{
    @Query("select p from Product p where lower(p.description) like lower(concat('%', :filterText, '%'))")
    List<Product> search(@Param("filterText")String filterText);
    boolean existsByDescription(String description);

}
