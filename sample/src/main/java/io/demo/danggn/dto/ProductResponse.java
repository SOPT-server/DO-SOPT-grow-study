package io.demo.danggn.dto;

public record ProductResponse(
        Long productId
) {

    public static ProductResponse of(final Long productId) {
        return new ProductResponse(productId);
    }
}
