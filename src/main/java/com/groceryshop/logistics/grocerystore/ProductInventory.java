package com.groceryshop.logistics.grocerystore;

public record ProductInventory(
    Integer storageUnitId,
    Integer shopId,
    Integer productId,
    Double availableStock
) {
}
