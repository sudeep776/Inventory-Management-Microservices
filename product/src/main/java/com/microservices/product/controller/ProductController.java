package com.microservices.product.controller;

import com.microservices.product.dto.ProductRequestDto;
import com.microservices.product.dto.ProductResponseDto;
import com.microservices.product.entity.Product;
import com.microservices.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    @PostMapping
    public ProductResponseDto createProduct(@RequestBody ProductRequestDto product) {
        // Logic to create a product
        return productService.createProduct(product);
    }

    @GetMapping
    public List<ProductResponseDto> getAllProducts() {
        // Logic to get all products
        return productService.getAllProducts();
    }

    @GetMapping("/{id}")
    public Product getProductById(@PathVariable Long id) {
        // Logic to get a product by ID
        return productService.getProductById(id);
    }

}
