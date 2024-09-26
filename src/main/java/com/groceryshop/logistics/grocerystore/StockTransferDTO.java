package com.groceryshop.logistics.grocerystore;

public record StockTransferDTO(
        Integer fromStorageUnitId,
        Integer toStorageUnitId,
        Double requestedAmount
) {
}
