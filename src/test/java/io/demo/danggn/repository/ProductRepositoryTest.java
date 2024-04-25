package io.demo.danggn.repository;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class ProductRepositoryTest {


    @Test
    @DisplayName("나눔하는 상품은 가격이 0원이어야 한다.")
    void registerProductWithNonZeroPrice() {
    }


    
}
