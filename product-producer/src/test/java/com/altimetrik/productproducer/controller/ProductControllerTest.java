package com.altimetrik.productproducer.controller;

import com.altimetrik.productproducer.model.Product;
import com.altimetrik.productproducer.repository.ProductRepository;
import com.altimetrik.productproducer.service.ProductService;
import com.altimetrik.productproducer.service.ProductServiceTest;
import com.altimetrik.productproducer.service.RabbitMQService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(ProductController.class)
public class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    @MockBean
    RabbitMQService rabbitMQService;

    @MockBean
    ProductRepository repository;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testSaveProducts() throws Exception {
        Product product = new Product();

        doNothing().when(productService).saveProduct(product);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(product)))
                .andExpect(MockMvcResultMatchers.status().isCreated());
        verify(productService, times(1)).saveProduct(product);

    }

    @Test
    void testGetAllProducts() throws Exception {
        List<Product> productList = Arrays.asList(new Product(), new Product());

        when(productService.getAllProducts()).thenReturn(productList);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/products"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.size()").value(productList.size()));
        verify(productService, times(1)).getAllProducts();
    }

    @Test
    void testGetProductsById() throws Exception {
        int productId = 1;
        Product product = Product.builder().id(productId).name("test").build();

        when(productService.getProductById(productId)).thenReturn(Optional.ofNullable(product));
        doNothing().when(rabbitMQService).sendProduct(product);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/products/{productId}", productId))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(productId));

        verify(productService, times(1)).getProductById(productId);
        verify(rabbitMQService, times(1)).sendProduct(product);
    }

}
