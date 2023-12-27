package com.altimetrik.productproducer.controller;

import com.altimetrik.productproducer.exception.ResourceNotFoundException;
import com.altimetrik.productproducer.model.Product;
import com.altimetrik.productproducer.service.ProductService;
import com.altimetrik.productproducer.service.RabbitMQService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/products")
public class ProductController {
    @Autowired
    ProductService service;

    @Autowired
    RabbitMQService rabbitMQService;
    @PostMapping
    public ResponseEntity<String> saveProduct(@RequestBody Product product){
        service.saveProduct(product);
        return new ResponseEntity<>("saved product successfully", HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts(){
        List<Product> savedProducts=service.getAllProducts();
        return new ResponseEntity<>(savedProducts,HttpStatus.OK);
    }

    @GetMapping("/{productId}")
    public ResponseEntity<Product> getProductById(@PathVariable int productId) {
        Optional<Product> productOptional = service.getProductById(productId);
        if (productOptional.isPresent()) {
            Product product = productOptional.get();
            rabbitMQService.sendProduct(product);
            return new ResponseEntity<>(product,HttpStatus.OK);
        } else {
            throw new ResourceNotFoundException("Product not found with id = " + productId);
        }
    }
}
