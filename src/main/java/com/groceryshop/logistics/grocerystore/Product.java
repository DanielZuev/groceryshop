package com.groceryshop.logistics.grocerystore;

public record Product(
        Integer productId,
        Integer shopId,
        String productName,
        Double lowVolumeThreshold,
        QuantityMetricType metricType
) {
}
