package com.altimetrik.productproducer.service;

import com.altimetrik.productproducer.exception.ResourceConflictException;
import com.altimetrik.productproducer.model.Product;
import com.altimetrik.productproducer.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {
    @Mock
    ProductRepository repository;

    @InjectMocks
    ProductService service;

    @Test
    void testSaveProduct_Success() {
        Product product = new Product(1, "Laptop", 999.99);

        service.saveProduct(product);

        verify(repository, times(1)).existsById(product.getId());
        verify(repository, times(1)).save(product);
    }

    @Test
    void testSaveProduct_Conflict() {
        Product existingProduct = new Product(1, "Laptop", 999.99);
        when(repository.existsById(existingProduct.getId())).thenReturn(true);

        assertThrows(ResourceConflictException.class, () -> service.saveProduct(existingProduct));

        verify(repository, never()).save(existingProduct);
    }

    @Test
    void testGetAllProducts() {
        List<Product> productList = new ArrayList<>();
        productList.add(new Product(1, "Laptop", 999.99));
        productList.add(new Product(2, "Smartphone", 599.99));
        when(repository.findAll()).thenReturn(productList);

        List<Product> result = service.getAllProducts();

        assertEquals(productList, result);
    }

    @Test
    void testGetProductById_Success() {
        Product product = new Product(1, "Laptop", 999.99);
        when(repository.findById(product.getId())).thenReturn(Optional.of(product));

        Optional<Product> result = service.getProductById(product.getId());

        assertTrue(result.isPresent());
        assertEquals(product, result.get());
    }


    @Test
    void testGetProductById_NotFound() {
        int productId = 1;
        when(repository.findById(productId)).thenReturn(Optional.empty());

        Optional<Product> result = service.getProductById(productId);

        assertTrue(result.isEmpty());
    }


}
