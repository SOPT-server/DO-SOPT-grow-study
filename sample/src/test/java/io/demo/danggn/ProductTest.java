package io.demo.danggn;

import io.demo.danggn.entity.Product;
import io.demo.danggn.entity.TradeMethod;
import io.demo.danggn.exception.BadRequestException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class ProductTest {

    @Test
    @DisplayName("상품 가격은 최대 1억원으로 제한한다.")
    void productWithInvalidPrice() throws Exception {
        Assertions.assertThatThrownBy(() -> {
        Product product = new Product(
            "상품명",
            TradeMethod.SALE,
            "상품 설명",
            100_000_001,
                true
        );
        }).isInstanceOf(BadRequestException.class)
                .hasMessage("상품 가격은 100,000,000원을 초과할 수 없습니다.");

        }

    @Test
    @DisplayName("상품 가격은 음수로 설정할 수 없다.")
    void productWithZeroPrice() throws Exception {
        Assertions.assertThatThrownBy(() -> {
                    Product product = new Product(
                            "상품명",
                            TradeMethod.SALE,
                            "상품 설명",
                            -1,
                            true
                    );
                }).isInstanceOf(BadRequestException.class)
                .hasMessage("상품 가격은 음수로 설정할 수 없습니다.");

    }

}
