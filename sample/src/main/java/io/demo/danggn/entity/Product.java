package io.demo.danggn.entity;

import io.demo.danggn.exception.BadRequestException;
import jakarta.persistence.*;

@Entity
public class Product {

    private static final int INITIAL_PRICE = 0;
    private static final int MAXIMUM_PRICE = 100_000_000;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String content;

    private TradeMethod method;

    @Column(columnDefinition = "TEXT")
    private String description;
    private int price;

    private boolean isSuggested;

    private Long memberId;

    protected Product() {
    }

    public Product(String title,
                   TradeMethod method,
                   String description,
                   int price,
                   boolean isSuggested) {
        validatePrice(price);
        this.title = title;
        this.method = method;
        this.description = description;
        this.price = price;
        this.isSuggested = isSuggested;
    }


    public Long getId() {
        return id;
    }

    public Product(String title, String content, TradeMethod method, String description, int price, boolean isSuggested, Long memberId) {
        validatePrice(price);
        this.title = title;
        this.content = content;
        this.method = method;
        this.description = description;
        this.price = price;
        this.isSuggested = isSuggested;
        this.memberId = memberId;
    }

    private void validatePrice(int price) {
        if (price >= MAXIMUM_PRICE) {
            throw new BadRequestException("상품 가격은 100,000,000원을 초과할 수 없습니다.");
        }

        if (price < INITIAL_PRICE) {
            throw new BadRequestException("상품 가격은 음수로 설정할 수 없습니다.");
        }
    }
}
