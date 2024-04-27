package io.demo.danggn;

import jakarta.persistence.*;

@Entity
public class Product {

    private static final int INITIAL_PRICE = 0;

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
        this.title = title;
        this.method = method;
        this.description = description;
        this.price = price;
        this.isSuggested = isSuggested;
    }


    public Long getId() {
        return id;
    }
}
