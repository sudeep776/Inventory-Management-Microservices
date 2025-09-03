package com.microservices.product.service.serviceImpl;

import com.microservices.product.clients.InventoryClient;
import com.microservices.product.dto.Inventory;
import com.microservices.product.dto.ProductCreatedEvent;
import com.microservices.product.dto.ProductRequestDto;
import com.microservices.product.dto.ProductResponseDto;
import com.microservices.product.entity.Product;
import com.microservices.product.repository.ProductRepository;
import com.microservices.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ModelMapper modelMapper;
    private final InventoryClient inventoryClient;
    private final KafkaTemplate<String, ProductCreatedEvent> kafkaTemplate;

//    @Override
//    public ProductResponseDto createProduct(ProductRequestDto productRequestDto) {
//        // Save the product first
//        Product product = modelMapper.map(productRequestDto, Product.class);
//        Product savedProduct = productRepository.save(product);
//        log.info("Product created successfully: {}", savedProduct);
//
//        // Create inventory entry via Feign client
//        Inventory inventory = new Inventory();
//        inventory.setProductId(savedProduct.getId()); // Ensure ID is numeric or adjust type
//        inventory.setSkuCode(savedProduct.getSkuCode()); // Generate SKU code
//        inventory.setQuantity(productRequestDto.getQuantity()); // Initial quantity (can be from request)
//
//        inventoryClient.createInventory(inventory);
//        log.info("Inventory created for product ID: {}", savedProduct.getId());
//
//        return modelMapper.map(savedProduct, ProductResponseDto.class);
//    }

    @Override
    public ProductResponseDto createProduct(ProductRequestDto productRequestDto) {
        // Save product
        Product product = modelMapper.map(productRequestDto, Product.class);
        Product savedProduct = productRepository.save(product);
        log.info("✅ Product created: {}", savedProduct);

        // Send event to Kafka instead of calling Inventory directly
        ProductCreatedEvent event = new ProductCreatedEvent(
                savedProduct.getId(),
                savedProduct.getSkuCode(),
                productRequestDto.getQuantity()
        );
        kafkaTemplate.send("product-created", event);
        log.info("📤 Published Kafka event: {}", event);

        return modelMapper.map(savedProduct, ProductResponseDto.class);
    }


    @Override
    public List<ProductResponseDto> getAllProducts() {
        // Logic to get all products
        List<Product> products = productRepository.findAll();
        log.info("Retrieved all products: {}", products);
        return products.stream()
                .map(product -> modelMapper.map(product, ProductResponseDto.class))
                .toList();
    }

    @Override
    public Product getProductById(Long id) {
        // Logic to get product by ID
        return productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with ID: " + id));
    }

}
