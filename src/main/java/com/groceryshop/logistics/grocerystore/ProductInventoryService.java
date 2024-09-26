package com.groceryshop.logistics.grocerystore;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductInventoryService {

    private final ProductInventoryRepository productInventoryRepository;
    private final ProductRepository productRepository;
    private final ShopStorageUnitRepository shopStorageUnitRepository;

    public ProductInventoryService(

            final ProductInventoryRepository productInventoryRepository,
            final ProductRepository productRepository,
            final ShopStorageUnitRepository shopStorageUnitRepository) {
        this.productInventoryRepository = productInventoryRepository;
        this.productRepository = productRepository;
        this.shopStorageUnitRepository = shopStorageUnitRepository;
    }

    public void addOrUpdateProductInventory(final ProductInventory productInventory) {
        ProductInventory existingProductInventory = productInventoryRepository.findProductInventory(
                productInventory.storageUnitId(),
                productInventory.shopId(),
                productInventory.productId()
        );

        if (existingProductInventory != null) {
            // Product exists; update stock
            // Calculate the new stock based on your business logic
            final double newStock = existingProductInventory.availableStock() + productInventory.availableStock(); // Adjust as needed

            // Create a new ProductInventory object with the updated stock
            final ProductInventory updatedProductInventory = new ProductInventory(
                    productInventory.storageUnitId(),
                    productInventory.shopId(),
                    productInventory.productId(),
                    newStock
            );

            // Update the stock in the repository
            productInventoryRepository.updateStock(updatedProductInventory);
        } else {
            // Product does not exist; create new inventory record
            productInventoryRepository.createProductInventory(productInventory);
        }
    }


    public List<Product> searchForProduct(final Integer shopId, final String productName) {
        List<Product> products = productRepository.productByNameFromShop(shopId, productName);
        return products;
    }

    public List<StorageUnit> storageUnitsContainingProduct(final Integer shopId, final Integer productId) {
        return productInventoryRepository.storageUnitsContainingProduct(shopId, productId);
    }

    public List<StorageUnit> storageUnitsNotContainingProduct(final Integer shopId, final Integer productId) {
        return productInventoryRepository.storageUnitsNotContainingProduct(shopId, productId);
    }

}
