package com.groceryshop.logistics.grocerystore;

public record ProductsInventory(
    Integer storageUnitId,
    Integer shopId,
    Integer productId,
    Double availableStock
) {
}
