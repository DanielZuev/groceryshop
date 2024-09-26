package com.groceryshop.logistics.grocerystore;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/productsinventory")
public class ProductInventoryController {
    private final ProductInventoryService productInventoryService;
    public ProductInventoryController(final ProductInventoryService productInventoryService) {
        this.productInventoryService = productInventoryService;
    }

    @PutMapping("/{shopId}/{storageUnitId}/{productId}")
    public ResponseEntity<Void> addOrUpdateProductInventory(
            @PathVariable final Integer shopId,
            @PathVariable final Integer storageUnitId,
            @PathVariable final Integer productId,
            @RequestBody final ProductInventoryUpdateDTO productInventoryUpdateDTO) {

        // Create a ProductInventory object with the provided data
        ProductInventory productInventory = new ProductInventory(
                storageUnitId,
                shopId,
                productId,
                productInventoryUpdateDTO.availableStock()
        );

        // Call the service method to add or update the product inventory
        productInventoryService.addOrUpdateProductInventory(productInventory);

        // Return HTTP 204 No Content to indicate success
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    public ResponseEntity<List<Product>> searchForProduct(
            @RequestParam final Integer shopId,
            @RequestParam final String productName) {

        List<Product> products = productInventoryService.searchForProduct(shopId, productName);
        return ResponseEntity.ok(products);
    }

    @GetMapping("/shop/{shopId}/product/{productId}/storageunits")
    public ResponseEntity<List<StorageUnit>> getStorageUnitsContainingProduct(
            @PathVariable final Integer shopId,
            @PathVariable final Integer productId) {

        final List<StorageUnit> storageUnits = productInventoryService.storageUnitsContainingProduct(shopId, productId);
        return ResponseEntity.ok(storageUnits);
    }

    @GetMapping("/shop/{shopId}/product/{productId}/available-storageunits")
    public ResponseEntity<List<StorageUnit>> getStorageUnitsNotContainingProduct(
            @PathVariable final Integer shopId,
            @PathVariable final Integer productId) {

        final List<StorageUnit> storageUnits = productInventoryService.storageUnitsNotContainingProduct(shopId, productId);
        return ResponseEntity.ok(storageUnits);
    }

}
