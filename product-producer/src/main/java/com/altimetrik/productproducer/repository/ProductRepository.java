package com.altimetrik.productproducer.repository;

import com.altimetrik.productproducer.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Integer> {
}
