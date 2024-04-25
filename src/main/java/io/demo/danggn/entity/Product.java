package io.demo.danggn.entity;

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

    protected Product() {
    }

    public Product(String title, String content, TradeMethod method, String description, int price) {
        this.title = title;
        this.content = content;
        this.method = method;
        this.description = description;
        this.price = price;
    }
}
