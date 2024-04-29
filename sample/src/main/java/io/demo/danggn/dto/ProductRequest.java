package io.demo.danggn.dto;

import io.demo.danggn.entity.TradeMethod;

public record ProductRequest(
    String title,
    TradeMethod method,
    boolean isSuggested,
    int price,
    String description) {

}
