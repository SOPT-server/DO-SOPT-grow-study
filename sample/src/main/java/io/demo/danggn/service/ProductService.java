package io.demo.danggn.service;


import io.demo.danggn.repository.ProductRepository;
import io.demo.danggn.dto.ProductRequest;
import io.demo.danggn.dto.ProductResponse;
import io.demo.danggn.entity.Product;
import org.springframework.stereotype.Service;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(final ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public ProductResponse create(final ProductRequest request) {
        final Product product = new Product(
                request.title(),
                request.method(),
                request.description(),
                request.price(),
                request.isSuggested()
        );
        final Product savedProduct = productRepository.save(product);
        return ProductResponse.of(savedProduct.getId());
    }


}
