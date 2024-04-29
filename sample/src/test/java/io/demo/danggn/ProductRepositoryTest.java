package io.demo.danggn;


import io.demo.danggn.repository.ProductRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class ProductRepositoryTest {

    @Autowired
    ProductRepository productRepository;



    @Test
    @DisplayName("나눔하는 상품은 가격이 0원이어야 한다.")
    void registerProductWithNonZeroPrice() {

    }


    
}
