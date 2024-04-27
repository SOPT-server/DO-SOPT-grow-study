package io.demo.danggn;


import org.springframework.stereotype.Service;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public ProductResponse createProduct(final ProductRequest request) {
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
