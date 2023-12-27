package com.altimetrik.productproducer;

import com.altimetrik.productproducer.model.Product;
import com.altimetrik.productproducer.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

import java.util.Arrays;
import java.util.List;

@SpringBootApplication
@EnableDiscoveryClient
public class ProductProducerApplication implements CommandLineRunner {
	@Autowired
	ProductRepository productRepository;

	public static void main(String[] args) {
		SpringApplication.run(ProductProducerApplication.class, args);
	}

	private void seedProductData() {
		Product laptop = Product.builder().id(1).name("Laptop").price(99000).build();
		Product mobile = Product.builder().id(2).name("Mobile").price(12000).build();
		Product headPhones = Product.builder().id(3).name("HeadPhones").price(8000).build();
		List<Product> products = Arrays.asList(laptop,mobile,headPhones);
		productRepository.saveAll(products);
	}

	@Override
	public void run(String... args){
		System.out.println("Seeding product data...");
		seedProductData();
	}
}
