package io.demo.danggn.controller;

import io.demo.danggn.dto.ProductRequest;
import io.demo.danggn.dto.ProductResponse;
import io.demo.danggn.service.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    public ResponseEntity<ProductResponse> registerProduct(@RequestBody ProductRequest request) {
        final ProductResponse response = productService.create(request);
        return ResponseEntity.ok(response);
    }


}
