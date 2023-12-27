package com.altimetrik.productproducer.service;

import com.altimetrik.productproducer.exception.ResourceConflictException;
import com.altimetrik.productproducer.model.Product;
import com.altimetrik.productproducer.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {
    @Autowired
    ProductRepository repository;
    public void saveProduct(Product product) {
        if (repository.existsById(product.getId())) {
            throw new ResourceConflictException("Product already exists with id "+product.getId());
        }
        repository.save(product);
    }

    public List<Product> getAllProducts() {
        return repository.findAll();
    }

    public Optional<Product> getProductById(int productId) {
        return repository.findById(productId);
    }
}
