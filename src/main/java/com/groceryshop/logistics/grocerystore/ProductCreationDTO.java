package com.groceryshop.logistics.grocerystore;

public record ProductCreationDTO(
        Integer shopId,
        String productName,
        Double lowVolumeThreshold,
        QuantityMetricType metricType
) {
}
