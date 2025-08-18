package com.microservices.product.service;

import com.microservices.product.dto.ProductRequestDto;
import com.microservices.product.dto.ProductResponseDto;
import com.microservices.product.entity.Product;

import java.util.List;

public interface ProductService {
    // Create a new product
    ProductResponseDto createProduct(ProductRequestDto productRequestDto);

    // Get all products
    List<ProductResponseDto> getAllProducts();

    //Get product by ID
    Product getProductById(Long id);
}
