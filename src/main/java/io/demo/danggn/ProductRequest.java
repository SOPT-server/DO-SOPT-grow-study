package io.demo.danggn;

public record ProductRequest(
    String title,
    TradeMethod method,
    boolean isSuggested,
    int price,
    String description) {

}
