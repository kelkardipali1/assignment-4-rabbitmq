package com.altimetrik.productconsumer.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Product {
    private String name;
    private double price;
}
